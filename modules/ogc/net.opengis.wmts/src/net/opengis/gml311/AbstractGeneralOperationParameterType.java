/**
 */
package net.opengis.gml311;

import java.math.BigInteger;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Abstract General Operation Parameter Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Abstract definition of a parameter or group of parameters used by an operation method. 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.AbstractGeneralOperationParameterType#getMinimumOccurs <em>Minimum Occurs</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getAbstractGeneralOperationParameterType()
 * @model abstract="true"
 *        extendedMetaData="name='AbstractGeneralOperationParameterType' kind='elementOnly'"
 * @generated
 */
public interface AbstractGeneralOperationParameterType extends DefinitionType {
    /**
     * Returns the value of the '<em><b>Minimum Occurs</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The minimum number of times that values for this parameter group or parameter are required. If this attribute is omitted, the minimum number is one. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Minimum Occurs</em>' attribute.
     * @see #setMinimumOccurs(BigInteger)
     * @see net.opengis.gml311.Gml311Package#getAbstractGeneralOperationParameterType_MinimumOccurs()
     * @model dataType="org.eclipse.emf.ecore.xml.type.NonNegativeInteger"
     *        extendedMetaData="kind='element' name='minimumOccurs' namespace='##targetNamespace'"
     * @generated
     */
    BigInteger getMinimumOccurs();

    /**
     * Sets the value of the '{@link net.opengis.gml311.AbstractGeneralOperationParameterType#getMinimumOccurs <em>Minimum Occurs</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Minimum Occurs</em>' attribute.
     * @see #getMinimumOccurs()
     * @generated
     */
    void setMinimumOccurs(BigInteger value);

} // AbstractGeneralOperationParameterType
