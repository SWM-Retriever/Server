package org.retriever.server.dailypet.domain.pet.entity;

import lombok.*;
import org.retriever.server.dailypet.domain.model.BaseTimeEntity;
import org.retriever.server.dailypet.domain.pet.enums.PetType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PetKind extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long petKindId;

    @Column(unique = true, nullable = false, length = 50)
    private String petKindName; // 품종 이름

    @Lob
    @Column(nullable = false)
    private String information;

    @Enumerated(EnumType.STRING)
    private PetType petType; // 강아지 or 고양이

    @OneToMany(mappedBy = "petKind")
    @Builder.Default
    private List<Pet> petList = new ArrayList<>();
}
