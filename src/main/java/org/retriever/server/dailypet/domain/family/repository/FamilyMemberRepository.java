package org.retriever.server.dailypet.domain.family.repository;

import org.retriever.server.dailypet.domain.family.entity.FamilyMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FamilyMemberRepository extends JpaRepository<FamilyMember, Long> {

    Optional<FamilyMember> findByMemberId(Long memberId);
}
