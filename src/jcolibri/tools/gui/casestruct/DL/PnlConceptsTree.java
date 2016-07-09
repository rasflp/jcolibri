/**
 * 
 */
package jcolibri.tools.gui.casestruct.DL;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.JTree;
import javax.swing.SpringLayout;
import javax.swing.border.Border;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import java.awt.*;

import jcolibri.tools.gui.SpringUtilities;
import jcolibri.util.CBRLogger;
import jcolibri.util.ImagesContainer;

import com.hp.hpl.jena.ontology.*;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.util.iterator.Filter;


import java.util.*;
import java.util.List;

/**
 * @author A. Luis Diez Hernández.
 *
 */
public class PnlConceptsTree extends JPanel  implements TreeSelectionListener{

	private static final long serialVersionUID = 1L;
	
	private JButton btnReadOnt;

	private JTree ontologyTree;
	
	private Vector concepts;
	
	private String nameSpace;
	      
    
    private static PnlConceptsTree _instance = null;
    public static PnlConceptsTree getInstance()
    {
        if(_instance == null)
            _instance = new PnlConceptsTree();
        return _instance;
    }

	/**
	 * Constructor
	 * 
	 * @param pnlDefConnJena
	 * 		panel to manage the Jena Connectors
	 */
	private PnlConceptsTree() {
		super();
		createComponents();
	}
	
	public void setPnlConnMappings(){
	}

	public Iterator getConcepts(){
		return concepts.iterator();
	}
	
	public String getNamespace(){
		return nameSpace;
	}
	
    public String getSelectedConcept(){
        return selectedConcept;
    }
    
    private String selectedConcept = null;
    public void valueChanged(TreeSelectionEvent event) {
        selectedConcept = ontologyTree.getLastSelectedPathComponent().toString();
      }
    
    public void setEnabled(boolean b)
    {
        this.btnReadOnt.setEnabled(b);
        this.ontologyTree.setEnabled(b);
    }
	
	public void createComponents(){
		JScrollPane scrPnl;
		ButtonsListener btnListener;
		JPanel pnlButtons, pnlAux;
		Border lineBorder, titleBorder, emptyBorder, compoundBorder;
		
		//set border and layout
		emptyBorder = BorderFactory.createEmptyBorder(0, 5, 0, 5);
		lineBorder = BorderFactory.createLineBorder(Color.BLACK);
		titleBorder = BorderFactory.createTitledBorder(lineBorder,
				"Ontology Structure");
		compoundBorder = BorderFactory.createCompoundBorder(titleBorder,
				emptyBorder);
		setBorder(compoundBorder);
		
		//set Ontology
		
		DefaultMutableTreeNode root= new DefaultMutableTreeNode("Thing");

		ontologyTree = new JTree(root);
		ontologyTree.setCellRenderer(new MyRenderer());
        ontologyTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        ontologyTree.addTreeSelectionListener(this);
        
        ontologyTree.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int selRow = ontologyTree.getRowForLocation(e.getX(), e.getY());
                TreePath selPath = ontologyTree.getPathForLocation(e.getX(), e.getY());
                if(selRow != -1) {
                        selectedConcept = selPath.toString();
                }
            }
        });
        
		scrPnl = new JScrollPane(ontologyTree);
		

		//Panel of buttons
		btnListener = new ButtonsListener();
		btnReadOnt = new JButton("Read ontology");
		btnReadOnt.addActionListener(btnListener);
		
		pnlButtons = new JPanel(new SpringLayout());
		pnlButtons.add(btnReadOnt);
		SpringUtilities.makeCompactGrid(pnlButtons, 1, 1, 2, 2, 1, 1);
		pnlAux = new JPanel();
		pnlAux.add(pnlButtons);
		
		setLayout(new BorderLayout());
		add(scrPnl,BorderLayout.CENTER);
		add(pnlAux,BorderLayout.PAGE_END);
	}
	
	/**
	 * Read the ontology classes.
	 * 
	 * @param m - the ontology model
	 */
	public void readOntology(OntModel m) {
		if (m == null)
			return;

		try {
			concepts = new Vector();
			// JAR pnlDefConnJena.setOntModel(m);
			String name;
			Vector inserted = new Vector();
			JScrollPane cont = (JScrollPane)((JViewport)ontologyTree.getParent()).getParent();
			cont.remove(ontologyTree);
			ontologyTree = null;
			name = "Thing";
			inserted.add(name);
			DefaultMutableTreeNode thing = new DefaultMutableTreeNode(name);
			ontologyTree = new JTree(thing);
			ontologyTree.setCellRenderer(new MyRenderer());
            ontologyTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
            ontologyTree.addTreeSelectionListener(this);

            cont.setViewportView(ontologyTree);
			 // create an iterator over the root classes that are not anonymous class expressions
	        Iterator i = m.listHierarchyRootClasses()
	                      .filterDrop( new Filter() {
	                                    public boolean accept( Object o ) {
	                                        return ((Resource) o).isAnon();
	                                    }} );
	        OntClass nxt = null;
	        while (i.hasNext()) {
	        	nxt = (OntClass) i.next();
	        	List lst = new ArrayList();
	        	lst.add(nxt.getProfile().THING());
	            DefaultMutableTreeNode child = showClass( nxt, lst, 0 );
	            if(child!=null)  thing.add(child);
	        }
	        ontologyTree.expandRow(0);
		} catch (Exception e) {
			CBRLogger.log(getClass(), CBRLogger.ERROR,
					"Error reading ontology structure " + e.getMessage());
			e.printStackTrace();
		}
	}


	 /** Present a class, then recurse down to the sub-classes.
     *  Use occurs check to prevent getting stuck in a loop
     */
    private DefaultMutableTreeNode showClass( OntClass cls, List occurs, int depth ) {
    	DefaultMutableTreeNode ret = null;
    	// recurse to the next level down
    	if (cls.canAs( OntClass.class )  &&  !occurs.contains( cls )) {
        	if(!cls.getLocalName().equalsIgnoreCase("Nothing")){
        		nameSpace = cls.getNameSpace();	
        		concepts.add(cls.getLocalName());
        		ret = new DefaultMutableTreeNode(cls.getLocalName());
        		DefaultMutableTreeNode aux;
        		for (Iterator i = cls.listSubClasses( true );  i.hasNext(); ) {
        			OntClass sub = (OntClass) i.next();
        			// 	we push this expression on the occurs list before we recurse
        			if(sub!=null && sub.getLocalName()!=null){
        				occurs.add( cls );
        				aux = showClass(  sub, occurs, depth + 1 );
        				if(aux!=null)ret.add(aux);
        			}
        		}
        	}
    	}
   		return ret;
    }

	/**
	 * Listener of button events.
	 * 
	 * @author A. Luis Diez Hernández.
	 */
	private class ButtonsListener implements ActionListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == btnReadOnt) {
				OntModel m = PnlReasonerProperties.getInstance().getJenaConnectionData().getOntmodel();
				readOntology(m);
			}
		}

	}

	class MyRenderer extends DefaultTreeCellRenderer {
		private static final long serialVersionUID = 1L;
		
		public MyRenderer() {
		}

		public Component getTreeCellRendererComponent(JTree tree, Object value,
				boolean sel, boolean expanded, boolean leaf, int row,
				boolean hasFocus) {

			super.getTreeCellRendererComponent(tree, value, sel, expanded,
					leaf, row, hasFocus);
				setIcon(ImagesContainer.CONCEPT);
			

			return this;
		}
	}
}



