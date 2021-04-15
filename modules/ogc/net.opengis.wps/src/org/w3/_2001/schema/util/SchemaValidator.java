/**
 */
package org.w3._2001.schema.util;

import java.math.BigInteger;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.common.util.ResourceLocator;

import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.EObjectValidator;

import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

import org.eclipse.emf.ecore.xml.type.util.XMLTypeUtil;
import org.eclipse.emf.ecore.xml.type.util.XMLTypeValidator;

import org.w3._2001.schema.*;

/**
 * <!-- begin-user-doc -->
 * The <b>Validator</b> for the model.
 * <!-- end-user-doc -->
 * @see org.w3._2001.schema.SchemaPackage
 * @generated
 */
public class SchemaValidator extends EObjectValidator {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final SchemaValidator INSTANCE = new SchemaValidator();

	/**
	 * A constant for the {@link org.eclipse.emf.common.util.Diagnostic#getSource() source} of diagnostic {@link org.eclipse.emf.common.util.Diagnostic#getCode() codes} from this package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.common.util.Diagnostic#getSource()
	 * @see org.eclipse.emf.common.util.Diagnostic#getCode()
	 * @generated
	 */
	public static final String DIAGNOSTIC_SOURCE = "org.w3._2001.schema";

	/**
	 * A constant with a fixed name that can be used as the base value for additional hand written constants.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final int GENERATED_DIAGNOSTIC_CODE_COUNT = 0;

	/**
	 * A constant with a fixed name that can be used as the base value for additional hand written constants in a derived class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static final int DIAGNOSTIC_CODE_COUNT = GENERATED_DIAGNOSTIC_CODE_COUNT;

	/**
	 * The cached base package validator.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected XMLTypeValidator xmlTypeValidator;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SchemaValidator() {
		super();
		xmlTypeValidator = XMLTypeValidator.INSTANCE;
	}

	/**
	 * Returns the package of this validator switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EPackage getEPackage() {
	  return SchemaPackage.eINSTANCE;
	}

	/**
	 * Calls <code>validateXXX</code> for the corresponding classifier of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected boolean validate(int classifierID, Object value, DiagnosticChain diagnostics, Map<Object, Object> context) {
		switch (classifierID) {
			case SchemaPackage.ALL:
				return validateAll((All)value, diagnostics, context);
			case SchemaPackage.ANNOTATED:
				return validateAnnotated((Annotated)value, diagnostics, context);
			case SchemaPackage.ANNOTATION_TYPE:
				return validateAnnotationType((AnnotationType)value, diagnostics, context);
			case SchemaPackage.ANY_TYPE:
				return validateAnyType((AnyType)value, diagnostics, context);
			case SchemaPackage.APPINFO_TYPE:
				return validateAppinfoType((AppinfoType)value, diagnostics, context);
			case SchemaPackage.ATTRIBUTE:
				return validateAttribute((Attribute)value, diagnostics, context);
			case SchemaPackage.ATTRIBUTE_GROUP:
				return validateAttributeGroup((AttributeGroup)value, diagnostics, context);
			case SchemaPackage.ATTRIBUTE_GROUP_REF:
				return validateAttributeGroupRef((AttributeGroupRef)value, diagnostics, context);
			case SchemaPackage.COMPLEX_CONTENT_TYPE:
				return validateComplexContentType((ComplexContentType)value, diagnostics, context);
			case SchemaPackage.COMPLEX_RESTRICTION_TYPE:
				return validateComplexRestrictionType((ComplexRestrictionType)value, diagnostics, context);
			case SchemaPackage.COMPLEX_TYPE:
				return validateComplexType((ComplexType)value, diagnostics, context);
			case SchemaPackage.DOCUMENTATION_TYPE:
				return validateDocumentationType((DocumentationType)value, diagnostics, context);
			case SchemaPackage.DOCUMENT_ROOT:
				return validateDocumentRoot((DocumentRoot)value, diagnostics, context);
			case SchemaPackage.ELEMENT:
				return validateElement((Element)value, diagnostics, context);
			case SchemaPackage.EXPLICIT_GROUP:
				return validateExplicitGroup((ExplicitGroup)value, diagnostics, context);
			case SchemaPackage.EXTENSION_TYPE:
				return validateExtensionType((ExtensionType)value, diagnostics, context);
			case SchemaPackage.FACET:
				return validateFacet((Facet)value, diagnostics, context);
			case SchemaPackage.FIELD_TYPE:
				return validateFieldType((FieldType)value, diagnostics, context);
			case SchemaPackage.GROUP:
				return validateGroup((Group)value, diagnostics, context);
			case SchemaPackage.GROUP_REF:
				return validateGroupRef((GroupRef)value, diagnostics, context);
			case SchemaPackage.IMPORT_TYPE:
				return validateImportType((ImportType)value, diagnostics, context);
			case SchemaPackage.INCLUDE_TYPE:
				return validateIncludeType((IncludeType)value, diagnostics, context);
			case SchemaPackage.KEYBASE:
				return validateKeybase((Keybase)value, diagnostics, context);
			case SchemaPackage.KEYREF_TYPE:
				return validateKeyrefType((KeyrefType)value, diagnostics, context);
			case SchemaPackage.LIST_TYPE:
				return validateListType((ListType)value, diagnostics, context);
			case SchemaPackage.LOCAL_COMPLEX_TYPE:
				return validateLocalComplexType((LocalComplexType)value, diagnostics, context);
			case SchemaPackage.LOCAL_ELEMENT:
				return validateLocalElement((LocalElement)value, diagnostics, context);
			case SchemaPackage.LOCAL_SIMPLE_TYPE:
				return validateLocalSimpleType((LocalSimpleType)value, diagnostics, context);
			case SchemaPackage.NAMED_ATTRIBUTE_GROUP:
				return validateNamedAttributeGroup((NamedAttributeGroup)value, diagnostics, context);
			case SchemaPackage.NAMED_GROUP:
				return validateNamedGroup((NamedGroup)value, diagnostics, context);
			case SchemaPackage.NARROW_MAX_MIN:
				return validateNarrowMaxMin((NarrowMaxMin)value, diagnostics, context);
			case SchemaPackage.NO_FIXED_FACET:
				return validateNoFixedFacet((NoFixedFacet)value, diagnostics, context);
			case SchemaPackage.NOTATION_TYPE:
				return validateNotationType((NotationType)value, diagnostics, context);
			case SchemaPackage.NUM_FACET:
				return validateNumFacet((NumFacet)value, diagnostics, context);
			case SchemaPackage.OPEN_ATTRS:
				return validateOpenAttrs((OpenAttrs)value, diagnostics, context);
			case SchemaPackage.PATTERN_TYPE:
				return validatePatternType((PatternType)value, diagnostics, context);
			case SchemaPackage.REAL_GROUP:
				return validateRealGroup((RealGroup)value, diagnostics, context);
			case SchemaPackage.REDEFINE_TYPE:
				return validateRedefineType((RedefineType)value, diagnostics, context);
			case SchemaPackage.RESTRICTION_TYPE:
				return validateRestrictionType((RestrictionType)value, diagnostics, context);
			case SchemaPackage.RESTRICTION_TYPE1:
				return validateRestrictionType1((RestrictionType1)value, diagnostics, context);
			case SchemaPackage.SCHEMA_TYPE:
				return validateSchemaType((SchemaType)value, diagnostics, context);
			case SchemaPackage.SELECTOR_TYPE:
				return validateSelectorType((SelectorType)value, diagnostics, context);
			case SchemaPackage.SIMPLE_CONTENT_TYPE:
				return validateSimpleContentType((SimpleContentType)value, diagnostics, context);
			case SchemaPackage.SIMPLE_EXPLICIT_GROUP:
				return validateSimpleExplicitGroup((SimpleExplicitGroup)value, diagnostics, context);
			case SchemaPackage.SIMPLE_EXTENSION_TYPE:
				return validateSimpleExtensionType((SimpleExtensionType)value, diagnostics, context);
			case SchemaPackage.SIMPLE_RESTRICTION_TYPE:
				return validateSimpleRestrictionType((SimpleRestrictionType)value, diagnostics, context);
			case SchemaPackage.SIMPLE_TYPE:
				return validateSimpleType((SimpleType)value, diagnostics, context);
			case SchemaPackage.TOP_LEVEL_ATTRIBUTE:
				return validateTopLevelAttribute((TopLevelAttribute)value, diagnostics, context);
			case SchemaPackage.TOP_LEVEL_COMPLEX_TYPE:
				return validateTopLevelComplexType((TopLevelComplexType)value, diagnostics, context);
			case SchemaPackage.TOP_LEVEL_ELEMENT:
				return validateTopLevelElement((TopLevelElement)value, diagnostics, context);
			case SchemaPackage.TOP_LEVEL_SIMPLE_TYPE:
				return validateTopLevelSimpleType((TopLevelSimpleType)value, diagnostics, context);
			case SchemaPackage.TOTAL_DIGITS_TYPE:
				return validateTotalDigitsType((TotalDigitsType)value, diagnostics, context);
			case SchemaPackage.UNION_TYPE:
				return validateUnionType((UnionType)value, diagnostics, context);
			case SchemaPackage.WHITE_SPACE_TYPE:
				return validateWhiteSpaceType((WhiteSpaceType)value, diagnostics, context);
			case SchemaPackage.WILDCARD:
				return validateWildcard((Wildcard)value, diagnostics, context);
			case SchemaPackage.ALL_NNI_MEMBER1:
				return validateAllNNIMember1((AllNNIMember1)value, diagnostics, context);
			case SchemaPackage.BLOCK_SET_MEMBER0:
				return validateBlockSetMember0((BlockSetMember0)value, diagnostics, context);
			case SchemaPackage.BLOCK_SET_MEMBER1_ITEM:
				return validateBlockSetMember1Item((BlockSetMember1Item)value, diagnostics, context);
			case SchemaPackage.DERIVATION_CONTROL:
				return validateDerivationControl((DerivationControl)value, diagnostics, context);
			case SchemaPackage.DERIVATION_SET_MEMBER0:
				return validateDerivationSetMember0((DerivationSetMember0)value, diagnostics, context);
			case SchemaPackage.FORM_CHOICE:
				return validateFormChoice((FormChoice)value, diagnostics, context);
			case SchemaPackage.FULL_DERIVATION_SET_MEMBER0:
				return validateFullDerivationSetMember0((FullDerivationSetMember0)value, diagnostics, context);
			case SchemaPackage.NAMESPACE_LIST_MEMBER0:
				return validateNamespaceListMember0((NamespaceListMember0)value, diagnostics, context);
			case SchemaPackage.NAMESPACE_LIST_MEMBER1_ITEM_MEMBER1:
				return validateNamespaceListMember1ItemMember1((NamespaceListMember1ItemMember1)value, diagnostics, context);
			case SchemaPackage.PROCESS_CONTENTS_TYPE:
				return validateProcessContentsType((ProcessContentsType)value, diagnostics, context);
			case SchemaPackage.REDUCED_DERIVATION_CONTROL:
				return validateReducedDerivationControl((ReducedDerivationControl)value, diagnostics, context);
			case SchemaPackage.SIMPLE_DERIVATION_SET_MEMBER0:
				return validateSimpleDerivationSetMember0((SimpleDerivationSetMember0)value, diagnostics, context);
			case SchemaPackage.SIMPLE_DERIVATION_SET_MEMBER1_ITEM:
				return validateSimpleDerivationSetMember1Item((SimpleDerivationSetMember1Item)value, diagnostics, context);
			case SchemaPackage.TYPE_DERIVATION_CONTROL:
				return validateTypeDerivationControl((TypeDerivationControl)value, diagnostics, context);
			case SchemaPackage.USE_TYPE:
				return validateUseType((UseType)value, diagnostics, context);
			case SchemaPackage.ALL_NNI:
				return validateAllNNI(value, diagnostics, context);
			case SchemaPackage.ALL_NNI_MEMBER1_OBJECT:
				return validateAllNNIMember1Object((AllNNIMember1)value, diagnostics, context);
			case SchemaPackage.BLOCK_SET:
				return validateBlockSet(value, diagnostics, context);
			case SchemaPackage.BLOCK_SET_MEMBER0_OBJECT:
				return validateBlockSetMember0Object((BlockSetMember0)value, diagnostics, context);
			case SchemaPackage.BLOCK_SET_MEMBER1:
				return validateBlockSetMember1((List<?>)value, diagnostics, context);
			case SchemaPackage.BLOCK_SET_MEMBER1_ITEM_OBJECT:
				return validateBlockSetMember1ItemObject((BlockSetMember1Item)value, diagnostics, context);
			case SchemaPackage.DERIVATION_CONTROL_OBJECT:
				return validateDerivationControlObject((DerivationControl)value, diagnostics, context);
			case SchemaPackage.DERIVATION_SET:
				return validateDerivationSet(value, diagnostics, context);
			case SchemaPackage.DERIVATION_SET_MEMBER0_OBJECT:
				return validateDerivationSetMember0Object((DerivationSetMember0)value, diagnostics, context);
			case SchemaPackage.DERIVATION_SET_MEMBER1:
				return validateDerivationSetMember1((List<?>)value, diagnostics, context);
			case SchemaPackage.FORM_CHOICE_OBJECT:
				return validateFormChoiceObject((FormChoice)value, diagnostics, context);
			case SchemaPackage.FULL_DERIVATION_SET:
				return validateFullDerivationSet(value, diagnostics, context);
			case SchemaPackage.FULL_DERIVATION_SET_MEMBER0_OBJECT:
				return validateFullDerivationSetMember0Object((FullDerivationSetMember0)value, diagnostics, context);
			case SchemaPackage.FULL_DERIVATION_SET_MEMBER1:
				return validateFullDerivationSetMember1((List<?>)value, diagnostics, context);
			case SchemaPackage.MEMBER_TYPES_TYPE:
				return validateMemberTypesType((List<?>)value, diagnostics, context);
			case SchemaPackage.NAMESPACE_LIST:
				return validateNamespaceList(value, diagnostics, context);
			case SchemaPackage.NAMESPACE_LIST_MEMBER0_OBJECT:
				return validateNamespaceListMember0Object((NamespaceListMember0)value, diagnostics, context);
			case SchemaPackage.NAMESPACE_LIST_MEMBER1:
				return validateNamespaceListMember1((List<?>)value, diagnostics, context);
			case SchemaPackage.NAMESPACE_LIST_MEMBER1_ITEM:
				return validateNamespaceListMember1Item(value, diagnostics, context);
			case SchemaPackage.NAMESPACE_LIST_MEMBER1_ITEM_MEMBER1_OBJECT:
				return validateNamespaceListMember1ItemMember1Object((NamespaceListMember1ItemMember1)value, diagnostics, context);
			case SchemaPackage.PROCESS_CONTENTS_TYPE_OBJECT:
				return validateProcessContentsTypeObject((ProcessContentsType)value, diagnostics, context);
			case SchemaPackage.PUBLIC:
				return validatePublic((String)value, diagnostics, context);
			case SchemaPackage.REDUCED_DERIVATION_CONTROL_OBJECT:
				return validateReducedDerivationControlObject((ReducedDerivationControl)value, diagnostics, context);
			case SchemaPackage.SIMPLE_DERIVATION_SET:
				return validateSimpleDerivationSet(value, diagnostics, context);
			case SchemaPackage.SIMPLE_DERIVATION_SET_MEMBER0_OBJECT:
				return validateSimpleDerivationSetMember0Object((SimpleDerivationSetMember0)value, diagnostics, context);
			case SchemaPackage.SIMPLE_DERIVATION_SET_MEMBER1:
				return validateSimpleDerivationSetMember1((List<?>)value, diagnostics, context);
			case SchemaPackage.SIMPLE_DERIVATION_SET_MEMBER1_ITEM_OBJECT:
				return validateSimpleDerivationSetMember1ItemObject((SimpleDerivationSetMember1Item)value, diagnostics, context);
			case SchemaPackage.TYPE_DERIVATION_CONTROL_OBJECT:
				return validateTypeDerivationControlObject((TypeDerivationControl)value, diagnostics, context);
			case SchemaPackage.USE_TYPE_OBJECT:
				return validateUseTypeObject((UseType)value, diagnostics, context);
			case SchemaPackage.XPATH_TYPE:
				return validateXpathType((String)value, diagnostics, context);
			case SchemaPackage.XPATH_TYPE1:
				return validateXpathType1((String)value, diagnostics, context);
			default:
				return true;
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateAll(All all, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(all, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateAnnotated(Annotated annotated, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(annotated, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateAnnotationType(AnnotationType annotationType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(annotationType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateAnyType(AnyType anyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(anyType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateAppinfoType(AppinfoType appinfoType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(appinfoType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateAttribute(Attribute attribute, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(attribute, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateAttributeGroup(AttributeGroup attributeGroup, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(attributeGroup, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateAttributeGroupRef(AttributeGroupRef attributeGroupRef, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(attributeGroupRef, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateComplexContentType(ComplexContentType complexContentType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(complexContentType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateComplexRestrictionType(ComplexRestrictionType complexRestrictionType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(complexRestrictionType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateComplexType(ComplexType complexType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(complexType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateDocumentationType(DocumentationType documentationType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(documentationType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateDocumentRoot(DocumentRoot documentRoot, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(documentRoot, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateElement(Element element, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(element, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateExplicitGroup(ExplicitGroup explicitGroup, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(explicitGroup, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateExtensionType(ExtensionType extensionType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(extensionType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateFacet(Facet facet, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(facet, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateFieldType(FieldType fieldType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(fieldType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateGroup(Group group, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(group, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateGroupRef(GroupRef groupRef, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(groupRef, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateImportType(ImportType importType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(importType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateIncludeType(IncludeType includeType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(includeType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateKeybase(Keybase keybase, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(keybase, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateKeyrefType(KeyrefType keyrefType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(keyrefType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateListType(ListType listType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(listType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateLocalComplexType(LocalComplexType localComplexType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(localComplexType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateLocalElement(LocalElement localElement, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(localElement, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateLocalSimpleType(LocalSimpleType localSimpleType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(localSimpleType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateNamedAttributeGroup(NamedAttributeGroup namedAttributeGroup, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(namedAttributeGroup, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateNamedGroup(NamedGroup namedGroup, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(namedGroup, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateNarrowMaxMin(NarrowMaxMin narrowMaxMin, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(narrowMaxMin, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateNoFixedFacet(NoFixedFacet noFixedFacet, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(noFixedFacet, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateNotationType(NotationType notationType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(notationType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateNumFacet(NumFacet numFacet, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(numFacet, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateOpenAttrs(OpenAttrs openAttrs, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(openAttrs, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validatePatternType(PatternType patternType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(patternType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateRealGroup(RealGroup realGroup, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(realGroup, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateRedefineType(RedefineType redefineType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(redefineType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateRestrictionType(RestrictionType restrictionType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(restrictionType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateRestrictionType1(RestrictionType1 restrictionType1, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(restrictionType1, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateSchemaType(SchemaType schemaType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(schemaType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateSelectorType(SelectorType selectorType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(selectorType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateSimpleContentType(SimpleContentType simpleContentType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(simpleContentType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateSimpleExplicitGroup(SimpleExplicitGroup simpleExplicitGroup, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(simpleExplicitGroup, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateSimpleExtensionType(SimpleExtensionType simpleExtensionType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(simpleExtensionType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateSimpleRestrictionType(SimpleRestrictionType simpleRestrictionType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(simpleRestrictionType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateSimpleType(SimpleType simpleType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(simpleType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateTopLevelAttribute(TopLevelAttribute topLevelAttribute, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(topLevelAttribute, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateTopLevelComplexType(TopLevelComplexType topLevelComplexType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(topLevelComplexType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateTopLevelElement(TopLevelElement topLevelElement, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(topLevelElement, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateTopLevelSimpleType(TopLevelSimpleType topLevelSimpleType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(topLevelSimpleType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateTotalDigitsType(TotalDigitsType totalDigitsType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(totalDigitsType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateUnionType(UnionType unionType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(unionType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateWhiteSpaceType(WhiteSpaceType whiteSpaceType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(whiteSpaceType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateWildcard(Wildcard wildcard, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(wildcard, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateAllNNIMember1(AllNNIMember1 allNNIMember1, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateBlockSetMember0(BlockSetMember0 blockSetMember0, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateBlockSetMember1Item(BlockSetMember1Item blockSetMember1Item, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateDerivationControl(DerivationControl derivationControl, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateDerivationSetMember0(DerivationSetMember0 derivationSetMember0, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateFormChoice(FormChoice formChoice, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateFullDerivationSetMember0(FullDerivationSetMember0 fullDerivationSetMember0, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateNamespaceListMember0(NamespaceListMember0 namespaceListMember0, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateNamespaceListMember1ItemMember1(NamespaceListMember1ItemMember1 namespaceListMember1ItemMember1, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateProcessContentsType(ProcessContentsType processContentsType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateReducedDerivationControl(ReducedDerivationControl reducedDerivationControl, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateSimpleDerivationSetMember0(SimpleDerivationSetMember0 simpleDerivationSetMember0, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateSimpleDerivationSetMember1Item(SimpleDerivationSetMember1Item simpleDerivationSetMember1Item, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateTypeDerivationControl(TypeDerivationControl typeDerivationControl, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateUseType(UseType useType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateAllNNI(Object allNNI, DiagnosticChain diagnostics, Map<Object, Object> context) {
		boolean result = validateAllNNI_MemberTypes(allNNI, diagnostics, context);
		return result;
	}

	/**
	 * Validates the MemberTypes constraint of '<em>All NNI</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateAllNNI_MemberTypes(Object allNNI, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (diagnostics != null) {
			BasicDiagnostic tempDiagnostics = new BasicDiagnostic();
			if (XMLTypePackage.Literals.NON_NEGATIVE_INTEGER.isInstance(allNNI)) {
				if (xmlTypeValidator.validateNonNegativeInteger((BigInteger)allNNI, tempDiagnostics, context)) return true;
			}
			if (SchemaPackage.Literals.ALL_NNI_MEMBER1.isInstance(allNNI)) {
				if (validateAllNNIMember1((AllNNIMember1)allNNI, tempDiagnostics, context)) return true;
			}
			for (Diagnostic diagnostic : tempDiagnostics.getChildren()) {
				diagnostics.add(diagnostic);
			}
		}
		else {
			if (XMLTypePackage.Literals.NON_NEGATIVE_INTEGER.isInstance(allNNI)) {
				if (xmlTypeValidator.validateNonNegativeInteger((BigInteger)allNNI, null, context)) return true;
			}
			if (SchemaPackage.Literals.ALL_NNI_MEMBER1.isInstance(allNNI)) {
				if (validateAllNNIMember1((AllNNIMember1)allNNI, null, context)) return true;
			}
		}
		return false;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateAllNNIMember1Object(AllNNIMember1 allNNIMember1Object, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateBlockSet(Object blockSet, DiagnosticChain diagnostics, Map<Object, Object> context) {
		boolean result = validateBlockSet_MemberTypes(blockSet, diagnostics, context);
		return result;
	}

	/**
	 * Validates the MemberTypes constraint of '<em>Block Set</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateBlockSet_MemberTypes(Object blockSet, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (diagnostics != null) {
			BasicDiagnostic tempDiagnostics = new BasicDiagnostic();
			if (SchemaPackage.Literals.BLOCK_SET_MEMBER0.isInstance(blockSet)) {
				if (validateBlockSetMember0((BlockSetMember0)blockSet, tempDiagnostics, context)) return true;
			}
			if (SchemaPackage.Literals.BLOCK_SET_MEMBER1.isInstance(blockSet)) {
				if (validateBlockSetMember1((List<?>)blockSet, tempDiagnostics, context)) return true;
			}
			for (Diagnostic diagnostic : tempDiagnostics.getChildren()) {
				diagnostics.add(diagnostic);
			}
		}
		else {
			if (SchemaPackage.Literals.BLOCK_SET_MEMBER0.isInstance(blockSet)) {
				if (validateBlockSetMember0((BlockSetMember0)blockSet, null, context)) return true;
			}
			if (SchemaPackage.Literals.BLOCK_SET_MEMBER1.isInstance(blockSet)) {
				if (validateBlockSetMember1((List<?>)blockSet, null, context)) return true;
			}
		}
		return false;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateBlockSetMember0Object(BlockSetMember0 blockSetMember0Object, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateBlockSetMember1(List<?> blockSetMember1, DiagnosticChain diagnostics, Map<Object, Object> context) {
		boolean result = validateBlockSetMember1_ItemType(blockSetMember1, diagnostics, context);
		return result;
	}

	/**
	 * Validates the ItemType constraint of '<em>Block Set Member1</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateBlockSetMember1_ItemType(List<?> blockSetMember1, DiagnosticChain diagnostics, Map<Object, Object> context) {
		boolean result = true;
		for (Iterator<?> i = blockSetMember1.iterator(); i.hasNext() && (result || diagnostics != null); ) {
			Object item = i.next();
			if (SchemaPackage.Literals.BLOCK_SET_MEMBER1_ITEM.isInstance(item)) {
				result &= validateBlockSetMember1Item((BlockSetMember1Item)item, diagnostics, context);
			}
			else {
				result = false;
				reportDataValueTypeViolation(SchemaPackage.Literals.BLOCK_SET_MEMBER1_ITEM, item, diagnostics, context);
			}
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateBlockSetMember1ItemObject(BlockSetMember1Item blockSetMember1ItemObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateDerivationControlObject(DerivationControl derivationControlObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateDerivationSet(Object derivationSet, DiagnosticChain diagnostics, Map<Object, Object> context) {
		boolean result = validateDerivationSet_MemberTypes(derivationSet, diagnostics, context);
		return result;
	}

	/**
	 * Validates the MemberTypes constraint of '<em>Derivation Set</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateDerivationSet_MemberTypes(Object derivationSet, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (diagnostics != null) {
			BasicDiagnostic tempDiagnostics = new BasicDiagnostic();
			if (SchemaPackage.Literals.DERIVATION_SET_MEMBER0.isInstance(derivationSet)) {
				if (validateDerivationSetMember0((DerivationSetMember0)derivationSet, tempDiagnostics, context)) return true;
			}
			if (SchemaPackage.Literals.DERIVATION_SET_MEMBER1.isInstance(derivationSet)) {
				if (validateDerivationSetMember1((List<?>)derivationSet, tempDiagnostics, context)) return true;
			}
			for (Diagnostic diagnostic : tempDiagnostics.getChildren()) {
				diagnostics.add(diagnostic);
			}
		}
		else {
			if (SchemaPackage.Literals.DERIVATION_SET_MEMBER0.isInstance(derivationSet)) {
				if (validateDerivationSetMember0((DerivationSetMember0)derivationSet, null, context)) return true;
			}
			if (SchemaPackage.Literals.DERIVATION_SET_MEMBER1.isInstance(derivationSet)) {
				if (validateDerivationSetMember1((List<?>)derivationSet, null, context)) return true;
			}
		}
		return false;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateDerivationSetMember0Object(DerivationSetMember0 derivationSetMember0Object, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateDerivationSetMember1(List<?> derivationSetMember1, DiagnosticChain diagnostics, Map<Object, Object> context) {
		boolean result = validateDerivationSetMember1_ItemType(derivationSetMember1, diagnostics, context);
		return result;
	}

	/**
	 * Validates the ItemType constraint of '<em>Derivation Set Member1</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateDerivationSetMember1_ItemType(List<?> derivationSetMember1, DiagnosticChain diagnostics, Map<Object, Object> context) {
		boolean result = true;
		for (Iterator<?> i = derivationSetMember1.iterator(); i.hasNext() && (result || diagnostics != null); ) {
			Object item = i.next();
			if (SchemaPackage.Literals.REDUCED_DERIVATION_CONTROL.isInstance(item)) {
				result &= validateReducedDerivationControl((ReducedDerivationControl)item, diagnostics, context);
			}
			else {
				result = false;
				reportDataValueTypeViolation(SchemaPackage.Literals.REDUCED_DERIVATION_CONTROL, item, diagnostics, context);
			}
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateFormChoiceObject(FormChoice formChoiceObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateFullDerivationSet(Object fullDerivationSet, DiagnosticChain diagnostics, Map<Object, Object> context) {
		boolean result = validateFullDerivationSet_MemberTypes(fullDerivationSet, diagnostics, context);
		return result;
	}

	/**
	 * Validates the MemberTypes constraint of '<em>Full Derivation Set</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateFullDerivationSet_MemberTypes(Object fullDerivationSet, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (diagnostics != null) {
			BasicDiagnostic tempDiagnostics = new BasicDiagnostic();
			if (SchemaPackage.Literals.FULL_DERIVATION_SET_MEMBER0.isInstance(fullDerivationSet)) {
				if (validateFullDerivationSetMember0((FullDerivationSetMember0)fullDerivationSet, tempDiagnostics, context)) return true;
			}
			if (SchemaPackage.Literals.FULL_DERIVATION_SET_MEMBER1.isInstance(fullDerivationSet)) {
				if (validateFullDerivationSetMember1((List<?>)fullDerivationSet, tempDiagnostics, context)) return true;
			}
			for (Diagnostic diagnostic : tempDiagnostics.getChildren()) {
				diagnostics.add(diagnostic);
			}
		}
		else {
			if (SchemaPackage.Literals.FULL_DERIVATION_SET_MEMBER0.isInstance(fullDerivationSet)) {
				if (validateFullDerivationSetMember0((FullDerivationSetMember0)fullDerivationSet, null, context)) return true;
			}
			if (SchemaPackage.Literals.FULL_DERIVATION_SET_MEMBER1.isInstance(fullDerivationSet)) {
				if (validateFullDerivationSetMember1((List<?>)fullDerivationSet, null, context)) return true;
			}
		}
		return false;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateFullDerivationSetMember0Object(FullDerivationSetMember0 fullDerivationSetMember0Object, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateFullDerivationSetMember1(List<?> fullDerivationSetMember1, DiagnosticChain diagnostics, Map<Object, Object> context) {
		boolean result = validateFullDerivationSetMember1_ItemType(fullDerivationSetMember1, diagnostics, context);
		return result;
	}

	/**
	 * Validates the ItemType constraint of '<em>Full Derivation Set Member1</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateFullDerivationSetMember1_ItemType(List<?> fullDerivationSetMember1, DiagnosticChain diagnostics, Map<Object, Object> context) {
		boolean result = true;
		for (Iterator<?> i = fullDerivationSetMember1.iterator(); i.hasNext() && (result || diagnostics != null); ) {
			Object item = i.next();
			if (SchemaPackage.Literals.TYPE_DERIVATION_CONTROL.isInstance(item)) {
				result &= validateTypeDerivationControl((TypeDerivationControl)item, diagnostics, context);
			}
			else {
				result = false;
				reportDataValueTypeViolation(SchemaPackage.Literals.TYPE_DERIVATION_CONTROL, item, diagnostics, context);
			}
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateMemberTypesType(List<?> memberTypesType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		boolean result = validateMemberTypesType_ItemType(memberTypesType, diagnostics, context);
		return result;
	}

	/**
	 * Validates the ItemType constraint of '<em>Member Types Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateMemberTypesType_ItemType(List<?> memberTypesType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		boolean result = true;
		for (Iterator<?> i = memberTypesType.iterator(); i.hasNext() && (result || diagnostics != null); ) {
			Object item = i.next();
			if (XMLTypePackage.Literals.QNAME.isInstance(item)) {
				result &= xmlTypeValidator.validateQName((QName)item, diagnostics, context);
			}
			else {
				result = false;
				reportDataValueTypeViolation(XMLTypePackage.Literals.QNAME, item, diagnostics, context);
			}
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateNamespaceList(Object namespaceList, DiagnosticChain diagnostics, Map<Object, Object> context) {
		boolean result = validateNamespaceList_MemberTypes(namespaceList, diagnostics, context);
		return result;
	}

	/**
	 * Validates the MemberTypes constraint of '<em>Namespace List</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateNamespaceList_MemberTypes(Object namespaceList, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (diagnostics != null) {
			BasicDiagnostic tempDiagnostics = new BasicDiagnostic();
			if (SchemaPackage.Literals.NAMESPACE_LIST_MEMBER0.isInstance(namespaceList)) {
				if (validateNamespaceListMember0((NamespaceListMember0)namespaceList, tempDiagnostics, context)) return true;
			}
			if (SchemaPackage.Literals.NAMESPACE_LIST_MEMBER1.isInstance(namespaceList)) {
				if (validateNamespaceListMember1((List<?>)namespaceList, tempDiagnostics, context)) return true;
			}
			for (Diagnostic diagnostic : tempDiagnostics.getChildren()) {
				diagnostics.add(diagnostic);
			}
		}
		else {
			if (SchemaPackage.Literals.NAMESPACE_LIST_MEMBER0.isInstance(namespaceList)) {
				if (validateNamespaceListMember0((NamespaceListMember0)namespaceList, null, context)) return true;
			}
			if (SchemaPackage.Literals.NAMESPACE_LIST_MEMBER1.isInstance(namespaceList)) {
				if (validateNamespaceListMember1((List<?>)namespaceList, null, context)) return true;
			}
		}
		return false;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateNamespaceListMember0Object(NamespaceListMember0 namespaceListMember0Object, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateNamespaceListMember1(List<?> namespaceListMember1, DiagnosticChain diagnostics, Map<Object, Object> context) {
		boolean result = validateNamespaceListMember1_ItemType(namespaceListMember1, diagnostics, context);
		return result;
	}

	/**
	 * Validates the ItemType constraint of '<em>Namespace List Member1</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateNamespaceListMember1_ItemType(List<?> namespaceListMember1, DiagnosticChain diagnostics, Map<Object, Object> context) {
		boolean result = true;
		for (Iterator<?> i = namespaceListMember1.iterator(); i.hasNext() && (result || diagnostics != null); ) {
			Object item = i.next();
			if (SchemaPackage.Literals.NAMESPACE_LIST_MEMBER1_ITEM.isInstance(item)) {
				result &= validateNamespaceListMember1Item(item, diagnostics, context);
			}
			else {
				result = false;
				reportDataValueTypeViolation(SchemaPackage.Literals.NAMESPACE_LIST_MEMBER1_ITEM, item, diagnostics, context);
			}
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateNamespaceListMember1Item(Object namespaceListMember1Item, DiagnosticChain diagnostics, Map<Object, Object> context) {
		boolean result = validateNamespaceListMember1Item_MemberTypes(namespaceListMember1Item, diagnostics, context);
		return result;
	}

	/**
	 * Validates the MemberTypes constraint of '<em>Namespace List Member1 Item</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateNamespaceListMember1Item_MemberTypes(Object namespaceListMember1Item, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (diagnostics != null) {
			BasicDiagnostic tempDiagnostics = new BasicDiagnostic();
			if (XMLTypePackage.Literals.ANY_URI.isInstance(namespaceListMember1Item)) {
				if (xmlTypeValidator.validateAnyURI((String)namespaceListMember1Item, tempDiagnostics, context)) return true;
			}
			if (SchemaPackage.Literals.NAMESPACE_LIST_MEMBER1_ITEM_MEMBER1.isInstance(namespaceListMember1Item)) {
				if (validateNamespaceListMember1ItemMember1((NamespaceListMember1ItemMember1)namespaceListMember1Item, tempDiagnostics, context)) return true;
			}
			for (Diagnostic diagnostic : tempDiagnostics.getChildren()) {
				diagnostics.add(diagnostic);
			}
		}
		else {
			if (XMLTypePackage.Literals.ANY_URI.isInstance(namespaceListMember1Item)) {
				if (xmlTypeValidator.validateAnyURI((String)namespaceListMember1Item, null, context)) return true;
			}
			if (SchemaPackage.Literals.NAMESPACE_LIST_MEMBER1_ITEM_MEMBER1.isInstance(namespaceListMember1Item)) {
				if (validateNamespaceListMember1ItemMember1((NamespaceListMember1ItemMember1)namespaceListMember1Item, null, context)) return true;
			}
		}
		return false;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateNamespaceListMember1ItemMember1Object(NamespaceListMember1ItemMember1 namespaceListMember1ItemMember1Object, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateProcessContentsTypeObject(ProcessContentsType processContentsTypeObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validatePublic(String public_, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateReducedDerivationControlObject(ReducedDerivationControl reducedDerivationControlObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateSimpleDerivationSet(Object simpleDerivationSet, DiagnosticChain diagnostics, Map<Object, Object> context) {
		boolean result = validateSimpleDerivationSet_MemberTypes(simpleDerivationSet, diagnostics, context);
		return result;
	}

	/**
	 * Validates the MemberTypes constraint of '<em>Simple Derivation Set</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateSimpleDerivationSet_MemberTypes(Object simpleDerivationSet, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (diagnostics != null) {
			BasicDiagnostic tempDiagnostics = new BasicDiagnostic();
			if (SchemaPackage.Literals.SIMPLE_DERIVATION_SET_MEMBER0.isInstance(simpleDerivationSet)) {
				if (validateSimpleDerivationSetMember0((SimpleDerivationSetMember0)simpleDerivationSet, tempDiagnostics, context)) return true;
			}
			if (SchemaPackage.Literals.SIMPLE_DERIVATION_SET_MEMBER1.isInstance(simpleDerivationSet)) {
				if (validateSimpleDerivationSetMember1((List<?>)simpleDerivationSet, tempDiagnostics, context)) return true;
			}
			for (Diagnostic diagnostic : tempDiagnostics.getChildren()) {
				diagnostics.add(diagnostic);
			}
		}
		else {
			if (SchemaPackage.Literals.SIMPLE_DERIVATION_SET_MEMBER0.isInstance(simpleDerivationSet)) {
				if (validateSimpleDerivationSetMember0((SimpleDerivationSetMember0)simpleDerivationSet, null, context)) return true;
			}
			if (SchemaPackage.Literals.SIMPLE_DERIVATION_SET_MEMBER1.isInstance(simpleDerivationSet)) {
				if (validateSimpleDerivationSetMember1((List<?>)simpleDerivationSet, null, context)) return true;
			}
		}
		return false;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateSimpleDerivationSetMember0Object(SimpleDerivationSetMember0 simpleDerivationSetMember0Object, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateSimpleDerivationSetMember1(List<?> simpleDerivationSetMember1, DiagnosticChain diagnostics, Map<Object, Object> context) {
		boolean result = validateSimpleDerivationSetMember1_ItemType(simpleDerivationSetMember1, diagnostics, context);
		return result;
	}

	/**
	 * Validates the ItemType constraint of '<em>Simple Derivation Set Member1</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateSimpleDerivationSetMember1_ItemType(List<?> simpleDerivationSetMember1, DiagnosticChain diagnostics, Map<Object, Object> context) {
		boolean result = true;
		for (Iterator<?> i = simpleDerivationSetMember1.iterator(); i.hasNext() && (result || diagnostics != null); ) {
			Object item = i.next();
			if (SchemaPackage.Literals.SIMPLE_DERIVATION_SET_MEMBER1_ITEM.isInstance(item)) {
				result &= validateSimpleDerivationSetMember1Item((SimpleDerivationSetMember1Item)item, diagnostics, context);
			}
			else {
				result = false;
				reportDataValueTypeViolation(SchemaPackage.Literals.SIMPLE_DERIVATION_SET_MEMBER1_ITEM, item, diagnostics, context);
			}
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateSimpleDerivationSetMember1ItemObject(SimpleDerivationSetMember1Item simpleDerivationSetMember1ItemObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateTypeDerivationControlObject(TypeDerivationControl typeDerivationControlObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateUseTypeObject(UseType useTypeObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateXpathType(String xpathType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		boolean result = validateXpathType_Pattern(xpathType, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @see #validateXpathType_Pattern
	 */
	public static final  PatternMatcher [][] XPATH_TYPE__PATTERN__VALUES =
		new PatternMatcher [][] {
			new PatternMatcher [] {
				XMLTypeUtil.createPatternMatcher("(\\.//)?((((child::)?((\\i\\c*:)?(\\i\\c*|\\*)))|\\.)/)*((((child::)?((\\i\\c*:)?(\\i\\c*|\\*)))|\\.)|((attribute::|@)((\\i\\c*:)?(\\i\\c*|\\*))))(\\|(\\.//)?((((child::)?((\\i\\c*:)?(\\i\\c*|\\*)))|\\.)/)*((((child::)?((\\i\\c*:)?(\\i\\c*|\\*)))|\\.)|((attribute::|@)((\\i\\c*:)?(\\i\\c*|\\*)))))*")
			}
		};

