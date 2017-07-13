/**
 */
package net.opengis.gml311.impl;

import net.opengis.gml311.FormulaType;
import net.opengis.gml311.Gml311Package;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Formula Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.FormulaTypeImpl#getA <em>A</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.FormulaTypeImpl#getB <em>B</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.FormulaTypeImpl#getC <em>C</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.FormulaTypeImpl#getD <em>D</em>}</li>
 * </ul>
 *
 * @generated
 */
public class FormulaTypeImpl extends MinimalEObjectImpl.Container implements FormulaType {
    /**
     * The default value of the '{@link #getA() <em>A</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getA()
     * @generated
     * @ordered
     */
    protected static final double A_EDEFAULT = 0.0;

    /**
     * The cached value of the '{@link #getA() <em>A</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getA()
     * @generated
     * @ordered
     */
    protected double a = A_EDEFAULT;

    /**
     * This is true if the A attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean aESet;

    /**
     * The default value of the '{@link #getB() <em>B</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getB()
     * @generated
     * @ordered
     */
    protected static final double B_EDEFAULT = 0.0;

    /**
     * The cached value of the '{@link #getB() <em>B</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getB()
     * @generated
     * @ordered
     */
    protected double b = B_EDEFAULT;

    /**
     * This is true if the B attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean bESet;

    /**
     * The default value of the '{@link #getC() <em>C</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getC()
     * @generated
     * @ordered
     */
    protected static final double C_EDEFAULT = 0.0;

    /**
     * The cached value of the '{@link #getC() <em>C</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getC()
     * @generated
     * @ordered
     */
    protected double c = C_EDEFAULT;

    /**
     * This is true if the C attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean cESet;

    /**
     * The default value of the '{@link #getD() <em>D</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getD()
     * @generated
     * @ordered
     */
    protected static final double D_EDEFAULT = 0.0;

    /**
     * The cached value of the '{@link #getD() <em>D</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getD()
     * @generated
     * @ordered
     */
    protected double d = D_EDEFAULT;

    /**
     * This is true if the D attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean dESet;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected FormulaTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getFormulaType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public double getA() {
        return a;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setA(double newA) {
        double oldA = a;
        a = newA;
        boolean oldAESet = aESet;
        aESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.FORMULA_TYPE__A, oldA, a, !oldAESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetA() {
        double oldA = a;
        boolean oldAESet = aESet;
        a = A_EDEFAULT;
        aESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Gml311Package.FORMULA_TYPE__A, oldA, A_EDEFAULT, oldAESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetA() {
        return aESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public double getB() {
        return b;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setB(double newB) {
        double oldB = b;
        b = newB;
        boolean oldBESet = bESet;
        bESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.FORMULA_TYPE__B, oldB, b, !oldBESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetB() {
        double oldB = b;
        boolean oldBESet = bESet;
        b = B_EDEFAULT;
        bESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Gml311Package.FORMULA_TYPE__B, oldB, B_EDEFAULT, oldBESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetB() {
        return bESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public double getC() {
        return c;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setC(double newC) {
        double oldC = c;
        c = newC;
        boolean oldCESet = cESet;
        cESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.FORMULA_TYPE__C, oldC, c, !oldCESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetC() {
        double oldC = c;
        boolean oldCESet = cESet;
        c = C_EDEFAULT;
        cESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Gml311Package.FORMULA_TYPE__C, oldC, C_EDEFAULT, oldCESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetC() {
        return cESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public double getD() {
        return d;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setD(double newD) {
        double oldD = d;
        d = newD;
        boolean oldDESet = dESet;
        dESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.FORMULA_TYPE__D, oldD, d, !oldDESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetD() {
        double oldD = d;
        boolean oldDESet = dESet;
        d = D_EDEFAULT;
        dESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Gml311Package.FORMULA_TYPE__D, oldD, D_EDEFAULT, oldDESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetD() {
        return dESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Gml311Package.FORMULA_TYPE__A:
                return getA();
            case Gml311Package.FORMULA_TYPE__B:
                return getB();
            case Gml311Package.FORMULA_TYPE__C:
                return getC();
            case Gml311Package.FORMULA_TYPE__D:
                return getD();
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
            case Gml311Package.FORMULA_TYPE__A:
                setA((Double)newValue);
                return;
            case Gml311Package.FORMULA_TYPE__B:
                setB((Double)newValue);
                return;
            case Gml311Package.FORMULA_TYPE__C:
                setC((Double)newValue);
                return;
            case Gml311Package.FORMULA_TYPE__D:
                setD((Double)newValue);
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
            case Gml311Package.FORMULA_TYPE__A:
                unsetA();
                return;
            case Gml311Package.FORMULA_TYPE__B:
                unsetB();
                return;
            case Gml311Package.FORMULA_TYPE__C:
                unsetC();
                return;
            case Gml311Package.FORMULA_TYPE__D:
                unsetD();
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
            case Gml311Package.FORMULA_TYPE__A:
                return isSetA();
            case Gml311Package.FORMULA_TYPE__B:
                return isSetB();
            case Gml311Package.FORMULA_TYPE__C:
                return isSetC();
            case Gml311Package.FORMULA_TYPE__D:
                return isSetD();
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
        result.append(" (a: ");
        if (aESet) result.append(a); else result.append("<unset>");
        result.append(", b: ");
        if (bESet) result.append(b); else result.append("<unset>");
        result.append(", c: ");
        if (cESet) result.append(c); else result.append("<unset>");
        result.append(", d: ");
        if (dESet) result.append(d); else result.append("<unset>");
        result.append(')');
        return result.toString();
    }

} //FormulaTypeImpl
