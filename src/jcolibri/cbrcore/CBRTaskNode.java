package jcolibri.cbrcore;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Instances of this class compose the internal representation of the task
 * hierarchy.
 */
public class CBRTaskNode implements Serializable {
	private static final long serialVersionUID = 1L;

	/** Asociated CBRTask. */
	private CBRTask task;

	/** Children task nodes. */
	private LinkedList children;

	/** Parent in the task hierarchy. */
	private CBRTaskNode parentRef;

	/**
	 * Constructor.
	 * 
	 * @param task
	 *            associated CBRTask.
	 */
	public CBRTaskNode(CBRTask task) {
		this.task = task;
		this.children = new LinkedList();
		this.parentRef = this;
	}

	/**
	 * Constructor.
	 * 
	 * @param task
	 *            associated CBRTask.
	 * @param children
	 *            children task nodes.
	 */
	public CBRTaskNode(CBRTask task, LinkedList children) {
		this.task = task;
		this.children = children;
		this.parentRef = this;
	}

	/**
	 * Constructor.
	 * 
	 * @param task
	 *            associated CBRTask.
	 * @param children
	 *            children task nodes.
	 * @param parentRef
	 *            parent task node.
	 */
	public CBRTaskNode(CBRTask task, LinkedList children, CBRTaskNode parentRef) {
		this.task = task;
		this.children = children;
		this.parentRef = parentRef;
	}

	/**
	 * Returns the asociated CBRTask.
	 * 
	 * @return CBRTask.
	 */
	public CBRTask getTask() {
		return task;
	}

	/**
	 * Returns the children task nodes.
	 * 
	 * @return chiildren tasks nodes.
	 */
	public LinkedList getChildren() {
		return children;
	}

	/**
	 * Sets the parent task node.
	 * 
	 * @param parent
	 *            parent task node.
	 */
	public void setParentRef(CBRTaskNode parent) {
		this.parentRef = parent;
	}

	/**
	 * Returns the parent task node.
	 * 
	 * @return parent task node.
	 */
	public CBRTaskNode getParentRef() {
		return parentRef;
	}

	/**
	 * Sets the children task nodes.
	 * 
	 * @param newList
	 */
	public void setChildren(LinkedList newList) {
		this.children = newList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return task.getName();
	}

	/**
	 * Checks if 2 CBRTasksNode are equals.
	 * 
	 * @param o
	 *            the other CBRTaskNode.
	 */
	public boolean equals(Object o) {
		CBRTaskNode otherNode = (CBRTaskNode) o;
		return task.getInstanceName().equals(
				otherNode.getTask().getInstanceName());
	}
}