package metricsextractor;

import java.util.ArrayList;
import java.util.Map;

import repositoryhandler.Commit;

public interface IMetricsExtractor {	
	Map<String, Integer> getMetrics(ArrayList<Commit> commits);
	String getOutputName();
}
