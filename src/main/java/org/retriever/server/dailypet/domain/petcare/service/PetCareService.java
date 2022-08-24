package org.retriever.server.dailypet.domain.petcare.service;

import lombok.RequiredArgsConstructor;
import org.retriever.server.dailypet.domain.member.entity.Member;
import org.retriever.server.dailypet.domain.member.exception.MemberNotFoundException;
import org.retriever.server.dailypet.domain.member.repository.MemberRepository;
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
import org.retriever.server.dailypet.global.config.security.CustomUserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PetCareService {

    private final PetRepository petRepository;
    private final PetCareRepository petCareRepository;
    private final CareLogRepository careLogRepository;
    private final MemberRepository memberRepository;
    private final CareLogQueryRepository careLogQueryRepository;

    @Transactional
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

    // TODO 1회 체크 동시성 이슈 해결 필요 (가족들 공동 접근)
    @Transactional
    public CheckPetCareResponse checkPetCare(CustomUserDetails userDetails, Long petId, Long petCareId) {
        Member member = memberRepository.findById(userDetails.getId())
                .orElseThrow(MemberNotFoundException::new);

        Pet pet = petRepository.findById(petId)
                .orElseThrow(PetNotFoundException::new);

        PetCare petCare = petCareRepository.findById(petCareId).orElseThrow(PetCareNotFoundException::new);
        petCare.pushCareCheckButton();

        CareLog careLog = CareLog.of(member, pet, petCare, CareLogStatus.CHECK);

        careLogRepository.save(careLog);

        return new CheckPetCareResponse(petCareId, petCare.getCurrentCount(), member.getFamilyRoleName());
    }

    /**
     * 내가 한 항목만 취소할 수 있도록, CareLog에서 요청한 멤버와 항목으로 검색한다.
     * CareLog에서 내가 한 행동의 가장 최근을 조회하고, 없으면 취소 불가
     * 있으면 해당 CareLog의 status를 cancel로 바꾼다.
     */
    @Transactional
    public CancelPetCareResponse cancelPetCare(CustomUserDetails userDetails, Long petId, Long petCareId) {
        Member member = memberRepository.findById(userDetails.getId())
                .orElseThrow(MemberNotFoundException::new);

        PetCare petCare = petCareRepository.findById(petCareId).orElseThrow(PetCareNotFoundException::new);
        CareLog careLog = careLogQueryRepository.findByMemberIdAndCareIdWithCurDateLatestLimit1(member.getId(), petCareId);

        if (careLog == null) {
            throw new NotCancelCareLogException();
        }
        petCare.cancelCareCheckButton();
        careLog.cancel();

        return new CancelPetCareResponse(petCareId, petCare.getCurrentCount());
    }
}
