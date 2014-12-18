package main;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;

import org.eclipse.jgit.lib.TextProgressMonitor;

import repositoryhandler.Commit;
import repositoryhandler.DateCommitFilter;
import repositoryhandler.GitRepositoryHandler;
import util.Interpolator;
import util.NoteMap;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

public class GitBrowser {

	private class RunClone implements Runnable {
		@Override
		public void run() {
			try {
				new GitRepositoryHandler(txtRepoURL.getText(), monitorClone);
			} catch (Exception e) {
				JOptionPane
						.showMessageDialog(frmGitrepobrowser,
								"Erro ao clonar o repositório, verifique sua conexão e tente novamente :(");
			}
		}
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GitBrowser window = new GitBrowser();
					window.frmGitrepobrowser.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private JFrame frmGitrepobrowser;
	private JLabel lblUrlDoRepositrio;
	private JTextField txtRepoURL;
	private JButton btnCloneRepo;
	private JLabel lblStatusDoRepositrio;
	private JLabel lblNa;
	private GitRepositoryHandler repoHandler;
	private TextProgressMonitor monitorClone;
	private JLabel lblPorcento;
	private JTable table;

	private JButton btnCarregarCommits;

	private JLabel lblCommitsCarregados;

	/**
	 * Create the application.
	 */
	public GitBrowser() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		// My stuff
		this.monitorClone = new TextProgressMonitor() {
			@Override
			protected void onEndTask(String taskName, int cmp, int totalWork,
					int pcnt) {
				if (taskName.contains("Updating references")) {
					super.onEndTask(taskName, cmp, totalWork, pcnt);
					lblNa.setText("OK!");
					lblNa.setForeground(Color.DARK_GRAY);
				}
			}

			@Override
			protected void onUpdate(String taskName, int cmp, int totalWork,
					int pcnt) {
				super.onUpdate(taskName, cmp, totalWork, pcnt);
				lblPorcento.setText(pcnt + "%");
			}
		};
		// End my stuff

		frmGitrepobrowser = new JFrame();
		frmGitrepobrowser.setTitle("Git Repository Browser");
		frmGitrepobrowser.setBounds(100, 100, 640, 480);
		frmGitrepobrowser.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmGitrepobrowser.getContentPane().setLayout(
				new MigLayout("", "[][grow][][][][grow]",
						"[][][][][][][][grow][][][][][][][]"));

		lblUrlDoRepositrio = new JLabel("URL Do Repositório:");
		frmGitrepobrowser.getContentPane().add(lblUrlDoRepositrio,
				"cell 1 1,aligny center");

		txtRepoURL = new JTextField();
		frmGitrepobrowser.getContentPane().add(txtRepoURL,
				"cell 2 1 2 1,alignx center,aligny center");
		txtRepoURL.setColumns(30);

