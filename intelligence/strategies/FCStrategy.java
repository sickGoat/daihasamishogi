package intelligence.strategies;

public class FCStrategy implements Strategy {

	private int [] weight = { 5 , 20 };
		
	@Override
	public int getWeight(StrategyWeightType strategyType) {
		return weight[strategyType.ordinal()];
	}

	
	@Override
	public String toString() {
		return "FCStrategy";
	}
}
