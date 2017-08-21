/**
 */
package net.opengis.gml311;

import javax.xml.datatype.XMLGregorianCalendar;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Abstract Datum Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A datum specifies the relationship of a coordinate system to the earth, thus creating a coordinate reference system. A datum uses a parameter or set of parameters that determine the location of the origin of the coordinate reference system. Each datum subtype can be associated with only specific types of coordinate systems. This abstract complexType shall not be used, extended, or restricted, in an Application Schema, to define a concrete subtype with a meaning equivalent to a concrete subtype specified in this document. 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.AbstractDatumType#getDatumID <em>Datum ID</em>}</li>
 *   <li>{@link net.opengis.gml311.AbstractDatumType#getRemarks <em>Remarks</em>}</li>
 *   <li>{@link net.opengis.gml311.AbstractDatumType#getAnchorPoint <em>Anchor Point</em>}</li>
 *   <li>{@link net.opengis.gml311.AbstractDatumType#getRealizationEpoch <em>Realization Epoch</em>}</li>
 *   <li>{@link net.opengis.gml311.AbstractDatumType#getValidArea <em>Valid Area</em>}</li>
 *   <li>{@link net.opengis.gml311.AbstractDatumType#getScope <em>Scope</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getAbstractDatumType()
 * @model abstract="true"
 *        extendedMetaData="name='AbstractDatumType' kind='elementOnly'"
 * @generated
 */
public interface AbstractDatumType extends AbstractDatumBaseType {
    /**
     * Returns the value of the '<em><b>Datum ID</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.IdentifierType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Set of alternative identifications of this datum. The first datumID, if any, is normally the primary identification code, and any others are aliases. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Datum ID</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getAbstractDatumType_DatumID()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='datumID' namespace='##targetNamespace'"
     * @generated
     */
    EList<IdentifierType> getDatumID();

    /**
     * Returns the value of the '<em><b>Remarks</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Comments on this reference system, including source information. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Remarks</em>' containment reference.
     * @see #setRemarks(StringOrRefType)
     * @see net.opengis.gml311.Gml311Package#getAbstractDatumType_Remarks()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='remarks' namespace='##targetNamespace'"
     * @generated
     */
    StringOrRefType getRemarks();

    /**
     * Sets the value of the '{@link net.opengis.gml311.AbstractDatumType#getRemarks <em>Remarks</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Remarks</em>' containment reference.
     * @see #getRemarks()
     * @generated
     */
    void setRemarks(StringOrRefType value);

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
     * @see net.opengis.gml311.Gml311Package#getAbstractDatumType_AnchorPoint()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='anchorPoint' namespace='##targetNamespace'"
     * @generated
     */
    CodeType getAnchorPoint();

    /**
     * Sets the value of the '{@link net.opengis.gml311.AbstractDatumType#getAnchorPoint <em>Anchor Point</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Anchor Point</em>' containment reference.
     * @see #getAnchorPoint()
     * @generated
     */
    void setAnchorPoint(CodeType value);

    /**
     * Returns the value of the '<em><b>Realization Epoch</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The time after which this datum definition is valid. This time may be precise (e.g. 1997.0 for IRTF97) or merely a year (e.g. 1983 for NAD83). In the latter case, the epoch usually refers to the year in which a major recalculation of the geodetic control network, underlying the datum, was executed or initiated. An old datum can remain valid after a new datum is defined. Alternatively, a datum may be superseded by a later datum, in which case the realization epoch for the new datum defines the upper limit for the validity of the superseded datum. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Realization Epoch</em>' attribute.
     * @see #setRealizationEpoch(XMLGregorianCalendar)
     * @see net.opengis.gml311.Gml311Package#getAbstractDatumType_RealizationEpoch()
     * @model dataType="org.eclipse.emf.ecore.xml.type.Date"
     *        extendedMetaData="kind='element' name='realizationEpoch' namespace='##targetNamespace'"
     * @generated
     */
    XMLGregorianCalendar getRealizationEpoch();

    /**
     * Sets the value of the '{@link net.opengis.gml311.AbstractDatumType#getRealizationEpoch <em>Realization Epoch</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Realization Epoch</em>' attribute.
     * @see #getRealizationEpoch()
     * @generated
     */
    void setRealizationEpoch(XMLGregorianCalendar value);

    /**
     * Returns the value of the '<em><b>Valid Area</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Area or region in which this CRS object is valid.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Valid Area</em>' containment reference.
     * @see #setValidArea(ExtentType)
     * @see net.opengis.gml311.Gml311Package#getAbstractDatumType_ValidArea()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='validArea' namespace='##targetNamespace'"
     * @generated
     */
    ExtentType getValidArea();

    /**
     * Sets the value of the '{@link net.opengis.gml311.AbstractDatumType#getValidArea <em>Valid Area</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Valid Area</em>' containment reference.
     * @see #getValidArea()
     * @generated
     */
    void setValidArea(ExtentType value);

    /**
     * Returns the value of the '<em><b>Scope</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Description of domain of usage, or limitations of usage, for which this CRS object is valid.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Scope</em>' attribute.
     * @see #setScope(String)
     * @see net.opengis.gml311.Gml311Package#getAbstractDatumType_Scope()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='element' name='scope' namespace='##targetNamespace'"
     * @generated
     */
    String getScope();

    /**
     * Sets the value of the '{@link net.opengis.gml311.AbstractDatumType#getScope <em>Scope</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Scope</em>' attribute.
     * @see #getScope()
     * @generated
     */
    void setScope(String value);

} // AbstractDatumType
