package model;

import model.enumerators.ConditionOp;
import model.enumerators.RuleAction;

public class Rule {
	RuleCondition	ruleCondition;
	
	int				ruleValue;
	int				maxValue;
	RuleAction		ruleAction;
	
	public Rule(RuleCondition ruleCondition, int ruleValue, int maxValue, RuleAction ruleAction) {
		if (ruleValue < 0 || ruleValue > maxValue) {
			throw new RuntimeException("ruleValue must be within limits of 0 and maxValue!");
		}
		
		this.ruleCondition = ruleCondition;
		this.ruleValue = ruleValue;
		this.maxValue = maxValue;
		this.ruleAction = ruleAction;
	}
	
	public boolean testCondition(int neighbourVal) {
		return ruleCondition.eval(neighbourVal);
	}
	
	public int doRule(int value) {
		switch (ruleAction) {
			case SET:
				return ruleValue;
			case ADD:
				return limitValidation(value + ruleValue);
			case SUBTRACT:
				return limitValidation(value - ruleValue);
			case MULTIPLY:
				return limitValidation(value * ruleValue);
			case DIVIDE:
				return limitValidation(value / ruleValue);
			default:
				throw new RuntimeException("unknown RuleAction");
		}
	}
	
	private int limitValidation(int value) {
		if (value < 0) {
			return 0;
		}
		if (value > maxValue) {
			return maxValue;
		}
		return value;
	}
	
	public static class RuleCondition {
		ConditionOp	operator;
		int			threshold;
		
		public RuleCondition(ConditionOp operator, int threshold) {
			this.operator = operator;
			this.threshold = threshold;
		}
		
		public boolean eval(int val) {
			switch (operator) {
				case EQUALS:
					return val == threshold;
				case UNEQUALS:
					return val != threshold;
				case SMALLER_THAN:
					return val < threshold;
				case LARGER_THAN:
					return val > threshold;
				default:
					return false;
			}
		}
	}
}
