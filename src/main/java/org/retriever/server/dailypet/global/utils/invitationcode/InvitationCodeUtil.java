package org.retriever.server.dailypet.global.utils.invitationcode;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Component
@Slf4j
public class InvitationCodeUtil {

    private static final int INVITATION_CODE_DIGIT = 10;

    private InvitationCodeUtil() {

    }

    public static String createInvitationCode() {
        String uuid = UUID.randomUUID().toString();
        String shortUUID = parseToShortUUID(uuid);
        log.info("가족 생성 : 초대코드 생성 = {}", shortUUID);
        return shortUUID;
    }

    private static String parseToShortUUID(String uuid) {
        int anInt = ByteBuffer.wrap(uuid.getBytes(StandardCharsets.UTF_8)).getInt();
        return Integer.toString(anInt, INVITATION_CODE_DIGIT);
    }
}
