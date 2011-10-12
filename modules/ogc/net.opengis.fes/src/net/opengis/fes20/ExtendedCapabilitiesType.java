/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.fes20;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Extended Capabilities Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.fes20.ExtendedCapabilitiesType#getAdditionalOperators <em>Additional Operators</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.fes20.Fes20Package#getExtendedCapabilitiesType()
 * @model extendedMetaData="name='Extended_CapabilitiesType' kind='elementOnly'"
 * @generated
 */
public interface ExtendedCapabilitiesType extends EObject {
    /**
     * Returns the value of the '<em><b>Additional Operators</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Additional Operators</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Additional Operators</em>' containment reference.
     * @see #setAdditionalOperators(AdditionalOperatorsType)
     * @see net.opengis.fes20.Fes20Package#getExtendedCapabilitiesType_AdditionalOperators()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='AdditionalOperators' namespace='##targetNamespace'"
     * @generated
     */
    AdditionalOperatorsType getAdditionalOperators();

    /**
     * Sets the value of the '{@link net.opengis.fes20.ExtendedCapabilitiesType#getAdditionalOperators <em>Additional Operators</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Additional Operators</em>' containment reference.
     * @see #getAdditionalOperators()
     * @generated
     */
    void setAdditionalOperators(AdditionalOperatorsType value);

} // ExtendedCapabilitiesType
