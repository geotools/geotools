/**
 * This file is hereby placed into the Public Domain. This means anyone is
 * free to do whatever they wish with this file.
 */
package mil.nga.giat.data.elasticsearch;

import java.io.Serializable;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Class describing and Elasticsearch attribute including name, type and
 * optional information on geometry and date types. Also includes an alternative
 * short name, if applicable, that can be used instead of the full path both in
 * the feature type and backend Elasticsearch queries.
 *
 */
public class ElasticAttribute implements Serializable, Comparable<ElasticAttribute> {

    public enum ElasticGeometryType {
        GEO_POINT,
        GEO_SHAPE
    }

    private static Pattern beginLetters = Pattern.compile("^[A-Za-z_].*");
    
    private static final long serialVersionUID = 8839579461838862328L;

    private final String name;

    private String shortName;

    private Boolean useShortName;

    private Class<?> type;

    private ElasticGeometryType geometryType;

    private Boolean use;

    private Boolean defaultGeometry;

    private Integer srid;

    private String dateFormat;

    private Boolean analyzed;

    private boolean stored;

    private boolean nested;

    private Integer order;

    private String customName;

    public ElasticAttribute(String name) {
        super();
        this.name = name;
        this.use = true;
        this.defaultGeometry = false;
        this.useShortName = false;
        this.stored = false;
        this.nested = false;
    }

    public ElasticAttribute(ElasticAttribute other) {
        this.name = other.name;
        this.shortName = other.shortName;
        this.type = other.type;
        this.use = other.use;
        this.defaultGeometry = other.defaultGeometry;
        this.srid = other.srid;
        this.dateFormat = other.dateFormat;
        this.useShortName = other.useShortName;
        this.geometryType = other.geometryType;
        this.analyzed = other.analyzed;
        this.stored = other.stored;
        this.nested = other.nested;
        this.order = other.order;
        this.customName = other.customName;        
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Boolean getUseShortName() {
        return useShortName;
    }

    public void setUseShortName(Boolean useShortName) {
        this.useShortName = useShortName;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public ElasticGeometryType getGeometryType() {
        return geometryType;
    }

    public void setGeometryType(ElasticGeometryType geometryType) {
        this.geometryType = geometryType;
    }

    public Boolean isUse() {
        return use;
    }

    public void setUse(Boolean use) {
        this.use = use;
    }

    public Boolean isDefaultGeometry() {
        return defaultGeometry;
    }

    public void setDefaultGeometry(Boolean defaultGeometry) {
        this.defaultGeometry = defaultGeometry;
    }

    public Integer getSrid() {
        return srid;
    }

    public void setSrid(Integer srid) {
        this.srid = srid;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public Boolean getAnalyzed() {
        return analyzed;
    }

    public void setAnalyzed(Boolean analyzed) {
        this.analyzed = analyzed;
    }

    public boolean isStored() {
        return stored;
    }

    public void setStored(boolean stored) {
        this.stored = stored;
    }

    public boolean isNested() {
        return nested;
    }

    public void setNested(boolean nested) {
        this.nested = nested;
    }
    
    public void setOrder(Integer order) {
        this.order = order;
    }
    
    public Integer getOrder() {
        return this.order;
    }
 
    public void setCustomName(String name) {
        this.customName = normalizeName(name);
    }
    
    public String getCustomName() {
        return this.customName;
    }
    
    public String getDisplayName() {
        final String displayName;
        if (useShortName) {
            displayName = shortName;
        } else {
            displayName = name;
        }
        return displayName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, use, defaultGeometry, srid, dateFormat,
                useShortName, geometryType, analyzed, stored, nested, order, customName);
    }

    @Override
    public boolean equals(Object obj) {
        boolean equal = true;
        if (obj == null || getClass() != obj.getClass()) {
            equal = false;
        } else {
            ElasticAttribute other = (ElasticAttribute) obj;
            equal &= Objects.equals(name, other.name);
            equal &= Objects.equals(type, other.type);
            equal &= Objects.equals(use, other.use);
            equal &= Objects.equals(defaultGeometry, other.defaultGeometry);
            equal &= Objects.equals(srid, other.srid);
            equal &= Objects.equals(dateFormat, other.dateFormat);
            equal &= Objects.equals(useShortName, other.useShortName);
            equal &= Objects.equals(geometryType, other.geometryType);
            equal &= Objects.equals(analyzed, other.analyzed);
            equal &= Objects.equals(stored, other.stored);
            equal &= Objects.equals(nested, other.nested);
            equal &= Objects.equals(order, other.order);
            equal &= Objects.equals(customName, other.customName);
        }
        return equal;
    }
    
    /**
     * Implement comparison logic
     * @param o is a non-null ElasticAttribute
     * @return negative for before, zero for same, positive after 
     */
    @Override
    public int compareTo(ElasticAttribute o) {
        if (this.order == null) {
            return o.order == null ? this.name.compareTo(o.name) : 1;
        }
        if (o.order == null) {
            return -1;
        }
        int i = this.order.compareTo(o.order);
        return i == 0 ? this.name.compareTo(o.name) : i;
    }  

    /**
     * Perform basic update to the given name to make it XML namespace
     * compliant.
     * 
     * @param name
     * @return Name that is XML safe
     */
    private static String normalizeName (String name) {
        
        String normalName = name;
        
        /* XML element naming rules:
         * 1. Element names must start with a letter or underscore
         * 2. Element names cannot start with the letters xml
         * 3. Element names cannot contain spaces
         */
        if (normalName.toLowerCase().startsWith("xml")) {
            normalName = "_".concat(normalName);
        } else if (! beginLetters.matcher(normalName).matches()) {
            normalName = "_".concat(normalName);
        }         
        /* Simply replace all spaces in the name with "_". */
        return normalName.replaceAll(" ", "_");
    }
}
