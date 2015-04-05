package repositorybrowser;

import java.util.Date;

import org.eclipse.jgit.revwalk.RevCommit;

public class Commit {
	// This class just wraps around RevCommit so that the client doesn't
	// explicitly depends on that, also makes things slightly more palatable.
	private RevCommit commit;

	public Commit(RevCommit commit) {
		this.commit = commit;
	}

	RevCommit getBaseCommit() {
		return this.commit;
	}

	public String getCommiter() {
		return this.commit.getCommitterIdent().getName();
	}

	public long getDate() {
		return (long) this.commit.getCommitTime();
	}

	public Date getHumanDate() {
		return new Date(this.commit.getCommitTime() * (long) 1000);
	}

	public String getName() {
		return this.commit.getName();
	}

}
