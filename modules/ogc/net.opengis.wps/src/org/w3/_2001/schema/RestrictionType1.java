/**
 */
package org.w3._2001.schema;

import javax.xml.namespace.QName;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Restriction Type1</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 *           base attribute and simpleType child are mutually
 *           exclusive, but one or other is required
 *         
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.w3._2001.schema.RestrictionType1#getSimpleType <em>Simple Type</em>}</li>
 *   <li>{@link org.w3._2001.schema.RestrictionType1#getFacets <em>Facets</em>}</li>
 *   <li>{@link org.w3._2001.schema.RestrictionType1#getMinExclusive <em>Min Exclusive</em>}</li>
 *   <li>{@link org.w3._2001.schema.RestrictionType1#getMinInclusive <em>Min Inclusive</em>}</li>
 *   <li>{@link org.w3._2001.schema.RestrictionType1#getMaxExclusive <em>Max Exclusive</em>}</li>
 *   <li>{@link org.w3._2001.schema.RestrictionType1#getMaxInclusive <em>Max Inclusive</em>}</li>
 *   <li>{@link org.w3._2001.schema.RestrictionType1#getTotalDigits <em>Total Digits</em>}</li>
 *   <li>{@link org.w3._2001.schema.RestrictionType1#getFractionDigits <em>Fraction Digits</em>}</li>
 *   <li>{@link org.w3._2001.schema.RestrictionType1#getLength <em>Length</em>}</li>
 *   <li>{@link org.w3._2001.schema.RestrictionType1#getMinLength <em>Min Length</em>}</li>
 *   <li>{@link org.w3._2001.schema.RestrictionType1#getMaxLength <em>Max Length</em>}</li>
 *   <li>{@link org.w3._2001.schema.RestrictionType1#getEnumeration <em>Enumeration</em>}</li>
 *   <li>{@link org.w3._2001.schema.RestrictionType1#getWhiteSpace <em>White Space</em>}</li>
 *   <li>{@link org.w3._2001.schema.RestrictionType1#getPattern <em>Pattern</em>}</li>
 *   <li>{@link org.w3._2001.schema.RestrictionType1#getBase <em>Base</em>}</li>
 * </ul>
 *
 * @see org.w3._2001.schema.SchemaPackage#getRestrictionType1()
 * @model extendedMetaData="name='restriction_._type' kind='elementOnly'"
 * @generated
 */
public interface RestrictionType1 extends Annotated {
	/**
	 * Returns the value of the '<em><b>Simple Type</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Simple Type</em>' containment reference.
	 * @see #setSimpleType(LocalSimpleType)
	 * @see org.w3._2001.schema.SchemaPackage#getRestrictionType1_SimpleType()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='simpleType' namespace='##targetNamespace'"
	 * @generated
	 */
	LocalSimpleType getSimpleType();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.RestrictionType1#getSimpleType <em>Simple Type</em>}' containment reference.
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
	 * @see org.w3._2001.schema.SchemaPackage#getRestrictionType1_Facets()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
	 *        extendedMetaData="kind='group' name='Facets:4'"
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
	 * @see org.w3._2001.schema.SchemaPackage#getRestrictionType1_MinExclusive()
	 * @model containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='minExclusive' namespace='##targetNamespace' group='#Facets:4'"
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
	 * @see org.w3._2001.schema.SchemaPackage#getRestrictionType1_MinInclusive()
	 * @model containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='minInclusive' namespace='##targetNamespace' group='#Facets:4'"
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
	 * @see org.w3._2001.schema.SchemaPackage#getRestrictionType1_MaxExclusive()
	 * @model containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='maxExclusive' namespace='##targetNamespace' group='#Facets:4'"
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
	 * @see org.w3._2001.schema.SchemaPackage#getRestrictionType1_MaxInclusive()
	 * @model containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='maxInclusive' namespace='##targetNamespace' group='#Facets:4'"
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
	 * @see org.w3._2001.schema.SchemaPackage#getRestrictionType1_TotalDigits()
	 * @model containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='totalDigits' namespace='##targetNamespace' group='#Facets:4'"
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
	 * @see org.w3._2001.schema.SchemaPackage#getRestrictionType1_FractionDigits()
	 * @model containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='fractionDigits' namespace='##targetNamespace' group='#Facets:4'"
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
	 * @see org.w3._2001.schema.SchemaPackage#getRestrictionType1_Length()
	 * @model containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='length' namespace='##targetNamespace' group='#Facets:4'"
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
	 * @see org.w3._2001.schema.SchemaPackage#getRestrictionType1_MinLength()
	 * @model containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='minLength' namespace='##targetNamespace' group='#Facets:4'"
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
	 * @see org.w3._2001.schema.SchemaPackage#getRestrictionType1_MaxLength()
	 * @model containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='maxLength' namespace='##targetNamespace' group='#Facets:4'"
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
	 * @see org.w3._2001.schema.SchemaPackage#getRestrictionType1_Enumeration()
	 * @model containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='enumeration' namespace='##targetNamespace' group='#Facets:4'"
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
	 * @see org.w3._2001.schema.SchemaPackage#getRestrictionType1_WhiteSpace()
	 * @model containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='whiteSpace' namespace='##targetNamespace' group='#Facets:4'"
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
	 * @see org.w3._2001.schema.SchemaPackage#getRestrictionType1_Pattern()
	 * @model containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='pattern' namespace='##targetNamespace' group='#Facets:4'"
	 * @generated
	 */
	EList<PatternType> getPattern();

	/**
	 * Returns the value of the '<em><b>Base</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base</em>' attribute.
	 * @see #setBase(QName)
	 * @see org.w3._2001.schema.SchemaPackage#getRestrictionType1_Base()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.QName"
	 *        extendedMetaData="kind='attribute' name='base'"
	 * @generated
	 */
	QName getBase();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.RestrictionType1#getBase <em>Base</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base</em>' attribute.
	 * @see #getBase()
	 * @generated
	 */
	void setBase(QName value);

} // RestrictionType1
