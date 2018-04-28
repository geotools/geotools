/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016 Open Source Geospatial Foundation (OSGeo)
 *    (C) 2014-2016 Boundless Spatial
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
package org.geotools.ysld.parse;

import static org.geotools.ysld.Ysld.transform;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.awt.*;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;
import org.geotools.filter.Filters;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.styling.*;
import org.geotools.styling.Font;
import org.geotools.styling.Stroke;
import org.geotools.ysld.TestUtils;
import org.geotools.ysld.YsldTests;
import org.junit.Test;
import org.opengis.style.ContrastMethod;

public class YsldParseCookbookTest {

    @Test
    public void testPointSimple() throws Exception {
        // <UserStyle>
        //   <Title>SLD Cook Book: Simple Point With Stroke</Title>
        //   <FeatureTypeStyle>
        //     <Rule>
        //       <PointSymbolizer>
        //         <Graphic>
        //           <Mark>
        //             <WellKnownName>circle</WellKnownName>
        //             <Fill>
        //               <CssParameter name="fill">#FF0000</CssParameter>
        //             </Fill>
        //           </Mark>
        //           <Size>6</Size>
        //         </Graphic>
        //       </PointSymbolizer>
        //     </Rule>
        //   </FeatureTypeStyle>
        // </UserStyle>
        Style style = parse("point", "simple.sld");
        assertEquals("SLD Cook Book: Simple Point With Stroke", style.getTitle());

        PointSymbolizer point = SLD.pointSymbolizer(style);
        assertEquals("circle", SLD.wellKnownName(SLD.mark(point)));
        assertEquals(1, point.getGraphic().graphicalSymbols().size());
        assertEquals(Color.red, SLD.color(SLD.fill(point)));
        assertEquals(6, SLD.pointSize(point));
    }

    @Test
    public void testPointWithStroke() throws Exception {
        //    <UserStyle>
        //      <Title>GeoServer SLD Cook Book: Simple point with stroke</Title>
        //      <FeatureTypeStyle>
        //        <Rule>
        //          <PointSymbolizer>
        //            <Graphic>
        //              <Mark>
        //                <WellKnownName>circle</WellKnownName>
        //                <Fill>
        //                  <CssParameter name="fill">#FF0000</CssParameter>
        //                </Fill>
        //                <Stroke>
        //                  <CssParameter name="stroke">#000000</CssParameter>
        //                  <CssParameter name="stroke-width">2</CssParameter>
        //                </Stroke>
        //              </Mark>
        //              <Size>6</Size>
        //            </Graphic>
        //          </PointSymbolizer>
        //        </Rule>
        //      </FeatureTypeStyle>
        //    </UserStyle>
        Style style = parse("point", "stroke.sld");

        PointSymbolizer point = SLD.pointSymbolizer(style);
        assertEquals(6, SLD.pointSize(point));

        Mark mark = SLD.pointMark(style);
        assertEquals("circle", SLD.wellKnownName(mark));
        assertEquals(Color.red, SLD.color(mark.getFill()));
        assertEquals(Color.black, SLD.color(mark.getStroke()));
        assertEquals(2, SLD.width(mark.getStroke()));
    }

    @Test
    public void testPointWithGraphic() throws Exception {
        //    <UserStyle>
        //      <Title>GeoServer SLD Cook Book: Point as graphic</Title>
        //      <FeatureTypeStyle>
        //        <Rule>
        //          <PointSymbolizer>
        //            <Graphic>
        //              <ExternalGraphic>
        //                <OnlineResource
        //                  xlink:type="simple"
        //                  xlink:href="smileyface.png" />
        //                <Format>image/png</Format>
        //              </ExternalGraphic>
        //              <Size>32</Size>
        //            </Graphic>
        //          </PointSymbolizer>
        //        </Rule>
        //      </FeatureTypeStyle>
        //    </UserStyle>
        Style style = parse("point", "graphic.sld");

        Graphic graphic = SLD.graphic(SLD.pointSymbolizer(style));
        assertEquals(32, Filters.asInt(graphic.getSize()));

        ExternalGraphic external = (ExternalGraphic) graphic.graphicalSymbols().get(0);
        assertEquals("smileyface.png", external.getLocation().getPath());
        assertEquals("image/png", external.getFormat());
    }

    @Test
    public void testPointWithScale() throws Exception {
        //    <UserStyle>
        //      <Title>GeoServer SLD Cook Book: Zoom-based point</Title>
        //      <FeatureTypeStyle>
        //        <Rule>
        //          <Name>Large</Name>
        //          <MaxScaleDenominator>160000000</MaxScaleDenominator>
        //          <PointSymbolizer>
        //            <Graphic>
        //              <Mark>
        //                <WellKnownName>circle</WellKnownName>
        //                <Fill>
        //                  <CssParameter name="fill">#CC3300</CssParameter>
        //                </Fill>
        //              </Mark>
        //              <Size>12</Size>
        //            </Graphic>
        //          </PointSymbolizer>
        //        </Rule>
        //        <Rule>
        //          <Name>Medium</Name>
        //          <MinScaleDenominator>160000000</MinScaleDenominator>
        //          <MaxScaleDenominator>320000000</MaxScaleDenominator>
        //          <PointSymbolizer>
        //            <Graphic>
        //              <Mark>
        //                <WellKnownName>circle</WellKnownName>
        //                <Fill>
        //                  <CssParameter name="fill">#CC3300</CssParameter>
        //                </Fill>
        //              </Mark>
        //              <Size>8</Size>
        //            </Graphic>
        //          </PointSymbolizer>
        //        </Rule>
        //        <Rule>
        //          <Name>Small</Name>
        //          <MinScaleDenominator>320000000</MinScaleDenominator>
        //          <PointSymbolizer>
        //            <Graphic>
        //              <Mark>
        //                <WellKnownName>circle</WellKnownName>
        //                <Fill>
        //                  <CssParameter name="fill">#CC3300</CssParameter>
        //                </Fill>
        //              </Mark>
        //              <Size>4</Size>
        //            </Graphic>
        //          </PointSymbolizer>
        //        </Rule>
        //      </FeatureTypeStyle>
        //    </UserStyle>
        Style style = parse("point", "zoom.sld");
        Rule rule = style.featureTypeStyles().get(0).rules().get(0);
        assertEquals("Large", rule.getName());
        assertEquals(160000000.0, rule.getMaxScaleDenominator(), 0.1);

        PointSymbolizer point = (PointSymbolizer) rule.symbolizers().get(0);
        assertEquals("circle", SLD.wellKnownName(SLD.mark(point)));
        assertEquals(color("CC3300"), SLD.color(SLD.fill(point)));
        assertEquals(12, SLD.pointSize(point));

        rule = style.featureTypeStyles().get(0).rules().get(1);
        assertEquals("Medium", rule.getName());
        assertEquals(160000000.0, rule.getMinScaleDenominator(), 0.1);
        assertEquals(320000000.0, rule.getMaxScaleDenominator(), 0.1);

        point = (PointSymbolizer) rule.symbolizers().get(0);
        assertEquals("circle", SLD.wellKnownName(SLD.mark(point)));
        assertEquals(color("CC3300"), SLD.color(SLD.fill(point)));
        assertEquals(8, SLD.pointSize(point));

        rule = style.featureTypeStyles().get(0).rules().get(2);
        assertEquals("Small", rule.getName());
        assertEquals(320000000.0, rule.getMinScaleDenominator(), 0.1);

        point = (PointSymbolizer) rule.symbolizers().get(0);
        assertEquals("circle", SLD.wellKnownName(SLD.mark(point)));
        assertEquals(color("CC3300"), SLD.color(SLD.fill(point)));
        assertEquals(4, SLD.pointSize(point));
    }

