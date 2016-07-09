package jcolibri.gui.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.tree.DefaultTreeModel;

import jcolibri.cbrcore.CBRState;
import jcolibri.cbrcore.CBRTaskNode;
import jcolibri.cbrcore.MethodType;

/**
 * 
 * @author Juan José Bello
 */
public class TaskTreeNode implements javax.swing.tree.TreeNode {

	// This map will be just used to avoid creating duplicated objects. //mega
	// chapuza
	public static HashMap childrenMap = new HashMap();

	CBRTaskNode node;

	DefaultTreeModel model;

	CBRState state;

	/** Creates a new instance of TaskTreeNode */
	public TaskTreeNode(CBRTaskNode task) {
		node = task;
		childrenMap.put(node, this);
	}

	public java.util.Enumeration children() {
		Vector v = new Vector();
		for (Iterator it = node.getChildren().iterator(); it.hasNext();) {
			v.add(associatedTaskTreeNode((CBRTaskNode) it.next()));
		}
		return v.elements();
	}

	public boolean getAllowsChildren() {
		return node.getTask().getMethodInstance().getMethodType().equals(
				MethodType.DECOMPOSITION);
	}

	public int getChildCount() {
		return node.getChildren().size();
	}

	public int getIndex(javax.swing.tree.TreeNode child) {
		List list = this.node.getChildren();
		CBRTaskNode task;
		int counter = 0;
		for (Iterator it = list.iterator(); it.hasNext();) {
			task = (CBRTaskNode) it.next();
			if (task.equals(((TaskTreeNode) child).node))
				return counter;
			counter++;
		}
		return -1;
	}

	private TaskTreeNode associatedTaskTreeNode(CBRTaskNode task) {
		TaskTreeNode result = (TaskTreeNode) childrenMap.get(task);
		if (result == null) {
			result = new TaskTreeNode(task);
			childrenMap.put(task, result);
		}
		return result;
	}

	public javax.swing.tree.TreeNode getParent() {
		CBRTaskNode task = node.getParentRef();
		return associatedTaskTreeNode(task);
	}

	public javax.swing.tree.TreeNode getChildAt(int childIndex) {
		CBRTaskNode task = (CBRTaskNode) node.getChildren().get(childIndex);
		return associatedTaskTreeNode(task);
	}

	public boolean isLeaf() {
		if (node.getTask().getMethodInstance() != null) {
			return node.getTask().getMethodInstance().getMethodType().equals(
					MethodType.RESOLUTION);
		} else
			return false;
	}

	public CBRTaskNode getCBRTaskNode() {
		return node;
	}

	public boolean equals(TaskTreeNode treeNode) {
		return node.equals(treeNode.node);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return node.toString();
	}

}
