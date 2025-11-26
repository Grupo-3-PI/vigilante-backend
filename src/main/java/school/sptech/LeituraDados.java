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

    private final JdbcTemplate jdbcTemplate;

    public LeituraDados(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private void logComTimestamp(String mensagem, String tipo) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println("[LOG] " + timestamp + " - " + mensagem);
        jdbcTemplate.update("INSERT INTO Logs (mensagem, tipo, dt_registro) VALUES (?, ?, ?)", mensagem, tipo, timestamp);
    }

    DataFormatter formatter = new DataFormatter();

    public List<Crime> lerCrimes(String caminhoDoArquivo) throws IOException {

        logComTimestamp("Iniciando leitura das bases de dados de crimes.", "PROCESSO");
        logComTimestamp("Abrindo arquivo: " + caminhoDoArquivo, "PROCESSO");

        InputStream arquivo = new FileInputStream(caminhoDoArquivo);
        Workbook workbook = new XSSFWorkbook(arquivo);
        Sheet sheet = workbook.getSheetAt(0);

        Integer ultimaLinha = sheet.getLastRowNum();
        List<Crime> listaDeCrimes = new ArrayList<>();
        Row linhaAnoMunicipios = sheet.getRow(1);

        logComTimestamp("Arquivo carregado. Total de linhas encontradas: " + ultimaLinha, "INFO");

        for (int i = 1; i <= ultimaLinha; i++) {
            Row linhaAtual = sheet.getRow(i);
            if (linhaAtual == null) continue;

            try {
                String tipo = linhaAtual.getCell(0).getStringCellValue();
                Integer ano = (int) linhaAnoMunicipios.getCell(15).getNumericCellValue();
                Municipio municipio = Municipio.valueOf(linhaAnoMunicipios.getCell(14).getStringCellValue().toUpperCase().replace("", "_"));

                int totalLinha = 0;

                for (int j = 1; j <= 12; j++) {
                    String valorOcorrencia = formatter.formatCellValue(linhaAtual.getCell(j));
                    if (valorOcorrencia.equals("...")) break;

                    Integer qtdOcorrencia = 0;
                    try {
                        qtdOcorrencia = Integer.parseInt(valorOcorrencia);
                    } catch (NumberFormatException e) {
                    }

                    listaDeCrimes.add(new Crime(tipo, qtdOcorrencia, ano, j, municipio, 0));
                    totalLinha += qtdOcorrencia;
                }

                logComTimestamp("Lendo linha " + i + ": " + tipo + " - Total: " + totalLinha, "INFO");

            } catch (Exception e) {
                logComTimestamp("Erro ao ler linha " + i + ": " + e.getMessage(), "ERRO");
            }
        }

        workbook.close();
        arquivo.close();

        logComTimestamp("Leitura de crimes finalizada. Total de registros: " + listaDeCrimes.size(), "SUCESSO");
        return listaDeCrimes;
    }

    public List<ProdutividadePolicial> lerProdutividadePolicial(String caminhoDoArquivo) throws IOException {

        logComTimestamp("Iniciando leitura das bases de dados de produtividade policial.", "PROCESSO");
        logComTimestamp("Abrindo arquivo: " + caminhoDoArquivo, "PROCESSO");

        InputStream arquivo = new FileInputStream(caminhoDoArquivo);
        Workbook workbook = new XSSFWorkbook(arquivo);
        Sheet sheet = workbook.getSheetAt(0);

        Integer ultimaLinha = sheet.getLastRowNum();
        List<ProdutividadePolicial> listaDeProdutividadePolicial = new ArrayList<>();
        Row linhaAnoMunicipios = sheet.getRow(1);

        logComTimestamp("Arquivo carregado. Total de linhas encontradas: " + ultimaLinha, "INFO");

        for (int i = 1; i <= ultimaLinha; i++) {
            Row linhaAtual = sheet.getRow(i);
            if (linhaAtual == null) continue;

            try {
                String tipo = linhaAtual.getCell(0).getStringCellValue();
                Integer ano = (int) linhaAnoMunicipios.getCell(15).getNumericCellValue();
                Municipio municipio = Municipio.valueOf(linhaAnoMunicipios.getCell(14).getStringCellValue().toUpperCase().replace("", "_"));

                int totalLinha = 0;

                for (int j = 1; j <= 12; j++) {
                    String valorOcorrencia = formatter.formatCellValue(linhaAtual.getCell(j));
                    if (valorOcorrencia.equals("...")) break;

                    Integer qtdOcorrencia = 0;
                    try {
                        qtdOcorrencia = Integer.parseInt(valorOcorrencia);
                    } catch (NumberFormatException e) {
                    }

                    listaDeProdutividadePolicial.add(new ProdutividadePolicial(tipo, qtdOcorrencia, ano, j, municipio));
                    totalLinha += qtdOcorrencia;
                }

                logComTimestamp("Lendo linha " + i + ": " + tipo + " - Total: " + totalLinha, "INFO");

            } catch (Exception e) {
                logComTimestamp("Erro ao ler linha " + i + ": " + e.getMessage(), "ERRO");
            }
        }

        workbook.close();
        arquivo.close();

        logComTimestamp("Leitura de produtividade policial finalizada. Total de registros: " + listaDeProdutividadePolicial.size(), "SUCESSO");
        return listaDeProdutividadePolicial;
    }
}
