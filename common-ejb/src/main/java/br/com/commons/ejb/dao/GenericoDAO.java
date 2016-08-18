package br.com.commons.ejb.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public abstract class GenericoDAO<T, P> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private static final String SELECT_ALL = "FROM %s o";
	private static final String INSERT = "INSERT";
	private static final String UPDATE = "UPDATE";
	private static final String DELETE = "DELETE";
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public void salvar(T entidade) throws Exception{
		executarComando(INSERT, entidade);
	}
	
	public void atualizar(T entidade) throws Exception{
		executarComando(UPDATE, entidade);
	}

	public void deletar(T entidade) throws Exception{
		executarComando(DELETE, entidade);
	}
	
	private void executarComando(String comando,T entidade) throws Exception{
		
		switch (comando) {
			case INSERT:
				getEntityManager().persist(entidade);
				break;
			case UPDATE:
				getEntityManager().merge(entidade);
				break;
			case DELETE:
				getEntityManager().remove(entidade);
				break;
				
			default:
			break;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<T> buscarTodos() throws Exception{
		String jpql=String.format(SELECT_ALL, getTipoClasse().getSimpleName());
		
		return getEntityManager().createQuery(jpql).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public T buscarPor(final P id) throws Exception{
		return (T) getEntityManager().find(getTipoClasse(), id);
	}
	
	protected Class<?> getTipoClasse() {
		return (Class<?>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	protected EntityManager getEntityManager(){
		return entityManager;
	}
}

