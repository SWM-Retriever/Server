package org.retriever.server.dailypet.domain.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.retriever.server.dailypet.domain.common.config.SecurityTestConfig;
import org.retriever.server.dailypet.domain.family.controller.FamilyController;
import org.retriever.server.dailypet.domain.family.service.FamilyService;
import org.retriever.server.dailypet.domain.member.controller.LoginController;
import org.retriever.server.dailypet.domain.member.controller.MemberController;
import org.retriever.server.dailypet.domain.member.service.LoginService;
import org.retriever.server.dailypet.domain.member.service.MemberService;
import org.retriever.server.dailypet.domain.pet.controller.PetController;
import org.retriever.server.dailypet.domain.pet.service.PetService;
import org.retriever.server.dailypet.domain.petcare.controller.PetCareController;
import org.retriever.server.dailypet.domain.petcare.service.PetCareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest({
        MemberController.class,
        FamilyController.class,
        LoginController.class,
        PetController.class,
        PetCareController.class
})
public abstract class ControllerTest extends SecurityTestConfig {

    @MockBean
    protected MemberService memberService;

    @MockBean
    protected LoginService loginService;

    @MockBean
    protected FamilyService familyService;

    @MockBean
    protected PetService petService;

    @MockBean
    protected PetCareService petCareService;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;
}
