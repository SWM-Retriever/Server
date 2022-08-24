package org.retriever.server.dailypet.domain.petcare.repository;

import lombok.RequiredArgsConstructor;
import org.retriever.server.dailypet.domain.petcare.entity.CareLog;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CareLogQueryRepository {

    private final EntityManager entityManager;

    public List<CareLog> findByPetCareIdWithCurDateOrderByCreatedAt(Long petCareId) {
        return entityManager.createQuery("select c from CareLog c" +
                        " where c.petCare.petCareId = :petCareId" +
                        " and" +
                        " c.logDate = current_date" +
                        " order by c.createdAt", CareLog.class)
                .setParameter("petCareId", petCareId)
                .getResultList();
    }

    public CareLog findByMemberIdAndCareIdWithCurDateLatestLimit1(Long memberId, Long petCareId) {
        return entityManager.createQuery("select c from CareLog c" +
                        " where" +
                        " c.member.id = :memberId" +
                        " and" +
                        " c.petCare.petCareId = :petCareId" +
                        " and" +
                        " c.logDate = current_date" +
                        " order by c.createdAt desc" +
                        " ", CareLog.class)
                .setParameter("memberId", memberId)
                .setParameter("petCareId", petCareId)
                .setMaxResults(1)
                .getSingleResult();
    }
}
