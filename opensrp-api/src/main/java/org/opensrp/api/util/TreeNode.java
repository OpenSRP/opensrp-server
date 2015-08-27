package org.opensrp.api.util;

import java.util.HashMap;
import java.util.Map;

public class TreeNode<K, T> {

	private K id;
	private String label;
	private T node;
	private Map<K, TreeNode<K, T>> children;
	private K parent;
	
	public TreeNode(K id, String label, T node, K parent) {
		this.id = id;
		this.label = label;
		this.node = node;
		this.parent = parent;
	}
	
	public TreeNode(K id, String label, T node, K parent, Map<K, TreeNode<K, T>> children) {
		this.id = id;
		this.label = label;
		this.node = node;
		this.parent = parent;
		this.children = children;
	}
	
	public void addChild(TreeNode<K, T> node){
		if(children == null){
			children = new HashMap<>();
		}
		children.put(node.getId(), node);
	}

	public TreeNode<K, T> findChild(K id){
		if(children != null){
			for (TreeNode<K, T> child : children.values()) {
				if(child.getId().equals(id)){
					return child;
				}
				else if(child.getChildren() != null){
					TreeNode<K, T> node = child.findChild(id);
					if(node != null) return node;
				}
			}
		}
		return null;
	}
	
	public TreeNode<K, T> removeChild(K id){
		if(children != null){
			for (TreeNode<K, T> child : children.values()) {
				if(child.getId().equals(id)){
					return children.remove(id);
				}
				else if(child.getChildren() != null){
					TreeNode<K, T> node = child.removeChild(id);
					if(node != null) return node;
				}
			}
		}
		return null;
	}
	
	public K getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}

	public T getNode() {
		return node;
	}

	public K getParent() {
		return parent;
	}

	public Map<K, TreeNode<K, T>> getChildren() {
		return children;
	}

	void setLabel(String label) {
		this.label = label;
	}

	void setNode(T node) {
		this.node = node;
	}
	
	void setParent(K parent) {
		this.parent = parent;
	}
}