    @Test
    public void testPointWithAttribute() throws Exception {
        //    <UserStyle>
        //      <Title>GeoServer SLD Cook Book: Attribute-based point</Title>
        //      <FeatureTypeStyle>
        //        <Rule>
        //          <Name>SmallPop</Name>
        //          <Title>1 to 50000</Title>
        //          <ogc:Filter>
        //            <ogc:PropertyIsLessThan>
        //              <ogc:PropertyName>pop</ogc:PropertyName>
        //              <ogc:Literal>50000</ogc:Literal>
        //            </ogc:PropertyIsLessThan>
        //          </ogc:Filter>
        //          <PointSymbolizer>
        //            <Graphic>
        //              <Mark>
        //                <WellKnownName>circle</WellKnownName>
        //                <Fill>
        //                  <CssParameter name="fill">#0033CC</CssParameter>
        //                </Fill>
        //              </Mark>
        //              <Size>8</Size>
        //            </Graphic>
        //          </PointSymbolizer>
        //        </Rule>
        //        <Rule>
        //          <Name>MediumPop</Name>
        //          <Title>50000 to 100000</Title>
        //          <ogc:Filter>
        //            <ogc:And>
        //              <ogc:PropertyIsGreaterThanOrEqualTo>
        //                <ogc:PropertyName>pop</ogc:PropertyName>
        //                <ogc:Literal>50000</ogc:Literal>
        //              </ogc:PropertyIsGreaterThanOrEqualTo>
        //              <ogc:PropertyIsLessThan>
        //                <ogc:PropertyName>pop</ogc:PropertyName>
        //                <ogc:Literal>100000</ogc:Literal>
        //              </ogc:PropertyIsLessThan>
        //            </ogc:And>
        //          </ogc:Filter>
        //          <PointSymbolizer>
        //            <Graphic>
        //              <Mark>
        //                <WellKnownName>circle</WellKnownName>
        //                <Fill>
        //                  <CssParameter name="fill">#0033CC</CssParameter>
        //                </Fill>
        //              </Mark>
        //              <Size>12</Size>
        //            </Graphic>
        //          </PointSymbolizer>
        //        </Rule>
        //        <Rule>
        //          <Name>LargePop</Name>
        //          <Title>Greater than 100000</Title>
        //          <ogc:Filter>
        //            <ogc:PropertyIsGreaterThanOrEqualTo>
        //              <ogc:PropertyName>pop</ogc:PropertyName>
        //              <ogc:Literal>100000</ogc:Literal>
        //            </ogc:PropertyIsGreaterThanOrEqualTo>
        //          </ogc:Filter>
        //          <PointSymbolizer>
        //            <Graphic>
        //              <Mark>
        //                <WellKnownName>circle</WellKnownName>
        //                <Fill>
        //                  <CssParameter name="fill">#0033CC</CssParameter>
        //                </Fill>
        //              </Mark>
        //              <Size>16</Size>
        //            </Graphic>
        //          </PointSymbolizer>
        //        </Rule>
        //      </FeatureTypeStyle>
        //    </UserStyle>
        Style style = parse("point", "attribute.sld");

        Rule rule = style.featureTypeStyles().get(0).rules().get(0);
        assertEquals("SmallPop", rule.getName());
        assertEquals("pop < '50000'", ECQL.toCQL(rule.getFilter()));

        rule = style.featureTypeStyles().get(0).rules().get(1);
        assertEquals("MediumPop", rule.getName());
        assertEquals("pop >= '50000' AND pop < '100000'", ECQL.toCQL(rule.getFilter()));

        rule = style.featureTypeStyles().get(0).rules().get(2);
        assertEquals("LargePop", rule.getName());
        assertEquals("pop >= '100000'", ECQL.toCQL(rule.getFilter()));
    }

    @Test
    public void testPointWithRotation() throws Exception {
        // <UserStyle>
        //   <Title>GeoServer SLD Cook Book: Rotated square</Title>
        //   <FeatureTypeStyle>
        //     <Rule>
        //       <PointSymbolizer>
        //         <Graphic>
        //           <Mark>
        //             <WellKnownName>square</WellKnownName>
        //             <Fill>
        //               <CssParameter name="fill">#009900</CssParameter>
        //             </Fill>
        //           </Mark>
        //           <Size>12</Size>
        //           <Rotation>45</Rotation>
        //         </Graphic>
        //       </PointSymbolizer>
        //     </Rule>
        //   </FeatureTypeStyle>
        // </UserStyle>
        Style style = parse("point", "rotated-square.sld");

        PointSymbolizer point = SLD.pointSymbolizer(style);
        assertEquals("square", SLD.wellKnownName(SLD.mark(point)));
        assertEquals(color("009900"), SLD.color(SLD.fill(point)));
        assertEquals(12, SLD.pointSize(point));
        assertEquals(45, Filters.asInt(SLD.graphic(point).getRotation()));
    }

    @Test
    public void testPointWithTransparentTriangle() throws Exception {
        //    <UserStyle>
        //      <Title>GeoServer SLD Cook Book: Transparent triangle</Title>
        //      <FeatureTypeStyle>
        //        <Rule>
        //          <PointSymbolizer>
        //            <Graphic>
        //              <Mark>
        //                <WellKnownName>triangle</WellKnownName>
        //                <Fill>
        //                  <CssParameter name="fill">#009900</CssParameter>
        //                  <CssParameter name="fill-opacity">0.2</CssParameter>
        //                </Fill>
        //                <Stroke>
        //                  <CssParameter name="stroke">#000000</CssParameter>
        //                  <CssParameter name="stroke-width">2</CssParameter>
        //                </Stroke>
        //              </Mark>
        //              <Size>12</Size>
        //            </Graphic>
        //          </PointSymbolizer>
        //        </Rule>
        //      </FeatureTypeStyle>
        //    </UserStyle>
        Style style = parse("point", "transparent-triangle.sld");

        PointSymbolizer point = SLD.pointSymbolizer(style);
        assertEquals("triangle", SLD.wellKnownName(SLD.mark(point)));
        assertEquals(color("009900"), SLD.color(SLD.fill(point)));
        assertEquals(0.2, SLD.opacity(SLD.fill(point)), 0.1);
        assertEquals(Color.black, SLD.color(SLD.stroke(point)));
        assertEquals(2, SLD.width(SLD.stroke(point)));
        assertEquals(12, SLD.pointSize(point));
    }

    @Test
    public void testPointWithLabel() throws Exception {
        //    <UserStyle>
        //      <Title>GeoServer SLD Cook Book: Point with default label</Title>
        //      <FeatureTypeStyle>
        //        <Rule>
        //          <PointSymbolizer>
        //            <Graphic>
        //              <Mark>
        //                <WellKnownName>circle</WellKnownName>
        //                <Fill>
        //                  <CssParameter name="fill">#FF0000</CssParameter>
        //                </Fill>
        //              </Mark>
        //              <Size>6</Size>
        //            </Graphic>
        //          </PointSymbolizer>
        //          <TextSymbolizer>
        //            <Label>
        //              <ogc:PropertyName>name</ogc:PropertyName>
        //            </Label>
        //            <Font />
        //            <Fill>
        //              <CssParameter name="fill">#000000</CssParameter>
        //            </Fill>
        //          </TextSymbolizer>
        //        </Rule>
        //      </FeatureTypeStyle>
        //    </UserStyle>
        Style style = parse("point", "default-label.sld");

        TextSymbolizer text = SLD.textSymbolizer(style);
        assertEquals("name", SLD.textLabelString(text));
        assertEquals(Color.black, SLD.textFontFill(text));
    }

    @Test
    public void testPointWithStyledLabel() throws Exception {
        //    <UserStyle>
        //      <Title>GeoServer SLD Cook Book: Point with styled label</Title>
        //      <FeatureTypeStyle>
        //        <Rule>
        //          <PointSymbolizer>
        //            <Graphic>
        //              <Mark>
        //                <WellKnownName>circle</WellKnownName>
        //                <Fill>
        //                  <CssParameter name="fill">#FF0000</CssParameter>
        //                </Fill>
        //              </Mark>
        //              <Size>6</Size>
        //            </Graphic>
        //          </PointSymbolizer>
        //          <TextSymbolizer>
        //            <Label>
        //              <ogc:PropertyName>name</ogc:PropertyName>
        //            </Label>
        //            <Font>
        //              <CssParameter name="font-family">Arial</CssParameter>
        //              <CssParameter name="font-size">12</CssParameter>
        //              <CssParameter name="font-style">normal</CssParameter>
        //              <CssParameter name="font-weight">bold</CssParameter>
        //            </Font>
        //            <LabelPlacement>
        //              <PointPlacement>
        //                <AnchorPoint>
        //                  <AnchorPointX>0.5</AnchorPointX>
        //                  <AnchorPointY>0.0</AnchorPointY>
        //                </AnchorPoint>
        //                <Displacement>
        //                  <DisplacementX>0</DisplacementX>
        //                  <DisplacementY>5</DisplacementY>
        //                </Displacement>
        //              </PointPlacement>
        //            </LabelPlacement>
        //            <Fill>
        //              <CssParameter name="fill">#000000</CssParameter>
        //            </Fill>
        //          </TextSymbolizer>
        //        </Rule>
        //      </FeatureTypeStyle>
        //    </UserStyle>
        Style style = parse("point", "styled-label.sld");

        TextSymbolizer text = SLD.textSymbolizer(style);
        assertEquals("name", SLD.textLabelString(text));
        assertEquals(Color.black, SLD.textFontFill(text));

        Font font = SLD.font(text);
        assertEquals("Arial", Filters.asString(font.getFamily().get(0)));
        assertEquals(12, Filters.asInt(font.getSize()));
        assertEquals("bold", Filters.asString(font.getWeight()));
        assertEquals("normal", Filters.asString(font.getStyle()));

        PointPlacement placement = (PointPlacement) text.getLabelPlacement();
        assertEquals(0.5, Filters.asDouble(placement.getAnchorPoint().getAnchorPointX()), 0.1);
        assertEquals(0.0, Filters.asDouble(placement.getAnchorPoint().getAnchorPointY()), 0.1);
        assertEquals(0, Filters.asInt(placement.getDisplacement().getDisplacementX()));
        assertEquals(5, Filters.asInt(placement.getDisplacement().getDisplacementY()));
    }

