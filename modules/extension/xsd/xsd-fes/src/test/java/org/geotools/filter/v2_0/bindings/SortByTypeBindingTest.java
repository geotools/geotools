package org.geotools.filter.v2_0.bindings;

import org.eclipse.emf.common.util.UniqueEList;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.v2_0.FES;
import org.geotools.filter.v2_0.FESTestSupport;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;
import org.w3c.dom.Document;

public class SortByTypeBindingTest extends FESTestSupport {

    public void testParse() throws Exception {
        String xml =
                "      <fes:SortBy xmlns:fes='http://www.opengis.net/fes/2.0'> "
                        + "         <fes:SortProperty> "
                        + "            <fes:ValueReference>myns:depth</fes:ValueReference> "
                        + "         </fes:SortProperty> "
                        + "         <fes:SortProperty> "
                        + "            <fes:ValueReference>myns:temperature</fes:ValueReference> "
                        + "            <fes:SortOrder>DESC</fes:SortOrder> "
                        + "         </fes:SortProperty> "
                        + "      </fes:SortBy>";
        buildDocument(xml);

        SortBy[] sortBy = (SortBy[]) parse();
        assertEquals(2, sortBy.length);

        assertEquals("myns:depth", sortBy[0].getPropertyName().getPropertyName());
        assertEquals(SortOrder.ASCENDING, sortBy[0].getSortOrder());

        assertEquals("myns:temperature", sortBy[1].getPropertyName().getPropertyName());
        assertEquals(SortOrder.DESCENDING, sortBy[1].getSortOrder());
    }

    public void testEncode() throws Exception {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        SortBy sortBy = ff.sort("myProperty", SortOrder.ASCENDING);

        UniqueEList<SortBy> list = new UniqueEList<>();
        list.add(sortBy);

        Document doc = encode(list, FES.SortBy);
        assertEquals("fes:SortBy", doc.getDocumentElement().getNodeName());
        assertEquals(1, doc.getElementsByTagNameNS(FES.NAMESPACE, "SortProperty").getLength());
        assertEquals(1, doc.getElementsByTagNameNS(FES.NAMESPACE, "SortOrder").getLength());
    }
}
