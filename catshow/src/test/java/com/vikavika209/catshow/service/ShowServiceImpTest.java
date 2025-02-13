package com.vikavika209.catshow.service;

import com.vikavika209.catshow.exception.CatNotFoundException;
import com.vikavika209.catshow.exception.OwnerNotFoundException;
import com.vikavika209.catshow.exception.ShowNotFoundException;
import com.vikavika209.catshow.model.Breed;
import com.vikavika209.catshow.model.Cat;
import com.vikavika209.catshow.model.Owner;
import com.vikavika209.catshow.model.Show;
import com.vikavika209.catshow.repository.CatRepository;
import com.vikavika209.catshow.repository.ShowRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ShowServiceImpTest {
    @Mock
    private FromOptional fromOptional;

    @Mock
    private ShowRepository showRepository;

    @Mock
    private CatRepository catRepository;

    @InjectMocks
    private ShowServiceImp showService;

    private Show show;
    private Cat cat;
    private Owner owner;
    private Date showDate;

    @BeforeEach
    void setUp() throws ParseException {
        owner = new Owner();
        owner.setId(1L);
        owner.setCity("city");

        cat = new Cat();
        cat.setName("Barsik");
        cat.setBreed(Breed.SPHYNX);
        cat.setOwner(owner);

        show = new Show();
        show.setCity("city");
        show.setAddress("address");
        showDate = new SimpleDateFormat("dd-MM-yyyy").parse("02-02-2025");
        show.setDate(showDate);
        show.setPotentialParticipants(new HashSet<>(List.of(cat)));
    }

    @Test
    void createShow() {
        when(showRepository.save(any(Show.class))).thenReturn(show);

        Show createdShow = showService.createShow("city", showDate, "address");

        assertNotNull(createdShow);
        assertEquals("city", createdShow.getCity());
        assertEquals("address", createdShow.getAddress());
        assertEquals(showDate, createdShow.getDate());
    }

    @Test
    void updateShow() throws ShowNotFoundException, CatNotFoundException, OwnerNotFoundException {
        when(fromOptional.objectFromOptional(Show.class, show.getId())).thenReturn(show);
        when(fromOptional.objectFromOptional(Show.class, 2L)).thenThrow(new ShowNotFoundException("Show not found with id: 2"));

        Show updatedShow = showService.updateShow(show.getId(), "new city", showDate, "address");

        assertNotNull(updatedShow);
        assertEquals("new city", updatedShow.getCity());
        assertEquals("address", updatedShow.getAddress());

        try{
            showService.updateShow(2L, "new city", showDate, "address");
        }catch(ShowNotFoundException e){
            assertEquals("Show not found with id: 2", e.getMessage());
        }
    }

    @Test
    void getAllShow() {
        when(showRepository.findAll()).thenReturn(List.of(show));
        List<Show> shows = showService.getAllShow();
        assertNotNull(shows);
        assertEquals(1, shows.size());
        assertEquals(show, shows.get(0));
    }

    @Test
    void getPotentialParticipantsByCity() {
        when(catRepository.findAll()).thenReturn(List.of(cat));

        Set<Cat> potentialParticipants = showService.getPotentialParticipantsByCity("city");
        assertNotNull(potentialParticipants);
        assertEquals(1, potentialParticipants.size());
        assertEquals("Barsik", potentialParticipants.iterator().next().getName());
        assertEquals("Сфинкс", potentialParticipants.iterator().next().getBreed().getDisplayName());
    }

    @Test
    void deleteShow() throws ShowNotFoundException, CatNotFoundException, OwnerNotFoundException {
        when(fromOptional.objectFromOptional(Show.class, show.getId())).thenReturn(show);
        doNothing().when(showRepository).delete(show);
        assertDoesNotThrow(() -> showService.deleteShow(show.getId()));
    }

    @Test
    void addPotentialCatsToShowsByCity() {
        Cat newCat = new Cat();
        newCat.setOwner(owner);

        when(showRepository.findAll()).thenReturn(List.of(show));

        showService.addPotentialCatsToShowsByCity(newCat);

        assertEquals(2, show.getPotentialParticipants().size());
    }

    @Test
    void addParticipant() throws ShowNotFoundException, CatNotFoundException, OwnerNotFoundException {

        Cat newCat = new Cat();
        newCat.setOwner(owner);
        show.setParticipants(new HashSet<>(List.of(newCat)));

        when(fromOptional.objectFromOptional(Cat.class, cat.getId())).thenReturn(cat);
        when(fromOptional.objectFromOptional(Show.class, show.getId())).thenReturn(show);

        showService.addParticipant(cat.getId(), show.getId());

        assertEquals(2, show.getParticipants().size());
        assertTrue(show.getPotentialParticipants().isEmpty());

    }

    @Test
    void deleteAll() {
        doNothing().when(showRepository).deleteAll();
        assertDoesNotThrow(() -> showService.deleteAll());
    }

    @Test
    void getShowById() throws ShowNotFoundException, CatNotFoundException, OwnerNotFoundException {
        when(fromOptional.objectFromOptional(Show.class, show.getId())).thenReturn(show);
        when(fromOptional.objectFromOptional(Show.class, 100L)).thenThrow(new ShowNotFoundException("Show not found with id: 100"));

        Show getShowById = showService.getShowById(show.getId());

        assertNotNull(getShowById);
        assertEquals("city", getShowById.getCity());
        assertEquals("address", getShowById.getAddress());

        try{
            Show notExistShow = showService.getShowById(100L);
        }catch(ShowNotFoundException e){
            assertEquals("Show not found with id: 100", e.getMessage());
        }
    }
}