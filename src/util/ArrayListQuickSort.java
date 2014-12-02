package util;

import java.util.ArrayList;

import repositoryhandler.Commit;

public class ArrayListQuickSort {
	private ArrayList<Commit> commits;
	private int count;

	private void exchange(int i, int j) {
		Commit temp = commits.get(i);
		commits.set(i, commits.get(j));
		commits.set(j, temp);
	}

	private void quicksort(int low, int high) {
		int i = low, j = high;
		// Get the pivot element from the middle of the list
		Commit pivot = commits.get(low + (high - low) / 2);

		// Divide into two lists
		while (i <= j) {
			// If the current value from the left list is smaller then the pivot
			// element then get the next element from the left list
			while (commits.get(i).getDate() < pivot.getDate()) {
				i++;
			}
			// If the current value from the right list is larger then the pivot
			// element then get the next element from the right list
			while (commits.get(j).getDate() > pivot.getDate()) {
				j--;
			}

			// If we have found a values in the left list which is larger then
			// the pivot element and if we have found a value in the right list
			// which is smaller then the pivot element then we exchange the
			// values.
			// As we are done we can increase i and j
			if (i <= j) {
				exchange(i, j);
				i++;
				j--;
			}
		}
		// Recursion
		if (low < j)
			quicksort(low, j);
		if (i < high)
			quicksort(i, high);
	}

	public void sortCommitsByDate(ArrayList<Commit> ret) {
		// check for empty or null array
		if (ret == null || ret.size() == 0) {
			return;
		}
		this.commits = ret;
		count = ret.size();
		quicksort(0, count - 1);
	}
}
