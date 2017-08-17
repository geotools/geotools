/**
 */
package net.opengis.gml311;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Conventional Unit Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Definition of a unit of measure which is related to a preferred unit for this quantity type through a conversion formula.  A method for deriving this unit by algebraic combination of more primitive units, may also be provided.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.ConventionalUnitType#getConversionToPreferredUnit <em>Conversion To Preferred Unit</em>}</li>
 *   <li>{@link net.opengis.gml311.ConventionalUnitType#getRoughConversionToPreferredUnit <em>Rough Conversion To Preferred Unit</em>}</li>
 *   <li>{@link net.opengis.gml311.ConventionalUnitType#getDerivationUnitTerm <em>Derivation Unit Term</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getConventionalUnitType()
 * @model extendedMetaData="name='ConventionalUnitType' kind='elementOnly'"
 * @generated
 */
public interface ConventionalUnitType extends UnitDefinitionType {
    /**
     * Returns the value of the '<em><b>Conversion To Preferred Unit</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This element is included when this unit has an accurate conversion to the preferred unit for this quantity type.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Conversion To Preferred Unit</em>' containment reference.
     * @see #setConversionToPreferredUnit(ConversionToPreferredUnitType)
     * @see net.opengis.gml311.Gml311Package#getConventionalUnitType_ConversionToPreferredUnit()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='conversionToPreferredUnit' namespace='##targetNamespace'"
     * @generated
     */
    ConversionToPreferredUnitType getConversionToPreferredUnit();

    /**
     * Sets the value of the '{@link net.opengis.gml311.ConventionalUnitType#getConversionToPreferredUnit <em>Conversion To Preferred Unit</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Conversion To Preferred Unit</em>' containment reference.
     * @see #getConversionToPreferredUnit()
     * @generated
     */
    void setConversionToPreferredUnit(ConversionToPreferredUnitType value);

    /**
     * Returns the value of the '<em><b>Rough Conversion To Preferred Unit</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This element is included when the correct definition of this unit is unknown, but this unit has a rough or inaccurate conversion to the preferred unit for this quantity type.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Rough Conversion To Preferred Unit</em>' containment reference.
     * @see #setRoughConversionToPreferredUnit(ConversionToPreferredUnitType)
     * @see net.opengis.gml311.Gml311Package#getConventionalUnitType_RoughConversionToPreferredUnit()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='roughConversionToPreferredUnit' namespace='##targetNamespace'"
     * @generated
     */
    ConversionToPreferredUnitType getRoughConversionToPreferredUnit();

    /**
     * Sets the value of the '{@link net.opengis.gml311.ConventionalUnitType#getRoughConversionToPreferredUnit <em>Rough Conversion To Preferred Unit</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Rough Conversion To Preferred Unit</em>' containment reference.
     * @see #getRoughConversionToPreferredUnit()
     * @generated
     */
    void setRoughConversionToPreferredUnit(ConversionToPreferredUnitType value);

    /**
     * Returns the value of the '<em><b>Derivation Unit Term</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.DerivationUnitTermType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Derivation Unit Term</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Derivation Unit Term</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getConventionalUnitType_DerivationUnitTerm()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='derivationUnitTerm' namespace='##targetNamespace'"
     * @generated
     */
    EList<DerivationUnitTermType> getDerivationUnitTerm();

} // ConventionalUnitType
