package org.retriever.server.dailypet.domain.family.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.retriever.server.dailypet.domain.common.factory.FamilyFactory;
import org.retriever.server.dailypet.domain.common.factory.MemberFactory;
import org.retriever.server.dailypet.domain.member.entity.Member;
import org.retriever.server.dailypet.domain.model.IsDeleted;

import static org.assertj.core.api.Assertions.assertThat;

class FamilyMemberTest {

    @DisplayName("FamilyMember 생성")
    @Test
    void create_family_member() {

        // given
        Member member = MemberFactory.createTestMember();
        Family family = FamilyFactory.createTestFamily();

        // when
        FamilyMember familyMember = FamilyMember.createFamilyMember(member, family);

        // then
        assertThat(familyMember.getFamily()).isEqualTo(family);
        assertThat(familyMember.getMember()).isEqualTo(member);
        assertThat(familyMember.getIsDeleted()).isEqualTo(IsDeleted.FALSE);
    }
}
