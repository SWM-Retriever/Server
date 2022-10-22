package org.retriever.server.dailypet.domain.diary.repository;

import lombok.RequiredArgsConstructor;
import org.retriever.server.dailypet.domain.diary.entity.Diary;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class DiaryQueryRepository {

    private final EntityManager entityManager;

    public Diary findRecentDiary(Long familyId) {
        return entityManager.createQuery(
                        "select d from Diary d" +
                                " join fetch d.author m" +
                                " where d.family.familyId = :familyId" +
                                " order by d.createdAt desc", Diary.class)
                .setParameter("familyId", familyId)
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);
    }
}
