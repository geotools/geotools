package org.opengis.filter.identity;

import java.util.Date;

/**
 * Union type class for the {@code Version} Union type in FES 2.0.
 * <p>
 * The union is actually captured as a union inside a single long field.
 * 
 * @invariant {@code #getVersionAction() != null || #getIndex() != null || #getDateTime() != null}
 */
public final class Version {

    /**
     * The VersionAction attribute may also be the strings FIRST, LATEST, PREVIOUS, NEXT and ALL.
     */
    public enum Action {
        /** Select the first version of a resource. */
        FIRST,
        /** Select the most recent version of a resource. */
        LAST,
        /** Select the previous version of a resource relative to the version specified using the rid attribute. */
        NEXT,
        /** Select the next version of a resource relative to the version specified using the rid attribute. */
        PREVIOUS,
        /** Select all available version of a resource. */
        ALL;

        public static Action lookup(int ordinal) {
            for (Action action : Action.values()) {
                if (action.ordinal() == ordinal) {
                    return action;
                }
            }
            return null; // not found
        }
    }

    static final long UNION_MASK = 0x0FFFFFFFFFFFFFFFl;

    static final long UNION_INTEGER = 0x1000000000000000l;

    static final long UNION_DATE = 0x2000000000000000l;

    static final long UNION_ACTION = 0x4000000000000000l;

    /**
     * The union is represented as
     * <ul>
     * <li>UNION_INTEGER | <code>int</code></li>
     * <li>UNION_DATE | <code>date.getTime()</code></li>
     * <li>UNION_ACTION | <code>action.ordinal()</code></li>
     * </ul>
     */
    final long union;

    public Version(long union){
        this.union = union;
    }
    
    public Version(final Action action) {
        if (action == null) {
            throw new IllegalArgumentException("action can't be null");
        }
        this.union = UNION_ACTION | action.ordinal();
    }

    public Version(final Integer index) {
        if (index == null) {
            throw new IllegalArgumentException("index can't be null");
        }
        this.union = UNION_INTEGER | (long) index;
    }

    public Version(final Date dateTime) {
        if (dateTime == null) {
            throw new IllegalArgumentException("dateTime can't be null");
        }
        this.union = UNION_DATE | (dateTime.getTime());
    }

    public boolean isVersionAction() {
        return (UNION_ACTION & union) > 0;
    }
    
    /**
     * Access to the union memento; this may be stored as an encoding of the
     * Version in memory sensitive settings where the over head of an object
     * is not desired.
     * <p>
     * To restore please use <code>new Version( union )</code>
     * 
     * @return memento holding the contents of a Version object
     */
    public long union(){
        return union;
    }
    
    /**
     * Version requested using a predefined constant.
     * <p>
     * The versionAction attribute may also be the strings FIRST, LATEST, PREVIOUS, NEXT and ALL. The token FIRST shall select the first version of a
     * resource. The token LATEST shall select the most recent version of a resource. The PREVIOUS and NEXT tokens shall select the previous or next
     * version of a resource relative to the version specified using the rid attribute. The token ALL shall select all available version of a
     * resource.
     * 
     * @return Version requested using a predefined constant.
     */
    public Action getVersionAction() {
        if ((UNION_ACTION & union) > 0) {
            int ordinal = (int) (union & UNION_MASK);
            Action action = Action.lookup(ordinal);
            return action;
        }
        return null;
    }

    /**
     * Version index requested.
     * 
     * @return true if the Version is supplied by an index
     */
    public boolean isIndex() {
        return (UNION_INTEGER & union) > 0;
    }

    /**
     * Version requested as defined by an index (from 1 throught to the latest).
     * <p>
     * The version attribute may be an integer N indicating that the Nth version of the resource shall be selected. The first version of a resource
     * shall be numbered 1. If N exceeds the number of versions available, the latest version of the resource shall be selected.
     * 
     * @return index of version requested (from 1 through to the latest)
     */
    public Integer getIndex() {
        if ((UNION_INTEGER & union) > 0) {
            int index = (int) (union & UNION_MASK);
            return index;
        }
        return null;
    }

    public boolean isDateTime() {
        return (UNION_DATE & union) > 0;
    }

    /**
     * Version requested as the closest to the provided date.
     * <p>
     * The version attribute may also be date indicating that the version of the resource closest to the specified date shall be selected.
     * 
     * @return date of version requested
     */
    public Date getDateTime() {
        if ((UNION_DATE & union) > 0) {
            long time = (union & UNION_MASK);
            return new Date(time);
        }
        return null;
    }
    
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Version)) {
            return false;
        }
        Version v = (Version) o;
        return union == v.union;
    }

    @Override
    public int hashCode() {
        return 17 * (int) union;
    }
}