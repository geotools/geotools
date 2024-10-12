/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.identity.FeatureId;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.referencing.CRS;
import org.geotools.test.xml.XmlTestSupport;
import org.junit.Assert;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.WKTReader;
import org.xmlunit.diff.Diff;

public class FilterTransformerTest extends XmlTestSupport {

    FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

    FilterTransformer transform = new FilterTransformer();

    @Override
    protected Map<String, String> getNamespaces() {
        return namespaces(
                Namespace("ogc", "http://www.opengis.net/ogc"),
                Namespace("gml", "http://www.opengis.net/gml"),
                Namespace("xsi", "http://www.w3.org/2001/XMLSchema-instance"));
    }

    @Test
    public void testIdEncode() throws Exception {
        String expected =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?><features><ogc:FeatureId xmlns=\"http://www.opengis.net/ogc\" xmlns:ogc=\"http://www.opengis.net/ogc\" xmlns:gml=\"http://www.opengis.net/gml\" fid=\"FID.1\"/><ogc:FeatureId xmlns:ogc=\"http://www.opengis.net/ogc\" fid=\"FID.2\"/></features>";

        HashSet<FeatureId> set = new LinkedHashSet<>();
        set.add(ff.featureId("FID.1"));
        set.add(ff.featureId("FID.2"));
        Filter filter = ff.id(set);

        String output = transform.transform(filter);
        Assert.assertNotNull("got xml", output);

        String actual = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<features>"
                + output.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "")
                + "</features>";

        Diff diff = diffSimilar(expected, actual);
        Assert.assertFalse(diff.toString(), diff.hasDifferences());
    }

    @Test
    public void testEncodeLong() throws Exception {
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><ogc:PropertyIsGreaterThan "
                + "xmlns=\"http://www.opengis.net/ogc\" xmlns:ogc=\"http://www.opengis.net/ogc\" "
                + "xmlns:gml=\"http://www.opengis.net/gml\">"
                + "<ogc:PropertyName>MYATT</ogc:PropertyName>"
                + "<ogc:Literal>50000000</ogc:Literal></ogc:PropertyIsGreaterThan>";

        Filter filter = ff.greater(ff.property("MYATT"), ff.literal(50000000l));
        String output = transform.transform(filter);
        Assert.assertNotNull("got xml", output);

        Diff diff = diffSimilar(expected, output);
        Assert.assertFalse(diff.toString(), diff.hasDifferences());
    }

    @Test
    public void testEncodeSRSNameLonLat() throws Exception {
        // create a georeferenced geometry
        CoordinateReferenceSystem wgs84 = CRS.decode("EPSG:4326", true);
        Geometry point = new WKTReader().read("POINT(10 0)");
        point.setUserData(wgs84);

        // a filter on top of it
        Filter filter = ff.overlaps(ff.property("geom"), ff.literal(point));
        String output = transform.transform(filter);
        assertThat(output, hasXPath("//gml:Point/@srsName", equalTo("EPSG:4326")));
    }

    @Test
    public void testEncodeSRSNameLatLon() throws Exception {
        // create a georeferenced geometry
        CoordinateReferenceSystem wgs84 = CRS.decode("urn:ogc:def:crs:EPSG::4326");
        Geometry point = new WKTReader().read("POINT(10 0)");
        point.setUserData(wgs84);

        // a filter on top of it
        Filter filter = ff.overlaps(ff.property("geom"), ff.literal(point));
        String output = transform.transform(filter);
        assertThat(output, hasXPath("//gml:Point/@srsName", equalTo("urn:ogc:def:crs:EPSG::4326")));
    }

    @Test
    public void testEncodeBBox() throws Exception {
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><ogc:BBOX "
                + "xmlns=\"http://www.opengis.net/ogc\" xmlns:ogc=\"http://www.opengis.net/ogc\" "
                + "xmlns:gml=\"http://www.opengis.net/gml\">"
                + "<ogc:PropertyName>geom</ogc:PropertyName>"
                + "<gml:Box><gml:coordinates xmlns:gml=\"http://www.opengis.net/gml\" decimal=\".\" cs=\",\" ts=\" \">-1,50 1,51</gml:coordinates></gml:Box>"
                + "</ogc:BBOX>";

        Filter filter = ff.bbox("geom", -1.0, 50.0, 1.0, 51, "EPSG:4326");
        String out = transform.transform(filter);
        Assert.assertNotNull("got xml", out);

        Diff diff = diffSimilar(expected, out);
        Assert.assertFalse(diff.toString(), diff.hasDifferences());
    }
}
