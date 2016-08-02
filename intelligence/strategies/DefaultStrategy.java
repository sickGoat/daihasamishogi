package intelligence.strategies;

public class DefaultStrategy implements Strategy {
	
	//catches valutate pari a 1
	//fc valutate pari a 1
	private int [] weight = { 6 , 2 };
	
	@Override
	public int getWeight(StrategyWeightType strategyType) {
		return weight[strategyType.ordinal()];
	}
	
	@Override
	public String toString() {
		return "Default";
	}
}
