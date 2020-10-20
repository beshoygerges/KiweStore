package com.kiwie.store.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class ForgetPasswordRequest implements Serializable {

    @NotEmpty
    private String username;
}
