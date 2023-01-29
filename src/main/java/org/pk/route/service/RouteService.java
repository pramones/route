package org.pk.route.service;

import org.pk.route.exception.ApplicationException;
import org.pk.route.exception.RouteNotFoundException;
import org.pk.route.finder.RouteCacheStorage;
import org.pk.route.finder.RouteFinder;
import org.pk.route.model.Country;
import org.pk.route.model.Route;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteService {

    private final String ROUTES_CACHE = "ROUTES_CACHE";

    private final CountryService countryService;

    private RouteCacheStorage routeCacheStorage;

    public RouteService(CountryService countryService, RouteCacheStorage routeCacheStorag) {
        this.countryService = countryService;
        this.routeCacheStorage = routeCacheStorag;
    }

    @Cacheable(ROUTES_CACHE)
    public Route getRoute(String origin, String destination) {
        List<Country> countries = countryService.getCountries()
                .orElseThrow(() -> new ApplicationException("Cannot retrieve country list."));
        Route route = RouteFinder.builder()
                .origin(origin)
                .destination(destination)
                .countries(countries)
                .cacheStorage(routeCacheStorage)
                .cached(true)
                .build()
                .findRoute().orElseThrow(() -> new RouteNotFoundException());
        return route;
    }
}
