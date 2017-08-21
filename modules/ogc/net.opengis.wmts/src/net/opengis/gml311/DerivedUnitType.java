/**
 */
package net.opengis.gml311;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Derived Unit Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Definition of a unit of measure which is defined through algebraic combination of more primitive units, which are usually base units from a particular system of units. Derived units based directly on base units are usually preferred for quantities other than the base units or fundamental quantities within a system.  If a derived unit is not the preferred unit, the ConventionalUnit element should be used instead.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.DerivedUnitType#getDerivationUnitTerm <em>Derivation Unit Term</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getDerivedUnitType()
 * @model extendedMetaData="name='DerivedUnitType' kind='elementOnly'"
 * @generated
 */
public interface DerivedUnitType extends UnitDefinitionType {
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
     * @see net.opengis.gml311.Gml311Package#getDerivedUnitType_DerivationUnitTerm()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='derivationUnitTerm' namespace='##targetNamespace'"
     * @generated
     */
    EList<DerivationUnitTermType> getDerivationUnitTerm();

} // DerivedUnitType
