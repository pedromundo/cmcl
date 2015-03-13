package core;

import java.util.ArrayList;

import repositoryhandler.ICommitFilter;
import metricsextractor.IMetricsExtractor;

public class WorkingSet {
	
	ArrayList<IMetricsExtractor> extractors;
	ArrayList<ICommitFilter> filters;
	
	public ArrayList<IMetricsExtractor> getExtractors() {
		return extractors;
	}

	public void putExtractor(IMetricsExtractor extractor) {
		this.extractors.add(extractor);
	}
	
	public void clearExtractors(){
		this.extractors = new ArrayList<IMetricsExtractor>();
	}

	public ArrayList<ICommitFilter> getFilters() {
		return filters;
	}

	public void putFilter(ICommitFilter filter) {
		this.filters.add(filter);
	}
	
	public void clearFilters(){
		this.filters = new ArrayList<ICommitFilter>();		
	}

	public WorkingSet() {
		this.extractors = new ArrayList<IMetricsExtractor>();
		this.filters = new ArrayList<ICommitFilter>();
	}

}
