Changes for SensorML version 1.0:

2006-09-07:
(1) Switch from attributes to elements for various component properties
(2) Improved harmonization with GML: using gml:UomSymbol, gml:UnitDefinition
    not using gml:description because it brings nothing and forces to switch namespace just for it...
(3) Added Matrix deriving from DataArray + ref and local Frame in parameters.xsd
(4) Added quality components Accuracy, Tolerance and ConfidenceLevel in parameters.xsd
(5) Added constraints to data components to better support SPS type descriptions

2006-09-12:
(1) Removed Hard Typed versions of Location, Orientation, GeoLocation in positionData.xsd

2006-10-06
(1) Fixed bug in Time constraints -> harmonize with Numerical Constraints

2006-10-10:
(1) Finished harmonization with ISO 19404 aggregate data types Record and Array
    Should allow to merge with Simon's recordType.xsd
(2) Moved codespace as an element just like uom for consistency
(3) Renamed axisCode to referenceAxis for consistency with referenceFrame
(4) Refactored schemas -> split/merge recordType.xsd + parameters.xsd into:
    basicTypes.xsd (all xsd simple types decimalList, decimalPair...)
    simpleTypes.xsd (Boolean, Quantity, Count, Category, Text, ItemDefinition)
    aggregateTypes.xsd (Record, Vector, Array, Series, ConditionalValue)
    curveTypes.xsd (Curve, NormalizedCurve)
    positionTypes (Position, SquareMatrix, GeoLocationArea, Envelope)
    --> Should be able to eliminate recordType.xsd and SWE_basicTypes.xsd (need to confirm with Simon)
    
2006-10-10
(1) Added DataStreamDefinition in data.xsd using MultiplexEncoding


2006-11-01  Several undocumented changes happened here


2006-12-18 (SVN version was 1306)
(1) switched to gml 3.1.1
(2) Removed recordType.xsd.old
(3) Removed ItemDefinition from simpleTypes and corresponding schema simpleTypeDerivation.xsd
(4) Renamed "referenceAxis" attribute in data types to "axisID" and clarified the annotation


2007-01-18 
(1) basicTypes.xsd:
replace AssociationAttributes with gml:AssociationAttributeGroup
merge SWE_basicTypes in
use UomSymbol for uom "code"
(2) simpleTypes.xsd:
General revision from type-derivation -> content-model-composition style
[Rationale: type derivation is only necessary to support substitution groups, which were not present; 
presence of complex derivation-by-restriction patterns suggested over-generalization of parents] 
Note: net effect is no change in elements!
(3) aggregates.xsd -> xmlData.xsd
(4) aggregateTypes.xsd:
add XMLData to the AnyData group
(5) phenomenon.xsd:
change to include basicTypes.xsd instead of SWE_basicTypes.xsd
(6) swe.xsd:
add include phenomenon.xsd and temporalAggregates.xsd
(7) added some notes and questions to Alex


2007-01-19 - by Alex Robin
(1) basicTypes.xsd
    Answered Simon's question about similarities with gml:TimePositionType
(2) simpleTypes.xsd:
    finished cleaning up simpleTypes.xsd and responded to some of Simon's comments. 
    Removed hard typed components for quality and used generic components instead.
    Type of quality will be specified through the use of URIs like on other components.
    Made CodeSpace an element just like Uom (following Simon's suggestion and my own intention)
(3) aggregateTypes.xsd:
    Added ConditionalData for future use in SensorML
(4) Created a folder for version 1.0