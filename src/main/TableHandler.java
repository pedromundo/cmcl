package main;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevCommitList;

public class TableHandler {

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

	public DefaultTableModel getModelFromCommitList(
			RevCommitList<RevCommit> commits, String columnName) {
		String col[] = { columnName };

		DefaultTableModel model = new DefaultTableModel(col, 0);
		for (RevCommit revision : commits) {
			String row[] = { revision.getName() };
			model.addRow(row);
		}

		return model;
	}

}
