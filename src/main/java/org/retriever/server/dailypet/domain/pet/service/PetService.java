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
import org.retriever.server.dailypet.domain.pet.dto.response.CareLogHistory;
import org.retriever.server.dailypet.domain.pet.dto.response.GetPetCaresDetailResponse;
import org.retriever.server.dailypet.domain.pet.dto.response.GetPetKindListResponse;
import org.retriever.server.dailypet.domain.pet.dto.response.RegisterPetResponse;
import org.retriever.server.dailypet.domain.pet.entity.Pet;
import org.retriever.server.dailypet.domain.pet.entity.PetKind;
import org.retriever.server.dailypet.domain.pet.enums.PetType;
import org.retriever.server.dailypet.domain.pet.exception.DuplicatePetNameInFamilyException;
import org.retriever.server.dailypet.domain.pet.exception.PetNotFoundException;
import org.retriever.server.dailypet.domain.pet.exception.PetTypeNotFoundException;
import org.retriever.server.dailypet.domain.pet.repository.PetKindRepository;
import org.retriever.server.dailypet.domain.pet.repository.PetRepository;
import org.retriever.server.dailypet.domain.petcare.entity.CareLog;
import org.retriever.server.dailypet.domain.petcare.entity.PetCare;
import org.retriever.server.dailypet.domain.petcare.repository.CareLogQueryRepository;
import org.retriever.server.dailypet.domain.petcare.repository.CareLogRepository;
import org.retriever.server.dailypet.global.config.security.CustomUserDetails;
import org.retriever.server.dailypet.global.utils.s3.S3FileUploader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
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
    private final CareLogQueryRepository careLogRepository;
    private final S3FileUploader s3FileUploader;

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
    public RegisterPetResponse registerPet(CustomUserDetails userDetails, RegisterPetRequest dto,
                                           Long familyId, MultipartFile image) throws IOException {

        Member member = memberRepository.findById(userDetails.getId()).orElseThrow(MemberNotFoundException::new);
        Family family = familyRepository.findById(familyId).orElseThrow(FamilyNotFoundException::new);
        PetKind petKind = petKindRepository.findByPetKindId(dto.getPetKindId()).orElseThrow(PetTypeNotFoundException::new);
        String profileImageUrl = s3FileUploader.upload(image, "test");

        Pet newPet = Pet.createPet(dto, profileImageUrl);

        newPet.setPetKind(petKind);
        newPet.setFamily(family);

        petRepository.save(newPet);

        return RegisterPetResponse.from(member, family);
    }

    /**
     *  // response : 이름, 횟수, 현재횟수, list
     * // 케어 리스트 조회, 각 케어 리스트마다 챙겨주기 이름, 최대 횟수, 현재 횟수, 기록(이름 리스트)
     * // 기록은 CareLogRepository에서 각 petCareId로 조회하되, 오늘 날짜만 뽑아오면 될듯. 그러면 petCare랑 careLog랑 fetchJoin해야할듯
     * // 현재 러프하게 짜면 하나의 펫에 n개의 petcareList가 나오고, petCareList에서 이름 최대횟수 현재횟수를 꺼내면서 N+1문제가 발생
     * // 이후 carelogRepository에서 petCareList만큼 꺼내면 역시 n번의 쿼리가 나올 것
     * // 최종 1+N+N 쿼리가 나옴 (펫 한개 + 케어리스트 N개에서 필드 조회(N번) + 각 케어리스트마다 케어로그 조회(N번))
     */
    // TODO : N+1 문제 해결하기
    public List<GetPetCaresDetailResponse> getPetCaresDetail(CustomUserDetails userDetails, Long petId) {
        Pet pet = petRepository.findById(petId).orElseThrow(PetNotFoundException::new);

        List<GetPetCaresDetailResponse> responseList = new ArrayList<>();

        List<PetCare> petCareList = pet.getPetCareList();
        for (PetCare petCare : petCareList) {
            Long petCareId = petCare.getPetCareId();
            List<CareLog> careLogListWithCurDate = careLogRepository
                    .findByPetCareIdWithCurDateOrderByCreatedAt(petCareId);
            List<CareLogHistory> careLogHistoryList = careLogListWithCurDate.stream()
                    .map(CareLogHistory::new)
                    .collect(Collectors.toList());

            GetPetCaresDetailResponse petCaresDetailResponse =
                    GetPetCaresDetailResponse.of(petCare, careLogHistoryList);
            responseList.add(petCaresDetailResponse);
        }
        return responseList;
    }
}
