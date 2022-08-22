package org.retriever.server.dailypet.domain.pet.entity;

import lombok.*;
import org.retriever.server.dailypet.domain.family.entity.Family;
import org.retriever.server.dailypet.domain.model.BaseTimeEntity;
import org.retriever.server.dailypet.domain.pet.dto.request.RegisterPetRequest;
import org.retriever.server.dailypet.domain.pet.enums.Gender;
import org.retriever.server.dailypet.domain.pet.enums.PetStatus;
import org.retriever.server.dailypet.domain.petcare.entity.CareLog;
import org.retriever.server.dailypet.domain.petcare.entity.PetCare;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Pet extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long petId;

    @Column(nullable = false, length = 50)
    private String petName;

    private String profileImageUrl;

    private LocalDate birthDate;

    private Double weight;

    @Column(unique = true, length = 50)
    private String registerNumber;

    @Column(nullable = false)
    private Boolean isNeutered;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private PetStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "petKindId", nullable = false)
    private PetKind petKind;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "familyId", nullable = false)
    private Family family;

    @OneToMany(mappedBy = "pet")
    @Builder.Default
    private List<PetCare> petCareList = new ArrayList<>();

    @OneToMany(mappedBy = "pet")
    @Builder.Default
    private List<CareLog> careLogList = new ArrayList<>();

    public static Pet createPet(RegisterPetRequest dto) {
        return Pet.builder()
                .petName(dto.getPetName())
                .profileImageUrl(dto.getProfileImageUrl())
                .birthDate(dto.getBirthDate())
                .weight(dto.getWeight())
                .registerNumber(dto.getRegisterNumber())
                .isNeutered(dto.getIsNeutered())
                .gender(dto.getGender())
                .status(PetStatus.ACTIVE)
                .build();
    }

    public void setPetKind(PetKind petKind) {
        this.petKind = petKind;
    }

    public void setFamily(Family family) {
        this.family = family;
    }

    public void registerPetCare(PetCare petCare) {
        this.petCareList.add(petCare);
        petCare.addPet(this);
    }
}
