<?xml version="1.0" encoding="UTF-8"?>
<sld:UserStyle xmlns:sld="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:gml="http://www.opengis.net/gml">
   <Name>LandSat</Name>
	<sld:Title>LandSat images of Benin</sld:Title>
   <sld:Abstract>Strechting Landsat information</sld:Abstract>
   <sld:FeatureTypeStyle>
   <sld:Rule>
 <RasterSymbolizer>
    <Opacity>1.0</Opacity>
    <ColorMap>
       <ColorMapEntry color="#000000" quantity="0"/>
       <ColorMapEntry color="#ff00bb" quantity="255"/>
    </ColorMap>
    <ChannelSelection>
       <RedChannel>
          <SourceChannelName> 
1 
</SourceChannelName>
          <ContrastEnhancement>
             <Histogram/>
          </ContrastEnhancement>
       </RedChannel>
       <GreenChannel>
          <SourceChannelName> 
2 
</SourceChannelName>
          <ContrastEnhancement>
             <GammaValue>2.5</GammaValue>
          </ContrastEnhancement>
       </GreenChannel>
       <BlueChannel>
          <SourceChannelName> 3 </SourceChannelName>
          <ContrastEnhancement>
             <Normalize/>
          </ContrastEnhancement>
       </BlueChannel>
    </ChannelSelection>
    <OverlapBehavior>
       <LATEST_ON_TOP/>
    </OverlapBehavior>
 </RasterSymbolizer>

  </sld:Rule>
</sld:FeatureTypeStyle>
</sld:UserStyle>
