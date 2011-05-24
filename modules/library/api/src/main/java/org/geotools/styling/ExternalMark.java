package org.geotools.styling;

import javax.swing.Icon;

import org.opengis.metadata.citation.OnLineResource;

/**
 * Specify a mark using an image files (svg, png, gif) or using mark index a true type font file.
 * <p>
 * Please note that not all render can handle all image file formats; please organize your marks
 * into a preferred order with the most specific (say SVG) followed by common formats (PNG, GIF) and ending
 * with an appropriate WellKnownName.
 *
 *
 * @source $URL$
 */
public interface ExternalMark extends org.opengis.style.ExternalMark {

    /**
     * Online resource defined by an URI.
     * <p>
     * Only one of OnlineResource or InlineContent can be supplied.
     *
     * @return OnlineResource or <code>null</code>
     */
    OnLineResource getOnlineResource();
    
    /**
     * @param resource Online resource with format defined by getFormat()
     */
    void setOnlineResource( OnLineResource resource );
    
    /**
     * Inline content.
     *
     * Only one of OnlineResource or InlineContent can be supplied.
     *
     * @return InlineContent or <code>null</code>
     */
    Icon getInlineContent();

    /**
     * Icon to use for inline content.
     * <p>
     * This is often a SwingImageIcon with a format defined by getFormat()
     * 
     * @param inline
     */
    void setInlineContent(Icon inline);

    /**
     * @deprecated use {@link #setInlineContent(Icon)}
     */
    void getInlineContent(Icon inline);
    
    /**
     * Mime type of the onlineResource/InlineContent
     * <p>
     * Common examples:
     * <ul>
     * <li>image/svg</li>
     * <li>image/png</li>
     * <li>image/gif</li>
     * </ul>
     * This information is used by a renderer to determine if it can support the
     * image format being supplied.
     * 
     * @return mime type
     */
    String getFormat();
    
    /**
     * 
     * @param mimeType Mime type of external (or internal) resource
     */
    void setFormat( String mimeType);
    
    /**
     * Returns an integer value that can used for accessing a particular
     * Font character in a TTF file or a catalog for example.
     *
     * @return integer
     */
    int getMarkIndex();
    
    /**
     * Mark index used to specify true type font character; or frame of an animated gif.
     * @param markIndex
     */
    void setMarkIndex( int markIndex );
}
