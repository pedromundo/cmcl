package repowalker;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.api.errors.CanceledException;
import org.eclipse.jgit.api.errors.DetachedHeadException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidConfigurationException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.jgit.lib.ProgressMonitor;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevCommitList;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.util.FileUtils;

public class RepositoryHandler {
	private File repoPath;
	private Git gitObject;

	public RepositoryHandler() {
	}

	public RepositoryHandler(String repositoryUrl, ProgressMonitor monitor)
			throws InvalidRemoteException, TransportException, GitAPIException,
			IOException {
		String urlParts[] = repositoryUrl.split("/");

		this.repoPath = new File("./" + urlParts[urlParts.length - 1]);
		if (this.repoPath.exists()) {
			FileUtils.delete(this.repoPath, FileUtils.RECURSIVE);
		}

		gitObject = Git.cloneRepository().setURI(repositoryUrl)
				.setDirectory(this.repoPath).setCloneAllBranches(true)
				.setProgressMonitor(monitor).call();
	}

	public void openExistingRepository(String repositoryUrl) throws IOException {
		String urlParts[] = repositoryUrl.split("/");
		this.repoPath = new File("./" + urlParts[urlParts.length - 1] + "/.git");

		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		Repository repository = builder.setGitDir(repoPath).readEnvironment() // scan
																				// environment
																				// GIT_*
																				// variables
				.findGitDir() // scan up the file system tree
				.build();

		this.gitObject = Git.wrap(repository);
		this.gitObject = Git.open(repoPath);
	}

	public void update() throws WrongRepositoryStateException,
			InvalidConfigurationException, DetachedHeadException,
			InvalidRemoteException, CanceledException, RefNotFoundException,
			NoHeadException, TransportException, GitAPIException {
		this.gitObject.pull().call();

	}

	public RevCommitList<RevCommit> getAllRevisions() throws NoHeadException,
			GitAPIException {
		RevCommitList<RevCommit> ret = new RevCommitList<RevCommit>();
		LogCommand logcommand = this.gitObject.log();
		Iterator<RevCommit> ite = logcommand.call().iterator();
		while (ite.hasNext()) {
			ret.add(ite.next());
		}
		return ret;
	}

	public RevCommitList<RevCommit> getRevisions(CommitFilter filter)
			throws NoHeadException, GitAPIException {
		RevCommitList<RevCommit> ret = filter.getFilteredCommits();
		return ret;
	}

}
