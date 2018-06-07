package org.avensio.common.service.security;

import org.avensio.common.persistence.model.security.PasswordResetToken;
import org.avensio.common.persistence.model.security.User;
import org.avensio.common.persistence.model.security.VerificationToken;
import org.avensio.common.validation.EmailExistsException;

public interface IUserService {

    User registerNewUser(User user) throws EmailExistsException;

    User findUserByEmail(String email);

    void createPasswordResetTokenForUser(User user, String token);

    PasswordResetToken getPasswordResetToken(String token);

    void changeUserPassword(User user, String password);

    void createVerificationTokenForUser(User user, String token);

    VerificationToken getVerificationToken(String token);

    void saveRegisteredUser(User user);

}
