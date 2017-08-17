/**
 */
package net.opengis.wmts.v_1.impl;

import java.util.Collection;

import net.opengis.ows11.BoundingBoxType;
import net.opengis.ows11.CodeType;

import net.opengis.ows11.impl.DescriptionTypeImpl;

import net.opengis.wmts.v_1.TileMatrixSetType;
import net.opengis.wmts.v_1.TileMatrixType;
import net.opengis.wmts.v_1.wmtsv_1Package;

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
 * An implementation of the model object '<em><b>Tile Matrix Set Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wmts.v_1.impl.TileMatrixSetTypeImpl#getIdentifier <em>Identifier</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.TileMatrixSetTypeImpl#getBoundingBoxGroup <em>Bounding Box Group</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.TileMatrixSetTypeImpl#getBoundingBox <em>Bounding Box</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.TileMatrixSetTypeImpl#getSupportedCRS <em>Supported CRS</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.TileMatrixSetTypeImpl#getWellKnownScaleSet <em>Well Known Scale Set</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.TileMatrixSetTypeImpl#getTileMatrix <em>Tile Matrix</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TileMatrixSetTypeImpl extends DescriptionTypeImpl implements TileMatrixSetType {
    /**
     * The cached value of the '{@link #getIdentifier() <em>Identifier</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getIdentifier()
     * @generated
     * @ordered
     */
    protected CodeType identifier;

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
     * The default value of the '{@link #getSupportedCRS() <em>Supported CRS</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSupportedCRS()
     * @generated
     * @ordered
     */
    protected static final String SUPPORTED_CRS_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getSupportedCRS() <em>Supported CRS</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSupportedCRS()
     * @generated
     * @ordered
     */
    protected String supportedCRS = SUPPORTED_CRS_EDEFAULT;

    /**
     * The default value of the '{@link #getWellKnownScaleSet() <em>Well Known Scale Set</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getWellKnownScaleSet()
     * @generated
     * @ordered
     */
    protected static final String WELL_KNOWN_SCALE_SET_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getWellKnownScaleSet() <em>Well Known Scale Set</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getWellKnownScaleSet()
     * @generated
     * @ordered
     */
    protected String wellKnownScaleSet = WELL_KNOWN_SCALE_SET_EDEFAULT;

    /**
     * The cached value of the '{@link #getTileMatrix() <em>Tile Matrix</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTileMatrix()
     * @generated
     * @ordered
     */
    protected EList<TileMatrixType> tileMatrix;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected TileMatrixSetTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return wmtsv_1Package.Literals.TILE_MATRIX_SET_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CodeType getIdentifier() {
        return identifier;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetIdentifier(CodeType newIdentifier, NotificationChain msgs) {
        CodeType oldIdentifier = identifier;
        identifier = newIdentifier;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, wmtsv_1Package.TILE_MATRIX_SET_TYPE__IDENTIFIER, oldIdentifier, newIdentifier);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setIdentifier(CodeType newIdentifier) {
        if (newIdentifier != identifier) {
            NotificationChain msgs = null;
            if (identifier != null)
                msgs = ((InternalEObject)identifier).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - wmtsv_1Package.TILE_MATRIX_SET_TYPE__IDENTIFIER, null, msgs);
            if (newIdentifier != null)
                msgs = ((InternalEObject)newIdentifier).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - wmtsv_1Package.TILE_MATRIX_SET_TYPE__IDENTIFIER, null, msgs);
            msgs = basicSetIdentifier(newIdentifier, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, wmtsv_1Package.TILE_MATRIX_SET_TYPE__IDENTIFIER, newIdentifier, newIdentifier));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getBoundingBoxGroup() {
        if (boundingBoxGroup == null) {
            boundingBoxGroup = new BasicFeatureMap(this, wmtsv_1Package.TILE_MATRIX_SET_TYPE__BOUNDING_BOX_GROUP);
        }
        return boundingBoxGroup;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BoundingBoxType getBoundingBox() {
        return (BoundingBoxType)getBoundingBoxGroup().get(wmtsv_1Package.Literals.TILE_MATRIX_SET_TYPE__BOUNDING_BOX, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetBoundingBox(BoundingBoxType newBoundingBox, NotificationChain msgs) {
        return ((FeatureMap.Internal)getBoundingBoxGroup()).basicAdd(wmtsv_1Package.Literals.TILE_MATRIX_SET_TYPE__BOUNDING_BOX, newBoundingBox, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setBoundingBox(BoundingBoxType newBoundingBox) {
        ((FeatureMap.Internal)getBoundingBoxGroup()).set(wmtsv_1Package.Literals.TILE_MATRIX_SET_TYPE__BOUNDING_BOX, newBoundingBox);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getSupportedCRS() {
        return supportedCRS;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSupportedCRS(String newSupportedCRS) {
        String oldSupportedCRS = supportedCRS;
        supportedCRS = newSupportedCRS;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, wmtsv_1Package.TILE_MATRIX_SET_TYPE__SUPPORTED_CRS, oldSupportedCRS, supportedCRS));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getWellKnownScaleSet() {
        return wellKnownScaleSet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setWellKnownScaleSet(String newWellKnownScaleSet) {
        String oldWellKnownScaleSet = wellKnownScaleSet;
        wellKnownScaleSet = newWellKnownScaleSet;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, wmtsv_1Package.TILE_MATRIX_SET_TYPE__WELL_KNOWN_SCALE_SET, oldWellKnownScaleSet, wellKnownScaleSet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<TileMatrixType> getTileMatrix() {
        if (tileMatrix == null) {
            tileMatrix = new EObjectContainmentEList<TileMatrixType>(TileMatrixType.class, this, wmtsv_1Package.TILE_MATRIX_SET_TYPE__TILE_MATRIX);
        }
        return tileMatrix;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case wmtsv_1Package.TILE_MATRIX_SET_TYPE__IDENTIFIER:
                return basicSetIdentifier(null, msgs);
            case wmtsv_1Package.TILE_MATRIX_SET_TYPE__BOUNDING_BOX_GROUP:
                return ((InternalEList<?>)getBoundingBoxGroup()).basicRemove(otherEnd, msgs);
            case wmtsv_1Package.TILE_MATRIX_SET_TYPE__BOUNDING_BOX:
                return basicSetBoundingBox(null, msgs);
            case wmtsv_1Package.TILE_MATRIX_SET_TYPE__TILE_MATRIX:
                return ((InternalEList<?>)getTileMatrix()).basicRemove(otherEnd, msgs);
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
            case wmtsv_1Package.TILE_MATRIX_SET_TYPE__IDENTIFIER:
                return getIdentifier();
            case wmtsv_1Package.TILE_MATRIX_SET_TYPE__BOUNDING_BOX_GROUP:
                if (coreType) return getBoundingBoxGroup();
                return ((FeatureMap.Internal)getBoundingBoxGroup()).getWrapper();
            case wmtsv_1Package.TILE_MATRIX_SET_TYPE__BOUNDING_BOX:
                return getBoundingBox();
            case wmtsv_1Package.TILE_MATRIX_SET_TYPE__SUPPORTED_CRS:
                return getSupportedCRS();
            case wmtsv_1Package.TILE_MATRIX_SET_TYPE__WELL_KNOWN_SCALE_SET:
                return getWellKnownScaleSet();
            case wmtsv_1Package.TILE_MATRIX_SET_TYPE__TILE_MATRIX:
                return getTileMatrix();
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
            case wmtsv_1Package.TILE_MATRIX_SET_TYPE__IDENTIFIER:
                setIdentifier((CodeType)newValue);
                return;
            case wmtsv_1Package.TILE_MATRIX_SET_TYPE__BOUNDING_BOX_GROUP:
                ((FeatureMap.Internal)getBoundingBoxGroup()).set(newValue);
                return;
            case wmtsv_1Package.TILE_MATRIX_SET_TYPE__BOUNDING_BOX:
                setBoundingBox((BoundingBoxType)newValue);
                return;
            case wmtsv_1Package.TILE_MATRIX_SET_TYPE__SUPPORTED_CRS:
                setSupportedCRS((String)newValue);
                return;
            case wmtsv_1Package.TILE_MATRIX_SET_TYPE__WELL_KNOWN_SCALE_SET:
                setWellKnownScaleSet((String)newValue);
                return;
            case wmtsv_1Package.TILE_MATRIX_SET_TYPE__TILE_MATRIX:
                getTileMatrix().clear();
                getTileMatrix().addAll((Collection<? extends TileMatrixType>)newValue);
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
            case wmtsv_1Package.TILE_MATRIX_SET_TYPE__IDENTIFIER:
                setIdentifier((CodeType)null);
                return;
            case wmtsv_1Package.TILE_MATRIX_SET_TYPE__BOUNDING_BOX_GROUP:
                getBoundingBoxGroup().clear();
                return;
            case wmtsv_1Package.TILE_MATRIX_SET_TYPE__BOUNDING_BOX:
                setBoundingBox((BoundingBoxType)null);
                return;
            case wmtsv_1Package.TILE_MATRIX_SET_TYPE__SUPPORTED_CRS:
                setSupportedCRS(SUPPORTED_CRS_EDEFAULT);
                return;
            case wmtsv_1Package.TILE_MATRIX_SET_TYPE__WELL_KNOWN_SCALE_SET:
                setWellKnownScaleSet(WELL_KNOWN_SCALE_SET_EDEFAULT);
                return;
            case wmtsv_1Package.TILE_MATRIX_SET_TYPE__TILE_MATRIX:
                getTileMatrix().clear();
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
            case wmtsv_1Package.TILE_MATRIX_SET_TYPE__IDENTIFIER:
                return identifier != null;
            case wmtsv_1Package.TILE_MATRIX_SET_TYPE__BOUNDING_BOX_GROUP:
                return boundingBoxGroup != null && !boundingBoxGroup.isEmpty();
            case wmtsv_1Package.TILE_MATRIX_SET_TYPE__BOUNDING_BOX:
                return getBoundingBox() != null;
            case wmtsv_1Package.TILE_MATRIX_SET_TYPE__SUPPORTED_CRS:
                return SUPPORTED_CRS_EDEFAULT == null ? supportedCRS != null : !SUPPORTED_CRS_EDEFAULT.equals(supportedCRS);
            case wmtsv_1Package.TILE_MATRIX_SET_TYPE__WELL_KNOWN_SCALE_SET:
                return WELL_KNOWN_SCALE_SET_EDEFAULT == null ? wellKnownScaleSet != null : !WELL_KNOWN_SCALE_SET_EDEFAULT.equals(wellKnownScaleSet);
            case wmtsv_1Package.TILE_MATRIX_SET_TYPE__TILE_MATRIX:
                return tileMatrix != null && !tileMatrix.isEmpty();
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
        result.append(" (boundingBoxGroup: ");
        result.append(boundingBoxGroup);
        result.append(", supportedCRS: ");
        result.append(supportedCRS);
        result.append(", wellKnownScaleSet: ");
        result.append(wellKnownScaleSet);
        result.append(')');
        return result.toString();
    }

} //TileMatrixSetTypeImpl
