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
 * A representation of the model object '<em><b>Property Is Nil Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.fes20.PropertyIsNilType#getExpressionGroup <em>Expression Group</em>}</li>
 *   <li>{@link net.opengis.fes20.PropertyIsNilType#getExpression <em>Expression</em>}</li>
 *   <li>{@link net.opengis.fes20.PropertyIsNilType#getNilReason <em>Nil Reason</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.fes20.Fes20Package#getPropertyIsNilType()
 * @model extendedMetaData="name='PropertyIsNilType' kind='elementOnly'"
 * @generated
 */
public interface PropertyIsNilType extends ComparisonOpsType {
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
     * @see net.opengis.fes20.Fes20Package#getPropertyIsNilType_ExpressionGroup()
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
     * @see net.opengis.fes20.Fes20Package#getPropertyIsNilType_Expression()
     * @model containment="true" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='expression' namespace='##targetNamespace' group='expression:group'"
     * @generated
     */
    EObject getExpression();

    /**
     * Returns the value of the '<em><b>Nil Reason</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Nil Reason</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Nil Reason</em>' attribute.
     * @see #setNilReason(String)
     * @see net.opengis.fes20.Fes20Package#getPropertyIsNilType_NilReason()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='nilReason'"
     * @generated
     */
    String getNilReason();

    /**
     * Sets the value of the '{@link net.opengis.fes20.PropertyIsNilType#getNilReason <em>Nil Reason</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Nil Reason</em>' attribute.
     * @see #getNilReason()
     * @generated
     */
    void setNilReason(String value);

} // PropertyIsNilType
