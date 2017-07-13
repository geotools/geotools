/**
 */
package net.opengis.gml311.impl;

import java.util.Collection;

import net.opengis.gml311.AbstractRingPropertyType;
import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.PolygonPatchType;
import net.opengis.gml311.SurfaceInterpolationType;

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
 * An implementation of the model object '<em><b>Polygon Patch Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.PolygonPatchTypeImpl#getExteriorGroup <em>Exterior Group</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.PolygonPatchTypeImpl#getExterior <em>Exterior</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.PolygonPatchTypeImpl#getInteriorGroup <em>Interior Group</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.PolygonPatchTypeImpl#getInterior <em>Interior</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.PolygonPatchTypeImpl#getInterpolation <em>Interpolation</em>}</li>
 * </ul>
 *
 * @generated
 */
public class PolygonPatchTypeImpl extends AbstractSurfacePatchTypeImpl implements PolygonPatchType {
    /**
     * The cached value of the '{@link #getExteriorGroup() <em>Exterior Group</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getExteriorGroup()
     * @generated
     * @ordered
     */
    protected FeatureMap exteriorGroup;

    /**
     * The cached value of the '{@link #getInteriorGroup() <em>Interior Group</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getInteriorGroup()
     * @generated
     * @ordered
     */
    protected FeatureMap interiorGroup;

    /**
     * The default value of the '{@link #getInterpolation() <em>Interpolation</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getInterpolation()
     * @generated
     * @ordered
     */
    protected static final SurfaceInterpolationType INTERPOLATION_EDEFAULT = SurfaceInterpolationType.PLANAR;

    /**
     * The cached value of the '{@link #getInterpolation() <em>Interpolation</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getInterpolation()
     * @generated
     * @ordered
     */
    protected SurfaceInterpolationType interpolation = INTERPOLATION_EDEFAULT;

