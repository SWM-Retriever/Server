package org.retriever.server.dailypet.domain.member.dto.response;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EditProfileImageResponse {

    private String profileImageUrl;

    public static EditProfileImageResponse from(String profileImageUrl) {
        return EditProfileImageResponse.builder()
                .profileImageUrl(profileImageUrl)
                .build();
    }
}
