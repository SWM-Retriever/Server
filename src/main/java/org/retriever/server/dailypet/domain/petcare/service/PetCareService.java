package org.retriever.server.dailypet.domain.petcare.service;

import lombok.RequiredArgsConstructor;
import org.retriever.server.dailypet.domain.member.entity.Member;
import org.retriever.server.dailypet.domain.pet.dto.response.CareLogHistory;
import org.retriever.server.dailypet.domain.pet.dto.response.GetPetCaresDetailResponse;
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
    private final SecurityUtil securityUtil;

    /**
     *  // response : 이름, 횟수, 현재횟수, list
     * // 케어 리스트 조회, 각 케어 리스트마다 챙겨주기 이름, 최대 횟수, 현재 횟수, 기록(이름 리스트)
     * // 기록은 CareLogRepository에서 각 petCareId로 조회하되, 오늘 날짜만 뽑아오면 될듯. 그러면 petCare랑 careLog랑 fetchJoin해야할듯
     * // 현재 러프하게 짜면 하나의 펫에 n개의 petcareList가 나오고, petCareList에서 이름 최대횟수 현재횟수를 꺼내면서 N+1문제가 발생
     * // 이후 carelogRepository에서 petCareList만큼 꺼내면 역시 n번의 쿼리가 나올 것
     * // 최종 1+N+N 쿼리가 나옴 (펫 한개 + 케어리스트 N개에서 필드 조회(N번) + 각 케어리스트마다 케어로그 조회(N번))
     */
    // TODO : N+1 문제 해결하기
    public List<GetPetCaresDetailResponse> getPetCaresDetail(Long petId) {
        Pet pet = petRepository.findById(petId).orElseThrow(PetNotFoundException::new);

        List<GetPetCaresDetailResponse> responseList = new ArrayList<>();

        List<PetCare> petCareList = pet.getPetCareList();
        for (PetCare petCare : petCareList) {
            Long petCareId = petCare.getPetCareId();
            List<CareLog> careLogListWithCurDate = careLogQueryRepository.findByPetCareIdWithCurDateOrderByCreatedAt(petCareId);
            List<CareLogHistory> careLogHistoryList = careLogListWithCurDate.stream()
                    .map(CareLogHistory::new)
                    .collect(Collectors.toList());

            GetPetCaresDetailResponse petCaresDetailResponse =
                    GetPetCaresDetailResponse.of(petCare, careLogHistoryList);
            responseList.add(petCaresDetailResponse);
        }
        return responseList;
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

        return new CheckPetCareResponse(petCareId, afterCount, member.getFamilyRoleName());
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

        return new CancelPetCareResponse(petCareId, afterCount);
    }
}
