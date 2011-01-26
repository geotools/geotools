/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.w3.xlink;

import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Document Root</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.w3.xlink.DocumentRoot#getMixed <em>Mixed</em>}</li>
 *   <li>{@link org.w3.xlink.DocumentRoot#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}</li>
 *   <li>{@link org.w3.xlink.DocumentRoot#getXSISchemaLocation <em>XSI Schema Location</em>}</li>
 *   <li>{@link org.w3.xlink.DocumentRoot#getActuate <em>Actuate</em>}</li>
 *   <li>{@link org.w3.xlink.DocumentRoot#getArcrole <em>Arcrole</em>}</li>
 *   <li>{@link org.w3.xlink.DocumentRoot#getFrom <em>From</em>}</li>
 *   <li>{@link org.w3.xlink.DocumentRoot#getHref <em>Href</em>}</li>
 *   <li>{@link org.w3.xlink.DocumentRoot#getLabel <em>Label</em>}</li>
 *   <li>{@link org.w3.xlink.DocumentRoot#getRole <em>Role</em>}</li>
 *   <li>{@link org.w3.xlink.DocumentRoot#getShow <em>Show</em>}</li>
 *   <li>{@link org.w3.xlink.DocumentRoot#getTitle <em>Title</em>}</li>
 *   <li>{@link org.w3.xlink.DocumentRoot#getTo <em>To</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.w3.xlink.XlinkPackage#getDocumentRoot()
 * @model extendedMetaData="name='' kind='mixed'"
 * @generated
 */
public interface DocumentRoot extends EObject {
    /**
	 * Returns the value of the '<em><b>Mixed</b></em>' attribute list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Mixed</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Mixed</em>' attribute list.
	 * @see org.w3.xlink.XlinkPackage#getDocumentRoot_Mixed()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
	 *        extendedMetaData="kind='elementWildcard' name=':mixed'"
	 * @generated
	 */
    FeatureMap getMixed();

    /**
	 * Returns the value of the '<em><b>XMLNS Prefix Map</b></em>' map.
	 * The key is of type {@link java.lang.String},
	 * and the value is of type {@link java.lang.String},
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>XMLNS Prefix Map</em>' map isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>XMLNS Prefix Map</em>' map.
	 * @see org.w3.xlink.XlinkPackage#getDocumentRoot_XMLNSPrefixMap()
	 * @model mapType="org.eclipse.emf.ecore.EStringToStringMapEntry" keyType="java.lang.String" valueType="java.lang.String" transient="true"
	 *        extendedMetaData="kind='attribute' name='xmlns:prefix'"
	 * @generated
	 */
    EMap getXMLNSPrefixMap();

    /**
	 * Returns the value of the '<em><b>XSI Schema Location</b></em>' map.
	 * The key is of type {@link java.lang.String},
	 * and the value is of type {@link java.lang.String},
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>XSI Schema Location</em>' map isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>XSI Schema Location</em>' map.
	 * @see org.w3.xlink.XlinkPackage#getDocumentRoot_XSISchemaLocation()
	 * @model mapType="org.eclipse.emf.ecore.EStringToStringMapEntry" keyType="java.lang.String" valueType="java.lang.String" transient="true"
	 *        extendedMetaData="kind='attribute' name='xsi:schemaLocation'"
	 * @generated
	 */
    EMap getXSISchemaLocation();

    /**
	 * Returns the value of the '<em><b>Actuate</b></em>' attribute.
	 * The default value is <code>"onLoad"</code>.
	 * The literals are from the enumeration {@link org.w3.xlink.ActuateType}.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *         The 'actuate' attribute is used to communicate the desired timing
	 *         of traversal from the starting resource to the ending resource;
	 *         it's value should be treated as follows:
	 *         onLoad - traverse to the ending resource immediately on loading
	 *                  the starting resource
	 *         onRequest - traverse from the starting resource to the ending
	 *                     resource only on a post-loading event triggered for
	 *                     this purpose
	 *         other - behavior is unconstrained; examine other markup in link
	 *                 for hints
	 *         none - behavior is unconstrained
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Actuate</em>' attribute.
	 * @see org.w3.xlink.ActuateType
	 * @see #isSetActuate()
	 * @see #unsetActuate()
	 * @see #setActuate(ActuateType)
	 * @see org.w3.xlink.XlinkPackage#getDocumentRoot_Actuate()
	 * @model default="onLoad" unsettable="true"
	 *        extendedMetaData="kind='attribute' name='actuate' namespace='##targetNamespace'"
	 * @generated
	 */
    ActuateType getActuate();

    /**
	 * Sets the value of the '{@link org.w3.xlink.DocumentRoot#getActuate <em>Actuate</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Actuate</em>' attribute.
	 * @see org.w3.xlink.ActuateType
	 * @see #isSetActuate()
	 * @see #unsetActuate()
	 * @see #getActuate()
	 * @generated
	 */
    void setActuate(ActuateType value);

    /**
	 * Unsets the value of the '{@link org.w3.xlink.DocumentRoot#getActuate <em>Actuate</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #isSetActuate()
	 * @see #getActuate()
	 * @see #setActuate(ActuateType)
	 * @generated
	 */
    void unsetActuate();

    /**
	 * Returns whether the value of the '{@link org.w3.xlink.DocumentRoot#getActuate <em>Actuate</em>}' attribute is set.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Actuate</em>' attribute is set.
	 * @see #unsetActuate()
	 * @see #getActuate()
	 * @see #setActuate(ActuateType)
	 * @generated
	 */
    boolean isSetActuate();

    /**
	 * Returns the value of the '<em><b>Arcrole</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Arcrole</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Arcrole</em>' attribute.
	 * @see #setArcrole(String)
	 * @see org.w3.xlink.XlinkPackage#getDocumentRoot_Arcrole()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
	 *        extendedMetaData="kind='attribute' name='arcrole' namespace='##targetNamespace'"
	 * @generated
	 */
    String getArcrole();

    /**
	 * Sets the value of the '{@link org.w3.xlink.DocumentRoot#getArcrole <em>Arcrole</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Arcrole</em>' attribute.
	 * @see #getArcrole()
	 * @generated
	 */
    void setArcrole(String value);

    /**
	 * Returns the value of the '<em><b>From</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>From</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>From</em>' attribute.
	 * @see #setFrom(String)
	 * @see org.w3.xlink.XlinkPackage#getDocumentRoot_From()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='from' namespace='##targetNamespace'"
	 * @generated
	 */
    String getFrom();

    /**
	 * Sets the value of the '{@link org.w3.xlink.DocumentRoot#getFrom <em>From</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>From</em>' attribute.
	 * @see #getFrom()
	 * @generated
	 */
    void setFrom(String value);

    /**
	 * Returns the value of the '<em><b>Href</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Href</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Href</em>' attribute.
	 * @see #setHref(String)
	 * @see org.w3.xlink.XlinkPackage#getDocumentRoot_Href()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
	 *        extendedMetaData="kind='attribute' name='href' namespace='##targetNamespace'"
	 * @generated
	 */
    String getHref();

    /**
	 * Sets the value of the '{@link org.w3.xlink.DocumentRoot#getHref <em>Href</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Href</em>' attribute.
	 * @see #getHref()
	 * @generated
	 */
    void setHref(String value);

    /**
	 * Returns the value of the '<em><b>Label</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Label</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Label</em>' attribute.
	 * @see #setLabel(String)
	 * @see org.w3.xlink.XlinkPackage#getDocumentRoot_Label()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='label' namespace='##targetNamespace'"
	 * @generated
	 */
    String getLabel();

    /**
	 * Sets the value of the '{@link org.w3.xlink.DocumentRoot#getLabel <em>Label</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Label</em>' attribute.
	 * @see #getLabel()
	 * @generated
	 */
    void setLabel(String value);

    /**
	 * Returns the value of the '<em><b>Role</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Role</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Role</em>' attribute.
	 * @see #setRole(String)
	 * @see org.w3.xlink.XlinkPackage#getDocumentRoot_Role()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
	 *        extendedMetaData="kind='attribute' name='role' namespace='##targetNamespace'"
	 * @generated
	 */
    String getRole();

    /**
	 * Sets the value of the '{@link org.w3.xlink.DocumentRoot#getRole <em>Role</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Role</em>' attribute.
	 * @see #getRole()
	 * @generated
	 */
    void setRole(String value);

    /**
	 * Returns the value of the '<em><b>Show</b></em>' attribute.
	 * The default value is <code>"new"</code>.
	 * The literals are from the enumeration {@link org.w3.xlink.ShowType}.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *         The 'show' attribute is used to communicate the desired presentation
	 *         of the ending resource on traversal from the starting resource; it's
	 *         value should be treated as follows:
	 *         new - load ending resource in a new window, frame, pane, or other
	 *               presentation context
	 *         replace - load the resource in the same window, frame, pane, or
	 *                   other presentation context
	 *         embed - load ending resource in place of the presentation of the
	 *                 starting resource
	 *         other - behavior is unconstrained; examine other markup in the
	 *                 link for hints
	 *         none - behavior is unconstrained
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Show</em>' attribute.
	 * @see org.w3.xlink.ShowType
	 * @see #isSetShow()
	 * @see #unsetShow()
	 * @see #setShow(ShowType)
	 * @see org.w3.xlink.XlinkPackage#getDocumentRoot_Show()
	 * @model default="new" unsettable="true"
	 *        extendedMetaData="kind='attribute' name='show' namespace='##targetNamespace'"
	 * @generated
	 */
    ShowType getShow();

    /**
	 * Sets the value of the '{@link org.w3.xlink.DocumentRoot#getShow <em>Show</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Show</em>' attribute.
	 * @see org.w3.xlink.ShowType
	 * @see #isSetShow()
	 * @see #unsetShow()
	 * @see #getShow()
	 * @generated
	 */
    void setShow(ShowType value);

    /**
	 * Unsets the value of the '{@link org.w3.xlink.DocumentRoot#getShow <em>Show</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #isSetShow()
	 * @see #getShow()
	 * @see #setShow(ShowType)
	 * @generated
	 */
    void unsetShow();

    /**
	 * Returns whether the value of the '{@link org.w3.xlink.DocumentRoot#getShow <em>Show</em>}' attribute is set.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Show</em>' attribute is set.
	 * @see #unsetShow()
	 * @see #getShow()
	 * @see #setShow(ShowType)
	 * @generated
	 */
    boolean isSetShow();

    /**
	 * Returns the value of the '<em><b>Title</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Title</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Title</em>' attribute.
	 * @see #setTitle(String)
	 * @see org.w3.xlink.XlinkPackage#getDocumentRoot_Title()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='title' namespace='##targetNamespace'"
	 * @generated
	 */
    String getTitle();

    /**
	 * Sets the value of the '{@link org.w3.xlink.DocumentRoot#getTitle <em>Title</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Title</em>' attribute.
	 * @see #getTitle()
	 * @generated
	 */
    void setTitle(String value);

    /**
	 * Returns the value of the '<em><b>To</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>To</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>To</em>' attribute.
	 * @see #setTo(String)
	 * @see org.w3.xlink.XlinkPackage#getDocumentRoot_To()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='to' namespace='##targetNamespace'"
	 * @generated
	 */
    String getTo();

    /**
	 * Sets the value of the '{@link org.w3.xlink.DocumentRoot#getTo <em>To</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>To</em>' attribute.
	 * @see #getTo()
	 * @generated
	 */
    void setTo(String value);

} // DocumentRoot