		btnCloneRepo = new JButton("Clonar Repositório");
		btnCloneRepo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Thread t = new Thread(new RunClone());
				t.start();
				JOptionPane.showMessageDialog(frmGitrepobrowser, "Clonando!");
			}
		});

		frmGitrepobrowser.getContentPane().add(btnCloneRepo,
				"cell 5 1,aligny center");

		lblPorcento = new JLabel("0%");
		frmGitrepobrowser.getContentPane().add(lblPorcento,
				"cell 5 2,alignx center");

		lblStatusDoRepositrio = new JLabel("Status do Repositório:");
		frmGitrepobrowser.getContentPane().add(lblStatusDoRepositrio,
				"cell 1 3");

		lblNa = new JLabel("N/A");
		lblNa.setFont(new Font("DejaVu Sans", Font.BOLD, 12));
		frmGitrepobrowser.getContentPane().add(lblNa, "cell 2 3");

		btnCarregarCommits = new JButton("Carregar Commits");
		btnCarregarCommits.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					repoHandler = new GitRepositoryHandler();
					repoHandler.openExistingRepository(txtRepoURL.getText());

					ArrayList<Commit> commits = repoHandler
							.getAllRevisions(true);

					SimpleDateFormat dateFormat = (SimpleDateFormat) DateFormat
							.getDateInstance();
					dateFormat.applyPattern("dd/MM/yyyy");

					Object[] dates = {
							dateFormat.parse("01/01/2013"),
							dateFormat.parse("31/12/2014") };

					commits = repoHandler.getRevisions(new DateCommitFilter(
							dates, commits));

					table.setModel(new TableHandler().getModelFromCommitList(
							commits, "Commit OIDs"));
					lblCommitsCarregados.setText("Commits Carregados: "
							+ commits.size());
					
					//From here its all metric extraction logic, which shouldn't be here AT ALL
					
					//Metric #1: Commits / Month
					TreeMap<String, Integer> commitsMonth = new TreeMap<>();
					
					//Metric #2: Commiters / Month
					TreeMap<String, Integer> committersMonth = new TreeMap<>();
					
					//Extracting #1

					for (Commit commit : commits) {
						@SuppressWarnings("deprecation")
						String key = ""
								+ (commit.getHumanDate().getYear() + 1900)
								+ "/"
								+ Integer.toHexString((commit.getHumanDate()
										.getMonth() + 1));
						if (!commitsMonth.containsKey(key)) {
							commitsMonth.put(key, 1);
						} else {
							commitsMonth.put(key, commitsMonth.get(key) + 1);
						}
					}
					
					//Min and maximum commits per month, for use in interpolation										

					Iterator<Entry<String, Integer>> ite = commitsMonth.entrySet().iterator();

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
					
					//After counting the commits, let's interpolate the values

					Interpolator interpolator = new Interpolator(10, 52, minCpm,
							maxCpm);					
					
					//And set them
					
					ite = commitsMonth.entrySet().iterator();
					while (ite.hasNext()) {
						Entry<String, Integer> atual = ite.next();
						atual.setValue(interpolator.interpolate(atual
								.getValue()));

					} //All done with #1
					
					//Doing #2 from here eventually patternize to do less iterators (visitor?)
					
					TreeMap<String, LinkedHashSet<String>> committersMonthTemp = new TreeMap<>();
					for (Commit commit : commits) {
						//Extra work to find out the commiters of each month for use in the con-
						//solidated metric						
						@SuppressWarnings("deprecation")
						String key = ""
								+ (commit.getHumanDate().getYear() + 1900)
								+ "/"
								+ Integer.toHexString((commit.getHumanDate()
										.getMonth() + 1));
						if (committersMonthTemp.containsKey(key)) {
							committersMonthTemp.get(key).add(commit.getCommiter());
						} else {
							LinkedHashSet<String> temp = new LinkedHashSet<String>();
							temp.add(commit.getCommiter());
							committersMonthTemp.put(key,temp);
						}
					}
					
					Iterator<Entry<String, LinkedHashSet<String>>> iteTemp = committersMonthTemp.entrySet()
							.iterator();
					
					//Setting the actual committers/month value
					while (iteTemp.hasNext()) {
						Entry<String, LinkedHashSet<String>> atual = iteTemp.next();
						committersMonth.put(atual.getKey(), atual.getValue().size());
						
					}
					
					//we can reuse the iterator from #1 here					
					ite = committersMonth.entrySet().iterator();
					
					// Ppm = People per month :x
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
					
					//also reuse the interpolator
					interpolator = new Interpolator(2, 8, minPpm,
							maxPpm);
					
					ite = committersMonth.entrySet().iterator();
					while (ite.hasNext()) {
						Entry<String, Integer> atual = ite.next();
						atual.setValue(interpolator.interpolate(atual
								.getValue()));

					} //All done with #2

					//Metrix extraction ends here
					
					//Sending the data do the template
					@SuppressWarnings("rawtypes")
					Map<String, TreeMap> root = new HashMap<String, TreeMap>();
					root.put("commitsMonth", commitsMonth);
					root.put("noteMap", new NoteMap().getMap());
					root.put("committersMonth", committersMonth);

					Configuration cfg = new Configuration(
							Configuration.VERSION_2_3_20);
					cfg.setDirectoryForTemplateLoading(new File("./templates"));
					cfg.setDefaultEncoding("UTF-8");
					cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

					Template temp = cfg.getTemplate("template_bassline.ftl");

					FileWriter fr = new FileWriter("./result1.ly", false);

					temp.process(root, fr);

				} catch (Exception erro) {
					erro.printStackTrace();
					JOptionPane
							.showMessageDialog(frmGitrepobrowser,
									"Erro ao Carregar os Commits, verifique se o repositório foi clonado :(");
				}
			}
		});
		frmGitrepobrowser.getContentPane().add(btnCarregarCommits, "cell 1 4");

		lblCommitsCarregados = new JLabel("Commits Carregados: N/A");
		frmGitrepobrowser.getContentPane()
				.add(lblCommitsCarregados, "cell 5 6");

		table = new JTable();
		table.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "Commit OID" }));
		frmGitrepobrowser.getContentPane().add(table, "cell 0 7 6 8,grow");
	}

}
