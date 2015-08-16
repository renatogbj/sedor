package br.com.sedor.odonto;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

public class Attributes {

	private HashMap<String, String> type;
	private HashMap<String, Double> probAttribute;
	private HashMap<String, HashMap<String, Double>> attribute;

	public Attributes() {
		attribute = new HashMap<String, HashMap<String, Double>>();
		type = new HashMap<String, String>();
		probAttribute = new HashMap<String, Double>();
	}

	public void addAtributo(String name, Double prob, String type) {
		attribute.put(name, new HashMap<String, Double>());
		this.type.put(name, type);
		probAttribute.put(name, prob);
	}
	
	public void addValue(String nameAttribute, String nameValue, Double prob) {
		HashMap<String, Double> values = attribute.get(nameAttribute);
		values.put(nameValue, prob);
	}

	public String[] getAttributes() {
		Set<String> list = attribute.keySet();
		Object[] obj = list.toArray();
		return Arrays.copyOf(obj, obj.length, String[].class);
	}
	
	public Double[] getProbAttributes() {
		Object[] obj = probAttribute.values().toArray();
		return Arrays.copyOf(obj, obj.length, Double[].class);
	}

	public String[] getValues(String nameAttribute) {
		HashMap<String, Double> values = attribute.get(nameAttribute);
		Set<String> list = values.keySet();
		Object[] obj = list.toArray();
		return Arrays.copyOf(obj, obj.length, String[].class);
	}
	
	public Double getProbAttribute(String nameAttribute) {
		return probAttribute.get(nameAttribute);
	}

	public Double[] getProbValues(String nameAttribute) {
		HashMap<String, Double> values = attribute.get(nameAttribute);
		Object[] obj = values.values().toArray();
		return Arrays.copyOf(obj, obj.length, Double[].class);
	}
	
	public Double getProbValue(String nameAttribute, String nameValue) {
		HashMap<String, Double> values = attribute.get(nameAttribute);
		return values.get(nameValue);
	}
	
	public String getType(String nameAttribute) {
		return type.get(nameAttribute);
	}
	
	public String toString() {
		String s = "";
		String[] at = getAttributes();
		for (String i : at) {
			s += i + ": ";
			String[] values = getValues(i);
			for (String v : values) {
				s += v + ", ";
			}
			s += "\n";
		}
		return s;
	}
}
