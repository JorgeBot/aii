package com.example.aii.util;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;

public class CommonUtils {

    public static <K, V> MultiValueMap<K, V> Map2MultiValueMap(Map<K, V> mp) {
        MultiValueMap<K, V> multiBody = new LinkedMultiValueMap<>();
        if (mp == null || mp.size() == 0) return multiBody;
        mp.forEach((k, v) -> {
            if (v instanceof String) {
                multiBody.set(k, v);
            }
            if (v instanceof List) {
                multiBody.put(k, (List) v);
            }
        });
        return multiBody;
    }
}
