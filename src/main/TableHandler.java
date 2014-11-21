package main;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

public class TableHandler {	
	
	public DefaultTableModel getModelFromStrings(ArrayList<String> strings, String columnName){		
		String col[] = {columnName};
		
		DefaultTableModel model = new DefaultTableModel(col, 0);
		for(String revision : strings){
			String row[] = {revision};
			model.addRow(row);					
		}
		
		return model;		
	}

}
