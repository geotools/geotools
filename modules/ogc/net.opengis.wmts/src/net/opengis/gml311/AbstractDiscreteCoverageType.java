/**
 */
package net.opengis.gml311;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Abstract Discrete Coverage Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A discrete coverage consists of a domain set, range set and optionally a coverage function. The domain set consists of either geometry or temporal objects, finite in number. The range set is comprised of a finite number of attribute values each of which is associated to every direct position within any single spatiotemporal object in the domain. In other words, the range values are constant on each spatiotemporal object in the domain. This coverage function maps each element from the coverage domain to an element in its range. This definition conforms to ISO 19123.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.AbstractDiscreteCoverageType#getCoverageFunction <em>Coverage Function</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getAbstractDiscreteCoverageType()
 * @model abstract="true"
 *        extendedMetaData="name='AbstractDiscreteCoverageType' kind='elementOnly'"
 * @generated
 */
public interface AbstractDiscreteCoverageType extends AbstractCoverageType {
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
     * @see net.opengis.gml311.Gml311Package#getAbstractDiscreteCoverageType_CoverageFunction()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='coverageFunction' namespace='##targetNamespace'"
     * @generated
     */
    CoverageFunctionType getCoverageFunction();

    /**
     * Sets the value of the '{@link net.opengis.gml311.AbstractDiscreteCoverageType#getCoverageFunction <em>Coverage Function</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Coverage Function</em>' containment reference.
     * @see #getCoverageFunction()
     * @generated
     */
    void setCoverageFunction(CoverageFunctionType value);

} // AbstractDiscreteCoverageType
