package com.example.ch01basictest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc // MockMvc를 사용하기 위한 어노테이션
public class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * 인증하지 않은 사용자 테스트
     */
    @Test
    public void testHomeAccess_Unauthenticated() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("hello"));
    }

    /**
     * ROLE_USER 권한을 가진 사용자 테스트
     */
    @Test
    @WithMockUser(roles = "USER")
    public void testUserAccess_AuthenticatedAsUser() throws Exception {
        mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("user page"));
    }

    /**
     * ROLE_ADMIN 권한을 가진 사용자 테스트
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    public void testAdminAccess_AuthenticatedAsAdmin() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("admin page"));
    }

    /**
     * ROLE_ADMIN 권한이 없는 사용자에 대한 테스트
     */
    @Test
    @WithMockUser(roles = "USER")
    public void testAdminAccess_DeniedForUserRole() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().isForbidden());
    }

    /**
     * ROLE_USER 권한이 없는 사용자에 대한 테스트
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    public void testUserAccess_DeniedForAdminRole() throws Exception {
        mockMvc.perform(get("/user"))
                .andExpect(status().isForbidden());
    }
}