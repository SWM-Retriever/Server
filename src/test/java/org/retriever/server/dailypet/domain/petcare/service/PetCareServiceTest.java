package org.retriever.server.dailypet.domain.petcare.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.retriever.server.dailypet.domain.common.factory.CareLogFactory;
import org.retriever.server.dailypet.domain.common.factory.MemberFactory;
import org.retriever.server.dailypet.domain.common.factory.PetCareFactory;
import org.retriever.server.dailypet.domain.common.factory.PetFactory;
import org.retriever.server.dailypet.domain.member.entity.Member;
import org.retriever.server.dailypet.domain.pet.entity.Pet;
import org.retriever.server.dailypet.domain.pet.repository.PetRepository;
import org.retriever.server.dailypet.domain.petcare.dto.request.CreatePetCareRequest;
import org.retriever.server.dailypet.domain.petcare.dto.response.CancelPetCareResponse;
import org.retriever.server.dailypet.domain.petcare.dto.response.CheckPetCareResponse;
import org.retriever.server.dailypet.domain.petcare.entity.CareLog;
import org.retriever.server.dailypet.domain.petcare.entity.PetCare;
import org.retriever.server.dailypet.domain.petcare.enums.CareLogStatus;
import org.retriever.server.dailypet.domain.petcare.exception.CareCountExceededException;
import org.retriever.server.dailypet.domain.petcare.exception.CareCountIsZeroException;
import org.retriever.server.dailypet.domain.petcare.exception.NotCancelCareLogException;
import org.retriever.server.dailypet.domain.petcare.repository.CareLogQueryRepository;
import org.retriever.server.dailypet.domain.petcare.repository.CareLogRepository;
import org.retriever.server.dailypet.domain.petcare.repository.PetCareRepository;
import org.retriever.server.dailypet.global.utils.security.SecurityUtil;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class PetCareServiceTest {

    @Mock
    PetRepository petRepository;
    @Mock
    PetCareRepository petCareRepository;
    @Mock
    CareLogRepository careLogRepository;
    @Mock
    CareLogQueryRepository careLogQueryRepository;
    @Mock
    SecurityUtil securityUtil;
    @InjectMocks
    PetCareService petCareService;

    @Test
    @DisplayName("챙겨주기 등록 요청을 받아 생성한다")
    void register_care_success() {

        // given
        Pet testPet = PetFactory.createTestPet();
        CreatePetCareRequest petCareRequest = PetCareFactory.createPetCareRequest();
        given(petRepository.findById(any())).willReturn(Optional.of(testPet));

        // when
        petCareService.registerPetCare(testPet.getPetId(), petCareRequest);

        // then
        Assertions.assertAll(
                () -> verify(petCareRepository, times(1)).save(any())
        );
    }

    @Test
    @DisplayName("챙겨주기 1회 등록 성공")
    void check_care_success() {

        // given
        Member testMember = MemberFactory.createTestMember();
        Pet testPet = PetFactory.createTestPet();
        PetCare testPetCare = PetCareFactory.createTestPetCare();
        int beforeCount = 3;
        given(securityUtil.getMemberByUserDetails()).willReturn(testMember);
        given(petRepository.findById(any())).willReturn(Optional.of(testPet));
        given(petCareRepository.findById(any())).willReturn(Optional.of(testPetCare));
        given(careLogQueryRepository.findTodayCountByCareId(any())).willReturn(beforeCount);

        // when
        CheckPetCareResponse checkPetCareResponse = petCareService.checkPetCare(testPet.getPetId(), testPetCare.getPetCareId());

        // then
        Assertions.assertAll(
                () -> verify(careLogRepository, times(1)).save(any()),
                () -> verify(careLogQueryRepository, times(1)).findTodayCountByCareId(any()),
                () -> assertEquals(checkPetCareResponse.getCurrentCount(), beforeCount+1),
                () -> assertEquals(checkPetCareResponse.getMemberNameWhoChecked(), testMember.getFamilyRoleName()),
                () -> assertEquals(checkPetCareResponse.getPetCareId(), testPetCare.getPetCareId())
        );
    }

    @Test
    @DisplayName("챙겨주기 1회 등록 실패 - 일일 할당량 초과")
    void check_care_fail() {

        // given
        Member testMember = MemberFactory.createTestMember();
        Pet testPet = PetFactory.createTestPet();
        PetCare testPetCare = PetCareFactory.createTestPetCare();
        int beforeCount = testPetCare.getTotalCountPerDay();
        given(securityUtil.getMemberByUserDetails()).willReturn(testMember);
        given(petRepository.findById(any())).willReturn(Optional.of(testPet));
        given(petCareRepository.findById(any())).willReturn(Optional.of(testPetCare));
        given(careLogQueryRepository.findTodayCountByCareId(any())).willReturn(beforeCount);

        // when, then
        assertThrows(CareCountExceededException.class, () -> petCareService.checkPetCare(testPet.getPetId(), testPetCare.getPetCareId()));
    }

    @Test
    @DisplayName("챙겨주기 1회 취소 성공")
    void cancel_care_success() {

        // given
        Member testMember = MemberFactory.createTestMember();
        PetCare testPetCare = PetCareFactory.createTestPetCare();
        CareLog testCareLog = CareLogFactory.createTestCareLog();
        int beforeCount = 3;
        given(securityUtil.getMemberByUserDetails()).willReturn(testMember);
        given(petCareRepository.findById(any())).willReturn(Optional.of(testPetCare));
        given(careLogQueryRepository.findByMemberIdAndCareIdWithCurDateLatestLimit1(any(), any())).willReturn(testCareLog);
        given(careLogQueryRepository.findTodayCountByCareId(any())).willReturn(beforeCount);

        // when
        CancelPetCareResponse cancelPetCareResponse = petCareService.cancelPetCare(testPetCare.getPetCareId());

        // then
        Assertions.assertAll(
                () -> verify(careLogQueryRepository, times(1)).findByMemberIdAndCareIdWithCurDateLatestLimit1(any(), any()),
                () -> verify(careLogQueryRepository, times(1)).findTodayCountByCareId(any()),
                () -> assertEquals(cancelPetCareResponse.getCurrentCount(), beforeCount - 1),
                () -> assertEquals(testCareLog.getCareLogStatus(), CareLogStatus.CANCEL)
        );
    }

    @Test
    @DisplayName("챙겨주기 1회 취소 실패 - 현재 횟수가 0인 경우")
    void cancel_care_fail_current_count_is_zero() {

        // given
        Member testMember = MemberFactory.createTestMember();
        PetCare testPetCare = PetCareFactory.createTestPetCare();
        CareLog testCareLog = CareLogFactory.createTestCareLog();
        int beforeCount = 0;
        given(securityUtil.getMemberByUserDetails()).willReturn(testMember);
        given(petCareRepository.findById(any())).willReturn(Optional.of(testPetCare));
        given(careLogQueryRepository.findByMemberIdAndCareIdWithCurDateLatestLimit1(any(), any())).willReturn(testCareLog);
        given(careLogQueryRepository.findTodayCountByCareId(any())).willReturn(beforeCount);

        // when, then
        assertThrows(CareCountIsZeroException.class, () -> petCareService.cancelPetCare(testPetCare.getPetCareId()));
    }

    @Test
    @DisplayName("챙겨주기 1회 취소 실패 - 내가 한 행동만 취소가 가능 (오늘 한 일이 없는 경우)")
    void cancel_care_fail_my_count_is_zero() {

        // given
        Member testMember = MemberFactory.createTestMember();
        PetCare testPetCare = PetCareFactory.createTestPetCare();
        given(securityUtil.getMemberByUserDetails()).willReturn(testMember);
        given(petCareRepository.findById(any())).willReturn(Optional.of(testPetCare));
        given(careLogQueryRepository.findByMemberIdAndCareIdWithCurDateLatestLimit1(any(), any())).willReturn(null);

        // when, then
        assertThrows(NotCancelCareLogException.class, () -> petCareService.cancelPetCare(testPetCare.getPetCareId()));
    }
}
