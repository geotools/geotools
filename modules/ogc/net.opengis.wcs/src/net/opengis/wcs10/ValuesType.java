/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs10;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Values Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs10.ValuesType#getDefault <em>Default</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs10.Wcs10Package#getValuesType()
 * @model extendedMetaData="name='values_._type' kind='elementOnly'"
 * @generated
 */
public interface ValuesType extends ValueEnumType {
    /**
	 * Returns the value of the '<em><b>Default</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Ordered sequence of the parameter value(s) that the server will use for GetCoverage requests which omit a constraint on this parameter axis. (GetCoverage requests against a coverage offering whose AxisDescription has no default must specify a valid constraint for this parameter.)
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Default</em>' containment reference.
	 * @see #setDefault(TypedLiteralType)
	 * @see net.opengis.wcs10.Wcs10Package#getValuesType_Default()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='default' namespace='##targetNamespace'"
	 * @generated
	 */
    TypedLiteralType getDefault();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.ValuesType#getDefault <em>Default</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Default</em>' containment reference.
	 * @see #getDefault()
	 * @generated
	 */
    void setDefault(TypedLiteralType value);

} // ValuesType
