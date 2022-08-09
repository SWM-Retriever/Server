package org.retriever.server.dailypet.domain.pet.entity;

import lombok.*;
import org.retriever.server.dailypet.domain.family.entity.Family;
import org.retriever.server.dailypet.domain.model.BaseTimeEntity;
import org.retriever.server.dailypet.domain.pet.enums.Gender;
import org.retriever.server.dailypet.domain.pet.enums.PetStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    private LocalDateTime birthDate;

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
    private PetKind petkind;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "familyId", nullable = false)
    private Family family;
}
