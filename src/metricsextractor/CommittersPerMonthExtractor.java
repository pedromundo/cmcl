package metricsextractor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import repositorybrowser.Commit;
import util.Interpolator;

public class CommittersPerMonthExtractor implements IMetricsExtractor {

	public CommittersPerMonthExtractor() {
	}

	@Override
	public Map<String, Integer> getMetrics(ArrayList<Commit> commits) {
		TreeMap<String, Integer> committersMonth = new TreeMap<>();
		TreeMap<String, LinkedHashSet<String>> committersMonthTemp = new TreeMap<>();

		for (Commit commit : commits) {
			// Extra work to find out the commiters of each month for use in the
			// con-
			// solidated metric, life's a b*tch
			@SuppressWarnings("deprecation")
			String key = ""
					+ (commit.getHumanDate().getYear() + 1900)
					+ "/"
					+ Integer
							.toHexString((commit.getHumanDate().getMonth() + 1));
			if (committersMonthTemp.containsKey(key)) {
				committersMonthTemp.get(key).add(commit.getCommiter());
			} else {
				LinkedHashSet<String> temp = new LinkedHashSet<String>();
				temp.add(commit.getCommiter());
				committersMonthTemp.put(key, temp);
			}
		}

		Iterator<Entry<String, LinkedHashSet<String>>> iteTemp = committersMonthTemp
				.entrySet().iterator();

		// Setting the actual committers/month value
		while (iteTemp.hasNext()) {
			Entry<String, LinkedHashSet<String>> atual = iteTemp.next();
			committersMonth.put(atual.getKey(), atual.getValue().size());

		}

		Iterator<Entry<String, Integer>> ite = committersMonth.entrySet()
				.iterator();

		// Ppm = People per month
		int minPpm = Integer.MAX_VALUE;
		int maxPpm = Integer.MIN_VALUE;

		while (ite.hasNext()) {
			Entry<String, Integer> atual = ite.next();
			int atualInt = atual.getValue();
			if (atualInt < minPpm) {
				minPpm = atual.getValue();
			}
			if (atualInt > maxPpm) {
				maxPpm = atual.getValue();
			}
		}

		Interpolator interpolator = new Interpolator(2, 8, minPpm, maxPpm);

		ite = committersMonth.entrySet().iterator();
		while (ite.hasNext()) {
			Entry<String, Integer> atual = ite.next();
			atual.setValue(interpolator.interpolate(atual.getValue()));

		}
		// all done :)

		return committersMonth;
	}

	@Override
	public String getOutputName() {
		return "committersMonth";
	}

}
