/**
 */
package net.opengis.gml311;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Ellipsoid Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * An ellipsoid is a geometric figure that can be used to describe the approximate shape of the earth. In mathematical terms, it is a surface formed by the rotation of an ellipse about its minor axis.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.EllipsoidType#getEllipsoidID <em>Ellipsoid ID</em>}</li>
 *   <li>{@link net.opengis.gml311.EllipsoidType#getRemarks <em>Remarks</em>}</li>
 *   <li>{@link net.opengis.gml311.EllipsoidType#getSemiMajorAxis <em>Semi Major Axis</em>}</li>
 *   <li>{@link net.opengis.gml311.EllipsoidType#getSecondDefiningParameter <em>Second Defining Parameter</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getEllipsoidType()
 * @model extendedMetaData="name='EllipsoidType' kind='elementOnly'"
 * @generated
 */
public interface EllipsoidType extends EllipsoidBaseType {
    /**
     * Returns the value of the '<em><b>Ellipsoid ID</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.IdentifierType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Set of alternative identifications of this ellipsoid. The first ellipsoidID, if any, is normally the primary identification code, and any others are aliases. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Ellipsoid ID</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getEllipsoidType_EllipsoidID()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='ellipsoidID' namespace='##targetNamespace'"
     * @generated
     */
    EList<IdentifierType> getEllipsoidID();

    /**
     * Returns the value of the '<em><b>Remarks</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Comments on or information about this ellipsoid, including source information. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Remarks</em>' containment reference.
     * @see #setRemarks(StringOrRefType)
     * @see net.opengis.gml311.Gml311Package#getEllipsoidType_Remarks()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='remarks' namespace='##targetNamespace'"
     * @generated
     */
    StringOrRefType getRemarks();

    /**
     * Sets the value of the '{@link net.opengis.gml311.EllipsoidType#getRemarks <em>Remarks</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Remarks</em>' containment reference.
     * @see #getRemarks()
     * @generated
     */
    void setRemarks(StringOrRefType value);

    /**
     * Returns the value of the '<em><b>Semi Major Axis</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Length of the semi-major axis of the ellipsoid, with its units. Uses the MeasureType with the restriction that the unit of measure referenced by uom must be suitable for a length, such as metres or feet. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Semi Major Axis</em>' containment reference.
     * @see #setSemiMajorAxis(MeasureType)
     * @see net.opengis.gml311.Gml311Package#getEllipsoidType_SemiMajorAxis()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='semiMajorAxis' namespace='##targetNamespace'"
     * @generated
     */
    MeasureType getSemiMajorAxis();

    /**
     * Sets the value of the '{@link net.opengis.gml311.EllipsoidType#getSemiMajorAxis <em>Semi Major Axis</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Semi Major Axis</em>' containment reference.
     * @see #getSemiMajorAxis()
     * @generated
     */
    void setSemiMajorAxis(MeasureType value);

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
     * @see net.opengis.gml311.Gml311Package#getEllipsoidType_SecondDefiningParameter()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='secondDefiningParameter' namespace='##targetNamespace'"
     * @generated
     */
    SecondDefiningParameterType getSecondDefiningParameter();

    /**
     * Sets the value of the '{@link net.opengis.gml311.EllipsoidType#getSecondDefiningParameter <em>Second Defining Parameter</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Second Defining Parameter</em>' containment reference.
     * @see #getSecondDefiningParameter()
     * @generated
     */
    void setSecondDefiningParameter(SecondDefiningParameterType value);

} // EllipsoidType
