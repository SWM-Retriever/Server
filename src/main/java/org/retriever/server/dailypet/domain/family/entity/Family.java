package org.retriever.server.dailypet.domain.family.entity;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.retriever.server.dailypet.domain.diary.entity.Diary;
import org.retriever.server.dailypet.domain.family.dto.request.ChangeGroupTypeRequest;
import org.retriever.server.dailypet.domain.family.dto.request.CreateFamilyRequest;
import org.retriever.server.dailypet.domain.family.enums.FamilyStatus;
import org.retriever.server.dailypet.domain.family.enums.GroupType;
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

    @Column(length = 50)
    private String familyName;

    @Column(unique = true, length = 10)
    private String invitationCode;

    @Enumerated(EnumType.STRING)
    private GroupType groupType;

    @OneToMany(mappedBy = "family", cascade = CascadeType.PERSIST)
    @Builder.Default
    private List<FamilyMember> familyMemberList = new ArrayList<>();

    @OneToMany(mappedBy = "family", cascade = CascadeType.REMOVE)
    @Builder.Default
    private List<Pet> petList = new ArrayList<>();

    @OneToMany(mappedBy = "family")
    @Builder.Default
    private List<Diary> diaryList = new ArrayList<>();

    public static Family createFamily(CreateFamilyRequest dto, String invitationCode) {
        return Family.builder()
                .familyName(dto.getFamilyName())
                .familyStatus(FamilyStatus.ACTIVE)
                .invitationCode(invitationCode)
                .familyMemberList(new ArrayList<>())
                .groupType(GroupType.FAMILY)
                .build();
    }

    public static Family createFamilyAlone() {
        return Family.builder()
                .familyName(null)
                .familyStatus(FamilyStatus.ACTIVE)
                .invitationCode(null)
                .familyMemberList(new ArrayList<>())
                .groupType(GroupType.ALONE)
                .build();
    }

    public void insertNewMember(FamilyMember familyMember) {
        familyMemberList.add(familyMember);
    }

    public void linkDiary(Diary diary) {
        this.diaryList.add(diary);
    }

    public void changeGroupType(ChangeGroupTypeRequest dto, String invitationCode) {
        this.familyName = dto.getFamilyName();
        this.invitationCode = invitationCode;
        this.groupType = GroupType.FAMILY;
    }
}
