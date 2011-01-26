/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.se.v1_1;

import java.awt.Color;

import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.styling.ColorMap;
import org.geotools.styling.ExternalGraphic;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Fill;
import org.geotools.styling.Font;
import org.geotools.styling.Graphic;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.Mark;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.SLD;
import org.geotools.styling.SelectedChannelType;
import org.geotools.styling.Stroke;
import org.geotools.styling.TextSymbolizer;
import org.geotools.styling.UomOgcMapping;
import org.geotools.xml.Parser;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.PropertyName;
import org.opengis.style.ContrastMethod;
import org.opengis.style.OverlapBehavior;
import org.opengis.style.Rule;

import junit.framework.TestCase;

public class SEExampleTest extends TestCase {

    SimpleFeature f1;
    
    @Override
    protected void setUp() throws Exception {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("test");
        tb.add("hospitalName", String.class);
        tb.add("numberOfBeds", Integer.class);
        
        SimpleFeatureBuilder b = new SimpleFeatureBuilder(tb.buildFeatureType());
        b.add("foobar");
        b.add(10);
        f1 = b.buildFeature(null);
    }
    
    public void testParsePointSymbolizer1() throws Exception {
        /*<PointSymbolizer version="1.1.0" xsi:schemaLocation="http://www.opengis.net/se/1.1.0/Symbolizer.xsd" xmlns="http://www.opengis.net/se" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" uom="http://www.opengeospatial.org/se/units/metre">
        <Name>MyPointSymbolizer</Name>
        <Description>
            <Title>Example Pointsymbolizer</Title>
            <Abstract>This is just a simple example of a point symbolizer.</Abstract>
        </Description>
        <Graphic>
            <Mark>
                <WellKnownName>star</WellKnownName>
                <Fill>
                    <SvgParameter name="fill">#ff0000</SvgParameter>
                </Fill>
            </Mark>
            <Size>8.0</Size>
        </Graphic>
       </PointSymbolizer>*/

        PointSymbolizer sym = (PointSymbolizer) parse("example-pointsymbolizer1.xml");
        assertEquals("Example Pointsymbolizer", sym.getDescription().getTitle().toString());
        assertEquals("This is just a simple example of a point symbolizer.", 
                sym.getDescription().getAbstract().toString());
        
        Graphic g = sym.getGraphic();
        assertEquals(8.0, g.getSize().evaluate(null, Double.class));
        assertEquals(1, g.getMarks().length);
        
        Mark m = g.getMarks()[0];
        assertEquals("star", m.getWellKnownName().evaluate(null, String.class));
        Color c = m.getFill().getColor().evaluate(null, Color.class);
        assertEquals(255, c.getRed());
    }
    
    public void testParsePointSymbolizer2() throws Exception {
     /*<PointSymbolizer version="1.1.0" xsi:schemaLocation="http://www.opengis.net/se http://www.opengis.net/se/1.1.0/Symbolizer.xsd" xmlns="http://www.opengis.net/se" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" uom="http://www.opengeospatial.org/se/units/pixel">
        <Name>MyPointSymbolizer</Name>
        <Description>
            <Title>Example Pointsymbolizer</Title>
            <Abstract>This is just a simple example of a point symbolizer.</Abstract>
        </Description>
        <Graphic>
            <ExternalGraphic>
              <OnlineResource xlink:type="simple" xlink:href="http://www.vendor.com/geosym/2267.svg"/>
              <Format>image/svg+xml</Format>
            </ExternalGraphic>
            <ExternalGraphic>
              <OnlineResource xlink:type="simple" xlink:href="http://www.vendor.com/geosym/2267.png"/>
              <Format>image/png</Format>
            </ExternalGraphic>
            <Mark/>
            <Size>15.0</Size>
        </Graphic>
    </PointSymbolizer>*/
        
        PointSymbolizer sym = (PointSymbolizer) parse("example-pointsymbolizer2.xml");
        assertEquals("MyPointSymbolizer", sym.getName());
        assertEquals("Example Pointsymbolizer", sym.getDescription().getTitle().toString());
        assertEquals("This is just a simple example of a point symbolizer.", 
                sym.getDescription().getAbstract().toString());
        
        Graphic g = sym.getGraphic();
        assertEquals(15.0, g.getSize().evaluate(null, Double.class));
        assertEquals(2, g.getExternalGraphics().length);
        
        ExternalGraphic eg = g.getExternalGraphics()[0];
        assertEquals("http://www.vendor.com/geosym/2267.svg", eg.getLocation().toString());
        assertEquals("image/svg+xml", eg.getFormat());
        
        eg = g.getExternalGraphics()[1];
        assertEquals("http://www.vendor.com/geosym/2267.png", eg.getLocation().toString());
        assertEquals("image/png", eg.getFormat());
    }
    
