package it.mtempobono.mechanicalappointment.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import it.mtempobono.mechanicalappointment.model.dto.MechanicalActionDto;
import it.mtempobono.mechanicalappointment.model.entity.MechanicalAction;
import it.mtempobono.mechanicalappointment.repository.MechanicalActionRepository;
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
public class MechanicalActionssControllerTest {
    @Autowired
    private MechanicalActionRepository repository;

    @Autowired
    private MockMvc mockMvc;
    JSONParser parser = new JSONParser();
    @Test
    @SqlGroup({
            @Sql(value = "classpath:init/clean.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:init/data_init.sql", executionPhase = BEFORE_TEST_METHOD)
    })
    void should_create_one_mechanicalActions() throws Exception {
        final File jsonFile = new ClassPathResource("mockData/mechanicalActions/createMechanicalActions.json").getFile();
        final String mechanicalActionsToCreate = Files.readString(jsonFile.toPath());

        MvcResult result = this.mockMvc.perform(post("/api/mechanicalActions")
                        .contentType(APPLICATION_JSON)
                        .content(mechanicalActionsToCreate))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        JSONObject jsonObject = (JSONObject) parser.parse(content);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);

        MechanicalActionDto createMechanicalActions = mapper.readValue(jsonObject.toString(), MechanicalActionDto.class);

        MechanicalAction mechanicalActions = repository.findById(Long.parseLong(jsonObject.get("id").toString())).get();

        List<String> compareGetters = new ArrayList<>();
        compareGetters.add("name");
        compareGetters.add("description");
        compareGetters.add("price");
        compareGetters.add("isActive");


        List<String> mechanicalActionsDifferences = MyReflectionTestUtils.compareGetters(mechanicalActions, createMechanicalActions, compareGetters);
        assertThat(mechanicalActionsDifferences).isEmpty();

        repository.delete(mechanicalActions);
    }

