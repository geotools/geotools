package com.bedatadriven.jackson.datatype.jts;

import java.io.IOException;
import java.util.ServiceConfigurationError;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import tools.jackson.databind.DatabindException;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

public class JtsModuleTest {
    private ObjectMapper mapper;

    @Before
    public void setupMapper() {

        mapper = JsonMapper.builder().addModule(new JtsModule()).build();
    }

    /**
     * Make sure to remove JtsModule from Service registration as Jackson 2 module.
     *
     * <p>https://osgeo-org.atlassian.net/browse/GEOT-7954
     */
    @Test
    public void serviceLoaderTest() {
        try {
            com.fasterxml.jackson.databind.ObjectMapper.findModules();
        } catch (ServiceConfigurationError e) {
            Assert.fail("JtsModule should not be registered as com.fasterxml.jackson.databind.Module");
        }
    }

    @Test(expected = DatabindException.class)
    public void invalidGeometryType() throws IOException {
        String json = "{\"type\":\"Singularity\",\"coordinates\":[]}";
        mapper.readValue(json, Geometry.class);
    }

    @Test(expected = DatabindException.class)
    @Ignore
    public void unsupportedGeometry() throws IOException {
        Geometry unsupportedGeometry = EasyMock.createNiceMock("NonEuclideanGeometry", Geometry.class);
        EasyMock.replay(unsupportedGeometry);

        mapper.writeValue(System.out, unsupportedGeometry);
    }
}
