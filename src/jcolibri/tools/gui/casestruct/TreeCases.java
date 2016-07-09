package jcolibri.tools.gui.casestruct;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import jcolibri.cbrcore.CBRGlobalSimilarity;
import jcolibri.cbrcore.CBRLocalSimilarity;
import jcolibri.cbrcore.DataTypesRegistry;
import jcolibri.cbrcore.GlobalSimilarityRegistry;
import jcolibri.cbrcore.LocalSimilarityRegistry;
import jcolibri.tools.data.CaseStAttribute;
import jcolibri.tools.data.CaseStComponent;
import jcolibri.tools.data.CaseStCompoundAtt;
import jcolibri.tools.data.CaseStSimpleAtt;
import jcolibri.tools.data.CaseStSimpleAttConcept;
import jcolibri.tools.data.CaseStructure;

/**
 * Visual tree to represent the structure of the case.
 * 
 * @author Antonio A. Sánchez Ruiz-Granados
 */
public class TreeCases extends JTree {
	private static final long serialVersionUID = 1L;

	/** Counter used to name new attributes. */
	private int contAtt;

	/**
	 * Constructor.
	 * 
	 * @param pnlDefCase
	 *            container panel.
	 */
	public TreeCases(FrCaseStructure pnlDefCase) {
		super();
		contAtt = 1;

		getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		setModel(new MyTreeModel());
		setCellRenderer(new MyTreeCellRenderer());
		setCaseStructure(new CaseStructure());
		addTreeSelectionListener(pnlDefCase);
	}

    
    public void reset()
    {
        setCaseStructure(new CaseStructure());
    }
	/**
	 * Returns the selected case component or null.
	 * 
	 * @return CaseStComponent.
	 */
	public CaseStComponent getSelCaseStComp() {
		MyTreeNode node;

		node = getSelectedNode();
		if (node == null)
			return null;
		else
			return (CaseStComponent) node.getUserObject();
	}

	/**
	 * Adds a new simple attribute as child of the selected node.
	 */
	public DefaultMutableTreeNode addNewAttSimple(boolean pureOnto) {
		MyTreeModel myModel;
		MyTreeNode myNode = null, mySelNode;
		CaseStAttribute att;
		String name, type;
		CBRLocalSimilarity localSim;
		float weight;

		// !! verificar que el nombre no existe ya.
		// Create attribute.
		name = "attribute" + contAtt;
		contAtt++;
		type = DataTypesRegistry.getCBRTERMDataType().getName();
		weight = 1;
		localSim = LocalSimilarityRegistry.getInstance().getDefLocalSimil();
        if (pureOnto)
            att = new CaseStSimpleAttConcept(name,type,weight,localSim,"RELATION");
        else
            att = new CaseStSimpleAtt(name, type, weight, localSim);

		// Add attribute.
		mySelNode = getSelectedNode();
		if ((mySelNode != null) && (mySelNode.getAllowsChildren())) {
			myModel = (MyTreeModel) getModel();
			myNode = new MyTreeNode(att);
			myNode.setAllowsChildren(false);
			myModel
					.insertNodeInto(myNode, mySelNode, mySelNode
							.getChildCount());
		}
        return myNode;
	}

	/**
	 * Adds a new compound attribute as child of the selected node.
	 */
	public DefaultMutableTreeNode addNewAttCompound() {
		MyTreeModel myModel;
		MyTreeNode myNode=null, mySelNode;
		CaseStAttribute att;
		String name;
		CBRGlobalSimilarity globalSim;

		// !! verificar que el nombre no existe ya.
		name = "attribute" + contAtt;
		contAtt++;
		globalSim = GlobalSimilarityRegistry.getInstance().getDefGlobalSimil();
		att = new CaseStCompoundAtt(name, 1, globalSim);

		// Add attribute.
		mySelNode = getSelectedNode();
		if ((mySelNode != null) && (mySelNode.getAllowsChildren())) {
			myModel = (MyTreeModel) getModel();
			myNode = new MyTreeNode(att);
			myModel
					.insertNodeInto(myNode, mySelNode, mySelNode
							.getChildCount());
		}
        return myNode;
	}

	/**
	 * Returns if the selected node can be removed (only attribute nodes can be
	 * removed).
	 * 
	 * @return if the selected node can be removed.
	 */
	public boolean getSelNodeRemovable() {
		MyTreeNode node;

		node = getSelectedNode();
		if (node != null)
			return node.getModifiable();
		else
			return false;
	}

	/**
	 * Removes the selected node.
	 */
	public void removeSelNode() {
		MyTreeModel model;
		MyTreeNode node;

		node = getSelectedNode();
		if (node != null) {
			model = (MyTreeModel) getModel();
			model.removeNodeFromParent(node);
		}
	}

	/**
	 * Updates the value of the selected node.
	 * 
	 * @param caseComp
	 *            new value.
	 */
	public void updateSelCaseStComp(CaseStComponent caseComp) {
		MyTreeModel model;
		MyTreeNode node;

		// Get selected node.
		node = getSelectedNode();
		if (node != null) {
			// Update attribute.
			model = (MyTreeModel) getModel();
			model.valueForPathChanged(getSelectionPath(), caseComp);
		}
	}

	/**
	 * Returns a copy of the case structure represented in the tree.
	 * 
	 * @return CaseStructure
	 */
	public CaseStructure getCaseStructure() {
		MyTreeNode nodeCase;

		nodeCase = (MyTreeNode) getModel().getRoot();
		return (CaseStructure) getCBRCaseComponent(nodeCase);
	}

