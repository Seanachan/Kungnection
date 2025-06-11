package org.kungnection.dto;

import lombok.Data;

@Data
public class UserUpdateDTO {
    private String username;
    private String nickname;
    private String email;
    private String password;
}