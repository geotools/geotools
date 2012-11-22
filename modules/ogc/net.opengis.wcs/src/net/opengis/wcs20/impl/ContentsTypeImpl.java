/**
 */
package net.opengis.wcs20.impl;

import java.util.Collection;

import net.opengis.ows20.impl.ContentsBaseTypeImpl;

import net.opengis.wcs20.ContentsType;
import net.opengis.wcs20.CoverageSummaryType;
import net.opengis.wcs20.ExtensionType;
import net.opengis.wcs20.Wcs20Package;

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
 * An implementation of the model object '<em><b>Contents Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs20.impl.ContentsTypeImpl#getCoverageSummary <em>Coverage Summary</em>}</li>
 *   <li>{@link net.opengis.wcs20.impl.ContentsTypeImpl#getExtension <em>Extension</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ContentsTypeImpl extends ContentsBaseTypeImpl implements ContentsType {
    /**
     * The cached value of the '{@link #getCoverageSummary() <em>Coverage Summary</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCoverageSummary()
     * @generated
     * @ordered
     */
    protected EList<CoverageSummaryType> coverageSummary;

    /**
     * The cached value of the '{@link #getExtension() <em>Extension</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getExtension()
     * @generated
     * @ordered
     */
    protected ExtensionType extension;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ContentsTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Wcs20Package.Literals.CONTENTS_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<CoverageSummaryType> getCoverageSummary() {
        if (coverageSummary == null) {
            coverageSummary = new EObjectContainmentEList<CoverageSummaryType>(CoverageSummaryType.class, this, Wcs20Package.CONTENTS_TYPE__COVERAGE_SUMMARY);
        }
        return coverageSummary;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ExtensionType getExtension() {
        return extension;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetExtension(ExtensionType newExtension, NotificationChain msgs) {
        ExtensionType oldExtension = extension;
        extension = newExtension;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wcs20Package.CONTENTS_TYPE__EXTENSION, oldExtension, newExtension);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setExtension(ExtensionType newExtension) {
        if (newExtension != extension) {
            NotificationChain msgs = null;
            if (extension != null)
                msgs = ((InternalEObject)extension).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wcs20Package.CONTENTS_TYPE__EXTENSION, null, msgs);
            if (newExtension != null)
                msgs = ((InternalEObject)newExtension).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wcs20Package.CONTENTS_TYPE__EXTENSION, null, msgs);
            msgs = basicSetExtension(newExtension, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs20Package.CONTENTS_TYPE__EXTENSION, newExtension, newExtension));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Wcs20Package.CONTENTS_TYPE__COVERAGE_SUMMARY:
                return ((InternalEList<?>)getCoverageSummary()).basicRemove(otherEnd, msgs);
            case Wcs20Package.CONTENTS_TYPE__EXTENSION:
                return basicSetExtension(null, msgs);
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
            case Wcs20Package.CONTENTS_TYPE__COVERAGE_SUMMARY:
                return getCoverageSummary();
            case Wcs20Package.CONTENTS_TYPE__EXTENSION:
                return getExtension();
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
            case Wcs20Package.CONTENTS_TYPE__COVERAGE_SUMMARY:
                getCoverageSummary().clear();
                getCoverageSummary().addAll((Collection<? extends CoverageSummaryType>)newValue);
                return;
            case Wcs20Package.CONTENTS_TYPE__EXTENSION:
                setExtension((ExtensionType)newValue);
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
            case Wcs20Package.CONTENTS_TYPE__COVERAGE_SUMMARY:
                getCoverageSummary().clear();
                return;
            case Wcs20Package.CONTENTS_TYPE__EXTENSION:
                setExtension((ExtensionType)null);
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
            case Wcs20Package.CONTENTS_TYPE__COVERAGE_SUMMARY:
                return coverageSummary != null && !coverageSummary.isEmpty();
            case Wcs20Package.CONTENTS_TYPE__EXTENSION:
                return extension != null;
        }
        return super.eIsSet(featureID);
    }

} //ContentsTypeImpl
