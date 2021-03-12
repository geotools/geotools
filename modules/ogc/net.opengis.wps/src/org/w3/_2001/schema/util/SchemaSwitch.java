/**
 */
package org.w3._2001.schema.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.Switch;

import org.w3._2001.schema.*;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see org.w3._2001.schema.SchemaPackage
 * @generated
 */
public class SchemaSwitch<T> extends Switch<T> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static SchemaPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SchemaSwitch() {
		if (modelPackage == null) {
			modelPackage = SchemaPackage.eINSTANCE;
		}
	}

	/**
	 * Checks whether this is a switch for the given package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param ePackage the package in question.
	 * @return whether this is a switch for the given package.
	 * @generated
	 */
	@Override
	protected boolean isSwitchFor(EPackage ePackage) {
		return ePackage == modelPackage;
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	@Override
	protected T doSwitch(int classifierID, EObject theEObject) {
		switch (classifierID) {
			case SchemaPackage.ALL: {
				All all = (All)theEObject;
				T result = caseAll(all);
				if (result == null) result = caseExplicitGroup(all);
				if (result == null) result = caseGroup(all);
				if (result == null) result = caseAnnotated(all);
				if (result == null) result = caseOpenAttrs(all);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.ANNOTATED: {
				Annotated annotated = (Annotated)theEObject;
				T result = caseAnnotated(annotated);
				if (result == null) result = caseOpenAttrs(annotated);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.ANNOTATION_TYPE: {
				AnnotationType annotationType = (AnnotationType)theEObject;
				T result = caseAnnotationType(annotationType);
				if (result == null) result = caseOpenAttrs(annotationType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.ANY_TYPE: {
				AnyType anyType = (AnyType)theEObject;
				T result = caseAnyType(anyType);
				if (result == null) result = caseWildcard(anyType);
				if (result == null) result = caseAnnotated(anyType);
				if (result == null) result = caseOpenAttrs(anyType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.APPINFO_TYPE: {
				AppinfoType appinfoType = (AppinfoType)theEObject;
				T result = caseAppinfoType(appinfoType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.ATTRIBUTE: {
				Attribute attribute = (Attribute)theEObject;
				T result = caseAttribute(attribute);
				if (result == null) result = caseAnnotated(attribute);
				if (result == null) result = caseOpenAttrs(attribute);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.ATTRIBUTE_GROUP: {
				AttributeGroup attributeGroup = (AttributeGroup)theEObject;
				T result = caseAttributeGroup(attributeGroup);
				if (result == null) result = caseAnnotated(attributeGroup);
				if (result == null) result = caseOpenAttrs(attributeGroup);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.ATTRIBUTE_GROUP_REF: {
				AttributeGroupRef attributeGroupRef = (AttributeGroupRef)theEObject;
				T result = caseAttributeGroupRef(attributeGroupRef);
				if (result == null) result = caseAttributeGroup(attributeGroupRef);
				if (result == null) result = caseAnnotated(attributeGroupRef);
				if (result == null) result = caseOpenAttrs(attributeGroupRef);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.COMPLEX_CONTENT_TYPE: {
				ComplexContentType complexContentType = (ComplexContentType)theEObject;
				T result = caseComplexContentType(complexContentType);
				if (result == null) result = caseAnnotated(complexContentType);
				if (result == null) result = caseOpenAttrs(complexContentType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.COMPLEX_RESTRICTION_TYPE: {
				ComplexRestrictionType complexRestrictionType = (ComplexRestrictionType)theEObject;
				T result = caseComplexRestrictionType(complexRestrictionType);
				if (result == null) result = caseRestrictionType(complexRestrictionType);
				if (result == null) result = caseAnnotated(complexRestrictionType);
				if (result == null) result = caseOpenAttrs(complexRestrictionType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.COMPLEX_TYPE: {
				ComplexType complexType = (ComplexType)theEObject;
				T result = caseComplexType(complexType);
				if (result == null) result = caseAnnotated(complexType);
				if (result == null) result = caseOpenAttrs(complexType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.DOCUMENTATION_TYPE: {
				DocumentationType documentationType = (DocumentationType)theEObject;
				T result = caseDocumentationType(documentationType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.DOCUMENT_ROOT: {
				DocumentRoot documentRoot = (DocumentRoot)theEObject;
				T result = caseDocumentRoot(documentRoot);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.ELEMENT: {
				Element element = (Element)theEObject;
				T result = caseElement(element);
				if (result == null) result = caseAnnotated(element);
				if (result == null) result = caseOpenAttrs(element);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.EXPLICIT_GROUP: {
				ExplicitGroup explicitGroup = (ExplicitGroup)theEObject;
				T result = caseExplicitGroup(explicitGroup);
				if (result == null) result = caseGroup(explicitGroup);
				if (result == null) result = caseAnnotated(explicitGroup);
				if (result == null) result = caseOpenAttrs(explicitGroup);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.EXTENSION_TYPE: {
				ExtensionType extensionType = (ExtensionType)theEObject;
				T result = caseExtensionType(extensionType);
				if (result == null) result = caseAnnotated(extensionType);
				if (result == null) result = caseOpenAttrs(extensionType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.FACET: {
				Facet facet = (Facet)theEObject;
				T result = caseFacet(facet);
				if (result == null) result = caseAnnotated(facet);
				if (result == null) result = caseOpenAttrs(facet);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.FIELD_TYPE: {
				FieldType fieldType = (FieldType)theEObject;
				T result = caseFieldType(fieldType);
				if (result == null) result = caseAnnotated(fieldType);
				if (result == null) result = caseOpenAttrs(fieldType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.GROUP: {
				Group group = (Group)theEObject;
				T result = caseGroup(group);
				if (result == null) result = caseAnnotated(group);
				if (result == null) result = caseOpenAttrs(group);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.GROUP_REF: {
				GroupRef groupRef = (GroupRef)theEObject;
				T result = caseGroupRef(groupRef);
				if (result == null) result = caseRealGroup(groupRef);
				if (result == null) result = caseGroup(groupRef);
				if (result == null) result = caseAnnotated(groupRef);
				if (result == null) result = caseOpenAttrs(groupRef);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.IMPORT_TYPE: {
				ImportType importType = (ImportType)theEObject;
				T result = caseImportType(importType);
				if (result == null) result = caseAnnotated(importType);
				if (result == null) result = caseOpenAttrs(importType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.INCLUDE_TYPE: {
				IncludeType includeType = (IncludeType)theEObject;
				T result = caseIncludeType(includeType);
				if (result == null) result = caseAnnotated(includeType);
				if (result == null) result = caseOpenAttrs(includeType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.KEYBASE: {
				Keybase keybase = (Keybase)theEObject;
				T result = caseKeybase(keybase);
				if (result == null) result = caseAnnotated(keybase);
				if (result == null) result = caseOpenAttrs(keybase);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.KEYREF_TYPE: {
				KeyrefType keyrefType = (KeyrefType)theEObject;
				T result = caseKeyrefType(keyrefType);
				if (result == null) result = caseKeybase(keyrefType);
				if (result == null) result = caseAnnotated(keyrefType);
				if (result == null) result = caseOpenAttrs(keyrefType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.LIST_TYPE: {
				ListType listType = (ListType)theEObject;
				T result = caseListType(listType);
				if (result == null) result = caseAnnotated(listType);
				if (result == null) result = caseOpenAttrs(listType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.LOCAL_COMPLEX_TYPE: {
				LocalComplexType localComplexType = (LocalComplexType)theEObject;
				T result = caseLocalComplexType(localComplexType);
				if (result == null) result = caseComplexType(localComplexType);
				if (result == null) result = caseAnnotated(localComplexType);
				if (result == null) result = caseOpenAttrs(localComplexType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.LOCAL_ELEMENT: {
				LocalElement localElement = (LocalElement)theEObject;
				T result = caseLocalElement(localElement);
				if (result == null) result = caseElement(localElement);
				if (result == null) result = caseAnnotated(localElement);
				if (result == null) result = caseOpenAttrs(localElement);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.LOCAL_SIMPLE_TYPE: {
				LocalSimpleType localSimpleType = (LocalSimpleType)theEObject;
				T result = caseLocalSimpleType(localSimpleType);
				if (result == null) result = caseSimpleType(localSimpleType);
				if (result == null) result = caseAnnotated(localSimpleType);
				if (result == null) result = caseOpenAttrs(localSimpleType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.NAMED_ATTRIBUTE_GROUP: {
				NamedAttributeGroup namedAttributeGroup = (NamedAttributeGroup)theEObject;
				T result = caseNamedAttributeGroup(namedAttributeGroup);
				if (result == null) result = caseAttributeGroup(namedAttributeGroup);
				if (result == null) result = caseAnnotated(namedAttributeGroup);
				if (result == null) result = caseOpenAttrs(namedAttributeGroup);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.NAMED_GROUP: {
				NamedGroup namedGroup = (NamedGroup)theEObject;
				T result = caseNamedGroup(namedGroup);
				if (result == null) result = caseRealGroup(namedGroup);
				if (result == null) result = caseGroup(namedGroup);
				if (result == null) result = caseAnnotated(namedGroup);
				if (result == null) result = caseOpenAttrs(namedGroup);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.NARROW_MAX_MIN: {
				NarrowMaxMin narrowMaxMin = (NarrowMaxMin)theEObject;
				T result = caseNarrowMaxMin(narrowMaxMin);
				if (result == null) result = caseLocalElement(narrowMaxMin);
				if (result == null) result = caseElement(narrowMaxMin);
				if (result == null) result = caseAnnotated(narrowMaxMin);
				if (result == null) result = caseOpenAttrs(narrowMaxMin);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.NO_FIXED_FACET: {
				NoFixedFacet noFixedFacet = (NoFixedFacet)theEObject;
				T result = caseNoFixedFacet(noFixedFacet);
				if (result == null) result = caseFacet(noFixedFacet);
				if (result == null) result = caseAnnotated(noFixedFacet);
				if (result == null) result = caseOpenAttrs(noFixedFacet);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.NOTATION_TYPE: {
				NotationType notationType = (NotationType)theEObject;
				T result = caseNotationType(notationType);
				if (result == null) result = caseAnnotated(notationType);
				if (result == null) result = caseOpenAttrs(notationType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.NUM_FACET: {
				NumFacet numFacet = (NumFacet)theEObject;
				T result = caseNumFacet(numFacet);
				if (result == null) result = caseFacet(numFacet);
				if (result == null) result = caseAnnotated(numFacet);
				if (result == null) result = caseOpenAttrs(numFacet);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.OPEN_ATTRS: {
				OpenAttrs openAttrs = (OpenAttrs)theEObject;
				T result = caseOpenAttrs(openAttrs);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.PATTERN_TYPE: {
				PatternType patternType = (PatternType)theEObject;
				T result = casePatternType(patternType);
				if (result == null) result = caseNoFixedFacet(patternType);
				if (result == null) result = caseFacet(patternType);
				if (result == null) result = caseAnnotated(patternType);
				if (result == null) result = caseOpenAttrs(patternType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.REAL_GROUP: {
				RealGroup realGroup = (RealGroup)theEObject;
				T result = caseRealGroup(realGroup);
				if (result == null) result = caseGroup(realGroup);
				if (result == null) result = caseAnnotated(realGroup);
				if (result == null) result = caseOpenAttrs(realGroup);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.REDEFINE_TYPE: {
				RedefineType redefineType = (RedefineType)theEObject;
				T result = caseRedefineType(redefineType);
				if (result == null) result = caseOpenAttrs(redefineType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.RESTRICTION_TYPE: {
				RestrictionType restrictionType = (RestrictionType)theEObject;
				T result = caseRestrictionType(restrictionType);
				if (result == null) result = caseAnnotated(restrictionType);
				if (result == null) result = caseOpenAttrs(restrictionType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.RESTRICTION_TYPE1: {
				RestrictionType1 restrictionType1 = (RestrictionType1)theEObject;
				T result = caseRestrictionType1(restrictionType1);
				if (result == null) result = caseAnnotated(restrictionType1);
				if (result == null) result = caseOpenAttrs(restrictionType1);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.SCHEMA_TYPE: {
				SchemaType schemaType = (SchemaType)theEObject;
				T result = caseSchemaType(schemaType);
				if (result == null) result = caseOpenAttrs(schemaType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.SELECTOR_TYPE: {
				SelectorType selectorType = (SelectorType)theEObject;
				T result = caseSelectorType(selectorType);
				if (result == null) result = caseAnnotated(selectorType);
				if (result == null) result = caseOpenAttrs(selectorType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.SIMPLE_CONTENT_TYPE: {
				SimpleContentType simpleContentType = (SimpleContentType)theEObject;
				T result = caseSimpleContentType(simpleContentType);
				if (result == null) result = caseAnnotated(simpleContentType);
				if (result == null) result = caseOpenAttrs(simpleContentType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.SIMPLE_EXPLICIT_GROUP: {
				SimpleExplicitGroup simpleExplicitGroup = (SimpleExplicitGroup)theEObject;
				T result = caseSimpleExplicitGroup(simpleExplicitGroup);
				if (result == null) result = caseExplicitGroup(simpleExplicitGroup);
				if (result == null) result = caseGroup(simpleExplicitGroup);
				if (result == null) result = caseAnnotated(simpleExplicitGroup);
				if (result == null) result = caseOpenAttrs(simpleExplicitGroup);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.SIMPLE_EXTENSION_TYPE: {
				SimpleExtensionType simpleExtensionType = (SimpleExtensionType)theEObject;
				T result = caseSimpleExtensionType(simpleExtensionType);
				if (result == null) result = caseExtensionType(simpleExtensionType);
				if (result == null) result = caseAnnotated(simpleExtensionType);
				if (result == null) result = caseOpenAttrs(simpleExtensionType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.SIMPLE_RESTRICTION_TYPE: {
				SimpleRestrictionType simpleRestrictionType = (SimpleRestrictionType)theEObject;
				T result = caseSimpleRestrictionType(simpleRestrictionType);
				if (result == null) result = caseRestrictionType(simpleRestrictionType);
				if (result == null) result = caseAnnotated(simpleRestrictionType);
				if (result == null) result = caseOpenAttrs(simpleRestrictionType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.SIMPLE_TYPE: {
				SimpleType simpleType = (SimpleType)theEObject;
				T result = caseSimpleType(simpleType);
				if (result == null) result = caseAnnotated(simpleType);
				if (result == null) result = caseOpenAttrs(simpleType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.TOP_LEVEL_ATTRIBUTE: {
				TopLevelAttribute topLevelAttribute = (TopLevelAttribute)theEObject;
				T result = caseTopLevelAttribute(topLevelAttribute);
				if (result == null) result = caseAttribute(topLevelAttribute);
				if (result == null) result = caseAnnotated(topLevelAttribute);
				if (result == null) result = caseOpenAttrs(topLevelAttribute);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.TOP_LEVEL_COMPLEX_TYPE: {
				TopLevelComplexType topLevelComplexType = (TopLevelComplexType)theEObject;
				T result = caseTopLevelComplexType(topLevelComplexType);
				if (result == null) result = caseComplexType(topLevelComplexType);
				if (result == null) result = caseAnnotated(topLevelComplexType);
				if (result == null) result = caseOpenAttrs(topLevelComplexType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.TOP_LEVEL_ELEMENT: {
				TopLevelElement topLevelElement = (TopLevelElement)theEObject;
				T result = caseTopLevelElement(topLevelElement);
				if (result == null) result = caseElement(topLevelElement);
				if (result == null) result = caseAnnotated(topLevelElement);
				if (result == null) result = caseOpenAttrs(topLevelElement);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.TOP_LEVEL_SIMPLE_TYPE: {
				TopLevelSimpleType topLevelSimpleType = (TopLevelSimpleType)theEObject;
				T result = caseTopLevelSimpleType(topLevelSimpleType);
				if (result == null) result = caseSimpleType(topLevelSimpleType);
				if (result == null) result = caseAnnotated(topLevelSimpleType);
				if (result == null) result = caseOpenAttrs(topLevelSimpleType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.TOTAL_DIGITS_TYPE: {
				TotalDigitsType totalDigitsType = (TotalDigitsType)theEObject;
				T result = caseTotalDigitsType(totalDigitsType);
				if (result == null) result = caseNumFacet(totalDigitsType);
				if (result == null) result = caseFacet(totalDigitsType);
				if (result == null) result = caseAnnotated(totalDigitsType);
				if (result == null) result = caseOpenAttrs(totalDigitsType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.UNION_TYPE: {
				UnionType unionType = (UnionType)theEObject;
				T result = caseUnionType(unionType);
				if (result == null) result = caseAnnotated(unionType);
				if (result == null) result = caseOpenAttrs(unionType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.WHITE_SPACE_TYPE: {
				WhiteSpaceType whiteSpaceType = (WhiteSpaceType)theEObject;
				T result = caseWhiteSpaceType(whiteSpaceType);
				if (result == null) result = caseFacet(whiteSpaceType);
				if (result == null) result = caseAnnotated(whiteSpaceType);
				if (result == null) result = caseOpenAttrs(whiteSpaceType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case SchemaPackage.WILDCARD: {
				Wildcard wildcard = (Wildcard)theEObject;
				T result = caseWildcard(wildcard);
				if (result == null) result = caseAnnotated(wildcard);
				if (result == null) result = caseOpenAttrs(wildcard);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			default: return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>All</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>All</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAll(All object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Annotated</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Annotated</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAnnotated(Annotated object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Annotation Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Annotation Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAnnotationType(AnnotationType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Any Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Any Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAnyType(AnyType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Appinfo Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Appinfo Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAppinfoType(AppinfoType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Attribute</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Attribute</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAttribute(Attribute object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Attribute Group</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Attribute Group</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAttributeGroup(AttributeGroup object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Attribute Group Ref</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Attribute Group Ref</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAttributeGroupRef(AttributeGroupRef object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Complex Content Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Complex Content Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseComplexContentType(ComplexContentType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Complex Restriction Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Complex Restriction Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseComplexRestrictionType(ComplexRestrictionType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Complex Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Complex Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseComplexType(ComplexType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Documentation Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Documentation Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDocumentationType(DocumentationType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Document Root</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Document Root</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDocumentRoot(DocumentRoot object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseElement(Element object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Explicit Group</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Explicit Group</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseExplicitGroup(ExplicitGroup object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Extension Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Extension Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseExtensionType(ExtensionType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Facet</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Facet</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseFacet(Facet object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Field Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Field Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseFieldType(FieldType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Group</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Group</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseGroup(Group object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Group Ref</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Group Ref</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseGroupRef(GroupRef object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Import Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Import Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseImportType(ImportType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Include Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Include Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIncludeType(IncludeType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Keybase</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Keybase</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseKeybase(Keybase object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Keyref Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Keyref Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseKeyrefType(KeyrefType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>List Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>List Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseListType(ListType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Local Complex Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Local Complex Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseLocalComplexType(LocalComplexType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Local Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Local Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseLocalElement(LocalElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Local Simple Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Local Simple Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseLocalSimpleType(LocalSimpleType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Named Attribute Group</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Named Attribute Group</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseNamedAttributeGroup(NamedAttributeGroup object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Named Group</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Named Group</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseNamedGroup(NamedGroup object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Narrow Max Min</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Narrow Max Min</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseNarrowMaxMin(NarrowMaxMin object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>No Fixed Facet</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>No Fixed Facet</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseNoFixedFacet(NoFixedFacet object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Notation Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Notation Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseNotationType(NotationType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Num Facet</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Num Facet</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseNumFacet(NumFacet object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Open Attrs</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Open Attrs</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseOpenAttrs(OpenAttrs object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Pattern Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Pattern Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T casePatternType(PatternType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Real Group</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Real Group</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseRealGroup(RealGroup object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Redefine Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Redefine Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseRedefineType(RedefineType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Restriction Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Restriction Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseRestrictionType(RestrictionType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Restriction Type1</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Restriction Type1</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseRestrictionType1(RestrictionType1 object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSchemaType(SchemaType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Selector Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Selector Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSelectorType(SelectorType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Simple Content Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Simple Content Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSimpleContentType(SimpleContentType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Simple Explicit Group</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Simple Explicit Group</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSimpleExplicitGroup(SimpleExplicitGroup object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Simple Extension Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Simple Extension Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSimpleExtensionType(SimpleExtensionType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Simple Restriction Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Simple Restriction Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSimpleRestrictionType(SimpleRestrictionType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Simple Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Simple Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSimpleType(SimpleType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Top Level Attribute</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Top Level Attribute</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseTopLevelAttribute(TopLevelAttribute object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Top Level Complex Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Top Level Complex Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseTopLevelComplexType(TopLevelComplexType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Top Level Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Top Level Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseTopLevelElement(TopLevelElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Top Level Simple Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Top Level Simple Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseTopLevelSimpleType(TopLevelSimpleType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Total Digits Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Total Digits Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseTotalDigitsType(TotalDigitsType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Union Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Union Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUnionType(UnionType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>White Space Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>White Space Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseWhiteSpaceType(WhiteSpaceType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Wildcard</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Wildcard</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseWildcard(Wildcard object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch, but this is the last case anyway.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject)
	 * @generated
	 */
	@Override
	public T defaultCase(EObject object) {
		return null;
	}

} //SchemaSwitch
