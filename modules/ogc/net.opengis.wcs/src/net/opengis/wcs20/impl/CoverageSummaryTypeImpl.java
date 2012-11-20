/**
 */
package net.opengis.wcs20.impl;

import java.util.Collection;

import javax.xml.namespace.QName;

import net.opengis.ows20.BoundingBoxType;
import net.opengis.ows20.MetadataType;
import net.opengis.ows20.WGS84BoundingBoxType;

import net.opengis.ows20.impl.DescriptionTypeImpl;

import net.opengis.wcs20.CoverageSubtypeParentType;
import net.opengis.wcs20.CoverageSummaryType;
import net.opengis.wcs20.Wcs20Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Coverage Summary Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs20.impl.CoverageSummaryTypeImpl#getWGS84BoundingBox <em>WGS84 Bounding Box</em>}</li>
 *   <li>{@link net.opengis.wcs20.impl.CoverageSummaryTypeImpl#getCoverageId <em>Coverage Id</em>}</li>
 *   <li>{@link net.opengis.wcs20.impl.CoverageSummaryTypeImpl#getCoverageSubtype <em>Coverage Subtype</em>}</li>
 *   <li>{@link net.opengis.wcs20.impl.CoverageSummaryTypeImpl#getCoverageSubtypeParent <em>Coverage Subtype Parent</em>}</li>
 *   <li>{@link net.opengis.wcs20.impl.CoverageSummaryTypeImpl#getBoundingBoxGroup <em>Bounding Box Group</em>}</li>
 *   <li>{@link net.opengis.wcs20.impl.CoverageSummaryTypeImpl#getBoundingBox <em>Bounding Box</em>}</li>
 *   <li>{@link net.opengis.wcs20.impl.CoverageSummaryTypeImpl#getMetadataGroup <em>Metadata Group</em>}</li>
 *   <li>{@link net.opengis.wcs20.impl.CoverageSummaryTypeImpl#getMetadata <em>Metadata</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CoverageSummaryTypeImpl extends DescriptionTypeImpl implements CoverageSummaryType {
    /**
     * The cached value of the '{@link #getWGS84BoundingBox() <em>WGS84 Bounding Box</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getWGS84BoundingBox()
     * @generated
     * @ordered
     */
    protected EList<WGS84BoundingBoxType> wGS84BoundingBox;

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
     * The default value of the '{@link #getCoverageSubtype() <em>Coverage Subtype</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCoverageSubtype()
     * @generated
     * @ordered
     */
    protected static final QName COVERAGE_SUBTYPE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getCoverageSubtype() <em>Coverage Subtype</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCoverageSubtype()
     * @generated
     * @ordered
     */
    protected QName coverageSubtype = COVERAGE_SUBTYPE_EDEFAULT;

    /**
     * The cached value of the '{@link #getCoverageSubtypeParent() <em>Coverage Subtype Parent</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCoverageSubtypeParent()
     * @generated
     * @ordered
     */
    protected CoverageSubtypeParentType coverageSubtypeParent;

    /**
     * The cached value of the '{@link #getBoundingBoxGroup() <em>Bounding Box Group</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getBoundingBoxGroup()
     * @generated
     * @ordered
     */
    protected FeatureMap boundingBoxGroup;

    /**
     * The cached value of the '{@link #getMetadataGroup() <em>Metadata Group</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMetadataGroup()
     * @generated
     * @ordered
     */
    protected FeatureMap metadataGroup;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected CoverageSummaryTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Wcs20Package.Literals.COVERAGE_SUMMARY_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<WGS84BoundingBoxType> getWGS84BoundingBox() {
        if (wGS84BoundingBox == null) {
            wGS84BoundingBox = new EObjectContainmentEList<WGS84BoundingBoxType>(WGS84BoundingBoxType.class, this, Wcs20Package.COVERAGE_SUMMARY_TYPE__WGS84_BOUNDING_BOX);
        }
        return wGS84BoundingBox;
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
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs20Package.COVERAGE_SUMMARY_TYPE__COVERAGE_ID, oldCoverageId, coverageId));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public QName getCoverageSubtype() {
        return coverageSubtype;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setCoverageSubtype(QName newCoverageSubtype) {
        QName oldCoverageSubtype = coverageSubtype;
        coverageSubtype = newCoverageSubtype;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs20Package.COVERAGE_SUMMARY_TYPE__COVERAGE_SUBTYPE, oldCoverageSubtype, coverageSubtype));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CoverageSubtypeParentType getCoverageSubtypeParent() {
        return coverageSubtypeParent;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetCoverageSubtypeParent(CoverageSubtypeParentType newCoverageSubtypeParent, NotificationChain msgs) {
        CoverageSubtypeParentType oldCoverageSubtypeParent = coverageSubtypeParent;
        coverageSubtypeParent = newCoverageSubtypeParent;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wcs20Package.COVERAGE_SUMMARY_TYPE__COVERAGE_SUBTYPE_PARENT, oldCoverageSubtypeParent, newCoverageSubtypeParent);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setCoverageSubtypeParent(CoverageSubtypeParentType newCoverageSubtypeParent) {
        if (newCoverageSubtypeParent != coverageSubtypeParent) {
            NotificationChain msgs = null;
            if (coverageSubtypeParent != null)
                msgs = ((InternalEObject)coverageSubtypeParent).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wcs20Package.COVERAGE_SUMMARY_TYPE__COVERAGE_SUBTYPE_PARENT, null, msgs);
            if (newCoverageSubtypeParent != null)
                msgs = ((InternalEObject)newCoverageSubtypeParent).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wcs20Package.COVERAGE_SUMMARY_TYPE__COVERAGE_SUBTYPE_PARENT, null, msgs);
            msgs = basicSetCoverageSubtypeParent(newCoverageSubtypeParent, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs20Package.COVERAGE_SUMMARY_TYPE__COVERAGE_SUBTYPE_PARENT, newCoverageSubtypeParent, newCoverageSubtypeParent));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getBoundingBoxGroup() {
        if (boundingBoxGroup == null) {
            boundingBoxGroup = new BasicFeatureMap(this, Wcs20Package.COVERAGE_SUMMARY_TYPE__BOUNDING_BOX_GROUP);
        }
        return boundingBoxGroup;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<BoundingBoxType> getBoundingBox() {
        return getBoundingBoxGroup().list(Wcs20Package.Literals.COVERAGE_SUMMARY_TYPE__BOUNDING_BOX);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getMetadataGroup() {
        if (metadataGroup == null) {
            metadataGroup = new BasicFeatureMap(this, Wcs20Package.COVERAGE_SUMMARY_TYPE__METADATA_GROUP);
        }
        return metadataGroup;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<MetadataType> getMetadata() {
        return getMetadataGroup().list(Wcs20Package.Literals.COVERAGE_SUMMARY_TYPE__METADATA);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Wcs20Package.COVERAGE_SUMMARY_TYPE__WGS84_BOUNDING_BOX:
                return ((InternalEList<?>)getWGS84BoundingBox()).basicRemove(otherEnd, msgs);
            case Wcs20Package.COVERAGE_SUMMARY_TYPE__COVERAGE_SUBTYPE_PARENT:
                return basicSetCoverageSubtypeParent(null, msgs);
            case Wcs20Package.COVERAGE_SUMMARY_TYPE__BOUNDING_BOX_GROUP:
                return ((InternalEList<?>)getBoundingBoxGroup()).basicRemove(otherEnd, msgs);
            case Wcs20Package.COVERAGE_SUMMARY_TYPE__BOUNDING_BOX:
                return ((InternalEList<?>)getBoundingBox()).basicRemove(otherEnd, msgs);
            case Wcs20Package.COVERAGE_SUMMARY_TYPE__METADATA_GROUP:
                return ((InternalEList<?>)getMetadataGroup()).basicRemove(otherEnd, msgs);
            case Wcs20Package.COVERAGE_SUMMARY_TYPE__METADATA:
                return ((InternalEList<?>)getMetadata()).basicRemove(otherEnd, msgs);
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
            case Wcs20Package.COVERAGE_SUMMARY_TYPE__WGS84_BOUNDING_BOX:
                return getWGS84BoundingBox();
            case Wcs20Package.COVERAGE_SUMMARY_TYPE__COVERAGE_ID:
                return getCoverageId();
            case Wcs20Package.COVERAGE_SUMMARY_TYPE__COVERAGE_SUBTYPE:
                return getCoverageSubtype();
            case Wcs20Package.COVERAGE_SUMMARY_TYPE__COVERAGE_SUBTYPE_PARENT:
                return getCoverageSubtypeParent();
            case Wcs20Package.COVERAGE_SUMMARY_TYPE__BOUNDING_BOX_GROUP:
                if (coreType) return getBoundingBoxGroup();
                return ((FeatureMap.Internal)getBoundingBoxGroup()).getWrapper();
            case Wcs20Package.COVERAGE_SUMMARY_TYPE__BOUNDING_BOX:
                return getBoundingBox();
            case Wcs20Package.COVERAGE_SUMMARY_TYPE__METADATA_GROUP:
                if (coreType) return getMetadataGroup();
                return ((FeatureMap.Internal)getMetadataGroup()).getWrapper();
            case Wcs20Package.COVERAGE_SUMMARY_TYPE__METADATA:
                return getMetadata();
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
            case Wcs20Package.COVERAGE_SUMMARY_TYPE__WGS84_BOUNDING_BOX:
                getWGS84BoundingBox().clear();
                getWGS84BoundingBox().addAll((Collection<? extends WGS84BoundingBoxType>)newValue);
                return;
            case Wcs20Package.COVERAGE_SUMMARY_TYPE__COVERAGE_ID:
                setCoverageId((String)newValue);
                return;
            case Wcs20Package.COVERAGE_SUMMARY_TYPE__COVERAGE_SUBTYPE:
                setCoverageSubtype((QName)newValue);
                return;
            case Wcs20Package.COVERAGE_SUMMARY_TYPE__COVERAGE_SUBTYPE_PARENT:
                setCoverageSubtypeParent((CoverageSubtypeParentType)newValue);
                return;
            case Wcs20Package.COVERAGE_SUMMARY_TYPE__BOUNDING_BOX_GROUP:
                ((FeatureMap.Internal)getBoundingBoxGroup()).set(newValue);
                return;
            case Wcs20Package.COVERAGE_SUMMARY_TYPE__BOUNDING_BOX:
                getBoundingBox().clear();
                getBoundingBox().addAll((Collection<? extends BoundingBoxType>)newValue);
                return;
            case Wcs20Package.COVERAGE_SUMMARY_TYPE__METADATA_GROUP:
                ((FeatureMap.Internal)getMetadataGroup()).set(newValue);
                return;
            case Wcs20Package.COVERAGE_SUMMARY_TYPE__METADATA:
                getMetadata().clear();
                getMetadata().addAll((Collection<? extends MetadataType>)newValue);
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
            case Wcs20Package.COVERAGE_SUMMARY_TYPE__WGS84_BOUNDING_BOX:
                getWGS84BoundingBox().clear();
                return;
            case Wcs20Package.COVERAGE_SUMMARY_TYPE__COVERAGE_ID:
                setCoverageId(COVERAGE_ID_EDEFAULT);
                return;
            case Wcs20Package.COVERAGE_SUMMARY_TYPE__COVERAGE_SUBTYPE:
                setCoverageSubtype(COVERAGE_SUBTYPE_EDEFAULT);
                return;
            case Wcs20Package.COVERAGE_SUMMARY_TYPE__COVERAGE_SUBTYPE_PARENT:
                setCoverageSubtypeParent((CoverageSubtypeParentType)null);
                return;
            case Wcs20Package.COVERAGE_SUMMARY_TYPE__BOUNDING_BOX_GROUP:
                getBoundingBoxGroup().clear();
                return;
            case Wcs20Package.COVERAGE_SUMMARY_TYPE__BOUNDING_BOX:
                getBoundingBox().clear();
                return;
            case Wcs20Package.COVERAGE_SUMMARY_TYPE__METADATA_GROUP:
                getMetadataGroup().clear();
                return;
            case Wcs20Package.COVERAGE_SUMMARY_TYPE__METADATA:
                getMetadata().clear();
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
            case Wcs20Package.COVERAGE_SUMMARY_TYPE__WGS84_BOUNDING_BOX:
                return wGS84BoundingBox != null && !wGS84BoundingBox.isEmpty();
            case Wcs20Package.COVERAGE_SUMMARY_TYPE__COVERAGE_ID:
                return COVERAGE_ID_EDEFAULT == null ? coverageId != null : !COVERAGE_ID_EDEFAULT.equals(coverageId);
            case Wcs20Package.COVERAGE_SUMMARY_TYPE__COVERAGE_SUBTYPE:
                return COVERAGE_SUBTYPE_EDEFAULT == null ? coverageSubtype != null : !COVERAGE_SUBTYPE_EDEFAULT.equals(coverageSubtype);
            case Wcs20Package.COVERAGE_SUMMARY_TYPE__COVERAGE_SUBTYPE_PARENT:
                return coverageSubtypeParent != null;
            case Wcs20Package.COVERAGE_SUMMARY_TYPE__BOUNDING_BOX_GROUP:
                return boundingBoxGroup != null && !boundingBoxGroup.isEmpty();
            case Wcs20Package.COVERAGE_SUMMARY_TYPE__BOUNDING_BOX:
                return !getBoundingBox().isEmpty();
            case Wcs20Package.COVERAGE_SUMMARY_TYPE__METADATA_GROUP:
                return metadataGroup != null && !metadataGroup.isEmpty();
            case Wcs20Package.COVERAGE_SUMMARY_TYPE__METADATA:
                return !getMetadata().isEmpty();
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
        result.append(", coverageSubtype: ");
        result.append(coverageSubtype);
        result.append(", boundingBoxGroup: ");
        result.append(boundingBoxGroup);
        result.append(", metadataGroup: ");
        result.append(metadataGroup);
        result.append(')');
        return result.toString();
    }

} //CoverageSummaryTypeImpl
