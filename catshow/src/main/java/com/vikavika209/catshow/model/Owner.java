package com.vikavika209.catshow.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Component
public class Owner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Пожалуйста, введите ваш email")
    @Email(message = "Неверный формат email")
    @Column(nullable = false, unique = true)
    private String email;

    @Transient
    @NotBlank(message = "Пожалуйста, введите пароль")
    @Size(min = 6, message = "Пароль должен содержать минимум 6 символов")
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

    @PrePersist
    protected void onCreate(){
        if(balance == null || balance == 0){
            balance = 100L;
        }
    }

    public Owner(String email, String password, String name, String city, Long balance, List<Cat> cats) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.city = city;
        this.balance = balance;
        this.cats = cats;
    }

    public Owner(String email, String password, String name, String city) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.city = city;
    }

    @Override
    public String toString() {
        return "Owner{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", balance=" + balance +
                '}';
    }
}
