/**
 */
package net.opengis.ows20.impl;

import java.util.Collection;

import net.opengis.ows20.HTTPType;
import net.opengis.ows20.RequestMethodType;
import net.opengis.ows20.Ows20Package;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>HTTP Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.ows20.impl.HTTPTypeImpl#getGroup <em>Group</em>}</li>
 *   <li>{@link net.opengis.ows20.impl.HTTPTypeImpl#getGet <em>Get</em>}</li>
 *   <li>{@link net.opengis.ows20.impl.HTTPTypeImpl#getPost <em>Post</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class HTTPTypeImpl extends EObjectImpl implements HTTPType {
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
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected HTTPTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Ows20Package.Literals.HTTP_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getGroup() {
        if (group == null) {
            group = new BasicFeatureMap(this, Ows20Package.HTTP_TYPE__GROUP);
        }
        return group;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<RequestMethodType> getGet() {
        return getGroup().list(Ows20Package.Literals.HTTP_TYPE__GET);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<RequestMethodType> getPost() {
        return getGroup().list(Ows20Package.Literals.HTTP_TYPE__POST);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Ows20Package.HTTP_TYPE__GROUP:
                return ((InternalEList<?>)getGroup()).basicRemove(otherEnd, msgs);
            case Ows20Package.HTTP_TYPE__GET:
                return ((InternalEList<?>)getGet()).basicRemove(otherEnd, msgs);
            case Ows20Package.HTTP_TYPE__POST:
                return ((InternalEList<?>)getPost()).basicRemove(otherEnd, msgs);
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
            case Ows20Package.HTTP_TYPE__GROUP:
                if (coreType) return getGroup();
                return ((FeatureMap.Internal)getGroup()).getWrapper();
            case Ows20Package.HTTP_TYPE__GET:
                return getGet();
            case Ows20Package.HTTP_TYPE__POST:
                return getPost();
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
            case Ows20Package.HTTP_TYPE__GROUP:
                ((FeatureMap.Internal)getGroup()).set(newValue);
                return;
            case Ows20Package.HTTP_TYPE__GET:
                getGet().clear();
                getGet().addAll((Collection<? extends RequestMethodType>)newValue);
                return;
            case Ows20Package.HTTP_TYPE__POST:
                getPost().clear();
                getPost().addAll((Collection<? extends RequestMethodType>)newValue);
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
            case Ows20Package.HTTP_TYPE__GROUP:
                getGroup().clear();
                return;
            case Ows20Package.HTTP_TYPE__GET:
                getGet().clear();
                return;
            case Ows20Package.HTTP_TYPE__POST:
                getPost().clear();
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
            case Ows20Package.HTTP_TYPE__GROUP:
                return group != null && !group.isEmpty();
            case Ows20Package.HTTP_TYPE__GET:
                return !getGet().isEmpty();
            case Ows20Package.HTTP_TYPE__POST:
                return !getPost().isEmpty();
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

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (group: ");
        result.append(group);
        result.append(')');
        return result.toString();
    }

} //HTTPTypeImpl
