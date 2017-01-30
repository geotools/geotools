NetCDF
======


##IWC component 1 and Climatological time
###Sample data

The climatological.zip sample data contains data which has been derived through NetCDF operators from data available at 
[United Kingdom Hydrographic Office website](http://www.ukho.gov.uk/Defence/AML/Pages/Home.aspx) on Sample and Test Data section, as 
[Test Data for IWC Component 1](http://www.ukho.gov.uk/Defence/AML/Documents/Item%2019%20Web%20-%20IWC_comp1_v2_demodata.zip)

Such a zip contains public sector information licensed under the [Open Government Licence v3.0](http://www.nationalarchives.gov.uk/doc/open-government-licence/version/3/).

Permission was obtained to include the data within the GeoTools repository from the **Defence Standards Manager, DMGIC, UKHO**

###Support
Climatological time on NetCDF sample is not exposed as a classic CF time variable with the **units** attribute like **seconds(/hours) since timeorigin** or something similar. 
It is instead a dimension with Strings formatted as **CCYYMMddHHmmss** representing climatological times of an undefined year.

A coordinateVariable handling mechanism based on plugins has been added in order to support this peculiar time variable as well as to allow defining future variables handlers.

In order to activate the plugins search, make sure to define a **netcdf.coordinates.enablePlugins=true** system property.
