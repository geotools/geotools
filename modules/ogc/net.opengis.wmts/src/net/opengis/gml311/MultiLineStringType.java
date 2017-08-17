/**
 */
package net.opengis.gml311;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Multi Line String Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A MultiLineString is defined by one or more LineStrings, referenced through lineStringMember elements. Deprecated with GML version 3.0. Use MultiCurveType instead.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.MultiLineStringType#getLineStringMember <em>Line String Member</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getMultiLineStringType()
 * @model extendedMetaData="name='MultiLineStringType' kind='elementOnly'"
 * @generated
 */
public interface MultiLineStringType extends AbstractGeometricAggregateType {
    /**
     * Returns the value of the '<em><b>Line String Member</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.LineStringPropertyType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Deprecated with GML 3.0 and included only for backwards compatibility with GML 2.0. Use "curveMember" instead.
     * This property element either references a line string via the XLink-attributes or contains the line string element.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Line String Member</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getMultiLineStringType_LineStringMember()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='lineStringMember' namespace='##targetNamespace'"
     * @generated
     */
    EList<LineStringPropertyType> getLineStringMember();

} // MultiLineStringType
