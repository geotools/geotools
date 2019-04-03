
package org.geotools.data.arcgisrest.schema.catalog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
<<<<<<< HEAD
<<<<<<< HEAD
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

=======
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS

/**
 * Project Open Data Dataset
 * <p>
 * The metadata format for all federal open data. Validates a single JSON object entry (as opposed to entire Data.json catalog).
 * 
 */
public class Dataset {

    /**
     * Metadata Context
     * <p>
     * IRI for the JSON-LD data type. This should be dcat:Dataset for each Dataset
     * 
     */
    @SerializedName("@type")
    @Expose
    private Dataset.Type type;
    /**
     * Public Access Level
     * <p>
     * The degree to which this dataset could be made publicly-available, regardless of whether it has been made available. Choices: public (Data asset is or could be made publicly available to all without restrictions), restricted public (Data asset is available under certain use restrictions), or non-public (Data asset is not available to members of the public)
     * (Required)
     * 
     */
    @SerializedName("accessLevel")
    @Expose
    private Dataset.AccessLevel accessLevel;
    /**
     * Rights
     * <p>
     * This may include information regarding access or restrictions based on privacy, security, or other policies. This should also provide an explanation for the selected "accessLevel" including instructions for how to access a restricted file, if applicable, or explanation for why a "non-public" or "restricted public" data assetis not "public," if applicable. Text, 255 characters.
     * 
     */
    @SerializedName("rights")
    @Expose
    private Object rights;
    /**
     * Frequency
     * <p>
     * Frequency with which dataset is published.
     * 
     */
    @SerializedName("accrualPeriodicity")
    @Expose
    private Object accrualPeriodicity;
    /**
     * Bureau Code
     * <p>
     * Federal agencies, combined agency and bureau code from <a href="http://www.whitehouse.gov/sites/default/files/omb/assets/a11_current_year/app_c.pdf">OMB Circular A-11, Appendix C</a> in the format of <code>015:010</code>.
     * (Required)
     * 
     */
    @SerializedName("bureauCode")
    @Expose
    private Set<String> bureauCode = new LinkedHashSet<String>();
    /**
     * Project Open Data ContactPoint vCard
     * <p>
     * A Dataset ContactPoint as a vCard object
     * (Required)
     * 
     */
    @SerializedName("contactPoint")
    @Expose
    private Vcard contactPoint;
    /**
     * Data Dictionary
     * <p>
     * URL to the data dictionary for the dataset or API. Note that documentation other than a data dictionary can be referenced using Related Documents as shown in the expanded fields.
     * 
     */
    @SerializedName("describedBy")
    @Expose
    private Object describedBy;
    /**
     * Data Dictionary Type
     * <p>
     * The machine-readable file format (IANA Media Type or MIME Type) of the distribution’s describedBy URL
     * 
     */
    @SerializedName("describedByType")
    @Expose
    private Object describedByType;
    /**
     * Data Standard
     * <p>
     * URI used to identify a standardized specification the dataset conforms to
     * 
     */
    @SerializedName("conformsTo")
    @Expose
    private Object conformsTo;
    /**
     * Data Quality
     * <p>
     * Whether the dataset meets the agency’s Information Quality Guidelines (true/false).
     * 
     */
    @SerializedName("dataQuality")
    @Expose
    private Object dataQuality;
    /**
     * Description
     * <p>
     * Human-readable description (e.g., an abstract) with sufficient detail to enable a user to quickly understand whether the asset is of interest.
     * (Required)
     * 
     */
    @SerializedName("description")
    @Expose
    private String description;
    /**
     * Distribution
     * <p>
     * A container for the array of Distribution objects
     * 
     */
    @SerializedName("distribution")
    @Expose
    private List<Distribution> distribution = new ArrayList<Distribution>();
    /**
     * Unique Identifier
     * <p>
     * A unique identifier for the dataset or API as maintained within an Agency catalog or database.
     * (Required)
     * 
     */
    @SerializedName("identifier")
    @Expose
    private String identifier;
    /**
     * Release Date
     * <p>
     * Date of formal issuance.
     * 
     */
    @SerializedName("issued")
    @Expose
    private Object issued;
    /**
     * Tags
     * <p>
     * Tags (or keywords) help users discover your dataset; please include terms that would be used by technical and non-technical users.
     * (Required)
     * 
     */
    @SerializedName("keyword")
    @Expose
    private List<String> keyword = new ArrayList<String>();
    /**
     * Homepage URL
     * <p>
     * Alternative landing page used to redirect user to a contextual, Agency-hosted “homepage” for the Dataset or API when selecting this resource from the Data.gov user interface.
     * 
     */
    @SerializedName("landingPage")
    @Expose
    private Object landingPage;
    /**
     * WebService URL (ESRI extension of the standard schema)
     * <p>
     * ESRI ArcGIS ReST API pf the datatse
     * 
     */
    @SerializedName("webService")
    @Expose
    private Object webService;
    /**
     * Language
     * <p>
     * The language of the dataset.
     * 
     */
    @SerializedName("language")
    @Expose
    private Object language;
    /**
     * License
     * <p>
     * The license dataset or API is published with. See <a href="https://project-open-data.cio.gov/open-licenses/">Open Licenses</a> for more information.
     * 
     */
    @SerializedName("license")
    @Expose
    private Object license;
    /**
     * Last Update
     * <p>
     * Most recent date on which the dataset was changed, updated or modified.
     * (Required)
     * 
     */
    @SerializedName("modified")
    @Expose
    private Object modified;
    /**
     * Primary IT Investment UII
     * <p>
     * For linking a dataset with an IT Unique Investment Identifier (UII)
     * 
     */
    @SerializedName("primaryITInvestmentUII")
    @Expose
    private Object primaryITInvestmentUII;
    /**
     * Program Code
     * <p>
     * Federal agencies, list the primary program related to this data asset, from the <a href="http://goals.performance.gov/sites/default/files/images/FederalProgramInventory_FY13_MachineReadable_091613.xls">Federal Program Inventory</a>. Use the format of <code>015:001</code>
     * (Required)
     * 
     */
    @SerializedName("programCode")
    @Expose
    private Set<String> programCode = new LinkedHashSet<String>();
    /**
     * Project Open Data Organization
     * <p>
     * A Dataset Publisher Organization as a foaf:Agent object
     * (Required)
     * 
     */
    @SerializedName("publisher")
    @Expose
    private Organization publisher;
    /**
     * Related Documents
     * <p>
     * Related documents such as technical information about a dataset, developer documentation, etc.
     * 
     */
    @SerializedName("references")
    @Expose
    private Object references;
    /**
     * Spatial
     * <p>
     * The range of spatial applicability of a dataset. Could include a spatial region like a bounding box or a named place.
     * 
     */
    @SerializedName("spatial")
    @Expose
    private Object spatial;
    /**
     * System of Records
     * <p>
     * If the systems is designated as a system of records under the Privacy Act of 1974, provide the URL to the System of Records Notice related to this dataset.
     * 
     */
    @SerializedName("systemOfRecords")
    @Expose
    private Object systemOfRecords;
    /**
     * Temporal
     * <p>
     * The range of temporal applicability of a dataset (i.e., a start and end date of applicability for the data).
     * 
     */
    @SerializedName("temporal")
    @Expose
    private Object temporal;
    /**
     * Collection
     * <p>
     * The collection of which the dataset is a subset
     * 
     */
    @SerializedName("isPartOf")
    @Expose
    private Object isPartOf;
    /**
     * Category
     * <p>
     * Main thematic category of the dataset.
     * 
     */
    @SerializedName("theme")
    @Expose
    private Object theme;
    /**
     * Title
     * <p>
     * Human-readable name of the asset. Should be in plain English and include sufficient detail to facilitate search and discovery.
     * (Required)
     * 
     */
    @SerializedName("title")
    @Expose
    private String title;

