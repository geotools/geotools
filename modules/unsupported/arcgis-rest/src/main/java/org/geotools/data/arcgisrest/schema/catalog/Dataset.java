package org.geotools.data.arcgisrest.schema.catalog;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Project Open Data Dataset
 *
 * <p>The metadata format for all federal open data. Validates a single JSON object entry (as
 * opposed to entire Data.json catalog).
 */
public class Dataset {

    /**
     * Metadata Context
     *
     * <p>IRI for the JSON-LD data type. This should be dcat:Dataset for each Dataset
     */
    @SerializedName("@type")
    @Expose
    private Dataset.Type type;
    /**
     * Public Access Level
     *
     * <p>The degree to which this dataset could be made publicly-available, regardless of whether
     * it has been made available. Choices: public (Data asset is or could be made publicly
     * available to all without restrictions), restricted public (Data asset is available under
     * certain use restrictions), or non-public (Data asset is not available to members of the
     * public) (Required)
     */
    @SerializedName("accessLevel")
    @Expose
    private Dataset.AccessLevel accessLevel;
    /**
     * Rights
     *
     * <p>This may include information regarding access or restrictions based on privacy, security,
     * or other policies. This should also provide an explanation for the selected "accessLevel"
     * including instructions for how to access a restricted file, if applicable, or explanation for
     * why a "non-public" or "restricted public" data assetis not "public," if applicable. Text, 255
     * characters.
     */
    @SerializedName("rights")
    @Expose
    private Object rights;
    /**
     * Frequency
     *
     * <p>Frequency with which dataset is published.
     */
    @SerializedName("accrualPeriodicity")
    @Expose
    private Object accrualPeriodicity;
    /**
     * Bureau Code
     *
     * <p>Federal agencies, combined agency and bureau code from <a
     * href="http://www.whitehouse.gov/sites/default/files/omb/assets/a11_current_year/app_c.pdf">OMB
     * Circular A-11, Appendix C</a> in the format of <code>015:010</code>. (Required)
     */
    @SerializedName("bureauCode")
    @Expose
    private Set<String> bureauCode = new LinkedHashSet<String>();
    /**
     * Project Open Data ContactPoint vCard
     *
     * <p>A Dataset ContactPoint as a vCard object (Required)
     */
    @SerializedName("contactPoint")
    @Expose
    private ContactPoint contactPoint;
    /**
     * Data Dictionary
     *
     * <p>URL to the data dictionary for the dataset or API. Note that documentation other than a
     * data dictionary can be referenced using Related Documents as shown in the expanded fields.
     */
    @SerializedName("describedBy")
    @Expose
    private Object describedBy;
    /**
     * Data Dictionary Type
     *
     * <p>The machine-readable file format (IANA Media Type or MIME Type) of the distribution’s
     * describedBy URL
     */
    @SerializedName("describedByType")
    @Expose
    private Object describedByType;
    /**
     * Data Standard
     *
     * <p>URI used to identify a standardized specification the dataset conforms to
     */
    @SerializedName("conformsTo")
    @Expose
    private Object conformsTo;
    /**
     * Data Quality
     *
     * <p>Whether the dataset meets the agency’s Information Quality Guidelines (true/false).
     */
    @SerializedName("dataQuality")
    @Expose
    private Object dataQuality;
    /**
     * Description
     *
     * <p>Human-readable description (e.g., an abstract) with sufficient detail to enable a user to
     * quickly understand whether the asset is of interest. (Required)
     */
    @SerializedName("description")
    @Expose
    private String description;
    /**
     * Distribution
     *
     * <p>A container for the array of Distribution objects
     */
    @SerializedName("distribution")
    @Expose
    private List<Distribution> distribution = new ArrayList<Distribution>();
    /**
     * Unique Identifier
     *
     * <p>A unique identifier for the dataset or API as maintained within an Agency catalog or
     * database. (Required)
     */
    @SerializedName("identifier")
    @Expose
    private String identifier;
    /**
     * Release Date
     *
     * <p>Date of formal issuance.
     */
    @SerializedName("issued")
    @Expose
    private Object issued;
    /**
     * Tags
     *
     * <p>Tags (or keywords) help users discover your dataset; please include terms that would be
     * used by technical and non-technical users. (Required)
     */
    @SerializedName("keyword")
    @Expose
    private List<String> keyword = new ArrayList<String>();
    /**
     * Homepage URL
     *
     * <p>Alternative landing page used to redirect user to a contextual, Agency-hosted “homepage”
     * for the Dataset or API when selecting this resource from the Data.gov user interface.
     */
    @SerializedName("landingPage")
    @Expose
    private Object landingPage;
    /**
     * WebService URL (ESRI extension of the standard schema)
     *
     * <p>ESRI ArcGIS ReST API pf the datatse
     */
    @SerializedName("webService")
    @Expose
    private Object webService;
    /**
     * Language
     *
     * <p>The language of the dataset.
     */
    @SerializedName("language")
    @Expose
    private Object language;
    /**
     * License
     *
     * <p>The license dataset or API is published with. See <a
     * href="https://project-open-data.cio.gov/open-licenses/">Open Licenses</a> for more
     * information.
     */
    @SerializedName("license")
    @Expose
    private Object license;
    /**
     * Last Update
     *
     * <p>Most recent date on which the dataset was changed, updated or modified. (Required)
     */
    @SerializedName("modified")
    @Expose
    private Object modified;
    /**
     * Primary IT Investment UII
     *
     * <p>For linking a dataset with an IT Unique Investment Identifier (UII)
     */
    @SerializedName("primaryITInvestmentUII")
    @Expose
    private Object primaryITInvestmentUII;
    /**
     * Program Code
     *
     * <p>Federal agencies, list the primary program related to this data asset, from the <a
     * href="http://goals.performance.gov/sites/default/files/images/FederalProgramInventory_FY13_MachineReadable_091613.xls">Federal
     * Program Inventory</a>. Use the format of <code>015:001</code> (Required)
     */
    @SerializedName("programCode")
    @Expose
    private Set<String> programCode = new LinkedHashSet<String>();
    /**
     * Project Open Data Organization
     *
     * <p>A Dataset Publisher Organization as a foaf:Agent object (Required)
     */
    @SerializedName("publisher")
    @Expose
    private Publisher publisher;
    /**
     * Related Documents
     *
     * <p>Related documents such as technical information about a dataset, developer documentation,
     * etc.
     */
    @SerializedName("references")
    @Expose
    private Object references;
    /**
     * Spatial
     *
     * <p>The range of spatial applicability of a dataset. Could include a spatial region like a
     * bounding box or a named place.
     */
    @SerializedName("spatial")
    @Expose
    private Object spatial;
    /**
     * System of Records
     *
     * <p>If the systems is designated as a system of records under the Privacy Act of 1974, provide
     * the URL to the System of Records Notice related to this dataset.
     */
    @SerializedName("systemOfRecords")
    @Expose
    private Object systemOfRecords;
    /**
     * Temporal
     *
     * <p>The range of temporal applicability of a dataset (i.e., a start and end date of
     * applicability for the data).
     */
    @SerializedName("temporal")
    @Expose
    private Object temporal;
    /**
     * Collection
     *
     * <p>The collection of which the dataset is a subset
     */
    @SerializedName("isPartOf")
    @Expose
    private Object isPartOf;
    /**
     * Category
     *
     * <p>Main thematic category of the dataset.
     */
    @SerializedName("theme")
    @Expose
    private Object theme;
    /**
     * Title
     *
     * <p>Human-readable name of the asset. Should be in plain English and include sufficient detail
     * to facilitate search and discovery. (Required)
     */
    @SerializedName("title")
    @Expose
    private String title;

