package org.geotools.wps.v2_0;

import net.opengis.wps20.DataInputType;
import net.opengis.wps20.ExecuteRequestType;
import net.opengis.wps20.OutputDefinitionType;
import org.geotools.xsd.Parser;
import org.junit.Assert;
import org.junit.Test;

public class WPSExecuteTest extends WPSTestSupport {

    @Test
    public void testParse() throws Exception {
        Parser parser = new Parser(createConfiguration());

        Object o = parser.parse(getClass().getResourceAsStream("wpsExecuteRequestExample.xml"));
        Assert.assertTrue(o instanceof ExecuteRequestType);

        ExecuteRequestType executeRequestType = (ExecuteRequestType) o;

        Assert.assertNotNull(executeRequestType);
        Assert.assertNotNull(executeRequestType.getIdentifier());
        Assert.assertEquals(
                "http://my.wps.server/processes/proximity/Planar-Buffer",
                executeRequestType.getIdentifier().getValue());

        Assert.assertNotNull(executeRequestType.getInput());
        Assert.assertEquals(2, executeRequestType.getInput().size());

        DataInputType input = executeRequestType.getInput().get(0);
        Assert.assertNotNull(input);
        Assert.assertNotNull("INPUT_GEOMETRY", input.getId());
        Assert.assertNotNull(input.getReference());
        Assert.assertNotNull(
                "http://some.data.server/mygmldata.xml", input.getReference().getHref());

        input = executeRequestType.getInput().get(1);
        Assert.assertNotNull(input);
        Assert.assertNotNull("DISTANCE", input.getId());
        Assert.assertNotNull(input.getData());
        Assert.assertEquals("10.0", input.getData().getMixed().getValue(0));

        Assert.assertNotNull(executeRequestType.getOutput());
        Assert.assertEquals(1, executeRequestType.getOutput().size());

        OutputDefinitionType output = executeRequestType.getOutput().get(0);
        Assert.assertNotNull(output);
        Assert.assertNotNull("BUFFERED_GEOMETRY", output.getId());
        Assert.assertNotNull(output.getTransmission());
        Assert.assertEquals("reference", output.getTransmission().getLiteral());
    }
}
