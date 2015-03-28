/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kltn.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Vu
 */
@Entity
@Table(name = "\"KLTN\".atm_location")
@NamedQueries({
    @NamedQuery(name = "AtmLocation.findAll", query = "SELECT a FROM AtmLocation a")})
public class AtmLocation implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "fulladdress")
    private String fulladdress;
    @Column(name = "bank")
    private String bank;
    @Column(name = "opentime")
    private String opentime;
    @Column(name = "province_city")
    private String provinceCity;
    @Column(name = "district")
    private String district;
    @Column(name = "precinct")
    private String precinct;
    @Column(name = "street")
    private String street;
    @Column(name = "phone")
    private String phone;
    @Column(name = "latd")
    private String latD;
    @Column(name = "longd")
    private String longD;
    @Column(name = "nummachine")
    private Integer nummachine;
    @Column(name = "standardlization")
    private Character standardlization;
    @Column(name = "uniquecode")
    private String uniquecode;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    public AtmLocation() {
    }

    public AtmLocation(Integer id) {
        this.id = id;
    }

    public String getFulladdress() {
        return fulladdress;
    }

    public void setFulladdress(String fulladdress) {
        this.fulladdress = fulladdress;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getOpentime() {
        return opentime;
    }

    public void setOpentime(String opentime) {
        this.opentime = opentime;
    }

    public String getProvinceCity() {
        return provinceCity;
    }

    public void setProvinceCity(String provinceCity) {
        this.provinceCity = provinceCity;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getPrecinct() {
        return precinct;
    }

    public void setPrecinct(String precinct) {
        this.precinct = precinct;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLatD() {
        return latD;
    }

    public void setLatD(String latD) {
        this.latD = latD;
    }

    public String getLongD() {
        return longD;
    }

    public void setLongD(String longD) {
        this.longD = longD;
    }

    public Integer getNummachine() {
        return nummachine;
    }

    public void setNummachine(Integer nummachine) {
        this.nummachine = nummachine;
    }

    public Character getStandardlization() {
        return standardlization;
    }

    public void setStandardlization(Character standardlization) {
        this.standardlization = standardlization;
    }

    public String getUniquecode() {
        return uniquecode;
    }

    public void setUniquecode(String uniquecode) {
        this.uniquecode = uniquecode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public void print(){
        System.out.println("Bank: "+bank);
        System.out.println("Address: "+fulladdress);
        System.out.println("Time Open: "+opentime);
        System.out.println("Num Machine: "+nummachine);
        System.out.println("Province: "+provinceCity);
        System.out.println("District: "+district);
        System.out.println("Precinct: "+precinct);
        System.out.println("Street: "+street);
        System.out.println("Phone: "+phone);
        System.out.println("Unique ATM Code: "+uniquecode);
        System.out.println("-------------------------------------");
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AtmLocation)) {
            return false;
        }
        AtmLocation other = (AtmLocation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "kltn.entity.AtmLocation[ id=" + id + " ]";
    }
    
}
