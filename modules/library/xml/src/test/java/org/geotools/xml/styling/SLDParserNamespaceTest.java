/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.xml.styling;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.PropertyIsEqualTo;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.style.FeatureTypeStyle;
import org.geotools.api.style.Rule;
import org.geotools.api.style.Style;
import org.geotools.api.style.StyleFactory;
import org.geotools.api.style.Symbolizer;
import org.geotools.factory.CommonFactoryFinder;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * Tests whether SLD Parser encodes Namespace in PropertyNames
 *
 * @author Niels Charlier
 */
public class SLDParserNamespaceTest {

    public static String SLD =
            "<StyledLayerDescriptor xmlns=\"http://www.opengis.net/sld\" version=\"1.0.0\" xmlns:gml=\"http://www.opengis.net/gml\" xmlns:ogc=\"http://www.opengis.net/ogc\" >"
                    + " <NamedLayer>"
                    + "  <Name>layer</Name>"
                    + "  <UserStyle>"
                    + "   <Name>style</Name>"
                    + "   <FeatureTypeStyle>"
                    + "    <Rule xmlns:gsml=\"urn:cgi:xmlns:CGI:GeoSciML:2.0\">"
                    + "      <ogc:Filter xmlns:xlink=\"http://www.w3.org/1999/xlink\">"
                    + "      <ogc:PropertyIsEqualTo>"
                    + "         <ogc:PropertyName>gsml:specification/gml:GeologicFeature/@xlink:href</ogc:PropertyName>"
                    + "         <ogc:Literal>foo</ogc:Literal>"
                    + "      </ogc:PropertyIsEqualTo>"
                    + "      </ogc:Filter>"
                    + "      <PolygonSymbolizer>"
                    + "           <Geometry> "
                    + "              <ogc:PropertyName>gsml:shape</ogc:PropertyName>"
                    + "           </Geometry> "
                    + "        <Fill>"
                    + "        <CssParameter name=\"fill\">#FF0000</CssParameter>"
                    + "       </Fill>"
                    + "      </PolygonSymbolizer>"
                    + "    </Rule>"
                    + "   </FeatureTypeStyle>"
                    + "  </UserStyle>"
                    + " </NamedLayer>"
                    + "</StyledLayerDescriptor>";

    static StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory(null);

    @Test
    public void testNamespace() throws Exception {
        SLDParser parser = new SLDParser(styleFactory, input());
        Style[] styles = parser.readXML();
        Assert.assertEquals(styles.length, 1);
        Style style = styles[0];
        List<FeatureTypeStyle> ftstyles = style.featureTypeStyles();
        Assert.assertEquals(ftstyles.size(), 1);
        FeatureTypeStyle ftstyle = ftstyles.get(0);
        List<Rule> rules = ftstyle.rules();
        Assert.assertEquals(rules.size(), 1);
        Rule rule = rules.get(0);
        Filter filter = rule.getFilter();
        assert filter instanceof PropertyIsEqualTo;
        Expression expr = ((PropertyIsEqualTo) filter).getExpression1();
        assert expr instanceof PropertyName;
        NamespaceSupport ns = ((PropertyName) expr).getNamespaceContext();
        Assert.assertEquals(ns.getURI("xlink"), "http://www.w3.org/1999/xlink");
        Assert.assertEquals(ns.getURI("gml"), "http://www.opengis.net/gml");
        Assert.assertEquals(ns.getURI("gsml"), "urn:cgi:xmlns:CGI:GeoSciML:2.0");

        Symbolizer s = rule.symbolizers().get(0);
        expr = s.getGeometry();
        assert expr instanceof PropertyName;
        ns = ((PropertyName) expr).getNamespaceContext();
        Assert.assertNull(ns.getURI("xlink"));
        Assert.assertEquals(ns.getURI("gml"), "http://www.opengis.net/gml");
        Assert.assertEquals(ns.getURI("gsml"), "urn:cgi:xmlns:CGI:GeoSciML:2.0");
    }

    InputStream input() {
        return new ByteArrayInputStream(SLD.getBytes(StandardCharsets.UTF_8));
    }
}
