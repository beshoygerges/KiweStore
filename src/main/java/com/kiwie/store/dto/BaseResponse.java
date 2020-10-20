package com.kiwie.store.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonInclude(NON_NULL)
public class BaseResponse<T> implements Serializable {

    public static final BaseResponse RESET_PASSWORD_RESPONSE = new BaseResponse("Password Changed Successfully");

    public static final BaseResponse ADD_PRODUCT_RESPONSE = new BaseResponse("Product added Successfully");

    public static final BaseResponse USER_REGISTER_RESPONSE = new BaseResponse("You have registered successfully");

    public static final BaseResponse CHANGE_PASSWORD_RESPONSE = RESET_PASSWORD_RESPONSE;

    public static final BaseResponse FORGET_PASSWORD_RESPONSE = new BaseResponse("Please use this token to change your Password");

    public static final BaseResponse USER_LOGIN_RESPONSE = new BaseResponse("Successful Login");

    public static final BaseResponse USER_LOGOUT_RESPONSE = new BaseResponse("Successful Logout");

    private LocalDateTime timestamp = LocalDateTime.now();

    private String code = "00";

    private String message = "Request processed successfully";

    private T data;

    public BaseResponse(T data) {
        this.data = data;
    }

    public BaseResponse(String message) {
        this.message = message;
    }
}
