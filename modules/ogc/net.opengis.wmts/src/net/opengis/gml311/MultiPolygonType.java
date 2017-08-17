/**
 */
package net.opengis.gml311;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Multi Polygon Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A MultiPolygon is defined by one or more Polygons, referenced through polygonMember elements. Deprecated with GML version 3.0. Use MultiSurfaceType instead.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.MultiPolygonType#getPolygonMember <em>Polygon Member</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getMultiPolygonType()
 * @model extendedMetaData="name='MultiPolygonType' kind='elementOnly'"
 * @generated
 */
public interface MultiPolygonType extends AbstractGeometricAggregateType {
    /**
     * Returns the value of the '<em><b>Polygon Member</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.PolygonPropertyType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Deprecated with GML 3.0 and included only for backwards compatibility with GML 2.0. Use "surfaceMember" instead.
     * This property element either references a polygon via the XLink-attributes or contains the polygon element.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Polygon Member</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getMultiPolygonType_PolygonMember()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='polygonMember' namespace='##targetNamespace'"
     * @generated
     */
    EList<PolygonPropertyType> getPolygonMember();

} // MultiPolygonType
