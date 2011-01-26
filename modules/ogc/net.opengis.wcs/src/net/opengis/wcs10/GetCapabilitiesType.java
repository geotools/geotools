/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs10;

import java.util.Map;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Get Capabilities Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs10.GetCapabilitiesType#getSection <em>Section</em>}</li>
 *   <li>{@link net.opengis.wcs10.GetCapabilitiesType#getService <em>Service</em>}</li>
 *   <li>{@link net.opengis.wcs10.GetCapabilitiesType#getUpdateSequence <em>Update Sequence</em>}</li>
 *   <li>{@link net.opengis.wcs10.GetCapabilitiesType#getVersion <em>Version</em>}</li>
 *   <li>{@link net.opengis.wcs10.GetCapabilitiesType#getBaseUrl <em>Base Url</em>}</li>
 *   <li>{@link net.opengis.wcs10.GetCapabilitiesType#getExtendedProperties <em>Extended Properties</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs10.Wcs10Package#getGetCapabilitiesType()
 * @model extendedMetaData="name='GetCapabilities_._type' kind='elementOnly'"
 * @generated
 */
public interface GetCapabilitiesType extends EObject {
    /**
	 * Returns the value of the '<em><b>Section</b></em>' attribute.
	 * The default value is <code>"/"</code>.
	 * The literals are from the enumeration {@link net.opengis.wcs10.CapabilitiesSectionType}.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Section</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Section</em>' attribute.
	 * @see net.opengis.wcs10.CapabilitiesSectionType
	 * @see #isSetSection()
	 * @see #unsetSection()
	 * @see #setSection(CapabilitiesSectionType)
	 * @see net.opengis.wcs10.Wcs10Package#getGetCapabilitiesType_Section()
	 * @model default="/" unsettable="true"
	 *        extendedMetaData="kind='element' name='section' namespace='##targetNamespace'"
	 * @generated
	 */
    CapabilitiesSectionType getSection();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.GetCapabilitiesType#getSection <em>Section</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Section</em>' attribute.
	 * @see net.opengis.wcs10.CapabilitiesSectionType
	 * @see #isSetSection()
	 * @see #unsetSection()
	 * @see #getSection()
	 * @generated
	 */
    void setSection(CapabilitiesSectionType value);

    /**
	 * Unsets the value of the '{@link net.opengis.wcs10.GetCapabilitiesType#getSection <em>Section</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #isSetSection()
	 * @see #getSection()
	 * @see #setSection(CapabilitiesSectionType)
	 * @generated
	 */
    void unsetSection();

    /**
	 * Returns whether the value of the '{@link net.opengis.wcs10.GetCapabilitiesType#getSection <em>Section</em>}' attribute is set.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Section</em>' attribute is set.
	 * @see #unsetSection()
	 * @see #getSection()
	 * @see #setSection(CapabilitiesSectionType)
	 * @generated
	 */
    boolean isSetSection();

    /**
	 * Returns the value of the '<em><b>Service</b></em>' attribute.
	 * The default value is <code>"WCS"</code>.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Service</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Service</em>' attribute.
	 * @see #isSetService()
	 * @see #unsetService()
	 * @see #setService(String)
	 * @see net.opengis.wcs10.Wcs10Package#getGetCapabilitiesType_Service()
	 * @model default="WCS" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='attribute' name='service'"
	 * @generated
	 */
    String getService();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.GetCapabilitiesType#getService <em>Service</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Service</em>' attribute.
	 * @see #isSetService()
	 * @see #unsetService()
	 * @see #getService()
	 * @generated
	 */
    void setService(String value);

    /**
	 * Unsets the value of the '{@link net.opengis.wcs10.GetCapabilitiesType#getService <em>Service</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #isSetService()
	 * @see #getService()
	 * @see #setService(String)
	 * @generated
	 */
    void unsetService();

    /**
	 * Returns whether the value of the '{@link net.opengis.wcs10.GetCapabilitiesType#getService <em>Service</em>}' attribute is set.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Service</em>' attribute is set.
	 * @see #unsetService()
	 * @see #getService()
	 * @see #setService(String)
	 * @generated
	 */
    boolean isSetService();

    /**
	 * Returns the value of the '<em><b>Update Sequence</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Service metadata (Capabilities) document version, having values that are "increased" whenever any change is made in service metadata document. Values are selected by each server, and are always opaque to clients. When omitted or not supported by server, server shall return latest complete service metadata document.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Update Sequence</em>' attribute.
	 * @see #setUpdateSequence(String)
	 * @see net.opengis.wcs10.Wcs10Package#getGetCapabilitiesType_UpdateSequence()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='updateSequence'"
	 * @generated
	 */
    String getUpdateSequence();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.GetCapabilitiesType#getUpdateSequence <em>Update Sequence</em>}' attribute.
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
	 * @see net.opengis.wcs10.Wcs10Package#getGetCapabilitiesType_Version()
	 * @model default="1.0.0" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='version'"
	 * @generated
	 */
    String getVersion();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.GetCapabilitiesType#getVersion <em>Version</em>}' attribute.
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
	 * Unsets the value of the '{@link net.opengis.wcs10.GetCapabilitiesType#getVersion <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #isSetVersion()
	 * @see #getVersion()
	 * @see #setVersion(String)
	 * @generated
	 */
    void unsetVersion();

    /**
	 * Returns whether the value of the '{@link net.opengis.wcs10.GetCapabilitiesType#getVersion <em>Version</em>}' attribute is set.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Version</em>' attribute is set.
	 * @see #unsetVersion()
	 * @see #getVersion()
	 * @see #setVersion(String)
	 * @generated
	 */
    boolean isSetVersion();

    /**
     * An extended property of the model which allows client code to specify
     * a base url with this object.
     * 
     * @model
     */
    String getBaseUrl();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.GetCapabilitiesType#getBaseUrl <em>Base Url</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Url</em>' attribute.
	 * @see #getBaseUrl()
	 * @generated
	 */
	void setBaseUrl(String value);
    
    /**
     * Extended model properties.
     * <p>
     * This map allows client to store additional properties with the this
     * request object, properties that are not part of the model proper.
     * </p>
     * 
     * @model
     */
    Map getExtendedProperties();

				/**
	 * Sets the value of the '{@link net.opengis.wcs10.GetCapabilitiesType#getExtendedProperties <em>Extended Properties</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Extended Properties</em>' attribute.
	 * @see #getExtendedProperties()
	 * @generated
	 */
	void setExtendedProperties(Map value);
    
} // GetCapabilitiesType
