package org.opensrp.common.util;


import org.junit.Test;

import java.util.Map;
import java.util.TreeMap;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class EasyMapTest {

    @Test
    public void basicTest() {
        EasyMap<String, Integer> easyMap = new EasyMap<>();

        assertTrue(easyMap.map().isEmpty());
        assertNull(easyMap.map().get(1));

        easyMap.put("a",1);

        assertEquals(1, (int)easyMap.map().get("a"));

        Map<String, Integer> map = new TreeMap<>();
        map.put("b", 2);
        map.put("c", 3);
        map.put("d", 4);

        easyMap.putAll(map);

        assertEquals(2, (int)easyMap.map().get("b"));
        assertEquals(3, (int)easyMap.map().get("c"));

    }

    @Test
    public void constructorTestForMapOfMethod() {
        Map<String, Integer> easyMap = EasyMap.mapOf("a", 1);
        assertEquals(1, easyMap.size());
        assertEquals(1, (int) easyMap.get("a"));
        assertNull(easyMap.get("s"));
    }

    @Test
    public void constructorTestForCreateMethod() {
        EasyMap<String, Integer> easyMap2 = EasyMap.create("b", 2);
        assertEquals(1, easyMap2.map().size());
        assertEquals(2, (int) easyMap2.map().get("b"));
        assertNull(easyMap2.map().get("s"));
    }
}
