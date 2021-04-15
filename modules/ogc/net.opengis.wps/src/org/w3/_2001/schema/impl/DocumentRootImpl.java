/**
 */
package org.w3._2001.schema.impl;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.EStringToStringMapEntryImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

import org.w3._2001.schema.All;
import org.w3._2001.schema.AnnotationType;
import org.w3._2001.schema.AnyType;
import org.w3._2001.schema.AppinfoType;
import org.w3._2001.schema.ComplexContentType;
import org.w3._2001.schema.DocumentRoot;
import org.w3._2001.schema.DocumentationType;
import org.w3._2001.schema.ExplicitGroup;
import org.w3._2001.schema.Facet;
import org.w3._2001.schema.FieldType;
import org.w3._2001.schema.ImportType;
import org.w3._2001.schema.IncludeType;
import org.w3._2001.schema.Keybase;
import org.w3._2001.schema.KeyrefType;
import org.w3._2001.schema.ListType;
import org.w3._2001.schema.NamedAttributeGroup;
import org.w3._2001.schema.NamedGroup;
import org.w3._2001.schema.NoFixedFacet;
import org.w3._2001.schema.NotationType;
import org.w3._2001.schema.NumFacet;
import org.w3._2001.schema.PatternType;
import org.w3._2001.schema.RedefineType;
import org.w3._2001.schema.RestrictionType1;
import org.w3._2001.schema.SchemaPackage;
import org.w3._2001.schema.SchemaType;
import org.w3._2001.schema.SelectorType;
import org.w3._2001.schema.SimpleContentType;
import org.w3._2001.schema.TopLevelAttribute;
import org.w3._2001.schema.TopLevelComplexType;
import org.w3._2001.schema.TopLevelElement;
import org.w3._2001.schema.TopLevelSimpleType;
import org.w3._2001.schema.TotalDigitsType;
import org.w3._2001.schema.UnionType;
import org.w3._2001.schema.WhiteSpaceType;
import org.w3._2001.schema.Wildcard;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Document Root</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.w3._2001.schema.impl.DocumentRootImpl#getMixed <em>Mixed</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.DocumentRootImpl#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.DocumentRootImpl#getXSISchemaLocation <em>XSI Schema Location</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.DocumentRootImpl#getAll <em>All</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.DocumentRootImpl#getAnnotation <em>Annotation</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.DocumentRootImpl#getAny <em>Any</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.DocumentRootImpl#getAnyAttribute <em>Any Attribute</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.DocumentRootImpl#getAppinfo <em>Appinfo</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.DocumentRootImpl#getAttribute <em>Attribute</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.DocumentRootImpl#getAttributeGroup <em>Attribute Group</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.DocumentRootImpl#getChoice <em>Choice</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.DocumentRootImpl#getComplexContent <em>Complex Content</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.DocumentRootImpl#getComplexType <em>Complex Type</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.DocumentRootImpl#getDocumentation <em>Documentation</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.DocumentRootImpl#getElement <em>Element</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.DocumentRootImpl#getEnumeration <em>Enumeration</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.DocumentRootImpl#getField <em>Field</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.DocumentRootImpl#getFractionDigits <em>Fraction Digits</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.DocumentRootImpl#getGroup <em>Group</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.DocumentRootImpl#getImport <em>Import</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.DocumentRootImpl#getInclude <em>Include</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.DocumentRootImpl#getKey <em>Key</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.DocumentRootImpl#getKeyref <em>Keyref</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.DocumentRootImpl#getLength <em>Length</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.DocumentRootImpl#getList <em>List</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.DocumentRootImpl#getMaxExclusive <em>Max Exclusive</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.DocumentRootImpl#getMaxInclusive <em>Max Inclusive</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.DocumentRootImpl#getMaxLength <em>Max Length</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.DocumentRootImpl#getMinExclusive <em>Min Exclusive</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.DocumentRootImpl#getMinInclusive <em>Min Inclusive</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.DocumentRootImpl#getMinLength <em>Min Length</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.DocumentRootImpl#getNotation <em>Notation</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.DocumentRootImpl#getPattern <em>Pattern</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.DocumentRootImpl#getRedefine <em>Redefine</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.DocumentRootImpl#getRestriction <em>Restriction</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.DocumentRootImpl#getSchema <em>Schema</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.DocumentRootImpl#getSelector <em>Selector</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.DocumentRootImpl#getSequence <em>Sequence</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.DocumentRootImpl#getSimpleContent <em>Simple Content</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.DocumentRootImpl#getSimpleType <em>Simple Type</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.DocumentRootImpl#getTotalDigits <em>Total Digits</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.DocumentRootImpl#getUnion <em>Union</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.DocumentRootImpl#getUnique <em>Unique</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.DocumentRootImpl#getWhiteSpace <em>White Space</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DocumentRootImpl extends MinimalEObjectImpl.Container implements DocumentRoot {
	/**
	 * The cached value of the '{@link #getMixed() <em>Mixed</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMixed()
	 * @generated
	 * @ordered
	 */
	protected FeatureMap mixed;

	/**
	 * The cached value of the '{@link #getXMLNSPrefixMap() <em>XMLNS Prefix Map</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getXMLNSPrefixMap()
	 * @generated
	 * @ordered
	 */
	protected EMap<String, String> xMLNSPrefixMap;

	/**
	 * The cached value of the '{@link #getXSISchemaLocation() <em>XSI Schema Location</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getXSISchemaLocation()
	 * @generated
	 * @ordered
	 */
	protected EMap<String, String> xSISchemaLocation;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DocumentRootImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchemaPackage.Literals.DOCUMENT_ROOT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FeatureMap getMixed() {
		if (mixed == null) {
			mixed = new BasicFeatureMap(this, SchemaPackage.DOCUMENT_ROOT__MIXED);
		}
		return mixed;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EMap<String, String> getXMLNSPrefixMap() {
		if (xMLNSPrefixMap == null) {
			xMLNSPrefixMap = new EcoreEMap<String,String>(EcorePackage.Literals.ESTRING_TO_STRING_MAP_ENTRY, EStringToStringMapEntryImpl.class, this, SchemaPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP);
		}
		return xMLNSPrefixMap;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EMap<String, String> getXSISchemaLocation() {
		if (xSISchemaLocation == null) {
			xSISchemaLocation = new EcoreEMap<String,String>(EcorePackage.Literals.ESTRING_TO_STRING_MAP_ENTRY, EStringToStringMapEntryImpl.class, this, SchemaPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION);
		}
		return xSISchemaLocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public All getAll() {
		return (All)getMixed().get(SchemaPackage.Literals.DOCUMENT_ROOT__ALL, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetAll(All newAll, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(SchemaPackage.Literals.DOCUMENT_ROOT__ALL, newAll, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAll(All newAll) {
		((FeatureMap.Internal)getMixed()).set(SchemaPackage.Literals.DOCUMENT_ROOT__ALL, newAll);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AnnotationType getAnnotation() {
		return (AnnotationType)getMixed().get(SchemaPackage.Literals.DOCUMENT_ROOT__ANNOTATION, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetAnnotation(AnnotationType newAnnotation, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(SchemaPackage.Literals.DOCUMENT_ROOT__ANNOTATION, newAnnotation, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAnnotation(AnnotationType newAnnotation) {
		((FeatureMap.Internal)getMixed()).set(SchemaPackage.Literals.DOCUMENT_ROOT__ANNOTATION, newAnnotation);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AnyType getAny() {
		return (AnyType)getMixed().get(SchemaPackage.Literals.DOCUMENT_ROOT__ANY, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetAny(AnyType newAny, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(SchemaPackage.Literals.DOCUMENT_ROOT__ANY, newAny, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAny(AnyType newAny) {
		((FeatureMap.Internal)getMixed()).set(SchemaPackage.Literals.DOCUMENT_ROOT__ANY, newAny);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Wildcard getAnyAttribute() {
		return (Wildcard)getMixed().get(SchemaPackage.Literals.DOCUMENT_ROOT__ANY_ATTRIBUTE, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetAnyAttribute(Wildcard newAnyAttribute, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(SchemaPackage.Literals.DOCUMENT_ROOT__ANY_ATTRIBUTE, newAnyAttribute, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAnyAttribute(Wildcard newAnyAttribute) {
		((FeatureMap.Internal)getMixed()).set(SchemaPackage.Literals.DOCUMENT_ROOT__ANY_ATTRIBUTE, newAnyAttribute);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AppinfoType getAppinfo() {
		return (AppinfoType)getMixed().get(SchemaPackage.Literals.DOCUMENT_ROOT__APPINFO, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetAppinfo(AppinfoType newAppinfo, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(SchemaPackage.Literals.DOCUMENT_ROOT__APPINFO, newAppinfo, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAppinfo(AppinfoType newAppinfo) {
		((FeatureMap.Internal)getMixed()).set(SchemaPackage.Literals.DOCUMENT_ROOT__APPINFO, newAppinfo);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TopLevelAttribute getAttribute() {
		return (TopLevelAttribute)getMixed().get(SchemaPackage.Literals.DOCUMENT_ROOT__ATTRIBUTE, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetAttribute(TopLevelAttribute newAttribute, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(SchemaPackage.Literals.DOCUMENT_ROOT__ATTRIBUTE, newAttribute, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAttribute(TopLevelAttribute newAttribute) {
		((FeatureMap.Internal)getMixed()).set(SchemaPackage.Literals.DOCUMENT_ROOT__ATTRIBUTE, newAttribute);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NamedAttributeGroup getAttributeGroup() {
		return (NamedAttributeGroup)getMixed().get(SchemaPackage.Literals.DOCUMENT_ROOT__ATTRIBUTE_GROUP, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetAttributeGroup(NamedAttributeGroup newAttributeGroup, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(SchemaPackage.Literals.DOCUMENT_ROOT__ATTRIBUTE_GROUP, newAttributeGroup, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAttributeGroup(NamedAttributeGroup newAttributeGroup) {
		((FeatureMap.Internal)getMixed()).set(SchemaPackage.Literals.DOCUMENT_ROOT__ATTRIBUTE_GROUP, newAttributeGroup);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ExplicitGroup getChoice() {
		return (ExplicitGroup)getMixed().get(SchemaPackage.Literals.DOCUMENT_ROOT__CHOICE, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetChoice(ExplicitGroup newChoice, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(SchemaPackage.Literals.DOCUMENT_ROOT__CHOICE, newChoice, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setChoice(ExplicitGroup newChoice) {
		((FeatureMap.Internal)getMixed()).set(SchemaPackage.Literals.DOCUMENT_ROOT__CHOICE, newChoice);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ComplexContentType getComplexContent() {
		return (ComplexContentType)getMixed().get(SchemaPackage.Literals.DOCUMENT_ROOT__COMPLEX_CONTENT, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetComplexContent(ComplexContentType newComplexContent, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(SchemaPackage.Literals.DOCUMENT_ROOT__COMPLEX_CONTENT, newComplexContent, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setComplexContent(ComplexContentType newComplexContent) {
		((FeatureMap.Internal)getMixed()).set(SchemaPackage.Literals.DOCUMENT_ROOT__COMPLEX_CONTENT, newComplexContent);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TopLevelComplexType getComplexType() {
		return (TopLevelComplexType)getMixed().get(SchemaPackage.Literals.DOCUMENT_ROOT__COMPLEX_TYPE, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetComplexType(TopLevelComplexType newComplexType, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(SchemaPackage.Literals.DOCUMENT_ROOT__COMPLEX_TYPE, newComplexType, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setComplexType(TopLevelComplexType newComplexType) {
		((FeatureMap.Internal)getMixed()).set(SchemaPackage.Literals.DOCUMENT_ROOT__COMPLEX_TYPE, newComplexType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DocumentationType getDocumentation() {
		return (DocumentationType)getMixed().get(SchemaPackage.Literals.DOCUMENT_ROOT__DOCUMENTATION, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetDocumentation(DocumentationType newDocumentation, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(SchemaPackage.Literals.DOCUMENT_ROOT__DOCUMENTATION, newDocumentation, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDocumentation(DocumentationType newDocumentation) {
		((FeatureMap.Internal)getMixed()).set(SchemaPackage.Literals.DOCUMENT_ROOT__DOCUMENTATION, newDocumentation);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TopLevelElement getElement() {
		return (TopLevelElement)getMixed().get(SchemaPackage.Literals.DOCUMENT_ROOT__ELEMENT, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetElement(TopLevelElement newElement, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(SchemaPackage.Literals.DOCUMENT_ROOT__ELEMENT, newElement, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setElement(TopLevelElement newElement) {
		((FeatureMap.Internal)getMixed()).set(SchemaPackage.Literals.DOCUMENT_ROOT__ELEMENT, newElement);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NoFixedFacet getEnumeration() {
		return (NoFixedFacet)getMixed().get(SchemaPackage.Literals.DOCUMENT_ROOT__ENUMERATION, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetEnumeration(NoFixedFacet newEnumeration, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(SchemaPackage.Literals.DOCUMENT_ROOT__ENUMERATION, newEnumeration, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEnumeration(NoFixedFacet newEnumeration) {
		((FeatureMap.Internal)getMixed()).set(SchemaPackage.Literals.DOCUMENT_ROOT__ENUMERATION, newEnumeration);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FieldType getField() {
		return (FieldType)getMixed().get(SchemaPackage.Literals.DOCUMENT_ROOT__FIELD, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetField(FieldType newField, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(SchemaPackage.Literals.DOCUMENT_ROOT__FIELD, newField, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setField(FieldType newField) {
		((FeatureMap.Internal)getMixed()).set(SchemaPackage.Literals.DOCUMENT_ROOT__FIELD, newField);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NumFacet getFractionDigits() {
		return (NumFacet)getMixed().get(SchemaPackage.Literals.DOCUMENT_ROOT__FRACTION_DIGITS, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetFractionDigits(NumFacet newFractionDigits, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(SchemaPackage.Literals.DOCUMENT_ROOT__FRACTION_DIGITS, newFractionDigits, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFractionDigits(NumFacet newFractionDigits) {
		((FeatureMap.Internal)getMixed()).set(SchemaPackage.Literals.DOCUMENT_ROOT__FRACTION_DIGITS, newFractionDigits);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NamedGroup getGroup() {
		return (NamedGroup)getMixed().get(SchemaPackage.Literals.DOCUMENT_ROOT__GROUP, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetGroup(NamedGroup newGroup, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(SchemaPackage.Literals.DOCUMENT_ROOT__GROUP, newGroup, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setGroup(NamedGroup newGroup) {
		((FeatureMap.Internal)getMixed()).set(SchemaPackage.Literals.DOCUMENT_ROOT__GROUP, newGroup);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ImportType getImport() {
		return (ImportType)getMixed().get(SchemaPackage.Literals.DOCUMENT_ROOT__IMPORT, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetImport(ImportType newImport, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(SchemaPackage.Literals.DOCUMENT_ROOT__IMPORT, newImport, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setImport(ImportType newImport) {
		((FeatureMap.Internal)getMixed()).set(SchemaPackage.Literals.DOCUMENT_ROOT__IMPORT, newImport);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IncludeType getInclude() {
		return (IncludeType)getMixed().get(SchemaPackage.Literals.DOCUMENT_ROOT__INCLUDE, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetInclude(IncludeType newInclude, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(SchemaPackage.Literals.DOCUMENT_ROOT__INCLUDE, newInclude, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setInclude(IncludeType newInclude) {
		((FeatureMap.Internal)getMixed()).set(SchemaPackage.Literals.DOCUMENT_ROOT__INCLUDE, newInclude);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Keybase getKey() {
		return (Keybase)getMixed().get(SchemaPackage.Literals.DOCUMENT_ROOT__KEY, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetKey(Keybase newKey, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(SchemaPackage.Literals.DOCUMENT_ROOT__KEY, newKey, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setKey(Keybase newKey) {
		((FeatureMap.Internal)getMixed()).set(SchemaPackage.Literals.DOCUMENT_ROOT__KEY, newKey);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public KeyrefType getKeyref() {
		return (KeyrefType)getMixed().get(SchemaPackage.Literals.DOCUMENT_ROOT__KEYREF, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetKeyref(KeyrefType newKeyref, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(SchemaPackage.Literals.DOCUMENT_ROOT__KEYREF, newKeyref, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setKeyref(KeyrefType newKeyref) {
		((FeatureMap.Internal)getMixed()).set(SchemaPackage.Literals.DOCUMENT_ROOT__KEYREF, newKeyref);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NumFacet getLength() {
		return (NumFacet)getMixed().get(SchemaPackage.Literals.DOCUMENT_ROOT__LENGTH, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetLength(NumFacet newLength, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(SchemaPackage.Literals.DOCUMENT_ROOT__LENGTH, newLength, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLength(NumFacet newLength) {
		((FeatureMap.Internal)getMixed()).set(SchemaPackage.Literals.DOCUMENT_ROOT__LENGTH, newLength);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ListType getList() {
		return (ListType)getMixed().get(SchemaPackage.Literals.DOCUMENT_ROOT__LIST, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetList(ListType newList, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(SchemaPackage.Literals.DOCUMENT_ROOT__LIST, newList, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setList(ListType newList) {
		((FeatureMap.Internal)getMixed()).set(SchemaPackage.Literals.DOCUMENT_ROOT__LIST, newList);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Facet getMaxExclusive() {
		return (Facet)getMixed().get(SchemaPackage.Literals.DOCUMENT_ROOT__MAX_EXCLUSIVE, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetMaxExclusive(Facet newMaxExclusive, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(SchemaPackage.Literals.DOCUMENT_ROOT__MAX_EXCLUSIVE, newMaxExclusive, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMaxExclusive(Facet newMaxExclusive) {
		((FeatureMap.Internal)getMixed()).set(SchemaPackage.Literals.DOCUMENT_ROOT__MAX_EXCLUSIVE, newMaxExclusive);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Facet getMaxInclusive() {
		return (Facet)getMixed().get(SchemaPackage.Literals.DOCUMENT_ROOT__MAX_INCLUSIVE, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetMaxInclusive(Facet newMaxInclusive, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(SchemaPackage.Literals.DOCUMENT_ROOT__MAX_INCLUSIVE, newMaxInclusive, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMaxInclusive(Facet newMaxInclusive) {
		((FeatureMap.Internal)getMixed()).set(SchemaPackage.Literals.DOCUMENT_ROOT__MAX_INCLUSIVE, newMaxInclusive);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NumFacet getMaxLength() {
		return (NumFacet)getMixed().get(SchemaPackage.Literals.DOCUMENT_ROOT__MAX_LENGTH, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetMaxLength(NumFacet newMaxLength, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(SchemaPackage.Literals.DOCUMENT_ROOT__MAX_LENGTH, newMaxLength, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMaxLength(NumFacet newMaxLength) {
		((FeatureMap.Internal)getMixed()).set(SchemaPackage.Literals.DOCUMENT_ROOT__MAX_LENGTH, newMaxLength);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Facet getMinExclusive() {
		return (Facet)getMixed().get(SchemaPackage.Literals.DOCUMENT_ROOT__MIN_EXCLUSIVE, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetMinExclusive(Facet newMinExclusive, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(SchemaPackage.Literals.DOCUMENT_ROOT__MIN_EXCLUSIVE, newMinExclusive, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMinExclusive(Facet newMinExclusive) {
		((FeatureMap.Internal)getMixed()).set(SchemaPackage.Literals.DOCUMENT_ROOT__MIN_EXCLUSIVE, newMinExclusive);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Facet getMinInclusive() {
		return (Facet)getMixed().get(SchemaPackage.Literals.DOCUMENT_ROOT__MIN_INCLUSIVE, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetMinInclusive(Facet newMinInclusive, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(SchemaPackage.Literals.DOCUMENT_ROOT__MIN_INCLUSIVE, newMinInclusive, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMinInclusive(Facet newMinInclusive) {
		((FeatureMap.Internal)getMixed()).set(SchemaPackage.Literals.DOCUMENT_ROOT__MIN_INCLUSIVE, newMinInclusive);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NumFacet getMinLength() {
		return (NumFacet)getMixed().get(SchemaPackage.Literals.DOCUMENT_ROOT__MIN_LENGTH, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetMinLength(NumFacet newMinLength, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(SchemaPackage.Literals.DOCUMENT_ROOT__MIN_LENGTH, newMinLength, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMinLength(NumFacet newMinLength) {
		((FeatureMap.Internal)getMixed()).set(SchemaPackage.Literals.DOCUMENT_ROOT__MIN_LENGTH, newMinLength);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotationType getNotation() {
		return (NotationType)getMixed().get(SchemaPackage.Literals.DOCUMENT_ROOT__NOTATION, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetNotation(NotationType newNotation, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(SchemaPackage.Literals.DOCUMENT_ROOT__NOTATION, newNotation, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNotation(NotationType newNotation) {
		((FeatureMap.Internal)getMixed()).set(SchemaPackage.Literals.DOCUMENT_ROOT__NOTATION, newNotation);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PatternType getPattern() {
		return (PatternType)getMixed().get(SchemaPackage.Literals.DOCUMENT_ROOT__PATTERN, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetPattern(PatternType newPattern, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(SchemaPackage.Literals.DOCUMENT_ROOT__PATTERN, newPattern, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPattern(PatternType newPattern) {
		((FeatureMap.Internal)getMixed()).set(SchemaPackage.Literals.DOCUMENT_ROOT__PATTERN, newPattern);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RedefineType getRedefine() {
		return (RedefineType)getMixed().get(SchemaPackage.Literals.DOCUMENT_ROOT__REDEFINE, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetRedefine(RedefineType newRedefine, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(SchemaPackage.Literals.DOCUMENT_ROOT__REDEFINE, newRedefine, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRedefine(RedefineType newRedefine) {
		((FeatureMap.Internal)getMixed()).set(SchemaPackage.Literals.DOCUMENT_ROOT__REDEFINE, newRedefine);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RestrictionType1 getRestriction() {
		return (RestrictionType1)getMixed().get(SchemaPackage.Literals.DOCUMENT_ROOT__RESTRICTION, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetRestriction(RestrictionType1 newRestriction, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(SchemaPackage.Literals.DOCUMENT_ROOT__RESTRICTION, newRestriction, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRestriction(RestrictionType1 newRestriction) {
		((FeatureMap.Internal)getMixed()).set(SchemaPackage.Literals.DOCUMENT_ROOT__RESTRICTION, newRestriction);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SchemaType getSchema() {
		return (SchemaType)getMixed().get(SchemaPackage.Literals.DOCUMENT_ROOT__SCHEMA, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSchema(SchemaType newSchema, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(SchemaPackage.Literals.DOCUMENT_ROOT__SCHEMA, newSchema, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSchema(SchemaType newSchema) {
		((FeatureMap.Internal)getMixed()).set(SchemaPackage.Literals.DOCUMENT_ROOT__SCHEMA, newSchema);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SelectorType getSelector() {
		return (SelectorType)getMixed().get(SchemaPackage.Literals.DOCUMENT_ROOT__SELECTOR, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSelector(SelectorType newSelector, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(SchemaPackage.Literals.DOCUMENT_ROOT__SELECTOR, newSelector, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSelector(SelectorType newSelector) {
		((FeatureMap.Internal)getMixed()).set(SchemaPackage.Literals.DOCUMENT_ROOT__SELECTOR, newSelector);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ExplicitGroup getSequence() {
		return (ExplicitGroup)getMixed().get(SchemaPackage.Literals.DOCUMENT_ROOT__SEQUENCE, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSequence(ExplicitGroup newSequence, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(SchemaPackage.Literals.DOCUMENT_ROOT__SEQUENCE, newSequence, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSequence(ExplicitGroup newSequence) {
		((FeatureMap.Internal)getMixed()).set(SchemaPackage.Literals.DOCUMENT_ROOT__SEQUENCE, newSequence);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SimpleContentType getSimpleContent() {
		return (SimpleContentType)getMixed().get(SchemaPackage.Literals.DOCUMENT_ROOT__SIMPLE_CONTENT, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSimpleContent(SimpleContentType newSimpleContent, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(SchemaPackage.Literals.DOCUMENT_ROOT__SIMPLE_CONTENT, newSimpleContent, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSimpleContent(SimpleContentType newSimpleContent) {
		((FeatureMap.Internal)getMixed()).set(SchemaPackage.Literals.DOCUMENT_ROOT__SIMPLE_CONTENT, newSimpleContent);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TopLevelSimpleType getSimpleType() {
		return (TopLevelSimpleType)getMixed().get(SchemaPackage.Literals.DOCUMENT_ROOT__SIMPLE_TYPE, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSimpleType(TopLevelSimpleType newSimpleType, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(SchemaPackage.Literals.DOCUMENT_ROOT__SIMPLE_TYPE, newSimpleType, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSimpleType(TopLevelSimpleType newSimpleType) {
		((FeatureMap.Internal)getMixed()).set(SchemaPackage.Literals.DOCUMENT_ROOT__SIMPLE_TYPE, newSimpleType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TotalDigitsType getTotalDigits() {
		return (TotalDigitsType)getMixed().get(SchemaPackage.Literals.DOCUMENT_ROOT__TOTAL_DIGITS, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetTotalDigits(TotalDigitsType newTotalDigits, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(SchemaPackage.Literals.DOCUMENT_ROOT__TOTAL_DIGITS, newTotalDigits, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTotalDigits(TotalDigitsType newTotalDigits) {
		((FeatureMap.Internal)getMixed()).set(SchemaPackage.Literals.DOCUMENT_ROOT__TOTAL_DIGITS, newTotalDigits);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UnionType getUnion() {
		return (UnionType)getMixed().get(SchemaPackage.Literals.DOCUMENT_ROOT__UNION, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetUnion(UnionType newUnion, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(SchemaPackage.Literals.DOCUMENT_ROOT__UNION, newUnion, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUnion(UnionType newUnion) {
		((FeatureMap.Internal)getMixed()).set(SchemaPackage.Literals.DOCUMENT_ROOT__UNION, newUnion);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Keybase getUnique() {
		return (Keybase)getMixed().get(SchemaPackage.Literals.DOCUMENT_ROOT__UNIQUE, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetUnique(Keybase newUnique, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(SchemaPackage.Literals.DOCUMENT_ROOT__UNIQUE, newUnique, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUnique(Keybase newUnique) {
		((FeatureMap.Internal)getMixed()).set(SchemaPackage.Literals.DOCUMENT_ROOT__UNIQUE, newUnique);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public WhiteSpaceType getWhiteSpace() {
		return (WhiteSpaceType)getMixed().get(SchemaPackage.Literals.DOCUMENT_ROOT__WHITE_SPACE, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetWhiteSpace(WhiteSpaceType newWhiteSpace, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(SchemaPackage.Literals.DOCUMENT_ROOT__WHITE_SPACE, newWhiteSpace, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setWhiteSpace(WhiteSpaceType newWhiteSpace) {
		((FeatureMap.Internal)getMixed()).set(SchemaPackage.Literals.DOCUMENT_ROOT__WHITE_SPACE, newWhiteSpace);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SchemaPackage.DOCUMENT_ROOT__MIXED:
				return ((InternalEList<?>)getMixed()).basicRemove(otherEnd, msgs);
			case SchemaPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
				return ((InternalEList<?>)getXMLNSPrefixMap()).basicRemove(otherEnd, msgs);
			case SchemaPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
				return ((InternalEList<?>)getXSISchemaLocation()).basicRemove(otherEnd, msgs);
			case SchemaPackage.DOCUMENT_ROOT__ALL:
				return basicSetAll(null, msgs);
			case SchemaPackage.DOCUMENT_ROOT__ANNOTATION:
				return basicSetAnnotation(null, msgs);
			case SchemaPackage.DOCUMENT_ROOT__ANY:
				return basicSetAny(null, msgs);
			case SchemaPackage.DOCUMENT_ROOT__ANY_ATTRIBUTE:
				return basicSetAnyAttribute(null, msgs);
			case SchemaPackage.DOCUMENT_ROOT__APPINFO:
				return basicSetAppinfo(null, msgs);
			case SchemaPackage.DOCUMENT_ROOT__ATTRIBUTE:
				return basicSetAttribute(null, msgs);
			case SchemaPackage.DOCUMENT_ROOT__ATTRIBUTE_GROUP:
				return basicSetAttributeGroup(null, msgs);
			case SchemaPackage.DOCUMENT_ROOT__CHOICE:
				return basicSetChoice(null, msgs);
			case SchemaPackage.DOCUMENT_ROOT__COMPLEX_CONTENT:
				return basicSetComplexContent(null, msgs);
			case SchemaPackage.DOCUMENT_ROOT__COMPLEX_TYPE:
				return basicSetComplexType(null, msgs);
			case SchemaPackage.DOCUMENT_ROOT__DOCUMENTATION:
				return basicSetDocumentation(null, msgs);
			case SchemaPackage.DOCUMENT_ROOT__ELEMENT:
				return basicSetElement(null, msgs);
			case SchemaPackage.DOCUMENT_ROOT__ENUMERATION:
				return basicSetEnumeration(null, msgs);
			case SchemaPackage.DOCUMENT_ROOT__FIELD:
				return basicSetField(null, msgs);
			case SchemaPackage.DOCUMENT_ROOT__FRACTION_DIGITS:
				return basicSetFractionDigits(null, msgs);
			case SchemaPackage.DOCUMENT_ROOT__GROUP:
				return basicSetGroup(null, msgs);
			case SchemaPackage.DOCUMENT_ROOT__IMPORT:
				return basicSetImport(null, msgs);
			case SchemaPackage.DOCUMENT_ROOT__INCLUDE:
				return basicSetInclude(null, msgs);
			case SchemaPackage.DOCUMENT_ROOT__KEY:
				return basicSetKey(null, msgs);
			case SchemaPackage.DOCUMENT_ROOT__KEYREF:
				return basicSetKeyref(null, msgs);
			case SchemaPackage.DOCUMENT_ROOT__LENGTH:
				return basicSetLength(null, msgs);
			case SchemaPackage.DOCUMENT_ROOT__LIST:
				return basicSetList(null, msgs);
			case SchemaPackage.DOCUMENT_ROOT__MAX_EXCLUSIVE:
				return basicSetMaxExclusive(null, msgs);
			case SchemaPackage.DOCUMENT_ROOT__MAX_INCLUSIVE:
				return basicSetMaxInclusive(null, msgs);
			case SchemaPackage.DOCUMENT_ROOT__MAX_LENGTH:
				return basicSetMaxLength(null, msgs);
			case SchemaPackage.DOCUMENT_ROOT__MIN_EXCLUSIVE:
				return basicSetMinExclusive(null, msgs);
			case SchemaPackage.DOCUMENT_ROOT__MIN_INCLUSIVE:
				return basicSetMinInclusive(null, msgs);
			case SchemaPackage.DOCUMENT_ROOT__MIN_LENGTH:
				return basicSetMinLength(null, msgs);
			case SchemaPackage.DOCUMENT_ROOT__NOTATION:
				return basicSetNotation(null, msgs);
			case SchemaPackage.DOCUMENT_ROOT__PATTERN:
				return basicSetPattern(null, msgs);
			case SchemaPackage.DOCUMENT_ROOT__REDEFINE:
				return basicSetRedefine(null, msgs);
			case SchemaPackage.DOCUMENT_ROOT__RESTRICTION:
				return basicSetRestriction(null, msgs);
			case SchemaPackage.DOCUMENT_ROOT__SCHEMA:
				return basicSetSchema(null, msgs);
			case SchemaPackage.DOCUMENT_ROOT__SELECTOR:
				return basicSetSelector(null, msgs);
			case SchemaPackage.DOCUMENT_ROOT__SEQUENCE:
				return basicSetSequence(null, msgs);
			case SchemaPackage.DOCUMENT_ROOT__SIMPLE_CONTENT:
				return basicSetSimpleContent(null, msgs);
			case SchemaPackage.DOCUMENT_ROOT__SIMPLE_TYPE:
				return basicSetSimpleType(null, msgs);
			case SchemaPackage.DOCUMENT_ROOT__TOTAL_DIGITS:
				return basicSetTotalDigits(null, msgs);
			case SchemaPackage.DOCUMENT_ROOT__UNION:
				return basicSetUnion(null, msgs);
			case SchemaPackage.DOCUMENT_ROOT__UNIQUE:
				return basicSetUnique(null, msgs);
			case SchemaPackage.DOCUMENT_ROOT__WHITE_SPACE:
				return basicSetWhiteSpace(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SchemaPackage.DOCUMENT_ROOT__MIXED:
				if (coreType) return getMixed();
				return ((FeatureMap.Internal)getMixed()).getWrapper();
			case SchemaPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
				if (coreType) return getXMLNSPrefixMap();
				else return getXMLNSPrefixMap().map();
			case SchemaPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
				if (coreType) return getXSISchemaLocation();
				else return getXSISchemaLocation().map();
			case SchemaPackage.DOCUMENT_ROOT__ALL:
				return getAll();
			case SchemaPackage.DOCUMENT_ROOT__ANNOTATION:
				return getAnnotation();
			case SchemaPackage.DOCUMENT_ROOT__ANY:
				return getAny();
			case SchemaPackage.DOCUMENT_ROOT__ANY_ATTRIBUTE:
				return getAnyAttribute();
			case SchemaPackage.DOCUMENT_ROOT__APPINFO:
				return getAppinfo();
			case SchemaPackage.DOCUMENT_ROOT__ATTRIBUTE:
				return getAttribute();
			case SchemaPackage.DOCUMENT_ROOT__ATTRIBUTE_GROUP:
				return getAttributeGroup();
			case SchemaPackage.DOCUMENT_ROOT__CHOICE:
				return getChoice();
			case SchemaPackage.DOCUMENT_ROOT__COMPLEX_CONTENT:
				return getComplexContent();
			case SchemaPackage.DOCUMENT_ROOT__COMPLEX_TYPE:
				return getComplexType();
			case SchemaPackage.DOCUMENT_ROOT__DOCUMENTATION:
				return getDocumentation();
			case SchemaPackage.DOCUMENT_ROOT__ELEMENT:
				return getElement();
			case SchemaPackage.DOCUMENT_ROOT__ENUMERATION:
				return getEnumeration();
			case SchemaPackage.DOCUMENT_ROOT__FIELD:
				return getField();
			case SchemaPackage.DOCUMENT_ROOT__FRACTION_DIGITS:
				return getFractionDigits();
			case SchemaPackage.DOCUMENT_ROOT__GROUP:
				return getGroup();
			case SchemaPackage.DOCUMENT_ROOT__IMPORT:
				return getImport();
			case SchemaPackage.DOCUMENT_ROOT__INCLUDE:
				return getInclude();
			case SchemaPackage.DOCUMENT_ROOT__KEY:
				return getKey();
			case SchemaPackage.DOCUMENT_ROOT__KEYREF:
				return getKeyref();
			case SchemaPackage.DOCUMENT_ROOT__LENGTH:
				return getLength();
			case SchemaPackage.DOCUMENT_ROOT__LIST:
				return getList();
			case SchemaPackage.DOCUMENT_ROOT__MAX_EXCLUSIVE:
				return getMaxExclusive();
			case SchemaPackage.DOCUMENT_ROOT__MAX_INCLUSIVE:
				return getMaxInclusive();
			case SchemaPackage.DOCUMENT_ROOT__MAX_LENGTH:
				return getMaxLength();
			case SchemaPackage.DOCUMENT_ROOT__MIN_EXCLUSIVE:
				return getMinExclusive();
			case SchemaPackage.DOCUMENT_ROOT__MIN_INCLUSIVE:
				return getMinInclusive();
			case SchemaPackage.DOCUMENT_ROOT__MIN_LENGTH:
				return getMinLength();
			case SchemaPackage.DOCUMENT_ROOT__NOTATION:
				return getNotation();
			case SchemaPackage.DOCUMENT_ROOT__PATTERN:
				return getPattern();
			case SchemaPackage.DOCUMENT_ROOT__REDEFINE:
				return getRedefine();
			case SchemaPackage.DOCUMENT_ROOT__RESTRICTION:
				return getRestriction();
			case SchemaPackage.DOCUMENT_ROOT__SCHEMA:
				return getSchema();
			case SchemaPackage.DOCUMENT_ROOT__SELECTOR:
				return getSelector();
			case SchemaPackage.DOCUMENT_ROOT__SEQUENCE:
				return getSequence();
			case SchemaPackage.DOCUMENT_ROOT__SIMPLE_CONTENT:
				return getSimpleContent();
			case SchemaPackage.DOCUMENT_ROOT__SIMPLE_TYPE:
				return getSimpleType();
			case SchemaPackage.DOCUMENT_ROOT__TOTAL_DIGITS:
				return getTotalDigits();
			case SchemaPackage.DOCUMENT_ROOT__UNION:
				return getUnion();
			case SchemaPackage.DOCUMENT_ROOT__UNIQUE:
				return getUnique();
			case SchemaPackage.DOCUMENT_ROOT__WHITE_SPACE:
				return getWhiteSpace();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case SchemaPackage.DOCUMENT_ROOT__MIXED:
				((FeatureMap.Internal)getMixed()).set(newValue);
				return;
			case SchemaPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
				((EStructuralFeature.Setting)getXMLNSPrefixMap()).set(newValue);
				return;
			case SchemaPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
				((EStructuralFeature.Setting)getXSISchemaLocation()).set(newValue);
				return;
			case SchemaPackage.DOCUMENT_ROOT__ALL:
				setAll((All)newValue);
				return;
			case SchemaPackage.DOCUMENT_ROOT__ANNOTATION:
				setAnnotation((AnnotationType)newValue);
				return;
			case SchemaPackage.DOCUMENT_ROOT__ANY:
				setAny((AnyType)newValue);
				return;
			case SchemaPackage.DOCUMENT_ROOT__ANY_ATTRIBUTE:
				setAnyAttribute((Wildcard)newValue);
				return;
			case SchemaPackage.DOCUMENT_ROOT__APPINFO:
				setAppinfo((AppinfoType)newValue);
				return;
			case SchemaPackage.DOCUMENT_ROOT__ATTRIBUTE:
				setAttribute((TopLevelAttribute)newValue);
				return;
			case SchemaPackage.DOCUMENT_ROOT__ATTRIBUTE_GROUP:
				setAttributeGroup((NamedAttributeGroup)newValue);
				return;
			case SchemaPackage.DOCUMENT_ROOT__CHOICE:
				setChoice((ExplicitGroup)newValue);
				return;
			case SchemaPackage.DOCUMENT_ROOT__COMPLEX_CONTENT:
				setComplexContent((ComplexContentType)newValue);
				return;
			case SchemaPackage.DOCUMENT_ROOT__COMPLEX_TYPE:
				setComplexType((TopLevelComplexType)newValue);
				return;
			case SchemaPackage.DOCUMENT_ROOT__DOCUMENTATION:
				setDocumentation((DocumentationType)newValue);
				return;
			case SchemaPackage.DOCUMENT_ROOT__ELEMENT:
				setElement((TopLevelElement)newValue);
				return;
			case SchemaPackage.DOCUMENT_ROOT__ENUMERATION:
				setEnumeration((NoFixedFacet)newValue);
				return;
			case SchemaPackage.DOCUMENT_ROOT__FIELD:
				setField((FieldType)newValue);
				return;
			case SchemaPackage.DOCUMENT_ROOT__FRACTION_DIGITS:
				setFractionDigits((NumFacet)newValue);
				return;
			case SchemaPackage.DOCUMENT_ROOT__GROUP:
				setGroup((NamedGroup)newValue);
				return;
			case SchemaPackage.DOCUMENT_ROOT__IMPORT:
				setImport((ImportType)newValue);
				return;
			case SchemaPackage.DOCUMENT_ROOT__INCLUDE:
				setInclude((IncludeType)newValue);
				return;
			case SchemaPackage.DOCUMENT_ROOT__KEY:
				setKey((Keybase)newValue);
				return;
			case SchemaPackage.DOCUMENT_ROOT__KEYREF:
				setKeyref((KeyrefType)newValue);
				return;
			case SchemaPackage.DOCUMENT_ROOT__LENGTH:
				setLength((NumFacet)newValue);
				return;
			case SchemaPackage.DOCUMENT_ROOT__LIST:
				setList((ListType)newValue);
				return;
			case SchemaPackage.DOCUMENT_ROOT__MAX_EXCLUSIVE:
				setMaxExclusive((Facet)newValue);
				return;
			case SchemaPackage.DOCUMENT_ROOT__MAX_INCLUSIVE:
				setMaxInclusive((Facet)newValue);
				return;
			case SchemaPackage.DOCUMENT_ROOT__MAX_LENGTH:
				setMaxLength((NumFacet)newValue);
				return;
			case SchemaPackage.DOCUMENT_ROOT__MIN_EXCLUSIVE:
				setMinExclusive((Facet)newValue);
				return;
			case SchemaPackage.DOCUMENT_ROOT__MIN_INCLUSIVE:
				setMinInclusive((Facet)newValue);
				return;
			case SchemaPackage.DOCUMENT_ROOT__MIN_LENGTH:
				setMinLength((NumFacet)newValue);
				return;
			case SchemaPackage.DOCUMENT_ROOT__NOTATION:
				setNotation((NotationType)newValue);
				return;
			case SchemaPackage.DOCUMENT_ROOT__PATTERN:
				setPattern((PatternType)newValue);
				return;
			case SchemaPackage.DOCUMENT_ROOT__REDEFINE:
				setRedefine((RedefineType)newValue);
				return;
			case SchemaPackage.DOCUMENT_ROOT__RESTRICTION:
				setRestriction((RestrictionType1)newValue);
				return;
			case SchemaPackage.DOCUMENT_ROOT__SCHEMA:
				setSchema((SchemaType)newValue);
				return;
			case SchemaPackage.DOCUMENT_ROOT__SELECTOR:
				setSelector((SelectorType)newValue);
				return;
			case SchemaPackage.DOCUMENT_ROOT__SEQUENCE:
				setSequence((ExplicitGroup)newValue);
				return;
			case SchemaPackage.DOCUMENT_ROOT__SIMPLE_CONTENT:
				setSimpleContent((SimpleContentType)newValue);
				return;
			case SchemaPackage.DOCUMENT_ROOT__SIMPLE_TYPE:
				setSimpleType((TopLevelSimpleType)newValue);
				return;
			case SchemaPackage.DOCUMENT_ROOT__TOTAL_DIGITS:
				setTotalDigits((TotalDigitsType)newValue);
				return;
			case SchemaPackage.DOCUMENT_ROOT__UNION:
				setUnion((UnionType)newValue);
				return;
			case SchemaPackage.DOCUMENT_ROOT__UNIQUE:
				setUnique((Keybase)newValue);
				return;
			case SchemaPackage.DOCUMENT_ROOT__WHITE_SPACE:
				setWhiteSpace((WhiteSpaceType)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case SchemaPackage.DOCUMENT_ROOT__MIXED:
				getMixed().clear();
				return;
			case SchemaPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
				getXMLNSPrefixMap().clear();
				return;
			case SchemaPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
				getXSISchemaLocation().clear();
				return;
			case SchemaPackage.DOCUMENT_ROOT__ALL:
				setAll((All)null);
				return;
			case SchemaPackage.DOCUMENT_ROOT__ANNOTATION:
				setAnnotation((AnnotationType)null);
				return;
			case SchemaPackage.DOCUMENT_ROOT__ANY:
				setAny((AnyType)null);
				return;
			case SchemaPackage.DOCUMENT_ROOT__ANY_ATTRIBUTE:
				setAnyAttribute((Wildcard)null);
				return;
			case SchemaPackage.DOCUMENT_ROOT__APPINFO:
				setAppinfo((AppinfoType)null);
				return;
			case SchemaPackage.DOCUMENT_ROOT__ATTRIBUTE:
				setAttribute((TopLevelAttribute)null);
				return;
			case SchemaPackage.DOCUMENT_ROOT__ATTRIBUTE_GROUP:
				setAttributeGroup((NamedAttributeGroup)null);
				return;
			case SchemaPackage.DOCUMENT_ROOT__CHOICE:
				setChoice((ExplicitGroup)null);
				return;
			case SchemaPackage.DOCUMENT_ROOT__COMPLEX_CONTENT:
				setComplexContent((ComplexContentType)null);
				return;
			case SchemaPackage.DOCUMENT_ROOT__COMPLEX_TYPE:
				setComplexType((TopLevelComplexType)null);
				return;
			case SchemaPackage.DOCUMENT_ROOT__DOCUMENTATION:
				setDocumentation((DocumentationType)null);
				return;
			case SchemaPackage.DOCUMENT_ROOT__ELEMENT:
				setElement((TopLevelElement)null);
				return;
			case SchemaPackage.DOCUMENT_ROOT__ENUMERATION:
				setEnumeration((NoFixedFacet)null);
				return;
			case SchemaPackage.DOCUMENT_ROOT__FIELD:
				setField((FieldType)null);
				return;
			case SchemaPackage.DOCUMENT_ROOT__FRACTION_DIGITS:
				setFractionDigits((NumFacet)null);
				return;
			case SchemaPackage.DOCUMENT_ROOT__GROUP:
				setGroup((NamedGroup)null);
				return;
			case SchemaPackage.DOCUMENT_ROOT__IMPORT:
				setImport((ImportType)null);
				return;
			case SchemaPackage.DOCUMENT_ROOT__INCLUDE:
				setInclude((IncludeType)null);
				return;
			case SchemaPackage.DOCUMENT_ROOT__KEY:
				setKey((Keybase)null);
				return;
			case SchemaPackage.DOCUMENT_ROOT__KEYREF:
				setKeyref((KeyrefType)null);
				return;
			case SchemaPackage.DOCUMENT_ROOT__LENGTH:
				setLength((NumFacet)null);
				return;
			case SchemaPackage.DOCUMENT_ROOT__LIST:
				setList((ListType)null);
				return;
			case SchemaPackage.DOCUMENT_ROOT__MAX_EXCLUSIVE:
				setMaxExclusive((Facet)null);
				return;
			case SchemaPackage.DOCUMENT_ROOT__MAX_INCLUSIVE:
				setMaxInclusive((Facet)null);
				return;
			case SchemaPackage.DOCUMENT_ROOT__MAX_LENGTH:
				setMaxLength((NumFacet)null);
				return;
			case SchemaPackage.DOCUMENT_ROOT__MIN_EXCLUSIVE:
				setMinExclusive((Facet)null);
				return;
			case SchemaPackage.DOCUMENT_ROOT__MIN_INCLUSIVE:
				setMinInclusive((Facet)null);
				return;
			case SchemaPackage.DOCUMENT_ROOT__MIN_LENGTH:
				setMinLength((NumFacet)null);
				return;
			case SchemaPackage.DOCUMENT_ROOT__NOTATION:
				setNotation((NotationType)null);
				return;
			case SchemaPackage.DOCUMENT_ROOT__PATTERN:
				setPattern((PatternType)null);
				return;
			case SchemaPackage.DOCUMENT_ROOT__REDEFINE:
				setRedefine((RedefineType)null);
				return;
			case SchemaPackage.DOCUMENT_ROOT__RESTRICTION:
				setRestriction((RestrictionType1)null);
				return;
			case SchemaPackage.DOCUMENT_ROOT__SCHEMA:
				setSchema((SchemaType)null);
				return;
			case SchemaPackage.DOCUMENT_ROOT__SELECTOR:
				setSelector((SelectorType)null);
				return;
			case SchemaPackage.DOCUMENT_ROOT__SEQUENCE:
				setSequence((ExplicitGroup)null);
				return;
			case SchemaPackage.DOCUMENT_ROOT__SIMPLE_CONTENT:
				setSimpleContent((SimpleContentType)null);
				return;
			case SchemaPackage.DOCUMENT_ROOT__SIMPLE_TYPE:
				setSimpleType((TopLevelSimpleType)null);
				return;
			case SchemaPackage.DOCUMENT_ROOT__TOTAL_DIGITS:
				setTotalDigits((TotalDigitsType)null);
				return;
			case SchemaPackage.DOCUMENT_ROOT__UNION:
				setUnion((UnionType)null);
				return;
			case SchemaPackage.DOCUMENT_ROOT__UNIQUE:
				setUnique((Keybase)null);
				return;
			case SchemaPackage.DOCUMENT_ROOT__WHITE_SPACE:
				setWhiteSpace((WhiteSpaceType)null);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case SchemaPackage.DOCUMENT_ROOT__MIXED:
				return mixed != null && !mixed.isEmpty();
			case SchemaPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
				return xMLNSPrefixMap != null && !xMLNSPrefixMap.isEmpty();
			case SchemaPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
				return xSISchemaLocation != null && !xSISchemaLocation.isEmpty();
			case SchemaPackage.DOCUMENT_ROOT__ALL:
				return getAll() != null;
			case SchemaPackage.DOCUMENT_ROOT__ANNOTATION:
				return getAnnotation() != null;
			case SchemaPackage.DOCUMENT_ROOT__ANY:
				return getAny() != null;
			case SchemaPackage.DOCUMENT_ROOT__ANY_ATTRIBUTE:
				return getAnyAttribute() != null;
			case SchemaPackage.DOCUMENT_ROOT__APPINFO:
				return getAppinfo() != null;
			case SchemaPackage.DOCUMENT_ROOT__ATTRIBUTE:
				return getAttribute() != null;
			case SchemaPackage.DOCUMENT_ROOT__ATTRIBUTE_GROUP:
				return getAttributeGroup() != null;
			case SchemaPackage.DOCUMENT_ROOT__CHOICE:
				return getChoice() != null;
			case SchemaPackage.DOCUMENT_ROOT__COMPLEX_CONTENT:
				return getComplexContent() != null;
			case SchemaPackage.DOCUMENT_ROOT__COMPLEX_TYPE:
				return getComplexType() != null;
			case SchemaPackage.DOCUMENT_ROOT__DOCUMENTATION:
				return getDocumentation() != null;
			case SchemaPackage.DOCUMENT_ROOT__ELEMENT:
				return getElement() != null;
			case SchemaPackage.DOCUMENT_ROOT__ENUMERATION:
				return getEnumeration() != null;
			case SchemaPackage.DOCUMENT_ROOT__FIELD:
				return getField() != null;
			case SchemaPackage.DOCUMENT_ROOT__FRACTION_DIGITS:
				return getFractionDigits() != null;
			case SchemaPackage.DOCUMENT_ROOT__GROUP:
				return getGroup() != null;
			case SchemaPackage.DOCUMENT_ROOT__IMPORT:
				return getImport() != null;
			case SchemaPackage.DOCUMENT_ROOT__INCLUDE:
				return getInclude() != null;
			case SchemaPackage.DOCUMENT_ROOT__KEY:
				return getKey() != null;
			case SchemaPackage.DOCUMENT_ROOT__KEYREF:
				return getKeyref() != null;
			case SchemaPackage.DOCUMENT_ROOT__LENGTH:
				return getLength() != null;
			case SchemaPackage.DOCUMENT_ROOT__LIST:
				return getList() != null;
			case SchemaPackage.DOCUMENT_ROOT__MAX_EXCLUSIVE:
				return getMaxExclusive() != null;
			case SchemaPackage.DOCUMENT_ROOT__MAX_INCLUSIVE:
				return getMaxInclusive() != null;
			case SchemaPackage.DOCUMENT_ROOT__MAX_LENGTH:
				return getMaxLength() != null;
			case SchemaPackage.DOCUMENT_ROOT__MIN_EXCLUSIVE:
				return getMinExclusive() != null;
			case SchemaPackage.DOCUMENT_ROOT__MIN_INCLUSIVE:
				return getMinInclusive() != null;
			case SchemaPackage.DOCUMENT_ROOT__MIN_LENGTH:
				return getMinLength() != null;
			case SchemaPackage.DOCUMENT_ROOT__NOTATION:
				return getNotation() != null;
			case SchemaPackage.DOCUMENT_ROOT__PATTERN:
				return getPattern() != null;
			case SchemaPackage.DOCUMENT_ROOT__REDEFINE:
				return getRedefine() != null;
			case SchemaPackage.DOCUMENT_ROOT__RESTRICTION:
				return getRestriction() != null;
			case SchemaPackage.DOCUMENT_ROOT__SCHEMA:
				return getSchema() != null;
			case SchemaPackage.DOCUMENT_ROOT__SELECTOR:
				return getSelector() != null;
			case SchemaPackage.DOCUMENT_ROOT__SEQUENCE:
				return getSequence() != null;
			case SchemaPackage.DOCUMENT_ROOT__SIMPLE_CONTENT:
				return getSimpleContent() != null;
			case SchemaPackage.DOCUMENT_ROOT__SIMPLE_TYPE:
				return getSimpleType() != null;
			case SchemaPackage.DOCUMENT_ROOT__TOTAL_DIGITS:
				return getTotalDigits() != null;
			case SchemaPackage.DOCUMENT_ROOT__UNION:
				return getUnion() != null;
			case SchemaPackage.DOCUMENT_ROOT__UNIQUE:
				return getUnique() != null;
			case SchemaPackage.DOCUMENT_ROOT__WHITE_SPACE:
				return getWhiteSpace() != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (mixed: ");
		result.append(mixed);
		result.append(')');
		return result.toString();
	}

} //DocumentRootImpl
