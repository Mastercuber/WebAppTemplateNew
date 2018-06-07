package org.avensio.common.service;

import org.avensio.common.persistence.model.INameableEntity;

public abstract class AbstractService<T extends INameableEntity> extends AbstractRawService<T> implements IService<T> {

    public AbstractService() {
        super();
    }

    // API

}
