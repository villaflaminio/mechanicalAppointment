package it.mtempobono.mechanicalappointment.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import it.mtempobono.mechanicalappointment.model.dto.OpenDayDto;
import it.mtempobono.mechanicalappointment.model.entity.OpenDay;
import it.mtempobono.mechanicalappointment.repository.OpenDayRepository;
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
public class OpenDaysControllerTest {
    @Autowired
    private OpenDayRepository repository;

    @Autowired
    private MockMvc mockMvc;
    JSONParser parser = new JSONParser();

    @Test
    @SqlGroup({
            @Sql(value = "classpath:init/clean.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:init/data_init.sql", executionPhase = BEFORE_TEST_METHOD)
    })    void should_create_one_openDay() throws Exception {
        final File jsonFile = new ClassPathResource("mockData/openDay/createOpenDay.json").getFile();
        final String companyToCreate = Files.readString(jsonFile.toPath());

        MvcResult result = this.mockMvc.perform(post("/api/opendays")
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

        OpenDayDto createOpenDay = mapper.readValue(jsonObject.toString(), OpenDayDto.class);

        OpenDay openDay = repository.findById(Long.parseLong(jsonObject.get("id").toString())).get();

        List<String> compareGetters = new ArrayList<>();
        compareGetters.add("date");
        compareGetters.add("maxParallelAppointments");

        List<String> companyDifferences = MyReflectionTestUtils.compareGetters(openDay, createOpenDay, compareGetters);
        assertThat(companyDifferences).isEmpty();
        compareGetters.clear();

        repository.delete(openDay);
    }

    @Test
    @SqlGroup({
            @Sql(value = "classpath:init/clean.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:init/data_init.sql", executionPhase = BEFORE_TEST_METHOD)
    })    void should_update_one_openDay() throws Exception {
        final File jsonFile = new ClassPathResource("mockData/openDay/createOpenDay.json").getFile();
        final String companyToCreate = Files.readString(jsonFile.toPath());

        MvcResult resultOpendayCreation = this.mockMvc.perform(post("/api/opendays")
                        .contentType(APPLICATION_JSON)
                        .content(companyToCreate))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        JSONObject jsonResultOpenDayCreation = (JSONObject) parser.parse(resultOpendayCreation.getResponse().getContentAsString());

        Long openDayId = Long.parseLong(jsonResultOpenDayCreation.get("id").toString());
        OpenDay oldOpenDay = repository.findById(openDayId).orElseThrow(() -> new Exception("OpenDay not found"));

        final File updateCompany = new ClassPathResource("mockData/openDay/updateOpenDay.json").getFile();
        final String companyToUpdate = Files.readString(updateCompany.toPath());

        MvcResult result = this.mockMvc.perform(put("/api/opendays/" + openDayId)
                        .contentType(APPLICATION_JSON)
                        .content(companyToUpdate))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        JSONObject jsonObject = (JSONObject) parser.parse(content);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        OpenDayDto createOpenDay = mapper.readValue(jsonObject.toString(), OpenDayDto.class);

        OpenDay openDay = repository.findById(Long.parseLong(jsonObject.get("id").toString())).get();

        List<String> compareGetters = new ArrayList<>();
        compareGetters.add("date");
        compareGetters.add("maxParallelAppointments");

        List<String> openDayDifferences = MyReflectionTestUtils.compareGetters(openDay, createOpenDay, compareGetters);
        assertThat(openDayDifferences).isEmpty();

        openDayDifferences = MyReflectionTestUtils.compareGetters(openDay, oldOpenDay, compareGetters);
        assertThat(openDayDifferences).hasSize(2);

        compareGetters.clear();

        repository.delete(openDay);
    }


    @Test
    @SqlGroup({
            @Sql(value = "classpath:init/clean.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:init/data_init.sql", executionPhase = BEFORE_TEST_METHOD)
    })
    void should_get_all_openDays() throws Exception {
        final File jsonFile = new ClassPathResource("mockData/openDay/createOpenDay.json").getFile();
        final String openDayToCreate = Files.readString(jsonFile.toPath());

        MvcResult resultOpenDayCreation = this.mockMvc.perform(post("/api/opendays")
                        .contentType(APPLICATION_JSON)
                        .content(openDayToCreate))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        JSONObject jsonResultOpenDayCreation = (JSONObject) parser.parse(resultOpenDayCreation.getResponse().getContentAsString());
        Long openDayId = Long.parseLong(jsonResultOpenDayCreation.get("id").toString());


        MvcResult resultGet = this.mockMvc.perform(get("/api/opendays")
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

        List<OpenDayDto> openDayDtos = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            openDayDtos.add(mapper.readValue(jsonArray.get(i).toString(), OpenDayDto.class));
        }

        int lenght = (int) repository.count();
        assertThat(openDayDtos).hasSize(lenght);

    }


    @Test
    @SqlGroup({
            @Sql(value = "classpath:init/clean.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:init/data_init.sql", executionPhase = BEFORE_TEST_METHOD)
    })
    void should_get_one_openDay_by_id() throws Exception {
        final File jsonFile = new ClassPathResource("mockData/openDay/createOpenDay.json").getFile();
        final String openDayToCreate = Files.readString(jsonFile.toPath());

        MvcResult resultOpenDayCreation = this.mockMvc.perform(post("/api/opendays")
                        .contentType(APPLICATION_JSON)
                        .content(openDayToCreate))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        JSONObject jsonResultOpenDayCreation = (JSONObject) parser.parse(resultOpenDayCreation.getResponse().getContentAsString());
        Long openDayId = Long.parseLong(jsonResultOpenDayCreation.get("id").toString());

        OpenDay openDay = repository.findById(openDayId).get();

        MvcResult resultGet = this.mockMvc.perform(get("/api/opendays/" + openDayId)
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String contentGet = resultGet.getResponse().getContentAsString();
        JSONObject jsonResult = (JSONObject) parser.parse(contentGet);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        OpenDayDto openDayDto = mapper.readValue(jsonResult.toString(), OpenDayDto.class);

        List<String> compareGetters = new ArrayList<>();
        compareGetters.add("date");
        compareGetters.add("maxParallelAppointments");

        List<String> openDayDifferences = MyReflectionTestUtils.compareGetters(openDay, openDayDto, compareGetters);
        assertThat(openDayDifferences).isEmpty();
    }

    @Test
    @SqlGroup({
            @Sql(value = "classpath:init/clean.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:init/data_init.sql", executionPhase = BEFORE_TEST_METHOD)
    })
    void should_delete_one_openDay_by_id() throws Exception {
        final File jsonFile = new ClassPathResource("mockData/openDay/createOpenDay.json").getFile();
        final String openDayToCreate = Files.readString(jsonFile.toPath());

        MvcResult resultOpenDayCreation = this.mockMvc.perform(post("/api/opendays")
                        .contentType(APPLICATION_JSON)
                        .content(openDayToCreate))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        JSONObject jsonResultOpenDayCreation = (JSONObject) parser.parse(resultOpenDayCreation.getResponse().getContentAsString());
        Long openDayId = Long.parseLong(jsonResultOpenDayCreation.get("id").toString());

        OpenDay openDay = repository.findById(openDayId).get();

        MvcResult resultGet = this.mockMvc.perform(delete("/api/opendays/" + openDay.getId())
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();


        Optional<OpenDay> openDayOptional = repository.findById(openDay.getId());
        assertThat(openDayOptional).isEmpty();
    }
}
