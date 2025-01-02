package com.vikavika209.catshow.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Component
public class Show {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private Date date;

    @Column(nullable = false)
    private String address;

    @ManyToMany
    @JoinTable(
            name = "show_cat",
            joinColumns = @JoinColumn(name = "show_id"),
            inverseJoinColumns = @JoinColumn(name = "cat_id")
    )
    private List<Cat> cats;

    public Show(String city, Date date, String address, List<Cat> cats) {
        this.city = city;
        this.date = date;
        this.address = address;
        this.cats = cats;
    }

    public Show(String city, String address, Date date) {
        this.city = city;
        this.address = address;
        this.date = date;
    }

    @Override
    public String toString() {
        return "Shows{" +
                "id=" + id +
                ", city='" + city + '\'' +
                ", date=" + date +
                ", address='" + address + '\'' +
                ", cats=" + cats +
                '}';
    }
}
