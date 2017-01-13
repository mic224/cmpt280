

enum Grain {
	WHEAT, BARLEY, OATS, RYE, OTHER
}


public class Sack {
	protected Grain type;
	protected double weight;
	
	
	
	/**
	 * @param type - the type of grain
	 * @param weight - how much grain in pounds
	 */
	public Sack(Grain type, double weight) {
		super();
		this.type = type;
		this.weight = weight;
	}
	
	
	/**
	 * @return the type of grain
	 */
	public Grain getType() {
		return type;
	}
	/**
	 * @param type the type of grain for this plunder
	 */
	public void setType(Grain type) {
		this.type = type;
	}
	/**
	 * @return the weight of grain for this plunder
	 */
	public double getWeight() {
		return weight;
	}
	/**
	 * @param weight the weight of grain for this plunder
	 */
	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	public String toString() {		
		return "("+ type.toString() + ", " + weight + ")";
	}
	
}
