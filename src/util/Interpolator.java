package util;

public class Interpolator {
	private int minTargetValue, maxTargetValue, minOriginalValue,
			maxOriginalValue;

	public Interpolator(int minTargetValue, int maxTargetValue,
			int minOriginalValue, int maxOriginalValue) {
		super();
		this.minTargetValue = minTargetValue;
		this.maxTargetValue = maxTargetValue;
		this.minOriginalValue = minOriginalValue;
		this.maxOriginalValue = maxOriginalValue;
	}

	public int interpolate(int value) {
		float y = this.minTargetValue
				+ (((value - this.minOriginalValue) / (float)(this.maxOriginalValue - this.minOriginalValue)) * (this.maxTargetValue - this.minTargetValue));
		return (int) y;
	}

}
