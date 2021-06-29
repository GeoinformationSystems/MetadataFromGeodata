/*
 * @author: Michael Wagner
 * @organization: TU Dresden
 * @contact: michael.wagner@tu-dresden.de
 */

package org.geokur.GeoDCAT;

import java.util.HashMap;
import java.util.Map;

public class NS {
    // namespaces from GeoDCAT
    // source: https://semiceu.github.io/GeoDCAT-AP/drafts/latest/

    public static final String ADMS = "http://www.w3.org/ns/adms#";
    public static final String CNT = "http://www.w3.org/2011/content#";
    public static final String DC = "http://purl.org/dc/elements/1.1/";
    public static final String DCAT = "http://www.w3.org/ns/dcat#";
    public static final String DCATAP = "http://data.europa.eu/r5r/";
    public static final String DCT = "http://purl.org/dc/terms/";
    public static final String DCTYPE = "http://purl.org/dc/dcmitype/";
    public static final String DQV = "http://www.w3.org/ns/dqv#";
    public static final String FOAF = "http://xmlns.com/foaf/0.1/";
    public static final String GEODCAT = "http://data.europa.eu/930/";
    public static final String GSP = "http://www.opengis.net/ont/geosparql#";
    public static final String LOCN = "http://www.w3.org/ns/locn#";
    public static final String ODRL = "http://www.w3.org/ns/odrl/2/";
    public static final String ORG = "http://www.w3.org/ns/org#";
    public static final String OWL = "http://www.w3.org/2002/07/owl#";
    public static final String PROV = "http://www.w3.org/ns/prov#";
    public static final String RDF = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
    public static final String RDFS = "http://www.w3.org/2000/01/rdf-schema#";
    public static final String SCHEMA = "http://schema.org/";
    public static final String SDMX_ATTRIBUTE = "http://purl.org/linked-data/sdmx/2009/attribute#";
    public static final String SKOS = "http://www.w3.org/2004/02/skos/core#";
    public static final String SPDX = "http://spdx.org/rdf/terms#";
    public static final String TIME = "http://www.w3.org/2006/time#";
    public static final String VCARD = "http://www.w3.org/2006/vcard/ns#";
    public static final String XSD = "http://www.w3.org/2001/XMLSchema#";
    public static final String GEOKUR = "https://geokur-dmp.geo.tu-dresden.de/"; //TODO: adjust if better url is available
    public static final String GEOQUAL = "https://geokur-dmp.geo.tu-dresden.de/pages/quality-elements#";


    public static Map<String, String> getNS() {
        // map with all namespaces for convenience

        Map<String, String> nsMap = new HashMap<>();

        nsMap.put("adms", NS.ADMS);
        nsMap.put("cnt", NS.CNT);
        nsMap.put("dc", NS.DC);
        nsMap.put("dcat", NS.DCAT);
        nsMap.put("dcatap", NS.DCATAP);
        nsMap.put("dct", NS.DCT);
        nsMap.put("dctype", NS.DCTYPE);
        nsMap.put("dqv", NS.DQV);
        nsMap.put("foaf", NS.FOAF);
        nsMap.put("geodcat", NS.GEODCAT);
        nsMap.put("gsp", NS.GSP);
        nsMap.put("locn", NS.LOCN);
        nsMap.put("odrl", NS.ODRL);
        nsMap.put("org", NS.ORG);
        nsMap.put("owl", NS.OWL);
        nsMap.put("prov", NS.PROV);
        nsMap.put("rdf", NS.RDF);
        nsMap.put("rdfs", NS.RDFS);
        nsMap.put("schema", NS.SCHEMA);
        nsMap.put("sdmx_attribute", NS.SDMX_ATTRIBUTE);
        nsMap.put("skos", NS.SKOS);
        nsMap.put("spdx", NS.SPDX);
        nsMap.put("time", NS.TIME);
        nsMap.put("vcard", NS.VCARD);
        nsMap.put("xsd", NS.XSD);
        nsMap.put("geokur", NS.GEOKUR);
        nsMap.put("geoqual", NS.GEOQUAL);

        return nsMap;
    }

}
