package school.sptech;

public enum Municipio {

    BERTIOGA("Bertioga", 64188),
    PERUIBE("Peruíbe", 68352),
    GUARUJA("Guarujá", 322750),
    SANTOS("Santos", 433311),
    CUBATAO("Cubatão", 118720),
    SAO_VICENTE("São Vicente", 374202),
    PRAIA_GRANDE("Praia Grande", 330845),
    MONGAGUA("Mongaguá", 56000),
    ITANHAEM("Itanhaém", 105000);

    private final String nome;
    private final int totalHabitantes;

    Municipio(String nome, int totalHabitantes) {
        this.nome = nome;
        this.totalHabitantes = totalHabitantes;
    }

    public String getNome() {
        return nome;
    }

    public int getTotalHabitantes() {
        return totalHabitantes;
    }

    @Override
    public String toString() {
        return nome + " (" + totalHabitantes + " habitantes)";
    }
}