    public void testParseLineSymbolizer() throws Exception {
        /*<LineSymbolizer version="1.1.0" xsi:schemaLocation="http://www.opengis.net/se http://www.opengis.net/se/1.1.0/Symbolizer.xsd" xmlns="http://www.opengis.net/se" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" uom="http://www.opengeospatial.org/se/units/metre">
            <Name>MyLineSymbolizer</Name>
            <Description>
                <Title>Example Symbol</Title>
                <Abstract>This is just a simple example of a line symbolizer.</Abstract>
            </Description>
            <Stroke>
                <SvgParameter name="stroke">#0000ff</SvgParameter>
                <SvgParameter name="stroke-width">2</SvgParameter>
            </Stroke>
        </LineSymbolizer>*/
        LineSymbolizer sym = (LineSymbolizer) parse("example-linesymbolizer.xml");
        assertEquals("MyLineSymbolizer", sym.getName());
        assertEquals("Example Symbol", sym.getDescription().getTitle().toString());
        assertEquals("This is just a simple example of a line symbolizer.", 
            sym.getDescription().getAbstract().toString());
        
        Stroke s = sym.getStroke();
        assertEquals(255, s.getColor().evaluate(null, Color.class).getBlue());
        assertEquals(new Integer(2), s.getWidth().evaluate(null, Integer.class));
    }
    
    public void testParsePolygonSymbolizer() throws Exception {
      /*<PolygonSymbolizer version="1.1.0" xsi:schemaLocation="http://www.opengis.net/se http://www.opengis.net/se/1.1.0/Symbolizer.xsd" xmlns="http://www.opengis.net/se" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" uom="http://www.opengeospatial.org/se/units/pixel">
        <Name>MyPolygonSymbolizer</Name>
        <Description>
            <Title>Example PolygonSymbolizer</Title>
            <Abstract>This is just a simple example of a polygon symbolizer.</Abstract>
        </Description>
        <Fill>
            <SvgParameter name="fill">#aaaaff</SvgParameter>
        </Fill>
        <Stroke>
            <SvgParameter name="stroke">#0000aa</SvgParameter>
        </Stroke>
    </PolygonSymbolizer>*/
        PolygonSymbolizer sym = (PolygonSymbolizer) parse("example-polygonsymbolizer.xml");
        assertEquals("MyPolygonSymbolizer", sym.getName());
        assertEquals("Example PolygonSymbolizer", sym.getDescription().getTitle().toString());
        assertEquals("This is just a simple example of a polygon symbolizer.", 
                sym.getDescription().getAbstract().toString());
        
        Fill f = sym.getFill();
        Color c = f.getColor().evaluate(null, Color.class);
        assertEquals(170, c.getRed());
        assertEquals(170, c.getGreen());
        assertEquals(255, c.getBlue());
        
        c = sym.getStroke().getColor().evaluate(null, Color.class);
        assertEquals(170, c.getBlue());
    }
    
