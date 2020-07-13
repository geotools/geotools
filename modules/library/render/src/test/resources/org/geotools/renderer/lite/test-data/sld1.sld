<?xml version="1.0" encoding="UTF-8"?><sld:StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:sld="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" version="1.0.0">
  <sld:NamedLayer>
    <sld:Name>sld1</sld:Name>
    <sld:UserStyle>
      <sld:Name>sld1</sld:Name>
      <sld:Title>sample sld</sld:Title>
      <sld:Abstract>Style</sld:Abstract>
      <sld:FeatureTypeStyle>
        <sld:Name>name</sld:Name>
        <sld:Rule>
          <sld:RasterSymbolizer>
            <sld:ChannelSelection>
              <sld:GrayChannel>
                <sld:SourceChannelName>1</sld:SourceChannelName>
                <sld:ContrastEnhancement>
                  <sld:GammaValue>1.0</sld:GammaValue>
                </sld:ContrastEnhancement>
              </sld:GrayChannel>
            </sld:ChannelSelection>
            <sld:ColorMap>
              <sld:ColorMapEntry color="#100000" opacity="1" quantity="0" label="0"/>
              <sld:ColorMapEntry color="#200000" opacity="1" quantity="0.1" label="0.1"/>
              <sld:ColorMapEntry color="#300000" opacity="1" quantity="0.2" label="0.2"/>
              <sld:ColorMapEntry color="#400000" opacity="1" quantity="0.3" label="0.3"/>
              <sld:ColorMapEntry color="#500000" opacity="1" quantity="0.4" label="0.4"/>
              <sld:ColorMapEntry color="#600000" opacity="1" quantity="0.5" label="0.5"/>
              <sld:ColorMapEntry color="#700000" opacity="1" quantity="0.6" label="0.6"/>
              <sld:ColorMapEntry color="#800000" opacity="1" quantity="0.7" label="0.7"/>
              <sld:ColorMapEntry color="#900000" opacity="1" quantity="0.8" label="0.8"/>
              <sld:ColorMapEntry color="#0A0000" opacity="1" quantity="0.9" label="0.9"/>
              <sld:ColorMapEntry color="#0B0000" opacity="1" quantity="1" label="1"/>
            </sld:ColorMap>
            <sld:ContrastEnhancement/>
          </sld:RasterSymbolizer>
        </sld:Rule>
      </sld:FeatureTypeStyle>
    </sld:UserStyle>
  </sld:NamedLayer>
</sld:StyledLayerDescriptor>
