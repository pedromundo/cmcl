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
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import metricsextractor.CommitsPerMonthExtractor;
import metricsextractor.CommittersPerMonthExtractor;
import net.miginfocom.swing.MigLayout;

import org.eclipse.jgit.lib.TextProgressMonitor;

import repositoryhandler.Commit;
import repositoryhandler.DateCommitFilter;
import repositoryhandler.GitRepositoryHandler;
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
							dateFormat.parse("01/01/2010"),
							dateFormat.parse("31/04/2013") };

					commits = repoHandler.getRevisions(new DateCommitFilter(
							dates, commits));

					table.setModel(new TableHandler().getModelFromCommitList(
							commits, "Commit OIDs"));
					lblCommitsCarregados.setText("Commits Carregados: "
							+ commits.size());

					//Metrix extraction ends here
					
					Map<String, Integer> commitsMonth = new CommitsPerMonthExtractor().getMetrics(commits);
					Map<String, Integer> committersMonth = new CommittersPerMonthExtractor().getMetrics(commits);
					
					//Sending the data do the template
					@SuppressWarnings("rawtypes")
					Map<String, Map> root = new HashMap<String, Map>();					
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
					
					Runtime.getRuntime().exec("lilypond result1.ly");
					

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