    /**
     * Metadata Context
     *
     * <p>IRI for the JSON-LD data type. This should be dcat:Dataset for each Dataset
     *
     * @return The type
     */
    public Dataset.Type getType() {
        return type;
    }

    /**
     * Metadata Context
     *
     * <p>IRI for the JSON-LD data type. This should be dcat:Dataset for each Dataset
     *
     * @param type The @type
     */
    public void setType(Dataset.Type type) {
        this.type = type;
    }

    /**
     * Public Access Level
     *
     * <p>The degree to which this dataset could be made publicly-available, regardless of whether
     * it has been made available. Choices: public (Data asset is or could be made publicly
     * available to all without restrictions), restricted public (Data asset is available under
     * certain use restrictions), or non-public (Data asset is not available to members of the
     * public) (Required)
     *
     * @return The accessLevel
     */
    public Dataset.AccessLevel getAccessLevel() {
        return accessLevel;
    }

    /**
     * Public Access Level
     *
     * <p>The degree to which this dataset could be made publicly-available, regardless of whether
     * it has been made available. Choices: public (Data asset is or could be made publicly
     * available to all without restrictions), restricted public (Data asset is available under
     * certain use restrictions), or non-public (Data asset is not available to members of the
     * public) (Required)
     *
     * @param accessLevel The accessLevel
     */
    public void setAccessLevel(Dataset.AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
    }

