package org.geotools.filter.v2_0.bindings;

import org.geotools.filter.v2_0.FESTestSupport;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;

public class SortByTypeBindingTest extends FESTestSupport {

    public void testParse() throws Exception {
        String xml = 
        "      <fes:SortBy xmlns:fes='http://www.opengis.net/fes/2.0'> " + 
        "         <fes:SortProperty> " + 
        "            <fes:ValueReference>myns:depth</fes:ValueReference> " + 
        "         </fes:SortProperty> " + 
        "         <fes:SortProperty> " + 
        "            <fes:ValueReference>myns:temperature</fes:ValueReference> " + 
        "            <fes:SortOrder>DESC</fes:SortOrder> " + 
        "         </fes:SortProperty> " + 
        "      </fes:SortBy>";
        buildDocument(xml);

        SortBy[] sortBy = (SortBy[]) parse();
        assertEquals(2, sortBy.length);

        assertEquals("myns:depth", sortBy[0].getPropertyName().getPropertyName());
        assertEquals(SortOrder.ASCENDING, sortBy[0].getSortOrder());
        
        assertEquals("myns:temperature", sortBy[1].getPropertyName().getPropertyName());
        assertEquals(SortOrder.DESCENDING, sortBy[1].getSortOrder());
    }
}
