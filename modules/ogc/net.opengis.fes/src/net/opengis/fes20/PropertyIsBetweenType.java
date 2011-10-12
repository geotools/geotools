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
 * A representation of the model object '<em><b>Property Is Between Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.fes20.PropertyIsBetweenType#getExpressionGroup <em>Expression Group</em>}</li>
 *   <li>{@link net.opengis.fes20.PropertyIsBetweenType#getExpression <em>Expression</em>}</li>
 *   <li>{@link net.opengis.fes20.PropertyIsBetweenType#getLowerBoundary <em>Lower Boundary</em>}</li>
 *   <li>{@link net.opengis.fes20.PropertyIsBetweenType#getUpperBoundary <em>Upper Boundary</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.fes20.Fes20Package#getPropertyIsBetweenType()
 * @model extendedMetaData="name='PropertyIsBetweenType' kind='elementOnly'"
 * @generated
 */
public interface PropertyIsBetweenType extends ComparisonOpsType {
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
     * @see net.opengis.fes20.Fes20Package#getPropertyIsBetweenType_ExpressionGroup()
     * @model dataType="org.eclipse.emf.ecore.EFeatureMapEntry" required="true" many="false"
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
     * @see net.opengis.fes20.Fes20Package#getPropertyIsBetweenType_Expression()
     * @model containment="true" required="true" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='expression' namespace='##targetNamespace' group='expression:group'"
     * @generated
     */
    EObject getExpression();

    /**
     * Returns the value of the '<em><b>Lower Boundary</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Lower Boundary</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Lower Boundary</em>' containment reference.
     * @see #setLowerBoundary(LowerBoundaryType)
     * @see net.opengis.fes20.Fes20Package#getPropertyIsBetweenType_LowerBoundary()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='LowerBoundary' namespace='##targetNamespace'"
     * @generated
     */
    LowerBoundaryType getLowerBoundary();

    /**
     * Sets the value of the '{@link net.opengis.fes20.PropertyIsBetweenType#getLowerBoundary <em>Lower Boundary</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Lower Boundary</em>' containment reference.
     * @see #getLowerBoundary()
     * @generated
     */
    void setLowerBoundary(LowerBoundaryType value);

    /**
     * Returns the value of the '<em><b>Upper Boundary</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Upper Boundary</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Upper Boundary</em>' containment reference.
     * @see #setUpperBoundary(UpperBoundaryType)
     * @see net.opengis.fes20.Fes20Package#getPropertyIsBetweenType_UpperBoundary()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='UpperBoundary' namespace='##targetNamespace'"
     * @generated
     */
    UpperBoundaryType getUpperBoundary();

    /**
     * Sets the value of the '{@link net.opengis.fes20.PropertyIsBetweenType#getUpperBoundary <em>Upper Boundary</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Upper Boundary</em>' containment reference.
     * @see #getUpperBoundary()
     * @generated
     */
    void setUpperBoundary(UpperBoundaryType value);

} // PropertyIsBetweenType
