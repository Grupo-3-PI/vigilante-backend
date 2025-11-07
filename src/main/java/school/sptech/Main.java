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
        String bucketName = "s3-sptech-teste";
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
        String[] municipios = {"Praia Grande", "Santos"};


        for (int i = 0; i < municipios.length; i++) {
            //Inserindo crimes
            Path caminhoCrime = Paths.get("OcorrenciaMensal(Criminal)-" + municipios[i] + "_2025.xlsx");
            List<Crime> crimes = leituraDados.lerCrimes(caminhoCrime.toString());
            System.out.println(crimes.toString());
            insercaoBD.inserirCrime(crimes);

            //Inserindo produtividade policial
            Path caminhoProdutividadePolicial = Paths.get("OcorrenciaMensal(ProdutividadePolicial)-" + municipios[i] + "_2025.xlsx");
            List<ProdutividadePolicial> produtividadePolicial = leituraDados.lerProdutividadePolicial(caminhoProdutividadePolicial.toString());
            System.out.println(produtividadePolicial.toString());
            insercaoBD.inserirProdutividadePolicial(produtividadePolicial);
        }

    }

}
