package org.retriever.server.dailypet.domain.petcare.entity;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.retriever.server.dailypet.domain.model.BaseTimeEntity;
import org.retriever.server.dailypet.domain.model.IsDeleted;
import org.retriever.server.dailypet.domain.petcare.enums.CustomDayOfWeek;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Where(clause = "is_deleted = 'FALSE'")
@SQLDelete(sql = "UPDATE pet_care_alarm SET is_deleted = 'TRUE' WHERE pet_care_alarm_id = ?")
public class PetCareAlarm extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long petCareAlarmId;

    @Enumerated(EnumType.STRING)
    private CustomDayOfWeek dayOfWeek;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "petCareId", nullable = false)
    private PetCare petCare;

    @Enumerated(EnumType.STRING)
    private IsDeleted isDeleted;

    public static PetCareAlarm from(CustomDayOfWeek dayOfWeek) {
        return PetCareAlarm.builder()
                .dayOfWeek(dayOfWeek)
                .isDeleted(IsDeleted.FALSE)
                .build();
    }

    public void setPetCare(PetCare petCare) {
        this.petCare = petCare;
    }
}
