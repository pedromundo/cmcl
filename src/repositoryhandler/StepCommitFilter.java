package repositoryhandler;

import java.util.ArrayList;

import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevCommitList;

import exceptions.BadFilterInitException;

public class StepCommitFilter extends ICommitFilter {
	/*
	 * Example filter, takes 1 Integer argument that represents the step through
	 * which it skips commits when filtering
	 */

	public StepCommitFilter(Object[] args, ArrayList<Commit> commits)
			throws BadFilterInitException {
		super(2, commits);
		this.setArgs(args);
	}

	StepCommitFilter(Object[] args, RevCommitList<RevCommit> commits)
			throws BadFilterInitException {
		super(1, commits);
		this.setArgs(args);
	}

	@Override
	public void doFilterCommit() {
		RevCommitList<RevCommit> filteredCommits = new RevCommitList<RevCommit>();
		RevCommitList<RevCommit> commits = this.getCommits();
		// Sim, quem usa a classe tem que saber :(
		Integer step = (Integer) this.getArgs()[0];

		if (step > commits.size()) {
			filteredCommits.add(commits.get(0));
			filteredCommits.add(commits.get(commits.size() - 1));
		} else {
			for (int i = 0; i < commits.size(); i += step) {
				RevCommit revCommit = commits.get(i);
				filteredCommits.add(revCommit);
			}
			if (commits.size() % step != 0) {
				filteredCommits.add(commits.get(commits.size() - 1));
			}
		}

		this.setCommits(filteredCommits);
	}
}
