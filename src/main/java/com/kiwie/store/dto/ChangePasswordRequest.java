package com.kiwie.store.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
public class ChangePasswordRequest implements Serializable {

    @NotEmpty
    @Pattern(regexp = "^[A-Za-z0-9]{8,}$")
    private String oldPassword;

    @NotEmpty
    @Pattern(regexp = "^[A-Za-z0-9]{8,}$")
    private String newPassword;
}
