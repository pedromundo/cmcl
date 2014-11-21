package main;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.TextProgressMonitor;

import repowalker.RepositoryHandler;

public class GitBrowser {

	private JFrame frmGitrepobrowser;
	private JLabel lblUrlDoRepositrio;
	private JTextField txtRepoURL;
	private JButton btnCloneRepo;
	private JLabel lblStatusDoRepositrio;
	private JLabel lblNa;
	private RepositoryHandler repoHandler;
	private TextProgressMonitor monitorClone;
	private JLabel lblPorcento;
	private JTable table;
	private JButton btnCarregarCommits;
	private JLabel lblCommitsCarregados;

	private class RunClone implements Runnable {
		@Override
		public void run() {
			try {
				new RepositoryHandler(txtRepoURL.getText(), monitorClone);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(frmGitrepobrowser, "Erro ao clonar o repositório, verifique sua conexão e tente novamente :(");
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
			protected void onUpdate(String taskName, int cmp, int totalWork,
					int pcnt) {
				super.onUpdate(taskName, cmp, totalWork, pcnt);
				lblPorcento.setText(pcnt + "%");
			}

			@Override
			protected void onEndTask(String taskName, int cmp, int totalWork,
					int pcnt) {
				if (taskName.contains("Updating references")) {
					super.onEndTask(taskName, cmp, totalWork, pcnt);
					lblNa.setText("OK!");
					lblNa.setForeground(Color.DARK_GRAY);
				}
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
				JOptionPane.showMessageDialog(frmGitrepobrowser, "Clonando!");
				Thread t = new Thread(new RunClone());
				t.start();
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
					repoHandler = new RepositoryHandler();
					repoHandler.openExistingRepository(txtRepoURL.getText());
					ArrayList<String> stringList = repoHandler
							.getAllRevisions();
					table.setModel(new TableHandler().getModelFromStrings(
							stringList, "Commit OIDs"));
					lblCommitsCarregados.setText("Commits Carregados: "+stringList.size());
				} catch (Exception erro) {
					JOptionPane.showMessageDialog(frmGitrepobrowser, "Erro ao Carregar os Commits, verifique se o repositório foi clonado :(");
				}
			}
		});
		frmGitrepobrowser.getContentPane().add(btnCarregarCommits, "cell 1 4");
		
		lblCommitsCarregados = new JLabel("Commits Carregados: N/A");
		frmGitrepobrowser.getContentPane().add(lblCommitsCarregados, "cell 5 6");

		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Commit OID"
			}
		));
		frmGitrepobrowser.getContentPane().add(table, "cell 0 7 6 8,grow");
	}

}