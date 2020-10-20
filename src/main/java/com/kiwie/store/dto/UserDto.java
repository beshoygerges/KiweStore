package com.kiwie.store.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;
import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

@Data
@JsonInclude(NON_NULL)
public class UserDto implements Serializable {

    @JsonProperty(access = READ_ONLY)
    private Long id;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @Email
    private String email;

    @NotEmpty
    private String username;

    @JsonProperty(access = WRITE_ONLY)
    @Pattern(regexp = "^[A-Za-z0-9]{8,}$")
    private String password;

    @Min(value = 18, message = "your age should be not less than 18")
    private int age;

    @JsonProperty(access = READ_ONLY)
    private String token;
}
