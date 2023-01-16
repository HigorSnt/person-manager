package com.attornatus.desafio.entity.address;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "ADDRESS")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "STREET_NAME")
    private String street;

    @Column(name = "ZIP_CODE")
    private String zipCode;

    @Column(name = "NUMBER")
    private Long number;

    @Column(name = "CITY")
    private String city;

    @Column(name = "MAIN_ADDRESS")
    private Boolean mainAddress;

    public Address() {
    }

    public Address(String street, String zipCode, Long number, String city) {
        this.street = street;
        this.zipCode = zipCode;
        this.number = number;
        this.city = city;
        mainAddress = false;
    }

    public Long getId() {
        return id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Boolean getMainAddress() {
        return mainAddress;
    }

    public void setMainAddress(Boolean mainAddress) {
        this.mainAddress = mainAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Address address = (Address) o;
        return Objects.equals(id, address.id) && Objects.equals(street, address.street) && Objects.equals(zipCode, address.zipCode) && Objects.equals(number, address.number) && Objects.equals(city, address.city) && Objects.equals(mainAddress, address.mainAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, street, zipCode, number, city, mainAddress);
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", street='" + street + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", number=" + number +
                ", city='" + city + '\'' +
                ", mainAddress=" + mainAddress +
                '}';
    }
}
