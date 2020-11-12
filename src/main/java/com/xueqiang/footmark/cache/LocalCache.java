package com.xueqiang.footmark.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * https://tech.meituan.com/2017/03/17/cache-about.html
 * 个别场景下，我们只需要简单的缓存数据的功能，而无需关注更多存取、清空策略等深入的特性时，直接编程实现缓存则是最便捷和高效的。
 * 以局部变量map结构缓存部分业务数据，减少频繁的重复数据库I/O操作。缺点仅限于类的自身作用域内，类间无法共享缓存。
 */
public class LocalCache {

    private static Map<Integer, String> localCache = new HashMap<>();

    // 第一种：成员变量或局部变量实现
    public void useLocalCache() {
        // 一个本地的缓存变量
        Map<String, Object> localCacheStoreMap = new HashMap<>();

        List<Object> infosList = this.getInfoList();
        for (Object item : infosList) {
            if (localCacheStoreMap.containsKey(item)) { //缓存命中，使用缓存数据
                System.out.println("缓存命中");
            } else { //缓存未命中，IO获取数据，结果存入缓存
                Object valueObject = this.getInfoFromDB();
                localCacheStoreMap.put(valueObject.toString(), valueObject);
            }
        }
    }

    private List<Object> getInfoList() {
        return new ArrayList<>();
    }

    // 从数据库中获取
    private Object getInfoFromDB() {
        return new Object();
    }

    /**
     * 第二种：静态变量实现.
     * 最常用的单例实现静态资源缓存
     *
     * 通过静态变量一次获取缓存内存中，减少频繁的I/O读取，静态变量实现类间可共享，进程内可共享，缓存的实时性稍差。
     */
    static {
        localCache.put(1, "test1");
    }

    public static String getName(int id) {
        String name = localCache.get(id);
        if (name == null) {
            name = "未知";
        }

        return name;
    }

    /**
     * 这类缓存实现，优点是能直接在heap区内读写，最快也最方便；缺点同样是受heap区域影响，缓存的数据量非常有限，同时缓存时间受GC影响。
     * 主要满足单机场景下的小数据量缓存需求，同时对缓存数据的变更无需太敏感感知，如上一般配置管理、基础静态数据等场景。
     */

}
