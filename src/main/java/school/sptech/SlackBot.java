package school.sptech;

import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SlackBot {

    private static final String BOT_TOKEN = "";
    private String CHANNEL_ID = "";
    private String MESSAGE_TEXT = "";
    private Integer status = 0;
    private Integer produtividade = 0;
    private Integer crimes = 0;
    private Conexao conexao;
    private Integer info = 0;
    private Integer warn = 0;
    private Integer error = 0;
    JdbcTemplate jdbcTemplate = new JdbcTemplate();
    DataFormatter formatter = new DataFormatter();


    public SlackBot(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public static void main(String[] args) {

        Conexao conexao = new Conexao();
        JdbcTemplate jdbc = conexao.getConexao();

        SlackBot bot = new SlackBot(jdbc);

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        scheduler.scheduleAtFixedRate(() -> {
            try {
                bot.verificarStatus();
                bot.calcularMetricas();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 10, TimeUnit.SECONDS);

        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    public void verificarStatus() {
        try {

            status = jdbcTemplate.queryForObject(
                    "SELECT status FROM AvisosSlack WHERE fk_usuario = ? LIMIT 1",
                    Integer.class, 1);

            CHANNEL_ID = jdbcTemplate.queryForObject(
                    "SELECT canal FROM AvisosSlack WHERE fk_usuario = ? LIMIT 1",
                    String.class, 1);

            info = jdbcTemplate.queryForObject(
                    "SELECT info FROM AvisosSlack WHERE fk_usuario = ? LIMIT 1",
                    Integer.class, 1);

            warn = jdbcTemplate.queryForObject(
                    "SELECT warn FROM AvisosSlack WHERE fk_usuario = ? LIMIT 1",
                    Integer.class, 1);

            error = jdbcTemplate.queryForObject(
                    "SELECT error FROM AvisosSlack WHERE fk_usuario = ? LIMIT 1",
                    Integer.class, 1);


            System.out.println("Info: " + info + " | Warn: " + warn + " | Error: " + error);

            status = jdbcTemplate.queryForObject(
                    "SELECT status FROM AvisosSlack WHERE fk_usuario = ? LIMIT 1",
                    Integer.class, 1);

            CHANNEL_ID = jdbcTemplate.queryForObject(
                    "SELECT canal FROM AvisosSlack WHERE fk_usuario = ? LIMIT 1",
                    String.class, 1);

            System.out.println("Status atual: " + status);
            System.out.println("Canal atual: " + CHANNEL_ID);


            if (status == 1) {
                enviarMensagem("Status = 1", CHANNEL_ID);
            }

        } catch (Exception e) {
            System.err.println("Erro ao consultar status/canal: " + e.getMessage());
        }
    }


    public void calcularMetricas() {
        if (status == 1) {
            try {

                // 1️⃣ Buscar os 2 meses mais recentes
                String sqlMeses = """
                        SELECT DISTINCT ano, mes 
                        FROM Ocorrencias 
                        ORDER BY ano DESC, mes DESC 
                        LIMIT 2
                        """;

                var meses = jdbcTemplate.queryForList(sqlMeses);

                if (meses.size() < 2) {
                    System.out.println("Não há dados suficientes.");
                    return;
                }

                int anoAtual = (int) meses.get(0).get("ano");
                int mesAtual = (int) meses.get(0).get("mes");
                int anoAnterior = (int) meses.get(1).get("ano");
                int mesAnterior = (int) meses.get(1).get("mes");


                // 2️⃣ Somar crimes e produtividade dos dois meses
                Integer crimesAtual = somarOcorrencias("Crime", mesAtual, anoAtual);
                Integer crimesAnterior = somarOcorrencias("Crime", mesAnterior, anoAnterior);

                Integer prodAtual = somarOcorrencias("Produtividade Policial", mesAtual, anoAtual);
                Integer prodAnterior = somarOcorrencias("Produtividade Policial", mesAnterior, anoAnterior);


                double varCrimes = (crimesAnterior == 0) ? 0 :
                        ((crimesAtual - crimesAnterior) * 100.0 / crimesAnterior);

                double varProd = (prodAnterior == 0) ? 0 :
                        ((prodAtual - prodAnterior) * 100.0 / prodAnterior);


                String prodTexto = (varProd < 0)
                        ? "produtividade policial caiu " + String.format("%.0f", Math.abs(varProd)) + "%"
                        : "produtividade policial subiu " + String.format("%.0f", varProd) + "%";

                String crimeTexto = (varCrimes < 0)
                        ? "e os crimes caíram " + String.format("%.0f", Math.abs(varCrimes)) + "%"
                        : "e os crimes subiram " + String.format("%.0f", varCrimes) + "%";

                String notifProdCrimes = prodTexto + " " + crimeTexto + ".";


                double abs = Math.abs(varCrimes);
                String mesNome = nomeMes(mesAnterior);
                String notifEstabilidade;
                String tipoEstabilidade;

                if (abs < 3) {
                    notifEstabilidade = "O índice de criminalidade foi estável (" +
                            String.format("%.0f", varCrimes) + "% em relação a " + mesNome + ").";
                    tipoEstabilidade = "info";

                } else {
                    notifEstabilidade = "Alerta: o índice de criminalidade variou " +
                            String.format("%.0f", varCrimes) + "% em relação a " + mesNome + ").";
                    tipoEstabilidade = "warn";
                }

                // PRODUTIVIDADE → sempre WARN
                if (warn == 1) {
                    enviarMensagemProdutividade(notifProdCrimes);
                }

                // ESTABILIDADE → agora só INFO ou WARN
                if (tipoEstabilidade.equals("info") && info == 1) {
                    enviarMensagemProdutividade(notifEstabilidade);
                }
                else if (tipoEstabilidade.equals("warn") && warn == 1) {
                    enviarMensagemProdutividade(notifEstabilidade);
                }

                // 6️⃣ Enviar mensagens ao Slack
                enviarMensagemProdutividade(notifProdCrimes);
                enviarMensagemProdutividade(notifEstabilidade);

                System.out.println(notifProdCrimes);
                System.out.println(notifEstabilidade);

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Erro ao calcular métricas: " + e.getMessage());
            }
        }
    }

    private Integer somarOcorrencias(String tipo, int mes, int ano) {

        String sql = """
            SELECT SUM(qtd_ocorrencias)
            FROM Ocorrencias
            WHERE tipo_ocorrencia = ?
              AND mes = ?
              AND ano = ?
            """;

        Integer total = jdbcTemplate.queryForObject(sql, new Object[]{tipo, mes, ano}, Integer.class);
        return (total == null) ? 0 : total;
    }

    private String nomeMes(int mes) {
        return switch (mes) {
            case 1 -> "janeiro";
            case 2 -> "fevereiro";
            case 3 -> "março";
            case 4 -> "abril";
            case 5 -> "maio";
            case 6 -> "junho";
            case 7 -> "julho";
            case 8 -> "agosto";
            case 9 -> "setembro";
            case 10 -> "outubro";
            case 11 -> "novembro";
            case 12 -> "dezembro";
            default -> "mês desconhecido";
        };
    }


    private void enviarMensagem(String MESSAGE_TEXT, String CHANNEL_ID) throws IOException, SlackApiException {

        Slack slack = Slack.getInstance();

        slack.methods(BOT_TOKEN).chatPostMessage(req -> req
                .channel(CHANNEL_ID)
                .text(MESSAGE_TEXT)
        );

        System.out.println("Mensagem enviada para o canal: " + CHANNEL_ID);
    }

    private void enviarMensagemProdutividade(String MESSAGE_TEXT) throws IOException, SlackApiException {

        Slack slack = Slack.getInstance();

        slack.methods(BOT_TOKEN).chatPostMessage(req -> req
                .channel(CHANNEL_ID)
                .text(MESSAGE_TEXT)
        );

        System.out.println("Mensagem enviada para o canal: " + CHANNEL_ID);
    }
}