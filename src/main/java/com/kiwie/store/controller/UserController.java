package com.kiwie.store.controller;

import com.kiwie.store.dto.*;
import com.kiwie.store.entity.User;
import com.kiwie.store.entity.UserSession;
import com.kiwie.store.service.JwtService;
import com.kiwie.store.service.UserService;
import com.kiwie.store.util.MapperUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping("/rest/v1/users")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public BaseResponse<UserDto> register(@Valid @RequestBody final UserDto userDto) {
        return userService.register(userDto);
    }

    @PutMapping("/{id}/changePassword")
    public BaseResponse<Void> changePassword(final @PathVariable Long id, @Valid @RequestBody final ChangePasswordRequest request) {
        return userService.changePassword(id, request);
    }

    @PostMapping("/forgetPassword")
    public BaseResponse<ForgetPasswordResponse> forgetPassword(@Valid @RequestBody final ForgetPasswordRequest request) {
        return userService.forgetPassword(request);
    }

    @PutMapping("/resetPassword")
    public BaseResponse<Void> resetPassword(@Valid @RequestBody final ResetPasswordRequest request) {
        return userService.resetPassword(request);
    }

    @Transactional
    @PostMapping("/login")
    public BaseResponse<UserDto> login(@Valid @RequestBody final LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUser(), request.getPassword()));

        final User user = (User) userService.loadUserByUsername(request.getUser());

        userService.invalidateUserSessions(user);

        final UserSession userSession = new UserSession();

        user.addUserSession(userSession);

        user.setToken(jwtService.generateToken(user, userSession.getSessionId()));

        final BaseResponse baseResponse = BaseResponse.USER_LOGIN_RESPONSE;

        baseResponse.setData(MapperUtil.map(user, UserDto.class));

        return baseResponse;
    }

    @PostMapping("/{id}/logout")
    public BaseResponse<Void> logout(@PathVariable Long id) {
        return userService.logout(id);
    }
}
