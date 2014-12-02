package main;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import repositoryhandler.Commit;

public class TableHandler {

	public DefaultTableModel getModelFromCommitList(ArrayList<Commit> commits,
			String columnName) {
		String col[] = { columnName };

		DefaultTableModel model = new DefaultTableModel(col, 0);
		for (Commit revision : commits) {
			String row[] = { revision.getName() + "   "
					+ revision.getHumanDate().toString() };
			model.addRow(row);
		}

		return model;
	}

	public DefaultTableModel getModelFromStrings(ArrayList<String> strings,
			String columnName) {
		String col[] = { columnName };

		DefaultTableModel model = new DefaultTableModel(col, 0);
		for (String revision : strings) {
			String row[] = { revision };
			model.addRow(row);
		}

		return model;
	}

}
