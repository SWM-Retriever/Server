package org.retriever.server.dailypet.domain.family.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.retriever.server.dailypet.domain.common.config.SecurityTestConfig;
import org.retriever.server.dailypet.domain.common.factory.FamilyFactory;
import org.retriever.server.dailypet.domain.family.dto.request.ValidateFamilyNameRequest;
import org.retriever.server.dailypet.domain.family.repository.FamilyRepository;
import org.retriever.server.dailypet.domain.family.service.FamilyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("FamilyController 테스트")
@WebMvcTest(controllers = FamilyController.class)
@Import(SecurityTestConfig.class)
class FamilyControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    FamilyService familyService;

    @MockBean
    FamilyRepository familyRepository;

    @DisplayName("가족 이름 검증 실패")
    @Test
    void validateFamilyName() {

    }

    @DisplayName("가족 이름 검증 성공")
    @Test
    void validateFamilyNameSuccess() throws Exception {
        ValidateFamilyNameRequest request = FamilyFactory.createValidateFamilyNameRequest();
        given(familyRepository.findByFamilyName(any())).willReturn(Optional.empty());

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
