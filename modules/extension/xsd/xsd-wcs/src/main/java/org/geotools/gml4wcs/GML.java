package org.geotools.gml4wcs;

import java.util.Set;

import javax.xml.namespace.QName;

import org.geotools.xlink.XLINK;
import org.geotools.xml.XSD;

/**
 * This interface contains the qualified names of all the types,elements, and
 * attributes in the http://www.opengis.net/gml schema.
 * 
 * @generated
 */
public final class GML extends XSD {

    /** singleton instance */
    private static final GML instance = new GML();

    /**
     * Returns the singleton instance.
     */
    public static final GML getInstance() {
        return instance;
    }

    /**
     * private constructor
     */
    private GML() {
    }

    protected void addDependencies(Set dependencies) {
        super.addDependencies(dependencies);
        
        dependencies.add(XLINK.getInstance());
    }

    /**
     * Returns 'http://www.opengis.net/gml'.
     */
    public String getNamespaceURI() {
        return NAMESPACE;
    }

    /**
     * Returns the location of 'gml4wcs.xsd.'.
     */
    public String getSchemaLocation() {
        return getClass().getResource("gml4wcs.xsd").toString();
    }

    /** @generated */
    public static final String NAMESPACE = "http://www.opengis.net/gml";

    /* Type Definitions */
    /** @generated */
    public static final QName AbstractGeometricPrimitiveType = new QName(
            "http://www.opengis.net/gml", "AbstractGeometricPrimitiveType");

    /** @generated */
    public static final QName AbstractGeometryBaseType = new QName(
            "http://www.opengis.net/gml", "AbstractGeometryBaseType");

    /** @generated */
    public static final QName AbstractGeometryType = new QName(
            "http://www.opengis.net/gml", "AbstractGeometryType");

    /** @generated */
    public static final QName AbstractGMLType = new QName(
            "http://www.opengis.net/gml", "AbstractGMLType");

    /** @generated */
    public static final QName AbstractMetaDataType = new QName(
            "http://www.opengis.net/gml", "AbstractMetaDataType");

    /** @generated */
    public static final QName AbstractRingPropertyType = new QName(
            "http://www.opengis.net/gml", "AbstractRingPropertyType");

    /** @generated */
    public static final QName AbstractRingType = new QName(
            "http://www.opengis.net/gml", "AbstractRingType");

    /** @generated */
    public static final QName AbstractSurfaceType = new QName(
            "http://www.opengis.net/gml", "AbstractSurfaceType");

    /** @generated */
    public static final QName BoundingShapeType = new QName(
            "http://www.opengis.net/gml", "BoundingShapeType");

    /** @generated */
    public static final QName CodeListType = new QName(
            "http://www.opengis.net/gml", "CodeListType");

    /** @generated */
    public static final QName CodeType = new QName(
            "http://www.opengis.net/gml", "CodeType");

    /** @generated */
    public static final QName DirectPositionType = new QName(
            "http://www.opengis.net/gml", "DirectPositionType");

    /** @generated */
    public static final QName doubleList = new QName(
            "http://www.opengis.net/gml", "doubleList");

    /** @generated */
    public static final QName EnvelopeType = new QName(
            "http://www.opengis.net/gml", "EnvelopeType");

    /** @generated */
    public static final QName EnvelopeWithTimePeriodType = new QName(
            "http://www.opengis.net/gml", "EnvelopeWithTimePeriodType");

    /** @generated */
    public static final QName GridEnvelopeType = new QName(
            "http://www.opengis.net/gml", "GridEnvelopeType");

    /** @generated */
    public static final QName GridLimitsType = new QName(
            "http://www.opengis.net/gml", "GridLimitsType");

    /** @generated */
    public static final QName GridType = new QName(
            "http://www.opengis.net/gml", "GridType");

    /** @generated */
    public static final QName integerList = new QName(
            "http://www.opengis.net/gml", "integerList");

    /** @generated */
    public static final QName LinearRingType = new QName(
            "http://www.opengis.net/gml", "LinearRingType");

    /** @generated */
    public static final QName MetaDataPropertyType = new QName(
            "http://www.opengis.net/gml", "MetaDataPropertyType");

