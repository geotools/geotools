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
package org.geotools.ysld.transform.sld;

import static org.junit.Assert.assertEquals;

import java.io.StringWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import org.geotools.ysld.YamlMap;
import org.geotools.ysld.YamlUtil;
import org.geotools.ysld.Ysld;
import org.geotools.ysld.YsldTests;
import org.junit.Test;

public class SldTransformerTest {

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
        YamlMap style = transform("point", "simple.sld");
        assertEquals("SLD Cook Book: Simple Point With Stroke", style.str("title"));

        YamlMap rule = style.seq("feature-styles").map(0).seq("rules").map(0);

        YamlMap point = rule.seq("symbolizers").map(0).map("point");
        assertEquals(6, point.integer("size").intValue());

        YamlMap mark = point.seq("symbols").map(0).map("mark");
        assertEquals("circle", mark.str("shape"));
        assertEquals("#FF0000", mark.str("fill-color"));
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
        YamlMap style = transform("point", "stroke.sld");

        YamlMap mark =
                style.seq("feature-styles")
                        .map(0)
                        .seq("rules")
                        .map(0)
                        .seq("symbolizers")
                        .map(0)
                        .map("point")
                        .seq("symbols")
                        .map(0)
                        .map("mark");
        assertEquals("circle", mark.str("shape"));

        assertEquals("#FF0000", mark.str("fill-color"));
        assertEquals("#000000", mark.str("stroke-color"));
        assertEquals(2, mark.integer("stroke-width").intValue());
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
        YamlMap style = transform("point", "graphic.sld");

        YamlMap eg =
                style.seq("feature-styles")
                        .map(0)
                        .seq("rules")
                        .map(0)
                        .seq("symbolizers")
                        .map(0)
                        .map("point")
                        .seq("symbols")
                        .map(0)
                        .map("external");
        assertEquals("image/png", eg.str("format"));
        assertEquals("smileyface.png", eg.str("url"));
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
        YamlMap style = transform("point", "zoom.sld");

        YamlMap rule = style.seq("feature-styles").map(0).seq("rules").map(0);
        assertEquals("Large", rule.str("name"));
        assertEquals("(,160000000)", rule.str("scale"));

        rule = style.seq("feature-styles").map(0).seq("rules").map(1);
        assertEquals("Medium", rule.str("name"));
        assertEquals("(160000000,320000000)", rule.str("scale"));

        rule = style.seq("feature-styles").map(0).seq("rules").map(2);
        assertEquals("Small", rule.str("name"));
        assertEquals("(320000000,)", rule.str("scale"));
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
        YamlMap style = transform("point", "attribute.sld");

        YamlMap rule = style.seq("feature-styles").map(0).seq("rules").map(0);
        assertEquals("SmallPop", rule.str("name"));
        assertEquals("pop < '50000'", rule.str("filter"));

        rule = style.seq("feature-styles").map(0).seq("rules").map(1);
        assertEquals("MediumPop", rule.str("name"));
        assertEquals("pop >= '50000' AND pop < '100000'", rule.str("filter"));

        rule = style.seq("feature-styles").map(0).seq("rules").map(2);
        assertEquals("LargePop", rule.str("name"));
        assertEquals("pop >= '100000'", rule.str("filter"));
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
        YamlMap style = transform("point", "rotated-square.sld");

        YamlMap point =
                style.seq("feature-styles")
                        .map(0)
                        .seq("rules")
                        .map(0)
                        .seq("symbolizers")
                        .map(0)
                        .map("point");
        assertEquals(12, point.integer("size").intValue());
        assertEquals(45, point.integer("rotation").intValue());
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
        YamlMap style = transform("point", "transparent-triangle.sld");

