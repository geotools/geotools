/**
 */
package net.opengis.wmts.v_1.impl;

import java.util.Collection;

import net.opengis.ows11.OnlineResourceType;

import net.opengis.ows11.impl.CapabilitiesBaseTypeImpl;

import net.opengis.wmts.v_1.CapabilitiesType;
import net.opengis.wmts.v_1.ContentsType;
import net.opengis.wmts.v_1.ThemesType;
import net.opengis.wmts.v_1.wmtsv_1Package;

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
 * An implementation of the model object '<em><b>Capabilities Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wmts.v_1.impl.CapabilitiesTypeImpl#getContents <em>Contents</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.CapabilitiesTypeImpl#getThemes <em>Themes</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.CapabilitiesTypeImpl#getWSDL <em>WSDL</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.CapabilitiesTypeImpl#getServiceMetadataURL <em>Service Metadata URL</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CapabilitiesTypeImpl extends CapabilitiesBaseTypeImpl implements CapabilitiesType {
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
     * The cached value of the '{@link #getThemes() <em>Themes</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getThemes()
     * @generated
     * @ordered
     */
    protected EList<ThemesType> themes;

    /**
     * The cached value of the '{@link #getWSDL() <em>WSDL</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getWSDL()
     * @generated
     * @ordered
     */
    protected EList<OnlineResourceType> wSDL;

    /**
     * The cached value of the '{@link #getServiceMetadataURL() <em>Service Metadata URL</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getServiceMetadataURL()
     * @generated
     * @ordered
     */
    protected EList<OnlineResourceType> serviceMetadataURL;

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
        return wmtsv_1Package.Literals.CAPABILITIES_TYPE;
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
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, wmtsv_1Package.CAPABILITIES_TYPE__CONTENTS, oldContents, newContents);
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
                msgs = ((InternalEObject)contents).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - wmtsv_1Package.CAPABILITIES_TYPE__CONTENTS, null, msgs);
            if (newContents != null)
                msgs = ((InternalEObject)newContents).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - wmtsv_1Package.CAPABILITIES_TYPE__CONTENTS, null, msgs);
            msgs = basicSetContents(newContents, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, wmtsv_1Package.CAPABILITIES_TYPE__CONTENTS, newContents, newContents));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<ThemesType> getThemes() {
        if (themes == null) {
            themes = new EObjectContainmentEList<ThemesType>(ThemesType.class, this, wmtsv_1Package.CAPABILITIES_TYPE__THEMES);
        }
        return themes;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<OnlineResourceType> getWSDL() {
        if (wSDL == null) {
            wSDL = new EObjectContainmentEList<OnlineResourceType>(OnlineResourceType.class, this, wmtsv_1Package.CAPABILITIES_TYPE__WSDL);
        }
        return wSDL;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<OnlineResourceType> getServiceMetadataURL() {
        if (serviceMetadataURL == null) {
            serviceMetadataURL = new EObjectContainmentEList<OnlineResourceType>(OnlineResourceType.class, this, wmtsv_1Package.CAPABILITIES_TYPE__SERVICE_METADATA_URL);
        }
        return serviceMetadataURL;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case wmtsv_1Package.CAPABILITIES_TYPE__CONTENTS:
                return basicSetContents(null, msgs);
            case wmtsv_1Package.CAPABILITIES_TYPE__THEMES:
                return ((InternalEList<?>)getThemes()).basicRemove(otherEnd, msgs);
            case wmtsv_1Package.CAPABILITIES_TYPE__WSDL:
                return ((InternalEList<?>)getWSDL()).basicRemove(otherEnd, msgs);
            case wmtsv_1Package.CAPABILITIES_TYPE__SERVICE_METADATA_URL:
                return ((InternalEList<?>)getServiceMetadataURL()).basicRemove(otherEnd, msgs);
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
            case wmtsv_1Package.CAPABILITIES_TYPE__CONTENTS:
                return getContents();
            case wmtsv_1Package.CAPABILITIES_TYPE__THEMES:
                return getThemes();
            case wmtsv_1Package.CAPABILITIES_TYPE__WSDL:
                return getWSDL();
            case wmtsv_1Package.CAPABILITIES_TYPE__SERVICE_METADATA_URL:
                return getServiceMetadataURL();
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
            case wmtsv_1Package.CAPABILITIES_TYPE__CONTENTS:
                setContents((ContentsType)newValue);
                return;
            case wmtsv_1Package.CAPABILITIES_TYPE__THEMES:
                getThemes().clear();
                getThemes().addAll((Collection<? extends ThemesType>)newValue);
                return;
            case wmtsv_1Package.CAPABILITIES_TYPE__WSDL:
                getWSDL().clear();
                getWSDL().addAll((Collection<? extends OnlineResourceType>)newValue);
                return;
            case wmtsv_1Package.CAPABILITIES_TYPE__SERVICE_METADATA_URL:
                getServiceMetadataURL().clear();
                getServiceMetadataURL().addAll((Collection<? extends OnlineResourceType>)newValue);
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
            case wmtsv_1Package.CAPABILITIES_TYPE__CONTENTS:
                setContents((ContentsType)null);
                return;
            case wmtsv_1Package.CAPABILITIES_TYPE__THEMES:
                getThemes().clear();
                return;
            case wmtsv_1Package.CAPABILITIES_TYPE__WSDL:
                getWSDL().clear();
                return;
            case wmtsv_1Package.CAPABILITIES_TYPE__SERVICE_METADATA_URL:
                getServiceMetadataURL().clear();
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
            case wmtsv_1Package.CAPABILITIES_TYPE__CONTENTS:
                return contents != null;
            case wmtsv_1Package.CAPABILITIES_TYPE__THEMES:
                return themes != null && !themes.isEmpty();
            case wmtsv_1Package.CAPABILITIES_TYPE__WSDL:
                return wSDL != null && !wSDL.isEmpty();
            case wmtsv_1Package.CAPABILITIES_TYPE__SERVICE_METADATA_URL:
                return serviceMetadataURL != null && !serviceMetadataURL.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //CapabilitiesTypeImpl
