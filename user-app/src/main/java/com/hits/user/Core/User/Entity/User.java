package com.hits.user.Core.User.Entity;

import com.hits.common.Core.User.DTO.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name="users")
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Сущность пользователя")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private LocalDateTime createTime;

    @Column(unique = true, nullable = false)
    @Size(min = 1, message = "Минимальная длина электронной почты не менее 1 символа")
    @Pattern(regexp = "[a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\\.[a-zA-Z0-9_-]+", message = "Неверный адрес электронной почты")
    @Schema(description = "Адрес электронной почты", example = "example@example.ru")
    private String email;

    @Column(unique = true, nullable = false)
    @Size(min = 1, message = "Минимальная длина логина не менее 1 символа")
    @Pattern(regexp = "[a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\\.[a-zA-Z0-9_-]+|([a-zA-Z0-9]+)", message = "Логин должен состоять из букв и цифр")
    @Schema(description = "Логин пользователя", example = "example")
    private String login;

    @Column(unique = true, nullable = false)
    @Pattern(regexp = "^\\+7 \\(\\d{3}\\) \\d{3}-\\d{2}-\\d{2}$", message = "Телефон должен быть указан в формате +7 (xxx) xxx-xx-xx")
    @Schema(description = "Телефонный номер", example = "+7 (777) 777-77-77")
    private String phoneNumber;

    @Column(length = 1000, nullable = false)
    @Pattern(regexp = "^(?=.*\\d).{6,}$", message = "Пароль должен содержать не менее 6 символов и 1 цифры")
    @Schema(description = "Пароль пользователя", example = "qwerty12345")
    private String password;

    @Schema(description = "Код подтверждения аккаунта")
    private String verificationCode;

    @Column(nullable = false)
    @Schema(description = "Статус подтверждения аккаунта пользователя", example = "true")
    private Boolean isConfirmed;

    @Column(nullable = false)
    @Schema(description = "Статус блокировки аккаунта пользователя", example = "false")
    private Boolean isBanned;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Schema(description = "Роль пользователя", example = "USER")
    private Role role;

    @Schema(description = "Идентификаторы категорий, управляемых пользователем, являющимся модератором")
    @ElementCollection
    @CollectionTable(name = "user_manage_categories", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "manage_category_id")
    private List<UUID> manageCategoryId;

    @PrePersist
    private void init(){
        createTime = LocalDateTime.now();
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        String roleName = this.role.name();

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + roleName);

        authorities.add(authority);

        return authorities;
    }
    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}