    /**
     * Rights
     *
     * <p>This may include information regarding access or restrictions based on privacy, security,
     * or other policies. This should also provide an explanation for the selected "accessLevel"
     * including instructions for how to access a restricted file, if applicable, or explanation for
     * why a "non-public" or "restricted public" data assetis not "public," if applicable. Text, 255
     * characters.
     *
     * @return The rights
     */
    public Object getRights() {
        return rights;
    }

    /**
     * Rights
     *
     * <p>This may include information regarding access or restrictions based on privacy, security,
     * or other policies. This should also provide an explanation for the selected "accessLevel"
     * including instructions for how to access a restricted file, if applicable, or explanation for
     * why a "non-public" or "restricted public" data assetis not "public," if applicable. Text, 255
     * characters.
     *
     * @param rights The rights
     */
    public void setRights(Object rights) {
        this.rights = rights;
    }

    /**
     * Frequency
     *
     * <p>Frequency with which dataset is published.
     *
     * @return The accrualPeriodicity
     */
    public Object getAccrualPeriodicity() {
        return accrualPeriodicity;
    }

    /**
     * Frequency
     *
     * <p>Frequency with which dataset is published.
     *
     * @param accrualPeriodicity The accrualPeriodicity
     */
    public void setAccrualPeriodicity(Object accrualPeriodicity) {
        this.accrualPeriodicity = accrualPeriodicity;
    }

    /**
     * Bureau Code
     *
     * <p>Federal agencies, combined agency and bureau code from <a
     * href="http://www.whitehouse.gov/sites/default/files/omb/assets/a11_current_year/app_c.pdf">OMB
     * Circular A-11, Appendix C</a> in the format of <code>015:010</code>. (Required)
     *
     * @return The bureauCode
     */
    public Set<String> getBureauCode() {
        return bureauCode;
    }

    /**
     * Bureau Code
     *
     * <p>Federal agencies, combined agency and bureau code from <a
     * href="http://www.whitehouse.gov/sites/default/files/omb/assets/a11_current_year/app_c.pdf">OMB
     * Circular A-11, Appendix C</a> in the format of <code>015:010</code>. (Required)
     *
     * @param bureauCode The bureauCode
     */
    public void setBureauCode(Set<String> bureauCode) {
        this.bureauCode = bureauCode;
    }

    /**
     * Project Open Data ContactPoint vCard
     *
     * <p>A Dataset ContactPoint as a vCard object (Required)
     *
     * @return The contactPoint
     */
    public ContactPoint getContactPoint() {
        return contactPoint;
    }

    /**
     * Project Open Data ContactPoint vCard
     *
     * <p>A Dataset ContactPoint as a vCard object (Required)
     *
     * @param contactPoint The contactPoint
     */
    public void setContactPoint(ContactPoint contactPoint) {
        this.contactPoint = contactPoint;
    }

    /**
     * Data Dictionary
     *
     * <p>URL to the data dictionary for the dataset or API. Note that documentation other than a
     * data dictionary can be referenced using Related Documents as shown in the expanded fields.
     *
     * @return The describedBy
     */
    public Object getDescribedBy() {
        return describedBy;
    }

    /**
     * Data Dictionary
     *
     * <p>URL to the data dictionary for the dataset or API. Note that documentation other than a
     * data dictionary can be referenced using Related Documents as shown in the expanded fields.
     *
     * @param describedBy The describedBy
     */
    public void setDescribedBy(Object describedBy) {
        this.describedBy = describedBy;
    }

    /**
     * Data Dictionary Type
     *
     * <p>The machine-readable file format (IANA Media Type or MIME Type) of the distribution’s
     * describedBy URL
     *
     * @return The describedByType
     */
    public Object getDescribedByType() {
        return describedByType;
    }

    /**
     * Data Dictionary Type
     *
     * <p>The machine-readable file format (IANA Media Type or MIME Type) of the distribution’s
     * describedBy URL
     *
     * @param describedByType The describedByType
     */
    public void setDescribedByType(Object describedByType) {
        this.describedByType = describedByType;
    }