        YamlMap mark =
                style.seq("feature-styles")
                        .map(0)
                        .seq("rules")
                        .map(0)
                        .seq("symbolizers")
                        .map(0)
                        .map("point")
                        .seq("symbols")
                        .map(0)
                        .map("mark");
        assertEquals("triangle", mark.str("shape"));
        assertEquals("#009900", mark.str("fill-color"));
        assertEquals(0.2, mark.doub("fill-opacity"), 0.1);
        assertEquals("#000000", mark.str("stroke-color"));
        assertEquals(2, mark.integer("stroke-width").intValue());
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
        YamlMap style = transform("point", "default-label.sld");

        YamlMap text =
                style.seq("feature-styles")
                        .map(0)
                        .seq("rules")
                        .map(0)
                        .seq("symbolizers")
                        .map(1)
                        .map("text");
        assertEquals("${name}", text.str("label"));
        assertEquals("#000000", text.str("fill-color"));
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
        YamlMap style = transform("point", "styled-label.sld");

        YamlMap text =
                style.seq("feature-styles")
                        .map(0)
                        .seq("rules")
                        .map(0)
                        .seq("symbolizers")
                        .map(1)
                        .map("text");

        assertEquals("${name}", text.str("label"));

        assertEquals("Arial", text.str("font-family"));
        assertEquals(12, text.integer("font-size").intValue());
        assertEquals("normal", text.str("font-style"));
        assertEquals("bold", text.str("font-weight"));

        assertEquals("point", text.map("placement").str("type"));
        assertEquals("(0.5,0.0)", text.map("placement").str("anchor"));
        assertEquals("(0,5)", text.map("placement").str("displacement"));

        assertEquals("#000000", text.str("fill-color"));
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
        YamlMap style = transform("point", "rotated-label.sld");
        YamlMap text =
                style.seq("feature-styles")
                        .map(0)
                        .seq("rules")
                        .map(0)
                        .seq("symbolizers")
                        .map(1)
                        .map("text");

        YamlMap pp = text.map("placement");
        assertEquals(-45, pp.integer("rotation").intValue());
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
        YamlMap style = transform("line", "simple.sld");
        YamlMap line =
                style.seq("feature-styles")
                        .map(0)
                        .seq("rules")
                        .map(0)
                        .seq("symbolizers")
                        .map(0)
                        .map("line");
        assertEquals("#000000", line.str("stroke-color"));
        assertEquals(3, line.integer("stroke-width").intValue());
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

        YamlMap style = transform("line", "attribute.sld");

        YamlMap rule = style.seq("feature-styles").map(0).seq("rules").map(0);
        assertEquals("local-road", rule.str("name"));
        assertEquals("type = 'local-road'", rule.str("filter"));

        YamlMap line = rule.seq("symbolizers").map(0).map("line");
        assertEquals("#009933", line.str("stroke-color"));
        assertEquals(2, line.integer("stroke-width").intValue());

        rule = style.seq("feature-styles").map(1).seq("rules").map(0);
        assertEquals("secondary", rule.str("name"));
        assertEquals("type = 'secondary'", rule.str("filter"));

        line = rule.seq("symbolizers").map(0).map("line");
        assertEquals("#0055CC", line.str("stroke-color"));
        assertEquals(3, line.integer("stroke-width").intValue());

        rule = style.seq("feature-styles").map(2).seq("rules").map(0);
        assertEquals("highway", rule.str("name"));
        assertEquals("type = 'highway'", rule.str("filter"));

        line = rule.seq("symbolizers").map(0).map("line");
        assertEquals("#FF0000", line.str("stroke-color"));
        assertEquals(6, line.integer("stroke-width").intValue());
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

        YamlMap style = transform("line", "border.sld");

        YamlMap rule = style.seq("feature-styles").map(0).seq("rules").map(0);
        YamlMap line = rule.seq("symbolizers").map(0).map("line");
        assertEquals("#333333", line.str("stroke-color"));
        assertEquals(5, line.integer("stroke-width").intValue());
        assertEquals("round", line.str("stroke-linecap"));

        rule = style.seq("feature-styles").map(1).seq("rules").map(0);
        line = rule.seq("symbolizers").map(0).map("line");
        assertEquals("#6699FF", line.str("stroke-color"));
        assertEquals(3, line.integer("stroke-width").intValue());
        assertEquals("round", line.str("stroke-linecap"));
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

