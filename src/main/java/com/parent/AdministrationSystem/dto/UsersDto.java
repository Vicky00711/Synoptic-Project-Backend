package com.parent.AdministrationSystem.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.parent.AdministrationSystem.entity.Students;
import com.parent.AdministrationSystem.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UsersDto {

    private long usersId;
    private String firstName;
    private String lastName;
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private Users.Level role;


}
