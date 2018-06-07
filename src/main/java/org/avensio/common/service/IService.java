package org.avensio.common.service;

import org.avensio.common.persistence.IByNameApi;
import org.avensio.common.persistence.model.INameableEntity;

public interface IService<T extends INameableEntity> extends IRawService<T>, IByNameApi<T> {

    //

}
