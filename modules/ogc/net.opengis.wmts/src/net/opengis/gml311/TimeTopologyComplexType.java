/**
 */
package net.opengis.gml311;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Time Topology Complex Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A temporal topology complex.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.TimeTopologyComplexType#getPrimitive <em>Primitive</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getTimeTopologyComplexType()
 * @model extendedMetaData="name='TimeTopologyComplexType' kind='elementOnly'"
 * @generated
 */
public interface TimeTopologyComplexType extends AbstractTimeComplexType {
    /**
     * Returns the value of the '<em><b>Primitive</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.TimeTopologyPrimitivePropertyType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Primitive</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Primitive</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getTimeTopologyComplexType_Primitive()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='primitive' namespace='##targetNamespace'"
     * @generated
     */
    EList<TimeTopologyPrimitivePropertyType> getPrimitive();

} // TimeTopologyComplexType
