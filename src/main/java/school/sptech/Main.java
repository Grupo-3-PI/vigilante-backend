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


        S3Provider s3Client = new S3Provider();
        S3Client credenciais = s3Client.getS3Client();
        String bucketName = "s3-prevcrime";
        ListObjectsRequest listObjects = ListObjectsRequest.builder().bucket(bucketName).build();

        List<Bucket> buckets = credenciais.listBuckets().buckets();
        for (Bucket bucket : buckets) {
            System.out.println("Bucket: " + bucket.name());
        }

        BasicDataSource dataSource = new BasicDataSource();
        Conexao conexao = new Conexao();

        LeituraDados leituraDados = new LeituraDados();
        InsercaoBD insercaoBDCrime = new InsercaoBD(conexao.getConexao());

        List<S3Object> objects = credenciais.listObjects(listObjects).contents();
        for (S3Object object : objects) {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(object.key())
                    .build();

            InputStream objectContent = credenciais.getObject(getObjectRequest, ResponseTransformer.toInputStream());
            Files.copy(objectContent, new File(object.key()).toPath());
        }

        Path caminhoCrime = Paths.get("crimes", "OcorrenciaMensal(Criminal)-Mongaguá_20250815_155244.xlsx");
        List<Crime> crimes = leituraDados.lerCrimes(caminhoCrime.toString());
        System.out.println(crimes.toString());
        insercaoBDCrime.inserirCrime(crimes);



        Path caminhoProdutividadePolicial = Paths.get("produtividadePolicial", "OcorrenciaMensal(ProdutividadePolicial)-Bertioga_20251017_185449.xlsx");
        List<ProdutividadePolicial> produtividadePolicial = leituraDados.lerProdutividadePolicial(caminhoProdutividadePolicial.toString());
        System.out.println(produtividadePolicial.toString());


    }

}
