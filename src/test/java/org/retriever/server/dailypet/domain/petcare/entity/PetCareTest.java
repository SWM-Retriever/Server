package org.retriever.server.dailypet.domain.petcare.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.retriever.server.dailypet.domain.common.factory.PetCareFactory;
import org.retriever.server.dailypet.domain.petcare.dto.request.CreatePetCareRequest;
import org.retriever.server.dailypet.domain.petcare.enums.PetCareStatus;
import org.retriever.server.dailypet.domain.petcare.exception.CareCountExceededException;
import org.retriever.server.dailypet.domain.petcare.exception.CareCountIsZeroException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PetCareTest {

    @Test
    @DisplayName("챙겨주기 등록 요청을 받아 생성한다")
    void create_care() {

        //given
        CreatePetCareRequest petCareRequest = PetCareFactory.createPetCareRequest();

        // when
        PetCare petCare = PetCare.from(petCareRequest);

        // then
        assertThat(petCare.getCareName()).isEqualTo(petCareRequest.getCareName());
        assertThat(petCare.getTotalCountPerDay()).isEqualTo(petCareRequest.getTotalCountPerDay());
        assertThat(petCare.getIsPushAgree()).isEqualTo(Boolean.FALSE);
        assertThat(petCare.getPetCareStatus()).isEqualTo(PetCareStatus.ACTIVE);
    }

    @Test
    @DisplayName("챙겨주기 1회 체크 시 1회 증가한다")
    void check_care_success() {
        // given
        PetCare testPetCare = PetCareFactory.createTestPetCare();
        int beforeCount = 3;

        // when
        int afterCount = testPetCare.pushCareCheckButton(beforeCount);

        // then
        assertThat(beforeCount + 1).isEqualTo(afterCount);
    }

    @Test
    @DisplayName("챙겨주기 1회 취소 시 1회 감소한다.")
    void cancel_care_success() {
        // given
        PetCare testPetCare = PetCareFactory.createTestPetCare();
        int beforeCount = 3;

        // when
        int afterCount = testPetCare.cancelCareCheckButton(beforeCount);

        // then
        assertThat(beforeCount - 1).isEqualTo(afterCount);
    }

    @Test
    @DisplayName("챙겨주기 1회 체크 실패 - 일일 할당량 초과")
    void check_care_fail() {
        // given
        PetCare testPetCare = PetCareFactory.createTestPetCare();
        int beforeCount = testPetCare.getTotalCountPerDay();

        // when, then
        assertThrows(CareCountExceededException.class, () -> testPetCare.pushCareCheckButton(beforeCount));
    }

    @Test
    @DisplayName("챙겨주기 1회 취소 실패 - 0회")
    void cancel_care_fail() {
        // given
        PetCare testPetCare = PetCareFactory.createTestPetCare();
        int beforeCount = 0;

        // when, then
        assertThrows(CareCountIsZeroException.class, () -> testPetCare.cancelCareCheckButton(beforeCount));
    }
}
