/**
 */
package net.opengis.gml311.impl;

import java.util.Collection;

import net.opengis.gml311.BaseStyleDescriptorType;
import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.ScaleType;
import net.opengis.gml311.StyleVariationType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.w3._2001.smil20.language.AnimateColorType;
import org.w3._2001.smil20.language.AnimateMotionType;
import org.w3._2001.smil20.language.AnimateType;
import org.w3._2001.smil20.language.SetType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Base Style Descriptor Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.BaseStyleDescriptorTypeImpl#getSpatialResolution <em>Spatial Resolution</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.BaseStyleDescriptorTypeImpl#getStyleVariation <em>Style Variation</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.BaseStyleDescriptorTypeImpl#getAnimate <em>Animate</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.BaseStyleDescriptorTypeImpl#getAnimateMotion <em>Animate Motion</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.BaseStyleDescriptorTypeImpl#getAnimateColor <em>Animate Color</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.BaseStyleDescriptorTypeImpl#getSet <em>Set</em>}</li>
 * </ul>
 *
 * @generated
 */
public class BaseStyleDescriptorTypeImpl extends AbstractGMLTypeImpl implements BaseStyleDescriptorType {
    /**
     * The cached value of the '{@link #getSpatialResolution() <em>Spatial Resolution</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSpatialResolution()
     * @generated
     * @ordered
     */
    protected ScaleType spatialResolution;

    /**
     * The cached value of the '{@link #getStyleVariation() <em>Style Variation</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getStyleVariation()
     * @generated
     * @ordered
     */
    protected EList<StyleVariationType> styleVariation;

    /**
     * The cached value of the '{@link #getAnimate() <em>Animate</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAnimate()
     * @generated
     * @ordered
     */
    protected EList<AnimateType> animate;

    /**
     * The cached value of the '{@link #getAnimateMotion() <em>Animate Motion</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAnimateMotion()
     * @generated
     * @ordered
     */
    protected EList<AnimateMotionType> animateMotion;

    /**
     * The cached value of the '{@link #getAnimateColor() <em>Animate Color</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAnimateColor()
     * @generated
     * @ordered
     */
    protected EList<AnimateColorType> animateColor;

