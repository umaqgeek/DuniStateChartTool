/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import helpers.Func;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

/**
 *
 * @author umarmukhtar
 */
public class DictionaryController {
    
    public static ArrayList<ArrayList<String>> dataList = new ArrayList<ArrayList<String>>();
    
    public static boolean clearData() {
        boolean status = true;
        try {
            
            dataList.removeAll(dataList);
            
            String data[][] = new String[dataList.size()][2];
            for (int index = 0; index < dataList.size(); index++) {
                data[index][0] = (index + 1) + ".";
                data[index][1] = "";
                for (int jndex = 0; jndex < dataList.get(index).size(); jndex++) {
                    data[index][1] += dataList.get(index).get(jndex);
                    if (jndex != dataList.get(index).size() - 1) {
                        data[index][1] += ", ";
                    }
                }
            }
            String columns[] = {"No.", "Data Dictionary"};
            Func.setTable(columns, data);
            
        } catch (Exception e) {
        }
        return status;
    }
    
    public static boolean setData(String file) {
        boolean status = true;
        try {
            
            File f = new File(file);
            BufferedReader b = new BufferedReader(new FileReader(f));

            String readLine = "";
            while ((readLine = b.readLine()) != null) {
                System.out.println(readLine);
                if (readLine.charAt(0) != '#') {
                    ArrayList<String> subData = new ArrayList<String>();
                    String dataRow[] = readLine.split(",");
                    for (String d : dataRow) {
                        subData.add(d);
                    }
                    dataList.add(subData);
                }
            }
            
            String data[][] = new String[dataList.size()][2];
            for (int index = 0; index < dataList.size(); index++) {
                data[index][0] = (index + 1) + ".";
                data[index][1] = "";
                for (int jndex = 0; jndex < dataList.get(index).size(); jndex++) {
                    data[index][1] += dataList.get(index).get(jndex);
                    if (jndex != dataList.get(index).size() - 1) {
                        data[index][1] += ", ";
                    }
                }
            }
            String columns[] = {"No.", "Data Dictionary"};
            Func.setTable(columns, data);
            
        } catch (Exception e) {
            status = false;
            e.printStackTrace();
        }
        return status;
    }
}
