/**
 * This file is hereby placed into the Public Domain. This means anyone is
 * free to do whatever they wish with this file.
 */
package mil.nga.giat.process.elasticsearch;

import org.geotools.filter.visitor.DuplicatingFilterVisitor;
import org.opengis.filter.Filter;
import org.opengis.filter.spatial.BBOX;

public class BBOXRemovingFilterVisitor extends DuplicatingFilterVisitor {

    private String geometryPropertyName;

    @Override
    public Object visit(BBOX filter, Object extraData) {
        geometryPropertyName = filter.getPropertyName();
        return Filter.INCLUDE;
    }

    public String getGeometryPropertyName() {
        return geometryPropertyName;
    }

    public void setGeometryPropertyName(String geometryPropertyName) {
        this.geometryPropertyName = geometryPropertyName;
    }

}
