
package org.geotools.data.arcgisrest.schema.query;

<<<<<<< HEAD

=======
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
public class Geometry {


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
<<<<<<< HEAD
        sb.append(Geometry.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
=======
        sb.append(Geometry.class.getName())
                .append('@')
                .append(Integer.toHexString(System.identityHashCode(this)))
                .append('[');
        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Geometry) == false) {
            return false;
        }
        Geometry rhs = ((Geometry) other);
        return true;
    }

}
