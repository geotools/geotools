/**
 */
package net.opengis.gml311;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Geometry Array Property Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A container for an array of geometry elements. The elements are always contained in the array property, 
 * 			referencing geometry elements or arrays of geometry elements is not supported.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.GeometryArrayPropertyType#getGeometryGroup <em>Geometry Group</em>}</li>
 *   <li>{@link net.opengis.gml311.GeometryArrayPropertyType#getGeometry <em>Geometry</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getGeometryArrayPropertyType()
 * @model extendedMetaData="name='GeometryArrayPropertyType' kind='elementOnly'"
 * @generated
 */
public interface GeometryArrayPropertyType extends EObject {
    /**
     * Returns the value of the '<em><b>Geometry Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The "_Geometry" element is the abstract head of the substituition group for all geometry elements of GML 3. This 
     * 			includes pre-defined and user-defined geometry elements. Any geometry element must be a direct or indirect extension/restriction 
     * 			of AbstractGeometryType and must be directly or indirectly in the substitution group of "_Geometry".
     * <!-- end-model-doc -->
     * @return the value of the '<em>Geometry Group</em>' attribute list.
     * @see net.opengis.gml311.Gml311Package#getGeometryArrayPropertyType_GeometryGroup()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        extendedMetaData="kind='group' name='_Geometry:group' namespace='##targetNamespace'"
     * @generated
     */
    FeatureMap getGeometryGroup();

    /**
     * Returns the value of the '<em><b>Geometry</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.AbstractGeometryType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The "_Geometry" element is the abstract head of the substituition group for all geometry elements of GML 3. This 
     * 			includes pre-defined and user-defined geometry elements. Any geometry element must be a direct or indirect extension/restriction 
     * 			of AbstractGeometryType and must be directly or indirectly in the substitution group of "_Geometry".
     * <!-- end-model-doc -->
     * @return the value of the '<em>Geometry</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getGeometryArrayPropertyType_Geometry()
     * @model containment="true" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='_Geometry' namespace='##targetNamespace' group='_Geometry:group'"
     * @generated
     */
    EList<AbstractGeometryType> getGeometry();

} // GeometryArrayPropertyType
