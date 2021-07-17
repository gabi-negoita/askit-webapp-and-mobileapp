package com.project.askit.specification;

import com.project.askit.entity.Notification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class NotificationSpecification implements org.springframework.data.jpa.domain.Specification<Notification> {

    private final SearchCriteria searchCriteria;

    public NotificationSpecification(SearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    @Override
    public Predicate toPredicate(Root<Notification> root,
                                 CriteriaQuery<?> query,
                                 CriteriaBuilder criteriaBuilder) {
        if (searchCriteria.getValue().toString().isBlank()) return criteriaBuilder.isNull(root.get(searchCriteria.getKey()));
        if (searchCriteria.getOperation().equalsIgnoreCase(">")) {
            // Handle greater than operator
            return criteriaBuilder.greaterThanOrEqualTo(root.get(searchCriteria.getKey()), searchCriteria.getValue().toString());
        } else if (searchCriteria.getOperation().equalsIgnoreCase("<")) {
            // Handle less than operator
            return criteriaBuilder.lessThanOrEqualTo(root.get(searchCriteria.getKey()), searchCriteria.getValue().toString());
        } else if (searchCriteria.getOperation().equalsIgnoreCase("=")) {
            // Handle equal operator
            if (root.get(searchCriteria.getKey()).getJavaType() == String.class) {
                // For strings
                return criteriaBuilder.like(root.get(searchCriteria.getKey()), "%" + searchCriteria.getValue() + "%");
            } else {
                // For other types
                return criteriaBuilder.equal(root.get(searchCriteria.getKey()), searchCriteria.getValue());
            }
        }

        return null;
    }
}
