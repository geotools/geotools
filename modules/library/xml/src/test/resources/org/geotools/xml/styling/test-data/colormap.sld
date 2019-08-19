<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor
xmlns="http://www.opengis.net/sld"
xmlns:ogc="http://www.opengis.net/ogc"
xmlns:xlink="http://www.w3.org/1999/xlink"
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xsi:schemaLocation="http://www.opengis.net/sld http://schemas.opengis.net/sld/1.0.0/StyledLayerDescriptor.xsd" version="1.0.0">
<UserLayer>
<Name>raster_layer</Name>
<LayerFeatureConstraints>
<FeatureTypeConstraint/>
</LayerFeatureConstraints>
<UserStyle>
<Name>dem</Name>
<Title>dem</Title>
<Abstract>dem</Abstract>
<FeatureTypeStyle>
<FeatureTypeName>Feature</FeatureTypeName>
<Rule>
<RasterSymbolizer>
<Opacity>1.0</Opacity>
<ColorMap extended="true" type="ramp">
<ColorMapEntry color="#000000" quantity="-32767.0" opacity="0"/>

<ColorMapEntry color="#040d4d" quantity="-3000"/>

<ColorMapEntry color="#030d52" quantity="-2600"/>

<ColorMapEntry color="#020d59" quantity="-2200"/>
<ColorMapEntry color="#020d66" quantity="-2000"/>

<ColorMapEntry color="#03107a" quantity="-1600"/>

<ColorMapEntry color="#051593" quantity="-1300"/>
<ColorMapEntry color="#041594" quantity="-1000"/>
<ColorMapEntry color="#061cac" quantity="-900"/>
<ColorMapEntry color="#0626bf" quantity="-800"/>
<ColorMapEntry color="#0534d4" quantity="-600"/>
<ColorMapEntry color="#0542e4" quantity="-450"/>
<ColorMapEntry color="#0450ef" quantity="-300"/>
<ColorMapEntry color="#0069ef" quantity="-250"/>
<ColorMapEntry color="#0074ef" quantity="-200"/>
<ColorMapEntry color="#2082be" quantity="-100"/>
<ColorMapEntry color="#56adbb" quantity="-40"/>
<ColorMapEntry color="#5aa5b3" quantity="-30"/>
<ColorMapEntry color="#3c9994" quantity="-25"/>
<ColorMapEntry color="#20afb6" quantity="-18"/>
<ColorMapEntry color="#05c4dc" quantity="-12"/>
<ColorMapEntry color="#6beaf7" quantity="-8"/>
<ColorMapEntry color="#a8eff0" quantity="-5"/>
<ColorMapEntry color="#98edf2" quantity="-2"/>


<ColorMapEntry color="#adeff0" quantity="-1"/>

<ColorMapEntry color="#ebedd0" quantity="0"/>
<ColorMapEntry color="#97ac83" quantity="30"/>
<ColorMapEntry color="#667755" quantity="105"/>
<ColorMapEntry color="#446622" quantity="300"/>
<ColorMapEntry color="#445533" quantity="400"/>
<ColorMapEntry color="#334433" quantity="600"/>
<ColorMapEntry color="#3e4433" quantity="800"/>
<ColorMapEntry color="#605237" quantity="1000"/>
<ColorMapEntry color="#555555" quantity="1200"/>
<ColorMapEntry color="#778899" quantity="1400"/>
<ColorMapEntry color="#bbbbcc" quantity="1600"/>
<ColorMapEntry color="#ffffff" quantity="2500"/>
<ColorMapEntry color="#ffffff" quantity="8000"/>
</ColorMap>

</RasterSymbolizer>
</Rule>
</FeatureTypeStyle>
</UserStyle>
</UserLayer>
</StyledLayerDescriptor>
