/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.shapefile.dbf;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

public class DbaseFileReaderTest {
    /*
       The nulls.dbf file contains 2 columns: gistool_id (integer) and att_loss (real).
       There are 4 records:
       GISTOOL_ID | ATT_LOSS
       -----------+---------
         98245    | <all spaces>
         98289    | ****************** (= all stars)
         98538    | 0.000000000
         98586    | 5.210000000
    */
    @Test
    public void test() throws Exception {
        InputStream dbf =
                this.getClass()
                        .getResourceAsStream(
                                "/org/geotools/data/shapefile/test-data/dbase-file-reader/nulls.dbf");
        DbaseFileReader dbfReader =
                new DbaseFileReader(Channels.newChannel(dbf), false, StandardCharsets.UTF_8);

        Map<Long, Double> records = new HashMap<>();
        while (dbfReader.hasNext()) {
            final Object[] fields = dbfReader.readEntry();
            records.put((Long) fields[0], (Double) fields[1]);
        }
        dbfReader.close();
        dbf.close();

        assertThat(records.get(98586L), is(5.21));
        assertThat(records.get(98538L), is(0.0));
        assertThat(records.get(98289L), nullValue());
        assertThat(records.get(98245L), nullValue()); // this fails with 0.0
    }
}