    /** @generated */
    public static final QName NameList = new QName(
            "http://www.opengis.net/gml", "NameList");

    /** @generated */
    public static final QName PointType = new QName(
            "http://www.opengis.net/gml", "PointType");

    /** @generated */
    public static final QName PolygonType = new QName(
            "http://www.opengis.net/gml", "PolygonType");

    /** @generated */
    public static final QName RectifiedGridType = new QName(
            "http://www.opengis.net/gml", "RectifiedGridType");

    /** @generated */
    public static final QName ReferenceType = new QName(
            "http://www.opengis.net/gml", "ReferenceType");

    /** @generated */
    public static final QName StringOrRefType = new QName(
            "http://www.opengis.net/gml", "StringOrRefType");

    /** @generated */
    public static final QName TemporalPositionType = new QName(
            "http://www.opengis.net/gml", "TemporalPositionType");

    /** @generated */
    public static final QName TimeDurationType = new QName(
            "http://www.opengis.net/gml", "TimeDurationType");

    /** @generated */
    public static final QName TimeIndeterminateValueType = new QName(
            "http://www.opengis.net/gml", "TimeIndeterminateValueType");

    /** @generated */
    public static final QName TimePositionType = new QName(
            "http://www.opengis.net/gml", "TimePositionType");

    /** @generated */
    public static final QName VectorType = new QName(
            "http://www.opengis.net/gml", "VectorType");

    /* Elements */
    /** @generated */
    public static final QName _GeometricPrimitive = new QName(
            "http://www.opengis.net/gml", "_GeometricPrimitive");

    /** @generated */
    public static final QName _Geometry = new QName(
            "http://www.opengis.net/gml", "_Geometry");

    /** @generated */
    public static final QName _GML = new QName("http://www.opengis.net/gml",
            "_GML");

    /** @generated */
    public static final QName _MetaData = new QName(
            "http://www.opengis.net/gml", "_MetaData");

    /** @generated */
    public static final QName _Object = new QName("http://www.opengis.net/gml",
            "_Object");

    /** @generated */
    public static final QName _Ring = new QName("http://www.opengis.net/gml",
            "_Ring");

    /** @generated */
    public static final QName _Surface = new QName(
            "http://www.opengis.net/gml", "_Surface");

    /** @generated */
    public static final QName boundedBy = new QName(
            "http://www.opengis.net/gml", "boundedBy");

    /** @generated */
    public static final QName description = new QName(
            "http://www.opengis.net/gml", "description");

    /** @generated */
    public static final QName Envelope = new QName(
            "http://www.opengis.net/gml", "Envelope");

    /** @generated */
    public static final QName EnvelopeWithTimePeriod = new QName(
            "http://www.opengis.net/gml", "EnvelopeWithTimePeriod");

    /** @generated */
    public static final QName exterior = new QName(
            "http://www.opengis.net/gml", "exterior");

    /** @generated */
    public static final QName Grid = new QName("http://www.opengis.net/gml",
            "Grid");

    /** @generated */
    public static final QName interior = new QName(
            "http://www.opengis.net/gml", "interior");

    /** @generated */
    public static final QName LinearRing = new QName(
            "http://www.opengis.net/gml", "LinearRing");

    /** @generated */
    public static final QName metaDataProperty = new QName(
            "http://www.opengis.net/gml", "metaDataProperty");

    /** @generated */
    public static final QName name = new QName("http://www.opengis.net/gml",
            "name");

    /** @generated */
    public static final QName Polygon = new QName("http://www.opengis.net/gml",
            "Polygon");

    /** @generated */
    public static final QName pos = new QName("http://www.opengis.net/gml",
            "pos");

    /** @generated */
    public static final QName RectifiedGrid = new QName(
            "http://www.opengis.net/gml", "RectifiedGrid");

    /** @generated */
    public static final QName timePosition = new QName(
            "http://www.opengis.net/gml", "timePosition");

    /* Attributes */
    /** @generated */
    public static final QName id = new QName("http://www.opengis.net/gml", "id");

    /** @generated */
    public static final QName remoteSchema = new QName(
            "http://www.opengis.net/gml", "remoteSchema");

}
