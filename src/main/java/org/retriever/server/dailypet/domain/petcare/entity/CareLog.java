package org.retriever.server.dailypet.domain.petcare.entity;

import lombok.*;
import org.retriever.server.dailypet.domain.member.entity.Member;
import org.retriever.server.dailypet.domain.model.BaseTimeEntity;
import org.retriever.server.dailypet.domain.pet.entity.Pet;

import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CareLog extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long careLogId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "petCareId", nullable = false)
    private PetCare petCare;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "petId", nullable = false)
    private Pet pet;

    public static CareLog of(Member member, Pet pet, PetCare petCare) {
        CareLog careLog = CareLog.builder()
                .member(member)
                .pet(pet)
                .petCare(petCare)
                .build();
        member.getCareLogList().add(careLog);

        return careLog;
    }
}
