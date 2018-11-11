/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import helpers.Func;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author umarmukhtar
 */
public class FMController {
    
    public static ArrayList<ArrayList<String>> dataList = new ArrayList<ArrayList<String>>();
    
    public static boolean setData(String file) {
        boolean status = true;
        try {
            
            BufferedReader in = new BufferedReader(new FileReader(file));
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            NodeList nList = doc.getElementsByTagName("feature_tree");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node NNODE = nList.item(temp);
                if (NNODE.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) NNODE;
                    
                    NamedNodeMap attributes = eElement.getAttributes();
                    Node carsNode = eElement.getFirstChild();
                    String numAttrs = carsNode.getNodeValue();
                    String[] outputs1 = numAttrs.split(":");
                    if (outputs1.length > 1) {
                        for (int i = 1; i < outputs1.length; i++) {
                            String output = "-";
                            try {
                                String output1 = outputs1[i];
                                String[] outputs2 = output1.split("\\(");
                                String output2 = (null != outputs2[0]) ? (outputs2[0]) : ("-");
                                String[] outputs3 = output2.split("r |m |g |o | ");
                                output = (null != outputs3[1]) ? (outputs3[1]) : ("-");
                            } catch (Exception e) {
                                e.printStackTrace();
                                output = "-";
                            }
                            ArrayList<String> dataDetail = new ArrayList<String>();
                            dataDetail.add(output);
                            dataList.add(dataDetail);
                        }
                    }
                }
            }
            
            String data[][] = new String[dataList.size()][2];
            for (int index = 0; index < dataList.size(); index++) {
                data[index][0] = (index + 1) + ".";
                data[index][1] = dataList.get(index).get(0);
            }
            String columns[] = {"No.", "Name"};
            Func.setTable(columns, data);
            
        } catch (Exception ex) {
            status = false;
            ex.printStackTrace();
        }
        return status;
    }
}
