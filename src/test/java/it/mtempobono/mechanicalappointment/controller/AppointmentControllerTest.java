package it.mtempobono.mechanicalappointment.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import it.mtempobono.mechanicalappointment.model.TimePeriod;
import it.mtempobono.mechanicalappointment.model.dto.AppointmentDto;
import it.mtempobono.mechanicalappointment.model.dto.AppointmentSearchDto;
import it.mtempobono.mechanicalappointment.model.dto.AppointmentVote;
import it.mtempobono.mechanicalappointment.model.entity.Appointment;
import it.mtempobono.mechanicalappointment.model.entity.Vote;
import it.mtempobono.mechanicalappointment.repository.AppointmentRepository;
import it.mtempobono.mechanicalappointment.repository.VoteRepository;
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
public class AppointmentControllerTest {
    @Autowired
    private AppointmentRepository repository;

    @Autowired
    private MockMvc mockMvc;
    JSONParser parser = new JSONParser();
    @Autowired
    private VoteRepository voteRepository;


    @Test
    @SqlGroup({
            @Sql(value = "classpath:init/clean.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:init/data_init.sql", executionPhase = BEFORE_TEST_METHOD)
    })
    void should_create_one_appointment() throws Exception {
        final File jsonFile = new ClassPathResource("mockData/appointment/createAppointment.json").getFile();
        final String appointmentToCreate = Files.readString(jsonFile.toPath());

        MvcResult result = this.mockMvc.perform(post("/api/appointments")
                        .contentType(APPLICATION_JSON)
                        .content(appointmentToCreate))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        JSONObject jsonObject = (JSONObject) parser.parse(content);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        AppointmentDto createAppointment = mapper.readValue(jsonObject.toString(), AppointmentDto.class);

        Appointment appointment = repository.findById(Long.parseLong(jsonObject.get("id").toString())).get();

        List<String> compareGetters = new ArrayList<>();
        compareGetters.add("comment");
        compareGetters.add("isMechanicalActionCustom");


        List<String> appointmentDifferences = MyReflectionTestUtils.compareGetters(appointment, createAppointment, compareGetters);
        assertThat(appointmentDifferences).isEmpty();

        repository.delete(appointment);
    }


    @Test
    @SqlGroup({
            @Sql(value = "classpath:init/clean.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:init/data_init.sql", executionPhase = BEFORE_TEST_METHOD)
    })
    void should_update_one_appointment() throws Exception {
        final File jsonFile = new ClassPathResource("mockData/appointment/createAppointment.json").getFile();
        final String appointmentToCreate = Files.readString(jsonFile.toPath());

        MvcResult resultAppointmentCreation = this.mockMvc.perform(post("/api/appointments")
                        .contentType(APPLICATION_JSON)
                        .content(appointmentToCreate))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        JSONObject jsonResultAppointmentCreation = (JSONObject) parser.parse(resultAppointmentCreation.getResponse().getContentAsString());
        Long appointmentId = Long.parseLong(jsonResultAppointmentCreation.get("id").toString());
        Appointment oldAppointment = repository.findById(appointmentId).orElseThrow(() -> new Exception("Appointment not found"));

        final File updateAppointment = new ClassPathResource("mockData/appointment/updateAppointment.json").getFile();
        final String AppointmentToUpdate = Files.readString(updateAppointment.toPath());

        MvcResult result = this.mockMvc.perform(put("/api/appointments/" + appointmentId)
                        .contentType(APPLICATION_JSON)
                        .content(AppointmentToUpdate))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        JSONObject jsonObject = (JSONObject) parser.parse(content);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        AppointmentDto modifyAppointmentDtoRequest = mapper.readValue(jsonObject.toString(), AppointmentDto.class);

        Appointment appointment = repository.findById(Long.parseLong(jsonObject.get("id").toString())).get();

        List<String> compareGetters = new ArrayList<>();
        compareGetters.add("comment");


        List<String> appointmentDifferences = MyReflectionTestUtils.compareGetters(appointment, modifyAppointmentDtoRequest, compareGetters);
        assertThat(appointmentDifferences).isEmpty();
        //compare with old company
        appointmentDifferences = MyReflectionTestUtils.compareGetters(appointment, oldAppointment, compareGetters);
        assertThat(appointmentDifferences).hasSize(1);

        appointmentDifferences.clear();
        compareGetters.clear();

        repository.delete(appointment);
    }


