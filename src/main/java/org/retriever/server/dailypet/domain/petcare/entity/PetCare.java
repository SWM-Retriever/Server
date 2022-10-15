package org.retriever.server.dailypet.domain.petcare.entity;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.retriever.server.dailypet.domain.model.BaseTimeEntity;
import org.retriever.server.dailypet.domain.model.IsDeleted;
import org.retriever.server.dailypet.domain.pet.entity.Pet;
import org.retriever.server.dailypet.domain.petcare.dto.request.CreatePetCareRequest;
import org.retriever.server.dailypet.domain.petcare.exception.CareCountExceededException;
import org.retriever.server.dailypet.domain.petcare.exception.CareCountIsZeroException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Where(clause = "is_deleted = 'FALSE'")
@SQLDelete(sql = "UPDATE pet_care SET is_deleted = 'TRUE' WHERE pet_care_id = ?")
public class PetCare extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long petCareId;

    private String careName;

    private int totalCountPerDay;

    private Boolean isPushAgree;

    @Enumerated(EnumType.STRING)
    private IsDeleted isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "petId", nullable = false)
    private Pet pet;

    @OneToMany(mappedBy = "petCare", cascade = CascadeType.ALL)
    @Builder.Default
    private List<PetCareAlarm> petCareAlarmList = new ArrayList<>();

    @OneToMany(mappedBy = "petCare")
    @Builder.Default
    private List<CareLog> careLogList = new ArrayList<>();

    public static PetCare from(CreatePetCareRequest dto) {
        return PetCare.builder()
                .careName(dto.getCareName())
                .totalCountPerDay(dto.getTotalCountPerDay())
                .isPushAgree(false)
                .isDeleted(IsDeleted.FALSE)
                .build();
    }

    public void addPet(Pet pet) {
        this.pet = pet;
    }

    public void addPetCareAlarm(PetCareAlarm petCareAlarm) {
        this.petCareAlarmList.add(petCareAlarm);
        petCareAlarm.setPetCare(this);
    }

    public int pushCareCheckButton(int currentCount) {
        int after = currentCount + 1;
        if (after > totalCountPerDay) {
            throw new CareCountExceededException();
        }
        return after;
    }

    public int cancelCareCheckButton(int currentCount) {
        int after = currentCount - 1;
        if (after < 0) {
            throw new CareCountIsZeroException();
        }
        return after;
    }

    public void updateTotalCount(int totalCountPerDay) {
        this.totalCountPerDay = totalCountPerDay;
    }
}
