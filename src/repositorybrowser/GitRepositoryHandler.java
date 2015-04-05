package repositorybrowser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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

import util.ArrayListQuickSort;

public class GitRepositoryHandler {
	private File repoPath;
	private Git gitObject;

	public GitRepositoryHandler() {
	}

	public GitRepositoryHandler(String repositoryUrl, ProgressMonitor monitor)
			throws InvalidRemoteException, TransportException, GitAPIException,
			IOException {
		String urlParts[] = repositoryUrl.split("/");

		this.repoPath = new File("./" + urlParts[urlParts.length - 1]);
		if (this.repoPath.exists()) {
			this.gitObject = Git.open(this.repoPath);
		} else {
			gitObject = Git.cloneRepository().setURI(repositoryUrl)
					.setDirectory(this.repoPath).setCloneAllBranches(true)
					.setProgressMonitor(monitor).call();
		}
	}

	public GitRepositoryHandler(String repositoryUrl)
			throws InvalidRemoteException, TransportException, GitAPIException,
			IOException {
		String urlParts[] = repositoryUrl.split("/");

		this.repoPath = new File("./" + urlParts[urlParts.length - 1]);
		if (this.repoPath.exists()) {
			this.gitObject = Git.open(this.repoPath);
		} else {
			gitObject = Git.cloneRepository().setURI(repositoryUrl)
					.setDirectory(this.repoPath).setCloneAllBranches(true)
					.call();
		}
	}

	public ArrayList<Commit> getAllRevisions(Boolean dateAscending)
			throws NoHeadException, GitAPIException {
		ArrayList<Commit> ret = new ArrayList<Commit>();
		LogCommand logcommand = this.gitObject.log();
		Iterator<RevCommit> ite = logcommand.call().iterator();
		while (ite.hasNext()) {
			ret.add(new Commit(ite.next()));
		}

		/*
		 * Whether or not the user wants the commits to be ascending in date
		 * we'll do
		 */
		if (dateAscending) {
			ArrayListQuickSort sorter = new ArrayListQuickSort();
			sorter.sortCommitsByDate(ret);
		}

		return ret;
	}

	public ArrayList<Commit> getRevisions(CommitFilter filter)
			throws NoHeadException, GitAPIException {
		RevCommitList<RevCommit> temp = filter.getFilteredCommits();
		ArrayList<Commit> ret = new ArrayList<Commit>();

		for (RevCommit commit : temp) {
			ret.add(new Commit(commit));
		}

		return ret;
	}

	public void openExistingRepository(String repositoryUrl) throws IOException {
		String urlParts[] = repositoryUrl.split("/");
		this.repoPath = new File("./" + urlParts[urlParts.length - 1] + "/.git");

		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		Repository repository = builder.setGitDir(repoPath).readEnvironment()
				.findGitDir().build();

		this.gitObject = Git.wrap(repository);
		this.gitObject = Git.open(repoPath);
	}

	public void update() throws WrongRepositoryStateException,
			InvalidConfigurationException, DetachedHeadException,
			InvalidRemoteException, CanceledException, RefNotFoundException,
			NoHeadException, TransportException, GitAPIException {
		this.gitObject.pull().call();

	}

}
