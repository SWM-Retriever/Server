package org.retriever.server.dailypet.domain.pet.repository;

import org.retriever.server.dailypet.domain.pet.entity.PetKind;
import org.retriever.server.dailypet.domain.pet.enums.PetType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PetKindRepository extends JpaRepository<PetKind, Long> {

    Optional<PetKind> findByPetKindName(String petKindName);

    Optional<PetKind> findByPetTypeOrderByPetKindName(PetType petType); // 이름 순으로 품종 정보 조회
}