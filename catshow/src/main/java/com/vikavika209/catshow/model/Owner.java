package com.vikavika209.catshow.model;

import com.vikavika209.catshow.security.SecurityConfig;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
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

    @Email(message = "Email must be valid")
    @Column(nullable = false, unique = true)
    private String email;

    @Transient
    private String password;

    @Column(nullable = false)
    private String name;

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