        YamlMap style = transform("line", "curved-label.sld");
        YamlMap text =
                style.seq("feature-styles")
                        .map(0)
                        .seq("rules")
                        .map(0)
                        .seq("symbolizers")
                        .map(1)
                        .map("text");
        assertEquals("${name}", text.str("label"));
        assertEquals("#000000", text.str("fill-color"));
        assertEquals(true, text.bool(Ysld.OPTION_PREFIX + "followLine"));
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
        YamlMap style = transform("line", "dash-dot.sld");
        YamlMap rule = style.seq("feature-styles").map(0).seq("rules").map(0);

        YamlMap line = rule.seq("symbolizers").map(0).map("line");
        assertEquals("#0000FF", line.str("stroke-color"));
        assertEquals(1, line.integer("stroke-width").intValue());
        assertEquals("10 10", line.str("stroke-dasharray"));

        line = rule.seq("symbolizers").map(1).map("line");
        assertEquals("5 15", line.str("stroke-dasharray"));
        assertEquals(7.5, line.doub("stroke-dashoffset"), 0.1);

        YamlMap g = line.map("stroke-graphic-stroke");
        assertEquals(5, g.integer("size").intValue());
        assertEquals("circle", g.seq("symbols").map(0).map("mark").str("shape"));
        assertEquals("#000033", g.seq("symbols").map(0).map("mark").str("stroke-color"));
        assertEquals(1, g.seq("symbols").map(0).map("mark").integer("stroke-width").intValue());
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

        YamlMap style = transform("line", "dashed-line.sld");
        YamlMap line =
                style.seq("feature-styles")
                        .map(0)
                        .seq("rules")
                        .map(0)
                        .seq("symbolizers")
                        .map(0)
                        .map("line");
        assertEquals("#0000FF", line.str("stroke-color"));
        assertEquals(3, line.integer("stroke-width").intValue());
        assertEquals("5 2", line.str("stroke-dasharray"));
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

        YamlMap style = transform("line", "dash-space.sld");
        YamlMap line =
                style.seq("feature-styles")
                        .map(0)
                        .seq("rules")
                        .map(0)
                        .seq("symbolizers")
                        .map(0)
                        .map("line");

        assertEquals("4 6", line.str("stroke-dasharray"));
        YamlMap g = line.map("stroke-graphic-stroke");

        assertEquals(4, g.integer("size").intValue());
        assertEquals("circle", g.seq("symbols").map(0).map("mark").str("shape"));
        assertEquals("#666666", g.seq("symbols").map(0).map("mark").str("fill-color"));
        assertEquals("#333333", g.seq("symbols").map(0).map("mark").str("stroke-color"));
        assertEquals(1, g.seq("symbols").map(0).map("mark").integer("stroke-width").intValue());
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

        YamlMap style = transform("line", "default-label.sld");

        YamlMap line =
                style.seq("feature-styles")
                        .map(0)
                        .seq("rules")
                        .map(0)
                        .seq("symbolizers")
                        .map(0)
                        .map("line");
        assertEquals("#FF0000", line.str("stroke-color"));

        YamlMap text =
                style.seq("feature-styles")
                        .map(0)
                        .seq("rules")
                        .map(0)
                        .seq("symbolizers")
                        .map(1)
                        .map("text");
        assertEquals("${name}", text.str("label"));
        assertEquals("#000000", text.str("fill-color"));
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

        YamlMap style = transform("line", "railroad.sld");

        YamlMap line =
                style.seq("feature-styles")
                        .map(0)
                        .seq("rules")
                        .map(0)
                        .seq("symbolizers")
                        .map(1)
                        .map("line");
        YamlMap mark = line.map("stroke-graphic-stroke").seq("symbols").map(0).map("mark");
        assertEquals("shape://vertline", mark.str("shape"));
        assertEquals("#333333", mark.str("stroke-color"));
        assertEquals(1, mark.integer("stroke-width").intValue());
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

