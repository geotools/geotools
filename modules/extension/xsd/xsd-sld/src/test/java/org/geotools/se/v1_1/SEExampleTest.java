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

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.function.EnvFunction;
import org.geotools.styling.AnchorPoint;
import org.geotools.styling.ColorMap;
import org.geotools.styling.DefaultResourceLocator;
import org.geotools.styling.ExternalGraphic;
import org.geotools.styling.ExternalMark;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Fill;
import org.geotools.styling.Font;
import org.geotools.styling.Graphic;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.Mark;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.ResourceLocator;
import org.geotools.styling.SLD;
import org.geotools.styling.SelectedChannelType;
import org.geotools.styling.Stroke;
import org.geotools.styling.TextSymbolizer;
import org.geotools.styling.TextSymbolizer2;
import org.geotools.styling.UomOgcMapping;
import org.geotools.xsd.Parser;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Function;
import org.opengis.style.ContrastMethod;
import org.opengis.style.Displacement;
import org.opengis.style.GraphicalSymbol;
import org.opengis.style.OverlapBehavior;
import org.opengis.style.Rule;
import org.picocontainer.MutablePicoContainer;

public class SEExampleTest extends SETestSupport {

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
        assertEquals(
                "This is just a simple example of a point symbolizer.",
                sym.getDescription().getAbstract().toString());

        Graphic g = sym.getGraphic();
        assertEquals(8.0, g.getSize().evaluate(null, Double.class));
        assertEquals(1, g.graphicalSymbols().size());

        Mark m = (Mark) g.graphicalSymbols().get(0);
        assertEquals("star", m.getWellKnownName().evaluate(null, String.class));
        Color c = m.getFill().getColor().evaluate(null, Color.class);
        assertEquals(255, c.getRed());
        assertNull(m.getStroke());
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
        assertEquals(
                "This is just a simple example of a point symbolizer.",
                sym.getDescription().getAbstract().toString());

        Graphic g = sym.getGraphic();
        assertEquals(15.0, g.getSize().evaluate(null, Double.class));
        assertEquals(2, g.graphicalSymbols().size());

        ExternalGraphic eg = (ExternalGraphic) g.graphicalSymbols().get(0);
        assertEquals("http://www.vendor.com/geosym/2267.svg", eg.getLocation().toString());
        assertEquals("image/svg+xml", eg.getFormat());

