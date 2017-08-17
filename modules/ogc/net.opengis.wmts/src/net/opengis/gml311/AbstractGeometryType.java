/**
 */
package net.opengis.gml311;

import java.math.BigInteger;

import java.util.List;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Abstract Geometry Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * All geometry elements are derived directly or indirectly from this abstract supertype. A geometry element may 
 * 			have an identifying attribute ("gml:id"), a name (attribute "name") and a description (attribute "description"). It may be associated 
 * 			with a spatial reference system (attribute "srsName"). The following rules shall be adhered: - Every geometry type shall derive 
 * 			from this abstract type. - Every geometry element (i.e. an element of a geometry type) shall be directly or indirectly in the 
 * 			substitution group of _Geometry.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.AbstractGeometryType#getAxisLabels <em>Axis Labels</em>}</li>
 *   <li>{@link net.opengis.gml311.AbstractGeometryType#getGid <em>Gid</em>}</li>
 *   <li>{@link net.opengis.gml311.AbstractGeometryType#getSrsDimension <em>Srs Dimension</em>}</li>
 *   <li>{@link net.opengis.gml311.AbstractGeometryType#getSrsName <em>Srs Name</em>}</li>
 *   <li>{@link net.opengis.gml311.AbstractGeometryType#getUomLabels <em>Uom Labels</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getAbstractGeometryType()
 * @model abstract="true"
 *        extendedMetaData="name='AbstractGeometryType' kind='elementOnly'"
 * @generated
 */
public interface AbstractGeometryType extends AbstractGMLType {
    /**
     * Returns the value of the '<em><b>Axis Labels</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Ordered list of labels for all the axes of this CRS. The gml:axisAbbrev value should be used for these axis 
     * 				labels, after spaces and forbiddden characters are removed. When the srsName attribute is included, this attribute is optional. 
     * 				When the srsName attribute is omitted, this attribute shall also be omitted.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Axis Labels</em>' attribute.
     * @see #setAxisLabels(List)
     * @see net.opengis.gml311.Gml311Package#getAbstractGeometryType_AxisLabels()
     * @model dataType="net.opengis.gml311.NCNameList" many="false"
     *        extendedMetaData="kind='attribute' name='axisLabels'"
     * @generated
     */
    List<String> getAxisLabels();

    /**
     * Sets the value of the '{@link net.opengis.gml311.AbstractGeometryType#getAxisLabels <em>Axis Labels</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Axis Labels</em>' attribute.
     * @see #getAxisLabels()
     * @generated
     */
    void setAxisLabels(List<String> value);

    /**
     * Returns the value of the '<em><b>Gid</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This attribute is included for backward compatibility with GML 2 and is deprecated with GML 3. 
     * 						This identifer is superceded by "gml:id" inherited from AbstractGMLType. The attribute "gid" should not be used 
     * 						anymore and may be deleted in future versions of GML without further notice.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Gid</em>' attribute.
     * @see #setGid(String)
     * @see net.opengis.gml311.Gml311Package#getAbstractGeometryType_Gid()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='gid'"
     * @generated
     */
    String getGid();

    /**
     * Sets the value of the '{@link net.opengis.gml311.AbstractGeometryType#getGid <em>Gid</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Gid</em>' attribute.
     * @see #getGid()
     * @generated
     */
    void setGid(String value);

    /**
     * Returns the value of the '<em><b>Srs Dimension</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The "srsDimension" is the length of coordinate sequence (the number of entries in the list). This dimension is 
     * 				specified by the coordinate reference system. When the srsName attribute is omitted, this attribute shall be omitted.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Srs Dimension</em>' attribute.
     * @see #setSrsDimension(BigInteger)
     * @see net.opengis.gml311.Gml311Package#getAbstractGeometryType_SrsDimension()
     * @model dataType="org.eclipse.emf.ecore.xml.type.PositiveInteger"
     *        extendedMetaData="kind='attribute' name='srsDimension'"
     * @generated
     */
    BigInteger getSrsDimension();

    /**
     * Sets the value of the '{@link net.opengis.gml311.AbstractGeometryType#getSrsDimension <em>Srs Dimension</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Srs Dimension</em>' attribute.
     * @see #getSrsDimension()
     * @generated
     */
    void setSrsDimension(BigInteger value);

    /**
     * Returns the value of the '<em><b>Srs Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * In general this reference points to a CRS instance of gml:CoordinateReferenceSystemType 
     * 				(see coordinateReferenceSystems.xsd). For well known references it is not required that the CRS description exists at the 
     * 				location the URI points to. If no srsName attribute is given, the CRS must be specified as part of the larger context this 
     * 				geometry element is part of, e.g. a geometric element like point, curve, etc. It is expected that this attribute will be specified 
     * 				at the direct position level only in rare cases.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Srs Name</em>' attribute.
     * @see #setSrsName(String)
     * @see net.opengis.gml311.Gml311Package#getAbstractGeometryType_SrsName()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
     *        extendedMetaData="kind='attribute' name='srsName'"
     * @generated
     */
    String getSrsName();

    /**
     * Sets the value of the '{@link net.opengis.gml311.AbstractGeometryType#getSrsName <em>Srs Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Srs Name</em>' attribute.
     * @see #getSrsName()
     * @generated
     */
    void setSrsName(String value);

    /**
     * Returns the value of the '<em><b>Uom Labels</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Ordered list of unit of measure (uom) labels for all the axes of this CRS. The value of the string in the 
     * 				gml:catalogSymbol should be used for this uom labels, after spaces and forbiddden characters are removed. When the 
     * 				axisLabels attribute is included, this attribute shall also be included. When the axisLabels attribute is omitted, this attribute 
     * 				shall also be omitted.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Uom Labels</em>' attribute.
     * @see #setUomLabels(List)
     * @see net.opengis.gml311.Gml311Package#getAbstractGeometryType_UomLabels()
     * @model dataType="net.opengis.gml311.NCNameList" many="false"
     *        extendedMetaData="kind='attribute' name='uomLabels'"
     * @generated
     */
    List<String> getUomLabels();

    /**
     * Sets the value of the '{@link net.opengis.gml311.AbstractGeometryType#getUomLabels <em>Uom Labels</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Uom Labels</em>' attribute.
     * @see #getUomLabels()
     * @generated
     */
    void setUomLabels(List<String> value);

} // AbstractGeometryType
