package com.xueqiang.footmark.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PersonForm {

	@NotNull //Does not allow a null value, which is what Spring MVC generates if the entry is empty.
	@Size(min=2, max=30) //Allows names between 2 and 30 characters long.
	private String name;

	@NotNull
	@Min(18) //Does not allow the age to be less than 18.
	private Integer age;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "PersonForm{" + "name='" + name + '\'' + ", age=" + age + '}';
	}
}
