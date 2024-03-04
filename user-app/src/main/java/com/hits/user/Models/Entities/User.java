<<<<<<<< HEAD:user-app/src/main/java/com/hits/user/Models/Entity/User.java
package com.hits.user.Models.Entity;
========
package com.hits.user.Models.Entities;
>>>>>>>> 652e6b5cc00632fb43cd0fa859c1d48e64471d8d:user-app/src/main/java/com/hits/user/Models/Entities/User.java

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
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name="users")
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private LocalDateTime createTime;

    @Column(unique = true, nullable = false)
    @Size(min = 1, message = "Минимальная длина не менее 1 символа")
    @Pattern(regexp = "[a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\\.[a-zA-Z0-9_-]+", message = "Неверный адрес электронной почты")
    private String email;

<<<<<<<< HEAD:user-app/src/main/java/com/hits/user/Models/Entity/User.java
========
    @Column(unique = true, nullable = false)
    @Size(min = 1, message = "Минимальная длина не менее 1 символа")
    @Pattern(regexp = "[a-zA-Z0-9]+", message = "Логин должен состоять из букв и цифр")
    private String login;

>>>>>>>> 652e6b5cc00632fb43cd0fa859c1d48e64471d8d:user-app/src/main/java/com/hits/user/Models/Entities/User.java
    @Column(length = 1000, nullable = false)
    @Pattern(regexp = "^(?=.*\\d).{6,}$", message = "Пароль должен содержать не менее 6 символов и 1 цифры")
    private String password;

    @PrePersist
    private void init(){
        createTime = LocalDateTime.now();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
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