        YamlMap style = transform("line", "zoom.sld");

        YamlMap rule = style.seq("feature-styles").map(0).seq("rules").map(0);
        assertEquals("Large", rule.str("name"));
        assertEquals("(,180000000)", rule.str("scale"));

        YamlMap line = rule.seq("symbolizers").map(0).map("line");
        assertEquals("#009933", line.str("stroke-color"));
        assertEquals(6, line.integer("stroke-width").intValue());

        rule = style.seq("feature-styles").map(0).seq("rules").map(1);
        assertEquals("Medium", rule.str("name"));
        assertEquals("(180000000,360000000)", rule.str("scale"));

        line = rule.seq("symbolizers").map(0).map("line");
        assertEquals("#009933", line.str("stroke-color"));
        assertEquals(4, line.integer("stroke-width").intValue());

        rule = style.seq("feature-styles").map(0).seq("rules").map(2);
        assertEquals("Small", rule.str("name"));
        assertEquals("(360000000,)", rule.str("scale"));

        line = rule.seq("symbolizers").map(0).map("line");
        assertEquals("#009933", line.str("stroke-color"));
        assertEquals(2, line.integer("stroke-width").intValue());
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

        YamlMap style = transform("line", "optimized-label.sld");

        YamlMap text =
                style.seq("feature-styles")
                        .map(0)
                        .seq("rules")
                        .map(0)
                        .seq("symbolizers")
                        .map(1)
                        .map("text");
        assertEquals("${name}", text.str("label"));
        assertEquals("#000000", text.str("fill-color"));
        assertEquals(true, text.bool(Ysld.OPTION_PREFIX + "followLine"));
        assertEquals(90, text.integer(Ysld.OPTION_PREFIX + "maxAngleDelta").intValue());
        assertEquals(400, text.integer(Ysld.OPTION_PREFIX + "maxDisplacement").intValue());
        assertEquals(150, text.integer(Ysld.OPTION_PREFIX + "repeat").intValue());
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

        YamlMap style = transform("line", "optimized-styled-label.sld");

        YamlMap text =
                style.seq("feature-styles")
                        .map(0)
                        .seq("rules")
                        .map(0)
                        .seq("symbolizers")
                        .map(1)
                        .map("text");

        assertEquals("${name}", text.str("label"));
        assertEquals("#000000", text.str("fill-color"));

        assertEquals("Arial", text.str("font-family"));
        assertEquals(10, text.integer("font-size").intValue());
        assertEquals("normal", text.str("font-style"));
        assertEquals("bold", text.str("font-weight"));

        assertEquals(true, text.bool(Ysld.OPTION_PREFIX + "followLine"));
        assertEquals(90, text.integer(Ysld.OPTION_PREFIX + "maxAngleDelta").intValue());
        assertEquals(400, text.integer(Ysld.OPTION_PREFIX + "maxDisplacement").intValue());
        assertEquals(150, text.integer(Ysld.OPTION_PREFIX + "repeat").intValue());
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

        YamlMap obj = transform("poly", "simple.sld");
        YamlMap poly =
                obj.seq("feature-styles")
                        .map(0)
                        .seq("rules")
                        .map(0)
                        .seq("symbolizers")
                        .map(0)
                        .map("polygon");

        assertEquals("#000080", poly.str("fill-color"));
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

        YamlMap obj = transform("poly", "attribute.sld");

        YamlMap rule = obj.seq("feature-styles").map(0).seq("rules").map(0);
        assertEquals("SmallPop", rule.str("name"));
        assertEquals("pop < '200000'", rule.str("filter"));

        YamlMap poly = rule.seq("symbolizers").map(0).map("polygon");
        assertEquals("#66FF66", poly.str("fill-color"));

        rule = obj.seq("feature-styles").map(0).seq("rules").map(1);
        assertEquals("MediumPop", rule.str("name"));
        assertEquals("pop >= '200000' AND pop < '500000'", rule.str("filter"));