    @Test
    public void testPointWithRotatedLabel() throws Exception {
        // <UserStyle>
        //   <Title>GeoServer SLD Cook Book: Point with rotated label</Title>
        //   <FeatureTypeStyle>
        //     <Rule>
        //       <PointSymbolizer>
        //         <Graphic>
        //           <Mark>
        //             <WellKnownName>circle</WellKnownName>
        //             <Fill>
        //               <CssParameter name="fill">#FF0000</CssParameter>
        //             </Fill>
        //           </Mark>
        //           <Size>6</Size>
        //         </Graphic>
        //       </PointSymbolizer>
        //       <TextSymbolizer>
        //         <Label>
        //           <ogc:PropertyName>name</ogc:PropertyName>
        //         </Label>
        //         <Font>
        //           <CssParameter name="font-family">Arial</CssParameter>
        //           <CssParameter name="font-size">12</CssParameter>
        //           <CssParameter name="font-style">normal</CssParameter>
        //           <CssParameter name="font-weight">bold</CssParameter>
        //         </Font>
        //         <LabelPlacement>
        //           <PointPlacement>
        //             <AnchorPoint>
        //               <AnchorPointX>0.5</AnchorPointX>
        //               <AnchorPointY>0.0</AnchorPointY>
        //             </AnchorPoint>
        //             <Displacement>
        //               <DisplacementX>0</DisplacementX>
        //               <DisplacementY>25</DisplacementY>
        //             </Displacement>
        //             <Rotation>-45</Rotation>
        //           </PointPlacement>
        //         </LabelPlacement>
        //         <Fill>
        //           <CssParameter name="fill">#990099</CssParameter>
        //         </Fill>
        //       </TextSymbolizer>
        //     </Rule>
        //   </FeatureTypeStyle>
        // </UserStyle>
        Style style = parse("point", "rotated-label.sld");

        TextSymbolizer text = SLD.textSymbolizer(style);
        assertEquals("name", SLD.textLabelString(text));
        assertEquals(color("990099"), SLD.textFontFill(text));

        Font font = SLD.font(text);
        assertEquals("Arial", Filters.asString(font.getFamily().get(0)));
        assertEquals(12, Filters.asInt(font.getSize()));
        assertEquals("bold", Filters.asString(font.getWeight()));
        assertEquals("normal", Filters.asString(font.getStyle()));

        PointPlacement placement = (PointPlacement) text.getLabelPlacement();
        assertEquals(0.5, Filters.asDouble(placement.getAnchorPoint().getAnchorPointX()), 0.1);
        assertEquals(0.0, Filters.asDouble(placement.getAnchorPoint().getAnchorPointY()), 0.1);
        assertEquals(0, Filters.asInt(placement.getDisplacement().getDisplacementX()));
        assertEquals(25, Filters.asInt(placement.getDisplacement().getDisplacementY()));
        assertEquals(-45, Filters.asInt(placement.getRotation()));
    }

    @Test
    public void testLineSimple() throws Exception {
        //    <UserStyle>
        //      <Title>SLD Cook Book: Simple Line</Title>
        //      <FeatureTypeStyle>
        //        <Rule>
        //          <LineSymbolizer>
        //            <Stroke>
        //              <CssParameter name="stroke">#000000</CssParameter>
        //              <CssParameter name="stroke-width">3</CssParameter>
        //            </Stroke>
        //          </LineSymbolizer>
        //        </Rule>
        //      </FeatureTypeStyle>
        //    </UserStyle>
        Style style = parse("line", "simple.sld");

        LineSymbolizer line = SLD.lineSymbolizer(style);
        assertEquals(Color.black, SLD.lineColor(line));
        assertEquals(3, SLD.lineWidth(line));
    }

    @Test
    public void testLineWithAttribute() throws Exception {
        //    <UserStyle>
        //      <Title>SLD Cook Book: Attribute-based line</Title>
        //      <FeatureTypeStyle>
        //        <Rule>
        //          <Name>local-road</Name>
        //          <ogc:Filter>
        //            <ogc:PropertyIsEqualTo>
        //              <ogc:PropertyName>type</ogc:PropertyName>
        //              <ogc:Literal>local-road</ogc:Literal>
        //            </ogc:PropertyIsEqualTo>
        //          </ogc:Filter>
        //          <LineSymbolizer>
        //            <Stroke>
        //              <CssParameter name="stroke">#009933</CssParameter>
        //              <CssParameter name="stroke-width">2</CssParameter>
        //            </Stroke>
        //          </LineSymbolizer>
        //        </Rule>
        //      </FeatureTypeStyle>
        //      <FeatureTypeStyle>
        //        <Rule>
        //          <Name>secondary</Name>
        //          <ogc:Filter>
        //            <ogc:PropertyIsEqualTo>
        //              <ogc:PropertyName>type</ogc:PropertyName>
        //              <ogc:Literal>secondary</ogc:Literal>
        //            </ogc:PropertyIsEqualTo>
        //          </ogc:Filter>
        //          <LineSymbolizer>
        //            <Stroke>
        //              <CssParameter name="stroke">#0055CC</CssParameter>
        //              <CssParameter name="stroke-width">3</CssParameter>
        //            </Stroke>
        //          </LineSymbolizer>
        //        </Rule>
        //      </FeatureTypeStyle>
        //      <FeatureTypeStyle>
        //        <Rule>
        //        <Name>highway</Name>
        //          <ogc:Filter>
        //            <ogc:PropertyIsEqualTo>
        //              <ogc:PropertyName>type</ogc:PropertyName>
        //              <ogc:Literal>highway</ogc:Literal>
        //            </ogc:PropertyIsEqualTo>
        //          </ogc:Filter>
        //          <LineSymbolizer>
        //            <Stroke>
        //              <CssParameter name="stroke">#FF0000</CssParameter>
        //              <CssParameter name="stroke-width">6</CssParameter>
        //            </Stroke>
        //          </LineSymbolizer>
        //        </Rule>
        //      </FeatureTypeStyle>
        //    </UserStyle>

        Style style = parse("line", "attribute.sld");

        FeatureTypeStyle featureStyle = style.featureTypeStyles().get(0);
        Rule rule = featureStyle.rules().get(0);
        assertEquals("local-road", rule.getName());
        assertEquals("type = 'local-road'", ECQL.toCQL(rule.getFilter()));

        LineSymbolizer line = (LineSymbolizer) rule.symbolizers().get(0);
        assertEquals(color("009933"), SLD.color(line));
        assertEquals(2, SLD.width(line));

        featureStyle = style.featureTypeStyles().get(1);
        rule = featureStyle.rules().get(0);
        assertEquals("secondary", rule.getName());
        assertEquals("type = 'secondary'", ECQL.toCQL(rule.getFilter()));

        line = (LineSymbolizer) rule.symbolizers().get(0);
        assertEquals(color("0055CC"), SLD.color(line));
        assertEquals(3, SLD.width(line));

        featureStyle = style.featureTypeStyles().get(2);
        rule = featureStyle.rules().get(0);
        assertEquals("highway", rule.getName());
        assertEquals("type = 'highway'", ECQL.toCQL(rule.getFilter()));

        line = (LineSymbolizer) rule.symbolizers().get(0);
        assertEquals(color("FF0000"), SLD.color(line));
        assertEquals(6, SLD.width(line));
    }

    @Test
    public void testLineWithBorder() throws Exception {
        //    <UserStyle>
        //    <Title>SLD Cook Book: Line w2th border</Title>
        //      <FeatureTypeStyle>
        //         <Rule>
        //          <LineSymbolizer>
        //            <Stroke>
        //              <CssParameter name="stroke">#333333</CssParameter>
        //              <CssParameter name="stroke-width">5</CssParameter>
        //              <CssParameter name="stroke-linecap">round</CssParameter>
        //            </Stroke>
        //          </LineSymbolizer>
        //        </Rule>
        //      </FeatureTypeStyle>
        //      <FeatureTypeStyle>
        //         <Rule>
        //          <LineSymbolizer>
        //          <Stroke>
        //              <CssParameter name="stroke">#6699FF</CssParameter>
        //              <CssParameter name="stroke-width">3</CssParameter>
        //              <CssParameter name="stroke-linecap">round</CssParameter>
        //            </Stroke>
        //          </LineSymbolizer>
        //         </Rule>
        //      </FeatureTypeStyle>
        //    </UserStyle>

        Style style = parse("line", "border.sld");

        FeatureTypeStyle featureStyle = style.featureTypeStyles().get(0);
        LineSymbolizer line = SLD.lineSymbolizer(featureStyle);

        assertEquals(color("333333"), SLD.color(line));
        assertEquals(5, SLD.width(line));
        assertEquals("round", SLD.lineLinecap(line));

        featureStyle = style.featureTypeStyles().get(1);
        line = SLD.lineSymbolizer(featureStyle);

        assertEquals(color("6699FF"), SLD.color(line));
        assertEquals(3, SLD.width(line));
        assertEquals("round", SLD.lineLinecap(line));
    }

    @Test
    public void testLineWithCurvedLabel() throws Exception {

        //    <UserStyle>
        //      <Title>SLD Cook Book: Label following line</Title>
        //      <FeatureTypeStyle>
        //        <Rule>
        //          <LineSymbolizer>
        //            <Stroke>
        //              <CssParameter name="stroke">#FF0000</CssParameter>
        //            </Stroke>
        //          </LineSymbolizer>
        //          <TextSymbolizer>
        //            <Label>
        //              <ogc:PropertyName>name</ogc:PropertyName>
        //            </Label>
        //            <LabelPlacement>
        //              <LinePlacement />
        //            </LabelPlacement>
        //            <Fill>
        //              <CssParameter name="fill">#000000</CssParameter>
        //            </Fill>
        //            <VendorOption name="followLine">true</VendorOption>
        //          </TextSymbolizer>
        //        </Rule>
        //      </FeatureTypeStyle>
        //    </UserStyle>

        Style style = parse("line", "curved-label.sld");
        TextSymbolizer text = SLD.textSymbolizer(style);
        assertEquals("name", SLD.textLabelString(text));
        Map<String, String> options = text.getOptions();
        assertEquals("true", options.get("followLine"));
    }

