/**
 */
package net.opengis.gml311;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.util.List;

import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;

import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Document Root</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getMixed <em>Mixed</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getXSISchemaLocation <em>XSI Schema Location</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getAssociation <em>Association</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getContinuousCoverage <em>Continuous Coverage</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getCoverage <em>Coverage</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getFeature <em>Feature</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getGML <em>GML</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getObject <em>Object</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getCoordinateOperation <em>Coordinate Operation</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getDefinition <em>Definition</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getCoordinateReferenceSystem <em>Coordinate Reference System</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getCRS <em>CRS</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getReferenceSystem <em>Reference System</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getCoordinateSystem <em>Coordinate System</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getCurve <em>Curve</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getGeometricPrimitive <em>Geometric Primitive</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getGeometry <em>Geometry</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getCurveSegment <em>Curve Segment</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getDatum <em>Datum</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getDiscreteCoverage <em>Discrete Coverage</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getFeatureCollection <em>Feature Collection</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getGeneralConversion <em>General Conversion</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getOperation <em>Operation</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getSingleOperation <em>Single Operation</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getGeneralDerivedCRS <em>General Derived CRS</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getGeneralOperationParameter <em>General Operation Parameter</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getGeneralParameterValue <em>General Parameter Value</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getGeneralTransformation <em>General Transformation</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getGeometricAggregate <em>Geometric Aggregate</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getGriddedSurface <em>Gridded Surface</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getParametricCurveSurface <em>Parametric Curve Surface</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getSurfacePatch <em>Surface Patch</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getImplicitGeometry <em>Implicit Geometry</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getMetaData <em>Meta Data</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getPositionalAccuracy <em>Positional Accuracy</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getReference <em>Reference</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getRing <em>Ring</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getSolid <em>Solid</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getStrictAssociation <em>Strict Association</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getStyle <em>Style</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getSurface <em>Surface</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getTimeComplex <em>Time Complex</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getTimeObject <em>Time Object</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getTimeGeometricPrimitive <em>Time Geometric Primitive</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getTimePrimitive <em>Time Primitive</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getTimeReferenceSystem <em>Time Reference System</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getTimeSlice <em>Time Slice</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getTimeTopologyPrimitive <em>Time Topology Primitive</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getTopology <em>Topology</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getTopoPrimitive <em>Topo Primitive</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getAbsoluteExternalPositionalAccuracy <em>Absolute External Positional Accuracy</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getAbstractGeneralOperationParameterRef <em>Abstract General Operation Parameter Ref</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getAffinePlacement <em>Affine Placement</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getAnchorPoint <em>Anchor Point</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getAngle <em>Angle</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getArc <em>Arc</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getArcString <em>Arc String</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getArcByBulge <em>Arc By Bulge</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getArcStringByBulge <em>Arc String By Bulge</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getArcByCenterPoint <em>Arc By Center Point</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getArray <em>Array</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getAxisAbbrev <em>Axis Abbrev</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getAxisDirection <em>Axis Direction</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getAxisID <em>Axis ID</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getBag <em>Bag</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getBaseCRS <em>Base CRS</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getBaseCurve <em>Base Curve</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getBaseSurface <em>Base Surface</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getBaseUnit <em>Base Unit</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getUnitDefinition <em>Unit Definition</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getBezier <em>Bezier</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getBSpline <em>BSpline</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#isBoolean <em>Boolean</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getBooleanList <em>Boolean List</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#isBooleanValue <em>Boolean Value</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getBoundedBy <em>Bounded By</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getBoundingBox <em>Bounding Box</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getBoundingPolygon <em>Bounding Polygon</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getCartesianCS <em>Cartesian CS</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getCartesianCSRef <em>Cartesian CS Ref</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getCatalogSymbol <em>Catalog Symbol</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getCategory <em>Category</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getCategoryExtent <em>Category Extent</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getCategoryList <em>Category List</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getCenterLineOf <em>Center Line Of</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getCenterOf <em>Center Of</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getCircle <em>Circle</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getCircleByCenterPoint <em>Circle By Center Point</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getClothoid <em>Clothoid</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getColumnIndex <em>Column Index</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getCompassPoint <em>Compass Point</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getCompositeCurve <em>Composite Curve</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getCompositeSolid <em>Composite Solid</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getCompositeSurface <em>Composite Surface</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getCompositeValue <em>Composite Value</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getCompoundCRS <em>Compound CRS</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getCompoundCRSRef <em>Compound CRS Ref</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getConcatenatedOperation <em>Concatenated Operation</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getConcatenatedOperationRef <em>Concatenated Operation Ref</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getCone <em>Cone</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getContainer <em>Container</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getConventionalUnit <em>Conventional Unit</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getConversion <em>Conversion</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getConversionRef <em>Conversion Ref</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getConversionToPreferredUnit <em>Conversion To Preferred Unit</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getCoord <em>Coord</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getCoordinateOperationID <em>Coordinate Operation ID</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getCoordinateOperationName <em>Coordinate Operation Name</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getName <em>Name</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getCoordinateOperationRef <em>Coordinate Operation Ref</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getCoordinateReferenceSystemRef <em>Coordinate Reference System Ref</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getCoordinates <em>Coordinates</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getCoordinateSystemAxis <em>Coordinate System Axis</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getCoordinateSystemAxisRef <em>Coordinate System Axis Ref</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getCoordinateSystemRef <em>Coordinate System Ref</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getCount <em>Count</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getCountExtent <em>Count Extent</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getCountList <em>Count List</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getCovariance <em>Covariance</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getCovarianceMatrix <em>Covariance Matrix</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getCoverageFunction <em>Coverage Function</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getCrsRef <em>Crs Ref</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getCsID <em>Cs ID</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getCsName <em>Cs Name</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getCubicSpline <em>Cubic Spline</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getCurve1 <em>Curve1</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getCurveArrayProperty <em>Curve Array Property</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getCurveMember <em>Curve Member</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getCurveMembers <em>Curve Members</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getCurveProperty <em>Curve Property</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getCylinder <em>Cylinder</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getCylindricalCS <em>Cylindrical CS</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getCylindricalCSRef <em>Cylindrical CS Ref</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getDataBlock <em>Data Block</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getDataSource <em>Data Source</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getDatumID <em>Datum ID</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getDatumName <em>Datum Name</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getDatumRef <em>Datum Ref</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getDecimalMinutes <em>Decimal Minutes</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getDefaultStyle <em>Default Style</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getDefinedByConversion <em>Defined By Conversion</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getDefinitionCollection <em>Definition Collection</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getDefinitionMember <em>Definition Member</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getDictionaryEntry <em>Dictionary Entry</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getDefinitionProxy <em>Definition Proxy</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getDefinitionRef <em>Definition Ref</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getDegrees <em>Degrees</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getDerivationUnitTerm <em>Derivation Unit Term</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getDerivedCRS <em>Derived CRS</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getDerivedCRSRef <em>Derived CRS Ref</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getDerivedCRSType <em>Derived CRS Type</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getDerivedUnit <em>Derived Unit</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getDescription <em>Description</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getDictionary <em>Dictionary</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getDirectedEdge <em>Directed Edge</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getDirectedFace <em>Directed Face</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getDirectedNode <em>Directed Node</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getDirectedObservation <em>Directed Observation</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getObservation <em>Observation</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getDirectedObservationAtDistance <em>Directed Observation At Distance</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getDirectedTopoSolid <em>Directed Topo Solid</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getDirection <em>Direction</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getDirectionVector <em>Direction Vector</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getDmsAngle <em>Dms Angle</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getDmsAngleValue <em>Dms Angle Value</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getDomainSet <em>Domain Set</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getDoubleOrNullTupleList <em>Double Or Null Tuple List</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getDuration <em>Duration</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getEdge <em>Edge</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getEdgeOf <em>Edge Of</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getEllipsoid <em>Ellipsoid</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getEllipsoidalCS <em>Ellipsoidal CS</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getEllipsoidalCSRef <em>Ellipsoidal CS Ref</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getEllipsoidID <em>Ellipsoid ID</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getEllipsoidName <em>Ellipsoid Name</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getEllipsoidRef <em>Ellipsoid Ref</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getEngineeringCRS <em>Engineering CRS</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getEngineeringCRSRef <em>Engineering CRS Ref</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getEngineeringDatum <em>Engineering Datum</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getEngineeringDatumRef <em>Engineering Datum Ref</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getEnvelope <em>Envelope</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getEnvelopeWithTimePeriod <em>Envelope With Time Period</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getExtentOf <em>Extent Of</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getExterior <em>Exterior</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getFace <em>Face</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getFeatureCollection1 <em>Feature Collection1</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getFeatureMember <em>Feature Member</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getFeatureMembers <em>Feature Members</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getFeatureProperty <em>Feature Property</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getFeatureStyle <em>Feature Style</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getFeatureStyle1 <em>Feature Style1</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getFile <em>File</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getGeneralConversionRef <em>General Conversion Ref</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getGeneralTransformationRef <em>General Transformation Ref</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getGenericMetaData <em>Generic Meta Data</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getGeocentricCRS <em>Geocentric CRS</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getGeocentricCRSRef <em>Geocentric CRS Ref</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getGeodesic <em>Geodesic</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getGeodesicString <em>Geodesic String</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getGeodeticDatum <em>Geodetic Datum</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getGeodeticDatumRef <em>Geodetic Datum Ref</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getGeographicCRS <em>Geographic CRS</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getGeographicCRSRef <em>Geographic CRS Ref</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getGeometricComplex <em>Geometric Complex</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getGeometryMember <em>Geometry Member</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getGeometryMembers <em>Geometry Members</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getGeometryStyle <em>Geometry Style</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getGeometryStyle1 <em>Geometry Style1</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getGraphStyle <em>Graph Style</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getGraphStyle1 <em>Graph Style1</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getGreenwichLongitude <em>Greenwich Longitude</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getGrid <em>Grid</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getGridCoverage <em>Grid Coverage</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getGridDomain <em>Grid Domain</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getGridFunction <em>Grid Function</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getGroupID <em>Group ID</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getGroupName <em>Group Name</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getHistory <em>History</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getImageCRS <em>Image CRS</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getImageCRSRef <em>Image CRS Ref</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getImageDatum <em>Image Datum</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getImageDatumRef <em>Image Datum Ref</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getIncludesCRS <em>Includes CRS</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getIncludesElement <em>Includes Element</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getIncludesParameter <em>Includes Parameter</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getIncludesValue <em>Includes Value</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getIndexMap <em>Index Map</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getIndirectEntry <em>Indirect Entry</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getInnerBoundaryIs <em>Inner Boundary Is</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getInterior <em>Interior</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getIntegerValue <em>Integer Value</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getIntegerValueList <em>Integer Value List</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getInverseFlattening <em>Inverse Flattening</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getIsolated <em>Isolated</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getIsSphere <em>Is Sphere</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getLabelStyle <em>Label Style</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getLabelStyle1 <em>Label Style1</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getLinearCS <em>Linear CS</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getLinearCSRef <em>Linear CS Ref</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getLinearRing <em>Linear Ring</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getLineString <em>Line String</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getLineStringMember <em>Line String Member</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getLineStringProperty <em>Line String Property</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getLineStringSegment <em>Line String Segment</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getLocation <em>Location</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getLocationKeyWord <em>Location Key Word</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getLocationString <em>Location String</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getMappingRule <em>Mapping Rule</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getMaximalComplex <em>Maximal Complex</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getMaximumOccurs <em>Maximum Occurs</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getMeasure <em>Measure</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getMeasureDescription <em>Measure Description</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getMember <em>Member</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getMembers <em>Members</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getMeridianID <em>Meridian ID</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getMeridianName <em>Meridian Name</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getMetaDataProperty <em>Meta Data Property</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getMethodFormula <em>Method Formula</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getMethodID <em>Method ID</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getMethodName <em>Method Name</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getMinimumOccurs <em>Minimum Occurs</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getMinutes <em>Minutes</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getModifiedCoordinate <em>Modified Coordinate</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getMovingObjectStatus <em>Moving Object Status</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getMultiCenterLineOf <em>Multi Center Line Of</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getMultiCenterOf <em>Multi Center Of</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getMultiCoverage <em>Multi Coverage</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getMultiCurve <em>Multi Curve</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getMultiCurveCoverage <em>Multi Curve Coverage</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getMultiCurveDomain <em>Multi Curve Domain</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getMultiCurveProperty <em>Multi Curve Property</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getMultiEdgeOf <em>Multi Edge Of</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getMultiExtentOf <em>Multi Extent Of</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getMultiGeometry <em>Multi Geometry</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getMultiGeometryProperty <em>Multi Geometry Property</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getMultiLineString <em>Multi Line String</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getMultiLocation <em>Multi Location</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getMultiPoint <em>Multi Point</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getMultiPointCoverage <em>Multi Point Coverage</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getMultiPointDomain <em>Multi Point Domain</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getMultiPointProperty <em>Multi Point Property</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getMultiPolygon <em>Multi Polygon</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getMultiPosition <em>Multi Position</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getMultiSolid <em>Multi Solid</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getMultiSolidCoverage <em>Multi Solid Coverage</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getMultiSolidDomain <em>Multi Solid Domain</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getMultiSolidProperty <em>Multi Solid Property</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getMultiSurface <em>Multi Surface</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getMultiSurfaceCoverage <em>Multi Surface Coverage</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getMultiSurfaceDomain <em>Multi Surface Domain</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getMultiSurfaceProperty <em>Multi Surface Property</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getNode <em>Node</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getNull <em>Null</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getObliqueCartesianCS <em>Oblique Cartesian CS</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getObliqueCartesianCSRef <em>Oblique Cartesian CS Ref</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getOffsetCurve <em>Offset Curve</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getOperationMethod <em>Operation Method</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getOperationMethodRef <em>Operation Method Ref</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getOperationParameter <em>Operation Parameter</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getOperationParameterGroup <em>Operation Parameter Group</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getOperationParameterGroupRef <em>Operation Parameter Group Ref</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getOperationParameterRef <em>Operation Parameter Ref</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getOperationRef <em>Operation Ref</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getOperationVersion <em>Operation Version</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getOrientableCurve <em>Orientable Curve</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getOrientableSurface <em>Orientable Surface</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getOrigin <em>Origin</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getOuterBoundaryIs <em>Outer Boundary Is</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getParameterID <em>Parameter ID</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getParameterName <em>Parameter Name</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getParameterValue <em>Parameter Value</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getParameterValueGroup <em>Parameter Value Group</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getPassThroughOperation <em>Pass Through Operation</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getPassThroughOperationRef <em>Pass Through Operation Ref</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getPatches <em>Patches</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getPixelInCell <em>Pixel In Cell</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getPoint <em>Point</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getPointArrayProperty <em>Point Array Property</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getPointMember <em>Point Member</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getPointMembers <em>Point Members</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getPointProperty <em>Point Property</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getPointRep <em>Point Rep</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getPolarCS <em>Polar CS</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getPolarCSRef <em>Polar CS Ref</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getPolygon <em>Polygon</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getPolygonMember <em>Polygon Member</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getPolygonPatch <em>Polygon Patch</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getPolygonPatches <em>Polygon Patches</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getPolygonProperty <em>Polygon Property</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getPolyhedralSurface <em>Polyhedral Surface</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getSurface1 <em>Surface1</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getPos <em>Pos</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getPosition <em>Position</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getPosList <em>Pos List</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getPrimeMeridian <em>Prime Meridian</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getPrimeMeridianRef <em>Prime Meridian Ref</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getPriorityLocation <em>Priority Location</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getProjectedCRS <em>Projected CRS</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getProjectedCRSRef <em>Projected CRS Ref</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getQuantity <em>Quantity</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getQuantityExtent <em>Quantity Extent</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getQuantityList <em>Quantity List</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getQuantityType <em>Quantity Type</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getRangeParameters <em>Range Parameters</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getRangeSet <em>Range Set</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getRealizationEpoch <em>Realization Epoch</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getRectangle <em>Rectangle</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getRectifiedGrid <em>Rectified Grid</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getRectifiedGridCoverage <em>Rectified Grid Coverage</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getRectifiedGridDomain <em>Rectified Grid Domain</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getReferenceSystemRef <em>Reference System Ref</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getRelativeInternalPositionalAccuracy <em>Relative Internal Positional Accuracy</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getRemarks <em>Remarks</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getResult <em>Result</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getResultOf <em>Result Of</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getRing1 <em>Ring1</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getRoughConversionToPreferredUnit <em>Rough Conversion To Preferred Unit</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getRowIndex <em>Row Index</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getScope <em>Scope</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getSecondDefiningParameter <em>Second Defining Parameter</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getSeconds <em>Seconds</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getSegments <em>Segments</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getSemiMajorAxis <em>Semi Major Axis</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getSemiMinorAxis <em>Semi Minor Axis</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getSingleOperationRef <em>Single Operation Ref</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getSolid1 <em>Solid1</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getSolidArrayProperty <em>Solid Array Property</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getSolidMember <em>Solid Member</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getSolidMembers <em>Solid Members</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getSolidProperty <em>Solid Property</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getSourceCRS <em>Source CRS</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getSourceDimensions <em>Source Dimensions</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getSphere <em>Sphere</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getSphericalCS <em>Spherical CS</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getSphericalCSRef <em>Spherical CS Ref</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getSrsID <em>Srs ID</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getSrsName <em>Srs Name</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getStatus <em>Status</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getStringValue <em>String Value</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getStyle1 <em>Style1</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getSubComplex <em>Sub Complex</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getSubject <em>Subject</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getTarget <em>Target</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getSuperComplex <em>Super Complex</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getSurfaceArrayProperty <em>Surface Array Property</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getSurfaceMember <em>Surface Member</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getSurfaceMembers <em>Surface Members</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getSurfaceProperty <em>Surface Property</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getSymbol <em>Symbol</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getTargetCRS <em>Target CRS</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getTargetDimensions <em>Target Dimensions</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getTemporalCRS <em>Temporal CRS</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getTemporalCRSRef <em>Temporal CRS Ref</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getTemporalCS <em>Temporal CS</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getTemporalCSRef <em>Temporal CS Ref</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getTemporalDatum <em>Temporal Datum</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getTemporalDatumRef <em>Temporal Datum Ref</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getTemporalExtent <em>Temporal Extent</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getTimeCalendar <em>Time Calendar</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getTimeCalendarEra <em>Time Calendar Era</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getTimeClock <em>Time Clock</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getTimeCoordinateSystem <em>Time Coordinate System</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getTimeEdge <em>Time Edge</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getTimeInstant <em>Time Instant</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getTimeInterval <em>Time Interval</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getTimeNode <em>Time Node</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getTimeOrdinalEra <em>Time Ordinal Era</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getTimeOrdinalReferenceSystem <em>Time Ordinal Reference System</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getTimePeriod <em>Time Period</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getTimePosition <em>Time Position</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getTimeTopologyComplex <em>Time Topology Complex</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getTin <em>Tin</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getTriangulatedSurface <em>Triangulated Surface</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getTopoComplex <em>Topo Complex</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getTopoComplexProperty <em>Topo Complex Property</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getTopoCurve <em>Topo Curve</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getTopoCurveProperty <em>Topo Curve Property</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getTopologyStyle <em>Topology Style</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getTopologyStyle1 <em>Topology Style1</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getTopoPoint <em>Topo Point</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getTopoPointProperty <em>Topo Point Property</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getTopoPrimitiveMember <em>Topo Primitive Member</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getTopoPrimitiveMembers <em>Topo Primitive Members</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getTopoSolid <em>Topo Solid</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getTopoSurface <em>Topo Surface</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getTopoSurfaceProperty <em>Topo Surface Property</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getTopoVolume <em>Topo Volume</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getTopoVolumeProperty <em>Topo Volume Property</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getTrack <em>Track</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getTransformation <em>Transformation</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getTransformationRef <em>Transformation Ref</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getTriangle <em>Triangle</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getTrianglePatches <em>Triangle Patches</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getTupleList <em>Tuple List</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getUnitOfMeasure <em>Unit Of Measure</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getUserDefinedCS <em>User Defined CS</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getUserDefinedCSRef <em>User Defined CS Ref</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getUsesAxis <em>Uses Axis</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getUsesCartesianCS <em>Uses Cartesian CS</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getUsesCS <em>Uses CS</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getUsesEllipsoid <em>Uses Ellipsoid</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getUsesEllipsoidalCS <em>Uses Ellipsoidal CS</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getUsesEngineeringDatum <em>Uses Engineering Datum</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getUsesGeodeticDatum <em>Uses Geodetic Datum</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getUsesImageDatum <em>Uses Image Datum</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getUsesMethod <em>Uses Method</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getUsesObliqueCartesianCS <em>Uses Oblique Cartesian CS</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getUsesOperation <em>Uses Operation</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getUsesParameter <em>Uses Parameter</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getUsesPrimeMeridian <em>Uses Prime Meridian</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getUsesSingleOperation <em>Uses Single Operation</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getUsesSphericalCS <em>Uses Spherical CS</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getUsesTemporalCS <em>Uses Temporal CS</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getUsesTemporalDatum <em>Uses Temporal Datum</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getUsesValue <em>Uses Value</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getUsesVerticalCS <em>Uses Vertical CS</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getUsesVerticalDatum <em>Uses Vertical Datum</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getUsing <em>Using</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getValidArea <em>Valid Area</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getValidTime <em>Valid Time</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getValue <em>Value</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getValueArray <em>Value Array</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getValueComponent <em>Value Component</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getValueComponents <em>Value Components</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getValueFile <em>Value File</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getValueList <em>Value List</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getValueOfParameter <em>Value Of Parameter</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getValueProperty <em>Value Property</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getValuesOfGroup <em>Values Of Group</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getVector <em>Vector</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getVersion <em>Version</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getVerticalCRS <em>Vertical CRS</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getVerticalCRSRef <em>Vertical CRS Ref</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getVerticalCS <em>Vertical CS</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getVerticalCSRef <em>Vertical CS Ref</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getVerticalDatum <em>Vertical Datum</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getVerticalDatumRef <em>Vertical Datum Ref</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getVerticalDatumType <em>Vertical Datum Type</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getVerticalExtent <em>Vertical Extent</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getId <em>Id</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getRemoteSchema <em>Remote Schema</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getTransform <em>Transform</em>}</li>
 *   <li>{@link net.opengis.gml311.DocumentRoot#getUom <em>Uom</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getDocumentRoot()
 * @model extendedMetaData="name='' kind='mixed'"
 * @generated
 */
public interface DocumentRoot extends EObject {
    /**
     * Returns the value of the '<em><b>Mixed</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Mixed</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Mixed</em>' attribute list.
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Mixed()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        extendedMetaData="kind='elementWildcard' name=':mixed'"
     * @generated
     */
    FeatureMap getMixed();

    /**
     * Returns the value of the '<em><b>XMLNS Prefix Map</b></em>' map.
     * The key is of type {@link java.lang.String},
     * and the value is of type {@link java.lang.String},
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>XMLNS Prefix Map</em>' map isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>XMLNS Prefix Map</em>' map.
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_XMLNSPrefixMap()
     * @model mapType="org.eclipse.emf.ecore.EStringToStringMapEntry<org.eclipse.emf.ecore.EString, org.eclipse.emf.ecore.EString>" transient="true"
     *        extendedMetaData="kind='attribute' name='xmlns:prefix'"
     * @generated
     */
    EMap<String, String> getXMLNSPrefixMap();

    /**
     * Returns the value of the '<em><b>XSI Schema Location</b></em>' map.
     * The key is of type {@link java.lang.String},
     * and the value is of type {@link java.lang.String},
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>XSI Schema Location</em>' map isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>XSI Schema Location</em>' map.
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_XSISchemaLocation()
     * @model mapType="org.eclipse.emf.ecore.EStringToStringMapEntry<org.eclipse.emf.ecore.EString, org.eclipse.emf.ecore.EString>" transient="true"
     *        extendedMetaData="kind='attribute' name='xsi:schemaLocation'"
     * @generated
     */
    EMap<String, String> getXSISchemaLocation();

    /**
     * Returns the value of the '<em><b>Association</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Association</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Association</em>' containment reference.
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Association()
     * @model containment="true" upper="-2" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='_association' namespace='##targetNamespace'"
     * @generated
     */
    AssociationType getAssociation();

    /**
     * Returns the value of the '<em><b>Continuous Coverage</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Continuous Coverage</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Continuous Coverage</em>' containment reference.
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_ContinuousCoverage()
     * @model containment="true" upper="-2" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='_ContinuousCoverage' namespace='##targetNamespace' affiliation='_Coverage'"
     * @generated
     */
    AbstractContinuousCoverageType getContinuousCoverage();

    /**
     * Returns the value of the '<em><b>Coverage</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Coverage</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Coverage</em>' containment reference.
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Coverage()
     * @model containment="true" upper="-2" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='_Coverage' namespace='##targetNamespace' affiliation='_Feature'"
     * @generated
     */
    AbstractCoverageType getCoverage();

    /**
     * Returns the value of the '<em><b>Feature</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Feature</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Feature</em>' containment reference.
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Feature()
     * @model containment="true" upper="-2" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='_Feature' namespace='##targetNamespace' affiliation='_GML'"
     * @generated
     */
    AbstractFeatureType getFeature();

    /**
     * Returns the value of the '<em><b>GML</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Global element which acts as the head of a substitution group that may include any element which is a GML feature, object, geometry or complex value
     * <!-- end-model-doc -->
     * @return the value of the '<em>GML</em>' containment reference.
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_GML()
     * @model containment="true" upper="-2" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='_GML' namespace='##targetNamespace' affiliation='_Object'"
     * @generated
     */
    AbstractGMLType getGML();

    /**
     * Returns the value of the '<em><b>Object</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This abstract element is the head of a substitutionGroup hierararchy which may contain either simpleContent or complexContent elements.  It is used to assert the model position of "class" elements declared in other GML schemas.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Object</em>' containment reference.
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Object()
     * @model containment="true" upper="-2" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='_Object' namespace='##targetNamespace'"
     * @generated
     */
    EObject getObject();

    /**
     * Returns the value of the '<em><b>Coordinate Operation</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Coordinate Operation</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Coordinate Operation</em>' containment reference.
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_CoordinateOperation()
     * @model containment="true" upper="-2" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='_CoordinateOperation' namespace='##targetNamespace' affiliation='Definition'"
     * @generated
     */
    AbstractCoordinateOperationType getCoordinateOperation();

    /**
     * Returns the value of the '<em><b>Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Definition</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Definition</em>' containment reference.
     * @see #setDefinition(DefinitionType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Definition()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Definition' namespace='##targetNamespace' affiliation='_GML'"
     * @generated
     */
    DefinitionType getDefinition();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getDefinition <em>Definition</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Definition</em>' containment reference.
     * @see #getDefinition()
     * @generated
     */
    void setDefinition(DefinitionType value);

    /**
     * Returns the value of the '<em><b>Coordinate Reference System</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * A coordinate reference system consists of an ordered sequence of coordinate system axes that are related to the earth through a datum. A coordinate reference system is defined by one datum and by one coordinate system. Most coordinate reference system do not move relative to the earth, except for engineering coordinate reference systems defined on moving platforms such as cars, ships, aircraft, and spacecraft. For further information, see OGC Abstract Specification Topic 2.
     * 
     * Coordinate reference systems are commonly divided into sub-types. The common classification criterion for sub-typing of coordinate reference systems is the way in which they deal with earth curvature. This has a direct effect on the portion of the earth's surface that can be covered by that type of CRS with an acceptable degree of error. The exception to the rule is the subtype "Temporal" which has been added by analogy. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Coordinate Reference System</em>' containment reference.
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_CoordinateReferenceSystem()
     * @model containment="true" upper="-2" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='_CoordinateReferenceSystem' namespace='##targetNamespace' affiliation='_CRS'"
     * @generated
     */
    AbstractReferenceSystemType getCoordinateReferenceSystem();

    /**
     * Returns the value of the '<em><b>CRS</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Abstract coordinate reference system, usually defined by a coordinate system and a datum. This abstract complexType shall not be used, extended, or restricted, in an Application Schema, to define a concrete subtype with a meaning equivalent to a concrete subtype specified in this document.
     * <!-- end-model-doc -->
     * @return the value of the '<em>CRS</em>' containment reference.
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_CRS()
     * @model containment="true" upper="-2" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='_CRS' namespace='##targetNamespace' affiliation='_ReferenceSystem'"
     * @generated
     */
    AbstractReferenceSystemType getCRS();

    /**
     * Returns the value of the '<em><b>Reference System</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Reference System</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Reference System</em>' containment reference.
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_ReferenceSystem()
     * @model containment="true" upper="-2" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='_ReferenceSystem' namespace='##targetNamespace' affiliation='Definition'"
     * @generated
     */
    AbstractReferenceSystemType getReferenceSystem();

    /**
     * Returns the value of the '<em><b>Coordinate System</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Coordinate System</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Coordinate System</em>' containment reference.
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_CoordinateSystem()
     * @model containment="true" upper="-2" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='_CoordinateSystem' namespace='##targetNamespace' affiliation='Definition'"
     * @generated
     */
    AbstractCoordinateSystemType getCoordinateSystem();

    /**
     * Returns the value of the '<em><b>Curve</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The "_Curve" element is the abstract head of the substituition group for all (continuous) curve elements.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Curve</em>' containment reference.
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Curve()
     * @model containment="true" upper="-2" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='_Curve' namespace='##targetNamespace' affiliation='_GeometricPrimitive'"
     * @generated
     */
    AbstractCurveType getCurve();

    /**
     * Returns the value of the '<em><b>Geometric Primitive</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The "_GeometricPrimitive" element is the abstract head of the substituition group for all (pre- and user-defined) 
     * 			geometric primitives.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Geometric Primitive</em>' containment reference.
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_GeometricPrimitive()
     * @model containment="true" upper="-2" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='_GeometricPrimitive' namespace='##targetNamespace' affiliation='_Geometry'"
     * @generated
     */
    AbstractGeometricPrimitiveType getGeometricPrimitive();

    /**
     * Returns the value of the '<em><b>Geometry</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The "_Geometry" element is the abstract head of the substituition group for all geometry elements of GML 3. This 
     * 			includes pre-defined and user-defined geometry elements. Any geometry element must be a direct or indirect extension/restriction 
     * 			of AbstractGeometryType and must be directly or indirectly in the substitution group of "_Geometry".
     * <!-- end-model-doc -->
     * @return the value of the '<em>Geometry</em>' containment reference.
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Geometry()
     * @model containment="true" upper="-2" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='_Geometry' namespace='##targetNamespace' affiliation='_GML'"
     * @generated
     */
    AbstractGeometryType getGeometry();

    /**
     * Returns the value of the '<em><b>Curve Segment</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The "_CurveSegment" element is the abstract head of the substituition group for all curve segment elements, i.e. continuous segments of the same interpolation mechanism.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Curve Segment</em>' containment reference.
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_CurveSegment()
     * @model containment="true" upper="-2" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='_CurveSegment' namespace='##targetNamespace'"
     * @generated
     */
    AbstractCurveSegmentType getCurveSegment();

    /**
     * Returns the value of the '<em><b>Datum</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Datum</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Datum</em>' containment reference.
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Datum()
     * @model containment="true" upper="-2" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='_Datum' namespace='##targetNamespace' affiliation='Definition'"
     * @generated
     */
    AbstractDatumType getDatum();

    /**
     * Returns the value of the '<em><b>Discrete Coverage</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Discrete Coverage</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Discrete Coverage</em>' containment reference.
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_DiscreteCoverage()
     * @model containment="true" upper="-2" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='_DiscreteCoverage' namespace='##targetNamespace' affiliation='_Coverage'"
     * @generated
     */
    AbstractDiscreteCoverageType getDiscreteCoverage();

    /**
     * Returns the value of the '<em><b>Feature Collection</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Feature Collection</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Feature Collection</em>' containment reference.
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_FeatureCollection()
     * @model containment="true" upper="-2" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='_FeatureCollection' namespace='##targetNamespace' affiliation='_Feature'"
     * @generated
     */
    AbstractFeatureCollectionType getFeatureCollection();

    /**
     * Returns the value of the '<em><b>General Conversion</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>General Conversion</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>General Conversion</em>' containment reference.
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_GeneralConversion()
     * @model containment="true" upper="-2" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='_GeneralConversion' namespace='##targetNamespace' affiliation='_Operation'"
     * @generated
     */
    AbstractGeneralConversionType getGeneralConversion();

    /**
     * Returns the value of the '<em><b>Operation</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * A parameterized mathematical operation on coordinates that transforms or converts coordinates to another coordinate reference system. This coordinate operation uses an operation method, usually with associated parameter values. However, operation methods and parameter values are directly associated with concrete subtypes, not with this abstract type.
     * 
     * This abstract complexType shall not be directly used, extended, or restricted in a compliant Application Schema. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Operation</em>' containment reference.
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Operation()
     * @model containment="true" upper="-2" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='_Operation' namespace='##targetNamespace' affiliation='_SingleOperation'"
     * @generated
     */
    AbstractCoordinateOperationType getOperation();

    /**
     * Returns the value of the '<em><b>Single Operation</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * A single (not concatenated) coordinate operation. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Single Operation</em>' containment reference.
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_SingleOperation()
     * @model containment="true" upper="-2" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='_SingleOperation' namespace='##targetNamespace' affiliation='_CoordinateOperation'"
     * @generated
     */
    AbstractCoordinateOperationType getSingleOperation();

    /**
     * Returns the value of the '<em><b>General Derived CRS</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>General Derived CRS</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>General Derived CRS</em>' containment reference.
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_GeneralDerivedCRS()
     * @model containment="true" upper="-2" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='_GeneralDerivedCRS' namespace='##targetNamespace' affiliation='_CoordinateReferenceSystem'"
     * @generated
     */
    AbstractGeneralDerivedCRSType getGeneralDerivedCRS();

    /**
     * Returns the value of the '<em><b>General Operation Parameter</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>General Operation Parameter</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>General Operation Parameter</em>' containment reference.
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_GeneralOperationParameter()
     * @model containment="true" upper="-2" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='_GeneralOperationParameter' namespace='##targetNamespace' affiliation='Definition'"
     * @generated
     */
    AbstractGeneralOperationParameterType getGeneralOperationParameter();

    /**
     * Returns the value of the '<em><b>General Parameter Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>General Parameter Value</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>General Parameter Value</em>' containment reference.
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_GeneralParameterValue()
     * @model containment="true" upper="-2" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='_generalParameterValue' namespace='##targetNamespace'"
     * @generated
     */
    AbstractGeneralParameterValueType getGeneralParameterValue();

    /**
     * Returns the value of the '<em><b>General Transformation</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>General Transformation</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>General Transformation</em>' containment reference.
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_GeneralTransformation()
     * @model containment="true" upper="-2" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='_GeneralTransformation' namespace='##targetNamespace' affiliation='_Operation'"
     * @generated
     */
    AbstractGeneralTransformationType getGeneralTransformation();

    /**
     * Returns the value of the '<em><b>Geometric Aggregate</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The "_GeometricAggregate" element is the abstract head of the substituition group for all geometric aggremates.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Geometric Aggregate</em>' containment reference.
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_GeometricAggregate()
     * @model containment="true" upper="-2" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='_GeometricAggregate' namespace='##targetNamespace' affiliation='_Geometry'"
     * @generated
     */
    AbstractGeometricAggregateType getGeometricAggregate();

    /**
     * Returns the value of the '<em><b>Gridded Surface</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Gridded Surface</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Gridded Surface</em>' containment reference.
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_GriddedSurface()
     * @model containment="true" upper="-2" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='_GriddedSurface' namespace='##targetNamespace' affiliation='_ParametricCurveSurface'"
     * @generated
     */
    AbstractGriddedSurfaceType getGriddedSurface();

    /**
     * Returns the value of the '<em><b>Parametric Curve Surface</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Parametric Curve Surface</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Parametric Curve Surface</em>' containment reference.
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_ParametricCurveSurface()
     * @model containment="true" upper="-2" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='_ParametricCurveSurface' namespace='##targetNamespace' affiliation='_SurfacePatch'"
     * @generated
     */
    AbstractParametricCurveSurfaceType getParametricCurveSurface();

    /**
     * Returns the value of the '<em><b>Surface Patch</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The "_SurfacePatch" element is the abstract head of the substituition group for all surface pach elements describing a continuous portion of a surface.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Surface Patch</em>' containment reference.
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_SurfacePatch()
     * @model containment="true" upper="-2" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='_SurfacePatch' namespace='##targetNamespace'"
     * @generated
     */
    AbstractSurfacePatchType getSurfacePatch();

    /**
     * Returns the value of the '<em><b>Implicit Geometry</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Implicit Geometry</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Implicit Geometry</em>' containment reference.
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_ImplicitGeometry()
     * @model containment="true" upper="-2" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='_ImplicitGeometry' namespace='##targetNamespace' affiliation='_Geometry'"
     * @generated
     */
    AbstractGeometryType getImplicitGeometry();

    /**
     * Returns the value of the '<em><b>Meta Data</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Abstract element which acts as the head of a substitution group for packages of MetaData properties.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Meta Data</em>' containment reference.
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_MetaData()
     * @model containment="true" upper="-2" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='_MetaData' namespace='##targetNamespace' affiliation='_Object'"
     * @generated
     */
    AbstractMetaDataType getMetaData();

    /**
     * Returns the value of the '<em><b>Positional Accuracy</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Positional Accuracy</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Positional Accuracy</em>' containment reference.
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_PositionalAccuracy()
     * @model containment="true" upper="-2" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='_positionalAccuracy' namespace='##targetNamespace'"
     * @generated
     */
    AbstractPositionalAccuracyType getPositionalAccuracy();

    /**
     * Returns the value of the '<em><b>Reference</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Reference</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Reference</em>' containment reference.
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Reference()
     * @model containment="true" upper="-2" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='_reference' namespace='##targetNamespace'"
     * @generated
     */
    ReferenceType getReference();

    /**
     * Returns the value of the '<em><b>Ring</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The "_Ring" element is the abstract head of the substituition group for all closed boundaries of a surface patch.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Ring</em>' containment reference.
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Ring()
     * @model containment="true" upper="-2" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='_Ring' namespace='##targetNamespace' affiliation='_Geometry'"
     * @generated
     */
    AbstractRingType getRing();

    /**
     * Returns the value of the '<em><b>Solid</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The "_Solid" element is the abstract head of the substituition group for all (continuous) solid elements.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Solid</em>' containment reference.
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Solid()
     * @model containment="true" upper="-2" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='_Solid' namespace='##targetNamespace' affiliation='_GeometricPrimitive'"
     * @generated
     */
    AbstractSolidType getSolid();

    /**
     * Returns the value of the '<em><b>Strict Association</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * must carry a reference to an object or contain an object but not both
     * <!-- end-model-doc -->
     * @return the value of the '<em>Strict Association</em>' containment reference.
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_StrictAssociation()
     * @model containment="true" upper="-2" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='_strictAssociation' namespace='##targetNamespace'"
     * @generated
     */
    AssociationType getStrictAssociation();

    /**
     * Returns the value of the '<em><b>Style</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The value of the top-level property. It is an abstract element. Used as the head element of the substitution group for extensibility purposes.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Style</em>' containment reference.
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Style()
     * @model containment="true" upper="-2" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='_Style' namespace='##targetNamespace' affiliation='_GML'"
     * @generated
     */
    AbstractStyleType getStyle();

    /**
     * Returns the value of the '<em><b>Surface</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The "_Surface" element is the abstract head of the substituition group for all (continuous) surface elements.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Surface</em>' containment reference.
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Surface()
     * @model containment="true" upper="-2" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='_Surface' namespace='##targetNamespace' affiliation='_GeometricPrimitive'"
     * @generated
     */
    AbstractSurfaceType getSurface();

    /**
     * Returns the value of the '<em><b>Time Complex</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This abstract element acts as the head of the substitution group for temporal complexes. 
     * 			Temporal complex is an aggregation of temporal primitives as its components, 
     * 			represents a temporal geometric complex and a temporal topology complex. 
     * 			N.B. Temporal geometric complex is not defined in this schema.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Time Complex</em>' containment reference.
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_TimeComplex()
     * @model containment="true" upper="-2" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='_TimeComplex' namespace='##targetNamespace' affiliation='_TimeObject'"
     * @generated
     */
    AbstractTimeComplexType getTimeComplex();

    /**
     * Returns the value of the '<em><b>Time Object</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This abstract element acts as the head of the substitution group for temporal primitives and complexes.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Time Object</em>' containment reference.
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_TimeObject()
     * @model containment="true" upper="-2" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='_TimeObject' namespace='##targetNamespace' affiliation='_GML'"
     * @generated
     */
    AbstractTimeObjectType getTimeObject();

    /**
     * Returns the value of the '<em><b>Time Geometric Primitive</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This abstract element acts as the head of the substitution group for temporal geometric primitives.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Time Geometric Primitive</em>' containment reference.
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_TimeGeometricPrimitive()
     * @model containment="true" upper="-2" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='_TimeGeometricPrimitive' namespace='##targetNamespace' affiliation='_TimePrimitive'"
     * @generated
     */
    AbstractTimeGeometricPrimitiveType getTimeGeometricPrimitive();

    /**
     * Returns the value of the '<em><b>Time Primitive</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This abstract element acts as the head of the substitution group for temporal primitives.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Time Primitive</em>' containment reference.
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_TimePrimitive()
     * @model containment="true" upper="-2" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='_TimePrimitive' namespace='##targetNamespace' affiliation='_TimeObject'"
     * @generated
     */
    AbstractTimePrimitiveType getTimePrimitive();

    /**
     * Returns the value of the '<em><b>Time Reference System</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Abstract element serves primarily as the head of a substitution group for temporal reference systems.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Time Reference System</em>' containment reference.
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_TimeReferenceSystem()
     * @model containment="true" upper="-2" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='_TimeReferenceSystem' namespace='##targetNamespace' affiliation='Definition'"
     * @generated
     */
    AbstractTimeReferenceSystemType getTimeReferenceSystem();

    /**
     * Returns the value of the '<em><b>Time Slice</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Time Slice</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Time Slice</em>' containment reference.
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_TimeSlice()
     * @model containment="true" upper="-2" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='_TimeSlice' namespace='##targetNamespace' affiliation='_GML'"
     * @generated
     */
    AbstractTimeSliceType getTimeSlice();

    /**
     * Returns the value of the '<em><b>Time Topology Primitive</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This abstract element acts as the head of the substitution group for temporal topology primitives.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Time Topology Primitive</em>' containment reference.
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_TimeTopologyPrimitive()
     * @model containment="true" upper="-2" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='_TimeTopologyPrimitive' namespace='##targetNamespace' affiliation='_TimePrimitive'"
     * @generated
     */
    AbstractTimeTopologyPrimitiveType getTimeTopologyPrimitive();

    /**
     * Returns the value of the '<em><b>Topology</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Topology</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Topology</em>' containment reference.
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Topology()
     * @model containment="true" upper="-2" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='_Topology' namespace='##targetNamespace' affiliation='_GML'"
     * @generated
     */
    AbstractTopologyType getTopology();

    /**
     * Returns the value of the '<em><b>Topo Primitive</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Substitution group branch for Topo Primitives, used by TopoPrimitiveArrayAssociationType
     * <!-- end-model-doc -->
     * @return the value of the '<em>Topo Primitive</em>' containment reference.
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_TopoPrimitive()
     * @model containment="true" upper="-2" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='_TopoPrimitive' namespace='##targetNamespace' affiliation='_Topology'"
     * @generated
     */
    AbstractTopoPrimitiveType getTopoPrimitive();

    /**
     * Returns the value of the '<em><b>Absolute External Positional Accuracy</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Absolute External Positional Accuracy</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Absolute External Positional Accuracy</em>' containment reference.
     * @see #setAbsoluteExternalPositionalAccuracy(AbsoluteExternalPositionalAccuracyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_AbsoluteExternalPositionalAccuracy()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='absoluteExternalPositionalAccuracy' namespace='##targetNamespace' affiliation='_positionalAccuracy'"
     * @generated
     */
    AbsoluteExternalPositionalAccuracyType getAbsoluteExternalPositionalAccuracy();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getAbsoluteExternalPositionalAccuracy <em>Absolute External Positional Accuracy</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Absolute External Positional Accuracy</em>' containment reference.
     * @see #getAbsoluteExternalPositionalAccuracy()
     * @generated
     */
    void setAbsoluteExternalPositionalAccuracy(AbsoluteExternalPositionalAccuracyType value);

    /**
     * Returns the value of the '<em><b>Abstract General Operation Parameter Ref</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Abstract General Operation Parameter Ref</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Abstract General Operation Parameter Ref</em>' containment reference.
     * @see #setAbstractGeneralOperationParameterRef(AbstractGeneralOperationParameterRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_AbstractGeneralOperationParameterRef()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='abstractGeneralOperationParameterRef' namespace='##targetNamespace'"
     * @generated
     */
    AbstractGeneralOperationParameterRefType getAbstractGeneralOperationParameterRef();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getAbstractGeneralOperationParameterRef <em>Abstract General Operation Parameter Ref</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Abstract General Operation Parameter Ref</em>' containment reference.
     * @see #getAbstractGeneralOperationParameterRef()
     * @generated
     */
    void setAbstractGeneralOperationParameterRef(AbstractGeneralOperationParameterRefType value);

    /**
     * Returns the value of the '<em><b>Affine Placement</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Affine Placement</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Affine Placement</em>' containment reference.
     * @see #setAffinePlacement(AffinePlacementType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_AffinePlacement()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='AffinePlacement' namespace='##targetNamespace'"
     * @generated
     */
    AffinePlacementType getAffinePlacement();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getAffinePlacement <em>Affine Placement</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Affine Placement</em>' containment reference.
     * @see #getAffinePlacement()
     * @generated
     */
    void setAffinePlacement(AffinePlacementType value);

    /**
     * Returns the value of the '<em><b>Anchor Point</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Description, possibly including coordinates, of the point or points used to anchor the datum to the Earth. Also known as the "origin", especially for engineering and image datums. The codeSpace attribute can be used to reference a source of more detailed on this point or surface, or on a set of such descriptions. 
     * - For a geodetic datum, this point is also known as the fundamental point, which is traditionally the point where the relationship between geoid and ellipsoid is defined. In some cases, the "fundamental point" may consist of a number of points. In those cases, the parameters defining the geoid/ellipsoid relationship have been averaged for these points, and the averages adopted as the datum definition.
     * - For an engineering datum, the anchor point may be a physical point, or it may be a point with defined coordinates in another CRS. When appropriate, the coordinates of this anchor point can be referenced in another document, such as referencing a GML feature that references or includes a point position.
     * - For an image datum, the anchor point is usually either the centre of the image or the corner of the image.
     * - For a temporal datum, this attribute is not defined. Instead of the anchor point, a temporal datum carries a separate time origin of type DateTime. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Anchor Point</em>' containment reference.
     * @see #setAnchorPoint(CodeType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_AnchorPoint()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='anchorPoint' namespace='##targetNamespace'"
     * @generated
     */
    CodeType getAnchorPoint();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getAnchorPoint <em>Anchor Point</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Anchor Point</em>' containment reference.
     * @see #getAnchorPoint()
     * @generated
     */
    void setAnchorPoint(CodeType value);

    /**
     * Returns the value of the '<em><b>Angle</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Angle</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Angle</em>' containment reference.
     * @see #setAngle(MeasureType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Angle()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='angle' namespace='##targetNamespace'"
     * @generated
     */
    MeasureType getAngle();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getAngle <em>Angle</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Angle</em>' containment reference.
     * @see #getAngle()
     * @generated
     */
    void setAngle(MeasureType value);

    /**
     * Returns the value of the '<em><b>Arc</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Arc</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Arc</em>' containment reference.
     * @see #setArc(ArcType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Arc()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Arc' namespace='##targetNamespace' affiliation='ArcString'"
     * @generated
     */
    ArcType getArc();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getArc <em>Arc</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Arc</em>' containment reference.
     * @see #getArc()
     * @generated
     */
    void setArc(ArcType value);

    /**
     * Returns the value of the '<em><b>Arc String</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Arc String</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Arc String</em>' containment reference.
     * @see #setArcString(ArcStringType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_ArcString()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='ArcString' namespace='##targetNamespace' affiliation='_CurveSegment'"
     * @generated
     */
    ArcStringType getArcString();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getArcString <em>Arc String</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Arc String</em>' containment reference.
     * @see #getArcString()
     * @generated
     */
    void setArcString(ArcStringType value);

    /**
     * Returns the value of the '<em><b>Arc By Bulge</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Arc By Bulge</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Arc By Bulge</em>' containment reference.
     * @see #setArcByBulge(ArcByBulgeType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_ArcByBulge()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='ArcByBulge' namespace='##targetNamespace' affiliation='ArcStringByBulge'"
     * @generated
     */
    ArcByBulgeType getArcByBulge();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getArcByBulge <em>Arc By Bulge</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Arc By Bulge</em>' containment reference.
     * @see #getArcByBulge()
     * @generated
     */
    void setArcByBulge(ArcByBulgeType value);

    /**
     * Returns the value of the '<em><b>Arc String By Bulge</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Arc String By Bulge</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Arc String By Bulge</em>' containment reference.
     * @see #setArcStringByBulge(ArcStringByBulgeType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_ArcStringByBulge()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='ArcStringByBulge' namespace='##targetNamespace' affiliation='_CurveSegment'"
     * @generated
     */
    ArcStringByBulgeType getArcStringByBulge();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getArcStringByBulge <em>Arc String By Bulge</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Arc String By Bulge</em>' containment reference.
     * @see #getArcStringByBulge()
     * @generated
     */
    void setArcStringByBulge(ArcStringByBulgeType value);

    /**
     * Returns the value of the '<em><b>Arc By Center Point</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Arc By Center Point</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Arc By Center Point</em>' containment reference.
     * @see #setArcByCenterPoint(ArcByCenterPointType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_ArcByCenterPoint()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='ArcByCenterPoint' namespace='##targetNamespace' affiliation='_CurveSegment'"
     * @generated
     */
    ArcByCenterPointType getArcByCenterPoint();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getArcByCenterPoint <em>Arc By Center Point</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Arc By Center Point</em>' containment reference.
     * @see #getArcByCenterPoint()
     * @generated
     */
    void setArcByCenterPoint(ArcByCenterPointType value);

    /**
     * Returns the value of the '<em><b>Array</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Generic GML element to contain a homogeneous array of GML _Objects
     * <!-- end-model-doc -->
     * @return the value of the '<em>Array</em>' containment reference.
     * @see #setArray(ArrayType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Array()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Array' namespace='##targetNamespace' affiliation='_GML'"
     * @generated
     */
    ArrayType getArray();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getArray <em>Array</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Array</em>' containment reference.
     * @see #getArray()
     * @generated
     */
    void setArray(ArrayType value);

    /**
     * Returns the value of the '<em><b>Axis Abbrev</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The abbreviation used for this coordinate system axis. This abbreviation can be used to identify the ordinates in a coordinate tuple. Examples are X and Y. The codeSpace attribute can reference a source of more information on a set of standardized abbreviations, or on this abbreviation. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Axis Abbrev</em>' containment reference.
     * @see #setAxisAbbrev(CodeType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_AxisAbbrev()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='axisAbbrev' namespace='##targetNamespace'"
     * @generated
     */
    CodeType getAxisAbbrev();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getAxisAbbrev <em>Axis Abbrev</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Axis Abbrev</em>' containment reference.
     * @see #getAxisAbbrev()
     * @generated
     */
    void setAxisAbbrev(CodeType value);

    /**
     * Returns the value of the '<em><b>Axis Direction</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Direction of this coordinate system axis (or in the case of Cartesian projected coordinates, the direction of this coordinate system axis at the origin). Examples: north or south, east or west, up or down. Within any set of coordinate system axes, only one of each pair of terms can be used. For earth-fixed CRSs, this direction is often approximate and intended to provide a human interpretable meaning to the axis. When a geodetic datum is used, the precise directions of the axes may therefore vary slightly from this approximate direction. Note that an EngineeringCRS can include specific descriptions of the directions of its coordinate system axes. For example, the path of a linear CRS axis can be referenced in another document, such as referencing a GML feature that references or includes a curve geometry. The codeSpace attribute can reference a source of more information on a set of standardized directions, or on this direction. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Axis Direction</em>' containment reference.
     * @see #setAxisDirection(CodeType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_AxisDirection()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='axisDirection' namespace='##targetNamespace'"
     * @generated
     */
    CodeType getAxisDirection();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getAxisDirection <em>Axis Direction</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Axis Direction</em>' containment reference.
     * @see #getAxisDirection()
     * @generated
     */
    void setAxisDirection(CodeType value);

    /**
     * Returns the value of the '<em><b>Axis ID</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * An identification of a coordinate system axis. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Axis ID</em>' containment reference.
     * @see #setAxisID(IdentifierType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_AxisID()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='axisID' namespace='##targetNamespace'"
     * @generated
     */
    IdentifierType getAxisID();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getAxisID <em>Axis ID</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Axis ID</em>' containment reference.
     * @see #getAxisID()
     * @generated
     */
    void setAxisID(IdentifierType value);

    /**
     * Returns the value of the '<em><b>Bag</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Generic GML element to contain a heterogeneous collection of GML _Objects
     * <!-- end-model-doc -->
     * @return the value of the '<em>Bag</em>' containment reference.
     * @see #setBag(BagType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Bag()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Bag' namespace='##targetNamespace' affiliation='_GML'"
     * @generated
     */
    BagType getBag();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getBag <em>Bag</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Bag</em>' containment reference.
     * @see #getBag()
     * @generated
     */
    void setBag(BagType value);

    /**
     * Returns the value of the '<em><b>Base CRS</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Association to the coordinate reference system used by this derived CRS. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Base CRS</em>' containment reference.
     * @see #setBaseCRS(CoordinateReferenceSystemRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_BaseCRS()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='baseCRS' namespace='##targetNamespace'"
     * @generated
     */
    CoordinateReferenceSystemRefType getBaseCRS();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getBaseCRS <em>Base CRS</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Base CRS</em>' containment reference.
     * @see #getBaseCRS()
     * @generated
     */
    void setBaseCRS(CoordinateReferenceSystemRefType value);

    /**
     * Returns the value of the '<em><b>Base Curve</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This property element either references a curve via the XLink-attributes or contains the curve element. A curve element is any element which is substitutable for "_Curve".
     * <!-- end-model-doc -->
     * @return the value of the '<em>Base Curve</em>' containment reference.
     * @see #setBaseCurve(CurvePropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_BaseCurve()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='baseCurve' namespace='##targetNamespace'"
     * @generated
     */
    CurvePropertyType getBaseCurve();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getBaseCurve <em>Base Curve</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Base Curve</em>' containment reference.
     * @see #getBaseCurve()
     * @generated
     */
    void setBaseCurve(CurvePropertyType value);

    /**
     * Returns the value of the '<em><b>Base Surface</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This property element either references a surface via the XLink-attributes or contains the surface element. A surface element is any element which is substitutable for "_Surface".
     * <!-- end-model-doc -->
     * @return the value of the '<em>Base Surface</em>' containment reference.
     * @see #setBaseSurface(SurfacePropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_BaseSurface()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='baseSurface' namespace='##targetNamespace'"
     * @generated
     */
    SurfacePropertyType getBaseSurface();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getBaseSurface <em>Base Surface</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Base Surface</em>' containment reference.
     * @see #getBaseSurface()
     * @generated
     */
    void setBaseSurface(SurfacePropertyType value);

    /**
     * Returns the value of the '<em><b>Base Unit</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Base Unit</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Base Unit</em>' containment reference.
     * @see #setBaseUnit(BaseUnitType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_BaseUnit()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='BaseUnit' namespace='##targetNamespace' affiliation='UnitDefinition'"
     * @generated
     */
    BaseUnitType getBaseUnit();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getBaseUnit <em>Base Unit</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Base Unit</em>' containment reference.
     * @see #getBaseUnit()
     * @generated
     */
    void setBaseUnit(BaseUnitType value);

    /**
     * Returns the value of the '<em><b>Unit Definition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Unit Definition</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Unit Definition</em>' containment reference.
     * @see #setUnitDefinition(UnitDefinitionType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_UnitDefinition()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='UnitDefinition' namespace='##targetNamespace' affiliation='Definition'"
     * @generated
     */
    UnitDefinitionType getUnitDefinition();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getUnitDefinition <em>Unit Definition</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Unit Definition</em>' containment reference.
     * @see #getUnitDefinition()
     * @generated
     */
    void setUnitDefinition(UnitDefinitionType value);

    /**
     * Returns the value of the '<em><b>Bezier</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Bezier</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Bezier</em>' containment reference.
     * @see #setBezier(BezierType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Bezier()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Bezier' namespace='##targetNamespace' affiliation='BSpline'"
     * @generated
     */
    BezierType getBezier();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getBezier <em>Bezier</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Bezier</em>' containment reference.
     * @see #getBezier()
     * @generated
     */
    void setBezier(BezierType value);

    /**
     * Returns the value of the '<em><b>BSpline</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>BSpline</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>BSpline</em>' containment reference.
     * @see #setBSpline(BSplineType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_BSpline()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='BSpline' namespace='##targetNamespace' affiliation='_CurveSegment'"
     * @generated
     */
    BSplineType getBSpline();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getBSpline <em>BSpline</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>BSpline</em>' containment reference.
     * @see #getBSpline()
     * @generated
     */
    void setBSpline(BSplineType value);

    /**
     * Returns the value of the '<em><b>Boolean</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * A value from two-valued logic, using the XML Schema boolean type.  An instance may take the values {true, false, 1, 0}.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Boolean</em>' attribute.
     * @see #setBoolean(boolean)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Boolean()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.Boolean" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Boolean' namespace='##targetNamespace'"
     * @generated
     */
    boolean isBoolean();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#isBoolean <em>Boolean</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Boolean</em>' attribute.
     * @see #isBoolean()
     * @generated
     */
    void setBoolean(boolean value);

    /**
     * Returns the value of the '<em><b>Boolean List</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * XML List based on XML Schema boolean type.  An element of this type contains a space-separated list of boolean values {0,1,true,false}
     * <!-- end-model-doc -->
     * @return the value of the '<em>Boolean List</em>' attribute.
     * @see #setBooleanList(List)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_BooleanList()
     * @model unique="false" dataType="net.opengis.gml311.BooleanOrNullList" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='BooleanList' namespace='##targetNamespace'"
     * @generated
     */
    List<Object> getBooleanList();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getBooleanList <em>Boolean List</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Boolean List</em>' attribute.
     * @see #getBooleanList()
     * @generated
     */
    void setBooleanList(List<Object> value);

    /**
     * Returns the value of the '<em><b>Boolean Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Boolean value of an operation parameter. A Boolean value does not have an associated unit of measure. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Boolean Value</em>' attribute.
     * @see #setBooleanValue(boolean)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_BooleanValue()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.Boolean" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='booleanValue' namespace='##targetNamespace'"
     * @generated
     */
    boolean isBooleanValue();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#isBooleanValue <em>Boolean Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Boolean Value</em>' attribute.
     * @see #isBooleanValue()
     * @generated
     */
    void setBooleanValue(boolean value);

    /**
     * Returns the value of the '<em><b>Bounded By</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Bounded By</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Bounded By</em>' containment reference.
     * @see #setBoundedBy(BoundingShapeType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_BoundedBy()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='boundedBy' namespace='##targetNamespace'"
     * @generated
     */
    BoundingShapeType getBoundedBy();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getBoundedBy <em>Bounded By</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Bounded By</em>' containment reference.
     * @see #getBoundedBy()
     * @generated
     */
    void setBoundedBy(BoundingShapeType value);

    /**
     * Returns the value of the '<em><b>Bounding Box</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * A bounding box (or envelope) defining the spatial domain of this object.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Bounding Box</em>' containment reference.
     * @see #setBoundingBox(EnvelopeType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_BoundingBox()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='boundingBox' namespace='##targetNamespace'"
     * @generated
     */
    EnvelopeType getBoundingBox();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getBoundingBox <em>Bounding Box</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Bounding Box</em>' containment reference.
     * @see #getBoundingBox()
     * @generated
     */
    void setBoundingBox(EnvelopeType value);

    /**
     * Returns the value of the '<em><b>Bounding Polygon</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * A bounding polygon defining the horizontal spatial domain of this object.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Bounding Polygon</em>' containment reference.
     * @see #setBoundingPolygon(PolygonType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_BoundingPolygon()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='boundingPolygon' namespace='##targetNamespace'"
     * @generated
     */
    PolygonType getBoundingPolygon();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getBoundingPolygon <em>Bounding Polygon</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Bounding Polygon</em>' containment reference.
     * @see #getBoundingPolygon()
     * @generated
     */
    void setBoundingPolygon(PolygonType value);

    /**
     * Returns the value of the '<em><b>Cartesian CS</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Cartesian CS</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Cartesian CS</em>' containment reference.
     * @see #setCartesianCS(CartesianCSType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_CartesianCS()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='CartesianCS' namespace='##targetNamespace' affiliation='_CoordinateSystem'"
     * @generated
     */
    CartesianCSType getCartesianCS();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getCartesianCS <em>Cartesian CS</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Cartesian CS</em>' containment reference.
     * @see #getCartesianCS()
     * @generated
     */
    void setCartesianCS(CartesianCSType value);

    /**
     * Returns the value of the '<em><b>Cartesian CS Ref</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Cartesian CS Ref</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Cartesian CS Ref</em>' containment reference.
     * @see #setCartesianCSRef(CartesianCSRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_CartesianCSRef()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='cartesianCSRef' namespace='##targetNamespace'"
     * @generated
     */
    CartesianCSRefType getCartesianCSRef();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getCartesianCSRef <em>Cartesian CS Ref</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Cartesian CS Ref</em>' containment reference.
     * @see #getCartesianCSRef()
     * @generated
     */
    void setCartesianCSRef(CartesianCSRefType value);

    /**
     * Returns the value of the '<em><b>Catalog Symbol</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * For global understanding of a unit of measure, it is often possible to reference an item in a catalog of units, using a symbol in that catalog. The "codeSpace" attribute in "CodeType" identifies a namespace for the catalog symbol value, and might reference the catalog. The "string" value in "CodeType" contains the value of a symbol that is unique within this catalog namespace. This symbol often appears explicitly in the catalog, but it could be a combination of symbols using a specified algebra of units. For example, the symbol "cm" might indicate that it is the "m" symbol combined with the "c" prefix.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Catalog Symbol</em>' containment reference.
     * @see #setCatalogSymbol(CodeType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_CatalogSymbol()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='catalogSymbol' namespace='##targetNamespace'"
     * @generated
     */
    CodeType getCatalogSymbol();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getCatalogSymbol <em>Catalog Symbol</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Catalog Symbol</em>' containment reference.
     * @see #getCatalogSymbol()
     * @generated
     */
    void setCatalogSymbol(CodeType value);

    /**
     * Returns the value of the '<em><b>Category</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * A term representing a classification.  It has an optional XML attribute codeSpace, whose value is a URI which identifies a dictionary, codelist or authority for the term.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Category</em>' containment reference.
     * @see #setCategory(CodeType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Category()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Category' namespace='##targetNamespace'"
     * @generated
     */
    CodeType getCategory();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getCategory <em>Category</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Category</em>' containment reference.
     * @see #getCategory()
     * @generated
     */
    void setCategory(CodeType value);

    /**
     * Returns the value of the '<em><b>Category Extent</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Utility element to store a 2-point range of ordinal values. If one member is a null, then this is a single ended interval.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Category Extent</em>' containment reference.
     * @see #setCategoryExtent(CategoryExtentType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_CategoryExtent()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='CategoryExtent' namespace='##targetNamespace'"
     * @generated
     */
    CategoryExtentType getCategoryExtent();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getCategoryExtent <em>Category Extent</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Category Extent</em>' containment reference.
     * @see #getCategoryExtent()
     * @generated
     */
    void setCategoryExtent(CategoryExtentType value);

    /**
     * Returns the value of the '<em><b>Category List</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * A space-separated list of terms or nulls.  A single XML attribute codeSpace may be provided, which authorises all the terms in the list.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Category List</em>' containment reference.
     * @see #setCategoryList(CodeOrNullListType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_CategoryList()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='CategoryList' namespace='##targetNamespace'"
     * @generated
     */
    CodeOrNullListType getCategoryList();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getCategoryList <em>Category List</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Category List</em>' containment reference.
     * @see #getCategoryList()
     * @generated
     */
    void setCategoryList(CodeOrNullListType value);

    /**
     * Returns the value of the '<em><b>Center Line Of</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Center Line Of</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Center Line Of</em>' containment reference.
     * @see #setCenterLineOf(CurvePropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_CenterLineOf()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='centerLineOf' namespace='##targetNamespace'"
     * @generated
     */
    CurvePropertyType getCenterLineOf();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getCenterLineOf <em>Center Line Of</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Center Line Of</em>' containment reference.
     * @see #getCenterLineOf()
     * @generated
     */
    void setCenterLineOf(CurvePropertyType value);

    /**
     * Returns the value of the '<em><b>Center Of</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Center Of</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Center Of</em>' containment reference.
     * @see #setCenterOf(PointPropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_CenterOf()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='centerOf' namespace='##targetNamespace'"
     * @generated
     */
    PointPropertyType getCenterOf();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getCenterOf <em>Center Of</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Center Of</em>' containment reference.
     * @see #getCenterOf()
     * @generated
     */
    void setCenterOf(PointPropertyType value);

    /**
     * Returns the value of the '<em><b>Circle</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Circle</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Circle</em>' containment reference.
     * @see #setCircle(CircleType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Circle()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Circle' namespace='##targetNamespace' affiliation='Arc'"
     * @generated
     */
    CircleType getCircle();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getCircle <em>Circle</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Circle</em>' containment reference.
     * @see #getCircle()
     * @generated
     */
    void setCircle(CircleType value);

    /**
     * Returns the value of the '<em><b>Circle By Center Point</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Circle By Center Point</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Circle By Center Point</em>' containment reference.
     * @see #setCircleByCenterPoint(CircleByCenterPointType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_CircleByCenterPoint()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='CircleByCenterPoint' namespace='##targetNamespace' affiliation='ArcByCenterPoint'"
     * @generated
     */
    CircleByCenterPointType getCircleByCenterPoint();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getCircleByCenterPoint <em>Circle By Center Point</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Circle By Center Point</em>' containment reference.
     * @see #getCircleByCenterPoint()
     * @generated
     */
    void setCircleByCenterPoint(CircleByCenterPointType value);

    /**
     * Returns the value of the '<em><b>Clothoid</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Clothoid</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Clothoid</em>' containment reference.
     * @see #setClothoid(ClothoidType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Clothoid()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Clothoid' namespace='##targetNamespace' affiliation='_CurveSegment'"
     * @generated
     */
    ClothoidType getClothoid();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getClothoid <em>Clothoid</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Clothoid</em>' containment reference.
     * @see #getClothoid()
     * @generated
     */
    void setClothoid(ClothoidType value);

    /**
     * Returns the value of the '<em><b>Column Index</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Column number of this covariance element value. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Column Index</em>' attribute.
     * @see #setColumnIndex(BigInteger)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_ColumnIndex()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.PositiveInteger" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='columnIndex' namespace='##targetNamespace'"
     * @generated
     */
    BigInteger getColumnIndex();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getColumnIndex <em>Column Index</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Column Index</em>' attribute.
     * @see #getColumnIndex()
     * @generated
     */
    void setColumnIndex(BigInteger value);

    /**
     * Returns the value of the '<em><b>Compass Point</b></em>' attribute.
     * The literals are from the enumeration {@link net.opengis.gml311.CompassPointEnumeration}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Compass Point</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Compass Point</em>' attribute.
     * @see net.opengis.gml311.CompassPointEnumeration
     * @see #setCompassPoint(CompassPointEnumeration)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_CompassPoint()
     * @model unique="false" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='CompassPoint' namespace='##targetNamespace'"
     * @generated
     */
    CompassPointEnumeration getCompassPoint();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getCompassPoint <em>Compass Point</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Compass Point</em>' attribute.
     * @see net.opengis.gml311.CompassPointEnumeration
     * @see #getCompassPoint()
     * @generated
     */
    void setCompassPoint(CompassPointEnumeration value);

    /**
     * Returns the value of the '<em><b>Composite Curve</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Composite Curve</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Composite Curve</em>' containment reference.
     * @see #setCompositeCurve(CompositeCurveType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_CompositeCurve()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='CompositeCurve' namespace='##targetNamespace' affiliation='_Curve'"
     * @generated
     */
    CompositeCurveType getCompositeCurve();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getCompositeCurve <em>Composite Curve</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Composite Curve</em>' containment reference.
     * @see #getCompositeCurve()
     * @generated
     */
    void setCompositeCurve(CompositeCurveType value);

    /**
     * Returns the value of the '<em><b>Composite Solid</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Composite Solid</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Composite Solid</em>' containment reference.
     * @see #setCompositeSolid(CompositeSolidType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_CompositeSolid()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='CompositeSolid' namespace='##targetNamespace' affiliation='_Solid'"
     * @generated
     */
    CompositeSolidType getCompositeSolid();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getCompositeSolid <em>Composite Solid</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Composite Solid</em>' containment reference.
     * @see #getCompositeSolid()
     * @generated
     */
    void setCompositeSolid(CompositeSolidType value);

    /**
     * Returns the value of the '<em><b>Composite Surface</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Composite Surface</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Composite Surface</em>' containment reference.
     * @see #setCompositeSurface(CompositeSurfaceType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_CompositeSurface()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='CompositeSurface' namespace='##targetNamespace' affiliation='_Surface'"
     * @generated
     */
    CompositeSurfaceType getCompositeSurface();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getCompositeSurface <em>Composite Surface</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Composite Surface</em>' containment reference.
     * @see #getCompositeSurface()
     * @generated
     */
    void setCompositeSurface(CompositeSurfaceType value);

    /**
     * Returns the value of the '<em><b>Composite Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Aggregate value built using the Composite pattern.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Composite Value</em>' containment reference.
     * @see #setCompositeValue(CompositeValueType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_CompositeValue()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='CompositeValue' namespace='##targetNamespace'"
     * @generated
     */
    CompositeValueType getCompositeValue();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getCompositeValue <em>Composite Value</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Composite Value</em>' containment reference.
     * @see #getCompositeValue()
     * @generated
     */
    void setCompositeValue(CompositeValueType value);

    /**
     * Returns the value of the '<em><b>Compound CRS</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Compound CRS</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Compound CRS</em>' containment reference.
     * @see #setCompoundCRS(CompoundCRSType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_CompoundCRS()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='CompoundCRS' namespace='##targetNamespace' affiliation='_CRS'"
     * @generated
     */
    CompoundCRSType getCompoundCRS();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getCompoundCRS <em>Compound CRS</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Compound CRS</em>' containment reference.
     * @see #getCompoundCRS()
     * @generated
     */
    void setCompoundCRS(CompoundCRSType value);

    /**
     * Returns the value of the '<em><b>Compound CRS Ref</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Compound CRS Ref</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Compound CRS Ref</em>' containment reference.
     * @see #setCompoundCRSRef(CompoundCRSRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_CompoundCRSRef()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='compoundCRSRef' namespace='##targetNamespace'"
     * @generated
     */
    CompoundCRSRefType getCompoundCRSRef();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getCompoundCRSRef <em>Compound CRS Ref</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Compound CRS Ref</em>' containment reference.
     * @see #getCompoundCRSRef()
     * @generated
     */
    void setCompoundCRSRef(CompoundCRSRefType value);

    /**
     * Returns the value of the '<em><b>Concatenated Operation</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Concatenated Operation</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Concatenated Operation</em>' containment reference.
     * @see #setConcatenatedOperation(ConcatenatedOperationType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_ConcatenatedOperation()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='ConcatenatedOperation' namespace='##targetNamespace' affiliation='_CoordinateOperation'"
     * @generated
     */
    ConcatenatedOperationType getConcatenatedOperation();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getConcatenatedOperation <em>Concatenated Operation</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Concatenated Operation</em>' containment reference.
     * @see #getConcatenatedOperation()
     * @generated
     */
    void setConcatenatedOperation(ConcatenatedOperationType value);

    /**
     * Returns the value of the '<em><b>Concatenated Operation Ref</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Concatenated Operation Ref</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Concatenated Operation Ref</em>' containment reference.
     * @see #setConcatenatedOperationRef(ConcatenatedOperationRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_ConcatenatedOperationRef()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='concatenatedOperationRef' namespace='##targetNamespace'"
     * @generated
     */
    ConcatenatedOperationRefType getConcatenatedOperationRef();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getConcatenatedOperationRef <em>Concatenated Operation Ref</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Concatenated Operation Ref</em>' containment reference.
     * @see #getConcatenatedOperationRef()
     * @generated
     */
    void setConcatenatedOperationRef(ConcatenatedOperationRefType value);

    /**
     * Returns the value of the '<em><b>Cone</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Cone</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Cone</em>' containment reference.
     * @see #setCone(ConeType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Cone()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Cone' namespace='##targetNamespace' affiliation='_GriddedSurface'"
     * @generated
     */
    ConeType getCone();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getCone <em>Cone</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Cone</em>' containment reference.
     * @see #getCone()
     * @generated
     */
    void setCone(ConeType value);

    /**
     * Returns the value of the '<em><b>Container</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Container</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Container</em>' containment reference.
     * @see #setContainer(ContainerPropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Container()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='container' namespace='##targetNamespace'"
     * @generated
     */
    ContainerPropertyType getContainer();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getContainer <em>Container</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Container</em>' containment reference.
     * @see #getContainer()
     * @generated
     */
    void setContainer(ContainerPropertyType value);

    /**
     * Returns the value of the '<em><b>Conventional Unit</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Conventional Unit</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Conventional Unit</em>' containment reference.
     * @see #setConventionalUnit(ConventionalUnitType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_ConventionalUnit()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='ConventionalUnit' namespace='##targetNamespace' affiliation='UnitDefinition'"
     * @generated
     */
    ConventionalUnitType getConventionalUnit();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getConventionalUnit <em>Conventional Unit</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Conventional Unit</em>' containment reference.
     * @see #getConventionalUnit()
     * @generated
     */
    void setConventionalUnit(ConventionalUnitType value);

    /**
     * Returns the value of the '<em><b>Conversion</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Conversion</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Conversion</em>' containment reference.
     * @see #setConversion(ConversionType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Conversion()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Conversion' namespace='##targetNamespace' affiliation='_GeneralConversion'"
     * @generated
     */
    ConversionType getConversion();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getConversion <em>Conversion</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Conversion</em>' containment reference.
     * @see #getConversion()
     * @generated
     */
    void setConversion(ConversionType value);

    /**
     * Returns the value of the '<em><b>Conversion Ref</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Conversion Ref</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Conversion Ref</em>' containment reference.
     * @see #setConversionRef(ConversionRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_ConversionRef()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='conversionRef' namespace='##targetNamespace'"
     * @generated
     */
    ConversionRefType getConversionRef();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getConversionRef <em>Conversion Ref</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Conversion Ref</em>' containment reference.
     * @see #getConversionRef()
     * @generated
     */
    void setConversionRef(ConversionRefType value);

    /**
     * Returns the value of the '<em><b>Conversion To Preferred Unit</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This element is included when this unit has an accurate conversion to the preferred unit for this quantity type.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Conversion To Preferred Unit</em>' containment reference.
     * @see #setConversionToPreferredUnit(ConversionToPreferredUnitType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_ConversionToPreferredUnit()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='conversionToPreferredUnit' namespace='##targetNamespace'"
     * @generated
     */
    ConversionToPreferredUnitType getConversionToPreferredUnit();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getConversionToPreferredUnit <em>Conversion To Preferred Unit</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Conversion To Preferred Unit</em>' containment reference.
     * @see #getConversionToPreferredUnit()
     * @generated
     */
    void setConversionToPreferredUnit(ConversionToPreferredUnitType value);

    /**
     * Returns the value of the '<em><b>Coord</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Deprecated with GML 3.0 and included for backwards compatibility with GML 2. Use the "pos" element instead.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Coord</em>' containment reference.
     * @see #setCoord(CoordType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Coord()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='coord' namespace='##targetNamespace'"
     * @generated
     */
    CoordType getCoord();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getCoord <em>Coord</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Coord</em>' containment reference.
     * @see #getCoord()
     * @generated
     */
    void setCoord(CoordType value);

    /**
     * Returns the value of the '<em><b>Coordinate Operation ID</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * An identification of a coordinate operation. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Coordinate Operation ID</em>' containment reference.
     * @see #setCoordinateOperationID(IdentifierType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_CoordinateOperationID()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='coordinateOperationID' namespace='##targetNamespace'"
     * @generated
     */
    IdentifierType getCoordinateOperationID();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getCoordinateOperationID <em>Coordinate Operation ID</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Coordinate Operation ID</em>' containment reference.
     * @see #getCoordinateOperationID()
     * @generated
     */
    void setCoordinateOperationID(IdentifierType value);

    /**
     * Returns the value of the '<em><b>Coordinate Operation Name</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The name by which this coordinate operation is identified. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Coordinate Operation Name</em>' containment reference.
     * @see #setCoordinateOperationName(CodeType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_CoordinateOperationName()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='coordinateOperationName' namespace='##targetNamespace' affiliation='name'"
     * @generated
     */
    CodeType getCoordinateOperationName();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getCoordinateOperationName <em>Coordinate Operation Name</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Coordinate Operation Name</em>' containment reference.
     * @see #getCoordinateOperationName()
     * @generated
     */
    void setCoordinateOperationName(CodeType value);

    /**
     * Returns the value of the '<em><b>Name</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Label for the object, normally a descriptive name. An object may have several names, typically assigned by different authorities.  The authority for a name is indicated by the value of its (optional) codeSpace attribute.  The name may or may not be unique, as determined by the rules of the organization responsible for the codeSpace.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Name</em>' containment reference.
     * @see #setName(CodeType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Name()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='name' namespace='##targetNamespace'"
     * @generated
     */
    CodeType getName();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getName <em>Name</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Name</em>' containment reference.
     * @see #getName()
     * @generated
     */
    void setName(CodeType value);

    /**
     * Returns the value of the '<em><b>Coordinate Operation Ref</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Coordinate Operation Ref</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Coordinate Operation Ref</em>' containment reference.
     * @see #setCoordinateOperationRef(CoordinateOperationRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_CoordinateOperationRef()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='coordinateOperationRef' namespace='##targetNamespace'"
     * @generated
     */
    CoordinateOperationRefType getCoordinateOperationRef();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getCoordinateOperationRef <em>Coordinate Operation Ref</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Coordinate Operation Ref</em>' containment reference.
     * @see #getCoordinateOperationRef()
     * @generated
     */
    void setCoordinateOperationRef(CoordinateOperationRefType value);

    /**
     * Returns the value of the '<em><b>Coordinate Reference System Ref</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Coordinate Reference System Ref</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Coordinate Reference System Ref</em>' containment reference.
     * @see #setCoordinateReferenceSystemRef(CoordinateReferenceSystemRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_CoordinateReferenceSystemRef()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='coordinateReferenceSystemRef' namespace='##targetNamespace'"
     * @generated
     */
    CoordinateReferenceSystemRefType getCoordinateReferenceSystemRef();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getCoordinateReferenceSystemRef <em>Coordinate Reference System Ref</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Coordinate Reference System Ref</em>' containment reference.
     * @see #getCoordinateReferenceSystemRef()
     * @generated
     */
    void setCoordinateReferenceSystemRef(CoordinateReferenceSystemRefType value);

    /**
     * Returns the value of the '<em><b>Coordinates</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Deprecated with GML version 3.1.0.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Coordinates</em>' containment reference.
     * @see #setCoordinates(CoordinatesType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Coordinates()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='coordinates' namespace='##targetNamespace'"
     * @generated
     */
    CoordinatesType getCoordinates();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getCoordinates <em>Coordinates</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Coordinates</em>' containment reference.
     * @see #getCoordinates()
     * @generated
     */
    void setCoordinates(CoordinatesType value);

    /**
     * Returns the value of the '<em><b>Coordinate System Axis</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Coordinate System Axis</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Coordinate System Axis</em>' containment reference.
     * @see #setCoordinateSystemAxis(CoordinateSystemAxisType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_CoordinateSystemAxis()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='CoordinateSystemAxis' namespace='##targetNamespace' affiliation='Definition'"
     * @generated
     */
    CoordinateSystemAxisType getCoordinateSystemAxis();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getCoordinateSystemAxis <em>Coordinate System Axis</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Coordinate System Axis</em>' containment reference.
     * @see #getCoordinateSystemAxis()
     * @generated
     */
    void setCoordinateSystemAxis(CoordinateSystemAxisType value);

    /**
     * Returns the value of the '<em><b>Coordinate System Axis Ref</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Coordinate System Axis Ref</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Coordinate System Axis Ref</em>' containment reference.
     * @see #setCoordinateSystemAxisRef(CoordinateSystemAxisRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_CoordinateSystemAxisRef()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='coordinateSystemAxisRef' namespace='##targetNamespace'"
     * @generated
     */
    CoordinateSystemAxisRefType getCoordinateSystemAxisRef();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getCoordinateSystemAxisRef <em>Coordinate System Axis Ref</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Coordinate System Axis Ref</em>' containment reference.
     * @see #getCoordinateSystemAxisRef()
     * @generated
     */
    void setCoordinateSystemAxisRef(CoordinateSystemAxisRefType value);

    /**
     * Returns the value of the '<em><b>Coordinate System Ref</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Coordinate System Ref</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Coordinate System Ref</em>' containment reference.
     * @see #setCoordinateSystemRef(CoordinateSystemRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_CoordinateSystemRef()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='coordinateSystemRef' namespace='##targetNamespace'"
     * @generated
     */
    CoordinateSystemRefType getCoordinateSystemRef();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getCoordinateSystemRef <em>Coordinate System Ref</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Coordinate System Ref</em>' containment reference.
     * @see #getCoordinateSystemRef()
     * @generated
     */
    void setCoordinateSystemRef(CoordinateSystemRefType value);

    /**
     * Returns the value of the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * An integer representing a frequency of occurrence.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Count</em>' attribute.
     * @see #setCount(BigInteger)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Count()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.Integer" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Count' namespace='##targetNamespace'"
     * @generated
     */
    BigInteger getCount();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getCount <em>Count</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Count</em>' attribute.
     * @see #getCount()
     * @generated
     */
    void setCount(BigInteger value);

    /**
     * Returns the value of the '<em><b>Count Extent</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Utility element to store a 2-point range of frequency values. If one member is a null, then this is a single ended interval.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Count Extent</em>' attribute.
     * @see #setCountExtent(List)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_CountExtent()
     * @model unique="false" dataType="net.opengis.gml311.CountExtentType" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='CountExtent' namespace='##targetNamespace'"
     * @generated
     */
    List<Object> getCountExtent();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getCountExtent <em>Count Extent</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Count Extent</em>' attribute.
     * @see #getCountExtent()
     * @generated
     */
    void setCountExtent(List<Object> value);

    /**
     * Returns the value of the '<em><b>Count List</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * A space-separated list of integers or nulls.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Count List</em>' attribute.
     * @see #setCountList(List)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_CountList()
     * @model unique="false" dataType="net.opengis.gml311.IntegerOrNullList" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='CountList' namespace='##targetNamespace'"
     * @generated
     */
    List<Object> getCountList();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getCountList <em>Count List</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Count List</em>' attribute.
     * @see #getCountList()
     * @generated
     */
    void setCountList(List<Object> value);

    /**
     * Returns the value of the '<em><b>Covariance</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Value of covariance matrix element. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Covariance</em>' attribute.
     * @see #setCovariance(double)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Covariance()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.Double" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='covariance' namespace='##targetNamespace'"
     * @generated
     */
    double getCovariance();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getCovariance <em>Covariance</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Covariance</em>' attribute.
     * @see #getCovariance()
     * @generated
     */
    void setCovariance(double value);

    /**
     * Returns the value of the '<em><b>Covariance Matrix</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Covariance Matrix</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Covariance Matrix</em>' containment reference.
     * @see #setCovarianceMatrix(CovarianceMatrixType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_CovarianceMatrix()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='covarianceMatrix' namespace='##targetNamespace' affiliation='_positionalAccuracy'"
     * @generated
     */
    CovarianceMatrixType getCovarianceMatrix();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getCovarianceMatrix <em>Covariance Matrix</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Covariance Matrix</em>' containment reference.
     * @see #getCovarianceMatrix()
     * @generated
     */
    void setCovarianceMatrix(CovarianceMatrixType value);

    /**
     * Returns the value of the '<em><b>Coverage Function</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Coverage Function</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Coverage Function</em>' containment reference.
     * @see #setCoverageFunction(CoverageFunctionType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_CoverageFunction()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='coverageFunction' namespace='##targetNamespace'"
     * @generated
     */
    CoverageFunctionType getCoverageFunction();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getCoverageFunction <em>Coverage Function</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Coverage Function</em>' containment reference.
     * @see #getCoverageFunction()
     * @generated
     */
    void setCoverageFunction(CoverageFunctionType value);

    /**
     * Returns the value of the '<em><b>Crs Ref</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Crs Ref</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Crs Ref</em>' containment reference.
     * @see #setCrsRef(CRSRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_CrsRef()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='crsRef' namespace='##targetNamespace'"
     * @generated
     */
    CRSRefType getCrsRef();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getCrsRef <em>Crs Ref</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Crs Ref</em>' containment reference.
     * @see #getCrsRef()
     * @generated
     */
    void setCrsRef(CRSRefType value);

    /**
     * Returns the value of the '<em><b>Cs ID</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * An identification of a coordinate system. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Cs ID</em>' containment reference.
     * @see #setCsID(IdentifierType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_CsID()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='csID' namespace='##targetNamespace'"
     * @generated
     */
    IdentifierType getCsID();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getCsID <em>Cs ID</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Cs ID</em>' containment reference.
     * @see #getCsID()
     * @generated
     */
    void setCsID(IdentifierType value);

    /**
     * Returns the value of the '<em><b>Cs Name</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The name by which this coordinate system is identified. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Cs Name</em>' containment reference.
     * @see #setCsName(CodeType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_CsName()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='csName' namespace='##targetNamespace' affiliation='name'"
     * @generated
     */
    CodeType getCsName();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getCsName <em>Cs Name</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Cs Name</em>' containment reference.
     * @see #getCsName()
     * @generated
     */
    void setCsName(CodeType value);

    /**
     * Returns the value of the '<em><b>Cubic Spline</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Cubic Spline</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Cubic Spline</em>' containment reference.
     * @see #setCubicSpline(CubicSplineType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_CubicSpline()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='CubicSpline' namespace='##targetNamespace' affiliation='_CurveSegment'"
     * @generated
     */
    CubicSplineType getCubicSpline();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getCubicSpline <em>Cubic Spline</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Cubic Spline</em>' containment reference.
     * @see #getCubicSpline()
     * @generated
     */
    void setCubicSpline(CubicSplineType value);

    /**
     * Returns the value of the '<em><b>Curve1</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Curve1</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Curve1</em>' containment reference.
     * @see #setCurve1(CurveType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Curve1()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Curve' namespace='##targetNamespace' affiliation='_Curve'"
     * @generated
     */
    CurveType getCurve1();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getCurve1 <em>Curve1</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Curve1</em>' containment reference.
     * @see #getCurve1()
     * @generated
     */
    void setCurve1(CurveType value);

    /**
     * Returns the value of the '<em><b>Curve Array Property</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Curve Array Property</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Curve Array Property</em>' containment reference.
     * @see #setCurveArrayProperty(CurveArrayPropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_CurveArrayProperty()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='curveArrayProperty' namespace='##targetNamespace'"
     * @generated
     */
    CurveArrayPropertyType getCurveArrayProperty();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getCurveArrayProperty <em>Curve Array Property</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Curve Array Property</em>' containment reference.
     * @see #getCurveArrayProperty()
     * @generated
     */
    void setCurveArrayProperty(CurveArrayPropertyType value);

    /**
     * Returns the value of the '<em><b>Curve Member</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This property element either references a curve via the XLink-attributes or contains the curve element. A curve element is any element which is substitutable for "_Curve".
     * <!-- end-model-doc -->
     * @return the value of the '<em>Curve Member</em>' containment reference.
     * @see #setCurveMember(CurvePropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_CurveMember()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='curveMember' namespace='##targetNamespace'"
     * @generated
     */
    CurvePropertyType getCurveMember();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getCurveMember <em>Curve Member</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Curve Member</em>' containment reference.
     * @see #getCurveMember()
     * @generated
     */
    void setCurveMember(CurvePropertyType value);

    /**
     * Returns the value of the '<em><b>Curve Members</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This property element contains a list of curves. The order of the elements is significant and shall be preserved when processing the array.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Curve Members</em>' containment reference.
     * @see #setCurveMembers(CurveArrayPropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_CurveMembers()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='curveMembers' namespace='##targetNamespace'"
     * @generated
     */
    CurveArrayPropertyType getCurveMembers();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getCurveMembers <em>Curve Members</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Curve Members</em>' containment reference.
     * @see #getCurveMembers()
     * @generated
     */
    void setCurveMembers(CurveArrayPropertyType value);

    /**
     * Returns the value of the '<em><b>Curve Property</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This property element either references a curve via the XLink-attributes or contains the curve element. curveProperty is the 
     * 			predefined property which can be used by GML Application Schemas whenever a GML Feature has a property with a value that is 
     * 			substitutable for _Curve.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Curve Property</em>' containment reference.
     * @see #setCurveProperty(CurvePropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_CurveProperty()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='curveProperty' namespace='##targetNamespace'"
     * @generated
     */
    CurvePropertyType getCurveProperty();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getCurveProperty <em>Curve Property</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Curve Property</em>' containment reference.
     * @see #getCurveProperty()
     * @generated
     */
    void setCurveProperty(CurvePropertyType value);

    /**
     * Returns the value of the '<em><b>Cylinder</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Cylinder</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Cylinder</em>' containment reference.
     * @see #setCylinder(CylinderType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Cylinder()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Cylinder' namespace='##targetNamespace' affiliation='_GriddedSurface'"
     * @generated
     */
    CylinderType getCylinder();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getCylinder <em>Cylinder</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Cylinder</em>' containment reference.
     * @see #getCylinder()
     * @generated
     */
    void setCylinder(CylinderType value);

    /**
     * Returns the value of the '<em><b>Cylindrical CS</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Cylindrical CS</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Cylindrical CS</em>' containment reference.
     * @see #setCylindricalCS(CylindricalCSType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_CylindricalCS()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='CylindricalCS' namespace='##targetNamespace' affiliation='_CoordinateSystem'"
     * @generated
     */
    CylindricalCSType getCylindricalCS();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getCylindricalCS <em>Cylindrical CS</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Cylindrical CS</em>' containment reference.
     * @see #getCylindricalCS()
     * @generated
     */
    void setCylindricalCS(CylindricalCSType value);

    /**
     * Returns the value of the '<em><b>Cylindrical CS Ref</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Cylindrical CS Ref</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Cylindrical CS Ref</em>' containment reference.
     * @see #setCylindricalCSRef(CylindricalCSRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_CylindricalCSRef()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='cylindricalCSRef' namespace='##targetNamespace'"
     * @generated
     */
    CylindricalCSRefType getCylindricalCSRef();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getCylindricalCSRef <em>Cylindrical CS Ref</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Cylindrical CS Ref</em>' containment reference.
     * @see #getCylindricalCSRef()
     * @generated
     */
    void setCylindricalCSRef(CylindricalCSRefType value);

    /**
     * Returns the value of the '<em><b>Data Block</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Data Block</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Data Block</em>' containment reference.
     * @see #setDataBlock(DataBlockType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_DataBlock()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='DataBlock' namespace='##targetNamespace'"
     * @generated
     */
    DataBlockType getDataBlock();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getDataBlock <em>Data Block</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Data Block</em>' containment reference.
     * @see #getDataBlock()
     * @generated
     */
    void setDataBlock(DataBlockType value);

    /**
     * Returns the value of the '<em><b>Data Source</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Data Source</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Data Source</em>' containment reference.
     * @see #setDataSource(StringOrRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_DataSource()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='dataSource' namespace='##targetNamespace'"
     * @generated
     */
    StringOrRefType getDataSource();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getDataSource <em>Data Source</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Data Source</em>' containment reference.
     * @see #getDataSource()
     * @generated
     */
    void setDataSource(StringOrRefType value);

    /**
     * Returns the value of the '<em><b>Datum ID</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * An identification of a datum. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Datum ID</em>' containment reference.
     * @see #setDatumID(IdentifierType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_DatumID()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='datumID' namespace='##targetNamespace'"
     * @generated
     */
    IdentifierType getDatumID();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getDatumID <em>Datum ID</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Datum ID</em>' containment reference.
     * @see #getDatumID()
     * @generated
     */
    void setDatumID(IdentifierType value);

    /**
     * Returns the value of the '<em><b>Datum Name</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The name by which this datum is identified. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Datum Name</em>' containment reference.
     * @see #setDatumName(CodeType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_DatumName()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='datumName' namespace='##targetNamespace' affiliation='name'"
     * @generated
     */
    CodeType getDatumName();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getDatumName <em>Datum Name</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Datum Name</em>' containment reference.
     * @see #getDatumName()
     * @generated
     */
    void setDatumName(CodeType value);

    /**
     * Returns the value of the '<em><b>Datum Ref</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Datum Ref</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Datum Ref</em>' containment reference.
     * @see #setDatumRef(DatumRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_DatumRef()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='datumRef' namespace='##targetNamespace'"
     * @generated
     */
    DatumRefType getDatumRef();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getDatumRef <em>Datum Ref</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Datum Ref</em>' containment reference.
     * @see #getDatumRef()
     * @generated
     */
    void setDatumRef(DatumRefType value);

    /**
     * Returns the value of the '<em><b>Decimal Minutes</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Decimal Minutes</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Decimal Minutes</em>' attribute.
     * @see #setDecimalMinutes(BigDecimal)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_DecimalMinutes()
     * @model unique="false" dataType="net.opengis.gml311.DecimalMinutesType" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='decimalMinutes' namespace='##targetNamespace'"
     * @generated
     */
    BigDecimal getDecimalMinutes();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getDecimalMinutes <em>Decimal Minutes</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Decimal Minutes</em>' attribute.
     * @see #getDecimalMinutes()
     * @generated
     */
    void setDecimalMinutes(BigDecimal value);

    /**
     * Returns the value of the '<em><b>Default Style</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Top-level property. Used in application schemas to "attach" the styling information to GML data. The link between the data and the style should be established through this property only.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Default Style</em>' containment reference.
     * @see #setDefaultStyle(DefaultStylePropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_DefaultStyle()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='defaultStyle' namespace='##targetNamespace'"
     * @generated
     */
    DefaultStylePropertyType getDefaultStyle();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getDefaultStyle <em>Default Style</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Default Style</em>' containment reference.
     * @see #getDefaultStyle()
     * @generated
     */
    void setDefaultStyle(DefaultStylePropertyType value);

    /**
     * Returns the value of the '<em><b>Defined By Conversion</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Association to the coordinate conversion used to define this derived CRS. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Defined By Conversion</em>' containment reference.
     * @see #setDefinedByConversion(GeneralConversionRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_DefinedByConversion()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='definedByConversion' namespace='##targetNamespace'"
     * @generated
     */
    GeneralConversionRefType getDefinedByConversion();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getDefinedByConversion <em>Defined By Conversion</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Defined By Conversion</em>' containment reference.
     * @see #getDefinedByConversion()
     * @generated
     */
    void setDefinedByConversion(GeneralConversionRefType value);

    /**
     * Returns the value of the '<em><b>Definition Collection</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Definition Collection</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Definition Collection</em>' containment reference.
     * @see #setDefinitionCollection(DictionaryType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_DefinitionCollection()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='DefinitionCollection' namespace='##targetNamespace' affiliation='Definition'"
     * @generated
     */
    DictionaryType getDefinitionCollection();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getDefinitionCollection <em>Definition Collection</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Definition Collection</em>' containment reference.
     * @see #getDefinitionCollection()
     * @generated
     */
    void setDefinitionCollection(DictionaryType value);

    /**
     * Returns the value of the '<em><b>Definition Member</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Definition Member</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Definition Member</em>' containment reference.
     * @see #setDefinitionMember(DictionaryEntryType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_DefinitionMember()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='definitionMember' namespace='##targetNamespace' affiliation='dictionaryEntry'"
     * @generated
     */
    DictionaryEntryType getDefinitionMember();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getDefinitionMember <em>Definition Member</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Definition Member</em>' containment reference.
     * @see #getDefinitionMember()
     * @generated
     */
    void setDefinitionMember(DictionaryEntryType value);

    /**
     * Returns the value of the '<em><b>Dictionary Entry</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Dictionary Entry</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Dictionary Entry</em>' containment reference.
     * @see #setDictionaryEntry(DictionaryEntryType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_DictionaryEntry()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='dictionaryEntry' namespace='##targetNamespace'"
     * @generated
     */
    DictionaryEntryType getDictionaryEntry();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getDictionaryEntry <em>Dictionary Entry</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Dictionary Entry</em>' containment reference.
     * @see #getDictionaryEntry()
     * @generated
     */
    void setDictionaryEntry(DictionaryEntryType value);

    /**
     * Returns the value of the '<em><b>Definition Proxy</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Definition Proxy</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Definition Proxy</em>' containment reference.
     * @see #setDefinitionProxy(DefinitionProxyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_DefinitionProxy()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='DefinitionProxy' namespace='##targetNamespace' affiliation='Definition'"
     * @generated
     */
    DefinitionProxyType getDefinitionProxy();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getDefinitionProxy <em>Definition Proxy</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Definition Proxy</em>' containment reference.
     * @see #getDefinitionProxy()
     * @generated
     */
    void setDefinitionProxy(DefinitionProxyType value);

    /**
     * Returns the value of the '<em><b>Definition Ref</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Definition Ref</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Definition Ref</em>' containment reference.
     * @see #setDefinitionRef(ReferenceType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_DefinitionRef()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='definitionRef' namespace='##targetNamespace'"
     * @generated
     */
    ReferenceType getDefinitionRef();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getDefinitionRef <em>Definition Ref</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Definition Ref</em>' containment reference.
     * @see #getDefinitionRef()
     * @generated
     */
    void setDefinitionRef(ReferenceType value);

    /**
     * Returns the value of the '<em><b>Degrees</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Degrees</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Degrees</em>' containment reference.
     * @see #setDegrees(DegreesType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Degrees()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='degrees' namespace='##targetNamespace'"
     * @generated
     */
    DegreesType getDegrees();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getDegrees <em>Degrees</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Degrees</em>' containment reference.
     * @see #getDegrees()
     * @generated
     */
    void setDegrees(DegreesType value);

    /**
     * Returns the value of the '<em><b>Derivation Unit Term</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Derivation Unit Term</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Derivation Unit Term</em>' containment reference.
     * @see #setDerivationUnitTerm(DerivationUnitTermType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_DerivationUnitTerm()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='derivationUnitTerm' namespace='##targetNamespace'"
     * @generated
     */
    DerivationUnitTermType getDerivationUnitTerm();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getDerivationUnitTerm <em>Derivation Unit Term</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Derivation Unit Term</em>' containment reference.
     * @see #getDerivationUnitTerm()
     * @generated
     */
    void setDerivationUnitTerm(DerivationUnitTermType value);

    /**
     * Returns the value of the '<em><b>Derived CRS</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Derived CRS</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Derived CRS</em>' containment reference.
     * @see #setDerivedCRS(DerivedCRSType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_DerivedCRS()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='DerivedCRS' namespace='##targetNamespace' affiliation='_GeneralDerivedCRS'"
     * @generated
     */
    DerivedCRSType getDerivedCRS();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getDerivedCRS <em>Derived CRS</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Derived CRS</em>' containment reference.
     * @see #getDerivedCRS()
     * @generated
     */
    void setDerivedCRS(DerivedCRSType value);

    /**
     * Returns the value of the '<em><b>Derived CRS Ref</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Derived CRS Ref</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Derived CRS Ref</em>' containment reference.
     * @see #setDerivedCRSRef(DerivedCRSRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_DerivedCRSRef()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='derivedCRSRef' namespace='##targetNamespace'"
     * @generated
     */
    DerivedCRSRefType getDerivedCRSRef();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getDerivedCRSRef <em>Derived CRS Ref</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Derived CRS Ref</em>' containment reference.
     * @see #getDerivedCRSRef()
     * @generated
     */
    void setDerivedCRSRef(DerivedCRSRefType value);

    /**
     * Returns the value of the '<em><b>Derived CRS Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Derived CRS Type</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Derived CRS Type</em>' containment reference.
     * @see #setDerivedCRSType(DerivedCRSTypeType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_DerivedCRSType()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='derivedCRSType' namespace='##targetNamespace'"
     * @generated
     */
    DerivedCRSTypeType getDerivedCRSType();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getDerivedCRSType <em>Derived CRS Type</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Derived CRS Type</em>' containment reference.
     * @see #getDerivedCRSType()
     * @generated
     */
    void setDerivedCRSType(DerivedCRSTypeType value);

    /**
     * Returns the value of the '<em><b>Derived Unit</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Derived Unit</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Derived Unit</em>' containment reference.
     * @see #setDerivedUnit(DerivedUnitType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_DerivedUnit()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='DerivedUnit' namespace='##targetNamespace' affiliation='UnitDefinition'"
     * @generated
     */
    DerivedUnitType getDerivedUnit();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getDerivedUnit <em>Derived Unit</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Derived Unit</em>' containment reference.
     * @see #getDerivedUnit()
     * @generated
     */
    void setDerivedUnit(DerivedUnitType value);

    /**
     * Returns the value of the '<em><b>Description</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Contains a simple text description of the object, or refers to an external description.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Description</em>' containment reference.
     * @see #setDescription(StringOrRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Description()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='description' namespace='##targetNamespace'"
     * @generated
     */
    StringOrRefType getDescription();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getDescription <em>Description</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Description</em>' containment reference.
     * @see #getDescription()
     * @generated
     */
    void setDescription(StringOrRefType value);

    /**
     * Returns the value of the '<em><b>Dictionary</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Dictionary</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Dictionary</em>' containment reference.
     * @see #setDictionary(DictionaryType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Dictionary()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Dictionary' namespace='##targetNamespace' affiliation='Definition'"
     * @generated
     */
    DictionaryType getDictionary();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getDictionary <em>Dictionary</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Dictionary</em>' containment reference.
     * @see #getDictionary()
     * @generated
     */
    void setDictionary(DictionaryType value);

    /**
     * Returns the value of the '<em><b>Directed Edge</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Directed Edge</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Directed Edge</em>' containment reference.
     * @see #setDirectedEdge(DirectedEdgePropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_DirectedEdge()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='directedEdge' namespace='##targetNamespace'"
     * @generated
     */
    DirectedEdgePropertyType getDirectedEdge();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getDirectedEdge <em>Directed Edge</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Directed Edge</em>' containment reference.
     * @see #getDirectedEdge()
     * @generated
     */
    void setDirectedEdge(DirectedEdgePropertyType value);

    /**
     * Returns the value of the '<em><b>Directed Face</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Directed Face</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Directed Face</em>' containment reference.
     * @see #setDirectedFace(DirectedFacePropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_DirectedFace()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='directedFace' namespace='##targetNamespace'"
     * @generated
     */
    DirectedFacePropertyType getDirectedFace();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getDirectedFace <em>Directed Face</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Directed Face</em>' containment reference.
     * @see #getDirectedFace()
     * @generated
     */
    void setDirectedFace(DirectedFacePropertyType value);

    /**
     * Returns the value of the '<em><b>Directed Node</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Directed Node</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Directed Node</em>' containment reference.
     * @see #setDirectedNode(DirectedNodePropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_DirectedNode()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='directedNode' namespace='##targetNamespace'"
     * @generated
     */
    DirectedNodePropertyType getDirectedNode();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getDirectedNode <em>Directed Node</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Directed Node</em>' containment reference.
     * @see #getDirectedNode()
     * @generated
     */
    void setDirectedNode(DirectedNodePropertyType value);

    /**
     * Returns the value of the '<em><b>Directed Observation</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Directed Observation</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Directed Observation</em>' containment reference.
     * @see #setDirectedObservation(DirectedObservationType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_DirectedObservation()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='DirectedObservation' namespace='##targetNamespace' affiliation='Observation'"
     * @generated
     */
    DirectedObservationType getDirectedObservation();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getDirectedObservation <em>Directed Observation</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Directed Observation</em>' containment reference.
     * @see #getDirectedObservation()
     * @generated
     */
    void setDirectedObservation(DirectedObservationType value);

    /**
     * Returns the value of the '<em><b>Observation</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Observation</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Observation</em>' containment reference.
     * @see #setObservation(ObservationType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Observation()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Observation' namespace='##targetNamespace' affiliation='_Feature'"
     * @generated
     */
    ObservationType getObservation();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getObservation <em>Observation</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Observation</em>' containment reference.
     * @see #getObservation()
     * @generated
     */
    void setObservation(ObservationType value);

    /**
     * Returns the value of the '<em><b>Directed Observation At Distance</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Directed Observation At Distance</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Directed Observation At Distance</em>' containment reference.
     * @see #setDirectedObservationAtDistance(DirectedObservationAtDistanceType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_DirectedObservationAtDistance()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='DirectedObservationAtDistance' namespace='##targetNamespace' affiliation='DirectedObservation'"
     * @generated
     */
    DirectedObservationAtDistanceType getDirectedObservationAtDistance();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getDirectedObservationAtDistance <em>Directed Observation At Distance</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Directed Observation At Distance</em>' containment reference.
     * @see #getDirectedObservationAtDistance()
     * @generated
     */
    void setDirectedObservationAtDistance(DirectedObservationAtDistanceType value);

    /**
     * Returns the value of the '<em><b>Directed Topo Solid</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Directed Topo Solid</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Directed Topo Solid</em>' containment reference.
     * @see #setDirectedTopoSolid(DirectedTopoSolidPropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_DirectedTopoSolid()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='directedTopoSolid' namespace='##targetNamespace'"
     * @generated
     */
    DirectedTopoSolidPropertyType getDirectedTopoSolid();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getDirectedTopoSolid <em>Directed Topo Solid</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Directed Topo Solid</em>' containment reference.
     * @see #getDirectedTopoSolid()
     * @generated
     */
    void setDirectedTopoSolid(DirectedTopoSolidPropertyType value);

    /**
     * Returns the value of the '<em><b>Direction</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Direction</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Direction</em>' containment reference.
     * @see #setDirection(DirectionPropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Direction()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='direction' namespace='##targetNamespace'"
     * @generated
     */
    DirectionPropertyType getDirection();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getDirection <em>Direction</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Direction</em>' containment reference.
     * @see #getDirection()
     * @generated
     */
    void setDirection(DirectionPropertyType value);

    /**
     * Returns the value of the '<em><b>Direction Vector</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Direction Vector</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Direction Vector</em>' containment reference.
     * @see #setDirectionVector(DirectionVectorType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_DirectionVector()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='DirectionVector' namespace='##targetNamespace'"
     * @generated
     */
    DirectionVectorType getDirectionVector();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getDirectionVector <em>Direction Vector</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Direction Vector</em>' containment reference.
     * @see #getDirectionVector()
     * @generated
     */
    void setDirectionVector(DirectionVectorType value);

    /**
     * Returns the value of the '<em><b>Dms Angle</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Dms Angle</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Dms Angle</em>' containment reference.
     * @see #setDmsAngle(DMSAngleType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_DmsAngle()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='dmsAngle' namespace='##targetNamespace'"
     * @generated
     */
    DMSAngleType getDmsAngle();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getDmsAngle <em>Dms Angle</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Dms Angle</em>' containment reference.
     * @see #getDmsAngle()
     * @generated
     */
    void setDmsAngle(DMSAngleType value);

    /**
     * Returns the value of the '<em><b>Dms Angle Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Value of an angle operation parameter, in either degree-minute-second format or single value format. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Dms Angle Value</em>' containment reference.
     * @see #setDmsAngleValue(DMSAngleType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_DmsAngleValue()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='dmsAngleValue' namespace='##targetNamespace'"
     * @generated
     */
    DMSAngleType getDmsAngleValue();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getDmsAngleValue <em>Dms Angle Value</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Dms Angle Value</em>' containment reference.
     * @see #getDmsAngleValue()
     * @generated
     */
    void setDmsAngleValue(DMSAngleType value);

    /**
     * Returns the value of the '<em><b>Domain Set</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Domain Set</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Domain Set</em>' containment reference.
     * @see #setDomainSet(DomainSetType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_DomainSet()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='domainSet' namespace='##targetNamespace'"
     * @generated
     */
    DomainSetType getDomainSet();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getDomainSet <em>Domain Set</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Domain Set</em>' containment reference.
     * @see #getDomainSet()
     * @generated
     */
    void setDomainSet(DomainSetType value);

    /**
     * Returns the value of the '<em><b>Double Or Null Tuple List</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Double Or Null Tuple List</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Double Or Null Tuple List</em>' attribute.
     * @see #setDoubleOrNullTupleList(List)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_DoubleOrNullTupleList()
     * @model unique="false" dataType="net.opengis.gml311.DoubleOrNullList" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='doubleOrNullTupleList' namespace='##targetNamespace'"
     * @generated
     */
    List<Object> getDoubleOrNullTupleList();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getDoubleOrNullTupleList <em>Double Or Null Tuple List</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Double Or Null Tuple List</em>' attribute.
     * @see #getDoubleOrNullTupleList()
     * @generated
     */
    void setDoubleOrNullTupleList(List<Object> value);

    /**
     * Returns the value of the '<em><b>Duration</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This element is an instance of the primitive xsd:duration simple type to 
     *       enable use of the ISO 8601 syntax for temporal length (e.g. P5DT4H30M). 
     *       It is a valid subtype of TimeDurationType according to section 3.14.6, 
     *       rule 2.2.4 in XML Schema, Part 1.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Duration</em>' attribute.
     * @see #setDuration(Duration)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Duration()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.Duration" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='duration' namespace='##targetNamespace'"
     * @generated
     */
    Duration getDuration();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getDuration <em>Duration</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Duration</em>' attribute.
     * @see #getDuration()
     * @generated
     */
    void setDuration(Duration value);

    /**
     * Returns the value of the '<em><b>Edge</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Edge</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Edge</em>' containment reference.
     * @see #setEdge(EdgeType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Edge()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Edge' namespace='##targetNamespace' affiliation='_TopoPrimitive'"
     * @generated
     */
    EdgeType getEdge();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getEdge <em>Edge</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Edge</em>' containment reference.
     * @see #getEdge()
     * @generated
     */
    void setEdge(EdgeType value);

    /**
     * Returns the value of the '<em><b>Edge Of</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Edge Of</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Edge Of</em>' containment reference.
     * @see #setEdgeOf(CurvePropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_EdgeOf()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='edgeOf' namespace='##targetNamespace'"
     * @generated
     */
    CurvePropertyType getEdgeOf();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getEdgeOf <em>Edge Of</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Edge Of</em>' containment reference.
     * @see #getEdgeOf()
     * @generated
     */
    void setEdgeOf(CurvePropertyType value);

    /**
     * Returns the value of the '<em><b>Ellipsoid</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Ellipsoid</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Ellipsoid</em>' containment reference.
     * @see #setEllipsoid(EllipsoidType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Ellipsoid()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Ellipsoid' namespace='##targetNamespace' affiliation='Definition'"
     * @generated
     */
    EllipsoidType getEllipsoid();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getEllipsoid <em>Ellipsoid</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Ellipsoid</em>' containment reference.
     * @see #getEllipsoid()
     * @generated
     */
    void setEllipsoid(EllipsoidType value);

    /**
     * Returns the value of the '<em><b>Ellipsoidal CS</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Ellipsoidal CS</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Ellipsoidal CS</em>' containment reference.
     * @see #setEllipsoidalCS(EllipsoidalCSType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_EllipsoidalCS()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='EllipsoidalCS' namespace='##targetNamespace' affiliation='_CoordinateSystem'"
     * @generated
     */
    EllipsoidalCSType getEllipsoidalCS();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getEllipsoidalCS <em>Ellipsoidal CS</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Ellipsoidal CS</em>' containment reference.
     * @see #getEllipsoidalCS()
     * @generated
     */
    void setEllipsoidalCS(EllipsoidalCSType value);

    /**
     * Returns the value of the '<em><b>Ellipsoidal CS Ref</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Ellipsoidal CS Ref</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Ellipsoidal CS Ref</em>' containment reference.
     * @see #setEllipsoidalCSRef(EllipsoidalCSRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_EllipsoidalCSRef()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='ellipsoidalCSRef' namespace='##targetNamespace'"
     * @generated
     */
    EllipsoidalCSRefType getEllipsoidalCSRef();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getEllipsoidalCSRef <em>Ellipsoidal CS Ref</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Ellipsoidal CS Ref</em>' containment reference.
     * @see #getEllipsoidalCSRef()
     * @generated
     */
    void setEllipsoidalCSRef(EllipsoidalCSRefType value);

    /**
     * Returns the value of the '<em><b>Ellipsoid ID</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * An identification of an ellipsoid. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Ellipsoid ID</em>' containment reference.
     * @see #setEllipsoidID(IdentifierType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_EllipsoidID()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='ellipsoidID' namespace='##targetNamespace'"
     * @generated
     */
    IdentifierType getEllipsoidID();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getEllipsoidID <em>Ellipsoid ID</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Ellipsoid ID</em>' containment reference.
     * @see #getEllipsoidID()
     * @generated
     */
    void setEllipsoidID(IdentifierType value);

    /**
     * Returns the value of the '<em><b>Ellipsoid Name</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The name by which this ellipsoid is identified. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Ellipsoid Name</em>' containment reference.
     * @see #setEllipsoidName(CodeType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_EllipsoidName()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='ellipsoidName' namespace='##targetNamespace' affiliation='name'"
     * @generated
     */
    CodeType getEllipsoidName();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getEllipsoidName <em>Ellipsoid Name</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Ellipsoid Name</em>' containment reference.
     * @see #getEllipsoidName()
     * @generated
     */
    void setEllipsoidName(CodeType value);

    /**
     * Returns the value of the '<em><b>Ellipsoid Ref</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Ellipsoid Ref</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Ellipsoid Ref</em>' containment reference.
     * @see #setEllipsoidRef(EllipsoidRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_EllipsoidRef()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='ellipsoidRef' namespace='##targetNamespace'"
     * @generated
     */
    EllipsoidRefType getEllipsoidRef();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getEllipsoidRef <em>Ellipsoid Ref</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Ellipsoid Ref</em>' containment reference.
     * @see #getEllipsoidRef()
     * @generated
     */
    void setEllipsoidRef(EllipsoidRefType value);

    /**
     * Returns the value of the '<em><b>Engineering CRS</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Engineering CRS</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Engineering CRS</em>' containment reference.
     * @see #setEngineeringCRS(EngineeringCRSType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_EngineeringCRS()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='EngineeringCRS' namespace='##targetNamespace' affiliation='_CoordinateReferenceSystem'"
     * @generated
     */
    EngineeringCRSType getEngineeringCRS();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getEngineeringCRS <em>Engineering CRS</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Engineering CRS</em>' containment reference.
     * @see #getEngineeringCRS()
     * @generated
     */
    void setEngineeringCRS(EngineeringCRSType value);

    /**
     * Returns the value of the '<em><b>Engineering CRS Ref</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Engineering CRS Ref</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Engineering CRS Ref</em>' containment reference.
     * @see #setEngineeringCRSRef(EngineeringCRSRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_EngineeringCRSRef()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='engineeringCRSRef' namespace='##targetNamespace'"
     * @generated
     */
    EngineeringCRSRefType getEngineeringCRSRef();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getEngineeringCRSRef <em>Engineering CRS Ref</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Engineering CRS Ref</em>' containment reference.
     * @see #getEngineeringCRSRef()
     * @generated
     */
    void setEngineeringCRSRef(EngineeringCRSRefType value);

    /**
     * Returns the value of the '<em><b>Engineering Datum</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Engineering Datum</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Engineering Datum</em>' containment reference.
     * @see #setEngineeringDatum(EngineeringDatumType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_EngineeringDatum()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='EngineeringDatum' namespace='##targetNamespace' affiliation='_Datum'"
     * @generated
     */
    EngineeringDatumType getEngineeringDatum();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getEngineeringDatum <em>Engineering Datum</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Engineering Datum</em>' containment reference.
     * @see #getEngineeringDatum()
     * @generated
     */
    void setEngineeringDatum(EngineeringDatumType value);

    /**
     * Returns the value of the '<em><b>Engineering Datum Ref</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Engineering Datum Ref</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Engineering Datum Ref</em>' containment reference.
     * @see #setEngineeringDatumRef(EngineeringDatumRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_EngineeringDatumRef()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='engineeringDatumRef' namespace='##targetNamespace'"
     * @generated
     */
    EngineeringDatumRefType getEngineeringDatumRef();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getEngineeringDatumRef <em>Engineering Datum Ref</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Engineering Datum Ref</em>' containment reference.
     * @see #getEngineeringDatumRef()
     * @generated
     */
    void setEngineeringDatumRef(EngineeringDatumRefType value);

    /**
     * Returns the value of the '<em><b>Envelope</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Envelope</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Envelope</em>' containment reference.
     * @see #setEnvelope(EnvelopeType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Envelope()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Envelope' namespace='##targetNamespace'"
     * @generated
     */
    EnvelopeType getEnvelope();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getEnvelope <em>Envelope</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Envelope</em>' containment reference.
     * @see #getEnvelope()
     * @generated
     */
    void setEnvelope(EnvelopeType value);

    /**
     * Returns the value of the '<em><b>Envelope With Time Period</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Envelope With Time Period</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Envelope With Time Period</em>' containment reference.
     * @see #setEnvelopeWithTimePeriod(EnvelopeWithTimePeriodType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_EnvelopeWithTimePeriod()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='EnvelopeWithTimePeriod' namespace='##targetNamespace' affiliation='Envelope'"
     * @generated
     */
    EnvelopeWithTimePeriodType getEnvelopeWithTimePeriod();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getEnvelopeWithTimePeriod <em>Envelope With Time Period</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Envelope With Time Period</em>' containment reference.
     * @see #getEnvelopeWithTimePeriod()
     * @generated
     */
    void setEnvelopeWithTimePeriod(EnvelopeWithTimePeriodType value);

    /**
     * Returns the value of the '<em><b>Extent Of</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Extent Of</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Extent Of</em>' containment reference.
     * @see #setExtentOf(SurfacePropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_ExtentOf()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='extentOf' namespace='##targetNamespace'"
     * @generated
     */
    SurfacePropertyType getExtentOf();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getExtentOf <em>Extent Of</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Extent Of</em>' containment reference.
     * @see #getExtentOf()
     * @generated
     */
    void setExtentOf(SurfacePropertyType value);

    /**
     * Returns the value of the '<em><b>Exterior</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * A boundary of a surface consists of a number of rings. In the normal 2D case, one of these rings is distinguished as being the exterior boundary. In a general manifold this is not always possible, in which case all boundaries shall be listed as interior boundaries, and the exterior will be empty.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Exterior</em>' containment reference.
     * @see #setExterior(AbstractRingPropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Exterior()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='exterior' namespace='##targetNamespace'"
     * @generated
     */
    AbstractRingPropertyType getExterior();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getExterior <em>Exterior</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Exterior</em>' containment reference.
     * @see #getExterior()
     * @generated
     */
    void setExterior(AbstractRingPropertyType value);

    /**
     * Returns the value of the '<em><b>Face</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Face</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Face</em>' containment reference.
     * @see #setFace(FaceType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Face()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Face' namespace='##targetNamespace' affiliation='_TopoPrimitive'"
     * @generated
     */
    FaceType getFace();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getFace <em>Face</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Face</em>' containment reference.
     * @see #getFace()
     * @generated
     */
    void setFace(FaceType value);

    /**
     * Returns the value of the '<em><b>Feature Collection1</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Feature Collection1</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Feature Collection1</em>' containment reference.
     * @see #setFeatureCollection1(FeatureCollectionType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_FeatureCollection1()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='FeatureCollection' namespace='##targetNamespace' affiliation='_Feature'"
     * @generated
     */
    FeatureCollectionType getFeatureCollection1();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getFeatureCollection1 <em>Feature Collection1</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Feature Collection1</em>' containment reference.
     * @see #getFeatureCollection1()
     * @generated
     */
    void setFeatureCollection1(FeatureCollectionType value);

    /**
     * Returns the value of the '<em><b>Feature Member</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Feature Member</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Feature Member</em>' containment reference.
     * @see #setFeatureMember(FeaturePropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_FeatureMember()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='featureMember' namespace='##targetNamespace'"
     * @generated
     */
    FeaturePropertyType getFeatureMember();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getFeatureMember <em>Feature Member</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Feature Member</em>' containment reference.
     * @see #getFeatureMember()
     * @generated
     */
    void setFeatureMember(FeaturePropertyType value);

    /**
     * Returns the value of the '<em><b>Feature Members</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Feature Members</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Feature Members</em>' containment reference.
     * @see #setFeatureMembers(FeatureArrayPropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_FeatureMembers()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='featureMembers' namespace='##targetNamespace'"
     * @generated
     */
    FeatureArrayPropertyType getFeatureMembers();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getFeatureMembers <em>Feature Members</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Feature Members</em>' containment reference.
     * @see #getFeatureMembers()
     * @generated
     */
    void setFeatureMembers(FeatureArrayPropertyType value);

    /**
     * Returns the value of the '<em><b>Feature Property</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Feature Property</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Feature Property</em>' containment reference.
     * @see #setFeatureProperty(FeaturePropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_FeatureProperty()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='featureProperty' namespace='##targetNamespace'"
     * @generated
     */
    FeaturePropertyType getFeatureProperty();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getFeatureProperty <em>Feature Property</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Feature Property</em>' containment reference.
     * @see #getFeatureProperty()
     * @generated
     */
    void setFeatureProperty(FeaturePropertyType value);

    /**
     * Returns the value of the '<em><b>Feature Style</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Feature Style</em>' containment reference.
     * @see #setFeatureStyle(FeatureStylePropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_FeatureStyle()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='featureStyle' namespace='##targetNamespace'"
     * @generated
     */
    FeatureStylePropertyType getFeatureStyle();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getFeatureStyle <em>Feature Style</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Feature Style</em>' containment reference.
     * @see #getFeatureStyle()
     * @generated
     */
    void setFeatureStyle(FeatureStylePropertyType value);

    /**
     * Returns the value of the '<em><b>Feature Style1</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The style descriptor for features.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Feature Style1</em>' containment reference.
     * @see #setFeatureStyle1(FeatureStyleType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_FeatureStyle1()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='FeatureStyle' namespace='##targetNamespace' affiliation='_GML'"
     * @generated
     */
    FeatureStyleType getFeatureStyle1();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getFeatureStyle1 <em>Feature Style1</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Feature Style1</em>' containment reference.
     * @see #getFeatureStyle1()
     * @generated
     */
    void setFeatureStyle1(FeatureStyleType value);

    /**
     * Returns the value of the '<em><b>File</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>File</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>File</em>' containment reference.
     * @see #setFile(FileType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_File()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='File' namespace='##targetNamespace'"
     * @generated
     */
    FileType getFile();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getFile <em>File</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>File</em>' containment reference.
     * @see #getFile()
     * @generated
     */
    void setFile(FileType value);

    /**
     * Returns the value of the '<em><b>General Conversion Ref</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>General Conversion Ref</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>General Conversion Ref</em>' containment reference.
     * @see #setGeneralConversionRef(GeneralConversionRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_GeneralConversionRef()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='generalConversionRef' namespace='##targetNamespace'"
     * @generated
     */
    GeneralConversionRefType getGeneralConversionRef();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getGeneralConversionRef <em>General Conversion Ref</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>General Conversion Ref</em>' containment reference.
     * @see #getGeneralConversionRef()
     * @generated
     */
    void setGeneralConversionRef(GeneralConversionRefType value);

    /**
     * Returns the value of the '<em><b>General Transformation Ref</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>General Transformation Ref</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>General Transformation Ref</em>' containment reference.
     * @see #setGeneralTransformationRef(GeneralTransformationRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_GeneralTransformationRef()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='generalTransformationRef' namespace='##targetNamespace'"
     * @generated
     */
    GeneralTransformationRefType getGeneralTransformationRef();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getGeneralTransformationRef <em>General Transformation Ref</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>General Transformation Ref</em>' containment reference.
     * @see #getGeneralTransformationRef()
     * @generated
     */
    void setGeneralTransformationRef(GeneralTransformationRefType value);

    /**
     * Returns the value of the '<em><b>Generic Meta Data</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Concrete element in the _MetaData substitution group, which permits any well-formed XML content.  Intended to act as a container for metadata defined in external schemas, for which it is not possible to add the concrete components to the GML _MetaData substitution group directly. Deprecated with GML version 3.1.0.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Generic Meta Data</em>' containment reference.
     * @see #setGenericMetaData(GenericMetaDataType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_GenericMetaData()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='GenericMetaData' namespace='##targetNamespace' affiliation='_MetaData'"
     * @generated
     */
    GenericMetaDataType getGenericMetaData();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getGenericMetaData <em>Generic Meta Data</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Generic Meta Data</em>' containment reference.
     * @see #getGenericMetaData()
     * @generated
     */
    void setGenericMetaData(GenericMetaDataType value);

    /**
     * Returns the value of the '<em><b>Geocentric CRS</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Geocentric CRS</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Geocentric CRS</em>' containment reference.
     * @see #setGeocentricCRS(GeocentricCRSType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_GeocentricCRS()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='GeocentricCRS' namespace='##targetNamespace' affiliation='_CoordinateReferenceSystem'"
     * @generated
     */
    GeocentricCRSType getGeocentricCRS();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getGeocentricCRS <em>Geocentric CRS</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Geocentric CRS</em>' containment reference.
     * @see #getGeocentricCRS()
     * @generated
     */
    void setGeocentricCRS(GeocentricCRSType value);

    /**
     * Returns the value of the '<em><b>Geocentric CRS Ref</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Geocentric CRS Ref</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Geocentric CRS Ref</em>' containment reference.
     * @see #setGeocentricCRSRef(GeocentricCRSRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_GeocentricCRSRef()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='geocentricCRSRef' namespace='##targetNamespace'"
     * @generated
     */
    GeocentricCRSRefType getGeocentricCRSRef();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getGeocentricCRSRef <em>Geocentric CRS Ref</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Geocentric CRS Ref</em>' containment reference.
     * @see #getGeocentricCRSRef()
     * @generated
     */
    void setGeocentricCRSRef(GeocentricCRSRefType value);

    /**
     * Returns the value of the '<em><b>Geodesic</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Geodesic</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Geodesic</em>' containment reference.
     * @see #setGeodesic(GeodesicType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Geodesic()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Geodesic' namespace='##targetNamespace' affiliation='GeodesicString'"
     * @generated
     */
    GeodesicType getGeodesic();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getGeodesic <em>Geodesic</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Geodesic</em>' containment reference.
     * @see #getGeodesic()
     * @generated
     */
    void setGeodesic(GeodesicType value);

    /**
     * Returns the value of the '<em><b>Geodesic String</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Geodesic String</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Geodesic String</em>' containment reference.
     * @see #setGeodesicString(GeodesicStringType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_GeodesicString()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='GeodesicString' namespace='##targetNamespace' affiliation='_CurveSegment'"
     * @generated
     */
    GeodesicStringType getGeodesicString();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getGeodesicString <em>Geodesic String</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Geodesic String</em>' containment reference.
     * @see #getGeodesicString()
     * @generated
     */
    void setGeodesicString(GeodesicStringType value);

    /**
     * Returns the value of the '<em><b>Geodetic Datum</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Geodetic Datum</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Geodetic Datum</em>' containment reference.
     * @see #setGeodeticDatum(GeodeticDatumType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_GeodeticDatum()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='GeodeticDatum' namespace='##targetNamespace' affiliation='_Datum'"
     * @generated
     */
    GeodeticDatumType getGeodeticDatum();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getGeodeticDatum <em>Geodetic Datum</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Geodetic Datum</em>' containment reference.
     * @see #getGeodeticDatum()
     * @generated
     */
    void setGeodeticDatum(GeodeticDatumType value);

    /**
     * Returns the value of the '<em><b>Geodetic Datum Ref</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Geodetic Datum Ref</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Geodetic Datum Ref</em>' containment reference.
     * @see #setGeodeticDatumRef(GeodeticDatumRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_GeodeticDatumRef()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='geodeticDatumRef' namespace='##targetNamespace'"
     * @generated
     */
    GeodeticDatumRefType getGeodeticDatumRef();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getGeodeticDatumRef <em>Geodetic Datum Ref</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Geodetic Datum Ref</em>' containment reference.
     * @see #getGeodeticDatumRef()
     * @generated
     */
    void setGeodeticDatumRef(GeodeticDatumRefType value);

    /**
     * Returns the value of the '<em><b>Geographic CRS</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Geographic CRS</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Geographic CRS</em>' containment reference.
     * @see #setGeographicCRS(GeographicCRSType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_GeographicCRS()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='GeographicCRS' namespace='##targetNamespace' affiliation='_CoordinateReferenceSystem'"
     * @generated
     */
    GeographicCRSType getGeographicCRS();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getGeographicCRS <em>Geographic CRS</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Geographic CRS</em>' containment reference.
     * @see #getGeographicCRS()
     * @generated
     */
    void setGeographicCRS(GeographicCRSType value);

    /**
     * Returns the value of the '<em><b>Geographic CRS Ref</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Geographic CRS Ref</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Geographic CRS Ref</em>' containment reference.
     * @see #setGeographicCRSRef(GeographicCRSRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_GeographicCRSRef()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='geographicCRSRef' namespace='##targetNamespace'"
     * @generated
     */
    GeographicCRSRefType getGeographicCRSRef();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getGeographicCRSRef <em>Geographic CRS Ref</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Geographic CRS Ref</em>' containment reference.
     * @see #getGeographicCRSRef()
     * @generated
     */
    void setGeographicCRSRef(GeographicCRSRefType value);

    /**
     * Returns the value of the '<em><b>Geometric Complex</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Geometric Complex</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Geometric Complex</em>' containment reference.
     * @see #setGeometricComplex(GeometricComplexType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_GeometricComplex()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='GeometricComplex' namespace='##targetNamespace' affiliation='_Geometry'"
     * @generated
     */
    GeometricComplexType getGeometricComplex();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getGeometricComplex <em>Geometric Complex</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Geometric Complex</em>' containment reference.
     * @see #getGeometricComplex()
     * @generated
     */
    void setGeometricComplex(GeometricComplexType value);

    /**
     * Returns the value of the '<em><b>Geometry Member</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This property element either references a geometry element via the XLink-attributes or contains the geometry element.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Geometry Member</em>' containment reference.
     * @see #setGeometryMember(GeometryPropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_GeometryMember()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='geometryMember' namespace='##targetNamespace'"
     * @generated
     */
    GeometryPropertyType getGeometryMember();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getGeometryMember <em>Geometry Member</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Geometry Member</em>' containment reference.
     * @see #getGeometryMember()
     * @generated
     */
    void setGeometryMember(GeometryPropertyType value);

    /**
     * Returns the value of the '<em><b>Geometry Members</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This property element contains a list of geometry elements. The order of the elements is significant and shall be preserved when processing the array.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Geometry Members</em>' containment reference.
     * @see #setGeometryMembers(GeometryArrayPropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_GeometryMembers()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='geometryMembers' namespace='##targetNamespace'"
     * @generated
     */
    GeometryArrayPropertyType getGeometryMembers();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getGeometryMembers <em>Geometry Members</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Geometry Members</em>' containment reference.
     * @see #getGeometryMembers()
     * @generated
     */
    void setGeometryMembers(GeometryArrayPropertyType value);

    /**
     * Returns the value of the '<em><b>Geometry Style</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Geometry Style</em>' containment reference.
     * @see #setGeometryStyle(GeometryStylePropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_GeometryStyle()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='geometryStyle' namespace='##targetNamespace'"
     * @generated
     */
    GeometryStylePropertyType getGeometryStyle();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getGeometryStyle <em>Geometry Style</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Geometry Style</em>' containment reference.
     * @see #getGeometryStyle()
     * @generated
     */
    void setGeometryStyle(GeometryStylePropertyType value);

    /**
     * Returns the value of the '<em><b>Geometry Style1</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The style descriptor for geometries of a feature.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Geometry Style1</em>' containment reference.
     * @see #setGeometryStyle1(GeometryStyleType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_GeometryStyle1()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='GeometryStyle' namespace='##targetNamespace' affiliation='_GML'"
     * @generated
     */
    GeometryStyleType getGeometryStyle1();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getGeometryStyle1 <em>Geometry Style1</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Geometry Style1</em>' containment reference.
     * @see #getGeometryStyle1()
     * @generated
     */
    void setGeometryStyle1(GeometryStyleType value);

    /**
     * Returns the value of the '<em><b>Graph Style</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Graph Style</em>' containment reference.
     * @see #setGraphStyle(GraphStylePropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_GraphStyle()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='graphStyle' namespace='##targetNamespace'"
     * @generated
     */
    GraphStylePropertyType getGraphStyle();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getGraphStyle <em>Graph Style</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Graph Style</em>' containment reference.
     * @see #getGraphStyle()
     * @generated
     */
    void setGraphStyle(GraphStylePropertyType value);

    /**
     * Returns the value of the '<em><b>Graph Style1</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The style descriptor for a graph consisting of a number of features. Describes graph-specific style attributes.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Graph Style1</em>' containment reference.
     * @see #setGraphStyle1(GraphStyleType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_GraphStyle1()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='GraphStyle' namespace='##targetNamespace' affiliation='_GML'"
     * @generated
     */
    GraphStyleType getGraphStyle1();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getGraphStyle1 <em>Graph Style1</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Graph Style1</em>' containment reference.
     * @see #getGraphStyle1()
     * @generated
     */
    void setGraphStyle1(GraphStyleType value);

    /**
     * Returns the value of the '<em><b>Greenwich Longitude</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Longitude of the prime meridian measured from the Greenwich meridian, positive eastward. The greenwichLongitude most common value is zero, and that value shall be used when the meridianName value is Greenwich. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Greenwich Longitude</em>' containment reference.
     * @see #setGreenwichLongitude(AngleChoiceType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_GreenwichLongitude()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='greenwichLongitude' namespace='##targetNamespace'"
     * @generated
     */
    AngleChoiceType getGreenwichLongitude();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getGreenwichLongitude <em>Greenwich Longitude</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Greenwich Longitude</em>' containment reference.
     * @see #getGreenwichLongitude()
     * @generated
     */
    void setGreenwichLongitude(AngleChoiceType value);

    /**
     * Returns the value of the '<em><b>Grid</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Grid</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Grid</em>' containment reference.
     * @see #setGrid(GridType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Grid()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Grid' namespace='##targetNamespace' affiliation='_ImplicitGeometry'"
     * @generated
     */
    GridType getGrid();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getGrid <em>Grid</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Grid</em>' containment reference.
     * @see #getGrid()
     * @generated
     */
    void setGrid(GridType value);

    /**
     * Returns the value of the '<em><b>Grid Coverage</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Grid Coverage</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Grid Coverage</em>' containment reference.
     * @see #setGridCoverage(GridCoverageType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_GridCoverage()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='GridCoverage' namespace='##targetNamespace' affiliation='_DiscreteCoverage'"
     * @generated
     */
    GridCoverageType getGridCoverage();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getGridCoverage <em>Grid Coverage</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Grid Coverage</em>' containment reference.
     * @see #getGridCoverage()
     * @generated
     */
    void setGridCoverage(GridCoverageType value);

    /**
     * Returns the value of the '<em><b>Grid Domain</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Grid Domain</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Grid Domain</em>' containment reference.
     * @see #setGridDomain(GridDomainType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_GridDomain()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='gridDomain' namespace='##targetNamespace' affiliation='domainSet'"
     * @generated
     */
    GridDomainType getGridDomain();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getGridDomain <em>Grid Domain</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Grid Domain</em>' containment reference.
     * @see #getGridDomain()
     * @generated
     */
    void setGridDomain(GridDomainType value);

    /**
     * Returns the value of the '<em><b>Grid Function</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Grid Function</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Grid Function</em>' containment reference.
     * @see #setGridFunction(GridFunctionType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_GridFunction()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='GridFunction' namespace='##targetNamespace'"
     * @generated
     */
    GridFunctionType getGridFunction();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getGridFunction <em>Grid Function</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Grid Function</em>' containment reference.
     * @see #getGridFunction()
     * @generated
     */
    void setGridFunction(GridFunctionType value);

    /**
     * Returns the value of the '<em><b>Group ID</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * An identification of an operation parameter group. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Group ID</em>' containment reference.
     * @see #setGroupID(IdentifierType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_GroupID()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='groupID' namespace='##targetNamespace'"
     * @generated
     */
    IdentifierType getGroupID();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getGroupID <em>Group ID</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Group ID</em>' containment reference.
     * @see #getGroupID()
     * @generated
     */
    void setGroupID(IdentifierType value);

    /**
     * Returns the value of the '<em><b>Group Name</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The name by which this operation parameter group is identified. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Group Name</em>' containment reference.
     * @see #setGroupName(CodeType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_GroupName()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='groupName' namespace='##targetNamespace' affiliation='name'"
     * @generated
     */
    CodeType getGroupName();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getGroupName <em>Group Name</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Group Name</em>' containment reference.
     * @see #getGroupName()
     * @generated
     */
    void setGroupName(CodeType value);

    /**
     * Returns the value of the '<em><b>History</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>History</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>History</em>' containment reference.
     * @see #setHistory(HistoryPropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_History()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='history' namespace='##targetNamespace'"
     * @generated
     */
    HistoryPropertyType getHistory();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getHistory <em>History</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>History</em>' containment reference.
     * @see #getHistory()
     * @generated
     */
    void setHistory(HistoryPropertyType value);

    /**
     * Returns the value of the '<em><b>Image CRS</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Image CRS</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Image CRS</em>' containment reference.
     * @see #setImageCRS(ImageCRSType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_ImageCRS()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='ImageCRS' namespace='##targetNamespace' affiliation='_CoordinateReferenceSystem'"
     * @generated
     */
    ImageCRSType getImageCRS();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getImageCRS <em>Image CRS</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Image CRS</em>' containment reference.
     * @see #getImageCRS()
     * @generated
     */
    void setImageCRS(ImageCRSType value);

    /**
     * Returns the value of the '<em><b>Image CRS Ref</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Image CRS Ref</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Image CRS Ref</em>' containment reference.
     * @see #setImageCRSRef(ImageCRSRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_ImageCRSRef()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='imageCRSRef' namespace='##targetNamespace'"
     * @generated
     */
    ImageCRSRefType getImageCRSRef();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getImageCRSRef <em>Image CRS Ref</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Image CRS Ref</em>' containment reference.
     * @see #getImageCRSRef()
     * @generated
     */
    void setImageCRSRef(ImageCRSRefType value);

    /**
     * Returns the value of the '<em><b>Image Datum</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Image Datum</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Image Datum</em>' containment reference.
     * @see #setImageDatum(ImageDatumType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_ImageDatum()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='ImageDatum' namespace='##targetNamespace' affiliation='_Datum'"
     * @generated
     */
    ImageDatumType getImageDatum();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getImageDatum <em>Image Datum</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Image Datum</em>' containment reference.
     * @see #getImageDatum()
     * @generated
     */
    void setImageDatum(ImageDatumType value);

    /**
     * Returns the value of the '<em><b>Image Datum Ref</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Image Datum Ref</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Image Datum Ref</em>' containment reference.
     * @see #setImageDatumRef(ImageDatumRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_ImageDatumRef()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='imageDatumRef' namespace='##targetNamespace'"
     * @generated
     */
    ImageDatumRefType getImageDatumRef();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getImageDatumRef <em>Image Datum Ref</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Image Datum Ref</em>' containment reference.
     * @see #getImageDatumRef()
     * @generated
     */
    void setImageDatumRef(ImageDatumRefType value);

    /**
     * Returns the value of the '<em><b>Includes CRS</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * An association to a component coordinate reference system included in this compound coordinate reference system. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Includes CRS</em>' containment reference.
     * @see #setIncludesCRS(CoordinateReferenceSystemRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_IncludesCRS()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='includesCRS' namespace='##targetNamespace'"
     * @generated
     */
    CoordinateReferenceSystemRefType getIncludesCRS();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getIncludesCRS <em>Includes CRS</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Includes CRS</em>' containment reference.
     * @see #getIncludesCRS()
     * @generated
     */
    void setIncludesCRS(CoordinateReferenceSystemRefType value);

    /**
     * Returns the value of the '<em><b>Includes Element</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Includes Element</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Includes Element</em>' containment reference.
     * @see #setIncludesElement(CovarianceElementType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_IncludesElement()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='includesElement' namespace='##targetNamespace'"
     * @generated
     */
    CovarianceElementType getIncludesElement();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getIncludesElement <em>Includes Element</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Includes Element</em>' containment reference.
     * @see #getIncludesElement()
     * @generated
     */
    void setIncludesElement(CovarianceElementType value);

    /**
     * Returns the value of the '<em><b>Includes Parameter</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Association to an operation parameter that is a member of a group. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Includes Parameter</em>' containment reference.
     * @see #setIncludesParameter(AbstractGeneralOperationParameterRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_IncludesParameter()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='includesParameter' namespace='##targetNamespace'"
     * @generated
     */
    AbstractGeneralOperationParameterRefType getIncludesParameter();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getIncludesParameter <em>Includes Parameter</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Includes Parameter</em>' containment reference.
     * @see #getIncludesParameter()
     * @generated
     */
    void setIncludesParameter(AbstractGeneralOperationParameterRefType value);

    /**
     * Returns the value of the '<em><b>Includes Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * A composition association to a parameter value or group of values included in this group. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Includes Value</em>' containment reference.
     * @see #setIncludesValue(AbstractGeneralParameterValueType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_IncludesValue()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='includesValue' namespace='##targetNamespace' affiliation='_generalParameterValue'"
     * @generated
     */
    AbstractGeneralParameterValueType getIncludesValue();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getIncludesValue <em>Includes Value</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Includes Value</em>' containment reference.
     * @see #getIncludesValue()
     * @generated
     */
    void setIncludesValue(AbstractGeneralParameterValueType value);

    /**
     * Returns the value of the '<em><b>Index Map</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Index Map</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Index Map</em>' containment reference.
     * @see #setIndexMap(IndexMapType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_IndexMap()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='IndexMap' namespace='##targetNamespace' affiliation='GridFunction'"
     * @generated
     */
    IndexMapType getIndexMap();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getIndexMap <em>Index Map</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Index Map</em>' containment reference.
     * @see #getIndexMap()
     * @generated
     */
    void setIndexMap(IndexMapType value);

    /**
     * Returns the value of the '<em><b>Indirect Entry</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Indirect Entry</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Indirect Entry</em>' containment reference.
     * @see #setIndirectEntry(IndirectEntryType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_IndirectEntry()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='indirectEntry' namespace='##targetNamespace'"
     * @generated
     */
    IndirectEntryType getIndirectEntry();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getIndirectEntry <em>Indirect Entry</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Indirect Entry</em>' containment reference.
     * @see #getIndirectEntry()
     * @generated
     */
    void setIndirectEntry(IndirectEntryType value);

    /**
     * Returns the value of the '<em><b>Inner Boundary Is</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Deprecated with GML 3.0, included only for backwards compatibility with GML 2. Use "interior" instead.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Inner Boundary Is</em>' containment reference.
     * @see #setInnerBoundaryIs(AbstractRingPropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_InnerBoundaryIs()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='innerBoundaryIs' namespace='##targetNamespace' affiliation='interior'"
     * @generated
     */
    AbstractRingPropertyType getInnerBoundaryIs();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getInnerBoundaryIs <em>Inner Boundary Is</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Inner Boundary Is</em>' containment reference.
     * @see #getInnerBoundaryIs()
     * @generated
     */
    void setInnerBoundaryIs(AbstractRingPropertyType value);

    /**
     * Returns the value of the '<em><b>Interior</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * A boundary of a surface consists of a number of rings. The "interior" rings seperate the surface / surface patch from the area enclosed by the rings.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Interior</em>' containment reference.
     * @see #setInterior(AbstractRingPropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Interior()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='interior' namespace='##targetNamespace'"
     * @generated
     */
    AbstractRingPropertyType getInterior();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getInterior <em>Interior</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Interior</em>' containment reference.
     * @see #getInterior()
     * @generated
     */
    void setInterior(AbstractRingPropertyType value);

    /**
     * Returns the value of the '<em><b>Integer Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Positive integer value of an operation parameter, usually used for a count. An integer value does not have an associated unit of measure. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Integer Value</em>' attribute.
     * @see #setIntegerValue(BigInteger)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_IntegerValue()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.PositiveInteger" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='integerValue' namespace='##targetNamespace'"
     * @generated
     */
    BigInteger getIntegerValue();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getIntegerValue <em>Integer Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Integer Value</em>' attribute.
     * @see #getIntegerValue()
     * @generated
     */
    void setIntegerValue(BigInteger value);

    /**
     * Returns the value of the '<em><b>Integer Value List</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Ordered sequence of two or more integer values of an operation parameter list, usually used for counts. These integer values do not have an associated unit of measure. An element of this type contains a space-separated sequence of integer values. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Integer Value List</em>' attribute.
     * @see #setIntegerValueList(List)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_IntegerValueList()
     * @model unique="false" dataType="net.opengis.gml311.IntegerList" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='integerValueList' namespace='##targetNamespace'"
     * @generated
     */
    List<BigInteger> getIntegerValueList();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getIntegerValueList <em>Integer Value List</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Integer Value List</em>' attribute.
     * @see #getIntegerValueList()
     * @generated
     */
    void setIntegerValueList(List<BigInteger> value);

    /**
     * Returns the value of the '<em><b>Inverse Flattening</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Inverse flattening value of the ellipsoid. Value is a scale factor (or ratio) that has no physical unit. Uses the MeasureType with the restriction that the unit of measure referenced by uom must be suitable for a scale factor, such as percent, permil, or parts-per-million. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Inverse Flattening</em>' containment reference.
     * @see #setInverseFlattening(MeasureType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_InverseFlattening()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='inverseFlattening' namespace='##targetNamespace'"
     * @generated
     */
    MeasureType getInverseFlattening();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getInverseFlattening <em>Inverse Flattening</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Inverse Flattening</em>' containment reference.
     * @see #getInverseFlattening()
     * @generated
     */
    void setInverseFlattening(MeasureType value);

    /**
     * Returns the value of the '<em><b>Isolated</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Isolated</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Isolated</em>' containment reference.
     * @see #setIsolated(IsolatedPropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Isolated()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='isolated' namespace='##targetNamespace'"
     * @generated
     */
    IsolatedPropertyType getIsolated();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getIsolated <em>Isolated</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Isolated</em>' containment reference.
     * @see #getIsolated()
     * @generated
     */
    void setIsolated(IsolatedPropertyType value);

    /**
     * Returns the value of the '<em><b>Is Sphere</b></em>' attribute.
     * The literals are from the enumeration {@link net.opengis.gml311.IsSphereType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The ellipsoid is degenerate and is actually a sphere. The sphere is completely defined by the semi-major axis, which is the radius of the sphere. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Is Sphere</em>' attribute.
     * @see net.opengis.gml311.IsSphereType
     * @see #setIsSphere(IsSphereType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_IsSphere()
     * @model unique="false" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='isSphere' namespace='##targetNamespace'"
     * @generated
     */
    IsSphereType getIsSphere();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getIsSphere <em>Is Sphere</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Is Sphere</em>' attribute.
     * @see net.opengis.gml311.IsSphereType
     * @see #getIsSphere()
     * @generated
     */
    void setIsSphere(IsSphereType value);

    /**
     * Returns the value of the '<em><b>Label Style</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Label Style</em>' containment reference.
     * @see #setLabelStyle(LabelStylePropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_LabelStyle()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='labelStyle' namespace='##targetNamespace'"
     * @generated
     */
    LabelStylePropertyType getLabelStyle();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getLabelStyle <em>Label Style</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Label Style</em>' containment reference.
     * @see #getLabelStyle()
     * @generated
     */
    void setLabelStyle(LabelStylePropertyType value);

    /**
     * Returns the value of the '<em><b>Label Style1</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The style descriptor for labels of a feature, geometry or topology.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Label Style1</em>' containment reference.
     * @see #setLabelStyle1(LabelStyleType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_LabelStyle1()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='LabelStyle' namespace='##targetNamespace' affiliation='_GML'"
     * @generated
     */
    LabelStyleType getLabelStyle1();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getLabelStyle1 <em>Label Style1</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Label Style1</em>' containment reference.
     * @see #getLabelStyle1()
     * @generated
     */
    void setLabelStyle1(LabelStyleType value);

    /**
     * Returns the value of the '<em><b>Linear CS</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Linear CS</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Linear CS</em>' containment reference.
     * @see #setLinearCS(LinearCSType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_LinearCS()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='LinearCS' namespace='##targetNamespace' affiliation='_CoordinateSystem'"
     * @generated
     */
    LinearCSType getLinearCS();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getLinearCS <em>Linear CS</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Linear CS</em>' containment reference.
     * @see #getLinearCS()
     * @generated
     */
    void setLinearCS(LinearCSType value);

    /**
     * Returns the value of the '<em><b>Linear CS Ref</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Linear CS Ref</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Linear CS Ref</em>' containment reference.
     * @see #setLinearCSRef(LinearCSRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_LinearCSRef()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='linearCSRef' namespace='##targetNamespace'"
     * @generated
     */
    LinearCSRefType getLinearCSRef();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getLinearCSRef <em>Linear CS Ref</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Linear CS Ref</em>' containment reference.
     * @see #getLinearCSRef()
     * @generated
     */
    void setLinearCSRef(LinearCSRefType value);

    /**
     * Returns the value of the '<em><b>Linear Ring</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Linear Ring</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Linear Ring</em>' containment reference.
     * @see #setLinearRing(LinearRingType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_LinearRing()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='LinearRing' namespace='##targetNamespace' affiliation='_Ring'"
     * @generated
     */
    LinearRingType getLinearRing();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getLinearRing <em>Linear Ring</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Linear Ring</em>' containment reference.
     * @see #getLinearRing()
     * @generated
     */
    void setLinearRing(LinearRingType value);

    /**
     * Returns the value of the '<em><b>Line String</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Line String</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Line String</em>' containment reference.
     * @see #setLineString(LineStringType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_LineString()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='LineString' namespace='##targetNamespace' affiliation='_Curve'"
     * @generated
     */
    LineStringType getLineString();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getLineString <em>Line String</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Line String</em>' containment reference.
     * @see #getLineString()
     * @generated
     */
    void setLineString(LineStringType value);

    /**
     * Returns the value of the '<em><b>Line String Member</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Deprecated with GML 3.0 and included only for backwards compatibility with GML 2.0. Use "curveMember" instead.
     * This property element either references a line string via the XLink-attributes or contains the line string element.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Line String Member</em>' containment reference.
     * @see #setLineStringMember(LineStringPropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_LineStringMember()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='lineStringMember' namespace='##targetNamespace'"
     * @generated
     */
    LineStringPropertyType getLineStringMember();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getLineStringMember <em>Line String Member</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Line String Member</em>' containment reference.
     * @see #getLineStringMember()
     * @generated
     */
    void setLineStringMember(LineStringPropertyType value);

    /**
     * Returns the value of the '<em><b>Line String Property</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Deprecated with GML 3.0 and included only for backwards compatibility with GML 2.0. Use "curveProperty" instead. This 
     * 			property element either references a line string via the XLink-attributes or contains the line string element.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Line String Property</em>' containment reference.
     * @see #setLineStringProperty(LineStringPropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_LineStringProperty()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='lineStringProperty' namespace='##targetNamespace'"
     * @generated
     */
    LineStringPropertyType getLineStringProperty();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getLineStringProperty <em>Line String Property</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Line String Property</em>' containment reference.
     * @see #getLineStringProperty()
     * @generated
     */
    void setLineStringProperty(LineStringPropertyType value);

    /**
     * Returns the value of the '<em><b>Line String Segment</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Line String Segment</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Line String Segment</em>' containment reference.
     * @see #setLineStringSegment(LineStringSegmentType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_LineStringSegment()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='LineStringSegment' namespace='##targetNamespace' affiliation='_CurveSegment'"
     * @generated
     */
    LineStringSegmentType getLineStringSegment();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getLineStringSegment <em>Line String Segment</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Line String Segment</em>' containment reference.
     * @see #getLineStringSegment()
     * @generated
     */
    void setLineStringSegment(LineStringSegmentType value);

    /**
     * Returns the value of the '<em><b>Location</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Deprecated in GML 3.1.0
     * <!-- end-model-doc -->
     * @return the value of the '<em>Location</em>' containment reference.
     * @see #setLocation(LocationPropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Location()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='location' namespace='##targetNamespace'"
     * @generated
     */
    LocationPropertyType getLocation();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getLocation <em>Location</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Location</em>' containment reference.
     * @see #getLocation()
     * @generated
     */
    void setLocation(LocationPropertyType value);

    /**
     * Returns the value of the '<em><b>Location Key Word</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Location Key Word</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Location Key Word</em>' containment reference.
     * @see #setLocationKeyWord(CodeType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_LocationKeyWord()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='LocationKeyWord' namespace='##targetNamespace'"
     * @generated
     */
    CodeType getLocationKeyWord();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getLocationKeyWord <em>Location Key Word</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Location Key Word</em>' containment reference.
     * @see #getLocationKeyWord()
     * @generated
     */
    void setLocationKeyWord(CodeType value);

    /**
     * Returns the value of the '<em><b>Location String</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Location String</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Location String</em>' containment reference.
     * @see #setLocationString(StringOrRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_LocationString()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='LocationString' namespace='##targetNamespace'"
     * @generated
     */
    StringOrRefType getLocationString();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getLocationString <em>Location String</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Location String</em>' containment reference.
     * @see #getLocationString()
     * @generated
     */
    void setLocationString(StringOrRefType value);

    /**
     * Returns the value of the '<em><b>Mapping Rule</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Description of a rule for associating members from the domainSet with members of the rangeSet.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Mapping Rule</em>' containment reference.
     * @see #setMappingRule(StringOrRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_MappingRule()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='MappingRule' namespace='##targetNamespace'"
     * @generated
     */
    StringOrRefType getMappingRule();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getMappingRule <em>Mapping Rule</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Mapping Rule</em>' containment reference.
     * @see #getMappingRule()
     * @generated
     */
    void setMappingRule(StringOrRefType value);

    /**
     * Returns the value of the '<em><b>Maximal Complex</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Need schamatron test here that isMaximal attribute value is true
     * <!-- end-model-doc -->
     * @return the value of the '<em>Maximal Complex</em>' containment reference.
     * @see #setMaximalComplex(TopoComplexMemberType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_MaximalComplex()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='maximalComplex' namespace='##targetNamespace'"
     * @generated
     */
    TopoComplexMemberType getMaximalComplex();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getMaximalComplex <em>Maximal Complex</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Maximal Complex</em>' containment reference.
     * @see #getMaximalComplex()
     * @generated
     */
    void setMaximalComplex(TopoComplexMemberType value);

    /**
     * Returns the value of the '<em><b>Maximum Occurs</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The maximum number of times that values for this parameter group can be included. If this attribute is omitted, the maximum number is one. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Maximum Occurs</em>' attribute.
     * @see #setMaximumOccurs(BigInteger)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_MaximumOccurs()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.PositiveInteger" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='maximumOccurs' namespace='##targetNamespace'"
     * @generated
     */
    BigInteger getMaximumOccurs();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getMaximumOccurs <em>Maximum Occurs</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Maximum Occurs</em>' attribute.
     * @see #getMaximumOccurs()
     * @generated
     */
    void setMaximumOccurs(BigInteger value);

    /**
     * Returns the value of the '<em><b>Measure</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Measure</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Measure</em>' containment reference.
     * @see #setMeasure(MeasureType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Measure()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='measure' namespace='##targetNamespace'"
     * @generated
     */
    MeasureType getMeasure();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getMeasure <em>Measure</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Measure</em>' containment reference.
     * @see #getMeasure()
     * @generated
     */
    void setMeasure(MeasureType value);

    /**
     * Returns the value of the '<em><b>Measure Description</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * A description of the position accuracy parameter(s) provided. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Measure Description</em>' containment reference.
     * @see #setMeasureDescription(CodeType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_MeasureDescription()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='measureDescription' namespace='##targetNamespace'"
     * @generated
     */
    CodeType getMeasureDescription();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getMeasureDescription <em>Measure Description</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Measure Description</em>' containment reference.
     * @see #getMeasureDescription()
     * @generated
     */
    void setMeasureDescription(CodeType value);

    /**
     * Returns the value of the '<em><b>Member</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Member</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Member</em>' containment reference.
     * @see #setMember(AssociationType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Member()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='member' namespace='##targetNamespace'"
     * @generated
     */
    AssociationType getMember();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getMember <em>Member</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Member</em>' containment reference.
     * @see #getMember()
     * @generated
     */
    void setMember(AssociationType value);

    /**
     * Returns the value of the '<em><b>Members</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Members</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Members</em>' containment reference.
     * @see #setMembers(ArrayAssociationType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Members()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='members' namespace='##targetNamespace'"
     * @generated
     */
    ArrayAssociationType getMembers();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getMembers <em>Members</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Members</em>' containment reference.
     * @see #getMembers()
     * @generated
     */
    void setMembers(ArrayAssociationType value);

    /**
     * Returns the value of the '<em><b>Meridian ID</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * An identification of a prime meridian. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Meridian ID</em>' containment reference.
     * @see #setMeridianID(IdentifierType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_MeridianID()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='meridianID' namespace='##targetNamespace'"
     * @generated
     */
    IdentifierType getMeridianID();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getMeridianID <em>Meridian ID</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Meridian ID</em>' containment reference.
     * @see #getMeridianID()
     * @generated
     */
    void setMeridianID(IdentifierType value);

    /**
     * Returns the value of the '<em><b>Meridian Name</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The name by which this prime meridian is identified. The meridianName most common value is Greenwich, and that value shall be used when the greenwichLongitude value is zero. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Meridian Name</em>' containment reference.
     * @see #setMeridianName(CodeType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_MeridianName()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='meridianName' namespace='##targetNamespace' affiliation='name'"
     * @generated
     */
    CodeType getMeridianName();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getMeridianName <em>Meridian Name</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Meridian Name</em>' containment reference.
     * @see #getMeridianName()
     * @generated
     */
    void setMeridianName(CodeType value);

    /**
     * Returns the value of the '<em><b>Meta Data Property</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Contains or refers to a metadata package that contains metadata properties.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Meta Data Property</em>' containment reference.
     * @see #setMetaDataProperty(MetaDataPropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_MetaDataProperty()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='metaDataProperty' namespace='##targetNamespace'"
     * @generated
     */
    MetaDataPropertyType getMetaDataProperty();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getMetaDataProperty <em>Meta Data Property</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Meta Data Property</em>' containment reference.
     * @see #getMetaDataProperty()
     * @generated
     */
    void setMetaDataProperty(MetaDataPropertyType value);

    /**
     * Returns the value of the '<em><b>Method Formula</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Formula(s) used by this operation method. The value may be a reference to a publication. Note that the operation method may not be analytic, in which case this element references or contains the procedure, not an analytic formula.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Method Formula</em>' containment reference.
     * @see #setMethodFormula(CodeType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_MethodFormula()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='methodFormula' namespace='##targetNamespace'"
     * @generated
     */
    CodeType getMethodFormula();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getMethodFormula <em>Method Formula</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Method Formula</em>' containment reference.
     * @see #getMethodFormula()
     * @generated
     */
    void setMethodFormula(CodeType value);

    /**
     * Returns the value of the '<em><b>Method ID</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * An identification of an operation method. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Method ID</em>' containment reference.
     * @see #setMethodID(IdentifierType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_MethodID()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='methodID' namespace='##targetNamespace'"
     * @generated
     */
    IdentifierType getMethodID();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getMethodID <em>Method ID</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Method ID</em>' containment reference.
     * @see #getMethodID()
     * @generated
     */
    void setMethodID(IdentifierType value);

    /**
     * Returns the value of the '<em><b>Method Name</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The name by which this operation method is identified. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Method Name</em>' containment reference.
     * @see #setMethodName(CodeType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_MethodName()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='methodName' namespace='##targetNamespace' affiliation='name'"
     * @generated
     */
    CodeType getMethodName();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getMethodName <em>Method Name</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Method Name</em>' containment reference.
     * @see #getMethodName()
     * @generated
     */
    void setMethodName(CodeType value);

    /**
     * Returns the value of the '<em><b>Minimum Occurs</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The minimum number of times that values for this parameter group or parameter are required. If this attribute is omitted, the minimum number is one. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Minimum Occurs</em>' attribute.
     * @see #setMinimumOccurs(BigInteger)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_MinimumOccurs()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.NonNegativeInteger" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='minimumOccurs' namespace='##targetNamespace'"
     * @generated
     */
    BigInteger getMinimumOccurs();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getMinimumOccurs <em>Minimum Occurs</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Minimum Occurs</em>' attribute.
     * @see #getMinimumOccurs()
     * @generated
     */
    void setMinimumOccurs(BigInteger value);

    /**
     * Returns the value of the '<em><b>Minutes</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Minutes</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Minutes</em>' attribute.
     * @see #setMinutes(BigInteger)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Minutes()
     * @model unique="false" dataType="net.opengis.gml311.ArcMinutesType" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='minutes' namespace='##targetNamespace'"
     * @generated
     */
    BigInteger getMinutes();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getMinutes <em>Minutes</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Minutes</em>' attribute.
     * @see #getMinutes()
     * @generated
     */
    void setMinutes(BigInteger value);

    /**
     * Returns the value of the '<em><b>Modified Coordinate</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * A positive integer defining a position in a coordinate tuple. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Modified Coordinate</em>' attribute.
     * @see #setModifiedCoordinate(BigInteger)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_ModifiedCoordinate()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.PositiveInteger" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='modifiedCoordinate' namespace='##targetNamespace'"
     * @generated
     */
    BigInteger getModifiedCoordinate();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getModifiedCoordinate <em>Modified Coordinate</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Modified Coordinate</em>' attribute.
     * @see #getModifiedCoordinate()
     * @generated
     */
    void setModifiedCoordinate(BigInteger value);

    /**
     * Returns the value of the '<em><b>Moving Object Status</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Moving Object Status</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Moving Object Status</em>' containment reference.
     * @see #setMovingObjectStatus(MovingObjectStatusType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_MovingObjectStatus()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='MovingObjectStatus' namespace='##targetNamespace' affiliation='_TimeSlice'"
     * @generated
     */
    MovingObjectStatusType getMovingObjectStatus();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getMovingObjectStatus <em>Moving Object Status</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Moving Object Status</em>' containment reference.
     * @see #getMovingObjectStatus()
     * @generated
     */
    void setMovingObjectStatus(MovingObjectStatusType value);

    /**
     * Returns the value of the '<em><b>Multi Center Line Of</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Multi Center Line Of</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Multi Center Line Of</em>' containment reference.
     * @see #setMultiCenterLineOf(MultiCurvePropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_MultiCenterLineOf()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='multiCenterLineOf' namespace='##targetNamespace'"
     * @generated
     */
    MultiCurvePropertyType getMultiCenterLineOf();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getMultiCenterLineOf <em>Multi Center Line Of</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Multi Center Line Of</em>' containment reference.
     * @see #getMultiCenterLineOf()
     * @generated
     */
    void setMultiCenterLineOf(MultiCurvePropertyType value);

    /**
     * Returns the value of the '<em><b>Multi Center Of</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Multi Center Of</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Multi Center Of</em>' containment reference.
     * @see #setMultiCenterOf(MultiPointPropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_MultiCenterOf()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='multiCenterOf' namespace='##targetNamespace'"
     * @generated
     */
    MultiPointPropertyType getMultiCenterOf();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getMultiCenterOf <em>Multi Center Of</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Multi Center Of</em>' containment reference.
     * @see #getMultiCenterOf()
     * @generated
     */
    void setMultiCenterOf(MultiPointPropertyType value);

    /**
     * Returns the value of the '<em><b>Multi Coverage</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Multi Coverage</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Multi Coverage</em>' containment reference.
     * @see #setMultiCoverage(MultiSurfacePropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_MultiCoverage()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='multiCoverage' namespace='##targetNamespace'"
     * @generated
     */
    MultiSurfacePropertyType getMultiCoverage();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getMultiCoverage <em>Multi Coverage</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Multi Coverage</em>' containment reference.
     * @see #getMultiCoverage()
     * @generated
     */
    void setMultiCoverage(MultiSurfacePropertyType value);

    /**
     * Returns the value of the '<em><b>Multi Curve</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Multi Curve</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Multi Curve</em>' containment reference.
     * @see #setMultiCurve(MultiCurveType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_MultiCurve()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='MultiCurve' namespace='##targetNamespace' affiliation='_GeometricAggregate'"
     * @generated
     */
    MultiCurveType getMultiCurve();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getMultiCurve <em>Multi Curve</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Multi Curve</em>' containment reference.
     * @see #getMultiCurve()
     * @generated
     */
    void setMultiCurve(MultiCurveType value);

    /**
     * Returns the value of the '<em><b>Multi Curve Coverage</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Multi Curve Coverage</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Multi Curve Coverage</em>' containment reference.
     * @see #setMultiCurveCoverage(MultiCurveCoverageType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_MultiCurveCoverage()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='MultiCurveCoverage' namespace='##targetNamespace' affiliation='_DiscreteCoverage'"
     * @generated
     */
    MultiCurveCoverageType getMultiCurveCoverage();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getMultiCurveCoverage <em>Multi Curve Coverage</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Multi Curve Coverage</em>' containment reference.
     * @see #getMultiCurveCoverage()
     * @generated
     */
    void setMultiCurveCoverage(MultiCurveCoverageType value);

    /**
     * Returns the value of the '<em><b>Multi Curve Domain</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Multi Curve Domain</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Multi Curve Domain</em>' containment reference.
     * @see #setMultiCurveDomain(MultiCurveDomainType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_MultiCurveDomain()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='multiCurveDomain' namespace='##targetNamespace' affiliation='domainSet'"
     * @generated
     */
    MultiCurveDomainType getMultiCurveDomain();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getMultiCurveDomain <em>Multi Curve Domain</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Multi Curve Domain</em>' containment reference.
     * @see #getMultiCurveDomain()
     * @generated
     */
    void setMultiCurveDomain(MultiCurveDomainType value);

    /**
     * Returns the value of the '<em><b>Multi Curve Property</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This property element either references a curve aggregate via the XLink-attributes or contains the "multi curve" element. multiCurveProperty is the predefined property which can be used by GML Application Schemas whenever a GML Feature has a property with a value that is substitutable for MultiCurve.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Multi Curve Property</em>' containment reference.
     * @see #setMultiCurveProperty(MultiCurvePropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_MultiCurveProperty()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='multiCurveProperty' namespace='##targetNamespace'"
     * @generated
     */
    MultiCurvePropertyType getMultiCurveProperty();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getMultiCurveProperty <em>Multi Curve Property</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Multi Curve Property</em>' containment reference.
     * @see #getMultiCurveProperty()
     * @generated
     */
    void setMultiCurveProperty(MultiCurvePropertyType value);

    /**
     * Returns the value of the '<em><b>Multi Edge Of</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Multi Edge Of</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Multi Edge Of</em>' containment reference.
     * @see #setMultiEdgeOf(MultiCurvePropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_MultiEdgeOf()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='multiEdgeOf' namespace='##targetNamespace'"
     * @generated
     */
    MultiCurvePropertyType getMultiEdgeOf();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getMultiEdgeOf <em>Multi Edge Of</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Multi Edge Of</em>' containment reference.
     * @see #getMultiEdgeOf()
     * @generated
     */
    void setMultiEdgeOf(MultiCurvePropertyType value);

    /**
     * Returns the value of the '<em><b>Multi Extent Of</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Multi Extent Of</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Multi Extent Of</em>' containment reference.
     * @see #setMultiExtentOf(MultiSurfacePropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_MultiExtentOf()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='multiExtentOf' namespace='##targetNamespace'"
     * @generated
     */
    MultiSurfacePropertyType getMultiExtentOf();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getMultiExtentOf <em>Multi Extent Of</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Multi Extent Of</em>' containment reference.
     * @see #getMultiExtentOf()
     * @generated
     */
    void setMultiExtentOf(MultiSurfacePropertyType value);

    /**
     * Returns the value of the '<em><b>Multi Geometry</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Multi Geometry</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Multi Geometry</em>' containment reference.
     * @see #setMultiGeometry(MultiGeometryType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_MultiGeometry()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='MultiGeometry' namespace='##targetNamespace' affiliation='_GeometricAggregate'"
     * @generated
     */
    MultiGeometryType getMultiGeometry();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getMultiGeometry <em>Multi Geometry</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Multi Geometry</em>' containment reference.
     * @see #getMultiGeometry()
     * @generated
     */
    void setMultiGeometry(MultiGeometryType value);

    /**
     * Returns the value of the '<em><b>Multi Geometry Property</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This property element either references a geometric aggregate via the XLink-attributes or contains the "multi geometry" element. multiGeometryProperty is the predefined property which can be used by GML Application Schemas whenever a GML Feature has a property with a value that is substitutable for _GeometricAggregate.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Multi Geometry Property</em>' containment reference.
     * @see #setMultiGeometryProperty(MultiGeometryPropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_MultiGeometryProperty()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='multiGeometryProperty' namespace='##targetNamespace'"
     * @generated
     */
    MultiGeometryPropertyType getMultiGeometryProperty();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getMultiGeometryProperty <em>Multi Geometry Property</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Multi Geometry Property</em>' containment reference.
     * @see #getMultiGeometryProperty()
     * @generated
     */
    void setMultiGeometryProperty(MultiGeometryPropertyType value);

    /**
     * Returns the value of the '<em><b>Multi Line String</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Deprecated with GML 3.0 and included for backwards compatibility with GML 2. Use the "MultiCurve" element instead.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Multi Line String</em>' containment reference.
     * @see #setMultiLineString(MultiLineStringType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_MultiLineString()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='MultiLineString' namespace='##targetNamespace' affiliation='_GeometricAggregate'"
     * @generated
     */
    MultiLineStringType getMultiLineString();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getMultiLineString <em>Multi Line String</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Multi Line String</em>' containment reference.
     * @see #getMultiLineString()
     * @generated
     */
    void setMultiLineString(MultiLineStringType value);

    /**
     * Returns the value of the '<em><b>Multi Location</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Deprecated with GML 3.0 and included only for backwards compatibility with GML 2.0. Use "curveMember" instead.
     * This property element either references a line string via the XLink-attributes or contains the line string element.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Multi Location</em>' containment reference.
     * @see #setMultiLocation(MultiPointPropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_MultiLocation()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='multiLocation' namespace='##targetNamespace'"
     * @generated
     */
    MultiPointPropertyType getMultiLocation();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getMultiLocation <em>Multi Location</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Multi Location</em>' containment reference.
     * @see #getMultiLocation()
     * @generated
     */
    void setMultiLocation(MultiPointPropertyType value);

    /**
     * Returns the value of the '<em><b>Multi Point</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Multi Point</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Multi Point</em>' containment reference.
     * @see #setMultiPoint(MultiPointType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_MultiPoint()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='MultiPoint' namespace='##targetNamespace' affiliation='_GeometricAggregate'"
     * @generated
     */
    MultiPointType getMultiPoint();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getMultiPoint <em>Multi Point</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Multi Point</em>' containment reference.
     * @see #getMultiPoint()
     * @generated
     */
    void setMultiPoint(MultiPointType value);

    /**
     * Returns the value of the '<em><b>Multi Point Coverage</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Multi Point Coverage</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Multi Point Coverage</em>' containment reference.
     * @see #setMultiPointCoverage(MultiPointCoverageType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_MultiPointCoverage()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='MultiPointCoverage' namespace='##targetNamespace' affiliation='_DiscreteCoverage'"
     * @generated
     */
    MultiPointCoverageType getMultiPointCoverage();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getMultiPointCoverage <em>Multi Point Coverage</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Multi Point Coverage</em>' containment reference.
     * @see #getMultiPointCoverage()
     * @generated
     */
    void setMultiPointCoverage(MultiPointCoverageType value);

    /**
     * Returns the value of the '<em><b>Multi Point Domain</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Multi Point Domain</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Multi Point Domain</em>' containment reference.
     * @see #setMultiPointDomain(MultiPointDomainType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_MultiPointDomain()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='multiPointDomain' namespace='##targetNamespace' affiliation='domainSet'"
     * @generated
     */
    MultiPointDomainType getMultiPointDomain();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getMultiPointDomain <em>Multi Point Domain</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Multi Point Domain</em>' containment reference.
     * @see #getMultiPointDomain()
     * @generated
     */
    void setMultiPointDomain(MultiPointDomainType value);

    /**
     * Returns the value of the '<em><b>Multi Point Property</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This property element either references a point aggregate via the XLink-attributes or contains the "multi point" element. multiPointProperty is the predefined property which can be used by GML Application Schemas whenever a GML Feature has a property with a value that is substitutable for MultiPoint.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Multi Point Property</em>' containment reference.
     * @see #setMultiPointProperty(MultiPointPropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_MultiPointProperty()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='multiPointProperty' namespace='##targetNamespace'"
     * @generated
     */
    MultiPointPropertyType getMultiPointProperty();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getMultiPointProperty <em>Multi Point Property</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Multi Point Property</em>' containment reference.
     * @see #getMultiPointProperty()
     * @generated
     */
    void setMultiPointProperty(MultiPointPropertyType value);

    /**
     * Returns the value of the '<em><b>Multi Polygon</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Deprecated with GML 3.0 and included for backwards compatibility with GML 2. Use the "MultiSurface" element instead.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Multi Polygon</em>' containment reference.
     * @see #setMultiPolygon(MultiPolygonType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_MultiPolygon()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='MultiPolygon' namespace='##targetNamespace' affiliation='_GeometricAggregate'"
     * @generated
     */
    MultiPolygonType getMultiPolygon();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getMultiPolygon <em>Multi Polygon</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Multi Polygon</em>' containment reference.
     * @see #getMultiPolygon()
     * @generated
     */
    void setMultiPolygon(MultiPolygonType value);

    /**
     * Returns the value of the '<em><b>Multi Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Multi Position</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Multi Position</em>' containment reference.
     * @see #setMultiPosition(MultiPointPropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_MultiPosition()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='multiPosition' namespace='##targetNamespace'"
     * @generated
     */
    MultiPointPropertyType getMultiPosition();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getMultiPosition <em>Multi Position</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Multi Position</em>' containment reference.
     * @see #getMultiPosition()
     * @generated
     */
    void setMultiPosition(MultiPointPropertyType value);

    /**
     * Returns the value of the '<em><b>Multi Solid</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Multi Solid</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Multi Solid</em>' containment reference.
     * @see #setMultiSolid(MultiSolidType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_MultiSolid()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='MultiSolid' namespace='##targetNamespace' affiliation='_GeometricAggregate'"
     * @generated
     */
    MultiSolidType getMultiSolid();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getMultiSolid <em>Multi Solid</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Multi Solid</em>' containment reference.
     * @see #getMultiSolid()
     * @generated
     */
    void setMultiSolid(MultiSolidType value);

    /**
     * Returns the value of the '<em><b>Multi Solid Coverage</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Multi Solid Coverage</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Multi Solid Coverage</em>' containment reference.
     * @see #setMultiSolidCoverage(MultiSolidCoverageType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_MultiSolidCoverage()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='MultiSolidCoverage' namespace='##targetNamespace' affiliation='_DiscreteCoverage'"
     * @generated
     */
    MultiSolidCoverageType getMultiSolidCoverage();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getMultiSolidCoverage <em>Multi Solid Coverage</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Multi Solid Coverage</em>' containment reference.
     * @see #getMultiSolidCoverage()
     * @generated
     */
    void setMultiSolidCoverage(MultiSolidCoverageType value);

    /**
     * Returns the value of the '<em><b>Multi Solid Domain</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Multi Solid Domain</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Multi Solid Domain</em>' containment reference.
     * @see #setMultiSolidDomain(MultiSolidDomainType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_MultiSolidDomain()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='multiSolidDomain' namespace='##targetNamespace' affiliation='domainSet'"
     * @generated
     */
    MultiSolidDomainType getMultiSolidDomain();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getMultiSolidDomain <em>Multi Solid Domain</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Multi Solid Domain</em>' containment reference.
     * @see #getMultiSolidDomain()
     * @generated
     */
    void setMultiSolidDomain(MultiSolidDomainType value);

    /**
     * Returns the value of the '<em><b>Multi Solid Property</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This property element either references a solid aggregate via the XLink-attributes or contains the "multi solid" element. multiSolidProperty is the predefined property which can be used by GML Application Schemas whenever a GML Feature has a property with a value that is substitutable for MultiSolid.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Multi Solid Property</em>' containment reference.
     * @see #setMultiSolidProperty(MultiSolidPropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_MultiSolidProperty()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='multiSolidProperty' namespace='##targetNamespace'"
     * @generated
     */
    MultiSolidPropertyType getMultiSolidProperty();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getMultiSolidProperty <em>Multi Solid Property</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Multi Solid Property</em>' containment reference.
     * @see #getMultiSolidProperty()
     * @generated
     */
    void setMultiSolidProperty(MultiSolidPropertyType value);

    /**
     * Returns the value of the '<em><b>Multi Surface</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Multi Surface</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Multi Surface</em>' containment reference.
     * @see #setMultiSurface(MultiSurfaceType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_MultiSurface()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='MultiSurface' namespace='##targetNamespace' affiliation='_GeometricAggregate'"
     * @generated
     */
    MultiSurfaceType getMultiSurface();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getMultiSurface <em>Multi Surface</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Multi Surface</em>' containment reference.
     * @see #getMultiSurface()
     * @generated
     */
    void setMultiSurface(MultiSurfaceType value);

    /**
     * Returns the value of the '<em><b>Multi Surface Coverage</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Multi Surface Coverage</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Multi Surface Coverage</em>' containment reference.
     * @see #setMultiSurfaceCoverage(MultiSurfaceCoverageType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_MultiSurfaceCoverage()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='MultiSurfaceCoverage' namespace='##targetNamespace' affiliation='_DiscreteCoverage'"
     * @generated
     */
    MultiSurfaceCoverageType getMultiSurfaceCoverage();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getMultiSurfaceCoverage <em>Multi Surface Coverage</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Multi Surface Coverage</em>' containment reference.
     * @see #getMultiSurfaceCoverage()
     * @generated
     */
    void setMultiSurfaceCoverage(MultiSurfaceCoverageType value);

    /**
     * Returns the value of the '<em><b>Multi Surface Domain</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Multi Surface Domain</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Multi Surface Domain</em>' containment reference.
     * @see #setMultiSurfaceDomain(MultiSurfaceDomainType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_MultiSurfaceDomain()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='multiSurfaceDomain' namespace='##targetNamespace' affiliation='domainSet'"
     * @generated
     */
    MultiSurfaceDomainType getMultiSurfaceDomain();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getMultiSurfaceDomain <em>Multi Surface Domain</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Multi Surface Domain</em>' containment reference.
     * @see #getMultiSurfaceDomain()
     * @generated
     */
    void setMultiSurfaceDomain(MultiSurfaceDomainType value);

    /**
     * Returns the value of the '<em><b>Multi Surface Property</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This property element either references a surface aggregate via the XLink-attributes or contains the "multi surface" element. multiSurfaceProperty is the predefined property which can be used by GML Application Schemas whenever a GML Feature has a property with a value that is substitutable for MultiSurface.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Multi Surface Property</em>' containment reference.
     * @see #setMultiSurfaceProperty(MultiSurfacePropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_MultiSurfaceProperty()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='multiSurfaceProperty' namespace='##targetNamespace'"
     * @generated
     */
    MultiSurfacePropertyType getMultiSurfaceProperty();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getMultiSurfaceProperty <em>Multi Surface Property</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Multi Surface Property</em>' containment reference.
     * @see #getMultiSurfaceProperty()
     * @generated
     */
    void setMultiSurfaceProperty(MultiSurfacePropertyType value);

    /**
     * Returns the value of the '<em><b>Node</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Node</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Node</em>' containment reference.
     * @see #setNode(NodeType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Node()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Node' namespace='##targetNamespace' affiliation='_TopoPrimitive'"
     * @generated
     */
    NodeType getNode();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getNode <em>Node</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Node</em>' containment reference.
     * @see #getNode()
     * @generated
     */
    void setNode(NodeType value);

    /**
     * Returns the value of the '<em><b>Null</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Null</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Null</em>' attribute.
     * @see #setNull(Object)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Null()
     * @model unique="false" dataType="net.opengis.gml311.NullType" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Null' namespace='##targetNamespace'"
     * @generated
     */
    Object getNull();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getNull <em>Null</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Null</em>' attribute.
     * @see #getNull()
     * @generated
     */
    void setNull(Object value);

    /**
     * Returns the value of the '<em><b>Oblique Cartesian CS</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Oblique Cartesian CS</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Oblique Cartesian CS</em>' containment reference.
     * @see #setObliqueCartesianCS(ObliqueCartesianCSType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_ObliqueCartesianCS()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='ObliqueCartesianCS' namespace='##targetNamespace' affiliation='_CoordinateSystem'"
     * @generated
     */
    ObliqueCartesianCSType getObliqueCartesianCS();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getObliqueCartesianCS <em>Oblique Cartesian CS</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Oblique Cartesian CS</em>' containment reference.
     * @see #getObliqueCartesianCS()
     * @generated
     */
    void setObliqueCartesianCS(ObliqueCartesianCSType value);

    /**
     * Returns the value of the '<em><b>Oblique Cartesian CS Ref</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Oblique Cartesian CS Ref</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Oblique Cartesian CS Ref</em>' containment reference.
     * @see #setObliqueCartesianCSRef(ObliqueCartesianCSRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_ObliqueCartesianCSRef()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='obliqueCartesianCSRef' namespace='##targetNamespace'"
     * @generated
     */
    ObliqueCartesianCSRefType getObliqueCartesianCSRef();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getObliqueCartesianCSRef <em>Oblique Cartesian CS Ref</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Oblique Cartesian CS Ref</em>' containment reference.
     * @see #getObliqueCartesianCSRef()
     * @generated
     */
    void setObliqueCartesianCSRef(ObliqueCartesianCSRefType value);

    /**
     * Returns the value of the '<em><b>Offset Curve</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Offset Curve</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Offset Curve</em>' containment reference.
     * @see #setOffsetCurve(OffsetCurveType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_OffsetCurve()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='OffsetCurve' namespace='##targetNamespace' affiliation='_CurveSegment'"
     * @generated
     */
    OffsetCurveType getOffsetCurve();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getOffsetCurve <em>Offset Curve</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Offset Curve</em>' containment reference.
     * @see #getOffsetCurve()
     * @generated
     */
    void setOffsetCurve(OffsetCurveType value);

    /**
     * Returns the value of the '<em><b>Operation Method</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Operation Method</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Operation Method</em>' containment reference.
     * @see #setOperationMethod(OperationMethodType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_OperationMethod()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='OperationMethod' namespace='##targetNamespace' affiliation='Definition'"
     * @generated
     */
    OperationMethodType getOperationMethod();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getOperationMethod <em>Operation Method</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Operation Method</em>' containment reference.
     * @see #getOperationMethod()
     * @generated
     */
    void setOperationMethod(OperationMethodType value);

    /**
     * Returns the value of the '<em><b>Operation Method Ref</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Operation Method Ref</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Operation Method Ref</em>' containment reference.
     * @see #setOperationMethodRef(OperationMethodRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_OperationMethodRef()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='operationMethodRef' namespace='##targetNamespace'"
     * @generated
     */
    OperationMethodRefType getOperationMethodRef();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getOperationMethodRef <em>Operation Method Ref</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Operation Method Ref</em>' containment reference.
     * @see #getOperationMethodRef()
     * @generated
     */
    void setOperationMethodRef(OperationMethodRefType value);

    /**
     * Returns the value of the '<em><b>Operation Parameter</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Operation Parameter</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Operation Parameter</em>' containment reference.
     * @see #setOperationParameter(OperationParameterType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_OperationParameter()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='OperationParameter' namespace='##targetNamespace' affiliation='_GeneralOperationParameter'"
     * @generated
     */
    OperationParameterType getOperationParameter();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getOperationParameter <em>Operation Parameter</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Operation Parameter</em>' containment reference.
     * @see #getOperationParameter()
     * @generated
     */
    void setOperationParameter(OperationParameterType value);

    /**
     * Returns the value of the '<em><b>Operation Parameter Group</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Operation Parameter Group</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Operation Parameter Group</em>' containment reference.
     * @see #setOperationParameterGroup(OperationParameterGroupType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_OperationParameterGroup()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='OperationParameterGroup' namespace='##targetNamespace' affiliation='_GeneralOperationParameter'"
     * @generated
     */
    OperationParameterGroupType getOperationParameterGroup();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getOperationParameterGroup <em>Operation Parameter Group</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Operation Parameter Group</em>' containment reference.
     * @see #getOperationParameterGroup()
     * @generated
     */
    void setOperationParameterGroup(OperationParameterGroupType value);

    /**
     * Returns the value of the '<em><b>Operation Parameter Group Ref</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Operation Parameter Group Ref</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Operation Parameter Group Ref</em>' containment reference.
     * @see #setOperationParameterGroupRef(OperationParameterRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_OperationParameterGroupRef()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='operationParameterGroupRef' namespace='##targetNamespace'"
     * @generated
     */
    OperationParameterRefType getOperationParameterGroupRef();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getOperationParameterGroupRef <em>Operation Parameter Group Ref</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Operation Parameter Group Ref</em>' containment reference.
     * @see #getOperationParameterGroupRef()
     * @generated
     */
    void setOperationParameterGroupRef(OperationParameterRefType value);

    /**
     * Returns the value of the '<em><b>Operation Parameter Ref</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Operation Parameter Ref</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Operation Parameter Ref</em>' containment reference.
     * @see #setOperationParameterRef(OperationParameterRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_OperationParameterRef()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='operationParameterRef' namespace='##targetNamespace'"
     * @generated
     */
    OperationParameterRefType getOperationParameterRef();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getOperationParameterRef <em>Operation Parameter Ref</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Operation Parameter Ref</em>' containment reference.
     * @see #getOperationParameterRef()
     * @generated
     */
    void setOperationParameterRef(OperationParameterRefType value);

    /**
     * Returns the value of the '<em><b>Operation Ref</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Operation Ref</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Operation Ref</em>' containment reference.
     * @see #setOperationRef(OperationRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_OperationRef()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='operationRef' namespace='##targetNamespace'"
     * @generated
     */
    OperationRefType getOperationRef();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getOperationRef <em>Operation Ref</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Operation Ref</em>' containment reference.
     * @see #getOperationRef()
     * @generated
     */
    void setOperationRef(OperationRefType value);

    /**
     * Returns the value of the '<em><b>Operation Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Version of the coordinate transformation (i.e., instantiation due to the stochastic nature of the parameters). Mandatory when describing a transformation, and should not be supplied for a conversion. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Operation Version</em>' attribute.
     * @see #setOperationVersion(String)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_OperationVersion()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='operationVersion' namespace='##targetNamespace'"
     * @generated
     */
    String getOperationVersion();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getOperationVersion <em>Operation Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Operation Version</em>' attribute.
     * @see #getOperationVersion()
     * @generated
     */
    void setOperationVersion(String value);

    /**
     * Returns the value of the '<em><b>Orientable Curve</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Orientable Curve</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Orientable Curve</em>' containment reference.
     * @see #setOrientableCurve(OrientableCurveType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_OrientableCurve()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='OrientableCurve' namespace='##targetNamespace' affiliation='_Curve'"
     * @generated
     */
    OrientableCurveType getOrientableCurve();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getOrientableCurve <em>Orientable Curve</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Orientable Curve</em>' containment reference.
     * @see #getOrientableCurve()
     * @generated
     */
    void setOrientableCurve(OrientableCurveType value);

    /**
     * Returns the value of the '<em><b>Orientable Surface</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Orientable Surface</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Orientable Surface</em>' containment reference.
     * @see #setOrientableSurface(OrientableSurfaceType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_OrientableSurface()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='OrientableSurface' namespace='##targetNamespace' affiliation='_Surface'"
     * @generated
     */
    OrientableSurfaceType getOrientableSurface();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getOrientableSurface <em>Orientable Surface</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Orientable Surface</em>' containment reference.
     * @see #getOrientableSurface()
     * @generated
     */
    void setOrientableSurface(OrientableSurfaceType value);

    /**
     * Returns the value of the '<em><b>Origin</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The date and time origin of this temporal datum. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Origin</em>' attribute.
     * @see #setOrigin(XMLGregorianCalendar)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Origin()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.DateTime" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='origin' namespace='##targetNamespace'"
     * @generated
     */
    XMLGregorianCalendar getOrigin();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getOrigin <em>Origin</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Origin</em>' attribute.
     * @see #getOrigin()
     * @generated
     */
    void setOrigin(XMLGregorianCalendar value);

    /**
     * Returns the value of the '<em><b>Outer Boundary Is</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Deprecated with GML 3.0, included only for backwards compatibility with GML 2. Use "exterior" instead.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Outer Boundary Is</em>' containment reference.
     * @see #setOuterBoundaryIs(AbstractRingPropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_OuterBoundaryIs()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='outerBoundaryIs' namespace='##targetNamespace' affiliation='exterior'"
     * @generated
     */
    AbstractRingPropertyType getOuterBoundaryIs();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getOuterBoundaryIs <em>Outer Boundary Is</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Outer Boundary Is</em>' containment reference.
     * @see #getOuterBoundaryIs()
     * @generated
     */
    void setOuterBoundaryIs(AbstractRingPropertyType value);

    /**
     * Returns the value of the '<em><b>Parameter ID</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * An identification of an operation parameter. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Parameter ID</em>' containment reference.
     * @see #setParameterID(IdentifierType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_ParameterID()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='parameterID' namespace='##targetNamespace'"
     * @generated
     */
    IdentifierType getParameterID();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getParameterID <em>Parameter ID</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Parameter ID</em>' containment reference.
     * @see #getParameterID()
     * @generated
     */
    void setParameterID(IdentifierType value);

    /**
     * Returns the value of the '<em><b>Parameter Name</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The name by which this operation parameter is identified. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Parameter Name</em>' containment reference.
     * @see #setParameterName(CodeType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_ParameterName()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='parameterName' namespace='##targetNamespace' affiliation='name'"
     * @generated
     */
    CodeType getParameterName();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getParameterName <em>Parameter Name</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Parameter Name</em>' containment reference.
     * @see #getParameterName()
     * @generated
     */
    void setParameterName(CodeType value);

    /**
     * Returns the value of the '<em><b>Parameter Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Parameter Value</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Parameter Value</em>' containment reference.
     * @see #setParameterValue(ParameterValueType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_ParameterValue()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='parameterValue' namespace='##targetNamespace' affiliation='_generalParameterValue'"
     * @generated
     */
    ParameterValueType getParameterValue();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getParameterValue <em>Parameter Value</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Parameter Value</em>' containment reference.
     * @see #getParameterValue()
     * @generated
     */
    void setParameterValue(ParameterValueType value);

    /**
     * Returns the value of the '<em><b>Parameter Value Group</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Parameter Value Group</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Parameter Value Group</em>' containment reference.
     * @see #setParameterValueGroup(ParameterValueGroupType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_ParameterValueGroup()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='parameterValueGroup' namespace='##targetNamespace' affiliation='_generalParameterValue'"
     * @generated
     */
    ParameterValueGroupType getParameterValueGroup();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getParameterValueGroup <em>Parameter Value Group</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Parameter Value Group</em>' containment reference.
     * @see #getParameterValueGroup()
     * @generated
     */
    void setParameterValueGroup(ParameterValueGroupType value);

    /**
     * Returns the value of the '<em><b>Pass Through Operation</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Pass Through Operation</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Pass Through Operation</em>' containment reference.
     * @see #setPassThroughOperation(PassThroughOperationType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_PassThroughOperation()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='PassThroughOperation' namespace='##targetNamespace' affiliation='_SingleOperation'"
     * @generated
     */
    PassThroughOperationType getPassThroughOperation();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getPassThroughOperation <em>Pass Through Operation</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Pass Through Operation</em>' containment reference.
     * @see #getPassThroughOperation()
     * @generated
     */
    void setPassThroughOperation(PassThroughOperationType value);

    /**
     * Returns the value of the '<em><b>Pass Through Operation Ref</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Pass Through Operation Ref</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Pass Through Operation Ref</em>' containment reference.
     * @see #setPassThroughOperationRef(PassThroughOperationRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_PassThroughOperationRef()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='passThroughOperationRef' namespace='##targetNamespace'"
     * @generated
     */
    PassThroughOperationRefType getPassThroughOperationRef();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getPassThroughOperationRef <em>Pass Through Operation Ref</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Pass Through Operation Ref</em>' containment reference.
     * @see #getPassThroughOperationRef()
     * @generated
     */
    void setPassThroughOperationRef(PassThroughOperationRefType value);

    /**
     * Returns the value of the '<em><b>Patches</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This property element contains a list of surface patches. The order of the elements is significant and shall be preserved when processing the array.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Patches</em>' containment reference.
     * @see #setPatches(SurfacePatchArrayPropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Patches()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='patches' namespace='##targetNamespace'"
     * @generated
     */
    SurfacePatchArrayPropertyType getPatches();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getPatches <em>Patches</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Patches</em>' containment reference.
     * @see #getPatches()
     * @generated
     */
    void setPatches(SurfacePatchArrayPropertyType value);

    /**
     * Returns the value of the '<em><b>Pixel In Cell</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Pixel In Cell</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Pixel In Cell</em>' containment reference.
     * @see #setPixelInCell(PixelInCellType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_PixelInCell()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='pixelInCell' namespace='##targetNamespace'"
     * @generated
     */
    PixelInCellType getPixelInCell();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getPixelInCell <em>Pixel In Cell</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Pixel In Cell</em>' containment reference.
     * @see #getPixelInCell()
     * @generated
     */
    void setPixelInCell(PixelInCellType value);

    /**
     * Returns the value of the '<em><b>Point</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Point</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Point</em>' containment reference.
     * @see #setPoint(PointType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Point()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Point' namespace='##targetNamespace' affiliation='_GeometricPrimitive'"
     * @generated
     */
    PointType getPoint();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getPoint <em>Point</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Point</em>' containment reference.
     * @see #getPoint()
     * @generated
     */
    void setPoint(PointType value);

    /**
     * Returns the value of the '<em><b>Point Array Property</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Point Array Property</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Point Array Property</em>' containment reference.
     * @see #setPointArrayProperty(PointArrayPropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_PointArrayProperty()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='pointArrayProperty' namespace='##targetNamespace'"
     * @generated
     */
    PointArrayPropertyType getPointArrayProperty();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getPointArrayProperty <em>Point Array Property</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Point Array Property</em>' containment reference.
     * @see #getPointArrayProperty()
     * @generated
     */
    void setPointArrayProperty(PointArrayPropertyType value);

    /**
     * Returns the value of the '<em><b>Point Member</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This property element either references a Point via the XLink-attributes or contains the Point element.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Point Member</em>' containment reference.
     * @see #setPointMember(PointPropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_PointMember()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='pointMember' namespace='##targetNamespace'"
     * @generated
     */
    PointPropertyType getPointMember();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getPointMember <em>Point Member</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Point Member</em>' containment reference.
     * @see #getPointMember()
     * @generated
     */
    void setPointMember(PointPropertyType value);

    /**
     * Returns the value of the '<em><b>Point Members</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This property element contains a list of points. The order of the elements is significant and shall be preserved when processing the array.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Point Members</em>' containment reference.
     * @see #setPointMembers(PointArrayPropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_PointMembers()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='pointMembers' namespace='##targetNamespace'"
     * @generated
     */
    PointArrayPropertyType getPointMembers();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getPointMembers <em>Point Members</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Point Members</em>' containment reference.
     * @see #getPointMembers()
     * @generated
     */
    void setPointMembers(PointArrayPropertyType value);

    /**
     * Returns the value of the '<em><b>Point Property</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This property element either references a point via the XLink-attributes or contains the point element. pointProperty 
     * 			is the predefined property which can be used by GML Application Schemas whenever a GML Feature has a property with a value that 
     * 			is substitutable for Point.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Point Property</em>' containment reference.
     * @see #setPointProperty(PointPropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_PointProperty()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='pointProperty' namespace='##targetNamespace'"
     * @generated
     */
    PointPropertyType getPointProperty();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getPointProperty <em>Point Property</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Point Property</em>' containment reference.
     * @see #getPointProperty()
     * @generated
     */
    void setPointProperty(PointPropertyType value);

    /**
     * Returns the value of the '<em><b>Point Rep</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Deprecated with GML version 3.1.0. Use "pointProperty" instead. Included for backwards compatibility with GML 3.0.0.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Point Rep</em>' containment reference.
     * @see #setPointRep(PointPropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_PointRep()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='pointRep' namespace='##targetNamespace'"
     * @generated
     */
    PointPropertyType getPointRep();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getPointRep <em>Point Rep</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Point Rep</em>' containment reference.
     * @see #getPointRep()
     * @generated
     */
    void setPointRep(PointPropertyType value);

    /**
     * Returns the value of the '<em><b>Polar CS</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Polar CS</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Polar CS</em>' containment reference.
     * @see #setPolarCS(PolarCSType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_PolarCS()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='PolarCS' namespace='##targetNamespace' affiliation='_CoordinateSystem'"
     * @generated
     */
    PolarCSType getPolarCS();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getPolarCS <em>Polar CS</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Polar CS</em>' containment reference.
     * @see #getPolarCS()
     * @generated
     */
    void setPolarCS(PolarCSType value);

    /**
     * Returns the value of the '<em><b>Polar CS Ref</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Polar CS Ref</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Polar CS Ref</em>' containment reference.
     * @see #setPolarCSRef(PolarCSRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_PolarCSRef()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='polarCSRef' namespace='##targetNamespace'"
     * @generated
     */
    PolarCSRefType getPolarCSRef();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getPolarCSRef <em>Polar CS Ref</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Polar CS Ref</em>' containment reference.
     * @see #getPolarCSRef()
     * @generated
     */
    void setPolarCSRef(PolarCSRefType value);

    /**
     * Returns the value of the '<em><b>Polygon</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Polygon</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Polygon</em>' containment reference.
     * @see #setPolygon(PolygonType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Polygon()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Polygon' namespace='##targetNamespace' affiliation='_Surface'"
     * @generated
     */
    PolygonType getPolygon();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getPolygon <em>Polygon</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Polygon</em>' containment reference.
     * @see #getPolygon()
     * @generated
     */
    void setPolygon(PolygonType value);

    /**
     * Returns the value of the '<em><b>Polygon Member</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Deprecated with GML 3.0 and included only for backwards compatibility with GML 2.0. Use "surfaceMember" instead.
     * This property element either references a polygon via the XLink-attributes or contains the polygon element.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Polygon Member</em>' containment reference.
     * @see #setPolygonMember(PolygonPropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_PolygonMember()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='polygonMember' namespace='##targetNamespace'"
     * @generated
     */
    PolygonPropertyType getPolygonMember();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getPolygonMember <em>Polygon Member</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Polygon Member</em>' containment reference.
     * @see #getPolygonMember()
     * @generated
     */
    void setPolygonMember(PolygonPropertyType value);

    /**
     * Returns the value of the '<em><b>Polygon Patch</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Polygon Patch</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Polygon Patch</em>' containment reference.
     * @see #setPolygonPatch(PolygonPatchType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_PolygonPatch()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='PolygonPatch' namespace='##targetNamespace' affiliation='_SurfacePatch'"
     * @generated
     */
    PolygonPatchType getPolygonPatch();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getPolygonPatch <em>Polygon Patch</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Polygon Patch</em>' containment reference.
     * @see #getPolygonPatch()
     * @generated
     */
    void setPolygonPatch(PolygonPatchType value);

    /**
     * Returns the value of the '<em><b>Polygon Patches</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This property element contains a list of
     *    polygon patches. The order of the patches is significant and 
     *    shall be preserved when processing the list.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Polygon Patches</em>' containment reference.
     * @see #setPolygonPatches(PolygonPatchArrayPropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_PolygonPatches()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='polygonPatches' namespace='##targetNamespace' affiliation='patches'"
     * @generated
     */
    PolygonPatchArrayPropertyType getPolygonPatches();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getPolygonPatches <em>Polygon Patches</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Polygon Patches</em>' containment reference.
     * @see #getPolygonPatches()
     * @generated
     */
    void setPolygonPatches(PolygonPatchArrayPropertyType value);

    /**
     * Returns the value of the '<em><b>Polygon Property</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Deprecated with GML 3.0 and included only for backwards compatibility with GML 2.0. Use "surfaceProperty" instead.
     * This property element either references a polygon via the XLink-attributes or contains the polygon element.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Polygon Property</em>' containment reference.
     * @see #setPolygonProperty(PolygonPropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_PolygonProperty()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='polygonProperty' namespace='##targetNamespace'"
     * @generated
     */
    PolygonPropertyType getPolygonProperty();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getPolygonProperty <em>Polygon Property</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Polygon Property</em>' containment reference.
     * @see #getPolygonProperty()
     * @generated
     */
    void setPolygonProperty(PolygonPropertyType value);

    /**
     * Returns the value of the '<em><b>Polyhedral Surface</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Polyhedral Surface</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Polyhedral Surface</em>' containment reference.
     * @see #setPolyhedralSurface(PolyhedralSurfaceType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_PolyhedralSurface()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='PolyhedralSurface' namespace='##targetNamespace' affiliation='Surface'"
     * @generated
     */
    PolyhedralSurfaceType getPolyhedralSurface();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getPolyhedralSurface <em>Polyhedral Surface</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Polyhedral Surface</em>' containment reference.
     * @see #getPolyhedralSurface()
     * @generated
     */
    void setPolyhedralSurface(PolyhedralSurfaceType value);

    /**
     * Returns the value of the '<em><b>Surface1</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Surface1</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Surface1</em>' containment reference.
     * @see #setSurface1(SurfaceType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Surface1()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Surface' namespace='##targetNamespace' affiliation='_Surface'"
     * @generated
     */
    SurfaceType getSurface1();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getSurface1 <em>Surface1</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Surface1</em>' containment reference.
     * @see #getSurface1()
     * @generated
     */
    void setSurface1(SurfaceType value);

    /**
     * Returns the value of the '<em><b>Pos</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Pos</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Pos</em>' containment reference.
     * @see #setPos(DirectPositionType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Pos()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='pos' namespace='##targetNamespace'"
     * @generated
     */
    DirectPositionType getPos();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getPos <em>Pos</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Pos</em>' containment reference.
     * @see #getPos()
     * @generated
     */
    void setPos(DirectPositionType value);

    /**
     * Returns the value of the '<em><b>Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Position</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Position</em>' containment reference.
     * @see #setPosition(PointPropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Position()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='position' namespace='##targetNamespace'"
     * @generated
     */
    PointPropertyType getPosition();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getPosition <em>Position</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Position</em>' containment reference.
     * @see #getPosition()
     * @generated
     */
    void setPosition(PointPropertyType value);

    /**
     * Returns the value of the '<em><b>Pos List</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Pos List</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Pos List</em>' containment reference.
     * @see #setPosList(DirectPositionListType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_PosList()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='posList' namespace='##targetNamespace'"
     * @generated
     */
    DirectPositionListType getPosList();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getPosList <em>Pos List</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Pos List</em>' containment reference.
     * @see #getPosList()
     * @generated
     */
    void setPosList(DirectPositionListType value);

    /**
     * Returns the value of the '<em><b>Prime Meridian</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Prime Meridian</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Prime Meridian</em>' containment reference.
     * @see #setPrimeMeridian(PrimeMeridianType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_PrimeMeridian()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='PrimeMeridian' namespace='##targetNamespace' affiliation='Definition'"
     * @generated
     */
    PrimeMeridianType getPrimeMeridian();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getPrimeMeridian <em>Prime Meridian</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Prime Meridian</em>' containment reference.
     * @see #getPrimeMeridian()
     * @generated
     */
    void setPrimeMeridian(PrimeMeridianType value);

    /**
     * Returns the value of the '<em><b>Prime Meridian Ref</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Prime Meridian Ref</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Prime Meridian Ref</em>' containment reference.
     * @see #setPrimeMeridianRef(PrimeMeridianRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_PrimeMeridianRef()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='primeMeridianRef' namespace='##targetNamespace'"
     * @generated
     */
    PrimeMeridianRefType getPrimeMeridianRef();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getPrimeMeridianRef <em>Prime Meridian Ref</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Prime Meridian Ref</em>' containment reference.
     * @see #getPrimeMeridianRef()
     * @generated
     */
    void setPrimeMeridianRef(PrimeMeridianRefType value);

    /**
     * Returns the value of the '<em><b>Priority Location</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Deprecated in GML 3.1.0
     * <!-- end-model-doc -->
     * @return the value of the '<em>Priority Location</em>' containment reference.
     * @see #setPriorityLocation(PriorityLocationPropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_PriorityLocation()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='priorityLocation' namespace='##targetNamespace' affiliation='location'"
     * @generated
     */
    PriorityLocationPropertyType getPriorityLocation();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getPriorityLocation <em>Priority Location</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Priority Location</em>' containment reference.
     * @see #getPriorityLocation()
     * @generated
     */
    void setPriorityLocation(PriorityLocationPropertyType value);

    /**
     * Returns the value of the '<em><b>Projected CRS</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Projected CRS</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Projected CRS</em>' containment reference.
     * @see #setProjectedCRS(ProjectedCRSType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_ProjectedCRS()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='ProjectedCRS' namespace='##targetNamespace' affiliation='_GeneralDerivedCRS'"
     * @generated
     */
    ProjectedCRSType getProjectedCRS();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getProjectedCRS <em>Projected CRS</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Projected CRS</em>' containment reference.
     * @see #getProjectedCRS()
     * @generated
     */
    void setProjectedCRS(ProjectedCRSType value);

    /**
     * Returns the value of the '<em><b>Projected CRS Ref</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Projected CRS Ref</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Projected CRS Ref</em>' containment reference.
     * @see #setProjectedCRSRef(ProjectedCRSRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_ProjectedCRSRef()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='projectedCRSRef' namespace='##targetNamespace'"
     * @generated
     */
    ProjectedCRSRefType getProjectedCRSRef();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getProjectedCRSRef <em>Projected CRS Ref</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Projected CRS Ref</em>' containment reference.
     * @see #getProjectedCRSRef()
     * @generated
     */
    void setProjectedCRSRef(ProjectedCRSRefType value);

    /**
     * Returns the value of the '<em><b>Quantity</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * A numeric value with a scale.  The content of the element is an amount using the XML Schema type double which permits decimal or scientific notation.  An XML attribute uom (unit of measure) is required, whose value is a URI which identifies the definition of the scale or units by which the numeric value must be multiplied.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Quantity</em>' containment reference.
     * @see #setQuantity(MeasureType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Quantity()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Quantity' namespace='##targetNamespace'"
     * @generated
     */
    MeasureType getQuantity();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getQuantity <em>Quantity</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Quantity</em>' containment reference.
     * @see #getQuantity()
     * @generated
     */
    void setQuantity(MeasureType value);

    /**
     * Returns the value of the '<em><b>Quantity Extent</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Utility element to store a 2-point range of numeric values. If one member is a null, then this is a single ended interval.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Quantity Extent</em>' containment reference.
     * @see #setQuantityExtent(QuantityExtentType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_QuantityExtent()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='QuantityExtent' namespace='##targetNamespace'"
     * @generated
     */
    QuantityExtentType getQuantityExtent();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getQuantityExtent <em>Quantity Extent</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Quantity Extent</em>' containment reference.
     * @see #getQuantityExtent()
     * @generated
     */
    void setQuantityExtent(QuantityExtentType value);

    /**
     * Returns the value of the '<em><b>Quantity List</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * A space separated list of amounts or nulls.  The amounts use the XML Schema type double.  A single XML attribute uom (unit of measure) is required, whose value is a URI which identifies the definition of the scale or units by which all the amounts in the list must be multiplied.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Quantity List</em>' containment reference.
     * @see #setQuantityList(MeasureOrNullListType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_QuantityList()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='QuantityList' namespace='##targetNamespace'"
     * @generated
     */
    MeasureOrNullListType getQuantityList();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getQuantityList <em>Quantity List</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Quantity List</em>' containment reference.
     * @see #getQuantityList()
     * @generated
     */
    void setQuantityList(MeasureOrNullListType value);

    /**
     * Returns the value of the '<em><b>Quantity Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Informal description of the phenomenon or type of quantity that is measured or observed. For example, "length", "angle", "time", "pressure", or "temperature". When the quantity is the result of an observation or measurement, this term is known as Observable Type or Measurand.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Quantity Type</em>' containment reference.
     * @see #setQuantityType(StringOrRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_QuantityType()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='quantityType' namespace='##targetNamespace'"
     * @generated
     */
    StringOrRefType getQuantityType();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getQuantityType <em>Quantity Type</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Quantity Type</em>' containment reference.
     * @see #getQuantityType()
     * @generated
     */
    void setQuantityType(StringOrRefType value);

    /**
     * Returns the value of the '<em><b>Range Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Range Parameters</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Range Parameters</em>' containment reference.
     * @see #setRangeParameters(RangeParametersType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_RangeParameters()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='rangeParameters' namespace='##targetNamespace'"
     * @generated
     */
    RangeParametersType getRangeParameters();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getRangeParameters <em>Range Parameters</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Range Parameters</em>' containment reference.
     * @see #getRangeParameters()
     * @generated
     */
    void setRangeParameters(RangeParametersType value);

    /**
     * Returns the value of the '<em><b>Range Set</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Range Set</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Range Set</em>' containment reference.
     * @see #setRangeSet(RangeSetType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_RangeSet()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='rangeSet' namespace='##targetNamespace'"
     * @generated
     */
    RangeSetType getRangeSet();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getRangeSet <em>Range Set</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Range Set</em>' containment reference.
     * @see #getRangeSet()
     * @generated
     */
    void setRangeSet(RangeSetType value);

    /**
     * Returns the value of the '<em><b>Realization Epoch</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The time after which this datum definition is valid. This time may be precise (e.g. 1997.0 for IRTF97) or merely a year (e.g. 1983 for NAD83). In the latter case, the epoch usually refers to the year in which a major recalculation of the geodetic control network, underlying the datum, was executed or initiated. An old datum can remain valid after a new datum is defined. Alternatively, a datum may be superseded by a later datum, in which case the realization epoch for the new datum defines the upper limit for the validity of the superseded datum. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Realization Epoch</em>' attribute.
     * @see #setRealizationEpoch(XMLGregorianCalendar)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_RealizationEpoch()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.Date" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='realizationEpoch' namespace='##targetNamespace'"
     * @generated
     */
    XMLGregorianCalendar getRealizationEpoch();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getRealizationEpoch <em>Realization Epoch</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Realization Epoch</em>' attribute.
     * @see #getRealizationEpoch()
     * @generated
     */
    void setRealizationEpoch(XMLGregorianCalendar value);

    /**
     * Returns the value of the '<em><b>Rectangle</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Rectangle</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Rectangle</em>' containment reference.
     * @see #setRectangle(RectangleType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Rectangle()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Rectangle' namespace='##targetNamespace' affiliation='_SurfacePatch'"
     * @generated
     */
    RectangleType getRectangle();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getRectangle <em>Rectangle</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Rectangle</em>' containment reference.
     * @see #getRectangle()
     * @generated
     */
    void setRectangle(RectangleType value);

    /**
     * Returns the value of the '<em><b>Rectified Grid</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Should be substitutionGroup="gml:Grid" but changed in order to accomplish Xerces-J schema validation
     * <!-- end-model-doc -->
     * @return the value of the '<em>Rectified Grid</em>' containment reference.
     * @see #setRectifiedGrid(RectifiedGridType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_RectifiedGrid()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='RectifiedGrid' namespace='##targetNamespace' affiliation='_ImplicitGeometry'"
     * @generated
     */
    RectifiedGridType getRectifiedGrid();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getRectifiedGrid <em>Rectified Grid</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Rectified Grid</em>' containment reference.
     * @see #getRectifiedGrid()
     * @generated
     */
    void setRectifiedGrid(RectifiedGridType value);

    /**
     * Returns the value of the '<em><b>Rectified Grid Coverage</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Rectified Grid Coverage</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Rectified Grid Coverage</em>' containment reference.
     * @see #setRectifiedGridCoverage(RectifiedGridCoverageType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_RectifiedGridCoverage()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='RectifiedGridCoverage' namespace='##targetNamespace' affiliation='_DiscreteCoverage'"
     * @generated
     */
    RectifiedGridCoverageType getRectifiedGridCoverage();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getRectifiedGridCoverage <em>Rectified Grid Coverage</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Rectified Grid Coverage</em>' containment reference.
     * @see #getRectifiedGridCoverage()
     * @generated
     */
    void setRectifiedGridCoverage(RectifiedGridCoverageType value);

    /**
     * Returns the value of the '<em><b>Rectified Grid Domain</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Rectified Grid Domain</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Rectified Grid Domain</em>' containment reference.
     * @see #setRectifiedGridDomain(RectifiedGridDomainType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_RectifiedGridDomain()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='rectifiedGridDomain' namespace='##targetNamespace' affiliation='domainSet'"
     * @generated
     */
    RectifiedGridDomainType getRectifiedGridDomain();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getRectifiedGridDomain <em>Rectified Grid Domain</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Rectified Grid Domain</em>' containment reference.
     * @see #getRectifiedGridDomain()
     * @generated
     */
    void setRectifiedGridDomain(RectifiedGridDomainType value);

    /**
     * Returns the value of the '<em><b>Reference System Ref</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Reference System Ref</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Reference System Ref</em>' containment reference.
     * @see #setReferenceSystemRef(ReferenceSystemRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_ReferenceSystemRef()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='referenceSystemRef' namespace='##targetNamespace'"
     * @generated
     */
    ReferenceSystemRefType getReferenceSystemRef();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getReferenceSystemRef <em>Reference System Ref</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Reference System Ref</em>' containment reference.
     * @see #getReferenceSystemRef()
     * @generated
     */
    void setReferenceSystemRef(ReferenceSystemRefType value);

    /**
     * Returns the value of the '<em><b>Relative Internal Positional Accuracy</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Relative Internal Positional Accuracy</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Relative Internal Positional Accuracy</em>' containment reference.
     * @see #setRelativeInternalPositionalAccuracy(RelativeInternalPositionalAccuracyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_RelativeInternalPositionalAccuracy()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='relativeInternalPositionalAccuracy' namespace='##targetNamespace' affiliation='_positionalAccuracy'"
     * @generated
     */
    RelativeInternalPositionalAccuracyType getRelativeInternalPositionalAccuracy();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getRelativeInternalPositionalAccuracy <em>Relative Internal Positional Accuracy</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Relative Internal Positional Accuracy</em>' containment reference.
     * @see #getRelativeInternalPositionalAccuracy()
     * @generated
     */
    void setRelativeInternalPositionalAccuracy(RelativeInternalPositionalAccuracyType value);

    /**
     * Returns the value of the '<em><b>Remarks</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Information about this object or code. Contains text or refers to external text.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Remarks</em>' containment reference.
     * @see #setRemarks(StringOrRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Remarks()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='remarks' namespace='##targetNamespace'"
     * @generated
     */
    StringOrRefType getRemarks();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getRemarks <em>Remarks</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Remarks</em>' containment reference.
     * @see #getRemarks()
     * @generated
     */
    void setRemarks(StringOrRefType value);

    /**
     * Returns the value of the '<em><b>Result</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * A quantitative result defined by the evaluation procedure used, and identified by the measureDescription. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Result</em>' containment reference.
     * @see #setResult(MeasureType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Result()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='result' namespace='##targetNamespace'"
     * @generated
     */
    MeasureType getResult();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getResult <em>Result</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Result</em>' containment reference.
     * @see #getResult()
     * @generated
     */
    void setResult(MeasureType value);

    /**
     * Returns the value of the '<em><b>Result Of</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The result of the observation: an image, external object, etc
     * <!-- end-model-doc -->
     * @return the value of the '<em>Result Of</em>' containment reference.
     * @see #setResultOf(AssociationType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_ResultOf()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='resultOf' namespace='##targetNamespace'"
     * @generated
     */
    AssociationType getResultOf();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getResultOf <em>Result Of</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Result Of</em>' containment reference.
     * @see #getResultOf()
     * @generated
     */
    void setResultOf(AssociationType value);

    /**
     * Returns the value of the '<em><b>Ring1</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Ring1</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Ring1</em>' containment reference.
     * @see #setRing1(RingType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Ring1()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Ring' namespace='##targetNamespace' affiliation='_Ring'"
     * @generated
     */
    RingType getRing1();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getRing1 <em>Ring1</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Ring1</em>' containment reference.
     * @see #getRing1()
     * @generated
     */
    void setRing1(RingType value);

    /**
     * Returns the value of the '<em><b>Rough Conversion To Preferred Unit</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This element is included when the correct definition of this unit is unknown, but this unit has a rough or inaccurate conversion to the preferred unit for this quantity type.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Rough Conversion To Preferred Unit</em>' containment reference.
     * @see #setRoughConversionToPreferredUnit(ConversionToPreferredUnitType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_RoughConversionToPreferredUnit()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='roughConversionToPreferredUnit' namespace='##targetNamespace'"
     * @generated
     */
    ConversionToPreferredUnitType getRoughConversionToPreferredUnit();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getRoughConversionToPreferredUnit <em>Rough Conversion To Preferred Unit</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Rough Conversion To Preferred Unit</em>' containment reference.
     * @see #getRoughConversionToPreferredUnit()
     * @generated
     */
    void setRoughConversionToPreferredUnit(ConversionToPreferredUnitType value);

    /**
     * Returns the value of the '<em><b>Row Index</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Row number of this covariance element value. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Row Index</em>' attribute.
     * @see #setRowIndex(BigInteger)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_RowIndex()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.PositiveInteger" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='rowIndex' namespace='##targetNamespace'"
     * @generated
     */
    BigInteger getRowIndex();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getRowIndex <em>Row Index</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Row Index</em>' attribute.
     * @see #getRowIndex()
     * @generated
     */
    void setRowIndex(BigInteger value);

    /**
     * Returns the value of the '<em><b>Scope</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Description of domain of usage, or limitations of usage, for which this CRS object is valid.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Scope</em>' attribute.
     * @see #setScope(String)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Scope()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='scope' namespace='##targetNamespace'"
     * @generated
     */
    String getScope();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getScope <em>Scope</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Scope</em>' attribute.
     * @see #getScope()
     * @generated
     */
    void setScope(String value);

    /**
     * Returns the value of the '<em><b>Second Defining Parameter</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Second Defining Parameter</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Second Defining Parameter</em>' containment reference.
     * @see #setSecondDefiningParameter(SecondDefiningParameterType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_SecondDefiningParameter()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='secondDefiningParameter' namespace='##targetNamespace'"
     * @generated
     */
    SecondDefiningParameterType getSecondDefiningParameter();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getSecondDefiningParameter <em>Second Defining Parameter</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Second Defining Parameter</em>' containment reference.
     * @see #getSecondDefiningParameter()
     * @generated
     */
    void setSecondDefiningParameter(SecondDefiningParameterType value);

    /**
     * Returns the value of the '<em><b>Seconds</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Seconds</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Seconds</em>' attribute.
     * @see #setSeconds(BigDecimal)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Seconds()
     * @model unique="false" dataType="net.opengis.gml311.ArcSecondsType" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='seconds' namespace='##targetNamespace'"
     * @generated
     */
    BigDecimal getSeconds();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getSeconds <em>Seconds</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Seconds</em>' attribute.
     * @see #getSeconds()
     * @generated
     */
    void setSeconds(BigDecimal value);

    /**
     * Returns the value of the '<em><b>Segments</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This property element contains a list of curve segments. The order of the elements is significant and shall be preserved when processing the array.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Segments</em>' containment reference.
     * @see #setSegments(CurveSegmentArrayPropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Segments()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='segments' namespace='##targetNamespace'"
     * @generated
     */
    CurveSegmentArrayPropertyType getSegments();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getSegments <em>Segments</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Segments</em>' containment reference.
     * @see #getSegments()
     * @generated
     */
    void setSegments(CurveSegmentArrayPropertyType value);

    /**
     * Returns the value of the '<em><b>Semi Major Axis</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Length of the semi-major axis of the ellipsoid, with its units. Uses the MeasureType with the restriction that the unit of measure referenced by uom must be suitable for a length, such as metres or feet. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Semi Major Axis</em>' containment reference.
     * @see #setSemiMajorAxis(MeasureType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_SemiMajorAxis()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='semiMajorAxis' namespace='##targetNamespace'"
     * @generated
     */
    MeasureType getSemiMajorAxis();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getSemiMajorAxis <em>Semi Major Axis</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Semi Major Axis</em>' containment reference.
     * @see #getSemiMajorAxis()
     * @generated
     */
    void setSemiMajorAxis(MeasureType value);

    /**
     * Returns the value of the '<em><b>Semi Minor Axis</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Length of the semi-minor axis of the ellipsoid. Uses the MeasureType with the restriction that the unit of measure referenced by uom must be suitable for a length, such as metres or feet. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Semi Minor Axis</em>' containment reference.
     * @see #setSemiMinorAxis(MeasureType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_SemiMinorAxis()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='semiMinorAxis' namespace='##targetNamespace'"
     * @generated
     */
    MeasureType getSemiMinorAxis();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getSemiMinorAxis <em>Semi Minor Axis</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Semi Minor Axis</em>' containment reference.
     * @see #getSemiMinorAxis()
     * @generated
     */
    void setSemiMinorAxis(MeasureType value);

    /**
     * Returns the value of the '<em><b>Single Operation Ref</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Single Operation Ref</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Single Operation Ref</em>' containment reference.
     * @see #setSingleOperationRef(SingleOperationRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_SingleOperationRef()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='singleOperationRef' namespace='##targetNamespace'"
     * @generated
     */
    SingleOperationRefType getSingleOperationRef();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getSingleOperationRef <em>Single Operation Ref</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Single Operation Ref</em>' containment reference.
     * @see #getSingleOperationRef()
     * @generated
     */
    void setSingleOperationRef(SingleOperationRefType value);

    /**
     * Returns the value of the '<em><b>Solid1</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Solid1</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Solid1</em>' containment reference.
     * @see #setSolid1(SolidType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Solid1()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Solid' namespace='##targetNamespace' affiliation='_Solid'"
     * @generated
     */
    SolidType getSolid1();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getSolid1 <em>Solid1</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Solid1</em>' containment reference.
     * @see #getSolid1()
     * @generated
     */
    void setSolid1(SolidType value);

    /**
     * Returns the value of the '<em><b>Solid Array Property</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Solid Array Property</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Solid Array Property</em>' containment reference.
     * @see #setSolidArrayProperty(SolidArrayPropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_SolidArrayProperty()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='solidArrayProperty' namespace='##targetNamespace'"
     * @generated
     */
    SolidArrayPropertyType getSolidArrayProperty();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getSolidArrayProperty <em>Solid Array Property</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Solid Array Property</em>' containment reference.
     * @see #getSolidArrayProperty()
     * @generated
     */
    void setSolidArrayProperty(SolidArrayPropertyType value);

    /**
     * Returns the value of the '<em><b>Solid Member</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This property element either references a solid via the XLink-attributes or contains the solid element. A solid element is any element which is substitutable for "_Solid".
     * <!-- end-model-doc -->
     * @return the value of the '<em>Solid Member</em>' containment reference.
     * @see #setSolidMember(SolidPropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_SolidMember()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='solidMember' namespace='##targetNamespace'"
     * @generated
     */
    SolidPropertyType getSolidMember();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getSolidMember <em>Solid Member</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Solid Member</em>' containment reference.
     * @see #getSolidMember()
     * @generated
     */
    void setSolidMember(SolidPropertyType value);

    /**
     * Returns the value of the '<em><b>Solid Members</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This property element contains a list of solids. The order of the elements is significant and shall be preserved when processing the array.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Solid Members</em>' containment reference.
     * @see #setSolidMembers(SolidArrayPropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_SolidMembers()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='solidMembers' namespace='##targetNamespace'"
     * @generated
     */
    SolidArrayPropertyType getSolidMembers();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getSolidMembers <em>Solid Members</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Solid Members</em>' containment reference.
     * @see #getSolidMembers()
     * @generated
     */
    void setSolidMembers(SolidArrayPropertyType value);

    /**
     * Returns the value of the '<em><b>Solid Property</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This property element either references a solid via the XLink-attributes or contains the solid element. solidProperty is the predefined property which can be used by GML Application Schemas whenever a GML Feature has a property with a value that is substitutable for _Solid.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Solid Property</em>' containment reference.
     * @see #setSolidProperty(SolidPropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_SolidProperty()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='solidProperty' namespace='##targetNamespace'"
     * @generated
     */
    SolidPropertyType getSolidProperty();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getSolidProperty <em>Solid Property</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Solid Property</em>' containment reference.
     * @see #getSolidProperty()
     * @generated
     */
    void setSolidProperty(SolidPropertyType value);

    /**
     * Returns the value of the '<em><b>Source CRS</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Association to the source CRS (coordinate reference system) of this coordinate operation. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Source CRS</em>' containment reference.
     * @see #setSourceCRS(CRSRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_SourceCRS()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='sourceCRS' namespace='##targetNamespace'"
     * @generated
     */
    CRSRefType getSourceCRS();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getSourceCRS <em>Source CRS</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Source CRS</em>' containment reference.
     * @see #getSourceCRS()
     * @generated
     */
    void setSourceCRS(CRSRefType value);

    /**
     * Returns the value of the '<em><b>Source Dimensions</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Number of dimensions in the source CRS of this operation method. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Source Dimensions</em>' attribute.
     * @see #setSourceDimensions(BigInteger)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_SourceDimensions()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.PositiveInteger" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='sourceDimensions' namespace='##targetNamespace'"
     * @generated
     */
    BigInteger getSourceDimensions();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getSourceDimensions <em>Source Dimensions</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Source Dimensions</em>' attribute.
     * @see #getSourceDimensions()
     * @generated
     */
    void setSourceDimensions(BigInteger value);

    /**
     * Returns the value of the '<em><b>Sphere</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Sphere</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Sphere</em>' containment reference.
     * @see #setSphere(SphereType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Sphere()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Sphere' namespace='##targetNamespace' affiliation='_GriddedSurface'"
     * @generated
     */
    SphereType getSphere();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getSphere <em>Sphere</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Sphere</em>' containment reference.
     * @see #getSphere()
     * @generated
     */
    void setSphere(SphereType value);

    /**
     * Returns the value of the '<em><b>Spherical CS</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Spherical CS</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Spherical CS</em>' containment reference.
     * @see #setSphericalCS(SphericalCSType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_SphericalCS()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='SphericalCS' namespace='##targetNamespace' affiliation='_CoordinateSystem'"
     * @generated
     */
    SphericalCSType getSphericalCS();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getSphericalCS <em>Spherical CS</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Spherical CS</em>' containment reference.
     * @see #getSphericalCS()
     * @generated
     */
    void setSphericalCS(SphericalCSType value);

    /**
     * Returns the value of the '<em><b>Spherical CS Ref</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Spherical CS Ref</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Spherical CS Ref</em>' containment reference.
     * @see #setSphericalCSRef(SphericalCSRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_SphericalCSRef()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='sphericalCSRef' namespace='##targetNamespace'"
     * @generated
     */
    SphericalCSRefType getSphericalCSRef();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getSphericalCSRef <em>Spherical CS Ref</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Spherical CS Ref</em>' containment reference.
     * @see #getSphericalCSRef()
     * @generated
     */
    void setSphericalCSRef(SphericalCSRefType value);

    /**
     * Returns the value of the '<em><b>Srs ID</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * An identification of a reference system.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Srs ID</em>' containment reference.
     * @see #setSrsID(IdentifierType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_SrsID()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='srsID' namespace='##targetNamespace'"
     * @generated
     */
    IdentifierType getSrsID();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getSrsID <em>Srs ID</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Srs ID</em>' containment reference.
     * @see #getSrsID()
     * @generated
     */
    void setSrsID(IdentifierType value);

    /**
     * Returns the value of the '<em><b>Srs Name</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The name by which this reference system is identified.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Srs Name</em>' containment reference.
     * @see #setSrsName(CodeType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_SrsName()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='srsName' namespace='##targetNamespace' affiliation='name'"
     * @generated
     */
    CodeType getSrsName();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getSrsName <em>Srs Name</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Srs Name</em>' containment reference.
     * @see #getSrsName()
     * @generated
     */
    void setSrsName(CodeType value);

    /**
     * Returns the value of the '<em><b>Status</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Status</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Status</em>' containment reference.
     * @see #setStatus(StringOrRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Status()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='status' namespace='##targetNamespace'"
     * @generated
     */
    StringOrRefType getStatus();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getStatus <em>Status</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Status</em>' containment reference.
     * @see #getStatus()
     * @generated
     */
    void setStatus(StringOrRefType value);

    /**
     * Returns the value of the '<em><b>String Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * String value of an operation parameter. A string value does not have an associated unit of measure. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>String Value</em>' attribute.
     * @see #setStringValue(String)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_StringValue()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='stringValue' namespace='##targetNamespace'"
     * @generated
     */
    String getStringValue();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getStringValue <em>String Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>String Value</em>' attribute.
     * @see #getStringValue()
     * @generated
     */
    void setStringValue(String value);

    /**
     * Returns the value of the '<em><b>Style1</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Predefined concrete value of the top-level property. Encapsulates all other styling information.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Style1</em>' containment reference.
     * @see #setStyle1(StyleType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Style1()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Style' namespace='##targetNamespace' affiliation='_Style'"
     * @generated
     */
    StyleType getStyle1();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getStyle1 <em>Style1</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Style1</em>' containment reference.
     * @see #getStyle1()
     * @generated
     */
    void setStyle1(StyleType value);

    /**
     * Returns the value of the '<em><b>Sub Complex</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Sub Complex</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Sub Complex</em>' containment reference.
     * @see #setSubComplex(TopoComplexMemberType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_SubComplex()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='subComplex' namespace='##targetNamespace'"
     * @generated
     */
    TopoComplexMemberType getSubComplex();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getSubComplex <em>Sub Complex</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Sub Complex</em>' containment reference.
     * @see #getSubComplex()
     * @generated
     */
    void setSubComplex(TopoComplexMemberType value);

    /**
     * Returns the value of the '<em><b>Subject</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Synonym for target - common word used for photographs
     * <!-- end-model-doc -->
     * @return the value of the '<em>Subject</em>' containment reference.
     * @see #setSubject(TargetPropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Subject()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='subject' namespace='##targetNamespace' affiliation='target'"
     * @generated
     */
    TargetPropertyType getSubject();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getSubject <em>Subject</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Subject</em>' containment reference.
     * @see #getSubject()
     * @generated
     */
    void setSubject(TargetPropertyType value);

    /**
     * Returns the value of the '<em><b>Target</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This element contains or points to the specimen, region or station which is the object of the observation
     * <!-- end-model-doc -->
     * @return the value of the '<em>Target</em>' containment reference.
     * @see #setTarget(TargetPropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Target()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='target' namespace='##targetNamespace'"
     * @generated
     */
    TargetPropertyType getTarget();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getTarget <em>Target</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Target</em>' containment reference.
     * @see #getTarget()
     * @generated
     */
    void setTarget(TargetPropertyType value);

    /**
     * Returns the value of the '<em><b>Super Complex</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Super Complex</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Super Complex</em>' containment reference.
     * @see #setSuperComplex(TopoComplexMemberType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_SuperComplex()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='superComplex' namespace='##targetNamespace'"
     * @generated
     */
    TopoComplexMemberType getSuperComplex();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getSuperComplex <em>Super Complex</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Super Complex</em>' containment reference.
     * @see #getSuperComplex()
     * @generated
     */
    void setSuperComplex(TopoComplexMemberType value);

    /**
     * Returns the value of the '<em><b>Surface Array Property</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Surface Array Property</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Surface Array Property</em>' containment reference.
     * @see #setSurfaceArrayProperty(SurfaceArrayPropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_SurfaceArrayProperty()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='surfaceArrayProperty' namespace='##targetNamespace'"
     * @generated
     */
    SurfaceArrayPropertyType getSurfaceArrayProperty();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getSurfaceArrayProperty <em>Surface Array Property</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Surface Array Property</em>' containment reference.
     * @see #getSurfaceArrayProperty()
     * @generated
     */
    void setSurfaceArrayProperty(SurfaceArrayPropertyType value);

    /**
     * Returns the value of the '<em><b>Surface Member</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This property element either references a surface via the XLink-attributes or contains the surface element. A surface element is any element which is substitutable for "_Surface".
     * <!-- end-model-doc -->
     * @return the value of the '<em>Surface Member</em>' containment reference.
     * @see #setSurfaceMember(SurfacePropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_SurfaceMember()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='surfaceMember' namespace='##targetNamespace'"
     * @generated
     */
    SurfacePropertyType getSurfaceMember();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getSurfaceMember <em>Surface Member</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Surface Member</em>' containment reference.
     * @see #getSurfaceMember()
     * @generated
     */
    void setSurfaceMember(SurfacePropertyType value);

    /**
     * Returns the value of the '<em><b>Surface Members</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This property element contains a list of surfaces. The order of the elements is significant and shall be preserved when processing the array.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Surface Members</em>' containment reference.
     * @see #setSurfaceMembers(SurfaceArrayPropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_SurfaceMembers()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='surfaceMembers' namespace='##targetNamespace'"
     * @generated
     */
    SurfaceArrayPropertyType getSurfaceMembers();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getSurfaceMembers <em>Surface Members</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Surface Members</em>' containment reference.
     * @see #getSurfaceMembers()
     * @generated
     */
    void setSurfaceMembers(SurfaceArrayPropertyType value);

    /**
     * Returns the value of the '<em><b>Surface Property</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This property element either references a surface via the XLink-attributes or contains the surface element. surfaceProperty is the predefined property which can be used by GML Application Schemas whenever a GML Feature has a property with a value that is substitutable for _Surface.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Surface Property</em>' containment reference.
     * @see #setSurfaceProperty(SurfacePropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_SurfaceProperty()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='surfaceProperty' namespace='##targetNamespace'"
     * @generated
     */
    SurfacePropertyType getSurfaceProperty();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getSurfaceProperty <em>Surface Property</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Surface Property</em>' containment reference.
     * @see #getSurfaceProperty()
     * @generated
     */
    void setSurfaceProperty(SurfacePropertyType value);

    /**
     * Returns the value of the '<em><b>Symbol</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The symbol property. Extends the gml:AssociationType to allow for remote referencing of symbols.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Symbol</em>' containment reference.
     * @see #setSymbol(SymbolType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Symbol()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='symbol' namespace='##targetNamespace'"
     * @generated
     */
    SymbolType getSymbol();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getSymbol <em>Symbol</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Symbol</em>' containment reference.
     * @see #getSymbol()
     * @generated
     */
    void setSymbol(SymbolType value);

    /**
     * Returns the value of the '<em><b>Target CRS</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Association to the target CRS (coordinate reference system) of this coordinate operation. For constraints on multiplicity of "sourceCRS" and "targetCRS", see UML model of Coordinate Operation package in OGC Abstract Specification topic 2. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Target CRS</em>' containment reference.
     * @see #setTargetCRS(CRSRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_TargetCRS()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='targetCRS' namespace='##targetNamespace'"
     * @generated
     */
    CRSRefType getTargetCRS();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getTargetCRS <em>Target CRS</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Target CRS</em>' containment reference.
     * @see #getTargetCRS()
     * @generated
     */
    void setTargetCRS(CRSRefType value);

    /**
     * Returns the value of the '<em><b>Target Dimensions</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Number of dimensions in the target CRS of this operation method. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Target Dimensions</em>' attribute.
     * @see #setTargetDimensions(BigInteger)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_TargetDimensions()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.PositiveInteger" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='targetDimensions' namespace='##targetNamespace'"
     * @generated
     */
    BigInteger getTargetDimensions();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getTargetDimensions <em>Target Dimensions</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Target Dimensions</em>' attribute.
     * @see #getTargetDimensions()
     * @generated
     */
    void setTargetDimensions(BigInteger value);

    /**
     * Returns the value of the '<em><b>Temporal CRS</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Temporal CRS</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Temporal CRS</em>' containment reference.
     * @see #setTemporalCRS(TemporalCRSType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_TemporalCRS()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='TemporalCRS' namespace='##targetNamespace' affiliation='_CoordinateReferenceSystem'"
     * @generated
     */
    TemporalCRSType getTemporalCRS();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getTemporalCRS <em>Temporal CRS</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Temporal CRS</em>' containment reference.
     * @see #getTemporalCRS()
     * @generated
     */
    void setTemporalCRS(TemporalCRSType value);

    /**
     * Returns the value of the '<em><b>Temporal CRS Ref</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Temporal CRS Ref</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Temporal CRS Ref</em>' containment reference.
     * @see #setTemporalCRSRef(TemporalCRSRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_TemporalCRSRef()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='temporalCRSRef' namespace='##targetNamespace'"
     * @generated
     */
    TemporalCRSRefType getTemporalCRSRef();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getTemporalCRSRef <em>Temporal CRS Ref</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Temporal CRS Ref</em>' containment reference.
     * @see #getTemporalCRSRef()
     * @generated
     */
    void setTemporalCRSRef(TemporalCRSRefType value);

    /**
     * Returns the value of the '<em><b>Temporal CS</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Temporal CS</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Temporal CS</em>' containment reference.
     * @see #setTemporalCS(TemporalCSType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_TemporalCS()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='TemporalCS' namespace='##targetNamespace' affiliation='_CoordinateSystem'"
     * @generated
     */
    TemporalCSType getTemporalCS();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getTemporalCS <em>Temporal CS</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Temporal CS</em>' containment reference.
     * @see #getTemporalCS()
     * @generated
     */
    void setTemporalCS(TemporalCSType value);

    /**
     * Returns the value of the '<em><b>Temporal CS Ref</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Temporal CS Ref</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Temporal CS Ref</em>' containment reference.
     * @see #setTemporalCSRef(TemporalCSRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_TemporalCSRef()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='temporalCSRef' namespace='##targetNamespace'"
     * @generated
     */
    TemporalCSRefType getTemporalCSRef();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getTemporalCSRef <em>Temporal CS Ref</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Temporal CS Ref</em>' containment reference.
     * @see #getTemporalCSRef()
     * @generated
     */
    void setTemporalCSRef(TemporalCSRefType value);

    /**
     * Returns the value of the '<em><b>Temporal Datum</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Temporal Datum</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Temporal Datum</em>' containment reference.
     * @see #setTemporalDatum(TemporalDatumType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_TemporalDatum()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='TemporalDatum' namespace='##targetNamespace' affiliation='_Datum'"
     * @generated
     */
    TemporalDatumType getTemporalDatum();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getTemporalDatum <em>Temporal Datum</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Temporal Datum</em>' containment reference.
     * @see #getTemporalDatum()
     * @generated
     */
    void setTemporalDatum(TemporalDatumType value);

    /**
     * Returns the value of the '<em><b>Temporal Datum Ref</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Temporal Datum Ref</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Temporal Datum Ref</em>' containment reference.
     * @see #setTemporalDatumRef(TemporalDatumRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_TemporalDatumRef()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='temporalDatumRef' namespace='##targetNamespace'"
     * @generated
     */
    TemporalDatumRefType getTemporalDatumRef();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getTemporalDatumRef <em>Temporal Datum Ref</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Temporal Datum Ref</em>' containment reference.
     * @see #getTemporalDatumRef()
     * @generated
     */
    void setTemporalDatumRef(TemporalDatumRefType value);

    /**
     * Returns the value of the '<em><b>Temporal Extent</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * A time period defining the temporal domain of this object.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Temporal Extent</em>' containment reference.
     * @see #setTemporalExtent(TimePeriodType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_TemporalExtent()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='temporalExtent' namespace='##targetNamespace'"
     * @generated
     */
    TimePeriodType getTemporalExtent();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getTemporalExtent <em>Temporal Extent</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Temporal Extent</em>' containment reference.
     * @see #getTemporalExtent()
     * @generated
     */
    void setTemporalExtent(TimePeriodType value);

    /**
     * Returns the value of the '<em><b>Time Calendar</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Time Calendar</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Time Calendar</em>' containment reference.
     * @see #setTimeCalendar(TimeCalendarType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_TimeCalendar()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='TimeCalendar' namespace='##targetNamespace' affiliation='_TimeReferenceSystem'"
     * @generated
     */
    TimeCalendarType getTimeCalendar();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getTimeCalendar <em>Time Calendar</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Time Calendar</em>' containment reference.
     * @see #getTimeCalendar()
     * @generated
     */
    void setTimeCalendar(TimeCalendarType value);

    /**
     * Returns the value of the '<em><b>Time Calendar Era</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Time Calendar Era</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Time Calendar Era</em>' containment reference.
     * @see #setTimeCalendarEra(TimeCalendarEraType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_TimeCalendarEra()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='TimeCalendarEra' namespace='##targetNamespace' affiliation='Definition'"
     * @generated
     */
    TimeCalendarEraType getTimeCalendarEra();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getTimeCalendarEra <em>Time Calendar Era</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Time Calendar Era</em>' containment reference.
     * @see #getTimeCalendarEra()
     * @generated
     */
    void setTimeCalendarEra(TimeCalendarEraType value);

    /**
     * Returns the value of the '<em><b>Time Clock</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Time Clock</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Time Clock</em>' containment reference.
     * @see #setTimeClock(TimeClockType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_TimeClock()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='TimeClock' namespace='##targetNamespace' affiliation='_TimeReferenceSystem'"
     * @generated
     */
    TimeClockType getTimeClock();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getTimeClock <em>Time Clock</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Time Clock</em>' containment reference.
     * @see #getTimeClock()
     * @generated
     */
    void setTimeClock(TimeClockType value);

    /**
     * Returns the value of the '<em><b>Time Coordinate System</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Time Coordinate System</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Time Coordinate System</em>' containment reference.
     * @see #setTimeCoordinateSystem(TimeCoordinateSystemType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_TimeCoordinateSystem()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='TimeCoordinateSystem' namespace='##targetNamespace' affiliation='_TimeReferenceSystem'"
     * @generated
     */
    TimeCoordinateSystemType getTimeCoordinateSystem();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getTimeCoordinateSystem <em>Time Coordinate System</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Time Coordinate System</em>' containment reference.
     * @see #getTimeCoordinateSystem()
     * @generated
     */
    void setTimeCoordinateSystem(TimeCoordinateSystemType value);

    /**
     * Returns the value of the '<em><b>Time Edge</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * TimeEdge is one dimensional temporal topology primitive,
     * 			 expresses a state in topological time. It has an orientation from its start toward the end, 
     * 			 and its boundaries shall associate with two different time nodes.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Time Edge</em>' containment reference.
     * @see #setTimeEdge(TimeEdgeType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_TimeEdge()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='TimeEdge' namespace='##targetNamespace' affiliation='_TimeTopologyPrimitive'"
     * @generated
     */
    TimeEdgeType getTimeEdge();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getTimeEdge <em>Time Edge</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Time Edge</em>' containment reference.
     * @see #getTimeEdge()
     * @generated
     */
    void setTimeEdge(TimeEdgeType value);

    /**
     * Returns the value of the '<em><b>Time Instant</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Time Instant</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Time Instant</em>' containment reference.
     * @see #setTimeInstant(TimeInstantType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_TimeInstant()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='TimeInstant' namespace='##targetNamespace' affiliation='_TimeGeometricPrimitive'"
     * @generated
     */
    TimeInstantType getTimeInstant();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getTimeInstant <em>Time Instant</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Time Instant</em>' containment reference.
     * @see #getTimeInstant()
     * @generated
     */
    void setTimeInstant(TimeInstantType value);

    /**
     * Returns the value of the '<em><b>Time Interval</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This element is a valid subtype of TimeDurationType 
     * 			according to section 3.14.6, rule 2.2.4 in XML Schema, Part 1.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Time Interval</em>' containment reference.
     * @see #setTimeInterval(TimeIntervalLengthType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_TimeInterval()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='timeInterval' namespace='##targetNamespace'"
     * @generated
     */
    TimeIntervalLengthType getTimeInterval();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getTimeInterval <em>Time Interval</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Time Interval</em>' containment reference.
     * @see #getTimeInterval()
     * @generated
     */
    void setTimeInterval(TimeIntervalLengthType value);

    /**
     * Returns the value of the '<em><b>Time Node</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * "TimeNode" is a zero dimensional temporal topology primitive, 
     * 			expresses a position in topological time, and is a start and an end of time edge, which represents states of time.
     * 			Time node may be isolated. However, it cannot describe the ordering relationships with other primitives. 
     * 			An isolated node may not be an element of any temporal topology complex.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Time Node</em>' containment reference.
     * @see #setTimeNode(TimeNodeType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_TimeNode()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='TimeNode' namespace='##targetNamespace' affiliation='_TimeTopologyPrimitive'"
     * @generated
     */
    TimeNodeType getTimeNode();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getTimeNode <em>Time Node</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Time Node</em>' containment reference.
     * @see #getTimeNode()
     * @generated
     */
    void setTimeNode(TimeNodeType value);

    /**
     * Returns the value of the '<em><b>Time Ordinal Era</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Time Ordinal Era</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Time Ordinal Era</em>' containment reference.
     * @see #setTimeOrdinalEra(TimeOrdinalEraType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_TimeOrdinalEra()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='TimeOrdinalEra' namespace='##targetNamespace'"
     * @generated
     */
    TimeOrdinalEraType getTimeOrdinalEra();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getTimeOrdinalEra <em>Time Ordinal Era</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Time Ordinal Era</em>' containment reference.
     * @see #getTimeOrdinalEra()
     * @generated
     */
    void setTimeOrdinalEra(TimeOrdinalEraType value);

    /**
     * Returns the value of the '<em><b>Time Ordinal Reference System</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Time Ordinal Reference System</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Time Ordinal Reference System</em>' containment reference.
     * @see #setTimeOrdinalReferenceSystem(TimeOrdinalReferenceSystemType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_TimeOrdinalReferenceSystem()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='TimeOrdinalReferenceSystem' namespace='##targetNamespace' affiliation='_TimeReferenceSystem'"
     * @generated
     */
    TimeOrdinalReferenceSystemType getTimeOrdinalReferenceSystem();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getTimeOrdinalReferenceSystem <em>Time Ordinal Reference System</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Time Ordinal Reference System</em>' containment reference.
     * @see #getTimeOrdinalReferenceSystem()
     * @generated
     */
    void setTimeOrdinalReferenceSystem(TimeOrdinalReferenceSystemType value);

    /**
     * Returns the value of the '<em><b>Time Period</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Time Period</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Time Period</em>' containment reference.
     * @see #setTimePeriod(TimePeriodType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_TimePeriod()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='TimePeriod' namespace='##targetNamespace' affiliation='_TimeGeometricPrimitive'"
     * @generated
     */
    TimePeriodType getTimePeriod();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getTimePeriod <em>Time Period</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Time Period</em>' containment reference.
     * @see #getTimePeriod()
     * @generated
     */
    void setTimePeriod(TimePeriodType value);

    /**
     * Returns the value of the '<em><b>Time Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Direct representation of a temporal position
     * <!-- end-model-doc -->
     * @return the value of the '<em>Time Position</em>' containment reference.
     * @see #setTimePosition(TimePositionType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_TimePosition()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='timePosition' namespace='##targetNamespace'"
     * @generated
     */
    TimePositionType getTimePosition();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getTimePosition <em>Time Position</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Time Position</em>' containment reference.
     * @see #getTimePosition()
     * @generated
     */
    void setTimePosition(TimePositionType value);

    /**
     * Returns the value of the '<em><b>Time Topology Complex</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This element represents temporal topology complex. It shall be the connected acyclic directed graph composed of time nodes and time edges.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Time Topology Complex</em>' containment reference.
     * @see #setTimeTopologyComplex(TimeTopologyComplexType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_TimeTopologyComplex()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='TimeTopologyComplex' namespace='##targetNamespace' affiliation='_TimeComplex'"
     * @generated
     */
    TimeTopologyComplexType getTimeTopologyComplex();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getTimeTopologyComplex <em>Time Topology Complex</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Time Topology Complex</em>' containment reference.
     * @see #getTimeTopologyComplex()
     * @generated
     */
    void setTimeTopologyComplex(TimeTopologyComplexType value);

    /**
     * Returns the value of the '<em><b>Tin</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Tin</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Tin</em>' containment reference.
     * @see #setTin(TinType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Tin()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Tin' namespace='##targetNamespace' affiliation='TriangulatedSurface'"
     * @generated
     */
    TinType getTin();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getTin <em>Tin</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Tin</em>' containment reference.
     * @see #getTin()
     * @generated
     */
    void setTin(TinType value);

    /**
     * Returns the value of the '<em><b>Triangulated Surface</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Triangulated Surface</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Triangulated Surface</em>' containment reference.
     * @see #setTriangulatedSurface(TriangulatedSurfaceType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_TriangulatedSurface()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='TriangulatedSurface' namespace='##targetNamespace' affiliation='Surface'"
     * @generated
     */
    TriangulatedSurfaceType getTriangulatedSurface();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getTriangulatedSurface <em>Triangulated Surface</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Triangulated Surface</em>' containment reference.
     * @see #getTriangulatedSurface()
     * @generated
     */
    void setTriangulatedSurface(TriangulatedSurfaceType value);

    /**
     * Returns the value of the '<em><b>Topo Complex</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Topo Complex</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Topo Complex</em>' containment reference.
     * @see #setTopoComplex(TopoComplexType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_TopoComplex()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='TopoComplex' namespace='##targetNamespace' affiliation='_Topology'"
     * @generated
     */
    TopoComplexType getTopoComplex();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getTopoComplex <em>Topo Complex</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Topo Complex</em>' containment reference.
     * @see #getTopoComplex()
     * @generated
     */
    void setTopoComplex(TopoComplexType value);

    /**
     * Returns the value of the '<em><b>Topo Complex Property</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Topo Complex Property</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Topo Complex Property</em>' containment reference.
     * @see #setTopoComplexProperty(TopoComplexMemberType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_TopoComplexProperty()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='topoComplexProperty' namespace='##targetNamespace'"
     * @generated
     */
    TopoComplexMemberType getTopoComplexProperty();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getTopoComplexProperty <em>Topo Complex Property</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Topo Complex Property</em>' containment reference.
     * @see #getTopoComplexProperty()
     * @generated
     */
    void setTopoComplexProperty(TopoComplexMemberType value);

    /**
     * Returns the value of the '<em><b>Topo Curve</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Topo Curve</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Topo Curve</em>' containment reference.
     * @see #setTopoCurve(TopoCurveType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_TopoCurve()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='TopoCurve' namespace='##targetNamespace'"
     * @generated
     */
    TopoCurveType getTopoCurve();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getTopoCurve <em>Topo Curve</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Topo Curve</em>' containment reference.
     * @see #getTopoCurve()
     * @generated
     */
    void setTopoCurve(TopoCurveType value);

    /**
     * Returns the value of the '<em><b>Topo Curve Property</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Topo Curve Property</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Topo Curve Property</em>' containment reference.
     * @see #setTopoCurveProperty(TopoCurvePropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_TopoCurveProperty()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='topoCurveProperty' namespace='##targetNamespace'"
     * @generated
     */
    TopoCurvePropertyType getTopoCurveProperty();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getTopoCurveProperty <em>Topo Curve Property</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Topo Curve Property</em>' containment reference.
     * @see #getTopoCurveProperty()
     * @generated
     */
    void setTopoCurveProperty(TopoCurvePropertyType value);

    /**
     * Returns the value of the '<em><b>Topology Style</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Topology Style</em>' containment reference.
     * @see #setTopologyStyle(TopologyStylePropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_TopologyStyle()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='topologyStyle' namespace='##targetNamespace'"
     * @generated
     */
    TopologyStylePropertyType getTopologyStyle();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getTopologyStyle <em>Topology Style</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Topology Style</em>' containment reference.
     * @see #getTopologyStyle()
     * @generated
     */
    void setTopologyStyle(TopologyStylePropertyType value);

    /**
     * Returns the value of the '<em><b>Topology Style1</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The style descriptor for topologies of a feature. Describes individual topology elements styles.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Topology Style1</em>' containment reference.
     * @see #setTopologyStyle1(TopologyStyleType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_TopologyStyle1()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='TopologyStyle' namespace='##targetNamespace' affiliation='_GML'"
     * @generated
     */
    TopologyStyleType getTopologyStyle1();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getTopologyStyle1 <em>Topology Style1</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Topology Style1</em>' containment reference.
     * @see #getTopologyStyle1()
     * @generated
     */
    void setTopologyStyle1(TopologyStyleType value);

    /**
     * Returns the value of the '<em><b>Topo Point</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Topo Point</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Topo Point</em>' containment reference.
     * @see #setTopoPoint(TopoPointType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_TopoPoint()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='TopoPoint' namespace='##targetNamespace'"
     * @generated
     */
    TopoPointType getTopoPoint();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getTopoPoint <em>Topo Point</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Topo Point</em>' containment reference.
     * @see #getTopoPoint()
     * @generated
     */
    void setTopoPoint(TopoPointType value);

    /**
     * Returns the value of the '<em><b>Topo Point Property</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Topo Point Property</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Topo Point Property</em>' containment reference.
     * @see #setTopoPointProperty(TopoPointPropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_TopoPointProperty()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='topoPointProperty' namespace='##targetNamespace'"
     * @generated
     */
    TopoPointPropertyType getTopoPointProperty();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getTopoPointProperty <em>Topo Point Property</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Topo Point Property</em>' containment reference.
     * @see #getTopoPointProperty()
     * @generated
     */
    void setTopoPointProperty(TopoPointPropertyType value);

    /**
     * Returns the value of the '<em><b>Topo Primitive Member</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Topo Primitive Member</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Topo Primitive Member</em>' containment reference.
     * @see #setTopoPrimitiveMember(TopoPrimitiveMemberType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_TopoPrimitiveMember()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='topoPrimitiveMember' namespace='##targetNamespace'"
     * @generated
     */
    TopoPrimitiveMemberType getTopoPrimitiveMember();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getTopoPrimitiveMember <em>Topo Primitive Member</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Topo Primitive Member</em>' containment reference.
     * @see #getTopoPrimitiveMember()
     * @generated
     */
    void setTopoPrimitiveMember(TopoPrimitiveMemberType value);

    /**
     * Returns the value of the '<em><b>Topo Primitive Members</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Topo Primitive Members</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Topo Primitive Members</em>' containment reference.
     * @see #setTopoPrimitiveMembers(TopoPrimitiveArrayAssociationType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_TopoPrimitiveMembers()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='topoPrimitiveMembers' namespace='##targetNamespace'"
     * @generated
     */
    TopoPrimitiveArrayAssociationType getTopoPrimitiveMembers();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getTopoPrimitiveMembers <em>Topo Primitive Members</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Topo Primitive Members</em>' containment reference.
     * @see #getTopoPrimitiveMembers()
     * @generated
     */
    void setTopoPrimitiveMembers(TopoPrimitiveArrayAssociationType value);

    /**
     * Returns the value of the '<em><b>Topo Solid</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Topo Solid</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Topo Solid</em>' containment reference.
     * @see #setTopoSolid(TopoSolidType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_TopoSolid()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='TopoSolid' namespace='##targetNamespace' affiliation='_TopoPrimitive'"
     * @generated
     */
    TopoSolidType getTopoSolid();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getTopoSolid <em>Topo Solid</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Topo Solid</em>' containment reference.
     * @see #getTopoSolid()
     * @generated
     */
    void setTopoSolid(TopoSolidType value);

    /**
     * Returns the value of the '<em><b>Topo Surface</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Topo Surface</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Topo Surface</em>' containment reference.
     * @see #setTopoSurface(TopoSurfaceType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_TopoSurface()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='TopoSurface' namespace='##targetNamespace'"
     * @generated
     */
    TopoSurfaceType getTopoSurface();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getTopoSurface <em>Topo Surface</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Topo Surface</em>' containment reference.
     * @see #getTopoSurface()
     * @generated
     */
    void setTopoSurface(TopoSurfaceType value);

    /**
     * Returns the value of the '<em><b>Topo Surface Property</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Topo Surface Property</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Topo Surface Property</em>' containment reference.
     * @see #setTopoSurfaceProperty(TopoSurfacePropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_TopoSurfaceProperty()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='topoSurfaceProperty' namespace='##targetNamespace'"
     * @generated
     */
    TopoSurfacePropertyType getTopoSurfaceProperty();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getTopoSurfaceProperty <em>Topo Surface Property</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Topo Surface Property</em>' containment reference.
     * @see #getTopoSurfaceProperty()
     * @generated
     */
    void setTopoSurfaceProperty(TopoSurfacePropertyType value);

    /**
     * Returns the value of the '<em><b>Topo Volume</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Topo Volume</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Topo Volume</em>' containment reference.
     * @see #setTopoVolume(TopoVolumeType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_TopoVolume()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='TopoVolume' namespace='##targetNamespace'"
     * @generated
     */
    TopoVolumeType getTopoVolume();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getTopoVolume <em>Topo Volume</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Topo Volume</em>' containment reference.
     * @see #getTopoVolume()
     * @generated
     */
    void setTopoVolume(TopoVolumeType value);

    /**
     * Returns the value of the '<em><b>Topo Volume Property</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Topo Volume Property</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Topo Volume Property</em>' containment reference.
     * @see #setTopoVolumeProperty(TopoVolumePropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_TopoVolumeProperty()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='topoVolumeProperty' namespace='##targetNamespace'"
     * @generated
     */
    TopoVolumePropertyType getTopoVolumeProperty();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getTopoVolumeProperty <em>Topo Volume Property</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Topo Volume Property</em>' containment reference.
     * @see #getTopoVolumeProperty()
     * @generated
     */
    void setTopoVolumeProperty(TopoVolumePropertyType value);

    /**
     * Returns the value of the '<em><b>Track</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Track</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Track</em>' containment reference.
     * @see #setTrack(TrackType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Track()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='track' namespace='##targetNamespace' affiliation='history'"
     * @generated
     */
    TrackType getTrack();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getTrack <em>Track</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Track</em>' containment reference.
     * @see #getTrack()
     * @generated
     */
    void setTrack(TrackType value);

    /**
     * Returns the value of the '<em><b>Transformation</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Transformation</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Transformation</em>' containment reference.
     * @see #setTransformation(TransformationType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Transformation()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Transformation' namespace='##targetNamespace' affiliation='_GeneralTransformation'"
     * @generated
     */
    TransformationType getTransformation();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getTransformation <em>Transformation</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Transformation</em>' containment reference.
     * @see #getTransformation()
     * @generated
     */
    void setTransformation(TransformationType value);

    /**
     * Returns the value of the '<em><b>Transformation Ref</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Transformation Ref</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Transformation Ref</em>' containment reference.
     * @see #setTransformationRef(TransformationRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_TransformationRef()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='transformationRef' namespace='##targetNamespace'"
     * @generated
     */
    TransformationRefType getTransformationRef();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getTransformationRef <em>Transformation Ref</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Transformation Ref</em>' containment reference.
     * @see #getTransformationRef()
     * @generated
     */
    void setTransformationRef(TransformationRefType value);

    /**
     * Returns the value of the '<em><b>Triangle</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Triangle</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Triangle</em>' containment reference.
     * @see #setTriangle(TriangleType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Triangle()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Triangle' namespace='##targetNamespace' affiliation='_SurfacePatch'"
     * @generated
     */
    TriangleType getTriangle();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getTriangle <em>Triangle</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Triangle</em>' containment reference.
     * @see #getTriangle()
     * @generated
     */
    void setTriangle(TriangleType value);

    /**
     * Returns the value of the '<em><b>Triangle Patches</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This property element contains a list of
     *    triangle patches. The order of the patches is significant and 
     *    shall be preserved when processing the list.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Triangle Patches</em>' containment reference.
     * @see #setTrianglePatches(TrianglePatchArrayPropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_TrianglePatches()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='trianglePatches' namespace='##targetNamespace' affiliation='patches'"
     * @generated
     */
    TrianglePatchArrayPropertyType getTrianglePatches();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getTrianglePatches <em>Triangle Patches</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Triangle Patches</em>' containment reference.
     * @see #getTrianglePatches()
     * @generated
     */
    void setTrianglePatches(TrianglePatchArrayPropertyType value);

    /**
     * Returns the value of the '<em><b>Tuple List</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Tuple List</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Tuple List</em>' containment reference.
     * @see #setTupleList(CoordinatesType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_TupleList()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='tupleList' namespace='##targetNamespace'"
     * @generated
     */
    CoordinatesType getTupleList();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getTupleList <em>Tuple List</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Tuple List</em>' containment reference.
     * @see #getTupleList()
     * @generated
     */
    void setTupleList(CoordinatesType value);

    /**
     * Returns the value of the '<em><b>Unit Of Measure</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Unit Of Measure</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Unit Of Measure</em>' containment reference.
     * @see #setUnitOfMeasure(UnitOfMeasureType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_UnitOfMeasure()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='unitOfMeasure' namespace='##targetNamespace'"
     * @generated
     */
    UnitOfMeasureType getUnitOfMeasure();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getUnitOfMeasure <em>Unit Of Measure</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Unit Of Measure</em>' containment reference.
     * @see #getUnitOfMeasure()
     * @generated
     */
    void setUnitOfMeasure(UnitOfMeasureType value);

    /**
     * Returns the value of the '<em><b>User Defined CS</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>User Defined CS</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>User Defined CS</em>' containment reference.
     * @see #setUserDefinedCS(UserDefinedCSType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_UserDefinedCS()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='UserDefinedCS' namespace='##targetNamespace' affiliation='_CoordinateSystem'"
     * @generated
     */
    UserDefinedCSType getUserDefinedCS();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getUserDefinedCS <em>User Defined CS</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>User Defined CS</em>' containment reference.
     * @see #getUserDefinedCS()
     * @generated
     */
    void setUserDefinedCS(UserDefinedCSType value);

    /**
     * Returns the value of the '<em><b>User Defined CS Ref</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>User Defined CS Ref</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>User Defined CS Ref</em>' containment reference.
     * @see #setUserDefinedCSRef(UserDefinedCSRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_UserDefinedCSRef()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='userDefinedCSRef' namespace='##targetNamespace'"
     * @generated
     */
    UserDefinedCSRefType getUserDefinedCSRef();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getUserDefinedCSRef <em>User Defined CS Ref</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>User Defined CS Ref</em>' containment reference.
     * @see #getUserDefinedCSRef()
     * @generated
     */
    void setUserDefinedCSRef(UserDefinedCSRefType value);

    /**
     * Returns the value of the '<em><b>Uses Axis</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Association to a coordinate system axis. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Uses Axis</em>' containment reference.
     * @see #setUsesAxis(CoordinateSystemAxisRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_UsesAxis()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='usesAxis' namespace='##targetNamespace'"
     * @generated
     */
    CoordinateSystemAxisRefType getUsesAxis();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getUsesAxis <em>Uses Axis</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Uses Axis</em>' containment reference.
     * @see #getUsesAxis()
     * @generated
     */
    void setUsesAxis(CoordinateSystemAxisRefType value);

    /**
     * Returns the value of the '<em><b>Uses Cartesian CS</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Association to the Cartesian coordinate system used by this CRS. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Uses Cartesian CS</em>' containment reference.
     * @see #setUsesCartesianCS(CartesianCSRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_UsesCartesianCS()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='usesCartesianCS' namespace='##targetNamespace'"
     * @generated
     */
    CartesianCSRefType getUsesCartesianCS();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getUsesCartesianCS <em>Uses Cartesian CS</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Uses Cartesian CS</em>' containment reference.
     * @see #getUsesCartesianCS()
     * @generated
     */
    void setUsesCartesianCS(CartesianCSRefType value);

    /**
     * Returns the value of the '<em><b>Uses CS</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Association to the coordinate system used by this CRS. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Uses CS</em>' containment reference.
     * @see #setUsesCS(CoordinateSystemRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_UsesCS()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='usesCS' namespace='##targetNamespace'"
     * @generated
     */
    CoordinateSystemRefType getUsesCS();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getUsesCS <em>Uses CS</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Uses CS</em>' containment reference.
     * @see #getUsesCS()
     * @generated
     */
    void setUsesCS(CoordinateSystemRefType value);

    /**
     * Returns the value of the '<em><b>Uses Ellipsoid</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Association to the ellipsoid used by this geodetic datum. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Uses Ellipsoid</em>' containment reference.
     * @see #setUsesEllipsoid(EllipsoidRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_UsesEllipsoid()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='usesEllipsoid' namespace='##targetNamespace'"
     * @generated
     */
    EllipsoidRefType getUsesEllipsoid();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getUsesEllipsoid <em>Uses Ellipsoid</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Uses Ellipsoid</em>' containment reference.
     * @see #getUsesEllipsoid()
     * @generated
     */
    void setUsesEllipsoid(EllipsoidRefType value);

    /**
     * Returns the value of the '<em><b>Uses Ellipsoidal CS</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Association to the ellipsoidal coordinate system used by this CRS. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Uses Ellipsoidal CS</em>' containment reference.
     * @see #setUsesEllipsoidalCS(EllipsoidalCSRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_UsesEllipsoidalCS()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='usesEllipsoidalCS' namespace='##targetNamespace'"
     * @generated
     */
    EllipsoidalCSRefType getUsesEllipsoidalCS();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getUsesEllipsoidalCS <em>Uses Ellipsoidal CS</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Uses Ellipsoidal CS</em>' containment reference.
     * @see #getUsesEllipsoidalCS()
     * @generated
     */
    void setUsesEllipsoidalCS(EllipsoidalCSRefType value);

    /**
     * Returns the value of the '<em><b>Uses Engineering Datum</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Association to the engineering datum used by this CRS. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Uses Engineering Datum</em>' containment reference.
     * @see #setUsesEngineeringDatum(EngineeringDatumRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_UsesEngineeringDatum()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='usesEngineeringDatum' namespace='##targetNamespace'"
     * @generated
     */
    EngineeringDatumRefType getUsesEngineeringDatum();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getUsesEngineeringDatum <em>Uses Engineering Datum</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Uses Engineering Datum</em>' containment reference.
     * @see #getUsesEngineeringDatum()
     * @generated
     */
    void setUsesEngineeringDatum(EngineeringDatumRefType value);

    /**
     * Returns the value of the '<em><b>Uses Geodetic Datum</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Association to the geodetic datum used by this CRS. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Uses Geodetic Datum</em>' containment reference.
     * @see #setUsesGeodeticDatum(GeodeticDatumRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_UsesGeodeticDatum()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='usesGeodeticDatum' namespace='##targetNamespace'"
     * @generated
     */
    GeodeticDatumRefType getUsesGeodeticDatum();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getUsesGeodeticDatum <em>Uses Geodetic Datum</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Uses Geodetic Datum</em>' containment reference.
     * @see #getUsesGeodeticDatum()
     * @generated
     */
    void setUsesGeodeticDatum(GeodeticDatumRefType value);

    /**
     * Returns the value of the '<em><b>Uses Image Datum</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Association to the image datum used by this CRS. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Uses Image Datum</em>' containment reference.
     * @see #setUsesImageDatum(ImageDatumRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_UsesImageDatum()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='usesImageDatum' namespace='##targetNamespace'"
     * @generated
     */
    ImageDatumRefType getUsesImageDatum();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getUsesImageDatum <em>Uses Image Datum</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Uses Image Datum</em>' containment reference.
     * @see #getUsesImageDatum()
     * @generated
     */
    void setUsesImageDatum(ImageDatumRefType value);

    /**
     * Returns the value of the '<em><b>Uses Method</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Association to the operation method used by this coordinate operation. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Uses Method</em>' containment reference.
     * @see #setUsesMethod(OperationMethodRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_UsesMethod()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='usesMethod' namespace='##targetNamespace'"
     * @generated
     */
    OperationMethodRefType getUsesMethod();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getUsesMethod <em>Uses Method</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Uses Method</em>' containment reference.
     * @see #getUsesMethod()
     * @generated
     */
    void setUsesMethod(OperationMethodRefType value);

    /**
     * Returns the value of the '<em><b>Uses Oblique Cartesian CS</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Association to the oblique Cartesian coordinate system used by this CRS.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Uses Oblique Cartesian CS</em>' containment reference.
     * @see #setUsesObliqueCartesianCS(ObliqueCartesianCSRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_UsesObliqueCartesianCS()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='usesObliqueCartesianCS' namespace='##targetNamespace'"
     * @generated
     */
    ObliqueCartesianCSRefType getUsesObliqueCartesianCS();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getUsesObliqueCartesianCS <em>Uses Oblique Cartesian CS</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Uses Oblique Cartesian CS</em>' containment reference.
     * @see #getUsesObliqueCartesianCS()
     * @generated
     */
    void setUsesObliqueCartesianCS(ObliqueCartesianCSRefType value);

    /**
     * Returns the value of the '<em><b>Uses Operation</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Association to the operation applied to the specified ordinates. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Uses Operation</em>' containment reference.
     * @see #setUsesOperation(OperationRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_UsesOperation()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='usesOperation' namespace='##targetNamespace'"
     * @generated
     */
    OperationRefType getUsesOperation();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getUsesOperation <em>Uses Operation</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Uses Operation</em>' containment reference.
     * @see #getUsesOperation()
     * @generated
     */
    void setUsesOperation(OperationRefType value);

    /**
     * Returns the value of the '<em><b>Uses Parameter</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Association to an operation parameter or parameter group used by this operation method. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Uses Parameter</em>' containment reference.
     * @see #setUsesParameter(AbstractGeneralOperationParameterRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_UsesParameter()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='usesParameter' namespace='##targetNamespace'"
     * @generated
     */
    AbstractGeneralOperationParameterRefType getUsesParameter();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getUsesParameter <em>Uses Parameter</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Uses Parameter</em>' containment reference.
     * @see #getUsesParameter()
     * @generated
     */
    void setUsesParameter(AbstractGeneralOperationParameterRefType value);

    /**
     * Returns the value of the '<em><b>Uses Prime Meridian</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Association to the prime meridian used by this geodetic datum. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Uses Prime Meridian</em>' containment reference.
     * @see #setUsesPrimeMeridian(PrimeMeridianRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_UsesPrimeMeridian()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='usesPrimeMeridian' namespace='##targetNamespace'"
     * @generated
     */
    PrimeMeridianRefType getUsesPrimeMeridian();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getUsesPrimeMeridian <em>Uses Prime Meridian</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Uses Prime Meridian</em>' containment reference.
     * @see #getUsesPrimeMeridian()
     * @generated
     */
    void setUsesPrimeMeridian(PrimeMeridianRefType value);

    /**
     * Returns the value of the '<em><b>Uses Single Operation</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Association to a single operation. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Uses Single Operation</em>' containment reference.
     * @see #setUsesSingleOperation(SingleOperationRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_UsesSingleOperation()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='usesSingleOperation' namespace='##targetNamespace'"
     * @generated
     */
    SingleOperationRefType getUsesSingleOperation();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getUsesSingleOperation <em>Uses Single Operation</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Uses Single Operation</em>' containment reference.
     * @see #getUsesSingleOperation()
     * @generated
     */
    void setUsesSingleOperation(SingleOperationRefType value);

    /**
     * Returns the value of the '<em><b>Uses Spherical CS</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Association to the spherical coordinate system used by this CRS.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Uses Spherical CS</em>' containment reference.
     * @see #setUsesSphericalCS(SphericalCSRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_UsesSphericalCS()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='usesSphericalCS' namespace='##targetNamespace'"
     * @generated
     */
    SphericalCSRefType getUsesSphericalCS();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getUsesSphericalCS <em>Uses Spherical CS</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Uses Spherical CS</em>' containment reference.
     * @see #getUsesSphericalCS()
     * @generated
     */
    void setUsesSphericalCS(SphericalCSRefType value);

    /**
     * Returns the value of the '<em><b>Uses Temporal CS</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Association to the temporal coordinate system used by this CRS. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Uses Temporal CS</em>' containment reference.
     * @see #setUsesTemporalCS(TemporalCSRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_UsesTemporalCS()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='usesTemporalCS' namespace='##targetNamespace'"
     * @generated
     */
    TemporalCSRefType getUsesTemporalCS();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getUsesTemporalCS <em>Uses Temporal CS</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Uses Temporal CS</em>' containment reference.
     * @see #getUsesTemporalCS()
     * @generated
     */
    void setUsesTemporalCS(TemporalCSRefType value);

    /**
     * Returns the value of the '<em><b>Uses Temporal Datum</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Association to the temporal datum used by this CRS. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Uses Temporal Datum</em>' containment reference.
     * @see #setUsesTemporalDatum(TemporalDatumRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_UsesTemporalDatum()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='usesTemporalDatum' namespace='##targetNamespace'"
     * @generated
     */
    TemporalDatumRefType getUsesTemporalDatum();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getUsesTemporalDatum <em>Uses Temporal Datum</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Uses Temporal Datum</em>' containment reference.
     * @see #getUsesTemporalDatum()
     * @generated
     */
    void setUsesTemporalDatum(TemporalDatumRefType value);

    /**
     * Returns the value of the '<em><b>Uses Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Composition association to a parameter value used by this coordinate operation. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Uses Value</em>' containment reference.
     * @see #setUsesValue(ParameterValueType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_UsesValue()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='usesValue' namespace='##targetNamespace'"
     * @generated
     */
    ParameterValueType getUsesValue();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getUsesValue <em>Uses Value</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Uses Value</em>' containment reference.
     * @see #getUsesValue()
     * @generated
     */
    void setUsesValue(ParameterValueType value);

    /**
     * Returns the value of the '<em><b>Uses Vertical CS</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Association to the vertical coordinate system used by this CRS. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Uses Vertical CS</em>' containment reference.
     * @see #setUsesVerticalCS(VerticalCSRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_UsesVerticalCS()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='usesVerticalCS' namespace='##targetNamespace'"
     * @generated
     */
    VerticalCSRefType getUsesVerticalCS();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getUsesVerticalCS <em>Uses Vertical CS</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Uses Vertical CS</em>' containment reference.
     * @see #getUsesVerticalCS()
     * @generated
     */
    void setUsesVerticalCS(VerticalCSRefType value);

    /**
     * Returns the value of the '<em><b>Uses Vertical Datum</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Association to the vertical datum used by this CRS. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Uses Vertical Datum</em>' containment reference.
     * @see #setUsesVerticalDatum(VerticalDatumRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_UsesVerticalDatum()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='usesVerticalDatum' namespace='##targetNamespace'"
     * @generated
     */
    VerticalDatumRefType getUsesVerticalDatum();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getUsesVerticalDatum <em>Uses Vertical Datum</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Uses Vertical Datum</em>' containment reference.
     * @see #getUsesVerticalDatum()
     * @generated
     */
    void setUsesVerticalDatum(VerticalDatumRefType value);

    /**
     * Returns the value of the '<em><b>Using</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This element contains or points to a description of a sensor, instrument or procedure used for the observation
     * <!-- end-model-doc -->
     * @return the value of the '<em>Using</em>' containment reference.
     * @see #setUsing(FeaturePropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Using()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='using' namespace='##targetNamespace'"
     * @generated
     */
    FeaturePropertyType getUsing();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getUsing <em>Using</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Using</em>' containment reference.
     * @see #getUsing()
     * @generated
     */
    void setUsing(FeaturePropertyType value);

    /**
     * Returns the value of the '<em><b>Valid Area</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Area or region in which this CRS object is valid.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Valid Area</em>' containment reference.
     * @see #setValidArea(ExtentType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_ValidArea()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='validArea' namespace='##targetNamespace'"
     * @generated
     */
    ExtentType getValidArea();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getValidArea <em>Valid Area</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Valid Area</em>' containment reference.
     * @see #getValidArea()
     * @generated
     */
    void setValidArea(ExtentType value);

    /**
     * Returns the value of the '<em><b>Valid Time</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Valid Time</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Valid Time</em>' containment reference.
     * @see #setValidTime(TimePrimitivePropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_ValidTime()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='validTime' namespace='##targetNamespace'"
     * @generated
     */
    TimePrimitivePropertyType getValidTime();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getValidTime <em>Valid Time</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Valid Time</em>' containment reference.
     * @see #getValidTime()
     * @generated
     */
    void setValidTime(TimePrimitivePropertyType value);

    /**
     * Returns the value of the '<em><b>Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Numeric value of an operation parameter, with its associated unit of measure. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Value</em>' containment reference.
     * @see #setValue(MeasureType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Value()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='value' namespace='##targetNamespace'"
     * @generated
     */
    MeasureType getValue();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getValue <em>Value</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Value</em>' containment reference.
     * @see #getValue()
     * @generated
     */
    void setValue(MeasureType value);

    /**
     * Returns the value of the '<em><b>Value Array</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * A Value Array is used for homogeneous arrays of primitive and aggregate values.   _ScalarValueList is preferred for arrays of Scalar Values since this is more efficient.  Since "choice" is not available for attribute groups, an external constraint (e.g. Schematron) would be required to enforce the selection of only one of these through schema validation
     * <!-- end-model-doc -->
     * @return the value of the '<em>Value Array</em>' containment reference.
     * @see #setValueArray(ValueArrayType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_ValueArray()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='ValueArray' namespace='##targetNamespace' affiliation='CompositeValue'"
     * @generated
     */
    ValueArrayType getValueArray();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getValueArray <em>Value Array</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Value Array</em>' containment reference.
     * @see #getValueArray()
     * @generated
     */
    void setValueArray(ValueArrayType value);

    /**
     * Returns the value of the '<em><b>Value Component</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Element which refers to, or contains, a Value.  This version is used in CompositeValues.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Value Component</em>' containment reference.
     * @see #setValueComponent(ValuePropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_ValueComponent()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='valueComponent' namespace='##targetNamespace'"
     * @generated
     */
    ValuePropertyType getValueComponent();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getValueComponent <em>Value Component</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Value Component</em>' containment reference.
     * @see #getValueComponent()
     * @generated
     */
    void setValueComponent(ValuePropertyType value);

    /**
     * Returns the value of the '<em><b>Value Components</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Element which refers to, or contains, a set of homogeneously typed Values.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Value Components</em>' containment reference.
     * @see #setValueComponents(ValueArrayPropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_ValueComponents()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='valueComponents' namespace='##targetNamespace'"
     * @generated
     */
    ValueArrayPropertyType getValueComponents();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getValueComponents <em>Value Components</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Value Components</em>' containment reference.
     * @see #getValueComponents()
     * @generated
     */
    void setValueComponents(ValueArrayPropertyType value);

    /**
     * Returns the value of the '<em><b>Value File</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Reference to a file or a part of a file containing one or more parameter values, each numeric value with its associated unit of measure. When referencing a part of a file, that file must contain multiple identified parts, such as an XML encoded document. Furthermore, the referenced file or part of a file can reference another part of the same or different files, as allowed in XML documents. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Value File</em>' attribute.
     * @see #setValueFile(String)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_ValueFile()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.AnyURI" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='valueFile' namespace='##targetNamespace'"
     * @generated
     */
    String getValueFile();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getValueFile <em>Value File</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Value File</em>' attribute.
     * @see #getValueFile()
     * @generated
     */
    void setValueFile(String value);

    /**
     * Returns the value of the '<em><b>Value List</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Ordered sequence of two or more numeric values of an operation parameter list, where each value has the same associated unit of measure. An element of this type contains a space-separated sequence of double values. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Value List</em>' containment reference.
     * @see #setValueList(MeasureListType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_ValueList()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='valueList' namespace='##targetNamespace'"
     * @generated
     */
    MeasureListType getValueList();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getValueList <em>Value List</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Value List</em>' containment reference.
     * @see #getValueList()
     * @generated
     */
    void setValueList(MeasureListType value);

    /**
     * Returns the value of the '<em><b>Value Of Parameter</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Association to the operation parameter that this is a value of. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Value Of Parameter</em>' containment reference.
     * @see #setValueOfParameter(OperationParameterRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_ValueOfParameter()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='valueOfParameter' namespace='##targetNamespace'"
     * @generated
     */
    OperationParameterRefType getValueOfParameter();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getValueOfParameter <em>Value Of Parameter</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Value Of Parameter</em>' containment reference.
     * @see #getValueOfParameter()
     * @generated
     */
    void setValueOfParameter(OperationParameterRefType value);

    /**
     * Returns the value of the '<em><b>Value Property</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Element which refers to, or contains, a Value
     * <!-- end-model-doc -->
     * @return the value of the '<em>Value Property</em>' containment reference.
     * @see #setValueProperty(ValuePropertyType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_ValueProperty()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='valueProperty' namespace='##targetNamespace'"
     * @generated
     */
    ValuePropertyType getValueProperty();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getValueProperty <em>Value Property</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Value Property</em>' containment reference.
     * @see #getValueProperty()
     * @generated
     */
    void setValueProperty(ValuePropertyType value);

    /**
     * Returns the value of the '<em><b>Values Of Group</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Association to the operation parameter group for which this element provides parameter values. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Values Of Group</em>' containment reference.
     * @see #setValuesOfGroup(OperationParameterGroupRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_ValuesOfGroup()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='valuesOfGroup' namespace='##targetNamespace'"
     * @generated
     */
    OperationParameterGroupRefType getValuesOfGroup();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getValuesOfGroup <em>Values Of Group</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Values Of Group</em>' containment reference.
     * @see #getValuesOfGroup()
     * @generated
     */
    void setValuesOfGroup(OperationParameterGroupRefType value);

    /**
     * Returns the value of the '<em><b>Vector</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Vector</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Vector</em>' containment reference.
     * @see #setVector(VectorType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Vector()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='vector' namespace='##targetNamespace'"
     * @generated
     */
    VectorType getVector();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getVector <em>Vector</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Vector</em>' containment reference.
     * @see #getVector()
     * @generated
     */
    void setVector(VectorType value);

    /**
     * Returns the value of the '<em><b>Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Identifier of the version of the associated codeSpace or code, as specified by the codeSpace or code authority. This version is included only when the "code" or "codeSpace" uses versions. When appropriate, the version is identified by the effective date, coded using ISO 8601 date format.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Version</em>' attribute.
     * @see #setVersion(String)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Version()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='version' namespace='##targetNamespace'"
     * @generated
     */
    String getVersion();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getVersion <em>Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Version</em>' attribute.
     * @see #getVersion()
     * @generated
     */
    void setVersion(String value);

    /**
     * Returns the value of the '<em><b>Vertical CRS</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Vertical CRS</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Vertical CRS</em>' containment reference.
     * @see #setVerticalCRS(VerticalCRSType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_VerticalCRS()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='VerticalCRS' namespace='##targetNamespace' affiliation='_CoordinateReferenceSystem'"
     * @generated
     */
    VerticalCRSType getVerticalCRS();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getVerticalCRS <em>Vertical CRS</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Vertical CRS</em>' containment reference.
     * @see #getVerticalCRS()
     * @generated
     */
    void setVerticalCRS(VerticalCRSType value);

    /**
     * Returns the value of the '<em><b>Vertical CRS Ref</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Vertical CRS Ref</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Vertical CRS Ref</em>' containment reference.
     * @see #setVerticalCRSRef(VerticalCRSRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_VerticalCRSRef()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='verticalCRSRef' namespace='##targetNamespace'"
     * @generated
     */
    VerticalCRSRefType getVerticalCRSRef();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getVerticalCRSRef <em>Vertical CRS Ref</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Vertical CRS Ref</em>' containment reference.
     * @see #getVerticalCRSRef()
     * @generated
     */
    void setVerticalCRSRef(VerticalCRSRefType value);

    /**
     * Returns the value of the '<em><b>Vertical CS</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Vertical CS</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Vertical CS</em>' containment reference.
     * @see #setVerticalCS(VerticalCSType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_VerticalCS()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='VerticalCS' namespace='##targetNamespace' affiliation='_CoordinateSystem'"
     * @generated
     */
    VerticalCSType getVerticalCS();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getVerticalCS <em>Vertical CS</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Vertical CS</em>' containment reference.
     * @see #getVerticalCS()
     * @generated
     */
    void setVerticalCS(VerticalCSType value);

    /**
     * Returns the value of the '<em><b>Vertical CS Ref</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Vertical CS Ref</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Vertical CS Ref</em>' containment reference.
     * @see #setVerticalCSRef(VerticalCSRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_VerticalCSRef()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='verticalCSRef' namespace='##targetNamespace'"
     * @generated
     */
    VerticalCSRefType getVerticalCSRef();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getVerticalCSRef <em>Vertical CS Ref</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Vertical CS Ref</em>' containment reference.
     * @see #getVerticalCSRef()
     * @generated
     */
    void setVerticalCSRef(VerticalCSRefType value);

    /**
     * Returns the value of the '<em><b>Vertical Datum</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Vertical Datum</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Vertical Datum</em>' containment reference.
     * @see #setVerticalDatum(VerticalDatumType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_VerticalDatum()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='VerticalDatum' namespace='##targetNamespace' affiliation='_Datum'"
     * @generated
     */
    VerticalDatumType getVerticalDatum();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getVerticalDatum <em>Vertical Datum</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Vertical Datum</em>' containment reference.
     * @see #getVerticalDatum()
     * @generated
     */
    void setVerticalDatum(VerticalDatumType value);

    /**
     * Returns the value of the '<em><b>Vertical Datum Ref</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Vertical Datum Ref</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Vertical Datum Ref</em>' containment reference.
     * @see #setVerticalDatumRef(VerticalDatumRefType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_VerticalDatumRef()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='verticalDatumRef' namespace='##targetNamespace'"
     * @generated
     */
    VerticalDatumRefType getVerticalDatumRef();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getVerticalDatumRef <em>Vertical Datum Ref</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Vertical Datum Ref</em>' containment reference.
     * @see #getVerticalDatumRef()
     * @generated
     */
    void setVerticalDatumRef(VerticalDatumRefType value);

    /**
     * Returns the value of the '<em><b>Vertical Datum Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Vertical Datum Type</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Vertical Datum Type</em>' containment reference.
     * @see #setVerticalDatumType(VerticalDatumTypeType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_VerticalDatumType()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='verticalDatumType' namespace='##targetNamespace'"
     * @generated
     */
    VerticalDatumTypeType getVerticalDatumType();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getVerticalDatumType <em>Vertical Datum Type</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Vertical Datum Type</em>' containment reference.
     * @see #getVerticalDatumType()
     * @generated
     */
    void setVerticalDatumType(VerticalDatumTypeType value);

    /**
     * Returns the value of the '<em><b>Vertical Extent</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * An interval defining the vertical spatial domain of this object.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Vertical Extent</em>' containment reference.
     * @see #setVerticalExtent(EnvelopeType)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_VerticalExtent()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='verticalExtent' namespace='##targetNamespace'"
     * @generated
     */
    EnvelopeType getVerticalExtent();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getVerticalExtent <em>Vertical Extent</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Vertical Extent</em>' containment reference.
     * @see #getVerticalExtent()
     * @generated
     */
    void setVerticalExtent(EnvelopeType value);

    /**
     * Returns the value of the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Database handle for the object.  It is of XML type ID, so is constrained to be unique in the XML document within which it occurs.  An external identifier for the object in the form of a URI may be constructed using standard XML and XPointer methods.  This is done by concatenating the URI for the document, a fragment separator, and the value of the id attribute.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Id</em>' attribute.
     * @see #setId(String)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Id()
     * @model id="true" dataType="org.eclipse.emf.ecore.xml.type.ID"
     *        extendedMetaData="kind='attribute' name='id' namespace='##targetNamespace'"
     * @generated
     */
    String getId();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getId <em>Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Id</em>' attribute.
     * @see #getId()
     * @generated
     */
    void setId(String value);

    /**
     * Returns the value of the '<em><b>Remote Schema</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Reference to an XML Schema fragment that specifies the content model of the propertys value. This is in conformance with the XML Schema Section 4.14 Referencing Schemas from Elsewhere.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Remote Schema</em>' attribute.
     * @see #setRemoteSchema(String)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_RemoteSchema()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
     *        extendedMetaData="kind='attribute' name='remoteSchema' namespace='##targetNamespace'"
     * @generated
     */
    String getRemoteSchema();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getRemoteSchema <em>Remote Schema</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Remote Schema</em>' attribute.
     * @see #getRemoteSchema()
     * @generated
     */
    void setRemoteSchema(String value);

    /**
     * Returns the value of the '<em><b>Transform</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Defines the geometric transformation of entities. There is no particular grammar defined for this value.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Transform</em>' attribute.
     * @see #setTransform(String)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Transform()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='transform' namespace='##targetNamespace'"
     * @generated
     */
    String getTransform();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getTransform <em>Transform</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Transform</em>' attribute.
     * @see #getTransform()
     * @generated
     */
    void setTransform(String value);

    /**
     * Returns the value of the '<em><b>Uom</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Identifier of the unit of measure used for this coordinate system axis. The value of this coordinate in a coordinate tuple shall be recorded using this unit of measure, whenever those coordinates use a coordinate reference system that uses a coordinate system that uses this axis.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Uom</em>' attribute.
     * @see #setUom(String)
     * @see net.opengis.gml311.Gml311Package#getDocumentRoot_Uom()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
     *        extendedMetaData="kind='attribute' name='uom' namespace='##targetNamespace'"
     * @generated
     */
    String getUom();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DocumentRoot#getUom <em>Uom</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Uom</em>' attribute.
     * @see #getUom()
     * @generated
     */
    void setUom(String value);

} // DocumentRoot
