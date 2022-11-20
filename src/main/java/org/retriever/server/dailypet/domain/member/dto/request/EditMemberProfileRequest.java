package org.retriever.server.dailypet.domain.member.dto.request;

import lombok.*;


@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class EditMemberProfileRequest {

    private String nickName;

    private String profileImageUrl;
}
