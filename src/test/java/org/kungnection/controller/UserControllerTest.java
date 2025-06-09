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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthControllerTest.class)
public class UserControllerTest {
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
}
