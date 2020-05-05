package com.spring.group.models.postalcode;

import javax.persistence.*;

@Entity
@Table(name = "greek_provinces", schema = "groupproject")
public class Province {
    private int provinceId;
    private String provinceName;

    @Id
    @Column(name = "PROVINCE_ID")
    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    @Basic
    @Column(name = "PROVINCE_NAME")
    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Province province = (Province) o;

        if (provinceId != province.provinceId) return false;
        if (provinceName != null ? !provinceName.equals(province.provinceName) : province.provinceName != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = provinceId;
        result = 31 * result + (provinceName != null ? provinceName.hashCode() : 0);
        return result;
    }
}
