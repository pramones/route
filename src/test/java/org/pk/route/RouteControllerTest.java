package org.pk.route;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pk.route.controller.RouteController;
import org.pk.route.util.ApplicationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToObject;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class RouteControllerTest {

    public static final String COUNTRIES_JSON_FILE = "data/countries.json";
    public static final String API_ROUTING = "/routing/{from}/{to}";
    public static final String ROUTE_NOT_FOUND = "Route not found";
    public static final String COUNTRY_NOT_FOUND = "Country not found";

    private MockMvc mockMvc;
    private MockRestServiceServer mockRestServiceServer;

    @Value("${api.countries}")
    private String API_COUNTRIES;

    @Autowired
    RouteController routeController;

    @Autowired
    private RestTemplate restTemplate;

    @BeforeEach
    void beforeEach(WebApplicationContext context) throws IOException {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        mockRestServiceServer = MockRestServiceServer.createServer(restTemplate);
        expectCountryRequest();
    }

    @AfterEach
    void afterEach() {
        mockMvc = null;
    }

    @Test
    void shouldFindRouteUsatoBra() throws Exception {
        expectCountryRequest();
        mockMvc.perform(get(API_ROUTING, "USA", "BRA")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.route[*]",
                        equalToObject(Arrays.asList(
                                "USA", "MEX", "GTM", "HND", "NIC", "CRI", "PAN", "COL", "BRA"
                        ))))
                .andDo(print());
    }

    @Test
    void shouldFindRouteCzeToIta() throws Exception {
        expectCountryRequest();
        mockMvc.perform(get(API_ROUTING, "CZE", "ITA")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.route[*]",
                        equalToObject(Arrays.asList(
                                "CZE", "AUT", "ITA"
                        ))))
                .andDo(print());
    }

    @Test
    void shouldFindRouteEspToEsp() throws Exception {
        expectCountryRequest();
        mockMvc.perform(get(API_ROUTING, "ESP", "ESP")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.route[*]",
                        equalToObject(Arrays.asList(
                                "ESP"
                        ))))
                .andDo(print());
    }

    @Test
    void shouldFailOnNoRoute() throws Exception {
        mockMvc.perform(get(API_ROUTING, "AUS", "BRA")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.detail", equalTo(ROUTE_NOT_FOUND)))
                .andDo(print());
    }

    @Test
    void shouldFailOnNoCountry() throws Exception {
        mockMvc.perform(get(API_ROUTING, "AUS", "BRA2")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.detail", equalTo(COUNTRY_NOT_FOUND)))
                .andDo(print());
    }

    void expectCountryRequest() throws IOException {
        mockRestServiceServer.expect(requestTo(API_COUNTRIES))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(
                        Files.readString(Path.of(ApplicationUtil.getRootPath(), COUNTRIES_JSON_FILE), StandardCharsets.UTF_8),
                        MediaType.APPLICATION_JSON));
    }
}
