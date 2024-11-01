package com.savindu.Todo.Application.dto.request;

import jakarta.validation.constraints.NotEmpty;

public class AuthenticationRequest{
    @NotEmpty
    private String email;
    @NotEmpty
    private String password;


}
