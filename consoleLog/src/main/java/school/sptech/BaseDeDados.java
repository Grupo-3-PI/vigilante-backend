package school.sptech;

public class BaseDeDados {
    private String tipo;
    private Integer qtdOcorrencias;
    private Integer mes;
    private Integer ano;
    private Municipio municipio;

    public BaseDeDados(Integer qtdOcorrencias, Integer ano, String tipo, Integer mes, Municipio municipio) {
        this.qtdOcorrencias = qtdOcorrencias;
        this.ano = ano;
        this.tipo = tipo;
        this.mes = mes;
        this.municipio = municipio;
    }

    //Getter e Setter
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getQtdOcorrencias() {
        return qtdOcorrencias;
    }

    public void setQtdOcorrencias(Integer qtdOcorrencias) {
        this.qtdOcorrencias = qtdOcorrencias;
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public Municipio getMunicipio() {
        return municipio;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }

    @Override
    public String toString() {
        return "BaseDeDados{" +
                "tipo='" + tipo + '\'' +
                ", qtdOcorrencias=" + qtdOcorrencias +
                ", mes=" + mes +
                ", ano=" + ano +
                ", municipio=" + municipio +
                '}';
    }
}
