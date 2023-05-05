package it.mtempobono.mechanicalappointment.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import it.mtempobono.mechanicalappointment.model.dto.AuthResponseDto;
import it.mtempobono.mechanicalappointment.model.dto.SignUpRequestDto;
import it.mtempobono.mechanicalappointment.model.entity.User;
import it.mtempobono.mechanicalappointment.repository.UserRepository;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
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
import java.nio.file.Files;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ActiveProfiles("test")
public class AuthControllerTest {

    @Autowired
    private UserRepository repository;

    @Autowired
    private MockMvc mockMvc;
    JSONParser parser = new JSONParser();


    @Test
    @SqlGroup({
            @Sql(value = "classpath:init/clean.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:init/data_init.sql", executionPhase = BEFORE_TEST_METHOD)
    })
    public void should_return_200_when_login() throws Exception {
        final File jsonFile = new ClassPathResource("mockData/auth/login.json").getFile();
        final String commentToCreate = Files.readString(jsonFile.toPath());

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(commentToCreate))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        JSONObject jsonObject = (JSONObject) parser.parse(content);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        AuthResponseDto authResponseDTO = mapper.readValue(jsonObject.toString(), AuthResponseDto.class);

        assert authResponseDTO != null;
        assert authResponseDTO.getToken() != null;
        assert authResponseDTO.getRefreshToken() != null;
        assert authResponseDTO.getId() != 0;
        assert authResponseDTO.getEmail() != null;
        assert authResponseDTO.getName() != null;
        assert authResponseDTO.getRole() != null;
    }


    @Test
    @SqlGroup({
            @Sql(value = "classpath:init/clean.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:init/data_init.sql", executionPhase = BEFORE_TEST_METHOD)
    })
    public void should_register_new_user() throws Exception {
        final File jsonFile = new ClassPathResource("mockData/auth/signUp.json").getFile();
        final String commentToCreate = Files.readString(jsonFile.toPath());


        MvcResult result = mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(commentToCreate))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        JSONObject jsonObject = (JSONObject) parser.parse(content);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        SignUpRequestDto registerUser = mapper.readValue(jsonObject.toString(), SignUpRequestDto.class);

        assert registerUser != null;
        Optional<User> userDb = repository.findByEmail(registerUser.getEmail());

        assert userDb.isPresent();
        assert userDb.get().getEmail().equals(registerUser.getEmail());
    }



    @Test
    @SqlGroup({
            @Sql(value = "classpath:init/clean.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:init/data_init.sql", executionPhase = BEFORE_TEST_METHOD)
    })
    public void should_return_200_when_refresh_token() throws Exception {
        final File jsonFile = new ClassPathResource("mockData/auth/login.json").getFile();
        final String commentToCreate = Files.readString(jsonFile.toPath());

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(commentToCreate))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        JSONObject jsonObject = (JSONObject) parser.parse(content);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        AuthResponseDto authResponseDTO = mapper.readValue(jsonObject.toString(), AuthResponseDto.class);


        result = mockMvc.perform(post("/api/auth/refreshToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("refreshToken", authResponseDTO.getRefreshToken()))
                .andExpect(status().isOk())
                .andReturn();

        content = result.getResponse().getContentAsString();
        jsonObject = (JSONObject) parser.parse(content);
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        AuthResponseDto refreshResponseDTO = mapper.readValue(jsonObject.toString(), AuthResponseDto.class);

        assert refreshResponseDTO != null;
        assert refreshResponseDTO.getToken() != null;
        assert refreshResponseDTO.getRefreshToken() != null;

        assertThat(refreshResponseDTO.getRefreshToken()).isNotEqualTo(authResponseDTO.getRefreshToken());
    }

}
