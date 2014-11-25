package repowalker;

import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevCommitList;

import Exceptions.BadFilterInitException;

//Base class for commitfilters - strategy pattern, eventually.
public abstract class CommitFilter {
	private final int argc;
	private Object[] args;
	private RevCommitList<RevCommit> commits;

	public final RevCommitList<RevCommit> getFilteredCommits() {
		this.doFilterCommit();
		return this.commits;
	}

	// Do your commit filtering logic by overriding this, remember to use argc
	// and argv;
	// if needed, of course.
	public abstract void doFilterCommit();

	// You SHOULD call super passing an integer as a parameter when inheriting,
	// this helps a lot of the magic go smoothly, also make sure to tell the
	// world
	// what the arguments mean for your filter
	public CommitFilter(Integer argc, RevCommitList<RevCommit> commits)
			throws BadFilterInitException {
		if (argc == null || argc < 0) {
			throw new BadFilterInitException();
		} else {
			this.argc = argc;
			this.commits = commits;
			this.setArgs(new Object[argc]);
		}

	}

	int getArgumentCount() {
		return argc;
	}

	RevCommitList<RevCommit> getCommits() {
		return commits;
	}

	void setCommits(RevCommitList<RevCommit> commits) {
		this.commits = commits;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}
}