package xml_manage;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import data_structure.BSearch;
import data_structure.BSearchNode;
import org.w3c.dom.*;

import java.util.ArrayList;

/**
 * This class was made as an XMLController for managing these type of files.
 */
public class XMLController {

    public DocumentBuilderFactory factory;
    public DocumentBuilder builder;
    public Document doc;

    public XMLController(){
        try {
            factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
            doc = builder.newDocument();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
     /**
     * Generate the initial xml file for admins
     */
    
    public void generate_initial_admin(){

        Element root = doc.createElement("admins");
        doc.appendChild(root);

        Element admin = doc.createElement("admin");
        root.appendChild(admin);

        Element user = doc.createElement("name");
        user.appendChild(doc.createTextNode("admin"));
        admin.appendChild(user);

        Element password = doc.createElement("password");
        password.appendChild(doc.createTextNode("admin"));
        admin.appendChild(password);

        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT,"yes");
            DOMSource source = new DOMSource(doc);

            StreamResult result = new StreamResult("src/main/java/xml_manage/admins.xml");
            transformer.transform(source, result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    
    /**
     * Generate the initial xml file for users
     */
    
    public void generate_inital_user(){

        Element root = doc.createElement("users");
        doc.appendChild(root);

        Element user = doc.createElement("user");
        root.appendChild(user);

        Element name = doc.createElement("name");
        name.appendChild(doc.createTextNode("user"));
        user.appendChild(name);

        Element password = doc.createElement("password");
        password.appendChild(doc.createTextNode("user"));
        user.appendChild(password);

        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT,"yes");
            DOMSource source = new DOMSource(doc);

            StreamResult result = new StreamResult("src/main/java/xml_manage/users.xml");
            transformer.transform(source, result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
     /**
     * Load data from xml file
     * @param xml
     * @param type
     * @return BSearch
     */
    public BSearch load_data(String xml, String type){

        BSearch tree = new BSearch();

        //load xml file
        try {
            this.factory = DocumentBuilderFactory.newInstance();
            this.builder = this.factory.newDocumentBuilder();
            this.doc = this.builder.parse(xml);
            this.doc.getDocumentElement().normalize();
            NodeList nList = this.doc.getElementsByTagName(type);
            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);
                Element eElement = (Element) nNode;
                tree.insert(eElement.getElementsByTagName("name").item(0).getTextContent(), eElement.getElementsByTagName("password").item(0).getTextContent());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return tree;

    }
    
     /**
     * Save data to xml file
     * @param xml
     * @param type
     * @param tree
     */
    public void save_data(String xml, String type, BSearch tree) {
        try {

            this.doc = this.builder.newDocument();

            ArrayList<BSearchNode> list = tree.getNodes();

            // Agregar nuevos elementos al documento
            Element root = doc.createElement(type + "s");
            doc.appendChild(root);

            for (BSearchNode node : list) {
                Element element = doc.createElement(type);
                root.appendChild(element);

                Element user = doc.createElement("name");
                user.appendChild(doc.createTextNode(node.user));
                element.appendChild(user);

                Element password = doc.createElement("password");
                password.appendChild(doc.createTextNode(node.password));
                element.appendChild(password);
            }

            // Guardar el documento en el disco duro
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT,"yes");
            DOMSource source = new DOMSource(doc);

            StreamResult result = new StreamResult(xml);
            transformer.transform(source, result);


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
