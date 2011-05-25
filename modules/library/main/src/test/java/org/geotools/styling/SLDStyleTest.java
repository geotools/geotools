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
package org.geotools.styling;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.geotools.filter.function.FilterFunction_buffer;
import org.geotools.test.TestData;
import org.opengis.filter.BinaryLogicOperator;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.Id;
import org.opengis.filter.Not;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.spatial.BinarySpatialOperator;
import org.opengis.filter.spatial.Disjoint;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Polygon;


/**
 * Try out our SLD parser and see how well it does.
 *
 * @author jamesm
 *
 * @source $URL$
 */
public class SLDStyleTest extends TestCase {
    StyleFactory sf = CommonFactoryFinder.getStyleFactory( GeoTools.getDefaultHints() );
    FilterFactory ff = CommonFactoryFinder.getFilterFactory( GeoTools.getDefaultHints() );
    StyleBuilder sb = new StyleBuilder(sf, ff);

    /**
     * Creates a new SLDStyleTest object.
     *
     * @param testName DOCUMENT ME!
     */
    public SLDStyleTest(java.lang.String testName) {
        super(testName);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(SLDStyleTest.class);

        return suite;
    }

    /**
     * Test of parseStyle method, of class org.geotools.styling.SLDStyle.
     *
     * @throws Exception DOCUMENT ME!
     */
    public void testParseStyle() throws Exception {
        //java.net.URL base = getClass().getResource("testData/");
        // base = getClass().getResource("testData");
        // base = getClass().getResource("/testData");

        //java.net.URL surl = new java.net.URL(base + "/test-sld.xml");
        java.net.URL surl = TestData.getResource(this, "test-sld.xml");
        SLDParser stylereader = new SLDParser(sf, surl);
        StyledLayerDescriptor sld = stylereader.parseSLD();
        assertEquals("My Layer", sld.getName());
        assertEquals("A layer by me", sld.getTitle());
        assertEquals("this is a sample layer", sld.getAbstract());
        assertEquals(1, sld.getStyledLayers().length);

        UserLayer layer = (UserLayer) sld.getStyledLayers()[0];
        assertNull(layer.getName());
        assertEquals(1, layer.getUserStyles().length);

        Style style = layer.getUserStyles()[0];
        assertEquals(1, style.getFeatureTypeStyles().length);
        assertEquals("My User Style", style.getName());
        assertEquals("A style by me", style.getTitle());
        assertEquals("this is a sample style", style.getAbstract());
        assertTrue(style.isDefault());

        FeatureTypeStyle fts = style.getFeatureTypeStyles()[0];
        Rule rule = fts.getRules()[0];
        LineSymbolizer lineSym = (LineSymbolizer) rule.getSymbolizers()[0];
        assertEquals(4,
            ((Number) lineSym.getStroke().getWidth().evaluate( null, Number.class )).intValue());
    }

    /**
     * XML --> SLD --> XML 
     * @throws Exception
     */
    public void testSLDParser() throws Exception {
        java.net.URL surl = TestData.getResource(this, "example-sld.xml");
        SLDParser stylereader = new SLDParser(sf, surl);
        StyledLayerDescriptor sld = stylereader.parseSLD();
        
        //convert back to xml again
        SLDTransformer aTransformer = new SLDTransformer();
        String xml = aTransformer.transform(sld);

        assertNotNull(xml);
        //we're content for the moment if this didn't throw an exception...
        //TODO: convert the buffer/resource to a string and compare
    }
    
    public void testEmptyElements() throws Exception {
        // before GEOT-3042 this would simply fail with an NPE
        java.net.URL surl = TestData.getResource(this, "test-empty-elements.sld");
        SLDParser stylereader = new SLDParser(sf, surl);
        StyledLayerDescriptor sld = stylereader.parseSLD();
        
        assertEquals(1, ((UserLayer) sld.getStyledLayers()[0]).getUserStyles().length);
        Style style = ((UserLayer) sld.getStyledLayers()[0]).getUserStyles()[0];
        assertEquals(1, style.featureTypeStyles().size());
        assertEquals(1, style.featureTypeStyles().get(0).rules().size());
        assertEquals(1, style.featureTypeStyles().get(0).rules().get(0).symbolizers().size());
    }
    
