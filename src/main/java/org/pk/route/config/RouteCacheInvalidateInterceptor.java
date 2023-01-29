package org.pk.route.config;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.EntityDetails;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.HttpResponseInterceptor;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.pk.route.model.Route;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.Optional;

@Slf4j
public class RouteCacheInvalidateInterceptor implements HttpResponseInterceptor {

    private Cache<String, Optional<Route>> routeCache;

    public RouteCacheInvalidateInterceptor(Cache<String, Optional<Route>> routeCache) {
        this.routeCache = routeCache;
    }

    @Override
    public void process(HttpResponse response, EntityDetails entity, HttpContext context) throws HttpException, IOException {
        if (response.getCode() == HttpStatus.OK.value()) {
            log.debug("Invalidating route cache");
            routeCache.invalidateAll();
        }
    }
}
