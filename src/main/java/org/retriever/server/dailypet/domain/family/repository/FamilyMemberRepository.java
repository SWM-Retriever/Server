package org.retriever.server.dailypet.domain.family.repository;

import org.retriever.server.dailypet.domain.family.entity.FamilyMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FamilyMemberRepository extends JpaRepository<FamilyMember, Long> {

}