    public void testDashArray1() throws Exception {
        // plain text in dasharray
        java.net.URL surl = TestData.getResource(this, "dasharray1.sld");
        SLDParser stylereader = new SLDParser(sf, surl);
        StyledLayerDescriptor sld = stylereader.parseSLD();
        
        validateDashArrayStyle(sld);
    }
    
    public void testDashArray2() throws Exception {
        // using ogc:Literal in dasharray
        java.net.URL surl = TestData.getResource(this, "dasharray2.sld");
        SLDParser stylereader = new SLDParser(sf, surl);
        StyledLayerDescriptor sld = stylereader.parseSLD();
        
        validateDashArrayStyle(sld);
    }

    private void validateDashArrayStyle(StyledLayerDescriptor sld) {
        assertEquals(1, ((UserLayer) sld.getStyledLayers()[0]).getUserStyles().length);
        Style style = ((UserLayer) sld.getStyledLayers()[0]).getUserStyles()[0];
        List<FeatureTypeStyle> fts = style.featureTypeStyles();
        assertEquals(1, fts.size());
        List<Rule> rules = fts.get(0).rules();
        assertEquals(1, rules.size());
        List<Symbolizer> symbolizers = rules.get(0).symbolizers();
        assertEquals(1, symbolizers.size());
        
        LineSymbolizer ls = (LineSymbolizer) symbolizers.get(0);
        assertTrue(Arrays.equals(new float[] {2.0f, 1.0f, 4.0f, 1.0f}, ls.getStroke().getDashArray()));
    }

    public void testSLDParserWithWhitespaceIsTrimmed() throws Exception {
    	java.net.URL surl = TestData.getResource(this, "whitespace.sld");
    	 SLDParser stylereader = new SLDParser(sf, surl);
         StyledLayerDescriptor sld = stylereader.parseSLD();
         
         TextSymbolizer ts = (TextSymbolizer) ((NamedLayer)sld.getStyledLayers()[0])
         	.getStyles()[0].getFeatureTypeStyles()[0].getRules()[0].getSymbolizers()[0];
         
         PropertyName property = (PropertyName) ts.getLabel();
         assertEquals( "testProperty", property.getPropertyName() );
         
         Expression color = ts.getFill().getColor();
         assertEquals( Color.BLACK, SLD.color( color ) );
    }
    
    public void testSLDParserWithhMixedContent() throws Exception {
        java.net.URL surl = TestData.getResource(this, "mixedContent.sld");
         SLDParser stylereader = new SLDParser(sf, surl);
         StyledLayerDescriptor sld = stylereader.parseSLD();
         
         Symbolizer[] symbolizers = ((NamedLayer) sld.getStyledLayers()[0]).getStyles()[0]
                .getFeatureTypeStyles()[0].getRules()[0].getSymbolizers();

         PolygonSymbolizer polygon = (PolygonSymbolizer) symbolizers[0];
         TextSymbolizer text = (TextSymbolizer) symbolizers[1];
         
         Expression fill = polygon.getFill().getColor();
         Expression label = text.getLabel();
         
         String fillValue = (String) fill.evaluate(null, String.class);
         String labelValue = (String) label.evaluate(null, String.class);

         assertEquals("#96C3F5", fillValue);
         assertEquals("this is a prefix; this is an expression; this is a postfix", labelValue);
    }
    
    public void testSLDExtendedColorMap() throws Exception {
        java.net.URL surl = TestData.getResource(this, "colormap.sld");
         SLDParser stylereader = new SLDParser(sf, surl);
         StyledLayerDescriptor sld = stylereader.parseSLD();
         
         Symbolizer[] symbolizers = ((UserLayer) sld.getStyledLayers()[0]).userStyles().get(0).getFeatureTypeStyles()[0].getRules()[0].getSymbolizers();

         RasterSymbolizer rs = (RasterSymbolizer) symbolizers[0];
         
         assertTrue(rs.getColorMap().getExtendedColors());
         assertTrue(rs.getColorMap().getType()==ColorMap.TYPE_RAMP);
    }
    
