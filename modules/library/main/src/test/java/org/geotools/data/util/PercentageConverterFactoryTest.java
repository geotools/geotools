/* (c) 2017 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geotools.data.util;

import static org.junit.Assert.*;

import org.geotools.util.Converters;
import org.junit.Test;

public class PercentageConverterFactoryTest {

    @Test
    public void testPercentage() throws Exception {
        assertEquals(0.1f, Converters.convert("10%", Float.class), 0f);
        assertEquals(0.5d, Converters.convert("50%", Double.class), 0d);
        assertEquals(-0.5d, Converters.convert("-50%", Double.class), 0d);
    }
}
