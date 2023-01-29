package org.pk.route.config;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.cache.HttpCacheStorage;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.cache.CacheConfig;
import org.apache.hc.client5.http.impl.cache.CachingHttpClientBuilder;
import org.apache.hc.client5.http.impl.cache.ManagedHttpCacheStorage;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.pk.route.model.Route;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Slf4j
@Configuration
public class HttpClientConfig {

    @Bean
    RouteCacheInvalidateInterceptor routeCacheInvalidateInterceptor(Cache<String, Optional<Route>> routeCache) {
        return new RouteCacheInvalidateInterceptor(routeCache);
    }

    @Bean
    PoolingHttpClientConnectionManager poolingHttpClientConnectionManager() {
        PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
        poolingHttpClientConnectionManager.setMaxTotal(100);
        return poolingHttpClientConnectionManager;
    }

    @Bean
    CacheConfig cacheConfig() {
        CacheConfig cacheConfig = CacheConfig.custom().setMaxCacheEntries(CacheConfig.DEFAULT_MAX_CACHE_ENTRIES).setMaxObjectSize(1_048_576).build();
        return cacheConfig;
    }

    @Bean
    HttpCacheStorage httpCacheStorage(CacheConfig cacheConfig) {
        HttpCacheStorage httpCacheStorage = new ManagedHttpCacheStorage(cacheConfig);
        return httpCacheStorage;
    }

    @Bean
    public HttpClient httpClient(PoolingHttpClientConnectionManager poolingHttpClientConnectionManager, CacheConfig cacheConfig, HttpCacheStorage httpCacheStorage, RouteCacheInvalidateInterceptor routeCacheInvalidateInterceptor) {
        HttpClient httpClient = CachingHttpClientBuilder.create().setCacheConfig(cacheConfig).setHttpCacheStorage(httpCacheStorage).setConnectionManager(poolingHttpClientConnectionManager).addResponseInterceptorFirst(routeCacheInvalidateInterceptor).build();
        return httpClient;
    }

}
