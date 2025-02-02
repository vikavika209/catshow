package com.vikavika209.catshow.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Set;

@Entity
@Getter
@Setter
public class Cat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Breed breed;

    @Column(nullable = false)
    private int score;

    @ManyToOne
    @JoinColumn
    private Owner owner;

    @ManyToMany(mappedBy = "potentialParticipants")
    private Set<Show> potentialShows;

    @ManyToMany(mappedBy = "participants")
    private Set<Show> participatedShows;

    public Cat(String name, Owner owner, int score, Breed breed) {
        this.name = name;
        this.owner = owner;
        this.score = score;
        this.breed = breed;
    }

    public Cat(String name, Breed breed, Owner owner) {
        this.name = name;
        this.breed = breed;
        this.owner = owner;
    }

    public Cat() {
    }

    @Override
    public String toString() {
        return "Cat{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", breed=" + breed +
                ", score=" + score +
                ", owner=" + owner.getId() +
                '}';
    }
}
