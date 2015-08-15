<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor>
   <Name>My Layer</Name>
   <Title>A layer by me</Title>
   <Abstract>this is a sample layer</Abstract>
   <UserLayer>
      <LayerFeatureConstraints>
         <FeatureTypeConstraint />
      </LayerFeatureConstraints>
      <UserStyle>
         <Name>My User Style</Name>
         <Title>A style by me</Title>
         <Abstract>this is a sample style</Abstract>
         <IsDefault>true</IsDefault>
         <FeatureTypeStyle>
            <Rule>
               <LineSymbolizer>
                  <Stroke>
                     <CssParameter name="stroke">
                        <Literal>#000044</Literal>
                     </CssParameter>
                     <CssParameter name="stroke-width">
                        <Literal>3</Literal>
                     </CssParameter>
                     <CssParameter name="stroke-offset">
                        <Literal>0</Literal>
                     </CssParameter>
                     <CssParameter name="stroke-dasharray">
                        <Literal>2.0 1.0</Literal>
                         <Mul>
                           <Literal>2.0</Literal>
                           <Literal>2.0</Literal>
                         </Mul>
                        <Literal>1.0</Literal>
                     </CssParameter>
                  </Stroke>
               </LineSymbolizer>
            </Rule>
         </FeatureTypeStyle>
      </UserStyle>
   </UserLayer>
</StyledLayerDescriptor>
