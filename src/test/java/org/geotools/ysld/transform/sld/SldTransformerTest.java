package org.geotools.ysld.transform.sld;

import org.geotools.ysld.YamlObj;
import org.geotools.ysld.YsldTests;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

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
        YamlObj style = transform("point", "simple.sld");
        assertEquals("SLD Cook Book: Simple Point With Stroke", style.s("title"));

        YamlObj rule = style.o("feature-styles", 0).o("rules", 0);

        YamlObj point = rule.o("symbolizers", 0).o("point");
        assertEquals(6, point.i("size").intValue());

        YamlObj mark = point.o("symbols", 0).o("mark");
        assertEquals("circle", mark.s("shape"));
        assertEquals("#FF0000", mark.o("fill").s("color"));
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
        YamlObj style = transform("point", "stroke.sld");

        YamlObj mark =
            style.o("feature-styles", 0).o("rules", 0).o("symbolizers", 0).o("point").o("symbols", 0).o("mark");
        assertEquals("circle", mark.s("shape"));

        assertEquals("#FF0000", mark.o("fill").s("color"));
        assertEquals("#000000", mark.o("stroke").s("color"));
        assertEquals(2, mark.o("stroke").i("width").intValue());
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
        YamlObj style = transform("point", "graphic.sld");

        YamlObj eg =
            style.o("feature-styles", 0).o("rules", 0).o("symbolizers", 0).o("point").o("symbols", 0).o("external");
        assertEquals("image/png", eg.s("format"));
        assertEquals("smileyface.png", eg.s("url"));
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
        YamlObj style = transform("point", "zoom.sld");

        YamlObj rule = style.o("feature-styles", 0).o("rules", 0);
        assertEquals("Large", rule.s("name"));
        assertEquals("(,160000000)", rule.s("scale"));

        rule = style.o("feature-styles", 0).o("rules", 1);
        assertEquals("Medium", rule.s("name"));
        assertEquals("(160000000,320000000)", rule.s("scale"));

        rule = style.o("feature-styles", 0).o("rules", 2);
        assertEquals("Small", rule.s("name"));
        assertEquals("(320000000,)", rule.s("scale"));
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
        YamlObj style = transform("point", "attribute.sld");

        YamlObj rule = style.o("feature-styles", 0).o("rules", 0);
        assertEquals("SmallPop", rule.s("name"));
        assertEquals("pop < '50000'", rule.s("filter"));

        rule = style.o("feature-styles", 0).o("rules", 1);
        assertEquals("MediumPop", rule.s("name"));
        assertEquals("pop >= '50000' AND pop < '100000'", rule.s("filter"));

        rule = style.o("feature-styles", 0).o("rules", 2);
        assertEquals("LargePop", rule.s("name"));
        assertEquals("pop >= '100000'", rule.s("filter"));
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
        YamlObj style = transform("point", "rotated-square.sld");

        YamlObj point = style.o("feature-styles", 0).o("rules", 0).o("symbolizers",0).o("point");
        assertEquals(12, point.i("size").intValue());
        assertEquals(45, point.i("rotation").intValue());
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
        YamlObj style = transform("point", "transparent-triangle.sld");

        YamlObj mark =
            style.o("feature-styles", 0).o("rules", 0).o("symbolizers",0).o("point").o("symbols", 0).o("mark");
        assertEquals("triangle", mark.s("shape"));
        assertEquals("#009900", mark.o("fill").s("color"));
        assertEquals(0.2, mark.o("fill").d("opacity"), 0.1);
        assertEquals("#000000", mark.o("stroke").s("color"));
        assertEquals(2, mark.o("stroke").i("width").intValue());
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
        YamlObj style = transform("point", "default-label.sld");

        YamlObj text =
            style.o("feature-styles", 0).o("rules", 0).o("symbolizers",1).o("text");
        assertEquals("[name]", text.s("label"));
        assertEquals("#000000", text.o("fill").s("color"));
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
        YamlObj style = transform("point", "styled-label.sld");

        YamlObj text =
            style.o("feature-styles", 0).o("rules", 0).o("symbolizers",1).o("text");

        assertEquals("[name]", text.s("label"));

        assertEquals("Arial", text.o("font").s("family"));
        assertEquals(12, text.o("font").i("size").intValue());
        assertEquals("normal", text.o("font").s("style"));
        assertEquals("bold", text.o("font").s("weight"));

        assertEquals("point", text.o("placement").s("type"));
        assertEquals("(0.5,0.0)", text.o("placement").s("anchor"));
        assertEquals("(0,5)", text.o("placement").s("displacement"));

        assertEquals("#000000", text.o("fill").s("color"));
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
        YamlObj style = transform("point", "rotated-label.sld");
        YamlObj text =
            style.o("feature-styles", 0).o("rules", 0).o("symbolizers",1).o("text");

        YamlObj pp = text.o("placement");
        assertEquals(-45, pp.i("rotation").intValue());
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
        YamlObj style = transform("line", "simple.sld");
        YamlObj line =
            style.o("feature-styles", 0).o("rules", 0).o("symbolizers",0).o("line");
        assertEquals("#000000", line.o("stroke").s("color"));
        assertEquals(3, line.o("stroke").i("width").intValue());
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

        YamlObj style = transform("line", "attribute.sld");


        YamlObj rule = style.o("feature-styles", 0).o("rules", 0);
        assertEquals("local-road", rule.s("name"));
        assertEquals("type = 'local-road'", rule.s("filter"));

        YamlObj line = rule.o("symbolizers", 0).o("line");
        assertEquals("#009933", line.o("stroke").s("color"));
        assertEquals(2, line.o("stroke").i("width").intValue());

        rule = style.o("feature-styles", 1).o("rules", 0);
        assertEquals("secondary", rule.s("name"));
        assertEquals("type = 'secondary'", rule.s("filter"));

        line = rule.o("symbolizers", 0).o("line");
        assertEquals("#0055CC", line.o("stroke").s("color"));
        assertEquals(3, line.o("stroke").i("width").intValue());

        rule = style.o("feature-styles", 2).o("rules", 0);
        assertEquals("highway", rule.s("name"));
        assertEquals("type = 'highway'", rule.s("filter"));

        line = rule.o("symbolizers", 0).o("line");
        assertEquals("#FF0000", line.o("stroke").s("color"));
        assertEquals(6, line.o("stroke").i("width").intValue());
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

        YamlObj style = transform("line", "border.sld");

        YamlObj rule = style.o("feature-styles", 0).o("rules", 0);
        YamlObj line = rule.o("symbolizers", 0).o("line");
        assertEquals("#333333", line.o("stroke").s("color"));
        assertEquals(5, line.o("stroke").i("width").intValue());
        assertEquals("round", line.o("stroke").s("linecap"));

        rule = style.o("feature-styles", 1).o("rules", 0);
        line = rule.o("symbolizers", 0).o("line");
        assertEquals("#6699FF", line.o("stroke").s("color"));
        assertEquals(3, line.o("stroke").i("width").intValue());
        assertEquals("round", line.o("stroke").s("linecap"));
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

        YamlObj style = transform("line", "curved-label.sld");
        YamlObj text = style.o("feature-styles", 0).o("rules", 0).o("symbolizers",1).o("text");
        assertEquals("[name]", text.s("label"));
        assertEquals("#000000", text.o("fill").s("color"));
        assertEquals(true, text.o("options").b("follow-line"));
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
        YamlObj style = transform("line", "dash-dot.sld");
        YamlObj rule = style.o("feature-styles", 0).o("rules", 0);

        YamlObj line = rule.o("symbolizers", 0).o("line");
        assertEquals("#0000FF", line.o("stroke").s("color"));
        assertEquals(1, line.o("stroke").i("width").intValue());
        assertEquals("10 10", line.o("stroke").s("dasharray"));

        line = rule.o("symbolizers", 1).o("line");
        assertEquals("5 15", line.o("stroke").s("dasharray"));
        assertEquals(7.5, line.o("stroke").d("dashoffset"), 0.1);

        YamlObj g = line.o("stroke").o("graphic-stroke");
        assertEquals(5, g.i("size").intValue());
        assertEquals("circle", g.o("symbols", 0).o("mark").s("shape"));
        assertEquals("#000033", g.o("symbols", 0).o("mark").o("stroke").s("color"));
        assertEquals(1, g.o("symbols", 0).o("mark").o("stroke").i("width").intValue());
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

        YamlObj style = transform("line", "dashed-line.sld");
        YamlObj stroke = style.o("feature-styles", 0).o("rules", 0).o("symbolizers", 0).o("line").o("stroke");
        assertEquals("#0000FF", stroke.s("color"));
        assertEquals(3, stroke.i("width").intValue());
        assertEquals("5 2", stroke.s("dasharray"));
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

        YamlObj style = transform("line", "dash-space.sld");
        YamlObj stroke = style.o("feature-styles", 0).o("rules", 0).o("symbolizers", 0).o("line").o("stroke");

        assertEquals("4 6", stroke.s("dasharray"));
        YamlObj g = stroke.o("graphic-stroke");

        assertEquals(4, g.i("size").intValue());
        assertEquals("circle", g.o("symbols", 0).o("mark").s("shape"));
        assertEquals("#666666", g.o("symbols", 0).o("mark").o("fill").s("color"));
        assertEquals("#333333", g.o("symbols", 0).o("mark").o("stroke").s("color"));
        assertEquals(1, g.o("symbols", 0).o("mark").o("stroke").i("width").intValue());
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

        YamlObj style = transform("line", "default-label.sld");

        YamlObj line = style.o("feature-styles", 0).o("rules", 0).o("symbolizers", 0).o("line");
        assertEquals("#FF0000", line.o("stroke").s("color"));

        YamlObj text = style.o("feature-styles", 0).o("rules", 0).o("symbolizers", 1).o("text");
        assertEquals("[name]", text.s("label"));
        assertEquals("#000000", text.o("fill").s("color"));
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

        YamlObj style = transform("line", "railroad.sld");

        YamlObj line = style.o("feature-styles", 0).o("rules", 0).o("symbolizers", 1).o("line");
        YamlObj mark = line.o("stroke").o("graphic-stroke").o("symbols", 0).o("mark");
        assertEquals("shape://vertline", mark.s("shape"));
        assertEquals("#333333", mark.o("stroke").s("color"));
        assertEquals(1, mark.o("stroke").i("width").intValue());

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

        YamlObj style = transform("line", "zoom.sld");

        YamlObj rule = style.o("feature-styles", 0).o("rules", 0);
        assertEquals("Large", rule.s("name"));
        assertEquals("(,180000000)", rule.s("scale"));

        YamlObj line = rule.o("symbolizers", 0).o("line");
        assertEquals("#009933", line.o("stroke").s("color"));
        assertEquals(6, line.o("stroke").i("width").intValue());

        rule = style.o("feature-styles", 0).o("rules", 1);
        assertEquals("Medium", rule.s("name"));
        assertEquals("(180000000,360000000)", rule.s("scale"));

        line = rule.o("symbolizers", 0).o("line");
        assertEquals("#009933", line.o("stroke").s("color"));
        assertEquals(4, line.o("stroke").i("width").intValue());

        rule = style.o("feature-styles", 0).o("rules", 2);
        assertEquals("Small", rule.s("name"));
        assertEquals("(360000000,)", rule.s("scale"));

        line = rule.o("symbolizers", 0).o("line");
        assertEquals("#009933", line.o("stroke").s("color"));
        assertEquals(2, line.o("stroke").i("width").intValue());
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

        YamlObj style = transform("line", "optimized-label.sld");

        YamlObj text = style.o("feature-styles", 0).o("rules", 0).o("symbolizers",1).o("text");
        assertEquals("[name]", text.s("label"));
        assertEquals("#000000", text.o("fill").s("color"));
        assertEquals(true, text.o("options").b("follow-line"));
        assertEquals(90, text.o("options").i("max-angle-delta").intValue());
        assertEquals(400, text.o("options").i("max-displacement").intValue());
        assertEquals(150, text.o("options").i("repeat").intValue());

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

        YamlObj style = transform("line", "optimized-styled-label.sld");

        YamlObj text = style.o("feature-styles", 0).o("rules", 0).o("symbolizers",1).o("text");

        assertEquals("[name]", text.s("label"));
        assertEquals("#000000", text.o("fill").s("color"));

        assertEquals("Arial", text.o("font").s("family"));
        assertEquals(10, text.o("font").i("size").intValue());
        assertEquals("normal", text.o("font").s("style"));
        assertEquals("bold", text.o("font").s("weight"));

        assertEquals(true, text.o("options").b("follow-line"));
        assertEquals(90, text.o("options").i("max-angle-delta").intValue());
        assertEquals(400, text.o("options").i("max-displacement").intValue());
        assertEquals(150, text.o("options").i("repeat").intValue());
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

        YamlObj obj = transform("poly", "simple.sld");
        YamlObj poly = obj.o("feature-styles", 0).o("rules", 0).o("symbolizers",0).o("polygon");

        assertEquals("#000080", poly.o("fill").s("color"));
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

        YamlObj obj = transform("poly", "attribute.sld");

        YamlObj rule = obj.o("feature-styles", 0).o("rules", 0);
        assertEquals("SmallPop", rule.s("name"));
        assertEquals("pop < '200000'", rule.s("filter"));

        YamlObj poly = rule.o("symbolizers",0).o("polygon");
        assertEquals("#66FF66", poly.o("fill").s("color"));

        rule = obj.o("feature-styles", 0).o("rules", 1);
        assertEquals("MediumPop", rule.s("name"));
        assertEquals("pop >= '200000' AND pop < '500000'", rule.s("filter"));

        poly = rule.o("symbolizers",0).o("polygon");
        assertEquals("#33CC33", poly.o("fill").s("color"));

        rule = obj.o("feature-styles", 0).o("rules", 2);
        assertEquals("LargePop", rule.s("name"));
        assertEquals("pop > '500000'", rule.s("filter"));

        poly = rule.o("symbolizers",0).o("polygon");
        assertEquals("#009900", poly.o("fill").s("color"));

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

        YamlObj obj = transform("poly", "default-label.sld");
        YamlObj poly = obj.o("feature-styles", 0).o("rules", 0).o("symbolizers",0).o("polygon");
        YamlObj text = obj.o("feature-styles", 0).o("rules", 0).o("symbolizers",1).o("text");

        assertEquals("#40FF40", poly.o("fill").s("color"));
        assertEquals("#FFFFFF", poly.o("stroke").s("color"));
        assertEquals(2, poly.o("stroke").i("width").intValue());

        assertEquals("[name]", text.s("label"));
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

        YamlObj obj = transform("poly", "graphic-fill.sld");
        YamlObj poly = obj.o("feature-styles", 0).o("rules", 0).o("symbolizers",0).o("polygon");

        YamlObj g = poly.o("fill").o("graphic").o("symbols", 0).o("external");
        assertEquals("colorblocks.png", g.s("url"));
        assertEquals("image/png", g.s("format"));
        assertEquals(93, poly.o("fill").o("graphic").i("size").intValue());
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

        YamlObj obj = transform("poly", "halo-label.sld");
        YamlObj text = obj.o("feature-styles", 0).o("rules", 0).o("symbolizers",1).o("text");
        assertEquals("[name]", text.s("label"));
        assertEquals(3, text.o("halo").i("radius").intValue());
        assertEquals("#FFFFFF", text.o("halo").o("fill").s("color"));

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

        YamlObj obj = transform("poly", "hatch-fill.sld");
        YamlObj poly = obj.o("feature-styles", 0).o("rules", 0).o("symbolizers",0).o("polygon");

        YamlObj mark = poly.o("fill").o("graphic").o("symbols", 0).o("mark");
        assertEquals("shape://times", mark.s("shape"));
        assertEquals("#990099", mark.o("stroke").s("color"));
        assertEquals(1, mark.o("stroke").i("width").intValue());

        assertEquals(16, poly.o("fill").o("graphic").i("size").intValue());
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

        YamlObj obj = transform("poly", "stroke.sld");
        YamlObj poly = obj.o("feature-styles", 0).o("rules", 0).o("symbolizers",0).o("polygon");

        assertEquals("#000080", poly.o("fill").s("color"));
        assertEquals("#FFFFFF", poly.o("stroke").s("color"));
        assertEquals(2, poly.o("stroke").i("width").intValue());
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

        YamlObj obj = transform("poly", "styled-label.sld");
        YamlObj text = obj.o("feature-styles", 0).o("rules", 0).o("symbolizers",1).o("text");
        assertEquals("[name]", text.s("label"));

        assertEquals("Arial", text.o("font").s("family"));
        assertEquals(11, text.o("font").i("size").intValue());
        assertEquals("normal", text.o("font").s("style"));
        assertEquals("bold", text.o("font").s("weight"));

        assertEquals("point", text.o("placement").s("type"));
        assertEquals("(0.5,0.5)", text.o("placement").s("anchor"));

        assertEquals("#000000", text.o("fill").s("color"));

        assertEquals(60, text.o("options").i("auto-wrap").intValue());
        assertEquals(150, text.o("options").i("max-displacement").intValue());
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

        YamlObj obj = transform("poly", "transparent.sld");
        YamlObj poly = obj.o("feature-styles", 0).o("rules", 0).o("symbolizers",0).o("polygon");

        assertEquals("#000080", poly.o("fill").s("color"));
        assertEquals(0.5, poly.o("fill").d("opacity"), 0.1);

        assertEquals("#FFFFFF", poly.o("stroke").s("color"));
        assertEquals(2, poly.o("stroke").i("width").intValue());
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

        YamlObj obj = transform("poly", "zoom.sld");

        YamlObj rule = obj.o("feature-styles", 0).o("rules", 0);
        assertEquals("Large", rule.s("name"));
        assertEquals("(,100000000)", rule.s("scale"));

        YamlObj poly = rule.o("symbolizers",0).o("polygon");
        assertEquals("#0000CC", poly.o("fill").s("color"));
        assertEquals("#000000", poly.o("stroke").s("color"));
        assertEquals(7, poly.o("stroke").i("width").intValue());

        YamlObj text = rule.o("symbolizers",1).o("text");
        assertEquals("[name]", text.s("label"));
        assertEquals("Arial", text.o("font").s("family"));
        assertEquals(14, text.o("font").i("size").intValue());
        assertEquals("normal", text.o("font").s("style"));
        assertEquals("bold", text.o("font").s("weight"));

        assertEquals("point", text.o("placement").s("type"));
        assertEquals("(0.5,0.5)", text.o("placement").s("anchor"));

        assertEquals("#FFFFFF", text.o("fill").s("color"));

        rule = obj.o("feature-styles", 0).o("rules", 1);
        assertEquals("Medium", rule.s("name"));
        assertEquals("(100000000,200000000)", rule.s("scale"));

        poly = rule.o("symbolizers",0).o("polygon");
        assertEquals("#0000CC", poly.o("fill").s("color"));
        assertEquals("#000000", poly.o("stroke").s("color"));
        assertEquals(4, poly.o("stroke").i("width").intValue());

        rule = obj.o("feature-styles", 0).o("rules", 2);
        assertEquals("Small", rule.s("name"));
        assertEquals("(200000000,)", rule.s("scale"));

        poly = rule.o("symbolizers",0).o("polygon");
        poly = rule.o("symbolizers",0).o("polygon");
        assertEquals("#0000CC", poly.o("fill").s("color"));
        assertEquals("#000000", poly.o("stroke").s("color"));
        assertEquals(1, poly.o("stroke").i("width").intValue());
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

        YamlObj obj = transform("raster", "alpha-channel.sld");
        YamlObj raster = obj.o("feature-styles", 0).o("rules", 0).o("symbolizers",0).o("raster");

        assertEquals("(#008000,,70,)", raster.o("color-map").s("entries", 0));
        assertEquals("(#008000,0,256,)", raster.o("color-map").s("entries", 1));
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

        YamlObj obj = transform("raster", "brightness-and-contrast.sld");
        YamlObj raster = obj.o("feature-styles", 0).o("rules", 0).o("symbolizers",0).o("raster");

        assertEquals("normalize", raster.o("contrast-enhancement").s("mode"));
        assertEquals(0.5, raster.o("contrast-enhancement").d("gamma"), 0.1);
        assertEquals("(#008000,,70,)", raster.o("color-map").s("entries", 0));
        assertEquals("(#663333,,256,)", raster.o("color-map").s("entries", 1));
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

        YamlObj obj = transform("raster", "discrete-colors.sld");
        YamlObj raster = obj.o("feature-styles", 0).o("rules", 0).o("symbolizers",0).o("raster");

        assertEquals("intervals", raster.o("color-map").s("type"));
        assertEquals("(#008000,,150,)", raster.o("color-map").s("entries", 0));
        assertEquals("(#663333,,256,)", raster.o("color-map").s("entries", 1));
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

        YamlObj obj = transform("raster", "many-color-gradient.sld");
        YamlObj raster = obj.o("feature-styles", 0).o("rules", 0).o("symbolizers",0).o("raster");

        assertEquals("(#000000,,95,)", raster.o("color-map").s("entries", 0));
        assertEquals("(#0000FF,,110,)", raster.o("color-map").s("entries", 1));
        assertEquals("(#00FF00,,135,)", raster.o("color-map").s("entries", 2));
        assertEquals("(#FF0000,,160,)", raster.o("color-map").s("entries", 3));
        assertEquals("(#FF00FF,,185,)", raster.o("color-map").s("entries", 4));
        assertEquals("(#FFFF00,,210,)", raster.o("color-map").s("entries", 5));
        assertEquals("(#00FFFF,,235,)", raster.o("color-map").s("entries", 6));
        assertEquals("(#FFFFFF,,256,)", raster.o("color-map").s("entries", 7));

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

        YamlObj obj = transform("raster", "three-color-gradient.sld");
        YamlObj raster = obj.o("feature-styles", 0).o("rules", 0).o("symbolizers",0).o("raster");

        assertEquals("(#0000FF,,150,)", raster.o("color-map").s("entries", 0));
        assertEquals("(#FFFF00,,200,)", raster.o("color-map").s("entries", 1));
        assertEquals("(#FF0000,,250,)", raster.o("color-map").s("entries", 2));
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

        YamlObj obj = transform("raster", "transparent-gradient.sld");
        YamlObj raster = obj.o("feature-styles", 0).o("rules", 0).o("symbolizers",0).o("raster");

        assertEquals(0.3, raster.d("opacity"), 0.1);
        assertEquals("(#008000,,70,)", raster.o("color-map").s("entries", 0));
        assertEquals("(#663333,,256,)", raster.o("color-map").s("entries", 1));
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

        YamlObj obj = transform("raster", "two-color-gradient.sld");
        YamlObj raster = obj.o("feature-styles", 0).o("rules", 0).o("symbolizers",0).o("raster");

        assertEquals("(#008000,,70,)", raster.o("color-map").s("entries", 0));
        assertEquals("(#663333,,256,)", raster.o("color-map").s("entries", 1));
    }

    SldTransformer transformer(String dirname, String filename) throws Exception {
        StringWriter yaml = new StringWriter();
        XMLInputFactory factory = XMLInputFactory.newFactory();
        XMLStreamReader xml =
            factory.createXMLStreamReader(YsldTests.sld(dirname, filename));
        return new SldTransformer(xml, yaml);
    }

    YamlObj yaml(SldTransformer transformer) throws Exception {
        String yaml = ((StringWriter)transformer.context().output()).toString();
        return new YamlObj(new Yaml().load(yaml));
    }

    YamlObj transform(String dirname, String filename) throws Exception {
        SldTransformer tx = transformer(dirname, filename);
        tx.context().trace();
        try {
            tx.transform();
            return yaml(tx);
        }
        catch(Exception e) {
            ((TracingEmitter)tx.context().emitter()).dump(System.out);
            throw e;
        }
    }
}

