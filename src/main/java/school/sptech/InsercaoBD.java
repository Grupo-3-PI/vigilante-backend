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

                jdbcTemplate.update(
                        "INSERT INTO ocorrencias (tipo, qtd_ocorrencia, mes, ano, nome_municipio, gravidade) VALUES (?, ?, ?, ?, ?, ?)",
                        crimeDaVez.getTipo(),
                        crimeDaVez.getQtdOcorrencias(),
                        crimeDaVez.getMes(),
                        crimeDaVez.getAno(),
                        crimeDaVez.getMunicipio().getNome(),
                        0
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
}