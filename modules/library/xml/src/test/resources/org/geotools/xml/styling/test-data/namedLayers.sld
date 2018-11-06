<!-- 
	This document is for testing SLDParser's NamedLayer elements
	as used by a WMS GetMap request to replace LAYERS and STYLES
	HTTP GET method parameters
-->
<StyledLayerDescriptor version="1.0.0">
	<NamedLayer>
		<LayerFeatureConstraints>
			<FeatureTypeConstraint>
				<FeatureTypeName>SomeFeatureName</FeatureTypeName>
				<Filter>
					<PropertyIsEqualTo>
						<PropertyName>SomeProperty</PropertyName>
						<Literal>100</Literal>
					</PropertyIsEqualTo>
				</Filter>
			</FeatureTypeConstraint>
		</LayerFeatureConstraints>
		<Name>Rivers</Name>
		<NamedStyle>
			<Name>CenterLine</Name>
		</NamedStyle>
	</NamedLayer>
	<NamedLayer>
		<Name>Roads</Name>
		<NamedStyle>
			<Name>CenterLine</Name>
		</NamedStyle>
	</NamedLayer>
	<NamedLayer>
		<Name>Houses</Name>
		<NamedStyle>
			<Name>Outline</Name>
		</NamedStyle>
	</NamedLayer>
</StyledLayerDescriptor>