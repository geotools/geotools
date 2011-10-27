package org.geotools.filter.v2_0.bindings;

import static org.opengis.filter.identity.Version.Action.ALL;
import static org.opengis.filter.identity.Version.Action.FIRST;
import static org.opengis.filter.identity.Version.Action.LAST;
import static org.opengis.filter.identity.Version.Action.NEXT;
import static org.opengis.filter.identity.Version.Action.PREVIOUS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.identity.ResourceIdImpl;
import org.geotools.filter.v2_0.FESTestSupport;
import org.geotools.xml.impl.DatatypeConverterImpl;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.Id;
import org.opengis.filter.identity.Identifier;
import org.opengis.filter.identity.ResourceId;
import org.opengis.filter.identity.Version;

public class ResourceIdTypeBindingTest extends FESTestSupport {

    public void testParse() throws Exception {
        String xml = "<fes:Filter "
                + "   xmlns:fes='http://www.opengis.net/fes/2.0' "
                + "   xmlns:gml='http://www.opengis.net/gml/3.2' "
                + "   xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' "
                + "   xsi:schemaLocation='http://www.opengis.net/fes/2.0 http://schemas.opengis.net/filter/2.0/filterAll.xsd"
                + " http://www.opengis.net/gml/3.2 http://schemas.opengis.net/gml/3.2.1/gml.xsd'>"
                + "   <fes:ResourceId rid=\"rid1@abc\" previousRid=\"previous1\" version=\"FIRST\"/> "
                + "   <fes:ResourceId rid=\"rid2\" version=\"LAST\"/> "
                + "   <fes:ResourceId rid=\"rid3\" version=\"PREVIOUS\"/> "
                + "   <fes:ResourceId rid=\"rid4\" version=\"NEXT\"/> "
                + "   <fes:ResourceId rid=\"rid5\" version=\"ALL\"/> "
                + "   <fes:ResourceId rid=\"rid6\" previousRid=\"previous2\" version=\"4\" startDate=\"1977-01-17T01:05:40Z\" endDate=\"2011-07-29T23:49:40Z\" /> "
                + "   <fes:ResourceId rid=\"rid7@123\" version=\"1977-01-17T01:05:40Z\"/> "
                + "</fes:Filter>";

        buildDocument(xml);

        Id filter = (Id) parse();
        assertNotNull(filter);
        assertEquals(7, filter.getIdentifiers().size());
        List<ResourceId> ids = new ArrayList<ResourceId>(7);
        for (Identifier id : filter.getIdentifiers()) {
            assertTrue(id instanceof ResourceId);
            ids.add((ResourceId) id);
        }
        Collections.sort(ids, new Comparator<ResourceId>() {
            @Override
            public int compare(ResourceId o1, ResourceId o2) {
                return o1.getRid().compareTo(o2.getRid());
            }
        });

        final DatatypeConverterImpl dateParser = DatatypeConverterImpl.getInstance();
        final Date date1 = dateParser.parseDateTime("1977-01-17T01:05:40Z").getTime();
        final Date date2 = dateParser.parseDateTime("2011-07-29T23:49:40Z").getTime();
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

        // This is in consistent as the FIRST entry cannot have a previous1
        ResourceIdImpl resourceId =  new ResourceIdImpl("rid1", "abc", new Version(FIRST));
        resourceId.setPreviousRid("previous1");
        assertEquals( resourceId.getID(), ids.get(0).getID());
        
        assertEquals(ff.resourceId("rid2", "", new Version(LAST)).getID(),
                     ids.get(1).getID());
        assertEquals(ff.resourceId("rid3", "", new Version(PREVIOUS)).getID(),
                     ids.get(2).getID());
        assertEquals(ff.resourceId("rid4", "", new Version(NEXT)).getID(),
                     ids.get(3).getID());
        assertEquals(ff.resourceId("rid5", "", new Version(ALL)).getID(),
                     ids.get(4).getID());
        
        // This is inconsistent as date and resource based query cannot be used at the same time
        ResourceIdImpl resourceId2 =  new ResourceIdImpl("rid6", "", new Version(4));
        resourceId2.setPreviousRid("previous2");
        resourceId2.setStartTime(date1);
        resourceId2.setEndTime(date2);
        assertEquals( resourceId2.getID(),
                      ids.get(5).getID());
        
        assertEquals(ff.resourceId("rid7", "123",  new Version(date1)).getID(), ids.get(6).getID());
    }
}
