package org.pk.route.finder;

import com.github.benmanes.caffeine.cache.Cache;
import org.pk.route.model.Route;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RouteCacheStorage {

    private Cache<String, Optional<Route>> cache;

    public RouteCacheStorage(Cache<String, Optional<Route>> cache) {
        this.cache = cache;
    }

    public void set(String routeKey, Optional<Route> route) {
        cache.put(routeKey, route);
    }

    public Optional<Route> getIfPresent(String routeKey) {
        return cache.getIfPresent(routeKey);
    }

    public Optional<Route> get(String routeKey) {
        return cache.getIfPresent(routeKey);
    }

}