    /**
     * Metadata Context
<<<<<<< HEAD
<<<<<<< HEAD
     * <p>
     * IRI for the JSON-LD data type. This should be dcat:Dataset for each Dataset
     * 
=======
     *
     * <p>IRI for the JSON-LD data type. This should be dcat:Dataset for each Dataset
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
     * <p>
     * IRI for the JSON-LD data type. This should be dcat:Dataset for each Dataset
     * 
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    public Dataset.Type getType() {
        return type;
    }

    /**
     * Metadata Context
<<<<<<< HEAD
<<<<<<< HEAD
     * <p>
     * IRI for the JSON-LD data type. This should be dcat:Dataset for each Dataset
     * 
=======
     *
     * <p>IRI for the JSON-LD data type. This should be dcat:Dataset for each Dataset
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
     * <p>
     * IRI for the JSON-LD data type. This should be dcat:Dataset for each Dataset
     * 
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    public void setType(Dataset.Type type) {
        this.type = type;
    }

    /**
     * Public Access Level
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     * <p>
     * The degree to which this dataset could be made publicly-available, regardless of whether it has been made available. Choices: public (Data asset is or could be made publicly available to all without restrictions), restricted public (Data asset is available under certain use restrictions), or non-public (Data asset is not available to members of the public)
     * (Required)
     * 
<<<<<<< HEAD
=======
     *
     * <p>The degree to which this dataset could be made publicly-available, regardless of whether
     * it has been made available. Choices: public (Data asset is or could be made publicly
     * available to all without restrictions), restricted public (Data asset is available under
     * certain use restrictions), or non-public (Data asset is not available to members of the
     * public) (Required)
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    public Dataset.AccessLevel getAccessLevel() {
        return accessLevel;
    }

    /**
     * Public Access Level
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     * <p>
     * The degree to which this dataset could be made publicly-available, regardless of whether it has been made available. Choices: public (Data asset is or could be made publicly available to all without restrictions), restricted public (Data asset is available under certain use restrictions), or non-public (Data asset is not available to members of the public)
     * (Required)
     * 
<<<<<<< HEAD
=======
     *
     * <p>The degree to which this dataset could be made publicly-available, regardless of whether
     * it has been made available. Choices: public (Data asset is or could be made publicly
     * available to all without restrictions), restricted public (Data asset is available under
     * certain use restrictions), or non-public (Data asset is not available to members of the
     * public) (Required)
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    public void setAccessLevel(Dataset.AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
    }

    /**
     * Rights
<<<<<<< HEAD
<<<<<<< HEAD
     * <p>
     * This may include information regarding access or restrictions based on privacy, security, or other policies. This should also provide an explanation for the selected "accessLevel" including instructions for how to access a restricted file, if applicable, or explanation for why a "non-public" or "restricted public" data assetis not "public," if applicable. Text, 255 characters.
     * 
=======
     *
     * <p>This may include information regarding access or restrictions based on privacy, security,
     * or other policies. This should also provide an explanation for the selected "accessLevel"
     * including instructions for how to access a restricted file, if applicable, or explanation for
     * why a "non-public" or "restricted public" data assetis not "public," if applicable. Text, 255
     * characters.
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
     * <p>
     * This may include information regarding access or restrictions based on privacy, security, or other policies. This should also provide an explanation for the selected "accessLevel" including instructions for how to access a restricted file, if applicable, or explanation for why a "non-public" or "restricted public" data assetis not "public," if applicable. Text, 255 characters.
     * 
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    public Object getRights() {
        return rights;
    }

    /**
     * Rights
<<<<<<< HEAD
<<<<<<< HEAD
     * <p>
     * This may include information regarding access or restrictions based on privacy, security, or other policies. This should also provide an explanation for the selected "accessLevel" including instructions for how to access a restricted file, if applicable, or explanation for why a "non-public" or "restricted public" data assetis not "public," if applicable. Text, 255 characters.
     * 
=======
     *
     * <p>This may include information regarding access or restrictions based on privacy, security,
     * or other policies. This should also provide an explanation for the selected "accessLevel"
     * including instructions for how to access a restricted file, if applicable, or explanation for
     * why a "non-public" or "restricted public" data assetis not "public," if applicable. Text, 255
     * characters.
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
     * <p>
     * This may include information regarding access or restrictions based on privacy, security, or other policies. This should also provide an explanation for the selected "accessLevel" including instructions for how to access a restricted file, if applicable, or explanation for why a "non-public" or "restricted public" data assetis not "public," if applicable. Text, 255 characters.
     * 
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    public void setRights(Object rights) {
        this.rights = rights;
    }

    /**
     * Frequency
<<<<<<< HEAD
<<<<<<< HEAD
     * <p>
     * Frequency with which dataset is published.
     * 
=======
     *
     * <p>Frequency with which dataset is published.
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
     * <p>
     * Frequency with which dataset is published.
     * 
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    public Object getAccrualPeriodicity() {
        return accrualPeriodicity;
    }

    /**
     * Frequency
<<<<<<< HEAD
<<<<<<< HEAD
     * <p>
     * Frequency with which dataset is published.
     * 
=======
     *
     * <p>Frequency with which dataset is published.
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
     * <p>
     * Frequency with which dataset is published.
     * 
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    public void setAccrualPeriodicity(Object accrualPeriodicity) {
        this.accrualPeriodicity = accrualPeriodicity;
    }

    /**
     * Bureau Code
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     * <p>
     * Federal agencies, combined agency and bureau code from <a href="http://www.whitehouse.gov/sites/default/files/omb/assets/a11_current_year/app_c.pdf">OMB Circular A-11, Appendix C</a> in the format of <code>015:010</code>.
     * (Required)
     * 
<<<<<<< HEAD
=======
     *
     * <p>Federal agencies, combined agency and bureau code from <a
     * href="http://www.whitehouse.gov/sites/default/files/omb/assets/a11_current_year/app_c.pdf">OMB
     * Circular A-11, Appendix C</a> in the format of <code>015:010</code>. (Required)
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    public Set<String> getBureauCode() {
        return bureauCode;
    }

    /**
     * Bureau Code
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     * <p>
     * Federal agencies, combined agency and bureau code from <a href="http://www.whitehouse.gov/sites/default/files/omb/assets/a11_current_year/app_c.pdf">OMB Circular A-11, Appendix C</a> in the format of <code>015:010</code>.
     * (Required)
     * 
<<<<<<< HEAD
=======
     *
     * <p>Federal agencies, combined agency and bureau code from <a
     * href="http://www.whitehouse.gov/sites/default/files/omb/assets/a11_current_year/app_c.pdf">OMB
     * Circular A-11, Appendix C</a> in the format of <code>015:010</code>. (Required)
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    public void setBureauCode(Set<String> bureauCode) {
        this.bureauCode = bureauCode;
    }

    /**
     * Project Open Data ContactPoint vCard
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     * <p>
     * A Dataset ContactPoint as a vCard object
     * (Required)
     * 
<<<<<<< HEAD
=======
     *
     * <p>A Dataset ContactPoint as a vCard object (Required)
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    public Vcard getContactPoint() {
        return contactPoint;
    }

    /**
     * Project Open Data ContactPoint vCard
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     * <p>
     * A Dataset ContactPoint as a vCard object
     * (Required)
     * 
<<<<<<< HEAD
=======
     *
     * <p>A Dataset ContactPoint as a vCard object (Required)
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    public void setContactPoint(Vcard contactPoint) {
        this.contactPoint = contactPoint;
    }

    /**
     * Data Dictionary
<<<<<<< HEAD
<<<<<<< HEAD
     * <p>
     * URL to the data dictionary for the dataset or API. Note that documentation other than a data dictionary can be referenced using Related Documents as shown in the expanded fields.
     * 
=======
     *
     * <p>URL to the data dictionary for the dataset or API. Note that documentation other than a
     * data dictionary can be referenced using Related Documents as shown in the expanded fields.
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
     * <p>
     * URL to the data dictionary for the dataset or API. Note that documentation other than a data dictionary can be referenced using Related Documents as shown in the expanded fields.
     * 
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    public Object getDescribedBy() {
        return describedBy;
    }

    /**
     * Data Dictionary
<<<<<<< HEAD
<<<<<<< HEAD
     * <p>
     * URL to the data dictionary for the dataset or API. Note that documentation other than a data dictionary can be referenced using Related Documents as shown in the expanded fields.
     * 
=======
     *
     * <p>URL to the data dictionary for the dataset or API. Note that documentation other than a
     * data dictionary can be referenced using Related Documents as shown in the expanded fields.
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
     * <p>
     * URL to the data dictionary for the dataset or API. Note that documentation other than a data dictionary can be referenced using Related Documents as shown in the expanded fields.
     * 
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    public void setDescribedBy(Object describedBy) {
        this.describedBy = describedBy;
    }

    /**
     * Data Dictionary Type
<<<<<<< HEAD
<<<<<<< HEAD
     * <p>
     * The machine-readable file format (IANA Media Type or MIME Type) of the distribution’s describedBy URL
     * 
=======
     *
     * <p>The machine-readable file format (IANA Media Type or MIME Type) of the distribution’s
     * describedBy URL
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
     * <p>
     * The machine-readable file format (IANA Media Type or MIME Type) of the distribution’s describedBy URL
     * 
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    public Object getDescribedByType() {
        return describedByType;
    }

    /**
     * Data Dictionary Type
<<<<<<< HEAD
<<<<<<< HEAD
     * <p>
     * The machine-readable file format (IANA Media Type or MIME Type) of the distribution’s describedBy URL
     * 
=======
     *
     * <p>The machine-readable file format (IANA Media Type or MIME Type) of the distribution’s
     * describedBy URL
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
     * <p>
     * The machine-readable file format (IANA Media Type or MIME Type) of the distribution’s describedBy URL
     * 
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    public void setDescribedByType(Object describedByType) {
        this.describedByType = describedByType;
    }

    /**
     * Data Standard
<<<<<<< HEAD
<<<<<<< HEAD
     * <p>
     * URI used to identify a standardized specification the dataset conforms to
     * 
=======
     *
     * <p>URI used to identify a standardized specification the dataset conforms to
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
     * <p>
     * URI used to identify a standardized specification the dataset conforms to
     * 
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    public Object getConformsTo() {
        return conformsTo;
    }

    /**
     * Data Standard
<<<<<<< HEAD
<<<<<<< HEAD
     * <p>
     * URI used to identify a standardized specification the dataset conforms to
     * 
=======
     *
     * <p>URI used to identify a standardized specification the dataset conforms to
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
     * <p>
     * URI used to identify a standardized specification the dataset conforms to
     * 
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    public void setConformsTo(Object conformsTo) {
        this.conformsTo = conformsTo;
    }

    /**
     * Data Quality
<<<<<<< HEAD
<<<<<<< HEAD
     * <p>
     * Whether the dataset meets the agency’s Information Quality Guidelines (true/false).
     * 
=======
     *
     * <p>Whether the dataset meets the agency’s Information Quality Guidelines (true/false).
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
     * <p>
     * Whether the dataset meets the agency’s Information Quality Guidelines (true/false).
     * 
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    public Object getDataQuality() {
        return dataQuality;
    }

    /**
     * Data Quality
<<<<<<< HEAD
<<<<<<< HEAD
     * <p>
     * Whether the dataset meets the agency’s Information Quality Guidelines (true/false).
     * 
=======
     *
     * <p>Whether the dataset meets the agency’s Information Quality Guidelines (true/false).
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
     * <p>
     * Whether the dataset meets the agency’s Information Quality Guidelines (true/false).
     * 
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    public void setDataQuality(Object dataQuality) {
        this.dataQuality = dataQuality;
    }

    /**
     * Description
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     * <p>
     * Human-readable description (e.g., an abstract) with sufficient detail to enable a user to quickly understand whether the asset is of interest.
     * (Required)
     * 
<<<<<<< HEAD
=======
     *
     * <p>Human-readable description (e.g., an abstract) with sufficient detail to enable a user to
     * quickly understand whether the asset is of interest. (Required)
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    public String getDescription() {
        return description;
    }

    /**
     * Description
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     * <p>
     * Human-readable description (e.g., an abstract) with sufficient detail to enable a user to quickly understand whether the asset is of interest.
     * (Required)
     * 
<<<<<<< HEAD
=======
     *
     * <p>Human-readable description (e.g., an abstract) with sufficient detail to enable a user to
     * quickly understand whether the asset is of interest. (Required)
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Distribution
<<<<<<< HEAD
<<<<<<< HEAD
     * <p>
     * A container for the array of Distribution objects
     * 
=======
     *
     * <p>A container for the array of Distribution objects
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
     * <p>
     * A container for the array of Distribution objects
     * 
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    public List<Distribution> getDistribution() {
        return distribution;
    }

    /**
     * Distribution
<<<<<<< HEAD
<<<<<<< HEAD
     * <p>
     * A container for the array of Distribution objects
     * 
=======
     *
     * <p>A container for the array of Distribution objects
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
     * <p>
     * A container for the array of Distribution objects
     * 
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    public void setDistribution(List<Distribution> distribution) {
        this.distribution = distribution;
    }

    /**
     * Unique Identifier
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     * <p>
     * A unique identifier for the dataset or API as maintained within an Agency catalog or database.
     * (Required)
     * 
<<<<<<< HEAD
=======
     *
     * <p>A unique identifier for the dataset or API as maintained within an Agency catalog or
     * database. (Required)
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Unique Identifier
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     * <p>
     * A unique identifier for the dataset or API as maintained within an Agency catalog or database.
     * (Required)
     * 
<<<<<<< HEAD
=======
     *
     * <p>A unique identifier for the dataset or API as maintained within an Agency catalog or
     * database. (Required)
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    /**
     * Release Date
<<<<<<< HEAD
<<<<<<< HEAD
     * <p>
     * Date of formal issuance.
     * 
=======
     *
     * <p>Date of formal issuance.
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
     * <p>
     * Date of formal issuance.
     * 
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    public Object getIssued() {
        return issued;
    }

    /**
     * Release Date
<<<<<<< HEAD
<<<<<<< HEAD
     * <p>
     * Date of formal issuance.
     * 
=======
     *
     * <p>Date of formal issuance.
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
     * <p>
     * Date of formal issuance.
     * 
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    public void setIssued(Object issued) {
        this.issued = issued;
    }

    /**
     * Tags
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     * <p>
     * Tags (or keywords) help users discover your dataset; please include terms that would be used by technical and non-technical users.
     * (Required)
     * 
<<<<<<< HEAD
=======
     *
     * <p>Tags (or keywords) help users discover your dataset; please include terms that would be
     * used by technical and non-technical users. (Required)
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    public List<String> getKeyword() {
        return keyword;
    }

    /**
     * Tags
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     * <p>
     * Tags (or keywords) help users discover your dataset; please include terms that would be used by technical and non-technical users.
     * (Required)
     * 
<<<<<<< HEAD
=======
     *
     * <p>Tags (or keywords) help users discover your dataset; please include terms that would be
     * used by technical and non-technical users. (Required)
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    public void setKeyword(List<String> keyword) {
        this.keyword = keyword;
    }

    /**
     * Homepage URL
<<<<<<< HEAD
<<<<<<< HEAD
     * <p>
     * Alternative landing page used to redirect user to a contextual, Agency-hosted “homepage” for the Dataset or API when selecting this resource from the Data.gov user interface.
     * 
=======
     *
     * <p>Alternative landing page used to redirect user to a contextual, Agency-hosted “homepage”
     * for the Dataset or API when selecting this resource from the Data.gov user interface.
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
     * <p>
     * Alternative landing page used to redirect user to a contextual, Agency-hosted “homepage” for the Dataset or API when selecting this resource from the Data.gov user interface.
     * 
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    public Object getLandingPage() {
        return landingPage;
    }

    /**
     * Homepage URL
<<<<<<< HEAD
<<<<<<< HEAD
     * <p>
     * Alternative landing page used to redirect user to a contextual, Agency-hosted “homepage” for the Dataset or API when selecting this resource from the Data.gov user interface.
     * 
=======
     *
     * <p>Alternative landing page used to redirect user to a contextual, Agency-hosted “homepage”
     * for the Dataset or API when selecting this resource from the Data.gov user interface.
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
     * <p>
     * Alternative landing page used to redirect user to a contextual, Agency-hosted “homepage” for the Dataset or API when selecting this resource from the Data.gov user interface.
     * 
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    public void setLandingPage(Object landingPage) {
        this.landingPage = landingPage;
    }

    /**
     * WebService URL (ESRI extension of the standard schema)
<<<<<<< HEAD
<<<<<<< HEAD
     * <p>
     * ESRI ArcGIS ReST API pf the datatse
     * 
=======
     *
     * <p>ESRI ArcGIS ReST API pf the datatse
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
     * <p>
     * ESRI ArcGIS ReST API pf the datatse
     * 
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    public Object getWebService() {
        return webService;
    }

    /**
     * WebService URL (ESRI extension of the standard schema)
<<<<<<< HEAD
<<<<<<< HEAD
     * <p>
     * ESRI ArcGIS ReST API pf the datatse
     * 
=======
     *
     * <p>ESRI ArcGIS ReST API pf the datatse
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
     * <p>
     * ESRI ArcGIS ReST API pf the datatse
     * 
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    public void setWebService(Object webService) {
        this.webService = webService;
    }

    /**
     * Language
<<<<<<< HEAD
<<<<<<< HEAD
     * <p>
     * The language of the dataset.
     * 
=======
     *
     * <p>The language of the dataset.
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
     * <p>
     * The language of the dataset.
     * 
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    public Object getLanguage() {
        return language;
    }

    /**
     * Language
<<<<<<< HEAD
<<<<<<< HEAD
     * <p>
     * The language of the dataset.
     * 
=======
     *
     * <p>The language of the dataset.
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
     * <p>
     * The language of the dataset.
     * 
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    public void setLanguage(Object language) {
        this.language = language;
    }

    /**
     * License
<<<<<<< HEAD
<<<<<<< HEAD
     * <p>
     * The license dataset or API is published with. See <a href="https://project-open-data.cio.gov/open-licenses/">Open Licenses</a> for more information.
     * 
=======
     *
     * <p>The license dataset or API is published with. See <a
     * href="https://project-open-data.cio.gov/open-licenses/">Open Licenses</a> for more
     * information.
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
     * <p>
     * The license dataset or API is published with. See <a href="https://project-open-data.cio.gov/open-licenses/">Open Licenses</a> for more information.
     * 
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    public Object getLicense() {
        return license;
    }

    /**
     * License
<<<<<<< HEAD
<<<<<<< HEAD
     * <p>
     * The license dataset or API is published with. See <a href="https://project-open-data.cio.gov/open-licenses/">Open Licenses</a> for more information.
     * 
=======
     *
     * <p>The license dataset or API is published with. See <a
     * href="https://project-open-data.cio.gov/open-licenses/">Open Licenses</a> for more
     * information.
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
     * <p>
     * The license dataset or API is published with. See <a href="https://project-open-data.cio.gov/open-licenses/">Open Licenses</a> for more information.
     * 
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    public void setLicense(Object license) {
        this.license = license;
    }

    /**
     * Last Update
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     * <p>
     * Most recent date on which the dataset was changed, updated or modified.
     * (Required)
     * 
<<<<<<< HEAD
=======
     *
     * <p>Most recent date on which the dataset was changed, updated or modified. (Required)
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    public Object getModified() {
        return modified;
    }

    /**
     * Last Update
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     * <p>
     * Most recent date on which the dataset was changed, updated or modified.
     * (Required)
     * 
<<<<<<< HEAD
=======
     *
     * <p>Most recent date on which the dataset was changed, updated or modified. (Required)
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    public void setModified(Object modified) {
        this.modified = modified;
    }

    /**
     * Primary IT Investment UII
<<<<<<< HEAD
<<<<<<< HEAD
     * <p>
     * For linking a dataset with an IT Unique Investment Identifier (UII)
     * 
=======
     *
     * <p>For linking a dataset with an IT Unique Investment Identifier (UII)
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
     * <p>
     * For linking a dataset with an IT Unique Investment Identifier (UII)
     * 
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    public Object getPrimaryITInvestmentUII() {
        return primaryITInvestmentUII;
    }

    /**
     * Primary IT Investment UII
<<<<<<< HEAD
<<<<<<< HEAD
     * <p>
     * For linking a dataset with an IT Unique Investment Identifier (UII)
     * 
=======
     *
     * <p>For linking a dataset with an IT Unique Investment Identifier (UII)
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
     * <p>
     * For linking a dataset with an IT Unique Investment Identifier (UII)
     * 
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    public void setPrimaryITInvestmentUII(Object primaryITInvestmentUII) {
        this.primaryITInvestmentUII = primaryITInvestmentUII;
    }

    /**
     * Program Code
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     * <p>
     * Federal agencies, list the primary program related to this data asset, from the <a href="http://goals.performance.gov/sites/default/files/images/FederalProgramInventory_FY13_MachineReadable_091613.xls">Federal Program Inventory</a>. Use the format of <code>015:001</code>
     * (Required)
     * 
<<<<<<< HEAD
=======
     *
     * <p>Federal agencies, list the primary program related to this data asset, from the <a
     * href="http://goals.performance.gov/sites/default/files/images/FederalProgramInventory_FY13_MachineReadable_091613.xls">Federal
     * Program Inventory</a>. Use the format of <code>015:001</code> (Required)
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    public Set<String> getProgramCode() {
        return programCode;
    }

    /**
     * Program Code
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     * <p>
     * Federal agencies, list the primary program related to this data asset, from the <a href="http://goals.performance.gov/sites/default/files/images/FederalProgramInventory_FY13_MachineReadable_091613.xls">Federal Program Inventory</a>. Use the format of <code>015:001</code>
     * (Required)
     * 
<<<<<<< HEAD
=======
     *
     * <p>Federal agencies, list the primary program related to this data asset, from the <a
     * href="http://goals.performance.gov/sites/default/files/images/FederalProgramInventory_FY13_MachineReadable_091613.xls">Federal
     * Program Inventory</a>. Use the format of <code>015:001</code> (Required)
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    public void setProgramCode(Set<String> programCode) {
        this.programCode = programCode;
    }

    /**
     * Project Open Data Organization
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     * <p>
     * A Dataset Publisher Organization as a foaf:Agent object
     * (Required)
     * 
<<<<<<< HEAD
=======
     *
     * <p>A Dataset Publisher Organization as a foaf:Agent object (Required)
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    public Organization getPublisher() {
        return publisher;
    }

    /**
     * Project Open Data Organization
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     * <p>
     * A Dataset Publisher Organization as a foaf:Agent object
     * (Required)
     * 
<<<<<<< HEAD
=======
     *
     * <p>A Dataset Publisher Organization as a foaf:Agent object (Required)
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    public void setPublisher(Organization publisher) {
        this.publisher = publisher;
    }

    /**
     * Related Documents
<<<<<<< HEAD
<<<<<<< HEAD
     * <p>
     * Related documents such as technical information about a dataset, developer documentation, etc.
     * 
=======
     *
     * <p>Related documents such as technical information about a dataset, developer documentation,
     * etc.
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
     * <p>
     * Related documents such as technical information about a dataset, developer documentation, etc.
     * 
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    public Object getReferences() {
        return references;
    }

    /**
     * Related Documents
<<<<<<< HEAD
<<<<<<< HEAD
     * <p>
     * Related documents such as technical information about a dataset, developer documentation, etc.
     * 
=======
     *
     * <p>Related documents such as technical information about a dataset, developer documentation,
     * etc.
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
     * <p>
     * Related documents such as technical information about a dataset, developer documentation, etc.
     * 
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    public void setReferences(Object references) {
        this.references = references;
    }

    /**
     * Spatial
<<<<<<< HEAD
<<<<<<< HEAD
     * <p>
     * The range of spatial applicability of a dataset. Could include a spatial region like a bounding box or a named place.
     * 
=======
     *
     * <p>The range of spatial applicability of a dataset. Could include a spatial region like a
     * bounding box or a named place.
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
     * <p>
     * The range of spatial applicability of a dataset. Could include a spatial region like a bounding box or a named place.
     * 
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    public Object getSpatial() {
        return spatial;
    }

    /**
     * Spatial
<<<<<<< HEAD
<<<<<<< HEAD
     * <p>
     * The range of spatial applicability of a dataset. Could include a spatial region like a bounding box or a named place.
     * 
=======
     *
     * <p>The range of spatial applicability of a dataset. Could include a spatial region like a
     * bounding box or a named place.
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
     * <p>
     * The range of spatial applicability of a dataset. Could include a spatial region like a bounding box or a named place.
     * 
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    public void setSpatial(Object spatial) {
        this.spatial = spatial;
    }

    /**
     * System of Records
<<<<<<< HEAD
<<<<<<< HEAD
     * <p>
     * If the systems is designated as a system of records under the Privacy Act of 1974, provide the URL to the System of Records Notice related to this dataset.
     * 
=======
     *
     * <p>If the systems is designated as a system of records under the Privacy Act of 1974, provide
     * the URL to the System of Records Notice related to this dataset.
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
     * <p>
     * If the systems is designated as a system of records under the Privacy Act of 1974, provide the URL to the System of Records Notice related to this dataset.
     * 
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    public Object getSystemOfRecords() {
        return systemOfRecords;
    }

    /**
     * System of Records
<<<<<<< HEAD
<<<<<<< HEAD
     * <p>
     * If the systems is designated as a system of records under the Privacy Act of 1974, provide the URL to the System of Records Notice related to this dataset.
     * 
=======
     *
     * <p>If the systems is designated as a system of records under the Privacy Act of 1974, provide
     * the URL to the System of Records Notice related to this dataset.
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
     * <p>
     * If the systems is designated as a system of records under the Privacy Act of 1974, provide the URL to the System of Records Notice related to this dataset.
     * 
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    public void setSystemOfRecords(Object systemOfRecords) {
        this.systemOfRecords = systemOfRecords;
    }

    /**
     * Temporal
<<<<<<< HEAD
<<<<<<< HEAD
     * <p>
     * The range of temporal applicability of a dataset (i.e., a start and end date of applicability for the data).
     * 
=======
     *
     * <p>The range of temporal applicability of a dataset (i.e., a start and end date of
     * applicability for the data).
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
     * <p>
     * The range of temporal applicability of a dataset (i.e., a start and end date of applicability for the data).
     * 
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    public Object getTemporal() {
        return temporal;
    }

    /**
     * Temporal
<<<<<<< HEAD
<<<<<<< HEAD
     * <p>
     * The range of temporal applicability of a dataset (i.e., a start and end date of applicability for the data).
     * 
=======
     *
     * <p>The range of temporal applicability of a dataset (i.e., a start and end date of
     * applicability for the data).
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
     * <p>
     * The range of temporal applicability of a dataset (i.e., a start and end date of applicability for the data).
     * 
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    public void setTemporal(Object temporal) {
        this.temporal = temporal;
    }

    /**
     * Collection
<<<<<<< HEAD
<<<<<<< HEAD
     * <p>
     * The collection of which the dataset is a subset
     * 
=======
     *
     * <p>The collection of which the dataset is a subset
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
     * <p>
     * The collection of which the dataset is a subset
     * 
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    public Object getIsPartOf() {
        return isPartOf;
    }

    /**
     * Collection
<<<<<<< HEAD
<<<<<<< HEAD
     * <p>
     * The collection of which the dataset is a subset
     * 
=======
     *
     * <p>The collection of which the dataset is a subset
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
     * <p>
     * The collection of which the dataset is a subset
     * 
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    public void setIsPartOf(Object isPartOf) {
        this.isPartOf = isPartOf;
    }

    /**
     * Category
<<<<<<< HEAD
<<<<<<< HEAD
     * <p>
     * Main thematic category of the dataset.
     * 
=======
     *
     * <p>Main thematic category of the dataset.
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
     * <p>
     * Main thematic category of the dataset.
     * 
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    public Object getTheme() {
        return theme;
    }

    /**
     * Category
<<<<<<< HEAD
<<<<<<< HEAD
     * <p>
     * Main thematic category of the dataset.
     * 
=======
     *
     * <p>Main thematic category of the dataset.
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
     * <p>
     * Main thematic category of the dataset.
     * 
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    public void setTheme(Object theme) {
        this.theme = theme;
    }

    /**
     * Title
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     * <p>
     * Human-readable name of the asset. Should be in plain English and include sufficient detail to facilitate search and discovery.
     * (Required)
     * 
<<<<<<< HEAD
=======
     *
     * <p>Human-readable name of the asset. Should be in plain English and include sufficient detail
     * to facilitate search and discovery. (Required)
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    public String getTitle() {
        return title;
    }

    /**
     * Title
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     * <p>
     * Human-readable name of the asset. Should be in plain English and include sufficient detail to facilitate search and discovery.
     * (Required)
     * 
<<<<<<< HEAD
=======
     *
     * <p>Human-readable name of the asset. Should be in plain English and include sufficient detail
     * to facilitate search and discovery. (Required)
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
     */
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
<<<<<<< HEAD
<<<<<<< HEAD
        sb.append(Dataset.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("type");
        sb.append('=');
        sb.append(((this.type == null)?"<null>":this.type));
        sb.append(',');
        sb.append("accessLevel");
        sb.append('=');
        sb.append(((this.accessLevel == null)?"<null>":this.accessLevel));
        sb.append(',');
        sb.append("rights");
        sb.append('=');
        sb.append(((this.rights == null)?"<null>":this.rights));
        sb.append(',');
        sb.append("accrualPeriodicity");
        sb.append('=');
        sb.append(((this.accrualPeriodicity == null)?"<null>":this.accrualPeriodicity));
        sb.append(',');
        sb.append("bureauCode");
        sb.append('=');
        sb.append(((this.bureauCode == null)?"<null>":this.bureauCode));
        sb.append(',');
        sb.append("contactPoint");
        sb.append('=');
        sb.append(((this.contactPoint == null)?"<null>":this.contactPoint));
        sb.append(',');
        sb.append("describedBy");
        sb.append('=');
        sb.append(((this.describedBy == null)?"<null>":this.describedBy));
        sb.append(',');
        sb.append("describedByType");
        sb.append('=');
        sb.append(((this.describedByType == null)?"<null>":this.describedByType));
        sb.append(',');
        sb.append("conformsTo");
        sb.append('=');
        sb.append(((this.conformsTo == null)?"<null>":this.conformsTo));
        sb.append(',');
        sb.append("dataQuality");
        sb.append('=');
        sb.append(((this.dataQuality == null)?"<null>":this.dataQuality));
        sb.append(',');
        sb.append("description");
        sb.append('=');
        sb.append(((this.description == null)?"<null>":this.description));
        sb.append(',');
        sb.append("distribution");
        sb.append('=');
        sb.append(((this.distribution == null)?"<null>":this.distribution));
        sb.append(',');
        sb.append("identifier");
        sb.append('=');
        sb.append(((this.identifier == null)?"<null>":this.identifier));
        sb.append(',');
        sb.append("issued");
        sb.append('=');
        sb.append(((this.issued == null)?"<null>":this.issued));
        sb.append(',');
        sb.append("keyword");
        sb.append('=');
        sb.append(((this.keyword == null)?"<null>":this.keyword));
        sb.append(',');
        sb.append("landingPage");
        sb.append('=');
        sb.append(((this.landingPage == null)?"<null>":this.landingPage));
        sb.append(',');
        sb.append("webService");
        sb.append('=');
        sb.append(((this.webService == null)?"<null>":this.webService));
        sb.append(',');
        sb.append("language");
        sb.append('=');
        sb.append(((this.language == null)?"<null>":this.language));
        sb.append(',');
        sb.append("license");
        sb.append('=');
        sb.append(((this.license == null)?"<null>":this.license));
        sb.append(',');
        sb.append("modified");
        sb.append('=');
        sb.append(((this.modified == null)?"<null>":this.modified));
        sb.append(',');
        sb.append("primaryITInvestmentUII");
        sb.append('=');
        sb.append(((this.primaryITInvestmentUII == null)?"<null>":this.primaryITInvestmentUII));
        sb.append(',');
        sb.append("programCode");
        sb.append('=');
        sb.append(((this.programCode == null)?"<null>":this.programCode));
        sb.append(',');
        sb.append("publisher");
        sb.append('=');
        sb.append(((this.publisher == null)?"<null>":this.publisher));
        sb.append(',');
        sb.append("references");
        sb.append('=');
        sb.append(((this.references == null)?"<null>":this.references));
        sb.append(',');
        sb.append("spatial");
        sb.append('=');
        sb.append(((this.spatial == null)?"<null>":this.spatial));
        sb.append(',');
        sb.append("systemOfRecords");
        sb.append('=');
        sb.append(((this.systemOfRecords == null)?"<null>":this.systemOfRecords));
        sb.append(',');
        sb.append("temporal");
        sb.append('=');
        sb.append(((this.temporal == null)?"<null>":this.temporal));
        sb.append(',');
        sb.append("isPartOf");
        sb.append('=');
        sb.append(((this.isPartOf == null)?"<null>":this.isPartOf));
        sb.append(',');
        sb.append("theme");
        sb.append('=');
        sb.append(((this.theme == null)?"<null>":this.theme));
        sb.append(',');
        sb.append("title");
        sb.append('=');
        sb.append(((this.title == null)?"<null>":this.title));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
=======
        sb.append(Dataset.class.getName())
                .append('@')
                .append(Integer.toHexString(System.identityHashCode(this)))
                .append('[');
=======
        sb.append(Dataset.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
        sb.append("type");
        sb.append('=');
        sb.append(((this.type == null)?"<null>":this.type));
        sb.append(',');
        sb.append("accessLevel");
        sb.append('=');
        sb.append(((this.accessLevel == null)?"<null>":this.accessLevel));
        sb.append(',');
        sb.append("rights");
        sb.append('=');
        sb.append(((this.rights == null)?"<null>":this.rights));
        sb.append(',');
        sb.append("accrualPeriodicity");
        sb.append('=');
        sb.append(((this.accrualPeriodicity == null)?"<null>":this.accrualPeriodicity));
        sb.append(',');
        sb.append("bureauCode");
        sb.append('=');
        sb.append(((this.bureauCode == null)?"<null>":this.bureauCode));
        sb.append(',');
        sb.append("contactPoint");
        sb.append('=');
        sb.append(((this.contactPoint == null)?"<null>":this.contactPoint));
        sb.append(',');
        sb.append("describedBy");
        sb.append('=');
        sb.append(((this.describedBy == null)?"<null>":this.describedBy));
        sb.append(',');
        sb.append("describedByType");
        sb.append('=');
        sb.append(((this.describedByType == null)?"<null>":this.describedByType));
        sb.append(',');
        sb.append("conformsTo");
        sb.append('=');
        sb.append(((this.conformsTo == null)?"<null>":this.conformsTo));
        sb.append(',');
        sb.append("dataQuality");
        sb.append('=');
        sb.append(((this.dataQuality == null)?"<null>":this.dataQuality));
        sb.append(',');
        sb.append("description");
        sb.append('=');
        sb.append(((this.description == null)?"<null>":this.description));
        sb.append(',');
        sb.append("distribution");
        sb.append('=');
        sb.append(((this.distribution == null)?"<null>":this.distribution));
        sb.append(',');
        sb.append("identifier");
        sb.append('=');
        sb.append(((this.identifier == null)?"<null>":this.identifier));
        sb.append(',');
        sb.append("issued");
        sb.append('=');
        sb.append(((this.issued == null)?"<null>":this.issued));
        sb.append(',');
        sb.append("keyword");
        sb.append('=');
        sb.append(((this.keyword == null)?"<null>":this.keyword));
        sb.append(',');
        sb.append("landingPage");
        sb.append('=');
        sb.append(((this.landingPage == null)?"<null>":this.landingPage));
        sb.append(',');
        sb.append("webService");
        sb.append('=');
        sb.append(((this.webService == null)?"<null>":this.webService));
        sb.append(',');
        sb.append("language");
        sb.append('=');
        sb.append(((this.language == null)?"<null>":this.language));
        sb.append(',');
        sb.append("license");
        sb.append('=');
        sb.append(((this.license == null)?"<null>":this.license));
        sb.append(',');
        sb.append("modified");
        sb.append('=');
        sb.append(((this.modified == null)?"<null>":this.modified));
        sb.append(',');
        sb.append("primaryITInvestmentUII");
        sb.append('=');
        sb.append(((this.primaryITInvestmentUII == null)?"<null>":this.primaryITInvestmentUII));
        sb.append(',');
        sb.append("programCode");
        sb.append('=');
        sb.append(((this.programCode == null)?"<null>":this.programCode));
        sb.append(',');
        sb.append("publisher");
        sb.append('=');
        sb.append(((this.publisher == null)?"<null>":this.publisher));
        sb.append(',');
        sb.append("references");
        sb.append('=');
        sb.append(((this.references == null)?"<null>":this.references));
        sb.append(',');
        sb.append("spatial");
        sb.append('=');
        sb.append(((this.spatial == null)?"<null>":this.spatial));
        sb.append(',');
        sb.append("systemOfRecords");
        sb.append('=');
        sb.append(((this.systemOfRecords == null)?"<null>":this.systemOfRecords));
        sb.append(',');
        sb.append("temporal");
        sb.append('=');
        sb.append(((this.temporal == null)?"<null>":this.temporal));
        sb.append(',');
        sb.append("isPartOf");
        sb.append('=');
        sb.append(((this.isPartOf == null)?"<null>":this.isPartOf));
        sb.append(',');
        sb.append("theme");
        sb.append('=');
        sb.append(((this.theme == null)?"<null>":this.theme));
        sb.append(',');
        sb.append("title");
        sb.append('=');
        sb.append(((this.title == null)?"<null>":this.title));
        sb.append(',');
<<<<<<< HEAD
        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
        result = ((result* 31)+((this.references == null)? 0 :this.references.hashCode()));
        result = ((result* 31)+((this.contactPoint == null)? 0 :this.contactPoint.hashCode()));
        result = ((result* 31)+((this.description == null)? 0 :this.description.hashCode()));
        result = ((result* 31)+((this.language == null)? 0 :this.language.hashCode()));
        result = ((result* 31)+((this.type == null)? 0 :this.type.hashCode()));
        result = ((result* 31)+((this.distribution == null)? 0 :this.distribution.hashCode()));
        result = ((result* 31)+((this.title == null)? 0 :this.title.hashCode()));
        result = ((result* 31)+((this.bureauCode == null)? 0 :this.bureauCode.hashCode()));
        result = ((result* 31)+((this.webService == null)? 0 :this.webService.hashCode()));
        result = ((result* 31)+((this.primaryITInvestmentUII == null)? 0 :this.primaryITInvestmentUII.hashCode()));
        result = ((result* 31)+((this.rights == null)? 0 :this.rights.hashCode()));
        result = ((result* 31)+((this.describedBy == null)? 0 :this.describedBy.hashCode()));
        result = ((result* 31)+((this.modified == null)? 0 :this.modified.hashCode()));
        result = ((result* 31)+((this.theme == null)? 0 :this.theme.hashCode()));
        result = ((result* 31)+((this.issued == null)? 0 :this.issued.hashCode()));
        result = ((result* 31)+((this.keyword == null)? 0 :this.keyword.hashCode()));
        result = ((result* 31)+((this.spatial == null)? 0 :this.spatial.hashCode()));
        result = ((result* 31)+((this.temporal == null)? 0 :this.temporal.hashCode()));
        result = ((result* 31)+((this.identifier == null)? 0 :this.identifier.hashCode()));
        result = ((result* 31)+((this.accessLevel == null)? 0 :this.accessLevel.hashCode()));
        result = ((result* 31)+((this.programCode == null)? 0 :this.programCode.hashCode()));
        result = ((result* 31)+((this.landingPage == null)? 0 :this.landingPage.hashCode()));
        result = ((result* 31)+((this.systemOfRecords == null)? 0 :this.systemOfRecords.hashCode()));
        result = ((result* 31)+((this.isPartOf == null)? 0 :this.isPartOf.hashCode()));
        result = ((result* 31)+((this.describedByType == null)? 0 :this.describedByType.hashCode()));
        result = ((result* 31)+((this.license == null)? 0 :this.license.hashCode()));
        result = ((result* 31)+((this.dataQuality == null)? 0 :this.dataQuality.hashCode()));
        result = ((result* 31)+((this.publisher == null)? 0 :this.publisher.hashCode()));
        result = ((result* 31)+((this.accrualPeriodicity == null)? 0 :this.accrualPeriodicity.hashCode()));
        result = ((result* 31)+((this.conformsTo == null)? 0 :this.conformsTo.hashCode()));
<<<<<<< HEAD
=======
        result = ((result * 31) + ((this.references == null) ? 0 : this.references.hashCode()));
        result = ((result * 31) + ((this.contactPoint == null) ? 0 : this.contactPoint.hashCode()));
        result = ((result * 31) + ((this.description == null) ? 0 : this.description.hashCode()));
        result = ((result * 31) + ((this.language == null) ? 0 : this.language.hashCode()));
        result = ((result * 31) + ((this.type == null) ? 0 : this.type.hashCode()));
        result = ((result * 31) + ((this.distribution == null) ? 0 : this.distribution.hashCode()));
        result = ((result * 31) + ((this.title == null) ? 0 : this.title.hashCode()));
        result = ((result * 31) + ((this.bureauCode == null) ? 0 : this.bureauCode.hashCode()));
        result = ((result * 31) + ((this.webService == null) ? 0 : this.webService.hashCode()));
        result =
                ((result * 31)
                        + ((this.primaryITInvestmentUII == null)
                                ? 0
                                : this.primaryITInvestmentUII.hashCode()));
        result = ((result * 31) + ((this.rights == null) ? 0 : this.rights.hashCode()));
        result = ((result * 31) + ((this.describedBy == null) ? 0 : this.describedBy.hashCode()));
        result = ((result * 31) + ((this.modified == null) ? 0 : this.modified.hashCode()));
        result = ((result * 31) + ((this.theme == null) ? 0 : this.theme.hashCode()));
        result = ((result * 31) + ((this.issued == null) ? 0 : this.issued.hashCode()));
        result = ((result * 31) + ((this.keyword == null) ? 0 : this.keyword.hashCode()));
        result = ((result * 31) + ((this.spatial == null) ? 0 : this.spatial.hashCode()));
        result = ((result * 31) + ((this.temporal == null) ? 0 : this.temporal.hashCode()));
        result = ((result * 31) + ((this.identifier == null) ? 0 : this.identifier.hashCode()));
        result = ((result * 31) + ((this.accessLevel == null) ? 0 : this.accessLevel.hashCode()));
        result = ((result * 31) + ((this.programCode == null) ? 0 : this.programCode.hashCode()));
        result = ((result * 31) + ((this.landingPage == null) ? 0 : this.landingPage.hashCode()));
        result =
                ((result * 31)
                        + ((this.systemOfRecords == null) ? 0 : this.systemOfRecords.hashCode()));
        result = ((result * 31) + ((this.isPartOf == null) ? 0 : this.isPartOf.hashCode()));
        result =
                ((result * 31)
                        + ((this.describedByType == null) ? 0 : this.describedByType.hashCode()));
        result = ((result * 31) + ((this.license == null) ? 0 : this.license.hashCode()));
        result = ((result * 31) + ((this.dataQuality == null) ? 0 : this.dataQuality.hashCode()));
        result = ((result * 31) + ((this.publisher == null) ? 0 : this.publisher.hashCode()));
        result =
                ((result * 31)
                        + ((this.accrualPeriodicity == null)
                                ? 0
                                : this.accrualPeriodicity.hashCode()));
        result = ((result * 31) + ((this.conformsTo == null) ? 0 : this.conformsTo.hashCode()));
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Dataset) == false) {
            return false;
        }
        Dataset rhs = ((Dataset) other);
<<<<<<< HEAD
<<<<<<< HEAD
        return (((((((((((((((((((((((((((((((this.references == rhs.references)||((this.references!= null)&&this.references.equals(rhs.references)))&&((this.contactPoint == rhs.contactPoint)||((this.contactPoint!= null)&&this.contactPoint.equals(rhs.contactPoint))))&&((this.description == rhs.description)||((this.description!= null)&&this.description.equals(rhs.description))))&&((this.language == rhs.language)||((this.language!= null)&&this.language.equals(rhs.language))))&&((this.type == rhs.type)||((this.type!= null)&&this.type.equals(rhs.type))))&&((this.distribution == rhs.distribution)||((this.distribution!= null)&&this.distribution.equals(rhs.distribution))))&&((this.title == rhs.title)||((this.title!= null)&&this.title.equals(rhs.title))))&&((this.bureauCode == rhs.bureauCode)||((this.bureauCode!= null)&&this.bureauCode.equals(rhs.bureauCode))))&&((this.webService == rhs.webService)||((this.webService!= null)&&this.webService.equals(rhs.webService))))&&((this.primaryITInvestmentUII == rhs.primaryITInvestmentUII)||((this.primaryITInvestmentUII!= null)&&this.primaryITInvestmentUII.equals(rhs.primaryITInvestmentUII))))&&((this.rights == rhs.rights)||((this.rights!= null)&&this.rights.equals(rhs.rights))))&&((this.describedBy == rhs.describedBy)||((this.describedBy!= null)&&this.describedBy.equals(rhs.describedBy))))&&((this.modified == rhs.modified)||((this.modified!= null)&&this.modified.equals(rhs.modified))))&&((this.theme == rhs.theme)||((this.theme!= null)&&this.theme.equals(rhs.theme))))&&((this.issued == rhs.issued)||((this.issued!= null)&&this.issued.equals(rhs.issued))))&&((this.keyword == rhs.keyword)||((this.keyword!= null)&&this.keyword.equals(rhs.keyword))))&&((this.spatial == rhs.spatial)||((this.spatial!= null)&&this.spatial.equals(rhs.spatial))))&&((this.temporal == rhs.temporal)||((this.temporal!= null)&&this.temporal.equals(rhs.temporal))))&&((this.identifier == rhs.identifier)||((this.identifier!= null)&&this.identifier.equals(rhs.identifier))))&&((this.accessLevel == rhs.accessLevel)||((this.accessLevel!= null)&&this.accessLevel.equals(rhs.accessLevel))))&&((this.programCode == rhs.programCode)||((this.programCode!= null)&&this.programCode.equals(rhs.programCode))))&&((this.landingPage == rhs.landingPage)||((this.landingPage!= null)&&this.landingPage.equals(rhs.landingPage))))&&((this.systemOfRecords == rhs.systemOfRecords)||((this.systemOfRecords!= null)&&this.systemOfRecords.equals(rhs.systemOfRecords))))&&((this.isPartOf == rhs.isPartOf)||((this.isPartOf!= null)&&this.isPartOf.equals(rhs.isPartOf))))&&((this.describedByType == rhs.describedByType)||((this.describedByType!= null)&&this.describedByType.equals(rhs.describedByType))))&&((this.license == rhs.license)||((this.license!= null)&&this.license.equals(rhs.license))))&&((this.dataQuality == rhs.dataQuality)||((this.dataQuality!= null)&&this.dataQuality.equals(rhs.dataQuality))))&&((this.publisher == rhs.publisher)||((this.publisher!= null)&&this.publisher.equals(rhs.publisher))))&&((this.accrualPeriodicity == rhs.accrualPeriodicity)||((this.accrualPeriodicity!= null)&&this.accrualPeriodicity.equals(rhs.accrualPeriodicity))))&&((this.conformsTo == rhs.conformsTo)||((this.conformsTo!= null)&&this.conformsTo.equals(rhs.conformsTo))));
=======
        return (((((((((((((((((((((((((((((((this.references == rhs.references)
                                                                                                                                                                                                                                                        || ((this
                                                                                                                                                                                                                                                                                .references
                                                                                                                                                                                                                                                                        != null)
                                                                                                                                                                                                                                                                && this
                                                                                                                                                                                                                                                                        .references
                                                                                                                                                                                                                                                                        .equals(
                                                                                                                                                                                                                                                                                rhs.references)))
                                                                                                                                                                                                                                                && ((this
                                                                                                                                                                                                                                                                        .contactPoint
                                                                                                                                                                                                                                                                == rhs.contactPoint)
                                                                                                                                                                                                                                                        || ((this
                                                                                                                                                                                                                                                                                .contactPoint
                                                                                                                                                                                                                                                                        != null)
                                                                                                                                                                                                                                                                && this
                                                                                                                                                                                                                                                                        .contactPoint
                                                                                                                                                                                                                                                                        .equals(
                                                                                                                                                                                                                                                                                rhs.contactPoint))))
                                                                                                                                                                                                                                        && ((this
                                                                                                                                                                                                                                                                .description
                                                                                                                                                                                                                                                        == rhs.description)
                                                                                                                                                                                                                                                || ((this
                                                                                                                                                                                                                                                                        .description
                                                                                                                                                                                                                                                                != null)
                                                                                                                                                                                                                                                        && this
                                                                                                                                                                                                                                                                .description
                                                                                                                                                                                                                                                                .equals(
                                                                                                                                                                                                                                                                        rhs.description))))
                                                                                                                                                                                                                                && ((this
                                                                                                                                                                                                                                                        .language
                                                                                                                                                                                                                                                == rhs.language)
                                                                                                                                                                                                                                        || ((this
                                                                                                                                                                                                                                                                .language
                                                                                                                                                                                                                                                        != null)
                                                                                                                                                                                                                                                && this
                                                                                                                                                                                                                                                        .language
                                                                                                                                                                                                                                                        .equals(
                                                                                                                                                                                                                                                                rhs.language))))
                                                                                                                                                                                                                        && ((this
                                                                                                                                                                                                                                                .type
                                                                                                                                                                                                                                        == rhs.type)
                                                                                                                                                                                                                                || ((this
                                                                                                                                                                                                                                                        .type
                                                                                                                                                                                                                                                != null)
                                                                                                                                                                                                                                        && this
                                                                                                                                                                                                                                                .type
                                                                                                                                                                                                                                                .equals(
                                                                                                                                                                                                                                                        rhs.type))))
                                                                                                                                                                                                                && ((this
                                                                                                                                                                                                                                        .distribution
                                                                                                                                                                                                                                == rhs.distribution)
                                                                                                                                                                                                                        || ((this
                                                                                                                                                                                                                                                .distribution
                                                                                                                                                                                                                                        != null)
                                                                                                                                                                                                                                && this
                                                                                                                                                                                                                                        .distribution
                                                                                                                                                                                                                                        .equals(
                                                                                                                                                                                                                                                rhs.distribution))))
                                                                                                                                                                                                        && ((this
                                                                                                                                                                                                                                .title
                                                                                                                                                                                                                        == rhs.title)
                                                                                                                                                                                                                || ((this
                                                                                                                                                                                                                                        .title
                                                                                                                                                                                                                                != null)
                                                                                                                                                                                                                        && this
                                                                                                                                                                                                                                .title
                                                                                                                                                                                                                                .equals(
                                                                                                                                                                                                                                        rhs.title))))
                                                                                                                                                                                                && ((this
                                                                                                                                                                                                                        .bureauCode
                                                                                                                                                                                                                == rhs.bureauCode)
                                                                                                                                                                                                        || ((this
                                                                                                                                                                                                                                .bureauCode
                                                                                                                                                                                                                        != null)
                                                                                                                                                                                                                && this
                                                                                                                                                                                                                        .bureauCode
                                                                                                                                                                                                                        .equals(
                                                                                                                                                                                                                                rhs.bureauCode))))
                                                                                                                                                                                        && ((this
                                                                                                                                                                                                                .webService
                                                                                                                                                                                                        == rhs.webService)
                                                                                                                                                                                                || ((this
                                                                                                                                                                                                                        .webService
                                                                                                                                                                                                                != null)
                                                                                                                                                                                                        && this
                                                                                                                                                                                                                .webService
                                                                                                                                                                                                                .equals(
                                                                                                                                                                                                                        rhs.webService))))
                                                                                                                                                                                && ((this
                                                                                                                                                                                                        .primaryITInvestmentUII
                                                                                                                                                                                                == rhs.primaryITInvestmentUII)
                                                                                                                                                                                        || ((this
                                                                                                                                                                                                                .primaryITInvestmentUII
                                                                                                                                                                                                        != null)
                                                                                                                                                                                                && this
                                                                                                                                                                                                        .primaryITInvestmentUII
                                                                                                                                                                                                        .equals(
                                                                                                                                                                                                                rhs.primaryITInvestmentUII))))
                                                                                                                                                                        && ((this
                                                                                                                                                                                                .rights
                                                                                                                                                                                        == rhs.rights)
                                                                                                                                                                                || ((this
                                                                                                                                                                                                        .rights
                                                                                                                                                                                                != null)
                                                                                                                                                                                        && this
                                                                                                                                                                                                .rights
                                                                                                                                                                                                .equals(
                                                                                                                                                                                                        rhs.rights))))
                                                                                                                                                                && ((this
                                                                                                                                                                                        .describedBy
                                                                                                                                                                                == rhs.describedBy)
                                                                                                                                                                        || ((this
                                                                                                                                                                                                .describedBy
                                                                                                                                                                                        != null)
                                                                                                                                                                                && this
                                                                                                                                                                                        .describedBy
                                                                                                                                                                                        .equals(
                                                                                                                                                                                                rhs.describedBy))))
                                                                                                                                                        && ((this
                                                                                                                                                                                .modified
                                                                                                                                                                        == rhs.modified)
                                                                                                                                                                || ((this
                                                                                                                                                                                        .modified
                                                                                                                                                                                != null)
                                                                                                                                                                        && this
                                                                                                                                                                                .modified
                                                                                                                                                                                .equals(
                                                                                                                                                                                        rhs.modified))))
                                                                                                                                                && ((this
                                                                                                                                                                        .theme
                                                                                                                                                                == rhs.theme)
                                                                                                                                                        || ((this
                                                                                                                                                                                .theme
                                                                                                                                                                        != null)
                                                                                                                                                                && this
                                                                                                                                                                        .theme
                                                                                                                                                                        .equals(
                                                                                                                                                                                rhs.theme))))
                                                                                                                                        && ((this
                                                                                                                                                                .issued
                                                                                                                                                        == rhs.issued)
                                                                                                                                                || ((this
                                                                                                                                                                        .issued
                                                                                                                                                                != null)
                                                                                                                                                        && this
                                                                                                                                                                .issued
                                                                                                                                                                .equals(
                                                                                                                                                                        rhs.issued))))
                                                                                                                                && ((this
                                                                                                                                                        .keyword
                                                                                                                                                == rhs.keyword)
                                                                                                                                        || ((this
                                                                                                                                                                .keyword
                                                                                                                                                        != null)
                                                                                                                                                && this
                                                                                                                                                        .keyword
                                                                                                                                                        .equals(
                                                                                                                                                                rhs.keyword))))
                                                                                                                        && ((this
                                                                                                                                                .spatial
                                                                                                                                        == rhs.spatial)
                                                                                                                                || ((this
                                                                                                                                                        .spatial
                                                                                                                                                != null)
                                                                                                                                        && this
                                                                                                                                                .spatial
                                                                                                                                                .equals(
                                                                                                                                                        rhs.spatial))))
                                                                                                                && ((this
                                                                                                                                        .temporal
                                                                                                                                == rhs.temporal)
                                                                                                                        || ((this
                                                                                                                                                .temporal
                                                                                                                                        != null)
                                                                                                                                && this
                                                                                                                                        .temporal
                                                                                                                                        .equals(
                                                                                                                                                rhs.temporal))))
                                                                                                        && ((this
                                                                                                                                .identifier
                                                                                                                        == rhs.identifier)
                                                                                                                || ((this
                                                                                                                                        .identifier
                                                                                                                                != null)
                                                                                                                        && this
                                                                                                                                .identifier
                                                                                                                                .equals(
                                                                                                                                        rhs.identifier))))
                                                                                                && ((this
                                                                                                                        .accessLevel
                                                                                                                == rhs.accessLevel)
                                                                                                        || ((this
                                                                                                                                .accessLevel
                                                                                                                        != null)
                                                                                                                && this
                                                                                                                        .accessLevel
                                                                                                                        .equals(
                                                                                                                                rhs.accessLevel))))
                                                                                        && ((this
                                                                                                                .programCode
                                                                                                        == rhs.programCode)
                                                                                                || ((this
                                                                                                                        .programCode
                                                                                                                != null)
                                                                                                        && this
                                                                                                                .programCode
                                                                                                                .equals(
                                                                                                                        rhs.programCode))))
                                                                                && ((this
                                                                                                        .landingPage
                                                                                                == rhs.landingPage)
                                                                                        || ((this
                                                                                                                .landingPage
                                                                                                        != null)
                                                                                                && this
                                                                                                        .landingPage
                                                                                                        .equals(
                                                                                                                rhs.landingPage))))
                                                                        && ((this.systemOfRecords
                                                                                        == rhs.systemOfRecords)
                                                                                || ((this
                                                                                                        .systemOfRecords
                                                                                                != null)
                                                                                        && this
                                                                                                .systemOfRecords
                                                                                                .equals(
                                                                                                        rhs.systemOfRecords))))
                                                                && ((this.isPartOf == rhs.isPartOf)
                                                                        || ((this.isPartOf != null)
                                                                                && this.isPartOf
                                                                                        .equals(
                                                                                                rhs.isPartOf))))
                                                        && ((this.describedByType
                                                                        == rhs.describedByType)
                                                                || ((this.describedByType != null)
                                                                        && this.describedByType
                                                                                .equals(
                                                                                        rhs.describedByType))))
                                                && ((this.license == rhs.license)
                                                        || ((this.license != null)
                                                                && this.license.equals(
                                                                        rhs.license))))
                                        && ((this.dataQuality == rhs.dataQuality)
                                                || ((this.dataQuality != null)
                                                        && this.dataQuality.equals(
                                                                rhs.dataQuality))))
                                && ((this.publisher == rhs.publisher)
                                        || ((this.publisher != null)
                                                && this.publisher.equals(rhs.publisher))))
                        && ((this.accrualPeriodicity == rhs.accrualPeriodicity)
                                || ((this.accrualPeriodicity != null)
                                        && this.accrualPeriodicity.equals(rhs.accrualPeriodicity))))
                && ((this.conformsTo == rhs.conformsTo)
                        || ((this.conformsTo != null) && this.conformsTo.equals(rhs.conformsTo))));
>>>>>>> 5fb8ab8508... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
=======
        return (((((((((((((((((((((((((((((((this.references == rhs.references)||((this.references!= null)&&this.references.equals(rhs.references)))&&((this.contactPoint == rhs.contactPoint)||((this.contactPoint!= null)&&this.contactPoint.equals(rhs.contactPoint))))&&((this.description == rhs.description)||((this.description!= null)&&this.description.equals(rhs.description))))&&((this.language == rhs.language)||((this.language!= null)&&this.language.equals(rhs.language))))&&((this.type == rhs.type)||((this.type!= null)&&this.type.equals(rhs.type))))&&((this.distribution == rhs.distribution)||((this.distribution!= null)&&this.distribution.equals(rhs.distribution))))&&((this.title == rhs.title)||((this.title!= null)&&this.title.equals(rhs.title))))&&((this.bureauCode == rhs.bureauCode)||((this.bureauCode!= null)&&this.bureauCode.equals(rhs.bureauCode))))&&((this.webService == rhs.webService)||((this.webService!= null)&&this.webService.equals(rhs.webService))))&&((this.primaryITInvestmentUII == rhs.primaryITInvestmentUII)||((this.primaryITInvestmentUII!= null)&&this.primaryITInvestmentUII.equals(rhs.primaryITInvestmentUII))))&&((this.rights == rhs.rights)||((this.rights!= null)&&this.rights.equals(rhs.rights))))&&((this.describedBy == rhs.describedBy)||((this.describedBy!= null)&&this.describedBy.equals(rhs.describedBy))))&&((this.modified == rhs.modified)||((this.modified!= null)&&this.modified.equals(rhs.modified))))&&((this.theme == rhs.theme)||((this.theme!= null)&&this.theme.equals(rhs.theme))))&&((this.issued == rhs.issued)||((this.issued!= null)&&this.issued.equals(rhs.issued))))&&((this.keyword == rhs.keyword)||((this.keyword!= null)&&this.keyword.equals(rhs.keyword))))&&((this.spatial == rhs.spatial)||((this.spatial!= null)&&this.spatial.equals(rhs.spatial))))&&((this.temporal == rhs.temporal)||((this.temporal!= null)&&this.temporal.equals(rhs.temporal))))&&((this.identifier == rhs.identifier)||((this.identifier!= null)&&this.identifier.equals(rhs.identifier))))&&((this.accessLevel == rhs.accessLevel)||((this.accessLevel!= null)&&this.accessLevel.equals(rhs.accessLevel))))&&((this.programCode == rhs.programCode)||((this.programCode!= null)&&this.programCode.equals(rhs.programCode))))&&((this.landingPage == rhs.landingPage)||((this.landingPage!= null)&&this.landingPage.equals(rhs.landingPage))))&&((this.systemOfRecords == rhs.systemOfRecords)||((this.systemOfRecords!= null)&&this.systemOfRecords.equals(rhs.systemOfRecords))))&&((this.isPartOf == rhs.isPartOf)||((this.isPartOf!= null)&&this.isPartOf.equals(rhs.isPartOf))))&&((this.describedByType == rhs.describedByType)||((this.describedByType!= null)&&this.describedByType.equals(rhs.describedByType))))&&((this.license == rhs.license)||((this.license!= null)&&this.license.equals(rhs.license))))&&((this.dataQuality == rhs.dataQuality)||((this.dataQuality!= null)&&this.dataQuality.equals(rhs.dataQuality))))&&((this.publisher == rhs.publisher)||((this.publisher!= null)&&this.publisher.equals(rhs.publisher))))&&((this.accrualPeriodicity == rhs.accrualPeriodicity)||((this.accrualPeriodicity!= null)&&this.accrualPeriodicity.equals(rhs.accrualPeriodicity))))&&((this.conformsTo == rhs.conformsTo)||((this.conformsTo!= null)&&this.conformsTo.equals(rhs.conformsTo))));
>>>>>>> db04a836af... [AUR-5856] Geoserver ArcGIS datastore Fails to Recognize CRS
    }

    public enum AccessLevel {

        @SerializedName("public")
        PUBLIC("public"),
        @SerializedName("restricted public")
        RESTRICTED_PUBLIC("restricted public"),
        @SerializedName("non-public")
        NON_PUBLIC("non-public");
        private final String value;
        private final static Map<String, Dataset.AccessLevel> CONSTANTS = new HashMap<String, Dataset.AccessLevel>();

        static {
            for (Dataset.AccessLevel c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private AccessLevel(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Dataset.AccessLevel fromValue(String value) {
            Dataset.AccessLevel constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum Type {

        @SerializedName("dcat:Dataset")
        DCAT_DATASET("dcat:Dataset");
        private final String value;
        private final static Map<String, Dataset.Type> CONSTANTS = new HashMap<String, Dataset.Type>();

        static {
            for (Dataset.Type c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private Type(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Dataset.Type fromValue(String value) {
            Dataset.Type constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
