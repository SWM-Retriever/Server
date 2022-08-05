package org.retriever.server.dailypet.domain.member.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum RoleType {
    MEMBER("ROLE_MEMBER", "일반 사용자 권한"),
    FAMILY_LEADER("ROLE_FAMILY_LEADER", "가족 그룹장 권한"),
    ADMIN("ROLE_ADMIN", "관리자 권한"),
    GUEST("GUEST", "게스트 권한");

    private final String code;
    private final String displayName;

    public static RoleType of(String code) {
        return Arrays.stream(RoleType.values())
                .filter(roleType -> roleType.getCode().equals(code))
                .findAny()
                .orElse(GUEST);
    }
}
