package school.sptech;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        LeituraDados leituraDados = new LeituraDados();

        Path caminhoCrime = Paths.get("crimes", "OcorrenciaMensal(Criminal)-Mongaguá_20250815_155244.xlsx");
        List<Crime> crimes = leituraDados.lerCrimes(caminhoCrime.toString());
        System.out.println(crimes.toString());


        Path caminhoProdutividadePolicial = Paths.get("produtividadePolicial", "OcorrenciaMensal(ProdutividadePolicial)-Bertioga_20251017_185449.xlsx");
        List<ProdutividadePolicial> produtividadePolicial = leituraDados.lerProdutividadePolicial(caminhoProdutividadePolicial.toString());
        System.out.println(produtividadePolicial.toString());
    }
}
