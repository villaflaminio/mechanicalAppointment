package it.mtempobono.mechanicalappointment.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import it.mtempobono.mechanicalappointment.model.dto.GarageDto;
import it.mtempobono.mechanicalappointment.model.entity.Garage;
import it.mtempobono.mechanicalappointment.model.entity.Place;
import it.mtempobono.mechanicalappointment.repository.GarageRepository;
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
    private PlaceRepository placeRepository;


    @Autowired
    private MockMvc mockMvc;
    JSONParser parser = new JSONParser();


    @Test
    @SqlGroup({
            @Sql(value = "classpath:init/clean.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:init/data_init.sql", executionPhase = BEFORE_TEST_METHOD)
    })
    void should_create_one_garage() throws Exception {
        final File jsonFile = new ClassPathResource("mockData/garage/createGarage.json").getFile();
        final String companyToCreate = Files.readString(jsonFile.toPath());

        MvcResult result = this.mockMvc.perform(post("/api/garages")
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


    @Test
    @SqlGroup({
            @Sql(value = "classpath:init/clean.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:init/data_init.sql", executionPhase = BEFORE_TEST_METHOD)
    })
    void should_update_one_garage() throws Exception {

        final File createCompany = new ClassPathResource("mockData/garage/createGarage.json").getFile();
        final String companyToCreate = Files.readString(createCompany.toPath());

        MvcResult resultCompanyCreation = this.mockMvc.perform(post("/api/garages")
                        .contentType(APPLICATION_JSON)
                        .content(companyToCreate))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        JSONObject jsonResultCompanyCreation = (JSONObject) parser.parse(resultCompanyCreation.getResponse().getContentAsString());

        Long garageId = Long.parseLong(jsonResultCompanyCreation.get("id").toString());
        Garage oldGarage = repository.findById(garageId).orElseThrow(() -> new Exception("Garage not found"));

        final File updateGarage = new ClassPathResource("mockData/garage/updateGarage.json").getFile();
        final String GarageToUpdate = Files.readString(updateGarage.toPath());

        MvcResult result = this.mockMvc.perform(put("/api/garages/" + garageId)
                        .contentType(APPLICATION_JSON)
                        .content(GarageToUpdate))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        JSONObject jsonObject = (JSONObject) parser.parse(content);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        GarageDto modifyGarageRequest = mapper.readValue(jsonObject.toString(), GarageDto.class);

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

        //compare with modifyCompanyRequest
        List<String> garageDifferences = MyReflectionTestUtils.compareGetters(garage, modifyGarageRequest, compareGetters);
        assertThat(garageDifferences).isEmpty();
        //compare with old company
        garageDifferences = MyReflectionTestUtils.compareGetters(garage, oldGarage, compareGetters);
        assertThat(garageDifferences).hasSize(10);

        garageDifferences.clear();
        compareGetters.clear();

        repository.delete(garage);
    }

    @Test
    @SqlGroup({
            @Sql(value = "classpath:init/clean.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:init/data_init.sql", executionPhase = BEFORE_TEST_METHOD)
    })
    void should_get_all_garages() throws Exception {
        final File jsonFile = new ClassPathResource("mockData/garage/createGarage.json").getFile();
        final String companyToCreate = Files.readString(jsonFile.toPath());

        MvcResult creation = this.mockMvc.perform(post("/api/garages")
                        .contentType(APPLICATION_JSON)
                        .content(companyToCreate))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String content = creation.getResponse().getContentAsString();
        JSONObject jsonObject = (JSONObject) parser.parse(content);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        Garage garage = repository.findById(Long.parseLong(jsonObject.get("id").toString())).get();


        MvcResult resultGet = this.mockMvc.perform(get("/api/garages")
                        .contentType(APPLICATION_JSON)
                        .content(companyToCreate))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();


        String contentGet = resultGet.getResponse().getContentAsString();
        JSONArray jsonArray = (JSONArray) parser.parse(contentGet);
        List<GarageDto> garages = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            garages.add(mapper.readValue(jsonArray.get(i).toString(), GarageDto.class));
        }

        int lenght = (int) repository.count();
        assertThat(garages).hasSize(lenght);

        repository.delete(garage);
    }


    @Test
    @SqlGroup({
            @Sql(value = "classpath:init/clean.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:init/data_init.sql", executionPhase = BEFORE_TEST_METHOD)
    })
    void should_get_one_garage_by_id() throws Exception {
        final File jsonFile = new ClassPathResource("mockData/garage/createGarage.json").getFile();
        final String companyToCreate = Files.readString(jsonFile.toPath());

        MvcResult creation = this.mockMvc.perform(post("/api/garages")
                        .contentType(APPLICATION_JSON)
                        .content(companyToCreate))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String content = creation.getResponse().getContentAsString();
        JSONObject jsonObject = (JSONObject) parser.parse(content);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        Garage garage = repository.findById(Long.parseLong(jsonObject.get("id").toString())).get();

        MvcResult resultGet = this.mockMvc.perform(get("/api/garages/" + garage.getId())
                        .contentType(APPLICATION_JSON)
                        .content(companyToCreate))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String contentGet = resultGet.getResponse().getContentAsString();
        JSONObject jsonResult = (JSONObject) parser.parse(contentGet);
        GarageDto garageDto = mapper.readValue(jsonResult.toString(), GarageDto.class);

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

        //compare with modifyCompanyRequest
        List<String> garageDifferences = MyReflectionTestUtils.compareGetters(garage, garageDto, compareGetters);
        assertThat(garageDifferences).isEmpty();

        repository.delete(garage);
    }

    @Test
    @SqlGroup({
            @Sql(value = "classpath:init/clean.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:init/data_init.sql", executionPhase = BEFORE_TEST_METHOD)
    })
    void should_delete_one_garage_by_id() throws Exception {
        final File jsonFile = new ClassPathResource("mockData/garage/createGarage.json").getFile();
        final String companyToCreate = Files.readString(jsonFile.toPath());

        MvcResult creation = this.mockMvc.perform(post("/api/garages")
                        .contentType(APPLICATION_JSON)
                        .content(companyToCreate))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String content = creation.getResponse().getContentAsString();
        JSONObject jsonObject = (JSONObject) parser.parse(content);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        Garage garage = repository.findById(Long.parseLong(jsonObject.get("id").toString())).get();

        MvcResult resultGet = this.mockMvc.perform(delete("/api/garages/" + garage.getId())
                        .contentType(APPLICATION_JSON)
                        .content(companyToCreate))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();


        Optional<Garage> garageOptional = repository.findById(garage.getId());
        assertThat(garageOptional).isEmpty();
    }

    @Test
    @SqlGroup({
            @Sql(value = "classpath:init/clean.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:init/data_init.sql", executionPhase = BEFORE_TEST_METHOD)
    })
    void should_retrive_garage_from_PlaceProvinceStartsWith() throws Exception {

        Place place = placeRepository.findById(1L).get();
        final File jsonFile = new ClassPathResource("mockData/garage/createGarage.json").getFile();
        final String companyToCreate = Files.readString(jsonFile.toPath());

        MvcResult creation = this.mockMvc.perform(post("/api/garages")
                        .contentType(APPLICATION_JSON)
                        .content(companyToCreate))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();



        MvcResult resultGet = this.mockMvc.perform(get("/api/garages/findGarageByPlaceProvinceStartsWith")
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

        List<GarageDto> garages = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            garages.add(mapper.readValue(jsonArray.get(i).toString(), GarageDto.class));
        }

        assertThat(garages).hasSize(1);

    }


    @Test
    @SqlGroup({
            @Sql(value = "classpath:init/clean.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:init/data_init.sql", executionPhase = BEFORE_TEST_METHOD)
    })
    void should_retrive_garage_from_PlaceRegionStartsWith() throws Exception {

        Place place = placeRepository.findById(1L).get();
        final File jsonFile = new ClassPathResource("mockData/garage/createGarage.json").getFile();
        final String companyToCreate = Files.readString(jsonFile.toPath());

        MvcResult creation = this.mockMvc.perform(post("/api/garages")
                        .contentType(APPLICATION_JSON)
                        .content(companyToCreate))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();



        MvcResult resultGet = this.mockMvc.perform(get("/api/garages/findGarageByPlaceRegionStartsWith")
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

        List<GarageDto> garages = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            garages.add(mapper.readValue(jsonArray.get(i).toString(), GarageDto.class));
        }

        assertThat(garages).hasSize(1);

    }


    @Test
    @SqlGroup({
            @Sql(value = "classpath:init/clean.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:init/data_init.sql", executionPhase = BEFORE_TEST_METHOD)
    })
    void should_retrive_garage_from_PlaceMunicipalityStartsWith() throws Exception {

        Place place = placeRepository.findById(1L).get();
        final File jsonFile = new ClassPathResource("mockData/garage/createGarage.json").getFile();
        final String companyToCreate = Files.readString(jsonFile.toPath());

        MvcResult creation = this.mockMvc.perform(post("/api/garages")
                        .contentType(APPLICATION_JSON)
                        .content(companyToCreate))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();



        MvcResult resultGet = this.mockMvc.perform(get("/api/garages/findGarageByPlaceMunicipalityStartsWith")
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

        List<GarageDto> garages = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            garages.add(mapper.readValue(jsonArray.get(i).toString(), GarageDto.class));
        }

        assertThat(garages).hasSize(1);

    }

}