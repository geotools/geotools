/**
 */
package net.opengis.gml311;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>History Property Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * The history relationship associates a feature with a sequence of TimeSlice instances.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.HistoryPropertyType#getGroup <em>Group</em>}</li>
 *   <li>{@link net.opengis.gml311.HistoryPropertyType#getTimeSliceGroup <em>Time Slice Group</em>}</li>
 *   <li>{@link net.opengis.gml311.HistoryPropertyType#getTimeSlice <em>Time Slice</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getHistoryPropertyType()
 * @model extendedMetaData="name='HistoryPropertyType' kind='elementOnly'"
 * @generated
 */
public interface HistoryPropertyType extends EObject {
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
     * @see net.opengis.gml311.Gml311Package#getHistoryPropertyType_Group()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        extendedMetaData="kind='group' name='group:0'"
     * @generated
     */
    FeatureMap getGroup();

    /**
     * Returns the value of the '<em><b>Time Slice Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Time Slice Group</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Time Slice Group</em>' attribute list.
     * @see net.opengis.gml311.Gml311Package#getHistoryPropertyType_TimeSliceGroup()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" required="true" many="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='group' name='_TimeSlice:group' namespace='##targetNamespace' group='#group:0'"
     * @generated
     */
    FeatureMap getTimeSliceGroup();

    /**
     * Returns the value of the '<em><b>Time Slice</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.AbstractTimeSliceType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Time Slice</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Time Slice</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getHistoryPropertyType_TimeSlice()
     * @model containment="true" required="true" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='_TimeSlice' namespace='##targetNamespace' group='_TimeSlice:group'"
     * @generated
     */
    EList<AbstractTimeSliceType> getTimeSlice();

} // HistoryPropertyType
