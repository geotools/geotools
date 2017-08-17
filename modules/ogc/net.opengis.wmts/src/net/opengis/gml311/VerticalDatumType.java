/**
 */
package net.opengis.gml311;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Vertical Datum Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A textual description and/or a set of parameters identifying a particular reference level surface used as a zero-height surface, including its position with respect to the Earth for any of the height types recognized by this standard. There are several types of Vertical Datums, and each may place constraints on the Coordinate Axis with which it is combined to create a Vertical CRS. 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.VerticalDatumType#getVerticalDatumType <em>Vertical Datum Type</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getVerticalDatumType()
 * @model extendedMetaData="name='VerticalDatumType' kind='elementOnly'"
 * @generated
 */
public interface VerticalDatumType extends AbstractDatumType {
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
     * @see net.opengis.gml311.Gml311Package#getVerticalDatumType_VerticalDatumType()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='verticalDatumType' namespace='##targetNamespace'"
     * @generated
     */
    VerticalDatumTypeType getVerticalDatumType();

    /**
     * Sets the value of the '{@link net.opengis.gml311.VerticalDatumType#getVerticalDatumType <em>Vertical Datum Type</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Vertical Datum Type</em>' containment reference.
     * @see #getVerticalDatumType()
     * @generated
     */
    void setVerticalDatumType(VerticalDatumTypeType value);

} // VerticalDatumType
