package com.vitdev;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

/**
 * Created by vitaly on 12/9/15.
 */
public class Main {
    public static void main(String[] args) {
        try {
            //new Main().parseXML(newXML,oldXML);
            new Main().parseXML("xml/server_strings.xml", "xml/temporary_strings.xml");
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }

    private void parseXML(String newFilePath, String oldFilePath) throws ParserConfigurationException, IOException, SAXException {
        File newFile = new File(newFilePath);
        File oldFile = new File(oldFilePath);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document newFileDoc = builder.parse(newFile);
        Element newFileResources = newFileDoc.getDocumentElement();

        Document oldFileDoc = builder.parse(oldFile);
        Element oldFileResources = oldFileDoc.getDocumentElement();

        NodeList newList = newFileResources.getElementsByTagName("string");
        NodeList oldList = oldFileResources.getElementsByTagName("string");

        int count = 0;

        for (int i = 0; i < newList.getLength(); i++) {
            for (int j = 0; j < oldList.getLength(); j++) {
                if (((Element) newList.item(i)).getAttribute("name").equals(((Element) oldList.item(j)).getAttribute("name"))) {

                    Node deleted = oldFileResources.removeChild(oldList.item(j));
                    oldFileResources.normalize();
                    System.out.println(deleted.getTextContent());
                    count++;
                    break;
                }
            }
        }
        System.out.println(count);

        if (count > 0) {

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = null;
            try {
                transformer = transformerFactory.newTransformer();
            } catch (TransformerConfigurationException e) {
                e.printStackTrace();
            }
            DOMSource domSource = new DOMSource(oldFileDoc);
            StreamResult streamResult = new StreamResult(oldFilePath);

            try {
                transformer.transform(domSource, streamResult);
            } catch (TransformerException e) {
                e.printStackTrace();
            }

        }

    }
}
