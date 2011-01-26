/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows11;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Reference Group Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Logical group of one or more references to remote and/or local resources, allowing including metadata about that group. A Group can be used instead of a Manifest that can only contain one group. 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.ows11.ReferenceGroupType#getAbstractReferenceBaseGroup <em>Abstract Reference Base Group</em>}</li>
 *   <li>{@link net.opengis.ows11.ReferenceGroupType#getAbstractReferenceBase <em>Abstract Reference Base</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.ows11.Ows11Package#getReferenceGroupType()
 * @model extendedMetaData="name='ReferenceGroupType' kind='elementOnly'"
 * @generated
 */
public interface ReferenceGroupType extends BasicIdentificationType {
    /**
     * Returns the value of the '<em><b>Abstract Reference Base Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Abstract Reference Base Group</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Abstract Reference Base Group</em>' attribute list.
     * @see net.opengis.ows11.Ows11Package#getReferenceGroupType_AbstractReferenceBaseGroup()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" required="true" many="true"
     *        extendedMetaData="kind='group' name='AbstractReferenceBase:group' namespace='##targetNamespace'"
     * @generated
     */
    FeatureMap getAbstractReferenceBaseGroup();

    /**
     * Returns the value of the '<em><b>Abstract Reference Base</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.ows11.AbstractReferenceBaseType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Abstract Reference Base</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Abstract Reference Base</em>' containment reference list.
     * @see net.opengis.ows11.Ows11Package#getReferenceGroupType_AbstractReferenceBase()
     * @model type="net.opengis.ows11.AbstractReferenceBaseType" containment="true" required="true" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='AbstractReferenceBase' namespace='##targetNamespace' group='AbstractReferenceBase:group'"
     * @generated
     */
    EList getAbstractReferenceBase();

} // ReferenceGroupType
