package org.geotools.kml;

/**
 * Container for folder name hierarchies
 *
 */
public class Folder {

    private String name;

    public Folder() {
        this(null);
    }

    public Folder(String name) {
        this.name = name;
    }

    /**
     * Return the folder's name.
     *
     * @return folder's name. Can be null.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the folder's name
     *
     * @param name folder's name
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Folder (name=" + name + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Folder)) {
            return false;
        }
        Folder that = (Folder) obj;
        if (name == null) {
            return that.name == null;
        }
        return this.name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name == null ? 0 : name.hashCode();
    }
}
