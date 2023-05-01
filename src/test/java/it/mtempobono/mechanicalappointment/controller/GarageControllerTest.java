package it.mtempobono.mechanicalappointment.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import it.mtempobono.mechanicalappointment.model.dto.GarageDto;
import it.mtempobono.mechanicalappointment.model.entity.Garage;
import it.mtempobono.mechanicalappointment.repository.GarageRepository;
import it.mtempobono.mechanicalappointment.utils.MyReflectionTestUtils;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ActiveProfiles("test")
class GarageControllerTest {

    @Autowired
    private GarageRepository repository;

    @Autowired
    private MockMvc mockMvc;
    JSONParser parser = new JSONParser();




//    @Test
//    void should_create_one_user() throws Exception {
//        final File jsonFile = new ClassPathResource("init/idea.json").getFile();
//        final String ideaToCreate = Files.readString(jsonFile.toPath());
//
//        this.mockMvc.perform(post("/api/ideas")
//                        .contentType(APPLICATION_JSON)
//                        .content(ideaToCreate))
//                .andDo(print())
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$").isMap());
//
//        assertThat(this.repository.findAll()).hasSize(13);
//
//    }
//

    //test create company
    @Test
    void should_create_one_garage() throws Exception {
        final File jsonFile = new ClassPathResource("mockData/garage/createGarage.json").getFile();
        final String companyToCreate = Files.readString(jsonFile.toPath());

        MvcResult result =  this.mockMvc.perform(post("/api/garages")
                        .contentType(APPLICATION_JSON)
                        .content(companyToCreate))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        JSONObject jsonObject = (JSONObject) parser.parse(content);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        GarageDto createGarage = mapper.readValue(jsonObject.toString(), GarageDto.class);

        Garage garage = repository.findById(Long.parseLong(jsonObject.get("id").toString())).get();

        List<String> compareGetters = new ArrayList<>();
        compareGetters.add("name");
        compareGetters.add("address");
        compareGetters.add("cap");
        compareGetters.add("linkGoogleMaps");
        compareGetters.add("latitude");
        compareGetters.add("longitude");
        compareGetters.add("phone");
        compareGetters.add("email");
        compareGetters.add("website");
        compareGetters.add("logo");

        List<String> companyDifferences = MyReflectionTestUtils.compareGetters(garage, createGarage, compareGetters);
        assertThat(companyDifferences).isEmpty();

        repository.delete(garage);
    }
}