/**
 */
package net.opengis.gml311;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Relative Position Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see net.opengis.gml311.Gml311Package#getRelativePositionType()
 * @model extendedMetaData="name='relativePosition_._type'"
 * @generated
 */
public enum RelativePositionType implements Enumerator {
    /**
     * The '<em><b>Before</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #BEFORE_VALUE
     * @generated
     * @ordered
     */
    BEFORE(0, "Before", "Before"),

    /**
     * The '<em><b>After</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #AFTER_VALUE
     * @generated
     * @ordered
     */
    AFTER(1, "After", "After"),

    /**
     * The '<em><b>Begins</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #BEGINS_VALUE
     * @generated
     * @ordered
     */
    BEGINS(2, "Begins", "Begins"),

    /**
     * The '<em><b>Ends</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #ENDS_VALUE
     * @generated
     * @ordered
     */
    ENDS(3, "Ends", "Ends"),

    /**
     * The '<em><b>During</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #DURING_VALUE
     * @generated
     * @ordered
     */
    DURING(4, "During", "During"),

    /**
     * The '<em><b>Equals</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #EQUALS_VALUE
     * @generated
     * @ordered
     */
    EQUALS(5, "Equals", "Equals"),

    /**
     * The '<em><b>Contains</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #CONTAINS_VALUE
     * @generated
     * @ordered
     */
    CONTAINS(6, "Contains", "Contains"),

    /**
     * The '<em><b>Overlaps</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #OVERLAPS_VALUE
     * @generated
     * @ordered
     */
    OVERLAPS(7, "Overlaps", "Overlaps"),

    /**
     * The '<em><b>Meets</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #MEETS_VALUE
     * @generated
     * @ordered
     */
    MEETS(8, "Meets", "Meets"),

    /**
     * The '<em><b>Overlapped By</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #OVERLAPPED_BY_VALUE
     * @generated
     * @ordered
     */
    OVERLAPPED_BY(9, "OverlappedBy", "OverlappedBy"),

    /**
     * The '<em><b>Met By</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #MET_BY_VALUE
     * @generated
     * @ordered
     */
    MET_BY(10, "MetBy", "MetBy"),

    /**
     * The '<em><b>Begun By</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #BEGUN_BY_VALUE
     * @generated
     * @ordered
     */
    BEGUN_BY(11, "BegunBy", "BegunBy"),

    /**
     * The '<em><b>Ended By</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #ENDED_BY_VALUE
     * @generated
     * @ordered
     */
    ENDED_BY(12, "EndedBy", "EndedBy");

    /**
     * The '<em><b>Before</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Before</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #BEFORE
     * @model name="Before"
     * @generated
     * @ordered
     */
    public static final int BEFORE_VALUE = 0;

    /**
     * The '<em><b>After</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>After</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #AFTER
     * @model name="After"
     * @generated
     * @ordered
     */
    public static final int AFTER_VALUE = 1;

    /**
     * The '<em><b>Begins</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Begins</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #BEGINS
     * @model name="Begins"
     * @generated
     * @ordered
     */
    public static final int BEGINS_VALUE = 2;

    /**
     * The '<em><b>Ends</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Ends</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #ENDS
     * @model name="Ends"
     * @generated
     * @ordered
     */
    public static final int ENDS_VALUE = 3;

    /**
     * The '<em><b>During</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>During</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #DURING
     * @model name="During"
     * @generated
     * @ordered
     */
    public static final int DURING_VALUE = 4;

    /**
     * The '<em><b>Equals</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Equals</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #EQUALS
     * @model name="Equals"
     * @generated
     * @ordered
     */
    public static final int EQUALS_VALUE = 5;

    /**
     * The '<em><b>Contains</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Contains</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #CONTAINS
     * @model name="Contains"
     * @generated
     * @ordered
     */
    public static final int CONTAINS_VALUE = 6;

    /**
     * The '<em><b>Overlaps</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Overlaps</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #OVERLAPS
     * @model name="Overlaps"
     * @generated
     * @ordered
     */
    public static final int OVERLAPS_VALUE = 7;

    /**
     * The '<em><b>Meets</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Meets</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #MEETS
     * @model name="Meets"
     * @generated
     * @ordered
     */
    public static final int MEETS_VALUE = 8;

    /**
     * The '<em><b>Overlapped By</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Overlapped By</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #OVERLAPPED_BY
     * @model name="OverlappedBy"
     * @generated
     * @ordered
     */
    public static final int OVERLAPPED_BY_VALUE = 9;

    /**
     * The '<em><b>Met By</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Met By</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #MET_BY
     * @model name="MetBy"
     * @generated
     * @ordered
     */
    public static final int MET_BY_VALUE = 10;

    /**
     * The '<em><b>Begun By</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Begun By</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #BEGUN_BY
     * @model name="BegunBy"
     * @generated
     * @ordered
     */
    public static final int BEGUN_BY_VALUE = 11;

    /**
     * The '<em><b>Ended By</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Ended By</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #ENDED_BY
     * @model name="EndedBy"
     * @generated
     * @ordered
     */
    public static final int ENDED_BY_VALUE = 12;

    /**
     * An array of all the '<em><b>Relative Position Type</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static final RelativePositionType[] VALUES_ARRAY =
        new RelativePositionType[] {
            BEFORE,
            AFTER,
            BEGINS,
            ENDS,
            DURING,
            EQUALS,
            CONTAINS,
            OVERLAPS,
            MEETS,
            OVERLAPPED_BY,
            MET_BY,
            BEGUN_BY,
            ENDED_BY,
        };

    /**
     * A public read-only list of all the '<em><b>Relative Position Type</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final List<RelativePositionType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
     * Returns the '<em><b>Relative Position Type</b></em>' literal with the specified literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param literal the literal.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static RelativePositionType get(String literal) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            RelativePositionType result = VALUES_ARRAY[i];
            if (result.toString().equals(literal)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Relative Position Type</b></em>' literal with the specified name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param name the name.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static RelativePositionType getByName(String name) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            RelativePositionType result = VALUES_ARRAY[i];
            if (result.getName().equals(name)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Relative Position Type</b></em>' literal with the specified integer value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the integer value.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static RelativePositionType get(int value) {
        switch (value) {
            case BEFORE_VALUE: return BEFORE;
            case AFTER_VALUE: return AFTER;
            case BEGINS_VALUE: return BEGINS;
            case ENDS_VALUE: return ENDS;
            case DURING_VALUE: return DURING;
            case EQUALS_VALUE: return EQUALS;
            case CONTAINS_VALUE: return CONTAINS;
            case OVERLAPS_VALUE: return OVERLAPS;
            case MEETS_VALUE: return MEETS;
            case OVERLAPPED_BY_VALUE: return OVERLAPPED_BY;
            case MET_BY_VALUE: return MET_BY;
            case BEGUN_BY_VALUE: return BEGUN_BY;
            case ENDED_BY_VALUE: return ENDED_BY;
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
    private RelativePositionType(int value, String name, String literal) {
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
    
} //RelativePositionType
