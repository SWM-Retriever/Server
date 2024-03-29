package org.retriever.server.dailypet.domain.s3;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.retriever.server.dailypet.domain.s3.dto.MultipartUrlResponse;
import org.retriever.server.dailypet.domain.s3.dto.PresignedUrlResponse;
import org.retriever.server.dailypet.global.utils.s3.S3FileUploader;
import org.retriever.server.dailypet.global.utils.s3.S3Path;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Image Upload API")
public class S3Controller {

    private final S3FileUploader s3FileUploader;

    @Operation(summary = "이미지 signedURL 반환 API", description = "파일의 이름 (dog.jpeg)과 종류에 맞는 enum type(MEMBER, PET, DIARY)을 넘겨서 signedURL을 얻는다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 요청 완료"),
            @ApiResponse(responseCode = "400", description = "요청 실패"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러")
    })
    @GetMapping("/presigned-url/{S3Path}/{fileName}")
    public ResponseEntity<PresignedUrlResponse> getPresignedUrl(@PathVariable S3Path S3Path, @PathVariable String fileName) {
        return ResponseEntity.ok(s3FileUploader.getPreSignedUrl(S3Path, fileName));
    }

    @Operation(summary = "이미지 Multipart 업로드", description = "멀티 파트 파일과 종류에 맞는 enum type(MEMBER, PET, DIARY)을 넘겨서 저장한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 요청 완료"),
            @ApiResponse(responseCode = "400", description = "요청 실패"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러")
    })
    @PostMapping("/{S3Path}/image")
    public ResponseEntity<MultipartUrlResponse> getS3Url(@RequestParam MultipartFile image, @PathVariable S3Path S3Path) throws IOException {
        return ResponseEntity.ok(s3FileUploader.uploadMultipartFileToS3(image, S3Path));
    }
}
