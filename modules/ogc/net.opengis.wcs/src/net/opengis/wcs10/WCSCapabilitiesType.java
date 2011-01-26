/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs10;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>WCS Capabilities Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Metadata for a WCS server, also known as Capabilities document. Reply from a WCS that performed the GetCapabilities operation.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs10.WCSCapabilitiesType#getService <em>Service</em>}</li>
 *   <li>{@link net.opengis.wcs10.WCSCapabilitiesType#getCapability <em>Capability</em>}</li>
 *   <li>{@link net.opengis.wcs10.WCSCapabilitiesType#getContentMetadata <em>Content Metadata</em>}</li>
 *   <li>{@link net.opengis.wcs10.WCSCapabilitiesType#getUpdateSequence <em>Update Sequence</em>}</li>
 *   <li>{@link net.opengis.wcs10.WCSCapabilitiesType#getVersion <em>Version</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs10.Wcs10Package#getWCSCapabilitiesType()
 * @model extendedMetaData="name='WCS_CapabilitiesType' kind='elementOnly'"
 * @generated
 */
public interface WCSCapabilitiesType extends EObject {
    /**
	 * Returns the value of the '<em><b>Service</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Service</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Service</em>' containment reference.
	 * @see #setService(ServiceType)
	 * @see net.opengis.wcs10.Wcs10Package#getWCSCapabilitiesType_Service()
	 * @model containment="true" required="true"
	 *        extendedMetaData="kind='element' name='Service' namespace='##targetNamespace'"
	 * @generated
	 */
    ServiceType getService();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.WCSCapabilitiesType#getService <em>Service</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Service</em>' containment reference.
	 * @see #getService()
	 * @generated
	 */
    void setService(ServiceType value);

    /**
	 * Returns the value of the '<em><b>Capability</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Capability</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Capability</em>' containment reference.
	 * @see #setCapability(WCSCapabilityType)
	 * @see net.opengis.wcs10.Wcs10Package#getWCSCapabilitiesType_Capability()
	 * @model containment="true" required="true"
	 *        extendedMetaData="kind='element' name='Capability' namespace='##targetNamespace'"
	 * @generated
	 */
    WCSCapabilityType getCapability();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.WCSCapabilitiesType#getCapability <em>Capability</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Capability</em>' containment reference.
	 * @see #getCapability()
	 * @generated
	 */
    void setCapability(WCSCapabilityType value);

    /**
	 * Returns the value of the '<em><b>Content Metadata</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Unordered list of brief descriptions of all coverages avaialble from this WCS, or a reference to another service from which this information is available.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Content Metadata</em>' containment reference.
	 * @see #setContentMetadata(ContentMetadataType)
	 * @see net.opengis.wcs10.Wcs10Package#getWCSCapabilitiesType_ContentMetadata()
	 * @model containment="true" required="true"
	 *        extendedMetaData="kind='element' name='ContentMetadata' namespace='##targetNamespace'"
	 * @generated
	 */
    ContentMetadataType getContentMetadata();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.WCSCapabilitiesType#getContentMetadata <em>Content Metadata</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Content Metadata</em>' containment reference.
	 * @see #getContentMetadata()
	 * @generated
	 */
    void setContentMetadata(ContentMetadataType value);

    /**
	 * Returns the value of the '<em><b>Update Sequence</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Service metadata (Capabilities) document version, having values that are "increased" whenever any change is made in service metadata document. Values are selected by each server, and are always opaque to clients. When supported by server, server shall return this attribute.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Update Sequence</em>' attribute.
	 * @see #setUpdateSequence(String)
	 * @see net.opengis.wcs10.Wcs10Package#getWCSCapabilitiesType_UpdateSequence()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='updateSequence'"
	 * @generated
	 */
    String getUpdateSequence();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.WCSCapabilitiesType#getUpdateSequence <em>Update Sequence</em>}' attribute.
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
	 * @see net.opengis.wcs10.Wcs10Package#getWCSCapabilitiesType_Version()
	 * @model default="1.0.0" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='attribute' name='version'"
	 * @generated
	 */
    String getVersion();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.WCSCapabilitiesType#getVersion <em>Version</em>}' attribute.
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
	 * Unsets the value of the '{@link net.opengis.wcs10.WCSCapabilitiesType#getVersion <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #isSetVersion()
	 * @see #getVersion()
	 * @see #setVersion(String)
	 * @generated
	 */
    void unsetVersion();

    /**
	 * Returns whether the value of the '{@link net.opengis.wcs10.WCSCapabilitiesType#getVersion <em>Version</em>}' attribute is set.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Version</em>' attribute is set.
	 * @see #unsetVersion()
	 * @see #getVersion()
	 * @see #setVersion(String)
	 * @generated
	 */
    boolean isSetVersion();

} // WCSCapabilitiesType