        poly = rule.seq("symbolizers").map(0).map("polygon");
        assertEquals("#33CC33", poly.str("fill-color"));

        rule = obj.seq("feature-styles").map(0).seq("rules").map(2);
        assertEquals("LargePop", rule.str("name"));
        assertEquals("pop > '500000'", rule.str("filter"));

        poly = rule.seq("symbolizers").map(0).map("polygon");
        assertEquals("#009900", poly.str("fill-color"));
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

        YamlMap obj = transform("poly", "default-label.sld");
        YamlMap poly =
                obj.seq("feature-styles")
                        .map(0)
                        .seq("rules")
                        .map(0)
                        .seq("symbolizers")
                        .map(0)
                        .map("polygon");
        YamlMap text =
                obj.seq("feature-styles")
                        .map(0)
                        .seq("rules")
                        .map(0)
                        .seq("symbolizers")
                        .map(1)
                        .map("text");

        assertEquals("#40FF40", poly.str("fill-color"));
        assertEquals("#FFFFFF", poly.str("stroke-color"));
        assertEquals(2, poly.integer("stroke-width").intValue());

        assertEquals("${name}", text.str("label"));
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

        YamlMap obj = transform("poly", "graphic-fill.sld");
        YamlMap poly =
                obj.seq("feature-styles")
                        .map(0)
                        .seq("rules")
                        .map(0)
                        .seq("symbolizers")
                        .map(0)
                        .map("polygon");

        YamlMap g = poly.map("fill-graphic").seq("symbols").map(0).map("external");
        assertEquals("colorblocks.png", g.str("url"));
        assertEquals("image/png", g.str("format"));
        assertEquals(93, poly.map("fill-graphic").integer("size").intValue());
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

        YamlMap obj = transform("poly", "halo-label.sld");
        YamlMap text =
                obj.seq("feature-styles")
                        .map(0)
                        .seq("rules")
                        .map(0)
                        .seq("symbolizers")
                        .map(1)
                        .map("text");
        assertEquals("${name}", text.str("label"));
        assertEquals(3, text.map("halo").integer("radius").intValue());
        assertEquals("#FFFFFF", text.map("halo").str("fill-color"));
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

        YamlMap obj = transform("poly", "hatch-fill.sld");
        YamlMap poly =
                obj.seq("feature-styles")
                        .map(0)
                        .seq("rules")
                        .map(0)
                        .seq("symbolizers")
                        .map(0)
                        .map("polygon");

        YamlMap mark = poly.map("fill-graphic").seq("symbols").map(0).map("mark");
        assertEquals("shape://times", mark.str("shape"));
        assertEquals("#990099", mark.str("stroke-color"));
        assertEquals(1, mark.integer("stroke-width").intValue());

        assertEquals(16, poly.map("fill-graphic").integer("size").intValue());
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

        YamlMap obj = transform("poly", "stroke.sld");
        YamlMap poly =
                obj.seq("feature-styles")
                        .map(0)
                        .seq("rules")
                        .map(0)
                        .seq("symbolizers")
                        .map(0)
                        .map("polygon");

        assertEquals("#000080", poly.str("fill-color"));
        assertEquals("#FFFFFF", poly.str("stroke-color"));
        assertEquals(2, poly.integer("stroke-width").intValue());
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

        YamlMap obj = transform("poly", "styled-label.sld");
        YamlMap text =
                obj.seq("feature-styles")
                        .map(0)
                        .seq("rules")
                        .map(0)
                        .seq("symbolizers")
                        .map(1)
                        .map("text");
        assertEquals("${name}", text.str("label"));

        assertEquals("Arial", text.str("font-family"));
        assertEquals(11, text.integer("font-size").intValue());
        assertEquals("normal", text.str("font-style"));
        assertEquals("bold", text.str("font-weight"));

        assertEquals("point", text.map("placement").str("type"));
        assertEquals("(0.5,0.5)", text.map("placement").str("anchor"));

        assertEquals("#000000", text.str("fill-color"));

