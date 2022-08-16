package org.retriever.server.dailypet.domain.petcare.repository;

import org.retriever.server.dailypet.domain.petcare.entity.CareLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CareLogRepository extends JpaRepository<CareLog, Long> {
}
