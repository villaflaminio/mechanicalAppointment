package it.mtempobono.mechanicalappointment.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import it.mtempobono.mechanicalappointment.model.dto.PlaceDto;
import it.mtempobono.mechanicalappointment.model.entity.Place;
import it.mtempobono.mechanicalappointment.repository.PlaceRepository;
import it.mtempobono.mechanicalappointment.utils.MyReflectionTestUtils;
import net.minidev.json.JSONArray;
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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ActiveProfiles("test")
public class PlacesControllerTest {
    @Autowired
    private PlaceRepository repository;

    @Autowired
    private MockMvc mockMvc;
    JSONParser parser = new JSONParser();


    @Test
    @SqlGroup({
            @Sql(value = "classpath:init/clean.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:init/data_init.sql", executionPhase = BEFORE_TEST_METHOD)
    })
    void should_get_all_places() throws Exception {

        MvcResult resultGet = this.mockMvc.perform(get("/api/places")
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        String contentGet = resultGet.getResponse().getContentAsString();
        JSONArray jsonArray = (JSONArray) parser.parse(contentGet);

        List<PlaceDto> placeDtos = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            placeDtos.add(mapper.readValue(jsonArray.get(i).toString(), PlaceDto.class));
        }

        int lenght = 10;
        assertThat(placeDtos).hasSize(lenght);

    }


    @Test
    @SqlGroup({
            @Sql(value = "classpath:init/clean.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:init/data_init.sql", executionPhase = BEFORE_TEST_METHOD)
    })
    void should_get_one_place_by_id() throws Exception {

        MvcResult resultGet = this.mockMvc.perform(get("/api/places/" + 10)
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        Place place = repository.findById(10L).get();
        String contentGet = resultGet.getResponse().getContentAsString();
        JSONObject jsonResult = (JSONObject) parser.parse(contentGet);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        PlaceDto placeDto = mapper.readValue(jsonResult.toString(), PlaceDto.class);

        List<String> compareGetters = new ArrayList<>();
        compareGetters.add("istat");
        compareGetters.add("municipality");
        compareGetters.add("province");
        compareGetters.add("region");

        List<String> placeDifferences = MyReflectionTestUtils.compareGetters(place, placeDto, compareGetters);
        assertThat(placeDifferences).isEmpty();
    }

    @Test
    @SqlGroup({
            @Sql(value = "classpath:init/clean.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:init/data_init.sql", executionPhase = BEFORE_TEST_METHOD)
    })
    void should_delete_one_place_by_id() throws Exception {

        MvcResult resultGet = this.mockMvc.perform(delete("/api/places/" + 1)
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();


        Optional<Place> placeOptional = repository.findById(1l);
        assertThat(placeOptional).isEmpty();
    }


    @Test
    @SqlGroup({
            @Sql(value = "classpath:init/clean.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:init/data_init.sql", executionPhase = BEFORE_TEST_METHOD)
    })
    void should_retrive_place_from_PlaceProvinceStartsWith() throws Exception {
        Place place = repository.findById(10L).get();

        MvcResult resultGet = this.mockMvc.perform(get("/api/places/findPlaceByProvinceStartsWith")
                        .contentType(APPLICATION_JSON)
                        .param("province", place.getProvince()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String contentGet = resultGet.getResponse().getContentAsString();
        JSONArray jsonArray = (JSONArray) parser.parse(contentGet);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        List<PlaceDto> places = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            places.add(mapper.readValue(jsonArray.get(i).toString(), PlaceDto.class));
        }

        assertThat(places).hasSize(12);

    }


    @Test
    @SqlGroup({
            @Sql(value = "classpath:init/clean.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:init/data_init.sql", executionPhase = BEFORE_TEST_METHOD)
    })
    void should_retrive_place_from_PlaceByRegionStartsWith() throws Exception {

        Place place = repository.findById(10L).get();

        MvcResult resultGet = this.mockMvc.perform(get("/api/places/findPlaceByRegionStartsWith")
                        .contentType(APPLICATION_JSON)
                        .param("region", place.getRegion()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String contentGet = resultGet.getResponse().getContentAsString();
        JSONArray jsonArray = (JSONArray) parser.parse(contentGet);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        List<PlaceDto> places = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            places.add(mapper.readValue(jsonArray.get(i).toString(), PlaceDto.class));
        }

        assertThat(places).hasSize(390);
    }


    @Test
    @SqlGroup({
            @Sql(value = "classpath:init/clean.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:init/data_init.sql", executionPhase = BEFORE_TEST_METHOD)
    })
    void should_retrive_place_from_PlaceMunicipalityStartsWith() throws Exception {

        Place place = repository.findById(10L).get();
        MvcResult resultGet = this.mockMvc.perform(get("/api/places/findPlaceByMunicipalityStartsWith")
                        .contentType(APPLICATION_JSON)
                        .param("municipality", place.getMunicipality()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String contentGet = resultGet.getResponse().getContentAsString();
        JSONArray jsonArray = (JSONArray) parser.parse(contentGet);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        List<PlaceDto> places = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            places.add(mapper.readValue(jsonArray.get(i).toString(), PlaceDto.class));
        }

        assertThat(places).hasSize(1);

    }
}
