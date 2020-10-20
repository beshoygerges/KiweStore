package com.kiwie.store.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ChangePasswordResponse implements Serializable {

    private String message;
}
