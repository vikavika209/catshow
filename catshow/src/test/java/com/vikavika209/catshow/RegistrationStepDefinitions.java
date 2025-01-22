package com.vikavika209.catshow;

import ch.qos.logback.core.Appender;
import com.vikavika209.catshow.controller.OwnerController;
import com.vikavika209.catshow.service.OwnerService;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.nalbion.TestAppender;
import org.junit.jupiter.api.Assertions;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;


import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class RegistrationStepDefinitions {

    private MockMvc mockMvc;
    OwnerService ownerService;
    private MvcResult mvcResult;

    @Given("пользователь заполнил форму регистрации с валидными данными")
    public void fillInWithCorrectData() throws Exception {

    }

    @Given("пользователь заполнил форму регистрации с некорректными данными")
    public void fillInWithIncorrectData() {
    }

    @Given("происходит ошибка при сохранении пользователя")
    public void errorOccursWhileUserSaving() throws Exception {
        ownerService = Mockito.mock(OwnerService.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new OwnerController(ownerService)).build();

        doThrow(new RuntimeException("Ошибка базы данных"))
                .when(this.ownerService).createOwner(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    @When("пользователь отправляет форму регистрации")
    public void userSendRegistrationForm() throws Exception {
        ownerService = Mockito.mock(OwnerService.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new OwnerController(ownerService)).build();

        mvcResult = mockMvc.perform(post("/submit_registration")
                        .param("name", "Test User")
                        .param("email", "test@example.com")
                        .param("password", "123456")
                        .param("city", "TestCity"))
                .andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());
    }

    @Then("пользователь должен быть перенаправлен на страницу успешной регистрации")
    public void successfulRegistration() throws Exception {

        Assertions.assertEquals("registration-success",
                Objects.requireNonNull(mvcResult.getModelAndView()).getViewName());
    }

    @Then("пользователь остаётся на странице регистрации")
    public void userOnTheRegistrationPage() throws Exception {
        ownerService = Mockito.mock(OwnerService.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new OwnerController(ownerService)).build();

        mvcResult = this.mockMvc.perform(post("/submit_registration"))
                        .andReturn();
        Assertions.assertEquals("registration", Objects.requireNonNull(mvcResult.getModelAndView()).getViewName());
    }
}
