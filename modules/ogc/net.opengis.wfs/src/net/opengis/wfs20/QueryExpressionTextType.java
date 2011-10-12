/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs20;

import java.util.List;

import javax.xml.namespace.QName;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Query Expression Text Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs20.QueryExpressionTextType#isIsPrivate <em>Is Private</em>}</li>
 *   <li>{@link net.opengis.wfs20.QueryExpressionTextType#getLanguage <em>Language</em>}</li>
 *   <li>{@link net.opengis.wfs20.QueryExpressionTextType#getReturnFeatureTypes <em>Return Feature Types</em>}</li>
 *   <li>{@link net.opengis.wfs20.QueryExpressionTextType#getValue <em>Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs20.Wfs20Package#getQueryExpressionTextType()
 * @model extendedMetaData="name='QueryExpressionTextType' kind='mixed'"
 * @generated
 */
public interface QueryExpressionTextType extends EObject {

    /**
     * Returns the value of the '<em><b>Is Private</b></em>' attribute.
     * The default value is <code>"false"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Is Private</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Is Private</em>' attribute.
     * @see #isSetIsPrivate()
     * @see #unsetIsPrivate()
     * @see #setIsPrivate(boolean)
     * @see net.opengis.wfs20.Wfs20Package#getQueryExpressionTextType_IsPrivate()
     * @model default="false" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
     *        extendedMetaData="kind='attribute' name='isPrivate'"
     * @generated
     */
    boolean isIsPrivate();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.QueryExpressionTextType#isIsPrivate <em>Is Private</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Is Private</em>' attribute.
     * @see #isSetIsPrivate()
     * @see #unsetIsPrivate()
     * @see #isIsPrivate()
     * @generated
     */
    void setIsPrivate(boolean value);

    /**
     * Unsets the value of the '{@link net.opengis.wfs20.QueryExpressionTextType#isIsPrivate <em>Is Private</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetIsPrivate()
     * @see #isIsPrivate()
     * @see #setIsPrivate(boolean)
     * @generated
     */
    void unsetIsPrivate();

    /**
     * Returns whether the value of the '{@link net.opengis.wfs20.QueryExpressionTextType#isIsPrivate <em>Is Private</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Is Private</em>' attribute is set.
     * @see #unsetIsPrivate()
     * @see #isIsPrivate()
     * @see #setIsPrivate(boolean)
     * @generated
     */
    boolean isSetIsPrivate();

    /**
     * Returns the value of the '<em><b>Language</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Language</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Language</em>' attribute.
     * @see #setLanguage(String)
     * @see net.opengis.wfs20.Wfs20Package#getQueryExpressionTextType_Language()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI" required="true"
     *        extendedMetaData="kind='attribute' name='language'"
     * @generated
     */
    String getLanguage();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.QueryExpressionTextType#getLanguage <em>Language</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Language</em>' attribute.
     * @see #getLanguage()
     * @generated
     */
    void setLanguage(String value);

    /**
     * Returns the value of the '<em><b>Return Feature Types</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Return Feature Types</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Return Feature Types</em>' attribute.
     * @see #setReturnFeatureTypes(List)
     * @see net.opengis.wfs20.Wfs20Package#getQueryExpressionTextType_ReturnFeatureTypes()
     * @model dataType="net.opengis.wfs20.ReturnFeatureTypesListType" required="true" many="false"
     *        extendedMetaData="kind='attribute' name='returnFeatureTypes'"
     * @generated
     */
    List<QName> getReturnFeatureTypes();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.QueryExpressionTextType#getReturnFeatureTypes <em>Return Feature Types</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Return Feature Types</em>' attribute.
     * @see #getReturnFeatureTypes()
     * @generated
     */
    void setReturnFeatureTypes(List<QName> value);
    
    /**
     * The query expression text content.
     * @model
     */
    String getValue();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.QueryExpressionTextType#getValue <em>Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Value</em>' attribute.
     * @see #getValue()
     * @generated
     */
    void setValue(String value);

} // QueryExpressionTextType
