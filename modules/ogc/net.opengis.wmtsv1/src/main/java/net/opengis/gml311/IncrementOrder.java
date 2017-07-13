/**
 */
package net.opengis.gml311;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Increment Order</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * <!-- begin-model-doc -->
 * The enumeration value here indicates the incrementation order  to be used on the first 2 axes, i.e. "+x-y" means that the points on the first axis are to be traversed from lowest to highest and  the points on the second axis are to be traversed from highest to lowest. The points on all other axes (if any) beyond the first 2 are assumed to increment from lowest to highest.
 * <!-- end-model-doc -->
 * @see net.opengis.gml311.Gml311Package#getIncrementOrder()
 * @model extendedMetaData="name='IncrementOrder'"
 * @generated
 */
public enum IncrementOrder implements Enumerator {
    /**
     * The '<em><b>XY</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #XY_VALUE
     * @generated
     * @ordered
     */
    XY(0, "xY", "+x+y"),

    /**
     * The '<em><b>YX</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #YX_VALUE
     * @generated
     * @ordered
     */
    YX(1, "yX", "+y+x"),

    /**
     * The '<em><b>XY1</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #XY1_VALUE
     * @generated
     * @ordered
     */
    XY1(2, "xY1", "+x-y"),

    /**
     * The '<em><b>XY2</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #XY2_VALUE
     * @generated
     * @ordered
     */
    XY2(3, "xY2", "-x-y");

    /**
     * The '<em><b>XY</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>XY</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #XY
     * @model name="xY" literal="+x+y"
     * @generated
     * @ordered
     */
    public static final int XY_VALUE = 0;

    /**
     * The '<em><b>YX</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>YX</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #YX
     * @model name="yX" literal="+y+x"
     * @generated
     * @ordered
     */
    public static final int YX_VALUE = 1;

    /**
     * The '<em><b>XY1</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>XY1</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #XY1
     * @model name="xY1" literal="+x-y"
     * @generated
     * @ordered
     */
    public static final int XY1_VALUE = 2;

    /**
     * The '<em><b>XY2</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>XY2</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #XY2
     * @model name="xY2" literal="-x-y"
     * @generated
     * @ordered
     */
    public static final int XY2_VALUE = 3;

    /**
     * An array of all the '<em><b>Increment Order</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static final IncrementOrder[] VALUES_ARRAY =
        new IncrementOrder[] {
            XY,
            YX,
            XY1,
            XY2,
        };

    /**
     * A public read-only list of all the '<em><b>Increment Order</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final List<IncrementOrder> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
     * Returns the '<em><b>Increment Order</b></em>' literal with the specified literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param literal the literal.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static IncrementOrder get(String literal) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            IncrementOrder result = VALUES_ARRAY[i];
            if (result.toString().equals(literal)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Increment Order</b></em>' literal with the specified name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param name the name.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static IncrementOrder getByName(String name) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            IncrementOrder result = VALUES_ARRAY[i];
            if (result.getName().equals(name)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Increment Order</b></em>' literal with the specified integer value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the integer value.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static IncrementOrder get(int value) {
        switch (value) {
            case XY_VALUE: return XY;
            case YX_VALUE: return YX;
            case XY1_VALUE: return XY1;
            case XY2_VALUE: return XY2;
        }
        return null;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private final int value;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private final String name;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private final String literal;

    /**
     * Only this class can construct instances.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private IncrementOrder(int value, String name, String literal) {
        this.value = value;
        this.name = name;
        this.literal = literal;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public int getValue() {
      return value;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getName() {
      return name;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getLiteral() {
      return literal;
    }

    /**
     * Returns the literal value of the enumerator, which is its string representation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString() {
        return literal;
    }
    
} //IncrementOrder
