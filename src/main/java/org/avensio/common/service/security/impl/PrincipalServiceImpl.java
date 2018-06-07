package org.avensio.common.service.security.impl;

import org.avensio.common.persistence.dao.jpa.security.IPrincipalJpaDao;
import org.avensio.common.persistence.model.security.Principal;
import org.avensio.common.service.AbstractService;
import org.avensio.common.service.security.IPrincipalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PrincipalServiceImpl extends AbstractService<Principal> implements IPrincipalService {

    @Autowired
    private IPrincipalJpaDao dao;

    public PrincipalServiceImpl() {
        super();
    }

    // API

    // find

    @Override
    @Transactional(readOnly = true)
    public Principal findByName(final String name) {
        return dao.findByName(name);
    }


    // other

    // Spring

    @Override
    protected final IPrincipalJpaDao getDao() {
        return dao;
    }

    @Override
    protected JpaSpecificationExecutor<Principal> getSpecificationExecutor() {
        return dao;
    }

}
