/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.fes20;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Filter Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.fes20.FilterType#getComparisonOpsGroup <em>Comparison Ops Group</em>}</li>
 *   <li>{@link net.opengis.fes20.FilterType#getComparisonOps <em>Comparison Ops</em>}</li>
 *   <li>{@link net.opengis.fes20.FilterType#getSpatialOpsGroup <em>Spatial Ops Group</em>}</li>
 *   <li>{@link net.opengis.fes20.FilterType#getSpatialOps <em>Spatial Ops</em>}</li>
 *   <li>{@link net.opengis.fes20.FilterType#getTemporalOpsGroup <em>Temporal Ops Group</em>}</li>
 *   <li>{@link net.opengis.fes20.FilterType#getTemporalOps <em>Temporal Ops</em>}</li>
 *   <li>{@link net.opengis.fes20.FilterType#getLogicOpsGroup <em>Logic Ops Group</em>}</li>
 *   <li>{@link net.opengis.fes20.FilterType#getLogicOps <em>Logic Ops</em>}</li>
 *   <li>{@link net.opengis.fes20.FilterType#getExtensionOpsGroup <em>Extension Ops Group</em>}</li>
 *   <li>{@link net.opengis.fes20.FilterType#getExtensionOps <em>Extension Ops</em>}</li>
 *   <li>{@link net.opengis.fes20.FilterType#getFunction <em>Function</em>}</li>
 *   <li>{@link net.opengis.fes20.FilterType#getIdGroup <em>Id Group</em>}</li>
 *   <li>{@link net.opengis.fes20.FilterType#getId <em>Id</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.fes20.Fes20Package#getFilterType()
 * @model extendedMetaData="name='FilterType' kind='elementOnly'"
 * @generated
 */
public interface FilterType extends AbstractSelectionClauseType {
    /**
     * Returns the value of the '<em><b>Comparison Ops Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Comparison Ops Group</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Comparison Ops Group</em>' attribute list.
     * @see net.opengis.fes20.Fes20Package#getFilterType_ComparisonOpsGroup()
     * @model dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="false"
     *        extendedMetaData="kind='group' name='comparisonOps:group' namespace='##targetNamespace'"
     * @generated
     */
    FeatureMap getComparisonOpsGroup();

    /**
     * Returns the value of the '<em><b>Comparison Ops</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Comparison Ops</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Comparison Ops</em>' containment reference.
     * @see net.opengis.fes20.Fes20Package#getFilterType_ComparisonOps()
     * @model containment="true" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='comparisonOps' namespace='##targetNamespace' group='comparisonOps:group'"
     * @generated
     */
    ComparisonOpsType getComparisonOps();

    /**
     * Returns the value of the '<em><b>Spatial Ops Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Spatial Ops Group</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Spatial Ops Group</em>' attribute list.
     * @see net.opengis.fes20.Fes20Package#getFilterType_SpatialOpsGroup()
     * @model dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="false"
     *        extendedMetaData="kind='group' name='spatialOps:group' namespace='##targetNamespace'"
     * @generated
     */
    FeatureMap getSpatialOpsGroup();

    /**
     * Returns the value of the '<em><b>Spatial Ops</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Spatial Ops</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Spatial Ops</em>' containment reference.
     * @see net.opengis.fes20.Fes20Package#getFilterType_SpatialOps()
     * @model containment="true" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='spatialOps' namespace='##targetNamespace' group='spatialOps:group'"
     * @generated
     */
    SpatialOpsType getSpatialOps();

    /**
     * Returns the value of the '<em><b>Temporal Ops Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Temporal Ops Group</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Temporal Ops Group</em>' attribute list.
     * @see net.opengis.fes20.Fes20Package#getFilterType_TemporalOpsGroup()
     * @model dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="false"
     *        extendedMetaData="kind='group' name='temporalOps:group' namespace='##targetNamespace'"
     * @generated
     */
    FeatureMap getTemporalOpsGroup();

    /**
     * Returns the value of the '<em><b>Temporal Ops</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Temporal Ops</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Temporal Ops</em>' containment reference.
     * @see net.opengis.fes20.Fes20Package#getFilterType_TemporalOps()
     * @model containment="true" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='temporalOps' namespace='##targetNamespace' group='temporalOps:group'"
     * @generated
     */
    TemporalOpsType getTemporalOps();

    /**
     * Returns the value of the '<em><b>Logic Ops Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Logic Ops Group</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Logic Ops Group</em>' attribute list.
     * @see net.opengis.fes20.Fes20Package#getFilterType_LogicOpsGroup()
     * @model dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="false"
     *        extendedMetaData="kind='group' name='logicOps:group' namespace='##targetNamespace'"
     * @generated
     */
    FeatureMap getLogicOpsGroup();

    /**
     * Returns the value of the '<em><b>Logic Ops</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Logic Ops</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Logic Ops</em>' containment reference.
     * @see net.opengis.fes20.Fes20Package#getFilterType_LogicOps()
     * @model containment="true" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='logicOps' namespace='##targetNamespace' group='logicOps:group'"
     * @generated
     */
    LogicOpsType getLogicOps();

    /**
     * Returns the value of the '<em><b>Extension Ops Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Extension Ops Group</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Extension Ops Group</em>' attribute list.
     * @see net.opengis.fes20.Fes20Package#getFilterType_ExtensionOpsGroup()
     * @model dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="false"
     *        extendedMetaData="kind='group' name='extensionOps:group' namespace='##targetNamespace'"
     * @generated
     */
    FeatureMap getExtensionOpsGroup();

    /**
     * Returns the value of the '<em><b>Extension Ops</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Extension Ops</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Extension Ops</em>' containment reference.
     * @see net.opengis.fes20.Fes20Package#getFilterType_ExtensionOps()
     * @model containment="true" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='extensionOps' namespace='##targetNamespace' group='extensionOps:group'"
     * @generated
     */
    ExtensionOpsType getExtensionOps();

    /**
     * Returns the value of the '<em><b>Function</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Function</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Function</em>' containment reference.
     * @see #setFunction(FunctionType)
     * @see net.opengis.fes20.Fes20Package#getFilterType_Function()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='Function' namespace='##targetNamespace'"
     * @generated
     */
    FunctionType getFunction();

    /**
     * Sets the value of the '{@link net.opengis.fes20.FilterType#getFunction <em>Function</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Function</em>' containment reference.
     * @see #getFunction()
     * @generated
     */
    void setFunction(FunctionType value);

    /**
     * Returns the value of the '<em><b>Id Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Id Group</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Id Group</em>' attribute list.
     * @see net.opengis.fes20.Fes20Package#getFilterType_IdGroup()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        extendedMetaData="kind='group' name='_Id:group' namespace='##targetNamespace'"
     * @generated
     */
    FeatureMap getIdGroup();

    /**
     * Returns the value of the '<em><b>Id</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.fes20.AbstractIdType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Id</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Id</em>' containment reference list.
     * @see net.opengis.fes20.Fes20Package#getFilterType_Id()
     * @model containment="true" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='_Id' namespace='##targetNamespace' group='_Id:group'"
     * @generated
     */
    EList<AbstractIdType> getId();

} // FilterType
