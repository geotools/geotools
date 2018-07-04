/**
 */
package net.opengis.gml311;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Solid Array Property Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A container for an array of solids. The elements are always contained in the array property, referencing geometry elements or arrays of geometry elements is not supported.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.SolidArrayPropertyType#getGroup <em>Group</em>}</li>
 *   <li>{@link net.opengis.gml311.SolidArrayPropertyType#getSolidGroup <em>Solid Group</em>}</li>
 *   <li>{@link net.opengis.gml311.SolidArrayPropertyType#getSolid <em>Solid</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getSolidArrayPropertyType()
 * @model extendedMetaData="name='SolidArrayPropertyType' kind='elementOnly'"
 * @generated
 */
public interface SolidArrayPropertyType extends EObject {
    /**
     * Returns the value of the '<em><b>Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Group</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Group</em>' attribute list.
     * @see net.opengis.gml311.Gml311Package#getSolidArrayPropertyType_Group()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        extendedMetaData="kind='group' name='group:0'"
     * @generated
     */
    FeatureMap getGroup();

    /**
     * Returns the value of the '<em><b>Solid Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The "_Solid" element is the abstract head of the substituition group for all (continuous) solid elements.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Solid Group</em>' attribute list.
     * @see net.opengis.gml311.Gml311Package#getSolidArrayPropertyType_SolidGroup()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='group' name='_Solid:group' namespace='##targetNamespace' group='#group:0'"
     * @generated
     */
    FeatureMap getSolidGroup();

    /**
     * Returns the value of the '<em><b>Solid</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.AbstractSolidType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The "_Solid" element is the abstract head of the substituition group for all (continuous) solid elements.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Solid</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getSolidArrayPropertyType_Solid()
     * @model containment="true" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='_Solid' namespace='##targetNamespace' group='_Solid:group'"
     * @generated
     */
    EList<AbstractSolidType> getSolid();

} // SolidArrayPropertyType
