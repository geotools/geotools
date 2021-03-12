/**
 */
package org.w3._2001.schema;

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
 * </p>
 * <ul>
 *   <li>{@link org.w3._2001.schema.DocumentRoot#getMixed <em>Mixed</em>}</li>
 *   <li>{@link org.w3._2001.schema.DocumentRoot#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}</li>
 *   <li>{@link org.w3._2001.schema.DocumentRoot#getXSISchemaLocation <em>XSI Schema Location</em>}</li>
 *   <li>{@link org.w3._2001.schema.DocumentRoot#getAll <em>All</em>}</li>
 *   <li>{@link org.w3._2001.schema.DocumentRoot#getAnnotation <em>Annotation</em>}</li>
 *   <li>{@link org.w3._2001.schema.DocumentRoot#getAny <em>Any</em>}</li>
 *   <li>{@link org.w3._2001.schema.DocumentRoot#getAnyAttribute <em>Any Attribute</em>}</li>
 *   <li>{@link org.w3._2001.schema.DocumentRoot#getAppinfo <em>Appinfo</em>}</li>
 *   <li>{@link org.w3._2001.schema.DocumentRoot#getAttribute <em>Attribute</em>}</li>
 *   <li>{@link org.w3._2001.schema.DocumentRoot#getAttributeGroup <em>Attribute Group</em>}</li>
 *   <li>{@link org.w3._2001.schema.DocumentRoot#getChoice <em>Choice</em>}</li>
 *   <li>{@link org.w3._2001.schema.DocumentRoot#getComplexContent <em>Complex Content</em>}</li>
 *   <li>{@link org.w3._2001.schema.DocumentRoot#getComplexType <em>Complex Type</em>}</li>
 *   <li>{@link org.w3._2001.schema.DocumentRoot#getDocumentation <em>Documentation</em>}</li>
 *   <li>{@link org.w3._2001.schema.DocumentRoot#getElement <em>Element</em>}</li>
 *   <li>{@link org.w3._2001.schema.DocumentRoot#getEnumeration <em>Enumeration</em>}</li>
 *   <li>{@link org.w3._2001.schema.DocumentRoot#getField <em>Field</em>}</li>
 *   <li>{@link org.w3._2001.schema.DocumentRoot#getFractionDigits <em>Fraction Digits</em>}</li>
 *   <li>{@link org.w3._2001.schema.DocumentRoot#getGroup <em>Group</em>}</li>
 *   <li>{@link org.w3._2001.schema.DocumentRoot#getImport <em>Import</em>}</li>
 *   <li>{@link org.w3._2001.schema.DocumentRoot#getInclude <em>Include</em>}</li>
 *   <li>{@link org.w3._2001.schema.DocumentRoot#getKey <em>Key</em>}</li>
 *   <li>{@link org.w3._2001.schema.DocumentRoot#getKeyref <em>Keyref</em>}</li>
 *   <li>{@link org.w3._2001.schema.DocumentRoot#getLength <em>Length</em>}</li>
 *   <li>{@link org.w3._2001.schema.DocumentRoot#getList <em>List</em>}</li>
 *   <li>{@link org.w3._2001.schema.DocumentRoot#getMaxExclusive <em>Max Exclusive</em>}</li>
 *   <li>{@link org.w3._2001.schema.DocumentRoot#getMaxInclusive <em>Max Inclusive</em>}</li>
 *   <li>{@link org.w3._2001.schema.DocumentRoot#getMaxLength <em>Max Length</em>}</li>
 *   <li>{@link org.w3._2001.schema.DocumentRoot#getMinExclusive <em>Min Exclusive</em>}</li>
 *   <li>{@link org.w3._2001.schema.DocumentRoot#getMinInclusive <em>Min Inclusive</em>}</li>
 *   <li>{@link org.w3._2001.schema.DocumentRoot#getMinLength <em>Min Length</em>}</li>
 *   <li>{@link org.w3._2001.schema.DocumentRoot#getNotation <em>Notation</em>}</li>
 *   <li>{@link org.w3._2001.schema.DocumentRoot#getPattern <em>Pattern</em>}</li>
 *   <li>{@link org.w3._2001.schema.DocumentRoot#getRedefine <em>Redefine</em>}</li>
 *   <li>{@link org.w3._2001.schema.DocumentRoot#getRestriction <em>Restriction</em>}</li>
 *   <li>{@link org.w3._2001.schema.DocumentRoot#getSchema <em>Schema</em>}</li>
 *   <li>{@link org.w3._2001.schema.DocumentRoot#getSelector <em>Selector</em>}</li>
 *   <li>{@link org.w3._2001.schema.DocumentRoot#getSequence <em>Sequence</em>}</li>
 *   <li>{@link org.w3._2001.schema.DocumentRoot#getSimpleContent <em>Simple Content</em>}</li>
 *   <li>{@link org.w3._2001.schema.DocumentRoot#getSimpleType <em>Simple Type</em>}</li>
 *   <li>{@link org.w3._2001.schema.DocumentRoot#getTotalDigits <em>Total Digits</em>}</li>
 *   <li>{@link org.w3._2001.schema.DocumentRoot#getUnion <em>Union</em>}</li>
 *   <li>{@link org.w3._2001.schema.DocumentRoot#getUnique <em>Unique</em>}</li>
 *   <li>{@link org.w3._2001.schema.DocumentRoot#getWhiteSpace <em>White Space</em>}</li>
 * </ul>
 *
 * @see org.w3._2001.schema.SchemaPackage#getDocumentRoot()
 * @model extendedMetaData="name='' kind='mixed'"
 * @generated
 */
public interface DocumentRoot extends EObject {
	/**
	 * Returns the value of the '<em><b>Mixed</b></em>' attribute list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Mixed</em>' attribute list.
	 * @see org.w3._2001.schema.SchemaPackage#getDocumentRoot_Mixed()
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
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>XMLNS Prefix Map</em>' map.
	 * @see org.w3._2001.schema.SchemaPackage#getDocumentRoot_XMLNSPrefixMap()
	 * @model mapType="org.eclipse.emf.ecore.EStringToStringMapEntry&lt;org.eclipse.emf.ecore.EString, org.eclipse.emf.ecore.EString&gt;" transient="true"
	 *        extendedMetaData="kind='attribute' name='xmlns:prefix'"
	 * @generated
	 */
	EMap<String, String> getXMLNSPrefixMap();

	/**
	 * Returns the value of the '<em><b>XSI Schema Location</b></em>' map.
	 * The key is of type {@link java.lang.String},
	 * and the value is of type {@link java.lang.String},
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>XSI Schema Location</em>' map.
	 * @see org.w3._2001.schema.SchemaPackage#getDocumentRoot_XSISchemaLocation()
	 * @model mapType="org.eclipse.emf.ecore.EStringToStringMapEntry&lt;org.eclipse.emf.ecore.EString, org.eclipse.emf.ecore.EString&gt;" transient="true"
	 *        extendedMetaData="kind='attribute' name='xsi:schemaLocation'"
	 * @generated
	 */
	EMap<String, String> getXSISchemaLocation();

	/**
	 * Returns the value of the '<em><b>All</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>All</em>' containment reference.
	 * @see #setAll(All)
	 * @see org.w3._2001.schema.SchemaPackage#getDocumentRoot_All()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='all' namespace='##targetNamespace'"
	 * @generated
	 */
	All getAll();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.DocumentRoot#getAll <em>All</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>All</em>' containment reference.
	 * @see #getAll()
	 * @generated
	 */
	void setAll(All value);

	/**
	 * Returns the value of the '<em><b>Annotation</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Annotation</em>' containment reference.
	 * @see #setAnnotation(AnnotationType)
	 * @see org.w3._2001.schema.SchemaPackage#getDocumentRoot_Annotation()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='annotation' namespace='##targetNamespace'"
	 * @generated
	 */
	AnnotationType getAnnotation();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.DocumentRoot#getAnnotation <em>Annotation</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Annotation</em>' containment reference.
	 * @see #getAnnotation()
	 * @generated
	 */
	void setAnnotation(AnnotationType value);

	/**
	 * Returns the value of the '<em><b>Any</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Any</em>' containment reference.
	 * @see #setAny(AnyType)
	 * @see org.w3._2001.schema.SchemaPackage#getDocumentRoot_Any()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='any' namespace='##targetNamespace'"
	 * @generated
	 */
	AnyType getAny();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.DocumentRoot#getAny <em>Any</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Any</em>' containment reference.
	 * @see #getAny()
	 * @generated
	 */
	void setAny(AnyType value);

	/**
	 * Returns the value of the '<em><b>Any Attribute</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Any Attribute</em>' containment reference.
	 * @see #setAnyAttribute(Wildcard)
	 * @see org.w3._2001.schema.SchemaPackage#getDocumentRoot_AnyAttribute()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='anyAttribute' namespace='##targetNamespace'"
	 * @generated
	 */
	Wildcard getAnyAttribute();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.DocumentRoot#getAnyAttribute <em>Any Attribute</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Any Attribute</em>' containment reference.
	 * @see #getAnyAttribute()
	 * @generated
	 */
	void setAnyAttribute(Wildcard value);

	/**
	 * Returns the value of the '<em><b>Appinfo</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Appinfo</em>' containment reference.
	 * @see #setAppinfo(AppinfoType)
	 * @see org.w3._2001.schema.SchemaPackage#getDocumentRoot_Appinfo()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='appinfo' namespace='##targetNamespace'"
	 * @generated
	 */
	AppinfoType getAppinfo();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.DocumentRoot#getAppinfo <em>Appinfo</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Appinfo</em>' containment reference.
	 * @see #getAppinfo()
	 * @generated
	 */
	void setAppinfo(AppinfoType value);

	/**
	 * Returns the value of the '<em><b>Attribute</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Attribute</em>' containment reference.
	 * @see #setAttribute(TopLevelAttribute)
	 * @see org.w3._2001.schema.SchemaPackage#getDocumentRoot_Attribute()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='attribute' namespace='##targetNamespace'"
	 * @generated
	 */
	TopLevelAttribute getAttribute();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.DocumentRoot#getAttribute <em>Attribute</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Attribute</em>' containment reference.
	 * @see #getAttribute()
	 * @generated
	 */
	void setAttribute(TopLevelAttribute value);

	/**
	 * Returns the value of the '<em><b>Attribute Group</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Attribute Group</em>' containment reference.
	 * @see #setAttributeGroup(NamedAttributeGroup)
	 * @see org.w3._2001.schema.SchemaPackage#getDocumentRoot_AttributeGroup()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='attributeGroup' namespace='##targetNamespace'"
	 * @generated
	 */
	NamedAttributeGroup getAttributeGroup();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.DocumentRoot#getAttributeGroup <em>Attribute Group</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Attribute Group</em>' containment reference.
	 * @see #getAttributeGroup()
	 * @generated
	 */
	void setAttributeGroup(NamedAttributeGroup value);

	/**
	 * Returns the value of the '<em><b>Choice</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Choice</em>' containment reference.
	 * @see #setChoice(ExplicitGroup)
	 * @see org.w3._2001.schema.SchemaPackage#getDocumentRoot_Choice()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='choice' namespace='##targetNamespace'"
	 * @generated
	 */
	ExplicitGroup getChoice();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.DocumentRoot#getChoice <em>Choice</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Choice</em>' containment reference.
	 * @see #getChoice()
	 * @generated
	 */
	void setChoice(ExplicitGroup value);

	/**
	 * Returns the value of the '<em><b>Complex Content</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Complex Content</em>' containment reference.
	 * @see #setComplexContent(ComplexContentType)
	 * @see org.w3._2001.schema.SchemaPackage#getDocumentRoot_ComplexContent()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='complexContent' namespace='##targetNamespace'"
	 * @generated
	 */
	ComplexContentType getComplexContent();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.DocumentRoot#getComplexContent <em>Complex Content</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Complex Content</em>' containment reference.
	 * @see #getComplexContent()
	 * @generated
	 */
	void setComplexContent(ComplexContentType value);

	/**
	 * Returns the value of the '<em><b>Complex Type</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Complex Type</em>' containment reference.
	 * @see #setComplexType(TopLevelComplexType)
	 * @see org.w3._2001.schema.SchemaPackage#getDocumentRoot_ComplexType()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='complexType' namespace='##targetNamespace'"
	 * @generated
	 */
	TopLevelComplexType getComplexType();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.DocumentRoot#getComplexType <em>Complex Type</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Complex Type</em>' containment reference.
	 * @see #getComplexType()
	 * @generated
	 */
	void setComplexType(TopLevelComplexType value);

	/**
	 * Returns the value of the '<em><b>Documentation</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Documentation</em>' containment reference.
	 * @see #setDocumentation(DocumentationType)
	 * @see org.w3._2001.schema.SchemaPackage#getDocumentRoot_Documentation()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='documentation' namespace='##targetNamespace'"
	 * @generated
	 */
	DocumentationType getDocumentation();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.DocumentRoot#getDocumentation <em>Documentation</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Documentation</em>' containment reference.
	 * @see #getDocumentation()
	 * @generated
	 */
	void setDocumentation(DocumentationType value);

	/**
	 * Returns the value of the '<em><b>Element</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Element</em>' containment reference.
	 * @see #setElement(TopLevelElement)
	 * @see org.w3._2001.schema.SchemaPackage#getDocumentRoot_Element()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='element' namespace='##targetNamespace'"
	 * @generated
	 */
	TopLevelElement getElement();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.DocumentRoot#getElement <em>Element</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Element</em>' containment reference.
	 * @see #getElement()
	 * @generated
	 */
	void setElement(TopLevelElement value);

	/**
	 * Returns the value of the '<em><b>Enumeration</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Enumeration</em>' containment reference.
	 * @see #setEnumeration(NoFixedFacet)
	 * @see org.w3._2001.schema.SchemaPackage#getDocumentRoot_Enumeration()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='enumeration' namespace='##targetNamespace'"
	 * @generated
	 */
	NoFixedFacet getEnumeration();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.DocumentRoot#getEnumeration <em>Enumeration</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Enumeration</em>' containment reference.
	 * @see #getEnumeration()
	 * @generated
	 */
	void setEnumeration(NoFixedFacet value);

	/**
	 * Returns the value of the '<em><b>Field</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Field</em>' containment reference.
	 * @see #setField(FieldType)
	 * @see org.w3._2001.schema.SchemaPackage#getDocumentRoot_Field()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='field' namespace='##targetNamespace'"
	 * @generated
	 */
	FieldType getField();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.DocumentRoot#getField <em>Field</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Field</em>' containment reference.
	 * @see #getField()
	 * @generated
	 */
	void setField(FieldType value);

	/**
	 * Returns the value of the '<em><b>Fraction Digits</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Fraction Digits</em>' containment reference.
	 * @see #setFractionDigits(NumFacet)
	 * @see org.w3._2001.schema.SchemaPackage#getDocumentRoot_FractionDigits()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='fractionDigits' namespace='##targetNamespace'"
	 * @generated
	 */
	NumFacet getFractionDigits();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.DocumentRoot#getFractionDigits <em>Fraction Digits</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fraction Digits</em>' containment reference.
	 * @see #getFractionDigits()
	 * @generated
	 */
	void setFractionDigits(NumFacet value);

	/**
	 * Returns the value of the '<em><b>Group</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Group</em>' containment reference.
	 * @see #setGroup(NamedGroup)
	 * @see org.w3._2001.schema.SchemaPackage#getDocumentRoot_Group()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='group' namespace='##targetNamespace'"
	 * @generated
	 */
	NamedGroup getGroup();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.DocumentRoot#getGroup <em>Group</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Group</em>' containment reference.
	 * @see #getGroup()
	 * @generated
	 */
	void setGroup(NamedGroup value);

	/**
	 * Returns the value of the '<em><b>Import</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Import</em>' containment reference.
	 * @see #setImport(ImportType)
	 * @see org.w3._2001.schema.SchemaPackage#getDocumentRoot_Import()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='import' namespace='##targetNamespace'"
	 * @generated
	 */
	ImportType getImport();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.DocumentRoot#getImport <em>Import</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Import</em>' containment reference.
	 * @see #getImport()
	 * @generated
	 */
	void setImport(ImportType value);

	/**
	 * Returns the value of the '<em><b>Include</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Include</em>' containment reference.
	 * @see #setInclude(IncludeType)
	 * @see org.w3._2001.schema.SchemaPackage#getDocumentRoot_Include()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='include' namespace='##targetNamespace'"
	 * @generated
	 */
	IncludeType getInclude();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.DocumentRoot#getInclude <em>Include</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Include</em>' containment reference.
	 * @see #getInclude()
	 * @generated
	 */
	void setInclude(IncludeType value);

	/**
	 * Returns the value of the '<em><b>Key</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Key</em>' containment reference.
	 * @see #setKey(Keybase)
	 * @see org.w3._2001.schema.SchemaPackage#getDocumentRoot_Key()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='key' namespace='##targetNamespace'"
	 * @generated
	 */
	Keybase getKey();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.DocumentRoot#getKey <em>Key</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Key</em>' containment reference.
	 * @see #getKey()
	 * @generated
	 */
	void setKey(Keybase value);

	/**
	 * Returns the value of the '<em><b>Keyref</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Keyref</em>' containment reference.
	 * @see #setKeyref(KeyrefType)
	 * @see org.w3._2001.schema.SchemaPackage#getDocumentRoot_Keyref()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='keyref' namespace='##targetNamespace'"
	 * @generated
	 */
	KeyrefType getKeyref();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.DocumentRoot#getKeyref <em>Keyref</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Keyref</em>' containment reference.
	 * @see #getKeyref()
	 * @generated
	 */
	void setKeyref(KeyrefType value);

	/**
	 * Returns the value of the '<em><b>Length</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Length</em>' containment reference.
	 * @see #setLength(NumFacet)
	 * @see org.w3._2001.schema.SchemaPackage#getDocumentRoot_Length()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='length' namespace='##targetNamespace'"
	 * @generated
	 */
	NumFacet getLength();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.DocumentRoot#getLength <em>Length</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Length</em>' containment reference.
	 * @see #getLength()
	 * @generated
	 */
	void setLength(NumFacet value);

	/**
	 * Returns the value of the '<em><b>List</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>List</em>' containment reference.
	 * @see #setList(ListType)
	 * @see org.w3._2001.schema.SchemaPackage#getDocumentRoot_List()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='list' namespace='##targetNamespace'"
	 * @generated
	 */
	ListType getList();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.DocumentRoot#getList <em>List</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>List</em>' containment reference.
	 * @see #getList()
	 * @generated
	 */
	void setList(ListType value);

	/**
	 * Returns the value of the '<em><b>Max Exclusive</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Max Exclusive</em>' containment reference.
	 * @see #setMaxExclusive(Facet)
	 * @see org.w3._2001.schema.SchemaPackage#getDocumentRoot_MaxExclusive()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='maxExclusive' namespace='##targetNamespace'"
	 * @generated
	 */
	Facet getMaxExclusive();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.DocumentRoot#getMaxExclusive <em>Max Exclusive</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Max Exclusive</em>' containment reference.
	 * @see #getMaxExclusive()
	 * @generated
	 */
	void setMaxExclusive(Facet value);

	/**
	 * Returns the value of the '<em><b>Max Inclusive</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Max Inclusive</em>' containment reference.
	 * @see #setMaxInclusive(Facet)
	 * @see org.w3._2001.schema.SchemaPackage#getDocumentRoot_MaxInclusive()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='maxInclusive' namespace='##targetNamespace'"
	 * @generated
	 */
	Facet getMaxInclusive();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.DocumentRoot#getMaxInclusive <em>Max Inclusive</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Max Inclusive</em>' containment reference.
	 * @see #getMaxInclusive()
	 * @generated
	 */
	void setMaxInclusive(Facet value);

	/**
	 * Returns the value of the '<em><b>Max Length</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Max Length</em>' containment reference.
	 * @see #setMaxLength(NumFacet)
	 * @see org.w3._2001.schema.SchemaPackage#getDocumentRoot_MaxLength()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='maxLength' namespace='##targetNamespace'"
	 * @generated
	 */
	NumFacet getMaxLength();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.DocumentRoot#getMaxLength <em>Max Length</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Max Length</em>' containment reference.
	 * @see #getMaxLength()
	 * @generated
	 */
	void setMaxLength(NumFacet value);

	/**
	 * Returns the value of the '<em><b>Min Exclusive</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Min Exclusive</em>' containment reference.
	 * @see #setMinExclusive(Facet)
	 * @see org.w3._2001.schema.SchemaPackage#getDocumentRoot_MinExclusive()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='minExclusive' namespace='##targetNamespace'"
	 * @generated
	 */
	Facet getMinExclusive();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.DocumentRoot#getMinExclusive <em>Min Exclusive</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Min Exclusive</em>' containment reference.
	 * @see #getMinExclusive()
	 * @generated
	 */
	void setMinExclusive(Facet value);

	/**
	 * Returns the value of the '<em><b>Min Inclusive</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Min Inclusive</em>' containment reference.
	 * @see #setMinInclusive(Facet)
	 * @see org.w3._2001.schema.SchemaPackage#getDocumentRoot_MinInclusive()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='minInclusive' namespace='##targetNamespace'"
	 * @generated
	 */
	Facet getMinInclusive();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.DocumentRoot#getMinInclusive <em>Min Inclusive</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Min Inclusive</em>' containment reference.
	 * @see #getMinInclusive()
	 * @generated
	 */
	void setMinInclusive(Facet value);

	/**
	 * Returns the value of the '<em><b>Min Length</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Min Length</em>' containment reference.
	 * @see #setMinLength(NumFacet)
	 * @see org.w3._2001.schema.SchemaPackage#getDocumentRoot_MinLength()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='minLength' namespace='##targetNamespace'"
	 * @generated
	 */
	NumFacet getMinLength();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.DocumentRoot#getMinLength <em>Min Length</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Min Length</em>' containment reference.
	 * @see #getMinLength()
	 * @generated
	 */
	void setMinLength(NumFacet value);

	/**
	 * Returns the value of the '<em><b>Notation</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Notation</em>' containment reference.
	 * @see #setNotation(NotationType)
	 * @see org.w3._2001.schema.SchemaPackage#getDocumentRoot_Notation()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='notation' namespace='##targetNamespace'"
	 * @generated
	 */
	NotationType getNotation();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.DocumentRoot#getNotation <em>Notation</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Notation</em>' containment reference.
	 * @see #getNotation()
	 * @generated
	 */
	void setNotation(NotationType value);

	/**
	 * Returns the value of the '<em><b>Pattern</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Pattern</em>' containment reference.
	 * @see #setPattern(PatternType)
	 * @see org.w3._2001.schema.SchemaPackage#getDocumentRoot_Pattern()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='pattern' namespace='##targetNamespace'"
	 * @generated
	 */
	PatternType getPattern();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.DocumentRoot#getPattern <em>Pattern</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Pattern</em>' containment reference.
	 * @see #getPattern()
	 * @generated
	 */
	void setPattern(PatternType value);

	/**
	 * Returns the value of the '<em><b>Redefine</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Redefine</em>' containment reference.
	 * @see #setRedefine(RedefineType)
	 * @see org.w3._2001.schema.SchemaPackage#getDocumentRoot_Redefine()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='redefine' namespace='##targetNamespace'"
	 * @generated
	 */
	RedefineType getRedefine();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.DocumentRoot#getRedefine <em>Redefine</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Redefine</em>' containment reference.
	 * @see #getRedefine()
	 * @generated
	 */
	void setRedefine(RedefineType value);

	/**
	 * Returns the value of the '<em><b>Restriction</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Restriction</em>' containment reference.
	 * @see #setRestriction(RestrictionType1)
	 * @see org.w3._2001.schema.SchemaPackage#getDocumentRoot_Restriction()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='restriction' namespace='##targetNamespace'"
	 * @generated
	 */
	RestrictionType1 getRestriction();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.DocumentRoot#getRestriction <em>Restriction</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Restriction</em>' containment reference.
	 * @see #getRestriction()
	 * @generated
	 */
	void setRestriction(RestrictionType1 value);

	/**
	 * Returns the value of the '<em><b>Schema</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Schema</em>' containment reference.
	 * @see #setSchema(SchemaType)
	 * @see org.w3._2001.schema.SchemaPackage#getDocumentRoot_Schema()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='schema' namespace='##targetNamespace'"
	 * @generated
	 */
	SchemaType getSchema();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.DocumentRoot#getSchema <em>Schema</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Schema</em>' containment reference.
	 * @see #getSchema()
	 * @generated
	 */
	void setSchema(SchemaType value);

	/**
	 * Returns the value of the '<em><b>Selector</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Selector</em>' containment reference.
	 * @see #setSelector(SelectorType)
	 * @see org.w3._2001.schema.SchemaPackage#getDocumentRoot_Selector()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='selector' namespace='##targetNamespace'"
	 * @generated
	 */
	SelectorType getSelector();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.DocumentRoot#getSelector <em>Selector</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Selector</em>' containment reference.
	 * @see #getSelector()
	 * @generated
	 */
	void setSelector(SelectorType value);

	/**
	 * Returns the value of the '<em><b>Sequence</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Sequence</em>' containment reference.
	 * @see #setSequence(ExplicitGroup)
	 * @see org.w3._2001.schema.SchemaPackage#getDocumentRoot_Sequence()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='sequence' namespace='##targetNamespace'"
	 * @generated
	 */
	ExplicitGroup getSequence();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.DocumentRoot#getSequence <em>Sequence</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Sequence</em>' containment reference.
	 * @see #getSequence()
	 * @generated
	 */
	void setSequence(ExplicitGroup value);

	/**
	 * Returns the value of the '<em><b>Simple Content</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Simple Content</em>' containment reference.
	 * @see #setSimpleContent(SimpleContentType)
	 * @see org.w3._2001.schema.SchemaPackage#getDocumentRoot_SimpleContent()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='simpleContent' namespace='##targetNamespace'"
	 * @generated
	 */
	SimpleContentType getSimpleContent();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.DocumentRoot#getSimpleContent <em>Simple Content</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Simple Content</em>' containment reference.
	 * @see #getSimpleContent()
	 * @generated
	 */
	void setSimpleContent(SimpleContentType value);

	/**
	 * Returns the value of the '<em><b>Simple Type</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Simple Type</em>' containment reference.
	 * @see #setSimpleType(TopLevelSimpleType)
	 * @see org.w3._2001.schema.SchemaPackage#getDocumentRoot_SimpleType()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='simpleType' namespace='##targetNamespace'"
	 * @generated
	 */
	TopLevelSimpleType getSimpleType();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.DocumentRoot#getSimpleType <em>Simple Type</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Simple Type</em>' containment reference.
	 * @see #getSimpleType()
	 * @generated
	 */
	void setSimpleType(TopLevelSimpleType value);

	/**
	 * Returns the value of the '<em><b>Total Digits</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Total Digits</em>' containment reference.
	 * @see #setTotalDigits(TotalDigitsType)
	 * @see org.w3._2001.schema.SchemaPackage#getDocumentRoot_TotalDigits()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='totalDigits' namespace='##targetNamespace'"
	 * @generated
	 */
	TotalDigitsType getTotalDigits();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.DocumentRoot#getTotalDigits <em>Total Digits</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Total Digits</em>' containment reference.
	 * @see #getTotalDigits()
	 * @generated
	 */
	void setTotalDigits(TotalDigitsType value);

	/**
	 * Returns the value of the '<em><b>Union</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Union</em>' containment reference.
	 * @see #setUnion(UnionType)
	 * @see org.w3._2001.schema.SchemaPackage#getDocumentRoot_Union()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='union' namespace='##targetNamespace'"
	 * @generated
	 */
	UnionType getUnion();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.DocumentRoot#getUnion <em>Union</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Union</em>' containment reference.
	 * @see #getUnion()
	 * @generated
	 */
	void setUnion(UnionType value);

	/**
	 * Returns the value of the '<em><b>Unique</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Unique</em>' containment reference.
	 * @see #setUnique(Keybase)
	 * @see org.w3._2001.schema.SchemaPackage#getDocumentRoot_Unique()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='unique' namespace='##targetNamespace'"
	 * @generated
	 */
	Keybase getUnique();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.DocumentRoot#getUnique <em>Unique</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Unique</em>' containment reference.
	 * @see #getUnique()
	 * @generated
	 */
	void setUnique(Keybase value);

	/**
	 * Returns the value of the '<em><b>White Space</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>White Space</em>' containment reference.
	 * @see #setWhiteSpace(WhiteSpaceType)
	 * @see org.w3._2001.schema.SchemaPackage#getDocumentRoot_WhiteSpace()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='whiteSpace' namespace='##targetNamespace'"
	 * @generated
	 */
	WhiteSpaceType getWhiteSpace();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.DocumentRoot#getWhiteSpace <em>White Space</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>White Space</em>' containment reference.
	 * @see #getWhiteSpace()
	 * @generated
	 */
	void setWhiteSpace(WhiteSpaceType value);

} // DocumentRoot
