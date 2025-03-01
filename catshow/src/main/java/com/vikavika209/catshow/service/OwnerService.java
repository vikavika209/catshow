package com.vikavika209.catshow.service;

import com.vikavika209.catshow.exception.CatNotFoundException;
import com.vikavika209.catshow.exception.OwnerNotFoundException;
import com.vikavika209.catshow.exception.ShowNotFoundException;
import com.vikavika209.catshow.model.Cat;
import com.vikavika209.catshow.model.Owner;
import com.vikavika209.catshow.model.Role;
import com.vikavika209.catshow.model.Show;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface OwnerService {
    Owner createOwner(String name, String email, String password, String city);
    Owner getOwner(long id) throws OwnerNotFoundException;
    List<Owner> getAllOwners ();
    Set<Cat> getCatsOfTheOwnerById(long ownerId) throws OwnerNotFoundException;
    Set<Show> getAllShowsByOwnerId(long id) throws ShowNotFoundException, CatNotFoundException, OwnerNotFoundException;
    Owner updateOwner(long id, String name, String email, String password, String city) throws OwnerNotFoundException, ShowNotFoundException, CatNotFoundException;
    void deleteOwner(long id);
    void deleteAll();
    UserDetails loadUserByUsername(String username);
    Owner getCurrentOwner() throws OwnerNotFoundException;
    Owner setOwnerRole(long id, Role role) throws ShowNotFoundException, CatNotFoundException, OwnerNotFoundException;
}
