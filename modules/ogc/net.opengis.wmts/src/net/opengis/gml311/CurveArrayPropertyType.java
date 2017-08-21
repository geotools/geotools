/**
 */
package net.opengis.gml311;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Curve Array Property Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A container for an array of curves. The elements are always contained in the array property, referencing geometry elements 
 * 			or arrays of geometry elements is not supported.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.CurveArrayPropertyType#getCurveGroup <em>Curve Group</em>}</li>
 *   <li>{@link net.opengis.gml311.CurveArrayPropertyType#getCurve <em>Curve</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getCurveArrayPropertyType()
 * @model extendedMetaData="name='CurveArrayPropertyType' kind='elementOnly'"
 * @generated
 */
public interface CurveArrayPropertyType extends EObject {
    /**
     * Returns the value of the '<em><b>Curve Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The "_Curve" element is the abstract head of the substituition group for all (continuous) curve elements.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Curve Group</em>' attribute list.
     * @see net.opengis.gml311.Gml311Package#getCurveArrayPropertyType_CurveGroup()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        extendedMetaData="kind='group' name='_Curve:group' namespace='##targetNamespace'"
     * @generated
     */
    FeatureMap getCurveGroup();

    /**
     * Returns the value of the '<em><b>Curve</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.AbstractCurveType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The "_Curve" element is the abstract head of the substituition group for all (continuous) curve elements.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Curve</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getCurveArrayPropertyType_Curve()
     * @model containment="true" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='_Curve' namespace='##targetNamespace' group='_Curve:group'"
     * @generated
     */
    EList<AbstractCurveType> getCurve();

} // CurveArrayPropertyType
