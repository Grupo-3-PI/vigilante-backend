package school.sptech;

public class Crime extends BaseDeDados{
    private Integer gravidade;

    public Crime(String tipo, Integer qtdOcorrencias, Integer ano, Integer mes, Municipio municipio, Integer gravidade) {
        super(tipo, qtdOcorrencias, ano, mes, municipio);
        this.gravidade = gravidade;
    }

    //Getter e Setter
    public Integer getGravidade() {
        return gravidade;
    }

    public void setGravidade(Integer gravidade) {
        this.gravidade = gravidade;
    }

    @Override
    public String toString() {
        return "Crime{"
                + super.toString() +
                "gravidade=" + gravidade +
                '}';
    }
}
