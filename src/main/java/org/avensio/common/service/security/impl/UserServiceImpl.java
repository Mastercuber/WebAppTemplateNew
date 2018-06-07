package org.avensio.common.service.security.impl;

import javax.transaction.Transactional;

import org.avensio.common.persistence.model.security.PasswordResetToken;
import org.avensio.common.persistence.model.security.User;
import org.avensio.common.persistence.model.security.VerificationToken;
import org.avensio.common.persistence.dao.jpa.security.IPasswordResetTokenJpaDao;
import org.avensio.common.persistence.dao.jpa.security.IUserJpaDao;
import org.avensio.common.persistence.dao.jpa.security.IVerificationTokenJpaDao;
import org.avensio.common.service.security.IUserService;
import org.avensio.common.validation.EmailExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
class UserServiceImpl implements IUserService {

    @Autowired
    private IUserJpaDao userJpaDao;

    @Autowired
    private IVerificationTokenJpaDao verificationTokenJpaDao;

    @Autowired
    private IPasswordResetTokenJpaDao passwordTokenRepository;

    @Override
    public User registerNewUser(final User user) throws EmailExistsException {
        if (emailExist(user.getEmail())) {
            throw new EmailExistsException("There is an account with that email address: " + user.getEmail());
        }
        return userJpaDao.save(user);
    }

    @Override
    public User findUserByEmail(final String email) {
        return userJpaDao.findByEmail(email);
    }

    @Override
    public void createPasswordResetTokenForUser(final User user, final String token) {
        final PasswordResetToken myToken = new PasswordResetToken(token, user);
        passwordTokenRepository.save(myToken);
    }

    @Override
    public PasswordResetToken getPasswordResetToken(final String token) {
        return passwordTokenRepository.findByToken(token);
    }

    @Override
    public void changeUserPassword(final User user, final String password) {
        user.setPassword(password);
        userJpaDao.save(user);
    }

    @Override
    public void createVerificationTokenForUser(final User user, final String token) {
        final VerificationToken myToken = new VerificationToken(token, user);
        verificationTokenJpaDao.save(myToken);
    }

    @Override
    public VerificationToken getVerificationToken(final String token) {
        return verificationTokenJpaDao.findByToken(token);
    }

    @Override
    public void saveRegisteredUser(final User user) {
        userJpaDao.save(user);
    }

    private boolean emailExist(final String email) {
        final User user = userJpaDao.findByEmail(email);
        return user != null;
    }

}
