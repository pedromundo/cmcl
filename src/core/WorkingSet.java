package core;

import java.io.IOException;
import java.util.ArrayList;

import metricsextractor.IMetricsExtractor;

import org.eclipse.jgit.api.errors.GitAPIException;

import repositorybrowser.CommitFilter;
import repositorybrowser.GitRepositoryHandler;
import soundrenderer.SoundRenderer;

public class WorkingSet {

	private ArrayList<IMetricsExtractor> extractors;
	private CommitFilter filter;
	private GitRepositoryHandler repoHandler;
	private SoundRenderer soundRenderer;
	private String fileName;
	private String templateName;

	public WorkingSet(String templateName) {
		this.extractors = new ArrayList<IMetricsExtractor>();
		this.repoHandler = new GitRepositoryHandler();
		this.soundRenderer = new SoundRenderer();
		this.templateName = templateName;
	}

	public WorkingSet(String fileName, String templateName) {
		this.extractors = new ArrayList<IMetricsExtractor>();
		this.repoHandler = new GitRepositoryHandler();
		this.soundRenderer = new SoundRenderer();
		this.fileName = fileName;
		this.templateName = templateName;
	}

	public void clearExtractors() {
		this.extractors = new ArrayList<IMetricsExtractor>();
	}

	public ArrayList<IMetricsExtractor> getExtractors() {
		return extractors;
	}

	public String getFileName() {
		return fileName;
	}

	public CommitFilter getFilters() {
		return filter;
	}

	public GitRepositoryHandler getRepoHandler() {
		return repoHandler;
	}

	public SoundRenderer getSoundrenderer() {
		return soundRenderer;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void initRepoHandler(String gitUrl) throws Exception {
		try {
			this.repoHandler = new GitRepositoryHandler(gitUrl);
		} catch (GitAPIException | IOException e) {
			throw e;
		}
	}

	public void putExtractor(IMetricsExtractor extractor) {
		this.extractors.add(extractor);
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setFilter(CommitFilter filter) {
		this.filter = filter;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

}
