/**
 */
package org.w3._2001.schema;

import javax.xml.namespace.QName;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Restriction Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.w3._2001.schema.RestrictionType#getGroup <em>Group</em>}</li>
 *   <li>{@link org.w3._2001.schema.RestrictionType#getAll <em>All</em>}</li>
 *   <li>{@link org.w3._2001.schema.RestrictionType#getChoice <em>Choice</em>}</li>
 *   <li>{@link org.w3._2001.schema.RestrictionType#getSequence <em>Sequence</em>}</li>
 *   <li>{@link org.w3._2001.schema.RestrictionType#getSimpleType <em>Simple Type</em>}</li>
 *   <li>{@link org.w3._2001.schema.RestrictionType#getFacets <em>Facets</em>}</li>
 *   <li>{@link org.w3._2001.schema.RestrictionType#getMinExclusive <em>Min Exclusive</em>}</li>
 *   <li>{@link org.w3._2001.schema.RestrictionType#getMinInclusive <em>Min Inclusive</em>}</li>
 *   <li>{@link org.w3._2001.schema.RestrictionType#getMaxExclusive <em>Max Exclusive</em>}</li>
 *   <li>{@link org.w3._2001.schema.RestrictionType#getMaxInclusive <em>Max Inclusive</em>}</li>
 *   <li>{@link org.w3._2001.schema.RestrictionType#getTotalDigits <em>Total Digits</em>}</li>
 *   <li>{@link org.w3._2001.schema.RestrictionType#getFractionDigits <em>Fraction Digits</em>}</li>
 *   <li>{@link org.w3._2001.schema.RestrictionType#getLength <em>Length</em>}</li>
 *   <li>{@link org.w3._2001.schema.RestrictionType#getMinLength <em>Min Length</em>}</li>
 *   <li>{@link org.w3._2001.schema.RestrictionType#getMaxLength <em>Max Length</em>}</li>
 *   <li>{@link org.w3._2001.schema.RestrictionType#getEnumeration <em>Enumeration</em>}</li>
 *   <li>{@link org.w3._2001.schema.RestrictionType#getWhiteSpace <em>White Space</em>}</li>
 *   <li>{@link org.w3._2001.schema.RestrictionType#getPattern <em>Pattern</em>}</li>
 *   <li>{@link org.w3._2001.schema.RestrictionType#getGroup1 <em>Group1</em>}</li>
 *   <li>{@link org.w3._2001.schema.RestrictionType#getAttribute <em>Attribute</em>}</li>
 *   <li>{@link org.w3._2001.schema.RestrictionType#getAttributeGroup <em>Attribute Group</em>}</li>
 *   <li>{@link org.w3._2001.schema.RestrictionType#getAnyAttribute1 <em>Any Attribute1</em>}</li>
 *   <li>{@link org.w3._2001.schema.RestrictionType#getBase <em>Base</em>}</li>
 * </ul>
 *
 * @see org.w3._2001.schema.SchemaPackage#getRestrictionType()
 * @model extendedMetaData="name='restrictionType' kind='elementOnly'"
 * @generated
 */
public interface RestrictionType extends Annotated {
	/**
	 * Returns the value of the '<em><b>Group</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Group</em>' containment reference.
	 * @see #setGroup(GroupRef)
	 * @see org.w3._2001.schema.SchemaPackage#getRestrictionType_Group()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='group' namespace='##targetNamespace'"
	 * @generated
	 */
	GroupRef getGroup();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.RestrictionType#getGroup <em>Group</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Group</em>' containment reference.
	 * @see #getGroup()
	 * @generated
	 */
	void setGroup(GroupRef value);

	/**
	 * Returns the value of the '<em><b>All</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>All</em>' containment reference.
	 * @see #setAll(All)
	 * @see org.w3._2001.schema.SchemaPackage#getRestrictionType_All()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='all' namespace='##targetNamespace'"
	 * @generated
	 */
	All getAll();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.RestrictionType#getAll <em>All</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>All</em>' containment reference.
	 * @see #getAll()
	 * @generated
	 */
	void setAll(All value);

	/**
	 * Returns the value of the '<em><b>Choice</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Choice</em>' containment reference.
	 * @see #setChoice(ExplicitGroup)
	 * @see org.w3._2001.schema.SchemaPackage#getRestrictionType_Choice()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='choice' namespace='##targetNamespace'"
	 * @generated
	 */
	ExplicitGroup getChoice();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.RestrictionType#getChoice <em>Choice</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Choice</em>' containment reference.
	 * @see #getChoice()
	 * @generated
	 */
	void setChoice(ExplicitGroup value);

	/**
	 * Returns the value of the '<em><b>Sequence</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Sequence</em>' containment reference.
	 * @see #setSequence(ExplicitGroup)
	 * @see org.w3._2001.schema.SchemaPackage#getRestrictionType_Sequence()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='sequence' namespace='##targetNamespace'"
	 * @generated
	 */
	ExplicitGroup getSequence();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.RestrictionType#getSequence <em>Sequence</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Sequence</em>' containment reference.
	 * @see #getSequence()
	 * @generated
	 */
	void setSequence(ExplicitGroup value);

	/**
	 * Returns the value of the '<em><b>Simple Type</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Simple Type</em>' containment reference.
	 * @see #setSimpleType(LocalSimpleType)
	 * @see org.w3._2001.schema.SchemaPackage#getRestrictionType_SimpleType()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='simpleType' namespace='##targetNamespace'"
	 * @generated
	 */
	LocalSimpleType getSimpleType();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.RestrictionType#getSimpleType <em>Simple Type</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Simple Type</em>' containment reference.
	 * @see #getSimpleType()
	 * @generated
	 */
	void setSimpleType(LocalSimpleType value);

	/**
	 * Returns the value of the '<em><b>Facets</b></em>' attribute list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Facets</em>' attribute list.
	 * @see org.w3._2001.schema.SchemaPackage#getRestrictionType_Facets()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
	 *        extendedMetaData="kind='group' name='Facets:8'"
	 * @generated
	 */
	FeatureMap getFacets();

	/**
	 * Returns the value of the '<em><b>Min Exclusive</b></em>' containment reference list.
	 * The list contents are of type {@link org.w3._2001.schema.Facet}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Min Exclusive</em>' containment reference list.
	 * @see org.w3._2001.schema.SchemaPackage#getRestrictionType_MinExclusive()
	 * @model containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='minExclusive' namespace='##targetNamespace' group='#Facets:8'"
	 * @generated
	 */
	EList<Facet> getMinExclusive();

	/**
	 * Returns the value of the '<em><b>Min Inclusive</b></em>' containment reference list.
	 * The list contents are of type {@link org.w3._2001.schema.Facet}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Min Inclusive</em>' containment reference list.
	 * @see org.w3._2001.schema.SchemaPackage#getRestrictionType_MinInclusive()
	 * @model containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='minInclusive' namespace='##targetNamespace' group='#Facets:8'"
	 * @generated
	 */
	EList<Facet> getMinInclusive();

	/**
	 * Returns the value of the '<em><b>Max Exclusive</b></em>' containment reference list.
	 * The list contents are of type {@link org.w3._2001.schema.Facet}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Max Exclusive</em>' containment reference list.
	 * @see org.w3._2001.schema.SchemaPackage#getRestrictionType_MaxExclusive()
	 * @model containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='maxExclusive' namespace='##targetNamespace' group='#Facets:8'"
	 * @generated
	 */
	EList<Facet> getMaxExclusive();

	/**
	 * Returns the value of the '<em><b>Max Inclusive</b></em>' containment reference list.
	 * The list contents are of type {@link org.w3._2001.schema.Facet}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Max Inclusive</em>' containment reference list.
	 * @see org.w3._2001.schema.SchemaPackage#getRestrictionType_MaxInclusive()
	 * @model containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='maxInclusive' namespace='##targetNamespace' group='#Facets:8'"
	 * @generated
	 */
	EList<Facet> getMaxInclusive();

	/**
	 * Returns the value of the '<em><b>Total Digits</b></em>' containment reference list.
	 * The list contents are of type {@link org.w3._2001.schema.TotalDigitsType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Total Digits</em>' containment reference list.
	 * @see org.w3._2001.schema.SchemaPackage#getRestrictionType_TotalDigits()
	 * @model containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='totalDigits' namespace='##targetNamespace' group='#Facets:8'"
	 * @generated
	 */
	EList<TotalDigitsType> getTotalDigits();

	/**
	 * Returns the value of the '<em><b>Fraction Digits</b></em>' containment reference list.
	 * The list contents are of type {@link org.w3._2001.schema.NumFacet}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Fraction Digits</em>' containment reference list.
	 * @see org.w3._2001.schema.SchemaPackage#getRestrictionType_FractionDigits()
	 * @model containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='fractionDigits' namespace='##targetNamespace' group='#Facets:8'"
	 * @generated
	 */
	EList<NumFacet> getFractionDigits();

	/**
	 * Returns the value of the '<em><b>Length</b></em>' containment reference list.
	 * The list contents are of type {@link org.w3._2001.schema.NumFacet}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Length</em>' containment reference list.
	 * @see org.w3._2001.schema.SchemaPackage#getRestrictionType_Length()
	 * @model containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='length' namespace='##targetNamespace' group='#Facets:8'"
	 * @generated
	 */
	EList<NumFacet> getLength();

	/**
	 * Returns the value of the '<em><b>Min Length</b></em>' containment reference list.
	 * The list contents are of type {@link org.w3._2001.schema.NumFacet}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Min Length</em>' containment reference list.
	 * @see org.w3._2001.schema.SchemaPackage#getRestrictionType_MinLength()
	 * @model containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='minLength' namespace='##targetNamespace' group='#Facets:8'"
	 * @generated
	 */
	EList<NumFacet> getMinLength();

	/**
	 * Returns the value of the '<em><b>Max Length</b></em>' containment reference list.
	 * The list contents are of type {@link org.w3._2001.schema.NumFacet}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Max Length</em>' containment reference list.
	 * @see org.w3._2001.schema.SchemaPackage#getRestrictionType_MaxLength()
	 * @model containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='maxLength' namespace='##targetNamespace' group='#Facets:8'"
	 * @generated
	 */
	EList<NumFacet> getMaxLength();

	/**
	 * Returns the value of the '<em><b>Enumeration</b></em>' containment reference list.
	 * The list contents are of type {@link org.w3._2001.schema.NoFixedFacet}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Enumeration</em>' containment reference list.
	 * @see org.w3._2001.schema.SchemaPackage#getRestrictionType_Enumeration()
	 * @model containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='enumeration' namespace='##targetNamespace' group='#Facets:8'"
	 * @generated
	 */
	EList<NoFixedFacet> getEnumeration();

	/**
	 * Returns the value of the '<em><b>White Space</b></em>' containment reference list.
	 * The list contents are of type {@link org.w3._2001.schema.WhiteSpaceType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>White Space</em>' containment reference list.
	 * @see org.w3._2001.schema.SchemaPackage#getRestrictionType_WhiteSpace()
	 * @model containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='whiteSpace' namespace='##targetNamespace' group='#Facets:8'"
	 * @generated
	 */
	EList<WhiteSpaceType> getWhiteSpace();

	/**
	 * Returns the value of the '<em><b>Pattern</b></em>' containment reference list.
	 * The list contents are of type {@link org.w3._2001.schema.PatternType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Pattern</em>' containment reference list.
	 * @see org.w3._2001.schema.SchemaPackage#getRestrictionType_Pattern()
	 * @model containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='pattern' namespace='##targetNamespace' group='#Facets:8'"
	 * @generated
	 */
	EList<PatternType> getPattern();

	/**
	 * Returns the value of the '<em><b>Group1</b></em>' attribute list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Group1</em>' attribute list.
	 * @see org.w3._2001.schema.SchemaPackage#getRestrictionType_Group1()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
	 *        extendedMetaData="kind='group' name='group:21'"
	 * @generated
	 */
	FeatureMap getGroup1();

	/**
	 * Returns the value of the '<em><b>Attribute</b></em>' containment reference list.
	 * The list contents are of type {@link org.w3._2001.schema.Attribute}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Attribute</em>' containment reference list.
	 * @see org.w3._2001.schema.SchemaPackage#getRestrictionType_Attribute()
	 * @model containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='attribute' namespace='##targetNamespace' group='#group:21'"
	 * @generated
	 */
	EList<Attribute> getAttribute();

	/**
	 * Returns the value of the '<em><b>Attribute Group</b></em>' containment reference list.
	 * The list contents are of type {@link org.w3._2001.schema.AttributeGroupRef}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Attribute Group</em>' containment reference list.
	 * @see org.w3._2001.schema.SchemaPackage#getRestrictionType_AttributeGroup()
	 * @model containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='attributeGroup' namespace='##targetNamespace' group='#group:21'"
	 * @generated
	 */
	EList<AttributeGroupRef> getAttributeGroup();

	/**
	 * Returns the value of the '<em><b>Any Attribute1</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Any Attribute1</em>' containment reference.
	 * @see #setAnyAttribute1(Wildcard)
	 * @see org.w3._2001.schema.SchemaPackage#getRestrictionType_AnyAttribute1()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='anyAttribute' namespace='##targetNamespace'"
	 * @generated
	 */
	Wildcard getAnyAttribute1();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.RestrictionType#getAnyAttribute1 <em>Any Attribute1</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Any Attribute1</em>' containment reference.
	 * @see #getAnyAttribute1()
	 * @generated
	 */
	void setAnyAttribute1(Wildcard value);

	/**
	 * Returns the value of the '<em><b>Base</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base</em>' attribute.
	 * @see #setBase(QName)
	 * @see org.w3._2001.schema.SchemaPackage#getRestrictionType_Base()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.QName" required="true"
	 *        extendedMetaData="kind='attribute' name='base'"
	 * @generated
	 */
	QName getBase();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.RestrictionType#getBase <em>Base</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base</em>' attribute.
	 * @see #getBase()
	 * @generated
	 */
	void setBase(QName value);

} // RestrictionType