    public void testParseTextSymbolizer() throws Exception {
        /*<TextSymbolizer version="1.1.0" xsi:schemaLocation="http://www.opengis.net/se/1.1.0/Symbolizer.xsd" xmlns="http://www.opengis.net/se" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" uom="http://www.opengeospatial.org/se/units/pixel">
        <Name>MyTextSymbolizer</Name>
        <Description>
            <Title>Example TextSymbolizer</Title>
            <Abstract>This is just an example of a text symbolizer using the FormatNumber function.</Abstract>
        </Description>
        <Geometry>
            <ogc:PropertyName>locatedAt</ogc:PropertyName>
        </Geometry>
        <Label>
            <ogc:PropertyName>hospitalName</ogc:PropertyName> (
            <FormatNumber fallbackValue="">
                <NumericValue>
                    <ogc:PropertyName>numberOfBeds</ogc:PropertyName>
                </NumericValue>
                <Pattern>#####</Pattern>
            </FormatNumber>)
        </Label>
        <Font>
            <SvgParameter name="font-family">Arial</SvgParameter>
            <SvgParameter name="font-family">Sans-Serif</SvgParameter>
            <SvgParameter name="font-style">italic</SvgParameter>
            <SvgParameter name="font-size">10</SvgParameter>
        </Font>
        <Halo/>
        <Fill>
            <SvgParameter name="fill">#000000</SvgParameter>
        </Fill>
    </TextSymbolizer>*/
        
        TextSymbolizer sym = (TextSymbolizer) parse("example-textsymbolizer.xml");
        assertEquals("MyTextSymbolizer", sym.getName());
        assertEquals("Example TextSymbolizer", sym.getDescription().getTitle().toString());
        assertEquals("This is just an example of a text symbolizer using the FormatNumber function.", 
            sym.getDescription().getAbstract().toString());
        
        assertEquals("locatedAt", sym.getGeometryPropertyName());
        
        Function l = (Function) sym.getLabel();
        assertEquals("foobar (10)", l.evaluate(f1));
        //assertEquals("hospitalName", l);
        
        Font f = sym.getFont();
        assertEquals(2, f.getFamily().size());
        assertEquals("Arial", f.getFamily().get(0).evaluate(null, String.class));
        assertEquals("Sans-Serif", f.getFamily().get(1).evaluate(null, String.class));
        assertEquals("italic", f.getStyle().evaluate(null, String.class));
        assertEquals("10", f.getSize().evaluate(null, String.class));
        
        Fill fill = sym.getFill();
        assertEquals(Color.BLACK, fill.getColor().evaluate(null, Color.class));
    }
    
