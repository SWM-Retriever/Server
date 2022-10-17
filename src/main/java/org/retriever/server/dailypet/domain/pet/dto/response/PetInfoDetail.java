package org.retriever.server.dailypet.domain.pet.dto.response;

import lombok.*;
import org.retriever.server.dailypet.domain.pet.entity.Pet;
import org.retriever.server.dailypet.domain.pet.enums.Gender;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PetInfoDetail {

    private Long petId;

    private String petName;

    private String profileImageUrl;

    private LocalDate birthDate;

    private Double weight;

    private String registerNumber;

    private Boolean isNeutered;

    private Gender gender;

    private String petKind;

    // builder는 전체 생성자가 필요하고, 생성자가 없을 경우 내부적으로 전체 생성자(package level)로 만들어서 사용하는데, 생성자를 만든 경우 allargsConstructor를 생성하지 않기 때문에 에러가 난다.
    public PetInfoDetail(Pet pet) {
        this.petId = pet.getPetId();
        this.petName = pet.getPetName();
        this.profileImageUrl = pet.getProfileImageUrl();
        this.birthDate = pet.getBirthDate();
        this.weight = pet.getWeight();
        this.registerNumber = pet.getRegisterNumber();
        this.isNeutered = pet.getIsNeutered();
        this.gender = pet.getGender();
        this.petKind = pet.getPetKind().getPetKindName();
    }

    public static PetInfoDetail from(Pet pet) {
        return PetInfoDetail.builder()
                .petId(pet.getPetId())
                .petName(pet.getPetName())
                .profileImageUrl(pet.getProfileImageUrl())
                .birthDate(pet.getBirthDate())
                .weight(pet.getWeight())
                .registerNumber(pet.getRegisterNumber())
                .isNeutered(pet.getIsNeutered())
                .gender(pet.getGender())
                .petKind(pet.getPetKind().getPetKindName())
                .build();
    }
}
