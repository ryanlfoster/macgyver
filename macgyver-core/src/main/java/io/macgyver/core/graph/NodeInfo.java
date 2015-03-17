package io.macgyver.core.graph;

import rx.functions.Action1;
import io.macgyver.neorx.rest.NeoRxClient;

import com.fasterxml.jackson.databind.JsonNode;

public class NodeInfo<T extends Object> {

	public static interface Action extends Action1<NodeInfo> {
		
	};
	long nodeId;
	JsonNode node;
	T userData;
	NeoRxClient neoRxClient;
	
	public NodeInfo(long id, JsonNode n, NeoRxClient client, T userData) {
		this.nodeId = id;
		this.node = n;
		this.userData = userData;
		this.neoRxClient = client;
	}

	public long getNodeId() {
		return nodeId;
	}

	public JsonNode getNode() {
		return node;
	}

	public T getUserData() {
		return userData;
	}

	public NeoRxClient getNeoRxClient() {
		return neoRxClient;
	}
	
	
}
