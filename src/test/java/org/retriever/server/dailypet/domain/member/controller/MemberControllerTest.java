package org.retriever.server.dailypet.domain.member.controller;

import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Null;
import org.retriever.server.dailypet.domain.common.ControllerTest;
import org.retriever.server.dailypet.domain.common.factory.MemberFactory;
import org.retriever.server.dailypet.domain.family.enums.GroupType;
import org.retriever.server.dailypet.domain.member.dto.request.SnsLoginRequest;
import org.retriever.server.dailypet.domain.member.dto.request.ValidateMemberNicknameRequest;
import org.retriever.server.dailypet.domain.member.dto.response.CalculateDayResponse;
import org.retriever.server.dailypet.domain.member.dto.response.CheckGroupResponse;
import org.retriever.server.dailypet.domain.member.dto.response.CheckPetResponse;
import org.retriever.server.dailypet.domain.member.dto.response.SnsLoginResponse;
import org.retriever.server.dailypet.domain.member.exception.DuplicateMemberNicknameException;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("MemberController 테스트")
@WithMockUser
class MemberControllerTest extends ControllerTest {

    @DisplayName("로그인 시도 시 회원 확인 후 정상 로그인, familyId, petId는 모두 Null")
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
        verify(memberService, times(1)).checkMemberAndLogin(any());

        String content = actions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("email").value(snsLoginRequest.getEmail()))
                .andExpect(jsonPath("nickName").value(snsLoginRequest.getSnsNickName()))
                .andExpect(jsonPath("jwtToken").value(snsLoginResponse.getJwtToken()))
                .andExpect(jsonPath("familyId").value(IsNull.nullValue()))
                .andExpect(jsonPath("familyName").value("testFamily"))
                .andExpect(jsonPath("invitationCode").value("testCode"))
                .andExpect(jsonPath("groupType").value(GroupType.FAMILY.toString()))
                .andExpect(jsonPath("profileImageUrl").value("testImageUrl"))
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(content).isEqualTo(objectMapper.writeValueAsString(snsLoginResponse));
    }

    @DisplayName("닉네임 검증에 성공하는 요청")
    @Test
    void validateMemberNickName() throws Exception {

        // given
        ValidateMemberNicknameRequest validateNicknameRequest =
                MemberFactory.createValidateNicknameRequest("test");
        willDoNothing().given(memberService).validateMemberNickName(any());

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

    @DisplayName("닉네임 검증에 실패하는 요청")
    @Test
    void validate_nickname_fail() throws Exception {

        // given
        ValidateMemberNicknameRequest validateNicknameRequest =
                MemberFactory.createValidateNicknameRequest("test");

        willThrow(new DuplicateMemberNicknameException()).given(memberService).validateMemberNickName(any());

        // when
        ResultActions actions = mockMvc.perform(post("/api/v1/validation/nickname")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validateNicknameRequest))
                .characterEncoding("UTF-8"));

        // then
        actions.andExpect(status().isConflict())
                .andDo(print())
                .andReturn()
                .getResponse();
    }

//    @DisplayName("회원가입 - 프로필 등록")
//    @Test
//    void signUpAndRegisterProfile() throws Exception {
//
//        // given
//        SignUpRequest signUpRequest = MemberFactory.createSignUpRequest();
//        SignUpResponse signUpResponse = MemberFactory.createSignUpResponse();
//        MockMultipartFile image = MemberFactory.createMultipartFile();
//        String requestDto = objectMapper.writeValueAsString(signUpRequest);
//        MockMultipartFile dto = new MockMultipartFile("dto", "dto", "application/yml", requestDto.getBytes(StandardCharsets.UTF_8));
//        given(memberService.signUpAndRegisterProfile(any(), any())).willReturn(signUpResponse);
//
//        // when
//        ResultActions actions = mockMvc.perform(multipart("/api/v1/auth/sign-up")
//                .file(dto)
//                .file(image)
//                .contentType(MediaType.MULTIPART_FORM_DATA)
//                .accept(MediaType.APPLICATION_JSON)
//                .characterEncoding("UTF-8"));
//
//
//        // then
//        String response = actions.andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("jwtToken").value(signUpResponse.getJwtToken()))
//                .andDo(print())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//
//        assertThat(response).isEqualTo(objectMapper.writeValueAsString(signUpResponse));
//    }


    @Test
    @DisplayName("회원 프로필 사진 수정")
    void edit_profile_image() {

    }

    @Test
    @DisplayName("메인 페이지 - 반려동물과 보낸 시간 조회")
    void calculate_meet_day() throws Exception {

        // given
        CalculateDayResponse calculateDayResponse = MemberFactory.createCalculateMeetDay();
        given(memberService.calculateDayOfFirstMeet(any())).willReturn(calculateDayResponse);

        // when
        ResultActions actions = mockMvc.perform(get("/api/v1/main/pets/{petId}/days", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-AUTH-TOKEN", "testToken")
                .characterEncoding("UTF-8"));

        // then
        String response = actions.andExpect(status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(response).isEqualTo(objectMapper.writeValueAsString(calculateDayResponse));
    }

    @Test
    @DisplayName("그룹 유무 조회 API")
    void check_group_success() throws Exception {

        // given
        CheckGroupResponse checkGroupResponse = MemberFactory.createCheckGroupResponse();
        given(memberService.checkGroup()).willReturn(checkGroupResponse);
        // when
        ResultActions actions = mockMvc.perform(get("/api/v1/auth/family")
                .header("X-AUTH-TOKEN", "testToken")
                .characterEncoding("UTF-8"));
        // then
        String response = actions.andExpect(status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(response).isEqualTo(objectMapper.writeValueAsString(checkGroupResponse));
    }

    @Test
    @DisplayName("반려동물 유무 조회 API")
    void check_pet_success() throws Exception {
        // given
        CheckPetResponse checkPetResponse = MemberFactory.createCheckPetResponse();
        given(memberService.checkPet(any())).willReturn(checkPetResponse);
        // when
        ResultActions actions = mockMvc.perform(get("/api/v1/auth/families/{familyId}/pet", 1L)
                .header("X-AUTH-TOKEN", "testToken")
                .characterEncoding("UTF-8"));
        // then
        String response = actions.andExpect(status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(response).isEqualTo(objectMapper.writeValueAsString(checkPetResponse));
    }

    @Test
    @DisplayName("회원 탈퇴 API")
    void delete_member_success() throws Exception {
        // given
        willDoNothing().given(memberService).deleteMember();
        // when
        ResultActions actions = mockMvc.perform(delete("/api/v1/auth/member")
                .header("X-AUTH-TOKEN", "testToken")
                .characterEncoding("UTF-8"));
        // then
        actions.andExpect(status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse();
    }
}
