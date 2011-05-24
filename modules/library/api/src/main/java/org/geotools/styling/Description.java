package org.geotools.styling;

import org.opengis.util.InternationalString;

/**
 * 
 *
 * @source $URL: http://svn.osgeo.org/geotools/trunk/modules/library/api/src/main/java/org/geotools/styling/Description.java $
 */
public interface Description extends org.opengis.style.Description {

    /**
     * Human readable title.
     * <p>
     * @return the human readable title.
     */
    InternationalString getTitle();
    
    void setTitle( InternationalString title );
    
    /**
     * Define title using the current locale.
     * @param title
     */
    void setTitle( String title );
    
    
    /**
     * Human readable description.
     * @param description Abstract providing a summary of contents
     */
    InternationalString getAbstract();
    
    void setAbstract( InternationalString description );
    
    /**
     * Define description in the current locale.
     * 
     * @param description Abstract providing summary of contents
     */
    void setAbstract( String description );
    
    /**
     * calls the visit method of a StyleVisitor
     *
     * @param visitor the style visitor
     */
    void accept(StyleVisitor visitor);
}
