package org.retriever.server.dailypet.domain.common.factory;

import org.retriever.server.dailypet.domain.family.dto.request.CreateFamilyRequest;
import org.retriever.server.dailypet.domain.family.dto.request.ValidateFamilyNameRequest;
import org.retriever.server.dailypet.domain.family.dto.request.ValidateFamilyRoleNameRequest;
import org.retriever.server.dailypet.domain.family.entity.Family;
import org.retriever.server.dailypet.domain.family.enums.FamilyStatus;

import java.util.ArrayList;

public class FamilyFactory {

    private FamilyFactory() {
    }

    public static Family createTestFamily() {
        return Family.builder()
                .familyName("testFamily")
                .familyStatus(FamilyStatus.ACTIVE)
                .invitationCode("1234567890")
                .profileImageUrl("testImage")
                .familyMemberList(new ArrayList<>())
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
}
