package repositoryhandler;

import java.util.ArrayList;

import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevCommitList;

import exceptions.BadFilterInitException;

//Base class for commitfilters - strategy pattern, eventually.
public abstract class ICommitFilter {
	private final int argc;
	private Object[] args;
	private RevCommitList<RevCommit> commits;

	// This only depends on our classes and ArrayList, this is the one your
	// runner classes
	// should be using
	public ICommitFilter(Integer argc, ArrayList<Commit> commits)
			throws BadFilterInitException {
		if (argc == null || argc < 0) {
			throw new BadFilterInitException();
		} else {
			RevCommitList<RevCommit> temp = new RevCommitList<RevCommit>();
			for (Commit commit : commits) {
				temp.add(commit.getBaseCommit());
			}
			this.argc = argc;
			this.commits = temp;
			this.setArgs(new Object[argc]);
		}

	}

	// This first constructor depends on jGit, please only use it in your
	// backend packages
	ICommitFilter(Integer argc, RevCommitList<RevCommit> commits)
			throws BadFilterInitException {
		if (argc == null || argc < 0) {
			throw new BadFilterInitException();
		} else {
			this.argc = argc;
			this.commits = commits;
			this.setArgs(new Object[argc]);
		}

	}

	/*
	 * You SHOULD call super passing an integer (number of parameters used) as a
	 * parameter when inheriting, make sure to tell the world what the
	 * parameters mean for your filter You can also be super smart and check for
	 * correct argument types in your filter's constructor, but you may also
	 * not, no pressure.
	 */

	/*
	 * Do your commit filtering logic by overriding this, remember to use argc
	 * and argv; if needed, of course.
	 */
	public abstract void doFilterCommit();

	public Object[] getArgs() {
		return args;
	}

	int getArgumentCount() {
		return argc;
	}

	RevCommitList<RevCommit> getCommits() {
		return commits;
	}

	public final RevCommitList<RevCommit> getFilteredCommits() {
		this.doFilterCommit();
		return this.commits;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}

	/*
	 * Use this after filtering so that clients can call your
	 * getFilteredCommits() and get the proper filtered results
	 */
	void setCommits(RevCommitList<RevCommit> commits) {
		this.commits = commits;
	}
}