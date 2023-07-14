package com.jdc.balance.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
class AccessLogsControllerTest {

	private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @WithMockUser(authorities = "Admin")
    @Test
    void accssLogControllerWithAdminTest() throws Exception {
        mockMvc.perform(get("/admin/accesslogs"))
                .andExpect(status().isOk());
    }
    
    @WithMockUser(authorities = "Member")
    @Test
    void accssLogControllerWithMemberTest() throws Exception {
        mockMvc.perform(get("/admin/accesslogs"))
                .andExpect(status().isForbidden());
    }
    
    @Test
    void accessLogControllerWithAnonymousTest() throws Exception {
        mockMvc.perform(get("/admin/accesslogs"))
                .andExpect(status().is3xxRedirection()) // Expect a redirection
                .andExpect(result -> {
                    String redirectUrl = result.getResponse().getRedirectedUrl();
                    assertThat(redirectUrl).endsWith("/signin");
                });
    }

}