    @Test
    public void testLineWithDashdot() throws Exception {
        //    <UserStyle>
        //      <Title>SLD Cook Book: Dash/Symbol line</Title>
        //      <FeatureTypeStyle>
        //        <Rule>
        //          <LineSymbolizer>
        //            <Stroke>
        //              <CssParameter name="stroke">#0000FF</CssParameter>
        //              <CssParameter name="stroke-width">1</CssParameter>
        //              <CssParameter name="stroke-dasharray">10 10</CssParameter>
        //            </Stroke>
        //          </LineSymbolizer>
        //          <LineSymbolizer>
        //            <Stroke>
        //              <GraphicStroke>
        //                <Graphic>
        //                  <Mark>
        //                    <WellKnownName>circle</WellKnownName>
        //                    <Stroke>
        //                      <CssParameter name="stroke">#000033</CssParameter>
        //                      <CssParameter name="stroke-width">1</CssParameter>
        //                    </Stroke>
        //                  </Mark>
        //                  <Size>5</Size>
        //                </Graphic>
        //              </GraphicStroke>
        //              <CssParameter name="stroke-dasharray">5 15</CssParameter>
        //              <CssParameter name="stroke-dashoffset">7.5</CssParameter>
        //            </Stroke>
        //          </LineSymbolizer>
        //        </Rule>
        //      </FeatureTypeStyle>
        //    </UserStyle>
        Style style = parse("line", "dash-dot.sld");

        LineSymbolizer line = (LineSymbolizer) SLD.rules(style)[0].symbolizers().get(0);
        assertEquals(Color.blue, SLD.color(line));
        assertEquals(1, SLD.width(line));
        assertEquals(10f, SLD.lineDash(line)[0], 0.1);
        assertEquals(10f, SLD.lineDash(line)[0], 0.1);

        line = (LineSymbolizer) SLD.rules(style)[0].symbolizers().get(1);
        Stroke stroke = line.getStroke();

        assertEquals(5f, SLD.lineDash(line)[0], 0.1);
        assertEquals(15f, SLD.lineDash(line)[1], 0.1);
        assertEquals(7.5, Filters.asDouble(stroke.getDashOffset()), 0.1);

        Graphic g = stroke.getGraphicStroke();
        assertEquals(5, Filters.asInt(g.getSize()));

        Mark mark = SLD.mark(g);
        assertEquals("circle", SLD.wellKnownName(mark));
        assertEquals(color("000033"), SLD.color(mark.getStroke()));
        assertEquals(1, SLD.width(mark.getStroke()));
    }

    @Test
    public void testLineWithDashedline() throws Exception {
        //    <UserStyle>
        //      <Title>SLD Cook Book: Dashed line</Title>
        //      <FeatureTypeStyle>
        //        <Rule>
        //          <LineSymbolizer>
        //            <Stroke>
        //              <CssParameter name="stroke">#0000FF</CssParameter>
        //              <CssParameter name="stroke-width">3</CssParameter>
        //              <CssParameter name="stroke-dasharray">5 2</CssParameter>
        //            </Stroke>
        //          </LineSymbolizer>
        //        </Rule>
        //      </FeatureTypeStyle>
        //    </UserStyle>

        Style style = parse("line", "dashed-line.sld");
        LineSymbolizer line = SLD.lineSymbolizer(style);
        assertEquals(Color.blue, SLD.color(line));
        assertEquals(3, SLD.width(line));
        assertEquals(5f, SLD.lineDash(line)[0], 0.1);
        assertEquals(2f, SLD.lineDash(line)[1], 0.1);
    }

    @Test
    public void testLineWithDashspace() throws Exception {
        //   <UserStyle>
        //     <Title>SLD Cook Book: Dash/Space line</Title>
        //     <FeatureTypeStyle>
        //       <Rule>
        //         <LineSymbolizer>
        //           <Stroke>
        //             <GraphicStroke>
        //               <Graphic>
        //                 <Mark>
        //                   <WellKnownName>circle</WellKnownName>
        //                   <Fill>
        //                     <CssParameter name="fill">#666666</CssParameter>
        //                   </Fill>
        //                   <Stroke>
        //                     <CssParameter name="stroke">#333333</CssParameter>
        //                     <CssParameter name="stroke-width">1</CssParameter>
        //                   </Stroke>
        //                 </Mark>
        //                 <Size>4</Size>
        //               </Graphic>
        //             </GraphicStroke>
        //             <CssParameter name="stroke-dasharray">4 6</CssParameter>
        //           </Stroke>
        //         </LineSymbolizer>
        //       </Rule>
        //     </FeatureTypeStyle>
        //   </UserStyle>

        Style style = parse("line", "dash-space.sld");

        LineSymbolizer line = SLD.lineSymbolizer(style);
        assertEquals(4f, SLD.lineDash(line)[0], 0.1);
        assertEquals(6f, SLD.lineDash(line)[1], 0.1);

        Mark mark = SLD.mark(line.getStroke().getGraphicStroke());
        assertEquals("circle", SLD.wellKnownName(mark));
        assertEquals(color("666666"), SLD.color(mark.getFill()));
        assertEquals(color("333333"), SLD.color(mark.getStroke()));
        assertEquals(1, SLD.width(mark.getStroke()));
    }

    @Test
    public void testLineWithDefaultLabel() throws Exception {
        //    <UserStyle>
        //      <Title>SLD Cook Book: Line with default label</Title>
        //      <FeatureTypeStyle>
        //        <Rule>
        //          <LineSymbolizer>
        //            <Stroke>
        //              <CssParameter name="stroke">#FF0000</CssParameter>
        //            </Stroke>
        //          </LineSymbolizer>
        //          <TextSymbolizer>
        //            <Label>
        //              <ogc:PropertyName>name</ogc:PropertyName>
        //            </Label>
        //            <Font />
        //            <Fill>
        //              <CssParameter name="fill">#000000</CssParameter>
        //            </Fill>
        //          </TextSymbolizer>
        //        </Rule>
        //      </FeatureTypeStyle>
        //    </UserStyle>

        Style style = parse("line", "default-label.sld");
        LineSymbolizer line = SLD.lineSymbolizer(style);
        assertEquals(Color.red, SLD.color(line));

        TextSymbolizer text = SLD.textSymbolizer(style);
        assertEquals("name", SLD.textLabelString(text));
        assertEquals(Color.black, SLD.color(text.getFill()));
    }

    @Test
    public void testLineWithRailroad() throws Exception {
        //    <UserStyle>
        //      <Title>SLD Cook Book: Railroad (hatching)</Title>
        //      <FeatureTypeStyle>
        //        <Rule>
        //          <LineSymbolizer>
        //            <Stroke>
        //              <CssParameter name="stroke">#333333</CssParameter>
        //              <CssParameter name="stroke-width">3</CssParameter>
        //            </Stroke>
        //          </LineSymbolizer>
        //         <LineSymbolizer>
        //            <Stroke>
        //              <GraphicStroke>
        //                <Graphic>
        //                  <Mark>
        //                    <WellKnownName>shape://vertline</WellKnownName>
        //                    <Stroke>
        //                      <CssParameter name="stroke">#333333</CssParameter>
        //                      <CssParameter name="stroke-width">1</CssParameter>
        //                    </Stroke>
        //                  </Mark>
        //                  <Size>12</Size>
        //                </Graphic>
        //              </GraphicStroke>
        //            </Stroke>
        //          </LineSymbolizer>
        //        </Rule>
        //      </FeatureTypeStyle>
        //    </UserStyle>

        Style style = parse("line", "railroad.sld");

        LineSymbolizer line = (LineSymbolizer) SLD.rules(style)[0].symbolizers().get(0);
        assertEquals(color("333333"), SLD.color(line));
        assertEquals(3, SLD.width(line));

        line = (LineSymbolizer) SLD.rules(style)[0].symbolizers().get(1);

        Graphic g = line.getStroke().getGraphicStroke();

        Mark mark = SLD.mark(g);
        assertEquals("shape://vertline", SLD.wellKnownName(mark));
        assertEquals(color("333333"), SLD.color(mark.getStroke()));
        assertEquals(1, SLD.width(mark.getStroke()));

        assertEquals(12, Filters.asInt(g.getSize()));
    }