    /**
     * Data Standard
     *
     * <p>URI used to identify a standardized specification the dataset conforms to
     *
     * @return The conformsTo
     */
    public Object getConformsTo() {
        return conformsTo;
    }

    /**
     * Data Standard
     *
     * <p>URI used to identify a standardized specification the dataset conforms to
     *
     * @param conformsTo The conformsTo
     */
    public void setConformsTo(Object conformsTo) {
        this.conformsTo = conformsTo;
    }

    /**
     * Data Quality
     *
     * <p>Whether the dataset meets the agency’s Information Quality Guidelines (true/false).
     *
     * @return The dataQuality
     */
    public Object getDataQuality() {
        return dataQuality;
    }

    /**
     * Data Quality
     *
     * <p>Whether the dataset meets the agency’s Information Quality Guidelines (true/false).
     *
     * @param dataQuality The dataQuality
     */
    public void setDataQuality(Object dataQuality) {
        this.dataQuality = dataQuality;
    }

    /**
     * Description
     *
     * <p>Human-readable description (e.g., an abstract) with sufficient detail to enable a user to
     * quickly understand whether the asset is of interest. (Required)
     *
     * @return The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Description
     *
     * <p>Human-readable description (e.g., an abstract) with sufficient detail to enable a user to
     * quickly understand whether the asset is of interest. (Required)
     *
     * @param description The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Distribution
     *
     * <p>A container for the array of Distribution objects
     *
     * @return The distribution
     */
    public List<Distribution> getDistribution() {
        return distribution;
    }

    /**
     * Distribution
     *
     * <p>A container for the array of Distribution objects
     *
     * @param distribution The distribution
     */
    public void setDistribution(List<Distribution> distribution) {
        this.distribution = distribution;
    }

    /**
     * Unique Identifier
     *
     * <p>A unique identifier for the dataset or API as maintained within an Agency catalog or
     * database. (Required)
     *
     * @return The identifier
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Unique Identifier
     *
     * <p>A unique identifier for the dataset or API as maintained within an Agency catalog or
     * database. (Required)
     *
     * @param identifier The identifier
     */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    /**
     * Release Date
     *
     * <p>Date of formal issuance.
     *
     * @return The issued
     */
    public Object getIssued() {
        return issued;
    }

    /**
     * Release Date
     *
     * <p>Date of formal issuance.
     *
     * @param issued The issued
     */
    public void setIssued(Object issued) {
        this.issued = issued;
    }

    /**
     * Tags
     *
     * <p>Tags (or keywords) help users discover your dataset; please include terms that would be
     * used by technical and non-technical users. (Required)
     *
     * @return The keyword
     */
    public List<String> getKeyword() {
        return keyword;
    }

    /**
     * Tags
     *
     * <p>Tags (or keywords) help users discover your dataset; please include terms that would be
     * used by technical and non-technical users. (Required)
     *
     * @param keyword The keyword
     */
    public void setKeyword(List<String> keyword) {
        this.keyword = keyword;
    }

    /**
     * Homepage URL
     *
     * <p>Alternative landing page used to redirect user to a contextual, Agency-hosted “homepage”
     * for the Dataset or API when selecting this resource from the Data.gov user interface.
     *
     * @return The landingPage
     */
    public Object getLandingPage() {
        return landingPage;
    }

    /**
     * Homepage URL
     *
     * <p>Alternative landing page used to redirect user to a contextual, Agency-hosted “homepage”
     * for the Dataset or API when selecting this resource from the Data.gov user interface.
     *
     * @param landingPage The landingPage
     */
    public void setLandingPage(Object landingPage) {
        this.landingPage = landingPage;
    }

    /**
     * WebService URL (ESRI extension of the standard schema)
     *
     * <p>ESRI ArcGIS ReST API pf the datatse
     *
     * @return The webService
     */
    public Object getWebService() {
        return webService;
    }

    /**
     * WebService URL (ESRI extension of the standard schema)
     *
     * <p>ESRI ArcGIS ReST API pf the datatse
     *
     * @param webService The webService
     */
    public void setWebService(Object webService) {
        this.webService = webService;
    }

    /**
     * Language
     *
     * <p>The language of the dataset.
     *
     * @return The language
     */
    public Object getLanguage() {
        return language;
    }

    /**
     * Language
     *
     * <p>The language of the dataset.
     *
     * @param language The language
     */
    public void setLanguage(Object language) {
        this.language = language;
    }

