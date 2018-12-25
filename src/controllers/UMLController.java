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
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import views.MainPage;

/**
 *
 * @author umarmukhtar
 */
public class UMLController {

    public static ArrayList<ArrayList<String>> dataList = new ArrayList<ArrayList<String>>();
    public static ArrayList<ArrayList<String>> dataListStates = new ArrayList<ArrayList<String>>();
    public static ArrayList<ArrayList<String>> dataListTransitions = new ArrayList<ArrayList<String>>();

    public static boolean setData(String file) {
        boolean status = true;
        try {
            
            dataListStates.removeAll(dataListStates);
            dataListTransitions.removeAll(dataListTransitions);

            BufferedReader in = new BufferedReader(new FileReader(file));

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            NodeList nList1 = doc.getElementsByTagName("UML:SimpleState");
            NodeList nList2 = doc.getElementsByTagName("UML:Transition");
            int num1 = nList1.getLength();
            int num2 = nList2.getLength();

            MainPage.totalVertices = 0;
            int stateCodeCount = 1;
            for (int l = 0; l < num1; l++) {
                Element node1 = (Element) nList1.item(l);
                NamedNodeMap attributes = node1.getAttributes();
                int numAttrs = attributes.getLength();
                for (int i = 0; i < numAttrs; i++) {
                    Attr attr = (Attr) attributes.item(i);
                    String attrName = attr.getNodeName();
                    String attrValue = attr.getNodeValue();
                    if (attrName.equals("name")) {
                        // attrName - list of states
                        ArrayList<String> dataDetail = new ArrayList<String>();
                        dataDetail.add(attrValue);
                        dataDetail.add("STATE");
                        dataDetail.add("s"+(stateCodeCount++));
                        dataList.add(dataDetail);
                        
                        dataListStates.add(dataDetail);
                        
                        MainPage.totalVertices += 1;
                    }
                }
            }

            MainPage.totalEdges = 0;
            int transitionCodeCount = 1;
            for (int m = 0; m < num2; m++) {
                Element node2 = (Element) nList2.item(m);
                NamedNodeMap attributes = node2.getAttributes();
                int numAttrs2 = attributes.getLength();
                for (int n = 0; n < numAttrs2; n++) {
                    Attr attr = (Attr) attributes.item(n);
                    String attrName = attr.getNodeName();
                    String attrValue = attr.getNodeValue();
                    if (attrName.equals("name")) {
                        // attrValue - list of transitions
                        ArrayList<String> dataDetail = new ArrayList<String>();
                        dataDetail.add(attrValue);
                        dataDetail.add("TRANSITION");
                        dataDetail.add("t"+(transitionCodeCount++));
                        dataList.add(dataDetail);
                        
                        dataListTransitions.add(dataDetail);
                        
                        MainPage.totalEdges += 1;
                    }
                }
            }

            String data[][] = new String[dataList.size()][4];
            for (int index = 0; index < dataList.size(); index++) {
                data[index][0] = (index + 1) + ".";
                data[index][1] = dataList.get(index).get(2);
                data[index][2] = dataList.get(index).get(0);
                data[index][3] = dataList.get(index).get(1);
            }
            String columns[] = {"No.", "Code", "Name", "Type"};
            Func.setTable(columns, data);
            
            MainPage.lblTotalStates.setText("Total States / Vertices: " + MainPage.totalVertices);
            MainPage.lblTotalTransition.setText("Total Transitions / Edges: " + MainPage.totalEdges);

        } catch (Exception ex) {
            status = false;
            ex.printStackTrace();
        }
        return status;
    }
}