    public void testSLDParserWithhMixedContentCDATA() throws Exception {
        java.net.URL surl = TestData.getResource(this, "mixedContentWithCDATA.xml");
         SLDParser stylereader = new SLDParser(sf, surl);
         StyledLayerDescriptor sld = stylereader.parseSLD();
         
         Symbolizer[] symbolizers = ((NamedLayer) sld.getStyledLayers()[0]).getStyles()[0]
                .getFeatureTypeStyles()[0].getRules()[0].getSymbolizers();

         TextSymbolizer text = (TextSymbolizer) symbolizers[0];
         
         Expression label = text.getLabel();
         
         String labelValue = (String) label.evaluate(null, String.class);

         assertEquals("literal_1\n cdata literal_2", labelValue);
    }
    
    public void testSLDParserWithhMixedContentCDATASpaces() throws Exception {
        java.net.URL surl = TestData.getResource(this, "mixedContentWithCDATASpaces.xml");
         SLDParser stylereader = new SLDParser(sf, surl);
         StyledLayerDescriptor sld = stylereader.parseSLD();
         
         Symbolizer[] symbolizers = ((NamedLayer) sld.getStyledLayers()[0]).getStyles()[0]
                .getFeatureTypeStyles()[0].getRules()[0].getSymbolizers();

         TextSymbolizer text = (TextSymbolizer) symbolizers[0];
         
         Expression label = text.getLabel();
         
         String labelValue = (String) label.evaluate(null, String.class);

         assertEquals("literal_1\nliteral_2", labelValue);
    }

    /**
	 * SLD --> XML --> SLD
	 * @throws Exception
	 */
    public void testSLDTransformer() throws Exception {
    	//create an SLD
    	StyledLayerDescriptor sld = sf.createStyledLayerDescriptor();
    	StyledLayerDescriptor sld2;
    	sld.setName("SLD Name");
    	sld.setTitle("SLD Title");
    	UserLayer layer = sf.createUserLayer();
    	layer.setName("UserLayer Name");

    	Style style = sf.createStyle();
    	style.setName("Style Name");
    	style.setTitle("Style Title");
    	Rule rule1 = sb.createRule(sb.createLineSymbolizer(new Color(0), 2));
    	// note: symbolizers containing a fill will likely fail, as the SLD
		// transformation loses a little data (background colour?)
    	FeatureTypeStyle fts1 = sf.createFeatureTypeStyle(new Rule[] {rule1});
    	fts1.setSemanticTypeIdentifiers(new String[] {"generic:geometry"});
    	style.setFeatureTypeStyles(new FeatureTypeStyle[] {fts1});
    	layer.setUserStyles(new Style[] {style});
    	sld.setStyledLayers(new UserLayer[] {layer});
    	
    	//convert it to XML
        SLDTransformer aTransformer = new SLDTransformer();
        String xml = aTransformer.transform(sld);

        //back to SLD
        InputStream is = new ByteArrayInputStream(xml.getBytes("UTF-8"));
        
        SLDParser stylereader = new SLDParser(sf, is);
        
        sld2 = stylereader.parseSLD();
// UNCOMMENT FOR DEBUGGING
//        assertEquals(SLD.rules(SLD.styles(sld)[0]).length, SLD.rules(SLD.styles(sld2)[0]).length);
//        for (int i = 0; i < SLD.rules(SLD.styles(sld)[0]).length; i++) {
//            Rule aRule = SLD.rules(SLD.styles(sld)[0])[i];
//            Rule bRule = SLD.rules(SLD.styles(sld2)[0])[i];
//            System.out.println(i+":"+aRule);
//        	Symbolizer[] symb1 = SLD.symbolizers(aRule);
//        	Symbolizer[] symb2 = SLD.symbolizers(bRule);
//        	for (int j = 0; j < symb1.length; j++) {
//        		//symbolizers are equal
//        		assertTrue(symb1[j].equals(symb2[j]));
//        	}
//        	//rules are equal
//            assertTrue(aRule.equals(bRule));
//        }
//        //feature type styles are equal
//        assertTrue(SLD.featureTypeStyles(sld)[0].equals(SLD.featureTypeStyles(sld2)[0]));
//        //styles are equal
//        assertTrue(SLD.styles(sld)[0].equals(SLD.styles(sld2)[0]));
//        //layers are equal
//        StyledLayer layer1 = sld.getStyledLayers()[0];
//        StyledLayer layer2 = sld2.getStyledLayers()[0];
//        boolean result = layer1.equals(layer2); 
//        assertTrue(result);
        
        //everything is equal
        assertEquals(sld2, sld);
    }

