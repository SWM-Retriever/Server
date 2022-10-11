package org.retriever.server.dailypet.domain.pet.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.retriever.server.dailypet.domain.common.factory.PetFactory;
import org.retriever.server.dailypet.domain.pet.dto.request.RegisterPetRequest;
import org.retriever.server.dailypet.domain.pet.enums.PetStatus;

import static org.assertj.core.api.Assertions.assertThat;

class PetTest {

    @DisplayName("등록 요청을 받아서 pet을 생성한다")
    @Test
    void create_new_pet() {

        // given
        RegisterPetRequest request = PetFactory.createRegisterPetRequest();
        String imageUrl = "testUrl";
        // when
        Pet pet = Pet.createPet(request);

        // then
        assertThat(pet.getPetName()).isEqualTo(request.getPetName());
        assertThat(pet.getBirthDate()).isEqualTo(request.getBirthDate());
        assertThat(pet.getGender()).isEqualTo(request.getGender());
        assertThat(pet.getRegisterNumber()).isEqualTo(request.getRegisterNumber());
        assertThat(pet.getProfileImageUrl()).isEqualTo(request.getProfileImageUrl());
        assertThat(pet.getWeight()).isEqualTo(request.getWeight());
        assertThat(pet.getIsNeutered()).isEqualTo(request.getIsNeutered());
        assertThat(pet.getPetStatus()).isEqualTo(PetStatus.ACTIVE);
    }
}
