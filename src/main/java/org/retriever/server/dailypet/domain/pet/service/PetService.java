package org.retriever.server.dailypet.domain.pet.service;

import lombok.RequiredArgsConstructor;
import org.retriever.server.dailypet.domain.family.entity.Family;
import org.retriever.server.dailypet.domain.family.exception.FamilyNotFoundException;
import org.retriever.server.dailypet.domain.family.repository.FamilyRepository;
import org.retriever.server.dailypet.domain.member.entity.Member;
import org.retriever.server.dailypet.domain.pet.dto.request.RegisterPetRequest;
import org.retriever.server.dailypet.domain.pet.dto.request.ValidatePetNameInFamilyRequest;
import org.retriever.server.dailypet.domain.pet.dto.response.*;
import org.retriever.server.dailypet.domain.pet.entity.Pet;
import org.retriever.server.dailypet.domain.pet.entity.PetKind;
import org.retriever.server.dailypet.domain.pet.enums.PetType;
import org.retriever.server.dailypet.domain.pet.exception.DuplicatePetNameInFamilyException;
import org.retriever.server.dailypet.domain.pet.exception.PetTypeNotFoundException;
import org.retriever.server.dailypet.domain.pet.repository.PetKindRepository;
import org.retriever.server.dailypet.domain.pet.repository.PetQueryRepository;
import org.retriever.server.dailypet.domain.pet.repository.PetRepository;
import org.retriever.server.dailypet.global.utils.security.SecurityUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PetService {

    private final FamilyRepository familyRepository;
    private final PetKindRepository petKindRepository;
    private final PetRepository petRepository;
    private final SecurityUtil securityUtil;
    private final PetQueryRepository petQueryRepository;

    // TODO : 멤버 정보를 이용해서 속한 가족 정보를 바로 참조하는 쿼리 작성 (현재는 familyMember를 거쳐야함)
    public void validatePetNameInFamily(ValidatePetNameInFamilyRequest dto, Long familyId) {
        Family family = familyRepository.findById(familyId).orElseThrow(FamilyNotFoundException::new);

        if (family.getPetList().stream()
                .anyMatch(pet -> pet.getPetName().equals(dto.getPetName()))) {
            throw new DuplicatePetNameInFamilyException();
        }
    }

    public GetPetKindListResponse getPetKindListByType(PetType petType) {
        List<PetKind> petKinds = petKindRepository.findAllByPetTypeOrderByPetKindName(petType).orElseThrow(PetTypeNotFoundException::new);

        return GetPetKindListResponse.from(
                petKinds.stream()
                .map(PetKindPair::new)
                .collect(Collectors.toList())
        );
    }

    @Transactional
    public RegisterPetResponse registerPet(RegisterPetRequest dto, Long familyId) throws IOException {

        Member member = securityUtil.getMemberByUserDetails();
        member.changeProgressStatusToPet();
        Family family = familyRepository.findById(familyId).orElseThrow(FamilyNotFoundException::new);
        PetKind petKind = petKindRepository.findByPetKindId(dto.getPetKindId()).orElseThrow(PetTypeNotFoundException::new);

        Pet newPet = Pet.createPet(dto);

        newPet.setPetKind(petKind);
        newPet.setFamily(family);

        family.getPetList().size();

        petRepository.save(newPet);

        return RegisterPetResponse.of(member, family);
    }

    public GetPetsResponse getPetsByFamilyId(Long familyId) {

        List<Pet> petList = petQueryRepository.findPetsByFamilyId(familyId);

        return GetPetsResponse.from(petList.stream()
                .map(PetInfoDetail::new)
                .collect(Collectors.toList())
        );
    }

    public PetInfoDetail getPetInfo(Long petId) {
        return PetInfoDetail.from(petQueryRepository.findPetByPetId(petId));
    }
}
