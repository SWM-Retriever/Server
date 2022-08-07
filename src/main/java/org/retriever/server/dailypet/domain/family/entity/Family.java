package org.retriever.server.dailypet.domain.family.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.retriever.server.dailypet.domain.enums.FamilyStatus;
import org.retriever.server.dailypet.domain.model.BaseTimeEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Family extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long familyId;

    @Enumerated(EnumType.STRING)
    private FamilyStatus familyStatus;

    @Column(nullable = false, unique = true, length = 50)
    private String familyName;

    private String invitationCode;

    private String profileImageUrl;

    @OneToMany(mappedBy = "family")
    private List<FamilyMember> familyMemberList = new ArrayList<>();
}
