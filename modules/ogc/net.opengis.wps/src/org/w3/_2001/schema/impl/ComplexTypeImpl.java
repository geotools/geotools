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

import org.w3._2001.schema.All;
import org.w3._2001.schema.Attribute;
import org.w3._2001.schema.AttributeGroupRef;
import org.w3._2001.schema.ComplexContentType;
import org.w3._2001.schema.ComplexType;
import org.w3._2001.schema.ExplicitGroup;
import org.w3._2001.schema.GroupRef;
import org.w3._2001.schema.SchemaPackage;
import org.w3._2001.schema.SimpleContentType;
import org.w3._2001.schema.Wildcard;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Complex Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.w3._2001.schema.impl.ComplexTypeImpl#getSimpleContent <em>Simple Content</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.ComplexTypeImpl#getComplexContent <em>Complex Content</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.ComplexTypeImpl#getGroup <em>Group</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.ComplexTypeImpl#getAll <em>All</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.ComplexTypeImpl#getChoice <em>Choice</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.ComplexTypeImpl#getSequence <em>Sequence</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.ComplexTypeImpl#getGroup1 <em>Group1</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.ComplexTypeImpl#getAttribute <em>Attribute</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.ComplexTypeImpl#getAttributeGroup <em>Attribute Group</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.ComplexTypeImpl#getAnyAttribute1 <em>Any Attribute1</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.ComplexTypeImpl#isAbstract <em>Abstract</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.ComplexTypeImpl#getBlock <em>Block</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.ComplexTypeImpl#getFinal <em>Final</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.ComplexTypeImpl#isMixed <em>Mixed</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.ComplexTypeImpl#getName <em>Name</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class ComplexTypeImpl extends AnnotatedImpl implements ComplexType {
	/**
	 * The cached value of the '{@link #getSimpleContent() <em>Simple Content</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSimpleContent()
	 * @generated
	 * @ordered
	 */
	protected SimpleContentType simpleContent;

	/**
	 * The cached value of the '{@link #getComplexContent() <em>Complex Content</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getComplexContent()
	 * @generated
	 * @ordered
	 */
	protected ComplexContentType complexContent;

	/**
	 * The cached value of the '{@link #getGroup() <em>Group</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGroup()
	 * @generated
	 * @ordered
	 */
	protected GroupRef group;

	/**
	 * The cached value of the '{@link #getAll() <em>All</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAll()
	 * @generated
	 * @ordered
	 */
	protected All all;

	/**
	 * The cached value of the '{@link #getChoice() <em>Choice</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChoice()
	 * @generated
	 * @ordered
	 */
	protected ExplicitGroup choice;

	/**
	 * The cached value of the '{@link #getSequence() <em>Sequence</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSequence()
	 * @generated
	 * @ordered
	 */
	protected ExplicitGroup sequence;

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
	 * The cached value of the '{@link #getAnyAttribute1() <em>Any Attribute1</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAnyAttribute1()
	 * @generated
	 * @ordered
	 */
	protected Wildcard anyAttribute1;

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
	 * The default value of the '{@link #isMixed() <em>Mixed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isMixed()
	 * @generated
	 * @ordered
	 */
	protected static final boolean MIXED_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isMixed() <em>Mixed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isMixed()
	 * @generated
	 * @ordered
	 */
	protected boolean mixed = MIXED_EDEFAULT;

	/**
	 * This is true if the Mixed attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean mixedESet;

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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ComplexTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchemaPackage.Literals.COMPLEX_TYPE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SimpleContentType getSimpleContent() {
		return simpleContent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSimpleContent(SimpleContentType newSimpleContent, NotificationChain msgs) {
		SimpleContentType oldSimpleContent = simpleContent;
		simpleContent = newSimpleContent;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchemaPackage.COMPLEX_TYPE__SIMPLE_CONTENT, oldSimpleContent, newSimpleContent);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSimpleContent(SimpleContentType newSimpleContent) {
		if (newSimpleContent != simpleContent) {
			NotificationChain msgs = null;
			if (simpleContent != null)
				msgs = ((InternalEObject)simpleContent).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SchemaPackage.COMPLEX_TYPE__SIMPLE_CONTENT, null, msgs);
			if (newSimpleContent != null)
				msgs = ((InternalEObject)newSimpleContent).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SchemaPackage.COMPLEX_TYPE__SIMPLE_CONTENT, null, msgs);
			msgs = basicSetSimpleContent(newSimpleContent, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.COMPLEX_TYPE__SIMPLE_CONTENT, newSimpleContent, newSimpleContent));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ComplexContentType getComplexContent() {
		return complexContent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetComplexContent(ComplexContentType newComplexContent, NotificationChain msgs) {
		ComplexContentType oldComplexContent = complexContent;
		complexContent = newComplexContent;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchemaPackage.COMPLEX_TYPE__COMPLEX_CONTENT, oldComplexContent, newComplexContent);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setComplexContent(ComplexContentType newComplexContent) {
		if (newComplexContent != complexContent) {
			NotificationChain msgs = null;
			if (complexContent != null)
				msgs = ((InternalEObject)complexContent).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SchemaPackage.COMPLEX_TYPE__COMPLEX_CONTENT, null, msgs);
			if (newComplexContent != null)
				msgs = ((InternalEObject)newComplexContent).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SchemaPackage.COMPLEX_TYPE__COMPLEX_CONTENT, null, msgs);
			msgs = basicSetComplexContent(newComplexContent, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.COMPLEX_TYPE__COMPLEX_CONTENT, newComplexContent, newComplexContent));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GroupRef getGroup() {
		return group;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetGroup(GroupRef newGroup, NotificationChain msgs) {
		GroupRef oldGroup = group;
		group = newGroup;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchemaPackage.COMPLEX_TYPE__GROUP, oldGroup, newGroup);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setGroup(GroupRef newGroup) {
		if (newGroup != group) {
			NotificationChain msgs = null;
			if (group != null)
				msgs = ((InternalEObject)group).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SchemaPackage.COMPLEX_TYPE__GROUP, null, msgs);
			if (newGroup != null)
				msgs = ((InternalEObject)newGroup).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SchemaPackage.COMPLEX_TYPE__GROUP, null, msgs);
			msgs = basicSetGroup(newGroup, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.COMPLEX_TYPE__GROUP, newGroup, newGroup));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public All getAll() {
		return all;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetAll(All newAll, NotificationChain msgs) {
		All oldAll = all;
		all = newAll;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchemaPackage.COMPLEX_TYPE__ALL, oldAll, newAll);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAll(All newAll) {
		if (newAll != all) {
			NotificationChain msgs = null;
			if (all != null)
				msgs = ((InternalEObject)all).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SchemaPackage.COMPLEX_TYPE__ALL, null, msgs);
			if (newAll != null)
				msgs = ((InternalEObject)newAll).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SchemaPackage.COMPLEX_TYPE__ALL, null, msgs);
			msgs = basicSetAll(newAll, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.COMPLEX_TYPE__ALL, newAll, newAll));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ExplicitGroup getChoice() {
		return choice;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetChoice(ExplicitGroup newChoice, NotificationChain msgs) {
		ExplicitGroup oldChoice = choice;
		choice = newChoice;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchemaPackage.COMPLEX_TYPE__CHOICE, oldChoice, newChoice);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setChoice(ExplicitGroup newChoice) {
		if (newChoice != choice) {
			NotificationChain msgs = null;
			if (choice != null)
				msgs = ((InternalEObject)choice).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SchemaPackage.COMPLEX_TYPE__CHOICE, null, msgs);
			if (newChoice != null)
				msgs = ((InternalEObject)newChoice).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SchemaPackage.COMPLEX_TYPE__CHOICE, null, msgs);
			msgs = basicSetChoice(newChoice, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.COMPLEX_TYPE__CHOICE, newChoice, newChoice));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ExplicitGroup getSequence() {
		return sequence;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSequence(ExplicitGroup newSequence, NotificationChain msgs) {
		ExplicitGroup oldSequence = sequence;
		sequence = newSequence;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchemaPackage.COMPLEX_TYPE__SEQUENCE, oldSequence, newSequence);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSequence(ExplicitGroup newSequence) {
		if (newSequence != sequence) {
			NotificationChain msgs = null;
			if (sequence != null)
				msgs = ((InternalEObject)sequence).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SchemaPackage.COMPLEX_TYPE__SEQUENCE, null, msgs);
			if (newSequence != null)
				msgs = ((InternalEObject)newSequence).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SchemaPackage.COMPLEX_TYPE__SEQUENCE, null, msgs);
			msgs = basicSetSequence(newSequence, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.COMPLEX_TYPE__SEQUENCE, newSequence, newSequence));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FeatureMap getGroup1() {
		if (group1 == null) {
			group1 = new BasicFeatureMap(this, SchemaPackage.COMPLEX_TYPE__GROUP1);
		}
		return group1;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Attribute> getAttribute() {
		return getGroup1().list(SchemaPackage.Literals.COMPLEX_TYPE__ATTRIBUTE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<AttributeGroupRef> getAttributeGroup() {
		return getGroup1().list(SchemaPackage.Literals.COMPLEX_TYPE__ATTRIBUTE_GROUP);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Wildcard getAnyAttribute1() {
		return anyAttribute1;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetAnyAttribute1(Wildcard newAnyAttribute1, NotificationChain msgs) {
		Wildcard oldAnyAttribute1 = anyAttribute1;
		anyAttribute1 = newAnyAttribute1;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchemaPackage.COMPLEX_TYPE__ANY_ATTRIBUTE1, oldAnyAttribute1, newAnyAttribute1);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAnyAttribute1(Wildcard newAnyAttribute1) {
		if (newAnyAttribute1 != anyAttribute1) {
			NotificationChain msgs = null;
			if (anyAttribute1 != null)
				msgs = ((InternalEObject)anyAttribute1).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SchemaPackage.COMPLEX_TYPE__ANY_ATTRIBUTE1, null, msgs);
			if (newAnyAttribute1 != null)
				msgs = ((InternalEObject)newAnyAttribute1).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SchemaPackage.COMPLEX_TYPE__ANY_ATTRIBUTE1, null, msgs);
			msgs = basicSetAnyAttribute1(newAnyAttribute1, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.COMPLEX_TYPE__ANY_ATTRIBUTE1, newAnyAttribute1, newAnyAttribute1));
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
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.COMPLEX_TYPE__ABSTRACT, oldAbstract, abstract_, !oldAbstractESet));
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
			eNotify(new ENotificationImpl(this, Notification.UNSET, SchemaPackage.COMPLEX_TYPE__ABSTRACT, oldAbstract, ABSTRACT_EDEFAULT, oldAbstractESet));
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
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.COMPLEX_TYPE__BLOCK, oldBlock, block));
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
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.COMPLEX_TYPE__FINAL, oldFinal, final_));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isMixed() {
		return mixed;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMixed(boolean newMixed) {
		boolean oldMixed = mixed;
		mixed = newMixed;
		boolean oldMixedESet = mixedESet;
		mixedESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.COMPLEX_TYPE__MIXED, oldMixed, mixed, !oldMixedESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetMixed() {
		boolean oldMixed = mixed;
		boolean oldMixedESet = mixedESet;
		mixed = MIXED_EDEFAULT;
		mixedESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, SchemaPackage.COMPLEX_TYPE__MIXED, oldMixed, MIXED_EDEFAULT, oldMixedESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetMixed() {
		return mixedESet;
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
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.COMPLEX_TYPE__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SchemaPackage.COMPLEX_TYPE__SIMPLE_CONTENT:
				return basicSetSimpleContent(null, msgs);
			case SchemaPackage.COMPLEX_TYPE__COMPLEX_CONTENT:
				return basicSetComplexContent(null, msgs);
			case SchemaPackage.COMPLEX_TYPE__GROUP:
				return basicSetGroup(null, msgs);
			case SchemaPackage.COMPLEX_TYPE__ALL:
				return basicSetAll(null, msgs);
			case SchemaPackage.COMPLEX_TYPE__CHOICE:
				return basicSetChoice(null, msgs);
			case SchemaPackage.COMPLEX_TYPE__SEQUENCE:
				return basicSetSequence(null, msgs);
			case SchemaPackage.COMPLEX_TYPE__GROUP1:
				return ((InternalEList<?>)getGroup1()).basicRemove(otherEnd, msgs);
			case SchemaPackage.COMPLEX_TYPE__ATTRIBUTE:
				return ((InternalEList<?>)getAttribute()).basicRemove(otherEnd, msgs);
			case SchemaPackage.COMPLEX_TYPE__ATTRIBUTE_GROUP:
				return ((InternalEList<?>)getAttributeGroup()).basicRemove(otherEnd, msgs);
			case SchemaPackage.COMPLEX_TYPE__ANY_ATTRIBUTE1:
				return basicSetAnyAttribute1(null, msgs);
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
			case SchemaPackage.COMPLEX_TYPE__SIMPLE_CONTENT:
				return getSimpleContent();
			case SchemaPackage.COMPLEX_TYPE__COMPLEX_CONTENT:
				return getComplexContent();
			case SchemaPackage.COMPLEX_TYPE__GROUP:
				return getGroup();
			case SchemaPackage.COMPLEX_TYPE__ALL:
				return getAll();
			case SchemaPackage.COMPLEX_TYPE__CHOICE:
				return getChoice();
			case SchemaPackage.COMPLEX_TYPE__SEQUENCE:
				return getSequence();
			case SchemaPackage.COMPLEX_TYPE__GROUP1:
				if (coreType) return getGroup1();
				return ((FeatureMap.Internal)getGroup1()).getWrapper();
			case SchemaPackage.COMPLEX_TYPE__ATTRIBUTE:
				return getAttribute();
			case SchemaPackage.COMPLEX_TYPE__ATTRIBUTE_GROUP:
				return getAttributeGroup();
			case SchemaPackage.COMPLEX_TYPE__ANY_ATTRIBUTE1:
				return getAnyAttribute1();
			case SchemaPackage.COMPLEX_TYPE__ABSTRACT:
				return isAbstract();
			case SchemaPackage.COMPLEX_TYPE__BLOCK:
				return getBlock();
			case SchemaPackage.COMPLEX_TYPE__FINAL:
				return getFinal();
			case SchemaPackage.COMPLEX_TYPE__MIXED:
				return isMixed();
			case SchemaPackage.COMPLEX_TYPE__NAME:
				return getName();
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
			case SchemaPackage.COMPLEX_TYPE__SIMPLE_CONTENT:
				setSimpleContent((SimpleContentType)newValue);
				return;
			case SchemaPackage.COMPLEX_TYPE__COMPLEX_CONTENT:
				setComplexContent((ComplexContentType)newValue);
				return;
			case SchemaPackage.COMPLEX_TYPE__GROUP:
				setGroup((GroupRef)newValue);
				return;
			case SchemaPackage.COMPLEX_TYPE__ALL:
				setAll((All)newValue);
				return;
			case SchemaPackage.COMPLEX_TYPE__CHOICE:
				setChoice((ExplicitGroup)newValue);
				return;
			case SchemaPackage.COMPLEX_TYPE__SEQUENCE:
				setSequence((ExplicitGroup)newValue);
				return;
			case SchemaPackage.COMPLEX_TYPE__GROUP1:
				((FeatureMap.Internal)getGroup1()).set(newValue);
				return;
			case SchemaPackage.COMPLEX_TYPE__ATTRIBUTE:
				getAttribute().clear();
				getAttribute().addAll((Collection<? extends Attribute>)newValue);
				return;
			case SchemaPackage.COMPLEX_TYPE__ATTRIBUTE_GROUP:
				getAttributeGroup().clear();
				getAttributeGroup().addAll((Collection<? extends AttributeGroupRef>)newValue);
				return;
			case SchemaPackage.COMPLEX_TYPE__ANY_ATTRIBUTE1:
				setAnyAttribute1((Wildcard)newValue);
				return;
			case SchemaPackage.COMPLEX_TYPE__ABSTRACT:
				setAbstract((Boolean)newValue);
				return;
			case SchemaPackage.COMPLEX_TYPE__BLOCK:
				setBlock(newValue);
				return;
			case SchemaPackage.COMPLEX_TYPE__FINAL:
				setFinal(newValue);
				return;
			case SchemaPackage.COMPLEX_TYPE__MIXED:
				setMixed((Boolean)newValue);
				return;
			case SchemaPackage.COMPLEX_TYPE__NAME:
				setName((String)newValue);
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
			case SchemaPackage.COMPLEX_TYPE__SIMPLE_CONTENT:
				setSimpleContent((SimpleContentType)null);
				return;
			case SchemaPackage.COMPLEX_TYPE__COMPLEX_CONTENT:
				setComplexContent((ComplexContentType)null);
				return;
			case SchemaPackage.COMPLEX_TYPE__GROUP:
				setGroup((GroupRef)null);
				return;
			case SchemaPackage.COMPLEX_TYPE__ALL:
				setAll((All)null);
				return;
			case SchemaPackage.COMPLEX_TYPE__CHOICE:
				setChoice((ExplicitGroup)null);
				return;
			case SchemaPackage.COMPLEX_TYPE__SEQUENCE:
				setSequence((ExplicitGroup)null);
				return;
			case SchemaPackage.COMPLEX_TYPE__GROUP1:
				getGroup1().clear();
				return;
			case SchemaPackage.COMPLEX_TYPE__ATTRIBUTE:
				getAttribute().clear();
				return;
			case SchemaPackage.COMPLEX_TYPE__ATTRIBUTE_GROUP:
				getAttributeGroup().clear();
				return;
			case SchemaPackage.COMPLEX_TYPE__ANY_ATTRIBUTE1:
				setAnyAttribute1((Wildcard)null);
				return;
			case SchemaPackage.COMPLEX_TYPE__ABSTRACT:
				unsetAbstract();
				return;
			case SchemaPackage.COMPLEX_TYPE__BLOCK:
				setBlock(BLOCK_EDEFAULT);
				return;
			case SchemaPackage.COMPLEX_TYPE__FINAL:
				setFinal(FINAL_EDEFAULT);
				return;
			case SchemaPackage.COMPLEX_TYPE__MIXED:
				unsetMixed();
				return;
			case SchemaPackage.COMPLEX_TYPE__NAME:
				setName(NAME_EDEFAULT);
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
			case SchemaPackage.COMPLEX_TYPE__SIMPLE_CONTENT:
				return simpleContent != null;
			case SchemaPackage.COMPLEX_TYPE__COMPLEX_CONTENT:
				return complexContent != null;
			case SchemaPackage.COMPLEX_TYPE__GROUP:
				return group != null;
			case SchemaPackage.COMPLEX_TYPE__ALL:
				return all != null;
			case SchemaPackage.COMPLEX_TYPE__CHOICE:
				return choice != null;
			case SchemaPackage.COMPLEX_TYPE__SEQUENCE:
				return sequence != null;
			case SchemaPackage.COMPLEX_TYPE__GROUP1:
				return group1 != null && !group1.isEmpty();
			case SchemaPackage.COMPLEX_TYPE__ATTRIBUTE:
				return !getAttribute().isEmpty();
			case SchemaPackage.COMPLEX_TYPE__ATTRIBUTE_GROUP:
				return !getAttributeGroup().isEmpty();
			case SchemaPackage.COMPLEX_TYPE__ANY_ATTRIBUTE1:
				return anyAttribute1 != null;
			case SchemaPackage.COMPLEX_TYPE__ABSTRACT:
				return isSetAbstract();
			case SchemaPackage.COMPLEX_TYPE__BLOCK:
				return BLOCK_EDEFAULT == null ? block != null : !BLOCK_EDEFAULT.equals(block);
			case SchemaPackage.COMPLEX_TYPE__FINAL:
				return FINAL_EDEFAULT == null ? final_ != null : !FINAL_EDEFAULT.equals(final_);
			case SchemaPackage.COMPLEX_TYPE__MIXED:
				return isSetMixed();
			case SchemaPackage.COMPLEX_TYPE__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
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
		result.append(" (group1: ");
		result.append(group1);
		result.append(", abstract: ");
		if (abstractESet) result.append(abstract_); else result.append("<unset>");
		result.append(", block: ");
		result.append(block);
		result.append(", final: ");
		result.append(final_);
		result.append(", mixed: ");
		if (mixedESet) result.append(mixed); else result.append("<unset>");
		result.append(", name: ");
		result.append(name);
		result.append(')');
		return result.toString();
	}

} //ComplexTypeImpl
