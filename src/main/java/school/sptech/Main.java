package school.sptech;

import org.apache.commons.dbcp2.BasicDataSource;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        BasicDataSource dataSource = new BasicDataSource();
        Conexao conexao = new Conexao();

        LeituraDados leituraDados = new LeituraDados();
        InsercaoBD insercaoBDCrime = new InsercaoBD(conexao.getConexao());

        Path caminhoCrime = Paths.get("crimes", "OcorrenciaMensal(Criminal)-Mongaguá_20250815_155244.xlsx");
        List<Crime> crimes = leituraDados.lerCrimes(caminhoCrime.toString());
        System.out.println(crimes.toString());
        insercaoBDCrime.inserirCrime(crimes);



        Path caminhoProdutividadePolicial = Paths.get("produtividadePolicial", "OcorrenciaMensal(ProdutividadePolicial)-Bertioga_20251017_185449.xlsx");
        List<ProdutividadePolicial> produtividadePolicial = leituraDados.lerProdutividadePolicial(caminhoProdutividadePolicial.toString());
        System.out.println(produtividadePolicial.toString());
    }
}
