package org.retriever.server.dailypet.domain.s3.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class PresignedUrlResponse {

    private String presignedUrl;

    private String originalUrl;

    public static PresignedUrlResponse from(String presignedUrl) {
        return PresignedUrlResponse.builder()
                .presignedUrl(presignedUrl)
                .originalUrl(presignedUrl.substring(0, presignedUrl.lastIndexOf("?")))
                .build();
    }
}
