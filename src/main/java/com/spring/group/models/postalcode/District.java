package com.spring.group.models.postalcode;

import javax.persistence.*;

@Entity
@Table(name = "greek_districts", schema = "groupproject")
public class District {
    private int districtId;
    private String districtName;

    @Id
    @Column(name = "DISTRICT_ID")
    public int getDistrictId() {
        return districtId;
    }

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
    }

    @Basic
    @Column(name = "DISTRICT_NAME")
    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        District district = (District) o;

        if (districtId != district.districtId) return false;
        if (districtName != null ? !districtName.equals(district.districtName) : district.districtName != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = districtId;
        result = 31 * result + (districtName != null ? districtName.hashCode() : 0);
        return result;
    }
}
