package com.vikavika209.catshow.service;

import com.vikavika209.catshow.exception.CatNotFoundException;
import com.vikavika209.catshow.exception.OwnerNotFoundException;
import com.vikavika209.catshow.exception.ShowNotFoundException;
import com.vikavika209.catshow.model.*;
import com.vikavika209.catshow.repository.CatRepository;
import com.vikavika209.catshow.repository.OwnerRepository;
import com.vikavika209.catshow.repository.ShowRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OwnerServiceImpTest {

    @Mock
    private OwnerRepository ownerRepository;

    @Mock
    private CatRepository catRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ShowRepository showRepository;

    @Mock
    private FromOptional fromOptional;

    @Mock
    Authentication authentication;

    @Mock
    UserDetails userDetails;

    @InjectMocks
    private OwnerServiceImp ownerService;

    @InjectMocks
    private ShowServiceImp showService;

    private Owner owner;
    private Cat cat;
    private Show show;

    @BeforeEach
    void setUp() throws ParseException, ShowNotFoundException, CatNotFoundException, OwnerNotFoundException {
        owner = new Owner();
        owner.setId(1L);
        owner.setName("test");
        owner.setUsername("username");
        owner.setPassword("password");
        owner.setCity("city");
        owner.setRoles(new HashSet<>(List.of(Role.USER)));

        cat = new Cat();
        cat.setId(1L);
        cat.setName("Barsik");
        cat.setBreed(Breed.MAINECOON);

        List<Cat> cats = new ArrayList<>();
        cats.add(cat);

        owner.setCats(cats);

        show = new Show();
        show.setCity("city");
        show.setAddress("address");
        show.setDate(new SimpleDateFormat("dd-MM-yyyy").parse("02-02-2025"));
        show.setParticipants(new HashSet<>(cats));

        when(catRepository.findCatsByCity("city")).thenReturn(Collections.singletonList(cat));
        showService.createShow(show.getCity(), show.getDate(), show.getAddress());

        cat.setParticipatedShows(new HashSet<>(List.of(show)));
    }

    @Test
    void createOwner() {
        when(ownerRepository.save(any(Owner.class))).thenReturn(owner);
        Owner createdOwner = ownerService.createOwner(owner.getName(), owner.getUsername(), "password", owner.getCity());
        String encodedPassword = passwordEncoder.encode("password");

        assertNotNull(createdOwner);
        assertEquals("test", createdOwner.getName());
        assertEquals("username", createdOwner.getUsername());
        assertEquals("city", createdOwner.getCity());
        assertEquals(encodedPassword, createdOwner.getPassword());
    }

    @Test
    void getOwner() throws OwnerNotFoundException {
        when(ownerRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(ownerRepository.findById(2L)).thenReturn(Optional.empty());

        Owner foundOwner = ownerService.getOwner(1L);
        assertNotNull(foundOwner);
        assertEquals(1L, foundOwner.getId());

        try{
            ownerService.getOwner(2L);
        }catch (OwnerNotFoundException e){
            assertEquals("Owner not found with id: 2", e.getMessage());
        }
    }

    @Test
    void getAllOwners() {
        when(ownerRepository.findAll()).thenReturn(Collections.singletonList(owner));
        assertFalse(ownerService.getAllOwners().isEmpty());
    }

    @Test
    void getCatsOfTheOwnerById() throws OwnerNotFoundException {
        when(ownerRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(ownerRepository.findById(2L)).thenReturn(Optional.empty());

        Set<Cat> cats = ownerService.getCatsOfTheOwnerById(1L);
        assertEquals(1, cats.size());
        assertEquals("Barsik", cats.iterator().next().getName());

        try{
            ownerService.getOwner(2L);
        }catch (OwnerNotFoundException e){
            assertEquals("Owner not found with id: 2", e.getMessage());
        }
    }

    @Test
    void getAllShowsByOwnerId() throws ShowNotFoundException, CatNotFoundException, OwnerNotFoundException {
        when(fromOptional.objectFromOptional(Owner.class, 1L)).thenReturn(owner);
        when(fromOptional.objectFromOptional(Owner.class, 2L)).thenThrow(new OwnerNotFoundException("Owner not found with id: 2"));

        Set<Show> shows = ownerService.getAllShowsByOwnerId(owner.getId());
        assertEquals(1, shows.size());
        assertEquals("address", shows.iterator().next().getAddress());

        try{
            ownerService.getAllShowsByOwnerId(2L);
        }catch (OwnerNotFoundException e){
            assertEquals("Owner not found with id: 2", e.getMessage());
        }

    }

    @Test
    void updateOwner() throws ShowNotFoundException, CatNotFoundException, OwnerNotFoundException {
        when(fromOptional.objectFromOptional(Owner.class, 1L)).thenReturn(owner);

        Owner updatedOwner = ownerService.updateOwner(1L, "new Name", "new Username", "new Password", "new City");
        assertEquals("new Name", updatedOwner.getName());
        assertEquals("new Username", updatedOwner.getUsername());
        assertEquals(passwordEncoder.encode("new Password"), updatedOwner.getPassword());
        assertEquals("new City", updatedOwner.getCity());
        assertEquals("Barsik", updatedOwner.getCats().get(0).getName());
    }

    @Test
    void deleteOwner() {
        doNothing().when(ownerRepository).deleteById(1L);
        assertDoesNotThrow(() -> ownerService.deleteOwner(1L));
    }

    @Test
    void deleteAll() {
        doNothing().when(ownerRepository).deleteAll();
        assertDoesNotThrow(() -> ownerService.deleteAll());
    }

    @Test
    void loadUserByUsername() {
        when(ownerRepository.findByUsername("username")).thenReturn(Optional.of(owner));
        UserDetails userDetails = ownerService.loadUserByUsername("username");
        assertEquals("username", userDetails.getUsername());
        assertNull(userDetails.getPassword());
    }

    @Test
    void getCurrentOwner() throws OwnerNotFoundException {

        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("username");
        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);

        when(ownerRepository.findByUsername("username")).thenReturn(Optional.ofNullable(owner));

        Owner foundOwner = ownerService.getCurrentOwner();

        assertNotNull(foundOwner);
        assertEquals("username", foundOwner.getUsername());
        assertEquals("password", foundOwner.getPassword());
    }

    @Test
    void setOwnerRole() throws ShowNotFoundException, CatNotFoundException, OwnerNotFoundException {
        when(ownerRepository.findById(1L)).thenReturn(Optional.of(owner));

        Owner ownerAdmin = ownerService.setOwnerRole(1L, Role.ADMIN);
        assertNotNull(ownerAdmin);
        assertEquals("username", ownerAdmin.getUsername());
        assertEquals("password", ownerAdmin.getPassword());
        assertEquals("[USER, ADMIN]", ownerAdmin.getRoles().toString());
    }
}