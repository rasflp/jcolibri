package jcolibri.gui;

import java.awt.Event;
import java.awt.FileDialog;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;

import jcolibri.application.template.CBRAppTemplateFiller;
import jcolibri.cbrcore.CBRCore;
import jcolibri.cbrcore.exception.InternalException;
import jcolibri.evaluation.EvaluatorsFactory;
import jcolibri.evaluation.gui.EvaluationGUI;
import jcolibri.tools.gui.casestruct.FrCaseStructure;
import jcolibri.tools.gui.connector.FrConnector;
import jcolibri.tools.gui.datatype.FrDataTypes;
import jcolibri.tools.gui.similarity.FrSimilarities;
import jcolibri.util.BrowserControl;
import jcolibri.util.CompileAndRun;
import jcolibri.util.ImagesContainer;

/**
 * 
 * @author Juan Jos� Bello
 */
public class CBRArmGUI extends javax.swing.JFrame {
	
    private static final String TITLE= "jCOLIBRI 1.1";
    
    private static final long serialVersionUID = 1L;

	private static CBRArmGUI reference;

	private static CBRCore core;

	private static String fileName;
    

	// private boolean usesTools;
	// private DlgMain frMain;

	/** Creates new form CBRArmGUI */
	public CBRArmGUI() {
		initComponents();
		reference = this;
		appFrame = null;
	}