//
//    @Test
//    @SqlGroup({
//            @Sql(value = "classpath:init/clean.sql", executionPhase = BEFORE_TEST_METHOD),
//            @Sql(value = "classpath:init/data_init.sql", executionPhase = BEFORE_TEST_METHOD)
//    })
//    void should_update_one_mechanicalActions() throws Exception {
//        final File jsonFile = new ClassPathResource("mockData/mechanicalActions/createMechanicalActions.json").getFile();
//        final String mechanicalActionsToCreate = Files.readString(jsonFile.toPath());
//
//        MvcResult resultMechanicalActionsCreation = this.mockMvc.perform(post("/api/mechanicalActionss")
//                        .contentType(APPLICATION_JSON)
//                        .content(mechanicalActionsToCreate))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andReturn();
//        JSONObject jsonResultMechanicalActionsCreation = (JSONObject) parser.parse(resultMechanicalActionsCreation.getResponse().getContentAsString());
//        Long mechanicalActionsId = Long.parseLong(jsonResultMechanicalActionsCreation.get("id").toString());
//        MechanicalActions oldMechanicalActions = repository.findById(mechanicalActionsId).orElseThrow(() -> new Exception("MechanicalActions not found"));
//
//        final File updateMechanicalActions = new ClassPathResource("mockData/mechanicalActions/updateMechanicalActions.json").getFile();
//        final String MechanicalActionsToUpdate = Files.readString(updateMechanicalActions.toPath());
//
//        MvcResult result = this.mockMvc.perform(put("/api/mechanicalActionss/" + mechanicalActionsId)
//                        .contentType(APPLICATION_JSON)
//                        .content(MechanicalActionsToUpdate))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andReturn();
//
//        String content = result.getResponse().getContentAsString();
//        JSONObject jsonObject = (JSONObject) parser.parse(content);
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.registerModule(new JavaTimeModule());
//        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//
//        MechanicalActionsDto modifyMechanicalActionsDtoRequest = mapper.readValue(jsonObject.toString(), MechanicalActionsDto.class);
//
//        MechanicalActions mechanicalActions = repository.findById(Long.parseLong(jsonObject.get("id").toString())).get();
//
//        List<String> compareGetters = new ArrayList<>();
//        compareGetters.add("comment");
//
//
//        List<String> mechanicalActionsDifferences = MyReflectionTestUtils.compareGetters(mechanicalActions, modifyMechanicalActionsDtoRequest, compareGetters);
//        assertThat(mechanicalActionsDifferences).isEmpty();
//        //compare with old company
//        mechanicalActionsDifferences = MyReflectionTestUtils.compareGetters(mechanicalActions, oldMechanicalActions, compareGetters);
//        assertThat(mechanicalActionsDifferences).hasSize(1);
//
//        mechanicalActionsDifferences.clear();
//        compareGetters.clear();
//
//        repository.delete(mechanicalActions);
//    }
//
//
//    @Test
//    @SqlGroup({
//            @Sql(value = "classpath:init/clean.sql", executionPhase = BEFORE_TEST_METHOD),
//            @Sql(value = "classpath:init/data_init.sql", executionPhase = BEFORE_TEST_METHOD)
//    })
//    void should_get_all_mechanicalActionss() throws Exception {
//        final File jsonFile = new ClassPathResource("mockData/mechanicalActions/createMechanicalActions.json").getFile();
//        final String mechanicalActionsToCreate = Files.readString(jsonFile.toPath());
//
//        MvcResult resultMechanicalActionsCreation = this.mockMvc.perform(post("/api/mechanicalActionss")
//                        .contentType(APPLICATION_JSON)
//                        .content(mechanicalActionsToCreate))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andReturn();
//        JSONObject jsonResultMechanicalActionsCreation = (JSONObject) parser.parse(resultMechanicalActionsCreation.getResponse().getContentAsString());
//        Long mechanicalActionsId = Long.parseLong(jsonResultMechanicalActionsCreation.get("id").toString());
//
//
//        MvcResult resultGet = this.mockMvc.perform(get("/api/mechanicalActionss")
//                        .contentType(APPLICATION_JSON)
//                )
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andReturn();
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.registerModule(new JavaTimeModule());
//        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//
//        String contentGet = resultGet.getResponse().getContentAsString();
//        JSONArray jsonArray = (JSONArray) parser.parse(contentGet);
//
//        List<MechanicalActionsDto> mechanicalActionsDtos = new ArrayList<>();
//        for (int i = 0; i < jsonArray.size(); i++) {
//            mechanicalActionsDtos.add(mapper.readValue(jsonArray.get(i).toString(), MechanicalActionsDto.class));
//        }
//
//        int lenght = (int) repository.count();
//        assertThat(mechanicalActionsDtos).hasSize(lenght);
//
//    }
//
//
//    @Test
//    @SqlGroup({
//            @Sql(value = "classpath:init/clean.sql", executionPhase = BEFORE_TEST_METHOD),
//            @Sql(value = "classpath:init/data_init.sql", executionPhase = BEFORE_TEST_METHOD)
//    })
//    void should_get_one_mechanicalActions_by_id() throws Exception {
//        final File jsonFile = new ClassPathResource("mockData/mechanicalActions/createMechanicalActions.json").getFile();
//        final String mechanicalActionsToCreate = Files.readString(jsonFile.toPath());
//
//        MvcResult resultMechanicalActionsCreation = this.mockMvc.perform(post("/api/mechanicalActionss")
//                        .contentType(APPLICATION_JSON)
//                        .content(mechanicalActionsToCreate))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andReturn();
//        JSONObject jsonResultMechanicalActionsCreation = (JSONObject) parser.parse(resultMechanicalActionsCreation.getResponse().getContentAsString());
//        Long mechanicalActionsId = Long.parseLong(jsonResultMechanicalActionsCreation.get("id").toString());
//
//        MechanicalActions mechanicalActions = repository.findById(mechanicalActionsId).get();
//
//        MvcResult resultGet = this.mockMvc.perform(get("/api/mechanicalActionss/" + mechanicalActionsId)
//                        .contentType(APPLICATION_JSON)
//                )
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andReturn();
//
//        String contentGet = resultGet.getResponse().getContentAsString();
//        JSONObject jsonResult = (JSONObject) parser.parse(contentGet);
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.registerModule(new JavaTimeModule());
//        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//
//        MechanicalActionsDto mechanicalActionsDto = mapper.readValue(jsonResult.toString(), MechanicalActionsDto.class);
//
//        List<String> compareGetters = new ArrayList<>();
//        compareGetters.add("comment");
//        compareGetters.add("isMechanicalActionCustom");
//
//        List<String> mechanicalActionsDifferences = MyReflectionTestUtils.compareGetters(mechanicalActions, mechanicalActionsDto, compareGetters);
//        assertThat(mechanicalActionsDifferences).isEmpty();
//    }
//
//    @Test
//    @SqlGroup({
//            @Sql(value = "classpath:init/clean.sql", executionPhase = BEFORE_TEST_METHOD),
//            @Sql(value = "classpath:init/data_init.sql", executionPhase = BEFORE_TEST_METHOD)
//    })
//    void should_delete_one_mechanicalActions_by_id() throws Exception {
//        final File jsonFile = new ClassPathResource("mockData/mechanicalActions/createMechanicalActions.json").getFile();
//        final String mechanicalActionsToCreate = Files.readString(jsonFile.toPath());
//
//        MvcResult resultMechanicalActionsCreation = this.mockMvc.perform(post("/api/mechanicalActionss")
//                        .contentType(APPLICATION_JSON)
//                        .content(mechanicalActionsToCreate))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andReturn();
//        JSONObject jsonResultMechanicalActionsCreation = (JSONObject) parser.parse(resultMechanicalActionsCreation.getResponse().getContentAsString());
//        Long mechanicalActionsId = Long.parseLong(jsonResultMechanicalActionsCreation.get("id").toString());
//
//        MechanicalActions mechanicalActions = repository.findById(mechanicalActionsId).get();
//
//        MvcResult resultGet = this.mockMvc.perform(delete("/api/mechanicalActionss/" + mechanicalActions.getId())
//                        .contentType(APPLICATION_JSON)
//                )
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andReturn();
//
//
//        Optional<MechanicalActions> mechanicalActionsOptional = repository.findById(mechanicalActions.getId());
//        assertThat(mechanicalActionsOptional).isEmpty();
//    }
//
//    @Test
//    @SqlGroup({
//            @Sql(value = "classpath:init/clean.sql", executionPhase = BEFORE_TEST_METHOD),
//            @Sql(value = "classpath:init/data_init.sql", executionPhase = BEFORE_TEST_METHOD)
//    })
//    void should_get_available_timeslots() throws Exception {
//        final File jsonFile = new ClassPathResource("mockData/mechanicalActions/createMechanicalActions.json").getFile();
//        final String mechanicalActionsToCreate = Files.readString(jsonFile.toPath());
//
//        MvcResult resultMechanicalActionsCreation = this.mockMvc.perform(post("/api/mechanicalActionss")
//                        .contentType(APPLICATION_JSON)
//                        .content(mechanicalActionsToCreate))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andReturn();
//        JSONObject jsonResultMechanicalActionsCreation = (JSONObject) parser.parse(resultMechanicalActionsCreation.getResponse().getContentAsString());
//        Long mechanicalActionsId = Long.parseLong(jsonResultMechanicalActionsCreation.get("id").toString());
//
//        MechanicalActions mechanicalActions = repository.findById(mechanicalActionsId).get();
//
//        MechanicalActionsSearchDto mechanicalActionsSearchDto = new MechanicalActionsSearchDto();
//        mechanicalActionsSearchDto.setMechanicalActionId(mechanicalActions.getMechanicalAction().getId());
//        mechanicalActionsSearchDto.setOpendayId(mechanicalActions.getOpenDay().getId());
//        mechanicalActionsSearchDto.setExternalTimeslot(true);
//        ObjectMapper mapper = new ObjectMapper();
//
//        String mechanicalActionsSearchDtoJson = mapper.writeValueAsString(mechanicalActionsSearchDto);
//        MvcResult resultGet = this.mockMvc.perform(post("/api/mechanicalActionss/availableTimeSlots")
//                        .contentType(APPLICATION_JSON)
//                        .content(mechanicalActionsSearchDtoJson)
//                )
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andReturn();
//
//        mapper.registerModule(new JavaTimeModule());
//        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//
//        String contentGet = resultGet.getResponse().getContentAsString();
//        JSONArray jsonArray = (JSONArray) parser.parse(contentGet);
//
//        List<TimePeriod> timePeriods = new ArrayList<>();
//        for (int i = 0; i < jsonArray.size(); i++) {
//            timePeriods.add(mapper.readValue(jsonArray.get(i).toString(), TimePeriod.class));
//        }
//        assertThat(timePeriods).isNotEmpty();
//    }
//
//
//    @Test
//    @SqlGroup({
//            @Sql(value = "classpath:init/clean.sql", executionPhase = BEFORE_TEST_METHOD),
//            @Sql(value = "classpath:init/data_init.sql", executionPhase = BEFORE_TEST_METHOD)
//    })
//    void should_get_one_mechanicalActions_by_user_id() throws Exception {
//        final File jsonFile = new ClassPathResource("mockData/mechanicalActions/createMechanicalActions.json").getFile();
//        final String mechanicalActionsToCreate = Files.readString(jsonFile.toPath());
//
//        MvcResult resultMechanicalActionsCreation = this.mockMvc.perform(post("/api/mechanicalActionss")
//                        .contentType(APPLICATION_JSON)
//                        .content(mechanicalActionsToCreate))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andReturn();
//        JSONObject jsonResultMechanicalActionsCreation = (JSONObject) parser.parse(resultMechanicalActionsCreation.getResponse().getContentAsString());
//        Long mechanicalActionsId = Long.parseLong(jsonResultMechanicalActionsCreation.get("id").toString());
//
//        MechanicalActions mechanicalActions = repository.findById(mechanicalActionsId).get();
//
//        MvcResult resultGet = this.mockMvc.perform(get("/api/mechanicalActionss/mechanicalActionssUser/" + mechanicalActions.getMechanicalActions().getUser().getId())
//                        .contentType(APPLICATION_JSON)
//                )
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andReturn();
//        ObjectMapper mapper = new ObjectMapper();
//
//        mapper.registerModule(new JavaTimeModule());
//        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//
//        String contentGet = resultGet.getResponse().getContentAsString();
//        JSONArray jsonArray = (JSONArray) parser.parse(contentGet);
//
//        List<MechanicalActionsDto> timePeriods = new ArrayList<>();
//        for (int i = 0; i < jsonArray.size(); i++) {
//            timePeriods.add(mapper.readValue(jsonArray.get(i).toString(), MechanicalActionsDto.class));
//        }
//        assertThat(timePeriods).isNotEmpty();
//    }
//
//    @Test
//    @SqlGroup({
//            @Sql(value = "classpath:init/clean.sql", executionPhase = BEFORE_TEST_METHOD),
//            @Sql(value = "classpath:init/data_init.sql", executionPhase = BEFORE_TEST_METHOD)
//    })
//    void should_vote_on_mechanicalActions() throws Exception {
//        final File jsonFile = new ClassPathResource("mockData/mechanicalActions/createMechanicalActions.json").getFile();
//        final String mechanicalActionsToCreate = Files.readString(jsonFile.toPath());
//
//        MvcResult resultMechanicalActionsCreation = this.mockMvc.perform(post("/api/mechanicalActionss")
//                        .contentType(APPLICATION_JSON)
//                        .content(mechanicalActionsToCreate))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andReturn();
//        JSONObject jsonResultMechanicalActionsCreation = (JSONObject) parser.parse(resultMechanicalActionsCreation.getResponse().getContentAsString());
//        Long mechanicalActionsId = Long.parseLong(jsonResultMechanicalActionsCreation.get("id").toString());
//
//        MechanicalActions mechanicalActions = repository.findById(mechanicalActionsId).get();
//
//        MechanicalActionsVote mechanicalActionsVote = new MechanicalActionsVote();
//        mechanicalActionsVote.setMechanicalActionsId(mechanicalActionsId);
//        mechanicalActionsVote.setRating(1);
//        mechanicalActionsVote.setComment("test");
//
//        ObjectMapper mapper = new ObjectMapper();
//        String mechanicalActionsSearchDtoJson = mapper.writeValueAsString(mechanicalActionsVote);
//
//        MvcResult result = this.mockMvc.perform(post("/api/mechanicalActionss/vote")
//                        .contentType(APPLICATION_JSON)
//                        .content(mechanicalActionsSearchDtoJson))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andReturn();
//
//        String content = result.getResponse().getContentAsString();
//        JSONObject jsonObject = (JSONObject) parser.parse(content);
//        mapper.registerModule(new JavaTimeModule());
//        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//
//        MechanicalActionsVote vote = mapper.readValue(jsonObject.toString(), MechanicalActionsVote.class);
//
//        assertThat(vote).isNotNull();
//        assertThat(vote.getId()).isEqualTo(mechanicalActionsId);
//    }
//
//    @Test
//    @SqlGroup({
//            @Sql(value = "classpath:init/clean.sql", executionPhase = BEFORE_TEST_METHOD),
//            @Sql(value = "classpath:init/data_init.sql", executionPhase = BEFORE_TEST_METHOD)
//    })
//    void should_update_vote() throws Exception {
//        final File jsonFile = new ClassPathResource("mockData/mechanicalActions/createMechanicalActions.json").getFile();
//        final String mechanicalActionsToCreate = Files.readString(jsonFile.toPath());
//
//        MvcResult resultMechanicalActionsCreation = this.mockMvc.perform(post("/api/mechanicalActionss")
//                        .contentType(APPLICATION_JSON)
//                        .content(mechanicalActionsToCreate))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andReturn();
//        JSONObject jsonResultMechanicalActionsCreation = (JSONObject) parser.parse(resultMechanicalActionsCreation.getResponse().getContentAsString());
//        Long mechanicalActionsId = Long.parseLong(jsonResultMechanicalActionsCreation.get("id").toString());
//
//        MechanicalActions mechanicalActions = repository.findById(mechanicalActionsId).get();
//
//        MechanicalActionsVote mechanicalActionsVote = new MechanicalActionsVote();
//        mechanicalActionsVote.setMechanicalActionsId(mechanicalActionsId);
//        mechanicalActionsVote.setRating(1);
//        mechanicalActionsVote.setComment("test");
//
//        ObjectMapper mapper = new ObjectMapper();
//        String mechanicalActionsSearchDtoJson = mapper.writeValueAsString(mechanicalActionsVote);
//
//        MvcResult result = this.mockMvc.perform(post("/api/mechanicalActionss/vote")
//                        .contentType(APPLICATION_JSON)
//                        .content(mechanicalActionsSearchDtoJson))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andReturn();
//
//        String content = result.getResponse().getContentAsString();
//        JSONObject jsonObject = (JSONObject) parser.parse(content);
//        mapper.registerModule(new JavaTimeModule());
//        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//
//        MechanicalActionsVote vote = mapper.readValue(jsonObject.toString(), MechanicalActionsVote.class);
//
//
//        mechanicalActionsVote.setRating(5);
//        mechanicalActionsSearchDtoJson = mapper.writeValueAsString(mechanicalActionsVote);
//
//        MvcResult resultPut = this.mockMvc.perform(put("/api/mechanicalActionss/vote/" + vote.getId())
//                        .contentType(APPLICATION_JSON)
//                        .content(mechanicalActionsSearchDtoJson))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andReturn();
//
//        content = result.getResponse().getContentAsString();
//        jsonObject = (JSONObject) parser.parse(content);
//        mapper.registerModule(new JavaTimeModule());
//        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//
//        vote = mapper.readValue(jsonObject.toString(), MechanicalActionsVote.class);
//
//        assertThat(vote).isNotNull();
//        assertThat(vote.getId()).isEqualTo(mechanicalActionsId);
//    }
//
//
//
//    @Test
//    @SqlGroup({
//            @Sql(value = "classpath:init/clean.sql", executionPhase = BEFORE_TEST_METHOD),
//            @Sql(value = "classpath:init/data_init.sql", executionPhase = BEFORE_TEST_METHOD)
//    })
//    void should_delete_vote() throws Exception {
//        final File jsonFile = new ClassPathResource("mockData/mechanicalActions/createMechanicalActions.json").getFile();
//        final String mechanicalActionsToCreate = Files.readString(jsonFile.toPath());
//
//        MvcResult resultMechanicalActionsCreation = this.mockMvc.perform(post("/api/mechanicalActionss")
//                        .contentType(APPLICATION_JSON)
//                        .content(mechanicalActionsToCreate))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andReturn();
//        JSONObject jsonResultMechanicalActionsCreation = (JSONObject) parser.parse(resultMechanicalActionsCreation.getResponse().getContentAsString());
//        Long mechanicalActionsId = Long.parseLong(jsonResultMechanicalActionsCreation.get("id").toString());
//
//        MechanicalActions mechanicalActions = repository.findById(mechanicalActionsId).get();
//
//        MechanicalActionsVote mechanicalActionsVote = new MechanicalActionsVote();
//        mechanicalActionsVote.setMechanicalActionsId(mechanicalActionsId);
//        mechanicalActionsVote.setRating(1);
//        mechanicalActionsVote.setComment("test");
//
//        ObjectMapper mapper = new ObjectMapper();
//        String mechanicalActionsSearchDtoJson = mapper.writeValueAsString(mechanicalActionsVote);
//
//        MvcResult result = this.mockMvc.perform(post("/api/mechanicalActionss/vote")
//                        .contentType(APPLICATION_JSON)
//                        .content(mechanicalActionsSearchDtoJson))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andReturn();
//
//        String content = result.getResponse().getContentAsString();
//        JSONObject jsonObject = (JSONObject) parser.parse(content);
//        mapper.registerModule(new JavaTimeModule());
//        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//
//        MechanicalActionsVote vote = mapper.readValue(jsonObject.toString(), MechanicalActionsVote.class);
//
//
//        mechanicalActionsVote.setRating(5);
//        mechanicalActionsSearchDtoJson = mapper.writeValueAsString(mechanicalActionsVote);
//
//        MvcResult resultDelete = this.mockMvc.perform(delete("/api/mechanicalActionss/vote/" + vote.getId())
//                        .contentType(APPLICATION_JSON)
//                        )
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andReturn();
//
//
//        Optional<Vote> mechanicalActionsOptional = voteRepository.findById(mechanicalActions.getId());
//        assertThat(mechanicalActionsOptional).isEmpty();
//    }
//
//
//
//    @Test
//    @SqlGroup({
//            @Sql(value = "classpath:init/clean.sql", executionPhase = BEFORE_TEST_METHOD),
//            @Sql(value = "classpath:init/data_init.sql", executionPhase = BEFORE_TEST_METHOD)
//    })
//    void should_set_status() throws Exception {
//        final File jsonFile = new ClassPathResource("mockData/mechanicalActions/createMechanicalActions.json").getFile();
//        final String mechanicalActionsToCreate = Files.readString(jsonFile.toPath());
//
//        MvcResult resultMechanicalActionsCreation = this.mockMvc.perform(post("/api/mechanicalActionss")
//                        .contentType(APPLICATION_JSON)
//                        .content(mechanicalActionsToCreate))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andReturn();
//        JSONObject jsonResultMechanicalActionsCreation = (JSONObject) parser.parse(resultMechanicalActionsCreation.getResponse().getContentAsString());
//        Long mechanicalActionsId = Long.parseLong(jsonResultMechanicalActionsCreation.get("id").toString());
//
//        MechanicalActions mechanicalActions = repository.findById(mechanicalActionsId).get();
//
//        MvcResult resultPost = this.mockMvc.perform(post("/api/mechanicalActionss/" + mechanicalActions.getId() + "/state/REJECTED" )
//                        .contentType(APPLICATION_JSON)
//                )
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andReturn();
//
//        assertThat(resultPost).isNotNull();
//    }
//
//    @Test
//    @SqlGroup({
//            @Sql(value = "classpath:init/clean.sql", executionPhase = BEFORE_TEST_METHOD),
//            @Sql(value = "classpath:init/data_init.sql", executionPhase = BEFORE_TEST_METHOD)
//    })
//    void should_handle_custom_mechanicalActions() throws Exception {
//        final File jsonFile = new ClassPathResource("mockData/mechanicalActions/createMechanicalActions.json").getFile();
//        final String mechanicalActionsToCreate = Files.readString(jsonFile.toPath());
//
//        MvcResult resultMechanicalActionsCreation = this.mockMvc.perform(post("/api/mechanicalActionss")
//                        .contentType(APPLICATION_JSON)
//                        .content(mechanicalActionsToCreate))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andReturn();
//        JSONObject jsonResultMechanicalActionsCreation = (JSONObject) parser.parse(resultMechanicalActionsCreation.getResponse().getContentAsString());
//        Long mechanicalActionsId = Long.parseLong(jsonResultMechanicalActionsCreation.get("id").toString());
//
//        MechanicalActions mechanicalActions = repository.findById(mechanicalActionsId).get();
//
//        final File jsonFileEval = new ClassPathResource("mockData/mechanicalActions/customMechanicalActionsEvaluation.json").getFile();
//        final String mechanicalActionsToEval = Files.readString(jsonFileEval.toPath());
//
//        MvcResult resultPost = this.mockMvc.perform(post("/api/mechanicalActionss/customMechanicalActions" )
//                        .contentType(APPLICATION_JSON)
//                        .content(mechanicalActionsToEval)
//                )
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andReturn();
//
//        assertThat(resultPost).isNotNull();
//
//        String content = resultPost.getResponse().getContentAsString();
//        JSONObject jsonObject = (JSONObject) parser.parse(content);
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.registerModule(new JavaTimeModule());
//        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//
//        MechanicalActionsDto customMechanicalActions = mapper.readValue(jsonObject.toString(), MechanicalActionsDto.class);
//
//        assertThat(customMechanicalActions).isNotNull();
//        assertThat(customMechanicalActions.getComment()).isEqualTo(mechanicalActions.getComment());
//    }



}
