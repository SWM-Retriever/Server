package org.retriever.server.dailypet.domain.pet.repository;

import org.retriever.server.dailypet.domain.pet.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {
}
