package org.geotools.test;

import org.geotools.data.complex.DataAccessRegistry;
import org.geotools.xml.AppSchemaXSDRegistry;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;

public class AppSchemaTestSupport {
    
    @AfterClass
    public static void oneTimeTearDown() throws Exception {
        DataAccessRegistry.unregisterAll();
        AppSchemaXSDRegistry.getInstance().dispose();
    }

}
