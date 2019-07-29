package org.geotools.data.wfs.internal;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import org.geotools.data.wfs.impl.WFSDataAccessFactory;
import org.junit.Test;

public class WFSConfigTest {

    @Test
    public void testConfiguresConnectionPoolSize() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put(WFSDataAccessFactory.MAX_CONNECTION_POOL_SIZE.getName(), "10");
        WFSConfig config = WFSConfig.fromParams(params);

        assertEquals(10, config.getMaxConnectionPoolSize());
    }

}
