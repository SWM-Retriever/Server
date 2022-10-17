package org.retriever.server.dailypet.domain.pet.repository;

import lombok.RequiredArgsConstructor;
import org.retriever.server.dailypet.domain.pet.entity.Pet;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PetQueryRepository {

    private final EntityManager entityManager;

    public List<Pet> findPetsByFamilyId(Long familyId) {
        return entityManager.createQuery(
                        "select p from Pet p" +
                                " join fetch p.petKind pk" +
                                " where p.family.familyId = :familyId"
                        , Pet.class)
                .setParameter("familyId", familyId)
                .getResultList();
    }

    public Pet findPetByPetId(Long petId) {
        return entityManager.createQuery(
                        "select p from Pet p" +
                                " join fetch p.petKind pk" +
                                " where p.petId = :petId"
                        , Pet.class)
                .setParameter("petId", petId)
                .getSingleResult();
    }
}