	/**
	 * Validates the Pattern constraint of '<em>Xpath Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateXpathType_Pattern(String xpathType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validatePattern(SchemaPackage.Literals.XPATH_TYPE, xpathType, XPATH_TYPE__PATTERN__VALUES, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateXpathType1(String xpathType1, DiagnosticChain diagnostics, Map<Object, Object> context) {
		boolean result = validateXpathType1_Pattern(xpathType1, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @see #validateXpathType1_Pattern
	 */
	public static final  PatternMatcher [][] XPATH_TYPE1__PATTERN__VALUES =
		new PatternMatcher [][] {
			new PatternMatcher [] {
				XMLTypeUtil.createPatternMatcher("(\\.//)?(((child::)?((\\i\\c*:)?(\\i\\c*|\\*)))|\\.)(/(((child::)?((\\i\\c*:)?(\\i\\c*|\\*)))|\\.))*(\\|(\\.//)?(((child::)?((\\i\\c*:)?(\\i\\c*|\\*)))|\\.)(/(((child::)?((\\i\\c*:)?(\\i\\c*|\\*)))|\\.))*)*")
			}
		};

	/**
	 * Validates the Pattern constraint of '<em>Xpath Type1</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateXpathType1_Pattern(String xpathType1, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validatePattern(SchemaPackage.Literals.XPATH_TYPE1, xpathType1, XPATH_TYPE1__PATTERN__VALUES, diagnostics, context);
	}

	/**
	 * Returns the resource locator that will be used to fetch messages for this validator's diagnostics.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ResourceLocator getResourceLocator() {
		// TODO
		// Specialize this to return a resource locator for messages specific to this validator.
		// Ensure that you remove @generated or mark it @generated NOT
		return super.getResourceLocator();
	}

} //SchemaValidator