    public void testParseRasterSymbolizer1() throws Exception {
        /*<RasterSymbolizer version="1.1.0" xsi:schemaLocation="http://www.opengis.net/se http://www.opengis.net/se/1.1.0/Symbolizer.xsd" xmlns="http://www.opengis.net/se" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
                <Opacity>1.0</Opacity>
                <OverlapBehavior>AVERAGE</OverlapBehavior>
                <ColorMap>
                        <Categorize fallbackValue="#78c818">
                        <LookupValue>Rasterdata</LookupValue>
                                <Value>#00ff00</Value>
                                <Threshold>-417</Threshold>
                                <Value>#00fa00</Value>
                                <Threshold>-333</Threshold>
                                <Value>#14f500</Value>
                                <Threshold>-250</Threshold>
                                <Value>#28f502</Value>
                                <Threshold>-167</Threshold>
                                <Value>#3cf505</Value>
                                <Threshold>-83</Threshold>
                                <Value>#50f50a</Value>
                                <Threshold>-1</Threshold>
                                <Value>#64f014</Value>
                                <Threshold>0</Threshold>
                                <Value>#7deb32</Value>
                                <Threshold>30</Threshold>
                                <Value>#78c818</Value>
                                <Threshold>105</Threshold>
                                <Value>#38840c</Value>
                                <Threshold>300</Threshold>
                                <Value>#2c4b04</Value>
                                <Threshold>400</Threshold>
                                <Value>#ffff00</Value>
                                <Threshold>700</Threshold>
                                <Value>#dcdc00</Value>
                                <Threshold>1200</Threshold>
                                <Value>#b47800</Value>
                                <Threshold>1400</Threshold>
                                <Value>#c85000</Value>
                                <Threshold>1600</Threshold>
                                <Value>#be4100</Value>
                                <Threshold>2000</Threshold>
                                <Value>#963000</Value>
                                <Threshold>3000</Threshold>
                                <Value>#3c0200</Value>
                                <Threshold>5000</Threshold>
                                <Value>#ffffff</Value>
                                <Threshold>13000</Threshold>
                                <Value>#ffffff</Value>
                        </Categorize>
                </ColorMap>
                <ShadedRelief/>
        </RasterSymbolizer>*/
        RasterSymbolizer sym = (RasterSymbolizer) parse("example-rastersymbolizer1.xml");
        assertEquals(1.0, sym.getOpacity().evaluate(null, Double.class));
        assertEquals(OverlapBehavior.AVERAGE, sym.getOverlapBehavior());
        
        ColorMap map = sym.getColorMap();
        assertNotNull(map);
        assertEquals(20, map.getColorMapEntries().length);
        
        //
        Color c = map.getColorMapEntry(0).getColor().evaluate(null, Color.class);
        assertEquals(0, c.getRed());
        assertEquals(255, c.getGreen());
        assertEquals(0, c.getBlue());
        
        c = map.getColorMapEntry(1).getColor().evaluate(null, Color.class);
        assertEquals(0, c.getRed());
        assertEquals(250, c.getGreen());
        assertEquals(0, c.getBlue());
        assertEquals(-417d, map.getColorMapEntry(1).getQuantity().evaluate(null, Double.class));
        
        c = map.getColorMapEntry(19).getColor().evaluate(null, Color.class);
        assertEquals(Color.WHITE, c);
        assertEquals(13000d, map.getColorMapEntry(19).getQuantity().evaluate(null, Double.class));
    }
    
    public void testParseRasterSymbolizer2() throws Exception {
        /*
        <RasterSymbolizer version="1.1.0" xsi:schemaLocation="http://www.opengis.net/se/1.1.0/Symbolizer.xsd" xmlns="http://www.opengis.net/se" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
                <Opacity>1.0</Opacity>
                <ChannelSelection>
                        <RedChannel>
                                <SourceChannelName>1</SourceChannelName>
                                <ContrastEnhancement>
                                        <Histogram/>
                                </ContrastEnhancement>
                        </RedChannel>
                        <GreenChannel>
                                <SourceChannelName>2</SourceChannelName>
                                <ContrastEnhancement>
                                        <GammaValue>2.5</GammaValue>
                                </ContrastEnhancement>
                        </GreenChannel>
                        <BlueChannel>
                                <SourceChannelName>3</SourceChannelName>
                                <ContrastEnhancement>
                                        <Normalize/>
                                </ContrastEnhancement>
                        </BlueChannel>
                </ChannelSelection>
                <OverlapBehavior>LATEST_ON_TOP</OverlapBehavior>
                <ColorMap>
                        <Interpolate fallbackValue="#dddddd">
                                <LookupValue>Rasterdata</LookupValue>
                                <InterpolationPoint>
                                        <Data>0</Data>
                                        <Value>#000000</Value>
                                </InterpolationPoint>
                                <InterpolationPoint>
                                        <Data>255</Data>
                                        <Value>#ffffff</Value>
                                </InterpolationPoint>
                        </Interpolate>
                </ColorMap>
                <ContrastEnhancement>
                        <GammaValue>1.0</GammaValue>
                </ContrastEnhancement>
        </RasterSymbolizer>*/

        RasterSymbolizer sym = (RasterSymbolizer) parse("example-rastersymbolizer2.xml");
        assertEquals(1.0, sym.getOpacity().evaluate(null, Double.class));
        assertEquals(OverlapBehavior.LATEST_ON_TOP, sym.getOverlapBehavior());
        
        SelectedChannelType[] ch = sym.getChannelSelection().getRGBChannels();
        assertEquals("1", ch[0].getChannelName());
        assertEquals(ContrastMethod.HISTOGRAM, ch[0].getContrastEnhancement().getMethod());
        assertEquals("2", ch[1].getChannelName());
        assertEquals(2.5, ch[1].getContrastEnhancement().getGammaValue().evaluate(null, Double.class));
        assertEquals("3", ch[2].getChannelName());
        assertEquals(ContrastMethod.NORMALIZE, ch[2].getContrastEnhancement().getMethod());
        
        ColorMap map = sym.getColorMap();
        assertNotNull(map);
        assertEquals(2, map.getColorMapEntries().length);
        
        Color c = map.getColorMapEntry(0).getColor().evaluate(null, Color.class);
        assertEquals(Color.BLACK, c);
        
        c = map.getColorMapEntry(1).getColor().evaluate(null, Color.class);
        assertEquals(Color.WHITE, c);
        
        assertEquals(1.0, sym.getContrastEnhancement().getGammaValue().evaluate(null, Double.class));
    }
    
