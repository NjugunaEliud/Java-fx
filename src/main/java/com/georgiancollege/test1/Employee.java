package com.georgiancollege.test1;

import java.util.regex.Pattern;

public class Employee {
    private int employeeId;
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String province;
    private String phoneNumber;

    public Employee(int employeeId, String firstName, String lastName, String address, String city, String province, String phoneNumber, String phone) {
        // Validation rules
        if (employeeId <= 200465000) {
            throw new IllegalArgumentException("Employee ID must be greater than 200465000");
        }
        if (firstName.length() < 2 || lastName.length() < 2) {
            throw new IllegalArgumentException("First name and last name must have at least 2 characters");
        }
        if (address.length() < 5) {
            throw new IllegalArgumentException("Address must have at least 5 characters");
        }
        if (!isValidProvince(province)) {
            throw new IllegalArgumentException("Invalid province");
        }
        if (!isValidPhoneNumber(phoneNumber)) {
            throw new IllegalArgumentException("Invalid phone number");
        }

        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.city = city;
        this.province = province;
        this.phoneNumber = phoneNumber;
    }

    // Getters and setters


    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    private boolean isValidProvince(String province) {
        String[] validProvinces = {"AB", "BC", "MB", "NB", "NL", "NS", "NT", "NU", "ON", "PE", "QC", "SK", "YT"};
        for (String validProvince : validProvinces) {
            if (validProvince.equals(province)) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidPhoneNumber(String phoneNumber) {

        return Pattern.matches("^\\d{10}$", phoneNumber);
    }
}
