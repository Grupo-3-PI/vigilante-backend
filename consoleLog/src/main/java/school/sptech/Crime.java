package school.sptech;

public class Crime extends BaseDeDados{
    private Integer gravidade;

    public Crime(Integer qtdOcorrencias, Integer ano, String tipo, Integer mes, Municipio municipio, Integer gravidade) {
        super(qtdOcorrencias, ano, tipo, mes, municipio);
        this.gravidade = gravidade;
    }

    //Getter e Setter

    public Integer getGravidade() {
        return gravidade;
    }

    public void setGravidade(Integer gravidade) {
        this.gravidade = gravidade;
    }
}
