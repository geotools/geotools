<StyledLayerDescriptor version="0.7.2">
<!-- a named layer is the basic building block of an sld document -->
<NamedLayer>
<Name>A Simple Layer</Name>
<title>A Small Town with a river running though it</title>
<abstract>
This is for testing only, it has two 'buildings' and two 
roads.  The roads meet at a junction and a river runs 
under them.  There is a boundary of some sort round
one part of the map that includes one but not both of the
buildings.  One road and the river run through the zone.
</abstract>
<!-- with in a layer you have Named Styles -->
<UserStyle>
  <Name>Clasic</Name>
    <FeatureTypeStyle>
      <FeatureTypeName>forest</FeatureTypeName>
      <Rule>
        <PolygonSymbolizer>
          <Fill>
            <CssParameter name="fill">#00FF00</CssParameter>
          </Fill>
        </PolygonSymbolizer>
      </Rule>
    </FeatureTypeStyle> <!--
    <FeatureTypeStyle>
      <FeatureTypeName>lake</FeatureTypeName>
      <Rule>
        <PolygonSymbolizer>
          <Fill>
            <CssParameter name="fill">#0000FF</CssParameter>
          </Fill>
        </PolygonSymbolizer>
      </Rule>
    </FeatureTypeStyle>
    <FeatureTypeStyle>
      <FeatureTypeName>road</FeatureTypeName>
      <Rule>
        <LineSymbolizer>
          <Stroke>
            <CssParameter name="stroke">#000000</CssParameter>
            <CssParameter name="stroke-width">2</CssParameter>
          </Stroke>
        </LineSymbolizer>
      </Rule>
    </FeatureTypeStyle>
    <FeatureTypeStyle>
      <FeatureTypeName>dividedroute</FeatureTypeName>
      <Rule>
        <LineSymbolizer>
          <Stroke>
            <CssParameter name="stroke">#000000</CssParameter>
            <CssParameter name="stroke-width">2</CssParameter>
          </Stroke>
        </LineSymbolizer>
      </Rule>
    </FeatureTypeStyle> -->
</UserStyle>
</NamedLayer>
</StyledLayerDescriptor>