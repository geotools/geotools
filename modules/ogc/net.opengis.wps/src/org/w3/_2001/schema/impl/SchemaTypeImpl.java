/**
 */
package org.w3._2001.schema.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

import org.w3._2001.schema.AnnotationType;
import org.w3._2001.schema.FormChoice;
import org.w3._2001.schema.ImportType;
import org.w3._2001.schema.IncludeType;
import org.w3._2001.schema.NamedAttributeGroup;
import org.w3._2001.schema.NamedGroup;
import org.w3._2001.schema.NotationType;
import org.w3._2001.schema.RedefineType;
import org.w3._2001.schema.SchemaFactory;
import org.w3._2001.schema.SchemaPackage;
import org.w3._2001.schema.SchemaType;
import org.w3._2001.schema.TopLevelAttribute;
import org.w3._2001.schema.TopLevelComplexType;
import org.w3._2001.schema.TopLevelElement;
import org.w3._2001.schema.TopLevelSimpleType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.w3._2001.schema.impl.SchemaTypeImpl#getGroup <em>Group</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.SchemaTypeImpl#getInclude <em>Include</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.SchemaTypeImpl#getImport <em>Import</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.SchemaTypeImpl#getRedefine <em>Redefine</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.SchemaTypeImpl#getAnnotation <em>Annotation</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.SchemaTypeImpl#getGroup1 <em>Group1</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.SchemaTypeImpl#getSimpleType <em>Simple Type</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.SchemaTypeImpl#getComplexType <em>Complex Type</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.SchemaTypeImpl#getGroup2 <em>Group2</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.SchemaTypeImpl#getAttributeGroup <em>Attribute Group</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.SchemaTypeImpl#getElement <em>Element</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.SchemaTypeImpl#getAttribute <em>Attribute</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.SchemaTypeImpl#getNotation <em>Notation</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.SchemaTypeImpl#getAnnotation1 <em>Annotation1</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.SchemaTypeImpl#getAttributeFormDefault <em>Attribute Form Default</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.SchemaTypeImpl#getBlockDefault <em>Block Default</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.SchemaTypeImpl#getElementFormDefault <em>Element Form Default</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.SchemaTypeImpl#getFinalDefault <em>Final Default</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.SchemaTypeImpl#getId <em>Id</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.SchemaTypeImpl#getLang <em>Lang</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.SchemaTypeImpl#getTargetNamespace <em>Target Namespace</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.SchemaTypeImpl#getVersion <em>Version</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SchemaTypeImpl extends OpenAttrsImpl implements SchemaType {
	/**
	 * The cached value of the '{@link #getGroup() <em>Group</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGroup()
	 * @generated
	 * @ordered
	 */
	protected FeatureMap group;

	/**
	 * The cached value of the '{@link #getGroup1() <em>Group1</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGroup1()
	 * @generated
	 * @ordered
	 */
	protected FeatureMap group1;

	/**
	 * The default value of the '{@link #getAttributeFormDefault() <em>Attribute Form Default</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAttributeFormDefault()
	 * @generated
	 * @ordered
	 */
	protected static final FormChoice ATTRIBUTE_FORM_DEFAULT_EDEFAULT = FormChoice.UNQUALIFIED;

	/**
	 * The cached value of the '{@link #getAttributeFormDefault() <em>Attribute Form Default</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAttributeFormDefault()
	 * @generated
	 * @ordered
	 */
	protected FormChoice attributeFormDefault = ATTRIBUTE_FORM_DEFAULT_EDEFAULT;

	/**
	 * This is true if the Attribute Form Default attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean attributeFormDefaultESet;

	/**
	 * The default value of the '{@link #getBlockDefault() <em>Block Default</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBlockDefault()
	 * @generated
	 * @ordered
	 */
	protected static final Object BLOCK_DEFAULT_EDEFAULT = SchemaFactory.eINSTANCE.createFromString(SchemaPackage.eINSTANCE.getBlockSet(), "");

	/**
	 * The cached value of the '{@link #getBlockDefault() <em>Block Default</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBlockDefault()
	 * @generated
	 * @ordered
	 */
	protected Object blockDefault = BLOCK_DEFAULT_EDEFAULT;

	/**
	 * This is true if the Block Default attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean blockDefaultESet;

	/**
	 * The default value of the '{@link #getElementFormDefault() <em>Element Form Default</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getElementFormDefault()
	 * @generated
	 * @ordered
	 */
	protected static final FormChoice ELEMENT_FORM_DEFAULT_EDEFAULT = FormChoice.UNQUALIFIED;

	/**
	 * The cached value of the '{@link #getElementFormDefault() <em>Element Form Default</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getElementFormDefault()
	 * @generated
	 * @ordered
	 */
	protected FormChoice elementFormDefault = ELEMENT_FORM_DEFAULT_EDEFAULT;

	/**
	 * This is true if the Element Form Default attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean elementFormDefaultESet;

	/**
	 * The default value of the '{@link #getFinalDefault() <em>Final Default</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFinalDefault()
	 * @generated
	 * @ordered
	 */
	protected static final Object FINAL_DEFAULT_EDEFAULT = SchemaFactory.eINSTANCE.createFromString(SchemaPackage.eINSTANCE.getFullDerivationSet(), "");

	/**
	 * The cached value of the '{@link #getFinalDefault() <em>Final Default</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFinalDefault()
	 * @generated
	 * @ordered
	 */
	protected Object finalDefault = FINAL_DEFAULT_EDEFAULT;

	/**
	 * This is true if the Final Default attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean finalDefaultESet;

	/**
	 * The default value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected static final String ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected String id = ID_EDEFAULT;

	/**
	 * The default value of the '{@link #getLang() <em>Lang</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLang()
	 * @generated
	 * @ordered
	 */
	protected static final String LANG_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLang() <em>Lang</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLang()
	 * @generated
	 * @ordered
	 */
	protected String lang = LANG_EDEFAULT;

	/**
	 * The default value of the '{@link #getTargetNamespace() <em>Target Namespace</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTargetNamespace()
	 * @generated
	 * @ordered
	 */
	protected static final String TARGET_NAMESPACE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getTargetNamespace() <em>Target Namespace</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTargetNamespace()
	 * @generated
	 * @ordered
	 */
	protected String targetNamespace = TARGET_NAMESPACE_EDEFAULT;

	/**
	 * The default value of the '{@link #getVersion() <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVersion()
	 * @generated
	 * @ordered
	 */
	protected static final String VERSION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getVersion() <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVersion()
	 * @generated
	 * @ordered
	 */
	protected String version = VERSION_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SchemaTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchemaPackage.Literals.SCHEMA_TYPE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FeatureMap getGroup() {
		if (group == null) {
			group = new BasicFeatureMap(this, SchemaPackage.SCHEMA_TYPE__GROUP);
		}
		return group;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<IncludeType> getInclude() {
		return getGroup().list(SchemaPackage.Literals.SCHEMA_TYPE__INCLUDE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ImportType> getImport() {
		return getGroup().list(SchemaPackage.Literals.SCHEMA_TYPE__IMPORT);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<RedefineType> getRedefine() {
		return getGroup().list(SchemaPackage.Literals.SCHEMA_TYPE__REDEFINE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<AnnotationType> getAnnotation() {
		return getGroup().list(SchemaPackage.Literals.SCHEMA_TYPE__ANNOTATION);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FeatureMap getGroup1() {
		if (group1 == null) {
			group1 = new BasicFeatureMap(this, SchemaPackage.SCHEMA_TYPE__GROUP1);
		}
		return group1;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<TopLevelSimpleType> getSimpleType() {
		return getGroup1().list(SchemaPackage.Literals.SCHEMA_TYPE__SIMPLE_TYPE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<TopLevelComplexType> getComplexType() {
		return getGroup1().list(SchemaPackage.Literals.SCHEMA_TYPE__COMPLEX_TYPE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<NamedGroup> getGroup2() {
		return getGroup1().list(SchemaPackage.Literals.SCHEMA_TYPE__GROUP2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<NamedAttributeGroup> getAttributeGroup() {
		return getGroup1().list(SchemaPackage.Literals.SCHEMA_TYPE__ATTRIBUTE_GROUP);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<TopLevelElement> getElement() {
		return getGroup1().list(SchemaPackage.Literals.SCHEMA_TYPE__ELEMENT);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<TopLevelAttribute> getAttribute() {
		return getGroup1().list(SchemaPackage.Literals.SCHEMA_TYPE__ATTRIBUTE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<NotationType> getNotation() {
		return getGroup1().list(SchemaPackage.Literals.SCHEMA_TYPE__NOTATION);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<AnnotationType> getAnnotation1() {
		return getGroup1().list(SchemaPackage.Literals.SCHEMA_TYPE__ANNOTATION1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FormChoice getAttributeFormDefault() {
		return attributeFormDefault;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAttributeFormDefault(FormChoice newAttributeFormDefault) {
		FormChoice oldAttributeFormDefault = attributeFormDefault;
		attributeFormDefault = newAttributeFormDefault == null ? ATTRIBUTE_FORM_DEFAULT_EDEFAULT : newAttributeFormDefault;
		boolean oldAttributeFormDefaultESet = attributeFormDefaultESet;
		attributeFormDefaultESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.SCHEMA_TYPE__ATTRIBUTE_FORM_DEFAULT, oldAttributeFormDefault, attributeFormDefault, !oldAttributeFormDefaultESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetAttributeFormDefault() {
		FormChoice oldAttributeFormDefault = attributeFormDefault;
		boolean oldAttributeFormDefaultESet = attributeFormDefaultESet;
		attributeFormDefault = ATTRIBUTE_FORM_DEFAULT_EDEFAULT;
		attributeFormDefaultESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, SchemaPackage.SCHEMA_TYPE__ATTRIBUTE_FORM_DEFAULT, oldAttributeFormDefault, ATTRIBUTE_FORM_DEFAULT_EDEFAULT, oldAttributeFormDefaultESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetAttributeFormDefault() {
		return attributeFormDefaultESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object getBlockDefault() {
		return blockDefault;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBlockDefault(Object newBlockDefault) {
		Object oldBlockDefault = blockDefault;
		blockDefault = newBlockDefault;
		boolean oldBlockDefaultESet = blockDefaultESet;
		blockDefaultESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.SCHEMA_TYPE__BLOCK_DEFAULT, oldBlockDefault, blockDefault, !oldBlockDefaultESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetBlockDefault() {
		Object oldBlockDefault = blockDefault;
		boolean oldBlockDefaultESet = blockDefaultESet;
		blockDefault = BLOCK_DEFAULT_EDEFAULT;
		blockDefaultESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, SchemaPackage.SCHEMA_TYPE__BLOCK_DEFAULT, oldBlockDefault, BLOCK_DEFAULT_EDEFAULT, oldBlockDefaultESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetBlockDefault() {
		return blockDefaultESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FormChoice getElementFormDefault() {
		return elementFormDefault;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setElementFormDefault(FormChoice newElementFormDefault) {
		FormChoice oldElementFormDefault = elementFormDefault;
		elementFormDefault = newElementFormDefault == null ? ELEMENT_FORM_DEFAULT_EDEFAULT : newElementFormDefault;
		boolean oldElementFormDefaultESet = elementFormDefaultESet;
		elementFormDefaultESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.SCHEMA_TYPE__ELEMENT_FORM_DEFAULT, oldElementFormDefault, elementFormDefault, !oldElementFormDefaultESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetElementFormDefault() {
		FormChoice oldElementFormDefault = elementFormDefault;
		boolean oldElementFormDefaultESet = elementFormDefaultESet;
		elementFormDefault = ELEMENT_FORM_DEFAULT_EDEFAULT;
		elementFormDefaultESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, SchemaPackage.SCHEMA_TYPE__ELEMENT_FORM_DEFAULT, oldElementFormDefault, ELEMENT_FORM_DEFAULT_EDEFAULT, oldElementFormDefaultESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetElementFormDefault() {
		return elementFormDefaultESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object getFinalDefault() {
		return finalDefault;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFinalDefault(Object newFinalDefault) {
		Object oldFinalDefault = finalDefault;
		finalDefault = newFinalDefault;
		boolean oldFinalDefaultESet = finalDefaultESet;
		finalDefaultESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.SCHEMA_TYPE__FINAL_DEFAULT, oldFinalDefault, finalDefault, !oldFinalDefaultESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetFinalDefault() {
		Object oldFinalDefault = finalDefault;
		boolean oldFinalDefaultESet = finalDefaultESet;
		finalDefault = FINAL_DEFAULT_EDEFAULT;
		finalDefaultESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, SchemaPackage.SCHEMA_TYPE__FINAL_DEFAULT, oldFinalDefault, FINAL_DEFAULT_EDEFAULT, oldFinalDefaultESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetFinalDefault() {
		return finalDefaultESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getId() {
		return id;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setId(String newId) {
		String oldId = id;
		id = newId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.SCHEMA_TYPE__ID, oldId, id));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getLang() {
		return lang;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLang(String newLang) {
		String oldLang = lang;
		lang = newLang;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.SCHEMA_TYPE__LANG, oldLang, lang));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getTargetNamespace() {
		return targetNamespace;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTargetNamespace(String newTargetNamespace) {
		String oldTargetNamespace = targetNamespace;
		targetNamespace = newTargetNamespace;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.SCHEMA_TYPE__TARGET_NAMESPACE, oldTargetNamespace, targetNamespace));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVersion(String newVersion) {
		String oldVersion = version;
		version = newVersion;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.SCHEMA_TYPE__VERSION, oldVersion, version));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SchemaPackage.SCHEMA_TYPE__GROUP:
				return ((InternalEList<?>)getGroup()).basicRemove(otherEnd, msgs);
			case SchemaPackage.SCHEMA_TYPE__INCLUDE:
				return ((InternalEList<?>)getInclude()).basicRemove(otherEnd, msgs);
			case SchemaPackage.SCHEMA_TYPE__IMPORT:
				return ((InternalEList<?>)getImport()).basicRemove(otherEnd, msgs);
			case SchemaPackage.SCHEMA_TYPE__REDEFINE:
				return ((InternalEList<?>)getRedefine()).basicRemove(otherEnd, msgs);
			case SchemaPackage.SCHEMA_TYPE__ANNOTATION:
				return ((InternalEList<?>)getAnnotation()).basicRemove(otherEnd, msgs);
			case SchemaPackage.SCHEMA_TYPE__GROUP1:
				return ((InternalEList<?>)getGroup1()).basicRemove(otherEnd, msgs);
			case SchemaPackage.SCHEMA_TYPE__SIMPLE_TYPE:
				return ((InternalEList<?>)getSimpleType()).basicRemove(otherEnd, msgs);
			case SchemaPackage.SCHEMA_TYPE__COMPLEX_TYPE:
				return ((InternalEList<?>)getComplexType()).basicRemove(otherEnd, msgs);
			case SchemaPackage.SCHEMA_TYPE__GROUP2:
				return ((InternalEList<?>)getGroup2()).basicRemove(otherEnd, msgs);
			case SchemaPackage.SCHEMA_TYPE__ATTRIBUTE_GROUP:
				return ((InternalEList<?>)getAttributeGroup()).basicRemove(otherEnd, msgs);
			case SchemaPackage.SCHEMA_TYPE__ELEMENT:
				return ((InternalEList<?>)getElement()).basicRemove(otherEnd, msgs);
			case SchemaPackage.SCHEMA_TYPE__ATTRIBUTE:
				return ((InternalEList<?>)getAttribute()).basicRemove(otherEnd, msgs);
			case SchemaPackage.SCHEMA_TYPE__NOTATION:
				return ((InternalEList<?>)getNotation()).basicRemove(otherEnd, msgs);
			case SchemaPackage.SCHEMA_TYPE__ANNOTATION1:
				return ((InternalEList<?>)getAnnotation1()).basicRemove(otherEnd, msgs);
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
			case SchemaPackage.SCHEMA_TYPE__GROUP:
				if (coreType) return getGroup();
				return ((FeatureMap.Internal)getGroup()).getWrapper();
			case SchemaPackage.SCHEMA_TYPE__INCLUDE:
				return getInclude();
			case SchemaPackage.SCHEMA_TYPE__IMPORT:
				return getImport();
			case SchemaPackage.SCHEMA_TYPE__REDEFINE:
				return getRedefine();
			case SchemaPackage.SCHEMA_TYPE__ANNOTATION:
				return getAnnotation();
			case SchemaPackage.SCHEMA_TYPE__GROUP1:
				if (coreType) return getGroup1();
				return ((FeatureMap.Internal)getGroup1()).getWrapper();
			case SchemaPackage.SCHEMA_TYPE__SIMPLE_TYPE:
				return getSimpleType();
			case SchemaPackage.SCHEMA_TYPE__COMPLEX_TYPE:
				return getComplexType();
			case SchemaPackage.SCHEMA_TYPE__GROUP2:
				return getGroup2();
			case SchemaPackage.SCHEMA_TYPE__ATTRIBUTE_GROUP:
				return getAttributeGroup();
			case SchemaPackage.SCHEMA_TYPE__ELEMENT:
				return getElement();
			case SchemaPackage.SCHEMA_TYPE__ATTRIBUTE:
				return getAttribute();
			case SchemaPackage.SCHEMA_TYPE__NOTATION:
				return getNotation();
			case SchemaPackage.SCHEMA_TYPE__ANNOTATION1:
				return getAnnotation1();
			case SchemaPackage.SCHEMA_TYPE__ATTRIBUTE_FORM_DEFAULT:
				return getAttributeFormDefault();
			case SchemaPackage.SCHEMA_TYPE__BLOCK_DEFAULT:
				return getBlockDefault();
			case SchemaPackage.SCHEMA_TYPE__ELEMENT_FORM_DEFAULT:
				return getElementFormDefault();
			case SchemaPackage.SCHEMA_TYPE__FINAL_DEFAULT:
				return getFinalDefault();
			case SchemaPackage.SCHEMA_TYPE__ID:
				return getId();
			case SchemaPackage.SCHEMA_TYPE__LANG:
				return getLang();
			case SchemaPackage.SCHEMA_TYPE__TARGET_NAMESPACE:
				return getTargetNamespace();
			case SchemaPackage.SCHEMA_TYPE__VERSION:
				return getVersion();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case SchemaPackage.SCHEMA_TYPE__GROUP:
				((FeatureMap.Internal)getGroup()).set(newValue);
				return;
			case SchemaPackage.SCHEMA_TYPE__INCLUDE:
				getInclude().clear();
				getInclude().addAll((Collection<? extends IncludeType>)newValue);
				return;
			case SchemaPackage.SCHEMA_TYPE__IMPORT:
				getImport().clear();
				getImport().addAll((Collection<? extends ImportType>)newValue);
				return;
			case SchemaPackage.SCHEMA_TYPE__REDEFINE:
				getRedefine().clear();
				getRedefine().addAll((Collection<? extends RedefineType>)newValue);
				return;
			case SchemaPackage.SCHEMA_TYPE__ANNOTATION:
				getAnnotation().clear();
				getAnnotation().addAll((Collection<? extends AnnotationType>)newValue);
				return;
			case SchemaPackage.SCHEMA_TYPE__GROUP1:
				((FeatureMap.Internal)getGroup1()).set(newValue);
				return;
			case SchemaPackage.SCHEMA_TYPE__SIMPLE_TYPE:
				getSimpleType().clear();
				getSimpleType().addAll((Collection<? extends TopLevelSimpleType>)newValue);
				return;
			case SchemaPackage.SCHEMA_TYPE__COMPLEX_TYPE:
				getComplexType().clear();
				getComplexType().addAll((Collection<? extends TopLevelComplexType>)newValue);
				return;
			case SchemaPackage.SCHEMA_TYPE__GROUP2:
				getGroup2().clear();
				getGroup2().addAll((Collection<? extends NamedGroup>)newValue);
				return;
			case SchemaPackage.SCHEMA_TYPE__ATTRIBUTE_GROUP:
				getAttributeGroup().clear();
				getAttributeGroup().addAll((Collection<? extends NamedAttributeGroup>)newValue);
				return;
			case SchemaPackage.SCHEMA_TYPE__ELEMENT:
				getElement().clear();
				getElement().addAll((Collection<? extends TopLevelElement>)newValue);
				return;
			case SchemaPackage.SCHEMA_TYPE__ATTRIBUTE:
				getAttribute().clear();
				getAttribute().addAll((Collection<? extends TopLevelAttribute>)newValue);
				return;
			case SchemaPackage.SCHEMA_TYPE__NOTATION:
				getNotation().clear();
				getNotation().addAll((Collection<? extends NotationType>)newValue);
				return;
			case SchemaPackage.SCHEMA_TYPE__ANNOTATION1:
				getAnnotation1().clear();
				getAnnotation1().addAll((Collection<? extends AnnotationType>)newValue);
				return;
			case SchemaPackage.SCHEMA_TYPE__ATTRIBUTE_FORM_DEFAULT:
				setAttributeFormDefault((FormChoice)newValue);
				return;
			case SchemaPackage.SCHEMA_TYPE__BLOCK_DEFAULT:
				setBlockDefault(newValue);
				return;
			case SchemaPackage.SCHEMA_TYPE__ELEMENT_FORM_DEFAULT:
				setElementFormDefault((FormChoice)newValue);
				return;
			case SchemaPackage.SCHEMA_TYPE__FINAL_DEFAULT:
				setFinalDefault(newValue);
				return;
			case SchemaPackage.SCHEMA_TYPE__ID:
				setId((String)newValue);
				return;
			case SchemaPackage.SCHEMA_TYPE__LANG:
				setLang((String)newValue);
				return;
			case SchemaPackage.SCHEMA_TYPE__TARGET_NAMESPACE:
				setTargetNamespace((String)newValue);
				return;
			case SchemaPackage.SCHEMA_TYPE__VERSION:
				setVersion((String)newValue);
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
			case SchemaPackage.SCHEMA_TYPE__GROUP:
				getGroup().clear();
				return;
			case SchemaPackage.SCHEMA_TYPE__INCLUDE:
				getInclude().clear();
				return;
			case SchemaPackage.SCHEMA_TYPE__IMPORT:
				getImport().clear();
				return;
			case SchemaPackage.SCHEMA_TYPE__REDEFINE:
				getRedefine().clear();
				return;
			case SchemaPackage.SCHEMA_TYPE__ANNOTATION:
				getAnnotation().clear();
				return;
			case SchemaPackage.SCHEMA_TYPE__GROUP1:
				getGroup1().clear();
				return;
			case SchemaPackage.SCHEMA_TYPE__SIMPLE_TYPE:
				getSimpleType().clear();
				return;
			case SchemaPackage.SCHEMA_TYPE__COMPLEX_TYPE:
				getComplexType().clear();
				return;
			case SchemaPackage.SCHEMA_TYPE__GROUP2:
				getGroup2().clear();
				return;
			case SchemaPackage.SCHEMA_TYPE__ATTRIBUTE_GROUP:
				getAttributeGroup().clear();
				return;
			case SchemaPackage.SCHEMA_TYPE__ELEMENT:
				getElement().clear();
				return;
			case SchemaPackage.SCHEMA_TYPE__ATTRIBUTE:
				getAttribute().clear();
				return;
			case SchemaPackage.SCHEMA_TYPE__NOTATION:
				getNotation().clear();
				return;
			case SchemaPackage.SCHEMA_TYPE__ANNOTATION1:
				getAnnotation1().clear();
				return;
			case SchemaPackage.SCHEMA_TYPE__ATTRIBUTE_FORM_DEFAULT:
				unsetAttributeFormDefault();
				return;
			case SchemaPackage.SCHEMA_TYPE__BLOCK_DEFAULT:
				unsetBlockDefault();
				return;
			case SchemaPackage.SCHEMA_TYPE__ELEMENT_FORM_DEFAULT:
				unsetElementFormDefault();
				return;
			case SchemaPackage.SCHEMA_TYPE__FINAL_DEFAULT:
				unsetFinalDefault();
				return;
			case SchemaPackage.SCHEMA_TYPE__ID:
				setId(ID_EDEFAULT);
				return;
			case SchemaPackage.SCHEMA_TYPE__LANG:
				setLang(LANG_EDEFAULT);
				return;
			case SchemaPackage.SCHEMA_TYPE__TARGET_NAMESPACE:
				setTargetNamespace(TARGET_NAMESPACE_EDEFAULT);
				return;
			case SchemaPackage.SCHEMA_TYPE__VERSION:
				setVersion(VERSION_EDEFAULT);
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
			case SchemaPackage.SCHEMA_TYPE__GROUP:
				return group != null && !group.isEmpty();
			case SchemaPackage.SCHEMA_TYPE__INCLUDE:
				return !getInclude().isEmpty();
			case SchemaPackage.SCHEMA_TYPE__IMPORT:
				return !getImport().isEmpty();
			case SchemaPackage.SCHEMA_TYPE__REDEFINE:
				return !getRedefine().isEmpty();
			case SchemaPackage.SCHEMA_TYPE__ANNOTATION:
				return !getAnnotation().isEmpty();
			case SchemaPackage.SCHEMA_TYPE__GROUP1:
				return group1 != null && !group1.isEmpty();
			case SchemaPackage.SCHEMA_TYPE__SIMPLE_TYPE:
				return !getSimpleType().isEmpty();
			case SchemaPackage.SCHEMA_TYPE__COMPLEX_TYPE:
				return !getComplexType().isEmpty();
			case SchemaPackage.SCHEMA_TYPE__GROUP2:
				return !getGroup2().isEmpty();
			case SchemaPackage.SCHEMA_TYPE__ATTRIBUTE_GROUP:
				return !getAttributeGroup().isEmpty();
			case SchemaPackage.SCHEMA_TYPE__ELEMENT:
				return !getElement().isEmpty();
			case SchemaPackage.SCHEMA_TYPE__ATTRIBUTE:
				return !getAttribute().isEmpty();
			case SchemaPackage.SCHEMA_TYPE__NOTATION:
				return !getNotation().isEmpty();
			case SchemaPackage.SCHEMA_TYPE__ANNOTATION1:
				return !getAnnotation1().isEmpty();
			case SchemaPackage.SCHEMA_TYPE__ATTRIBUTE_FORM_DEFAULT:
				return isSetAttributeFormDefault();
			case SchemaPackage.SCHEMA_TYPE__BLOCK_DEFAULT:
				return isSetBlockDefault();
			case SchemaPackage.SCHEMA_TYPE__ELEMENT_FORM_DEFAULT:
				return isSetElementFormDefault();
			case SchemaPackage.SCHEMA_TYPE__FINAL_DEFAULT:
				return isSetFinalDefault();
			case SchemaPackage.SCHEMA_TYPE__ID:
				return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
			case SchemaPackage.SCHEMA_TYPE__LANG:
				return LANG_EDEFAULT == null ? lang != null : !LANG_EDEFAULT.equals(lang);
			case SchemaPackage.SCHEMA_TYPE__TARGET_NAMESPACE:
				return TARGET_NAMESPACE_EDEFAULT == null ? targetNamespace != null : !TARGET_NAMESPACE_EDEFAULT.equals(targetNamespace);
			case SchemaPackage.SCHEMA_TYPE__VERSION:
				return VERSION_EDEFAULT == null ? version != null : !VERSION_EDEFAULT.equals(version);
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
		result.append(" (group: ");
		result.append(group);
		result.append(", group1: ");
		result.append(group1);
		result.append(", attributeFormDefault: ");
		if (attributeFormDefaultESet) result.append(attributeFormDefault); else result.append("<unset>");
		result.append(", blockDefault: ");
		if (blockDefaultESet) result.append(blockDefault); else result.append("<unset>");
		result.append(", elementFormDefault: ");
		if (elementFormDefaultESet) result.append(elementFormDefault); else result.append("<unset>");
		result.append(", finalDefault: ");
		if (finalDefaultESet) result.append(finalDefault); else result.append("<unset>");
		result.append(", id: ");
		result.append(id);
		result.append(", lang: ");
		result.append(lang);
		result.append(", targetNamespace: ");
		result.append(targetNamespace);
		result.append(", version: ");
		result.append(version);
		result.append(')');
		return result.toString();
	}

} //SchemaTypeImpl
