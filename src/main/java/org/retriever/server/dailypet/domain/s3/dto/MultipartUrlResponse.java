package org.retriever.server.dailypet.domain.s3.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class MultipartUrlResponse {

    private String imageUrl;

    public static MultipartUrlResponse from(String imageUrl) {
        return MultipartUrlResponse.builder()
                .imageUrl(imageUrl)
                .build();
    }
}
