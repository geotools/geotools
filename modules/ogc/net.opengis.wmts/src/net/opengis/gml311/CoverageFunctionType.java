/**
 */
package net.opengis.gml311;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Coverage Function Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * The function or rule which defines the map from members of the domainSet to the range.  
 *       More functions will be added to this list
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.CoverageFunctionType#getMappingRule <em>Mapping Rule</em>}</li>
 *   <li>{@link net.opengis.gml311.CoverageFunctionType#getGridFunctionGroup <em>Grid Function Group</em>}</li>
 *   <li>{@link net.opengis.gml311.CoverageFunctionType#getGridFunction <em>Grid Function</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getCoverageFunctionType()
 * @model extendedMetaData="name='CoverageFunctionType' kind='elementOnly'"
 * @generated
 */
public interface CoverageFunctionType extends EObject {
    /**
     * Returns the value of the '<em><b>Mapping Rule</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Description of a rule for associating members from the domainSet with members of the rangeSet.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Mapping Rule</em>' containment reference.
     * @see #setMappingRule(StringOrRefType)
     * @see net.opengis.gml311.Gml311Package#getCoverageFunctionType_MappingRule()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='MappingRule' namespace='##targetNamespace'"
     * @generated
     */
    StringOrRefType getMappingRule();

    /**
     * Sets the value of the '{@link net.opengis.gml311.CoverageFunctionType#getMappingRule <em>Mapping Rule</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Mapping Rule</em>' containment reference.
     * @see #getMappingRule()
     * @generated
     */
    void setMappingRule(StringOrRefType value);

    /**
     * Returns the value of the '<em><b>Grid Function Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Grid Function Group</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Grid Function Group</em>' attribute list.
     * @see net.opengis.gml311.Gml311Package#getCoverageFunctionType_GridFunctionGroup()
     * @model dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="false"
     *        extendedMetaData="kind='group' name='GridFunction:group' namespace='##targetNamespace'"
     * @generated
     */
    FeatureMap getGridFunctionGroup();

    /**
     * Returns the value of the '<em><b>Grid Function</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Grid Function</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Grid Function</em>' containment reference.
     * @see #setGridFunction(GridFunctionType)
     * @see net.opengis.gml311.Gml311Package#getCoverageFunctionType_GridFunction()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='GridFunction' namespace='##targetNamespace' group='GridFunction:group'"
     * @generated
     */
    GridFunctionType getGridFunction();

    /**
     * Sets the value of the '{@link net.opengis.gml311.CoverageFunctionType#getGridFunction <em>Grid Function</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Grid Function</em>' containment reference.
     * @see #getGridFunction()
     * @generated
     */
    void setGridFunction(GridFunctionType value);

} // CoverageFunctionType
