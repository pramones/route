package org.pk.route.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.TimeUnit;

@Configuration
public class ServiceCacheConfig {

    @Bean
    Caffeine serviceCache() {
        return Caffeine.newBuilder()
                .expireAfterWrite(300, TimeUnit.SECONDS);
    }

    @Bean
    @Primary
    CacheManager serviceCacheManager(@Qualifier("serviceCache") Caffeine caffeine) {
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
        caffeineCacheManager.setCaffeine(caffeine);
        return caffeineCacheManager;
    }

}
