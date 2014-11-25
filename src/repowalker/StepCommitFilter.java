package repowalker;

import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevCommitList;

import Exceptions.BadFilterInitException;

public class StepCommitFilter extends CommitFilter {
	/*
	 * Example filter, takes 1 Integer argument that represents the step through
	 * which it skips commits when filtering
	 */

	public StepCommitFilter(Object[] args, RevCommitList<RevCommit> commits)
			throws BadFilterInitException {
		super(1, commits);
		this.setArgs(args);
	}

	@Override
	public void doFilterCommit() {
		RevCommitList<RevCommit> ret = new RevCommitList<RevCommit>();
		RevCommitList<RevCommit> commits = this.getCommits();
		// Sim, quem usa a classe tem que saber :(
		Integer step = (Integer) this.getArgs()[0];

		if (step > commits.size()) {
			ret.add(commits.get(0));
			ret.add(commits.get(commits.size() - 1));
		} else {
			for (int i = 0; i < commits.size(); i += step) {
				RevCommit revCommit = commits.get(i);
				ret.add(revCommit);
			}
			if (commits.size() % step != 0) {
				ret.add(commits.get(commits.size() - 1));
			}
		}

		this.setCommits(ret);
	}
}