	/**
	 * Sets the case structure that this tree must represent.
	 * 
	 * @param caseSt
	 *            case structure.
	 */
	public void setCaseStructure(CaseStructure caseSt) {
		MyTreeModel treeModel;

		treeModel = (MyTreeModel) getModel();
		treeModel.setRoot(createNodeFromCaseComponent(caseSt));
		treeModel.reload();
	}

	/**
	 * Creates a tree node that represents a case component.
	 * 
	 * @param comp
	 *            case component.
	 * @return tree node.
	 */
	private MyTreeNode createNodeFromCaseComponent(CaseStComponent comp) {
		MyTreeNode node, nodeAux;

		node = new MyTreeNode(comp);
		node.setAllowsChildren(comp.canHaveChildrens());
		for (int i = 0; i < comp.getNumChildrens(); i++) {
			nodeAux = createNodeFromCaseComponent(comp.getChild(i));
			node.add(nodeAux);
		}
		return node;
	}

	/**
	 * Returns the case component associated to the tree node.
	 * 
	 * @param node
	 *            tree node.
	 * @return case component.
	 */
	private CaseStComponent getCBRCaseComponent(MyTreeNode node) {
		MyTreeNode childNode;
		CaseStComponent comp, childComp;

		comp = (CaseStComponent) node.getUserObject();
		comp.removeAllChildrens();
		for (int i = 0; i < node.getChildCount(); i++) {
			childNode = (MyTreeNode) node.getChildAt(i);
			childComp = getCBRCaseComponent(childNode);
			comp.addChild(childComp);
		}

		return comp;
	}

	/**
	 * Returns de selected node or null.
	 * 
	 * @return the selected node or null.
	 */
	private MyTreeNode getSelectedNode() {
		TreePath path;
		MyTreeNode node;

		path = getSelectionPath();
		if (path != null)
			node = (MyTreeNode) path.getLastPathComponent();
		else
			node = null;

		return node;
	}

	/**
	 * Tree node.
	 * 
	 * @author Antonio
	 */
	class MyTreeNode extends DefaultMutableTreeNode {
		private static final long serialVersionUID = 1L;

		/** Indicates if the node can be removed and the userObject overriden. */
		private boolean modifiable;

		/**
		 * Constructor.
		 * 
		 * @param userObject
		 *            user object.
		 */
		public MyTreeNode(Object userObject) {
			super(userObject);
			modifiable = true;
		}

		/**
		 * Returns if the node can be delete or modified.
		 * 
		 * @return
		 */
		public boolean getModifiable() {
			return modifiable;
		}

		/**
		 * Sets if the node can be modified.
		 * 
		 * @param modifiable
		 */
		public void setModifiable(boolean modifiable) {
			this.modifiable = modifiable;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.tree.MutableTreeNode#setUserObject(java.lang.Object)
		 */
		public void setUserObject(Object userObject) {
			if (!modifiable)
				throw new IllegalStateException(
						"node does not allow change user object");
			super.setUserObject(userObject);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.tree.MutableTreeNode#removeFromParent()
		 */
		public void removeFromParent() {
			if (!modifiable)
				throw new IllegalStateException(
						"node does not allow be removed");
			super.removeFromParent();
		}
	}

	/**
	 * Data model.
	 * 
	 * @author Antonio
	 */
	class MyTreeModel extends DefaultTreeModel {
		private static final long serialVersionUID = 1L;

		/**
		 * Constructor.
		 */
		public MyTreeModel() {
			super(null);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.tree.TreeModel#isLeaf(java.lang.Object)
		 */
		public boolean isLeaf(Object node) {
			MyTreeNode myNode;

			myNode = (MyTreeNode) node;
			if (myNode.getAllowsChildren() || (myNode.getChildCount() > 0))
				return false;
			else
				return true;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.tree.DefaultTreeModel#removeNodeFromParent(javax.swing.tree.MutableTreeNode)
		 */
		public void removeNodeFromParent(MutableTreeNode node) {
			MyTreeNode myNode;

			myNode = (MyTreeNode) node;
			if (!myNode.getModifiable())
				return;

			super.removeNodeFromParent(node);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.tree.TreeModel#valueForPathChanged(javax.swing.tree.TreePath,
		 *      java.lang.Object)
		 */
		public void valueForPathChanged(TreePath path, Object newValue) {
			MyTreeNode node;

			node = (MyTreeNode) path.getLastPathComponent();
			if (!node.getModifiable())
				return;

			super.valueForPathChanged(path, newValue);
		}

	}

	/**
	 * Renderer.
	 * 
	 * @author Antonio
	 */
	class MyTreeCellRenderer extends DefaultTreeCellRenderer {
		private static final long serialVersionUID = 1L;

		/**
		 * Constructor.
		 */
		public MyTreeCellRenderer() {
			super();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.tree.TreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree,
		 *      java.lang.Object, boolean, boolean, boolean, int, boolean)
		 */
		public Component getTreeCellRendererComponent(JTree tree, Object value,
				boolean sel, boolean expanded, boolean leaf, int row,
				boolean hasFocus) {
			CaseStComponent caseComp;

			// Use getName() of CaseStComponent as tree node name.
			caseComp = (CaseStComponent) ((MyTreeNode) value).getUserObject();
			leaf = !caseComp.canHaveChildrens();
			return super.getTreeCellRendererComponent(tree, caseComp.getName(),
					sel, expanded, leaf, row, hasFocus);
		}
	}
}
