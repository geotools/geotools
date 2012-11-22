/**
 */
package net.opengis.wcs20.impl;

import net.opengis.ows20.impl.CapabilitiesBaseTypeImpl;

import net.opengis.wcs20.CapabilitiesType;
import net.opengis.wcs20.ContentsType;
import net.opengis.wcs20.ServiceMetadataType;
import net.opengis.wcs20.Wcs20Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Capabilities Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs20.impl.CapabilitiesTypeImpl#getServiceMetadata <em>Service Metadata</em>}</li>
 *   <li>{@link net.opengis.wcs20.impl.CapabilitiesTypeImpl#getContents <em>Contents</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CapabilitiesTypeImpl extends CapabilitiesBaseTypeImpl implements CapabilitiesType {
    /**
     * The cached value of the '{@link #getServiceMetadata() <em>Service Metadata</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getServiceMetadata()
     * @generated
     * @ordered
     */
    protected ServiceMetadataType serviceMetadata;

    /**
     * The cached value of the '{@link #getContents() <em>Contents</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getContents()
     * @generated
     * @ordered
     */
    protected ContentsType contents;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected CapabilitiesTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Wcs20Package.Literals.CAPABILITIES_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ServiceMetadataType getServiceMetadata() {
        return serviceMetadata;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetServiceMetadata(ServiceMetadataType newServiceMetadata, NotificationChain msgs) {
        ServiceMetadataType oldServiceMetadata = serviceMetadata;
        serviceMetadata = newServiceMetadata;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wcs20Package.CAPABILITIES_TYPE__SERVICE_METADATA, oldServiceMetadata, newServiceMetadata);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setServiceMetadata(ServiceMetadataType newServiceMetadata) {
        if (newServiceMetadata != serviceMetadata) {
            NotificationChain msgs = null;
            if (serviceMetadata != null)
                msgs = ((InternalEObject)serviceMetadata).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wcs20Package.CAPABILITIES_TYPE__SERVICE_METADATA, null, msgs);
            if (newServiceMetadata != null)
                msgs = ((InternalEObject)newServiceMetadata).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wcs20Package.CAPABILITIES_TYPE__SERVICE_METADATA, null, msgs);
            msgs = basicSetServiceMetadata(newServiceMetadata, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs20Package.CAPABILITIES_TYPE__SERVICE_METADATA, newServiceMetadata, newServiceMetadata));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ContentsType getContents() {
        return contents;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetContents(ContentsType newContents, NotificationChain msgs) {
        ContentsType oldContents = contents;
        contents = newContents;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wcs20Package.CAPABILITIES_TYPE__CONTENTS, oldContents, newContents);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setContents(ContentsType newContents) {
        if (newContents != contents) {
            NotificationChain msgs = null;
            if (contents != null)
                msgs = ((InternalEObject)contents).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wcs20Package.CAPABILITIES_TYPE__CONTENTS, null, msgs);
            if (newContents != null)
                msgs = ((InternalEObject)newContents).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wcs20Package.CAPABILITIES_TYPE__CONTENTS, null, msgs);
            msgs = basicSetContents(newContents, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs20Package.CAPABILITIES_TYPE__CONTENTS, newContents, newContents));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Wcs20Package.CAPABILITIES_TYPE__SERVICE_METADATA:
                return basicSetServiceMetadata(null, msgs);
            case Wcs20Package.CAPABILITIES_TYPE__CONTENTS:
                return basicSetContents(null, msgs);
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
            case Wcs20Package.CAPABILITIES_TYPE__SERVICE_METADATA:
                return getServiceMetadata();
            case Wcs20Package.CAPABILITIES_TYPE__CONTENTS:
                return getContents();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case Wcs20Package.CAPABILITIES_TYPE__SERVICE_METADATA:
                setServiceMetadata((ServiceMetadataType)newValue);
                return;
            case Wcs20Package.CAPABILITIES_TYPE__CONTENTS:
                setContents((ContentsType)newValue);
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
            case Wcs20Package.CAPABILITIES_TYPE__SERVICE_METADATA:
                setServiceMetadata((ServiceMetadataType)null);
                return;
            case Wcs20Package.CAPABILITIES_TYPE__CONTENTS:
                setContents((ContentsType)null);
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
            case Wcs20Package.CAPABILITIES_TYPE__SERVICE_METADATA:
                return serviceMetadata != null;
            case Wcs20Package.CAPABILITIES_TYPE__CONTENTS:
                return contents != null;
        }
        return super.eIsSet(featureID);
    }

} //CapabilitiesTypeImpl