	public static CBRArmGUI getReference() {
		return reference;
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	private void initComponents() { // GEN-BEGIN:initComponents
		desktopPane = new javax.swing.JDesktopPane();
		statusPanel = new LogPanel();
		menuBar = new javax.swing.JMenuBar();
		fileMenu = new javax.swing.JMenu();
		openApplicationMenuitem = new javax.swing.JMenuItem();
		saveMenuItem = new javax.swing.JMenuItem();
		saveAsMenuItem = new javax.swing.JMenuItem();
		exitMenuItem = new javax.swing.JMenuItem();
		CBR = new javax.swing.JMenu();
		manageTasksMenuItem = new javax.swing.JMenuItem();
		manageMethodsMenuItem = new javax.swing.JMenuItem();
		manageCaseStructuresMenuItem = new javax.swing.JMenuItem();
		manageConnectorsMenuItem = new javax.swing.JMenuItem();
		manageSimilaritiesMenuItem = new javax.swing.JMenuItem();
		manageDataTypesMenuItem = new javax.swing.JMenuItem();
		newCBRMenuItem = new javax.swing.JMenuItem();
		helpMenu = new javax.swing.JMenu();
		contentMenuItem = new javax.swing.JMenuItem();
		aboutMenuItem = new javax.swing.JMenuItem();
		generateMenuItem = new javax.swing.JMenuItem();
		generateAndRunMenuItem = new JMenuItem();
        
        
        this.setIconImage(new ImageIcon(getClass().getResource("/jcolibri/resources/jcolibri.gif")).getImage());
        

		setTitle(TITLE);
		java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit()
				.getScreenSize();
		setBounds((screenSize.width - 800) / 2, (screenSize.height - 600) / 2,
				800, 600);

		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent evt) {
				exitForm(evt);
			}
		});
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				desktopPane, statusPanel);
		getContentPane().add(splitPane, java.awt.BorderLayout.CENTER);

		this.repaint();
		splitPane.setDividerSize(2);
		splitPane.setDividerLocation(500);
		// getContentPane().add(desktopPane, java.awt.BorderLayout.CENTER);
		// statusPanel.setBorder(new
		// javax.swing.border.LineBorder(java.awt.SystemColor.inactiveCaptionBorder));
		// getContentPane().add(statusPanel, java.awt.BorderLayout.SOUTH);

        
        /////////////////////////////////////////////////////////////////////////
        // HELP menu
        /////////////////////////////////////////////////////////////////////////
        
		fileMenu.setText("File");

		openApplicationMenuitem.setText("Open CBR application");
        openApplicationMenuitem.setIcon(ImagesContainer.LOAD);
        openApplicationMenuitem.setAccelerator(KeyStroke.getKeyStroke('O',Event.CTRL_MASK));
		openApplicationMenuitem
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						openApplicationMenuitemActionPerformed(evt);
					}
				});
		fileMenu.add(openApplicationMenuitem);

		saveMenuItem.setText("Save");
        saveMenuItem.setIcon(ImagesContainer.SAVE);        
        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke('S',Event.CTRL_MASK));
		saveMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				saveMenuItemActionPerformed(evt);
			}
		});
		fileMenu.add(saveMenuItem);
		saveMenuItem.setEnabled(false);

		saveAsMenuItem.setText("Save As ...");
        saveAsMenuItem.setIcon(ImagesContainer.SAVE_AS); 
		saveAsMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				saveAsMenuItemActionPerformed(evt);
			}
		});
		fileMenu.add(saveAsMenuItem);
		saveAsMenuItem.setEnabled(false);

		fileMenu.addSeparator();
		generateMenuItem.setText("Generate application template");
        generateMenuItem.setIcon(ImagesContainer.GENERATE);         
        generateMenuItem.setAccelerator(KeyStroke.getKeyStroke('G',Event.CTRL_MASK));
		generateMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				generateMenuItemActionPerformed(evt);
			}
		});
		generateMenuItem.setEnabled(false);
		fileMenu.add(generateMenuItem);

		generateAndRunMenuItem.setText("Generate & Run application template");
        generateAndRunMenuItem.setIcon(ImagesContainer.GENERATE_AND_RUN); 
        generateAndRunMenuItem.setAccelerator(KeyStroke.getKeyStroke('R',Event.CTRL_MASK));
		generateAndRunMenuItem
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						generateAndRunMenuItemActionPerformed(evt);
					}
				});
		generateAndRunMenuItem.setEnabled(false);
		fileMenu.add(generateAndRunMenuItem);

		fileMenu.addSeparator();
		exitMenuItem.setText("Exit");
        exitMenuItem.setIcon(ImagesContainer.EXIT);
        exitMenuItem.setAccelerator(KeyStroke.getKeyStroke('Q',Event.CTRL_MASK));
		exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				exitMenuItemActionPerformed(evt);
			}
		});

		fileMenu.add(exitMenuItem);

		menuBar.add(fileMenu);

        
        /////////////////////////////////////////////////////////////////////////
        // CBR menu
        /////////////////////////////////////////////////////////////////////////        
        
		CBR.setText("CBR");

		newCBRMenuItem.setText("New CBR system");
        newCBRMenuItem.setIcon(ImagesContainer.CBRSYSTEM);
        newCBRMenuItem.setAccelerator(KeyStroke.getKeyStroke('N',Event.CTRL_MASK));
        newCBRMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				newCBRMenuItemActionPerformed(evt);
			}
		});

		CBR.add(newCBRMenuItem);

		CBR.addSeparator();

		manageTasksMenuItem.setText("Manage Tasks");
        manageTasksMenuItem.setIcon(ImagesContainer.TASK_DECOM);
        manageTasksMenuItem
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						manageTasksMenuItemActionPerformed(evt);
					}
				});
		manageTasksMenuItem.setEnabled(false);
		CBR.add(manageTasksMenuItem);

		manageMethodsMenuItem.setText("Manage Methods");
        manageMethodsMenuItem.setIcon(ImagesContainer.TASK_EXEC);
        manageMethodsMenuItem
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						manageMethodsMenuItemActionPerformed(evt);
					}
				});
		manageMethodsMenuItem.setEnabled(false);
		CBR.add(manageMethodsMenuItem);

		manageCaseStructuresMenuItem.setText("Manage Case Structures");
        manageCaseStructuresMenuItem.setIcon(ImagesContainer.CASESTRUCTURE);
        manageCaseStructuresMenuItem
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						manageCaseStructuresMenuItemActionPerformed(evt);
					}
				});
		manageCaseStructuresMenuItem.setEnabled(false);
		CBR.add(manageCaseStructuresMenuItem);

		manageConnectorsMenuItem.setText("Manage Connectors");
        manageConnectorsMenuItem.setIcon(ImagesContainer.CONNECTOR);
        manageConnectorsMenuItem
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						manageConnectorsMenuItemActionPerformed(evt);
					}
				});
		manageConnectorsMenuItem.setEnabled(false);
		CBR.add(manageConnectorsMenuItem);

		manageSimilaritiesMenuItem.setText("Manage Similarities");
        manageSimilaritiesMenuItem.setIcon(ImagesContainer.SIMILARITY);
        manageSimilaritiesMenuItem
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						manageSimilaritiesMenuItemActionPerformed(evt);
					}
				});
		manageSimilaritiesMenuItem.setEnabled(false);
		CBR.add(manageSimilaritiesMenuItem);

		manageDataTypesMenuItem.setText("Manage DataTypes");
        manageDataTypesMenuItem.setIcon(ImagesContainer.DATA_TYPE);
		manageDataTypesMenuItem
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						manageDataTypesMenuItemActionPerformed(evt);
					}
				});
		manageDataTypesMenuItem.setEnabled(false);
		CBR.add(manageDataTypesMenuItem);

		menuBar.add(CBR);

        /////////////////////////////////////////////////////////////////////////
        // EVALUATION menu
        /////////////////////////////////////////////////////////////////////////
        
        Evaluation = new JMenu();
        configureEvaluation = new JMenuItem();
        runEvaluation = new JMenuItem();
        
        Evaluation.setText("Evaluation");
        
        configureEvaluation.setText("Configure");
        configureEvaluation.setIcon(ImagesContainer.CONFIG_EVALUATION);
        configureEvaluation.setAccelerator(KeyStroke.getKeyStroke('C',Event.CTRL_MASK));
        configureEvaluation.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                configureEvaluation(e);
            }
        });
        configureEvaluation.setEnabled(false);
        
        runEvaluation.setText("Evaluate");
        runEvaluation.setIcon(ImagesContainer.BARGRAPH);
        runEvaluation.setAccelerator(KeyStroke.getKeyStroke('E',Event.CTRL_MASK));
        runEvaluation.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                runEvaluation(e);
            }
        });
        runEvaluation.setEnabled(false);
        
        Evaluation.add(configureEvaluation);
        Evaluation.addSeparator();
        Evaluation.add(runEvaluation);
        
        menuBar.add(Evaluation);

        
        /////////////////////////////////////////////////////////////////////////
        // HELP menu
        /////////////////////////////////////////////////////////////////////////
        
		helpMenu.setText("Help");
		contentMenuItem.setText("Contents");
        contentMenuItem.setIcon(ImagesContainer.HELP_CONTENTS);
		contentMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				HelpFrame hf = new HelpFrame();
				hf.setVisible(true);
			}
		});
        contentMenuItem.setAccelerator(KeyStroke.getKeyStroke('H',Event.CTRL_MASK));

        
		helpMenu.add(contentMenuItem);
		helpMenu.addSeparator();
		JMenuItem APIMenuItem = new JMenuItem("jCOLIBRI API");
        APIMenuItem.setIcon(ImagesContainer.HELP_API);
		APIMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {		    
			    BrowserControl.displayURL("file://"+new File("."+File.separatorChar+"doc"+File.separatorChar+"index.html").getAbsolutePath());
			}
		});
		helpMenu.add(APIMenuItem);
		
		JMenuItem tutorialsMenuItem = new JMenuItem("Tutorials");
        tutorialsMenuItem.setIcon(ImagesContainer.TUTORIALS);
		tutorialsMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {		    
			    BrowserControl.displayURL("http://gaia.fdi.ucm.es/projects/jcolibri/tutorials.html");
			}
		});
		helpMenu.add(tutorialsMenuItem);

		JMenuItem homePageMenuItem = new JMenuItem("Home Page");
        homePageMenuItem.setIcon(ImagesContainer.HOME);
		homePageMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {		    
			    BrowserControl.displayURL("http://gaia.fdi.ucm.es/projects/jcolibri/");
			}
		});
		helpMenu.add(homePageMenuItem);

		
		helpMenu.addSeparator();
		
		
		aboutMenuItem.setText("About");
        aboutMenuItem.setIcon(ImagesContainer.ABOUT);
		aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				String message = "jCOLIBRI has been developed by: \n - Bel�n D�az Agudo\n - Pedro A. Gonz�lez Calero\n - Juan Jos� Bello Tom�s\n - Juan Antonio Recio Garc�a\n - Antonio S�nchez Ruiz-Granados\n - Pablo Beltr�n Ferruz\n\nGAIA - Group for Artificial Intelligence Applications\nhttp://gaia.fdi.ucm.es\nUniversidad Complutense de Madrid\n\nhttp://sourceforge.net/projects/jcolibri-cbr/";
				JOptionPane.showMessageDialog(null, message);
			}
		});

		helpMenu.add(aboutMenuItem);
		menuBar.add(helpMenu);

		setJMenuBar(menuBar);
	} 

    private void configureEvaluation(java.awt.event.ActionEvent evt)
    {
        JInternalFrame internalFrame = new EvaluationGUI(this, core);
        desktopPane.add(internalFrame);
        internalFrame.show();               
    }
    
    private void runEvaluation(java.awt.event.ActionEvent evt)
    {
        FileDialog fd = new FileDialog(this,
                "Load evaluation XML description...", FileDialog.LOAD);
        fd.setVisible(true);
        if (fd.getFile() == null) 
            return;
        String fileName = fd.getDirectory() + fd.getFile();
        EvaluatorsFactory.getEvaluator(fileName,core).evaluate();
    }
    
	private void manageMethodsMenuItemActionPerformed(
			java.awt.event.ActionEvent evt) {
		// GEN-FIRST:event_manageMethodsMenuItemActionPerformed
		JInternalFrame internalFrame = new MethodsFrame();
		desktopPane.add(internalFrame);
		internalFrame.show();
	} // GEN-LAST:event_manageMethodsMenuItemActionPerformed

	private void manageCaseStructuresMenuItemActionPerformed(
			java.awt.event.ActionEvent evt) {
		// GEN-FIRST:event_manageCaseStructuresMenuItemActionPerformed
		JInternalFrame internalFrame = new FrCaseStructure();
		desktopPane.add(internalFrame);
		internalFrame.show();
	} // GEN-LAST:event_manageCaseStructuresMenuItemActionPerformed

	private void manageConnectorsMenuItemActionPerformed(
			java.awt.event.ActionEvent evt) {
		// GEN-FIRST:event_manageConnectorsMenuItemActionPerformed
		JInternalFrame internalFrame = new FrConnector(null);
		desktopPane.add(internalFrame);
		internalFrame.show();
	} // GEN-LAST:event_manageConnectorsMenuItemActionPerformed

	private void manageSimilaritiesMenuItemActionPerformed(
			java.awt.event.ActionEvent evt) {
		// GEN-FIRST:event_manageSimilaritiesMenuItemActionPerformed
		JInternalFrame internalFrame = new FrSimilarities();
		desktopPane.add(internalFrame);
		internalFrame.show();
	} // GEN-LAST:event_manageSimilaritiesMenuItemActionPerformed

	private void manageDataTypesMenuItemActionPerformed(
			java.awt.event.ActionEvent evt) {
		JInternalFrame internalFrame = new FrDataTypes();
		desktopPane.add(internalFrame);
		internalFrame.show();
	}

	private void openApplicationMenuitemActionPerformed(ActionEvent evt) {
		try {
			if (!CloseAppFrame())
				return;

			FileDialog fd = new FileDialog(this,
					"Load jCOLIBRI application from file...", FileDialog.LOAD);
			fd.setVisible(true);
			if (fd.getFile() != null) {
				fileName = fd.getDirectory() + fd.getFile();
				core = new CBRCore();
				// CHANGE r4 //core.init();
				core.loadApplication(fileName);
				appFrame = new CBRConfigurationFrame(core, this);
				desktopPane.add(appFrame);
				appFrame.show();
				try {
					appFrame.setMaximum(true);
				} catch (java.beans.PropertyVetoException pve) {
					pve.printStackTrace();
				}
				setEnableMenus(true);
			}
		} catch (InternalException ie) {
			JOptionPane.showMessageDialog(this, ie.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void saveAsMenuItemActionPerformed(ActionEvent evt) {
		try {
			FileDialog fd = new FileDialog(this,
					"Save jCOLIBRI application to file...", FileDialog.SAVE);
			fd.setVisible(true);
			if (fd.getFile() != null) {
				fileName = fd.getDirectory() + fd.getFile();
				core.persistApplication(fileName);
			}
		} catch (InternalException ie) {
			ie.printStackTrace();
			JOptionPane.showMessageDialog(this, ie.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private String generateMenuItemActionPerformed(ActionEvent evt) {
		String filePath = null;
		try {
			FileDialog fd = new FileDialog(this, "Generate java file ...",
					FileDialog.SAVE);
			fd.setFile(CBRAppTemplateFiller.escape(core.getName()) + ".java");
			fd.setDirectory("src/application");
			fd.setFilenameFilter(new FilenameFilter() {
				public boolean accept(File file, String name) {
					return name.endsWith(".java");
				}
			}); // El filtro no funciona en windows :(
			fd.setVisible(true);
			if (fd.getFile() != null) {
				filePath = fd.getDirectory() + fd.getFile();
				File file = new File(filePath);
				CBRAppTemplateFiller.generateApplication(core, file);
			}
		} catch (Exception ie) {
			JOptionPane.showMessageDialog(this, ie.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		return filePath;
	}

	private void generateAndRunMenuItemActionPerformed(ActionEvent evt) {
		String filePath = this.generateMenuItemActionPerformed(evt);
		if (filePath == null)
			return;
		int pos = filePath.indexOf("src");
		filePath = filePath.substring(pos);
		CompileAndRun.compileAndRun(filePath);

	}

	private void saveMenuItemActionPerformed(ActionEvent evt) {
		if (fileName == null)
			saveAsMenuItemActionPerformed(evt);
		else {
			try {
				core.persistApplication(fileName);
			} catch (InternalException ie) {
				JOptionPane.showMessageDialog(this, ie.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void newCBRMenuItemActionPerformed(java.awt.event.ActionEvent evt) {

		if (!CloseAppFrame())
			return;

		String input = JOptionPane.showInputDialog(this,
				"CBR Application name:");
		if (input == null || input.equals(""))
			return;

		PackageChooser pc = new PackageChooser(this);
		pc.setVisible(true);

		core = new CBRCore(input);
		core.init();

		setEnableMenus(true);

		// GEN-FIRST:event_newCBRMenuItemActionPerformed
		appFrame = new CBRConfigurationFrame(core, this);
		appFrame.setTitle("CBR -" + input);
		desktopPane.add(appFrame);
		appFrame.show();
		try {
			appFrame.setMaximum(true);
		} catch (java.beans.PropertyVetoException pve) {
			pve.printStackTrace();
		}

	} // GEN-LAST:event_newCBRMenuItemActionPerformed

	public boolean CloseAppFrame() {
		if (appFrame == null)
			return true;
		int res = JOptionPane.showConfirmDialog(this,
				"Do you want to save your changes?", "Save?",
				JOptionPane.YES_NO_CANCEL_OPTION);
		if (res == JOptionPane.YES_OPTION)
			saveMenuItemActionPerformed(null);
		else if (res == JOptionPane.CANCEL_OPTION)
			return false;
		this.desktopPane.remove(appFrame);
		appFrame.dispose();
		appFrame = null;
		this.desktopPane.repaint();
		setEnableMenus(false);
		return true;
	}

	private void manageTasksMenuItemActionPerformed(
			java.awt.event.ActionEvent evt) {
		// GEN-FIRST:event_manageTasksMenuItemActionPerformed
		JInternalFrame internalFrame = new TasksFrame();
		desktopPane.add(internalFrame);
		internalFrame.show();
	} // GEN-LAST:event_manageTasksMenuItemActionPerformed

	private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
		// GEN-FIRST:event_exitMenuItemActionPerformed
		System.exit(0);
	} // GEN-LAST:event_exitMenuItemActionPerformed

	/** Exit the Application */
	private void exitForm(java.awt.event.WindowEvent evt) { // GEN-FIRST:event_exitForm
		System.exit(0);
	} // GEN-LAST:event_exitForm

	private void setEnableMenus(boolean enable) {
		manageTasksMenuItem.setEnabled(enable);
		manageMethodsMenuItem.setEnabled(enable);
		manageCaseStructuresMenuItem.setEnabled(enable);
		manageConnectorsMenuItem.setEnabled(enable);
		manageDataTypesMenuItem.setEnabled(enable);
		manageSimilaritiesMenuItem.setEnabled(enable);
		this.generateAndRunMenuItem.setEnabled(enable);
		this.generateMenuItem.setEnabled(enable);
		this.saveAsMenuItem.setEnabled(enable);
		this.saveMenuItem.setEnabled(enable);
        this.configureEvaluation.setEnabled(enable);
        this.runEvaluation.setEnabled(enable);
	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		new CBRArmGUI().setVisible(true);
		LogoFrame logo = new LogoFrame();
		try {
			Thread.sleep(2000);
		} catch (Exception e) {
		}
		logo.dispose();

	}

    private javax.swing.JMenu fileMenu;
        private javax.swing.JMenuItem openApplicationMenuitem;
        private javax.swing.JMenuItem saveAsMenuItem;
        private javax.swing.JMenuItem saveMenuItem;
        private javax.swing.JMenuItem generateMenuItem;
        private javax.swing.JMenuItem generateAndRunMenuItem;
        private javax.swing.JMenuItem exitMenuItem;
    
    private javax.swing.JMenu CBR;
        private javax.swing.JMenuItem newCBRMenuItem;
        private javax.swing.JMenuItem manageMethodsMenuItem;
        private javax.swing.JMenuItem manageTasksMenuItem;
        private javax.swing.JMenuItem manageCaseStructuresMenuItem;
        private javax.swing.JMenuItem manageConnectorsMenuItem;
        private javax.swing.JMenuItem manageSimilaritiesMenuItem;
        private javax.swing.JMenuItem manageDataTypesMenuItem;
    
    private javax.swing.JMenu Evaluation;
        private javax.swing.JMenuItem configureEvaluation;
        private javax.swing.JMenuItem runEvaluation;
    
        
    private javax.swing.JMenu helpMenu;
        private javax.swing.JMenuItem aboutMenuItem;
        private javax.swing.JMenuItem contentMenuItem;

    private javax.swing.JDesktopPane desktopPane;
	private javax.swing.JMenuBar menuBar;
	private javax.swing.JComponent statusPanel;


	private javax.swing.JInternalFrame appFrame;

}