        eg = (ExternalGraphic) g.graphicalSymbols().get(1);
        assertEquals("http://www.vendor.com/geosym/2267.png", eg.getLocation().toString());
        assertEquals("image/png", eg.getFormat());
    }

    public void testParsePointSymbolizer3() throws Exception {
        /*<PointSymbolizer version="1.1.0" xmlns="http://www.opengis.net/se" uom="http://www.opengeospatial.org/se/units/pixel">
            <Name>MyPointSymbolizer</Name>
            <Description>
                <Title>Example Pointsymbolizer</Title>
                <Abstract>This is just a simple example of a point symbolizer.</Abstract>
            </Description>
            <Graphic>
                <ExternalGraphic>
                    <InlineContent encoding="base64"> iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAIAAACQkWg2AAAAK3RFWHRDcmVhdGlvbiBUaW1lAFd0IDE0IHdyeiAyMDEwIDEyOjA2OjAyICswMTAweoAlkgAAAAd0SU1FB9oJDgo6HdmGt90AAAAJcEhZcwAACxIAAAsSAdLdfvwAAAAEZ0FNQQAAsY8L/GEFAAABfklEQVR42mP8//8/AwYACn779o2bmxtTiokBG9iwboOfq9+1K9eI0vDp06cZk2e8ffp2zuQ5mPZj0bB963aGLwxTI6YdO3QMiyX/UcHz58/tLeyXZ674O+NfgVthWnTaz58/kRWg27B8yXKu31yeGp6MjIwpVimnjp86fvg4Tic9ffp0zfI1ZU7lfBx8QK6WpFasSdzMiTN//fqFRQPQusULFsvzyFsqWMIFw4zDHt5+uH/Xfiwarl27tnj+4nSzdDYWNrigtIB0iF5IZ1MnMOhQNACNnztjrq64roWiJQMqCDUO+/D6w/YN21E0XLp06fD+w3l2+cjGwy1Jt82YO3Pe+/fvoRqAfupq6bKVs7VQsMAa8REmEf8+/V04fSFUw/Hjxy+cvZBklgwMSqwagIGWYpm6cOEiYDCyAI2fPnG6mrD6n39/Lj25xIADAB325/vvVQtXsXz58uX7z+8vfr7I2ZrNgBew87HfvnebERg+X79+/ffvHwMRgJWVFQBa4Mt756r78AAAAABJRU5ErkJggg==</InlineContent>
                    <Format>image/png</Format>
                </ExternalGraphic>
                <Size>15.0</Size>
            </Graphic>
        </PointSymbolizer>*/
        BufferedImage referenceImage = getReferenceImage("inlineContent-image.png");

        PointSymbolizer sym = (PointSymbolizer) parse("example-pointsymbolizer3.xml");
        assertEquals("MyPointSymbolizer", sym.getName());
        assertEquals("Example Pointsymbolizer", sym.getDescription().getTitle().toString());
        assertEquals(
                "This is just a simple example of a point symbolizer.",
                sym.getDescription().getAbstract().toString());

        Graphic g = sym.getGraphic();
        assertEquals(15.0, g.getSize().evaluate(null, Double.class));
        assertEquals(1, g.graphicalSymbols().size());

        ExternalGraphic eg = (ExternalGraphic) g.graphicalSymbols().get(0);
        assertNull(eg.getLocation());
        assertEquals("image/png", eg.getFormat());
        assertImagesEqual(referenceImage, eg.getInlineContent());
        assertNull(eg.getLocation());
    }

    public void testParsePointSymbolizer4() throws Exception {
        /*<PointSymbolizer version="1.1.0" xmlns="http://www.opengis.net/se" uom="http://www.opengeospatial.org/se/units/pixel">
            <Name>MyPointSymbolizer</Name>
            <Description>
                <Title>Example Pointsymbolizer</Title>
                <Abstract>This is just a simple example of a point symbolizer.</Abstract>
            </Description>
            <Graphic>
                <Mark>
                    <InlineContent encoding="base64">iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAIAAACQkWg2AAAAK3RFWHRDcmVhdGlvbiBUaW1lAFd0IDE0IHdyeiAyMDEwIDEyOjA2OjAyICswMTAweoAlkgAAAAd0SU1FB9oJDgo6HdmGt90AAAAJcEhZcwAACxIAAAsSAdLdfvwAAAAEZ0FNQQAAsY8L/GEFAAABfklEQVR42mP8//8/AwYACn779o2bmxtTiokBG9iwboOfq9+1K9eI0vDp06cZk2e8ffp2zuQ5mPZj0bB963aGLwxTI6YdO3QMiyX/UcHz58/tLeyXZ674O+NfgVthWnTaz58/kRWg27B8yXKu31yeGp6MjIwpVimnjp86fvg4Tic9ffp0zfI1ZU7lfBx8QK6WpFasSdzMiTN//fqFRQPQusULFsvzyFsqWMIFw4zDHt5+uH/Xfiwarl27tnj+4nSzdDYWNrigtIB0iF5IZ1MnMOhQNACNnztjrq64roWiJQMqCDUO+/D6w/YN21E0XLp06fD+w3l2+cjGwy1Jt82YO3Pe+/fvoRqAfupq6bKVs7VQsMAa8REmEf8+/V04fSFUw/Hjxy+cvZBklgwMSqwagIGWYpm6cOEiYDCyAI2fPnG6mrD6n39/Lj25xIADAB325/vvVQtXsXz58uX7z+8vfr7I2ZrNgBew87HfvnebERg+X79+/ffvHwMRgJWVFQBa4Mt756r78AAAAABJRU5ErkJggg==</InlineContent>
                    <Format>image/png</Format>
                </Mark>
                <Size>15.0</Size>
            </Graphic>
        </PointSymbolizer>*/
        BufferedImage referenceImage = getReferenceImage("inlineContent-image.png");

        PointSymbolizer sym = (PointSymbolizer) parse("example-pointsymbolizer4.xml");
        assertEquals("MyPointSymbolizer", sym.getName());
        assertEquals("Example Pointsymbolizer", sym.getDescription().getTitle().toString());
        assertEquals(
                "This is just a simple example of a point symbolizer.",
                sym.getDescription().getAbstract().toString());

        Graphic g = sym.getGraphic();
        assertEquals(15.0, g.getSize().evaluate(null, Double.class));
        assertEquals(1, g.graphicalSymbols().size());

        Mark mark = (Mark) g.graphicalSymbols().get(0);
        ExternalMark em = mark.getExternalMark();
        assertNotNull(em);
        assertEquals("image/png", em.getFormat());
        assertImagesEqual(referenceImage, em.getInlineContent());
        assertNull(em.getOnlineResource());
    }

    public void testParsePointSymbolizerRelativeURL() throws Exception {
        /*<?xml version="1.0" encoding="ISO-8859-1"?>
        <PointSymbolizer version="1.1.0" xsi:schemaLocation="http://www.opengis.net/se http://www.opengis.net/se/1.1.0/Symbolizer.xsd" xmlns="http://www.opengis.net/se" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" uom="http://www.opengeospatial.org/se/units/pixel">
        <Name>MyPointSymbolizer</Name>
        <Description>
                <Title>Example Pointsymbolizer</Title>
                <Abstract>This is just a simple example of a point symbolizer.</Abstract>
        </Description>
        <Graphic>
                <ExternalGraphic>
                        <OnlineResource xlink:type="simple" xlink:href="inlineContent-image.png"/>
                        <Format>image/png</Format>
                </ExternalGraphic>
                <Size>15.0</Size>
        </Graphic>
        </PointSymbolizer>
        */

        SEConfiguration se =
                new SEConfiguration() {
                    @Override
                    protected void configureContext(MutablePicoContainer container) {

                        super.configureContext(container);

                        DefaultResourceLocator locator =
                                (DefaultResourceLocator)
                                        container.getComponentInstance(ResourceLocator.class);
                        try {
                            locator.setSourceUrl(new URL("http://my.test.host/"));
                        } catch (MalformedURLException e) {
                        }
                    }
                };
        Parser p = new Parser(se);
        PointSymbolizer sym =
                (PointSymbolizer)
                        p.parse(getClass().getResourceAsStream("example-pointsymbolizer6.xml"));
        assertEquals("MyPointSymbolizer", sym.getName());
        assertEquals("Example Pointsymbolizer", sym.getDescription().getTitle().toString());
        assertEquals(
                "This is just a simple example of a point symbolizer.",
                sym.getDescription().getAbstract().toString());

        Graphic g = sym.getGraphic();
        assertEquals(15.0, g.getSize().evaluate(null, Double.class));
        assertEquals(1, g.graphicalSymbols().size());

        ExternalGraphic eg = (ExternalGraphic) g.graphicalSymbols().get(0);
        assertEquals("http://my.test.host/inlineContent-image.png", eg.getURI().toString());
        assertEquals("image/png", eg.getFormat());
    }

    public void testParsePointSymbolizerAnchorDisplacement() throws Exception {
        PointSymbolizer sym = (PointSymbolizer) parse("example-pointsymbolizer5.xml");

        Graphic g = sym.getGraphic();
        assertEquals(15.0, g.getSize().evaluate(null, Double.class));
        assertEquals(1, g.graphicalSymbols().size());
        AnchorPoint ap = g.getAnchorPoint();
        assertNotNull(ap);
        assertEquals(0, ap.getAnchorPointX().evaluate(null, Double.class), 0d);
        assertEquals(1, ap.getAnchorPointY().evaluate(null, Double.class), 0d);
        Displacement d = g.getDisplacement();
        assertNotNull(d);
        assertEquals(10, d.getDisplacementX().evaluate(null, Double.class), 0d);
        assertEquals(20, d.getDisplacementY().evaluate(null, Double.class), 0d);
    }

    public void testParsePointSymbolizerMarkIndex() throws Exception {
        PointSymbolizer sym = (PointSymbolizer) parse("example-pointsymbolizer-markindex.xml");

        Graphic g = sym.getGraphic();
        assertEquals(1, g.graphicalSymbols().size());
        Mark mark = (Mark) g.graphicalSymbols().get(0);
        assertNotNull(mark.getExternalMark());
        ExternalMark em = mark.getExternalMark();
        assertEquals("ttf://Webdings", em.getOnlineResource().getLinkage().toString());
        assertEquals(64, em.getMarkIndex());
        assertEquals("ttf", em.getFormat());
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
        assertEquals(
                "This is just a simple example of a line symbolizer.",
                sym.getDescription().getAbstract().toString());

        Stroke s = sym.getStroke();
        assertEquals(255, s.getColor().evaluate(null, Color.class).getBlue());
        assertEquals(Integer.valueOf(2), s.getWidth().evaluate(null, Integer.class));
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
        assertEquals(
                "This is just a simple example of a polygon symbolizer.",
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
        TextSymbolizer sym = (TextSymbolizer) parse("example-textsymbolizer.xml");
        assertEquals("MyTextSymbolizer", sym.getName());
        assertEquals("Example TextSymbolizer", sym.getDescription().getTitle().toString());
        assertEquals(
                "This is just an example of a text symbolizer using the FormatNumber function.",
                sym.getDescription().getAbstract().toString());

        assertEquals("locatedAt", sym.getGeometryPropertyName());

        Function l = (Function) sym.getLabel();
        assertEquals("foobar (10)", l.evaluate(f1));
        // assertEquals("hospitalName", l);

        Font f = sym.getFont();
        assertEquals(2, f.getFamily().size());
        assertEquals("Arial", f.getFamily().get(0).evaluate(null, String.class));
        assertEquals("Sans-Serif", f.getFamily().get(1).evaluate(null, String.class));
        assertEquals("italic", f.getStyle().evaluate(null, String.class));
        assertEquals("10", f.getSize().evaluate(null, String.class));

        Fill fill = sym.getFill();
        assertEquals(Color.BLACK, fill.getColor().evaluate(null, Color.class));

        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        // System.out.println(sym.getPriority());
        assertEquals(ff.property("people"), sym.getPriority());

        Map<String, String> options = sym.getOptions();
        assertEquals(1, options.size());
        assertEquals("100", options.get("auto-wrap"));
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
        assertEquals("1", ch[0].getChannelName().evaluate(null, String.class));
        assertEquals(ContrastMethod.HISTOGRAM, ch[0].getContrastEnhancement().getMethod());
        assertEquals("2", ch[1].getChannelName().evaluate(null, String.class));
        assertEquals(
                2.5, ch[1].getContrastEnhancement().getGammaValue().evaluate(null, Double.class));
        assertEquals("3", ch[2].getChannelName().evaluate(null, String.class));
        assertEquals(ContrastMethod.NORMALIZE, ch[2].getContrastEnhancement().getMethod());

        ColorMap map = sym.getColorMap();
        assertNotNull(map);
        assertEquals(2, map.getColorMapEntries().length);

        Color c = map.getColorMapEntry(0).getColor().evaluate(null, Color.class);
        assertEquals(Color.BLACK, c);

        c = map.getColorMapEntry(1).getColor().evaluate(null, Color.class);
        assertEquals(Color.WHITE, c);

        assertEquals(
                1.0, sym.getContrastEnhancement().getGammaValue().evaluate(null, Double.class));
    }

    /** Test the Expression parser for SelectChannel */
    @Test
    public void testParseRasterChannelExpression() throws Exception {
        RasterSymbolizer sym = (RasterSymbolizer) parse("example-raster-channel-expression.xml");
        assertEquals(1.0, sym.getOpacity().evaluate(null, Double.class));
        assertEquals(OverlapBehavior.LATEST_ON_TOP, sym.getOverlapBehavior());

        SelectedChannelType[] ch = sym.getChannelSelection().getRGBChannels();

        // assert default value : 1
        EnvFunction.removeLocalValue("B1");
        assertEquals(1, ch[0].getChannelName().evaluate(null, Integer.class).intValue());
        // assert ENV variable value: B1:20
        EnvFunction.setLocalValue("B1", "20");
        assertEquals(20, ch[0].getChannelName().evaluate(null, Integer.class).intValue());
        EnvFunction.removeLocalValue("B1");

        assertEquals(ContrastMethod.HISTOGRAM, ch[0].getContrastEnhancement().getMethod());
        assertEquals("2", ch[1].getChannelName().evaluate(null, String.class));
        assertEquals(
                2.5, ch[1].getContrastEnhancement().getGammaValue().evaluate(null, Double.class));
        assertEquals("3", ch[2].getChannelName().evaluate(null, String.class));
        assertEquals(ContrastMethod.NORMALIZE, ch[2].getContrastEnhancement().getMethod());

        ColorMap map = sym.getColorMap();
        assertNotNull(map);
        assertEquals(2, map.getColorMapEntries().length);

        Color c = map.getColorMapEntry(0).getColor().evaluate(null, Color.class);
        assertEquals(Color.BLACK, c);

        c = map.getColorMapEntry(1).getColor().evaluate(null, Color.class);
        assertEquals(Color.WHITE, c);

        assertEquals(
                1.0, sym.getContrastEnhancement().getGammaValue().evaluate(null, Double.class));
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
        assertEquals(
                "oceansea:Foundation", fts.featureTypeNames().iterator().next().getLocalPart());
        assertEquals(1, fts.rules().size());

        Rule rule = fts.rules().get(0);
        assertEquals("main", rule.getName());

        assertEquals(1, rule.symbolizers().size());

        PolygonSymbolizer sym = (PolygonSymbolizer) rule.symbolizers().get(0);
        assertEquals(UomOgcMapping.PIXEL.getUnit(), sym.getUnitOfMeasure());
        assertEquals(SLD.toColor("#96C3F5"), sym.getFill().getColor().evaluate(null, Color.class));
    }

    public void testParseFeatureStyleVendor() throws Exception {
        FeatureTypeStyle fts = (FeatureTypeStyle) parse("example-featurestyle-vendor.xml");
        assertEquals(
                "oceansea:Foundation", fts.featureTypeNames().iterator().next().getLocalPart());
        assertEquals(1, fts.rules().size());
        Map<String, String> options = fts.getOptions();
        assertEquals(1, options.size());
        assertEquals("value", options.get("key"));
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
        assertEquals(
                "Band.band1",
                sym.getChannelSelection()
                        .getGrayChannel()
                        .getChannelName()
                        .evaluate(null, String.class));
    }

    public void testParseValidatePointSymbolizerGeomTransform() throws Exception {
        PointSymbolizer ps = (PointSymbolizer) parse("example-pointsymbolizer-geotrans.xml");
        assertTrue(ps.getGeometry() instanceof Function);

        List errors = validate("example-pointsymbolizer-geotrans.xml");
        assertEquals(0, errors.size());
    }

    public void testParseGraphicWithFallbacks() throws Exception {
        Graphic graphic = (Graphic) parse("example-graphic-fallback.xml");
        final List<GraphicalSymbol> symbols = graphic.graphicalSymbols();
        // check all the symbols are there (used to kick out external graphics when mark were
        // present)
        assertEquals(3, symbols.size());
        // check the order has been preserved
        ExternalGraphic eg1 = (ExternalGraphic) symbols.get(0);
        assertThat(eg1.getURI(), containsString("transport/amenity=parking.svg?fill=%2300eb00"));
        ExternalGraphic eg2 = (ExternalGraphic) symbols.get(1);
        assertThat(eg2.getURI(), containsString("transport/amenity=parking.svg"));
        assertThat(
                eg2.getURI(),
                not((containsString("transport/amenity=parking.svg?fill=%2300eb00"))));
        Mark mark = (Mark) symbols.get(2);
        assertEquals("square", mark.getWellKnownName().evaluate(null, String.class));
    }

    public void testParseTextSymbolizerWithGraphic() throws Exception {
        TextSymbolizer2 sym = (TextSymbolizer2) parse("example-textsymbolizer-graphic.xml");
        Graphic graphic = sym.getGraphic();
        assertNotNull(graphic);
        assertNotNull(graphic.graphicalSymbols());
        assertEquals(1, graphic.graphicalSymbols().size());
        Mark mark = (Mark) graphic.graphicalSymbols().get(0);
        assertEquals("square", mark.getWellKnownName().evaluate(null, String.class));
        assertEquals(Integer.valueOf(7), graphic.getSize().evaluate(null, Integer.class));
    }
}
