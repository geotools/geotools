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
import org.w3._2001.schema.NamedAttributeGroup;
import org.w3._2001.schema.NamedGroup;
import org.w3._2001.schema.RedefineType;
import org.w3._2001.schema.SchemaPackage;
import org.w3._2001.schema.TopLevelComplexType;
import org.w3._2001.schema.TopLevelSimpleType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Redefine Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.w3._2001.schema.impl.RedefineTypeImpl#getGroup <em>Group</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.RedefineTypeImpl#getAnnotation <em>Annotation</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.RedefineTypeImpl#getSimpleType <em>Simple Type</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.RedefineTypeImpl#getComplexType <em>Complex Type</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.RedefineTypeImpl#getGroup1 <em>Group1</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.RedefineTypeImpl#getAttributeGroup <em>Attribute Group</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.RedefineTypeImpl#getId <em>Id</em>}</li>
 *   <li>{@link org.w3._2001.schema.impl.RedefineTypeImpl#getSchemaLocation <em>Schema Location</em>}</li>
 * </ul>
 *
 * @generated
 */
public class RedefineTypeImpl extends OpenAttrsImpl implements RedefineType {
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
	 * The default value of the '{@link #getSchemaLocation() <em>Schema Location</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSchemaLocation()
	 * @generated
	 * @ordered
	 */
	protected static final String SCHEMA_LOCATION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSchemaLocation() <em>Schema Location</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSchemaLocation()
	 * @generated
	 * @ordered
	 */
	protected String schemaLocation = SCHEMA_LOCATION_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected RedefineTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchemaPackage.Literals.REDEFINE_TYPE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FeatureMap getGroup() {
		if (group == null) {
			group = new BasicFeatureMap(this, SchemaPackage.REDEFINE_TYPE__GROUP);
		}
		return group;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<AnnotationType> getAnnotation() {
		return getGroup().list(SchemaPackage.Literals.REDEFINE_TYPE__ANNOTATION);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<TopLevelSimpleType> getSimpleType() {
		return getGroup().list(SchemaPackage.Literals.REDEFINE_TYPE__SIMPLE_TYPE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<TopLevelComplexType> getComplexType() {
		return getGroup().list(SchemaPackage.Literals.REDEFINE_TYPE__COMPLEX_TYPE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<NamedGroup> getGroup1() {
		return getGroup().list(SchemaPackage.Literals.REDEFINE_TYPE__GROUP1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<NamedAttributeGroup> getAttributeGroup() {
		return getGroup().list(SchemaPackage.Literals.REDEFINE_TYPE__ATTRIBUTE_GROUP);
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
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.REDEFINE_TYPE__ID, oldId, id));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getSchemaLocation() {
		return schemaLocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSchemaLocation(String newSchemaLocation) {
		String oldSchemaLocation = schemaLocation;
		schemaLocation = newSchemaLocation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchemaPackage.REDEFINE_TYPE__SCHEMA_LOCATION, oldSchemaLocation, schemaLocation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SchemaPackage.REDEFINE_TYPE__GROUP:
				return ((InternalEList<?>)getGroup()).basicRemove(otherEnd, msgs);
			case SchemaPackage.REDEFINE_TYPE__ANNOTATION:
				return ((InternalEList<?>)getAnnotation()).basicRemove(otherEnd, msgs);
			case SchemaPackage.REDEFINE_TYPE__SIMPLE_TYPE:
				return ((InternalEList<?>)getSimpleType()).basicRemove(otherEnd, msgs);
			case SchemaPackage.REDEFINE_TYPE__COMPLEX_TYPE:
				return ((InternalEList<?>)getComplexType()).basicRemove(otherEnd, msgs);
			case SchemaPackage.REDEFINE_TYPE__GROUP1:
				return ((InternalEList<?>)getGroup1()).basicRemove(otherEnd, msgs);
			case SchemaPackage.REDEFINE_TYPE__ATTRIBUTE_GROUP:
				return ((InternalEList<?>)getAttributeGroup()).basicRemove(otherEnd, msgs);
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
			case SchemaPackage.REDEFINE_TYPE__GROUP:
				if (coreType) return getGroup();
				return ((FeatureMap.Internal)getGroup()).getWrapper();
			case SchemaPackage.REDEFINE_TYPE__ANNOTATION:
				return getAnnotation();
			case SchemaPackage.REDEFINE_TYPE__SIMPLE_TYPE:
				return getSimpleType();
			case SchemaPackage.REDEFINE_TYPE__COMPLEX_TYPE:
				return getComplexType();
			case SchemaPackage.REDEFINE_TYPE__GROUP1:
				return getGroup1();
			case SchemaPackage.REDEFINE_TYPE__ATTRIBUTE_GROUP:
				return getAttributeGroup();
			case SchemaPackage.REDEFINE_TYPE__ID:
				return getId();
			case SchemaPackage.REDEFINE_TYPE__SCHEMA_LOCATION:
				return getSchemaLocation();
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
			case SchemaPackage.REDEFINE_TYPE__GROUP:
				((FeatureMap.Internal)getGroup()).set(newValue);
				return;
			case SchemaPackage.REDEFINE_TYPE__ANNOTATION:
				getAnnotation().clear();
				getAnnotation().addAll((Collection<? extends AnnotationType>)newValue);
				return;
			case SchemaPackage.REDEFINE_TYPE__SIMPLE_TYPE:
				getSimpleType().clear();
				getSimpleType().addAll((Collection<? extends TopLevelSimpleType>)newValue);
				return;
			case SchemaPackage.REDEFINE_TYPE__COMPLEX_TYPE:
				getComplexType().clear();
				getComplexType().addAll((Collection<? extends TopLevelComplexType>)newValue);
				return;
			case SchemaPackage.REDEFINE_TYPE__GROUP1:
				getGroup1().clear();
				getGroup1().addAll((Collection<? extends NamedGroup>)newValue);
				return;
			case SchemaPackage.REDEFINE_TYPE__ATTRIBUTE_GROUP:
				getAttributeGroup().clear();
				getAttributeGroup().addAll((Collection<? extends NamedAttributeGroup>)newValue);
				return;
			case SchemaPackage.REDEFINE_TYPE__ID:
				setId((String)newValue);
				return;
			case SchemaPackage.REDEFINE_TYPE__SCHEMA_LOCATION:
				setSchemaLocation((String)newValue);
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
			case SchemaPackage.REDEFINE_TYPE__GROUP:
				getGroup().clear();
				return;
			case SchemaPackage.REDEFINE_TYPE__ANNOTATION:
				getAnnotation().clear();
				return;
			case SchemaPackage.REDEFINE_TYPE__SIMPLE_TYPE:
				getSimpleType().clear();
				return;
			case SchemaPackage.REDEFINE_TYPE__COMPLEX_TYPE:
				getComplexType().clear();
				return;
			case SchemaPackage.REDEFINE_TYPE__GROUP1:
				getGroup1().clear();
				return;
			case SchemaPackage.REDEFINE_TYPE__ATTRIBUTE_GROUP:
				getAttributeGroup().clear();
				return;
			case SchemaPackage.REDEFINE_TYPE__ID:
				setId(ID_EDEFAULT);
				return;
			case SchemaPackage.REDEFINE_TYPE__SCHEMA_LOCATION:
				setSchemaLocation(SCHEMA_LOCATION_EDEFAULT);
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
			case SchemaPackage.REDEFINE_TYPE__GROUP:
				return group != null && !group.isEmpty();
			case SchemaPackage.REDEFINE_TYPE__ANNOTATION:
				return !getAnnotation().isEmpty();
			case SchemaPackage.REDEFINE_TYPE__SIMPLE_TYPE:
				return !getSimpleType().isEmpty();
			case SchemaPackage.REDEFINE_TYPE__COMPLEX_TYPE:
				return !getComplexType().isEmpty();
			case SchemaPackage.REDEFINE_TYPE__GROUP1:
				return !getGroup1().isEmpty();
			case SchemaPackage.REDEFINE_TYPE__ATTRIBUTE_GROUP:
				return !getAttributeGroup().isEmpty();
			case SchemaPackage.REDEFINE_TYPE__ID:
				return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
			case SchemaPackage.REDEFINE_TYPE__SCHEMA_LOCATION:
				return SCHEMA_LOCATION_EDEFAULT == null ? schemaLocation != null : !SCHEMA_LOCATION_EDEFAULT.equals(schemaLocation);
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
		result.append(", id: ");
		result.append(id);
		result.append(", schemaLocation: ");
		result.append(schemaLocation);
		result.append(')');
		return result.toString();
	}

} //RedefineTypeImpl
