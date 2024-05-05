package bibliotecaquestionario.br.com.biblioteca.mestraaugusta.dao;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import bibliotecaquestionario.br.com.biblioteca.mestraaugusta.entidade.Aluno;
import bibliotecaquestionario.br.com.biblioteca.mestraaugusta.entidade.Questionario;
import bibliotecaquestionario.br.com.biblioteca.mestraaugusta.repository.RepositoryQuestionario;
import bibliotecaquestionario.br.com.biblioteca.mestraaugusta.util.VerificarUtil;

@Stateless
public class DaoQuestionario {

	@EJB
	private RepositoryQuestionario repositoryQuestionario;
	
	public Questionario createOrUpdate(Questionario questionario) {
		if (VerificarUtil.verificarSeNulo(questionario.getId())) {
			return create(questionario);
		} else {
			return update(questionario);
		}
	}
	
	private Questionario create(Questionario questionario) {
		VerificarUtil.verificarSeNulo(questionario);
		return repositoryQuestionario.store(questionario);
	}
	
	public void delete(Integer id) {
		repositoryQuestionario.delete(id);
	}
	
	private Questionario update(Questionario questionario) {
		VerificarUtil.verificarSeNulo(questionario);
		VerificarUtil.verificarSeNulo(questionario.getId());
		return repositoryQuestionario.update(questionario);
	}
	
	public Questionario findById(Integer id) {
		return repositoryQuestionario.findById(id);
	}
	
	public Questionario findByAluno(Aluno aluno){
		return repositoryQuestionario.findByAluno(aluno);
	}
	
	public boolean vereficarSeJaPreenchido(String nome, Integer escola){
		return repositoryQuestionario.vereficarSeJaPreenchido(nome, escola);
	}
	
	public List<Long> countByEscolaGroupTurma(Integer escola, String filtro){
		return repositoryQuestionario.countByEscolaGroupTurma(escola, filtro);
	}
	
	public List<Long> countTotalGroupTurma(String filtro){
		return repositoryQuestionario.countTotalGroupTurma(filtro);
	}
	
}
