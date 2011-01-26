/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows11;

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
 *   <li>{@link net.opengis.ows11.DocumentRoot#getMixed <em>Mixed</em>}</li>
 *   <li>{@link net.opengis.ows11.DocumentRoot#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}</li>
 *   <li>{@link net.opengis.ows11.DocumentRoot#getXSISchemaLocation <em>XSI Schema Location</em>}</li>
 *   <li>{@link net.opengis.ows11.DocumentRoot#getAbstract <em>Abstract</em>}</li>
 *   <li>{@link net.opengis.ows11.DocumentRoot#getAbstractMetaData <em>Abstract Meta Data</em>}</li>
 *   <li>{@link net.opengis.ows11.DocumentRoot#getAbstractReferenceBase <em>Abstract Reference Base</em>}</li>
 *   <li>{@link net.opengis.ows11.DocumentRoot#getAccessConstraints <em>Access Constraints</em>}</li>
 *   <li>{@link net.opengis.ows11.DocumentRoot#getAllowedValues <em>Allowed Values</em>}</li>
 *   <li>{@link net.opengis.ows11.DocumentRoot#getAnyValue <em>Any Value</em>}</li>
 *   <li>{@link net.opengis.ows11.DocumentRoot#getAvailableCRS <em>Available CRS</em>}</li>
 *   <li>{@link net.opengis.ows11.DocumentRoot#getBoundingBox <em>Bounding Box</em>}</li>
 *   <li>{@link net.opengis.ows11.DocumentRoot#getContactInfo <em>Contact Info</em>}</li>
 *   <li>{@link net.opengis.ows11.DocumentRoot#getDatasetDescriptionSummary <em>Dataset Description Summary</em>}</li>
 *   <li>{@link net.opengis.ows11.DocumentRoot#getDataType <em>Data Type</em>}</li>
 *   <li>{@link net.opengis.ows11.DocumentRoot#getDCP <em>DCP</em>}</li>
 *   <li>{@link net.opengis.ows11.DocumentRoot#getDefaultValue <em>Default Value</em>}</li>
 *   <li>{@link net.opengis.ows11.DocumentRoot#getException <em>Exception</em>}</li>
 *   <li>{@link net.opengis.ows11.DocumentRoot#getExceptionReport <em>Exception Report</em>}</li>
 *   <li>{@link net.opengis.ows11.DocumentRoot#getExtendedCapabilities <em>Extended Capabilities</em>}</li>
 *   <li>{@link net.opengis.ows11.DocumentRoot#getFees <em>Fees</em>}</li>
 *   <li>{@link net.opengis.ows11.DocumentRoot#getGetCapabilities <em>Get Capabilities</em>}</li>
 *   <li>{@link net.opengis.ows11.DocumentRoot#getGetResourceByID <em>Get Resource By ID</em>}</li>
 *   <li>{@link net.opengis.ows11.DocumentRoot#getHTTP <em>HTTP</em>}</li>
 *   <li>{@link net.opengis.ows11.DocumentRoot#getIdentifier <em>Identifier</em>}</li>
 *   <li>{@link net.opengis.ows11.DocumentRoot#getIndividualName <em>Individual Name</em>}</li>
 *   <li>{@link net.opengis.ows11.DocumentRoot#getInputData <em>Input Data</em>}</li>
 *   <li>{@link net.opengis.ows11.DocumentRoot#getKeywords <em>Keywords</em>}</li>
 *   <li>{@link net.opengis.ows11.DocumentRoot#getLanguage <em>Language</em>}</li>
 *   <li>{@link net.opengis.ows11.DocumentRoot#getManifest <em>Manifest</em>}</li>
 *   <li>{@link net.opengis.ows11.DocumentRoot#getMaximumValue <em>Maximum Value</em>}</li>
 *   <li>{@link net.opengis.ows11.DocumentRoot#getMeaning <em>Meaning</em>}</li>
 *   <li>{@link net.opengis.ows11.DocumentRoot#getMetadata <em>Metadata</em>}</li>
 *   <li>{@link net.opengis.ows11.DocumentRoot#getMinimumValue <em>Minimum Value</em>}</li>
 *   <li>{@link net.opengis.ows11.DocumentRoot#getNoValues <em>No Values</em>}</li>
 *   <li>{@link net.opengis.ows11.DocumentRoot#getOperation <em>Operation</em>}</li>
 *   <li>{@link net.opengis.ows11.DocumentRoot#getOperationResponse <em>Operation Response</em>}</li>
 *   <li>{@link net.opengis.ows11.DocumentRoot#getOperationsMetadata <em>Operations Metadata</em>}</li>
 *   <li>{@link net.opengis.ows11.DocumentRoot#getOrganisationName <em>Organisation Name</em>}</li>
 *   <li>{@link net.opengis.ows11.DocumentRoot#getOtherSource <em>Other Source</em>}</li>
 *   <li>{@link net.opengis.ows11.DocumentRoot#getOutputFormat <em>Output Format</em>}</li>
 *   <li>{@link net.opengis.ows11.DocumentRoot#getPointOfContact <em>Point Of Contact</em>}</li>
 *   <li>{@link net.opengis.ows11.DocumentRoot#getPositionName <em>Position Name</em>}</li>
 *   <li>{@link net.opengis.ows11.DocumentRoot#getRange <em>Range</em>}</li>
 *   <li>{@link net.opengis.ows11.DocumentRoot#getReference <em>Reference</em>}</li>
 *   <li>{@link net.opengis.ows11.DocumentRoot#getReferenceGroup <em>Reference Group</em>}</li>
 *   <li>{@link net.opengis.ows11.DocumentRoot#getReferenceSystem <em>Reference System</em>}</li>
 *   <li>{@link net.opengis.ows11.DocumentRoot#getResource <em>Resource</em>}</li>
 *   <li>{@link net.opengis.ows11.DocumentRoot#getRole <em>Role</em>}</li>
 *   <li>{@link net.opengis.ows11.DocumentRoot#getServiceIdentification <em>Service Identification</em>}</li>
 *   <li>{@link net.opengis.ows11.DocumentRoot#getServiceProvider <em>Service Provider</em>}</li>
 *   <li>{@link net.opengis.ows11.DocumentRoot#getServiceReference <em>Service Reference</em>}</li>
 *   <li>{@link net.opengis.ows11.DocumentRoot#getSpacing <em>Spacing</em>}</li>
 *   <li>{@link net.opengis.ows11.DocumentRoot#getSupportedCRS <em>Supported CRS</em>}</li>
 *   <li>{@link net.opengis.ows11.DocumentRoot#getTitle <em>Title</em>}</li>
 *   <li>{@link net.opengis.ows11.DocumentRoot#getUOM <em>UOM</em>}</li>
 *   <li>{@link net.opengis.ows11.DocumentRoot#getValue <em>Value</em>}</li>
 *   <li>{@link net.opengis.ows11.DocumentRoot#getValuesReference <em>Values Reference</em>}</li>
 *   <li>{@link net.opengis.ows11.DocumentRoot#getWGS84BoundingBox <em>WGS84 Bounding Box</em>}</li>
 *   <li>{@link net.opengis.ows11.DocumentRoot#getRangeClosure <em>Range Closure</em>}</li>
 *   <li>{@link net.opengis.ows11.DocumentRoot#getReference1 <em>Reference1</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.ows11.Ows11Package#getDocumentRoot()
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
     * @see net.opengis.ows11.Ows11Package#getDocumentRoot_Mixed()
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
     * @see net.opengis.ows11.Ows11Package#getDocumentRoot_XMLNSPrefixMap()
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
     * @see net.opengis.ows11.Ows11Package#getDocumentRoot_XSISchemaLocation()
     * @model mapType="org.eclipse.emf.ecore.EStringToStringMapEntry" keyType="java.lang.String" valueType="java.lang.String" transient="true"
     *        extendedMetaData="kind='attribute' name='xsi:schemaLocation'"
     * @generated
     */
    EMap getXSISchemaLocation();

    /**
     * Returns the value of the '<em><b>Abstract</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Brief narrative description of this resource, normally used for display to a human. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Abstract</em>' containment reference.
     * @see #setAbstract(LanguageStringType)
     * @see net.opengis.ows11.Ows11Package#getDocumentRoot_Abstract()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Abstract' namespace='##targetNamespace'"
     * @generated
     */
    LanguageStringType getAbstract();

    /**
     * Sets the value of the '{@link net.opengis.ows11.DocumentRoot#getAbstract <em>Abstract</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Abstract</em>' containment reference.
     * @see #getAbstract()
     * @generated
     */
    void setAbstract(LanguageStringType value);

    /**
     * Returns the value of the '<em><b>Abstract Meta Data</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Abstract element containing more metadata about the element that includes the containing "metadata" element. A specific server implementation, or an Implementation Specification, can define concrete elements in the AbstractMetaData substitution group. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Abstract Meta Data</em>' containment reference.
     * @see net.opengis.ows11.Ows11Package#getDocumentRoot_AbstractMetaData()
     * @model containment="true" upper="-2" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='AbstractMetaData' namespace='##targetNamespace'"
     * @generated
     */
    EObject getAbstractMetaData();

    /**
     * Returns the value of the '<em><b>Abstract Reference Base</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Abstract Reference Base</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Abstract Reference Base</em>' containment reference.
     * @see net.opengis.ows11.Ows11Package#getDocumentRoot_AbstractReferenceBase()
     * @model containment="true" upper="-2" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='AbstractReferenceBase' namespace='##targetNamespace'"
     * @generated
     */
    AbstractReferenceBaseType getAbstractReferenceBase();

    /**
     * Returns the value of the '<em><b>Access Constraints</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Access constraint applied to assure the protection of privacy or intellectual property, or any other restrictions on retrieving or using data from or otherwise using this server. The reserved value NONE (case insensitive) shall be used to mean no access constraints are imposed. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Access Constraints</em>' attribute.
     * @see #setAccessConstraints(String)
     * @see net.opengis.ows11.Ows11Package#getDocumentRoot_AccessConstraints()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='AccessConstraints' namespace='##targetNamespace'"
     * @generated
     */
    String getAccessConstraints();

    /**
     * Sets the value of the '{@link net.opengis.ows11.DocumentRoot#getAccessConstraints <em>Access Constraints</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Access Constraints</em>' attribute.
     * @see #getAccessConstraints()
     * @generated
     */
    void setAccessConstraints(String value);

    /**
     * Returns the value of the '<em><b>Allowed Values</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * List of all the valid values and/or ranges of values for this quantity. For numeric quantities, signed values should be ordered from negative infinity to positive infinity. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Allowed Values</em>' containment reference.
     * @see #setAllowedValues(AllowedValuesType)
     * @see net.opengis.ows11.Ows11Package#getDocumentRoot_AllowedValues()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='AllowedValues' namespace='##targetNamespace'"
     * @generated
     */
    AllowedValuesType getAllowedValues();

    /**
     * Sets the value of the '{@link net.opengis.ows11.DocumentRoot#getAllowedValues <em>Allowed Values</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Allowed Values</em>' containment reference.
     * @see #getAllowedValues()
     * @generated
     */
    void setAllowedValues(AllowedValuesType value);

    /**
     * Returns the value of the '<em><b>Any Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Specifies that any value is allowed for this parameter.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Any Value</em>' containment reference.
     * @see #setAnyValue(AnyValueType)
     * @see net.opengis.ows11.Ows11Package#getDocumentRoot_AnyValue()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='AnyValue' namespace='##targetNamespace'"
     * @generated
     */
    AnyValueType getAnyValue();

    /**
     * Sets the value of the '{@link net.opengis.ows11.DocumentRoot#getAnyValue <em>Any Value</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Any Value</em>' containment reference.
     * @see #getAnyValue()
     * @generated
     */
    void setAnyValue(AnyValueType value);

    /**
     * Returns the value of the '<em><b>Available CRS</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Available CRS</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Available CRS</em>' attribute.
     * @see #setAvailableCRS(String)
     * @see net.opengis.ows11.Ows11Package#getDocumentRoot_AvailableCRS()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.AnyURI" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='AvailableCRS' namespace='##targetNamespace'"
     * @generated
     */
    String getAvailableCRS();

    /**
     * Sets the value of the '{@link net.opengis.ows11.DocumentRoot#getAvailableCRS <em>Available CRS</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Available CRS</em>' attribute.
     * @see #getAvailableCRS()
     * @generated
     */
    void setAvailableCRS(String value);

    /**
     * Returns the value of the '<em><b>Bounding Box</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Bounding Box</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Bounding Box</em>' containment reference.
     * @see #setBoundingBox(BoundingBoxType)
     * @see net.opengis.ows11.Ows11Package#getDocumentRoot_BoundingBox()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='BoundingBox' namespace='##targetNamespace'"
     * @generated
     */
    BoundingBoxType getBoundingBox();

    /**
     * Sets the value of the '{@link net.opengis.ows11.DocumentRoot#getBoundingBox <em>Bounding Box</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Bounding Box</em>' containment reference.
     * @see #getBoundingBox()
     * @generated
     */
    void setBoundingBox(BoundingBoxType value);

    /**
     * Returns the value of the '<em><b>Contact Info</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Address of the responsible party. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Contact Info</em>' containment reference.
     * @see #setContactInfo(ContactType)
     * @see net.opengis.ows11.Ows11Package#getDocumentRoot_ContactInfo()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='ContactInfo' namespace='##targetNamespace'"
     * @generated
     */
    ContactType getContactInfo();

    /**
     * Sets the value of the '{@link net.opengis.ows11.DocumentRoot#getContactInfo <em>Contact Info</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Contact Info</em>' containment reference.
     * @see #getContactInfo()
     * @generated
     */
    void setContactInfo(ContactType value);

    /**
     * Returns the value of the '<em><b>Dataset Description Summary</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Dataset Description Summary</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Dataset Description Summary</em>' containment reference.
     * @see #setDatasetDescriptionSummary(DatasetDescriptionSummaryBaseType)
     * @see net.opengis.ows11.Ows11Package#getDocumentRoot_DatasetDescriptionSummary()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='DatasetDescriptionSummary' namespace='##targetNamespace'"
     * @generated
     */
    DatasetDescriptionSummaryBaseType getDatasetDescriptionSummary();

    /**
     * Sets the value of the '{@link net.opengis.ows11.DocumentRoot#getDatasetDescriptionSummary <em>Dataset Description Summary</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Dataset Description Summary</em>' containment reference.
     * @see #getDatasetDescriptionSummary()
     * @generated
     */
    void setDatasetDescriptionSummary(DatasetDescriptionSummaryBaseType value);

    /**
     * Returns the value of the '<em><b>Data Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Definition of the data type of this set of values. In this case, the xlink:href attribute can reference a URN for a well-known data type. For example, such a URN could be a data type identification URN defined in the "ogc" URN namespace. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Data Type</em>' containment reference.
     * @see #setDataType(DomainMetadataType)
     * @see net.opengis.ows11.Ows11Package#getDocumentRoot_DataType()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='DataType' namespace='##targetNamespace'"
     * @generated
     */
    DomainMetadataType getDataType();

    /**
     * Sets the value of the '{@link net.opengis.ows11.DocumentRoot#getDataType <em>Data Type</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Data Type</em>' containment reference.
     * @see #getDataType()
     * @generated
     */
    void setDataType(DomainMetadataType value);

    /**
     * Returns the value of the '<em><b>DCP</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Information for one distributed Computing Platform (DCP) supported for this operation. At present, only the HTTP DCP is defined, so this element only includes the HTTP element.
     * 
     * <!-- end-model-doc -->
     * @return the value of the '<em>DCP</em>' containment reference.
     * @see #setDCP(DCPType)
     * @see net.opengis.ows11.Ows11Package#getDocumentRoot_DCP()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='DCP' namespace='##targetNamespace'"
     * @generated
     */
    DCPType getDCP();

    /**
     * Sets the value of the '{@link net.opengis.ows11.DocumentRoot#getDCP <em>DCP</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>DCP</em>' containment reference.
     * @see #getDCP()
     * @generated
     */
    void setDCP(DCPType value);

    /**
     * Returns the value of the '<em><b>Default Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The default value for a quantity for which multiple values are allowed. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Default Value</em>' containment reference.
     * @see #setDefaultValue(ValueType)
     * @see net.opengis.ows11.Ows11Package#getDocumentRoot_DefaultValue()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='DefaultValue' namespace='##targetNamespace'"
     * @generated
     */
    ValueType getDefaultValue();

    /**
     * Sets the value of the '{@link net.opengis.ows11.DocumentRoot#getDefaultValue <em>Default Value</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Default Value</em>' containment reference.
     * @see #getDefaultValue()
     * @generated
     */
    void setDefaultValue(ValueType value);

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
     * @see net.opengis.ows11.Ows11Package#getDocumentRoot_Exception()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Exception' namespace='##targetNamespace'"
     * @generated
     */
    ExceptionType getException();

    /**
     * Sets the value of the '{@link net.opengis.ows11.DocumentRoot#getException <em>Exception</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Exception</em>' containment reference.
     * @see #getException()
     * @generated
     */
    void setException(ExceptionType value);

    /**
     * Returns the value of the '<em><b>Exception Report</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Report message returned to the client that requested any OWS operation when the server detects an error while processing that operation request. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Exception Report</em>' containment reference.
     * @see #setExceptionReport(ExceptionReportType)
     * @see net.opengis.ows11.Ows11Package#getDocumentRoot_ExceptionReport()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='ExceptionReport' namespace='##targetNamespace'"
     * @generated
     */
    ExceptionReportType getExceptionReport();

    /**
     * Sets the value of the '{@link net.opengis.ows11.DocumentRoot#getExceptionReport <em>Exception Report</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Exception Report</em>' containment reference.
     * @see #getExceptionReport()
     * @generated
     */
    void setExceptionReport(ExceptionReportType value);

    /**
     * Returns the value of the '<em><b>Extended Capabilities</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Individual software vendors and servers can use this element to provide metadata about any additional server abilities. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Extended Capabilities</em>' containment reference.
     * @see #setExtendedCapabilities(EObject)
     * @see net.opengis.ows11.Ows11Package#getDocumentRoot_ExtendedCapabilities()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='ExtendedCapabilities' namespace='##targetNamespace'"
     * @generated
     */
    EObject getExtendedCapabilities();

    /**
     * Sets the value of the '{@link net.opengis.ows11.DocumentRoot#getExtendedCapabilities <em>Extended Capabilities</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Extended Capabilities</em>' containment reference.
     * @see #getExtendedCapabilities()
     * @generated
     */
    void setExtendedCapabilities(EObject value);

    /**
     * Returns the value of the '<em><b>Fees</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Fees and terms for retrieving data from or otherwise using this server, including the monetary units as specified in ISO 4217. The reserved value NONE (case insensitive) shall be used to mean no fees or terms. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Fees</em>' attribute.
     * @see #setFees(String)
     * @see net.opengis.ows11.Ows11Package#getDocumentRoot_Fees()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Fees' namespace='##targetNamespace'"
     * @generated
     */
    String getFees();

    /**
     * Sets the value of the '{@link net.opengis.ows11.DocumentRoot#getFees <em>Fees</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Fees</em>' attribute.
     * @see #getFees()
     * @generated
     */
    void setFees(String value);

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
     * @see net.opengis.ows11.Ows11Package#getDocumentRoot_GetCapabilities()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='GetCapabilities' namespace='##targetNamespace'"
     * @generated
     */
    GetCapabilitiesType getGetCapabilities();

    /**
     * Sets the value of the '{@link net.opengis.ows11.DocumentRoot#getGetCapabilities <em>Get Capabilities</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Get Capabilities</em>' containment reference.
     * @see #getGetCapabilities()
     * @generated
     */
    void setGetCapabilities(GetCapabilitiesType value);

    /**
     * Returns the value of the '<em><b>Get Resource By ID</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Get Resource By ID</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Get Resource By ID</em>' containment reference.
     * @see #setGetResourceByID(GetResourceByIdType)
     * @see net.opengis.ows11.Ows11Package#getDocumentRoot_GetResourceByID()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='GetResourceByID' namespace='##targetNamespace'"
     * @generated
     */
    GetResourceByIdType getGetResourceByID();

    /**
     * Sets the value of the '{@link net.opengis.ows11.DocumentRoot#getGetResourceByID <em>Get Resource By ID</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Get Resource By ID</em>' containment reference.
     * @see #getGetResourceByID()
     * @generated
     */
    void setGetResourceByID(GetResourceByIdType value);

    /**
     * Returns the value of the '<em><b>HTTP</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Connect point URLs for the HTTP Distributed Computing Platform (DCP). Normally, only one Get and/or one Post is included in this element. More than one Get and/or Post is allowed to support including alternative URLs for uses such as load balancing or backup. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>HTTP</em>' containment reference.
     * @see #setHTTP(HTTPType)
     * @see net.opengis.ows11.Ows11Package#getDocumentRoot_HTTP()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='HTTP' namespace='##targetNamespace'"
     * @generated
     */
    HTTPType getHTTP();

    /**
     * Sets the value of the '{@link net.opengis.ows11.DocumentRoot#getHTTP <em>HTTP</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>HTTP</em>' containment reference.
     * @see #getHTTP()
     * @generated
     */
    void setHTTP(HTTPType value);

    /**
     * Returns the value of the '<em><b>Identifier</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Unique identifier or name of this dataset. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Identifier</em>' containment reference.
     * @see #setIdentifier(CodeType)
     * @see net.opengis.ows11.Ows11Package#getDocumentRoot_Identifier()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Identifier' namespace='##targetNamespace'"
     * @generated
     */
    CodeType getIdentifier();

    /**
     * Sets the value of the '{@link net.opengis.ows11.DocumentRoot#getIdentifier <em>Identifier</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Identifier</em>' containment reference.
     * @see #getIdentifier()
     * @generated
     */
    void setIdentifier(CodeType value);

    /**
     * Returns the value of the '<em><b>Individual Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Name of the responsible person: surname, given name, title separated by a delimiter. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Individual Name</em>' attribute.
     * @see #setIndividualName(String)
     * @see net.opengis.ows11.Ows11Package#getDocumentRoot_IndividualName()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='IndividualName' namespace='##targetNamespace'"
     * @generated
     */
    String getIndividualName();

    /**
     * Sets the value of the '{@link net.opengis.ows11.DocumentRoot#getIndividualName <em>Individual Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Individual Name</em>' attribute.
     * @see #getIndividualName()
     * @generated
     */
    void setIndividualName(String value);

    /**
     * Returns the value of the '<em><b>Input Data</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Input data in a XML-encoded OWS operation request, allowing including multiple data items with each data item either included or referenced. This InputData element, or an element using the ManifestType with a more-specific element name (TBR), shall be used whenever applicable within XML-encoded OWS operation requests. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Input Data</em>' containment reference.
     * @see #setInputData(ManifestType)
     * @see net.opengis.ows11.Ows11Package#getDocumentRoot_InputData()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='InputData' namespace='##targetNamespace'"
     * @generated
     */
    ManifestType getInputData();

    /**
     * Sets the value of the '{@link net.opengis.ows11.DocumentRoot#getInputData <em>Input Data</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Input Data</em>' containment reference.
     * @see #getInputData()
     * @generated
     */
    void setInputData(ManifestType value);

    /**
     * Returns the value of the '<em><b>Keywords</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Keywords</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Keywords</em>' containment reference.
     * @see #setKeywords(KeywordsType)
     * @see net.opengis.ows11.Ows11Package#getDocumentRoot_Keywords()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Keywords' namespace='##targetNamespace'"
     * @generated
     */
    KeywordsType getKeywords();

    /**
     * Sets the value of the '{@link net.opengis.ows11.DocumentRoot#getKeywords <em>Keywords</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Keywords</em>' containment reference.
     * @see #getKeywords()
     * @generated
     */
    void setKeywords(KeywordsType value);

    /**
     * Returns the value of the '<em><b>Language</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Identifier of a language used by the data(set) contents. This language identifier shall be as specified in IETF RFC 4646. When this element is omitted, the language used is not identified. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Language</em>' attribute.
     * @see #setLanguage(String)
     * @see net.opengis.ows11.Ows11Package#getDocumentRoot_Language()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.Language" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Language' namespace='##targetNamespace'"
     * @generated
     */
    String getLanguage();

    /**
     * Sets the value of the '{@link net.opengis.ows11.DocumentRoot#getLanguage <em>Language</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Language</em>' attribute.
     * @see #getLanguage()
     * @generated
     */
    void setLanguage(String value);

    /**
     * Returns the value of the '<em><b>Manifest</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Manifest</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Manifest</em>' containment reference.
     * @see #setManifest(ManifestType)
     * @see net.opengis.ows11.Ows11Package#getDocumentRoot_Manifest()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Manifest' namespace='##targetNamespace'"
     * @generated
     */
    ManifestType getManifest();

    /**
     * Sets the value of the '{@link net.opengis.ows11.DocumentRoot#getManifest <em>Manifest</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Manifest</em>' containment reference.
     * @see #getManifest()
     * @generated
     */
    void setManifest(ManifestType value);

    /**
     * Returns the value of the '<em><b>Maximum Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Maximum value of this numeric parameter. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Maximum Value</em>' containment reference.
     * @see #setMaximumValue(ValueType)
     * @see net.opengis.ows11.Ows11Package#getDocumentRoot_MaximumValue()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='MaximumValue' namespace='##targetNamespace'"
     * @generated
     */
    ValueType getMaximumValue();

    /**
     * Sets the value of the '{@link net.opengis.ows11.DocumentRoot#getMaximumValue <em>Maximum Value</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Maximum Value</em>' containment reference.
     * @see #getMaximumValue()
     * @generated
     */
    void setMaximumValue(ValueType value);

    /**
     * Returns the value of the '<em><b>Meaning</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Definition of the meaning or semantics of this set of values. This Meaning can provide more specific, complete, precise, machine accessible, and machine understandable semantics about this quantity, relative to other available semantic information. For example, other semantic information is often provided in "documentation" elements in XML Schemas or "description" elements in GML objects. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Meaning</em>' containment reference.
     * @see #setMeaning(DomainMetadataType)
     * @see net.opengis.ows11.Ows11Package#getDocumentRoot_Meaning()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Meaning' namespace='##targetNamespace'"
     * @generated
     */
    DomainMetadataType getMeaning();

    /**
     * Sets the value of the '{@link net.opengis.ows11.DocumentRoot#getMeaning <em>Meaning</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Meaning</em>' containment reference.
     * @see #getMeaning()
     * @generated
     */
    void setMeaning(DomainMetadataType value);

    /**
     * Returns the value of the '<em><b>Metadata</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Metadata</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Metadata</em>' containment reference.
     * @see #setMetadata(MetadataType)
     * @see net.opengis.ows11.Ows11Package#getDocumentRoot_Metadata()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Metadata' namespace='##targetNamespace'"
     * @generated
     */
    MetadataType getMetadata();

    /**
     * Sets the value of the '{@link net.opengis.ows11.DocumentRoot#getMetadata <em>Metadata</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Metadata</em>' containment reference.
     * @see #getMetadata()
     * @generated
     */
    void setMetadata(MetadataType value);

    /**
     * Returns the value of the '<em><b>Minimum Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Minimum value of this numeric parameter. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Minimum Value</em>' containment reference.
     * @see #setMinimumValue(ValueType)
     * @see net.opengis.ows11.Ows11Package#getDocumentRoot_MinimumValue()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='MinimumValue' namespace='##targetNamespace'"
     * @generated
     */
    ValueType getMinimumValue();

    /**
     * Sets the value of the '{@link net.opengis.ows11.DocumentRoot#getMinimumValue <em>Minimum Value</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Minimum Value</em>' containment reference.
     * @see #getMinimumValue()
     * @generated
     */
    void setMinimumValue(ValueType value);

    /**
     * Returns the value of the '<em><b>No Values</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Specifies that no values are allowed for this parameter or quantity.
     * <!-- end-model-doc -->
     * @return the value of the '<em>No Values</em>' containment reference.
     * @see #setNoValues(NoValuesType)
     * @see net.opengis.ows11.Ows11Package#getDocumentRoot_NoValues()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='NoValues' namespace='##targetNamespace'"
     * @generated
     */
    NoValuesType getNoValues();

    /**
     * Sets the value of the '{@link net.opengis.ows11.DocumentRoot#getNoValues <em>No Values</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>No Values</em>' containment reference.
     * @see #getNoValues()
     * @generated
     */
    void setNoValues(NoValuesType value);

    /**
     * Returns the value of the '<em><b>Operation</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Metadata for one operation that this server implements. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Operation</em>' containment reference.
     * @see #setOperation(OperationType)
     * @see net.opengis.ows11.Ows11Package#getDocumentRoot_Operation()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Operation' namespace='##targetNamespace'"
     * @generated
     */
    OperationType getOperation();

    /**
     * Sets the value of the '{@link net.opengis.ows11.DocumentRoot#getOperation <em>Operation</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Operation</em>' containment reference.
     * @see #getOperation()
     * @generated
     */
    void setOperation(OperationType value);

    /**
     * Returns the value of the '<em><b>Operation Response</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Response from an OWS operation, allowing including multiple output data items with each item either included or referenced. This OperationResponse element, or an element using the ManifestType with a more specific element name, shall be used whenever applicable for responses from OWS operations. 
     * This element is specified for use where the ManifestType contents are needed for an operation response, but the Manifest element name is not fully applicable. This element or the ManifestType shall be used instead of using the ows:ReferenceType proposed in OGC 04-105. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Operation Response</em>' containment reference.
     * @see #setOperationResponse(ManifestType)
     * @see net.opengis.ows11.Ows11Package#getDocumentRoot_OperationResponse()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='OperationResponse' namespace='##targetNamespace'"
     * @generated
     */
    ManifestType getOperationResponse();

    /**
     * Sets the value of the '{@link net.opengis.ows11.DocumentRoot#getOperationResponse <em>Operation Response</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Operation Response</em>' containment reference.
     * @see #getOperationResponse()
     * @generated
     */
    void setOperationResponse(ManifestType value);

    /**
     * Returns the value of the '<em><b>Operations Metadata</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Metadata about the operations and related abilities specified by this service and implemented by this server, including the URLs for operation requests. The basic contents of this section shall be the same for all OWS types, but individual services can add elements and/or change the optionality of optional elements. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Operations Metadata</em>' containment reference.
     * @see #setOperationsMetadata(OperationsMetadataType)
     * @see net.opengis.ows11.Ows11Package#getDocumentRoot_OperationsMetadata()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='OperationsMetadata' namespace='##targetNamespace'"
     * @generated
     */
    OperationsMetadataType getOperationsMetadata();

    /**
     * Sets the value of the '{@link net.opengis.ows11.DocumentRoot#getOperationsMetadata <em>Operations Metadata</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Operations Metadata</em>' containment reference.
     * @see #getOperationsMetadata()
     * @generated
     */
    void setOperationsMetadata(OperationsMetadataType value);

    /**
     * Returns the value of the '<em><b>Organisation Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Name of the responsible organization. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Organisation Name</em>' attribute.
     * @see #setOrganisationName(String)
     * @see net.opengis.ows11.Ows11Package#getDocumentRoot_OrganisationName()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='OrganisationName' namespace='##targetNamespace'"
     * @generated
     */
    String getOrganisationName();

    /**
     * Sets the value of the '{@link net.opengis.ows11.DocumentRoot#getOrganisationName <em>Organisation Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Organisation Name</em>' attribute.
     * @see #getOrganisationName()
     * @generated
     */
    void setOrganisationName(String value);

    /**
     * Returns the value of the '<em><b>Other Source</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Reference to a source of metadata describing  coverage offerings available from this server. This  parameter can reference a catalogue server from which dataset metadata is available. This ability is expected to be used by servers with thousands or millions of datasets, for which searching a catalogue is more feasible than fetching a long Capabilities XML document. When no DatasetDescriptionSummaries are included, and one or more catalogue servers are referenced, this set of catalogues shall contain current metadata summaries for all the datasets currently available from this OWS server, with the metadata for each such dataset referencing this OWS server. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Other Source</em>' containment reference.
     * @see #setOtherSource(MetadataType)
     * @see net.opengis.ows11.Ows11Package#getDocumentRoot_OtherSource()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='OtherSource' namespace='##targetNamespace'"
     * @generated
     */
    MetadataType getOtherSource();

    /**
     * Sets the value of the '{@link net.opengis.ows11.DocumentRoot#getOtherSource <em>Other Source</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Other Source</em>' containment reference.
     * @see #getOtherSource()
     * @generated
     */
    void setOtherSource(MetadataType value);

    /**
     * Returns the value of the '<em><b>Output Format</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Reference to a format in which this data can be encoded and transferred. More specific parameter names should be used by specific OWS specifications wherever applicable. More than one such parameter can be included for different purposes. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Output Format</em>' attribute.
     * @see #setOutputFormat(String)
     * @see net.opengis.ows11.Ows11Package#getDocumentRoot_OutputFormat()
     * @model unique="false" dataType="net.opengis.ows11.MimeType" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='OutputFormat' namespace='##targetNamespace'"
     * @generated
     */
    String getOutputFormat();

    /**
     * Sets the value of the '{@link net.opengis.ows11.DocumentRoot#getOutputFormat <em>Output Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Output Format</em>' attribute.
     * @see #getOutputFormat()
     * @generated
     */
    void setOutputFormat(String value);

    /**
     * Returns the value of the '<em><b>Point Of Contact</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Identification of, and means of communication with, person(s) responsible for the resource(s). 
     * For OWS use in the ServiceProvider section of a service metadata document, the optional organizationName element was removed, since this type is always used with the ProviderName element which provides that information. The optional individualName element was made mandatory, since either the organizationName or individualName element is mandatory. The mandatory "role" element was changed to optional, since no clear use of this information is known in the ServiceProvider section. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Point Of Contact</em>' containment reference.
     * @see #setPointOfContact(ResponsiblePartyType)
     * @see net.opengis.ows11.Ows11Package#getDocumentRoot_PointOfContact()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='PointOfContact' namespace='##targetNamespace'"
     * @generated
     */
    ResponsiblePartyType getPointOfContact();

    /**
     * Sets the value of the '{@link net.opengis.ows11.DocumentRoot#getPointOfContact <em>Point Of Contact</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Point Of Contact</em>' containment reference.
     * @see #getPointOfContact()
     * @generated
     */
    void setPointOfContact(ResponsiblePartyType value);

    /**
     * Returns the value of the '<em><b>Position Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Role or position of the responsible person. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Position Name</em>' attribute.
     * @see #setPositionName(String)
     * @see net.opengis.ows11.Ows11Package#getDocumentRoot_PositionName()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='PositionName' namespace='##targetNamespace'"
     * @generated
     */
    String getPositionName();

    /**
     * Sets the value of the '{@link net.opengis.ows11.DocumentRoot#getPositionName <em>Position Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Position Name</em>' attribute.
     * @see #getPositionName()
     * @generated
     */
    void setPositionName(String value);

    /**
     * Returns the value of the '<em><b>Range</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Range</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Range</em>' containment reference.
     * @see #setRange(RangeType)
     * @see net.opengis.ows11.Ows11Package#getDocumentRoot_Range()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Range' namespace='##targetNamespace'"
     * @generated
     */
    RangeType getRange();

    /**
     * Sets the value of the '{@link net.opengis.ows11.DocumentRoot#getRange <em>Range</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Range</em>' containment reference.
     * @see #getRange()
     * @generated
     */
    void setRange(RangeType value);

    /**
     * Returns the value of the '<em><b>Reference</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Reference</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Reference</em>' containment reference.
     * @see #setReference(ReferenceType)
     * @see net.opengis.ows11.Ows11Package#getDocumentRoot_Reference()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Reference' namespace='##targetNamespace' affiliation='AbstractReferenceBase'"
     * @generated
     */
    ReferenceType getReference();

    /**
     * Sets the value of the '{@link net.opengis.ows11.DocumentRoot#getReference <em>Reference</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Reference</em>' containment reference.
     * @see #getReference()
     * @generated
     */
    void setReference(ReferenceType value);

    /**
     * Returns the value of the '<em><b>Reference Group</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Reference Group</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Reference Group</em>' containment reference.
     * @see #setReferenceGroup(ReferenceGroupType)
     * @see net.opengis.ows11.Ows11Package#getDocumentRoot_ReferenceGroup()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='ReferenceGroup' namespace='##targetNamespace'"
     * @generated
     */
    ReferenceGroupType getReferenceGroup();

    /**
     * Sets the value of the '{@link net.opengis.ows11.DocumentRoot#getReferenceGroup <em>Reference Group</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Reference Group</em>' containment reference.
     * @see #getReferenceGroup()
     * @generated
     */
    void setReferenceGroup(ReferenceGroupType value);

    /**
     * Returns the value of the '<em><b>Reference System</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Definition of the reference system used by this set of values, including the unit of measure whenever applicable (as is normal). In this case, the xlink:href attribute can reference a URN for a well-known reference system, such as for a coordinate reference system (CRS). For example, such a URN could be a CRS identification URN defined in the "ogc" URN namespace. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Reference System</em>' containment reference.
     * @see #setReferenceSystem(DomainMetadataType)
     * @see net.opengis.ows11.Ows11Package#getDocumentRoot_ReferenceSystem()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='ReferenceSystem' namespace='##targetNamespace'"
     * @generated
     */
    DomainMetadataType getReferenceSystem();

    /**
     * Sets the value of the '{@link net.opengis.ows11.DocumentRoot#getReferenceSystem <em>Reference System</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Reference System</em>' containment reference.
     * @see #getReferenceSystem()
     * @generated
     */
    void setReferenceSystem(DomainMetadataType value);

    /**
     * Returns the value of the '<em><b>Resource</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * XML encoded GetResourceByID operation response. The complexType used by this element shall be specified by each specific OWS.  
     * <!-- end-model-doc -->
     * @return the value of the '<em>Resource</em>' containment reference.
     * @see #setResource(EObject)
     * @see net.opengis.ows11.Ows11Package#getDocumentRoot_Resource()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Resource' namespace='##targetNamespace'"
     * @generated
     */
    EObject getResource();

    /**
     * Sets the value of the '{@link net.opengis.ows11.DocumentRoot#getResource <em>Resource</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Resource</em>' containment reference.
     * @see #getResource()
     * @generated
     */
    void setResource(EObject value);

    /**
     * Returns the value of the '<em><b>Role</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Function performed by the responsible party. Possible values of this Role shall include the values and the meanings listed in Subclause B.5.5 of ISO 19115:2003. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Role</em>' containment reference.
     * @see #setRole(CodeType)
     * @see net.opengis.ows11.Ows11Package#getDocumentRoot_Role()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Role' namespace='##targetNamespace'"
     * @generated
     */
    CodeType getRole();

    /**
     * Sets the value of the '{@link net.opengis.ows11.DocumentRoot#getRole <em>Role</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Role</em>' containment reference.
     * @see #getRole()
     * @generated
     */
    void setRole(CodeType value);

    /**
     * Returns the value of the '<em><b>Service Identification</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * General metadata for this specific server. This XML Schema of this section shall be the same for all OWS. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Service Identification</em>' containment reference.
     * @see #setServiceIdentification(ServiceIdentificationType)
     * @see net.opengis.ows11.Ows11Package#getDocumentRoot_ServiceIdentification()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='ServiceIdentification' namespace='##targetNamespace'"
     * @generated
     */
    ServiceIdentificationType getServiceIdentification();

    /**
     * Sets the value of the '{@link net.opengis.ows11.DocumentRoot#getServiceIdentification <em>Service Identification</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Service Identification</em>' containment reference.
     * @see #getServiceIdentification()
     * @generated
     */
    void setServiceIdentification(ServiceIdentificationType value);

    /**
     * Returns the value of the '<em><b>Service Provider</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Metadata about the organization that provides this specific service instance or server. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Service Provider</em>' containment reference.
     * @see #setServiceProvider(ServiceProviderType)
     * @see net.opengis.ows11.Ows11Package#getDocumentRoot_ServiceProvider()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='ServiceProvider' namespace='##targetNamespace'"
     * @generated
     */
    ServiceProviderType getServiceProvider();

    /**
     * Sets the value of the '{@link net.opengis.ows11.DocumentRoot#getServiceProvider <em>Service Provider</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Service Provider</em>' containment reference.
     * @see #getServiceProvider()
     * @generated
     */
    void setServiceProvider(ServiceProviderType value);

    /**
     * Returns the value of the '<em><b>Service Reference</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Service Reference</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Service Reference</em>' containment reference.
     * @see #setServiceReference(ServiceReferenceType)
     * @see net.opengis.ows11.Ows11Package#getDocumentRoot_ServiceReference()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='ServiceReference' namespace='##targetNamespace' affiliation='Reference'"
     * @generated
     */
    ServiceReferenceType getServiceReference();

    /**
     * Sets the value of the '{@link net.opengis.ows11.DocumentRoot#getServiceReference <em>Service Reference</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Service Reference</em>' containment reference.
     * @see #getServiceReference()
     * @generated
     */
    void setServiceReference(ServiceReferenceType value);

    /**
     * Returns the value of the '<em><b>Spacing</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The regular distance or spacing between the allowed values in a range. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Spacing</em>' containment reference.
     * @see #setSpacing(ValueType)
     * @see net.opengis.ows11.Ows11Package#getDocumentRoot_Spacing()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Spacing' namespace='##targetNamespace'"
     * @generated
     */
    ValueType getSpacing();

    /**
     * Sets the value of the '{@link net.opengis.ows11.DocumentRoot#getSpacing <em>Spacing</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Spacing</em>' containment reference.
     * @see #getSpacing()
     * @generated
     */
    void setSpacing(ValueType value);

    /**
     * Returns the value of the '<em><b>Supported CRS</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Coordinate reference system in which data from this data(set) or resource is available or supported. More specific parameter names should be used by specific OWS specifications wherever applicable. More than one such parameter can be included for different purposes. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Supported CRS</em>' attribute.
     * @see #setSupportedCRS(String)
     * @see net.opengis.ows11.Ows11Package#getDocumentRoot_SupportedCRS()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.AnyURI" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='SupportedCRS' namespace='##targetNamespace' affiliation='AvailableCRS'"
     * @generated
     */
    String getSupportedCRS();

    /**
     * Sets the value of the '{@link net.opengis.ows11.DocumentRoot#getSupportedCRS <em>Supported CRS</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Supported CRS</em>' attribute.
     * @see #getSupportedCRS()
     * @generated
     */
    void setSupportedCRS(String value);

    /**
     * Returns the value of the '<em><b>Title</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Title of this resource, normally used for display to a human. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Title</em>' containment reference.
     * @see #setTitle(LanguageStringType)
     * @see net.opengis.ows11.Ows11Package#getDocumentRoot_Title()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Title' namespace='##targetNamespace'"
     * @generated
     */
    LanguageStringType getTitle();

    /**
     * Sets the value of the '{@link net.opengis.ows11.DocumentRoot#getTitle <em>Title</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Title</em>' containment reference.
     * @see #getTitle()
     * @generated
     */
    void setTitle(LanguageStringType value);

    /**
     * Returns the value of the '<em><b>UOM</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Definition of the unit of measure of this set of values. In this case, the xlink:href attribute can reference a URN for a well-known unit of measure (uom). For example, such a URN could be a UOM identification URN defined in the "ogc" URN namespace. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>UOM</em>' containment reference.
     * @see #setUOM(DomainMetadataType)
     * @see net.opengis.ows11.Ows11Package#getDocumentRoot_UOM()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='UOM' namespace='##targetNamespace'"
     * @generated
     */
    DomainMetadataType getUOM();

    /**
     * Sets the value of the '{@link net.opengis.ows11.DocumentRoot#getUOM <em>UOM</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>UOM</em>' containment reference.
     * @see #getUOM()
     * @generated
     */
    void setUOM(DomainMetadataType value);

    /**
     * Returns the value of the '<em><b>Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Value</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Value</em>' containment reference.
     * @see #setValue(ValueType)
     * @see net.opengis.ows11.Ows11Package#getDocumentRoot_Value()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Value' namespace='##targetNamespace'"
     * @generated
     */
    ValueType getValue();

    /**
     * Sets the value of the '{@link net.opengis.ows11.DocumentRoot#getValue <em>Value</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Value</em>' containment reference.
     * @see #getValue()
     * @generated
     */
    void setValue(ValueType value);

    /**
     * Returns the value of the '<em><b>Values Reference</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Reference to externally specified list of all the valid values and/or ranges of values for this quantity. (Informative: This element was simplified from the metaDataProperty element in GML 3.0.) 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Values Reference</em>' containment reference.
     * @see #setValuesReference(ValuesReferenceType)
     * @see net.opengis.ows11.Ows11Package#getDocumentRoot_ValuesReference()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='ValuesReference' namespace='##targetNamespace'"
     * @generated
     */
    ValuesReferenceType getValuesReference();

    /**
     * Sets the value of the '{@link net.opengis.ows11.DocumentRoot#getValuesReference <em>Values Reference</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Values Reference</em>' containment reference.
     * @see #getValuesReference()
     * @generated
     */
    void setValuesReference(ValuesReferenceType value);

    /**
     * Returns the value of the '<em><b>WGS84 Bounding Box</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>WGS84 Bounding Box</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>WGS84 Bounding Box</em>' containment reference.
     * @see #setWGS84BoundingBox(WGS84BoundingBoxType)
     * @see net.opengis.ows11.Ows11Package#getDocumentRoot_WGS84BoundingBox()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='WGS84BoundingBox' namespace='##targetNamespace' affiliation='BoundingBox'"
     * @generated
     */
    WGS84BoundingBoxType getWGS84BoundingBox();

    /**
     * Sets the value of the '{@link net.opengis.ows11.DocumentRoot#getWGS84BoundingBox <em>WGS84 Bounding Box</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>WGS84 Bounding Box</em>' containment reference.
     * @see #getWGS84BoundingBox()
     * @generated
     */
    void setWGS84BoundingBox(WGS84BoundingBoxType value);

    /**
     * Returns the value of the '<em><b>Range Closure</b></em>' attribute.
     * The default value is <code>"closed"</code>.
     * The literals are from the enumeration {@link net.opengis.ows11.RangeClosureType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Specifies which of the minimum and maximum values are included in the range. Note that plus and minus infinity are considered closed bounds. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Range Closure</em>' attribute.
     * @see net.opengis.ows11.RangeClosureType
     * @see #isSetRangeClosure()
     * @see #unsetRangeClosure()
     * @see #setRangeClosure(RangeClosureType)
     * @see net.opengis.ows11.Ows11Package#getDocumentRoot_RangeClosure()
     * @model default="closed" unsettable="true"
     *        extendedMetaData="kind='attribute' name='rangeClosure' namespace='##targetNamespace'"
     * @generated
     */
    RangeClosureType getRangeClosure();

    /**
     * Sets the value of the '{@link net.opengis.ows11.DocumentRoot#getRangeClosure <em>Range Closure</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Range Closure</em>' attribute.
     * @see net.opengis.ows11.RangeClosureType
     * @see #isSetRangeClosure()
     * @see #unsetRangeClosure()
     * @see #getRangeClosure()
     * @generated
     */
    void setRangeClosure(RangeClosureType value);

    /**
     * Unsets the value of the '{@link net.opengis.ows11.DocumentRoot#getRangeClosure <em>Range Closure</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetRangeClosure()
     * @see #getRangeClosure()
     * @see #setRangeClosure(RangeClosureType)
     * @generated
     */
    void unsetRangeClosure();

    /**
     * Returns whether the value of the '{@link net.opengis.ows11.DocumentRoot#getRangeClosure <em>Range Closure</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Range Closure</em>' attribute is set.
     * @see #unsetRangeClosure()
     * @see #getRangeClosure()
     * @see #setRangeClosure(RangeClosureType)
     * @generated
     */
    boolean isSetRangeClosure();

    /**
     * Returns the value of the '<em><b>Reference1</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Reference to data or metadata recorded elsewhere, either external to this XML document or within it. Whenever practical, this attribute should be a URL from which this metadata can be electronically retrieved. Alternately, this attribute can reference a URN for well-known metadata. For example, such a URN could be a URN defined in the "ogc" URN namespace. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Reference1</em>' attribute.
     * @see #setReference1(String)
     * @see net.opengis.ows11.Ows11Package#getDocumentRoot_Reference1()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
     *        extendedMetaData="kind='attribute' name='reference' namespace='##targetNamespace'"
     * @generated
     */
    String getReference1();

    /**
     * Sets the value of the '{@link net.opengis.ows11.DocumentRoot#getReference1 <em>Reference1</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Reference1</em>' attribute.
     * @see #getReference1()
     * @generated
     */
    void setReference1(String value);

} // DocumentRoot
