package com.example.cityexplorer.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MessageController.class)
public class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getWeatherData_ShouldReturnSuccessStatus() throws Exception {
        String city = "Raleigh";

        mockMvc.perform(get("/api/" + city)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()); // check for HTTP 200 OK status
    }
}
