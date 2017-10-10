package com.consol.citrus.variable;

/**
 * VariableDefinition
 *
 * @author ext_azhuravlev2
 * @date 01.08.2017
 */
public class VariableDefinition {
	
	private String name;
	
	private String value;
	
	private boolean global = false;
	
	public VariableDefinition(String name, String variableValue, boolean global) {
		this.name = name;
		this.value = variableValue;
		this.global = global;
	}
	
	public VariableDefinition(String name, String variableValue) {
		this.name = name;
		this.value = variableValue;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public boolean isGlobal() {
		return global;
	}
	
	public void setGlobal(boolean global) {
		this.global = global;
	}
}
