import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class log {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        String hora = ((LocalTime.now()).toString()).substring(0, 8);
        String data = (LocalDate.now()).toString().substring(8, 10) + "/" + (LocalDate.now()).toString().substring(5, 7) + "/" + (LocalDate.now()).toString().substring(0, 4);

        System.out.println("Digite seu nome: ");
        String nome = scanner.nextLine();
        Integer numRandom = ThreadLocalRandom.current().nextInt(1, 3);

        if (numRandom == 1) {
            System.out.println("✅ Info: Usuário " + nome + " cadastrado com sucesso  [ " + data + " - " + hora + " ]");
        } else if (numRandom == 2){
            System.out.println("❌ ERRO: Não foi possível cadastrar o usuário " + nome + "  [ " + data + " - " + hora + " ]");
        }

    }

}
