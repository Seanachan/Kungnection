package org.kungnection.controller;

import org.junit.jupiter.api.Test;
import org.kungnection.model.User;
import org.kungnection.security.JwtUtil;
import org.kungnection.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    @MockBean
    private JwtUtil jwtUtil;

    @Test
    public void testRegister() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("123456");
        when(userService.register(any(User.class))).thenReturn(user);

        String json = "{\"email\":\"test@example.com\",\"password\":\"123456\"}";
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void testLoginSuccess() throws Exception {
        User user = new User();
        user.setId(1);
        user.setEmail("test@example.com");
        user.setPassword("123456");
        when(userService.login("test@example.com", "123456")).thenReturn(user);
        when(jwtUtil.generateToken(1)).thenReturn("mocked-jwt-token");

        String json = "{\"email\":\"test@example.com\",\"password\":\"123456\"}";
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string("mocked-jwt-token"));
    }

    @Test
    public void testLoginFail() throws Exception {
        when(userService.login("fail@example.com", "wrong")).thenReturn(null);
        String json = "{\"email\":\"fail@example.com\",\"password\":\"wrong\"}";
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isUnauthorized());
    }
}
