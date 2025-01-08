package com.vikavika209.catshow.model;

import jakarta.persistence.*;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.Set;

@Entity
@Component
public class Show {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "show_seq")
    @SequenceGenerator(name = "show_seq", sequenceName = "show_seq", allocationSize = 1)
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
    private Set<Cat> potentialParticipants;

    @ManyToMany
    private Set<Cat> participants;

    public Show() {
    }

    public Show(String city, Date date, String address) {
        this.city = city;
        this.date = date;
        this.address = address;
    }

    public long getId() {
        return id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<Cat> getPotentialParticipants() {
        return potentialParticipants;
    }

    public void setPotentialParticipants(Set<Cat> potentialParticipants) {
        this.potentialParticipants = potentialParticipants;
    }

    public Set<Cat> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<Cat> participants) {
        this.participants = participants;
    }

    @Override
    public String toString() {
        return "Show{" +
                "city='" + city + '\'' +
                ", date=" + date +
                ", address='" + address + '\'' +
                '}';
    }
}
