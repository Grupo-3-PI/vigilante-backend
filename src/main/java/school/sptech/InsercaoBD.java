package school.sptech;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;


public class InsercaoBD {

    private final JdbcTemplate jdbcTemplate;

    public InsercaoBD(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void inserirCrime(List<Crime> crimes) {

        if (crimes == null || crimes.isEmpty()){
            System.out.println("Não é possível realizar a inserção com lista vazia");
        }
        for (Crime crimeDaVez : crimes) {
            jdbcTemplate.update("INSERT INTO ocorrencias (tipo, qtd_ocorrencia, mes, ano, nome_municipio, gravidade) VALUES (?, ?, ?, ?, ?, ?)",
                    crimeDaVez.getTipo(), crimeDaVez.getQtdOcorrencias(), crimeDaVez.getMes(), crimeDaVez.getAno(), crimeDaVez.getMunicipio().getNome(), 0);
        }
    }
}
