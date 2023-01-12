/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.h2gis.geotools;

import org.geotools.jdbc.JDBCUuidOnlineTest;
import org.geotools.jdbc.JDBCUuidTestSetup;

public class H2GISUuidTest extends JDBCUuidOnlineTest {

    @Override
    protected JDBCUuidTestSetup createTestSetup() {
        return new H2GISUuidTestSetup(new H2GISUuidTestSetup(new H2GISTestSetup()));
    }
}
