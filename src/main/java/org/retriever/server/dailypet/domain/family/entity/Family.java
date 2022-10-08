package org.retriever.server.dailypet.domain.family.entity;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
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
@Where(clause = "family_status = 'ACTIVE'")
@SQLDelete(sql = "UPDATE family SET family_status = 'DELETED' WHERE family_id = ?")
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

    @OneToMany(mappedBy = "family", cascade = CascadeType.ALL)
    @Builder.Default
    private List<FamilyMember> familyMemberList = new ArrayList<>();

    @OneToMany(mappedBy = "family", cascade = CascadeType.REMOVE)
    @Builder.Default
    private List<Pet> petList = new ArrayList<>();

    public static Family createFamily(CreateFamilyRequest dto, String invitationCode) {
        return Family.builder()
                .familyName(dto.getFamilyName())
                .familyStatus(FamilyStatus.ACTIVE)
                .invitationCode(invitationCode)
                .familyMemberList(new ArrayList<>())
                .build();
    }

    public void insertNewMember(FamilyMember familyMember) {
        familyMemberList.add(familyMember);
    }
}
