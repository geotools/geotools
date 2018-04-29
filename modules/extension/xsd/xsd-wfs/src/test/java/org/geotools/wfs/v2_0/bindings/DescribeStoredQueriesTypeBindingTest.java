/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.wfs.v2_0.bindings;

import java.io.InputStream;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import net.opengis.wfs20.DescribeStoredQueriesResponseType;
import net.opengis.wfs20.ParameterExpressionType;
import net.opengis.wfs20.QueryExpressionTextType;
import net.opengis.wfs20.StoredQueryDescriptionType;
import org.geotools.wfs.WFS_2_0_0_ParsingTest;
import org.geotools.wfs.v2_0.WFSTestSupport;

public class DescribeStoredQueriesTypeBindingTest extends WFSTestSupport {

    public void testParse() throws Exception {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        docFactory.setNamespaceAware(true);

        InputStream is =
                WFS_2_0_0_ParsingTest.class.getResourceAsStream(
                        "fmi-DescribeStoredQueries_2_0_0.xml");
        try {
            document = docFactory.newDocumentBuilder().parse(is);
        } finally {
            is.close();
        }

        Object o = parse();
        DescribeStoredQueriesResponseType response = (DescribeStoredQueriesResponseType) o;
        assertNotNull(response);

        List<StoredQueryDescriptionType> descs = response.getStoredQueryDescription();
        assertNotNull(descs);
        assertEquals(1, descs.size());

        StoredQueryDescriptionType desc = descs.get(0);
        assertNotNull(desc);

        assertEquals("Hirlam Pressure Grid", desc.getTitle().get(0).getValue());
        assertEquals(
                "Hirlam forecast model's pressure levels as a grid data encoded in GRIB format.",
                desc.getAbstract().get(0).getValue());

        assertEquals(6, desc.getParameter().size());
        ParameterExpressionType param1 = desc.getParameter().get(0);
        assertNotNull(param1);
        assertEquals("starttime", param1.getName());
        assertEquals("dateTime", param1.getType().getLocalPart());

        assertEquals(1, desc.getQueryExpressionText().size());
        QueryExpressionTextType queryExpr = desc.getQueryExpressionText().get(0);
        assertNotNull(queryExpr);

        assertEquals(1, queryExpr.getReturnFeatureTypes().size());
        QName returnType = queryExpr.getReturnFeatureTypes().get(0);

        String language = queryExpr.getLanguage();

        QName expectedReturnType =
                new QName(
                        "http://inspire.ec.europa.eu/schemas/omso/2.0rc3", "GridSeriesObservation");
        String expectedLanguage = "urn:ogc:def:queryLanguage:OGC-WFS::WFS_QueryExpression";

        assertEquals(expectedReturnType, returnType);
        assertEquals(expectedLanguage, language);
    }
}
