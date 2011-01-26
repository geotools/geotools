<!-- Beware, not really valid according to the OGC schema, we're testing some OWS5 extensions -->
<StyledLayerDescriptor version="1.0.0" 
  xmlns="http://http://www.opengis.net/sld"
  xmlns:ogc="http://http://www.opengis.net/ogc"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://www.opengis.net/sld
                      http://schemas.opengis.net/sld/1.0.0/StyledLayerDescriptor.xsd">
  <!-- a named layer is the basic building block of an sld document -->
  <NamedLayer>
    <Name>A Test Layer</Name>
    <title>The title of the layer</title>
    <abstract>A styling layer used for the unit tests of sldstyler</abstract>
    <!-- with in a layer you have Named Styles -->
    <UserStyle>
      <!-- again they have names, titles and abstracts -->
      <Name>MyStyle</Name>
      <!-- FeatureTypeStyles describe how to render different features -->
      <FeatureTypeStyle>
        <FeatureTypeName>feature</FeatureTypeName>
        <rule>
          <TextSymbolizer>
            <Label>
              <Literal>Point Label</Literal>
            </Label>
            <Snippet><ogc:PropertyName>propertyOne</ogc:PropertyName></Snippet>
            <FeatureDescription>This one is mixed<ogc:PropertyName>propertyTwo</ogc:PropertyName></FeatureDescription>
            <OtherText target="extrude">10</OtherText>
          </TextSymbolizer>
        </rule>
      </FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>
