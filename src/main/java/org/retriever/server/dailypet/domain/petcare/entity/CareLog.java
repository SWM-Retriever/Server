package org.retriever.server.dailypet.domain.petcare.entity;

import lombok.*;
import org.retriever.server.dailypet.domain.member.entity.Member;
import org.retriever.server.dailypet.domain.model.BaseTimeEntity;
import org.retriever.server.dailypet.domain.pet.entity.Pet;
import org.retriever.server.dailypet.domain.petcare.enums.CareLogStatus;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CareLog extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long careLogId;

    @Enumerated(EnumType.STRING)
    private CareLogStatus careLogStatus;

    private LocalDate logDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "petCareId", nullable = false)
    private PetCare petCare;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "petId", nullable = false)
    private Pet pet;

    public static CareLog of(Member member, Pet pet, PetCare petCare, CareLogStatus careLogStatus) {
        CareLog careLog = CareLog.builder()
                .member(member)
                .pet(pet)
                .petCare(petCare)
                .careLogStatus(careLogStatus)
                .logDate(LocalDate.now())
                .build();
        member.getCareLogList().add(careLog);

        return careLog;
    }

    public void cancel() {
        this.careLogStatus = CareLogStatus.CANCEL;
    }
}
