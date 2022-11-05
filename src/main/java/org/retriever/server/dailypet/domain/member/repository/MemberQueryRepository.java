package org.retriever.server.dailypet.domain.member.repository;

import lombok.RequiredArgsConstructor;
import org.retriever.server.dailypet.domain.family.entity.FamilyMember;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberQueryRepository {

    private final EntityManager entityManager;

    public List<FamilyMember> findFamilyByMemberId(Long memberId) {
        return entityManager.createQuery("select f from FamilyMember f" +
                        " join fetch f.family" +
                        " where f.member.id = :memberId", FamilyMember.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }
}
