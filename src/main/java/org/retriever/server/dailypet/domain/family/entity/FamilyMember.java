package org.retriever.server.dailypet.domain.family.entity;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.retriever.server.dailypet.domain.member.entity.Member;
import org.retriever.server.dailypet.domain.model.BaseTimeEntity;
import org.retriever.server.dailypet.domain.model.IsDeleted;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Where(clause = "is_deleted = 'FALSE'")
@SQLDelete(sql = "UPDATE family_member SET is_deleted = 'TRUE' WHERE family_member_id = ?")
public class FamilyMember extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long familyMemberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "familyId", nullable = false)
    private Family family;

    @Enumerated(EnumType.STRING)
    private IsDeleted isDeleted;

    public static FamilyMember createFamilyMember(Member member, Family family) {
        return FamilyMember.builder()
                .member(member)
                .family(family)
                .isDeleted(IsDeleted.FALSE)
                .build();
    }
}
