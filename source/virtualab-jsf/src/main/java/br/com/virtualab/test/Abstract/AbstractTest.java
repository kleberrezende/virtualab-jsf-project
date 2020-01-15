package br.com.virtualab.test.Abstract;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

@ContextConfiguration(locations = {
	    "classpath*:META-INF/test-datasource.xml",
	    "classpath*:META-INF/test-security-context.xml",
	    "classpath*:META-INF/*-business-context.xml"
	})
public abstract class AbstractTest extends AbstractTransactionalJUnit4SpringContextTests {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @PersistenceContext
    protected EntityManager entityManager;

    @SuppressWarnings("unchecked")
	protected <T> List<T> find(Class<T> klazz) {
        return entityManager.createQuery(String.format("from %s order by id", klazz.getCanonicalName())).getResultList();
    }

}
