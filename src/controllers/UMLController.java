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
import org.w3c.dom.Node;
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
    
    public static ArrayList<ArrayList<String>> transitions = new ArrayList<ArrayList<String>>();
    public static ArrayList<ArrayList<String>> preMatrix = new ArrayList<ArrayList<String>>();

    public static boolean setData(String file) {
        boolean status = true;
        try {
            
            dataListStates.removeAll(dataListStates);
            dataListTransitions.removeAll(dataListTransitions);

            BufferedReader in = new BufferedReader(new FileReader(file));

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            NodeList nList0 = doc.getElementsByTagName("UML:PseudoState");
            NodeList nList1 = doc.getElementsByTagName("UML:SimpleState");
            NodeList nList2 = doc.getElementsByTagName("UML:Transition");
            int num0 = nList0.getLength();
            int num1 = nList1.getLength();
            int num2 = nList2.getLength();
            
            MainPage.totalVertices = 0;
            int stateCodeCount = 1;
            for (int l = 0; l < num1; l++) {
                Element node1 = (Element) nList1.item(l);
                NamedNodeMap attributes = node1.getAttributes();
                int numAttrs = attributes.getLength();
                
                String name = "-";
                String code = "-";
                String id = "-";
                boolean foundName = false;
                boolean foundId = false;
                
                for (int i = 0; i < numAttrs; i++) {
                    Attr attr = (Attr) attributes.item(i);
                    String attrName = attr.getNodeName();
                    String attrValue = attr.getNodeValue();
                    if (attrName.equals("name")) {
                        foundName = true;
                        name = attrValue;
                        code = "s" + (stateCodeCount + 1);
                    }
                    if (attrName.equals("xmi.id")) {
                        foundId = true;
                        id = attrValue;
                    }
                }
                
                if (foundName && foundId) {
                    ArrayList<String> dataDetail = new ArrayList<String>();
                    dataDetail.add(name);
                    dataDetail.add("STATE");
                    dataDetail.add(code);
                    dataDetail.add(id);

                    dataList.add(dataDetail);
                    dataListStates.add(dataDetail);
                    
                    stateCodeCount += 1;
                    MainPage.totalVertices += 1;
                }
            }
            
            int pseudostateCodeCount = stateCodeCount + 1;
            for (int l = 0; l < num0; l++) {
                Element node0 = (Element) nList0.item(l);
                NamedNodeMap attributes = node0.getAttributes();
                int numAttrs = attributes.getLength();
                
                String name = "-";
                String code = "-";
                String id = "-";
                boolean foundName = false;
                boolean foundId = false;
                
                for (int i = 0; i < numAttrs; i++) {
                    Attr attr = (Attr) attributes.item(i);
                    String attrName = attr.getNodeName();
                    String attrValue = attr.getNodeValue();
                    if (attrName.equals("name")) {
                        foundName = true;
                        name = attrValue;
                        if (name.toLowerCase().equals("start")) {
                            code = "s1";
                        } else if (name.toLowerCase().equals("final")) {
                            code = "s" + pseudostateCodeCount;
                        }
                    }
                    if (attrName.equals("xmi.id")) {
                        foundId = true;
                        id = attrValue;
                    }
                }
                
                if (foundName && foundId) {
                    ArrayList<String> dataDetail = new ArrayList<String>();
                    dataDetail.add(name);
                    dataDetail.add("STATE");
                    dataDetail.add(code);
                    dataDetail.add(id);

                    dataList.add(dataDetail);
                    dataListStates.add(dataDetail);
                }
            }
            
            // re-arrange Start to be the first states in the states list.
            int startIndex = 0;
            for (int i = 0; i < dataListStates.size(); i++) {
                if (dataListStates.get(i).get(2).toLowerCase().equals("s1")) {
                    startIndex = i;
                }
            }
            for (int i = startIndex; i >= 1; i--) {
                ArrayList<String> temp = dataListStates.get(i);
                dataListStates.set(i, dataListStates.get(i-1));
                dataListStates.set(i-1, temp);
            }

            MainPage.totalEdges = 0;
            int transitionCodeCount = 1;
            for (int m = 0; m < num2; m++) {
                
                String transName = "-";
                String transCode = "-";
                String sourceId = "-";
                String destinationId = "-";
                String weight = "0";
                boolean foundName = false;
                boolean foundSource = false;
                boolean foundDestination = false;
                
                Element node2 = (Element) nList2.item(m);
                NamedNodeMap attributes = node2.getAttributes();
                
                try {
                    NodeList node2Child = node2.getElementsByTagName("UML:BooleanExpression");
                    for (int i = 0; i < node2Child.getLength(); i++) {
                        Node node = node2Child.item(i);
                        for (int j = 0; j < node.getAttributes().getLength(); j++) {
                            if (node.getAttributes().item(j).getNodeName().equals("body")) {
                                weight = node.getAttributes().item(j).getNodeValue();
                            }
                        }
                    }
                } catch (Exception e) {
                }
                
                int numAttrs2 = attributes.getLength();
                for (int n = 0; n < numAttrs2; n++) {
                    Attr attr = (Attr) attributes.item(n);
                    String attrName = attr.getNodeName();
                    String attrValue = attr.getNodeValue();
                    if (attrName.equals("name")) {
                        foundName = true;
                        transName = attrValue;
                        transCode = "t"+ (transitionCodeCount);
                    }
                    if (attrName.equals("source")) {
                        foundSource = true;
                        sourceId = attrValue;
                    }
                    if (attrName.equals("target")) {
                        foundDestination = true;
                        destinationId = attrValue;
                    }
                }
                
                if (foundName && foundSource && foundDestination) {
                    ArrayList<String> dataDetail = new ArrayList<String>();
                    dataDetail.add(transName);
                    dataDetail.add("TRANSITION");
                    dataDetail.add(transCode);
                    dataDetail.add(sourceId);
                    dataDetail.add(destinationId);
                    dataDetail.add(weight);
                    
                    dataList.add(dataDetail);
                    dataListTransitions.add(dataDetail);

                    transitionCodeCount += 1;
                    MainPage.totalEdges += 1;
                }
            }
            
            // reset and clear the pre-matrix
            preMatrix.removeAll(preMatrix);
            
            // match all vertices with their edges and put into a matrix form.
            int numVertices = dataListStates.size();
            for (int i = 0; i < numVertices; i++) {
                ArrayList<String> preRow = new ArrayList<String>();
                for (int j = 0; j < numVertices; j++) {
                    if (i == j) {
                        preRow.add("0");
                    } else {
                        String sourceCode = "s" + (i + 1);
                        String destinationCode = "s" + (j + 1);
                        String sourceId = getStateId(sourceCode);
                        String destinationId = getStateId(destinationCode);
                        String weight = getWeightTransition(sourceId, destinationId);
                        preRow.add(weight);
                    }
                }
                preMatrix.add(preRow);
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
    
    public static String getWeightTransition(String sourceId, String destinationId) {
        String str = "INF";
        try {
            for (int i = 0; i < dataListTransitions.size(); i++) {
                if (dataListTransitions.get(i).get(3).toLowerCase().equals(sourceId.toLowerCase()) 
                        && dataListTransitions.get(i).get(4).toLowerCase().equals(destinationId.toLowerCase())) {
                    str = dataListTransitions.get(i).get(5);
                    break;
                }
            }
        } catch (Exception e) {
        }
        return str;
    }
    
    public static String getStateId(String code) {
        String str = "-";
        try {
            for (int i = 0; i < dataListStates.size(); i++) {
                if (dataListStates.get(i).get(2).toLowerCase().equals(code.toLowerCase())) {
                    str = dataListStates.get(i).get(3);
                    break;
                }
            }
        } catch (Exception e) {
        }
        return str;
    }
    
    public static String getStateName(String code) {
        String str = "-";
        try {
            for (int i = 0; i < dataListStates.size(); i++) {
                if (dataListStates.get(i).get(2).toLowerCase().equals(code.toLowerCase())) {
                    str = dataListStates.get(i).get(0);
                    break;
                }
            }
        } catch (Exception e) {
        }
        return str;
    }
    
    public static String getStateCode(String id) {
        String str = "-";
        try {
            for (int i = 0; i < dataListStates.size(); i++) {
                if (dataListStates.get(i).get(3).toLowerCase().equals(id.toLowerCase())) {
                    str = dataListStates.get(i).get(2);
                    break;
                }
            }
        } catch (Exception e) {
        }
        return str;
    }
}
