package com.kiwie.store.service.impl;

import com.kiwie.store.dto.*;
import com.kiwie.store.entity.ForgetPasswordToken;
import com.kiwie.store.entity.User;
import com.kiwie.store.exception.InvalidTokenException;
import com.kiwie.store.exception.PasswordNotMatchedException;
import com.kiwie.store.exception.UserExistException;
import com.kiwie.store.exception.UserNotExistException;
import com.kiwie.store.repsoitory.UserRepository;
import com.kiwie.store.service.UserService;
import com.kiwie.store.util.MapperUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public BaseResponse<UserDto> register(final UserDto userDto) {

        final User user = MapperUtil.map(userDto, User.class);

        final Optional<User> optionalUser = userRepository.findByEmailOrUsername(user.getEmail(), user.getUsername());

        optionalUser.ifPresent(s -> {
            throw new UserExistException("user is already exist");
        });

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        final BaseResponse<UserDto> baseResponse = BaseResponse.USER_REGISTER_RESPONSE;

        UserDto data = MapperUtil.map(userRepository.save(user), UserDto.class);

        baseResponse.setData(data);

        return baseResponse;
    }

    @Transactional
    @Override
    public BaseResponse<Void> changePassword(final Long id, final ChangePasswordRequest request) {
        final Optional<User> optionalUser = userRepository.findById(id);

        final User user = optionalUser.orElseThrow(() -> new UserNotExistException("User not found"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword()))
            throw new PasswordNotMatchedException("incorrect password");

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        invalidateUserSessions(user);

        return BaseResponse.CHANGE_PASSWORD_RESPONSE;
    }

    @Transactional
    @Override
    public BaseResponse<ForgetPasswordResponse> forgetPassword(final ForgetPasswordRequest request) {
        final Optional<User> optionalUser = userRepository.findByEmailOrUsername(request.getUsername(), request.getUsername());

        final User user = optionalUser.orElseThrow(() -> new UserNotExistException("User not found"));

        invalidateUserTokens(user);

        ForgetPasswordToken forgetPasswordToken = new ForgetPasswordToken();

        user.addPasswordToken(forgetPasswordToken);

        final ForgetPasswordResponse data = new ForgetPasswordResponse(forgetPasswordToken.getToken());

        final BaseResponse<ForgetPasswordResponse> baseResponse = BaseResponse.FORGET_PASSWORD_RESPONSE;

        baseResponse.setData(data);

        return baseResponse;
    }

    @Transactional
    @Override
    public BaseResponse<Void> resetPassword(final ResetPasswordRequest request) {
        final Optional<User> optionalUser = userRepository.findByEmailOrUsername(request.getUsername(), request.getUsername());

        final User user = optionalUser.orElseThrow(() -> new UserNotExistException("User not found"));

        if (!isValidToken(user, request.getToken()))
            throw new InvalidTokenException("Forget Password Token is invalid or expired");

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        invalidateUserTokens(user);

        invalidateUserSessions(user);

        return BaseResponse.RESET_PASSWORD_RESPONSE;
    }

    @Transactional
    @Override
    public BaseResponse<Void> logout(final Long id) {
        final Optional<User> optionalUser = userRepository.findById(id);

        final User user = optionalUser.orElseThrow(() -> new UserNotExistException("User not found"));

        invalidateUserSessions(user);

        return BaseResponse.USER_LOGOUT_RESPONSE;
    }

    @Override
    public void invalidateUserSessions(final User user) {
        user.getUserSessions().forEach(s -> s.setEnabled(false));
    }

    private void invalidateUserTokens(final User user) {
        user.getPasswordTokens().forEach(t -> t.setUsed(true));
    }

    private boolean isValidToken(final User user, final UUID token) {
        return user.getPasswordTokens()
                .stream()
                .anyMatch(t -> t.getToken().equals(token) && !t.isUsed() && !t.isExpired());
    }

    @Override
    public User loadUserByUsername(final String s) {

        final Optional<User> optionalUser = userRepository.findByEmailOrUsername(s, s);

        return optionalUser.orElseThrow(
                () -> new UsernameNotFoundException("User not found " + s));
    }

}
