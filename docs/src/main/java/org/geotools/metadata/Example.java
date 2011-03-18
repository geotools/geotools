package org.geotools.metadata;

import org.geotools.util.Utilities;

public class Example {
    
    private Object field1;
    private int field2;
    private double array[];
    
    @Override
    public int hashCode() {
        int result = 1;
        result = Utilities.hash(field1, result);
        result = Utilities.hash(field2, result);
        result = Utilities.hash(array, result);
        
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Example)) {
            return false;
        }
        Example other = (Example) obj;
        return Utilities.equals(field1, other.field1) && Utilities.equals(field2, other.field2)
                && Utilities.deepEquals(array, other.array);
    }
    
    @Override
    public String toString() {
        StringBuilder build = new StringBuilder();
        build.append("Example[");
        if (field1 != null) {
            build.append(" field1=");
            build.append(field1);
        }
        build.append(" field2=");
        build.append(field2);
        if (array != null) {
            build.append(" array=");
            build.append(Utilities.deepToString(array));
        }
        build.append("]");
        return build.toString();
    }
}