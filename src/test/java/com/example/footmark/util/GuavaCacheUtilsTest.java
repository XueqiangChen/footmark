package com.example.footmark.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;


class GuavaCacheUtilsTest {

    @Test
    public void testCache() {
        LoadingCache<String, String> graphs = CacheBuilder.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build(
                        new CacheLoader<String, String>() {
                            @Override
                            public String load(String key) throws Exception {
                                return "hello " + key;
                            }
                        });

        Cache<String, String> cache = CacheBuilder.newBuilder()
                .maximumSize(1000)
                .build(); // look Ma, no CacheLoader

        try {
            String value  = graphs.get("world");
            System.out.println(value);

            String value2 = graphs.get("world");
            System.out.println("value2 is " + value2);

            graphs.asMap();

//            ImmutableMap<String, String> allValues = graphs.getAll(Arrays.asList("world"));

            // this method returns the value associated with the key in the cache, or computes it from the specified
            // Callable and adds it to the cache
            cache.get("foo", () -> "hello foo");
        } catch (ExecutionException e) {
            throw new RuntimeException(e.getCause());
        }

    }

}