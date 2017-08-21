/**
 */
package net.opengis.gml311;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Abstract Time Primitive Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * The abstract supertype for temporal primitives.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.AbstractTimePrimitiveType#getRelatedTime <em>Related Time</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getAbstractTimePrimitiveType()
 * @model abstract="true"
 *        extendedMetaData="name='AbstractTimePrimitiveType' kind='elementOnly'"
 * @generated
 */
public interface AbstractTimePrimitiveType extends AbstractTimeObjectType {
    /**
     * Returns the value of the '<em><b>Related Time</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.RelatedTimeType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Related Time</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Related Time</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getAbstractTimePrimitiveType_RelatedTime()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='relatedTime' namespace='##targetNamespace'"
     * @generated
     */
    EList<RelatedTimeType> getRelatedTime();

} // AbstractTimePrimitiveType
