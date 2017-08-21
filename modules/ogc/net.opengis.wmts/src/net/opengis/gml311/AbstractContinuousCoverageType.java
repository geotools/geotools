/**
 */
package net.opengis.gml311;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Abstract Continuous Coverage Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A continuous coverage as defined in ISO 19123 is a coverage that can return different values for the same feature attribute at different direct positions within a single spatiotemporal object in its spatiotemporal domain
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.AbstractContinuousCoverageType#getCoverageFunction <em>Coverage Function</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getAbstractContinuousCoverageType()
 * @model abstract="true"
 *        extendedMetaData="name='AbstractContinuousCoverageType' kind='elementOnly'"
 * @generated
 */
public interface AbstractContinuousCoverageType extends AbstractCoverageType {
    /**
     * Returns the value of the '<em><b>Coverage Function</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Coverage Function</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Coverage Function</em>' containment reference.
     * @see #setCoverageFunction(CoverageFunctionType)
     * @see net.opengis.gml311.Gml311Package#getAbstractContinuousCoverageType_CoverageFunction()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='coverageFunction' namespace='##targetNamespace'"
     * @generated
     */
    CoverageFunctionType getCoverageFunction();

    /**
     * Sets the value of the '{@link net.opengis.gml311.AbstractContinuousCoverageType#getCoverageFunction <em>Coverage Function</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Coverage Function</em>' containment reference.
     * @see #getCoverageFunction()
     * @generated
     */
    void setCoverageFunction(CoverageFunctionType value);

} // AbstractContinuousCoverageType
