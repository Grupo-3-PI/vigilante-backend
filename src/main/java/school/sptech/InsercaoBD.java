package school.sptech;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;

public class InsercaoBD {

    private final JdbcTemplate jdbcTemplate;

    // Códigos de cor ANSI
    private static final String RESET = "\u001B[0m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED = "\u001B[31m";
    private static final String CYAN = "\u001B[36m";

    public InsercaoBD(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Método auxiliar para logs com timestamp e cor
    private void logComTimestamp(String mensagem, String cor) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println(cor + "[LOG] " + timestamp + " - " + mensagem + RESET);
    }

    //Método para retornar o número do município
    private Integer obterFkMunicipio(String nomeMunicipio) {
        switch (nomeMunicipio) {
            case "Bertioga":
                return 1;
            case "Cubatão":
                return 2;
            case "Guarujá":
                return 3;
            case "Itanhaém":
                return 4;
            case "Mongaguá":
                return 5;
            case "Peruíbe":
                return 6;
            case "Praia Grande":
                return 7;
            case "Santos":
                return 8;
            case "São Vicente":
                return 9;
            default:
                throw new IllegalArgumentException("Município desconhecido: " + nomeMunicipio);
        }
    }


    //Inserindo crime no BD
    public void inserirCrime(List<Crime> crimes) {

        if (crimes == null || crimes.isEmpty()) {
            System.out.println(RED + "[ERRO] Lista de crimes vazia. Inserção cancelada." + RESET);
            return;
        }

        logComTimestamp("Iniciando inserção de dados na tabela 'ocorrencias'.", CYAN);
        logComTimestamp("Quantidade de registros a inserir: " + crimes.size(), CYAN);

        for (Crime crimeDaVez : crimes) {
            try {
                logComTimestamp("Inserindo crime: " + crimeDaVez.getTipo() +
                        " - Município: " + crimeDaVez.getMunicipio().getNome() +
                        " - Data: 0" + crimeDaVez.getMes() + "/" + crimeDaVez.getAno(), YELLOW);

                Integer fkMunicipio = obterFkMunicipio(crimeDaVez.getMunicipio().getNome());

                jdbcTemplate.update(
                        "INSERT INTO Ocorrencias (nome_crime, qtd_ocorrencias, mes, ano, gravidade, tipo_ocorrencia, fk_municipio) VALUES (?, ?, ?, ?, ?, ?, ?)",
                        crimeDaVez.getTipo(),
                        crimeDaVez.getQtdOcorrencias(),
                        crimeDaVez.getMes(),
                        crimeDaVez.getAno(),
                        0,
                        "Crime",
                        fkMunicipio
                );

                logComTimestamp("Inserção concluída com sucesso para o crime: " +
                        crimeDaVez.getTipo(), GREEN);

            } catch (Exception e) {
                logComTimestamp("Erro ao inserir crime: " + crimeDaVez.getTipo() +
                        " - " + e.getMessage(), RED);
            }
        }

        logComTimestamp("Finalizando inserção de dados na tabela 'ocorrencias'.", CYAN);
    }

    //Inserindo produtividade no BD
    public void inserirProdutividadePolicial(List<ProdutividadePolicial> produtividades) {

        if (produtividades == null || produtividades.isEmpty()) {
            System.out.println(RED + "[ERRO] Lista de produtividades vazia. Inserção cancelada." + RESET);
            return;
        }

        logComTimestamp("Iniciando inserção de dados na tabela 'ocorrencias' (Produtividade Policial).", CYAN);
        logComTimestamp("Quantidade de registros a inserir: " + produtividades.size(), CYAN);

        for (ProdutividadePolicial produtividadeDaVez : produtividades) {
            try {
                logComTimestamp("Inserindo produtividade: " + produtividadeDaVez.getTipo() +
                        " - Município: " + produtividadeDaVez.getMunicipio().getNome() +
                        " - Data: 0" + produtividadeDaVez.getMes() + "/" + produtividadeDaVez.getAno(), YELLOW);

                Integer fkMunicipio = obterFkMunicipio(produtividadeDaVez.getMunicipio().getNome());

                jdbcTemplate.update(
                        "INSERT INTO Ocorrencias (nome_crime, qtd_ocorrencias, mes, ano, tipo_ocorrencia, fk_municipio) VALUES (?, ?, ?, ?, ?, ?)",
                        produtividadeDaVez.getTipo(),
                        produtividadeDaVez.getQtdOcorrencias(),
                        produtividadeDaVez.getMes(),
                        produtividadeDaVez.getAno(),
                        "Produtividade Policial",
                        fkMunicipio
                );

                logComTimestamp("Inserção concluída com sucesso para: " +
                        produtividadeDaVez.getTipo(), GREEN);

            } catch (Exception e) {
                logComTimestamp("Erro ao inserir produtividade: " + produtividadeDaVez.getTipo() +
                        " - " + e.getMessage(), RED);
            }
        }

        logComTimestamp("Finalizando inserção de dados na tabela 'ocorrencias' (Produtividade Policial).", CYAN);
    }

}