package com.america.domain;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Member.
 */
@Entity
@Table(name = "member")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "member")
public class Member implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 35)
    @Column(name = "first_name", length = 35)
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "gender")
    private String gender;

    @Column(name = "address")
    private String address;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "zip")
    private String zip;

    @Column(name = "county")
    private String county;

    @Column(name = "country")
    private String country;

    @Column(name = "tobacco_use_indicator")
    private Boolean tobaccoUseIndicator;

    @Column(name = "substance_abuse_indicator")
    private Boolean substanceAbuseIndicator;

    @Column(name = "last_update_datetime")
    private LocalDate lastUpdateDatetime;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Member id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Member firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Member lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return this.birthDate;
    }

    public Member birthDate(LocalDate birthDate) {
        this.setBirthDate(birthDate);
        return this;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return this.gender;
    }

    public Member gender(String gender) {
        this.setGender(gender);
        return this;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return this.address;
    }

    public Member address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return this.city;
    }

    public Member city(String city) {
        this.setCity(city);
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return this.state;
    }

    public Member state(String state) {
        this.setState(state);
        return this;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return this.zip;
    }

    public Member zip(String zip) {
        this.setZip(zip);
        return this;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCounty() {
        return this.county;
    }

    public Member county(String county) {
        this.setCounty(county);
        return this;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getCountry() {
        return this.country;
    }

    public Member country(String country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Boolean getTobaccoUseIndicator() {
        return this.tobaccoUseIndicator;
    }

    public Member tobaccoUseIndicator(Boolean tobaccoUseIndicator) {
        this.setTobaccoUseIndicator(tobaccoUseIndicator);
        return this;
    }

    public void setTobaccoUseIndicator(Boolean tobaccoUseIndicator) {
        this.tobaccoUseIndicator = tobaccoUseIndicator;
    }

    public Boolean getSubstanceAbuseIndicator() {
        return this.substanceAbuseIndicator;
    }

    public Member substanceAbuseIndicator(Boolean substanceAbuseIndicator) {
        this.setSubstanceAbuseIndicator(substanceAbuseIndicator);
        return this;
    }

    public void setSubstanceAbuseIndicator(Boolean substanceAbuseIndicator) {
        this.substanceAbuseIndicator = substanceAbuseIndicator;
    }

    public LocalDate getLastUpdateDatetime() {
        return this.lastUpdateDatetime;
    }

    public Member lastUpdateDatetime(LocalDate lastUpdateDatetime) {
        this.setLastUpdateDatetime(lastUpdateDatetime);
        return this;
    }

    public void setLastUpdateDatetime(LocalDate lastUpdateDatetime) {
        this.lastUpdateDatetime = lastUpdateDatetime;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Member)) {
            return false;
        }
        return id != null && id.equals(((Member) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Member{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", birthDate='" + getBirthDate() + "'" +
            ", gender='" + getGender() + "'" +
            ", address='" + getAddress() + "'" +
            ", city='" + getCity() + "'" +
            ", state='" + getState() + "'" +
            ", zip='" + getZip() + "'" +
            ", county='" + getCounty() + "'" +
            ", country='" + getCountry() + "'" +
            ", tobaccoUseIndicator='" + getTobaccoUseIndicator() + "'" +
            ", substanceAbuseIndicator='" + getSubstanceAbuseIndicator() + "'" +
            ", lastUpdateDatetime='" + getLastUpdateDatetime() + "'" +
            "}";
    }
}
