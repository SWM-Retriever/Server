package org.retriever.server.dailypet.domain.member.dto.response;

import lombok.*;
import org.retriever.server.dailypet.domain.member.entity.Member;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EditMemberProfileResponse {

    private String nickName;
    private String profileImageUrl;

    public static EditMemberProfileResponse from(Member member) {
        return EditMemberProfileResponse.builder()
                .nickName(member.getNickName())
                .profileImageUrl(member.getProfileImageUrl())
                .build();
    }
}
