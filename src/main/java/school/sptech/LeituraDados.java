package school.sptech;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class LeituraDados {

    private void logComTimestamp(String mensagem) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println("[LOG] " + timestamp + " - " + mensagem);
    }

    Conexao conexao = new Conexao();
    JdbcTemplate jdbcTemplate = new JdbcTemplate(conexao.getConexao());
    DataFormatter formatter = new DataFormatter();

    // Método - Leitura das Bases de dados - Crimes
    public List<Crime> lerCrimes(String caminhoDoArquivo) throws IOException {

        logComTimestamp("Iniciando leitura das bases de dados de crimes.");
        logComTimestamp("Abrindo arquivo: " + caminhoDoArquivo);

        InputStream arquivo = new FileInputStream(caminhoDoArquivo);
        Workbook workbook = new XSSFWorkbook(arquivo);
        Sheet sheet = workbook.getSheetAt(0);

        Integer ultimaLinha = sheet.getLastRowNum();
        List<Crime> listaDeCrimes = new ArrayList<>();
        Row linhaAnoMunicipios = sheet.getRow(1);

        logComTimestamp("Arquivo carregado. Total de linhas encontradas: " + ultimaLinha);

        for (int i = 1; i <= ultimaLinha; i++) {
            Row linhaAtual = sheet.getRow(i);
            if (linhaAtual == null) continue;

            String tipo = linhaAtual.getCell(0).getStringCellValue();
            Integer ano = (int) linhaAnoMunicipios.getCell(15).getNumericCellValue();
            Municipio municipio = new Municipio(linhaAnoMunicipios.getCell(14).getStringCellValue(), 0);

            int totalLinha = 0;

            for (int j = 1; j <= 12; j++) {
                String valorOcorrencia = formatter.formatCellValue(linhaAtual.getCell(j));
                if (valorOcorrencia.equals("...")) break;

                Integer qtdOcorrencia = 0;
                try {
                    qtdOcorrencia = Integer.parseInt(valorOcorrencia);
                } catch (NumberFormatException e) {
                    // Mantém 0 se não for número
                }

                listaDeCrimes.add(new Crime(tipo, qtdOcorrencia, ano, j, municipio, 0));
                totalLinha += qtdOcorrencia;
            }

            logComTimestamp("Lendo linha " + i + ": " + tipo + " - Total: " + totalLinha);
        }

        workbook.close();
        arquivo.close();

        logComTimestamp("Leitura de crimes finalizada. Total de registros: " + listaDeCrimes.size());
        return listaDeCrimes;
    }

    // Método - Leitura das Bases de dados - Produtividade Policial
    public List<ProdutividadePolicial> lerProdutividadePolicial(String caminhoDoArquivo) throws IOException {

        logComTimestamp("Iniciando leitura das bases de dados de produtividade policial.");
        logComTimestamp("Abrindo arquivo: " + caminhoDoArquivo);

        InputStream arquivo = new FileInputStream(caminhoDoArquivo);
        Workbook workbook = new XSSFWorkbook(arquivo);
        Sheet sheet = workbook.getSheetAt(0);

        Integer ultimaLinha = sheet.getLastRowNum();
        List<ProdutividadePolicial> listaDeProdutividadePolicial = new ArrayList<>();
        Row linhaAnoMunicipios = sheet.getRow(1);

        logComTimestamp("Arquivo carregado. Total de linhas encontradas: " + ultimaLinha);

        for (int i = 1; i <= ultimaLinha; i++) {
            Row linhaAtual = sheet.getRow(i);
            if (linhaAtual == null) continue;

            String tipo = linhaAtual.getCell(0).getStringCellValue();
            Integer ano = (int) linhaAnoMunicipios.getCell(15).getNumericCellValue();
            Municipio municipio = new Municipio(linhaAnoMunicipios.getCell(14).getStringCellValue(), 0);

            int totalLinha = 0;

            for (int j = 1; j <= 12; j++) {
                String valorOcorrencia = formatter.formatCellValue(linhaAtual.getCell(j));
                if (valorOcorrencia.equals("...")) break;

                Integer qtdOcorrencia = 0;
                try {
                    qtdOcorrencia = Integer.parseInt(valorOcorrencia);
                } catch (NumberFormatException e) {
                    // Mantém 0 se não for número
                }

                listaDeProdutividadePolicial.add(new ProdutividadePolicial(tipo, qtdOcorrencia, ano, j, municipio));
                totalLinha += qtdOcorrencia;
            }

            logComTimestamp("Lendo linha " + i + ": " + tipo + " - Total: " + totalLinha);
        }

        workbook.close();
        arquivo.close();

        logComTimestamp("Leitura de produtividade policial finalizada. Total de registros: " + listaDeProdutividadePolicial.size());
        return listaDeProdutividadePolicial;
    }
}