    @Test
    public void testLineWithZoom() throws Exception {
        //    <UserStyle>
        //      <Title>SLD Cook Book: Zoom-based line</Title>
        //      <FeatureTypeStyle>
        //        <Rule>
        //          <Name>Large</Name>
        //          <MaxScaleDenominator>180000000</MaxScaleDenominator>
        //          <LineSymbolizer>
        //            <Stroke>
        //              <CssParameter name="stroke">#009933</CssParameter>
        //              <CssParameter name="stroke-width">6</CssParameter>
        //            </Stroke>
        //          </LineSymbolizer>
        //        </Rule>
        //        <Rule>
        //          <Name>Medium</Name>
        //          <MinScaleDenominator>180000000</MinScaleDenominator>
        //          <MaxScaleDenominator>360000000</MaxScaleDenominator>
        //          <LineSymbolizer>
        //            <Stroke>
        //              <CssParameter name="stroke">#009933</CssParameter>
        //              <CssParameter name="stroke-width">4</CssParameter>
        //            </Stroke>
        //          </LineSymbolizer>
        //        </Rule>
        //        <Rule>
        //          <Name>Small</Name>
        //          <MinScaleDenominator>360000000</MinScaleDenominator>
        //          <LineSymbolizer>
        //            <Stroke>
        //              <CssParameter name="stroke">#009933</CssParameter>
        //              <CssParameter name="stroke-width">2</CssParameter>
        //            </Stroke>
        //          </LineSymbolizer>
        //        </Rule>
        //      </FeatureTypeStyle>
        //    </UserStyle>

        Style style = parse("line", "zoom.sld");

        Rule rule = style.featureTypeStyles().get(0).rules().get(0);
        assertEquals("Large", rule.getName());
        assertEquals(180000000d, rule.getMaxScaleDenominator(), 0.1);

        LineSymbolizer line = (LineSymbolizer) rule.symbolizers().get(0);
        assertEquals(color("009933"), SLD.color(line));
        assertEquals(6, SLD.width(line));

        rule = style.featureTypeStyles().get(0).rules().get(1);
        assertEquals("Medium", rule.getName());
        assertEquals(360000000d, rule.getMaxScaleDenominator(), 0.1);
        assertEquals(180000000d, rule.getMinScaleDenominator(), 0.1);

        line = (LineSymbolizer) rule.symbolizers().get(0);
        assertEquals(color("009933"), SLD.color(line));
        assertEquals(4, SLD.width(line));

        rule = style.featureTypeStyles().get(0).rules().get(2);
        assertEquals("Small", rule.getName());
        assertEquals(360000000d, rule.getMinScaleDenominator(), 0.1);

        line = (LineSymbolizer) rule.symbolizers().get(0);
        assertEquals(color("009933"), SLD.color(line));
        assertEquals(2, SLD.width(line));
    }

    @Test
    public void testLineWithOptimizedLabel() throws Exception {
        //    <UserStyle>
        //      <Title>SLD Cook Book: Optimized label placement</Title>
        //      <FeatureTypeStyle>
        //        <Rule>
        //          <LineSymbolizer>
        //            <Stroke>
        //              <CssParameter name="stroke">#FF0000</CssParameter>
        //            </Stroke>
        //          </LineSymbolizer>
        //          <TextSymbolizer>
        //            <Label>
        //              <ogc:PropertyName>name</ogc:PropertyName>
        //            </Label>
        //            <LabelPlacement>
        //              <LinePlacement />
        //            </LabelPlacement>
        //            <Fill>
        //              <CssParameter name="fill">#000000</CssParameter>
        //            </Fill>
        //            <VendorOption name="followLine">true</VendorOption>
        //            <VendorOption name="maxAngleDelta">90</VendorOption>
        //            <VendorOption name="maxDisplacement">400</VendorOption>
        //            <VendorOption name="repeat">150</VendorOption>
        //          </TextSymbolizer>
        //        </Rule>
        //      </FeatureTypeStyle>
        //    </UserStyle>

        Style style = parse("line", "optimized-label.sld");

        LineSymbolizer line = SLD.lineSymbolizer(style);
        assertEquals(Color.red, SLD.color(line));

        TextSymbolizer text = SLD.textSymbolizer(style);
        assertEquals("name", SLD.textLabelString(text));
        assertEquals(Color.black, SLD.color(text.getFill()));

        Map<String, String> options = text.getOptions();
        assertEquals("true", options.get("followLine"));
        assertEquals("90", options.get("maxAngleDelta"));
        assertEquals("400", options.get("maxDisplacement"));
        assertEquals("150", options.get("repeat"));
    }

    @Test
    public void testLineWithOptimizedAndStyledLabel() throws Exception {
        //    <UserStyle>
        //      <Title>SLD Cook Book: Optimized and styled label</Title>
        //      <FeatureTypeStyle>
        //        <Rule>
        //          <LineSymbolizer>
        //            <Stroke>
        //              <CssParameter name="stroke">#FF0000</CssParameter>
        //            </Stroke>
        //          </LineSymbolizer>
        //          <TextSymbolizer>
        //            <Label>
        //              <ogc:PropertyName>name</ogc:PropertyName>
        //            </Label>
        //            <LabelPlacement>
        //              <LinePlacement />
        //            </LabelPlacement>
        //            <Fill>
        //              <CssParameter name="fill">#000000</CssParameter>
        //            </Fill>
        //            <Font>
        //              <CssParameter name="font-family">Arial</CssParameter>
        //              <CssParameter name="font-size">10</CssParameter>
        //              <CssParameter name="font-style">normal</CssParameter>
        //              <CssParameter name="font-weight">bold</CssParameter>
        //            </Font>
        //            <VendorOption name="followLine">true</VendorOption>
        //            <VendorOption name="maxAngleDelta">90</VendorOption>
        //            <VendorOption name="maxDisplacement">400</VendorOption>
        //            <VendorOption name="repeat">150</VendorOption>
        //          </TextSymbolizer>
        //        </Rule>
        //      </FeatureTypeStyle>
        //    </UserStyle>

        Style style = parse("line", "optimized-styled-label.sld");

        LineSymbolizer line = SLD.lineSymbolizer(style);
        assertEquals(Color.red, SLD.color(line));

        TextSymbolizer text = SLD.textSymbolizer(style);
        assertEquals("name", SLD.textLabelString(text));
        assertEquals(Color.black, SLD.color(text.getFill()));

        Font font = SLD.font(text);
        assertEquals("Arial", Filters.asString(font.getFontFamily()));
        assertEquals(10, Filters.asInt(font.getSize()));
        assertEquals("normal", Filters.asString(font.getStyle()));
        assertEquals("bold", Filters.asString(font.getWeight()));

        Map<String, String> options = text.getOptions();
        assertEquals("true", options.get("followLine"));
        assertEquals("90", options.get("maxAngleDelta"));
        assertEquals("400", options.get("maxDisplacement"));
        assertEquals("150", options.get("repeat"));
    }

    @Test
    public void testPolygonSimple() throws Exception {
        //    <UserStyle>
        //      <Title>SLD Cook Book: Simple polygon</Title>
        //      <FeatureTypeStyle>
        //        <Rule>
        //          <PolygonSymbolizer>
        //            <Fill>
        //              <CssParameter name="fill">#000080</CssParameter>
        //            </Fill>
        //          </PolygonSymbolizer>
        //        </Rule>
        //      </FeatureTypeStyle>
        //    </UserStyle>

        Style style = parse("poly", "simple.sld");

        PolygonSymbolizer poly = SLD.polySymbolizer(style);
        assertEquals(color("000080"), SLD.color(poly.getFill()));
    }

    @Test
    public void testPolygonWithAttribute() throws Exception {
        //    <UserStyle>
        //      <Title>SLD Cook Book: Attribute-based polygon</Title>
        //      <FeatureTypeStyle>
        //        <Rule>
        //          <Name>SmallPop</Name>
        //          <Title>Less Than 200,000</Title>
        //          <ogc:Filter>
        //            <ogc:PropertyIsLessThan>
        //              <ogc:PropertyName>pop</ogc:PropertyName>
        //              <ogc:Literal>200000</ogc:Literal>
        //            </ogc:PropertyIsLessThan>
        //          </ogc:Filter>
        //          <PolygonSymbolizer>
        //            <Fill>
        //              <CssParameter name="fill">#66FF66</CssParameter>
        //            </Fill>
        //          </PolygonSymbolizer>
        //        </Rule>
        //        <Rule>
        //          <Name>MediumPop</Name>
        //          <Title>200,000 to 500,000</Title>
        //          <ogc:Filter>
        //            <ogc:And>
        //              <ogc:PropertyIsGreaterThanOrEqualTo>
        //                <ogc:PropertyName>pop</ogc:PropertyName>
        //                <ogc:Literal>200000</ogc:Literal>
        //              </ogc:PropertyIsGreaterThanOrEqualTo>
        //              <ogc:PropertyIsLessThan>
        //                <ogc:PropertyName>pop</ogc:PropertyName>
        //                <ogc:Literal>500000</ogc:Literal>
        //              </ogc:PropertyIsLessThan>
        //            </ogc:And>
        //          </ogc:Filter>
        //          <PolygonSymbolizer>
        //            <Fill>
        //              <CssParameter name="fill">#33CC33</CssParameter>
        //            </Fill>
        //          </PolygonSymbolizer>
        //        </Rule>
        //        <Rule>
        //          <Name>LargePop</Name>
        //          <Title>Greater Than 500,000</Title>
        //          <ogc:Filter>
        //            <ogc:PropertyIsGreaterThan>
        //              <ogc:PropertyName>pop</ogc:PropertyName>
        //              <ogc:Literal>500000</ogc:Literal>
        //            </ogc:PropertyIsGreaterThan>
        //          </ogc:Filter>
        //          <PolygonSymbolizer>
        //            <Fill>
        //              <CssParameter name="fill">#009900</CssParameter>
        //            </Fill>
        //          </PolygonSymbolizer>
        //        </Rule>
        //      </FeatureTypeStyle>
        //    </UserStyle>

        Style style = parse("poly", "attribute.sld");

        Rule rule = style.featureTypeStyles().get(0).rules().get(0);
        assertEquals("SmallPop", rule.getName());
        assertEquals("pop < '200000'", ECQL.toCQL(rule.getFilter()));

        PolygonSymbolizer poly = (PolygonSymbolizer) rule.symbolizers().get(0);
        assertEquals(color("66FF66"), SLD.color(poly.getFill()));

        rule = style.featureTypeStyles().get(0).rules().get(1);
        assertEquals("MediumPop", rule.getName());
        assertEquals("pop >= '200000' AND pop < '500000'", ECQL.toCQL(rule.getFilter()));

        poly = (PolygonSymbolizer) rule.symbolizers().get(0);
        assertEquals(color("33CC33"), SLD.color(poly.getFill()));

        rule = style.featureTypeStyles().get(0).rules().get(2);
        assertEquals("LargePop", rule.getName());
        assertEquals("pop > '500000'", ECQL.toCQL(rule.getFilter()));

        poly = (PolygonSymbolizer) rule.symbolizers().get(0);
        assertEquals(color("009900"), SLD.color(poly.getFill()));
    }

