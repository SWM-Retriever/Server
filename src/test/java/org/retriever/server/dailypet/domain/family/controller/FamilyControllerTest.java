package org.retriever.server.dailypet.domain.family.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.retriever.server.dailypet.domain.common.ControllerTest;
import org.retriever.server.dailypet.domain.common.factory.FamilyFactory;
import org.retriever.server.dailypet.domain.family.dto.request.ValidateFamilyNameRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("FamilyController 테스트")
@WithMockUser
class FamilyControllerTest extends ControllerTest {

    @DisplayName("가족 이름 검증 실패")
    @Test
    void validateFamilyName() {

    }

    @DisplayName("가족 이름 검증 성공")
    @Test
    void validateFamilyNameSuccess() throws Exception {
        ValidateFamilyNameRequest request = FamilyFactory.createValidateFamilyNameRequest();
        willDoNothing().given(familyService).validateFamilyName(any());

        ResultActions actions = mockMvc.perform(post("/api/v1/validation/family-name")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .characterEncoding("UTF-8"));

        actions
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse();
    }

    @DisplayName("가족 이름 검증 실패 - 중복")
    @Test
    void validateFamilyNameFail() throws Exception {
//        Family family = FamilyFactory.createTestFamily();
//        ValidateFamilyNameRequest request = FamilyFactory.createValidateFamilyNameRequest();
//        doThrow(DuplicateFamilyNameException.class).when(familyService).validateFamilyName(any());
//
//        ResultActions actions = mockMvc.perform(post("/api/v1/validation/family-name")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(request))
//                .characterEncoding("UTF-8"));
//
//        actions
//                .andExpect(status().isBadRequest())
//                .andDo(print())
//                .andReturn()
//                .getResponse();
    }

    @DisplayName("가족 내 닉네임 검증 성공")
    @Test
    void validateFamilyRoleNameSuccess() {
    }

    @DisplayName("가족 생성 성공")
    @Test
    void createFamilySuccess() {

    }

    @DisplayName("가족 초대 코드 입력")
    @Test
    void findFamilyWithInvitationCode() throws Exception {
//
//        // given
//        Family family = FamilyFactory.createTestFamily();
//        FindFamilyWithInvitationCodeResponse response = FamilyFactory.findFamilyWithInvitationCodeResponse();
//        given(familyService.findFamilyWithInvitationCode(any())).willReturn(response);
//
//        // when
//        ResultActions actions = mockMvc.perform(get("/api/v1/families/{invitationCode}", family.getInvitationCode())
//                .contentType(MediaType.APPLICATION_JSON)
//                .characterEncoding("UTF-8"));
//
//        // then
//        actions
//                .andExpect(status().isOk())
////                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("familyId").value(response.getFamilyId()))
//                .andExpect(jsonPath("familyName").value(response.getFamilyName()))
//                .andExpect(jsonPath("profileImageUrl").value(response.getProfileImageUrl()))
//                .andExpect(jsonPath("familyMemberCount").value(5))
//                .andDo(print())
//                .andReturn()
//                .getResponse();

    }

    @DisplayName("가족 입장")
    @Test
    void enterFamily() {

    }
}
