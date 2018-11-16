<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0"
  xsi:schemaLocation="http://www.opengis.net/sld http://schemas.opengis.net/sld/1.0.0/StyledLayerDescriptor.xsd"
  xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">

  <NamedLayer>
    <Name>Raster Symbolizer Test SLD</Name>
    <UserStyle>
      <Name>Raster SLD</Name>
      <FeatureTypeStyle>
        <Rule>
          <Name>Raster Symbolizer Test</Name>
          <RasterSymbolizer>
            <Opacity>0.75</Opacity>
            <ChannelSelection>
              <RedChannel>
                <SourceChannelName>1</SourceChannelName>
                <ContrastEnhancement><Histogram/></ContrastEnhancement>
              </RedChannel>
              <GreenChannel>
                <SourceChannelName>2</SourceChannelName>
                <ContrastEnhancement><GammaValue>
                    <ogc:Add>
                     <ogc:Literal>3.0</ogc:Literal>
                     <ogc:Literal>0.8</ogc:Literal>
                    </ogc:Add>
                </GammaValue></ContrastEnhancement>
              </GreenChannel>  
              <BlueChannel>
                <SourceChannelName>6</SourceChannelName>
                <ContrastEnhancement>
                <Normalize>
                    <Algorithm>ClipToMinimumMaximum</Algorithm>
                    <Parameter name='minValue'>1</Parameter>
                    <Parameter name='maxValue'>10</Parameter>
                </Normalize>
                </ContrastEnhancement>
              </BlueChannel>
            </ChannelSelection>
            
            <OverlapBehavior><LATEST_ON_TOP/></OverlapBehavior>
            <ContrastEnhancement>
              <GammaValue>
                <ogc:Add>
                 <ogc:Literal>1.0</ogc:Literal>
                 <ogc:Literal>0.5</ogc:Literal>
                </ogc:Add>
              </GammaValue>
            </ContrastEnhancement>
          </RasterSymbolizer>
        </Rule>
      </FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>
