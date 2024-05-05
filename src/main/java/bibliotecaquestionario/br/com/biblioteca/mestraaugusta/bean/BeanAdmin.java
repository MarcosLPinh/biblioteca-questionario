package bibliotecaquestionario.br.com.biblioteca.mestraaugusta.bean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearTicks;
import org.primefaces.model.charts.bar.BarChartDataSet;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.bar.BarChartOptions;
import org.primefaces.model.charts.optionconfig.legend.Legend;
import org.primefaces.model.charts.optionconfig.legend.LegendLabel;
import org.primefaces.model.charts.optionconfig.title.Title;

import bibliotecaquestionario.br.com.biblioteca.mestraaugusta.dao.DaoAluno;
import bibliotecaquestionario.br.com.biblioteca.mestraaugusta.dao.DaoQuestionario;
import bibliotecaquestionario.br.com.biblioteca.mestraaugusta.entidade.Aluno;
import bibliotecaquestionario.br.com.biblioteca.mestraaugusta.entidade.Aluno.Sexo;
import bibliotecaquestionario.br.com.biblioteca.mestraaugusta.entidade.Questionario;
import bibliotecaquestionario.br.com.biblioteca.mestraaugusta.util.map.MapFrequencia;
import bibliotecaquestionario.br.com.biblioteca.mestraaugusta.util.map.MapGenero;
import bibliotecaquestionario.br.com.biblioteca.mestraaugusta.util.map.MapHabitoLeitura;
import bibliotecaquestionario.br.com.biblioteca.mestraaugusta.util.map.MapIncentivoPais;
import bibliotecaquestionario.br.com.biblioteca.mestraaugusta.util.map.MapPontoNegativo;
import bibliotecaquestionario.br.com.biblioteca.mestraaugusta.util.map.MapPontoPositivo;
import bibliotecaquestionario.br.com.biblioteca.mestraaugusta.util.map.MapTurmas;

@Named()
@ViewScoped
public class BeanAdmin extends BeanMain {

	private static final long serialVersionUID = 1L;
	
	@EJB
	private DaoQuestionario daoQuestionario;
	
	@EJB
	private DaoAluno daoAluno;
	
	//Filtros
	private Map<Object, Object> listaHabitoLeitura, listaFrequencia, listaIncentivoPais, listaGenero,
	listaPontoPositivo, listaPontoNegativo;

	private Sexo sexo;
	private Integer idadeInicial, idadeFinal, habitoLeitura, frequencia, incentivoPais, genero, pontoPositivo, pontoNegativo;
	private Date dataInicial, dataFinal;
	
	//Tabs
	private BarChartModel graficoPorEscolas, graficoTotal;
	
	private List<Aluno> listaAlunos;
	
	private Questionario questionarioAluno = new Questionario();
	
	//Auxiliar
	private StringBuilder filtro = new StringBuilder("");
	
	@PostConstruct
	public void init() {
		iniciarListas();
		iniciarGraficos();
	}
	
	private void iniciarListas() {
		listaHabitoLeitura = new MapHabitoLeitura().getMap();
		listaFrequencia = new MapFrequencia().getMap();
		listaIncentivoPais = new MapIncentivoPais().getMap();
		listaGenero = new MapGenero().getMap();
		listaPontoPositivo = new MapPontoPositivo().getMap();
		listaPontoNegativo = new MapPontoNegativo().getMap();
		
		listaAlunos = daoAluno.getList("");
	}
	
