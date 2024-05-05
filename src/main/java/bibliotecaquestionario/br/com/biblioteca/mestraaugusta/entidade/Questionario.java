package bibliotecaquestionario.br.com.biblioteca.mestraaugusta.entidade;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "questionario")
public class Questionario implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "datahrCadastro")
	private Date dataHrCadastro;
	
	@OneToOne
	@JoinColumn(name = "pessoa_id", nullable = false)
	private Aluno aluno;
	
	@Column(name = "habitoLeitura")
	private Integer habitoLeitura;
	
	@Column(name = "frequencia")
	private Integer frequencia;
	
	@Column(name = "incentivoPais")
	private Integer incentivoPais;
	
	@Column(name = "genero")
	private Integer genero;
	
	@Column(name = "pontoPositivo")
	private Integer pontoPositivo;
	
	@Column(name = "pontoNegativo")
	private Integer pontoNegativo;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDataHrCadastro() {
		return dataHrCadastro;
	}

	public void setDataHrCadastro(Date dataHrCadastro) {
		this.dataHrCadastro = dataHrCadastro;
	}

	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	public Integer getHabitoLeitura() {
		return habitoLeitura;
	}

	public void setHabitoLeitura(Integer habitoLeitura) {
		this.habitoLeitura = habitoLeitura;
	}

	public Integer getFrequencia() {
		return frequencia;
	}

	public void setFrequencia(Integer frequencia) {
		this.frequencia = frequencia;
	}

	public Integer getIncentivoPais() {
		return incentivoPais;
	}

	public void setIncentivoPais(Integer incentivoPais) {
		this.incentivoPais = incentivoPais;
	}

	public Integer getGenero() {
		return genero;
	}

	public void setGenero(Integer genero) {
		this.genero = genero;
	}

	public Integer getPontoPositivo() {
		return pontoPositivo;
	}

	public void setPontoPositivo(Integer pontoPositivo) {
		this.pontoPositivo = pontoPositivo;
	}

	public Integer getPontoNegativo() {
		return pontoNegativo;
	}

	public void setPontoNegativo(Integer pontoNegativo) {
		this.pontoNegativo = pontoNegativo;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Questionario other = (Questionario) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Questionario [id=" + id + ", dataHrCadastro=" + dataHrCadastro + ", aluno=" + aluno + ", habitoLeitura="
				+ habitoLeitura + ", frequencia=" + frequencia + ", incentivoPais=" + incentivoPais + ", genero="
				+ genero + ", pontoPositivo=" + pontoPositivo + ", pontoNegativo=" + pontoNegativo + "]";
	}
	
}
