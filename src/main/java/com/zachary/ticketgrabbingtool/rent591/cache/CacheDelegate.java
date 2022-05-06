package com.zachary.ticketgrabbingtool.rent591.cache;

import com.zachary.ticketgrabbingtool.rent591.model.PostModel;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;

public class CacheDelegate {

    private static CacheDelegate instance;

    private CacheManager cacheManager;

    private CacheDelegate() {
        cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache("PostModel",
                        CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, PostModel.class,
                                ResourcePoolsBuilder.heap(100)))
                .build(true);
    }

    public static CacheDelegate getInstance() {
        if (instance == null) {
            instance = new CacheDelegate();
        }
        return instance;
    }

    public void putPostModel(String key, PostModel value) {
        cacheManager.getCache("PostModel", String.class, PostModel.class).put(key, value);
    }

    public void removePostModel(String key) {
        cacheManager.getCache("PostModel", String.class, PostModel.class).remove(key);
    }

    public PostModel getPostModel(String key) {
        return cacheManager.getCache("PostModel", String.class, PostModel.class).get(key);
    }
}
