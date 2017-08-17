/**
 */
package net.opengis.gml311;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Abstract Ring Property Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Encapsulates a ring to represent the surface boundary property of a surface.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.AbstractRingPropertyType#getRingGroup <em>Ring Group</em>}</li>
 *   <li>{@link net.opengis.gml311.AbstractRingPropertyType#getRing <em>Ring</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getAbstractRingPropertyType()
 * @model extendedMetaData="name='AbstractRingPropertyType' kind='elementOnly'"
 * @generated
 */
public interface AbstractRingPropertyType extends EObject {
    /**
     * Returns the value of the '<em><b>Ring Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The "_Ring" element is the abstract head of the substituition group for all closed boundaries of a surface patch.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Ring Group</em>' attribute list.
     * @see net.opengis.gml311.Gml311Package#getAbstractRingPropertyType_RingGroup()
     * @model dataType="org.eclipse.emf.ecore.EFeatureMapEntry" required="true" many="false"
     *        extendedMetaData="kind='group' name='_Ring:group' namespace='##targetNamespace'"
     * @generated
     */
    FeatureMap getRingGroup();

    /**
     * Returns the value of the '<em><b>Ring</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The "_Ring" element is the abstract head of the substituition group for all closed boundaries of a surface patch.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Ring</em>' containment reference.
     * @see net.opengis.gml311.Gml311Package#getAbstractRingPropertyType_Ring()
     * @model containment="true" required="true" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='_Ring' namespace='##targetNamespace' group='_Ring:group'"
     * @generated
     */
    AbstractRingType getRing();

} // AbstractRingPropertyType
