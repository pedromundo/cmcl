package repositoryhandler;

import java.util.ArrayList;

import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevCommitList;

import util.RevCommitListQuickSort;
import exceptions.BadFilterInitException;

public class RangeCommitFilter extends CommitFilter {
	/*
	 * Example filter, takes 2 String arguments that represent the starting and
	 * ending commits of the range to be filtered, respectively.
	 */

	public RangeCommitFilter(Object[] args, ArrayList<Commit> commits)
			throws BadFilterInitException {
		super(2, commits);
		this.setArgs(args);
	}

	RangeCommitFilter(Object[] args, RevCommitList<RevCommit> commits)
			throws BadFilterInitException {
		super(2, commits);
		this.setArgs(args);
	}

	@Override
	public void doFilterCommit() {
		RevCommitList<RevCommit> filteredCommits = new RevCommitList<RevCommit>();
		RevCommitList<RevCommit> commits = this.getCommits();

		RevCommitListQuickSort sorter = new RevCommitListQuickSort();
		sorter.sortCommitsByDate(commits);

		String startingCommit = (String) this.getArgs()[0];
		String endingCommit = (String) this.getArgs()[1];

		Boolean isGetting = false;

		for (RevCommit revCommit : commits) {
			if (revCommit.getName().equalsIgnoreCase(startingCommit)) {
				isGetting = true;
			} else if (revCommit.getName().equalsIgnoreCase(endingCommit)) {
				filteredCommits.add(revCommit);
				this.setCommits(filteredCommits);
				return;
			}
			if (isGetting) {
				filteredCommits.add(revCommit);
			}
		}
		this.setCommits(filteredCommits);
	}

}
