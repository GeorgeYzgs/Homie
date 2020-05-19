package com.spring.group.dto.property.specifications;

import com.spring.group.models.Address_;
import com.spring.group.models.property.Category;
import com.spring.group.models.property.HeatingType;
import com.spring.group.models.property.Property;
import com.spring.group.models.property.Property_;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class PropertySpecificationsCustom {
    public static Specification<Property> getPropertiesByCity(String city) {
        return new Specification<Property>() {
            @Override
            public Predicate toPredicate(Root<Property> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                return criteriaBuilder.like(root.get(Property_.address).get(Address_.city), "%" + city + "%");
            }
        };
    }


    public static Specification<Property> getPropertiesByCategory(String category) {
        return new Specification<Property>() {
            @Override
            public Predicate toPredicate(Root<Property> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get(Property_.category), Category.valueOf(category));
            }
        };
    }

    public static Specification<Property> getPropertiesByHeating(String heating) {
        return new Specification<Property>() {
            @Override
            public Predicate toPredicate(Root<Property> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get(Property_.heatingType), HeatingType.valueOf(heating));
            }
        };
    }

    public static Specification<Property> getPropertiesByPriceGreaterThanOrEqualTo(Integer startingPrice) {
        return new Specification<Property>() {
            @Override
            public Predicate toPredicate(Root<Property> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get(Property_.price), startingPrice);
            }
        };
    }

    public static Specification<Property> getPropertiesByPriceLessThanOrEqualTo(Integer ceilingPrice) {
        return new Specification<Property>() {
            @Override
            public Predicate toPredicate(Root<Property> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.lessThanOrEqualTo(root.get(Property_.price), ceilingPrice);
            }
        };
    }

    public static Specification<Property> getPropertiesByAreaGreaterThanOrEqualTo(Integer startingArea) {
        return new Specification<Property>() {
            @Override
            public Predicate toPredicate(Root<Property> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get(Property_.area), startingArea);
            }
        };
    }

    public static Specification<Property> getPropertiesByAreaLessThanOrEqualTo(Integer ceilingArea) {
        return new Specification<Property>() {
            @Override
            public Predicate toPredicate(Root<Property> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.lessThanOrEqualTo(root.get(Property_.area), ceilingArea);
            }
        };
    }

    public static Specification<Property> getPropertiesByRoomsGreaterThanOrEqualTo(Integer startingRooms) {
        return new Specification<Property>() {
            @Override
            public Predicate toPredicate(Root<Property> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get(Property_.numberOfRooms), startingRooms);
            }
        };
    }

    public static Specification<Property> getPropertiesByRoomsLessThanOrEqualTo(Integer ceilingRooms) {
        return new Specification<Property>() {
            @Override
            public Predicate toPredicate(Root<Property> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.lessThanOrEqualTo(root.get(Property_.numberOfRooms), ceilingRooms);
            }
        };
    }

    public static Specification<Property> getPropertiesByFloorGreaterThanOrEqualTo(Integer startingFloor) {
        return new Specification<Property>() {
            @Override
            public Predicate toPredicate(Root<Property> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get(Property_.floor), startingFloor);
            }
        };
    }

    public static Specification<Property> getPropertiesByFloorLessThanOrEqualTo(Integer ceilingFloor) {
        return new Specification<Property>() {
            @Override
            public Predicate toPredicate(Root<Property> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.lessThanOrEqualTo(root.get(Property_.floor), ceilingFloor);
            }
        };
    }
}