    /**
     * This is true if the Interpolation attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean interpolationESet;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected PolygonPatchTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getPolygonPatchType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getExteriorGroup() {
        if (exteriorGroup == null) {
            exteriorGroup = new BasicFeatureMap(this, Gml311Package.POLYGON_PATCH_TYPE__EXTERIOR_GROUP);
        }
        return exteriorGroup;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AbstractRingPropertyType getExterior() {
        return (AbstractRingPropertyType)getExteriorGroup().get(Gml311Package.eINSTANCE.getPolygonPatchType_Exterior(), true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetExterior(AbstractRingPropertyType newExterior, NotificationChain msgs) {
        return ((FeatureMap.Internal)getExteriorGroup()).basicAdd(Gml311Package.eINSTANCE.getPolygonPatchType_Exterior(), newExterior, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setExterior(AbstractRingPropertyType newExterior) {
        ((FeatureMap.Internal)getExteriorGroup()).set(Gml311Package.eINSTANCE.getPolygonPatchType_Exterior(), newExterior);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getInteriorGroup() {
        if (interiorGroup == null) {
            interiorGroup = new BasicFeatureMap(this, Gml311Package.POLYGON_PATCH_TYPE__INTERIOR_GROUP);
        }
        return interiorGroup;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<AbstractRingPropertyType> getInterior() {
        return getInteriorGroup().list(Gml311Package.eINSTANCE.getPolygonPatchType_Interior());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SurfaceInterpolationType getInterpolation() {
        return interpolation;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setInterpolation(SurfaceInterpolationType newInterpolation) {
        SurfaceInterpolationType oldInterpolation = interpolation;
        interpolation = newInterpolation == null ? INTERPOLATION_EDEFAULT : newInterpolation;
        boolean oldInterpolationESet = interpolationESet;
        interpolationESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.POLYGON_PATCH_TYPE__INTERPOLATION, oldInterpolation, interpolation, !oldInterpolationESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetInterpolation() {
        SurfaceInterpolationType oldInterpolation = interpolation;
        boolean oldInterpolationESet = interpolationESet;
        interpolation = INTERPOLATION_EDEFAULT;
        interpolationESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Gml311Package.POLYGON_PATCH_TYPE__INTERPOLATION, oldInterpolation, INTERPOLATION_EDEFAULT, oldInterpolationESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetInterpolation() {
        return interpolationESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.POLYGON_PATCH_TYPE__EXTERIOR_GROUP:
                return ((InternalEList<?>)getExteriorGroup()).basicRemove(otherEnd, msgs);
            case Gml311Package.POLYGON_PATCH_TYPE__EXTERIOR:
                return basicSetExterior(null, msgs);
            case Gml311Package.POLYGON_PATCH_TYPE__INTERIOR_GROUP:
                return ((InternalEList<?>)getInteriorGroup()).basicRemove(otherEnd, msgs);
            case Gml311Package.POLYGON_PATCH_TYPE__INTERIOR:
                return ((InternalEList<?>)getInterior()).basicRemove(otherEnd, msgs);
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
            case Gml311Package.POLYGON_PATCH_TYPE__EXTERIOR_GROUP:
                if (coreType) return getExteriorGroup();
                return ((FeatureMap.Internal)getExteriorGroup()).getWrapper();
            case Gml311Package.POLYGON_PATCH_TYPE__EXTERIOR:
                return getExterior();
            case Gml311Package.POLYGON_PATCH_TYPE__INTERIOR_GROUP:
                if (coreType) return getInteriorGroup();
                return ((FeatureMap.Internal)getInteriorGroup()).getWrapper();
            case Gml311Package.POLYGON_PATCH_TYPE__INTERIOR:
                return getInterior();
            case Gml311Package.POLYGON_PATCH_TYPE__INTERPOLATION:
                return getInterpolation();
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
            case Gml311Package.POLYGON_PATCH_TYPE__EXTERIOR_GROUP:
                ((FeatureMap.Internal)getExteriorGroup()).set(newValue);
                return;
            case Gml311Package.POLYGON_PATCH_TYPE__EXTERIOR:
                setExterior((AbstractRingPropertyType)newValue);
                return;
            case Gml311Package.POLYGON_PATCH_TYPE__INTERIOR_GROUP:
                ((FeatureMap.Internal)getInteriorGroup()).set(newValue);
                return;
            case Gml311Package.POLYGON_PATCH_TYPE__INTERIOR:
                getInterior().clear();
                getInterior().addAll((Collection<? extends AbstractRingPropertyType>)newValue);
                return;
            case Gml311Package.POLYGON_PATCH_TYPE__INTERPOLATION:
                setInterpolation((SurfaceInterpolationType)newValue);
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
            case Gml311Package.POLYGON_PATCH_TYPE__EXTERIOR_GROUP:
                getExteriorGroup().clear();
                return;
            case Gml311Package.POLYGON_PATCH_TYPE__EXTERIOR:
                setExterior((AbstractRingPropertyType)null);
                return;
            case Gml311Package.POLYGON_PATCH_TYPE__INTERIOR_GROUP:
                getInteriorGroup().clear();
                return;
            case Gml311Package.POLYGON_PATCH_TYPE__INTERIOR:
                getInterior().clear();
                return;
            case Gml311Package.POLYGON_PATCH_TYPE__INTERPOLATION:
                unsetInterpolation();
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
            case Gml311Package.POLYGON_PATCH_TYPE__EXTERIOR_GROUP:
                return exteriorGroup != null && !exteriorGroup.isEmpty();
            case Gml311Package.POLYGON_PATCH_TYPE__EXTERIOR:
                return getExterior() != null;
            case Gml311Package.POLYGON_PATCH_TYPE__INTERIOR_GROUP:
                return interiorGroup != null && !interiorGroup.isEmpty();
            case Gml311Package.POLYGON_PATCH_TYPE__INTERIOR:
                return !getInterior().isEmpty();
            case Gml311Package.POLYGON_PATCH_TYPE__INTERPOLATION:
                return isSetInterpolation();
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
        result.append(" (exteriorGroup: ");
        result.append(exteriorGroup);
        result.append(", interiorGroup: ");
        result.append(interiorGroup);
        result.append(", interpolation: ");
        if (interpolationESet) result.append(interpolation); else result.append("<unset>");
        result.append(')');
        return result.toString();
    }

} //PolygonPatchTypeImpl
