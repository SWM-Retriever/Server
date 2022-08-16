package org.retriever.server.dailypet.domain.petcare.entity;

import lombok.*;
import org.retriever.server.dailypet.domain.model.BaseTimeEntity;
import org.retriever.server.dailypet.domain.pet.entity.Pet;
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

    private int alarmSetting;

    private Boolean isPushAgree;

    @Enumerated(EnumType.STRING)
    private PetCareStatus petCareStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "petId", nullable = false)
    private Pet pet;

    @OneToMany(mappedBy = "petCare")
    @Builder.Default
    private List<PetCareAlarm> petCareAlarmList = new ArrayList<>();

    @OneToMany(mappedBy = "petCare")
    @Builder.Default
    private List<CareLog> careLogList = new ArrayList<>();
}
