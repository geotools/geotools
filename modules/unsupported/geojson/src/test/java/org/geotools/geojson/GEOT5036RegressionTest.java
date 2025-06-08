/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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
 */
package org.geotools.geojson;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.apache.commons.lang3.time.FastDateFormat;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Exploit bug GEOT-5036, concurrent use of GeoJSONUtil.
 *
 * @author Guido Grazioli <guido.grazioli@gmail.com>
 */
public class GEOT5036RegressionTest {

    private final Random rand = new Random();
    private final Map<Date, Future<String>> expectationMap = new HashMap<>();

    public static class Task implements Callable<String> {
        private final Date date;

        public Task(Date d) {
            this.date = d;
        }

        @Override
        public String call() throws Exception {
            final StringBuilder sb = new StringBuilder();
            GeoJSONUtil.literal(date, sb);
            return sb.toString();
        }
    }

    @Before
    public void setUp() throws Exception {
        // perform 50 conversions
        for (int i = 0; i < 50; i++) {
            final Date date = new Date(System.currentTimeMillis() - rand.nextInt(100) * 1000 * 3600 * 24);
            expectationMap.put(date, null);
        }
    }

    @Test
    public void testDateFormatterResults() throws Exception {
        final FastDateFormat sdf = GeoJSONUtil.dateFormatter;

        // pool with 8 threads
        final ExecutorService exec = Executors.newFixedThreadPool(8);

        // perform date conversions
        for (final Date key : expectationMap.keySet()) {
            final Task task = new Task(key);
            expectationMap.put(key, exec.submit(task));
        }
        exec.shutdown();

        // look at the results
        for (final Map.Entry<Date, Future<String>> entry : expectationMap.entrySet()) {
            Assert.assertEquals(
                    "incorrect date string was returned: ",
                    entry.getValue().get(),
                    "\"" + sdf.format(entry.getKey()) + "\"");
        }
    }
}
