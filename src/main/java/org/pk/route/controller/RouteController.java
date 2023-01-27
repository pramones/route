package org.pk.route.controller;

import org.pk.route.model.Route;
import org.pk.route.service.RouteService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RouteController {

    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    // uppercased country codes only
    @GetMapping("/routing/{origin}/{destination}")
    public Route getRouting(@PathVariable("origin") String origin, @PathVariable("destination") String destination) {
        return this.routeService.getRoute(origin, destination);
    }
}

