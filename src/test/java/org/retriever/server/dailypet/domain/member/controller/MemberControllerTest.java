package org.retriever.server.dailypet.domain.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.retriever.server.dailypet.domain.common.config.SecurityTestConfig;
import org.retriever.server.dailypet.domain.common.factory.MemberFactory;
import org.retriever.server.dailypet.domain.member.dto.request.SnsLoginRequest;
import org.retriever.server.dailypet.domain.member.dto.request.ValidateMemberNicknameRequest;
import org.retriever.server.dailypet.domain.member.dto.response.SnsLoginResponse;
import org.retriever.server.dailypet.domain.member.repository.MemberRepository;
import org.retriever.server.dailypet.domain.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("MemberController 테스트")
@WebMvcTest(controllers = MemberController.class)
@Import(SecurityTestConfig.class)
class MemberControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    MemberService memberService;

    @MockBean
    MemberRepository memberRepository;

    @DisplayName("로그인 시도 시 회원 확인 후 정상 로그인, familyId, petId는 모두 -1L")
    @Test
    void checkMemberAndLogin() throws Exception {

        // given
        SnsLoginRequest snsLoginRequest = MemberFactory.createSnsLoginRequest();
        SnsLoginResponse snsLoginResponse = MemberFactory.createSnsLoginResponse();
        given(memberService.checkMemberAndLogin(any()))
                .willReturn(snsLoginResponse);

        // when
        ResultActions actions = mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(snsLoginRequest))
                .characterEncoding("UTF-8"));

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("email").value(snsLoginRequest.getEmail()))
                .andExpect(jsonPath("snsNickName").value(snsLoginRequest.getSnsNickName()))
                .andExpect(jsonPath("jwtToken").value("testToken"))
                .andExpect(jsonPath("familyId").value(-1L))
                .andExpect(jsonPath("petIdList[0]").value(-1L))
                .andDo(print())
                .andReturn()
                .getResponse();
    }

    @DisplayName("닉네임 검증에 성공하는 요청")
    @Test
    void validateMemberNickName() throws Exception {

        // given
        ValidateMemberNicknameRequest validateNicknameRequest =
                MemberFactory.createValidateNicknameRequest("test");
        when(memberRepository.findByNickName(any())).thenReturn(Optional.empty());

        // when
        ResultActions actions = mockMvc.perform(post("/api/v1/validation/nickname")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validateNicknameRequest))
                .characterEncoding("UTF-8"));

        // then
        actions.andExpect(status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse();
    }

    @Test
    void signUpAndRegisterProfile() {
    }

    @Test
    @WithMockUser(roles = "MEMBER")
    void testController() throws Exception {

        // given

        // when
        final ResultActions actions = mockMvc.perform(get("/api/v1/test")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"));

        // then
        actions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().string(containsString("hello it's test")));

    }
}
