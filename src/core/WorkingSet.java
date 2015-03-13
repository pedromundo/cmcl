package core;

import java.io.IOException;
import java.util.ArrayList;

import metricsextractor.IMetricsExtractor;

import org.eclipse.jgit.api.errors.GitAPIException;

import repositoryhandler.CommitFilter;
import repositoryhandler.GitRepositoryHandler;
import soundrenderer.SoundRenderer;

public class WorkingSet {
	
	private ArrayList<IMetricsExtractor> extractors;
	private ArrayList<CommitFilter> filters;	
	private GitRepositoryHandler repoHandler;
	private SoundRenderer soundrenderer;
	
	public SoundRenderer getSoundrenderer() {
		return soundrenderer;
	}
	
	public GitRepositoryHandler getRepoHandler() {
		return repoHandler;
	}

	public void initRepoHandler(String gitUrl) throws Exception {
		try {
			this.repoHandler = new GitRepositoryHandler(gitUrl, null);
		} catch (GitAPIException | IOException e) {
			throw e;
		}
	}

	public ArrayList<IMetricsExtractor> getExtractors() {
		return extractors;
	}

	public void putExtractor(IMetricsExtractor extractor) {
		this.extractors.add(extractor);
	}
	
	public void clearExtractors(){
		this.extractors = new ArrayList<IMetricsExtractor>();
	}

	public ArrayList<CommitFilter> getFilters() {
		return filters;
	}

	public void putFilter(CommitFilter filter) {
		this.filters.add(filter);
	}
	
	public void clearFilters(){
		this.filters = new ArrayList<CommitFilter>();		
	}

	public WorkingSet() {
		this.extractors = new ArrayList<IMetricsExtractor>();
		this.filters = new ArrayList<CommitFilter>();
		this.repoHandler = new GitRepositoryHandler();
		this.soundrenderer = new SoundRenderer();
	}

}
