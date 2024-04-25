import java.util.Random;
import java.util.Date;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Vetor {
    static void geraVetor(int[] v) {
        Random random = new Random();
        for (int i = 0; i < v.length; i++) {
            v[i] = random.nextInt(v.length * 10);
        }
    }

    static void exibeVetor(int[] v) {
        for (int i = 0; i < v.length; i++) {
            System.out.print(v[i] + " ");
        }
        System.out.println();
    }

    static void bubbleSort(int[] v) {
        for (int i = 1; i < v.length; i++) {
            for (int j = 0; j < v.length - i; j++) {
                if (v[j] > v[j + 1]) {
                    int aux = v[j];
                    v[j] = v[j + 1];
                    v[j + 1] = aux;
                }
            }
        }
    }

    static void insertionSort(int[] v) {
        for (int i = 0; i < v.length; i++) {
            int chave = v[i];
            int j = i - 1;
            while (j >= 0 && v[j] > chave) {
                v[j + 1] = v[j];
                j -= 1;
            }
            v[j + 1] = chave;

        }
    }

    static void selectionSort(int[] v) {
        for (int j = 0; j < v.length; j++) {
            int valorMinimo = j;
            for (int i = j + 1; i < v.length; i++) {
                if (v[i] < v[valorMinimo]) {
                    valorMinimo = i;
                }

            }
            int guardaValor = v[j];
            v[j] = v[valorMinimo];
            v[valorMinimo] = guardaValor;
        }
    }

    private static Connection createConnection() {
        try {
            Connection connection = DriverManager.getConnection();
            System.out.println("Conectado e enviando dados ao banco de dados");
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException("Cannot connect to database" + e);
        }
    }

    static void salvarDados(int tamanho, long tempo, String hostname, String tipo) {
        while(true){
            try {
                Connection con = createConnection();
                PreparedStatement pst = con
                .prepareStatement("INSERT INTO Resultados (tamanho, tempo, pc, tipo) VALUES (?,?,?, ?);");
                pst.setInt(1, tamanho);
                pst.setLong(2, tempo);
                pst.setString(3, hostname);
                pst.setString(4, tipo);
                pst.executeUpdate();
                break;
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        System.out.println("Dados enviados!");
    }

    static String getNomePC() {
        String hostname = "Unknown";
        try {
            InetAddress addr;
            addr = InetAddress.getLocalHost();
            hostname = addr.getHostName();
        } catch (UnknownHostException ex) {
            System.out.println("Hostname can not be resolved");
        }
        return hostname;
    }

    static void realizaTeste(int n, String hostname){
        int[] vB = new int[n];
        int[] vI = new int[n];
        int[] vS = new int[n];
        geraVetor(vB);
        geraVetor(vI);
        geraVetor(vS);
        long iniB = new Date().getTime();
        bubbleSort(vB);
        long fimB = new Date().getTime();
        long iniI = new Date().getTime();
        insertionSort(vI);
        long fimI = new Date().getTime();
        long iniS = new Date().getTime();
        selectionSort(vS);
        long fimS = new Date().getTime();
        long tempoTotalB = fimB - iniB;
        salvarDados(n, tempoTotalB, hostname, "bubbleSort");
        long tempoTotalI = fimI - iniI;
        salvarDados(n, tempoTotalI, hostname, "insertionSort");
        long tempoTotalS = fimS - iniS;
        salvarDados(n, tempoTotalS, hostname, "selectionSort");
    }

    public static void main(String[] args) {
        String hostname = getNomePC();
        for (int n = 200000; n <= 1400000; n = n + 200) {
                realizaTeste(n,hostname);
                System.out.println("Vetor é de: " + n + " :D");
        }
    }
}