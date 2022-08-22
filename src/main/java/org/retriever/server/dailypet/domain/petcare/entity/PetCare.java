package org.retriever.server.dailypet.domain.petcare.entity;

import lombok.*;
import org.retriever.server.dailypet.domain.model.BaseTimeEntity;
import org.retriever.server.dailypet.domain.pet.entity.Pet;
import org.retriever.server.dailypet.domain.petcare.dto.request.CreatePetCareRequest;
import org.retriever.server.dailypet.domain.petcare.enums.PetCareStatus;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class PetCare extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long petCareId;

    private String careName;

    private int curCnt;

    private int repeatCnt;

    private Boolean isPushAgree;

    @Enumerated(EnumType.STRING)
    private PetCareStatus petCareStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "petId", nullable = false)
    private Pet pet;

    @OneToMany(mappedBy = "petCare", cascade = CascadeType.PERSIST)
    @Builder.Default
    private List<PetCareAlarm> petCareAlarmList = new ArrayList<>();

    @OneToMany(mappedBy = "petCare")
    @Builder.Default
    private List<CareLog> careLogList = new ArrayList<>();

    public static PetCare from(CreatePetCareRequest dto) {
        return PetCare.builder()
                .careName(dto.getCareName())
                .repeatCnt(dto.getRepeatCnt())
                .isPushAgree(false)
                .petCareStatus(PetCareStatus.ACTIVE)
                .build();
    }

    public void addPet(Pet pet) {
        this.pet = pet;
    }

    public void addPetCareAlarm(PetCareAlarm petCareAlarm) {
        this.petCareAlarmList.add(petCareAlarm);
        petCareAlarm.setPetCare(this);
    }
}
