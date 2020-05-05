package com.spring.group.models.postalcode;

import javax.persistence.*;

@Entity
@Table(name = "greek_postalcodes", schema = "groupproject")
public class PostalCode {
    private int postalcodeId;
    private String postalcode;
    private City greekCitiesByCityId;

    @Id
    @Column(name = "POSTALCODE_ID")
    public int getPostalcodeId() {
        return postalcodeId;
    }

    public void setPostalcodeId(int postalcodeId) {
        this.postalcodeId = postalcodeId;
    }

    @Basic
    @Column(name = "POSTALCODE")
    public String getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PostalCode that = (PostalCode) o;

        if (postalcodeId != that.postalcodeId) return false;
        if (postalcode != null ? !postalcode.equals(that.postalcode) : that.postalcode != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = postalcodeId;
        result = 31 * result + (postalcode != null ? postalcode.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "CITY_ID", referencedColumnName = "CITY_ID")
    public City getGreekCitiesByCityId() {
        return greekCitiesByCityId;
    }

    public void setGreekCitiesByCityId(City greekCitiesByCityId) {
        this.greekCitiesByCityId = greekCitiesByCityId;
    }
}
