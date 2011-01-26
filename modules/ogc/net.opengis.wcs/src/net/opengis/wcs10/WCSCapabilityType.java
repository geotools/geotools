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
 * A representation of the model object '<em><b>WCS Capability Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * XML encoded WCS GetCapabilities operation response. The Capabilities document provides clients with service metadata about a specific service instance, including metadata about the coverages served.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs10.WCSCapabilityType#getRequest <em>Request</em>}</li>
 *   <li>{@link net.opengis.wcs10.WCSCapabilityType#getException <em>Exception</em>}</li>
 *   <li>{@link net.opengis.wcs10.WCSCapabilityType#getVendorSpecificCapabilities <em>Vendor Specific Capabilities</em>}</li>
 *   <li>{@link net.opengis.wcs10.WCSCapabilityType#getUpdateSequence <em>Update Sequence</em>}</li>
 *   <li>{@link net.opengis.wcs10.WCSCapabilityType#getVersion <em>Version</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs10.Wcs10Package#getWCSCapabilityType()
 * @model extendedMetaData="name='WCSCapabilityType' kind='elementOnly'"
 * @generated
 */
public interface WCSCapabilityType extends EObject {
    /**
	 * Returns the value of the '<em><b>Request</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Request</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Request</em>' containment reference.
	 * @see #setRequest(RequestType)
	 * @see net.opengis.wcs10.Wcs10Package#getWCSCapabilityType_Request()
	 * @model containment="true" required="true"
	 *        extendedMetaData="kind='element' name='Request' namespace='##targetNamespace'"
	 * @generated
	 */
    RequestType getRequest();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.WCSCapabilityType#getRequest <em>Request</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Request</em>' containment reference.
	 * @see #getRequest()
	 * @generated
	 */
    void setRequest(RequestType value);

    /**
	 * Returns the value of the '<em><b>Exception</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Exception</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Exception</em>' containment reference.
	 * @see #setException(ExceptionType)
	 * @see net.opengis.wcs10.Wcs10Package#getWCSCapabilityType_Exception()
	 * @model containment="true" required="true"
	 *        extendedMetaData="kind='element' name='Exception' namespace='##targetNamespace'"
	 * @generated
	 */
    ExceptionType getException();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.WCSCapabilityType#getException <em>Exception</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Exception</em>' containment reference.
	 * @see #getException()
	 * @generated
	 */
    void setException(ExceptionType value);

    /**
	 * Returns the value of the '<em><b>Vendor Specific Capabilities</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Vendor Specific Capabilities</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Vendor Specific Capabilities</em>' containment reference.
	 * @see #setVendorSpecificCapabilities(VendorSpecificCapabilitiesType)
	 * @see net.opengis.wcs10.Wcs10Package#getWCSCapabilityType_VendorSpecificCapabilities()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='VendorSpecificCapabilities' namespace='##targetNamespace'"
	 * @generated
	 */
    VendorSpecificCapabilitiesType getVendorSpecificCapabilities();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.WCSCapabilityType#getVendorSpecificCapabilities <em>Vendor Specific Capabilities</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vendor Specific Capabilities</em>' containment reference.
	 * @see #getVendorSpecificCapabilities()
	 * @generated
	 */
    void setVendorSpecificCapabilities(VendorSpecificCapabilitiesType value);

    /**
	 * Returns the value of the '<em><b>Update Sequence</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Service metadata document version, having values that are "increased" whenever any change is made in service metadata document. Values are selected by each server, and are always opaque to clients. When not supported by server, server shall not return this attribute.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Update Sequence</em>' attribute.
	 * @see #setUpdateSequence(String)
	 * @see net.opengis.wcs10.Wcs10Package#getWCSCapabilityType_UpdateSequence()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='updateSequence'"
	 * @generated
	 */
    String getUpdateSequence();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.WCSCapabilityType#getUpdateSequence <em>Update Sequence</em>}' attribute.
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
	 * @see net.opengis.wcs10.Wcs10Package#getWCSCapabilityType_Version()
	 * @model default="1.0.0" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='version'"
	 * @generated
	 */
    String getVersion();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.WCSCapabilityType#getVersion <em>Version</em>}' attribute.
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
	 * Unsets the value of the '{@link net.opengis.wcs10.WCSCapabilityType#getVersion <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #isSetVersion()
	 * @see #getVersion()
	 * @see #setVersion(String)
	 * @generated
	 */
    void unsetVersion();

    /**
	 * Returns whether the value of the '{@link net.opengis.wcs10.WCSCapabilityType#getVersion <em>Version</em>}' attribute is set.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Version</em>' attribute is set.
	 * @see #unsetVersion()
	 * @see #getVersion()
	 * @see #setVersion(String)
	 * @generated
	 */
    boolean isSetVersion();

} // WCSCapabilityType
