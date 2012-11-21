/**
 */
package net.opengis.ows20;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Range Closure Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see net.opengis.ows20.Ows20Package#getRangeClosureType()
 * @model extendedMetaData="name='rangeClosure_._type'"
 * @generated
 */
public enum RangeClosureType implements Enumerator {
    /**
     * The '<em><b>Closed</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #CLOSED_VALUE
     * @generated
     * @ordered
     */
    CLOSED(0, "closed", "closed"),

    /**
     * The '<em><b>Open</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #OPEN_VALUE
     * @generated
     * @ordered
     */
    OPEN(1, "open", "open"),

    /**
     * The '<em><b>Open Closed</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #OPEN_CLOSED_VALUE
     * @generated
     * @ordered
     */
    OPEN_CLOSED(2, "openClosed", "open-closed"),

    /**
     * The '<em><b>Closed Open</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #CLOSED_OPEN_VALUE
     * @generated
     * @ordered
     */
    CLOSED_OPEN(3, "closedOpen", "closed-open");

    /**
     * The '<em><b>Closed</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The specified minimum and maximum values are
     *             included in this range.
     * <!-- end-model-doc -->
     * @see #CLOSED
     * @model name="closed"
     * @generated
     * @ordered
     */
    public static final int CLOSED_VALUE = 0;

    /**
     * The '<em><b>Open</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The specified minimum and maximum values are NOT
     *             included in this range.
     * <!-- end-model-doc -->
     * @see #OPEN
     * @model name="open"
     * @generated
     * @ordered
     */
    public static final int OPEN_VALUE = 1;

    /**
     * The '<em><b>Open Closed</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The specified minimum value is NOT included in this
     *             range, and the specified maximum value IS included in this
     *             range.
     * <!-- end-model-doc -->
     * @see #OPEN_CLOSED
     * @model name="openClosed" literal="open-closed"
     * @generated
     * @ordered
     */
    public static final int OPEN_CLOSED_VALUE = 2;

    /**
     * The '<em><b>Closed Open</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The specified minimum value IS included in this
     *             range, and the specified maximum value is NOT included in this
     *             range.
     * <!-- end-model-doc -->
     * @see #CLOSED_OPEN
     * @model name="closedOpen" literal="closed-open"
     * @generated
     * @ordered
     */
    public static final int CLOSED_OPEN_VALUE = 3;

    /**
     * An array of all the '<em><b>Range Closure Type</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static final RangeClosureType[] VALUES_ARRAY =
        new RangeClosureType[] {
            CLOSED,
            OPEN,
            OPEN_CLOSED,
            CLOSED_OPEN,
        };

    /**
     * A public read-only list of all the '<em><b>Range Closure Type</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final List<RangeClosureType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
     * Returns the '<em><b>Range Closure Type</b></em>' literal with the specified literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static RangeClosureType get(String literal) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            RangeClosureType result = VALUES_ARRAY[i];
            if (result.toString().equals(literal)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Range Closure Type</b></em>' literal with the specified name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static RangeClosureType getByName(String name) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            RangeClosureType result = VALUES_ARRAY[i];
            if (result.getName().equals(name)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Range Closure Type</b></em>' literal with the specified integer value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static RangeClosureType get(int value) {
        switch (value) {
            case CLOSED_VALUE: return CLOSED;
            case OPEN_VALUE: return OPEN;
            case OPEN_CLOSED_VALUE: return OPEN_CLOSED;
            case CLOSED_OPEN_VALUE: return CLOSED_OPEN;
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
    private RangeClosureType(int value, String name, String literal) {
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
    
} //RangeClosureType
