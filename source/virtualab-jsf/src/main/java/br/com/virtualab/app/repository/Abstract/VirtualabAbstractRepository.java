package br.com.virtualab.app.repository.Abstract;

import br.com.virtualab.app.entity.Abstract.AbstractEntity;
import br.com.virtualab.app.entity.usuario.Usuario;
import br.com.virtualab.exception.RepositoryException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class VirtualabAbstractRepository implements Serializable {

    private static final long serialVersionUID = 1L;
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected static final long LIST_MAX = 50;

    @PersistenceContext
    private EntityManager entityManager;

    protected Session createSession() {
        return (Session) getEntityManagerDefault().getDelegate();
    }

    @Transactional(propagation = Propagation.MANDATORY)
    protected EntityManager getEntityManagerDefault() {
        return entityManager;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    protected EntityManager getEntityManagerConfig(String username, String password, String persistenceUnitName, String url, String nomeSchema) {
        return configPersistence(username, password, persistenceUnitName, url, nomeSchema);
    }

    private EntityManager configPersistence(String username, String password, String persistenceUnitName, String url, String nomeSchema) {
        Map properties = new HashMap();
        //datasource.xml
        properties.put("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
        properties.put("hibernate.connection.url", "jdbc:mysql://" + url + "/" + nomeSchema);
        properties.put("hibernate.connection.username", username);
        properties.put("hibernate.connection.password", password);
        //persistence.xml
        properties.put("hibernate.cache.provider_class", "org.hibernate.cache.NoCacheProvider");
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");
        properties.put("hibernate.jdbc.batch_size", "50");
        properties.put("hibernate.ejb.event.post-insert", "org.hibernate.ejb.event.EJB3PostInsertEventListener,org.hibernate.envers.event.AuditEventListener");
        properties.put("hibernate.ejb.event.post-update", "org.hibernate.ejb.event.EJB3PostUpdateEventListener,org.hibernate.envers.event.AuditEventListener");
        properties.put("hibernate.ejb.event.post-delete", "org.hibernate.ejb.event.EJB3PostDeleteEventListener,org.hibernate.envers.event.AuditEventListener");
        properties.put("hibernate.ejb.event.pre-collection-update", "org.hibernate.envers.event.AuditEventListener");
        properties.put("hibernate.ejb.event.pre-collection-remove", "org.hibernate.envers.event.AuditEventListener");
        properties.put("hibernate.ejb.event.post-collection-recreate", "org.hibernate.envers.event.AuditEventListener");
        //business-context.xml
        properties.put("hibernate.format_sql", "false");
        properties.put("hibernate.generate_statistics", "false");
        properties.put("hibernate.hbm2ddl.auto", "none");
        properties.put("hibernate.show_sql", "false");

        EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceUnitName, properties);
        EntityManager entityManagerConfig = (EntityManager) emf.createEntityManager();
        return entityManagerConfig;
    }

    protected void setAlteracao(AbstractEntity entidade) {
        entidade.setIdUsuarioAlt(((Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());
        entidade.setDataAlteracao(new Date());
    }

    protected void tratarErro(Exception e) {
        throw new RepositoryException(e.getMessage());
    }

}
