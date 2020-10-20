package com.kiwie.store.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForgetPasswordResponse implements Serializable {

    private UUID forgetPasswordToken;
}
