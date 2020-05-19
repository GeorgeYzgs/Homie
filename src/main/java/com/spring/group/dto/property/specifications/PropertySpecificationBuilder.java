package com.spring.group.dto.property.specifications;

import com.spring.group.models.property.Property;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.metamodel.SingularAttribute;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PropertySpecificationBuilder {

    private final List<SearchCriteria> params;
    List<Specification> specs;

    public PropertySpecificationBuilder() {
        params = new ArrayList<SearchCriteria>();
        specs = new ArrayList<>();
    }

    public PropertySpecificationBuilder with(SingularAttribute key, String operation, Object value) {
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    public PropertySpecificationBuilder with(SearchCriteria searchCriteria) {
        params.add(searchCriteria);
        return this;
    }

    public PropertySpecificationBuilder with(Specification specification) {
        specs.add(specification);
        return this;
    }

    public Specification<Property> build() {
        if (params.size() == 0 && specs.size() == 0) {
            return null;
        }

        specs.addAll(params.stream()
                .map(PropertySpecification::new)
                .collect(Collectors.toList()));

        Specification<Property> result = specs.get(0);

        for (int i = 1; i < specs.size(); i++) {
            result = Specification.where(result).and(specs.get(i));
        }
        return result;
    }
}
