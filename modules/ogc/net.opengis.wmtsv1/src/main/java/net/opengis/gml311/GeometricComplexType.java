/**
 */
package net.opengis.gml311;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Geometric Complex Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A geometric complex.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.GeometricComplexType#getElement <em>Element</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getGeometricComplexType()
 * @model extendedMetaData="name='GeometricComplexType' kind='elementOnly'"
 * @generated
 */
public interface GeometricComplexType extends AbstractGeometryType {
    /**
     * Returns the value of the '<em><b>Element</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.GeometricPrimitivePropertyType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Element</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Element</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getGeometricComplexType_Element()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='element' namespace='##targetNamespace'"
     * @generated
     */
    EList<GeometricPrimitivePropertyType> getElement();

} // GeometricComplexType
