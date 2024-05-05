package bibliotecaquestionario.br.com.biblioteca.mestraaugusta.repository;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import bibliotecaquestionario.br.com.biblioteca.mestraaugusta.entidade.Aluno;
import bibliotecaquestionario.br.com.biblioteca.mestraaugusta.entidade.Questionario;

@Stateless
public class RepositoryQuestionario {

	@PersistenceContext
	private EntityManager em;
	
	public Questionario store(Questionario questionario) {
		em.persist(questionario);
		return questionario;
	}
	
	public Questionario update(Questionario questionario) {
		em.merge(questionario);
		return questionario;
	}
	
	public Questionario findById(Integer id) {
		return em.find(Questionario.class, id);
	}
	
	public void delete(Integer id) {
		Questionario Questionario = findById(id);
		
		if (Questionario != null) {
			em.remove(Questionario);
		}
	}
	
	public Questionario findByAluno(Aluno aluno) {
		try {
			return em.createQuery("SELECT q FROM Questionario q WHERE q.aluno = :aluno", Questionario.class)
					.setParameter("aluno", aluno)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public boolean vereficarSeJaPreenchido(String nome, Integer escola) {
		try {
			return em.createQuery("SELECT q FROM Questionario q WHERE q.aluno.nome = :nome AND q.aluno.escola = :escola", Questionario.class)
					.setParameter("nome", nome)
					.setParameter("escola", escola)
					.getSingleResult() != null;
		} catch (NoResultException e) {
			return false;
		}
	}
	
	public List<Long> countByEscolaGroupTurma(Integer escola, String filtro) {
		try {
			return em.createQuery(
					"SELECT COUNT(q.id) FROM Questionario q WHERE q.aluno.escola = :escola "+ filtro +" GROUP BY q.aluno.turma ORDER BY q.aluno.turma", Long.class)
					.setParameter("escola", escola)
					.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public List<Long> countTotalGroupTurma(String filtro) {
		try {
			return em.createQuery(
					"SELECT COUNT(q.id) FROM Questionario q WHERE 1=1 "+ filtro +" GROUP BY q.aluno.turma ORDER BY q.aluno.turma", Long.class)
					.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}
}
