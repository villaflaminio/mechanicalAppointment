package it.mtempobono.mechanicalappointment.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import it.mtempobono.mechanicalappointment.model.dto.VehicleDto;
import it.mtempobono.mechanicalappointment.model.entity.Vehicle;
import it.mtempobono.mechanicalappointment.repository.VehicleRepository;
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
public class VehiclesControllerTest {
    @Autowired
    private VehicleRepository repository;

    @Autowired
    private MockMvc mockMvc;
    JSONParser parser = new JSONParser();


    @Test
    @SqlGroup({
            @Sql(value = "classpath:init/clean.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:init/data_init.sql", executionPhase = BEFORE_TEST_METHOD)
    })
    void should_create_one_vehicle() throws Exception {
        final File jsonFile = new ClassPathResource("mockData/vehicle/createVehicle.json").getFile();
        final String vehicleToCreate = Files.readString(jsonFile.toPath());

        MvcResult result = this.mockMvc.perform(post("/api/vehicles")
                        .contentType(APPLICATION_JSON)
                        .content(vehicleToCreate))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        JSONObject jsonObject = (JSONObject) parser.parse(content);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        VehicleDto createVehicle = mapper.readValue(jsonObject.toString(), VehicleDto.class);

        Vehicle vehicle = repository.findById(Long.parseLong(jsonObject.get("id").toString())).get();

        List<String> compareGetters = new ArrayList<>();
        compareGetters.add("plate");
        compareGetters.add("model");
        compareGetters.add("brand");
        compareGetters.add("year");
        compareGetters.add("fuel");
        compareGetters.add("isActive");

        List<String> vehicleDifferences = MyReflectionTestUtils.compareGetters(vehicle, createVehicle, compareGetters);
        assertThat(vehicleDifferences).isEmpty();

        repository.delete(vehicle);
    }


    @Test
    @SqlGroup({
            @Sql(value = "classpath:init/clean.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:init/data_init.sql", executionPhase = BEFORE_TEST_METHOD)
    })
    void should_update_one_vehicle() throws Exception {
        final File jsonFile = new ClassPathResource("mockData/vehicle/createVehicle.json").getFile();
        final String vehicleToCreate = Files.readString(jsonFile.toPath());

        MvcResult resultVehicleCreation = this.mockMvc.perform(post("/api/vehicles")
                        .contentType(APPLICATION_JSON)
                        .content(vehicleToCreate))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        JSONObject jsonResultVehicleCreation = (JSONObject) parser.parse(resultVehicleCreation.getResponse().getContentAsString());
        Long vehicleId = Long.parseLong(jsonResultVehicleCreation.get("id").toString());
        Vehicle oldVehicle = repository.findById(vehicleId).orElseThrow(() -> new Exception("Vehicle not found"));

        final File updateVehicle = new ClassPathResource("mockData/vehicle/updateVehicle.json").getFile();
        final String VehicleToUpdate = Files.readString(updateVehicle.toPath());

        MvcResult result = this.mockMvc.perform(put("/api/vehicles/" + vehicleId)
                        .contentType(APPLICATION_JSON)
                        .content(VehicleToUpdate))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        JSONObject jsonObject = (JSONObject) parser.parse(content);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        VehicleDto modifyVehicleDtoRequest = mapper.readValue(jsonObject.toString(), VehicleDto.class);

        Vehicle vehicle = repository.findById(Long.parseLong(jsonObject.get("id").toString())).get();

        List<String> compareGetters = new ArrayList<>();
        compareGetters.add("plate");
        compareGetters.add("model");
        compareGetters.add("brand");
        compareGetters.add("year");
        compareGetters.add("fuel");
        compareGetters.add("isActive");

        List<String> vehicleDifferences = MyReflectionTestUtils.compareGetters(vehicle, modifyVehicleDtoRequest, compareGetters);
        assertThat(vehicleDifferences).isEmpty();
        vehicleDifferences = MyReflectionTestUtils.compareGetters(vehicle, oldVehicle, compareGetters);
        assertThat(vehicleDifferences).hasSize(6);

        vehicleDifferences.clear();
        compareGetters.clear();

        repository.delete(vehicle);
    }


    @Test
    @SqlGroup({
            @Sql(value = "classpath:init/clean.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:init/data_init.sql", executionPhase = BEFORE_TEST_METHOD)
    })
    void should_get_all_vehicles() throws Exception {
        final File jsonFile = new ClassPathResource("mockData/vehicle/createVehicle.json").getFile();
        final String vehicleToCreate = Files.readString(jsonFile.toPath());

        MvcResult resultVehicleCreation = this.mockMvc.perform(post("/api/vehicles")
                        .contentType(APPLICATION_JSON)
                        .content(vehicleToCreate))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        JSONObject jsonResultVehicleCreation = (JSONObject) parser.parse(resultVehicleCreation.getResponse().getContentAsString());
        Long vehicleId = Long.parseLong(jsonResultVehicleCreation.get("id").toString());


        MvcResult resultGet = this.mockMvc.perform(get("/api/vehicles")
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

        List<VehicleDto> vehicleDtos = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            vehicleDtos.add(mapper.readValue(jsonArray.get(i).toString(), VehicleDto.class));
        }

        int lenght = (int) repository.count();
        assertThat(vehicleDtos).hasSize(lenght);

    }

    @Test
    @SqlGroup({
            @Sql(value = "classpath:init/clean.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:init/data_init.sql", executionPhase = BEFORE_TEST_METHOD)
    })
    void should_delete_one_vehicle_by_id() throws Exception {
        final File jsonFile = new ClassPathResource("mockData/vehicle/createVehicle.json").getFile();
        final String vehicleToCreate = Files.readString(jsonFile.toPath());

        MvcResult resultVehicleCreation = this.mockMvc.perform(post("/api/vehicles")
                        .contentType(APPLICATION_JSON)
                        .content(vehicleToCreate))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        JSONObject jsonResultVehicleCreation = (JSONObject) parser.parse(resultVehicleCreation.getResponse().getContentAsString());
        Long vehicleId = Long.parseLong(jsonResultVehicleCreation.get("id").toString());

        Vehicle vehicle = repository.findById(vehicleId).get();

        MvcResult resultGet = this.mockMvc.perform(delete("/api/vehicles/" + vehicle.getId())
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();


        Optional<Vehicle> vehicleOptional = repository.findById(vehicle.getId());
        assertThat(vehicleOptional).isEmpty();
    }

    @Test
    @SqlGroup({
            @Sql(value = "classpath:init/clean.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:init/data_init.sql", executionPhase = BEFORE_TEST_METHOD)
    })
    void should_get_vehicles_by_user_id() throws Exception {
        final File jsonFile = new ClassPathResource("mockData/vehicle/createVehicle.json").getFile();
        final String vehicleToCreate = Files.readString(jsonFile.toPath());

        MvcResult resultVehicleCreation = this.mockMvc.perform(post("/api/vehicles")
                        .contentType(APPLICATION_JSON)
                        .content(vehicleToCreate))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        JSONObject jsonResultVehicleCreation = (JSONObject) parser.parse(resultVehicleCreation.getResponse().getContentAsString());
        Long vehicleId = Long.parseLong(jsonResultVehicleCreation.get("id").toString());

        Vehicle vehicle = repository.findById(vehicleId).get();

        MvcResult resultGet = this.mockMvc.perform(get("/api/vehicles/user/" + vehicle.getUser().getId())
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

        List<VehicleDto> timePeriods = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            timePeriods.add(mapper.readValue(jsonArray.get(i).toString(), VehicleDto.class));
        }
        assertThat(timePeriods).isNotEmpty();
    }

}
