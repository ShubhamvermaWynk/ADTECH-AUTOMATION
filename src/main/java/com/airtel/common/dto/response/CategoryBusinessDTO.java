package com.airtel.common.dto.response;

import java.util.Objects;

public class CategoryBusinessDTO {

    private String natureOfBusiness;
    private Integer id;

    public String getNatureOfBusiness() {
        return natureOfBusiness;
    }

    public void setNatureOfBusiness(String natureOfBusiness) {
        this.natureOfBusiness = natureOfBusiness;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategoryBusinessDTO)) return false;
        CategoryBusinessDTO that = (CategoryBusinessDTO) o;
        return Objects.equals(getNatureOfBusiness(), that.getNatureOfBusiness()) &&
                Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNatureOfBusiness(), getId());
    }

    @Override
    public String toString() {
        return "CategoryBusinessDTO{" +
                "natureOfBusiness='" + natureOfBusiness + '\'' +
                ", id=" + id +
                '}';
    }
}
