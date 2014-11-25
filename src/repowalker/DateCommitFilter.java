package repowalker;

import java.util.Date;

import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevCommitList;

import util.QuickSort;
import Exceptions.BadFilterInitException;

public class DateCommitFilter extends CommitFilter {
	/*
	 * Example filter, takes 2 Date arguments, meaning beginning and final date
	 * for commits that should be shown, in this order.
	 */
	public DateCommitFilter(Object[] args, RevCommitList<RevCommit> commits)
			throws BadFilterInitException {
		super(2, commits);
		this.setArgs(args);
	}

	@Override
	public void doFilterCommit() {
		RevCommitList<RevCommit> ret = new RevCommitList<RevCommit>();
		RevCommitList<RevCommit> commits = this.getCommits();
		
		QuickSort sorter = new QuickSort();
		sorter.sortByTime(commits);

		final Date initialDate = (Date) this.getArgs()[0];
		final Date finalDate = (Date) this.getArgs()[1];
		
		for (RevCommit revCommit : commits) {
			if(revCommit.getCommitTime() >= initialDate.getTime()/1000 && revCommit.getCommitTime() <= finalDate.getTime()/1000){
				ret.add(revCommit);
			}			
		}
		
		this.setCommits(ret);

	}

}
