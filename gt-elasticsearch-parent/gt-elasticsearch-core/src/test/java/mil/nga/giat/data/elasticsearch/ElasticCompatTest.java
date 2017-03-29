/**
 * This file is hereby placed into the Public Domain. This means anyone is
 * free to do whatever they wish with this file.
 */
package mil.nga.giat.data.elasticsearch;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;

import org.elasticsearch.common.geo.GeoPoint;

public class ElasticCompatTest {

    @Before
    public void setup() {
        ElasticCompatLoader.setCompat(null);
    }

    @Test
    public void testDefaultCompatLoader() {
        assertTrue(ElasticCompatLoader.getCompat(null) instanceof ElasticCompat5);
    }

    @Test
    public void testCachedCompatLoader() {
        ElasticCompat compat = ElasticCompatLoader.getCompat(null);
        assertEquals(compat, ElasticCompatLoader.getCompat(null));
    }

    @Test(expected=RuntimeException.class)
    public void testOverrideCompatLoader() {
        ElasticCompatLoader.getCompat("ElasticCompat0");
    }

    @Test
    public void testElasticCompat5() {
        ElasticCompat compat = new ElasticCompat5();
        assertTrue(compat.newFilterToElastic() instanceof FilterToElastic5);
        assertEquals("value", compat.createSettings("key","value").get("key"));
        GeoPoint point = compat.decodeGeohash(compat.encodeGeohash(1, 2, 10));
        assertEquals(2, point.getLat(), 1e-4);
        assertEquals(1, point.getLon(), 1e-4);
        assertEquals(86461000, compat.parseDateTime("1970-01-02 00:01:01", "yyyy-mm-dd HH:mm:ss").getTime());
        assertFalse(compat.isAnalyzed(new HashMap<>()));
        assertFalse(compat.isAnalyzed(ImmutableMap.of("type", "keyword")));
        assertFalse(compat.isAnalyzed(ImmutableMap.of("type", ImmutableMap.of("type", "keyword"))));
        assertFalse(compat.isAnalyzed(ImmutableMap.of("type", "not_valid")));
        assertTrue(compat.isAnalyzed(ImmutableMap.of("type", "text")));
    }
    

    @Test(expected=IOException.class)
    public void testCreateClient() throws IOException {
        ElasticCompat compat = new ElasticCompat5();
        compat.createClient("localhost", 9285, "elasticsearch");
    }

}