    /**
     * License
     *
     * <p>The license dataset or API is published with. See <a
     * href="https://project-open-data.cio.gov/open-licenses/">Open Licenses</a> for more
     * information.
     *
     * @return The license
     */
    public Object getLicense() {
        return license;
    }

    /**
     * License
     *
     * <p>The license dataset or API is published with. See <a
     * href="https://project-open-data.cio.gov/open-licenses/">Open Licenses</a> for more
     * information.
     *
     * @param license The license
     */
    public void setLicense(Object license) {
        this.license = license;
    }

    /**
     * Last Update
     *
     * <p>Most recent date on which the dataset was changed, updated or modified. (Required)
     *
     * @return The modified
     */
    public Object getModified() {
        return modified;
    }

    /**
     * Last Update
     *
     * <p>Most recent date on which the dataset was changed, updated or modified. (Required)
     *
     * @param modified The modified
     */
    public void setModified(Object modified) {
        this.modified = modified;
    }

    /**
     * Primary IT Investment UII
     *
     * <p>For linking a dataset with an IT Unique Investment Identifier (UII)
     *
     * @return The primaryITInvestmentUII
     */
    public Object getPrimaryITInvestmentUII() {
        return primaryITInvestmentUII;
    }

    /**
     * Primary IT Investment UII
     *
     * <p>For linking a dataset with an IT Unique Investment Identifier (UII)
     *
     * @param primaryITInvestmentUII The primaryITInvestmentUII
     */
    public void setPrimaryITInvestmentUII(Object primaryITInvestmentUII) {
        this.primaryITInvestmentUII = primaryITInvestmentUII;
    }

    /**
     * Program Code
     *
     * <p>Federal agencies, list the primary program related to this data asset, from the <a
     * href="http://goals.performance.gov/sites/default/files/images/FederalProgramInventory_FY13_MachineReadable_091613.xls">Federal
     * Program Inventory</a>. Use the format of <code>015:001</code> (Required)
     *
     * @return The programCode
     */
    public Set<String> getProgramCode() {
        return programCode;
    }

    /**
     * Program Code
     *
     * <p>Federal agencies, list the primary program related to this data asset, from the <a
     * href="http://goals.performance.gov/sites/default/files/images/FederalProgramInventory_FY13_MachineReadable_091613.xls">Federal
     * Program Inventory</a>. Use the format of <code>015:001</code> (Required)
     *
     * @param programCode The programCode
     */
    public void setProgramCode(Set<String> programCode) {
        this.programCode = programCode;
    }

    /**
     * Project Open Data Organization
     *
     * <p>A Dataset Publisher Organization as a foaf:Agent object (Required)
     *
     * @return The publisher
     */
    public Publisher getPublisher() {
        return publisher;
    }

    /**
     * Project Open Data Organization
     *
     * <p>A Dataset Publisher Organization as a foaf:Agent object (Required)
     *
     * @param publisher The publisher
     */
    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    /**
     * Related Documents
     *
     * <p>Related documents such as technical information about a dataset, developer documentation,
     * etc.
     *
     * @return The references
     */
    public Object getReferences() {
        return references;
    }

    /**
     * Related Documents
     *
     * <p>Related documents such as technical information about a dataset, developer documentation,
     * etc.
     *
     * @param references The references
     */
    public void setReferences(Object references) {
        this.references = references;
    }

    /**
     * Spatial
     *
     * <p>The range of spatial applicability of a dataset. Could include a spatial region like a
     * bounding box or a named place.
     *
     * @return The spatial
     */
    public Object getSpatial() {
        return spatial;
    }

    /**
     * Spatial
     *
     * <p>The range of spatial applicability of a dataset. Could include a spatial region like a
     * bounding box or a named place.
     *
     * @param spatial The spatial
     */
    public void setSpatial(Object spatial) {
        this.spatial = spatial;
    }

    /**
     * System of Records
     *
     * <p>If the systems is designated as a system of records under the Privacy Act of 1974, provide
     * the URL to the System of Records Notice related to this dataset.
     *
     * @return The systemOfRecords
     */
    public Object getSystemOfRecords() {
        return systemOfRecords;
    }

    /**
     * System of Records
     *
     * <p>If the systems is designated as a system of records under the Privacy Act of 1974, provide
     * the URL to the System of Records Notice related to this dataset.
     *
     * @param systemOfRecords The systemOfRecords
     */
    public void setSystemOfRecords(Object systemOfRecords) {
        this.systemOfRecords = systemOfRecords;
    }

