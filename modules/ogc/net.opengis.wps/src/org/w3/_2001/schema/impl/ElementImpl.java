/**
 */
package org.w3._2001.schema.impl;

import java.math.BigInteger;

import java.util.Collection;

import javax.xml.namespace.QName;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

import org.w3._2001.schema.Element;
import org.w3._2001.schema.FormChoice;
import org.w3._2001.schema.Keybase;
import org.w3._2001.schema.KeyrefType;
import org.w3._2001.schema.LocalComplexType;
import org.w3._2001.schema.LocalSimpleType;
import org.w3._2001.schema.SchemaFactory;
import org.w3._2001.schema.SchemaPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Element</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.w3._2001.schema.impl.ElementImpl#getSimpleType <em>Simple Type</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.ElementImpl#getComplexType <em>Complex Type</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.ElementImpl#getIdentityConstraint <em>Identity Constraint</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.ElementImpl#getUnique <em>Unique</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.ElementImpl#getKey <em>Key</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.ElementImpl#getKeyref <em>Keyref</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.ElementImpl#isAbstract <em>Abstract</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.ElementImpl#getBlock <em>Block</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.ElementImpl#getDefault <em>Default</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.ElementImpl#getFinal <em>Final</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.ElementImpl#getFixed <em>Fixed</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.ElementImpl#getForm <em>Form</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.ElementImpl#getMaxOccurs <em>Max Occurs</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.ElementImpl#getMinOccurs <em>Min Occurs</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.ElementImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.ElementImpl#isNillable <em>Nillable</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.ElementImpl#getRef <em>Ref</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.ElementImpl#getSubstitutionGroup <em>Substitution Group</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.ElementImpl#getType <em>Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class ElementImpl extends AnnotatedImpl implements Element {
	/**
	 * The cached value of the '{@link #getSimpleType() <em>Simple Type</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSimpleType()
	 * @generated
	 * @ordered
	 */
	protected LocalSimpleType simpleType;

	/**
	 * The cached value of the '{@link #getComplexType() <em>Complex Type</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getComplexType()
	 * @generated
	 * @ordered
	 */
	protected LocalComplexType complexType;

	/**
	 * The cached value of the '{@link #getIdentityConstraint() <em>Identity Constraint</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIdentityConstraint()
	 * @generated
	 * @ordered
	 */
	protected FeatureMap identityConstraint;

	/**
	 * The default value of the '{@link #isAbstract() <em>Abstract</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isAbstract()
	 * @generated
	 * @ordered
	 */
	protected static final boolean ABSTRACT_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isAbstract() <em>Abstract</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isAbstract()
	 * @generated
	 * @ordered
	 */
	protected boolean abstract_ = ABSTRACT_EDEFAULT;

	/**
	 * This is true if the Abstract attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean abstractESet;

	/**
	 * The default value of the '{@link #getBlock() <em>Block</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBlock()
	 * @generated
	 * @ordered
	 */
	protected static final Object BLOCK_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getBlock() <em>Block</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBlock()
	 * @generated
	 * @ordered
	 */
	protected Object block = BLOCK_EDEFAULT;

	/**
	 * The default value of the '{@link #getDefault() <em>Default</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefault()
	 * @generated
	 * @ordered
	 */
	protected static final String DEFAULT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDefault() <em>Default</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefault()
	 * @generated
	 * @ordered
	 */
	protected String default_ = DEFAULT_EDEFAULT;

	/**
	 * The default value of the '{@link #getFinal() <em>Final</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFinal()
	 * @generated
	 * @ordered
	 */
	protected static final Object FINAL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getFinal() <em>Final</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFinal()
	 * @generated
	 * @ordered
	 */
	protected Object final_ = FINAL_EDEFAULT;

	/**
	 * The default value of the '{@link #getFixed() <em>Fixed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFixed()
	 * @generated
	 * @ordered
	 */
	protected static final String FIXED_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getFixed() <em>Fixed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFixed()
	 * @generated
	 * @ordered
	 */
	protected String fixed = FIXED_EDEFAULT;

	/**
	 * The default value of the '{@link #getForm() <em>Form</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getForm()
	 * @generated
	 * @ordered
	 */
	protected static final FormChoice FORM_EDEFAULT = FormChoice.QUALIFIED;

	/**
	 * The cached value of the '{@link #getForm() <em>Form</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getForm()
	 * @generated
	 * @ordered
	 */
	protected FormChoice form = FORM_EDEFAULT;

	/**
	 * This is true if the Form attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean formESet;

	/**
	 * The default value of the '{@link #getMaxOccurs() <em>Max Occurs</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxOccurs()
	 * @generated
	 * @ordered
	 */
	protected static final Object MAX_OCCURS_EDEFAULT = SchemaFactory.eINSTANCE.createFromString(SchemaPackage.eINSTANCE.getAllNNI(), "1");

	/**
	 * The cached value of the '{@link #getMaxOccurs() <em>Max Occurs</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxOccurs()
	 * @generated
	 * @ordered
	 */
	protected Object maxOccurs = MAX_OCCURS_EDEFAULT;

	/**
	 * This is true if the Max Occurs attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean maxOccursESet;

	/**
	 * The default value of the '{@link #getMinOccurs() <em>Min Occurs</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinOccurs()
	 * @generated
	 * @ordered
	 */
	protected static final BigInteger MIN_OCCURS_EDEFAULT = new BigInteger("1");

	/**
	 * The cached value of the '{@link #getMinOccurs() <em>Min Occurs</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinOccurs()
	 * @generated
	 * @ordered
	 */
	protected BigInteger minOccurs = MIN_OCCURS_EDEFAULT;

	/**
	 * This is true if the Min Occurs attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean minOccursESet;

	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #isNillable() <em>Nillable</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isNillable()
	 * @generated
	 * @ordered
	 */
	protected static final boolean NILLABLE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isNillable() <em>Nillable</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isNillable()
	 * @generated
	 * @ordered
	 */
	protected boolean nillable = NILLABLE_EDEFAULT;

	/**
	 * This is true if the Nillable attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean nillableESet;

	/**
	 * The default value of the '{@link #getRef() <em>Ref</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRef()
	 * @generated
	 * @ordered
	 */
	protected static final QName REF_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getRef() <em>Ref</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRef()
	 * @generated
	 * @ordered
	 */
	protected QName ref = REF_EDEFAULT;

	/**
	 * The default value of the '{@link #getSubstitutionGroup() <em>Substitution Group</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSubstitutionGroup()
	 * @generated
	 * @ordered
	 */
	protected static final QName SUBSTITUTION_GROUP_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSubstitutionGroup() <em>Substitution Group</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSubstitutionGroup()
	 * @generated
	 * @ordered
	 */
	protected QName substitutionGroup = SUBSTITUTION_GROUP_EDEFAULT;

	/**
	 * The default value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected static final QName TYPE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected QName type = TYPE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ElementImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchemaPackage.Literals.ELEMENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LocalSimpleType getSimpleType() {
		return simpleType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSimpleType(LocalSimpleType newSimpleType, NotificationChain msgs) {
		LocalSimpleType oldSimpleType = simpleType;
		simpleType = newSimpleType;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchemaPackage.ELEMENT__SIMPLE_TYPE, oldSimpleType, newSimpleType);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSimpleType(LocalSimpleType newSimpleType) {
		if (newSimpleType != simpleType) {
			NotificationChain msgs = null;
			if (simpleType != null)
				msgs = ((InternalEObject)simpleType).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SchemaPackage.ELEMENT__SIMPLE_TYPE, null, msgs);
			if (newSimpleType != null)
				msgs = ((InternalEObject)newSimpleType).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SchemaPackage.ELEMENT__SIMPLE_TYPE, null, msgs);
			msgs = basicSetSimpleType(newSimpleType, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.ELEMENT__SIMPLE_TYPE, newSimpleType, newSimpleType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LocalComplexType getComplexType() {
		return complexType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetComplexType(LocalComplexType newComplexType, NotificationChain msgs) {
		LocalComplexType oldComplexType = complexType;
		complexType = newComplexType;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchemaPackage.ELEMENT__COMPLEX_TYPE, oldComplexType, newComplexType);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setComplexType(LocalComplexType newComplexType) {
		if (newComplexType != complexType) {
			NotificationChain msgs = null;
			if (complexType != null)
				msgs = ((InternalEObject)complexType).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SchemaPackage.ELEMENT__COMPLEX_TYPE, null, msgs);
			if (newComplexType != null)
				msgs = ((InternalEObject)newComplexType).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SchemaPackage.ELEMENT__COMPLEX_TYPE, null, msgs);
			msgs = basicSetComplexType(newComplexType, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.ELEMENT__COMPLEX_TYPE, newComplexType, newComplexType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FeatureMap getIdentityConstraint() {
		if (identityConstraint == null) {
			identityConstraint = new BasicFeatureMap(this, SchemaPackage.ELEMENT__IDENTITY_CONSTRAINT);
		}
		return identityConstraint;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Keybase> getUnique() {
		return getIdentityConstraint().list(SchemaPackage.Literals.ELEMENT__UNIQUE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Keybase> getKey() {
		return getIdentityConstraint().list(SchemaPackage.Literals.ELEMENT__KEY);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<KeyrefType> getKeyref() {
		return getIdentityConstraint().list(SchemaPackage.Literals.ELEMENT__KEYREF);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isAbstract() {
		return abstract_;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAbstract(boolean newAbstract) {
		boolean oldAbstract = abstract_;
		abstract_ = newAbstract;
		boolean oldAbstractESet = abstractESet;
		abstractESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.ELEMENT__ABSTRACT, oldAbstract, abstract_, !oldAbstractESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetAbstract() {
		boolean oldAbstract = abstract_;
		boolean oldAbstractESet = abstractESet;
		abstract_ = ABSTRACT_EDEFAULT;
		abstractESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, SchemaPackage.ELEMENT__ABSTRACT, oldAbstract, ABSTRACT_EDEFAULT, oldAbstractESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetAbstract() {
		return abstractESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object getBlock() {
		return block;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBlock(Object newBlock) {
		Object oldBlock = block;
		block = newBlock;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.ELEMENT__BLOCK, oldBlock, block));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDefault() {
		return default_;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDefault(String newDefault) {
		String oldDefault = default_;
		default_ = newDefault;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.ELEMENT__DEFAULT, oldDefault, default_));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object getFinal() {
		return final_;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFinal(Object newFinal) {
		Object oldFinal = final_;
		final_ = newFinal;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.ELEMENT__FINAL, oldFinal, final_));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getFixed() {
		return fixed;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFixed(String newFixed) {
		String oldFixed = fixed;
		fixed = newFixed;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.ELEMENT__FIXED, oldFixed, fixed));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FormChoice getForm() {
		return form;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setForm(FormChoice newForm) {
		FormChoice oldForm = form;
		form = newForm == null ? FORM_EDEFAULT : newForm;
		boolean oldFormESet = formESet;
		formESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.ELEMENT__FORM, oldForm, form, !oldFormESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetForm() {
		FormChoice oldForm = form;
		boolean oldFormESet = formESet;
		form = FORM_EDEFAULT;
		formESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, SchemaPackage.ELEMENT__FORM, oldForm, FORM_EDEFAULT, oldFormESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetForm() {
		return formESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object getMaxOccurs() {
		return maxOccurs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMaxOccurs(Object newMaxOccurs) {
		Object oldMaxOccurs = maxOccurs;
		maxOccurs = newMaxOccurs;
		boolean oldMaxOccursESet = maxOccursESet;
		maxOccursESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.ELEMENT__MAX_OCCURS, oldMaxOccurs, maxOccurs, !oldMaxOccursESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetMaxOccurs() {
		Object oldMaxOccurs = maxOccurs;
		boolean oldMaxOccursESet = maxOccursESet;
		maxOccurs = MAX_OCCURS_EDEFAULT;
		maxOccursESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, SchemaPackage.ELEMENT__MAX_OCCURS, oldMaxOccurs, MAX_OCCURS_EDEFAULT, oldMaxOccursESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetMaxOccurs() {
		return maxOccursESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BigInteger getMinOccurs() {
		return minOccurs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMinOccurs(BigInteger newMinOccurs) {
		BigInteger oldMinOccurs = minOccurs;
		minOccurs = newMinOccurs;
		boolean oldMinOccursESet = minOccursESet;
		minOccursESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.ELEMENT__MIN_OCCURS, oldMinOccurs, minOccurs, !oldMinOccursESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetMinOccurs() {
		BigInteger oldMinOccurs = minOccurs;
		boolean oldMinOccursESet = minOccursESet;
		minOccurs = MIN_OCCURS_EDEFAULT;
		minOccursESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, SchemaPackage.ELEMENT__MIN_OCCURS, oldMinOccurs, MIN_OCCURS_EDEFAULT, oldMinOccursESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetMinOccurs() {
		return minOccursESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.ELEMENT__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isNillable() {
		return nillable;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNillable(boolean newNillable) {
		boolean oldNillable = nillable;
		nillable = newNillable;
		boolean oldNillableESet = nillableESet;
		nillableESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.ELEMENT__NILLABLE, oldNillable, nillable, !oldNillableESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetNillable() {
		boolean oldNillable = nillable;
		boolean oldNillableESet = nillableESet;
		nillable = NILLABLE_EDEFAULT;
		nillableESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, SchemaPackage.ELEMENT__NILLABLE, oldNillable, NILLABLE_EDEFAULT, oldNillableESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetNillable() {
		return nillableESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public QName getRef() {
		return ref;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRef(QName newRef) {
		QName oldRef = ref;
		ref = newRef;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.ELEMENT__REF, oldRef, ref));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public QName getSubstitutionGroup() {
		return substitutionGroup;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSubstitutionGroup(QName newSubstitutionGroup) {
		QName oldSubstitutionGroup = substitutionGroup;
		substitutionGroup = newSubstitutionGroup;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.ELEMENT__SUBSTITUTION_GROUP, oldSubstitutionGroup, substitutionGroup));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public QName getType() {
		return type;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setType(QName newType) {
		QName oldType = type;
		type = newType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.ELEMENT__TYPE, oldType, type));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SchemaPackage.ELEMENT__SIMPLE_TYPE:
				return basicSetSimpleType(null, msgs);
			case SchemaPackage.ELEMENT__COMPLEX_TYPE:
				return basicSetComplexType(null, msgs);
			case SchemaPackage.ELEMENT__IDENTITY_CONSTRAINT:
				return ((InternalEList<?>)getIdentityConstraint()).basicRemove(otherEnd, msgs);
			case SchemaPackage.ELEMENT__UNIQUE:
				return ((InternalEList<?>)getUnique()).basicRemove(otherEnd, msgs);
			case SchemaPackage.ELEMENT__KEY:
				return ((InternalEList<?>)getKey()).basicRemove(otherEnd, msgs);
			case SchemaPackage.ELEMENT__KEYREF:
				return ((InternalEList<?>)getKeyref()).basicRemove(otherEnd, msgs);
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
			case SchemaPackage.ELEMENT__SIMPLE_TYPE:
				return getSimpleType();
			case SchemaPackage.ELEMENT__COMPLEX_TYPE:
				return getComplexType();
			case SchemaPackage.ELEMENT__IDENTITY_CONSTRAINT:
				if (coreType) return getIdentityConstraint();
				return ((FeatureMap.Internal)getIdentityConstraint()).getWrapper();
			case SchemaPackage.ELEMENT__UNIQUE:
				return getUnique();
			case SchemaPackage.ELEMENT__KEY:
				return getKey();
			case SchemaPackage.ELEMENT__KEYREF:
				return getKeyref();
			case SchemaPackage.ELEMENT__ABSTRACT:
				return isAbstract();
			case SchemaPackage.ELEMENT__BLOCK:
				return getBlock();
			case SchemaPackage.ELEMENT__DEFAULT:
				return getDefault();
			case SchemaPackage.ELEMENT__FINAL:
				return getFinal();
			case SchemaPackage.ELEMENT__FIXED:
				return getFixed();
			case SchemaPackage.ELEMENT__FORM:
				return getForm();
			case SchemaPackage.ELEMENT__MAX_OCCURS:
				return getMaxOccurs();
			case SchemaPackage.ELEMENT__MIN_OCCURS:
				return getMinOccurs();
			case SchemaPackage.ELEMENT__NAME:
				return getName();
			case SchemaPackage.ELEMENT__NILLABLE:
				return isNillable();
			case SchemaPackage.ELEMENT__REF:
				return getRef();
			case SchemaPackage.ELEMENT__SUBSTITUTION_GROUP:
				return getSubstitutionGroup();
			case SchemaPackage.ELEMENT__TYPE:
				return getType();
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
			case SchemaPackage.ELEMENT__SIMPLE_TYPE:
				setSimpleType((LocalSimpleType)newValue);
				return;
			case SchemaPackage.ELEMENT__COMPLEX_TYPE:
				setComplexType((LocalComplexType)newValue);
				return;
			case SchemaPackage.ELEMENT__IDENTITY_CONSTRAINT:
				((FeatureMap.Internal)getIdentityConstraint()).set(newValue);
				return;
			case SchemaPackage.ELEMENT__UNIQUE:
				getUnique().clear();
				getUnique().addAll((Collection<? extends Keybase>)newValue);
				return;
			case SchemaPackage.ELEMENT__KEY:
				getKey().clear();
				getKey().addAll((Collection<? extends Keybase>)newValue);
				return;
			case SchemaPackage.ELEMENT__KEYREF:
				getKeyref().clear();
				getKeyref().addAll((Collection<? extends KeyrefType>)newValue);
				return;
			case SchemaPackage.ELEMENT__ABSTRACT:
				setAbstract((Boolean)newValue);
				return;
			case SchemaPackage.ELEMENT__BLOCK:
				setBlock(newValue);
				return;
			case SchemaPackage.ELEMENT__DEFAULT:
				setDefault((String)newValue);
				return;
			case SchemaPackage.ELEMENT__FINAL:
				setFinal(newValue);
				return;
			case SchemaPackage.ELEMENT__FIXED:
				setFixed((String)newValue);
				return;
			case SchemaPackage.ELEMENT__FORM:
				setForm((FormChoice)newValue);
				return;
			case SchemaPackage.ELEMENT__MAX_OCCURS:
				setMaxOccurs(newValue);
				return;
			case SchemaPackage.ELEMENT__MIN_OCCURS:
				setMinOccurs((BigInteger)newValue);
				return;
			case SchemaPackage.ELEMENT__NAME:
				setName((String)newValue);
				return;
			case SchemaPackage.ELEMENT__NILLABLE:
				setNillable((Boolean)newValue);
				return;
			case SchemaPackage.ELEMENT__REF:
				setRef((QName)newValue);
				return;
			case SchemaPackage.ELEMENT__SUBSTITUTION_GROUP:
				setSubstitutionGroup((QName)newValue);
				return;
			case SchemaPackage.ELEMENT__TYPE:
				setType((QName)newValue);
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
			case SchemaPackage.ELEMENT__SIMPLE_TYPE:
				setSimpleType((LocalSimpleType)null);
				return;
			case SchemaPackage.ELEMENT__COMPLEX_TYPE:
				setComplexType((LocalComplexType)null);
				return;
			case SchemaPackage.ELEMENT__IDENTITY_CONSTRAINT:
				getIdentityConstraint().clear();
				return;
			case SchemaPackage.ELEMENT__UNIQUE:
				getUnique().clear();
				return;
			case SchemaPackage.ELEMENT__KEY:
				getKey().clear();
				return;
			case SchemaPackage.ELEMENT__KEYREF:
				getKeyref().clear();
				return;
			case SchemaPackage.ELEMENT__ABSTRACT:
				unsetAbstract();
				return;
			case SchemaPackage.ELEMENT__BLOCK:
				setBlock(BLOCK_EDEFAULT);
				return;
			case SchemaPackage.ELEMENT__DEFAULT:
				setDefault(DEFAULT_EDEFAULT);
				return;
			case SchemaPackage.ELEMENT__FINAL:
				setFinal(FINAL_EDEFAULT);
				return;
			case SchemaPackage.ELEMENT__FIXED:
				setFixed(FIXED_EDEFAULT);
				return;
			case SchemaPackage.ELEMENT__FORM:
				unsetForm();
				return;
			case SchemaPackage.ELEMENT__MAX_OCCURS:
				unsetMaxOccurs();
				return;
			case SchemaPackage.ELEMENT__MIN_OCCURS:
				unsetMinOccurs();
				return;
			case SchemaPackage.ELEMENT__NAME:
				setName(NAME_EDEFAULT);
				return;
			case SchemaPackage.ELEMENT__NILLABLE:
				unsetNillable();
				return;
			case SchemaPackage.ELEMENT__REF:
				setRef(REF_EDEFAULT);
				return;
			case SchemaPackage.ELEMENT__SUBSTITUTION_GROUP:
				setSubstitutionGroup(SUBSTITUTION_GROUP_EDEFAULT);
				return;
			case SchemaPackage.ELEMENT__TYPE:
				setType(TYPE_EDEFAULT);
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
			case SchemaPackage.ELEMENT__SIMPLE_TYPE:
				return simpleType != null;
			case SchemaPackage.ELEMENT__COMPLEX_TYPE:
				return complexType != null;
			case SchemaPackage.ELEMENT__IDENTITY_CONSTRAINT:
				return identityConstraint != null && !identityConstraint.isEmpty();
			case SchemaPackage.ELEMENT__UNIQUE:
				return !getUnique().isEmpty();
			case SchemaPackage.ELEMENT__KEY:
				return !getKey().isEmpty();
			case SchemaPackage.ELEMENT__KEYREF:
				return !getKeyref().isEmpty();
			case SchemaPackage.ELEMENT__ABSTRACT:
				return isSetAbstract();
			case SchemaPackage.ELEMENT__BLOCK:
				return BLOCK_EDEFAULT == null ? block != null : !BLOCK_EDEFAULT.equals(block);
			case SchemaPackage.ELEMENT__DEFAULT:
				return DEFAULT_EDEFAULT == null ? default_ != null : !DEFAULT_EDEFAULT.equals(default_);
			case SchemaPackage.ELEMENT__FINAL:
				return FINAL_EDEFAULT == null ? final_ != null : !FINAL_EDEFAULT.equals(final_);
			case SchemaPackage.ELEMENT__FIXED:
				return FIXED_EDEFAULT == null ? fixed != null : !FIXED_EDEFAULT.equals(fixed);
			case SchemaPackage.ELEMENT__FORM:
				return isSetForm();
			case SchemaPackage.ELEMENT__MAX_OCCURS:
				return isSetMaxOccurs();
			case SchemaPackage.ELEMENT__MIN_OCCURS:
				return isSetMinOccurs();
			case SchemaPackage.ELEMENT__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case SchemaPackage.ELEMENT__NILLABLE:
				return isSetNillable();
			case SchemaPackage.ELEMENT__REF:
				return REF_EDEFAULT == null ? ref != null : !REF_EDEFAULT.equals(ref);
			case SchemaPackage.ELEMENT__SUBSTITUTION_GROUP:
				return SUBSTITUTION_GROUP_EDEFAULT == null ? substitutionGroup != null : !SUBSTITUTION_GROUP_EDEFAULT.equals(substitutionGroup);
			case SchemaPackage.ELEMENT__TYPE:
				return TYPE_EDEFAULT == null ? type != null : !TYPE_EDEFAULT.equals(type);
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
		result.append(" (identityConstraint: ");
		result.append(identityConstraint);
		result.append(", abstract: ");
		if (abstractESet) result.append(abstract_); else result.append("<unset>");
		result.append(", block: ");
		result.append(block);
		result.append(", default: ");
		result.append(default_);
		result.append(", final: ");
		result.append(final_);
		result.append(", fixed: ");
		result.append(fixed);
		result.append(", form: ");
		if (formESet) result.append(form); else result.append("<unset>");
		result.append(", maxOccurs: ");
		if (maxOccursESet) result.append(maxOccurs); else result.append("<unset>");
		result.append(", minOccurs: ");
		if (minOccursESet) result.append(minOccurs); else result.append("<unset>");
		result.append(", name: ");
		result.append(name);
		result.append(", nillable: ");
		if (nillableESet) result.append(nillable); else result.append("<unset>");
		result.append(", ref: ");
		result.append(ref);
		result.append(", substitutionGroup: ");
		result.append(substitutionGroup);
		result.append(", type: ");
		result.append(type);
		result.append(')');
		return result.toString();
	}

} //ElementImpl
