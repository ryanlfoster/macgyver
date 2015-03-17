package io.macgyver.core.service;

import io.macgyver.core.graph.NodeInfo;
import io.macgyver.neorx.rest.NeoRxClient;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;


/**
 * ServiceScanner helps to structure the common MacGyver pattern of needing to traverse through the
 * graph and decorate the nodes and relationships.
 * 
 * ServiceScanner tries to be helpful in providing structure, but leaves most the implementation work to
 * concrete subclasses.
 * 
 * @author rschoening
 *
 * @param <T>
 */
public abstract class ServiceScanner<T extends Object> {

	private NeoRxClient neo4j;
	private T service;
	
	private List<Action1> decorators = Lists.newCopyOnWriteArrayList();

	
	
	public ServiceScanner(NeoRxClient neo4j, T service) {
		Preconditions.checkNotNull(neo4j);
		Preconditions.checkNotNull(service);
		this.neo4j = neo4j;
		this.service = service;
	}
	
	public T getServiceClient() {
		return service;
	}
	
	public NeoRxClient getNeoRxClient() {
		return neo4j;
	}
	public void addDecorationAction(NodeInfo.Action action) {
		Preconditions.checkNotNull(action);
		decorators.add(action);
	}
	public void addDecorationAction(Action1<NodeInfo> d) {
		Preconditions.checkNotNull(d);
		decorators.add(d);
	}
	public void decorate(long id, ObjectNode n) {
		
		NodeInfo<Object> x = new NodeInfo<Object>(id, n, neo4j, this);
		
		
		// this seems silly...need to find out how to compose Actions together
		for (Action1 decorator: decorators) {
			Observable.just(x).subscribe(decorator);
		}
	}
	public abstract void scan();
}
