/**
 */
package net.opengis.ows20.impl;

import net.opengis.ows20.AcceptFormatsType;
import net.opengis.ows20.AcceptLanguagesType;
import net.opengis.ows20.AcceptVersionsType;
import net.opengis.ows20.GetCapabilitiesType;
import net.opengis.ows20.SectionsType;
import net.opengis.ows20.Ows20Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Get Capabilities Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.ows20.impl.GetCapabilitiesTypeImpl#getAcceptVersions <em>Accept Versions</em>}</li>
 *   <li>{@link net.opengis.ows20.impl.GetCapabilitiesTypeImpl#getSections <em>Sections</em>}</li>
 *   <li>{@link net.opengis.ows20.impl.GetCapabilitiesTypeImpl#getAcceptFormats <em>Accept Formats</em>}</li>
 *   <li>{@link net.opengis.ows20.impl.GetCapabilitiesTypeImpl#getAcceptLanguages <em>Accept Languages</em>}</li>
 *   <li>{@link net.opengis.ows20.impl.GetCapabilitiesTypeImpl#getUpdateSequence <em>Update Sequence</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class GetCapabilitiesTypeImpl extends EObjectImpl implements GetCapabilitiesType {
    /**
     * The cached value of the '{@link #getAcceptVersions() <em>Accept Versions</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAcceptVersions()
     * @generated
     * @ordered
     */
    protected AcceptVersionsType acceptVersions;

    /**
     * The cached value of the '{@link #getSections() <em>Sections</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSections()
     * @generated
     * @ordered
     */
    protected SectionsType sections;

    /**
     * The cached value of the '{@link #getAcceptFormats() <em>Accept Formats</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAcceptFormats()
     * @generated
     * @ordered
     */
    protected AcceptFormatsType acceptFormats;

    /**
     * The cached value of the '{@link #getAcceptLanguages() <em>Accept Languages</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAcceptLanguages()
     * @generated
     * @ordered
     */
    protected AcceptLanguagesType acceptLanguages;

    /**
     * The default value of the '{@link #getUpdateSequence() <em>Update Sequence</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getUpdateSequence()
     * @generated
     * @ordered
     */
    protected static final String UPDATE_SEQUENCE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getUpdateSequence() <em>Update Sequence</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getUpdateSequence()
     * @generated
     * @ordered
     */
    protected String updateSequence = UPDATE_SEQUENCE_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected GetCapabilitiesTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Ows20Package.Literals.GET_CAPABILITIES_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AcceptVersionsType getAcceptVersions() {
        return acceptVersions;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetAcceptVersions(AcceptVersionsType newAcceptVersions, NotificationChain msgs) {
        AcceptVersionsType oldAcceptVersions = acceptVersions;
        acceptVersions = newAcceptVersions;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Ows20Package.GET_CAPABILITIES_TYPE__ACCEPT_VERSIONS, oldAcceptVersions, newAcceptVersions);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setAcceptVersions(AcceptVersionsType newAcceptVersions) {
        if (newAcceptVersions != acceptVersions) {
            NotificationChain msgs = null;
            if (acceptVersions != null)
                msgs = ((InternalEObject)acceptVersions).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Ows20Package.GET_CAPABILITIES_TYPE__ACCEPT_VERSIONS, null, msgs);
            if (newAcceptVersions != null)
                msgs = ((InternalEObject)newAcceptVersions).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Ows20Package.GET_CAPABILITIES_TYPE__ACCEPT_VERSIONS, null, msgs);
            msgs = basicSetAcceptVersions(newAcceptVersions, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Ows20Package.GET_CAPABILITIES_TYPE__ACCEPT_VERSIONS, newAcceptVersions, newAcceptVersions));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SectionsType getSections() {
        return sections;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetSections(SectionsType newSections, NotificationChain msgs) {
        SectionsType oldSections = sections;
        sections = newSections;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Ows20Package.GET_CAPABILITIES_TYPE__SECTIONS, oldSections, newSections);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSections(SectionsType newSections) {
        if (newSections != sections) {
            NotificationChain msgs = null;
            if (sections != null)
                msgs = ((InternalEObject)sections).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Ows20Package.GET_CAPABILITIES_TYPE__SECTIONS, null, msgs);
            if (newSections != null)
                msgs = ((InternalEObject)newSections).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Ows20Package.GET_CAPABILITIES_TYPE__SECTIONS, null, msgs);
            msgs = basicSetSections(newSections, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Ows20Package.GET_CAPABILITIES_TYPE__SECTIONS, newSections, newSections));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AcceptFormatsType getAcceptFormats() {
        return acceptFormats;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetAcceptFormats(AcceptFormatsType newAcceptFormats, NotificationChain msgs) {
        AcceptFormatsType oldAcceptFormats = acceptFormats;
        acceptFormats = newAcceptFormats;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Ows20Package.GET_CAPABILITIES_TYPE__ACCEPT_FORMATS, oldAcceptFormats, newAcceptFormats);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setAcceptFormats(AcceptFormatsType newAcceptFormats) {
        if (newAcceptFormats != acceptFormats) {
            NotificationChain msgs = null;
            if (acceptFormats != null)
                msgs = ((InternalEObject)acceptFormats).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Ows20Package.GET_CAPABILITIES_TYPE__ACCEPT_FORMATS, null, msgs);
            if (newAcceptFormats != null)
                msgs = ((InternalEObject)newAcceptFormats).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Ows20Package.GET_CAPABILITIES_TYPE__ACCEPT_FORMATS, null, msgs);
            msgs = basicSetAcceptFormats(newAcceptFormats, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Ows20Package.GET_CAPABILITIES_TYPE__ACCEPT_FORMATS, newAcceptFormats, newAcceptFormats));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AcceptLanguagesType getAcceptLanguages() {
        return acceptLanguages;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetAcceptLanguages(AcceptLanguagesType newAcceptLanguages, NotificationChain msgs) {
        AcceptLanguagesType oldAcceptLanguages = acceptLanguages;
        acceptLanguages = newAcceptLanguages;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Ows20Package.GET_CAPABILITIES_TYPE__ACCEPT_LANGUAGES, oldAcceptLanguages, newAcceptLanguages);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setAcceptLanguages(AcceptLanguagesType newAcceptLanguages) {
        if (newAcceptLanguages != acceptLanguages) {
            NotificationChain msgs = null;
            if (acceptLanguages != null)
                msgs = ((InternalEObject)acceptLanguages).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Ows20Package.GET_CAPABILITIES_TYPE__ACCEPT_LANGUAGES, null, msgs);
            if (newAcceptLanguages != null)
                msgs = ((InternalEObject)newAcceptLanguages).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Ows20Package.GET_CAPABILITIES_TYPE__ACCEPT_LANGUAGES, null, msgs);
            msgs = basicSetAcceptLanguages(newAcceptLanguages, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Ows20Package.GET_CAPABILITIES_TYPE__ACCEPT_LANGUAGES, newAcceptLanguages, newAcceptLanguages));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getUpdateSequence() {
        return updateSequence;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setUpdateSequence(String newUpdateSequence) {
        String oldUpdateSequence = updateSequence;
        updateSequence = newUpdateSequence;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Ows20Package.GET_CAPABILITIES_TYPE__UPDATE_SEQUENCE, oldUpdateSequence, updateSequence));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Ows20Package.GET_CAPABILITIES_TYPE__ACCEPT_VERSIONS:
                return basicSetAcceptVersions(null, msgs);
            case Ows20Package.GET_CAPABILITIES_TYPE__SECTIONS:
                return basicSetSections(null, msgs);
            case Ows20Package.GET_CAPABILITIES_TYPE__ACCEPT_FORMATS:
                return basicSetAcceptFormats(null, msgs);
            case Ows20Package.GET_CAPABILITIES_TYPE__ACCEPT_LANGUAGES:
                return basicSetAcceptLanguages(null, msgs);
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
            case Ows20Package.GET_CAPABILITIES_TYPE__ACCEPT_VERSIONS:
                return getAcceptVersions();
            case Ows20Package.GET_CAPABILITIES_TYPE__SECTIONS:
                return getSections();
            case Ows20Package.GET_CAPABILITIES_TYPE__ACCEPT_FORMATS:
                return getAcceptFormats();
            case Ows20Package.GET_CAPABILITIES_TYPE__ACCEPT_LANGUAGES:
                return getAcceptLanguages();
            case Ows20Package.GET_CAPABILITIES_TYPE__UPDATE_SEQUENCE:
                return getUpdateSequence();
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
            case Ows20Package.GET_CAPABILITIES_TYPE__ACCEPT_VERSIONS:
                setAcceptVersions((AcceptVersionsType)newValue);
                return;
            case Ows20Package.GET_CAPABILITIES_TYPE__SECTIONS:
                setSections((SectionsType)newValue);
                return;
            case Ows20Package.GET_CAPABILITIES_TYPE__ACCEPT_FORMATS:
                setAcceptFormats((AcceptFormatsType)newValue);
                return;
            case Ows20Package.GET_CAPABILITIES_TYPE__ACCEPT_LANGUAGES:
                setAcceptLanguages((AcceptLanguagesType)newValue);
                return;
            case Ows20Package.GET_CAPABILITIES_TYPE__UPDATE_SEQUENCE:
                setUpdateSequence((String)newValue);
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
            case Ows20Package.GET_CAPABILITIES_TYPE__ACCEPT_VERSIONS:
                setAcceptVersions((AcceptVersionsType)null);
                return;
            case Ows20Package.GET_CAPABILITIES_TYPE__SECTIONS:
                setSections((SectionsType)null);
                return;
            case Ows20Package.GET_CAPABILITIES_TYPE__ACCEPT_FORMATS:
                setAcceptFormats((AcceptFormatsType)null);
                return;
            case Ows20Package.GET_CAPABILITIES_TYPE__ACCEPT_LANGUAGES:
                setAcceptLanguages((AcceptLanguagesType)null);
                return;
            case Ows20Package.GET_CAPABILITIES_TYPE__UPDATE_SEQUENCE:
                setUpdateSequence(UPDATE_SEQUENCE_EDEFAULT);
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
            case Ows20Package.GET_CAPABILITIES_TYPE__ACCEPT_VERSIONS:
                return acceptVersions != null;
            case Ows20Package.GET_CAPABILITIES_TYPE__SECTIONS:
                return sections != null;
            case Ows20Package.GET_CAPABILITIES_TYPE__ACCEPT_FORMATS:
                return acceptFormats != null;
            case Ows20Package.GET_CAPABILITIES_TYPE__ACCEPT_LANGUAGES:
                return acceptLanguages != null;
            case Ows20Package.GET_CAPABILITIES_TYPE__UPDATE_SEQUENCE:
                return UPDATE_SEQUENCE_EDEFAULT == null ? updateSequence != null : !UPDATE_SEQUENCE_EDEFAULT.equals(updateSequence);
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
        result.append(" (updateSequence: ");
        result.append(updateSequence);
        result.append(')');
        return result.toString();
    }

} //GetCapabilitiesTypeImpl
