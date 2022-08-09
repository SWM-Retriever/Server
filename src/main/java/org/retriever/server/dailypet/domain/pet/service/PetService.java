package org.retriever.server.dailypet.domain.pet.service;

import lombok.RequiredArgsConstructor;
import org.retriever.server.dailypet.domain.family.entity.Family;
import org.retriever.server.dailypet.domain.family.exception.FamilyNotFoundException;
import org.retriever.server.dailypet.domain.family.repository.FamilyRepository;
import org.retriever.server.dailypet.domain.member.entity.Member;
import org.retriever.server.dailypet.domain.member.exception.MemberNotFoundException;
import org.retriever.server.dailypet.domain.member.repository.MemberRepository;
import org.retriever.server.dailypet.domain.pet.dto.request.RegisterPetRequest;
import org.retriever.server.dailypet.domain.pet.dto.request.ValidatePetNameInFamilyRequest;
import org.retriever.server.dailypet.domain.pet.dto.response.GetPetKindListResponse;
import org.retriever.server.dailypet.domain.pet.dto.response.RegisterPetResponse;
import org.retriever.server.dailypet.domain.pet.entity.Pet;
import org.retriever.server.dailypet.domain.pet.entity.PetKind;
import org.retriever.server.dailypet.domain.pet.enums.PetType;
import org.retriever.server.dailypet.domain.pet.exception.DuplicatePetNameInFamilyException;
import org.retriever.server.dailypet.domain.pet.exception.PetTypeNotFoundException;
import org.retriever.server.dailypet.domain.pet.repository.PetKindRepository;
import org.retriever.server.dailypet.domain.pet.repository.PetRepository;
import org.retriever.server.dailypet.global.config.security.CustomUserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PetService {

    private final FamilyRepository familyRepository;
    private final PetKindRepository petKindRepository;
    private final PetRepository petRepository;
    private final MemberRepository memberRepository;

    // TODO : 멤버 정보를 이용해서 속한 가족 정보를 바로 참조하는 쿼리 작성 (현재는 familyMember를 거쳐야함)
    public void validatePetNameInFamily(CustomUserDetails userDetails, ValidatePetNameInFamilyRequest dto, Long familyId) {
        Family family = familyRepository.findById(familyId).orElseThrow(FamilyNotFoundException::new);

        if (family.getPetList().stream()
                .anyMatch(pet -> pet.getPetName().equals(dto.getPetName()))) {
            throw new DuplicatePetNameInFamilyException();
        }
    }

    public List<GetPetKindListResponse> getPetKindListByType(PetType petType) {
        List<PetKind> petKinds = petKindRepository.findAllByPetTypeOrderByPetKindName(petType).orElseThrow(PetTypeNotFoundException::new);

        return petKinds.stream()
                .map(GetPetKindListResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public RegisterPetResponse registerPet(CustomUserDetails userDetails, RegisterPetRequest dto, Long familyId) {

        Member member = memberRepository.findById(userDetails.getId()).orElseThrow(MemberNotFoundException::new);
        Family family = familyRepository.findById(familyId).orElseThrow(FamilyNotFoundException::new);
        PetKind petKind = petKindRepository.findByPetKindId(dto.getPetKindId()).orElseThrow(PetTypeNotFoundException::new);

        Pet newPet = Pet.createPet(dto);

        newPet.setPetKind(petKind);
        newPet.setFamily(family);

        petRepository.save(newPet);

        return RegisterPetResponse.from(member, family);
    }
}
