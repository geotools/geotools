package org.geotools.styling;

import org.geotools.util.SimpleInternationalString;
import org.opengis.util.InternationalString;

public class DescriptionImpl implements Description {
    private InternationalString title;

    private InternationalString description;

    public DescriptionImpl() {
        title = null;
        description = null;
    }

    public DescriptionImpl(String title, String description) {
        this(new SimpleInternationalString(title), new SimpleInternationalString(description));
    }

    public DescriptionImpl(InternationalString title, InternationalString description) {
        this.title = title;
        this.description = description;
    }

    /**
     * Copy constructor.
     * @param description
     */
    public DescriptionImpl(org.opengis.style.Description description) {
        this( description.getTitle(), description.getAbstract() );
    }

    public InternationalString getTitle() {
        return title;
    }

    public void setTitle(InternationalString title) {
        this.title = title;
    }

    public void setTitle(String title) {
        this.title = new SimpleInternationalString(title);
    }
    
    public InternationalString getAbstract() {
        return description;
    }

    public void setAbstract(InternationalString description) {
        this.description = description;
    }

    public void setAbstract(String title) {
        this.description = new SimpleInternationalString(title);
    }
    
    public Object accept(org.opengis.style.StyleVisitor visitor, Object extraData) {
        return null;
    }

    public void accept(StyleVisitor visitor) {
        // nothing to do
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DescriptionImpl other = (DescriptionImpl) obj;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
            return false;
        return true;
    }

    /**
     * Check the provided description return it as a DescriptionImpl
     * @param description
     * @return DescriptionImpl from the provided description
     */
    static DescriptionImpl cast(org.opengis.style.Description description) {
        if( description == null ){
            return null;
        }
        else if (description instanceof DescriptionImpl){
            return (DescriptionImpl) description;
        }
        else {
            DescriptionImpl copy = new DescriptionImpl();
            copy.setTitle( description.getTitle() );
            copy.setAbstract( description.getAbstract() );            
            return copy;
        }
    }

}