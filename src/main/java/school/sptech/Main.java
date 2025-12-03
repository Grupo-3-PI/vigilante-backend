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

        //Configurações para conexão com S3
        S3Provider s3Client = new S3Provider();
        S3Client credenciais = s3Client.getS3Client();
        String bucketName = "nome-bucket";
        ListObjectsRequest listObjects = ListObjectsRequest.builder().bucket(bucketName).build();

        List<Bucket> buckets = credenciais.listBuckets().buckets();
        for (Bucket bucket : buckets) {
            System.out.println("Bucket: " + bucket.name());
        }

        BasicDataSource dataSource = new BasicDataSource();

        List<S3Object> objects = credenciais.listObjects(listObjects).contents();
        for (S3Object object : objects) {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(object.key())
                    .build();

            InputStream objectContent = credenciais.getObject(getObjectRequest, ResponseTransformer.toInputStream());
            Files.copy(objectContent, new File(object.key()).toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);

        }

        //Variáveis para inserir crimes/produtividade policial na lista e colocar no banco de dados
        Conexao conexao = new Conexao();
        LeituraDados leituraDados = new LeituraDados(conexao.getConexao());
        InsercaoBD insercaoBD = new InsercaoBD(conexao.getConexao());


        //Loop para inserir crimes/produtividade policial na lista e colocar no banco de dados
        String[] municipios = {"Bertioga", "Cubatão", "Guarujá", "Itanhaém", "Mongaguá", "Peruíbe", "Praia Grande", "Santos", "São Vicente"};


        for (int i = 0; i < municipios.length; i++) {
            //Inserindo crimes (2025)
            Path caminhoCrime2025 = Paths.get("OcorrenciaMensal(Criminal)-" + municipios[i] + "_2025.xlsx");
            List<Crime> crimes2025 = leituraDados.lerCrimes(caminhoCrime2025.toString());
            System.out.println(crimes2025.toString());
            insercaoBD.inserirCrime(crimes2025);

            //Inserindo crimes (2024)
            Path caminhoCrime2024 = Paths.get("OcorrenciaMensal(Criminal)-" + municipios[i] + "_2024.xlsx");
            List<Crime> crimes2024 = leituraDados.lerCrimes(caminhoCrime2024.toString());
            System.out.println(crimes2024.toString());
            insercaoBD.inserirCrime(crimes2024);

            //Inserindo crimes (2023)
            Path caminhoCrime2023 = Paths.get("OcorrenciaMensal(Criminal)-" + municipios[i] + "_2023.xlsx");
            List<Crime> crimes2023 = leituraDados.lerCrimes(caminhoCrime2023.toString());
            System.out.println(crimes2023.toString());
            insercaoBD.inserirCrime(crimes2023);

            //Inserindo produtividade policial (2025)
            Path caminhoProdutividadePolicial2025 = Paths.get("OcorrenciaMensal(ProdutividadePolicial)-" + municipios[i] + "_2025.xlsx");
            List<ProdutividadePolicial> produtividadePolicial2025 = leituraDados.lerProdutividadePolicial(caminhoProdutividadePolicial2025.toString());
            System.out.println(produtividadePolicial2025.toString());
            insercaoBD.inserirProdutividadePolicial(produtividadePolicial2025);

            //Inserindo produtividade policial (2024)
            Path caminhoProdutividadePolicial2024 = Paths.get("OcorrenciaMensal(ProdutividadePolicial)-" + municipios[i] + "_2024.xlsx");
            List<ProdutividadePolicial> produtividadePolicial2024 = leituraDados.lerProdutividadePolicial(caminhoProdutividadePolicial2024.toString());
            System.out.println(produtividadePolicial2024.toString());
            insercaoBD.inserirProdutividadePolicial(produtividadePolicial2024);

            //Inserindo produtividade policial (2023)
            Path caminhoProdutividadePolicial2023 = Paths.get("OcorrenciaMensal(ProdutividadePolicial)-" + municipios[i] + "_2023.xlsx");
            List<ProdutividadePolicial> produtividadePolicial2023 = leituraDados.lerProdutividadePolicial(caminhoProdutividadePolicial2023.toString());
            System.out.println(produtividadePolicial2023.toString());
            insercaoBD.inserirProdutividadePolicial(produtividadePolicial2023);
        }

    }

}
