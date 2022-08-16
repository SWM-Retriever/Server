package org.retriever.server.dailypet.domain.petcare.entity;

import lombok.*;
import org.retriever.server.dailypet.domain.model.BaseTimeEntity;
import org.retriever.server.dailypet.domain.petcare.enums.CustomDayOfWeek;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class PetCareAlarm extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long petCareAlarmId;

    @Enumerated(EnumType.STRING)
    private CustomDayOfWeek dayOfWeek;

    private int repeatCnt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "petCareId", nullable = false)
    private PetCare petCare;
}