    @Test
    public void testPolygonWithDefaultLabel() throws Exception {
        //    <UserStyle>
        //      <Title>SLD Cook Book: Polygon with default label</Title>
        //      <FeatureTypeStyle>
        //        <Rule>
        //          <PolygonSymbolizer>
        //            <Fill>
        //              <CssParameter name="fill">#40FF40</CssParameter>
        //            </Fill>
        //            <Stroke>
        //              <CssParameter name="stroke">#FFFFFF</CssParameter>
        //              <CssParameter name="stroke-width">2</CssParameter>
        //            </Stroke>
        //          </PolygonSymbolizer>
        //          <TextSymbolizer>
        //            <Label>
        //              <ogc:PropertyName>name</ogc:PropertyName>
        //            </Label>
        //          </TextSymbolizer>
        //        </Rule>
        //      </FeatureTypeStyle>
        //    </UserStyle>

        Style style = parse("poly", "default-label.sld");

        PolygonSymbolizer poly = SLD.polySymbolizer(style);
        assertEquals(color("40FF40"), SLD.color(poly.getFill()));
        assertEquals(color("FFFFFF"), SLD.color(poly.getStroke()));
        assertEquals(2, SLD.width(poly.getStroke()));

        TextSymbolizer text = SLD.textSymbolizer(style);
        assertEquals("name", SLD.textLabelString(text));
    }

    @Test
    public void testPolygonWithGraphicFill() throws Exception {
        //    <UserStyle>
        //      <Title>SLD Cook Book: Graphic fill</Title>
        //      <FeatureTypeStyle>
        //        <Rule>
        //          <PolygonSymbolizer>
        //            <Fill>
        //              <GraphicFill>
        //                <Graphic>
        //                  <ExternalGraphic>
        //                    <OnlineResource
        //                      xlink:type="simple"
        //                      xlink:href="colorblocks.png" />
        //                    <Format>image/png</Format>
        //                  </ExternalGraphic>
        //                <Size>93</Size>
        //                </Graphic>
        //              </GraphicFill>
        //            </Fill>
        //          </PolygonSymbolizer>
        //        </Rule>
        //      </FeatureTypeStyle>
        //    </UserStyle>

        Style style = parse("poly", "graphic-fill.sld");

        PolygonSymbolizer poly = SLD.polySymbolizer(style);
        Graphic g = poly.getFill().getGraphicFill();

        ExternalGraphic external = (ExternalGraphic) g.graphicalSymbols().get(0);
        assertEquals("file:colorblocks.png", external.getLocation().toString());
        assertEquals("image/png", external.getFormat());
        assertEquals(93, Filters.asInt(g.getSize()));
    }

    @Test
    public void testPolygonWithHaloLabel() throws Exception {
        //    <UserStyle>
        //      <Title>SLD Cook Book: Label halo</Title>
        //      <FeatureTypeStyle>
        //        <Rule>
        //          <PolygonSymbolizer>
        //            <Fill>
        //              <CssParameter name="fill">#40FF40</CssParameter>
        //            </Fill>
        //            <Stroke>
        //              <CssParameter name="stroke">#FFFFFF</CssParameter>
        //              <CssParameter name="stroke-width">2</CssParameter>
        //            </Stroke>
        //          </PolygonSymbolizer>
        //          <TextSymbolizer>
        //            <Label>
        //              <ogc:PropertyName>name</ogc:PropertyName>
        //            </Label>
        //            <Halo>
        //              <Radius>3</Radius>
        //              <Fill>
        //                <CssParameter name="fill">#FFFFFF</CssParameter>
        //              </Fill>
        //            </Halo>
        //          </TextSymbolizer>
        //        </Rule>
        //      </FeatureTypeStyle>
        //    </UserStyle>

        Style style = parse("poly", "halo-label.sld");

        PolygonSymbolizer poly = SLD.polySymbolizer(style);
        assertEquals(color("40FF40"), SLD.color(poly.getFill()));
        assertEquals(color("FFFFFF"), SLD.color(poly.getStroke()));
        assertEquals(2, SLD.width(poly.getStroke()));

        TextSymbolizer text = SLD.textSymbolizer(style);
        assertEquals("name", SLD.textLabelString(text));

        assertEquals(color("FFFFFF"), SLD.textHaloFill(text));
        assertEquals(3, SLD.textHaloWidth(text));
    }

    @Test
    public void testPolygonWithHatchFill() throws Exception {
        //    <UserStyle>
        //      <Title>SLD Cook Book: Hatching fill</Title>
        //      <FeatureTypeStyle>
        //        <Rule>
        //          <PolygonSymbolizer>
        //            <Fill>
        //              <GraphicFill>
        //                <Graphic>
        //                  <Mark>
        //                    <WellKnownName>shape://times</WellKnownName>
        //                    <Stroke>
        //                      <CssParameter name="stroke">#990099</CssParameter>
        //                      <CssParameter name="stroke-width">1</CssParameter>
        //                    </Stroke>
        //                  </Mark>
        //                  <Size>16</Size>
        //                </Graphic>
        //              </GraphicFill>
        //            </Fill>
        //          </PolygonSymbolizer>
        //        </Rule>
        //      </FeatureTypeStyle>
        //    </UserStyle>

        Style style = parse("poly", "hatch-fill.sld");

        PolygonSymbolizer poly = SLD.polySymbolizer(style);

        Mark mark = SLD.mark(poly.getFill().getGraphicFill());
        assertEquals("shape://times", Filters.asString(mark.getWellKnownName()));

        assertEquals(color("990099"), SLD.color(mark.getStroke()));
        assertEquals(1, SLD.width(mark.getStroke()));

        assertEquals(16, Filters.asInt(poly.getFill().getGraphicFill().getSize()));
    }

    @Test
    public void testPolygonWithStroke() throws Exception {
        //    <UserStyle>
        //      <Title>SLD Cook Book: Simple polygon with stroke</Title>
        //      <FeatureTypeStyle>
        //        <Rule>
        //          <PolygonSymbolizer>
        //            <Fill>
        //              <CssParameter name="fill">#000080</CssParameter>
        //            </Fill>
        //            <Stroke>
        //              <CssParameter name="stroke">#FFFFFF</CssParameter>
        //              <CssParameter name="stroke-width">2</CssParameter>
        //            </Stroke>
        //          </PolygonSymbolizer>
        //        </Rule>
        //      </FeatureTypeStyle>
        //    </UserStyle>

        Style style = parse("poly", "stroke.sld");

        PolygonSymbolizer poly = SLD.polySymbolizer(style);
        assertEquals(color("000080"), SLD.color(poly.getFill()));
        assertEquals(color("FFFFFF"), SLD.color(poly.getStroke()));
        assertEquals(2, SLD.width(poly.getStroke()));
    }

