package org.retriever.server.dailypet.domain.petcare.repository;

import lombok.RequiredArgsConstructor;
import org.retriever.server.dailypet.domain.petcare.entity.CareLog;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

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
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);
    }

    // 오늘 해당 챙겨주기 항목에 대한 횟수 조회 쿼리
    public int findTodayCountByCareId(Long petCareId) {
        return entityManager.createQuery("select c from CareLog c" +
                        " where" +
                        " c.petCare.petCareId = :petCareId" +
                        " and" +
                        " c.logDate = current_date", CareLog.class)
                .setParameter("petCareId", petCareId)
                .getResultList().size();
    }

    // 기간 동안 챙겨주기 횟수 조회 (항목 무관, 'CHECK'만)
    public List<CareLog> findCareLogBetweenDate(LocalDate startDate, LocalDate endDate) {
        return entityManager.createQuery(
                        "select c from CareLog c" +
                                " join fetch c.member" +
                                " where c.logDate between :startDate and :endDate", CareLog.class)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();
    }

    public List<CareLog> findCareLogFetchJoinPetCareAndMemberBetweenDate(LocalDate startDate, LocalDate endDate) {
        return entityManager.createQuery(
                        "select c from CareLog c" +
                                " join fetch c.member" +
                                " join fetch c.petCare" +
                                " where c.logDate between :startDate and :endDate", CareLog.class)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();
    }
}
