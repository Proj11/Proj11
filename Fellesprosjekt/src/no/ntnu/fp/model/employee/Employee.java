package no.ntnu.fp.model.employee;

public class Employee {
	
	private String name;
	private String username;
	private String password;
	
	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public Employee(String name) {
		this.username=name;
	}
	
	public Employee(String name, String username) {
		this.name = name;
		this.username = username;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
