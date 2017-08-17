/**
 */
package net.opengis.gml311;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Point Array Property Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A container for an array of points. The elements are always contained in the array property, referencing geometry 
 * 			elements or arrays of geometry elements is not supported.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.PointArrayPropertyType#getPoint <em>Point</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getPointArrayPropertyType()
 * @model extendedMetaData="name='PointArrayPropertyType' kind='elementOnly'"
 * @generated
 */
public interface PointArrayPropertyType extends EObject {
    /**
     * Returns the value of the '<em><b>Point</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.PointType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Point</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Point</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getPointArrayPropertyType_Point()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='Point' namespace='##targetNamespace'"
     * @generated
     */
    EList<PointType> getPoint();

} // PointArrayPropertyType
