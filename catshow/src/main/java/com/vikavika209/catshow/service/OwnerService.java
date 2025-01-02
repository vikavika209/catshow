package com.vikavika209.catshow.service;

import com.vikavika209.catshow.model.Owner;

import java.util.List;

public interface OwnerService {
    Owner createOwner(String name, String email, String password, String city);
    Owner getOwner(long id);
    Owner updateOwner(long id, String name, String email, String password, String city);
    void deleteOwner(long id);
}
