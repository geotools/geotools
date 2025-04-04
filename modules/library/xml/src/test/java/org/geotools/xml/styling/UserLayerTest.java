/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.geotools.api.data.DataStore;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.PropertyIsLessThan;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.style.ExternalGraphic;
import org.geotools.api.style.FeatureTypeConstraint;
import org.geotools.api.style.FeatureTypeStyle;
import org.geotools.api.style.Graphic;
import org.geotools.api.style.PointSymbolizer;
import org.geotools.api.style.Style;
import org.geotools.api.style.StyleFactory;
import org.geotools.api.style.StyledLayer;
import org.geotools.api.style.StyledLayerDescriptor;
import org.geotools.api.style.UserLayer;
import org.geotools.data.DataUtilities;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.NameImpl;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.CRS;
import org.geotools.styling.StyleBuilder;
import org.geotools.test.TestData;
import org.geotools.util.factory.GeoTools;
import org.junit.Assert;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

public class UserLayerTest {

    private static final String CRS_WKT = "GEOGCS[\"WGS 84\", "
            + "  DATUM[\"WGS_1984\","
            + "    SPHEROID[\"WGS 84\", 6378137.0, 298.257223563, AUTHORITY[\"EPSG\",\"7030\"]],"
            + "    AUTHORITY[\"EPSG\",\"6326\"]],"
            + "  PRIMEM[\"Greenwich\", 0.0, AUTHORITY[\"EPSG\",\"8901\"]],"
            + "  UNIT[\"degree\", 0.017453292519943295],"
            + "  AXIS[\"Lon\", EAST],"
            + "  AXIS[\"Lat\", NORTH],"
            + "  AUTHORITY[\"EPSG\",\"4326\"]]";

    private static final int SRID = 4326;

    private static final String LAYER_NAME = "user-layer-1";

    private static final String ID_COLUMN = "id";

    private static final String GEOMETRY_COLUMN = "geometry";

    private static final String LABEL_COLUMN = "label";

    private static final String ID_1 = "point-1";

    private static final double X_1 = -180.0;

    private static final double Y_1 = -90.0;

    private static final String LABEL_1 = "point location #1";

    private static final String ID_2 = "point-2";

    private static final double X_2 = +180.0;

    private static final double Y_2 = +90.0;

    private static final String LABEL_2 = "point location #2";

    private static final double OPACITY = 0.75;

    private static final String IMAGE_EXT = "jpg";

    private static final String IMAGE_URL = "file:/somewhere/image." + IMAGE_EXT;

    private static final String MY_NAMESPACE = "integeo";

    private static final URI MY_URI;

    private static final String MY_FEATURE = "myFeature";

    static {
        try {
            MY_URI = new URI("http://geotools.org");
        } catch (URISyntaxException x) {
            throw new ExceptionInInitializerError(x);
        }
    }

