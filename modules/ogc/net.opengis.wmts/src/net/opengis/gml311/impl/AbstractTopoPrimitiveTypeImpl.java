/**
 */
package net.opengis.gml311.impl;

import java.util.Collection;

import net.opengis.gml311.AbstractTopoPrimitiveType;
import net.opengis.gml311.ContainerPropertyType;
import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.IsolatedPropertyType;

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
 * An implementation of the model object '<em><b>Abstract Topo Primitive Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.AbstractTopoPrimitiveTypeImpl#getIsolated <em>Isolated</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.AbstractTopoPrimitiveTypeImpl#getContainer <em>Container</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class AbstractTopoPrimitiveTypeImpl extends AbstractTopologyTypeImpl implements AbstractTopoPrimitiveType {
    /**
     * The cached value of the '{@link #getIsolated() <em>Isolated</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getIsolated()
     * @generated
     * @ordered
     */
    protected EList<IsolatedPropertyType> isolated;

    /**
     * The cached value of the '{@link #getContainer() <em>Container</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getContainer()
     * @generated
     * @ordered
     */
    protected ContainerPropertyType container;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected AbstractTopoPrimitiveTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getAbstractTopoPrimitiveType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<IsolatedPropertyType> getIsolated() {
        if (isolated == null) {
            isolated = new EObjectContainmentEList<IsolatedPropertyType>(IsolatedPropertyType.class, this, Gml311Package.ABSTRACT_TOPO_PRIMITIVE_TYPE__ISOLATED);
        }
        return isolated;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ContainerPropertyType getContainer() {
        return container;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetContainer(ContainerPropertyType newContainer, NotificationChain msgs) {
        ContainerPropertyType oldContainer = container;
        container = newContainer;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.ABSTRACT_TOPO_PRIMITIVE_TYPE__CONTAINER, oldContainer, newContainer);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setContainer(ContainerPropertyType newContainer) {
        if (newContainer != container) {
            NotificationChain msgs = null;
            if (container != null)
                msgs = ((InternalEObject)container).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ABSTRACT_TOPO_PRIMITIVE_TYPE__CONTAINER, null, msgs);
            if (newContainer != null)
                msgs = ((InternalEObject)newContainer).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ABSTRACT_TOPO_PRIMITIVE_TYPE__CONTAINER, null, msgs);
            msgs = basicSetContainer(newContainer, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.ABSTRACT_TOPO_PRIMITIVE_TYPE__CONTAINER, newContainer, newContainer));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.ABSTRACT_TOPO_PRIMITIVE_TYPE__ISOLATED:
                return ((InternalEList<?>)getIsolated()).basicRemove(otherEnd, msgs);
            case Gml311Package.ABSTRACT_TOPO_PRIMITIVE_TYPE__CONTAINER:
                return basicSetContainer(null, msgs);
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
            case Gml311Package.ABSTRACT_TOPO_PRIMITIVE_TYPE__ISOLATED:
                return getIsolated();
            case Gml311Package.ABSTRACT_TOPO_PRIMITIVE_TYPE__CONTAINER:
                return getContainer();
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
            case Gml311Package.ABSTRACT_TOPO_PRIMITIVE_TYPE__ISOLATED:
                getIsolated().clear();
                getIsolated().addAll((Collection<? extends IsolatedPropertyType>)newValue);
                return;
            case Gml311Package.ABSTRACT_TOPO_PRIMITIVE_TYPE__CONTAINER:
                setContainer((ContainerPropertyType)newValue);
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
            case Gml311Package.ABSTRACT_TOPO_PRIMITIVE_TYPE__ISOLATED:
                getIsolated().clear();
                return;
            case Gml311Package.ABSTRACT_TOPO_PRIMITIVE_TYPE__CONTAINER:
                setContainer((ContainerPropertyType)null);
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
            case Gml311Package.ABSTRACT_TOPO_PRIMITIVE_TYPE__ISOLATED:
                return isolated != null && !isolated.isEmpty();
            case Gml311Package.ABSTRACT_TOPO_PRIMITIVE_TYPE__CONTAINER:
                return container != null;
        }
        return super.eIsSet(featureID);
    }

} //AbstractTopoPrimitiveTypeImpl
