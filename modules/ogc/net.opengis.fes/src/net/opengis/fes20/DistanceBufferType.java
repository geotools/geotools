/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.fes20;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Distance Buffer Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.fes20.DistanceBufferType#getExpressionGroup <em>Expression Group</em>}</li>
 *   <li>{@link net.opengis.fes20.DistanceBufferType#getExpression <em>Expression</em>}</li>
 *   <li>{@link net.opengis.fes20.DistanceBufferType#getAny <em>Any</em>}</li>
 *   <li>{@link net.opengis.fes20.DistanceBufferType#getDistance <em>Distance</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.fes20.Fes20Package#getDistanceBufferType()
 * @model extendedMetaData="name='DistanceBufferType' kind='elementOnly'"
 * @generated
 */
public interface DistanceBufferType extends SpatialOpsType {
    /**
     * Returns the value of the '<em><b>Expression Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Expression Group</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Expression Group</em>' attribute list.
     * @see net.opengis.fes20.Fes20Package#getDistanceBufferType_ExpressionGroup()
     * @model dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="false"
     *        extendedMetaData="kind='group' name='expression:group' namespace='##targetNamespace'"
     * @generated
     */
    FeatureMap getExpressionGroup();

    /**
     * Returns the value of the '<em><b>Expression</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Expression</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Expression</em>' containment reference.
     * @see net.opengis.fes20.Fes20Package#getDistanceBufferType_Expression()
     * @model containment="true" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='expression' namespace='##targetNamespace' group='expression:group'"
     * @generated
     */
    EObject getExpression();

    /**
     * Returns the value of the '<em><b>Any</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Any</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Any</em>' attribute list.
     * @see net.opengis.fes20.Fes20Package#getDistanceBufferType_Any()
     * @model dataType="org.eclipse.emf.ecore.EFeatureMapEntry" required="true" many="false"
     *        extendedMetaData="kind='elementWildcard' wildcards='##other' name=':2' processing='strict'"
     * @generated
     */
    FeatureMap getAny();

    /**
     * Returns the value of the '<em><b>Distance</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Distance</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Distance</em>' containment reference.
     * @see #setDistance(MeasureType)
     * @see net.opengis.fes20.Fes20Package#getDistanceBufferType_Distance()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='Distance' namespace='##targetNamespace'"
     * @generated
     */
    MeasureType getDistance();

    /**
     * Sets the value of the '{@link net.opengis.fes20.DistanceBufferType#getDistance <em>Distance</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Distance</em>' containment reference.
     * @see #getDistance()
     * @generated
     */
    void setDistance(MeasureType value);

} // DistanceBufferType
