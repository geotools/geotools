

<StyledLayerDescriptor version="0.7.2">
<!-- a named layer is the basic building block of an sld document -->
<NamedLayer>
<Name>A Test Layer</Name>
<title>The title of the layer</title>
<abstract>
A styling layer used for the unit tests of sldstyler
</abstract>
<!-- with in a layer you have Named Styles -->
<UserStyle>
    <!-- again they have names, titles and abstracts -->
  <Name>MyStyle</Name>
    <!-- FeatureTypeStyles describe how to render different features -->
    <FeatureTypeStyle>
        <!-- this describes the featureTypeName to apply this style to e.g. road -->
      <FeatureTypeName>linefeature</FeatureTypeName>
       <!-- the actual rule describes the style -->
      <Rule>
        <!-- these are lines so we need a line symbolizer -->
        <LineSymbolizer>
           <!-- A stroke describes how the line looks -->
          <Stroke>
            <!-- the CssParameters describe the actual style 
                you can set stroke (color of line), stroke-width, stroke-opacity, stroke-linejoin
                stroke-linecap, stroke-dasharray and stroke-dashoffset -->
            <CssParameter name="stroke">#FF0000</CssParameter>
            <CssParameter name="stroke-width">10</CssParameter>
          </Stroke>
        </LineSymbolizer>
        <!-- multiple linesyombolizers are applied one after the other -->
        <LineSymbolizer>
          <Stroke>
            <CssParameter name="stroke">#0000FF</CssParameter>
            <CssParameter name="width">
                <Add>
                    <literal>1</literal>
                    <literal>2</literal>
                </Add>
            </CssParameter>
          </Stroke>
        </LineSymbolizer>
      </Rule>
    </FeatureTypeStyle>
    
    <FeatureTypeStyle>
        <!-- this describes the featureTypeName to apply this style to e.g. road -->
      <FeatureTypeName>linefeature2</FeatureTypeName>
       <!-- the actual rule describes the style -->
      <Rule>
        <!-- these are lines so we need a line symbolizer -->
        <LineSymbolizer>
           <!-- A stroke describes how the line looks -->
          <Stroke>
            <!-- the CssParameters describe the actual style 
                you can set stroke (color of line), stroke-width, stroke-opacity, stroke-linejoin
                stroke-linecap, stroke-dasharray and stroke-dashoffset -->
            <CssParameter name="stroke">#FF0000</CssParameter>
            <CssParameter name="stroke-width">10</CssParameter>
            <GraphicFill>
                <Graphic>
                    <size>10</size>
                    <mark>
                        <wellknownname>triangle</wellknownname>
                        <fill>
                            <CssParameter name="fill">#00FF00</CssParameter>
                        </fill>
                    </mark>
                </Graphic>
            </GraphicFill>    
          </Stroke>
        </LineSymbolizer>
        <LineSymbolizer>
          <Stroke>
            <CssParameter name="width">
                    <literal>1</literal>
            </CssParameter>
          </Stroke>
        </LineSymbolizer>
    </Rule>
    </FeatureTypeStyle>
    <FeatureTypeStyle>
        <FeatureTypeName>linefeature3</FeatureTypeName>
        <Rule>
        
        <LineSymbolizer>
          <geometry>
             <PropertyName>the_geom</PropertyName>
          </geometry>
           <!-- A stroke describes how the line looks -->
          <Stroke>
            <!-- the CssParameters describe the actual style 
                you can set stroke (color of line), stroke-width, stroke-opacity, stroke-linejoin
                stroke-linecap, stroke-dasharray and stroke-dashoffset -->
            <CssParameter name="stroke">#FF0000</CssParameter>
            <CssParameter name="stroke-width">10</CssParameter>
            <GraphicStroke>
                <Graphic>
                    <size>10</size>
                    <ExternalGraphic>
                        <onLineResource xmlns:xlink="http://www.w3.org/1999/xlink" xlink:type="simple" xlink:href="http://www.ccg.leeds.ac.uk/ian/geotools/icons/rail.gif"/>
                        <format>image/gif</format>
                    </ExternalGraphic>
                    <mark>
                        <wellknownname>arrow</wellknownname>
                        <fill>
                            <CssParameter name="fill">#0000FF</CssParameter>
                        </fill>
                    </mark>
                </Graphic>
            </GraphicStroke>    
          </Stroke>
        </LineSymbolizer>
        <LineSymbolizer>
          <Stroke>
            <CssParameter name="width">
                    <literal>1</literal>
            </CssParameter>
            <CssParameter name="stroke"><literal>#FF0000</literal></CssParameter>
          </Stroke>
        </LineSymbolizer>
    </Rule>
    </FeatureTypeStyle>
    <!-- a feature type for polygons -->
    <FeatureTypeStyle>
      <FeatureTypeName>polygon</FeatureTypeName>
      <Rule>
        <!-- like a linesymbolizer but with a fill too -->
        <PolygonSymbolizer>
            <Stroke>
                <CssParameter name="stroke-width">
                <Literal>3 </Literal>
                </CssParameter>
                <CssParameter name="stoke">#0000FF</CssParameter>
          </Stroke>
        </PolygonSymbolizer>
        <!-- again these are applied in order so the following "overdraws" the previous one 
            giving the dashed blue and yellow line -->
        <PolygonSymbolizer>
        <!-- describes the fill of the polygon - if missing the polygon is empty -->  
          <Fill>
            <!-- CssParameters allowed are fill (the color) and fill-opacity -->
            <CssParameter name="fill">#FF0000</CssParameter>
            <CssParameter name="fill-opacity">0.5</CssParameter>
            <GraphicFill>
                <Graphic>
                    <size>10</size>
                    <mark>
                        <wellknownname>circle</wellknownname>
                        <Fill>
                            <!-- CssParameters allowed are fill (the color) and fill-opacity -->
                            <CssParameter name="fill">#FFFF00</CssParameter>
                        </Fill>
                        <!-- stroke>
                            <CssParameter name="stroke-width">1</CssParameter>
                            <CssParameter name="stroke">#00FFFF</CssParameter>
                        </stroke -->
                    </mark>
                </Graphic>
            </GraphicFill>
          </Fill>
          <Stroke>
            <CssParameter name="stroke">#FFFF00</CssParameter>
            <CssParameter name="stroke-width">3</CssParameter>
            <CssParameter name="stroke-dasharray">1 2 </CssParameter>
          </Stroke>
        </PolygonSymbolizer>
        
      </Rule>
    </FeatureTypeStyle>
    <FeatureTypeStyle>
      <FeatureTypeName>polygontest2</FeatureTypeName>
      <Rule>
        
        <PolygonSymbolizer>
        
          <Fill>
            <GraphicFill>
                <graphic>
                    <size>20</size>
                    <externalGraphic>
                        <onLineResource xmlns:xlink="http://www.w3.org/1999/xlink" xlink:type="simple" xlink:href="http://www.ccg.leeds.ac.uk/ian/geotools/icons/brick1.gif"/>
                        <format>image/gif</format>
                    </externalGraphic>
                    
                    <mark>
                        <wellknownname>triangle</wellknownname>
                        <Fill>
                    
                            <CssParameter name="fill">#FF00FF</CssParameter>
                            <CssParameter name="fill-opacity">0.5</CssParameter>
                        </Fill>
                    </mark>
                </graphic>
            </GraphicFill>
            <!-- CssParameters allowed are fill (the color) and fill-opacity -->
            <CssParameter name="fill">#FF00FF</CssParameter>
            <CssParameter name="fill-opacity">0.5</CssParameter>
          </Fill>
          <Stroke>
            <!-- default stroke #0000000 -->
          </Stroke>
        </PolygonSymbolizer>
        
      </Rule>
    </FeatureTypeStyle>
    <FeatureTypeStyle>
      <FeatureTypeName>polygontest3</FeatureTypeName>
      <Rule>
        <PolygonSymbolizer>
        <!-- describes the fill of the polygon - if missing the polygon is empty -->  
          <Fill>
            <GraphicFill>
                <graphic>
                    <size>10</size>
                    <mark>
                        <wellknownname>triangle</wellknownname>
                        <Fill>
                    
                            <CssParameter name="fill">#FF00FF</CssParameter>
                            <CssParameter name="fill-opacity">0.5</CssParameter>
                        </Fill>
                    </mark>
                </graphic>
            </GraphicFill>
            <!-- CssParameters allowed are fill (the color) and fill-opacity -->
            <CssParameter name="fill">#FF00FF</CssParameter>
            <CssParameter name="fill-opacity">0.5</CssParameter>
          </Fill>
          <Stroke>
            <GraphicStroke>
                    <Graphic>
                        <size>8</size>
                        <mark>
                            <wellknownname>arrow</wellknownname>
                            <fill>
                                <CssParameter name="fill">#2020FF</CssParameter>
                            </fill>
                        </mark>
                    </Graphic>
                </GraphicStroke>    
          </Stroke>
        </PolygonSymbolizer>
        
      </Rule>
    </FeatureTypeStyle>
    <FeatureTypeStyle>
        <FeatureTypeName>pointfeature</FeatureTypeName>
        <rule>
            <PointSymbolizer>
                <graphic>
                    <size>10</size>
                    
                    <rotation>45.0</rotation>
                    <externalGraphic>
                        <onLineResource xmlns:xlink="http://www.w3.org/1999/xlink" xlink:type="simple" xlink:href="http://www.ccg.leeds.ac.uk/ian/geotools/icons/blob.gif"/>
                        <format>image/gif</format>
                    </externalGraphic>
                    <mark>
                    
                    <!-- since cross is not implemented yet should draw next mark -->
                        <wellknownname>triangle</wellknownname>
                        <Fill>
                            <!-- CssParameters allowed are fill (the color) and fill-opacity -->
                            <CssParameter name="fill">#FF00FF</CssParameter>
                            <CssParameter name="fill-opacity">0.5</CssParameter>
                        </Fill>
                    </mark>
                    <mark>
                        <wellknownname>square</wellknownname>
                        <Fill>
                            <!-- CssParameters allowed are fill (the color) and fill-opacity -->
                            <CssParameter name="fill">#00FF00</CssParameter>
                        </Fill>
                    </mark>
                </graphic>
            </PointSymbolizer>
            <TextSymbolizer>
                <Label><Literal>Point Label</Literal></Label>
                <Font>
                    <CssParameter name="font-family">Times</CssParameter>
                    <CssParameter name="font-Size">10</CssParameter>
                </Font>
                <Fill>
                    <CssParameter name="fill">#00FF00</CssParameter>
                </Fill>
                <Halo/>
            </TextSymbolizer>
                    
        </rule>
    </FeatureTypeStyle>
