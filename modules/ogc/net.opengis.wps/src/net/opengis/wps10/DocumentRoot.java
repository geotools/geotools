/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10;

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
 *   <li>{@link net.opengis.wps10.DocumentRoot#getMixed <em>Mixed</em>}</li>
 *   <li>{@link net.opengis.wps10.DocumentRoot#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}</li>
 *   <li>{@link net.opengis.wps10.DocumentRoot#getXSISchemaLocation <em>XSI Schema Location</em>}</li>
 *   <li>{@link net.opengis.wps10.DocumentRoot#getCapabilities <em>Capabilities</em>}</li>
 *   <li>{@link net.opengis.wps10.DocumentRoot#getDescribeProcess <em>Describe Process</em>}</li>
 *   <li>{@link net.opengis.wps10.DocumentRoot#getExecute <em>Execute</em>}</li>
 *   <li>{@link net.opengis.wps10.DocumentRoot#getExecuteResponse <em>Execute Response</em>}</li>
 *   <li>{@link net.opengis.wps10.DocumentRoot#getGetCapabilities <em>Get Capabilities</em>}</li>
 *   <li>{@link net.opengis.wps10.DocumentRoot#getLanguages <em>Languages</em>}</li>
 *   <li>{@link net.opengis.wps10.DocumentRoot#getProcessDescriptions <em>Process Descriptions</em>}</li>
 *   <li>{@link net.opengis.wps10.DocumentRoot#getProcessOfferings <em>Process Offerings</em>}</li>
 *   <li>{@link net.opengis.wps10.DocumentRoot#getWSDL <em>WSDL</em>}</li>
 *   <li>{@link net.opengis.wps10.DocumentRoot#getProcessVersion <em>Process Version</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wps10.Wps10Package#getDocumentRoot()
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
     * @see net.opengis.wps10.Wps10Package#getDocumentRoot_Mixed()
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
     * @see net.opengis.wps10.Wps10Package#getDocumentRoot_XMLNSPrefixMap()
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
     * @see net.opengis.wps10.Wps10Package#getDocumentRoot_XSISchemaLocation()
     * @model mapType="org.eclipse.emf.ecore.EStringToStringMapEntry" keyType="java.lang.String" valueType="java.lang.String" transient="true"
     *        extendedMetaData="kind='attribute' name='xsi:schemaLocation'"
     * @generated
     */
    EMap getXSISchemaLocation();

    /**
     * Returns the value of the '<em><b>Capabilities</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * WPS GetCapabilities operation response. This document provides clients with service metadata about a specific service instance, including metadata about the processes that can be executed. Since the server does not implement the updateSequence and Sections parameters, the server shall always return the complete Capabilities document, without the updateSequence parameter.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Capabilities</em>' containment reference.
     * @see #setCapabilities(WPSCapabilitiesType)
     * @see net.opengis.wps10.Wps10Package#getDocumentRoot_Capabilities()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Capabilities' namespace='##targetNamespace'"
     * @generated
     */
    WPSCapabilitiesType getCapabilities();

    /**
     * Sets the value of the '{@link net.opengis.wps10.DocumentRoot#getCapabilities <em>Capabilities</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Capabilities</em>' containment reference.
     * @see #getCapabilities()
     * @generated
     */
    void setCapabilities(WPSCapabilitiesType value);

    /**
     * Returns the value of the '<em><b>Describe Process</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * WPS DescribeProcess operation request.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Describe Process</em>' containment reference.
     * @see #setDescribeProcess(DescribeProcessType)
     * @see net.opengis.wps10.Wps10Package#getDocumentRoot_DescribeProcess()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='DescribeProcess' namespace='##targetNamespace'"
     * @generated
     */
    DescribeProcessType getDescribeProcess();

    /**
     * Sets the value of the '{@link net.opengis.wps10.DocumentRoot#getDescribeProcess <em>Describe Process</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Describe Process</em>' containment reference.
     * @see #getDescribeProcess()
     * @generated
     */
    void setDescribeProcess(DescribeProcessType value);

    /**
     * Returns the value of the '<em><b>Execute</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * WPS Execute operation request, to execute one identified Process. If a process is to be run multiple times, each run shall be submitted as a separate Execute request.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Execute</em>' containment reference.
     * @see #setExecute(ExecuteType)
     * @see net.opengis.wps10.Wps10Package#getDocumentRoot_Execute()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Execute' namespace='##targetNamespace'"
     * @generated
     */
    ExecuteType getExecute();

    /**
     * Sets the value of the '{@link net.opengis.wps10.DocumentRoot#getExecute <em>Execute</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Execute</em>' containment reference.
     * @see #getExecute()
     * @generated
     */
    void setExecute(ExecuteType value);

    /**
     * Returns the value of the '<em><b>Execute Response</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * WPS Execute operation response. By default, this XML document is delivered to the client in response to an Execute request. If "status" is "false" in the Execute operation request, this document is normally returned when process execution has been completed.
     * 			If "status" in the Execute request is "true", this response shall be returned as soon as the Execute request has been accepted for processing. In this case, the same XML document is also made available as a web-accessible resource from the URL identified in the statusLocation, and the WPS server shall repopulate it once the process has completed. It may repopulate it on an ongoing basis while the process is executing.
     * 			However, the response to an Execute request will not include this element in the special case where the output is a single complex value result and the Execute request indicates that "store" is "false". Instead, the server shall return the complex result (e.g., GIF image or GML) directly, without encoding it in the ExecuteResponse. If processing fails in this special case, the normal ExecuteResponse shall be sent, with the error condition indicated. This option is provided to simplify the programming required for simple clients and for service chaining.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Execute Response</em>' containment reference.
     * @see #setExecuteResponse(ExecuteResponseType)
     * @see net.opengis.wps10.Wps10Package#getDocumentRoot_ExecuteResponse()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='ExecuteResponse' namespace='##targetNamespace'"
     * @generated
     */
    ExecuteResponseType getExecuteResponse();

    /**
     * Sets the value of the '{@link net.opengis.wps10.DocumentRoot#getExecuteResponse <em>Execute Response</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Execute Response</em>' containment reference.
     * @see #getExecuteResponse()
     * @generated
     */
    void setExecuteResponse(ExecuteResponseType value);

    /**
     * Returns the value of the '<em><b>Get Capabilities</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Get Capabilities</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Get Capabilities</em>' containment reference.
     * @see #setGetCapabilities(GetCapabilitiesType)
     * @see net.opengis.wps10.Wps10Package#getDocumentRoot_GetCapabilities()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='GetCapabilities' namespace='##targetNamespace'"
     * @generated
     */
    GetCapabilitiesType getGetCapabilities();

    /**
     * Sets the value of the '{@link net.opengis.wps10.DocumentRoot#getGetCapabilities <em>Get Capabilities</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Get Capabilities</em>' containment reference.
     * @see #getGetCapabilities()
     * @generated
     */
    void setGetCapabilities(GetCapabilitiesType value);

    /**
     * Returns the value of the '<em><b>Languages</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Listing of the default and other languages supported by this service.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Languages</em>' containment reference.
     * @see #setLanguages(LanguagesType1)
     * @see net.opengis.wps10.Wps10Package#getDocumentRoot_Languages()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Languages' namespace='##targetNamespace'"
     * @generated
     */
    LanguagesType1 getLanguages();

    /**
     * Sets the value of the '{@link net.opengis.wps10.DocumentRoot#getLanguages <em>Languages</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Languages</em>' containment reference.
     * @see #getLanguages()
     * @generated
     */
    void setLanguages(LanguagesType1 value);

    /**
     * Returns the value of the '<em><b>Process Descriptions</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * WPS DescribeProcess operation response.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Process Descriptions</em>' containment reference.
     * @see #setProcessDescriptions(ProcessDescriptionsType)
     * @see net.opengis.wps10.Wps10Package#getDocumentRoot_ProcessDescriptions()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='ProcessDescriptions' namespace='##targetNamespace'"
     * @generated
     */
    ProcessDescriptionsType getProcessDescriptions();

    /**
     * Sets the value of the '{@link net.opengis.wps10.DocumentRoot#getProcessDescriptions <em>Process Descriptions</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Process Descriptions</em>' containment reference.
     * @see #getProcessDescriptions()
     * @generated
     */
    void setProcessDescriptions(ProcessDescriptionsType value);

    /**
     * Returns the value of the '<em><b>Process Offerings</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * List of brief descriptions of the processes offered by this WPS server.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Process Offerings</em>' containment reference.
     * @see #setProcessOfferings(ProcessOfferingsType)
     * @see net.opengis.wps10.Wps10Package#getDocumentRoot_ProcessOfferings()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='ProcessOfferings' namespace='##targetNamespace'"
     * @generated
     */
    ProcessOfferingsType getProcessOfferings();

    /**
     * Sets the value of the '{@link net.opengis.wps10.DocumentRoot#getProcessOfferings <em>Process Offerings</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Process Offerings</em>' containment reference.
     * @see #getProcessOfferings()
     * @generated
     */
    void setProcessOfferings(ProcessOfferingsType value);

    /**
     * Returns the value of the '<em><b>WSDL</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Location of a WSDL document.
     * <!-- end-model-doc -->
     * @return the value of the '<em>WSDL</em>' containment reference.
     * @see #setWSDL(WSDLType)
     * @see net.opengis.wps10.Wps10Package#getDocumentRoot_WSDL()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='WSDL' namespace='##targetNamespace'"
     * @generated
     */
    WSDLType getWSDL();

    /**
     * Sets the value of the '{@link net.opengis.wps10.DocumentRoot#getWSDL <em>WSDL</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>WSDL</em>' containment reference.
     * @see #getWSDL()
     * @generated
     */
    void setWSDL(WSDLType value);

    /**
     * Returns the value of the '<em><b>Process Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Release version of this Process, included when a process version needs to be included for clarification about the process to be used. It is possible that a WPS supports a process with different versions due to reasons such as modifications of process algorithms.  Notice that this is the version identifier for the process, not the version of the WPS interface. The processVersion is informative only.  Version negotiation for processVersion is not available.  Requests to Execute a process do not include a processVersion identifier.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Process Version</em>' attribute.
     * @see #setProcessVersion(String)
     * @see net.opengis.wps10.Wps10Package#getDocumentRoot_ProcessVersion()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='processVersion' namespace='##targetNamespace'"
     * @generated
     */
    String getProcessVersion();

    /**
     * Sets the value of the '{@link net.opengis.wps10.DocumentRoot#getProcessVersion <em>Process Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Process Version</em>' attribute.
     * @see #getProcessVersion()
     * @generated
     */
    void setProcessVersion(String value);

} // DocumentRoot
