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
	
	public String getName() {
		return name;
	}
}
