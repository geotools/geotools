/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.gml.impl;

import net.opengis.gml.BoundingShapeType;
import net.opengis.gml.EnvelopeType;
import net.opengis.gml.GmlPackage;


import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Bounding Shape Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.gml.impl.BoundingShapeTypeImpl#getEnvelopeGroup <em>Envelope Group</em>}</li>
 *   <li>{@link net.opengis.gml.impl.BoundingShapeTypeImpl#getEnvelope <em>Envelope</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class BoundingShapeTypeImpl extends EObjectImpl implements BoundingShapeType {
    /**
	 * The cached value of the '{@link #getEnvelopeGroup() <em>Envelope Group</em>}' attribute list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getEnvelopeGroup()
	 * @generated
	 * @ordered
	 */
    protected FeatureMap envelopeGroup;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected BoundingShapeTypeImpl() {
		super();
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected EClass eStaticClass() {
		return GmlPackage.Literals.BOUNDING_SHAPE_TYPE;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public FeatureMap getEnvelopeGroup() {
		if (envelopeGroup == null) {
			envelopeGroup = new BasicFeatureMap(this, GmlPackage.BOUNDING_SHAPE_TYPE__ENVELOPE_GROUP);
		}
		return envelopeGroup;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EnvelopeType getEnvelope() {
		return (EnvelopeType)getEnvelopeGroup().get(GmlPackage.Literals.BOUNDING_SHAPE_TYPE__ENVELOPE, true);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetEnvelope(EnvelopeType newEnvelope, NotificationChain msgs) {
		return ((FeatureMap.Internal)getEnvelopeGroup()).basicAdd(GmlPackage.Literals.BOUNDING_SHAPE_TYPE__ENVELOPE, newEnvelope, msgs);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setEnvelope(EnvelopeType newEnvelope) {
		((FeatureMap.Internal)getEnvelopeGroup()).set(GmlPackage.Literals.BOUNDING_SHAPE_TYPE__ENVELOPE, newEnvelope);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case GmlPackage.BOUNDING_SHAPE_TYPE__ENVELOPE_GROUP:
				return ((InternalEList)getEnvelopeGroup()).basicRemove(otherEnd, msgs);
			case GmlPackage.BOUNDING_SHAPE_TYPE__ENVELOPE:
				return basicSetEnvelope(null, msgs);
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
			case GmlPackage.BOUNDING_SHAPE_TYPE__ENVELOPE_GROUP:
				if (coreType) return getEnvelopeGroup();
				return ((FeatureMap.Internal)getEnvelopeGroup()).getWrapper();
			case GmlPackage.BOUNDING_SHAPE_TYPE__ENVELOPE:
				return getEnvelope();
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
			case GmlPackage.BOUNDING_SHAPE_TYPE__ENVELOPE_GROUP:
				((FeatureMap.Internal)getEnvelopeGroup()).set(newValue);
				return;
			case GmlPackage.BOUNDING_SHAPE_TYPE__ENVELOPE:
				setEnvelope((EnvelopeType)newValue);
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
			case GmlPackage.BOUNDING_SHAPE_TYPE__ENVELOPE_GROUP:
				getEnvelopeGroup().clear();
				return;
			case GmlPackage.BOUNDING_SHAPE_TYPE__ENVELOPE:
				setEnvelope((EnvelopeType)null);
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
			case GmlPackage.BOUNDING_SHAPE_TYPE__ENVELOPE_GROUP:
				return envelopeGroup != null && !envelopeGroup.isEmpty();
			case GmlPackage.BOUNDING_SHAPE_TYPE__ENVELOPE:
				return getEnvelope() != null;
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
		result.append(" (envelopeGroup: ");
		result.append(envelopeGroup);
		result.append(')');
		return result.toString();
	}

} //BoundingShapeTypeImpl
