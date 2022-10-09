package org.retriever.server.dailypet.domain.diary.repository;

import org.retriever.server.dailypet.domain.diary.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

    // TODO : QueryDSL JPQL로 변경
    List<Diary> findAllByFamily_FamilyId(Long familyId);
}
