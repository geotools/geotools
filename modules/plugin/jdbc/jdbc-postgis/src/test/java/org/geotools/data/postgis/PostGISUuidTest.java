/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.geotools.data.postgis;

import org.geotools.jdbc.JDBCUuidTest;
import org.geotools.jdbc.JDBCUuidTestSetup;

/**
 *
 * @author kbyte
 */
public class PostGISUuidTest extends JDBCUuidTest {

    @Override
    protected JDBCUuidTestSetup createTestSetup() {
        return new PostGISUuidTestSetup(new PostGISTestSetup());
    }

}

