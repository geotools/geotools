<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0"
                       xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd"
                       xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc"
                       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <NamedLayer>
    <Name></Name>
    <title></title>
    <abstract></abstract>
    <UserStyle>
      <Name>Label_Style</Name>
      <Title>urban_places_label</Title>
      <FeatureTypeStyle>
        <Name>Label_Style_1</Name>
        <Rule>
          <Name>Label</Name>
          <LineSymbolizer>
            <Stroke/>
          </LineSymbolizer>
          <TextSymbolizer>
            <Label>
              <ogc:PropertyName>name</ogc:PropertyName>
            </Label>
            <Font>
              <CssParameter name="font-family">Bitstream Vera Sans</CssParameter>
              <CssParameter name="font-weight">bold</CssParameter>
              <CssParameter name="font-size">48</CssParameter>
              <CssParameter name="font-color">#222222</CssParameter>
            </Font>
            <Fill>
              <CssParameter name="fill">#333333</CssParameter>
            </Fill>
            <VendorOption name="followLine">true</VendorOption>
          </TextSymbolizer>
        </Rule>


      </FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>
