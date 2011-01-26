/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs11;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Interpolation Method Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Identifier of a spatial interpolation method applicable to continuous grid coverages, plus the optional "null Resistance" parameter. 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs11.InterpolationMethodType#getNullResistance <em>Null Resistance</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs11.Wcs111Package#getInterpolationMethodType()
 * @model extendedMetaData="name='InterpolationMethodType' kind='simple'"
 * @generated
 */
public interface InterpolationMethodType extends InterpolationMethodBaseType {
    /**
     * Returns the value of the '<em><b>Null Resistance</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Identifier of how the server handles null values when spatially interpolating values in this field using this interpolation method. This identifier shall be selected from the referenced dictionary. This parameter shall be omitted when the rule for handling nulls is unknown. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Null Resistance</em>' attribute.
     * @see #setNullResistance(String)
     * @see net.opengis.wcs11.Wcs111Package#getInterpolationMethodType_NullResistance()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='nullResistance'"
     * @generated
     */
    String getNullResistance();

    /**
     * Sets the value of the '{@link net.opengis.wcs11.InterpolationMethodType#getNullResistance <em>Null Resistance</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Null Resistance</em>' attribute.
     * @see #getNullResistance()
     * @generated
     */
    void setNullResistance(String value);

} // InterpolationMethodType
