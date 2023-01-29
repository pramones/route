package org.pk.route.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.cache.HttpCacheStorage;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.cache.CacheConfig;
import org.apache.hc.client5.http.impl.cache.CachingHttpClientBuilder;
import org.apache.hc.client5.http.impl.cache.ManagedHttpCacheStorage;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class HttpClientConfig {

    @Bean
    PoolingHttpClientConnectionManager poolingHttpClientConnectionManager() {
        PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
        poolingHttpClientConnectionManager.setMaxTotal(100);
        return poolingHttpClientConnectionManager;
    }

    @Bean
    CacheConfig cacheConfig() {
        CacheConfig cacheConfig = CacheConfig
                .custom()
                .setMaxCacheEntries(CacheConfig.DEFAULT_MAX_CACHE_ENTRIES)
                .build();
        return cacheConfig;
    }

    @Bean
    HttpCacheStorage httpCacheStorage(CacheConfig cacheConfig) {
        HttpCacheStorage httpCacheStorage = new ManagedHttpCacheStorage(cacheConfig);
        return httpCacheStorage;
    }

    @Bean
    public HttpClient httpClient(
            PoolingHttpClientConnectionManager poolingHttpClientConnectionManager,
            CacheConfig cacheConfig,
            HttpCacheStorage httpCacheStorage
    ) {
        HttpClient httpClient = CachingHttpClientBuilder
                .create()
                .setCacheConfig(cacheConfig)
                .setHttpCacheStorage(httpCacheStorage)
                .setConnectionManager(poolingHttpClientConnectionManager)
                .build();
        return httpClient;
    }

}
