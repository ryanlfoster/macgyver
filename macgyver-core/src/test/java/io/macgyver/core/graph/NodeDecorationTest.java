package io.macgyver.core.graph;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import rx.Observable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class NodeDecorationTest {

	
	@Test
	public void testIt() {
		
		final JsonNode n = new ObjectMapper().createObjectNode();
		
		NodeInfo x = new NodeInfo<Object>(99, n, null, this);
		final NodeDecorationTest outer = this;
		NodeInfo.Action action = new NodeInfo.Action() {

			@Override
			public void call(NodeInfo decoration) {
	
				assertThat(decoration).isNotNull();
				assertThat(decoration.getNodeId()).isEqualTo(99);
				assertThat(decoration.getNode()).isSameAs(n);
				assertThat(decoration.getUserData()).isSameAs(outer);
				
			}
		};
		
	
		Observable.just(x).subscribe(action);
		
	
		

	}
}
