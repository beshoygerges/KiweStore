package com.kiwie.store.service;

import com.kiwie.store.dto.*;
import com.kiwie.store.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.validation.Valid;

public interface UserService extends UserDetailsService {

    BaseResponse<UserDto> register(UserDto userDto);

    BaseResponse<Void> changePassword(Long id, ChangePasswordRequest request);

    BaseResponse<ForgetPasswordResponse> forgetPassword(@Valid ForgetPasswordRequest user);

    BaseResponse<Void> resetPassword(ResetPasswordRequest request);

    BaseResponse<Void> logout(Long id);

    void invalidateUserSessions(User user);
}
