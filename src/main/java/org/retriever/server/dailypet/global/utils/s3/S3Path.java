package org.retriever.server.dailypet.global.utils.s3;

import lombok.Getter;

@Getter
public enum S3Path {
    MEMBER("image/member/"),
    DIARY("image/diary/"),
    PET("image/pet/");

    private final String filePath;

    S3Path(String filePath) {
        this.filePath = filePath;
    }
}
