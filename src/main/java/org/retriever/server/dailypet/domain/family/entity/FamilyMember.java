package org.retriever.server.dailypet.domain.family.entity;

import lombok.*;
import org.retriever.server.dailypet.domain.member.entity.Member;
import org.retriever.server.dailypet.domain.model.BaseTimeEntity;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class FamilyMember extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long familyMemberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "familyId", nullable = false)
    private Family family;

    public static FamilyMember createFamilyMember(Member member, Family family) {
        return FamilyMember.builder()
                .member(member)
                .family(family)
                .build();
    }
}
