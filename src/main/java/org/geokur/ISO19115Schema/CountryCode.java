/*
 * Copyright 2020
 * @author: Michael Wagner
 */

package org.geokur.ISO19115Schema;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "CountryCode")
public class CountryCode {
    // codelist - can be extended

    public enum CountryCodes {
        AF ("Afghanistan", "The Islamic Republic of Afghanistan", "AFG", 4),
        AD ("Andorra", "The Principality of Andorra", "AND", 20),
        AE ("The United Arab Emirates", "The United Arab Emirates", "ARE", 784),
        AG ("Antigua and Barbuda", "Antigua and Barbuda", "ATG", 28),
        AI ("Anguilla", "Anguilla", "AIA", 660),
        AL ("Albania", "The Republic of Albania", "ALB", 8),
        AM ("Armenia", "The Republic of Armenia", "ARM", 51),
        AO ("Angola", "The Republic of Angola", "AGO", 24),
        AQ ("Antarctica", "All land and ice shelves south of the 60th parallel south", "ATA", 10),
        AR ("Argentina", "The Argentine Republic", "ARG", 32),
        AS ("American Samoa", "The Territory of American Samoa", "ASM", 16),
        AT ("Austria", "The Republic of Austria", "AUT", 40),
        AU ("Australia", "The Commonwealth of Australia", "AUS", 36),
        AW ("Aruba", "Aruba", "ABW", 533),
        AX ("Åland Islands", "Åland", "ALA", 248),
        AZ ("Azerbaijan", "The Republic of Azerbaijan", "AZE", 31),
        BA ("Bosnia and Herzegovina", "Bosnia and Herzegovina", "BIH", 70),
        BB ("Barbados", "Barbados", "BRB", 52),
        BD ("Bangladesh", "The People's Republic of Bangladesh", "BGD", 50),
        BE ("Belgium", "The Kingdom of Belgium", "BEL", 56),
        BF ("Burkina Faso", "Burkina Faso", "BFA", 854),
        BG ("Bulgaria", "The Republic of Bulgaria", "BGR", 100),
        BH ("Bahrain", "The Kingdom of Bahrain", "BHR", 48),
        BI ("Burundi", "The Republic of Burundi", "BDI", 108),
        BJ ("Benin", "The Republic of Benin", "BEN", 204),
        BL ("Saint Barthélemy", "The Collectivity of Saint-Barthélemy", "BLM", 652),
        BM ("Bermuda", "Bermuda", "BMU", 60),
        BN ("Brunei Darussalam", "The Nation of Brunei, the Abode of Peace", "BRN", 96),
        BO ("Bolivia", "The Plurinational State of Bolivia", "BOL", 68),
        BQ ("Saba", "Bonaire, Sint Eustatius and Saba", "BES", 535),
        BR ("Brazil", "The Federative Republic of Brazil", "BRA", 76),
        BS ("Bahamas", "The Commonwealth of The Bahamas", "BHS", 44),
        BT ("Bhutan", "The Kingdom of Bhutan", "BTN", 64),
        BV ("Bouvet Island", "Bouvet Island", "BVT", 74),
        BW ("Botswana", "The Republic of Botswana", "BWA", 72),
        BY ("Belarus", "The Republic of Belarus", "BLR", 112),
        BZ ("Belize", "Belize", "BLZ", 84),
        CA ("Canada", "Canada", "CAN", 124),
        CC ("The Cocos (Keeling) Islands", "The Territory of Cocos (Keeling) Islands", "CCK", 166),
        CD ("Democratic Republic of the Congo", "The Democratic Republic of the Congo", "COD", 180),
        CF ("Central African Republic", "The Central African Republic", "CAF", 140),
        CG ("Republic of the Congo", "The Republic of the Congo", "COG", 178),
        CH ("Switzerland", "The Swiss Confederation", "CHE", 756),
        CI ("Côte d'Ivoire ", "The Republic of Côte d'Ivoire", "CIV", 384),
        CK ("Cook Islands", "The Cook Islands", "COK", 184),
        CL ("Chile", "The Republic of Chile", "CHL", 152),
        CM ("Cameroon", "The Republic of Cameroon", "CMR", 120),
        CN ("China", "The People's Republic of China", "CHN", 156),
        CO ("Colombia", "The Republic of Colombia", "COL", 170),
        CR ("Costa Rica", "The Republic of Costa Rica", "CRI", 188),
        CU ("Cuba", "The Republic of Cuba", "CUB", 192),
        CV ("Cape Verde", "The Republic of Cabo Verde", "CPV", 132),
        CW ("Curaçao", "The Country of Curaçao", "CUW", 531),
        CX ("Christmas Island", "The Territory of Christmas Island", "CXR", 162),
        CY ("Cyprus", "The Republic of Cyprus", "CYP", 196),
        CZ ("Czech Republic", "The Czech Republic", "CZE", 203),
        DE ("Germany", "The Federal Republic of Germany", "DEU", 276),
        DJ ("Djibouti", "The Republic of Djibouti", "DJI", 262),
        DK ("Denmark", "The Kingdom of Denmark", "DNK", 208),
        DM ("Dominica", "The Commonwealth of Dominica", "DMA", 212),
        DO ("Dominican Republic", "The Dominican Republic", "DOM", 214),
        DZ ("Algeria", "The People's Democratic Republic of Algeria", "DZA", 12),
        EC ("Ecuador", "The Republic of Ecuador", "ECU", 218),
        EE ("Estonia", "The Republic of Estonia", "EST", 233),
        EG ("Egypt", "The Arab Republic of Egypt", "EGY", 818),
        EH ("Western Sahara", "The Sahrawi Arab Democratic Republic", "ESH", 732),
        ER ("Eritrea", "The State of Eritrea", "ERI", 232),
        ES ("Spain", "The Kingdom of Spain", "ESP", 724),
        ET ("Ethiopia", "The Federal Democratic Republic of Ethiopia", "ETH", 231),
        FI ("Finland", "The Republic of Finland", "FIN", 246),
        FJ ("Fiji", "The Republic of Fiji", "FJI", 242),
        FK ("Falkland Islands (Malvinas)", "The Falkland Islands", "FLK", 238),
        FM ("Federated States of Micronesia", "The Federated States of Micronesia", "FSM", 583),
        FO ("Faroe Islands", "The Faroe Islands", "FRO", 234),
        FR ("France", "The French Republic", "FRA", 250),
        GA ("Gabon", "The Gabonese Republic", "GAB", 266),
        GB ("United Kingdom of Great Britain and Northern Ireland", "The United Kingdom of Great Britain and Northern Ireland", "GBR", 826),
        GD ("Grenada", "Grenada", "GRD", 308),
        GE ("Georgia", "Georgia", "GEO", 268),
        GF ("French Guiana", "Guyane", "GUF", 254),
        GG ("Bailiwick of Guernsey", "The Bailiwick of Guernsey", "GGY", 831),
        GH ("Ghana", "The Republic of Ghana", "GHA", 288),
        GI ("Gibraltar", "Gibraltar", "GIB", 292),
        GL ("Greenland", "Kalaallit Nunaat", "GRL", 304),
        GM ("Gambia", "The Republic of The Gambia", "GMB", 270),
        GN ("Guinea", "The Republic of Guinea", "GIN", 324),
        GP ("Guadeloupe", "Guadeloupe", "GLP", 312),
        GQ ("Equatorial Guinea", "The Republic of Equatorial Guinea", "GNQ", 226),
        GR ("Greece", "The Hellenic Republic", "GRC", 300),
        GS ("South Georgia and the South Sandwich Islands", "South Georgia and the South Sandwich Islands", "SGS", 239),
        GT ("Guatemala", "The Republic of Guatemala", "GTM", 320),
        GU ("Guam", "The Territory of Guam", "GUM", 316),
        GW ("Guinea-Bissau", "The Republic of Guinea-Bissau", "GNB", 624),
        GY ("Guyana", "The Co-operative Republic of Guyana", "GUY", 328),
        HK ("Hong Kong", "The Hong Kong Special Administrative Region of China", "HKG", 344),
        HM ("Heard Island and McDonald Islands", "The Territory of Heard Island and McDonald Islands", "HMD", 334),
        HN ("Honduras", "The Republic of Honduras", "HND", 340),
        HR ("Croatia", "The Republic of Croatia", "HRV", 191),
        HT ("Haiti", "The Republic of Haiti", "HTI", 332),
        HU ("Hungary", "Hungary", "HUN", 348),
        ID ("Indonesia", "The Republic of Indonesia", "IDN", 360),
        IE ("Republic of Ireland", "Ireland", "IRL", 372),
        IL ("Israel", "The State of Israel", "ISR", 376),
        IM ("Isle of Man", "The Isle of Man", "IMN", 833),
        IN ("India", "The Republic of India", "IND", 356),
        IO ("British Indian Ocean Territory", "The British Indian Ocean Territory", "IOT", 86),
        IQ ("Iraq", "The Republic of Iraq", "IRQ", 368),
        IR ("Iran", "The Islamic Republic of Iran", "IRN", 364),
        IS ("Iceland", "Iceland", "ISL", 352),
        IT ("Italy", "The Italian Republic", "ITA", 380),
        JE ("Jersey", "The Bailiwick of Jersey", "JEY", 832),
        JM ("Jamaica", "Jamaica", "JAM", 388),
        JO ("Jordan", "The Hashemite Kingdom of Jordan", "JOR", 400),
        JP ("Japan", "Japan", "JPN", 392),
        KE ("Kenya", "The Republic of Kenya", "KEN", 404),
        KG ("Kyrgyzstan", "The Kyrgyz Republic", "KGZ", 417),
        KH ("Cambodia", "The Kingdom of Cambodia", "KHM", 116),
        KI ("Kiribati", "The Republic of Kiribati", "KIR", 296),
        KM ("Comoros", "The Union of the Comoros", "COM", 174),
        KN ("Saint Kitts and Nevis", "Saint Kitts and Nevis", "KNA", 659),
        KP ("North Korea", "The Democratic People's Republic of Korea", "PRK", 408),
        KR ("South Korea", "The Republic of Korea", "KOR", 410),
        KW ("Kuwait", "The State of Kuwait", "KWT", 414),
        KY ("Cayman Islands", "The Cayman Islands", "CYM", 136),
        KZ ("Kazakhstan", "The Republic of Kazakhstan", "KAZ", 398),
        LA ("Laos", "The Lao People's Democratic Republic", "LAO", 418),
        LB ("Lebanon", "The Lebanese Republic", "LBN", 422),
        LC ("Saint Lucia", "Saint Lucia", "LCA", 662),
        LI ("Liechtenstein", "The Principality of Liechtenstein", "LIE", 438),
        LK ("Sri Lanka", "The Democratic Socialist Republic of Sri Lanka", "LKA", 144),
        LR ("Liberia", "The Republic of Liberia", "LBR", 430),
        LS ("Lesotho", "The Kingdom of Lesotho", "LSO", 426),
        LT ("Lithuania", "The Republic of Lithuania", "LTU", 440),
        LU ("Luxembourg", "The Grand Duchy of Luxembourg", "LUX", 442),
        LV ("Latvia", "The Republic of Latvia", "LVA", 428),
        LY ("Libya", "The State of Libya", "LBY", 434),
        MA ("Morocco", "The Kingdom of Morocco", "MAR", 504),
        MC ("Monaco", "The Principality of Monaco", "MCO", 492),
        MD ("Moldova", "The Republic of Moldova", "MDA", 498),
        ME ("Montenegro", "Montenegro", "MNE", 499),
        MF ("Collectivity of Saint Martin", "The Collectivity of Saint-Martin", "MAF", 663),
        MG ("Madagascar", "The Republic of Madagascar", "MDG", 450),
        MH ("Marshall Islands", "The Republic of the Marshall Islands", "MHL", 584),
        MK ("North Macedonia", "Republic of North Macedonia", "MKD", 807),
        ML ("Mali", "The Republic of Mali", "MLI", 466),
        MM ("Myanmar", "The Republic of the Union of Myanmar", "MMR", 104),
        MN ("Mongolia", "The State of Mongolia", "MNG", 496),
        MO ("Macau", "Macao Special Administrative Region of China", "MAC", 446),
        MP ("Northern Mariana Islands", "The Commonwealth of the Northern Mariana Islands", "MNP", 580),
        MQ ("Martinique", "Martinique", "MTQ", 474),
        MR ("Mauritania", "The Islamic Republic of Mauritania", "MRT", 478),
        MS ("Montserrat", "Montserrat", "MSR", 500),
        MT ("Malta", "The Republic of Malta", "MLT", 470),
        MU ("Mauritius", "The Republic of Mauritius", "MUS", 480),
        MV ("Maldives", "The Republic of Maldives", "MDV", 462),
        MW ("Malawi", "The Republic of Malawi", "MWI", 454),
        MX ("Mexico", "The United Mexican States", "MEX", 484),
        MY ("Malaysia", "Malaysia", "MYS", 458),
        MZ ("Mozambique", "The Republic of Mozambique", "MOZ", 508),
        NA ("Namibia", "The Republic of Namibia", "NAM", 516),
        NC ("New Caledonia", "New Caledonia", "NCL", 540),
        NE ("Niger", "The Republic of the Niger", "NER", 562),
        NF ("Norfolk Island", "The Territory of Norfolk Island", "NFK", 574),
        NG ("Nigeria", "The Federal Republic of Nigeria", "NGA", 566),
        NI ("Nicaragua", "The Republic of Nicaragua", "NIC", 558),
        NL ("Netherlands", "The Kingdom of the Netherlands", "NLD", 528),
        NO ("Norway", "The Kingdom of Norway", "NOR", 578),
        NP ("Nepal", "The Federal Democratic Republic of Nepal", "NPL", 524),
        NR ("Nauru", "The Republic of Nauru", "NRU", 520),
        NU ("Niue", "Niue", "NIU", 570),
        NZ ("New Zealand", "New Zealand", "NZL", 554),
        OM ("Oman", "The Sultanate of Oman", "OMN", 512),
        PA ("Panama", "The Republic of Panamá", "PAN", 591),
        PE ("Peru", "The Republic of Perú", "PER", 604),
        PF ("French Polynesia", "French Polynesia", "PYF", 258),
        PG ("Papua New Guinea", "The Independent State of Papua New Guinea", "PNG", 598),
        PH ("Philippines", "The Republic of the Philippines", "PHL", 608),
        PK ("Pakistan", "The Islamic Republic of Pakistan", "PAK", 586),
        PL ("Poland", "The Republic of Poland", "POL", 616),
        PM ("Saint Pierre and Miquelon", "The Overseas Collectivity of Saint-Pierre and Miquelon", "SPM", 666),
        PN ("Pitcairn Islands", "The Pitcairn, Henderson, Ducie and Oeno Islands", "PCN", 612),
        PR ("Puerto Rico", "The Commonwealth of Puerto Rico", "PRI", 630),
        PS ("State of Palestine", "The State of Palestine", "PSE", 275),
        PT ("Portugal", "The Portuguese Republic", "PRT", 620),
        PW ("Palau", "The Republic of Palau", "PLW", 585),
        PY ("Paraguay", "The Republic of Paraguay", "PRY", 600),
        QA ("Qatar", "The State of Qatar", "QAT", 634),
        RE ("Réunion", "Réunion", "REU", 638),
        RO ("Romania", "Romania", "ROU", 642),
        RS ("Serbia", "The Republic of Serbia", "SRB", 688),
        RU ("Russia", "The Russian Federation", "RUS", 643),
        RW ("Rwanda", "The Republic of Rwanda", "RWA", 646),
        SA ("Saudi Arabia", "The Kingdom of Saudi Arabia", "SAU", 682),
        SB ("Solomon Islands", "The Solomon Islands", "SLB", 90),
        SC ("Seychelles", "The Republic of Seychelles", "SYC", 690),
        SD ("Sudan", "The Republic of the Sudan", "SDN", 729),
        SE ("Sweden", "The Kingdom of Sweden", "SWE", 752),
        SG ("Singapore", "The Republic of Singapore", "SGP", 702),
        SH ("Tristan da Cunha", "Saint Helena, Ascension and Tristan da Cunha", "SHN", 654),
        SI ("Slovenia", "The Republic of Slovenia", "SVN", 705),
        SJ ("Jan Mayen", "Svalbard and Jan Mayen", "SJM", 744),
        SK ("Slovakia", "The Slovak Republic", "SVK", 703),
        SL ("Sierra Leone", "The Republic of Sierra Leone", "SLE", 694),
        SM ("San Marino", "The Republic of San Marino", "SMR", 674),
        SN ("Senegal", "The Republic of Senegal", "SEN", 686),
        SO ("Somalia", "The Federal Republic of Somalia", "SOM", 706),
        SR ("Suriname", "The Republic of Suriname", "SUR", 740),
        SS ("South Sudan", "The Republic of South Sudan", "SSD", 728),
        ST ("São Tomé and Príncipe", "The Democratic Republic of São Tomé and Príncipe", "STP", 678),
        SV ("El Salvador", "The Republic of El Salvador", "SLV", 222),
        SX ("Sint Maarten", "Sint Maarten", "SXM", 534),
        SY ("Syria", "The Syrian Arab Republic", "SYR", 760),
        SZ ("Eswatini", "The Kingdom of Eswatini", "SWZ", 748),
        TC ("Turks and Caicos Islands", "The Turks and Caicos Islands", "TCA", 796),
        TD ("Chad", "The Republic of Chad", "TCD", 148),
        TF ("French Southern and Antarctic Lands", "The French Southern and Antarctic Lands", "ATF", 260),
        TG ("Togo", "The Togolese Republic", "TGO", 768),
        TH ("Thailand", "The Kingdom of Thailand", "THA", 764),
        TJ ("Tajikistan", "The Republic of Tajikistan", "TJK", 762),
        TK ("Tokelau", "Tokelau", "TKL", 772),
        TL ("Timor-Leste", "The Democratic Republic of Timor-Leste", "TLS", 626),
        TM ("Turkmenistan", "Turkmenistan", "TKM", 795),
        TN ("Tunisia", "The Republic of Tunisia", "TUN", 788),
        TO ("Tonga", "The Kingdom of Tonga", "TON", 776),
        TR ("Turkey", "The Republic of Turkey", "TUR", 792),
        TT ("Trinidad and Tobago", "The Republic of Trinidad and Tobago", "TTO", 780),
        TV ("Tuvalu", "Tuvalu", "TUV", 798),
        TW ("Taiwan (Province of China)", "The Republic of China", "TWN", 158),
        TZ ("Tanzania", "The United Republic of Tanzania", "TZA", 834),
        UA ("Ukraine", "Ukraine", "UKR", 804),
        UG ("Uganda", "The Republic of Uganda", "UGA", 800),
        UM ("United States Minor Outlying Islands", "Baker Island, Howland Island, Jarvis Island, Johnston Atoll, Kingman Reef, Midway Atoll, Navassa Island, Palmyra Atoll, and Wake Island", "UMI", 581),
        US ("United States of America", "The United States of America", "USA", 840),
        UY ("Uruguay", "The Oriental Republic of Uruguay", "URY", 858),
        UZ ("Uzbekistan", "The Republic of Uzbekistan", "UZB", 860),
        VA ("Holy See", "The Holy See", "VAT", 336),
        VC ("Saint Vincent and the Grenadines", "Saint Vincent and the Grenadines", "VCT", 670),
        VE ("Venezuela", "The Bolivarian Republic of Venezuela", "VEN", 862),
        VG ("British Virgin Islands", "The Virgin Islands", "VGB", 92),
        VI ("United States Virgin Islands", "The Virgin Islands of the United States", "VIR", 850),
        VN ("Vietnam", "The Socialist Republic of Viet Nam", "VNM", 704),
        VU ("Vanuatu", "The Republic of Vanuatu", "VUT", 548),
        WF ("Wallis and Futuna", "The Territory of the Wallis and Futuna Islands", "WLF", 876),
        WS ("Samoa", "The Independent State of Samoa", "WSM", 882),
        YE ("Yemen", "The Republic of Yemen", "YEM", 887),
        YT ("Mayotte", "The Department of Mayotte", "MYT", 175),
        ZA ("South Africa", "The Republic of South Africa", "ZAF", 710),
        ZM ("Zambia", "The Republic of Zambia", "ZMB", 894),
        ZW ("Zimbabwe", "The Republic of Zimbabwe", "ZWE", 716);

        private final String countryName;
        private final String officialStateName;
        private final String alpha3Code;
        private final int numericCode;

        CountryCodes(String countryName, String officialStateName, String alpha3Code, int numericCode) {
            this.countryName = countryName;
            this.officialStateName = officialStateName;
            this.alpha3Code = alpha3Code;
            this.numericCode = numericCode;
        }

        public String getCountryName() {
            return countryName;
        }

        public String getOfficialStateName() {
            return officialStateName;
        }

        public String getAlpha3Code() {
            return alpha3Code;
        }

        public int getNumericCode() {
            return numericCode;
        }
    }

    @XmlAttribute
    final String codeList = "https://www.iso.org/obp/ui/#search/code/";

    @XmlAttribute
    public CountryCode.CountryCodes codeListValue;
    @XmlAttribute(name = "codeListValue")
    public String codeListValueNew;

    public CountryCode(){}

    public CountryCode(CountryCode.CountryCodes codeListValue) {
        this.codeListValue = codeListValue;
    }

    public CountryCode(String codeListValueNew) {
        this.codeListValueNew = codeListValueNew;
    }
}