    public void testSLDTransformerIndentation() throws Exception {
    	//create a simple object
    	StyledLayerDescriptor sld = sf.createStyledLayerDescriptor();
    	NamedLayer nl = sf.createNamedLayer();
    	nl.setName("named_layer_1");
    	sld.addStyledLayer(nl);
    	//convert it to XML
        SLDTransformer aTransformer = new SLDTransformer();
        aTransformer.setIndentation(3); //3 spaces
        String xml1 = aTransformer.transform(sld);
        aTransformer.setIndentation(4); //4 spaces
        String xml2 = aTransformer.transform(sld);
        //generated xml contains 4 indents, so if indentation is working, the difference should be 4
        assertEquals(xml1.length() + 4, xml2.length());
    }
    
    public void testParseSLD_NameSpaceAware() throws Exception {
        URL surl = TestData.getResource(this, "test-ns.sld");
        StyleFactory factory = CommonFactoryFinder.getStyleFactory(null);
        
        SLDParser stylereader = new SLDParser(factory, surl);
        StyledLayerDescriptor sld = stylereader.parseSLD();
        
        assertEquals(1, sld.getStyledLayers().length);
        FeatureTypeStyle[] fts = SLD.featureTypeStyles(sld);
        assertEquals(2, fts.length);
        assertEquals(1, fts[0].getSemanticTypeIdentifiers().length);
        assertEquals(2, fts[1].getSemanticTypeIdentifiers().length);
        assertEquals("colorbrewer:default", fts[1].getSemanticTypeIdentifiers()[1]);
    }
    
    /**
     * Test of parseSLD method to ensure NamedLayer/Name and
     * NamedLayer/NamedStyle are parsed correctly
     *
     * @throws Exception boom
     */
    public void testParseSLDNamedLayersOnly() throws Exception {
        StyleFactory factory = CommonFactoryFinder.getStyleFactory(null);
        java.net.URL surl = TestData.getResource(this, "namedLayers.sld");
        SLDParser stylereader = new SLDParser(factory, surl);

        StyledLayerDescriptor sld = stylereader.parseSLD();

        final int expectedLayerCount = 3;
        final String[] layerNames = { "Rivers", "Roads", "Houses" };
        final String[] namedStyleNames = { "CenterLine", "CenterLine", "Outline" };
        StyledLayer[] layers = sld.getStyledLayers();

        assertEquals(expectedLayerCount, layers.length);

        for (int i = 0; i < expectedLayerCount; i++) {
            assertTrue(layers[i] instanceof NamedLayer);
        }

        for (int i = 0; i < expectedLayerCount; i++) {
            assertEquals(layerNames[i], layers[i].getName());
        }

        for (int i = 0; i < expectedLayerCount; i++) {
            NamedLayer layer = (NamedLayer) layers[i];
            assertEquals(1, layer.getStyles().length);
            assertTrue(layer.getStyles()[0] instanceof NamedStyle);
            assertEquals(namedStyleNames[i], layer.getStyles()[0].getName());
        }
        
        // find the rivers layers and test the LayerFeatureConstraints
        for (int i = 0; i < expectedLayerCount; i++) {
        	NamedLayer layer = (NamedLayer) layers[i];
        	if (layer.getName().equals("Rivers")) {
        		FeatureTypeConstraint[] featureTypeConstraints = layer.getLayerFeatureConstraints();
    	        final int featureTypeConstraintCount = 1;
    	        assertEquals(featureTypeConstraintCount, featureTypeConstraints.length);
    	        Filter filter = featureTypeConstraints[0].getFilter();
    	        assertTrue(filter instanceof PropertyIsEqualTo);
    	        PropertyIsEqualTo equal = (PropertyIsEqualTo) filter;
    	        assertTrue(equal.getExpression1() instanceof PropertyName);
    	        assertTrue(equal.getExpression2() instanceof Literal);
        	}
        }
        
    }

