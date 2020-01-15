package br.com.virtualab.app.service.Abstract;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.virtualab.exception.RepositoryException;
import br.com.virtualab.exception.ServiceException;

public abstract class AbstractService implements Serializable {

    private static final long serialVersionUID = 1L;
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected void tratarErro(Exception e) {
        if (e instanceof RepositoryException) {
            throw new RepositoryException(e.getMessage());
        } else if (e instanceof IllegalArgumentException) {
            throw new IllegalArgumentException(e.getMessage());
        } else {
            throw new ServiceException(e.getMessage());
        }

    }

}
