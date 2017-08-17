/**
 */
package net.opengis.gml311;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Base Unit Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Definition of a unit of measure which is a base unit from the system of units.  A base unit cannot be derived by combination of other base units within this system.  Sometimes known as "fundamental unit".
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.BaseUnitType#getUnitsSystem <em>Units System</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getBaseUnitType()
 * @model extendedMetaData="name='BaseUnitType' kind='elementOnly'"
 * @generated
 */
public interface BaseUnitType extends UnitDefinitionType {
    /**
     * Returns the value of the '<em><b>Units System</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Units System</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Units System</em>' containment reference.
     * @see #setUnitsSystem(ReferenceType)
     * @see net.opengis.gml311.Gml311Package#getBaseUnitType_UnitsSystem()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='unitsSystem' namespace='##targetNamespace'"
     * @generated
     */
    ReferenceType getUnitsSystem();

    /**
     * Sets the value of the '{@link net.opengis.gml311.BaseUnitType#getUnitsSystem <em>Units System</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Units System</em>' containment reference.
     * @see #getUnitsSystem()
     * @generated
     */
    void setUnitsSystem(ReferenceType value);

} // BaseUnitType
