/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.fes20;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Property Is Like Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.fes20.PropertyIsLikeType#getExpressionGroup <em>Expression Group</em>}</li>
 *   <li>{@link net.opengis.fes20.PropertyIsLikeType#getExpression <em>Expression</em>}</li>
 *   <li>{@link net.opengis.fes20.PropertyIsLikeType#getEscapeChar <em>Escape Char</em>}</li>
 *   <li>{@link net.opengis.fes20.PropertyIsLikeType#getSingleChar <em>Single Char</em>}</li>
 *   <li>{@link net.opengis.fes20.PropertyIsLikeType#getWildCard <em>Wild Card</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.fes20.Fes20Package#getPropertyIsLikeType()
 * @model extendedMetaData="name='PropertyIsLikeType' kind='elementOnly'"
 * @generated
 */
public interface PropertyIsLikeType extends ComparisonOpsType {
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
     * @see net.opengis.fes20.Fes20Package#getPropertyIsLikeType_ExpressionGroup()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" lower="2" upper="2"
     *        extendedMetaData="kind='group' name='expression:group' namespace='##targetNamespace'"
     * @generated
     */
    FeatureMap getExpressionGroup();

    /**
     * Returns the value of the '<em><b>Expression</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.emf.ecore.EObject}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Expression</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Expression</em>' containment reference list.
     * @see net.opengis.fes20.Fes20Package#getPropertyIsLikeType_Expression()
     * @model containment="true" lower="2" upper="2" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='expression' namespace='##targetNamespace' group='expression:group'"
     * @generated
     */
    EList<EObject> getExpression();

    /**
     * Returns the value of the '<em><b>Escape Char</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Escape Char</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Escape Char</em>' attribute.
     * @see #setEscapeChar(String)
     * @see net.opengis.fes20.Fes20Package#getPropertyIsLikeType_EscapeChar()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     *        extendedMetaData="kind='attribute' name='escapeChar'"
     * @generated
     */
    String getEscapeChar();

    /**
     * Sets the value of the '{@link net.opengis.fes20.PropertyIsLikeType#getEscapeChar <em>Escape Char</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Escape Char</em>' attribute.
     * @see #getEscapeChar()
     * @generated
     */
    void setEscapeChar(String value);

    /**
     * Returns the value of the '<em><b>Single Char</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Single Char</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Single Char</em>' attribute.
     * @see #setSingleChar(String)
     * @see net.opengis.fes20.Fes20Package#getPropertyIsLikeType_SingleChar()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     *        extendedMetaData="kind='attribute' name='singleChar'"
     * @generated
     */
    String getSingleChar();

    /**
     * Sets the value of the '{@link net.opengis.fes20.PropertyIsLikeType#getSingleChar <em>Single Char</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Single Char</em>' attribute.
     * @see #getSingleChar()
     * @generated
     */
    void setSingleChar(String value);

    /**
     * Returns the value of the '<em><b>Wild Card</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Wild Card</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Wild Card</em>' attribute.
     * @see #setWildCard(String)
     * @see net.opengis.fes20.Fes20Package#getPropertyIsLikeType_WildCard()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     *        extendedMetaData="kind='attribute' name='wildCard'"
     * @generated
     */
    String getWildCard();

    /**
     * Sets the value of the '{@link net.opengis.fes20.PropertyIsLikeType#getWildCard <em>Wild Card</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Wild Card</em>' attribute.
     * @see #getWildCard()
     * @generated
     */
    void setWildCard(String value);

} // PropertyIsLikeType
