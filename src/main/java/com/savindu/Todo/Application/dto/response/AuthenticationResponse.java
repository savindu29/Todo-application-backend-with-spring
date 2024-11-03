 package com.savindu.Todo.Application.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenticationResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;

}
