/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2023, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.oracle;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.geotools.api.data.DataAccessFactory;
import org.junit.Test;

public class OracleOCIDataSourceOnlineTest {
    /** Test that the OracleNGOCIDataStoreFactory has needed parameters. */
    @Test
    public void testParams() {
        OracleNGOCIDataStoreFactory ociFactory = new OracleNGOCIDataStoreFactory();
        DataAccessFactory.Param[] paramsOCI = ociFactory.getParametersInfo();
        List<String> ociKeys = new ArrayList<>();
        for (DataAccessFactory.Param p : paramsOCI) {
            ociKeys.add(p.key);
        }
        assertThat(ociKeys, hasItems(OracleNGOCIDataStoreFactory.ALIAS.key));
        assertThat(ociKeys, hasItems(OracleNGDataStoreFactory.USER.key));
        assertThat(ociKeys, hasItems(OracleNGDataStoreFactory.PASSWD.key));
        assertThat(ociKeys, hasItems(OracleNGDataStoreFactory.VALIDATECONN.key));
        assertThat(ociKeys, hasItems(OracleNGDataStoreFactory.MAX_OPEN_PREPARED_STATEMENTS.key));
        assertThat(ociKeys, hasItems(OracleNGDataStoreFactory.PK_METADATA_TABLE.key));
        assertThat(ociKeys, hasItems(OracleNGDataStoreFactory.LOOSEBBOX.key));
        assertThat(ociKeys, hasItems(OracleNGDataStoreFactory.ESTIMATED_EXTENTS.key));
        assertThat(ociKeys, hasItems(OracleNGDataStoreFactory.GEOMETRY_METADATA_TABLE.key));
        assertThat(ociKeys, hasItems(OracleNGDataStoreFactory.METADATA_BBOX.key));
        assertThat(ociKeys, hasItems(OracleNGDataStoreFactory.GET_REMARKS.key));
    }
}
