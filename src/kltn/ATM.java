/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kltn;

/**
 *
 * @author Vu
 */
public class ATM {
    private String bank;
    private String address;
    private String numOfMachine;
    private String openTime;
    private String province_city;
    private String district;
    private String street;
    public ATM() {
        this.bank = "";
        this.address = "";
        this.numOfMachine = "";
        this.openTime = "";
        this.province_city = "";
        this.district = "";
        this.street = "";
    }

    public ATM(String bank, String address, String numOfMachine, String openTime, String province_city, String district, String street) {
        this.bank = bank;
        this.address = address;
        this.numOfMachine = numOfMachine;
        this.openTime = openTime;
        this.province_city = province_city;
        this.district = district;
        this.street = street;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNumOfMachine() {
        return numOfMachine;
    }

    public void setNumOfMachine(String numOfMachine) {
        this.numOfMachine = numOfMachine;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getProvince_city() {
        return province_city;
    }

    public void setProvince_city(String province_city) {
        this.province_city = province_city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    
    public void print(){
        System.out.println("Bank: "+bank);
        System.out.println("Address: "+address);
        System.out.println("Time Open: "+openTime);
        System.out.println("Num Machine: "+numOfMachine);
        System.out.println("Province: "+province_city);
        System.out.println("District: "+district);
        System.out.println("Street: "+street);
        System.out.println("-------------------------------------");
    }
    
}
