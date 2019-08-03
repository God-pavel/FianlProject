package springBoot.entity.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER,
    SENIOR_CASHIER,
    MERCHANDISER,
    ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
