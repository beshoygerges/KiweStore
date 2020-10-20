package com.kiwie.store.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
public class LoginRequest implements Serializable {

    @NotEmpty
    private String user;

    @Pattern(regexp = "^[A-Za-z0-9]{8,}$")
    private String password;
}