</UserStyle>
<UserStyle>
    <!-- again they have names, titles and abstracts -->
  <Name>MyStyle2</Name>
    <!-- FeatureTypeStyles describe how to render different features -->
    <FeatureTypeStyle>
        <!-- this describes the featureTypeName to apply this style to e.g. road -->
      <FeatureTypeName>linefeature</FeatureTypeName>
       <!-- the actual rule describes the style -->
      <Rule>
        <!-- these are lines so we need a line symbolizer -->
        <LineSymbolizer>
           <!-- A stroke describes how the line looks -->
          <Stroke>
            <!-- the CssParameters describe the actual style 
                you can set stroke (color of line), stroke-width, stroke-opacity, stroke-linejoin
                stroke-linecap, stroke-dasharray and stroke-dashoffset -->
            <CssParameter name="stroke">#00FF00</CssParameter>
            <CssParameter name="stroke-width">10</CssParameter>
          </Stroke>
        </LineSymbolizer>
        <!-- multiple linesyombolizers are applied one after the other -->
        <LineSymbolizer>
          <Stroke>
            <CssParameter name="stroke">#FF0000</CssParameter>
            <CssParameter name="width">
                <Add>
                    <literal>1</literal>
                    <literal>2</literal>
                </Add>
            </CssParameter>
          </Stroke>
        </LineSymbolizer>
      </Rule>
    </FeatureTypeStyle>
    
    <FeatureTypeStyle>
        <!-- this describes the featureTypeName to apply this style to e.g. road -->
      <FeatureTypeName>linefeature2</FeatureTypeName>
       <!-- the actual rule describes the style -->
      <Rule>
        <!-- these are lines so we need a line symbolizer -->
        <LineSymbolizer>
           <!-- A stroke describes how the line looks -->
          <Stroke>
            <!-- the CssParameters describe the actual style 
                you can set stroke (color of line), stroke-width, stroke-opacity, stroke-linejoin
                stroke-linecap, stroke-dasharray and stroke-dashoffset -->
            <CssParameter name="stroke">#00FF00</CssParameter>
            <CssParameter name="stroke-width">10</CssParameter>
            <GraphicFill>
                <Graphic>
                    <size>10</size>
                    <mark>
                        <wellknownname>triangle</wellknownname>
                        <fill>
                            <CssParameter name="fill">#0000FF</CssParameter>
                        </fill>
                    </mark>
                </Graphic>
            </GraphicFill>    
          </Stroke>
        </LineSymbolizer>
        <LineSymbolizer>
          <Stroke>
            <CssParameter name="width">
                    <literal>1</literal>
            </CssParameter>
          </Stroke>
        </LineSymbolizer>
    </Rule>
    </FeatureTypeStyle>
    <FeatureTypeStyle>
        <FeatureTypeName>linefeature3</FeatureTypeName>
        <Rule>
        
        <LineSymbolizer>
           <!-- A stroke describes how the line looks -->
          <Stroke>
            <!-- the CssParameters describe the actual style 
                you can set stroke (color of line), stroke-width, stroke-opacity, stroke-linejoin
                stroke-linecap, stroke-dasharray and stroke-dashoffset -->
            <CssParameter name="stroke">#00FF00</CssParameter>
            <CssParameter name="stroke-width">10</CssParameter>
            <GraphicStroke>
                <Graphic>
                    <size>10</size>
                    <ExternalGraphic>
                        <onLineResource xmlns:xlink="http://www.w3.org/1999/xlink" xlink:type="simple" xlink:href="http://www.ccg.leeds.ac.uk/ian/geotools/icons/rail.gif"/>
                        <format>image/gif</format>
                    </ExternalGraphic>
                    <mark>
                        <wellknownname>arrow</wellknownname>
                        <fill>
                            <CssParameter name="fill">#FF0000</CssParameter>
                        </fill>
                    </mark>
                </Graphic>
            </GraphicStroke>    
          </Stroke>
        </LineSymbolizer>
        <LineSymbolizer>
          <Stroke>
            <CssParameter name="width">
                    <literal>1</literal>
            </CssParameter>
            <CssParameter name="stroke"><literal>#00FF00</literal></CssParameter>
          </Stroke>
        </LineSymbolizer>
    </Rule>
    </FeatureTypeStyle>
    <!-- a feature type for polygons -->
    <FeatureTypeStyle>
      <FeatureTypeName>polygon</FeatureTypeName>
      <Rule>
        <!-- like a linesymbolizer but with a fill too -->
        <PolygonSymbolizer>
            <Stroke>
                <CssParameter name="stroke-width">
                <Literal>3 </Literal>
                </CssParameter>
                <CssParameter name="stoke">#FF0000</CssParameter>
          </Stroke>
        </PolygonSymbolizer>
        <!-- again these are applied in order so the following "overdraws" the previous one 
            giving the dashed blue and yellow line -->
        <PolygonSymbolizer>
        <!-- describes the fill of the polygon - if missing the polygon is empty -->  
          <Fill>
            <!-- CssParameters allowed are fill (the color) and fill-opacity -->
            <CssParameter name="fill">#00FF00</CssParameter>
            <CssParameter name="fill-opacity">0.5</CssParameter>
            <GraphicFill>
                <Graphic>
                    <size>10</size>
                    <mark>
                        <wellknownname>circle</wellknownname>
                        <Fill>
                            <!-- CssParameters allowed are fill (the color) and fill-opacity -->
                            <CssParameter name="fill">#00FFFF</CssParameter>
                        </Fill>
                        <!-- stroke>
                            <CssParameter name="stroke-width">1</CssParameter>
                            <CssParameter name="stroke">#00FFFF</CssParameter>
                        </stroke -->
                    </mark>
                </Graphic>
            </GraphicFill>
          </Fill>
          <Stroke>
            <CssParameter name="stroke">#00FFFF</CssParameter>
            <CssParameter name="stroke-width">3</CssParameter>
            <CssParameter name="stroke-dasharray">1 2 </CssParameter>
          </Stroke>
        </PolygonSymbolizer>
        
      </Rule>
    </FeatureTypeStyle>
    <FeatureTypeStyle>
      <FeatureTypeName>polygontest2</FeatureTypeName>
      <Rule>
        
        <PolygonSymbolizer>
        
          <Fill>
            <GraphicFill>
                <graphic>
                    <size>20</size>
                    <externalGraphic>
                        <onLineResource xmlns:xlink="http://www.w3.org/1999/xlink" xlink:type="simple" xlink:href="http://www.ccg.leeds.ac.uk/ian/geotools/icons/brick1.gif"/>
                        <format>image/gif</format>
                    </externalGraphic>
                    
                    <mark>
                        <wellknownname>triangle</wellknownname>
                        <Fill>
                    
                            <CssParameter name="fill">#FFFF00</CssParameter>
                            <CssParameter name="fill-opacity">0.5</CssParameter>
                        </Fill>
                    </mark>
                </graphic>
            </GraphicFill>
            <!-- CssParameters allowed are fill (the color) and fill-opacity -->
            <CssParameter name="fill">#FFFF00</CssParameter>
            <CssParameter name="fill-opacity">0.5</CssParameter>
          </Fill>
          <Stroke>
            <!-- default stroke #0000000 -->
          </Stroke>
        </PolygonSymbolizer>
        
      </Rule>
    </FeatureTypeStyle>
    <FeatureTypeStyle>
      <FeatureTypeName>polygontest3</FeatureTypeName>
      <Rule>
        <PolygonSymbolizer>
        <!-- describes the fill of the polygon - if missing the polygon is empty -->  
          <Fill>
            <GraphicFill>
                <graphic>
                    <size>10</size>
                    <mark>
                        <wellknownname>triangle</wellknownname>
                        <Fill>
                    
                            <CssParameter name="fill">#FFFF00</CssParameter>
                            <CssParameter name="fill-opacity">0.5</CssParameter>
                        </Fill>
                    </mark>
                </graphic>
            </GraphicFill>
            <!-- CssParameters allowed are fill (the color) and fill-opacity -->
            <CssParameter name="fill">#FFFF00</CssParameter>
            <CssParameter name="fill-opacity">0.5</CssParameter>
          </Fill>
          <Stroke>
            <GraphicStroke>
                    <Graphic>
                        <size>8</size>
                        <mark>
                            <wellknownname>arrow</wellknownname>
                            <fill>
                                <CssParameter name="fill">#FF2020</CssParameter>
                            </fill>
                        </mark>
                    </Graphic>
                </GraphicStroke>    
          </Stroke>
        </PolygonSymbolizer>
        
      </Rule>
    </FeatureTypeStyle>
    <FeatureTypeStyle>
        <FeatureTypeName>pointfeature</FeatureTypeName>
        <rule>
            <PointSymbolizer>
                <graphic>
                    <size>10</size>
                    
                    <rotation>45.0</rotation>
                    <externalGraphic>
                        <onLineResource xmlns:xlink="http://www.w3.org/1999/xlink" xlink:type="simple" xlink:href="http://www.ccg.leeds.ac.uk/ian/geotools/icons/blob.gif"/>
                        <format>image/gif</format>
                    </externalGraphic>
                    <mark>
                    
                    <!-- since cross is not implemented yet should draw next mark -->
                        <wellknownname>triangle</wellknownname>
                        <Fill>
                            <!-- CssParameters allowed are fill (the color) and fill-opacity -->
                            <CssParameter name="fill">#FFFF00</CssParameter>
                            <CssParameter name="fill-opacity">0.5</CssParameter>
                        </Fill>
                    </mark>
                    <mark>
                        <wellknownname>square</wellknownname>
                        <Fill>
                            <!-- CssParameters allowed are fill (the color) and fill-opacity -->
                            <CssParameter name="fill">#0000FF</CssParameter>
                        </Fill>
                    </mark>
                </graphic>
            </PointSymbolizer>
            <TextSymbolizer>
                <Label><Literal>Point Label</Literal></Label>
                <Font>
                    <CssParameter name="font-family">Times</CssParameter>
                    <CssParameter name="font-Size">10</CssParameter>
                </Font>
                <Fill>
                    <CssParameter name="fill">#0000FF</CssParameter>
                </Fill>
                <Halo/>
            </TextSymbolizer>
                    
        </rule>
    </FeatureTypeStyle>
</UserStyle>
</NamedLayer>
</StyledLayerDescriptor>

