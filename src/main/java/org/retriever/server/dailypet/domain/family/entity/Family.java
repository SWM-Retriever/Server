package org.retriever.server.dailypet.domain.family.entity;

import lombok.*;
import org.retriever.server.dailypet.domain.family.dto.request.CreateFamilyRequest;
import org.retriever.server.dailypet.domain.family.enums.FamilyStatus;
import org.retriever.server.dailypet.domain.model.BaseTimeEntity;
import org.retriever.server.dailypet.domain.pet.entity.Pet;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Family extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long familyId;

    @Enumerated(EnumType.STRING)
    private FamilyStatus familyStatus;

    @Column(nullable = false, unique = true, length = 50)
    private String familyName;

    @Column(nullable = false, unique = true, length = 10)
    private String invitationCode;

    private String profileImageUrl;

    @OneToMany(mappedBy = "family", cascade = CascadeType.ALL)
    private List<FamilyMember> familyMemberList = new ArrayList<>();

    @OneToMany(mappedBy = "family")
    private List<Pet> petList = new ArrayList<>();

    public static Family createFamily(CreateFamilyRequest dto, String invitationCode) {
        return Family.builder()
                .familyName(dto.getFamilyName())
                .familyStatus(FamilyStatus.ACTIVE)
                .invitationCode(invitationCode)
                .profileImageUrl(dto.getProfileImageUrl())
                .familyMemberList(new ArrayList<>())
                .build();
    }

    public void insertNewMember(FamilyMember familyMember) {
        familyMemberList.add(familyMember);
    }
}
