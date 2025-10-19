package school.sptech;

public class ProdutividadePolicial extends BaseDeDados{
    public ProdutividadePolicial(String tipo, Integer qtdOcorrencias, Integer ano, Integer mes, Municipio municipio) {
        super(tipo, qtdOcorrencias, ano, mes, municipio);
    }

    @Override
    public String toString() {
        return "ProdutividadePolicial{" + super.toString() + "}";
    }
}
