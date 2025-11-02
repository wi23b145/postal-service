package org.example.controller;

import org.example.entities.LetterEntity;
import org.example.entities.PackageEntity;
import org.example.repo.LetterRepository;
import org.example.repo.PackageRepository;
import org.example.service.MessagePublisher;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ApiController.class)
class ApiControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    LetterRepository letterRepo;

    @MockBean
    PackageRepository packageRepo;

    @MockBean
    MessagePublisher publisher;

    @Test
    void sendLetter_normalizes_andPublishes() throws Exception {
        mvc.perform(post("/api/letter/{country}/{name}", "  at  ", "  Alice  "))
                .andExpect(status().isOk());

        var capt = ArgumentCaptor.forClass(LetterEntity.class);
        verify(letterRepo).save(capt.capture());

        assertEquals("AT",   capt.getValue().getCountry()); // trim + uppercase
        assertEquals("Alice",capt.getValue().getName());
        verify(publisher).publishLetter(capt.getValue().getId());
    }

    @Test
    void sendPackage_persistsWeight_andPublishes() throws Exception {
        mvc.perform(post("/api/package/{weight}/{name}", "2.50", "Box"))
                .andExpect(status().isOk());

        var capt = ArgumentCaptor.forClass(PackageEntity.class);
        verify(packageRepo).save(capt.capture());

        assertEquals(new BigDecimal("2.50"), capt.getValue().getWeightKg());
        verify(publisher).publishPackage(capt.getValue().getId());
    }
}
