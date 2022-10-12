package org.retriever.server.dailypet.domain.petcare.service;

import lombok.RequiredArgsConstructor;
import org.retriever.server.dailypet.domain.member.entity.Member;
import org.retriever.server.dailypet.domain.pet.dto.response.CareLogHistory;
import org.retriever.server.dailypet.domain.pet.dto.response.CurrentPetCareInfo;
import org.retriever.server.dailypet.domain.pet.dto.response.GetPetCaresResponse;
import org.retriever.server.dailypet.domain.pet.entity.Pet;
import org.retriever.server.dailypet.domain.pet.exception.PetNotFoundException;
import org.retriever.server.dailypet.domain.pet.repository.PetRepository;
import org.retriever.server.dailypet.domain.petcare.dto.request.CreatePetCareRequest;
import org.retriever.server.dailypet.domain.petcare.dto.response.CancelPetCareResponse;
import org.retriever.server.dailypet.domain.petcare.dto.response.CheckPetCareResponse;
import org.retriever.server.dailypet.domain.petcare.entity.CareLog;
import org.retriever.server.dailypet.domain.petcare.entity.PetCare;
import org.retriever.server.dailypet.domain.petcare.entity.PetCareAlarm;
import org.retriever.server.dailypet.domain.petcare.enums.CareLogStatus;
import org.retriever.server.dailypet.domain.petcare.enums.CustomDayOfWeek;
import org.retriever.server.dailypet.domain.petcare.exception.NotCancelCareLogException;
import org.retriever.server.dailypet.domain.petcare.exception.PetCareNotFoundException;
import org.retriever.server.dailypet.domain.petcare.repository.CareLogQueryRepository;
import org.retriever.server.dailypet.domain.petcare.repository.CareLogRepository;
import org.retriever.server.dailypet.domain.petcare.repository.PetCareQueryRepository;
import org.retriever.server.dailypet.domain.petcare.repository.PetCareRepository;
import org.retriever.server.dailypet.global.utils.security.SecurityUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PetCareService {

    private final PetRepository petRepository;
    private final PetCareRepository petCareRepository;
    private final CareLogRepository careLogRepository;
    private final CareLogQueryRepository careLogQueryRepository;
    private final PetCareQueryRepository petCareQueryRepository;
    private final SecurityUtil securityUtil;

    // TODO : N+1 문제 해결하기 -> petId를 통해서 petCare를 바로 얻어오고, 컬렉션 fetch join을 활용해서 carelog를 처리해보기
    public GetPetCaresResponse getPetCaresDetail(Long petId) {
        List<CurrentPetCareInfo> responseList = new ArrayList<>();

        List<PetCare> petCareWithCareLogList = petCareQueryRepository.findByPetIdFetchJoinCareAlarm(petId);
        for (PetCare petCare : petCareWithCareLogList) {
            List<CustomDayOfWeek> dayOfWeeks = petCare.getPetCareAlarmList()
                    .stream()
                    .map(PetCareAlarm::getDayOfWeek)
                    .collect(Collectors.toList());

            List<CareLogHistory> careLogHistoryList = getCareLogHistory(petCare.getPetCareId());

            CurrentPetCareInfo petCaresDetailResponse =
                    CurrentPetCareInfo.of(petCare, careLogHistoryList, dayOfWeeks);
            responseList.add(petCaresDetailResponse);
        }
        return GetPetCaresResponse.of(petId, responseList);
    }

    public void registerPetCare(Long petId, CreatePetCareRequest dto) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(PetNotFoundException::new);

        PetCare petCare = PetCare.from(dto);

        List<CustomDayOfWeek> dayOfWeeks = dto.getDayOfWeeks();

        for (CustomDayOfWeek dayOfWeek : dayOfWeeks) {
            PetCareAlarm petCareAlarm = PetCareAlarm.from(dayOfWeek);
            petCare.addPetCareAlarm(petCareAlarm);
        }

        pet.registerPetCare(petCare);

        // CascadeType.PERSIST를 통해 연관된 petCareAlarm 포함해서 저장
        petCareRepository.save(petCare);
    }

    public void deletePetCare(Long careId) {
        // cascadeType.All careAlarm 같이 삭제
        petCareRepository.deleteById(careId);
    }

    // TODO 1회 체크 동시성 이슈 해결 필요 (가족들 공동 접근)
    public CheckPetCareResponse checkPetCare(Long petId, Long petCareId) {
        Member member = securityUtil.getMemberByUserDetails();

        Pet pet = petRepository.findById(petId).orElseThrow(PetNotFoundException::new);

        PetCare petCare = petCareRepository.findById(petCareId).orElseThrow(PetCareNotFoundException::new);
        int currentCount = careLogQueryRepository.findTodayCountByCareId(petCareId);
        int afterCount = petCare.pushCareCheckButton(currentCount);

        careLogRepository.save(CareLog.of(member, pet, petCare, CareLogStatus.CHECK));

        return CheckPetCareResponse.of(petCareId, afterCount, getCareLogHistory(petCareId));
    }

    /**
     * 내가 한 항목만 취소할 수 있도록, CareLog에서 요청한 멤버와 항목으로 검색한다.
     * CareLog에서 내가 한 행동의 가장 최근을 조회하고, 없으면 취소 불가
     * 있으면 해당 CareLog의 status를 cancel로 바꾼다.
     */
    public CancelPetCareResponse cancelPetCare(Long petCareId) {
        Member member = securityUtil.getMemberByUserDetails();

        PetCare petCare = petCareRepository.findById(petCareId).orElseThrow(PetCareNotFoundException::new);
        CareLog careLog = careLogQueryRepository.findByMemberIdAndCareIdWithCurDateLatestLimit1(member.getId(), petCareId);

        if (careLog == null) {
            throw new NotCancelCareLogException();
        }
        int currentCount = careLogQueryRepository.findTodayCountByCareId(petCareId);
        int afterCount = petCare.cancelCareCheckButton(currentCount);
        careLog.cancel();

        return new CancelPetCareResponse(petCareId, afterCount, getCareLogHistory(petCareId));
    }

    private List<CareLogHistory> getCareLogHistory(Long petCareId) {
            List<CareLog> careLogListWithCurDate = careLogQueryRepository.findByPetCareIdWithCurDateOrderByCreatedAt(petCareId);

            return careLogListWithCurDate.stream()
                    .map(CareLogHistory::new)
                    .collect(Collectors.toList());
    }
}
