/**
 */
package net.opengis.gml311;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Composite Value Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Aggregate value built from other Values using the Composite pattern. It contains zero or an arbitrary number of valueComponent elements, and zero or one valueComponents elements.  It may be used for strongly coupled aggregates (vectors, tensors) or for arbitrary collections of values.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.CompositeValueType#getValueComponent <em>Value Component</em>}</li>
 *   <li>{@link net.opengis.gml311.CompositeValueType#getValueComponents <em>Value Components</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getCompositeValueType()
 * @model extendedMetaData="name='CompositeValueType' kind='elementOnly'"
 * @generated
 */
public interface CompositeValueType extends AbstractGMLType {
    /**
     * Returns the value of the '<em><b>Value Component</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.ValuePropertyType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Element which refers to, or contains, a Value.  This version is used in CompositeValues.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Value Component</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getCompositeValueType_ValueComponent()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='valueComponent' namespace='##targetNamespace'"
     * @generated
     */
    EList<ValuePropertyType> getValueComponent();

    /**
     * Returns the value of the '<em><b>Value Components</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Element which refers to, or contains, a set of homogeneously typed Values.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Value Components</em>' containment reference.
     * @see #setValueComponents(ValueArrayPropertyType)
     * @see net.opengis.gml311.Gml311Package#getCompositeValueType_ValueComponents()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='valueComponents' namespace='##targetNamespace'"
     * @generated
     */
    ValueArrayPropertyType getValueComponents();

    /**
     * Sets the value of the '{@link net.opengis.gml311.CompositeValueType#getValueComponents <em>Value Components</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Value Components</em>' containment reference.
     * @see #getValueComponents()
     * @generated
     */
    void setValueComponents(ValueArrayPropertyType value);

} // CompositeValueType
