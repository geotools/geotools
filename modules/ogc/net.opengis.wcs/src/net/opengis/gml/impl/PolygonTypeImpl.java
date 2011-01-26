/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.gml.impl;

import java.util.Collection;

import net.opengis.gml.AbstractRingPropertyType;
import net.opengis.gml.GmlPackage;

import net.opengis.gml.PolygonType;

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
 * An implementation of the model object '<em><b>Polygon Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.gml.impl.PolygonTypeImpl#getExterior <em>Exterior</em>}</li>
 *   <li>{@link net.opengis.gml.impl.PolygonTypeImpl#getInterior <em>Interior</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class PolygonTypeImpl extends AbstractSurfaceTypeImpl implements PolygonType {
    /**
	 * The cached value of the '{@link #getExterior() <em>Exterior</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getExterior()
	 * @generated
	 * @ordered
	 */
    protected AbstractRingPropertyType exterior;

    /**
	 * The cached value of the '{@link #getInterior() <em>Interior</em>}' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getInterior()
	 * @generated
	 * @ordered
	 */
    protected EList interior;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected PolygonTypeImpl() {
		super();
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected EClass eStaticClass() {
		return GmlPackage.Literals.POLYGON_TYPE;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public AbstractRingPropertyType getExterior() {
		return exterior;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetExterior(AbstractRingPropertyType newExterior, NotificationChain msgs) {
		AbstractRingPropertyType oldExterior = exterior;
		exterior = newExterior;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, GmlPackage.POLYGON_TYPE__EXTERIOR, oldExterior, newExterior);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setExterior(AbstractRingPropertyType newExterior) {
		if (newExterior != exterior) {
			NotificationChain msgs = null;
			if (exterior != null)
				msgs = ((InternalEObject)exterior).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - GmlPackage.POLYGON_TYPE__EXTERIOR, null, msgs);
			if (newExterior != null)
				msgs = ((InternalEObject)newExterior).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - GmlPackage.POLYGON_TYPE__EXTERIOR, null, msgs);
			msgs = basicSetExterior(newExterior, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GmlPackage.POLYGON_TYPE__EXTERIOR, newExterior, newExterior));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EList getInterior() {
		if (interior == null) {
			interior = new EObjectContainmentEList(AbstractRingPropertyType.class, this, GmlPackage.POLYGON_TYPE__INTERIOR);
		}
		return interior;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case GmlPackage.POLYGON_TYPE__EXTERIOR:
				return basicSetExterior(null, msgs);
			case GmlPackage.POLYGON_TYPE__INTERIOR:
				return ((InternalEList)getInterior()).basicRemove(otherEnd, msgs);
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
			case GmlPackage.POLYGON_TYPE__EXTERIOR:
				return getExterior();
			case GmlPackage.POLYGON_TYPE__INTERIOR:
				return getInterior();
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
			case GmlPackage.POLYGON_TYPE__EXTERIOR:
				setExterior((AbstractRingPropertyType)newValue);
				return;
			case GmlPackage.POLYGON_TYPE__INTERIOR:
				getInterior().clear();
				getInterior().addAll((Collection)newValue);
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
			case GmlPackage.POLYGON_TYPE__EXTERIOR:
				setExterior((AbstractRingPropertyType)null);
				return;
			case GmlPackage.POLYGON_TYPE__INTERIOR:
				getInterior().clear();
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
			case GmlPackage.POLYGON_TYPE__EXTERIOR:
				return exterior != null;
			case GmlPackage.POLYGON_TYPE__INTERIOR:
				return interior != null && !interior.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //PolygonTypeImpl
