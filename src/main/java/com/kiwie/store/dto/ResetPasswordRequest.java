package com.kiwie.store.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.UUID;

@Data
public class ResetPasswordRequest implements Serializable {

    @NotEmpty
    private String username;

    private UUID token;

    @Pattern(regexp = "^[A-Za-z0-9]{8,}$")
    private String password;
}