	private void iniciarGraficos() {
		try {
			iniciarPorEscolas();
			iniciarTotal();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void iniciarPorEscolas() throws Exception{
		graficoPorEscolas = new BarChartModel();
        ChartData data = new ChartData();

        data.addChartDataSet(getDataSetEscola("Escola Estadual Lauro Machado", 1));
        data.addChartDataSet(getDataSetEscola("Escola Estadual Badaró Junior", 2));
        data.addChartDataSet(getDataSetEscola("Escola Municipal São João Batista", 3));
        data.setLabels(getLabels());
        
        graficoPorEscolas.setData(data);
        graficoPorEscolas.setOptions(getOptions(true));
	}
	
	private BarChartDataSet getDataSetEscola(String nomeEscola, Integer escola) throws Exception{
		BarChartDataSet barDataSet = new BarChartDataSet();
        barDataSet.setLabel(nomeEscola);
        
        List<Number> values = new ArrayList<>(daoQuestionario.countByEscolaGroupTurma(escola, filtro.toString()));
        barDataSet.setData(values);

        List<String> colors = getColors(escola);
        barDataSet.setBackgroundColor(colors);
        barDataSet.setBorderColor(colors);
        barDataSet.setBorderWidth(1);
        
        return barDataSet;
	}
	
	private List<String> getColors(Integer escola){
		List<String> listColors = new ArrayList<>();
		switch (escola) {
		case 1:
			listColors.add("rgba(255, 99, 132, 0.2)");
			break;
		case 2:
			listColors.add("rgba(255, 159, 64, 0.2)");
			break;
		case 3:
			listColors.add("rgba(54, 162, 235, 0.2)");
			break;
		default:
			listColors.add("rgba(75, 192, 192, 0.2)");
			break;
		}
        return listColors;
	}
	
	private List<String> getLabels(){
		Map<Object, Object> turmas = new MapTurmas().getMap();
        List<String> labels = new ArrayList<>();
        turmas.forEach((key, value) -> {
        	if(value != null) 
        		labels.add(key.toString());
        	});
        return labels;
	}
	
	private BarChartOptions getOptions(boolean mostrarLegend) {
        BarChartOptions options = new BarChartOptions();
        CartesianScales cScales = new CartesianScales();
        CartesianLinearAxes linearAxes = new CartesianLinearAxes();
        linearAxes.setOffset(true);
        CartesianLinearTicks ticks = new CartesianLinearTicks();
        ticks.setPrecision(0);
        linearAxes.setTicks(ticks);
        cScales.addYAxesData(linearAxes);
        options.setScales(cScales);

        Title title = new Title();
        title.setDisplay(true);
        title.setText("Quantidade de Alunos que preencheram o questionário");
        options.setTitle(title);

        Legend legend = new Legend();
        legend.setDisplay(mostrarLegend);
        legend.setPosition("top");
        LegendLabel legendLabels = new LegendLabel();
        legendLabels.setFontColor("#333");
        legendLabels.setFontSize(18);
        legend.setLabels(legendLabels);
        options.setLegend(legend);
        
        return options;
	}
	
	private void iniciarTotal() throws Exception{
		graficoTotal = new BarChartModel();
        ChartData data = new ChartData();

        data.addChartDataSet(getDataSetTotal());
        data.setLabels(getLabels());
        
        graficoTotal.setData(data);
        graficoTotal.setOptions(getOptions(false));
	}
	
	private BarChartDataSet getDataSetTotal() throws Exception{
		BarChartDataSet barDataSet = new BarChartDataSet();
        barDataSet.setLabel("Total");
        
        List<Number> values = new ArrayList<>(daoQuestionario.countTotalGroupTurma(filtro.toString()));
        barDataSet.setData(values);

        List<String> colors = getColors(0);
        barDataSet.setBackgroundColor(colors);
        barDataSet.setBorderColor(colors);
        barDataSet.setBorderWidth(1);
        
        return barDataSet;
	}
	
	public void limpar() {
		 habitoLeitura = null;
		 frequencia = null;
		 incentivoPais = null;
		 genero = null;
		 pontoPositivo = null;
		 pontoNegativo = null;
		 sexo = null;
		 idadeInicial = null;
		 idadeFinal = null;
		 dataInicial = null;
		 dataFinal = null;
		 
		 pesquisar();
	}
	
	public void pesquisar() {
		filtro = new StringBuilder("");
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		if(sexo != null) {
			filtro.append(" AND q.aluno.sexo = " + sexo.ordinal());
		}
		
		if(idadeInicial != null) {
			filtro.append(" AND q.aluno.idade >= " + idadeInicial);
		}
		
		if(idadeFinal != null) {
			filtro.append(" AND q.aluno.idade <= " + idadeFinal);
		}
		
		if(dataInicial != null) {
			filtro.append(" AND to_char(q.aluno.dataHrCadastro, 'dd/MM/yyyy') >= '" + sdf.format(dataInicial) + "'");
		}
		
		if(dataFinal != null) {
			filtro.append(" AND to_char(q.aluno.dataHrCadastro, 'dd/MM/yyyy') <= '" + sdf.format(dataFinal) + "'");
		}
		
		if(habitoLeitura != null) {
			filtro.append(" AND q.habitoLeitura = ").append(habitoLeitura);
		}
		if(frequencia != null) {
			filtro.append(" AND q.frequencia = ").append(frequencia);
		}
		if(incentivoPais != null) {
			filtro.append(" AND q.incentivoPais = ").append(incentivoPais);
		}
		if(genero != null) {
			filtro.append(" AND q.genero = ").append(genero);
		}
		if(pontoPositivo != null) {
			filtro.append(" AND q.pontoPositivo = ").append(pontoPositivo);
		}
		if(pontoNegativo != null) {
			filtro.append(" AND q.pontoPositivo = ").append(pontoNegativo);
		}
		
		iniciarGraficos();
		listaAlunos = daoAluno.getList(filtro.toString());
	}
	
	public void visualizarRespostas(Aluno aluno) {
		questionarioAluno = daoQuestionario.findByAluno(aluno);
	}
	
	public BarChartModel getGraficoPorEscolas() {
		return graficoPorEscolas;
	}
	
	public void setGraficoPorEscolas(BarChartModel graficoPorEscolas) {
		this.graficoPorEscolas = graficoPorEscolas;
	}
	
	public BarChartModel getGraficoTotal() {
		return graficoTotal;
	}
	
	public void setGraficoTotal(BarChartModel graficoTotal) {
		this.graficoTotal = graficoTotal;
	}

	public Map<Object, Object> getListaHabitoLeitura() {
		return listaHabitoLeitura;
	}

	public void setListaHabitoLeitura(Map<Object, Object> listaHabitoLeitura) {
		this.listaHabitoLeitura = listaHabitoLeitura;
	}

	public Map<Object, Object> getListaFrequencia() {
		return listaFrequencia;
	}

	public void setListaFrequencia(Map<Object, Object> listaFrequencia) {
		this.listaFrequencia = listaFrequencia;
	}

	public Map<Object, Object> getListaIncentivoPais() {
		return listaIncentivoPais;
	}

	public void setListaIncentivoPais(Map<Object, Object> listaIncentivoPais) {
		this.listaIncentivoPais = listaIncentivoPais;
	}

	public Map<Object, Object> getListaGenero() {
		return listaGenero;
	}

	public void setListaGenero(Map<Object, Object> listaGenero) {
		this.listaGenero = listaGenero;
	}

	public Map<Object, Object> getListaPontoPositivo() {
		return listaPontoPositivo;
	}

	public void setListaPontoPositivo(Map<Object, Object> listaPontoPositivo) {
		this.listaPontoPositivo = listaPontoPositivo;
	}

	public Map<Object, Object> getListaPontoNegativo() {
		return listaPontoNegativo;
	}

	public void setListaPontoNegativo(Map<Object, Object> listaPontoNegativo) {
		this.listaPontoNegativo = listaPontoNegativo;
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

	public List<Aluno> getListaAlunos() {
		return listaAlunos;
	}

	public Sexo getSexo() {
		return sexo;
	}

	public void setSexo(Sexo sexo) {
		this.sexo = sexo;
	}

	public Integer getIdadeInicial() {
		return idadeInicial;
	}

	public void setIdadeInicial(Integer idadeInicial) {
		this.idadeInicial = idadeInicial;
	}

	public Integer getIdadeFinal() {
		return idadeFinal;
	}

	public void setIdadeFinal(Integer idadeFinal) {
		this.idadeFinal = idadeFinal;
	}

	public Date getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}
	
	public Questionario getQuestionarioAluno() {
		return questionarioAluno;
	}
	
	public void setQuestionarioAluno(Questionario questionarioAluno) {
		this.questionarioAluno = questionarioAluno;
	}
}
