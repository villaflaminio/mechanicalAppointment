package it.flaminiovilla.mechanicalappointment.negativeTests;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import it.flaminiovilla.mechanicalappointment.model.entity.Appointment;
import it.flaminiovilla.mechanicalappointment.model.entity.MechanicalAction;
import it.flaminiovilla.mechanicalappointment.model.entity.OpenDay;
import it.flaminiovilla.mechanicalappointment.service.impl.AppointmentCore;
import it.flaminiovilla.mechanicalappointment.service.impl.AppointmentServiceImpl;
import it.flaminiovilla.mechanicalappointment.model.DayPlan;
import it.flaminiovilla.mechanicalappointment.model.TimePeriod;
import it.flaminiovilla.mechanicalappointment.model.dto.VehicleDto;
import it.flaminiovilla.mechanicalappointment.repository.PlaceRepository;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ActiveProfiles("test")
class NegativeTest {

    @InjectMocks
    AppointmentServiceImpl appointmentService = new AppointmentServiceImpl();
    private AppointmentCore appointmentCore = AppointmentCore.getInstance();
    OpenDay openDay = new OpenDay();
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PlaceRepository placeRepository;
    JSONParser parser = new JSONParser();

    @BeforeEach
    void setUp() {

        DayPlan dayPlan = new DayPlan();
        List<TimePeriod> breaks = new ArrayList<>();
        breaks.add(new TimePeriod(LocalTime.of(12, 0), LocalTime.of(14, 0)));
        dayPlan.setBreaks(breaks);

        openDay.setWorkPlan(dayPlan);
    }


    @Test()
    void getAvailableHoursWithMaxTwoParallelAppointments() {

        openDay.getWorkPlan().setWorkingHours(new TimePeriod(LocalTime.of(11, 0), LocalTime.of(17, 0)));
        openDay.setMaxParallelAppointments(2);
        Appointment appointment = new Appointment();
        appointment.setInternalTime(new TimePeriod(LocalTime.of(14, 0), LocalTime.of(16, 0)));

        Appointment appointment2 = new Appointment();
        appointment2.setInternalTime(new TimePeriod(LocalTime.of(15, 0), LocalTime.of(16, 30)));

        Appointment appointment3 = new Appointment();
        appointment3.setInternalTime(new TimePeriod(LocalTime.of(15, 30), LocalTime.of(17, 0)));

        Appointment appointment4 = new Appointment();
        appointment4.setInternalTime(new TimePeriod(LocalTime.of(14, 30), LocalTime.of(15, 30)));

        ArrayList<Appointment> appointments = new ArrayList<>();
        appointments.add(appointment);
        appointments.add(appointment2);
        appointments.add(appointment3);
        appointments.add(appointment4);
        openDay.setAppointments(appointments);


        MechanicalAction work = new MechanicalAction();
        work.setInternalDuration(Duration.ofHours(0));
        work.setExternalDuration(Duration.ofHours(0));

        assertThrows(IllegalArgumentException.class, () -> appointmentCore.getAvailableAppointments(openDay, work));

    }


    @Test
    @SqlGroup({
            @Sql(value = "classpath:init/clean.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:init/data_init.sql", executionPhase = BEFORE_TEST_METHOD)
    })
    public void should_register_new_user() throws Exception {
        final File jsonFile = new ClassPathResource("mockData/auth/signUp.json").getFile();
        final String commentToCreate = Files.readString(jsonFile.toPath());


        assertThrows(AssertionError.class, () -> mockMvc.perform(post("/api/auth/NOTEXISTING")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(commentToCreate))
                .andExpect(status().isOk())
                .andReturn());
    }


    @Test
    void should_return_error_when_place_not_found() throws Exception {
        assertThrows(FileNotFoundException.class, () -> new ClassPathResource("mockData/appointment/appointment.json").getFile());

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

        int lenght = (int) placeRepository.count();
        assertThrows(AssertionError.class, () -> assertThat(vehicleDtos).hasSize(lenght));

    }

    @Test
    @SqlGroup({
            @Sql(value = "classpath:init/clean.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:init/data_init.sql", executionPhase = BEFORE_TEST_METHOD)
    })
    void should_get_all_mechanicalActionss() throws Exception {

        MvcResult resultGet = this.mockMvc.perform(get("/api/appointments")
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        ObjectMapper mapper = new ObjectMapper();

        String contentGet = resultGet.getResponse().getContentAsString();
        JSONArray jsonArray = (JSONArray) parser.parse(contentGet);

    }


    @Test
    @SqlGroup({
            @Sql(value = "classpath:init/clean.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:init/data_init.sql", executionPhase = BEFORE_TEST_METHOD)
    })
    void should_get_one_mechanicalActions_by_id() throws Exception {
        final File jsonFile = new ClassPathResource("mockData/mechanicalActions/createMechanicalActions.json").getFile();
        final String mechanicalActionsToCreate = Files.readString(jsonFile.toPath());

        MvcResult resultMechanicalActionsCreation = this.mockMvc.perform(post("/api/mechanicalActions")
                        .contentType(APPLICATION_JSON)
                        .content(mechanicalActionsToCreate))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        JSONObject jsonResultMechanicalActionsCreation = (JSONObject) parser.parse(resultMechanicalActionsCreation.getResponse().getContentAsString());
        assertThrows(NullPointerException.class, () -> Long.parseLong(jsonResultMechanicalActionsCreation.get("id_errato").toString()));

    }

    @Test
    @SqlGroup({
            @Sql(value = "classpath:init/clean.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:init/data_init.sql", executionPhase = BEFORE_TEST_METHOD)
    })
    void should_delete_one_mechanicalActions_by_id() throws Exception {
        final File jsonFile = new ClassPathResource("mockData/mechanicalActions/createMechanicalActions.json").getFile();
        final String mechanicalActionsToCreate = Files.readString(jsonFile.toPath());

        MvcResult resultMechanicalActionsCreation = this.mockMvc.perform(post("/api/mechanicalActions")
                        .contentType(APPLICATION_JSON)
                        .content(mechanicalActionsToCreate))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        assertThrows(ClassCastException.class, () -> Long.parseLong((String) parser.parse(resultMechanicalActionsCreation.getResponse().getContentAsString())));

    }
}