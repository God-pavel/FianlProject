package springBoot.dto;

import lombok.*;
import springBoot.entity.enums.Role;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDTO {

    private String username;
    private String password;
    private Set<Role> roles;
}