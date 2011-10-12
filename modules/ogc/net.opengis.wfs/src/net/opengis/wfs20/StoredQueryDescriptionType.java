/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs20;

import net.opengis.ows11.MetadataType;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Stored Query Description Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs20.StoredQueryDescriptionType#getTitle <em>Title</em>}</li>
 *   <li>{@link net.opengis.wfs20.StoredQueryDescriptionType#getAbstract <em>Abstract</em>}</li>
 *   <li>{@link net.opengis.wfs20.StoredQueryDescriptionType#getMetadata <em>Metadata</em>}</li>
 *   <li>{@link net.opengis.wfs20.StoredQueryDescriptionType#getParameter <em>Parameter</em>}</li>
 *   <li>{@link net.opengis.wfs20.StoredQueryDescriptionType#getQueryExpressionText <em>Query Expression Text</em>}</li>
 *   <li>{@link net.opengis.wfs20.StoredQueryDescriptionType#getId <em>Id</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs20.Wfs20Package#getStoredQueryDescriptionType()
 * @model extendedMetaData="name='StoredQueryDescriptionType' kind='elementOnly'"
 * @generated
 */
public interface StoredQueryDescriptionType extends EObject {
    /**
     * Returns the value of the '<em><b>Title</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.wfs20.TitleType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Title</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Title</em>' containment reference list.
     * @see net.opengis.wfs20.Wfs20Package#getStoredQueryDescriptionType_Title()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='Title' namespace='##targetNamespace'"
     * @generated
     */
    EList<TitleType> getTitle();

    /**
     * Returns the value of the '<em><b>Abstract</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.wfs20.AbstractType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Abstract</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Abstract</em>' containment reference list.
     * @see net.opengis.wfs20.Wfs20Package#getStoredQueryDescriptionType_Abstract()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='Abstract' namespace='##targetNamespace'"
     * @generated
     */
    EList<AbstractType> getAbstract();

    /**
     * Returns the value of the '<em><b>Metadata</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.ows11.MetadataType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Metadata</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Metadata</em>' containment reference list.
     * @see net.opengis.wfs20.Wfs20Package#getStoredQueryDescriptionType_Metadata()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='Metadata' namespace='http://www.opengis.net/ows/1.1'"
     * @generated
     */
    EList<MetadataType> getMetadata();

    /**
     * Returns the value of the '<em><b>Parameter</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.wfs20.ParameterExpressionType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Parameter</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Parameter</em>' containment reference list.
     * @see net.opengis.wfs20.Wfs20Package#getStoredQueryDescriptionType_Parameter()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='Parameter' namespace='##targetNamespace'"
     * @generated
     */
    EList<ParameterExpressionType> getParameter();

    /**
     * Returns the value of the '<em><b>Query Expression Text</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.wfs20.QueryExpressionTextType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Query Expression Text</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Query Expression Text</em>' containment reference list.
     * @see net.opengis.wfs20.Wfs20Package#getStoredQueryDescriptionType_QueryExpressionText()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='QueryExpressionText' namespace='##targetNamespace'"
     * @generated
     */
    EList<QueryExpressionTextType> getQueryExpressionText();

    /**
     * Returns the value of the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Id</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Id</em>' attribute.
     * @see #setId(String)
     * @see net.opengis.wfs20.Wfs20Package#getStoredQueryDescriptionType_Id()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI" required="true"
     *        extendedMetaData="kind='attribute' name='id'"
     * @generated
     */
    String getId();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.StoredQueryDescriptionType#getId <em>Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Id</em>' attribute.
     * @see #getId()
     * @generated
     */
    void setId(String value);

} // StoredQueryDescriptionType
