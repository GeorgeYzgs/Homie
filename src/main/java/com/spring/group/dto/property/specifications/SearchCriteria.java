package com.spring.group.dto.property.specifications;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.metamodel.SingularAttribute;
import java.util.function.Predicate;

public class SearchCriteria {
    private SingularAttribute key;
    private String operation;
    private Object value;
    private boolean isPredicate;
    private Predicate predicate;
    private boolean isSpecification;
    private Specification specification;

    public SearchCriteria(SingularAttribute key, String operation, Object value) {
        this.key = key;
        this.operation = operation;
        this.value = value;
        this.isPredicate = false;
    }

    public SearchCriteria(Predicate predicate) {
        this.predicate = predicate;
        this.isPredicate = true;
    }

    public SearchCriteria(Specification specification) {
        this.specification = specification;
        this.isSpecification = true;
    }

    public SingularAttribute getKey() {
        return key;
    }

    public void setKey(SingularAttribute key) {
        this.key = key;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public boolean isPredicate() {
        return isPredicate;
    }

    public Predicate getPredicate() {
        return predicate;
    }

    public void setPredicate(Predicate predicate) {
        this.predicate = predicate;
    }

    public boolean isSpecification() {
        return isSpecification;
    }

    public Specification getSpecification() {
        return specification;
    }

    public void setSpecification(Specification specification) {
        this.specification = specification;
    }
}
