/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.xml.filter;

import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.NotImpl;
import org.geotools.xml.XMLHandlerHints;
import org.opengis.filter.And;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;

public class FilterCompliancePreProcessorTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testSingleNOTHighCompliance() throws Exception {
        FilterFactory2 ff2 = CommonFactoryFinder.getFilterFactory2(null);
        FilterCompliancePreProcessor compliancePreProcessor =
                new FilterCompliancePreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_HIGH);
        Filter not = ff2.not(ff2.isNull(ff2.property("GM_CODE")));
        not.accept(compliancePreProcessor, null);
        Filter processed = compliancePreProcessor.getFilter();
        assertTrue(not instanceof NotImpl);
        assertTrue(processed instanceof NotImpl);
    }

    public void testNestedNOTHighCompliance() throws Exception {
        FilterFactory2 ff2 = CommonFactoryFinder.getFilterFactory2(null);

        List<String> attrs = new ArrayList<>();
        attrs.add("gm_naam");
        attrs.add("gm_code");

        List<Filter> filters = new ArrayList<>();
        for (String attr : attrs) {
            Filter f = ff2.not(ff2.isNull(ff2.property(attr)));
            filters.add(f);
        }
        And and = ff2.and(filters);
        FilterTransformer transform = new FilterTransformer();
        transform.setIndentation(2);

        String xml = transform.transform(and);
        FilterCompliancePreProcessor compliancePreProcessor =
                new FilterCompliancePreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_HIGH);
        and.accept(compliancePreProcessor, null);

        Filter processed = compliancePreProcessor.getFilter();
        String xmlProcessed = transform.transform(processed);
        assertTrue(xmlProcessed.contains("Not"));
        assertTrue(xml.contains("Not"));
    }
}
