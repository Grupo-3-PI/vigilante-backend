import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class log {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        String hora = ((LocalTime.now()).toString()).substring(0, 8);
        String data = (LocalDate.now()).toString().substring(8, 10) + "/" + (LocalDate.now()).toString().substring(5, 7) + "/" + (LocalDate.now()).toString().substring(0, 4);
        String nome = "";

        while (1 > 0) {

            System.out.println("Digite seu nome: ");
            Integer numRandom = ThreadLocalRandom.current().nextInt(1, 3);
            nome = scanner.nextLine();

            if (numRandom == 1) {
                System.out.println("\u001B[32mInfo: Usuário " + nome + " cadastrado com sucesso  [ " + data + " - " + hora + " ]\u001B[0m");
            } else if (numRandom == 2){
                System.out.println("\u001B[31mERRO: Não foi possível cadastrar o usuário " + nome + "  [ " + data + " - " + hora + " ]\u001B[0m");
            }
        }


    }

}
