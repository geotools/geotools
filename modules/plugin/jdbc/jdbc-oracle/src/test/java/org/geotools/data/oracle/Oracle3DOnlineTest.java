/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.jdbc.JDBC3DOnlineTest;
import org.geotools.jdbc.JDBC3DTestSetup;

public class Oracle3DOnlineTest extends JDBC3DOnlineTest {

    @Override
    protected JDBC3DTestSetup createTestSetup() {
        return new Oracle3DTestSetup();
    }

    /**
     * This test is overridden to disable it, since it is a known issue. The issue is that the
     * Oracle driver writes Rectangles as Oracle SDO Rectangle structures, which don't preserve 3D
     * See GEOT-4133
     */
    @Override
    public void testCreateSchemaAndInsertPolyRectangle() {}

    /**
     * This test does not work in Oracle, not even using plain SQL. Override to disable. see
     * https://osgeo-org.atlassian.net/browse/GEOT-6534 GEOT-6534
     *
     * <p>It sends the following 2D query to the database:
     *
     * <pre>{@code
     * SELECT count(*) FROM GEOTOOLS.LINE3D
     * WHERE SDO_RELATE(
     * GEOM,
     * MDSYS.SDO_GEOMETRY(2003,4326,NULL,MDSYS.SDO_ELEM_INFO_ARRAY(1,1003,3),MDSYS.SDO_ORDINATE_ARRAY(2.0,1.0,3.0,2.0)),
     * 'mask=anyinteract querytype=WINDOW'
     * ) = 'TRUE'
     * }</pre>
     *
     * returns 2 (==all) records and not 0 as the test expects.
     *
     * <p>But even eseding a 3D select window will still fail:
     *
     * <pre>{@code
     * SELECT count(*) FROM GEOTOOLS.LINE3D
     * WHERE SDO_RELATE(
     * GEOM,
     * MDSYS.SDO_GEOMETRY(3003,4326,NULL,MDSYS.SDO_ELEM_INFO_ARRAY(1,1003,3),MDSYS.SDO_ORDINATE_ARRAY(2.0,1.0,100.0,3.0,2.0,101.0)),
     * 'mask=anyinteract querytype=WINDOW'
     * ) = 'TRUE'
     * }</pre>
     */
    @Override
    public void testBBOX3DOutsideLine() {}
}
