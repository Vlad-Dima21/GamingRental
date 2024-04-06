package com.vladima.gamingrental.integration.controllers;

import com.vladima.gamingrental.helpers.StringifyJSON;
import com.vladima.gamingrental.security.dto.AdminDTO;
import com.vladima.gamingrental.security.dto.UserClientDTO;
import com.vladima.gamingrental.security.dto.UserDTO;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Random;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("mysql")
public abstract class BaseControllerMYSQLTests<Model> {

    @Autowired
    protected MockMvc mockMvc;

    protected String adminToken;
    protected String userToken;

    protected Model sampledModel;

    protected JpaRepository<Model, Long> getRepository() {
        return null;
    }

    @Before
    public void init() throws Exception {
        var random = new Random();
        var models = getRepository().findAll();
        sampledModel = models.get(random.nextInt(models.size()));
    }

    protected void retrieveAdminToken(AdminDTO adminDTO) throws Exception {
        String AUTH_ADMIN = "/api/auth/admin/login";
        adminToken = fetchToken(AUTH_ADMIN, StringifyJSON.toJSON(adminDTO));
    }
    protected void retrieveUserToken(UserClientDTO userDTO) throws Exception {
        String AUTH_USER = "/api/auth/register/client";
        userToken = fetchToken(AUTH_USER, StringifyJSON.toJSON(userDTO));
    }

    private String fetchToken(String path, String json) throws Exception {
        var result = mockMvc.perform(post(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn()
                .getResponse()
                .getContentAsString();

        var parser = new JacksonJsonParser();
        return parser.parseMap(result).get("token").toString();
    }
}
