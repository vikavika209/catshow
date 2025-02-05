package com.vikavika209.catshow.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Component
public class Owner implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Пожалуйста, введите ваш email")
    @Email(message = "Неверный формат email")
    @Column(nullable = false, unique = true)
    private String username;

    @NotBlank(message = "Пожалуйста, введите пароль")
    @Size(min = 6, message = "Пароль должен содержать минимум 6 символов")
    @Column
    private String password;

    @NotBlank(message = "Пожалуйста, введите ваше имя")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Пожалуйста, введите ваш город")
    @Column(nullable = false)
    private String city;

    @Column
    private Long balance;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    private List<Cat> cats;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "owner_roles", joinColumns = @JoinColumn(name = "owner_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Set<Role> roles = new HashSet<>();

    @PrePersist
    protected void onCreate(){
        if(balance == null || balance == 0){
            balance = 100L;
        }
    }

    public Owner(String email, String password, String name, String city, Long balance, List<Cat> cats) {
        this.username = email;
        this.password = password;
        this.name = name;
        this.city = city;
        this.balance = balance;
        this.cats = cats;
    }

    public Owner(String email, String password, String name, String city) {
        this.username = email;
        this.password = password;
        this.name = name;
        this.city = city;
    }

    public Owner(String email, String password, Set<Role> roles) {
        this.username = email;
        this.password = password;
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "Owner{" +
                "id=" + id +
                ", email='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", balance=" + balance +
                '}';
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getAuthority()))
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return this.username;
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
