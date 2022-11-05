package org.retriever.server.dailypet.domain.petcare.entity;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.retriever.server.dailypet.domain.member.entity.Member;
import org.retriever.server.dailypet.domain.model.BaseTimeEntity;
import org.retriever.server.dailypet.domain.model.IsDeleted;
import org.retriever.server.dailypet.domain.pet.entity.Pet;
import org.retriever.server.dailypet.domain.petcare.enums.CareLogStatus;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "is_deleted = 'FALSE' and care_log_status = 'CHECK'")
@SQLDelete(sql = "UPDATE care_log SET is_deleted = 'TRUE' WHERE care_log_id = ?")
public class CareLog extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long careLogId;

    @Enumerated(EnumType.STRING)
    private CareLogStatus careLogStatus;

    @Enumerated(EnumType.STRING)
    private IsDeleted isDeleted;

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
                .isDeleted(IsDeleted.FALSE)
                .build();
        member.getCareLogList().add(careLog);

        return careLog;
    }

    public void cancel() {
        this.careLogStatus = CareLogStatus.CANCEL;
    }
}
