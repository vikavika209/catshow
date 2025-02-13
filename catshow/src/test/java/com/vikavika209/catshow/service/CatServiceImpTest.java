package com.vikavika209.catshow.service;

import com.vikavika209.catshow.exception.CatNotFoundException;
import com.vikavika209.catshow.exception.OwnerNotFoundException;
import com.vikavika209.catshow.exception.ShowNotFoundException;
import com.vikavika209.catshow.model.Breed;
import com.vikavika209.catshow.model.Cat;
import com.vikavika209.catshow.model.Owner;
import com.vikavika209.catshow.model.Show;
import com.vikavika209.catshow.repository.CatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CatServiceImpTest {

    @Mock
    private CatRepository catRepository;

    @Mock
    private OwnerServiceImp ownerService;

    @Mock
    private ShowServiceImp showService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    FromOptional fromOptional;

    @InjectMocks
    private CatServiceImp catService;

    private Cat cat;
    private Owner owner;
    private Show show;

    @BeforeEach
    void setUp() {
        cat = new Cat();
        cat.setId(1L);
        cat.setName("cat1");
        cat.setBreed(Breed.SPHYNX);

        owner = new Owner();
        owner.setId(1L);
        owner.setName("owner1");
        owner.setCity("city");

        cat.setOwner(owner);

        show = new Show();
        show.setCity("city");

    }

    @Test
    void createCat() throws OwnerNotFoundException {
        when(catRepository.save(any(Cat.class))).thenReturn(cat);
        when(ownerService.getOwner(1L)).thenReturn(owner);
        when(ownerService.getOwner(2L)).thenThrow(new OwnerNotFoundException("Owner not found with id: 2"));
        doNothing().when(showService).addPotentialCatsToShowsByCity(any(Cat.class));

        Cat createdCat = catService.createCat("cat1", Breed.SPHYNX, 1L);

        assertEquals("cat1", createdCat.getName());
        assertEquals(Breed.SPHYNX, createdCat.getBreed());
        assertEquals(1L, createdCat.getOwner().getId());

        try{
            Cat createdCatInvockedExeption = catService.createCat("cat2", Breed.SPHYNX, 2L);
        }catch(OwnerNotFoundException e){
            assertEquals("Owner not found with id: 2", e.getMessage());
        }
    }

    @Test
    void getCatById() throws ShowNotFoundException, CatNotFoundException, OwnerNotFoundException {
        when(fromOptional.objectFromOptional(Cat.class, 1L)).thenReturn(cat);
        Cat getCatById = catService.getCatById(1L);

        assertEquals("cat1", getCatById.getName());
        assertEquals(Breed.SPHYNX, getCatById.getBreed());
        assertEquals(1L, getCatById.getOwner().getId());
    }

    @Test
    void getAllCats() {
        when(catRepository.findAll()).thenReturn(List.of(cat));
        List<Cat> getAllCats = catService.getAllCats();
        assertEquals(1, getAllCats.size());
        assertEquals("cat1", getAllCats.get(0).getName());
    }

    @Test
    void updateCat() throws ShowNotFoundException, CatNotFoundException, OwnerNotFoundException {
        when(fromOptional.objectFromOptional(Cat.class, 1L)).thenReturn(cat);
        when(fromOptional.objectFromOptional(Cat.class, 2L)).thenThrow(new CatNotFoundException("Cat not found with id: 2"));
        when(ownerService.getOwner(1L)).thenReturn(owner);
        when(catRepository.save(any(Cat.class))).thenReturn(cat);

        Cat updatedCat = catService.updateCat(1L, "new Name", Breed.SPHYNX, 1L);
        assertEquals("new Name", updatedCat.getName());
        assertEquals(Breed.SPHYNX, updatedCat.getBreed());
        assertEquals(1L, updatedCat.getOwner().getId());

        try{
            Cat failedToFindCat = catService.updateCat(2L, "new Name", Breed.SPHYNX, 2L);
        }catch (CatNotFoundException e){
            assertEquals("Cat not found with id: 2", e.getMessage());
        }
    }

    @Test
    void deleteCat() throws CatNotFoundException {
        when(catRepository.existsById(cat.getId())).thenReturn(true);
        when(catRepository.existsById(2L)).thenReturn(false);
        doNothing().when(catRepository).deleteById(cat.getId());

        catService.deleteCat(1L);
        assertDoesNotThrow(() -> catService.deleteCat(1L));

        try{
            catService.deleteCat(2L);
        }catch (CatNotFoundException e){
            assertEquals("Cat with id 2 not found", e.getMessage());
        }

    }

    @Test
    void deleteAll() {
        doNothing().when(catRepository).deleteAll();
        assertDoesNotThrow(() -> catService.deleteAll());
    }

    @Test
    void getShowByCatId() throws ShowNotFoundException, CatNotFoundException, OwnerNotFoundException {

        cat.setParticipatedShows(Set.of(show));

        when(fromOptional.objectFromOptional(Cat.class, cat.getId())).thenReturn(cat);

        Set<Show> shows = catService.getShowByCatId(1L);

        assertEquals(1, shows.size());
        assertEquals("city", shows.iterator().next().getCity());
    }

    @Test
    void allCats() {
        when(catRepository.findAll()).thenReturn(List.of(cat));
        List<Cat> getAllCats = catService.allCats();
        assertEquals(1, getAllCats.size());
        assertEquals("cat1", getAllCats.get(0).getName());
    }
}