/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.geotools.data.h2;

import org.geotools.jdbc.JDBCUuidOnlineTest;
import org.geotools.jdbc.JDBCUuidTestSetup;

public class H2UuidTest extends JDBCUuidOnlineTest {

    @Override
    protected JDBCUuidTestSetup createTestSetup() {
        return new H2UuidTestSetup(new H2UuidTestSetup(new H2TestSetup()));
    }
}
