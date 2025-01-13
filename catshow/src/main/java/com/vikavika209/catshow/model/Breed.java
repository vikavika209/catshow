package com.vikavika209.catshow.model;

public enum Breed {
    MAINECOON("Мейкун"),
    BRITISHSHORTHAIR("Британская короткошерстная"),
    SIAMESE("Сиамская"),
    RAGDOLL("Рэгдолл"),
    SPHYNX("Сфинкс"),
    PERSIAN("Персидская"),
    DEVONREX("Девон-рекс"),
    ABYSSINIAN("Абиссинская");

    private final String displayName;

    Breed(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}