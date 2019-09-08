
package org.geotools.data.arcgisrest.schema.webservice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Index {

    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("name")
    @Expose
    private String name;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("fields")
    @Expose
    private String fields;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("isAscending")
    @Expose
    private Boolean isAscending;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("isUnique")
    @Expose
    private Boolean isUnique;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("description")
    @Expose
    private String description;

    /**
     * 
     * (Required)
     * 
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * (Required)
     * 
     */
    public String getFields() {
        return fields;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setFields(String fields) {
        this.fields = fields;
    }

    /**
     * 
     * (Required)
     * 
     */
    public Boolean getIsAscending() {
        return isAscending;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setIsAscending(Boolean isAscending) {
        this.isAscending = isAscending;
    }

    /**
     * 
     * (Required)
     * 
     */
    public Boolean getIsUnique() {
        return isUnique;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setIsUnique(Boolean isUnique) {
        this.isUnique = isUnique;
    }

    /**
     * 
     * (Required)
     * 
     */
    public String getDescription() {
        return description;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Index.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("name");
        sb.append('=');
        sb.append(((this.name == null)?"<null>":this.name));
        sb.append(',');
        sb.append("fields");
        sb.append('=');
        sb.append(((this.fields == null)?"<null>":this.fields));
        sb.append(',');
        sb.append("isAscending");
        sb.append('=');
        sb.append(((this.isAscending == null)?"<null>":this.isAscending));
        sb.append(',');
        sb.append("isUnique");
        sb.append('=');
        sb.append(((this.isUnique == null)?"<null>":this.isUnique));
        sb.append(',');
        sb.append("description");
        sb.append('=');
        sb.append(((this.description == null)?"<null>":this.description));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result* 31)+((this.name == null)? 0 :this.name.hashCode()));
        result = ((result* 31)+((this.isUnique == null)? 0 :this.isUnique.hashCode()));
        result = ((result* 31)+((this.description == null)? 0 :this.description.hashCode()));
        result = ((result* 31)+((this.fields == null)? 0 :this.fields.hashCode()));
        result = ((result* 31)+((this.isAscending == null)? 0 :this.isAscending.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Index) == false) {
            return false;
        }
        Index rhs = ((Index) other);
        return ((((((this.name == rhs.name)||((this.name!= null)&&this.name.equals(rhs.name)))&&((this.isUnique == rhs.isUnique)||((this.isUnique!= null)&&this.isUnique.equals(rhs.isUnique))))&&((this.description == rhs.description)||((this.description!= null)&&this.description.equals(rhs.description))))&&((this.fields == rhs.fields)||((this.fields!= null)&&this.fields.equals(rhs.fields))))&&((this.isAscending == rhs.isAscending)||((this.isAscending!= null)&&this.isAscending.equals(rhs.isAscending))));
    }

}