    /**
     * Test of parseSLD method to ensure NamedLayer/Name and
     * NamedLayer/NamedStyle are parsed correctly
     *
     * @throws Exception boom
     */
    public void testParseSLDNamedAndUserLayers() throws Exception {
        StyleFactory factory = CommonFactoryFinder.getStyleFactory(null);
        java.net.URL surl = TestData.getResource(this, "mixedLayerTypes.sld");
        SLDParser stylereader = new SLDParser(factory, surl);

        StyledLayerDescriptor sld = stylereader.parseSLD();

        final int expectedLayerCount = 4;

        StyledLayer[] layers = sld.getStyledLayers();

        assertEquals(expectedLayerCount, layers.length);
        assertTrue(layers[0] instanceof NamedLayer);
        assertTrue(layers[1] instanceof UserLayer);
        assertTrue(layers[2] instanceof NamedLayer);
        assertTrue(layers[3] instanceof UserLayer);
    }

    /**
     * Verifies that geometry filters inside SLD documents are correctly
     * parsed.
     *
     * @throws IOException boom
     */
    public void testParseGeometryFilters() throws IOException {
        final String TYPE_NAME = "testType";
        final String GEOMETRY_ATTR = "Polygons";
        StyleFactory factory = CommonFactoryFinder.getStyleFactory(null);
        java.net.URL surl = TestData.getResource(this, "spatialFilter.xml");
        SLDParser stylereader = new SLDParser(factory, surl);

        Style[] styles = stylereader.readXML();

        final int expectedStyleCount = 1;
        assertEquals(expectedStyleCount, styles.length);

        Style notDisjoint = styles[0];
        assertEquals(1, notDisjoint.getFeatureTypeStyles().length);

        FeatureTypeStyle fts = notDisjoint.getFeatureTypeStyles()[0];
        assertEquals(TYPE_NAME, fts.getFeatureTypeName());
        assertEquals(1, fts.getRules().length);

        Filter filter = fts.getRules()[0].getFilter();
        assertTrue( filter instanceof Not );

        BinarySpatialOperator spatialFilter = (BinarySpatialOperator) ((BinaryLogicOperator) filter).getChildren().get(0);
        assertTrue( spatialFilter instanceof Disjoint );

        Expression left = spatialFilter.getExpression1();
        Expression right = spatialFilter.getExpression2();
                
        assertTrue( left instanceof PropertyName );
        
        assertTrue( right instanceof Literal );
        assertTrue( right.evaluate(null) instanceof Geometry );

        assertEquals(GEOMETRY_ATTR, ((PropertyName)left).getPropertyName() );
        assertTrue( right.evaluate(null) instanceof Polygon);

        Envelope bbox = ((Polygon) right.evaluate(null)).getEnvelopeInternal();
        assertEquals(-10D, bbox.getMinX(), 0);
        assertEquals(-10D, bbox.getMinY(), 0);
        assertEquals(10D, bbox.getMaxX(), 0);
        assertEquals(10D, bbox.getMaxY(), 0);
    }

    /**
     * Verifies that a FID Filter is correctly parsed (GEOT-992).
     *
     * @throws IOException boom
     */
    public void testParseFidFilter() throws IOException {
        StyleFactory factory = CommonFactoryFinder.getStyleFactory(null);
        java.net.URL surl = TestData.getResource(this, "fidFilter.xml");
        SLDParser stylereader = new SLDParser(factory, surl);

        Style[] styles = stylereader.readXML();

        final int expectedStyleCount = 1;
        assertEquals(expectedStyleCount, styles.length);

        Style style = styles[0];
        assertEquals(1, style.getFeatureTypeStyles().length);

        FeatureTypeStyle fts = style.getFeatureTypeStyles()[0];
        assertEquals("Feature", fts.getFeatureTypeName());
        assertEquals(1, fts.getRules().length);

        
        Filter filter = fts.getRules()[0].getFilter();
        assertTrue( filter instanceof Id);

        Id fidFilter = (Id) filter;
        Set ids = fidFilter.getIDs();
        String[] fids = (String[]) ids.toArray(new String[ ids.size()] );
        assertEquals("Wrong number of fids", 5, fids.length);
        
        Arrays.sort(fids);
        
        assertEquals("fid.0", fids[0]);
        assertEquals("fid.1", fids[1]);
        assertEquals("fid.2", fids[2]);
        assertEquals("fid.3", fids[3]);
        assertEquals("fid.4", fids[4]);
    }
    
