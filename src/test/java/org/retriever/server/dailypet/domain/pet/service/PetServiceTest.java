package org.retriever.server.dailypet.domain.pet.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.retriever.server.dailypet.domain.common.factory.FamilyFactory;
import org.retriever.server.dailypet.domain.common.factory.MemberFactory;
import org.retriever.server.dailypet.domain.common.factory.PetFactory;
import org.retriever.server.dailypet.domain.family.entity.Family;
import org.retriever.server.dailypet.domain.family.repository.FamilyRepository;
import org.retriever.server.dailypet.domain.member.entity.Member;
import org.retriever.server.dailypet.domain.member.enums.AccountProgressStatus;
import org.retriever.server.dailypet.domain.member.repository.MemberRepository;
import org.retriever.server.dailypet.domain.pet.dto.request.RegisterPetRequest;
import org.retriever.server.dailypet.domain.pet.dto.request.ValidatePetNameInFamilyRequest;
import org.retriever.server.dailypet.domain.pet.dto.response.GetPetKindListResponse;
import org.retriever.server.dailypet.domain.pet.dto.response.RegisterPetResponse;
import org.retriever.server.dailypet.domain.pet.entity.PetKind;
import org.retriever.server.dailypet.domain.pet.enums.PetType;
import org.retriever.server.dailypet.domain.pet.exception.DuplicatePetNameInFamilyException;
import org.retriever.server.dailypet.domain.pet.exception.PetTypeNotFoundException;
import org.retriever.server.dailypet.domain.pet.repository.PetKindRepository;
import org.retriever.server.dailypet.domain.pet.repository.PetRepository;
import org.retriever.server.dailypet.global.utils.s3.S3FileUploader;
import org.retriever.server.dailypet.global.utils.security.SecurityUtil;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PetServiceTest {

    @Mock
    PetRepository petRepository;

    @Mock
    FamilyRepository familyRepository;

    @Mock
    PetKindRepository petKindRepository;

    @Mock
    MemberRepository memberRepository;

    @Mock
    S3FileUploader s3FileUploader;

    @Mock
    SecurityUtil securityUtil;

    @InjectMocks
    PetService petService;

    @DisplayName("반려동물 이름 검증 - 검증 실패 시 DuplicatePetNameInFamilyException 발생")
    @Test
    void validate_pet_name_fail_and_throw_exception() {

        // given
        String name1 = "doggy";
        String name2 = "puppy";
        Family family = FamilyFactory.createTestFamilyWithPetList(name1, name2);
        ValidatePetNameInFamilyRequest request = PetFactory.createValidatePetNameInFamilyRequest(name1);
        given(familyRepository.findById(any())).willReturn(Optional.of(family));

        // when, then
        assertThrows(DuplicatePetNameInFamilyException.class, () -> petService.validatePetNameInFamily(request, any()));
    }

    @DisplayName("품종 정보 조회 - PetType에 해당하는 반려동물 품종 정보 조회 실패 시 PetTypeNotFoundException 발생")
    @Test
    void get_pet_kind_list_fail() {

        // given DOG는 조회 성공이지만 테스트용
        PetType nonePetType = PetType.DOG;
        given(petKindRepository.findAllByPetTypeOrderByPetKindName(any())).willReturn(Optional.empty());

        // when, then
        assertThrows(PetTypeNotFoundException.class, () -> petService.getPetKindListByType(nonePetType));
    }

    @DisplayName("품종 정보 모름- 해당 반려견의 품종 정보를 모르는 경우 UNKNOWN 처리")
    @Test
    void unknown_pet_kind() {

        // given
        PetType unknown = PetType.UNKNOWN;
        List<PetKind> unknownPetKind = PetFactory.createUnknownPetKind();
        given(petKindRepository.findAllByPetTypeOrderByPetKindName(any())).willReturn(Optional.of(unknownPetKind));

        // when
        GetPetKindListResponse response = petService.getPetKindListByType(unknown);

        // then
        assertThat(response.getPetKindList().size()).isEqualTo(1);
        assertThat(response.getPetKindList().get(0).getPetKindName()).isEqualTo(unknownPetKind.get(0).getPetKindName());
        assertThat(response.getPetKindList().get(0).getPetKindId()).isEqualTo(0L);
    }

    @DisplayName("반려동물 등록 - 요청을 받아서 정상 등록을 진행한다.")
    @Test
    void register_pet_success() throws IOException {

        // given
        Member member = MemberFactory.createTestMember();
        MockMultipartFile file = MemberFactory.createMultipartFile();
        Family family = FamilyFactory.createTestFamily();
        PetKind petKind = PetFactory.createTestPetKind();
        RegisterPetRequest request = PetFactory.createRegisterPetRequest();
        String imageUrl = "testUrl";

        given(familyRepository.findById(any())).willReturn(Optional.of(family));
        given(s3FileUploader.upload(any(), any())).willReturn(imageUrl);
        given(petKindRepository.findByPetKindId(any())).willReturn(Optional.of(petKind));
        given(securityUtil.getMemberByUserDetails()).willReturn(member);

        // when
        RegisterPetResponse response = petService.registerPet(request, family.getFamilyId(), file);

        // then
        verify(petRepository, times(1)).save(any());
        assertThat(response.getFamilyId()).isEqualTo(family.getFamilyId());
        assertThat(response.getFamilyName()).isEqualTo(family.getFamilyName());
        assertThat(response.getNickName()).isEqualTo(member.getNickName());
        assertThat(response.getGroupType()).isEqualTo(family.getGroupType());
        assertThat(response.getProfileImageUrl()).isEqualTo(member.getProfileImageUrl());
        assertThat(response.getInvitationCode()).isEqualTo(family.getInvitationCode());
        assertThat(response.getPetList()).isNotNull();
        assertThat(response.getPetList().get(0).getPetName()).isEqualTo(request.getPetName());
        assertThat(family.getPetList()).isNotNull();
        assertThat(member.getAccountProgressStatus()).isEqualTo(AccountProgressStatus.PET);
    }

    @DisplayName("메인 페이지 - 반려동물 챙겨주기 탭 조회 성공")
    @Test
    void getPetCaresDetail() {

        // given
//        Pet pet = PetFactory.createTestPet();
//        given(petRepository.findById(any())).willReturn(Optional.of(pet));
    }
}
