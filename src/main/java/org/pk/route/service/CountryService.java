package org.pk.route.service;

import lombok.extern.slf4j.Slf4j;
import org.pk.route.model.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CountryService {

    private final String COUNTRIES_CACHE = "COUNTRIES_CACHE";

    @Value("${api.countries}")
    private String API_COUNTRIES;

    private final RestTemplate restTemplate;

    public CountryService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Cacheable(value = COUNTRIES_CACHE)
    public Optional<List<Country>> getCountries() {
        log.debug("Retrieving countries [{}] ", API_COUNTRIES);
        RequestEntity request = RequestEntity.get(API_COUNTRIES)
                .accept(MediaType.APPLICATION_JSON)
                .build();
        ResponseEntity<List<Country>> response = restTemplate.exchange(
                request,
                new ParameterizedTypeReference<List<Country>>() {
                }
        );
        int responseCode = response.getStatusCode().value();
        if (responseCode == HttpStatus.OK.value()) {
            log.debug("Successfully retrieved a list of countries [{}] with HTTP Code=[{}]",
                    API_COUNTRIES, responseCode);
            return Optional.ofNullable(response.getBody());
        } else {
            log.debug("Countries cannot be retrieved [{}] with HTTP Code={}.",
                    API_COUNTRIES, responseCode);
            return Optional.empty();
        }
    }
}