    @Test
    @SqlGroup({
            @Sql(value = "classpath:init/clean.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:init/data_init.sql", executionPhase = BEFORE_TEST_METHOD)
    })
    void should_get_all_appointments() throws Exception {
        final File jsonFile = new ClassPathResource("mockData/appointment/createAppointment.json").getFile();
        final String appointmentToCreate = Files.readString(jsonFile.toPath());

        MvcResult resultAppointmentCreation = this.mockMvc.perform(post("/api/appointments")
                        .contentType(APPLICATION_JSON)
                        .content(appointmentToCreate))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        JSONObject jsonResultAppointmentCreation = (JSONObject) parser.parse(resultAppointmentCreation.getResponse().getContentAsString());
        Long appointmentId = Long.parseLong(jsonResultAppointmentCreation.get("id").toString());


        MvcResult resultGet = this.mockMvc.perform(get("/api/appointments")
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

        List<AppointmentDto> appointmentDtos = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            appointmentDtos.add(mapper.readValue(jsonArray.get(i).toString(), AppointmentDto.class));
        }

        int lenght = (int) repository.count();
        assertThat(appointmentDtos).hasSize(lenght);

    }


    @Test
    @SqlGroup({
            @Sql(value = "classpath:init/clean.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:init/data_init.sql", executionPhase = BEFORE_TEST_METHOD)
    })
    void should_get_one_appointment_by_id() throws Exception {
        final File jsonFile = new ClassPathResource("mockData/appointment/createAppointment.json").getFile();
        final String appointmentToCreate = Files.readString(jsonFile.toPath());

        MvcResult resultAppointmentCreation = this.mockMvc.perform(post("/api/appointments")
                        .contentType(APPLICATION_JSON)
                        .content(appointmentToCreate))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        JSONObject jsonResultAppointmentCreation = (JSONObject) parser.parse(resultAppointmentCreation.getResponse().getContentAsString());
        Long appointmentId = Long.parseLong(jsonResultAppointmentCreation.get("id").toString());

        Appointment appointment = repository.findById(appointmentId).get();

        MvcResult resultGet = this.mockMvc.perform(get("/api/appointments/" + appointmentId)
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

        AppointmentDto appointmentDto = mapper.readValue(jsonResult.toString(), AppointmentDto.class);

        List<String> compareGetters = new ArrayList<>();
        compareGetters.add("comment");
        compareGetters.add("isMechanicalActionCustom");

        List<String> appointmentDifferences = MyReflectionTestUtils.compareGetters(appointment, appointmentDto, compareGetters);
        assertThat(appointmentDifferences).isEmpty();
    }

    @Test
    @SqlGroup({
            @Sql(value = "classpath:init/clean.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:init/data_init.sql", executionPhase = BEFORE_TEST_METHOD)
    })
    void should_delete_one_appointment_by_id() throws Exception {
        final File jsonFile = new ClassPathResource("mockData/appointment/createAppointment.json").getFile();
        final String appointmentToCreate = Files.readString(jsonFile.toPath());

        MvcResult resultAppointmentCreation = this.mockMvc.perform(post("/api/appointments")
                        .contentType(APPLICATION_JSON)
                        .content(appointmentToCreate))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        JSONObject jsonResultAppointmentCreation = (JSONObject) parser.parse(resultAppointmentCreation.getResponse().getContentAsString());
        Long appointmentId = Long.parseLong(jsonResultAppointmentCreation.get("id").toString());

        Appointment appointment = repository.findById(appointmentId).get();

        MvcResult resultGet = this.mockMvc.perform(delete("/api/appointments/" + appointment.getId())
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();


        Optional<Appointment> appointmentOptional = repository.findById(appointment.getId());
        assertThat(appointmentOptional).isEmpty();
    }

    @Test
    @SqlGroup({
            @Sql(value = "classpath:init/clean.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:init/data_init.sql", executionPhase = BEFORE_TEST_METHOD)
    })
    void should_get_available_timeslots() throws Exception {
        final File jsonFile = new ClassPathResource("mockData/appointment/createAppointment.json").getFile();
        final String appointmentToCreate = Files.readString(jsonFile.toPath());

        MvcResult resultAppointmentCreation = this.mockMvc.perform(post("/api/appointments")
                        .contentType(APPLICATION_JSON)
                        .content(appointmentToCreate))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        JSONObject jsonResultAppointmentCreation = (JSONObject) parser.parse(resultAppointmentCreation.getResponse().getContentAsString());
        Long appointmentId = Long.parseLong(jsonResultAppointmentCreation.get("id").toString());

        Appointment appointment = repository.findById(appointmentId).get();

        AppointmentSearchDto appointmentSearchDto = new AppointmentSearchDto();
        appointmentSearchDto.setMechanicalActionId(appointment.getMechanicalAction().getId());
        appointmentSearchDto.setOpendayId(appointment.getOpenDay().getId());
        appointmentSearchDto.setExternalTimeslot(true);
        ObjectMapper mapper = new ObjectMapper();

        String appointmentSearchDtoJson = mapper.writeValueAsString(appointmentSearchDto);
        MvcResult resultGet = this.mockMvc.perform(post("/api/appointments/availableTimeSlots")
                        .contentType(APPLICATION_JSON)
                        .content(appointmentSearchDtoJson)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        String contentGet = resultGet.getResponse().getContentAsString();
        JSONArray jsonArray = (JSONArray) parser.parse(contentGet);

        List<TimePeriod> timePeriods = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            timePeriods.add(mapper.readValue(jsonArray.get(i).toString(), TimePeriod.class));
        }
        assertThat(timePeriods).isNotEmpty();
    }


    @Test
    @SqlGroup({
            @Sql(value = "classpath:init/clean.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:init/data_init.sql", executionPhase = BEFORE_TEST_METHOD)
    })
    void should_get_one_appointment_by_user_id() throws Exception {
        final File jsonFile = new ClassPathResource("mockData/appointment/createAppointment.json").getFile();
        final String appointmentToCreate = Files.readString(jsonFile.toPath());

        MvcResult resultAppointmentCreation = this.mockMvc.perform(post("/api/appointments")
                        .contentType(APPLICATION_JSON)
                        .content(appointmentToCreate))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        JSONObject jsonResultAppointmentCreation = (JSONObject) parser.parse(resultAppointmentCreation.getResponse().getContentAsString());
        Long appointmentId = Long.parseLong(jsonResultAppointmentCreation.get("id").toString());

        Appointment appointment = repository.findById(appointmentId).get();

        MvcResult resultGet = this.mockMvc.perform(get("/api/appointments/appointmentsUser/" + appointment.getVehicle().getUser().getId())
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

        List<AppointmentDto> timePeriods = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            timePeriods.add(mapper.readValue(jsonArray.get(i).toString(), AppointmentDto.class));
        }
        assertThat(timePeriods).isNotEmpty();
    }

    @Test
    @SqlGroup({
            @Sql(value = "classpath:init/clean.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:init/data_init.sql", executionPhase = BEFORE_TEST_METHOD)
    })
    void should_vote_on_appointment() throws Exception {
        final File jsonFile = new ClassPathResource("mockData/appointment/createAppointment.json").getFile();
        final String appointmentToCreate = Files.readString(jsonFile.toPath());

        MvcResult resultAppointmentCreation = this.mockMvc.perform(post("/api/appointments")
                        .contentType(APPLICATION_JSON)
                        .content(appointmentToCreate))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        JSONObject jsonResultAppointmentCreation = (JSONObject) parser.parse(resultAppointmentCreation.getResponse().getContentAsString());
        Long appointmentId = Long.parseLong(jsonResultAppointmentCreation.get("id").toString());

        Appointment appointment = repository.findById(appointmentId).get();

        AppointmentVote appointmentVote = new AppointmentVote();
        appointmentVote.setAppointmentId(appointmentId);
        appointmentVote.setRating(1);
        appointmentVote.setComment("test");

        ObjectMapper mapper = new ObjectMapper();
        String appointmentSearchDtoJson = mapper.writeValueAsString(appointmentVote);

        MvcResult result = this.mockMvc.perform(post("/api/appointments/vote")
                        .contentType(APPLICATION_JSON)
                        .content(appointmentSearchDtoJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        JSONObject jsonObject = (JSONObject) parser.parse(content);
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        AppointmentVote vote = mapper.readValue(jsonObject.toString(), AppointmentVote.class);

        assertThat(vote).isNotNull();
        assertThat(vote.getId()).isEqualTo(appointmentId);
    }

    @Test
    @SqlGroup({
            @Sql(value = "classpath:init/clean.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:init/data_init.sql", executionPhase = BEFORE_TEST_METHOD)
    })
    void should_update_vote() throws Exception {
        final File jsonFile = new ClassPathResource("mockData/appointment/createAppointment.json").getFile();
        final String appointmentToCreate = Files.readString(jsonFile.toPath());

        MvcResult resultAppointmentCreation = this.mockMvc.perform(post("/api/appointments")
                        .contentType(APPLICATION_JSON)
                        .content(appointmentToCreate))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        JSONObject jsonResultAppointmentCreation = (JSONObject) parser.parse(resultAppointmentCreation.getResponse().getContentAsString());
        Long appointmentId = Long.parseLong(jsonResultAppointmentCreation.get("id").toString());

        Appointment appointment = repository.findById(appointmentId).get();

        AppointmentVote appointmentVote = new AppointmentVote();
        appointmentVote.setAppointmentId(appointmentId);
        appointmentVote.setRating(1);
        appointmentVote.setComment("test");

        ObjectMapper mapper = new ObjectMapper();
        String appointmentSearchDtoJson = mapper.writeValueAsString(appointmentVote);

        MvcResult result = this.mockMvc.perform(post("/api/appointments/vote")
                        .contentType(APPLICATION_JSON)
                        .content(appointmentSearchDtoJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        JSONObject jsonObject = (JSONObject) parser.parse(content);
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        AppointmentVote vote = mapper.readValue(jsonObject.toString(), AppointmentVote.class);


        appointmentVote.setRating(5);
        appointmentSearchDtoJson = mapper.writeValueAsString(appointmentVote);

        MvcResult resultPut = this.mockMvc.perform(put("/api/appointments/vote/" + vote.getId())
                        .contentType(APPLICATION_JSON)
                        .content(appointmentSearchDtoJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        content = result.getResponse().getContentAsString();
        jsonObject = (JSONObject) parser.parse(content);
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        vote = mapper.readValue(jsonObject.toString(), AppointmentVote.class);

        assertThat(vote).isNotNull();
        assertThat(vote.getId()).isEqualTo(appointmentId);
    }



    @Test
    @SqlGroup({
            @Sql(value = "classpath:init/clean.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:init/data_init.sql", executionPhase = BEFORE_TEST_METHOD)
    })
    void should_delete_vote() throws Exception {
        final File jsonFile = new ClassPathResource("mockData/appointment/createAppointment.json").getFile();
        final String appointmentToCreate = Files.readString(jsonFile.toPath());

        MvcResult resultAppointmentCreation = this.mockMvc.perform(post("/api/appointments")
                        .contentType(APPLICATION_JSON)
                        .content(appointmentToCreate))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        JSONObject jsonResultAppointmentCreation = (JSONObject) parser.parse(resultAppointmentCreation.getResponse().getContentAsString());
        Long appointmentId = Long.parseLong(jsonResultAppointmentCreation.get("id").toString());

        Appointment appointment = repository.findById(appointmentId).get();

        AppointmentVote appointmentVote = new AppointmentVote();
        appointmentVote.setAppointmentId(appointmentId);
        appointmentVote.setRating(1);
        appointmentVote.setComment("test");

        ObjectMapper mapper = new ObjectMapper();
        String appointmentSearchDtoJson = mapper.writeValueAsString(appointmentVote);

        MvcResult result = this.mockMvc.perform(post("/api/appointments/vote")
                        .contentType(APPLICATION_JSON)
                        .content(appointmentSearchDtoJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        JSONObject jsonObject = (JSONObject) parser.parse(content);
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        AppointmentVote vote = mapper.readValue(jsonObject.toString(), AppointmentVote.class);


        appointmentVote.setRating(5);
        appointmentSearchDtoJson = mapper.writeValueAsString(appointmentVote);

        MvcResult resultDelete = this.mockMvc.perform(delete("/api/appointments/vote/" + vote.getId())
                        .contentType(APPLICATION_JSON)
                        )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();


        Optional<Vote> appointmentOptional = voteRepository.findById(appointment.getId());
        assertThat(appointmentOptional).isEmpty();
    }



    @Test
    @SqlGroup({
            @Sql(value = "classpath:init/clean.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:init/data_init.sql", executionPhase = BEFORE_TEST_METHOD)
    })
    void should_set_status() throws Exception {
        final File jsonFile = new ClassPathResource("mockData/appointment/createAppointment.json").getFile();
        final String appointmentToCreate = Files.readString(jsonFile.toPath());

        MvcResult resultAppointmentCreation = this.mockMvc.perform(post("/api/appointments")
                        .contentType(APPLICATION_JSON)
                        .content(appointmentToCreate))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        JSONObject jsonResultAppointmentCreation = (JSONObject) parser.parse(resultAppointmentCreation.getResponse().getContentAsString());
        Long appointmentId = Long.parseLong(jsonResultAppointmentCreation.get("id").toString());

        Appointment appointment = repository.findById(appointmentId).get();

        MvcResult resultPost = this.mockMvc.perform(post("/api/appointments/" + appointment.getId() + "/state/REJECTED" )
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertThat(resultPost).isNotNull();
    }

    @Test
    @SqlGroup({
            @Sql(value = "classpath:init/clean.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:init/data_init.sql", executionPhase = BEFORE_TEST_METHOD)
    })
    void should_handle_custom_appointment() throws Exception {
        final File jsonFile = new ClassPathResource("mockData/appointment/createAppointment.json").getFile();
        final String appointmentToCreate = Files.readString(jsonFile.toPath());

        MvcResult resultAppointmentCreation = this.mockMvc.perform(post("/api/appointments")
                        .contentType(APPLICATION_JSON)
                        .content(appointmentToCreate))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        JSONObject jsonResultAppointmentCreation = (JSONObject) parser.parse(resultAppointmentCreation.getResponse().getContentAsString());
        Long appointmentId = Long.parseLong(jsonResultAppointmentCreation.get("id").toString());

        Appointment appointment = repository.findById(appointmentId).get();

        final File jsonFileEval = new ClassPathResource("mockData/appointment/customAppointmentEvaluation.json").getFile();
        final String appointmentToEval = Files.readString(jsonFileEval.toPath());

        MvcResult resultPost = this.mockMvc.perform(post("/api/appointments/customAppointment" )
                        .contentType(APPLICATION_JSON)
                        .content(appointmentToEval)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertThat(resultPost).isNotNull();

        String content = resultPost.getResponse().getContentAsString();
        JSONObject jsonObject = (JSONObject) parser.parse(content);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        AppointmentDto customAppointment = mapper.readValue(jsonObject.toString(), AppointmentDto.class);

        assertThat(customAppointment).isNotNull();
        assertThat(customAppointment.getComment()).isEqualTo(appointment.getComment());
    }



}
