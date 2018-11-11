/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import helpers.Func;
import java.util.ArrayList;

/**
 *
 * @author umarmukhtar
 */
public class MatchController {
    
    public static ArrayList<ArrayList<String>> dataList = new ArrayList<ArrayList<String>>();
    
    public static ArrayList<String> getMatchList(int index, String element, String elementList, String type) {
        ArrayList<String> listDetail = new ArrayList<String>();
        if ((element.toLowerCase().contains(elementList.toLowerCase()))
                || (elementList.toLowerCase().contains(element.toLowerCase()))) {
//                featuresList.add(elementList + " - " + type);
            listDetail.add((index + 1) + ".");
            listDetail.add(element);
            listDetail.add(elementList);
            listDetail.add(type);
        }
        return listDetail;
    }
    
    public static boolean setMatch() {
        boolean status = true;
        try {
            
            int count = 0;
            for (int index = 0; index < FMController.dataList.size(); index++) {
                for (int jndex = 0; jndex < UMLController.dataList.size(); jndex++) {
                    ArrayList<String> dataDetail = getMatchList(count, FMController.dataList.get(index).get(0), UMLController.dataList.get(jndex).get(0), UMLController.dataList.get(jndex).get(1));
                    if (dataDetail.size() == 4) {
                        count++;
                        MatchController.dataList.add(dataDetail);
                    }
                }
            }
            System.out.println(MatchController.dataList);
            
            int numCol = 4;
            String data[][] = new String[MatchController.dataList.size()][numCol];
            if (MatchController.dataList.size() > 0) {
                for (int index = 0; index < MatchController.dataList.size(); index++) {
                    for (int jndex = 0; jndex < numCol; jndex++) {
                        data[index][jndex] = MatchController.dataList.get(index).get(jndex);
                    }
                }
            }
            String columns[] = {"No.", "Feature", "State/Transition", "Type"};
            Func.setTable(columns, data);
            
        } catch (Exception e) {
            status = false;
            e.printStackTrace();
        }
        return status;
    }
}
