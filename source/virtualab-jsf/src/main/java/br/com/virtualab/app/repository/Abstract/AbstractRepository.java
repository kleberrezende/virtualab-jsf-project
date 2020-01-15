package br.com.virtualab.app.repository.Abstract;

import javax.persistence.EntityManager;

public class AbstractRepository extends VirtualabAbstractRepository {

    private static final long serialVersionUID = 1L;

    protected EntityManager getEntityManager() {
        return getEntityManagerDefault();
    }

}
