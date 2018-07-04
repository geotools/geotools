/**
 */
package net.opengis.gml311;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Conversion To Preferred Unit Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Relation of a unit to the preferred unit for this quantity type, specified by an arithmetic conversion (scaling and/or offset). A preferred unit is either a base unit or a derived unit selected for all units of one quantity type. The mandatory attribute "uom" shall reference the preferred unit that this conversion applies to. The conversion is specified by one of two alternative elements: "factor" or "formula".
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.ConversionToPreferredUnitType#getFactor <em>Factor</em>}</li>
 *   <li>{@link net.opengis.gml311.ConversionToPreferredUnitType#getFormula <em>Formula</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getConversionToPreferredUnitType()
 * @model extendedMetaData="name='ConversionToPreferredUnitType' kind='elementOnly'"
 * @generated
 */
public interface ConversionToPreferredUnitType extends UnitOfMeasureType {
    /**
     * Returns the value of the '<em><b>Factor</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Specification of the scale factor by which a value using this unit of measure can be multiplied to obtain the corresponding value using the preferred unit of measure.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Factor</em>' attribute.
     * @see #isSetFactor()
     * @see #unsetFactor()
     * @see #setFactor(double)
     * @see net.opengis.gml311.Gml311Package#getConversionToPreferredUnitType_Factor()
     * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Double"
     *        extendedMetaData="kind='element' name='factor' namespace='##targetNamespace'"
     * @generated
     */
    double getFactor();

    /**
     * Sets the value of the '{@link net.opengis.gml311.ConversionToPreferredUnitType#getFactor <em>Factor</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Factor</em>' attribute.
     * @see #isSetFactor()
     * @see #unsetFactor()
     * @see #getFactor()
     * @generated
     */
    void setFactor(double value);

    /**
     * Unsets the value of the '{@link net.opengis.gml311.ConversionToPreferredUnitType#getFactor <em>Factor</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetFactor()
     * @see #getFactor()
     * @see #setFactor(double)
     * @generated
     */
    void unsetFactor();

    /**
     * Returns whether the value of the '{@link net.opengis.gml311.ConversionToPreferredUnitType#getFactor <em>Factor</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Factor</em>' attribute is set.
     * @see #unsetFactor()
     * @see #getFactor()
     * @see #setFactor(double)
     * @generated
     */
    boolean isSetFactor();

    /**
     * Returns the value of the '<em><b>Formula</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Specification of the formula by which a value using this unit of measure can be converted to obtain the corresponding value using the preferred unit of measure.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Formula</em>' containment reference.
     * @see #setFormula(FormulaType)
     * @see net.opengis.gml311.Gml311Package#getConversionToPreferredUnitType_Formula()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='formula' namespace='##targetNamespace'"
     * @generated
     */
    FormulaType getFormula();

    /**
     * Sets the value of the '{@link net.opengis.gml311.ConversionToPreferredUnitType#getFormula <em>Formula</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Formula</em>' containment reference.
     * @see #getFormula()
     * @generated
     */
    void setFormula(FormulaType value);

} // ConversionToPreferredUnitType
