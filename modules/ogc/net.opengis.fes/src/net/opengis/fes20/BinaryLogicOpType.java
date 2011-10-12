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
 * A representation of the model object '<em><b>Binary Logic Op Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.fes20.BinaryLogicOpType#getFilterPredicates <em>Filter Predicates</em>}</li>
 *   <li>{@link net.opengis.fes20.BinaryLogicOpType#getComparisonOpsGroup <em>Comparison Ops Group</em>}</li>
 *   <li>{@link net.opengis.fes20.BinaryLogicOpType#getComparisonOps <em>Comparison Ops</em>}</li>
 *   <li>{@link net.opengis.fes20.BinaryLogicOpType#getSpatialOpsGroup <em>Spatial Ops Group</em>}</li>
 *   <li>{@link net.opengis.fes20.BinaryLogicOpType#getSpatialOps <em>Spatial Ops</em>}</li>
 *   <li>{@link net.opengis.fes20.BinaryLogicOpType#getTemporalOpsGroup <em>Temporal Ops Group</em>}</li>
 *   <li>{@link net.opengis.fes20.BinaryLogicOpType#getTemporalOps <em>Temporal Ops</em>}</li>
 *   <li>{@link net.opengis.fes20.BinaryLogicOpType#getLogicOpsGroup <em>Logic Ops Group</em>}</li>
 *   <li>{@link net.opengis.fes20.BinaryLogicOpType#getLogicOps <em>Logic Ops</em>}</li>
 *   <li>{@link net.opengis.fes20.BinaryLogicOpType#getExtensionOpsGroup <em>Extension Ops Group</em>}</li>
 *   <li>{@link net.opengis.fes20.BinaryLogicOpType#getExtensionOps <em>Extension Ops</em>}</li>
 *   <li>{@link net.opengis.fes20.BinaryLogicOpType#getFunction <em>Function</em>}</li>
 *   <li>{@link net.opengis.fes20.BinaryLogicOpType#getIdGroup <em>Id Group</em>}</li>
 *   <li>{@link net.opengis.fes20.BinaryLogicOpType#getId <em>Id</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.fes20.Fes20Package#getBinaryLogicOpType()
 * @model extendedMetaData="name='BinaryLogicOpType' kind='elementOnly'"
 * @generated
 */
public interface BinaryLogicOpType extends LogicOpsType {
    /**
     * Returns the value of the '<em><b>Filter Predicates</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Filter Predicates</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Filter Predicates</em>' attribute list.
     * @see net.opengis.fes20.Fes20Package#getBinaryLogicOpType_FilterPredicates()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        extendedMetaData="kind='group' name='FilterPredicates:0'"
     * @generated
     */
    FeatureMap getFilterPredicates();

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
     * @see net.opengis.fes20.Fes20Package#getBinaryLogicOpType_ComparisonOpsGroup()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='group' name='comparisonOps:group' namespace='##targetNamespace' group='#FilterPredicates:0'"
     * @generated
     */
    FeatureMap getComparisonOpsGroup();

    /**
     * Returns the value of the '<em><b>Comparison Ops</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.fes20.ComparisonOpsType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Comparison Ops</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Comparison Ops</em>' containment reference list.
     * @see net.opengis.fes20.Fes20Package#getBinaryLogicOpType_ComparisonOps()
     * @model containment="true" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='comparisonOps' namespace='##targetNamespace' group='comparisonOps:group'"
     * @generated
     */
    EList<ComparisonOpsType> getComparisonOps();

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
     * @see net.opengis.fes20.Fes20Package#getBinaryLogicOpType_SpatialOpsGroup()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='group' name='spatialOps:group' namespace='##targetNamespace' group='#FilterPredicates:0'"
     * @generated
     */
    FeatureMap getSpatialOpsGroup();

    /**
     * Returns the value of the '<em><b>Spatial Ops</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.fes20.SpatialOpsType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Spatial Ops</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Spatial Ops</em>' containment reference list.
     * @see net.opengis.fes20.Fes20Package#getBinaryLogicOpType_SpatialOps()
     * @model containment="true" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='spatialOps' namespace='##targetNamespace' group='spatialOps:group'"
     * @generated
     */
    EList<SpatialOpsType> getSpatialOps();

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
     * @see net.opengis.fes20.Fes20Package#getBinaryLogicOpType_TemporalOpsGroup()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='group' name='temporalOps:group' namespace='##targetNamespace' group='#FilterPredicates:0'"
     * @generated
     */
    FeatureMap getTemporalOpsGroup();

    /**
     * Returns the value of the '<em><b>Temporal Ops</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.fes20.TemporalOpsType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Temporal Ops</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Temporal Ops</em>' containment reference list.
     * @see net.opengis.fes20.Fes20Package#getBinaryLogicOpType_TemporalOps()
     * @model containment="true" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='temporalOps' namespace='##targetNamespace' group='temporalOps:group'"
     * @generated
     */
    EList<TemporalOpsType> getTemporalOps();

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
     * @see net.opengis.fes20.Fes20Package#getBinaryLogicOpType_LogicOpsGroup()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='group' name='logicOps:group' namespace='##targetNamespace' group='#FilterPredicates:0'"
     * @generated
     */
    FeatureMap getLogicOpsGroup();

    /**
     * Returns the value of the '<em><b>Logic Ops</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.fes20.LogicOpsType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Logic Ops</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Logic Ops</em>' containment reference list.
     * @see net.opengis.fes20.Fes20Package#getBinaryLogicOpType_LogicOps()
     * @model containment="true" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='logicOps' namespace='##targetNamespace' group='logicOps:group'"
     * @generated
     */
    EList<LogicOpsType> getLogicOps();

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
     * @see net.opengis.fes20.Fes20Package#getBinaryLogicOpType_ExtensionOpsGroup()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='group' name='extensionOps:group' namespace='##targetNamespace' group='#FilterPredicates:0'"
     * @generated
     */
    FeatureMap getExtensionOpsGroup();

    /**
     * Returns the value of the '<em><b>Extension Ops</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.fes20.ExtensionOpsType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Extension Ops</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Extension Ops</em>' containment reference list.
     * @see net.opengis.fes20.Fes20Package#getBinaryLogicOpType_ExtensionOps()
     * @model containment="true" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='extensionOps' namespace='##targetNamespace' group='extensionOps:group'"
     * @generated
     */
    EList<ExtensionOpsType> getExtensionOps();

    /**
     * Returns the value of the '<em><b>Function</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.fes20.FunctionType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Function</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Function</em>' containment reference list.
     * @see net.opengis.fes20.Fes20Package#getBinaryLogicOpType_Function()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Function' namespace='##targetNamespace' group='#FilterPredicates:0'"
     * @generated
     */
    EList<FunctionType> getFunction();

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
     * @see net.opengis.fes20.Fes20Package#getBinaryLogicOpType_IdGroup()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='group' name='_Id:group' namespace='##targetNamespace' group='#FilterPredicates:0'"
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
     * @see net.opengis.fes20.Fes20Package#getBinaryLogicOpType_Id()
     * @model containment="true" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='_Id' namespace='##targetNamespace' group='_Id:group'"
     * @generated
     */
    EList<AbstractIdType> getId();

} // BinaryLogicOpType
