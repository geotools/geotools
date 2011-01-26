/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfsv;

import javax.xml.datatype.XMLGregorianCalendar;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Abstract Versioned Feature Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfsv.AbstractVersionedFeatureType#getVersion <em>Version</em>}</li>
 *   <li>{@link net.opengis.wfsv.AbstractVersionedFeatureType#getAuthor <em>Author</em>}</li>
 *   <li>{@link net.opengis.wfsv.AbstractVersionedFeatureType#getDate <em>Date</em>}</li>
 *   <li>{@link net.opengis.wfsv.AbstractVersionedFeatureType#getMessage <em>Message</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfsv.WfsvPackage#getAbstractVersionedFeatureType()
 * @model abstract="true"
 *        extendedMetaData="name='AbstractVersionedFeatureType' kind='elementOnly'"
 * @generated
 */
public interface AbstractVersionedFeatureType extends EObject {
    /**
     * Returns the value of the '<em><b>Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Version</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Version</em>' attribute.
     * @see #setVersion(String)
     * @see net.opengis.wfsv.WfsvPackage#getAbstractVersionedFeatureType_Version()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     *        extendedMetaData="kind='element' name='version' namespace='##targetNamespace'"
     * @generated
     */
    String getVersion();

    /**
     * Sets the value of the '{@link net.opengis.wfsv.AbstractVersionedFeatureType#getVersion <em>Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Version</em>' attribute.
     * @see #getVersion()
     * @generated
     */
    void setVersion(String value);

    /**
     * Returns the value of the '<em><b>Author</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Author</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Author</em>' attribute.
     * @see #setAuthor(String)
     * @see net.opengis.wfsv.WfsvPackage#getAbstractVersionedFeatureType_Author()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='element' name='author' namespace='##targetNamespace'"
     * @generated
     */
    String getAuthor();

    /**
     * Sets the value of the '{@link net.opengis.wfsv.AbstractVersionedFeatureType#getAuthor <em>Author</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Author</em>' attribute.
     * @see #getAuthor()
     * @generated
     */
    void setAuthor(String value);

    /**
     * Returns the value of the '<em><b>Date</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Date</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Date</em>' attribute.
     * @see #setDate(XMLGregorianCalendar)
     * @see net.opengis.wfsv.WfsvPackage#getAbstractVersionedFeatureType_Date()
     * @model dataType="org.eclipse.emf.ecore.xml.type.DateTime" required="true"
     *        extendedMetaData="kind='element' name='date' namespace='##targetNamespace'"
     * @generated
     */
    XMLGregorianCalendar getDate();

    /**
     * Sets the value of the '{@link net.opengis.wfsv.AbstractVersionedFeatureType#getDate <em>Date</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Date</em>' attribute.
     * @see #getDate()
     * @generated
     */
    void setDate(XMLGregorianCalendar value);

    /**
     * Returns the value of the '<em><b>Message</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Message</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Message</em>' attribute.
     * @see #setMessage(String)
     * @see net.opengis.wfsv.WfsvPackage#getAbstractVersionedFeatureType_Message()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='element' name='message' namespace='##targetNamespace'"
     * @generated
     */
    String getMessage();

    /**
     * Sets the value of the '{@link net.opengis.wfsv.AbstractVersionedFeatureType#getMessage <em>Message</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Message</em>' attribute.
     * @see #getMessage()
     * @generated
     */
    void setMessage(String value);

} // AbstractVersionedFeatureType
