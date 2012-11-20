/**
 */
package net.opengis.wcs20.impl;

import net.opengis.wcs20.DimensionSubsetType;
import net.opengis.wcs20.GetCoverageType;
import net.opengis.wcs20.Wcs20Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Get Coverage Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs20.impl.GetCoverageTypeImpl#getCoverageId <em>Coverage Id</em>}</li>
 *   <li>{@link net.opengis.wcs20.impl.GetCoverageTypeImpl#getDimensionSubsetGroup <em>Dimension Subset Group</em>}</li>
 *   <li>{@link net.opengis.wcs20.impl.GetCoverageTypeImpl#getDimensionSubset <em>Dimension Subset</em>}</li>
 *   <li>{@link net.opengis.wcs20.impl.GetCoverageTypeImpl#getFormat <em>Format</em>}</li>
 *   <li>{@link net.opengis.wcs20.impl.GetCoverageTypeImpl#getMediaType <em>Media Type</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class GetCoverageTypeImpl extends RequestBaseTypeImpl implements GetCoverageType {
    /**
     * The default value of the '{@link #getCoverageId() <em>Coverage Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCoverageId()
     * @generated
     * @ordered
     */
    protected static final String COVERAGE_ID_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getCoverageId() <em>Coverage Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCoverageId()
     * @generated
     * @ordered
     */
    protected String coverageId = COVERAGE_ID_EDEFAULT;

    /**
     * The cached value of the '{@link #getDimensionSubsetGroup() <em>Dimension Subset Group</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDimensionSubsetGroup()
     * @generated
     * @ordered
     */
    protected FeatureMap dimensionSubsetGroup;

    /**
     * The default value of the '{@link #getFormat() <em>Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFormat()
     * @generated
     * @ordered
     */
    protected static final String FORMAT_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getFormat() <em>Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFormat()
     * @generated
     * @ordered
     */
    protected String format = FORMAT_EDEFAULT;

    /**
     * The default value of the '{@link #getMediaType() <em>Media Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMediaType()
     * @generated
     * @ordered
     */
    protected static final String MEDIA_TYPE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getMediaType() <em>Media Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMediaType()
     * @generated
     * @ordered
     */
    protected String mediaType = MEDIA_TYPE_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected GetCoverageTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Wcs20Package.Literals.GET_COVERAGE_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getCoverageId() {
        return coverageId;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setCoverageId(String newCoverageId) {
        String oldCoverageId = coverageId;
        coverageId = newCoverageId;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs20Package.GET_COVERAGE_TYPE__COVERAGE_ID, oldCoverageId, coverageId));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getDimensionSubsetGroup() {
        if (dimensionSubsetGroup == null) {
            dimensionSubsetGroup = new BasicFeatureMap(this, Wcs20Package.GET_COVERAGE_TYPE__DIMENSION_SUBSET_GROUP);
        }
        return dimensionSubsetGroup;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<DimensionSubsetType> getDimensionSubset() {
        return getDimensionSubsetGroup().list(Wcs20Package.Literals.GET_COVERAGE_TYPE__DIMENSION_SUBSET);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getFormat() {
        return format;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setFormat(String newFormat) {
        String oldFormat = format;
        format = newFormat;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs20Package.GET_COVERAGE_TYPE__FORMAT, oldFormat, format));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getMediaType() {
        return mediaType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setMediaType(String newMediaType) {
        String oldMediaType = mediaType;
        mediaType = newMediaType;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs20Package.GET_COVERAGE_TYPE__MEDIA_TYPE, oldMediaType, mediaType));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Wcs20Package.GET_COVERAGE_TYPE__DIMENSION_SUBSET_GROUP:
                return ((InternalEList<?>)getDimensionSubsetGroup()).basicRemove(otherEnd, msgs);
            case Wcs20Package.GET_COVERAGE_TYPE__DIMENSION_SUBSET:
                return ((InternalEList<?>)getDimensionSubset()).basicRemove(otherEnd, msgs);
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
            case Wcs20Package.GET_COVERAGE_TYPE__COVERAGE_ID:
                return getCoverageId();
            case Wcs20Package.GET_COVERAGE_TYPE__DIMENSION_SUBSET_GROUP:
                if (coreType) return getDimensionSubsetGroup();
                return ((FeatureMap.Internal)getDimensionSubsetGroup()).getWrapper();
            case Wcs20Package.GET_COVERAGE_TYPE__DIMENSION_SUBSET:
                return getDimensionSubset();
            case Wcs20Package.GET_COVERAGE_TYPE__FORMAT:
                return getFormat();
            case Wcs20Package.GET_COVERAGE_TYPE__MEDIA_TYPE:
                return getMediaType();
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
            case Wcs20Package.GET_COVERAGE_TYPE__COVERAGE_ID:
                setCoverageId((String)newValue);
                return;
            case Wcs20Package.GET_COVERAGE_TYPE__DIMENSION_SUBSET_GROUP:
                ((FeatureMap.Internal)getDimensionSubsetGroup()).set(newValue);
                return;
            case Wcs20Package.GET_COVERAGE_TYPE__FORMAT:
                setFormat((String)newValue);
                return;
            case Wcs20Package.GET_COVERAGE_TYPE__MEDIA_TYPE:
                setMediaType((String)newValue);
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
            case Wcs20Package.GET_COVERAGE_TYPE__COVERAGE_ID:
                setCoverageId(COVERAGE_ID_EDEFAULT);
                return;
            case Wcs20Package.GET_COVERAGE_TYPE__DIMENSION_SUBSET_GROUP:
                getDimensionSubsetGroup().clear();
                return;
            case Wcs20Package.GET_COVERAGE_TYPE__FORMAT:
                setFormat(FORMAT_EDEFAULT);
                return;
            case Wcs20Package.GET_COVERAGE_TYPE__MEDIA_TYPE:
                setMediaType(MEDIA_TYPE_EDEFAULT);
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
            case Wcs20Package.GET_COVERAGE_TYPE__COVERAGE_ID:
                return COVERAGE_ID_EDEFAULT == null ? coverageId != null : !COVERAGE_ID_EDEFAULT.equals(coverageId);
            case Wcs20Package.GET_COVERAGE_TYPE__DIMENSION_SUBSET_GROUP:
                return dimensionSubsetGroup != null && !dimensionSubsetGroup.isEmpty();
            case Wcs20Package.GET_COVERAGE_TYPE__DIMENSION_SUBSET:
                return !getDimensionSubset().isEmpty();
            case Wcs20Package.GET_COVERAGE_TYPE__FORMAT:
                return FORMAT_EDEFAULT == null ? format != null : !FORMAT_EDEFAULT.equals(format);
            case Wcs20Package.GET_COVERAGE_TYPE__MEDIA_TYPE:
                return MEDIA_TYPE_EDEFAULT == null ? mediaType != null : !MEDIA_TYPE_EDEFAULT.equals(mediaType);
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
        result.append(" (coverageId: ");
        result.append(coverageId);
        result.append(", dimensionSubsetGroup: ");
        result.append(dimensionSubsetGroup);
        result.append(", format: ");
        result.append(format);
        result.append(", mediaType: ");
        result.append(mediaType);
        result.append(')');
        return result.toString();
    }

} //GetCoverageTypeImpl