        assertEquals(60, text.integer(Ysld.OPTION_PREFIX + "autoWrap").intValue());
        assertEquals(150, text.integer(Ysld.OPTION_PREFIX + "maxDisplacement").intValue());
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

        YamlMap obj = transform("poly", "transparent.sld");
        YamlMap poly =
                obj.seq("feature-styles")
                        .map(0)
                        .seq("rules")
                        .map(0)
                        .seq("symbolizers")
                        .map(0)
                        .map("polygon");

        assertEquals("#000080", poly.str("fill-color"));
        assertEquals(0.5, poly.doub("fill-opacity"), 0.1);

        assertEquals("#FFFFFF", poly.str("stroke-color"));
        assertEquals(2, poly.integer("stroke-width").intValue());
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

        YamlMap obj = transform("poly", "zoom.sld");

        YamlMap rule = obj.seq("feature-styles").map(0).seq("rules").map(0);
        assertEquals("Large", rule.str("name"));
        assertEquals("(,100000000)", rule.str("scale"));

        YamlMap poly = rule.seq("symbolizers").map(0).map("polygon");
        assertEquals("#0000CC", poly.str("fill-color"));
        assertEquals("#000000", poly.str("stroke-color"));
        assertEquals(7, poly.integer("stroke-width").intValue());

        YamlMap text = rule.seq("symbolizers").map(1).map("text");
        assertEquals("${name}", text.str("label"));
        assertEquals("Arial", text.str("font-family"));
        assertEquals(14, text.integer("font-size").intValue());
        assertEquals("normal", text.str("font-style"));
        assertEquals("bold", text.str("font-weight"));

        assertEquals("point", text.map("placement").str("type"));
        assertEquals("(0.5,0.5)", text.map("placement").str("anchor"));

        assertEquals("#FFFFFF", text.str("fill-color"));

        rule = obj.seq("feature-styles").map(0).seq("rules").map(1);
        assertEquals("Medium", rule.str("name"));
        assertEquals("(100000000,200000000)", rule.str("scale"));

        poly = rule.seq("symbolizers").map(0).map("polygon");
        assertEquals("#0000CC", poly.str("fill-color"));
        assertEquals("#000000", poly.str("stroke-color"));
        assertEquals(4, poly.integer("stroke-width").intValue());

        rule = obj.seq("feature-styles").map(0).seq("rules").map(2);
        assertEquals("Small", rule.str("name"));
        assertEquals("(200000000,)", rule.str("scale"));

        poly = rule.seq("symbolizers").map(0).map("polygon");
        poly = rule.seq("symbolizers").map(0).map("polygon");
        assertEquals("#0000CC", poly.str("fill-color"));
        assertEquals("#000000", poly.str("stroke-color"));
        assertEquals(1, poly.integer("stroke-width").intValue());
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

        YamlMap obj = transform("raster", "alpha-channel.sld");
        YamlMap raster =
                obj.seq("feature-styles")
                        .map(0)
                        .seq("rules")
                        .map(0)
                        .seq("symbolizers")
                        .map(0)
                        .map("raster");

        assertEquals("(#008000,,70,)", raster.map("color-map").seq("entries").str(0));
        assertEquals("(#008000,0,256,)", raster.map("color-map").seq("entries").str(1));
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

        YamlMap obj = transform("raster", "brightness-and-contrast.sld");
        YamlMap raster =
                obj.seq("feature-styles")
                        .map(0)
                        .seq("rules")
                        .map(0)
                        .seq("symbolizers")
                        .map(0)
                        .map("raster");

        assertEquals("normalize", raster.map("contrast-enhancement").str("mode"));
        assertEquals(0.5, raster.map("contrast-enhancement").doub("gamma"), 0.1);
        assertEquals("(#008000,,70,)", raster.map("color-map").seq("entries").str(0));
        assertEquals("(#663333,,256,)", raster.map("color-map").seq("entries").str(1));
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

