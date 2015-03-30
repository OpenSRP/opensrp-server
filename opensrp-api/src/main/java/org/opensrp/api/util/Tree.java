package org.opensrp.api.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Tree<K,T>{

	Map<K, TreeNode<K, T>> map;
	
	public Map<K, TreeNode<K, T>> getTree() {
		return Collections.unmodifiableMap(map);
	}

	public Tree() {
		map = new HashMap<K, TreeNode<K, T>>();
	}
	
	public void addNode(K id, String label, T node, K parentId) {
		if(map == null){
			map = new HashMap<K, TreeNode<K, T>>();
		}
		
		// if node exists we should break since user should write optimized code and also tree can not have duplicates
		if(getNode(id) != null){
			throw new IllegalArgumentException("Node with ID "+id+" already exists in tree");
		}
		// if no parent it is root node
		if(parentId == null){
			TreeNode<K, T> n = new TreeNode<K, T>(id, label, node, null);
			map.put(n.getId(), n);
		}
		else {
			TreeNode<K, T> p = getNode(parentId);
			if(p == null){
				throw new IllegalArgumentException("No node with Parent ID "+parentId+" found in tree");
			}
			
			TreeNode<K, T> n = new TreeNode<K, T>(id, label, node, p.getId());
			p.addChild(n);
		}
	}
	
	public TreeNode<K, T> getNode(K id) {
		// Check if id is any root node
		if (map.containsKey(id)) {
			return map.get(id);
		} 
		else {// means that we only need to check children of each root.
				// neither root itself nor parent of root
			for (TreeNode<K, T> root : map.values()) {
				TreeNode<K, T> n = root.findChild(id);
				if(n != null) return n;
			}
		}
		return null;
	}
	
	public boolean hasNode(K id) {
		return getNode(id)!=null;
	}
}