    @Test
    public void testUserLayerWithInlineFeatures() throws Exception {
        // create the feature's schema ----------------------------------------
        final CoordinateReferenceSystem crs = CRS.parseWKT(CRS_WKT);
        SimpleFeatureTypeBuilder ftb = new SimpleFeatureTypeBuilder();
        ftb.add(ID_COLUMN, Integer.class);
        ftb.add(GEOMETRY_COLUMN, Point.class, crs);
        ftb.add(LABEL_COLUMN, String.class);
        ftb.setName(MY_FEATURE);
        final SimpleFeatureType schema = ftb.buildFeatureType();

        // create a feature collection ----------------------------------------
        final DefaultFeatureCollection fc = new DefaultFeatureCollection();

        // populate the collection --------------------------------------------
        final PrecisionModel pm = new PrecisionModel(PrecisionModel.FLOATING);
        final GeometryFactory jtsFactory = new GeometryFactory(pm, SRID);

        // create 1st point
        final Point g1 = jtsFactory.createPoint(new Coordinate(X_1, Y_1));
        fc.add(SimpleFeatureBuilder.build(schema, new Object[] {1, g1, LABEL_1}, ID_1));

        // create 2nd point
        final Point g2 = jtsFactory.createPoint(new Coordinate(X_2, Y_2));
        fc.add(SimpleFeatureBuilder.build(schema, new Object[] {2, g2, LABEL_2}, ID_2));

        final DataStore ds = DataUtilities.dataStore(fc);

        // create and populate the layer --------------------------------------
        final StyleFactory sf = CommonFactoryFinder.getStyleFactory(GeoTools.getDefaultHints());

        final UserLayer layer = sf.createUserLayer();
        layer.setName(LAYER_NAME);
        layer.setInlineFeatureType(schema);
        layer.setInlineFeatureDatastore(ds);

        // create a user style and add it to that layer -----------------------
        final Style style = sf.createStyle();

        final StyleBuilder sb = new StyleBuilder(sf);
        final ExternalGraphic overlay = sb.createExternalGraphic(IMAGE_URL, "image/" + IMAGE_EXT);
        final Graphic g = sb.createGraphic(overlay, null, null, OPACITY, Double.NaN, 0.0);
        final PointSymbolizer ps = sb.createPointSymbolizer(g);
        final FeatureTypeStyle fts = sb.createFeatureTypeStyle(ps);
        fts.featureTypeNames().add(new NameImpl(MY_NAMESPACE + ":" + MY_FEATURE));

        style.featureTypeStyles().add(fts);
        layer.addUserStyle(style);

        // create an SLD and populate it with that styled layer ---------------
        final StyledLayerDescriptor sld1 = sf.createStyledLayerDescriptor();
        sld1.addStyledLayer(layer);

        // marshal the SLD to XML ---------------------------------------------
        final Map<URI, String> nsMap = new HashMap<>();
        nsMap.put(MY_URI, MY_NAMESPACE);
        final SLDTransformer sldTransformer = new SLDTransformer(nsMap);
        sldTransformer.setIndentation(2);
        String xml = sldTransformer.transform(sld1);

        // unmarshal it back to an SLD instance -------------------------------
        final InputStream is = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
        final SLDParser parser = new SLDParser(sf);
        parser.setInput(is);
        final StyledLayerDescriptor sld2 = parser.parseSLD();
        xml = sldTransformer.transform(sld2);

        // check both SLDs ----------------------------------------------------
        final StyledLayer[] layers = sld2.getStyledLayers();
        Assert.assertNotNull("Styled layers array MUST NOT be null", layers);
        Assert.assertEquals("Styled layers array MUST be 1-element long", 1, layers.length);
        final StyledLayer sLayer = layers[0];
        Assert.assertNotNull("Single styled layer MUST NOT be null", sLayer);
        Assert.assertTrue("Single layer MUST be a UserLayer", UserLayer.class.isAssignableFrom(sLayer.getClass()));
        final UserLayer uLayer = (UserLayer) sLayer;
        final String lName = uLayer.getName();
        Assert.assertEquals("Read layer name MUST match", LAYER_NAME, lName);
        final SimpleFeatureType ft = uLayer.getInlineFeatureType();
        Assert.assertNotNull("Unmarshalled feature type MUST NOT be null", ft);
        final String fName = ft.getTypeName();
        Assert.assertEquals("Read feature type name MUST match", MY_FEATURE, fName);
        Assert.assertEquals(CRS.decode("EPSG:4326"), ft.getGeometryDescriptor().getCoordinateReferenceSystem());
    }

    @Test
    public void testUserLayerWithRemoteOWS() throws Exception {
        URL sldUrl = TestData.getResource(this, "remoteOws.sld");
        StyleFactory factory = CommonFactoryFinder.getStyleFactory(null);
        SLDParser stylereader = new SLDParser(factory, sldUrl);
        StyledLayerDescriptor sld = stylereader.parseSLD();
        Assert.assertEquals(1, sld.getStyledLayers().length);
        Assert.assertTrue(sld.getStyledLayers()[0] instanceof UserLayer);
        UserLayer layer = (UserLayer) sld.getStyledLayers()[0];
        Assert.assertEquals("LayerWithRemoteOWS", layer.getName());
        Assert.assertNotNull(layer.getRemoteOWS());
        Assert.assertEquals("WFS", layer.getRemoteOWS().getService());
        Assert.assertEquals(
                "http://sigma.openplans.org:8080/geoserver/wfs?",
                layer.getRemoteOWS().getOnlineResource());
        Assert.assertEquals(1, layer.getLayerFeatureConstraints().length);
        FeatureTypeConstraint ftc = layer.getLayerFeatureConstraints()[0];
        Assert.assertEquals("topp:states", ftc.getFeatureTypeName());
        Assert.assertNotNull(ftc.getFilter());
        Assert.assertTrue(ftc.getFilter() instanceof PropertyIsLessThan);
    }
}