    @Test
    public void testPolygonWithStyledLabel() throws Exception {
        //    <UserStyle>
        //      <Title>SLD Cook Book: Polygon with styled label</Title>
        //      <FeatureTypeStyle>
        //        <Rule>
        //          <PolygonSymbolizer>
        //            <Fill>
        //              <CssParameter name="fill">#40FF40</CssParameter>
        //            </Fill>
        //            <Stroke>
        //              <CssParameter name="stroke">#FFFFFF</CssParameter>
        //              <CssParameter name="stroke-width">2</CssParameter>
        //            </Stroke>
        //          </PolygonSymbolizer>
        //          <TextSymbolizer>
        //            <Label>
        //              <ogc:PropertyName>name</ogc:PropertyName>
        //            </Label>
        //            <Font>
        //              <CssParameter name="font-family">Arial</CssParameter>
        //              <CssParameter name="font-size">11</CssParameter>
        //              <CssParameter name="font-style">normal</CssParameter>
        //              <CssParameter name="font-weight">bold</CssParameter>
        //            </Font>
        //            <LabelPlacement>
        //              <PointPlacement>
        //                <AnchorPoint>
        //                  <AnchorPointX>0.5</AnchorPointX>
        //                  <AnchorPointY>0.5</AnchorPointY>
        //                </AnchorPoint>
        //              </PointPlacement>
        //            </LabelPlacement>
        //            <Fill>
        //              <CssParameter name="fill">#000000</CssParameter>
        //            </Fill>
        //            <VendorOption name="autoWrap">60</VendorOption>
        //            <VendorOption name="maxDisplacement">150</VendorOption>
        //          </TextSymbolizer>
        //        </Rule>
        //      </FeatureTypeStyle>
        //    </UserStyle>

        Style style = parse("poly", "styled-label.sld");

        PolygonSymbolizer poly = SLD.polySymbolizer(style);
        assertEquals(color("40FF40"), SLD.color(poly.getFill()));
        assertEquals(color("FFFFFF"), SLD.color(poly.getStroke()));
        assertEquals(2, SLD.width(poly.getStroke()));

        TextSymbolizer text = SLD.textSymbolizer(style);
        assertThat(SLD.textLabel(text), TestUtils.attribute("name"));
        assertEquals(Color.black, SLD.color(text.getFill()));

        PointPlacement place = (PointPlacement) text.getLabelPlacement();
        assertEquals(0.5, Filters.asDouble(place.getAnchorPoint().getAnchorPointX()), 0.1);
        assertEquals(0.5, Filters.asDouble(place.getAnchorPoint().getAnchorPointY()), 0.1);

        Font font = SLD.font(text);
        assertEquals("Arial", Filters.asString(font.getFontFamily()));
        assertEquals(11, Filters.asInt(font.getSize()));
        assertEquals("normal", Filters.asString(font.getStyle()));
        assertEquals("bold", Filters.asString(font.getWeight()));

        Map<String, String> options = text.getOptions();
        assertEquals("60", options.get("autoWrap"));
        assertEquals("150", options.get("maxDisplacement"));
    }

    @Test
    public void testPolygonWithTransparent() throws Exception {
        //    <UserStyle>
        //      <Title>SLD Cook Book: Transparent polygon</Title>
        //      <FeatureTypeStyle>
        //        <Rule>
        //          <PolygonSymbolizer>
        //            <Fill>
        //              <CssParameter name="fill">#000080</CssParameter>
        //              <CssParameter name="fill-opacity">0.5</CssParameter>
        //            </Fill>
        //            <Stroke>
        //              <CssParameter name="stroke">#FFFFFF</CssParameter>
        //              <CssParameter name="stroke-width">2</CssParameter>
        //            </Stroke>
        //          </PolygonSymbolizer>
        //        </Rule>
        //      </FeatureTypeStyle>
        //    </UserStyle>

        Style style = parse("poly", "transparent.sld");

        PolygonSymbolizer poly = SLD.polySymbolizer(style);
        assertEquals(color("000080"), SLD.color(poly.getFill()));
        assertEquals(0.5, SLD.opacity(poly.getFill()), 0.1);

        assertEquals(color("FFFFFF"), SLD.color(poly.getStroke()));
        assertEquals(2, SLD.width(poly.getStroke()));
    }

    @Test
    public void testPolygonWithZoom() throws Exception {
        //    <UserStyle>
        //      <Title>SLD Cook Book: Zoom-based polygon</Title>
        //      <FeatureTypeStyle>
        //        <Rule>
        //          <Name>Large</Name>
        //          <MaxScaleDenominator>100000000</MaxScaleDenominator>
        //          <PolygonSymbolizer>
        //            <Fill>
        //              <CssParameter name="fill">#0000CC</CssParameter>
        //            </Fill>
        //            <Stroke>
        //              <CssParameter name="stroke">#000000</CssParameter>
        //              <CssParameter name="stroke-width">7</CssParameter>
        //            </Stroke>
        //          </PolygonSymbolizer>
        //          <TextSymbolizer>
        //            <Label>
        //              <ogc:PropertyName>name</ogc:PropertyName>
        //            </Label>
        //            <Font>
        //              <CssParameter name="font-family">Arial</CssParameter>
        //              <CssParameter name="font-size">14</CssParameter>
        //              <CssParameter name="font-style">normal</CssParameter>
        //              <CssParameter name="font-weight">bold</CssParameter>
        //            </Font>
        //            <LabelPlacement>
        //              <PointPlacement>
        //                <AnchorPoint>
        //                  <AnchorPointX>0.5</AnchorPointX>
        //                  <AnchorPointY>0.5</AnchorPointY>
        //                </AnchorPoint>
        //              </PointPlacement>
        //            </LabelPlacement>
        //            <Fill>
        //              <CssParameter name="fill">#FFFFFF</CssParameter>
        //            </Fill>
        //          </TextSymbolizer>
        //        </Rule>
        //        <Rule>
        //          <Name>Medium</Name>
        //          <MinScaleDenominator>100000000</MinScaleDenominator>
        //          <MaxScaleDenominator>200000000</MaxScaleDenominator>
        //          <PolygonSymbolizer>
        //            <Fill>
        //              <CssParameter name="fill">#0000CC</CssParameter>
        //            </Fill>
        //            <Stroke>
        //              <CssParameter name="stroke">#000000</CssParameter>
        //              <CssParameter name="stroke-width">4</CssParameter>
        //            </Stroke>
        //          </PolygonSymbolizer>
        //        </Rule>
        //        <Rule>
        //          <Name>Small</Name>
        //          <MinScaleDenominator>200000000</MinScaleDenominator>
        //          <PolygonSymbolizer>
        //            <Fill>
        //              <CssParameter name="fill">#0000CC</CssParameter>
        //            </Fill>
        //            <Stroke>
        //              <CssParameter name="stroke">#000000</CssParameter>
        //              <CssParameter name="stroke-width">1</CssParameter>
        //            </Stroke>
        //          </PolygonSymbolizer>
        //        </Rule>
        //      </FeatureTypeStyle>
        //    </UserStyle>

        Style style = parse("poly", "zoom.sld");

        Rule rule = style.featureTypeStyles().get(0).rules().get(0);
        assertEquals("Large", rule.getName());
        assertEquals(100000000d, rule.getMaxScaleDenominator(), 0.1);

        PolygonSymbolizer poly = (PolygonSymbolizer) rule.symbolizers().get(0);
        assertEquals(color("0000CC"), SLD.color(poly.getFill()));
        assertEquals(color("000000"), SLD.color(poly.getStroke()));
        assertEquals(7, SLD.width(poly.getStroke()));

        rule = style.featureTypeStyles().get(0).rules().get(1);
        assertEquals("Medium", rule.getName());
        assertEquals(200000000d, rule.getMaxScaleDenominator(), 0.1);
        assertEquals(100000000d, rule.getMinScaleDenominator(), 0.1);

        poly = (PolygonSymbolizer) rule.symbolizers().get(0);
        assertEquals(color("0000CC"), SLD.color(poly.getFill()));
        assertEquals(color("000000"), SLD.color(poly.getStroke()));
        assertEquals(4, SLD.width(poly.getStroke()));

        rule = style.featureTypeStyles().get(0).rules().get(2);
        assertEquals("Small", rule.getName());
        assertEquals(200000000d, rule.getMinScaleDenominator(), 0.1);

        poly = (PolygonSymbolizer) rule.symbolizers().get(0);
        assertEquals(color("0000CC"), SLD.color(poly.getFill()));
        assertEquals(color("000000"), SLD.color(poly.getStroke()));
        assertEquals(1, SLD.width(poly.getStroke()));
    }

    @Test
    public void testRasterWithAlphaChannel() throws Exception {
        //    <UserStyle>
        //      <Title>SLD Cook Book: Alpha channel</Title>
        //      <FeatureTypeStyle>
        //        <Rule>
        //          <RasterSymbolizer>
        //            <ColorMap>
        //              <ColorMapEntry color="#008000" quantity="70" />
        //              <ColorMapEntry color="#008000" quantity="256" opacity="0"/>
        //            </ColorMap>
        //          </RasterSymbolizer>
        //        </Rule>
        //      </FeatureTypeStyle>
        //    </UserStyle>

        Style style = parse("raster", "alpha-channel.sld");
        RasterSymbolizer raster = SLD.rasterSymbolizer(style);

        ColorMapEntry e = raster.getColorMap().getColorMapEntry(0);
        assertEquals("#008000", Filters.asString(e.getColor()));
        assertEquals(70, Filters.asInt(e.getQuantity()));

        e = raster.getColorMap().getColorMapEntry(1);
        assertEquals("#008000", Filters.asString(e.getColor()));
        assertEquals(256, Filters.asInt(e.getQuantity()));
        assertEquals(0, Filters.asInt(e.getOpacity()));
    }

