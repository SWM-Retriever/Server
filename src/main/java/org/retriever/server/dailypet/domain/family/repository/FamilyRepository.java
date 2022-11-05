package org.retriever.server.dailypet.domain.family.repository;

import org.retriever.server.dailypet.domain.family.entity.Family;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FamilyRepository extends JpaRepository<Family, Long> {

    Optional<Family> findByFamilyName(String familyName);

    Optional<Family> findByInvitationCode(String invitationCode);
}
