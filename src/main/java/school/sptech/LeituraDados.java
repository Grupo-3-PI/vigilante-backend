package school.sptech;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class LeituraDados {

    Conexao conexao = new Conexao();
    JdbcTemplate jdbcTemplate = conexao.getConexao();


    DataFormatter formatter = new DataFormatter();

    //Método - Leitura das Bases de dados - Crimes
    public List<Crime> lerCrimes(String caminhoDoArquivo) throws IOException {

        //Acessando a planilha
        InputStream arquivo = new FileInputStream(caminhoDoArquivo);
        Workbook workbook = new XSSFWorkbook(arquivo);
        //Criando variáveis
        Sheet sheet = workbook.getSheetAt(0);
        Integer ultimaLinha = sheet.getLastRowNum();
        List<Crime> listaDeCrimes = new ArrayList<>();
        Row linhaAnoMunicipios = sheet.getRow(1);

        //Percorrendo a planilha
        for (int i = 1; i <= ultimaLinha; i++) {
            Row linhaAtual = sheet.getRow(i);

            for (int j = 1; j <= 12; j++) {
                //Pegando valoresda planilha
                String tipo = linhaAtual.getCell(0).getStringCellValue();
                //Formatando ocorrências
                String valorOcorrencia = formatter.formatCellValue(linhaAtual.getCell(j));
                if (valorOcorrencia.equals("...")) break;
                Integer qtdOcorrencia = 0;
                try {
                    qtdOcorrencia = Integer.parseInt(valorOcorrencia);
                } catch (NumberFormatException e) {
                    //Se não for número, continua 0
                }

                Integer ano = (int) linhaAnoMunicipios.getCell(15).getNumericCellValue();
                Integer mes = j;
                Municipio municipio = new Municipio(linhaAnoMunicipios.getCell(14).getStringCellValue(), 0);

                Crime crime = new Crime(tipo, qtdOcorrencia, ano, mes, municipio, 0);
                listaDeCrimes.add(crime);
            }
        }

        workbook.close();
        arquivo.close();

        return listaDeCrimes;
    }

    //Método - Leitura das Bases de dados - Produtividade Policial
    public List<ProdutividadePolicial> lerProdutividadePolicial(String caminhoDoArquivo) throws IOException {

        //Acessando a planilha
        InputStream arquivo = new FileInputStream(caminhoDoArquivo);
        Workbook workbook = new XSSFWorkbook(arquivo);

        //Criando variáveis
        Sheet sheet = workbook.getSheetAt(0);
        Integer ultimaLinha = sheet.getLastRowNum();
        List<ProdutividadePolicial> listaDeProdutividadePolicial = new ArrayList<>();
        Row linhaAnoMunicipios = sheet.getRow(1);

        //Percorrendo a planilha
        for (int i = 1; i <= ultimaLinha; i++) {
            Row linhaAtual = sheet.getRow(i);

            for (int j = 1; j <= 12; j++) {
                //Pegando valores da planilha
                String tipo = linhaAtual.getCell(0).getStringCellValue();
                //Formatando ocorrências
                String valorOcorrencia = formatter.formatCellValue(linhaAtual.getCell(j));
                if (valorOcorrencia.equals("...")) break;
                Integer qtdOcorrencia = 0;
                try {
                    qtdOcorrencia = Integer.parseInt(valorOcorrencia);
                } catch (NumberFormatException e) {
                    //Se não for número, continua 0
                }

                Integer ano = (int) linhaAnoMunicipios.getCell(15).getNumericCellValue();
                Integer mes = j;
                Municipio municipio = new Municipio(linhaAnoMunicipios.getCell(14).getStringCellValue(), 0);

                ProdutividadePolicial produtividadePolicial = new ProdutividadePolicial(tipo, qtdOcorrencia, ano, mes, municipio);
                listaDeProdutividadePolicial.add(produtividadePolicial);
            }
        }

        workbook.close();
        arquivo.close();

        return listaDeProdutividadePolicial;
    }
}
