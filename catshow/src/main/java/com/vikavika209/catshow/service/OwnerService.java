package com.vikavika209.catshow.service;

import com.vikavika209.catshow.exception.OwnerNotFoundException;
import com.vikavika209.catshow.model.Owner;

public interface OwnerService {
    Owner createOwner(String name, String email, String password, String city);
    Owner getOwner(long id) throws OwnerNotFoundException;
    Owner updateOwner(long id, String name, String email, String password, String city) throws OwnerNotFoundException;
    void deleteOwner(long id);
}
