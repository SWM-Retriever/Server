package org.retriever.server.dailypet.domain.common.factory;

import org.retriever.server.dailypet.domain.family.dto.request.CreateFamilyRequest;
import org.retriever.server.dailypet.domain.family.dto.request.EnterFamilyRequest;
import org.retriever.server.dailypet.domain.family.dto.request.ValidateFamilyNameRequest;
import org.retriever.server.dailypet.domain.family.dto.request.ValidateFamilyRoleNameRequest;
import org.retriever.server.dailypet.domain.family.dto.response.CreateFamilyResponse;
import org.retriever.server.dailypet.domain.family.dto.response.FindFamilyWithInvitationCodeResponse;
import org.retriever.server.dailypet.domain.family.entity.Family;
import org.retriever.server.dailypet.domain.family.entity.FamilyMember;
import org.retriever.server.dailypet.domain.family.enums.FamilyStatus;
import org.retriever.server.dailypet.domain.family.enums.GroupType;
import org.retriever.server.dailypet.domain.member.entity.Member;
import org.retriever.server.dailypet.domain.pet.entity.Pet;

import java.util.ArrayList;
import java.util.List;

public class FamilyFactory {

    private FamilyFactory() {
    }

    public static Family createTestFamily() {
        return Family.builder()
                .familyId(1L)
                .familyName("testFamily")
                .familyStatus(FamilyStatus.ACTIVE)
                .invitationCode("1234567890")
                .familyMemberList(new ArrayList<>())
                .petList(new ArrayList<>())
                .groupType(GroupType.FAMILY)
                .build();
    }

    public static CreateFamilyRequest createFamilyRequest() {
        return CreateFamilyRequest.builder()
                .familyName("testFamily")
                .familyRoleName("동생")
                .build();
    }

    public static ValidateFamilyNameRequest createValidateFamilyNameRequest() {
        return ValidateFamilyNameRequest.builder()
                .familyName("testFamily")
                .build();
    }

    public static ValidateFamilyRoleNameRequest createValidateFamilyRoleNameRequest(String name) {
        return ValidateFamilyRoleNameRequest.builder()
                .familyRoleName(name)
                .build();
    }

    public static EnterFamilyRequest createEnterFamilyRequest() {
        return EnterFamilyRequest
                .builder()
                .familyRoleName("동생")
                .build();
    }

    public static CreateFamilyResponse createFamilyResponse() {
        return CreateFamilyResponse.builder()
                .familyId(1L)
                .build();
    }

    public static FindFamilyWithInvitationCodeResponse findFamilyWithInvitationCodeResponse() {
        return FindFamilyWithInvitationCodeResponse.builder()
                .familyName("testFamily")
                .familyId(1L)
                .familyMemberCount(5)
                .build();
    }

    public static Family createTestFamilyWithPetList(String name1, String name2) {
        return Family.builder()
                .familyName("testFamily")
                .familyStatus(FamilyStatus.ACTIVE)
                .invitationCode("1234567890")
                .familyMemberList(new ArrayList<>())
                .petList(new ArrayList<>(List.of(Pet.builder().petName(name1).build(), Pet.builder().petName(name2).build())))
                .build();
    }

    public static Family createTestFamilyWithFamilyMember(FamilyMember familyMember) {
        return Family.builder()
                .familyName("testFamily")
                .familyStatus(FamilyStatus.ACTIVE)
                .invitationCode("1234567890")
                .familyMemberList(List.of(familyMember))
                .petList(new ArrayList<>())
                .build();
    }

    public static List<FamilyMember> createTestFamilyMember(Member member, Family family) {
        return List.of(
                FamilyMember.createFamilyMember(member, family)
        );
    }

    public static List<FamilyMember> createTestDuplicateFamilyMember(String name1, String name2) {
        return List.of(
                FamilyMember.builder().member((MemberFactory.createTestFamilyRoleNameMember(name1))).build(),
                FamilyMember.builder().member((MemberFactory.createTestFamilyRoleNameMember(name2))).build());
    }
}

