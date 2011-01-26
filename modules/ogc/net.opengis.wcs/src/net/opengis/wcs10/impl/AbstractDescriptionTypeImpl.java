/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs10.impl;

import java.util.Collection;

import net.opengis.wcs10.AbstractDescriptionType;
import net.opengis.wcs10.MetadataLinkType;
import net.opengis.wcs10.Wcs10Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Abstract Description Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs10.impl.AbstractDescriptionTypeImpl#getMetadataLink <em>Metadata Link</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.AbstractDescriptionTypeImpl#getDescription1 <em>Description1</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.AbstractDescriptionTypeImpl#getName1 <em>Name1</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.AbstractDescriptionTypeImpl#getLabel <em>Label</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class AbstractDescriptionTypeImpl extends AbstractDescriptionBaseTypeImpl implements AbstractDescriptionType {
    /**
	 * The cached value of the '{@link #getMetadataLink() <em>Metadata Link</em>}' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getMetadataLink()
	 * @generated
	 * @ordered
	 */
    protected EList metadataLink;

    /**
	 * The default value of the '{@link #getDescription1() <em>Description1</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getDescription1()
	 * @generated
	 * @ordered
	 */
    protected static final String DESCRIPTION1_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getDescription1() <em>Description1</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getDescription1()
	 * @generated
	 * @ordered
	 */
    protected String description1 = DESCRIPTION1_EDEFAULT;

    /**
	 * The default value of the '{@link #getName1() <em>Name1</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getName1()
	 * @generated
	 * @ordered
	 */
    protected static final String NAME1_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getName1() <em>Name1</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getName1()
	 * @generated
	 * @ordered
	 */
    protected String name1 = NAME1_EDEFAULT;

    /**
	 * The default value of the '{@link #getLabel() <em>Label</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getLabel()
	 * @generated
	 * @ordered
	 */
    protected static final String LABEL_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getLabel() <em>Label</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getLabel()
	 * @generated
	 * @ordered
	 */
    protected String label = LABEL_EDEFAULT;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected AbstractDescriptionTypeImpl() {
		super();
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected EClass eStaticClass() {
		return Wcs10Package.Literals.ABSTRACT_DESCRIPTION_TYPE;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EList getMetadataLink() {
		if (metadataLink == null) {
			metadataLink = new EObjectContainmentEList(MetadataLinkType.class, this, Wcs10Package.ABSTRACT_DESCRIPTION_TYPE__METADATA_LINK);
		}
		return metadataLink;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public String getDescription1() {
		return description1;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setDescription1(String newDescription1) {
		String oldDescription1 = description1;
		description1 = newDescription1;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.ABSTRACT_DESCRIPTION_TYPE__DESCRIPTION1, oldDescription1, description1));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public String getName1() {
		return name1;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setName1(String newName1) {
		String oldName1 = name1;
		name1 = newName1;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.ABSTRACT_DESCRIPTION_TYPE__NAME1, oldName1, name1));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public String getLabel() {
		return label;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setLabel(String newLabel) {
		String oldLabel = label;
		label = newLabel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.ABSTRACT_DESCRIPTION_TYPE__LABEL, oldLabel, label));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case Wcs10Package.ABSTRACT_DESCRIPTION_TYPE__METADATA_LINK:
				return ((InternalEList)getMetadataLink()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case Wcs10Package.ABSTRACT_DESCRIPTION_TYPE__METADATA_LINK:
				return getMetadataLink();
			case Wcs10Package.ABSTRACT_DESCRIPTION_TYPE__DESCRIPTION1:
				return getDescription1();
			case Wcs10Package.ABSTRACT_DESCRIPTION_TYPE__NAME1:
				return getName1();
			case Wcs10Package.ABSTRACT_DESCRIPTION_TYPE__LABEL:
				return getLabel();
		}
		return super.eGet(featureID, resolve, coreType);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case Wcs10Package.ABSTRACT_DESCRIPTION_TYPE__METADATA_LINK:
				getMetadataLink().clear();
				getMetadataLink().addAll((Collection)newValue);
				return;
			case Wcs10Package.ABSTRACT_DESCRIPTION_TYPE__DESCRIPTION1:
				setDescription1((String)newValue);
				return;
			case Wcs10Package.ABSTRACT_DESCRIPTION_TYPE__NAME1:
				setName1((String)newValue);
				return;
			case Wcs10Package.ABSTRACT_DESCRIPTION_TYPE__LABEL:
				setLabel((String)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void eUnset(int featureID) {
		switch (featureID) {
			case Wcs10Package.ABSTRACT_DESCRIPTION_TYPE__METADATA_LINK:
				getMetadataLink().clear();
				return;
			case Wcs10Package.ABSTRACT_DESCRIPTION_TYPE__DESCRIPTION1:
				setDescription1(DESCRIPTION1_EDEFAULT);
				return;
			case Wcs10Package.ABSTRACT_DESCRIPTION_TYPE__NAME1:
				setName1(NAME1_EDEFAULT);
				return;
			case Wcs10Package.ABSTRACT_DESCRIPTION_TYPE__LABEL:
				setLabel(LABEL_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean eIsSet(int featureID) {
		switch (featureID) {
			case Wcs10Package.ABSTRACT_DESCRIPTION_TYPE__METADATA_LINK:
				return metadataLink != null && !metadataLink.isEmpty();
			case Wcs10Package.ABSTRACT_DESCRIPTION_TYPE__DESCRIPTION1:
				return DESCRIPTION1_EDEFAULT == null ? description1 != null : !DESCRIPTION1_EDEFAULT.equals(description1);
			case Wcs10Package.ABSTRACT_DESCRIPTION_TYPE__NAME1:
				return NAME1_EDEFAULT == null ? name1 != null : !NAME1_EDEFAULT.equals(name1);
			case Wcs10Package.ABSTRACT_DESCRIPTION_TYPE__LABEL:
				return LABEL_EDEFAULT == null ? label != null : !LABEL_EDEFAULT.equals(label);
		}
		return super.eIsSet(featureID);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (description1: ");
		result.append(description1);
		result.append(", name1: ");
		result.append(name1);
		result.append(", label: ");
		result.append(label);
		result.append(')');
		return result.toString();
	}

} //AbstractDescriptionTypeImpl
