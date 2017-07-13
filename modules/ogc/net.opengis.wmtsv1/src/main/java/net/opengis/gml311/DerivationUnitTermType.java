/**
 */
package net.opengis.gml311;

import java.math.BigInteger;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Derivation Unit Term Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Definition of one unit term for a derived unit of measure. This unit term references another unit of measure (uom) and provides an integer exponent applied to that unit in defining the compound unit. The exponent can be positive or negative, but not zero.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.DerivationUnitTermType#getExponent <em>Exponent</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getDerivationUnitTermType()
 * @model extendedMetaData="name='DerivationUnitTermType' kind='empty'"
 * @generated
 */
public interface DerivationUnitTermType extends UnitOfMeasureType {
    /**
     * Returns the value of the '<em><b>Exponent</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Exponent</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Exponent</em>' attribute.
     * @see #setExponent(BigInteger)
     * @see net.opengis.gml311.Gml311Package#getDerivationUnitTermType_Exponent()
     * @model dataType="org.eclipse.emf.ecore.xml.type.Integer"
     *        extendedMetaData="kind='attribute' name='exponent'"
     * @generated
     */
    BigInteger getExponent();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DerivationUnitTermType#getExponent <em>Exponent</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Exponent</em>' attribute.
     * @see #getExponent()
     * @generated
     */
    void setExponent(BigInteger value);

} // DerivationUnitTermType
