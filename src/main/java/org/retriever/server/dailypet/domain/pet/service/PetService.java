package org.retriever.server.dailypet.domain.pet.service;

import lombok.RequiredArgsConstructor;
import org.retriever.server.dailypet.domain.family.entity.Family;
import org.retriever.server.dailypet.domain.family.exception.FamilyNotFoundException;
import org.retriever.server.dailypet.domain.family.repository.FamilyRepository;
import org.retriever.server.dailypet.domain.pet.dto.request.ValidatePetNameInFamilyRequest;
import org.retriever.server.dailypet.domain.pet.exception.DuplicatePetNameInFamilyException;
import org.retriever.server.dailypet.global.config.security.CustomUserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PetService {

    private final FamilyRepository familyRepository;

    // TODO : 멤버 정보를 이용해서 속한 가족 정보를 바로 참조하는 쿼리 작성 (현재는 familyMember를 거쳐야함)
    public void validatePetNameInFamily(CustomUserDetails userDetails, ValidatePetNameInFamilyRequest dto, Long familyId) {
        Family family = familyRepository.findById(familyId).orElseThrow(FamilyNotFoundException::new);

        if (family.getPetList().stream()
                .anyMatch(pet -> pet.getPetName().equals(dto.getPetName()))) {
            throw new DuplicatePetNameInFamilyException();
        }
    }
}
