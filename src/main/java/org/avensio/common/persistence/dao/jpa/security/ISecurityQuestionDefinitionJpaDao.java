package org.avensio.common.persistence.dao.jpa.security;

import org.avensio.common.persistence.model.security.SecurityQuestionDefinition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISecurityQuestionDefinitionJpaDao extends JpaRepository<SecurityQuestionDefinition, Long> {

}