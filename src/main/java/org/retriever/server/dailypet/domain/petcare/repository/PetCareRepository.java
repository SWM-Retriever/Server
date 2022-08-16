package org.retriever.server.dailypet.domain.petcare.repository;

import org.retriever.server.dailypet.domain.petcare.entity.PetCare;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetCareRepository extends JpaRepository<PetCare, Long> {
}
