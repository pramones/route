package org.pk.route.controller;

import org.pk.route.model.Route;
import org.pk.route.service.CountryService;
import org.pk.route.service.RouteService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RouteController {

    private final RouteService routeService;

    private final CountryService countryService;

    public RouteController(RouteService routeService, CountryService countryService) {
        this.routeService = routeService;
        this.countryService = countryService;
    }

    // uppercased country codes only
    @GetMapping("/routing/{origin}/{destination}")
    public Route getRouting(@PathVariable("origin") String origin, @PathVariable("destination") String destination) {
        return this.routeService.getRoute(origin, destination);
    }

    @GetMapping("/countries/evict")
    public void evictCountries() {
        countryService.evictCountries();
    }

    @GetMapping("countries/put/{country}")
    public String putCountry(@PathVariable("country") String country) {
        return countryService.putCountries(country);
    }

    @GetMapping("countries/get/{country}")
    public String getCountry(@PathVariable("country") String country) {
        return countryService.getCountries2(country);
    }
}

