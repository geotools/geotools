/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs10;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

import org.w3.xlink.ActuateType;
import org.w3.xlink.ShowType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Content Metadata Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs10.ContentMetadataType#getCoverageOfferingBrief <em>Coverage Offering Brief</em>}</li>
 *   <li>{@link net.opengis.wcs10.ContentMetadataType#getActuate <em>Actuate</em>}</li>
 *   <li>{@link net.opengis.wcs10.ContentMetadataType#getArcrole <em>Arcrole</em>}</li>
 *   <li>{@link net.opengis.wcs10.ContentMetadataType#getHref <em>Href</em>}</li>
 *   <li>{@link net.opengis.wcs10.ContentMetadataType#getRemoteSchema <em>Remote Schema</em>}</li>
 *   <li>{@link net.opengis.wcs10.ContentMetadataType#getRole <em>Role</em>}</li>
 *   <li>{@link net.opengis.wcs10.ContentMetadataType#getShow <em>Show</em>}</li>
 *   <li>{@link net.opengis.wcs10.ContentMetadataType#getTitle <em>Title</em>}</li>
 *   <li>{@link net.opengis.wcs10.ContentMetadataType#getType <em>Type</em>}</li>
 *   <li>{@link net.opengis.wcs10.ContentMetadataType#getUpdateSequence <em>Update Sequence</em>}</li>
 *   <li>{@link net.opengis.wcs10.ContentMetadataType#getVersion <em>Version</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs10.Wcs10Package#getContentMetadataType()
 * @model extendedMetaData="name='ContentMetadata_._type' kind='elementOnly'"
 * @generated
 */
public interface ContentMetadataType extends EObject {
    /**
	 * Returns the value of the '<em><b>Coverage Offering Brief</b></em>' containment reference list.
	 * The list contents are of type {@link net.opengis.wcs10.CoverageOfferingBriefType}.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Coverage Offering Brief</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Coverage Offering Brief</em>' containment reference list.
	 * @see net.opengis.wcs10.Wcs10Package#getContentMetadataType_CoverageOfferingBrief()
	 * @model type="net.opengis.wcs10.CoverageOfferingBriefType" containment="true"
	 *        extendedMetaData="kind='element' name='CoverageOfferingBrief' namespace='##targetNamespace'"
	 * @generated
	 */
    EList getCoverageOfferingBrief();

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
	 * @see net.opengis.wcs10.Wcs10Package#getContentMetadataType_Actuate()
	 * @model default="onLoad" unsettable="true"
	 *        extendedMetaData="kind='attribute' name='actuate' namespace='http://www.w3.org/1999/xlink'"
	 * @generated
	 */
    ActuateType getActuate();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.ContentMetadataType#getActuate <em>Actuate</em>}' attribute.
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
	 * Unsets the value of the '{@link net.opengis.wcs10.ContentMetadataType#getActuate <em>Actuate</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #isSetActuate()
	 * @see #getActuate()
	 * @see #setActuate(ActuateType)
	 * @generated
	 */
    void unsetActuate();

    /**
	 * Returns whether the value of the '{@link net.opengis.wcs10.ContentMetadataType#getActuate <em>Actuate</em>}' attribute is set.
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
	 * @see net.opengis.wcs10.Wcs10Package#getContentMetadataType_Arcrole()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
	 *        extendedMetaData="kind='attribute' name='arcrole' namespace='http://www.w3.org/1999/xlink'"
	 * @generated
	 */
    String getArcrole();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.ContentMetadataType#getArcrole <em>Arcrole</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Arcrole</em>' attribute.
	 * @see #getArcrole()
	 * @generated
	 */
    void setArcrole(String value);

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
	 * @see net.opengis.wcs10.Wcs10Package#getContentMetadataType_Href()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
	 *        extendedMetaData="kind='attribute' name='href' namespace='http://www.w3.org/1999/xlink'"
	 * @generated
	 */
    String getHref();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.ContentMetadataType#getHref <em>Href</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Href</em>' attribute.
	 * @see #getHref()
	 * @generated
	 */
    void setHref(String value);

    /**
	 * Returns the value of the '<em><b>Remote Schema</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Reference to an XML Schema fragment that specifies the content model of the property’s value. This is in conformance with the XML Schema Section 4.14 Referencing Schemas from Elsewhere.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Remote Schema</em>' attribute.
	 * @see #setRemoteSchema(String)
	 * @see net.opengis.wcs10.Wcs10Package#getContentMetadataType_RemoteSchema()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
	 *        extendedMetaData="kind='attribute' name='remoteSchema' namespace='http://www.opengis.net/gml'"
	 * @generated
	 */
    String getRemoteSchema();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.ContentMetadataType#getRemoteSchema <em>Remote Schema</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Remote Schema</em>' attribute.
	 * @see #getRemoteSchema()
	 * @generated
	 */
    void setRemoteSchema(String value);

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
	 * @see net.opengis.wcs10.Wcs10Package#getContentMetadataType_Role()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
	 *        extendedMetaData="kind='attribute' name='role' namespace='http://www.w3.org/1999/xlink'"
	 * @generated
	 */
    String getRole();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.ContentMetadataType#getRole <em>Role</em>}' attribute.
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
	 * @see net.opengis.wcs10.Wcs10Package#getContentMetadataType_Show()
	 * @model default="new" unsettable="true"
	 *        extendedMetaData="kind='attribute' name='show' namespace='http://www.w3.org/1999/xlink'"
	 * @generated
	 */
    ShowType getShow();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.ContentMetadataType#getShow <em>Show</em>}' attribute.
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
	 * Unsets the value of the '{@link net.opengis.wcs10.ContentMetadataType#getShow <em>Show</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #isSetShow()
	 * @see #getShow()
	 * @see #setShow(ShowType)
	 * @generated
	 */
    void unsetShow();

    /**
	 * Returns whether the value of the '{@link net.opengis.wcs10.ContentMetadataType#getShow <em>Show</em>}' attribute is set.
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
	 * @see net.opengis.wcs10.Wcs10Package#getContentMetadataType_Title()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='title' namespace='http://www.w3.org/1999/xlink'"
	 * @generated
	 */
    String getTitle();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.ContentMetadataType#getTitle <em>Title</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Title</em>' attribute.
	 * @see #getTitle()
	 * @generated
	 */
    void setTitle(String value);

    /**
	 * Returns the value of the '<em><b>Type</b></em>' attribute.
	 * The default value is <code>"simple"</code>.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see #isSetType()
	 * @see #unsetType()
	 * @see #setType(String)
	 * @see net.opengis.wcs10.Wcs10Package#getContentMetadataType_Type()
	 * @model default="simple" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='type' namespace='http://www.w3.org/1999/xlink'"
	 * @generated
	 */
    String getType();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.ContentMetadataType#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see #isSetType()
	 * @see #unsetType()
	 * @see #getType()
	 * @generated
	 */
    void setType(String value);

    /**
	 * Unsets the value of the '{@link net.opengis.wcs10.ContentMetadataType#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #isSetType()
	 * @see #getType()
	 * @see #setType(String)
	 * @generated
	 */
    void unsetType();

    /**
	 * Returns whether the value of the '{@link net.opengis.wcs10.ContentMetadataType#getType <em>Type</em>}' attribute is set.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Type</em>' attribute is set.
	 * @see #unsetType()
	 * @see #getType()
	 * @see #setType(String)
	 * @generated
	 */
    boolean isSetType();

    /**
	 * Returns the value of the '<em><b>Update Sequence</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Service metadata document version, having values that are "increased" whenever any change is made in service metadata document. Values are selected by each server, and are always opaque to clients. When not supported by server, server shall not return this attribute.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Update Sequence</em>' attribute.
	 * @see #setUpdateSequence(String)
	 * @see net.opengis.wcs10.Wcs10Package#getContentMetadataType_UpdateSequence()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='updateSequence'"
	 * @generated
	 */
    String getUpdateSequence();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.ContentMetadataType#getUpdateSequence <em>Update Sequence</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Update Sequence</em>' attribute.
	 * @see #getUpdateSequence()
	 * @generated
	 */
    void setUpdateSequence(String value);

    /**
	 * Returns the value of the '<em><b>Version</b></em>' attribute.
	 * The default value is <code>"1.0.0"</code>.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Version</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Version</em>' attribute.
	 * @see #isSetVersion()
	 * @see #unsetVersion()
	 * @see #setVersion(String)
	 * @see net.opengis.wcs10.Wcs10Package#getContentMetadataType_Version()
	 * @model default="1.0.0" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='version'"
	 * @generated
	 */
    String getVersion();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.ContentMetadataType#getVersion <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Version</em>' attribute.
	 * @see #isSetVersion()
	 * @see #unsetVersion()
	 * @see #getVersion()
	 * @generated
	 */
    void setVersion(String value);

    /**
	 * Unsets the value of the '{@link net.opengis.wcs10.ContentMetadataType#getVersion <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #isSetVersion()
	 * @see #getVersion()
	 * @see #setVersion(String)
	 * @generated
	 */
    void unsetVersion();

    /**
	 * Returns whether the value of the '{@link net.opengis.wcs10.ContentMetadataType#getVersion <em>Version</em>}' attribute is set.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Version</em>' attribute is set.
	 * @see #unsetVersion()
	 * @see #getVersion()
	 * @see #setVersion(String)
	 * @generated
	 */
    boolean isSetVersion();

} // ContentMetadataType
