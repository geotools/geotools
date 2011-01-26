/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10;

import java.math.BigInteger;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Supported Complex Data Input Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wps10.SupportedComplexDataInputType#getMaximumMegabytes <em>Maximum Megabytes</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wps10.Wps10Package#getSupportedComplexDataInputType()
 * @model extendedMetaData="name='SupportedComplexDataInputType' kind='elementOnly'"
 * @generated
 */
public interface SupportedComplexDataInputType extends SupportedComplexDataType {
    /**
     * Returns the value of the '<em><b>Maximum Megabytes</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The maximum file size, in megabytes, of this input.  If the input exceeds this size, the server will return an error instead of processing the inputs.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Maximum Megabytes</em>' attribute.
     * @see #setMaximumMegabytes(BigInteger)
     * @see net.opengis.wps10.Wps10Package#getSupportedComplexDataInputType_MaximumMegabytes()
     * @model dataType="org.eclipse.emf.ecore.xml.type.Integer"
     *        extendedMetaData="kind='attribute' name='maximumMegabytes'"
     * @generated
     */
    BigInteger getMaximumMegabytes();

    /**
     * Sets the value of the '{@link net.opengis.wps10.SupportedComplexDataInputType#getMaximumMegabytes <em>Maximum Megabytes</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Maximum Megabytes</em>' attribute.
     * @see #getMaximumMegabytes()
     * @generated
     */
    void setMaximumMegabytes(BigInteger value);

} // SupportedComplexDataInputType
