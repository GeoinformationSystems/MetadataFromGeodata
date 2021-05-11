# Example Datasets and Metadata used for AGILE 2021

This folder contains the properties file, datasets, and metadata used for the AGILE 2021 short paper:

Michael Wagner, Christin Henzen, Ralph Müller-Pfefferkorn: A Research Data Infrastructure Component for the Automated Metadata and Data Quality Extraction to Foster the Provision of FAIR Data in Earth System Sciences.

- `propertiesMetaAGILE2021.txt`: properties file to control metadataFromGeodata
- `EurostatPreprocessed.csv`: original data comes from Eurostat, is owned by the EU and can be retrieved from https://appsso.eurostat.ec.europa.eu/nui/show.do?dataset=apro_cpsh1&lang=en). The original dataset is licensed under Creative Commons Attribution 4.0 International (CC BY 4.0) licence (http://creativecommons.org/licenses/by/4.0/). This dataset is preprocessed for use with metadataFromGeodata and in order to harmonize the country names:
  - all double quotes removed
  - all occurrences of "European Union (EU6-1958, EU9-1973, EU10-1981, EU12-1986, EU15-1995, EU25-2004, EU27-2007, EU28-2013, EU27-2020)" removed
  - "Czechia" changed to "Czech Republic"
  - "Germany (until 1990 former territory of the FRG)" changed to "Germany"
  - "Kosovo (under United Nations Security Council Resolution 1244/99)" changed to "Kosovo"
  - "North Macedonia" changed to "Macedonia"
- `EurostatMetadata.xml`: metadata as ISO 19115 compliant XML for `EurostatPreprocessed.csv` processed with metadataFromGeodata
- `EurostatMetadata.db`: metadata as SQLite database for `EurostatPreprocessed.csv` processed with metadataFromGeodata
- `GADM36Metadata.xml`: metadata as ISO 19115 compliant XML for GADM V3.6 level 0 (nation level) data (retrievable from https://gadm.org/)
- `GADM36Metadata.db`: metadata as SQLite database for GADM V3.6 level 0

Due to licensing we are not allowed to redistribute parts of GADM V3.6 data. They can be retrieved from https://gadm.org/. A necessary change in order to make it work together with the Eurostat data is to add polygons for the European Union with 28/27 countries.
