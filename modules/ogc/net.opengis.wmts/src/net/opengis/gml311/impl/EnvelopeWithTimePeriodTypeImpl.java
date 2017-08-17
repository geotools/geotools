/**
 */
package net.opengis.gml311.impl;

import java.util.Collection;

import net.opengis.gml311.EnvelopeWithTimePeriodType;
import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.TimePositionType;

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
 * An implementation of the model object '<em><b>Envelope With Time Period Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.EnvelopeWithTimePeriodTypeImpl#getTimePosition <em>Time Position</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.EnvelopeWithTimePeriodTypeImpl#getFrame <em>Frame</em>}</li>
 * </ul>
 *
 * @generated
 */
public class EnvelopeWithTimePeriodTypeImpl extends EnvelopeTypeImpl implements EnvelopeWithTimePeriodType {
    /**
     * The cached value of the '{@link #getTimePosition() <em>Time Position</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTimePosition()
     * @generated
     * @ordered
     */
    protected EList<TimePositionType> timePosition;

    /**
     * The default value of the '{@link #getFrame() <em>Frame</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFrame()
     * @generated
     * @ordered
     */
    protected static final String FRAME_EDEFAULT = "#ISO-8601";

    /**
     * The cached value of the '{@link #getFrame() <em>Frame</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFrame()
     * @generated
     * @ordered
     */
    protected String frame = FRAME_EDEFAULT;

    /**
     * This is true if the Frame attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean frameESet;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EnvelopeWithTimePeriodTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getEnvelopeWithTimePeriodType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<TimePositionType> getTimePosition() {
        if (timePosition == null) {
            timePosition = new EObjectContainmentEList<TimePositionType>(TimePositionType.class, this, Gml311Package.ENVELOPE_WITH_TIME_PERIOD_TYPE__TIME_POSITION);
        }
        return timePosition;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getFrame() {
        return frame;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setFrame(String newFrame) {
        String oldFrame = frame;
        frame = newFrame;
        boolean oldFrameESet = frameESet;
        frameESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.ENVELOPE_WITH_TIME_PERIOD_TYPE__FRAME, oldFrame, frame, !oldFrameESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetFrame() {
        String oldFrame = frame;
        boolean oldFrameESet = frameESet;
        frame = FRAME_EDEFAULT;
        frameESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Gml311Package.ENVELOPE_WITH_TIME_PERIOD_TYPE__FRAME, oldFrame, FRAME_EDEFAULT, oldFrameESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetFrame() {
        return frameESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.ENVELOPE_WITH_TIME_PERIOD_TYPE__TIME_POSITION:
                return ((InternalEList<?>)getTimePosition()).basicRemove(otherEnd, msgs);
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
            case Gml311Package.ENVELOPE_WITH_TIME_PERIOD_TYPE__TIME_POSITION:
                return getTimePosition();
            case Gml311Package.ENVELOPE_WITH_TIME_PERIOD_TYPE__FRAME:
                return getFrame();
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
            case Gml311Package.ENVELOPE_WITH_TIME_PERIOD_TYPE__TIME_POSITION:
                getTimePosition().clear();
                getTimePosition().addAll((Collection<? extends TimePositionType>)newValue);
                return;
            case Gml311Package.ENVELOPE_WITH_TIME_PERIOD_TYPE__FRAME:
                setFrame((String)newValue);
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
            case Gml311Package.ENVELOPE_WITH_TIME_PERIOD_TYPE__TIME_POSITION:
                getTimePosition().clear();
                return;
            case Gml311Package.ENVELOPE_WITH_TIME_PERIOD_TYPE__FRAME:
                unsetFrame();
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
            case Gml311Package.ENVELOPE_WITH_TIME_PERIOD_TYPE__TIME_POSITION:
                return timePosition != null && !timePosition.isEmpty();
            case Gml311Package.ENVELOPE_WITH_TIME_PERIOD_TYPE__FRAME:
                return isSetFrame();
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
        result.append(" (frame: ");
        if (frameESet) result.append(frame); else result.append("<unset>");
        result.append(')');
        return result.toString();
    }

} //EnvelopeWithTimePeriodTypeImpl
