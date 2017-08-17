/**
 */
package net.opengis.gml311;

import java.math.BigInteger;

import java.util.List;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Envelope Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Envelope defines an extent using a pair of positions defining opposite corners in arbitrary dimensions. The first direct 
 * 			position is the "lower corner" (a coordinate position consisting of all the minimal ordinates for each dimension for all points within the envelope), 
 * 			the second one the "upper corner" (a coordinate position consisting of all the maximal ordinates for each dimension for all points within the 
 * 			envelope).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.EnvelopeType#getLowerCorner <em>Lower Corner</em>}</li>
 *   <li>{@link net.opengis.gml311.EnvelopeType#getUpperCorner <em>Upper Corner</em>}</li>
 *   <li>{@link net.opengis.gml311.EnvelopeType#getCoord <em>Coord</em>}</li>
 *   <li>{@link net.opengis.gml311.EnvelopeType#getPos <em>Pos</em>}</li>
 *   <li>{@link net.opengis.gml311.EnvelopeType#getCoordinates <em>Coordinates</em>}</li>
 *   <li>{@link net.opengis.gml311.EnvelopeType#getAxisLabels <em>Axis Labels</em>}</li>
 *   <li>{@link net.opengis.gml311.EnvelopeType#getSrsDimension <em>Srs Dimension</em>}</li>
 *   <li>{@link net.opengis.gml311.EnvelopeType#getSrsName <em>Srs Name</em>}</li>
 *   <li>{@link net.opengis.gml311.EnvelopeType#getUomLabels <em>Uom Labels</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getEnvelopeType()
 * @model extendedMetaData="name='EnvelopeType' kind='elementOnly'"
 * @generated
 */
public interface EnvelopeType extends EObject {
    /**
     * Returns the value of the '<em><b>Lower Corner</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Lower Corner</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Lower Corner</em>' containment reference.
     * @see #setLowerCorner(DirectPositionType)
     * @see net.opengis.gml311.Gml311Package#getEnvelopeType_LowerCorner()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='lowerCorner' namespace='##targetNamespace'"
     * @generated
     */
    DirectPositionType getLowerCorner();

    /**
     * Sets the value of the '{@link net.opengis.gml311.EnvelopeType#getLowerCorner <em>Lower Corner</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Lower Corner</em>' containment reference.
     * @see #getLowerCorner()
     * @generated
     */
    void setLowerCorner(DirectPositionType value);

    /**
     * Returns the value of the '<em><b>Upper Corner</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Upper Corner</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Upper Corner</em>' containment reference.
     * @see #setUpperCorner(DirectPositionType)
     * @see net.opengis.gml311.Gml311Package#getEnvelopeType_UpperCorner()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='upperCorner' namespace='##targetNamespace'"
     * @generated
     */
    DirectPositionType getUpperCorner();

    /**
     * Sets the value of the '{@link net.opengis.gml311.EnvelopeType#getUpperCorner <em>Upper Corner</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Upper Corner</em>' containment reference.
     * @see #getUpperCorner()
     * @generated
     */
    void setUpperCorner(DirectPositionType value);

    /**
     * Returns the value of the '<em><b>Coord</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.CoordType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * deprecated with GML version 3.0
     * <!-- end-model-doc -->
     * @return the value of the '<em>Coord</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getEnvelopeType_Coord()
     * @model containment="true" upper="2"
     *        extendedMetaData="kind='element' name='coord' namespace='##targetNamespace'"
     * @generated
     */
    EList<CoordType> getCoord();

    /**
     * Returns the value of the '<em><b>Pos</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.DirectPositionType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Deprecated with GML version 3.1. Use the explicit properties "lowerCorner" and "upperCorner" instead.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Pos</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getEnvelopeType_Pos()
     * @model containment="true" upper="2"
     *        extendedMetaData="kind='element' name='pos' namespace='##targetNamespace'"
     * @generated
     */
    EList<DirectPositionType> getPos();

    /**
     * Returns the value of the '<em><b>Coordinates</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Deprecated with GML version 3.1.0. Use the explicit properties "lowerCorner" and "upperCorner" instead.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Coordinates</em>' containment reference.
     * @see #setCoordinates(CoordinatesType)
     * @see net.opengis.gml311.Gml311Package#getEnvelopeType_Coordinates()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='coordinates' namespace='##targetNamespace'"
     * @generated
     */
    CoordinatesType getCoordinates();

    /**
     * Sets the value of the '{@link net.opengis.gml311.EnvelopeType#getCoordinates <em>Coordinates</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Coordinates</em>' containment reference.
     * @see #getCoordinates()
     * @generated
     */
    void setCoordinates(CoordinatesType value);

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
     * @see net.opengis.gml311.Gml311Package#getEnvelopeType_AxisLabels()
     * @model dataType="net.opengis.gml311.NCNameList" many="false"
     *        extendedMetaData="kind='attribute' name='axisLabels'"
     * @generated
     */
    List<String> getAxisLabels();

    /**
     * Sets the value of the '{@link net.opengis.gml311.EnvelopeType#getAxisLabels <em>Axis Labels</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Axis Labels</em>' attribute.
     * @see #getAxisLabels()
     * @generated
     */
    void setAxisLabels(List<String> value);

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
     * @see net.opengis.gml311.Gml311Package#getEnvelopeType_SrsDimension()
     * @model dataType="org.eclipse.emf.ecore.xml.type.PositiveInteger"
     *        extendedMetaData="kind='attribute' name='srsDimension'"
     * @generated
     */
    BigInteger getSrsDimension();

    /**
     * Sets the value of the '{@link net.opengis.gml311.EnvelopeType#getSrsDimension <em>Srs Dimension</em>}' attribute.
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
     * @see net.opengis.gml311.Gml311Package#getEnvelopeType_SrsName()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
     *        extendedMetaData="kind='attribute' name='srsName'"
     * @generated
     */
    String getSrsName();

    /**
     * Sets the value of the '{@link net.opengis.gml311.EnvelopeType#getSrsName <em>Srs Name</em>}' attribute.
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
     * @see net.opengis.gml311.Gml311Package#getEnvelopeType_UomLabels()
     * @model dataType="net.opengis.gml311.NCNameList" many="false"
     *        extendedMetaData="kind='attribute' name='uomLabels'"
     * @generated
     */
    List<String> getUomLabels();

    /**
     * Sets the value of the '{@link net.opengis.gml311.EnvelopeType#getUomLabels <em>Uom Labels</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Uom Labels</em>' attribute.
     * @see #getUomLabels()
     * @generated
     */
    void setUomLabels(List<String> value);

} // EnvelopeType
