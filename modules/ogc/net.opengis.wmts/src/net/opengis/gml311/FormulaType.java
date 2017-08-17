/**
 */
package net.opengis.gml311;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Formula Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Paremeters of a simple formula by which a value using this unit of measure can be converted to the corresponding value using the preferred unit of measure. The formula element contains elements a, b, c and d, whose values use the XML Schema type "double". These values are used in the formula y = (a + bx) / (c + dx), where x is a value using this unit, and y is the corresponding value using the preferred unit. The elements a and d are optional, and if values are not provided, those parameters are considered to be zero. If values are not provided for both a and d, the formula is equivalent to a fraction with numerator and denominator parameters.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.FormulaType#getA <em>A</em>}</li>
 *   <li>{@link net.opengis.gml311.FormulaType#getB <em>B</em>}</li>
 *   <li>{@link net.opengis.gml311.FormulaType#getC <em>C</em>}</li>
 *   <li>{@link net.opengis.gml311.FormulaType#getD <em>D</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getFormulaType()
 * @model extendedMetaData="name='FormulaType' kind='elementOnly'"
 * @generated
 */
public interface FormulaType extends EObject {
    /**
     * Returns the value of the '<em><b>A</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>A</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>A</em>' attribute.
     * @see #isSetA()
     * @see #unsetA()
     * @see #setA(double)
     * @see net.opengis.gml311.Gml311Package#getFormulaType_A()
     * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Double"
     *        extendedMetaData="kind='element' name='a' namespace='##targetNamespace'"
     * @generated
     */
    double getA();

    /**
     * Sets the value of the '{@link net.opengis.gml311.FormulaType#getA <em>A</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>A</em>' attribute.
     * @see #isSetA()
     * @see #unsetA()
     * @see #getA()
     * @generated
     */
    void setA(double value);

    /**
     * Unsets the value of the '{@link net.opengis.gml311.FormulaType#getA <em>A</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetA()
     * @see #getA()
     * @see #setA(double)
     * @generated
     */
    void unsetA();

    /**
     * Returns whether the value of the '{@link net.opengis.gml311.FormulaType#getA <em>A</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>A</em>' attribute is set.
     * @see #unsetA()
     * @see #getA()
     * @see #setA(double)
     * @generated
     */
    boolean isSetA();

    /**
     * Returns the value of the '<em><b>B</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>B</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>B</em>' attribute.
     * @see #isSetB()
     * @see #unsetB()
     * @see #setB(double)
     * @see net.opengis.gml311.Gml311Package#getFormulaType_B()
     * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Double" required="true"
     *        extendedMetaData="kind='element' name='b' namespace='##targetNamespace'"
     * @generated
     */
    double getB();

    /**
     * Sets the value of the '{@link net.opengis.gml311.FormulaType#getB <em>B</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>B</em>' attribute.
     * @see #isSetB()
     * @see #unsetB()
     * @see #getB()
     * @generated
     */
    void setB(double value);

    /**
     * Unsets the value of the '{@link net.opengis.gml311.FormulaType#getB <em>B</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetB()
     * @see #getB()
     * @see #setB(double)
     * @generated
     */
    void unsetB();

    /**
     * Returns whether the value of the '{@link net.opengis.gml311.FormulaType#getB <em>B</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>B</em>' attribute is set.
     * @see #unsetB()
     * @see #getB()
     * @see #setB(double)
     * @generated
     */
    boolean isSetB();

    /**
     * Returns the value of the '<em><b>C</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>C</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>C</em>' attribute.
     * @see #isSetC()
     * @see #unsetC()
     * @see #setC(double)
     * @see net.opengis.gml311.Gml311Package#getFormulaType_C()
     * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Double" required="true"
     *        extendedMetaData="kind='element' name='c' namespace='##targetNamespace'"
     * @generated
     */
    double getC();

    /**
     * Sets the value of the '{@link net.opengis.gml311.FormulaType#getC <em>C</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>C</em>' attribute.
     * @see #isSetC()
     * @see #unsetC()
     * @see #getC()
     * @generated
     */
    void setC(double value);

    /**
     * Unsets the value of the '{@link net.opengis.gml311.FormulaType#getC <em>C</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetC()
     * @see #getC()
     * @see #setC(double)
     * @generated
     */
    void unsetC();

    /**
     * Returns whether the value of the '{@link net.opengis.gml311.FormulaType#getC <em>C</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>C</em>' attribute is set.
     * @see #unsetC()
     * @see #getC()
     * @see #setC(double)
     * @generated
     */
    boolean isSetC();

    /**
     * Returns the value of the '<em><b>D</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>D</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>D</em>' attribute.
     * @see #isSetD()
     * @see #unsetD()
     * @see #setD(double)
     * @see net.opengis.gml311.Gml311Package#getFormulaType_D()
     * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Double"
     *        extendedMetaData="kind='element' name='d' namespace='##targetNamespace'"
     * @generated
     */
    double getD();

    /**
     * Sets the value of the '{@link net.opengis.gml311.FormulaType#getD <em>D</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>D</em>' attribute.
     * @see #isSetD()
     * @see #unsetD()
     * @see #getD()
     * @generated
     */
    void setD(double value);

    /**
     * Unsets the value of the '{@link net.opengis.gml311.FormulaType#getD <em>D</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetD()
     * @see #getD()
     * @see #setD(double)
     * @generated
     */
    void unsetD();

    /**
     * Returns whether the value of the '{@link net.opengis.gml311.FormulaType#getD <em>D</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>D</em>' attribute is set.
     * @see #unsetD()
     * @see #getD()
     * @see #setD(double)
     * @generated
     */
    boolean isSetD();

} // FormulaType
