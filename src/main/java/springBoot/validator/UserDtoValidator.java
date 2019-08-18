package springBoot.validator;

import springBoot.dto.UserDTO;

public class UserDtoValidator implements Validator<UserDTO> {

    private final PasswordValidator passwordValidator;
    private final UsernameValidator usernameValidator;

    public UserDtoValidator(PasswordValidator passwordValidator, UsernameValidator usernameValidator) {
        this.passwordValidator = passwordValidator;
        this.usernameValidator = usernameValidator;
    }


    @Override
    public Result validate(UserDTO dto) {
        if (!usernameValidator.validate(dto.getUsername()).isValid())
            return new Result(false, usernameValidator.validate(dto.getUsername()).getMessage());
        if (!passwordValidator.validate(dto.getPassword()).isValid())
            return new Result(false, passwordValidator.validate(dto.getPassword()).getMessage());

        return new Result(true);

    }
}