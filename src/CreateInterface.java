//package ISO19115;

import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

public interface CreateInterface {
//    Element getElement(Map<String, Namespace> ns, String configFile, NestedMap<String, List<String>> content, List<String> elementChain, int indexChain, Writer logFileWriter) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException, JDOMException, IOException;
    Element getElement(Map<String, Namespace> ns, String configFile, Element content, List<String> elementChain, int indexChain, Writer logFileWriter) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException, JDOMException, IOException;
}
