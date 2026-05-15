/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2026, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 */
package org.geotools.tileverse.rangereader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.junit.Test;

/**
 * Verifies that {@link RangeReaderParams#toProperties(Map)} accepts forward-compatible {@code storage.*} parameter keys
 * (canonical in tileverse 2.x) by translating them to the canonical {@code io.tileverse.rangereader.*} form tileverse
 * 1.4 understands.
 */
public class RangeReaderParamsTest {

    @Test
    public void toPropertiesNormalizesFutureKeys() {
        Map<String, Object> connectionParams = new HashMap<>();
        connectionParams.put("pmtiles", "file:///tmp/sample.pmtiles");
        connectionParams.put("storage.provider", "file");
        connectionParams.put("storage.caching.enabled", Boolean.TRUE);
        connectionParams.put("storage.s3.region", "us-west-2");

        Properties props = RangeReaderParams.toProperties(connectionParams);

        assertEquals("file", props.getProperty("io.tileverse.rangereader.provider"));
        assertEquals("true", props.getProperty("io.tileverse.rangereader.caching.enabled"));
        assertEquals("us-west-2", props.getProperty("io.tileverse.rangereader.s3.region"));
        assertFalse(
                "no storage.* keys should remain in the output",
                props.stringPropertyNames().stream().anyMatch(k -> k.startsWith("storage.")));
    }

    @Test
    public void toPropertiesPassesCanonicalKeys() {
        Map<String, Object> connectionParams = new HashMap<>();
        connectionParams.put("pmtiles", "file:///tmp/sample.pmtiles");
        connectionParams.put("io.tileverse.rangereader.provider", "file");
        connectionParams.put("io.tileverse.rangereader.caching.enabled", Boolean.TRUE);

        Properties props = RangeReaderParams.toProperties(connectionParams);

        assertEquals("file", props.getProperty("io.tileverse.rangereader.provider"));
        assertEquals("true", props.getProperty("io.tileverse.rangereader.caching.enabled"));
    }
}