    public void testParseFeatureStyle() throws Exception {
        /*
        <FeatureTypeStyle version="1.1.0" xsi:schemaLocation="http://www.opengis.net/se/1.1.0/FeatureStyle.xsd" xmlns="http://www.opengis.net/se" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"  xmlns:oceansea="http://www.myurl.net/oceansea">
                <FeatureTypeName>oceansea:Foundation</FeatureTypeName>
                <Rule>
                        <Name>main</Name>
                        <PolygonSymbolizer uom="http://www.opengeospatial.org/sld/units/pixel">
                                <Fill>
                                        <SvgParameter name="fill">#96C3F5</SvgParameter>
                                </Fill>
                        </PolygonSymbolizer>
                </Rule>
        </FeatureTypeStyle>*/
        
        FeatureTypeStyle fts = (FeatureTypeStyle) parse("example-featurestyle.xml");
        assertEquals("oceansea:Foundation", fts.getFeatureTypeName());
        assertEquals(1, fts.rules().size());
    
        Rule rule = fts.rules().get(0);
        assertEquals("main", rule.getName());
        
        assertEquals(1, rule.symbolizers().size());
        
        PolygonSymbolizer sym = (PolygonSymbolizer) rule.symbolizers().get(0);
        assertEquals(UomOgcMapping.PIXEL.getUnit(), sym.getUnitOfMeasure());
        assertEquals(SLD.toColor("#96C3F5"), sym.getFill().getColor().evaluate(null, Color.class));
        
    }
    
    public void testParseCoverageStyle() throws Exception {
        /*
        <CoverageStyle version="1.1.0" xsi:schemaLocation="http://www.opengis.net/se http://www.opengis.net/se/1.1.0/FeatureStyle.xsd" xmlns="http://www.opengis.net/se" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
        <Rule>
            <Name>ChannelSelection</Name>
            <Description>
                <Title>Gray channel mapping</Title>
            </Description>
            <RasterSymbolizer>
                <ChannelSelection>
                    <GrayChannel>
                        <SourceChannelName>Band.band1</SourceChannelName>
                    </GrayChannel>
                </ChannelSelection>
                <ContrastEnhancement>
                    <Normalize/>
                </ContrastEnhancement>
            </RasterSymbolizer>
        </Rule>
        </CoverageStyle>
        */
        
        FeatureTypeStyle cs = (FeatureTypeStyle) parse("example-coveragestyle.xml");
        assertEquals(1, cs.rules().size());
        Rule rule = cs.rules().get(0);
        
        assertEquals("ChannelSelection", rule.getName());
        assertEquals("Gray channel mapping", rule.getDescription().getTitle().toString());
        assertEquals(1, rule.symbolizers().size());
        
        RasterSymbolizer sym = (RasterSymbolizer) rule.symbolizers().get(0);
        assertEquals("Band.band1", sym.getChannelSelection().getGrayChannel().getChannelName());
    }
    
    
    Object parse(String filename) throws Exception {
        SEConfiguration se = new SEConfiguration();
        Parser p = new Parser(se);
        return p.parse(getClass().getResourceAsStream(filename));
    }
}
