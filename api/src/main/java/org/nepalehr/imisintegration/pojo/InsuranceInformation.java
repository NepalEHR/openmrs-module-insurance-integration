
package org.nepalehr.imisintegration.pojo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "Insurance")
@XmlType(propOrder = {"value", "name"})

public class InsuranceInformation {
	private String name;
	private String value;

	@XmlElement
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	/*
	 * @Override public String toString() { return "InsuranceInformation [name=" +
	 * name + ", value=" + value + "]"; }
	 */

}
