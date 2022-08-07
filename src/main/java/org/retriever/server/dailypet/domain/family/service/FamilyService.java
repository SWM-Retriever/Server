package org.retriever.server.dailypet.domain.family.service;

import lombok.RequiredArgsConstructor;
import org.retriever.server.dailypet.domain.family.dto.request.ValidateFamilyNameRequest;
import org.retriever.server.dailypet.domain.family.exception.DuplicateFamilyNameException;
import org.retriever.server.dailypet.domain.family.repository.FamilyRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FamilyService {

    private final FamilyRepository familyRepository;

    public void validateFamilyName(ValidateFamilyNameRequest dto) {
        if (familyRepository.findByFamilyName(dto.getFamilyName()).isPresent()) {
            throw new DuplicateFamilyNameException();
        }
    }
}
