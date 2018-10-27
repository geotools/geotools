package org.geotools.wfs.v2_0.bindings;

import java.io.StringReader;
import javax.xml.namespace.QName;
import net.opengis.wfs20.LockFeatureType;
import net.opengis.wfs20.QueryType;
import org.geotools.wfs.v2_0.WFSConfiguration;
import org.geotools.wfs.v2_0.WFSTestSupport;
import org.geotools.xsd.Parser;
import org.junit.Test;
import org.opengis.filter.Id;

public class LockFeatureTypeBindingTest extends WFSTestSupport {

    public void testParse1() throws Exception {
        String xml =
                "<LockFeature "
                        + "   version='2.0.0' "
                        + "   service='WFS' "
                        + "   lockAction='SOME' "
                        + "   xmlns='http://www.opengis.net/wfs/2.0' "
                        + "   xmlns:wfs='http://www.opengis.net/wfs/2.0' "
                        + "   xmlns:fes='http://www.opengis.net/fes/2.0' "
                        + "   xmlns:myns='http://www.someserver.com/myns' "
                        + "   xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' "
                        + "   xsi:schemaLocation='http://www.opengis.net/wfs/2.0 "
                        + "                       http://schemas.opengis.net/wfs/2.0/wfs.xsd'> "
                        + "   <Query typeNames='myns:InWaterA_1M'> "
                        + "      <fes:Filter> "
                        + "         <fes:ResourceId rid='InWaterA_1M.1013'/> "
                        + "         <fes:ResourceId rid='InWaterA_1M.1014'/> "
                        + "         <fes:ResourceId rid='InWaterA_1M.1015'/> "
                        + "         <fes:ResourceId rid='InWaterA_1M.1016'/> "
                        + "         <fes:ResourceId rid='InWaterA_1M.1017'/> "
                        + " "
                        + "      </fes:Filter> "
                        + "   </Query> "
                        + "</LockFeature>";
        buildDocument(xml);

        LockFeatureType lf = (LockFeatureType) parse();
        assertNotNull(lf);

        assertEquals(1, lf.getAbstractQueryExpression().size());
        QueryType q = (QueryType) lf.getAbstractQueryExpression().get(0);

        assertTrue(
                q.getTypeNames()
                        .contains(new QName("http://www.someserver.com/myns", "InWaterA_1M")));
        Id f = (Id) q.getFilter();
        assertEquals(5, f.getIDs().size());
    }

    @Test
    public void testParseEmptyLock() throws Exception {
        String xml =
                "<wfs:LockFeature xmlns:wfs=\"http://www.opengis.net/wfs/2.0\" "
                        + "lockId=\"GeoServer_eb5fd6b6b6024d5\" service=\"WFS\" version=\"2.0.0\"> "
                        + "</wfs:LockFeature>";

        Parser p = new Parser(new WFSConfiguration());
        p.validate(new StringReader(xml));

        assertTrue(p.getValidationErrors().isEmpty());
    }
}
