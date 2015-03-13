package repositoryhandler;

import java.util.ArrayList;
import java.util.Date;

import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevCommitList;

import util.RevCommitListQuickSort;
import exceptions.BadFilterInitException;

public class DateCommitFilter extends CommitFilter {
	public DateCommitFilter(Object[] args, ArrayList<Commit> commits)
			throws BadFilterInitException {
		super(2, commits);
		this.setArgs(args);
	}

	/*
	 * Example filter, takes 2 Date arguments, meaning beginning and final date
	 * for commits that should be shown, in this order.
	 */
	DateCommitFilter(Object[] args, RevCommitList<RevCommit> commits)
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

		final Date initialDate = (Date) this.getArgs()[0];
		final Date finalDate = (Date) this.getArgs()[1];

		for (RevCommit revCommit : commits) {
			if (revCommit.getCommitTime() >= initialDate.getTime() / 1000
					&& revCommit.getCommitTime() <= finalDate.getTime() / 1000) {
				filteredCommits.add(revCommit);
			}
		}

		this.setCommits(filteredCommits);

	}

}
