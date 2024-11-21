package com.coffee.sashie.horadriccube.api;

import com.coffee.sashie.horadriccube.HoradricCube;
import org.yaml.snakeyaml.constructor.AbstractConstruct;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;

import java.util.Map;

/**
 * Needed to register a custom tag.
 * 
 * @param <T>
 */
public abstract class ConstructedClass<T> extends AbstractConstruct {

	/**
	 * Prepare a value for deserialization.
	 * 
	 * @param values
	 *            the serialized map
	 * @return the deserialized data
	 */
	public abstract T construct(Map<Object, Object> values);
	
	@Override
	public Object construct(Node node) {
		final Map<Object, Object> values = HoradricCube.getInstance().getConstructor().constructMap((MappingNode) node);
		return construct(values);
	}
}