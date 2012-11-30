package org.geotools.ows.v2_0;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;

import javax.xml.namespace.QName;

import net.opengis.ows20.Ows20Factory;

import org.geotools.ows.bindings.BoundingBoxTypeBinding;
import org.geotools.ows.bindings.UnitBinding;
import org.geotools.xlink.XLINKConfiguration;
import org.geotools.xml.ComplexEMFBinding;
import org.geotools.xml.Configuration;
import org.geotools.xml.SimpleContentComplexEMFBinding;
import org.geotools.xml.XMLConfiguration;
import org.picocontainer.MutablePicoContainer;

/**
 * Parser configuration for the http://www.opengis.net/ows/2.0 schema.
 * 
 * @generated
 */
public class OWSConfiguration extends Configuration {

    /**
     * Creates a new configuration.
     * 
     * @generated
     */
    public OWSConfiguration() {
        super(OWS.getInstance());

        addDependency(new XMLConfiguration());
        addDependency(new XLINKConfiguration());
    }

    /**
     * Registers the bindings for the configuration.
     * 
     * @generated
     */
    @SuppressWarnings("unchecked")
    protected final void registerBindings(Map bindings) {
        // manually setup bindings
        bindings.put(OWS.BoundingBoxType, new BoundingBoxTypeBinding(Ows20Factory.eINSTANCE,
                OWS.BoundingBoxType));
        bindings.put(OWS.CodeType, new SimpleContentComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.CodeType));
        bindings.put(OWS.DomainMetadataType, new SimpleContentComplexEMFBinding(
                Ows20Factory.eINSTANCE, OWS.DomainMetadataType));
        bindings.put(OWS.LanguageStringType, new SimpleContentComplexEMFBinding(
                Ows20Factory.eINSTANCE, OWS.LanguageStringType));
        bindings.put(OWS.ValueType, new SimpleContentComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.ValueType));
        bindings.put(OWS.UOM, new UnitBinding());

        // "automatic" bindings
        bindings.put(OWS.AbstractReferenceBaseType, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.AbstractReferenceBaseType));
        bindings.put(OWS.AcceptFormatsType, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.AcceptFormatsType));
        bindings.put(OWS.AcceptVersionsType, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.AcceptVersionsType));
        bindings.put(OWS.AdditionalParametersBaseType, new ComplexEMFBinding(
                Ows20Factory.eINSTANCE, OWS.AdditionalParametersBaseType));
        bindings.put(OWS.AdditionalParametersType, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.AdditionalParametersType));
        bindings.put(OWS.AddressType,
                new ComplexEMFBinding(Ows20Factory.eINSTANCE, OWS.AddressType));
        bindings.put(OWS.BasicIdentificationType, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.BasicIdentificationType));
        bindings.put(OWS.BoundingBoxType, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.BoundingBoxType));
        bindings.put(OWS.CapabilitiesBaseType, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.CapabilitiesBaseType));
        bindings.put(OWS.ContactType,
                new ComplexEMFBinding(Ows20Factory.eINSTANCE, OWS.ContactType));
        bindings.put(OWS.ContentsBaseType, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.ContentsBaseType));
        bindings.put(OWS.DatasetDescriptionSummaryBaseType, new ComplexEMFBinding(
                Ows20Factory.eINSTANCE, OWS.DatasetDescriptionSummaryBaseType));
        bindings.put(OWS.DescriptionType, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.DescriptionType));
        bindings.put(OWS.DomainType, new ComplexEMFBinding(Ows20Factory.eINSTANCE, OWS.DomainType));
        bindings.put(OWS.ExceptionType, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.ExceptionType));
        bindings.put(OWS.GetCapabilitiesType, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.GetCapabilitiesType));
        bindings.put(OWS.GetResourceByIdType, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.GetResourceByIdType));
        bindings.put(OWS.IdentificationType, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.IdentificationType));
        bindings.put(OWS.KeywordsType, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.KeywordsType));
        bindings.put(OWS.ManifestType, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.ManifestType));
        bindings.put(OWS.MetadataType, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.MetadataType));
        bindings.put(OWS.NilValueType, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.NilValueType));
        bindings.put(OWS.OnlineResourceType, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.OnlineResourceType));
        bindings.put(OWS.PositionType, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.PositionType));
        bindings.put(OWS.PositionType2D, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.PositionType2D));
        bindings.put(OWS.RangeType, new ComplexEMFBinding(Ows20Factory.eINSTANCE, OWS.RangeType));
        bindings.put(OWS.ReferenceGroupType, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.ReferenceGroupType));
        bindings.put(OWS.ReferenceType, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.ReferenceType));
        bindings.put(OWS.RequestMethodType, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.RequestMethodType));
        bindings.put(OWS.ResponsiblePartySubsetType, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.ResponsiblePartySubsetType));
        bindings.put(OWS.ResponsiblePartyType, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.ResponsiblePartyType));
        bindings.put(OWS.SectionsType, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.SectionsType));
        bindings.put(OWS.ServiceReferenceType, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.ServiceReferenceType));
        bindings.put(OWS.TelephoneType, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.TelephoneType));
        bindings.put(OWS.UnNamedDomainType, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.UnNamedDomainType));
        bindings.put(OWS.UpdateSequenceType, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.UpdateSequenceType));
        bindings.put(OWS.WGS84BoundingBoxType, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.WGS84BoundingBoxType));
        bindings.put(OWS._AdditionalParameter, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS._AdditionalParameter));
        bindings.put(OWS._AllowedValues, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS._AllowedValues));
        bindings.put(OWS._AnyValue, new ComplexEMFBinding(Ows20Factory.eINSTANCE, OWS._AnyValue));
        bindings.put(OWS._DCP, new ComplexEMFBinding(Ows20Factory.eINSTANCE, OWS._DCP));
        bindings.put(OWS._ExceptionReport, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS._ExceptionReport));
        bindings.put(OWS._HTTP, new ComplexEMFBinding(Ows20Factory.eINSTANCE, OWS._HTTP));
        bindings.put(OWS._NoValues, new ComplexEMFBinding(Ows20Factory.eINSTANCE, OWS._NoValues));
        bindings.put(OWS._Operation, new ComplexEMFBinding(Ows20Factory.eINSTANCE, OWS._Operation));
        bindings.put(OWS._OperationsMetadata, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS._OperationsMetadata));
        bindings.put(OWS._ServiceIdentification, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS._ServiceIdentification));
        bindings.put(OWS._ServiceProvider, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS._ServiceProvider));
        bindings.put(OWS._ValuesReference, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS._ValuesReference));
        bindings.put(OWS._rangeClosure, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS._rangeClosure));
        bindings.put(OWS.CapabilitiesBaseType_Languages, new ComplexEMFBinding(
                Ows20Factory.eINSTANCE, OWS.CapabilitiesBaseType_Languages));
        bindings.put(OWS.GetCapabilitiesType_AcceptLanguages, new ComplexEMFBinding(
                Ows20Factory.eINSTANCE, OWS.GetCapabilitiesType_AcceptLanguages));
        bindings.put(OWS.Abstract, new ComplexEMFBinding(Ows20Factory.eINSTANCE, OWS.Abstract));
        bindings.put(OWS.AbstractMetaData, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.AbstractMetaData));
        bindings.put(OWS.AbstractReferenceBase, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.AbstractReferenceBase));
        bindings.put(OWS.AccessConstraints, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.AccessConstraints));
        bindings.put(OWS.AdditionalParameter, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.AdditionalParameter));
        bindings.put(OWS.AdditionalParameters, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.AdditionalParameters));
        bindings.put(OWS.AllowedValues, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.AllowedValues));
        bindings.put(OWS.AnyValue, new ComplexEMFBinding(Ows20Factory.eINSTANCE, OWS.AnyValue));
        bindings.put(OWS.AvailableCRS, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.AvailableCRS));
        bindings.put(OWS.BoundingBox,
                new ComplexEMFBinding(Ows20Factory.eINSTANCE, OWS.BoundingBox));
        bindings.put(OWS.ContactInfo,
                new ComplexEMFBinding(Ows20Factory.eINSTANCE, OWS.ContactInfo));
        bindings.put(OWS.DatasetDescriptionSummary, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.DatasetDescriptionSummary));
        bindings.put(OWS.DataType, new ComplexEMFBinding(Ows20Factory.eINSTANCE, OWS.DataType));
        bindings.put(OWS.DCP, new ComplexEMFBinding(Ows20Factory.eINSTANCE, OWS.DCP));
        bindings.put(OWS.DefaultValue, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.DefaultValue));
        bindings.put(OWS.Exception, new ComplexEMFBinding(Ows20Factory.eINSTANCE, OWS.Exception));
        bindings.put(OWS.ExceptionReport, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.ExceptionReport));
        bindings.put(OWS.ExtendedCapabilities, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.ExtendedCapabilities));
        bindings.put(OWS.Fees, new ComplexEMFBinding(Ows20Factory.eINSTANCE, OWS.Fees));
        bindings.put(OWS.GetCapabilities, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.GetCapabilities));
        bindings.put(OWS.GetResourceByID, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.GetResourceByID));
        bindings.put(OWS.HTTP, new ComplexEMFBinding(Ows20Factory.eINSTANCE, OWS.HTTP));
        bindings.put(OWS.Identifier, new ComplexEMFBinding(Ows20Factory.eINSTANCE, OWS.Identifier));
        bindings.put(OWS.IndividualName, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.IndividualName));
        bindings.put(OWS.InputData, new ComplexEMFBinding(Ows20Factory.eINSTANCE, OWS.InputData));
        bindings.put(OWS.Keywords, new ComplexEMFBinding(Ows20Factory.eINSTANCE, OWS.Keywords));
        bindings.put(OWS.Language, new ComplexEMFBinding(Ows20Factory.eINSTANCE, OWS.Language));
        bindings.put(OWS.Manifest, new ComplexEMFBinding(Ows20Factory.eINSTANCE, OWS.Manifest));
        bindings.put(OWS.MaximumValue, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.MaximumValue));
        bindings.put(OWS.Meaning, new ComplexEMFBinding(Ows20Factory.eINSTANCE, OWS.Meaning));
        bindings.put(OWS.Metadata, new ComplexEMFBinding(Ows20Factory.eINSTANCE, OWS.Metadata));
        bindings.put(OWS.MinimumValue, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.MinimumValue));
        bindings.put(OWS.nilValue, new ComplexEMFBinding(Ows20Factory.eINSTANCE, OWS.nilValue));
        bindings.put(OWS.NoValues, new ComplexEMFBinding(Ows20Factory.eINSTANCE, OWS.NoValues));
        bindings.put(OWS.Operation, new ComplexEMFBinding(Ows20Factory.eINSTANCE, OWS.Operation));
        bindings.put(OWS.OperationResponse, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.OperationResponse));
        bindings.put(OWS.OperationsMetadata, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.OperationsMetadata));
        bindings.put(OWS.OrganisationName, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.OrganisationName));
        bindings.put(OWS.OtherSource,
                new ComplexEMFBinding(Ows20Factory.eINSTANCE, OWS.OtherSource));
        bindings.put(OWS.PointOfContact, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.PointOfContact));
        bindings.put(OWS.PositionName, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.PositionName));
        bindings.put(OWS.Range, new ComplexEMFBinding(Ows20Factory.eINSTANCE, OWS.Range));
        bindings.put(OWS.Reference, new ComplexEMFBinding(Ows20Factory.eINSTANCE, OWS.Reference));
        bindings.put(OWS.ReferenceGroup, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.ReferenceGroup));
        bindings.put(OWS.ReferenceSystem, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.ReferenceSystem));
        bindings.put(OWS.Resource, new ComplexEMFBinding(Ows20Factory.eINSTANCE, OWS.Resource));
        bindings.put(OWS.Role, new ComplexEMFBinding(Ows20Factory.eINSTANCE, OWS.Role));
        bindings.put(OWS.ServiceIdentification, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.ServiceIdentification));
        bindings.put(OWS.ServiceProvider, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.ServiceProvider));
        bindings.put(OWS.ServiceReference, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.ServiceReference));
        bindings.put(OWS.Spacing, new ComplexEMFBinding(Ows20Factory.eINSTANCE, OWS.Spacing));
        bindings.put(OWS.SupportedCRS, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.SupportedCRS));
        bindings.put(OWS.Title, new ComplexEMFBinding(Ows20Factory.eINSTANCE, OWS.Title));
        bindings.put(OWS.Value, new ComplexEMFBinding(Ows20Factory.eINSTANCE, OWS.Value));
        bindings.put(OWS.ValuesReference, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.ValuesReference));
        bindings.put(OWS.WGS84BoundingBox, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.WGS84BoundingBox));
        bindings.put(OWS.rangeClosure, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
                OWS.rangeClosure));
        bindings.put(OWS.reference, new ComplexEMFBinding(Ows20Factory.eINSTANCE, OWS.reference));

        // bindings.put(OWS.ServiceType, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
        // OWS.ServiceType));
        // bindings.put(OWS.VersionType,
        // new ComplexEMFBinding(Ows20Factory.eINSTANCE, OWS.VersionType));
        // bindings.put(OWS.OutputFormat, new ComplexEMFBinding(Ows20Factory.eINSTANCE,
        // OWS.OutputFormat));
        // bindings.put(OWS.MimeType, new ComplexEMFBinding(Ows20Factory.eINSTANCE, OWS.MimeType));

    }

    protected void configureContext(MutablePicoContainer container) {
        container.registerComponentInstance(Ows20Factory.eINSTANCE);
    }

    /**
     * Generates the bindings registrations for this class
     * 
     * @param args
     */
    public static void main(String[] args) {
        for (Field f : OWS.class.getFields()) {
            if ((f.getModifiers() & (Modifier.STATIC | Modifier.FINAL)) != 0
                    && f.getType().equals(QName.class)) {
                System.out.println("bindings.put(OWS." + f.getName()
                        + ", new ComplexEMFBinding(Ows20Factory.eINSTANCE, OWS." + f.getName()
                        + "));");
            }
        }
    }
}