/**
 */
package net.opengis.wmts.v_1.impl;

import java.math.BigInteger;

import net.opengis.ows11.impl.OnlineResourceTypeImpl;

import net.opengis.wmts.v_1.LegendURLType;
import net.opengis.wmts.v_1.wmtsv_1Package;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Legend URL Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wmts.v_1.impl.LegendURLTypeImpl#getFormat <em>Format</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.LegendURLTypeImpl#getHeight <em>Height</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.LegendURLTypeImpl#getMaxScaleDenominator <em>Max Scale Denominator</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.LegendURLTypeImpl#getMinScaleDenominator <em>Min Scale Denominator</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.LegendURLTypeImpl#getWidth <em>Width</em>}</li>
 * </ul>
 *
 * @generated
 */
public class LegendURLTypeImpl extends OnlineResourceTypeImpl implements LegendURLType {
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
     * The default value of the '{@link #getHeight() <em>Height</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getHeight()
     * @generated
     * @ordered
     */
    protected static final BigInteger HEIGHT_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getHeight() <em>Height</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getHeight()
     * @generated
     * @ordered
     */
    protected BigInteger height = HEIGHT_EDEFAULT;

    /**
     * The default value of the '{@link #getMaxScaleDenominator() <em>Max Scale Denominator</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMaxScaleDenominator()
     * @generated
     * @ordered
     */
    protected static final double MAX_SCALE_DENOMINATOR_EDEFAULT = 0.0;

    /**
     * The cached value of the '{@link #getMaxScaleDenominator() <em>Max Scale Denominator</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMaxScaleDenominator()
     * @generated
     * @ordered
     */
    protected double maxScaleDenominator = MAX_SCALE_DENOMINATOR_EDEFAULT;

    /**
     * This is true if the Max Scale Denominator attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean maxScaleDenominatorESet;

    /**
     * The default value of the '{@link #getMinScaleDenominator() <em>Min Scale Denominator</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMinScaleDenominator()
     * @generated
     * @ordered
     */
    protected static final double MIN_SCALE_DENOMINATOR_EDEFAULT = 0.0;

    /**
     * The cached value of the '{@link #getMinScaleDenominator() <em>Min Scale Denominator</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMinScaleDenominator()
     * @generated
     * @ordered
     */
    protected double minScaleDenominator = MIN_SCALE_DENOMINATOR_EDEFAULT;

