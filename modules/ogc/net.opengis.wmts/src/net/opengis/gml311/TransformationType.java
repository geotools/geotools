/**
 */
package net.opengis.gml311;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Transformation Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A concrete operation on coordinates that usually includes a change of datum. The parameters of a coordinate transformation are empirically derived from data containing the coordinates of a series of points in both coordinate reference systems. This computational process is usually "over-determined", allowing derivation of error (or accuracy) estimates for the transformation. Also, the stochastic nature of the parameters may result in multiple (different) versions of the same coordinate transformation.
 * 
 * This concrete complexType can be used for all operation methods, without using an Application Schema that defines operation-method-specialized element names and contents, especially for methods with only one Transformation instance. 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.TransformationType#getUsesMethod <em>Uses Method</em>}</li>
 *   <li>{@link net.opengis.gml311.TransformationType#getUsesValue <em>Uses Value</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getTransformationType()
 * @model extendedMetaData="name='TransformationType' kind='elementOnly'"
 * @generated
 */
public interface TransformationType extends AbstractGeneralTransformationType {
    /**
     * Returns the value of the '<em><b>Uses Method</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Association to the operation method used by this coordinate operation. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Uses Method</em>' containment reference.
     * @see #setUsesMethod(OperationMethodRefType)
     * @see net.opengis.gml311.Gml311Package#getTransformationType_UsesMethod()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='usesMethod' namespace='##targetNamespace'"
     * @generated
     */
    OperationMethodRefType getUsesMethod();

    /**
     * Sets the value of the '{@link net.opengis.gml311.TransformationType#getUsesMethod <em>Uses Method</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Uses Method</em>' containment reference.
     * @see #getUsesMethod()
     * @generated
     */
    void setUsesMethod(OperationMethodRefType value);

    /**
     * Returns the value of the '<em><b>Uses Value</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.ParameterValueType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Unordered set of composition associations to the set of parameter values used by this transformation operation. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Uses Value</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getTransformationType_UsesValue()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='usesValue' namespace='##targetNamespace'"
     * @generated
     */
    EList<ParameterValueType> getUsesValue();

} // TransformationType
