package school.sptech;
import org.apache.logging.log4j.core.appender.db.jdbc.DriverManagerConnectionSource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

public class Conexao {
    private DataSource conexao;

    public Conexao() {
        DriverManagerDataSource driver = new DriverManagerDataSource();
        driver.setUsername("");
        driver.setPassword("");
        driver.setUrl("jdbc:mysql://localhost:3306/teste?useSSL=false&serverTimezone=UT");
        //trocar "teste" e adicionar senha e username
        driver.setDriverClassName("com.mysql.cj.jdbc.Driver\n");

        this.conexao = driver;
    }

    public DataSource getConexao() {
        return conexao;
    }


}
