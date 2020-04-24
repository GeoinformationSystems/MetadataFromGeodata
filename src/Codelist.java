/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Codelist {
    String filename;

    public Codelist(String filename) {
        this.filename = filename;
    }

    public List<List<String>> getEntries() {
        List<String> root = new ArrayList<>();
        List<String> entries = new ArrayList<>();

        try {
            Document codelistDoc = new SAXBuilder().build(filename);
            Element codelistRoot = codelistDoc.getRootElement();
            List<Element> codelistElements = codelistRoot.getChildren("entry");

            root.add(codelistRoot.getName());
            root.add(codelistRoot.getAttribute("codespace").getValue());

            for (Element entry : codelistElements) {
                List<Element> entryCode = entry.getChildren("code");
                entries.add(entryCode.get(0).getValue());
            }

        }
        catch (IOException | JDOMException ex) {
            System.out.println(ex.getMessage());
        }

        List<List<String>> outList = new ArrayList<>();
        outList.add(root);
        outList.add(entries);
        return outList;
    }
}
