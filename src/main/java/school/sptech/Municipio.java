package school.sptech;

public class Municipio {
    private String nome;
    private Integer totalHabitantes;

    //Getter e Setter
    public Municipio(String nome, Integer totalHabitantes) {
        this.nome = nome;
        this.totalHabitantes = totalHabitantes;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getTotalHabitantes() {
        return totalHabitantes;
    }

    public void setTotalHabitantes(int totalHabitantes) {
        this.totalHabitantes = totalHabitantes;
    }

    @Override
    public String toString() {
        return "Municipio{" +
                "nome='" + nome + '\'' +
                ", totalHabitantes=" + totalHabitantes +
                '}';
    }
}
