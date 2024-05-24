package com.br.comanda.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.br.comanda.models.Produto;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.ManagedType;
import jakarta.persistence.metamodel.PluralAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.Type;

public class ProdutoSpecification {

	public static Specification<Produto> searchAllFields(String searchTerm) {
		return (root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();

			// Obtém o tipo da entidade gerenciada
			ManagedType<?> managedType = (ManagedType<?>) root.getModel();

			// Itera sobre os atributos da entidade
			for (Attribute<?, ?> attribute : managedType.getAttributes()) {
				if (attribute.getJavaType().equals(String.class)) {
					// Adiciona condição de like para atributos do tipo String
					predicates.add(
							criteriaBuilder.like(criteriaBuilder.lower(root.get(attribute.getName()).as(String.class)),
									"%" + searchTerm.toLowerCase() + "%"));
				} else if (attribute instanceof SingularAttribute && ((SingularAttribute<?, ?>) attribute).getType()
						.getPersistenceType() == Type.PersistenceType.ENTITY) {
					// Para associações do tipo MANY_TO_ONE ou ONE_TO_ONE
					Path<?> join = root.get(attribute.getName());
					ManagedType<?> joinType = (ManagedType<?>) ((SingularAttribute<?, ?>) attribute).getType();
					for (Attribute<?, ?> nestedAttribute : joinType.getAttributes()) {
						if (nestedAttribute.getJavaType().equals(String.class)) {
							predicates.add(criteriaBuilder.like(
									criteriaBuilder.lower(join.get(nestedAttribute.getName()).as(String.class)),
									"%" + searchTerm.toLowerCase() + "%"));
						}
					}
				} else if (attribute instanceof PluralAttribute && ((PluralAttribute<?, ?, ?>) attribute)
						.getElementType().getPersistenceType() == Type.PersistenceType.ENTITY) {
					// Para coleções de entidades (MANY_TO_MANY ou ONE_TO_MANY)
					Join<?, ?> join = root.join(attribute.getName(), JoinType.LEFT);
					ManagedType<?> joinType = (ManagedType<?>) ((PluralAttribute<?, ?, ?>) attribute).getElementType();
					for (Attribute<?, ?> nestedAttribute : joinType.getAttributes()) {
						if (nestedAttribute.getJavaType().equals(String.class)) {
							predicates.add(criteriaBuilder.like(
									criteriaBuilder.lower(join.get(nestedAttribute.getName()).as(String.class)),
									"%" + searchTerm.toLowerCase() + "%"));
						}
					}
				}
			}

			return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
		};
	}
}
