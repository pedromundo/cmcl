package metricsextractor;

import java.util.ArrayList;
import java.util.Map;

import repositorybrowser.Commit;

public interface IMetricsExtractor {
	Map<String, Integer> getMetrics(ArrayList<Commit> commits);

	String getOutputName();
}