        YamlMap obj = transform("raster", "discrete-colors.sld");
        YamlMap raster =
                obj.seq("feature-styles")
                        .map(0)
                        .seq("rules")
                        .map(0)
                        .seq("symbolizers")
                        .map(0)
                        .map("raster");

        assertEquals("intervals", raster.map("color-map").str("type"));
        assertEquals("(#008000,,150,)", raster.map("color-map").seq("entries").str(0));
        assertEquals("(#663333,,256,)", raster.map("color-map").seq("entries").str(1));
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

        YamlMap obj = transform("raster", "many-color-gradient.sld");
        YamlMap raster =
                obj.seq("feature-styles")
                        .map(0)
                        .seq("rules")
                        .map(0)
                        .seq("symbolizers")
                        .map(0)
                        .map("raster");

        assertEquals("(#000000,,95,)", raster.map("color-map").seq("entries").str(0));
        assertEquals("(#0000FF,,110,)", raster.map("color-map").seq("entries").str(1));
        assertEquals("(#00FF00,,135,)", raster.map("color-map").seq("entries").str(2));
        assertEquals("(#FF0000,,160,)", raster.map("color-map").seq("entries").str(3));
        assertEquals("(#FF00FF,,185,)", raster.map("color-map").seq("entries").str(4));
        assertEquals("(#FFFF00,,210,)", raster.map("color-map").seq("entries").str(5));
        assertEquals("(#00FFFF,,235,)", raster.map("color-map").seq("entries").str(6));
        assertEquals("(#FFFFFF,,256,)", raster.map("color-map").seq("entries").str(7));
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

        YamlMap obj = transform("raster", "three-color-gradient.sld");
        YamlMap raster =
                obj.seq("feature-styles")
                        .map(0)
                        .seq("rules")
                        .map(0)
                        .seq("symbolizers")
                        .map(0)
                        .map("raster");

        assertEquals("(#0000FF,,150,)", raster.map("color-map").seq("entries").str(0));
        assertEquals("(#FFFF00,,200,)", raster.map("color-map").seq("entries").str(1));
        assertEquals("(#FF0000,,250,)", raster.map("color-map").seq("entries").str(2));
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

        YamlMap obj = transform("raster", "transparent-gradient.sld");
        YamlMap raster =
                obj.seq("feature-styles")
                        .map(0)
                        .seq("rules")
                        .map(0)
                        .seq("symbolizers")
                        .map(0)
                        .map("raster");

        assertEquals(0.3, raster.doub("opacity"), 0.1);
        assertEquals("(#008000,,70,)", raster.map("color-map").seq("entries").str(0));
        assertEquals("(#663333,,256,)", raster.map("color-map").seq("entries").str(1));
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

        YamlMap obj = transform("raster", "two-color-gradient.sld");
        YamlMap raster =
                obj.seq("feature-styles")
                        .map(0)
                        .seq("rules")
                        .map(0)
                        .seq("symbolizers")
                        .map(0)
                        .map("raster");

        assertEquals("(#008000,,70,)", raster.map("color-map").seq("entries").str(0));
        assertEquals("(#663333,,256,)", raster.map("color-map").seq("entries").str(1));
    }

    SldTransformer transformer(String dirname, String filename) throws Exception {
        StringWriter yaml = new StringWriter();
        XMLInputFactory factory = XMLInputFactory.newFactory();
        XMLStreamReader xml = factory.createXMLStreamReader(YsldTests.sld(dirname, filename));
        return new SldTransformer(xml, yaml);
    }

    YamlMap yaml(SldTransformer transformer) throws Exception {
        String yaml = ((StringWriter) transformer.context().output()).toString();
        return new YamlMap(YamlUtil.getSafeYaml().load(yaml));
    }

    YamlMap transform(String dirname, String filename) throws Exception {
        SldTransformer tx = transformer(dirname, filename);
        tx.context().trace();
        try {
            tx.transform();
            return yaml(tx);
        } catch (Exception e) {
            ((TracingEmitter) tx.context().emitter()).dump(System.out);
            throw e;
        }
    }
}
