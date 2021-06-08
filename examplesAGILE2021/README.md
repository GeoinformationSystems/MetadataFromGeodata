# Example Datasets and Metadata used for AGILE 2021

This folder contains the properties file, datasets, and metadata used for the AGILE 2021 short paper:

Wagner, M., Henzen, C., and Müller-Pfefferkorn, R.: A Research Data Infrastructure Component for the Automated Metadata and Data Quality Extraction to Foster the Provision of FAIR Data in Earth System Sciences,
AGILE GIScience Ser., 2, 41, https://doi.org/10.5194/agile-giss-2-41-2021, 2021.


## Content

- `metadataFromGeodata.jar`: executable jar file of metadataFromGeodata tool
- `propertiesMetaAGILE2021_Eurostat.txt`: properties file to control metadataFromGeodata for Eurostat tabular data
- `propertiesMetaAGILE2021_GADM36.txt`: properties file to control metadataFromGeodata for GADM data geopackage
- `propertiesMetaAGILE2021_GADM36whole.txt`: properties file to control metadataFromGeodata for whole GADM data geopackage
- `propertiesMetaAGILE2021_MapSPAM.txt`: properties file to control metadataFromGeodata for MapSPAM data
- `EurostatPreprocessed.csv`: original data comes from Eurostat, is owned by the EU and can be retrieved from https://appsso.eurostat.ec.europa.eu/nui/show.do?dataset=apro_cpsh1&lang=en.
  The original dataset is licensed under Creative Commons Attribution 4.0 International (CC BY 4.0) licence (http://creativecommons.org/licenses/by/4.0/).
  This dataset is preprocessed for use with metadataFromGeodata and in order to harmonize the country names:
  - all double quotes removed
  - all occurrences of "European Union (EU6-1958, EU9-1973, EU10-1981, EU12-1986, EU15-1995, EU25-2004, EU27-2007, EU28-2013, EU27-2020)" removed
  - "Czechia" changed to "Czech Republic"
  - "Germany (until 1990 former territory of the FRG)" changed to "Germany"
  - "Kosovo (under United Nations Security Council Resolution 1244/99)" changed to "Kosovo"
  - "North Macedonia" changed to "Macedonia"
- `spam2010V2r0_global_Y_WHEA_A.tif`: Wheat yield from Spatial Production Allocation Model - original data is available on https://www.mapspam.info/.
  The original dataset is licensed under Creative Commons Attribution-NonCommercial 3.0 Unported (CC BY-NC 3.0) licence (https://creativecommons.org/licenses/by-nc/3.0/)
- `EurostatMetadata.xml`: metadata as ISO 19115 compliant XML for `EurostatPreprocessed.csv` processed with metadataFromGeodata
- `EurostatMetadata.db`: metadata as SQLite database for `EurostatPreprocessed.csv` processed with metadataFromGeodata
- `GADM36Metadata.xml`: metadata as ISO 19115 compliant XML for GADM V3.6 level 0 (nation level) data (retrievable from https://gadm.org/)
- `GADM36Metadata.db`: metadata as SQLite database for GADM V3.6 level 0
- `GADM36wholeMetadata.xml`: metadata as ISO 19115 compliant XML for GADM V3.6 for all levels (retrievable with no change from https://gadm.org/)
- `GADM36wholeMetadata.db`: metadata as SQLite database for GADM V3.6 for all levels
- `spam2010V2r0_global_Y_WHEA_A.xml`: metadata as ISO 19115 compliant XML for wheat yields from MapSPAM
- `spam2010V2r0_global_Y_WHEA_A.db`: metadata as SQLite database for wheat yields from MapSPAM

Due to licensing we are not allowed to redistribute parts of GADM V3.6 data. They can be retrieved from https://gadm.org/.
A necessary change in order to make it work together with the Eurostat data is to add polygons for the European Union with 28/27 countries.


## Usage

The tool can currently be used from the command line with:  
`java -jar metadataFromGeodata.jar propertiesFileName`

In the case of the whole GADM dataset a relatively large Java memory allocation pool is needed. Depending on individual settings the following command allows the tool 8 GB of RAM:
`java -Xmx8G -jar metadataFromGeodata.jar propertiesFileName`