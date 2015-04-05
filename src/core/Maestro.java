package core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import metricsextractor.IMetricsExtractor;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;

import repositorybrowser.Commit;
import repositorybrowser.GitRepositoryHandler;
import util.NoteMap;
import freemarker.template.TemplateException;

public class Maestro {

	private WorkingSet ws;

	public Maestro(WorkingSet ws) {
		this.ws = ws;
	}

	public void makeMusic() throws NoHeadException, GitAPIException,
			IOException, TemplateException {
		GitRepositoryHandler handler = this.ws.getRepoHandler();

		ArrayList<Commit> commits = new ArrayList<Commit>();
		commits = handler.getRevisions(this.ws.getFilters());

		Map<String, Map> data = new TreeMap<String, Map>();

		for (IMetricsExtractor extractor : this.ws.getExtractors()) {
			data.put(extractor.getOutputName(), extractor.getMetrics(commits));
		}

		// Lets always pass this map, because there must always be a way to map
		// numbers to notes.
		data.put("noteMap", NoteMap.getInstance());

		if (this.ws.getFileName() != null) {
			this.ws.getSoundrenderer().Render(this.ws.getTemplateName(), data,
					this.ws.getFileName());
		} else {
			this.ws.getSoundrenderer().Render(this.ws.getTemplateName(), data);
		}

	}
}
