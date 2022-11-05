package org.retriever.server.dailypet.domain.family.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.retriever.server.dailypet.domain.common.factory.FamilyFactory;
import org.retriever.server.dailypet.domain.family.dto.request.CreateFamilyRequest;
import org.retriever.server.dailypet.domain.family.enums.FamilyStatus;
import org.retriever.server.dailypet.domain.family.enums.GroupType;
import org.retriever.server.dailypet.domain.pet.dto.response.PetInfoResponse;
import org.retriever.server.dailypet.global.utils.invitationcode.InvitationCodeUtil;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class FamilyTest {

    @DisplayName("그룹 생성 request를 받아서 새로운 그룹 생성")
    @Test
    void create_family() {

        // given
        CreateFamilyRequest familyRequest = FamilyFactory.createFamilyRequest();
        String invitationCode = InvitationCodeUtil.createInvitationCode();

        // when
        Family family = Family.createFamily(familyRequest, invitationCode);

        // then
        assertThat(family.getFamilyName()).isEqualTo(familyRequest.getFamilyName());
        assertThat(family.getFamilyStatus()).isEqualTo(FamilyStatus.ACTIVE);
        assertThat(family.getInvitationCode()).isEqualTo(invitationCode);
        assertThat(family.getGroupType()).isEqualTo(GroupType.FAMILY);
    }

    @Test
    void null_test() {
        Family family = Family.builder().build();
        System.out.println("family.getFamilyName() = " + family.getFamilyName());
        System.out.println("family.getFamilyId() = " + family.getFamilyId());
        List<PetInfoResponse> list = family.getPetList().stream().map(PetInfoResponse::new).collect(Collectors.toList());
        System.out.println("list.size() = " + list.size());

        family = FamilyFactory.createTestFamily();
        System.out.println("family.getFamilyName() = " + family.getFamilyName());
    }
}
