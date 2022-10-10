package org.retriever.server.dailypet.domain.family.repository;

import lombok.RequiredArgsConstructor;
import org.retriever.server.dailypet.domain.family.entity.FamilyMember;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class FamilyQueryRepository {

    private final EntityManager entityManager;

    public List<FamilyMember> findMembersByFamilyId(Long familyId) {
        return entityManager.createQuery(
                        "select fm from FamilyMember fm" +
                                " join fetch fm.member m" +
                                " join fetch fm.family f " +
                                " where f.familyId = :familyId", FamilyMember.class)
                .setParameter("familyId", familyId)
                .getResultList();
    }
}
