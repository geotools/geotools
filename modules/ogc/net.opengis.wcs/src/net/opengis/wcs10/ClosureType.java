/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs10;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.AbstractEnumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Closure Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see net.opengis.wcs10.Wcs10Package#getClosureType()
 * @model extendedMetaData="name='closure_._type'"
 * @generated
 */
public final class ClosureType extends AbstractEnumerator {
    /**
	 * The '<em><b>Closed</b></em>' literal value.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The specified minimum and maximum values are included in this range.
	 * <!-- end-model-doc -->
	 * @see #CLOSED_LITERAL
	 * @model name="closed"
	 * @generated
	 * @ordered
	 */
    public static final int CLOSED = 0;

    /**
	 * The '<em><b>Open</b></em>' literal value.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The specified minimum and maximum values are NOT included in this range.
	 * <!-- end-model-doc -->
	 * @see #OPEN_LITERAL
	 * @model name="open"
	 * @generated
	 * @ordered
	 */
    public static final int OPEN = 1;

    /**
	 * The '<em><b>Open Closed</b></em>' literal value.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The specified minimum value is NOT included in this range, and the specified maximum value IS included in this range.
	 * <!-- end-model-doc -->
	 * @see #OPEN_CLOSED_LITERAL
	 * @model name="openClosed" literal="open-closed"
	 * @generated
	 * @ordered
	 */
    public static final int OPEN_CLOSED = 2;

    /**
	 * The '<em><b>Closed Open</b></em>' literal value.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The specified minimum value IS included in this range, and the specified maximum value is NOT included in this range.
	 * <!-- end-model-doc -->
	 * @see #CLOSED_OPEN_LITERAL
	 * @model name="closedOpen" literal="closed-open"
	 * @generated
	 * @ordered
	 */
    public static final int CLOSED_OPEN = 3;

    /**
	 * The '<em><b>Closed</b></em>' literal object.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #CLOSED
	 * @generated
	 * @ordered
	 */
    public static final ClosureType CLOSED_LITERAL = new ClosureType(CLOSED, "closed", "closed");

    /**
	 * The '<em><b>Open</b></em>' literal object.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #OPEN
	 * @generated
	 * @ordered
	 */
    public static final ClosureType OPEN_LITERAL = new ClosureType(OPEN, "open", "open");

    /**
	 * The '<em><b>Open Closed</b></em>' literal object.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #OPEN_CLOSED
	 * @generated
	 * @ordered
	 */
    public static final ClosureType OPEN_CLOSED_LITERAL = new ClosureType(OPEN_CLOSED, "openClosed", "open-closed");

    /**
	 * The '<em><b>Closed Open</b></em>' literal object.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #CLOSED_OPEN
	 * @generated
	 * @ordered
	 */
    public static final ClosureType CLOSED_OPEN_LITERAL = new ClosureType(CLOSED_OPEN, "closedOpen", "closed-open");

    /**
	 * An array of all the '<em><b>Closure Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private static final ClosureType[] VALUES_ARRAY =
        new ClosureType[] {
			CLOSED_LITERAL,
			OPEN_LITERAL,
			OPEN_CLOSED_LITERAL,
			CLOSED_OPEN_LITERAL,
		};

    /**
	 * A public read-only list of all the '<em><b>Closure Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public static final List VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
	 * Returns the '<em><b>Closure Type</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public static ClosureType get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			ClosureType result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

    /**
	 * Returns the '<em><b>Closure Type</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public static ClosureType getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			ClosureType result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

    /**
	 * Returns the '<em><b>Closure Type</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public static ClosureType get(int value) {
		switch (value) {
			case CLOSED: return CLOSED_LITERAL;
			case OPEN: return OPEN_LITERAL;
			case OPEN_CLOSED: return OPEN_CLOSED_LITERAL;
			case CLOSED_OPEN: return CLOSED_OPEN_LITERAL;
		}
		return null;
	}

    /**
	 * Only this class can construct instances.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private ClosureType(int value, String name, String literal) {
		super(value, name, literal);
	}

} //ClosureType