    public void testParseKmlExtensions() throws IOException {
        StyleFactory factory = CommonFactoryFinder.getStyleFactory(null);
        java.net.URL surl = TestData.getResource(this, "kmlSymbolizer.sld");
        SLDParser stylereader = new SLDParser(factory, surl);

        // basic checks
        Style[] styles = stylereader.readXML();
        assertEquals(1, styles.length);
        assertEquals(1, styles[0].getFeatureTypeStyles().length);
        assertEquals(1, styles[0].getFeatureTypeStyles()[0].getRules().length);
        final Rule rule = styles[0].getFeatureTypeStyles()[0].getRules()[0];
        assertEquals(1, rule.getSymbolizers().length);
        TextSymbolizer2 ts = (TextSymbolizer2) rule.getSymbolizers()[0];
        
        // abstract == property name
        assertEquals("propertyOne", ((PropertyName) ts.getSnippet()).getPropertyName());
        
        // abstract == mixed literal + propertyName
        Expression desc = ts.getFeatureDescription();
        assertTrue(desc instanceof Function);
        assertEquals("strConcat", ((Function) desc).getName());
        assertEquals(2, ((Function) desc).getParameters().size());
        assertTrue(((Function) desc).getParameters().get(0) instanceof Literal);
        assertTrue(((Function) desc).getParameters().get(1) instanceof PropertyName);
        
        // other text -> target & literal
        assertEquals("extrude", ts.getOtherText().getTarget());
        assertTrue(ts.getOtherText().getText() instanceof Literal);
    }
    
    /**
     * Tests the parsing of a raster symbolizer sld
     * @throws IOException
     */
    public void testParseRasterSymbolizer() throws IOException{
    	 StyleFactory factory = CommonFactoryFinder.getStyleFactory(null);
         java.net.URL surl = TestData.getResource(this, "rasterSymbolizer.sld");
         SLDParser stylereader = new SLDParser(factory, surl);
    	
         Style[] styles = stylereader.readXML();
         assertEquals(1, styles.length);
         assertEquals(1, styles[0].getFeatureTypeStyles().length);
         assertEquals(1, styles[0].getFeatureTypeStyles()[0].getRules().length);
         
         Rule r = styles[0].getFeatureTypeStyles()[0].getRules()[0];
         assertEquals(1, r.getSymbolizers().length);
         
         RasterSymbolizer rs = (RasterSymbolizer)r.getSymbolizers()[0];
         
         //opacity
         assertEquals(0.75, SLD.opacity(rs));
         
         //channels
         ChannelSelection cs = rs.getChannelSelection();
         SelectedChannelType redChannel = cs.getRGBChannels()[0];
         SelectedChannelType greenChannel = cs.getRGBChannels()[1];
         SelectedChannelType blueChannel = cs.getRGBChannels()[2];
         
         //channel names
         assertEquals("1", redChannel.getChannelName());
         assertEquals("2", greenChannel.getChannelName());
         assertEquals("6", blueChannel.getChannelName());
    	
         //contrast enhancement
         ContrastEnhancement rcs =  redChannel.getContrastEnhancement();
         String type = (String)rcs.getType().evaluate(null);
         assertEquals("Histogram", type);

         ContrastEnhancement gcs = greenChannel.getContrastEnhancement();
         Double ggamma = (Double)gcs.getGammaValue().evaluate(null);
         assertEquals(2.8, ggamma.doubleValue());
         
         ContrastEnhancement bcs =  blueChannel.getContrastEnhancement();
         type = (String)bcs.getType().evaluate(null);
         assertEquals("Normalize", type);
         
         //overlap behaviour
         Expression overlapExpr = rs.getOverlap();
         type = (String)overlapExpr.evaluate(null);
         assertEquals("LATEST_ON_TOP", type);
         
         //ContrastEnhancement
         ContrastEnhancement ce =  rs.getContrastEnhancement();
         Double v = (Double)ce.getGammaValue().evaluate(null);
         assertEquals(1.0, v.doubleValue());
    }
    
