package school.sptech;

import org.apache.commons.dbcp2.BasicDataSource;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Bucket;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {


        //Variáveis para inserir crimes/produtividade policial na lista e colocar no banco de dados
        Conexao conexao = new Conexao();
        LeituraDados leituraDados = new LeituraDados();
        InsercaoBD insercaoBDCrime = new InsercaoBD(conexao.getConexao());


        //Loop para inserir crimes/produtividade policial na lista e colocar no banco de dados
        String[] municipios = {"Bertioga", "Cubatão", "Guarujá", "Itanhaém", "Mongaguá", "Peruíbe",
        "Praia Grande", "Santos", "São Vicente"};


        for (int i = 0; i < municipios.length; i++) {
            //Inserindo crimes
            Path caminhoCrime = Paths.get("OcorrenciaMensal(Criminal)-" + municipios[i] + "_2025.xlsx");
            List<Crime> crimes = leituraDados.lerCrimes(caminhoCrime.toString());
            System.out.println(crimes.toString());
            insercaoBDCrime.inserirCrime(crimes);

            //Inserindo produtividade policial
            Path caminhoProdutividadePolicial = Paths.get("OcorrenciaMensal(ProdutividadePolicial)-" + municipios[i] + "_2025.xlsx");
            List<ProdutividadePolicial> produtividadePolicial = leituraDados.lerProdutividadePolicial(caminhoProdutividadePolicial.toString());
            System.out.println(produtividadePolicial.toString());
        }

    }

}
