package br.com.sedor.odonto;


public class Lesion implements Comparable<Lesion> {
	
	private Attributes attributes;
	private String name;
	private Double prob;
	
	public Lesion(Attributes attributes, String name, Double prob) {
		this.attributes = attributes;
		this.name = name;
		this.prob = prob;
	}
	
	public Lesion() {
		this(null, null, null);
	}

	public Attributes getAttributes() {
		return attributes;
	}

	public void setAttributes(Attributes attributes) {
		this.attributes = attributes;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getProb() {
		return prob;
	}

	public void setProb(Double prob) {
		this.prob = prob;
	}
	
	@Override
	public int compareTo(Lesion o) {
		if (prob > o.getProb())
			return -1;
		else if (prob == o.getProb())
			return 0;
		else
			return 1;
	}
	
	
}
