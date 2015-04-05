package metricsextractor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import repositoryhandler.Commit;
import util.Interpolator;

public class CommitsPerMonthExtractor implements IMetricsExtractor {

	public CommitsPerMonthExtractor() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Map<String, Integer> getMetrics(ArrayList<Commit> commits) {
		// From here its all metric extraction logic, which shouldn't be here AT
		// ALL

		// Metric #1: Commits / Month
		TreeMap<String, Integer> commitsMonth = new TreeMap<>();

		for (Commit commit : commits) {
			@SuppressWarnings("deprecation")
			String key = ""
					+ (commit.getHumanDate().getYear() + 1900)
					+ "/"
					+ Integer
							.toHexString((commit.getHumanDate().getMonth() + 1));
			if (!commitsMonth.containsKey(key)) {
				commitsMonth.put(key, 1);
			} else {
				commitsMonth.put(key, commitsMonth.get(key) + 1);
			}
		}

		// Min and maximum commits per month, for use in interpolation

		Iterator<Entry<String, Integer>> ite = commitsMonth.entrySet()
				.iterator();

		// Cpm = Commits per month :P
		int minCpm = Integer.MAX_VALUE;
		int maxCpm = Integer.MIN_VALUE;

		while (ite.hasNext()) {
			Entry<String, Integer> atual = ite.next();
			int atualInt = atual.getValue();
			if (atualInt < minCpm) {
				minCpm = atual.getValue();
			}
			if (atualInt > maxCpm) {
				maxCpm = atual.getValue();
			}
		}

		// After counting the commits, let's interpolate the values

		Interpolator interpolator = new Interpolator(10, 52, minCpm, maxCpm);

		// And set them

		ite = commitsMonth.entrySet().iterator();
		while (ite.hasNext()) {
			Entry<String, Integer> atual = ite.next();
			atual.setValue(interpolator.interpolate(atual.getValue()));

		}

		// All done :)
		return commitsMonth;
	}

	@Override
	public String getOutputName() {
		return "commitsMonth";
	}

}
