package org.retriever.server.dailypet.domain.family.dto.response;

import lombok.*;
import org.retriever.server.dailypet.domain.family.entity.Family;
import org.retriever.server.dailypet.domain.family.entity.FamilyMember;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class GetGroupResponse {

    private Long familyId;

    private String familyName;

    private int familyMemberCount;

    private List<FamilyMemberInfo> familyMemberList;

    public static GetGroupResponse of(Family family, List<FamilyMember> familyMembers) {
        return GetGroupResponse.builder()
                .familyId(family.getFamilyId())
                .familyName(family.getFamilyName())
                .familyMemberCount(familyMembers.size())
                .familyMemberList(familyMembers.stream()
                        .map(FamilyMemberInfo::new)
                        .collect(Collectors.toList()))
                .build();
    }
}