    /**
     * Temporal
     *
     * <p>The range of temporal applicability of a dataset (i.e., a start and end date of
     * applicability for the data).
     *
     * @return The temporal
     */
    public Object getTemporal() {
        return temporal;
    }

    /**
     * Temporal
     *
     * <p>The range of temporal applicability of a dataset (i.e., a start and end date of
     * applicability for the data).
     *
     * @param temporal The temporal
     */
    public void setTemporal(Object temporal) {
        this.temporal = temporal;
    }

    /**
     * Collection
     *
     * <p>The collection of which the dataset is a subset
     *
     * @return The isPartOf
     */
    public Object getIsPartOf() {
        return isPartOf;
    }

    /**
     * Collection
     *
     * <p>The collection of which the dataset is a subset
     *
     * @param isPartOf The isPartOf
     */
    public void setIsPartOf(Object isPartOf) {
        this.isPartOf = isPartOf;
    }

    /**
     * Category
     *
     * <p>Main thematic category of the dataset.
     *
     * @return The theme
     */
    public Object getTheme() {
        return theme;
    }

    /**
     * Category
     *
     * <p>Main thematic category of the dataset.
     *
     * @param theme The theme
     */
    public void setTheme(Object theme) {
        this.theme = theme;
    }

    /**
     * Title
     *
     * <p>Human-readable name of the asset. Should be in plain English and include sufficient detail
     * to facilitate search and discovery. (Required)
     *
     * @return The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Title
     *
     * <p>Human-readable name of the asset. Should be in plain English and include sufficient detail
     * to facilitate search and discovery. (Required)
     *
     * @param title The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(type)
                .append(accessLevel)
                .append(rights)
                .append(accrualPeriodicity)
                .append(bureauCode)
                .append(contactPoint)
                .append(describedBy)
                .append(describedByType)
                .append(conformsTo)
                .append(dataQuality)
                .append(description)
                .append(distribution)
                .append(identifier)
                .append(issued)
                .append(keyword)
                .append(landingPage)
                .append(webService)
                .append(language)
                .append(license)
                .append(modified)
                .append(primaryITInvestmentUII)
                .append(programCode)
                .append(publisher)
                .append(references)
                .append(spatial)
                .append(systemOfRecords)
                .append(temporal)
                .append(isPartOf)
                .append(theme)
                .append(title)
                .toHashCode();
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
        return new EqualsBuilder()
                .append(type, rhs.type)
                .append(accessLevel, rhs.accessLevel)
                .append(rights, rhs.rights)
                .append(accrualPeriodicity, rhs.accrualPeriodicity)
                .append(bureauCode, rhs.bureauCode)
                .append(contactPoint, rhs.contactPoint)
                .append(describedBy, rhs.describedBy)
                .append(describedByType, rhs.describedByType)
                .append(conformsTo, rhs.conformsTo)
                .append(dataQuality, rhs.dataQuality)
                .append(description, rhs.description)
                .append(distribution, rhs.distribution)
                .append(identifier, rhs.identifier)
                .append(issued, rhs.issued)
                .append(keyword, rhs.keyword)
                .append(landingPage, rhs.landingPage)
                .append(webService, rhs.webService)
                .append(language, rhs.language)
                .append(license, rhs.license)
                .append(modified, rhs.modified)
                .append(primaryITInvestmentUII, rhs.primaryITInvestmentUII)
                .append(programCode, rhs.programCode)
                .append(publisher, rhs.publisher)
                .append(references, rhs.references)
                .append(spatial, rhs.spatial)
                .append(systemOfRecords, rhs.systemOfRecords)
                .append(temporal, rhs.temporal)
                .append(isPartOf, rhs.isPartOf)
                .append(theme, rhs.theme)
                .append(title, rhs.title)
                .isEquals();
    }

    public enum AccessLevel {
        @SerializedName("public")
        PUBLIC("public"),
        @SerializedName("restricted public")
        RESTRICTED_PUBLIC("restricted public"),
        @SerializedName("non-public")
        NON_PUBLIC("non-public");
        private final String value;
        private static final Map<String, Dataset.AccessLevel> CONSTANTS =
                new HashMap<String, Dataset.AccessLevel>();

        static {
            for (Dataset.AccessLevel c : values()) {
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
        private static final Map<String, Dataset.Type> CONSTANTS =
                new HashMap<String, Dataset.Type>();

        static {
            for (Dataset.Type c : values()) {
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