    /**
     * The cached value of the '{@link #getSet() <em>Set</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSet()
     * @generated
     * @ordered
     */
    protected EList<SetType> set;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected BaseStyleDescriptorTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getBaseStyleDescriptorType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ScaleType getSpatialResolution() {
        return spatialResolution;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetSpatialResolution(ScaleType newSpatialResolution, NotificationChain msgs) {
        ScaleType oldSpatialResolution = spatialResolution;
        spatialResolution = newSpatialResolution;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.BASE_STYLE_DESCRIPTOR_TYPE__SPATIAL_RESOLUTION, oldSpatialResolution, newSpatialResolution);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSpatialResolution(ScaleType newSpatialResolution) {
        if (newSpatialResolution != spatialResolution) {
            NotificationChain msgs = null;
            if (spatialResolution != null)
                msgs = ((InternalEObject)spatialResolution).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.BASE_STYLE_DESCRIPTOR_TYPE__SPATIAL_RESOLUTION, null, msgs);
            if (newSpatialResolution != null)
                msgs = ((InternalEObject)newSpatialResolution).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.BASE_STYLE_DESCRIPTOR_TYPE__SPATIAL_RESOLUTION, null, msgs);
            msgs = basicSetSpatialResolution(newSpatialResolution, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.BASE_STYLE_DESCRIPTOR_TYPE__SPATIAL_RESOLUTION, newSpatialResolution, newSpatialResolution));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<StyleVariationType> getStyleVariation() {
        if (styleVariation == null) {
            styleVariation = new EObjectContainmentEList<StyleVariationType>(StyleVariationType.class, this, Gml311Package.BASE_STYLE_DESCRIPTOR_TYPE__STYLE_VARIATION);
        }
        return styleVariation;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<AnimateType> getAnimate() {
        if (animate == null) {
            animate = new EObjectContainmentEList<AnimateType>(AnimateType.class, this, Gml311Package.BASE_STYLE_DESCRIPTOR_TYPE__ANIMATE);
        }
        return animate;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<AnimateMotionType> getAnimateMotion() {
        if (animateMotion == null) {
            animateMotion = new EObjectContainmentEList<AnimateMotionType>(AnimateMotionType.class, this, Gml311Package.BASE_STYLE_DESCRIPTOR_TYPE__ANIMATE_MOTION);
        }
        return animateMotion;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<AnimateColorType> getAnimateColor() {
        if (animateColor == null) {
            animateColor = new EObjectContainmentEList<AnimateColorType>(AnimateColorType.class, this, Gml311Package.BASE_STYLE_DESCRIPTOR_TYPE__ANIMATE_COLOR);
        }
        return animateColor;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<SetType> getSet() {
        if (set == null) {
            set = new EObjectContainmentEList<SetType>(SetType.class, this, Gml311Package.BASE_STYLE_DESCRIPTOR_TYPE__SET);
        }
        return set;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.BASE_STYLE_DESCRIPTOR_TYPE__SPATIAL_RESOLUTION:
                return basicSetSpatialResolution(null, msgs);
            case Gml311Package.BASE_STYLE_DESCRIPTOR_TYPE__STYLE_VARIATION:
                return ((InternalEList<?>)getStyleVariation()).basicRemove(otherEnd, msgs);
            case Gml311Package.BASE_STYLE_DESCRIPTOR_TYPE__ANIMATE:
                return ((InternalEList<?>)getAnimate()).basicRemove(otherEnd, msgs);
            case Gml311Package.BASE_STYLE_DESCRIPTOR_TYPE__ANIMATE_MOTION:
                return ((InternalEList<?>)getAnimateMotion()).basicRemove(otherEnd, msgs);
            case Gml311Package.BASE_STYLE_DESCRIPTOR_TYPE__ANIMATE_COLOR:
                return ((InternalEList<?>)getAnimateColor()).basicRemove(otherEnd, msgs);
            case Gml311Package.BASE_STYLE_DESCRIPTOR_TYPE__SET:
                return ((InternalEList<?>)getSet()).basicRemove(otherEnd, msgs);
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
            case Gml311Package.BASE_STYLE_DESCRIPTOR_TYPE__SPATIAL_RESOLUTION:
                return getSpatialResolution();
            case Gml311Package.BASE_STYLE_DESCRIPTOR_TYPE__STYLE_VARIATION:
                return getStyleVariation();
            case Gml311Package.BASE_STYLE_DESCRIPTOR_TYPE__ANIMATE:
                return getAnimate();
            case Gml311Package.BASE_STYLE_DESCRIPTOR_TYPE__ANIMATE_MOTION:
                return getAnimateMotion();
            case Gml311Package.BASE_STYLE_DESCRIPTOR_TYPE__ANIMATE_COLOR:
                return getAnimateColor();
            case Gml311Package.BASE_STYLE_DESCRIPTOR_TYPE__SET:
                return getSet();
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
            case Gml311Package.BASE_STYLE_DESCRIPTOR_TYPE__SPATIAL_RESOLUTION:
                setSpatialResolution((ScaleType)newValue);
                return;
            case Gml311Package.BASE_STYLE_DESCRIPTOR_TYPE__STYLE_VARIATION:
                getStyleVariation().clear();
                getStyleVariation().addAll((Collection<? extends StyleVariationType>)newValue);
                return;
            case Gml311Package.BASE_STYLE_DESCRIPTOR_TYPE__ANIMATE:
                getAnimate().clear();
                getAnimate().addAll((Collection<? extends AnimateType>)newValue);
                return;
            case Gml311Package.BASE_STYLE_DESCRIPTOR_TYPE__ANIMATE_MOTION:
                getAnimateMotion().clear();
                getAnimateMotion().addAll((Collection<? extends AnimateMotionType>)newValue);
                return;
            case Gml311Package.BASE_STYLE_DESCRIPTOR_TYPE__ANIMATE_COLOR:
                getAnimateColor().clear();
                getAnimateColor().addAll((Collection<? extends AnimateColorType>)newValue);
                return;
            case Gml311Package.BASE_STYLE_DESCRIPTOR_TYPE__SET:
                getSet().clear();
                getSet().addAll((Collection<? extends SetType>)newValue);
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
            case Gml311Package.BASE_STYLE_DESCRIPTOR_TYPE__SPATIAL_RESOLUTION:
                setSpatialResolution((ScaleType)null);
                return;
            case Gml311Package.BASE_STYLE_DESCRIPTOR_TYPE__STYLE_VARIATION:
                getStyleVariation().clear();
                return;
            case Gml311Package.BASE_STYLE_DESCRIPTOR_TYPE__ANIMATE:
                getAnimate().clear();
                return;
            case Gml311Package.BASE_STYLE_DESCRIPTOR_TYPE__ANIMATE_MOTION:
                getAnimateMotion().clear();
                return;
            case Gml311Package.BASE_STYLE_DESCRIPTOR_TYPE__ANIMATE_COLOR:
                getAnimateColor().clear();
                return;
            case Gml311Package.BASE_STYLE_DESCRIPTOR_TYPE__SET:
                getSet().clear();
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
            case Gml311Package.BASE_STYLE_DESCRIPTOR_TYPE__SPATIAL_RESOLUTION:
                return spatialResolution != null;
            case Gml311Package.BASE_STYLE_DESCRIPTOR_TYPE__STYLE_VARIATION:
                return styleVariation != null && !styleVariation.isEmpty();
            case Gml311Package.BASE_STYLE_DESCRIPTOR_TYPE__ANIMATE:
                return animate != null && !animate.isEmpty();
            case Gml311Package.BASE_STYLE_DESCRIPTOR_TYPE__ANIMATE_MOTION:
                return animateMotion != null && !animateMotion.isEmpty();
            case Gml311Package.BASE_STYLE_DESCRIPTOR_TYPE__ANIMATE_COLOR:
                return animateColor != null && !animateColor.isEmpty();
            case Gml311Package.BASE_STYLE_DESCRIPTOR_TYPE__SET:
                return set != null && !set.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //BaseStyleDescriptorTypeImpl