    /**
     * Tests the parsing of a raster symbolizer sld with color Map
     * @throws IOException
     */
    public void testParseRasterSymbolizerColorMap() throws IOException{
    	 StyleFactory factory = CommonFactoryFinder.getStyleFactory(null);
         java.net.URL surl = TestData.getResource(this, "rasterSymbolizerColorMap.sld");
         SLDParser stylereader = new SLDParser(factory, surl);
    	
         Style[] styles = stylereader.readXML();
         assertEquals(1, styles.length);
         assertEquals(1, styles[0].getFeatureTypeStyles().length);
         assertEquals(1, styles[0].getFeatureTypeStyles()[0].getRules().length);
         
         Rule r = styles[0].getFeatureTypeStyles()[0].getRules()[0];
         assertEquals(1, r.getSymbolizers().length);
         
         RasterSymbolizer rs = (RasterSymbolizer)r.getSymbolizers()[0];
         
         //opacity         
         Double d = (Double)rs.getOpacity().evaluate(null);
         assertEquals(1.0, d.doubleValue());
                
         //overlap behaviour
         Expression overlapExpr = rs.getOverlap();
         String type = (String)overlapExpr.evaluate(null);
         assertEquals("AVERAGE", type);
         
         //ColorMap
         ColorMap cMap = rs.getColorMap();
         assertEquals(20, cMap.getColorMapEntries().length);
         ColorMapEntry[] centeries = cMap.getColorMapEntries();
         String[] colors = new String[]{"#00ff00","#00fa00","#14f500","#28f502","#3cf505","#50f50a","#64f014","#7deb32","#78c818","#38840c","#2c4b04","#ffff00","#dcdc00","#b47800","#c85000","#be4100","#963000","#3c0200","#ffffff","#ffffff"};
         int[] values = new int[]{-500,-417,-333,-250,-167,-83,-1,0,30,105,300,400,700,1200,1400,1600,2000,3000,5000,13000};
         for (int i = 0; i < centeries.length; i++) {
			ColorMapEntry entry = centeries[i];
			String c = (String) entry.getColor().evaluate(null);
			Integer q = (Integer) entry.getQuantity().evaluate(null);
			assertEquals(colors[i], c);
			assertEquals(values[i], q.intValue());
		}
         
         
    }
    
    public void testParseGeometryExpressions() throws Exception {
        StyleFactory factory = CommonFactoryFinder.getStyleFactory(null);
        java.net.URL surl = TestData.getResource(this, "geometryTransformation.sld");
        SLDParser stylereader = new SLDParser(factory, surl);
       
        Style[] styles = stylereader.readXML();
        assertEquals(1, styles.length);
        assertEquals(1, styles[0].featureTypeStyles().size());
        assertEquals(1, styles[0].featureTypeStyles().get(0).rules().size());
        
        Rule r = styles[0].featureTypeStyles().get(0).rules().get(0);
        assertEquals(1, r.getSymbolizers().length);
        
        PolygonSymbolizer ps = (PolygonSymbolizer) r.getSymbolizers()[0];
        Expression geom = ps.getGeometry();
        assertNotNull(geom);
        assertTrue(geom instanceof FilterFunction_buffer);
        FilterFunction_buffer buf = (FilterFunction_buffer) geom;
        assertTrue(buf.getParameters().get(0) instanceof PropertyName);
        assertTrue(buf.getParameters().get(1) instanceof Literal);
    }
    
    public void testParsePlainGeometryExpression() throws Exception {
        StyleFactory factory = CommonFactoryFinder.getStyleFactory(null);
        java.net.URL surl = TestData.getResource(this, "geometryPlain.sld");
        SLDParser stylereader = new SLDParser(factory, surl);
       
        Style[] styles = stylereader.readXML();
        assertEquals(1, styles.length);
        assertEquals(1, styles[0].featureTypeStyles().size());
        assertEquals(1, styles[0].featureTypeStyles().get(0).rules().size());
        
        Rule r = styles[0].featureTypeStyles().get(0).rules().get(0);
        assertEquals(1, r.getSymbolizers().length);
        
        PolygonSymbolizer ps = (PolygonSymbolizer) r.getSymbolizers()[0];
        Expression geom = ps.getGeometry();
        assertNotNull(geom);
        assertTrue(geom instanceof PropertyName);
        PropertyName pn = (PropertyName) geom;
        assertEquals("the_geom", pn.getPropertyName());
        assertEquals("the_geom", ps.getGeometryPropertyName());

    }
}