    @Test
    public void testRasterWithBrightnessAndContrast() throws Exception {
        //    <UserStyle>
        //      <Title>SLD Cook Book: Brightness and contrast</Title>
        //      <FeatureTypeStyle>
        //        <Rule>
        //          <RasterSymbolizer>
        //            <ContrastEnhancement>
        //              <Normalize />
        //              <GammaValue>0.5</GammaValue>
        //            </ContrastEnhancement>
        //            <ColorMap>
        //              <ColorMapEntry color="#008000" quantity="70" />
        //              <ColorMapEntry color="#663333" quantity="256" />
        //            </ColorMap>
        //          </RasterSymbolizer>
        //        </Rule>
        //      </FeatureTypeStyle>
        //    </UserStyle>

        Style style = parse("raster", "brightness-and-contrast.sld");

        RasterSymbolizer raster = SLD.rasterSymbolizer(style);

        ContrastEnhancement contrast = raster.getContrastEnhancement();
        assertEquals(0.5, Filters.asDouble(contrast.getGammaValue()), 0.1);
        assertEquals(ContrastMethod.NORMALIZE, contrast.getMethod());

        ColorMapEntry e = raster.getColorMap().getColorMapEntry(0);
        assertEquals("#008000", Filters.asString(e.getColor()));
        assertEquals(70, Filters.asInt(e.getQuantity()));

        e = raster.getColorMap().getColorMapEntry(1);
        assertEquals("#663333", Filters.asString(e.getColor()));
        assertEquals(256, Filters.asInt(e.getQuantity()));
    }

    @Test
    public void testRasterWithDiscreteColors() throws Exception {
        //    <UserStyle>
        //      <Title>SLD Cook Book: Discrete colors</Title>
        //      <FeatureTypeStyle>
        //        <Rule>
        //          <RasterSymbolizer>
        //            <ColorMap type="intervals">
        //              <ColorMapEntry color="#008000" quantity="150" />
        //              <ColorMapEntry color="#663333" quantity="256" />
        //            </ColorMap>
        //          </RasterSymbolizer>
        //        </Rule>
        //      </FeatureTypeStyle>
        //    </UserStyle>

        Style style = parse("raster", "discrete-colors.sld");

        RasterSymbolizer raster = SLD.rasterSymbolizer(style);

        ColorMapEntry e = raster.getColorMap().getColorMapEntry(0);
        assertEquals("#008000", Filters.asString(e.getColor()));
        assertEquals(150, Filters.asInt(e.getQuantity()));

        e = raster.getColorMap().getColorMapEntry(1);
        assertEquals("#663333", Filters.asString(e.getColor()));
        assertEquals(256, Filters.asInt(e.getQuantity()));
    }

    @Test
    public void testRasterWithManyColorGradient() throws Exception {
        //    <UserStyle>
        //      <Title>SLD Cook Book: Many color gradient</Title>
        //      <FeatureTypeStyle>
        //        <Rule>
        //          <RasterSymbolizer>
        //            <ColorMap>
        //              <ColorMapEntry color="#000000" quantity="95" />
        //              <ColorMapEntry color="#0000FF" quantity="110" />
        //              <ColorMapEntry color="#00FF00" quantity="135" />
        //              <ColorMapEntry color="#FF0000" quantity="160" />
        //              <ColorMapEntry color="#FF00FF" quantity="185" />
        //              <ColorMapEntry color="#FFFF00" quantity="210" />
        //              <ColorMapEntry color="#00FFFF" quantity="235" />
        //              <ColorMapEntry color="#FFFFFF" quantity="256" />
        //            </ColorMap>
        //          </RasterSymbolizer>
        //        </Rule>
        //      </FeatureTypeStyle>
        //    </UserStyle>

        Style style = parse("raster", "many-color-gradient.sld");

        RasterSymbolizer raster = SLD.rasterSymbolizer(style);

        ColorMapEntry e = raster.getColorMap().getColorMapEntry(0);
        assertEquals("#000000", Filters.asString(e.getColor()));
        assertEquals(95, Filters.asInt(e.getQuantity()));

        e = raster.getColorMap().getColorMapEntry(1);
        assertEquals("#0000FF", Filters.asString(e.getColor()));
        assertEquals(110, Filters.asInt(e.getQuantity()));

        e = raster.getColorMap().getColorMapEntry(2);
        assertEquals("#00FF00", Filters.asString(e.getColor()));
        assertEquals(135, Filters.asInt(e.getQuantity()));

        e = raster.getColorMap().getColorMapEntry(3);
        assertEquals("#FF0000", Filters.asString(e.getColor()));
        assertEquals(160, Filters.asInt(e.getQuantity()));

        e = raster.getColorMap().getColorMapEntry(4);
        assertEquals("#FF00FF", Filters.asString(e.getColor()));
        assertEquals(185, Filters.asInt(e.getQuantity()));

        e = raster.getColorMap().getColorMapEntry(5);
        assertEquals("#FFFF00", Filters.asString(e.getColor()));
        assertEquals(210, Filters.asInt(e.getQuantity()));

        e = raster.getColorMap().getColorMapEntry(6);
        assertEquals("#00FFFF", Filters.asString(e.getColor()));
        assertEquals(235, Filters.asInt(e.getQuantity()));

        e = raster.getColorMap().getColorMapEntry(7);
        assertEquals("#FFFFFF", Filters.asString(e.getColor()));
        assertEquals(256, Filters.asInt(e.getQuantity()));
    }

    @Test
    public void testRasterWithThreeColorGradient() throws Exception {
        //    <UserStyle>
        //      <Title>SLD Cook Book: Three color gradient</Title>
        //      <FeatureTypeStyle>
        //        <Rule>
        //          <RasterSymbolizer>
        //            <ColorMap>
        //              <ColorMapEntry color="#0000FF" quantity="150" />
        //              <ColorMapEntry color="#FFFF00" quantity="200" />
        //              <ColorMapEntry color="#FF0000" quantity="250" />
        //            </ColorMap>
        //          </RasterSymbolizer>
        //        </Rule>
        //      </FeatureTypeStyle>
        //    </UserStyle>

        Style style = parse("raster", "three-color-gradient.sld");

        RasterSymbolizer raster = SLD.rasterSymbolizer(style);

        ColorMapEntry e = raster.getColorMap().getColorMapEntry(0);
        assertEquals("#0000FF", Filters.asString(e.getColor()));
        assertEquals(150, Filters.asInt(e.getQuantity()));

        e = raster.getColorMap().getColorMapEntry(1);
        assertEquals("#FFFF00", Filters.asString(e.getColor()));
        assertEquals(200, Filters.asInt(e.getQuantity()));

        e = raster.getColorMap().getColorMapEntry(2);
        assertEquals("#FF0000", Filters.asString(e.getColor()));
        assertEquals(250, Filters.asInt(e.getQuantity()));
    }

    @Test
    public void testRasterWithTransparentGradient() throws Exception {
        //    <UserStyle>
        //      <Title>SLD Cook Book: Transparent gradient</Title>
        //      <FeatureTypeStyle>
        //        <Rule>
        //          <RasterSymbolizer>
        //            <Opacity>0.3</Opacity>
        //            <ColorMap>
        //              <ColorMapEntry color="#008000" quantity="70" />
        //              <ColorMapEntry color="#663333" quantity="256" />
        //            </ColorMap>
        //          </RasterSymbolizer>
        //        </Rule>
        //      </FeatureTypeStyle>
        //    </UserStyle>

        Style style = parse("raster", "transparent-gradient.sld");

        RasterSymbolizer raster = SLD.rasterSymbolizer(style);

        assertEquals(0.3, Filters.asDouble(raster.getOpacity()), 0.1);

        ColorMapEntry e = raster.getColorMap().getColorMapEntry(0);
        assertEquals("#008000", Filters.asString(e.getColor()));
        assertEquals(70, Filters.asInt(e.getQuantity()));

        e = raster.getColorMap().getColorMapEntry(1);
        assertEquals("#663333", Filters.asString(e.getColor()));
        assertEquals(256, Filters.asInt(e.getQuantity()));
    }

    @Test
    public void testRasterWithTwoColorGradient() throws Exception {
        //    <UserStyle>
        //      <Title>SLD Cook Book: Two color gradient</Title>
        //      <FeatureTypeStyle>
        //        <Rule>
        //          <RasterSymbolizer>
        //            <ColorMap>
        //              <ColorMapEntry color="#008000" quantity="70" />
        //              <ColorMapEntry color="#663333" quantity="256" />
        //            </ColorMap>
        //          </RasterSymbolizer>
        //        </Rule>
        //      </FeatureTypeStyle>
        //    </UserStyle>

        Style style = parse("raster", "two-color-gradient.sld");

        RasterSymbolizer raster = SLD.rasterSymbolizer(style);

        ColorMapEntry e = raster.getColorMap().getColorMapEntry(0);
        assertEquals("#008000", Filters.asString(e.getColor()));
        assertEquals(70, Filters.asInt(e.getQuantity()));

        e = raster.getColorMap().getColorMapEntry(1);
        assertEquals("#663333", Filters.asString(e.getColor()));
        assertEquals(256, Filters.asInt(e.getQuantity()));
    }

    Style parse(String dir, String file) throws IOException {
        StringWriter writer = new StringWriter();
        transform(YsldTests.sld(dir, file), writer);
        // System.out.println(writer.toString());
        YsldParser p = new YsldParser(new StringReader(writer.toString()));
        return SLD.defaultStyle(p.parse());
    }

    Color color(String hex) {
        return new Color(
                Integer.valueOf(hex.substring(0, 2), 16),
                Integer.valueOf(hex.substring(2, 4), 16),
                Integer.valueOf(hex.substring(4, 6), 16));
    }
}
