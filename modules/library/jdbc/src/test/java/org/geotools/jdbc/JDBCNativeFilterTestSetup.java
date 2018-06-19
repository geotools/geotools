/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.jdbc;

/**
 * A table named GT_JDBC_TEST_MEASUREMENTS should be created with following schema:
 *
 * <p>
 *
 * <table>
 *   <tr>
 *     <th>Attribute</th>
 *     <th>Type</th>
 *   </tr>
 *   <tr>
 *     <td>ID</td>
 *     <td>Integer</td>
 *   </tr>
 *   <tr>
 *     <td>CODE</td>
 *     <td>String</td>
 *   </tr>
 *   <tr>
 *     <td>TYPE</td>
 *     <td>String</td>
 *   </tr>
 *   <tr>
 *     <td>VALUE</td>
 *     <td>Double</td>
 *   </tr>
 *   <tr>
 *     <td>LOCATION</td>
 *     <td>Point</td>
 *   </tr>
 * </table>
 *
 * <p>The following values are expected:
 *
 * <p>
 *
 * <table>
 *   <tr>
 *     <th>ID</th>
 *     <th>CODE</th>
 *     <th>TYPE</th>
 *     <th>VALUE</th>
 *     <th>LOCATION</th>
 *   </tr>
 *   <tr>
 *     <td>1</td>
 *     <td>#1</td>
 *     <td>temperature</td>
 *     <td>15.0</td>
 *     <td>POINT (1.0 2.0)</td>
 *   </tr>
 *   <tr>
 *     <td>2</td>
 *     <td>#2</td>
 *     <td>temperature</td>
 *     <td>18.5</td>
 *     <td>POINT (1.0 4.0)</td>
 *   </tr>
 *   <tr>
 *     <td>3</td>
 *     <td>#3</td>
 *     <td>wind</td>
 *     <td>8.5</td>
 *     <td>POINT (2.0 4.0)</td>
 *   </tr>
 *   <tr>
 *     <td>4</td>
 *     <td>#4</td>
 *     <td>wind</td>
 *     <td>4.5</td>
 *     <td>POINT (2.0 2.0)</td>
 *   </tr>
 *   <tr>
 *     <td>5</td>
 *     <td>#5</td>
 *     <td>humidity</td>
 *     <td>0.7</td>
 *     <td>POINT (1.0 4.0)</td>
 *   </tr>
 *   <tr>
 *     <td>6</td>
 *     <td>#6</td>
 *     <td>humidity</td>
 *     <td>0.5</td>
 *     <td>POINT (5.0 4.0)</td>
 *   </tr>
 *   </table>
 */
public abstract class JDBCNativeFilterTestSetup extends JDBCDelegatingTestSetup {

    protected JDBCNativeFilterTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    @Override
    protected final void setUpData() throws Exception {
        try {
            dropMeasurementsTable();
        } finally {
            createMeasurementsTable();
        }
    }

    protected abstract void createMeasurementsTable() throws Exception;

    protected abstract void dropMeasurementsTable() throws Exception;
}