    /**
     * This is true if the Min Scale Denominator attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean minScaleDenominatorESet;

    /**
     * The default value of the '{@link #getWidth() <em>Width</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getWidth()
     * @generated
     * @ordered
     */
    protected static final BigInteger WIDTH_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getWidth() <em>Width</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getWidth()
     * @generated
     * @ordered
     */
    protected BigInteger width = WIDTH_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected LegendURLTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return wmtsv_1Package.Literals.LEGEND_URL_TYPE;
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
            eNotify(new ENotificationImpl(this, Notification.SET, wmtsv_1Package.LEGEND_URL_TYPE__FORMAT, oldFormat, format));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BigInteger getHeight() {
        return height;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setHeight(BigInteger newHeight) {
        BigInteger oldHeight = height;
        height = newHeight;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, wmtsv_1Package.LEGEND_URL_TYPE__HEIGHT, oldHeight, height));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public double getMaxScaleDenominator() {
        return maxScaleDenominator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setMaxScaleDenominator(double newMaxScaleDenominator) {
        double oldMaxScaleDenominator = maxScaleDenominator;
        maxScaleDenominator = newMaxScaleDenominator;
        boolean oldMaxScaleDenominatorESet = maxScaleDenominatorESet;
        maxScaleDenominatorESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, wmtsv_1Package.LEGEND_URL_TYPE__MAX_SCALE_DENOMINATOR, oldMaxScaleDenominator, maxScaleDenominator, !oldMaxScaleDenominatorESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetMaxScaleDenominator() {
        double oldMaxScaleDenominator = maxScaleDenominator;
        boolean oldMaxScaleDenominatorESet = maxScaleDenominatorESet;
        maxScaleDenominator = MAX_SCALE_DENOMINATOR_EDEFAULT;
        maxScaleDenominatorESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, wmtsv_1Package.LEGEND_URL_TYPE__MAX_SCALE_DENOMINATOR, oldMaxScaleDenominator, MAX_SCALE_DENOMINATOR_EDEFAULT, oldMaxScaleDenominatorESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetMaxScaleDenominator() {
        return maxScaleDenominatorESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public double getMinScaleDenominator() {
        return minScaleDenominator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setMinScaleDenominator(double newMinScaleDenominator) {
        double oldMinScaleDenominator = minScaleDenominator;
        minScaleDenominator = newMinScaleDenominator;
        boolean oldMinScaleDenominatorESet = minScaleDenominatorESet;
        minScaleDenominatorESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, wmtsv_1Package.LEGEND_URL_TYPE__MIN_SCALE_DENOMINATOR, oldMinScaleDenominator, minScaleDenominator, !oldMinScaleDenominatorESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetMinScaleDenominator() {
        double oldMinScaleDenominator = minScaleDenominator;
        boolean oldMinScaleDenominatorESet = minScaleDenominatorESet;
        minScaleDenominator = MIN_SCALE_DENOMINATOR_EDEFAULT;
        minScaleDenominatorESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, wmtsv_1Package.LEGEND_URL_TYPE__MIN_SCALE_DENOMINATOR, oldMinScaleDenominator, MIN_SCALE_DENOMINATOR_EDEFAULT, oldMinScaleDenominatorESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetMinScaleDenominator() {
        return minScaleDenominatorESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BigInteger getWidth() {
        return width;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setWidth(BigInteger newWidth) {
        BigInteger oldWidth = width;
        width = newWidth;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, wmtsv_1Package.LEGEND_URL_TYPE__WIDTH, oldWidth, width));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case wmtsv_1Package.LEGEND_URL_TYPE__FORMAT:
                return getFormat();
            case wmtsv_1Package.LEGEND_URL_TYPE__HEIGHT:
                return getHeight();
            case wmtsv_1Package.LEGEND_URL_TYPE__MAX_SCALE_DENOMINATOR:
                return getMaxScaleDenominator();
            case wmtsv_1Package.LEGEND_URL_TYPE__MIN_SCALE_DENOMINATOR:
                return getMinScaleDenominator();
            case wmtsv_1Package.LEGEND_URL_TYPE__WIDTH:
                return getWidth();
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
            case wmtsv_1Package.LEGEND_URL_TYPE__FORMAT:
                setFormat((String)newValue);
                return;
            case wmtsv_1Package.LEGEND_URL_TYPE__HEIGHT:
                setHeight((BigInteger)newValue);
                return;
            case wmtsv_1Package.LEGEND_URL_TYPE__MAX_SCALE_DENOMINATOR:
                setMaxScaleDenominator((Double)newValue);
                return;
            case wmtsv_1Package.LEGEND_URL_TYPE__MIN_SCALE_DENOMINATOR:
                setMinScaleDenominator((Double)newValue);
                return;
            case wmtsv_1Package.LEGEND_URL_TYPE__WIDTH:
                setWidth((BigInteger)newValue);
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
            case wmtsv_1Package.LEGEND_URL_TYPE__FORMAT:
                setFormat(FORMAT_EDEFAULT);
                return;
            case wmtsv_1Package.LEGEND_URL_TYPE__HEIGHT:
                setHeight(HEIGHT_EDEFAULT);
                return;
            case wmtsv_1Package.LEGEND_URL_TYPE__MAX_SCALE_DENOMINATOR:
                unsetMaxScaleDenominator();
                return;
            case wmtsv_1Package.LEGEND_URL_TYPE__MIN_SCALE_DENOMINATOR:
                unsetMinScaleDenominator();
                return;
            case wmtsv_1Package.LEGEND_URL_TYPE__WIDTH:
                setWidth(WIDTH_EDEFAULT);
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
            case wmtsv_1Package.LEGEND_URL_TYPE__FORMAT:
                return FORMAT_EDEFAULT == null ? format != null : !FORMAT_EDEFAULT.equals(format);
            case wmtsv_1Package.LEGEND_URL_TYPE__HEIGHT:
                return HEIGHT_EDEFAULT == null ? height != null : !HEIGHT_EDEFAULT.equals(height);
            case wmtsv_1Package.LEGEND_URL_TYPE__MAX_SCALE_DENOMINATOR:
                return isSetMaxScaleDenominator();
            case wmtsv_1Package.LEGEND_URL_TYPE__MIN_SCALE_DENOMINATOR:
                return isSetMinScaleDenominator();
            case wmtsv_1Package.LEGEND_URL_TYPE__WIDTH:
                return WIDTH_EDEFAULT == null ? width != null : !WIDTH_EDEFAULT.equals(width);
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
        result.append(" (format: ");
        result.append(format);
        result.append(", height: ");
        result.append(height);
        result.append(", maxScaleDenominator: ");
        if (maxScaleDenominatorESet) result.append(maxScaleDenominator); else result.append("<unset>");
        result.append(", minScaleDenominator: ");
        if (minScaleDenominatorESet) result.append(minScaleDenominator); else result.append("<unset>");
        result.append(", width: ");
        result.append(width);
        result.append(')');
        return result.toString();
    }

} //LegendURLTypeImpl
