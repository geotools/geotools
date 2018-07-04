/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.collection;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.store.FeatureCollectionWrapperTestSupport;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.Test;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;

/** @author ian */
public class SpatialIndexFeatureCollectionTest extends FeatureCollectionWrapperTestSupport {
    private static final Logger LOGGER = Logger.getLogger("SpatialIndexFeatureCollectionTest");

    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);

    /**
     * Test method for {@link
     * org.geotools.data.collection.SpatialIndexFeatureCollection#subCollection(org.opengis.filter.Filter)}.
     */
    @Test
    public void testSimpleSubCollection() {
        // delegate has 5 points running diagonally from -140,45 -> -136,49
        ReferencedEnvelope bbox =
                new ReferencedEnvelope(-145, -139.5, 44, 47, DefaultGeographicCRS.WGS84);

        Filter filter =
                ff.bbox(
                        ff.property(delegate.getSchema().getGeometryDescriptor().getLocalName()),
                        bbox);
        SpatialIndexFeatureCollection collection = null;
        try {
            collection = new SpatialIndexFeatureCollection(delegate);
        } catch (IOException e) {
            LOGGER.log(Level.FINER, e.getMessage(), e);
        }
        SimpleFeatureCollection sub = collection.subCollection(filter);
        assertEquals(1, sub.size());
    }

    @Test
    public void testLineSubCollection() {
        // delegate has 5 Lines running diagonally from -140,45 -> -136,49
        ReferencedEnvelope bbox =
                new ReferencedEnvelope(-145, -139.5, 44, 47, DefaultGeographicCRS.WGS84);

        Filter filter = ff.bbox(ff.property("otherGeom"), bbox);
        SpatialIndexFeatureCollection collection = null;
        try {
            collection = new SpatialIndexFeatureCollection(delegate);
        } catch (IOException e) {
            LOGGER.log(Level.FINER, e.getMessage(), e);
        }
        SimpleFeatureCollection sub = collection.subCollection(filter);
        assertEquals(1, sub.size());
    }

    @Test
    public void testNonSpatialFilter() {
        Filter filter = ff.between(ff.property("someAtt"), ff.literal(2), ff.literal(4));
        SpatialIndexFeatureCollection collection = null;
        try {
            collection = new SpatialIndexFeatureCollection(delegate);
        } catch (IOException e) {
            LOGGER.log(Level.FINER, e.getMessage(), e);
        }
        SimpleFeatureCollection sub = collection.subCollection(filter);
        assertEquals(3, sub.size());
    }

    @Test
    public void testPolygonFilter() throws ParseException {
        SpatialIndexFeatureCollection indexedCollection = new SpatialIndexFeatureCollection();
        String[] wkt = {
            "POLYGON ((-119.97417774025547 33.196727650688246, -124.20325506879374 37.89757918851887, -121.58399416917237 35.3171267970568, -116.79866996862741 32.28105376879453, -119.62649647423575 27.41747397332591, -119.97417774025547 33.196727650688246))",
            "POLYGON ((65.06177329588579 -42.80587059354284, 65.49208429348238 -42.522022826074185, 66.42319791837836 -38.01375647971375, 61.610804166470324 -41.37131573282127, 64.34877590921953 -42.50426700931395, 65.06177329588579 -42.80587059354284))",
            "POLYGON ((-58.25321643962343 -82.38927165318137, -62.00628097604683 -81.93669538339687, -63.925878934383604 -80.1733448545024, -61.18478438478274 -76.53049974015083, -56.93672089519853 -77.44114363127662, -58.25321643962343 -82.38927165318137))",
            "POLYGON ((73.94416557978639 42.13590617848595, 74.96396706093608 44.74654477122582, 75.17706539724284 41.194411916043315, 76.14012827344702 41.30378672806298, 76.32346116734506 39.80285252884591, 73.94416557978639 42.13590617848595))",
            "POLYGON ((174.3167657957211 -86.98916599624629, 171.18193648893796 -90.13332684319256, 167.1986439852051 -88.57310698835934, 164.33926945424184 -91.4023929174037, 167.6066840579414 -95.16544595419158, 174.3167657957211 -86.98916599624629))",
            "POLYGON ((114.66446946709624 26.923461307091898, 116.86718943407843 25.806930508373853, 116.92173188209411 22.66467578472146, 120.35458911244616 18.23434642266621, 117.75933916606633 16.328021142831986, 114.66446946709624 26.923461307091898))",
            "POLYGON ((-109.70421086755613 75.86976513490433, -111.243111530655 78.0557341402728, -114.21700234345307 75.01935930357456, -118.42046183284896 74.44680785290822, -116.52265119284037 71.23202025340255, -109.70421086755613 75.86976513490433))",
            "POLYGON ((-71.33171564347607 -67.4232337461037, -75.58127758360841 -63.39946753762101, -79.05807900058221 -63.283928466994816, -81.44712296365057 -61.1804806120166, -85.95640919263549 -59.80257963586822, -71.33171564347607 -67.4232337461037))",
            "POLYGON ((-80.50273776630162 -66.70199176773544, -77.22636970259796 -62.07190233482849, -72.9501352433325 -64.70305871733284, -77.502674497898 -67.66283385466468, -75.21365963270617 -65.13802436295082, -80.50273776630162 -66.70199176773544))",
            "POLYGON ((34.37909412973852 -13.318776472280689, 38.20683624209362 -12.830670034849854, 35.63848580688023 -13.058067086159358, 39.338208040002 -12.524568935269919, 35.71178753285789 -13.009231341976175, 34.37909412973852 -13.318776472280689))"
        };
        SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();

        typeBuilder.setName("test");
        typeBuilder.setNamespaceURI("test");
        typeBuilder.setCRS(crs);
        typeBuilder.add("polyGeom", Polygon.class);
        typeBuilder.setDefaultGeometry("polyGeom");
        typeBuilder.add("someAtt", Integer.class);

        SimpleFeatureType featureType = (SimpleFeatureType) typeBuilder.buildFeatureType();

        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(featureType);
        WKTReader reader = new WKTReader();
        int i = 100;
        for (String text : wkt) {
            Polygon poly = (Polygon) reader.read(text);
            builder.add(poly);
            builder.add(new Integer(i++));
            SimpleFeature feature = builder.buildFeature(null);

            indexedCollection.add(feature);
        }
        ReferencedEnvelope bbox =
                new ReferencedEnvelope(-120, -42, -93, -33, DefaultGeographicCRS.WGS84);
        Filter filter = ff.bbox(ff.property("polyGeom"), bbox);
        SimpleFeatureCollection sub = indexedCollection.subCollection(filter);
        assertEquals(3, sub.size());
    }
}
