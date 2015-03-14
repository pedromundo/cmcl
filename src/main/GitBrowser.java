package main;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

import core.Maestro;
import core.WorkingSet;
import repositoryhandler.Commit;
import repositoryhandler.DateCommitFilter;
import repositoryhandler.GitRepositoryHandler;
import util.NoteMap;

public class GitBrowser {
	
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
	private GitRepositoryHandler repoHandler;
	private TextProgressMonitor monitorClone;

	private JButton btnCarregarCommits;

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
		
		frmGitrepobrowser = new JFrame();
		frmGitrepobrowser.setTitle("Git Repository Browser");
		frmGitrepobrowser.setBounds(100, 100, 640, 82);
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
		
				btnCarregarCommits = new JButton("Gerar Sonorização!");
				btnCarregarCommits.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						try {					
							
							SimpleDateFormat dateFormat = (SimpleDateFormat) DateFormat
									.getDateInstance();
							dateFormat.applyPattern("dd/MM/yyyy");

							Object[] dates = {
									dateFormat.parse("01/01/2013"),
									dateFormat.parse("31/12/2014") };
							
							WorkingSet ws = new WorkingSet("template_bassline");					
							ws.initRepoHandler(txtRepoURL.getText());					
							ws.setFilter(new DateCommitFilter(dates, ws.getRepoHandler().getAllRevisions(true)));
							
							ws.putExtractor(new CommitsPerMonthExtractor());
							ws.putExtractor(new CommittersPerMonthExtractor());
							
							ws.setFileName("Duse");
							
							Maestro vivaldi = new Maestro(ws);							
							vivaldi.makeMusic();
							
							JOptionPane
							.showMessageDialog(frmGitrepobrowser,
									"Sonorização gerada com sucesso! :D");
							
							
						} catch (Exception erro) {
							erro.printStackTrace();
							JOptionPane
									.showMessageDialog(frmGitrepobrowser,
											"Ocorreu um problema ao gerar a sonorização :(");
						}
					}
				});
				
				frmGitrepobrowser.getContentPane().add(btnCarregarCommits, "cell 4 1");
	}

}
