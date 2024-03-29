package ru.borshchevskiy.pcs.repository.project;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import ru.borshchevskiy.pcs.dto.project.filter.ProjectFilter;
import ru.borshchevskiy.pcs.entities.project.Project;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProjectSpecificationUtil {

    /*
    Поиск проектов. Поиск должен осуществляться по текстовому значению (по полям Наименование проекта, Код проекта)
    и с применением фильтров по Статусу проекта. Т.е. на вход передается некоторое текстовое значение и список статусов.
    */
    /*
    Условие фильтра формируется следующим образом: where (filter.value like project.code OR filter.value like project.name) AND project.status in [value.statuses]
    */

    public static Specification<Project> getSpecification(ProjectFilter filter) {
        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();
            Predicate attributeLikeSearchValue = null;
            Predicate statusEquals = null;

            // Предикат для поиска по текстовому значению. Текстовое значение like project.code ИЛИ текстовое значение like project.name
            if (!ObjectUtils.isEmpty(filter.value())) {
                String searchValue = "%" + filter.value() + "%";
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), searchValue.toLowerCase()));
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("code")), searchValue.toLowerCase()));
                attributeLikeSearchValue = criteriaBuilder.or(predicates.toArray(Predicate[]::new));
            }


            // Предикат для фильтрации статусов
            if (!CollectionUtils.isEmpty(filter.statuses())) {
                predicates = filter.statuses()
                        .stream()
                        .map(s -> criteriaBuilder.equal(root.get("status").as(String.class), s.name()))
                        .toList();
                statusEquals = criteriaBuilder.or(predicates.toArray(Predicate[]::new));
            }


            // Формирование where
            if (!ObjectUtils.isEmpty(attributeLikeSearchValue) && !ObjectUtils.isEmpty(statusEquals)) {
                return query.where(criteriaBuilder.and(statusEquals, attributeLikeSearchValue)).getRestriction();

            } else if (!ObjectUtils.isEmpty(attributeLikeSearchValue)) {
                return query.where(attributeLikeSearchValue).getRestriction();

            } else if (!ObjectUtils.isEmpty(statusEquals)) {
                return query.where(statusEquals).getRestriction();
            }

            return query.where().getRestriction();
        };
    }
}
