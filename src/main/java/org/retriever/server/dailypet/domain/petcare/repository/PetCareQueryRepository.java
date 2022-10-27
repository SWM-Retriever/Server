package org.retriever.server.dailypet.domain.petcare.repository;

import lombok.RequiredArgsConstructor;
import org.retriever.server.dailypet.domain.petcare.entity.PetCare;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PetCareQueryRepository {

    private final EntityManager entityManager;

    // 컬렉션 fetch join으로 컬렉션 개수만큼 row가 증가한다 => distinct를 통해 중복된 petCareId를 제거한다.
    public List<PetCare> findByPetIdFetchJoinCareAlarm(Long petId) {
        return entityManager.createQuery(
                        "select distinct pc from PetCare pc" +
                                " join fetch pc.petCareAlarmList" +
                                " where pc.pet.petId = :petId", PetCare.class)
                .setParameter("petId", petId)
                .getResultList();
    }

    public PetCare findPetCareFetchJoinCareAlarm(Long petCareId) {
        return entityManager.createQuery(
                        "select pc from PetCare pc" +
                                " join fetch pc.petCareAlarmList" +
                                " where pc.petCareId = :petCareId", PetCare.class)
                .setParameter("petCareId", petCareId)
                .getResultList()
                .get(0);
    }
}
