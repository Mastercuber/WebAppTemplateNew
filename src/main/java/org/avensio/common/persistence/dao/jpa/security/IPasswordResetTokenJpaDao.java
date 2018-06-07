package org.avensio.common.persistence.dao.jpa.security;

import org.avensio.common.persistence.model.security.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPasswordResetTokenJpaDao extends JpaRepository<PasswordResetToken, Long> {

    PasswordResetToken findByToken(String token);

}
