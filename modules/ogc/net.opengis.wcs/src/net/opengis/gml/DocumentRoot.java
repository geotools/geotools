/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.gml;

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
 * <ul>
 *   <li>{@link net.opengis.gml.DocumentRoot#getMixed <em>Mixed</em>}</li>
 *   <li>{@link net.opengis.gml.DocumentRoot#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}</li>
 *   <li>{@link net.opengis.gml.DocumentRoot#getXSISchemaLocation <em>XSI Schema Location</em>}</li>
 *   <li>{@link net.opengis.gml.DocumentRoot#getGeometricPrimitive <em>Geometric Primitive</em>}</li>
 *   <li>{@link net.opengis.gml.DocumentRoot#getGeometry <em>Geometry</em>}</li>
 *   <li>{@link net.opengis.gml.DocumentRoot#getGML <em>GML</em>}</li>
 *   <li>{@link net.opengis.gml.DocumentRoot#getObject <em>Object</em>}</li>
 *   <li>{@link net.opengis.gml.DocumentRoot#getMetaData <em>Meta Data</em>}</li>
 *   <li>{@link net.opengis.gml.DocumentRoot#getRing <em>Ring</em>}</li>
 *   <li>{@link net.opengis.gml.DocumentRoot#getSurface <em>Surface</em>}</li>
 *   <li>{@link net.opengis.gml.DocumentRoot#getBoundedBy <em>Bounded By</em>}</li>
 *   <li>{@link net.opengis.gml.DocumentRoot#getDescription <em>Description</em>}</li>
 *   <li>{@link net.opengis.gml.DocumentRoot#getEnvelope <em>Envelope</em>}</li>
 *   <li>{@link net.opengis.gml.DocumentRoot#getEnvelopeWithTimePeriod <em>Envelope With Time Period</em>}</li>
 *   <li>{@link net.opengis.gml.DocumentRoot#getExterior <em>Exterior</em>}</li>
 *   <li>{@link net.opengis.gml.DocumentRoot#getGrid <em>Grid</em>}</li>
 *   <li>{@link net.opengis.gml.DocumentRoot#getInterior <em>Interior</em>}</li>
 *   <li>{@link net.opengis.gml.DocumentRoot#getLinearRing <em>Linear Ring</em>}</li>
 *   <li>{@link net.opengis.gml.DocumentRoot#getMetaDataProperty <em>Meta Data Property</em>}</li>
 *   <li>{@link net.opengis.gml.DocumentRoot#getName <em>Name</em>}</li>
 *   <li>{@link net.opengis.gml.DocumentRoot#getPolygon <em>Polygon</em>}</li>
 *   <li>{@link net.opengis.gml.DocumentRoot#getPos <em>Pos</em>}</li>
 *   <li>{@link net.opengis.gml.DocumentRoot#getRectifiedGrid <em>Rectified Grid</em>}</li>
 *   <li>{@link net.opengis.gml.DocumentRoot#getTimePosition <em>Time Position</em>}</li>
 *   <li>{@link net.opengis.gml.DocumentRoot#getId <em>Id</em>}</li>
 *   <li>{@link net.opengis.gml.DocumentRoot#getRemoteSchema <em>Remote Schema</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.gml.GmlPackage#getDocumentRoot()
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
	 * @see net.opengis.gml.GmlPackage#getDocumentRoot_Mixed()
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
	 * @see net.opengis.gml.GmlPackage#getDocumentRoot_XMLNSPrefixMap()
	 * @model mapType="org.eclipse.emf.ecore.EStringToStringMapEntry" keyType="java.lang.String" valueType="java.lang.String" transient="true"
	 *        extendedMetaData="kind='attribute' name='xmlns:prefix'"
	 * @generated
	 */
    EMap getXMLNSPrefixMap();

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
	 * @see net.opengis.gml.GmlPackage#getDocumentRoot_XSISchemaLocation()
	 * @model mapType="org.eclipse.emf.ecore.EStringToStringMapEntry" keyType="java.lang.String" valueType="java.lang.String" transient="true"
	 *        extendedMetaData="kind='attribute' name='xsi:schemaLocation'"
	 * @generated
	 */
    EMap getXSISchemaLocation();

    /**
	 * Returns the value of the '<em><b>Geometric Primitive</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The "_GeometricPrimitive" element is the abstract head of the substituition group for all (pre- and user-defined) geometric primitives.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Geometric Primitive</em>' containment reference.
	 * @see net.opengis.gml.GmlPackage#getDocumentRoot_GeometricPrimitive()
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
	 * The "_Geometry" element is the abstract head of the substituition group for all geometry elements of GML 3. This includes pre-defined and user-defined geometry elements. Any geometry element must be a direct or indirect extension/restriction of AbstractGeometryType and must be directly or indirectly in the substitution group of "_Geometry".
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Geometry</em>' containment reference.
	 * @see net.opengis.gml.GmlPackage#getDocumentRoot_Geometry()
	 * @model containment="true" upper="-2" transient="true" changeable="false" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='_Geometry' namespace='##targetNamespace' affiliation='_GML'"
	 * @generated
	 */
    AbstractGeometryType getGeometry();

    /**
	 * Returns the value of the '<em><b>GML</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Global element which acts as the head of a substitution group that may include any element which is a GML feature, object, geometry or complex value
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>GML</em>' containment reference.
	 * @see net.opengis.gml.GmlPackage#getDocumentRoot_GML()
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
	 * This abstract element is the head of a substitutionGroup hierararchy which may contain either simpleContent or complexContent elements. It is used to assert the model position of "class" elements declared in other GML schemas.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Object</em>' containment reference.
	 * @see net.opengis.gml.GmlPackage#getDocumentRoot_Object()
	 * @model containment="true" upper="-2" transient="true" changeable="false" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='_Object' namespace='##targetNamespace'"
	 * @generated
	 */
    EObject getObject();

    /**
	 * Returns the value of the '<em><b>Meta Data</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Abstract element which acts as the head of a substitution group for packages of MetaData properties.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Meta Data</em>' containment reference.
	 * @see net.opengis.gml.GmlPackage#getDocumentRoot_MetaData()
	 * @model containment="true" upper="-2" transient="true" changeable="false" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='_MetaData' namespace='##targetNamespace' affiliation='_Object'"
	 * @generated
	 */
    AbstractMetaDataType getMetaData();

    /**
	 * Returns the value of the '<em><b>Ring</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The "_Ring" element is the abstract head of the substituition group for all closed boundaries of a surface patch.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Ring</em>' containment reference.
	 * @see net.opengis.gml.GmlPackage#getDocumentRoot_Ring()
	 * @model containment="true" upper="-2" transient="true" changeable="false" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='_Ring' namespace='##targetNamespace' affiliation='_Geometry'"
	 * @generated
	 */
    AbstractRingType getRing();

    /**
	 * Returns the value of the '<em><b>Surface</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The "_Surface" element is the abstract head of the substituition group for all (continuous) surface elements.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Surface</em>' containment reference.
	 * @see net.opengis.gml.GmlPackage#getDocumentRoot_Surface()
	 * @model containment="true" upper="-2" transient="true" changeable="false" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='_Surface' namespace='##targetNamespace' affiliation='_GeometricPrimitive'"
	 * @generated
	 */
    AbstractSurfaceType getSurface();

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
	 * @see net.opengis.gml.GmlPackage#getDocumentRoot_BoundedBy()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='boundedBy' namespace='##targetNamespace'"
	 * @generated
	 */
    BoundingShapeType getBoundedBy();

    /**
	 * Sets the value of the '{@link net.opengis.gml.DocumentRoot#getBoundedBy <em>Bounded By</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Bounded By</em>' containment reference.
	 * @see #getBoundedBy()
	 * @generated
	 */
    void setBoundedBy(BoundingShapeType value);

    /**
	 * Returns the value of the '<em><b>Description</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Contains a simple text description of the object, or refers to an external description.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Description</em>' containment reference.
	 * @see #setDescription(StringOrRefType)
	 * @see net.opengis.gml.GmlPackage#getDocumentRoot_Description()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='description' namespace='##targetNamespace'"
	 * @generated
	 */
    StringOrRefType getDescription();

    /**
	 * Sets the value of the '{@link net.opengis.gml.DocumentRoot#getDescription <em>Description</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Description</em>' containment reference.
	 * @see #getDescription()
	 * @generated
	 */
    void setDescription(StringOrRefType value);

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
	 * @see net.opengis.gml.GmlPackage#getDocumentRoot_Envelope()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='Envelope' namespace='##targetNamespace' affiliation='_Geometry'"
	 * @generated
	 */
    EnvelopeType getEnvelope();

    /**
	 * Sets the value of the '{@link net.opengis.gml.DocumentRoot#getEnvelope <em>Envelope</em>}' containment reference.
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
	 * @see net.opengis.gml.GmlPackage#getDocumentRoot_EnvelopeWithTimePeriod()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='EnvelopeWithTimePeriod' namespace='##targetNamespace' affiliation='Envelope'"
	 * @generated
	 */
    EnvelopeWithTimePeriodType getEnvelopeWithTimePeriod();

    /**
	 * Sets the value of the '{@link net.opengis.gml.DocumentRoot#getEnvelopeWithTimePeriod <em>Envelope With Time Period</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Envelope With Time Period</em>' containment reference.
	 * @see #getEnvelopeWithTimePeriod()
	 * @generated
	 */
    void setEnvelopeWithTimePeriod(EnvelopeWithTimePeriodType value);

    /**
	 * Returns the value of the '<em><b>Exterior</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * A boundary of a surface consists of a number of rings. In the normal 2D case, one of these rings is distinguished as being the exterior boundary. In a general manifold this is not always possible, in which case all boundaries shall be listed as interior boundaries, and the exterior will be empty.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Exterior</em>' containment reference.
	 * @see #setExterior(AbstractRingPropertyType)
	 * @see net.opengis.gml.GmlPackage#getDocumentRoot_Exterior()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='exterior' namespace='##targetNamespace'"
	 * @generated
	 */
    AbstractRingPropertyType getExterior();

    /**
	 * Sets the value of the '{@link net.opengis.gml.DocumentRoot#getExterior <em>Exterior</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Exterior</em>' containment reference.
	 * @see #getExterior()
	 * @generated
	 */
    void setExterior(AbstractRingPropertyType value);

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
	 * @see net.opengis.gml.GmlPackage#getDocumentRoot_Grid()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='Grid' namespace='##targetNamespace' affiliation='_Geometry'"
	 * @generated
	 */
    GridType getGrid();

    /**
	 * Sets the value of the '{@link net.opengis.gml.DocumentRoot#getGrid <em>Grid</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Grid</em>' containment reference.
	 * @see #getGrid()
	 * @generated
	 */
    void setGrid(GridType value);

    /**
	 * Returns the value of the '<em><b>Interior</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * A boundary of a surface consists of a number of rings. The "interior" rings seperate the surface / surface patch from the area enclosed by the rings.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Interior</em>' containment reference.
	 * @see #setInterior(AbstractRingPropertyType)
	 * @see net.opengis.gml.GmlPackage#getDocumentRoot_Interior()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='interior' namespace='##targetNamespace'"
	 * @generated
	 */
    AbstractRingPropertyType getInterior();

    /**
	 * Sets the value of the '{@link net.opengis.gml.DocumentRoot#getInterior <em>Interior</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Interior</em>' containment reference.
	 * @see #getInterior()
	 * @generated
	 */
    void setInterior(AbstractRingPropertyType value);

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
	 * @see net.opengis.gml.GmlPackage#getDocumentRoot_LinearRing()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='LinearRing' namespace='##targetNamespace' affiliation='_Ring'"
	 * @generated
	 */
    LinearRingType getLinearRing();

    /**
	 * Sets the value of the '{@link net.opengis.gml.DocumentRoot#getLinearRing <em>Linear Ring</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Linear Ring</em>' containment reference.
	 * @see #getLinearRing()
	 * @generated
	 */
    void setLinearRing(LinearRingType value);

    /**
	 * Returns the value of the '<em><b>Meta Data Property</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Contains or refers to a metadata package that contains metadata properties.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Meta Data Property</em>' containment reference.
	 * @see #setMetaDataProperty(MetaDataPropertyType)
	 * @see net.opengis.gml.GmlPackage#getDocumentRoot_MetaDataProperty()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='metaDataProperty' namespace='##targetNamespace'"
	 * @generated
	 */
    MetaDataPropertyType getMetaDataProperty();

    /**
	 * Sets the value of the '{@link net.opengis.gml.DocumentRoot#getMetaDataProperty <em>Meta Data Property</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Meta Data Property</em>' containment reference.
	 * @see #getMetaDataProperty()
	 * @generated
	 */
    void setMetaDataProperty(MetaDataPropertyType value);

    /**
	 * Returns the value of the '<em><b>Name</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Identifier for the object, normally a descriptive name. An object may have several names, typically assigned by different authorities.  The authority for a name is indicated by the value of its (optional) codeSpace attribute.  The name may or may not be unique, as determined by the rules of the organization responsible for the codeSpace.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Name</em>' containment reference.
	 * @see #setName(CodeType)
	 * @see net.opengis.gml.GmlPackage#getDocumentRoot_Name()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='name' namespace='##targetNamespace'"
	 * @generated
	 */
    CodeType getName();

    /**
	 * Sets the value of the '{@link net.opengis.gml.DocumentRoot#getName <em>Name</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' containment reference.
	 * @see #getName()
	 * @generated
	 */
    void setName(CodeType value);

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
	 * @see net.opengis.gml.GmlPackage#getDocumentRoot_Polygon()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='Polygon' namespace='##targetNamespace' affiliation='_Surface'"
	 * @generated
	 */
    PolygonType getPolygon();

    /**
	 * Sets the value of the '{@link net.opengis.gml.DocumentRoot#getPolygon <em>Polygon</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Polygon</em>' containment reference.
	 * @see #getPolygon()
	 * @generated
	 */
    void setPolygon(PolygonType value);

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
	 * @see net.opengis.gml.GmlPackage#getDocumentRoot_Pos()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='pos' namespace='##targetNamespace'"
	 * @generated
	 */
    DirectPositionType getPos();

    /**
	 * Sets the value of the '{@link net.opengis.gml.DocumentRoot#getPos <em>Pos</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Pos</em>' containment reference.
	 * @see #getPos()
	 * @generated
	 */
    void setPos(DirectPositionType value);

    /**
	 * Returns the value of the '<em><b>Rectified Grid</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Rectified Grid</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Rectified Grid</em>' containment reference.
	 * @see #setRectifiedGrid(RectifiedGridType)
	 * @see net.opengis.gml.GmlPackage#getDocumentRoot_RectifiedGrid()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='RectifiedGrid' namespace='##targetNamespace' affiliation='Grid'"
	 * @generated
	 */
    RectifiedGridType getRectifiedGrid();

    /**
	 * Sets the value of the '{@link net.opengis.gml.DocumentRoot#getRectifiedGrid <em>Rectified Grid</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Rectified Grid</em>' containment reference.
	 * @see #getRectifiedGrid()
	 * @generated
	 */
    void setRectifiedGrid(RectifiedGridType value);

    /**
	 * Returns the value of the '<em><b>Time Position</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Direct representation of a temporal position.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Time Position</em>' containment reference.
	 * @see #setTimePosition(TimePositionType)
	 * @see net.opengis.gml.GmlPackage#getDocumentRoot_TimePosition()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='timePosition' namespace='##targetNamespace'"
	 * @generated
	 */
    TimePositionType getTimePosition();

    /**
	 * Sets the value of the '{@link net.opengis.gml.DocumentRoot#getTimePosition <em>Time Position</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Time Position</em>' containment reference.
	 * @see #getTimePosition()
	 * @generated
	 */
    void setTimePosition(TimePositionType value);

    /**
	 * Returns the value of the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Database handle for the object.  It is of XML type “ID”, so is constrained to be unique in the XML document within which it occurs.  An external identifier for the object in the form of a URI may be constructed using standard XML and XPointer methods.  This is done by concatenating the URI for the document, a fragment separator “#”, and the value of the id attribute.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Id</em>' attribute.
	 * @see #setId(String)
	 * @see net.opengis.gml.GmlPackage#getDocumentRoot_Id()
	 * @model id="true" dataType="org.eclipse.emf.ecore.xml.type.ID"
	 *        extendedMetaData="kind='attribute' name='id' namespace='##targetNamespace'"
	 * @generated
	 */
    String getId();

    /**
	 * Sets the value of the '{@link net.opengis.gml.DocumentRoot#getId <em>Id</em>}' attribute.
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
	 * Reference to an XML Schema fragment that specifies the content model of the property’s value. This is in conformance with the XML Schema Section 4.14 Referencing Schemas from Elsewhere.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Remote Schema</em>' attribute.
	 * @see #setRemoteSchema(String)
	 * @see net.opengis.gml.GmlPackage#getDocumentRoot_RemoteSchema()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
	 *        extendedMetaData="kind='attribute' name='remoteSchema' namespace='##targetNamespace'"
	 * @generated
	 */
    String getRemoteSchema();

    /**
	 * Sets the value of the '{@link net.opengis.gml.DocumentRoot#getRemoteSchema <em>Remote Schema</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Remote Schema</em>' attribute.
	 * @see #getRemoteSchema()
	 * @generated
	 */
    void setRemoteSchema(String value);

} // DocumentRoot
