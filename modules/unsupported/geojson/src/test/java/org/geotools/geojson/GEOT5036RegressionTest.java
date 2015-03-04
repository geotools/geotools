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

import java.io.IOException;
import java.io.StringReader;
import java.lang.StringBuilder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.Random;

import junit.framework.TestCase;

/**
 * Exploit bug GEOT-5036, concurrent use of GeoJSONUtil.
 * @author Guido Grazioli <guido.grazioli@gmail.com>
 *
 */
public class GEOT5036RegressionTest extends TestCase {

    private Random rand = new Random();
    private Map<Date, Future<String>> expectationMap = new HashMap<Date, Future<String>>();

    public class Task implements Callable<String> {
        private final Date date;

        public Task(Date d) {
            this.date = d;
        }

        public String call() throws Exception {
            StringBuilder sb = new StringBuilder();
            GeoJSONUtil.literal(date, sb);
            return sb.toString();
        }
    }

    public void setUp() throws java.lang.Exception {
        DateFormat sdf = GeoJSONUtil.dateFormatter.get();
        // perform 50 conversions
        for (int i = 0; i < 50; i++) {
            Date date = new Date(System.currentTimeMillis() - rand.nextInt(100) * 1000 * 3600 * 24);
            expectationMap.put(date, null);
        }
    }
    
    public void testDateFormatterResults() throws Exception {
        DateFormat sdf = GeoJSONUtil.dateFormatter.get();

        // pool with 8 threads
        ExecutorService exec = Executors.newFixedThreadPool(8);

        // perform date conversions
        for (Date key : expectationMap.keySet()) {
            Task task = new Task(key);
            expectationMap.put(key, exec.submit(task));
        }
        exec.shutdown();

        // look at the results
        for (Map.Entry<Date, Future<String>> entry : expectationMap.entrySet()) {
            assertEquals("incorrect date string was returned: ", entry.getValue().get(),
                    "\"" + sdf.format(entry.getKey()) + "\"");
        }
    }

}
