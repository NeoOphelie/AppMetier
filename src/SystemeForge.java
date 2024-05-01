import java.sql.SQLException;
import java.util.Random;

public class SystemeForge {
    public static void lootMinerai(int perso_id, String mortalite, int quantite, String localisation) throws SQLException {

        // palier de récupération selon la mortalité
        int[][] paliers = {
                {60, 40, 20, 4},    // Mortel
                {80, 60, 30, 6},   // Surhumain
                {100, 80, 40, 10},   // immortel
                {100, 100, 50, 15},   // Demi Divin
                {100, 100, 65, 20},  // Divin
        };
        // équivalent mortalité niveau :
        int niveau = getMortaliteValue(mortalite);
        if (niveau == 0) {return;};
        // lancer de dé pour savoir si on trouve du minerai
        Random random = new Random();

        for (int i = 0; i < quantite; i++) {
            int jet1 = random.nextInt(100) + 1;
            int jet2 = random.nextInt(100) + 1;
            int jet3 = random.nextInt(100) + 1;
            int jet4 = random.nextInt(100) + 1;

            String resultat = "";

            if (jet1 > paliers[niveau - 1][0]) {
                resultat = "Aucun minerai";
                // System.out.println(resultat);
            } else if (jet2 > paliers[niveau - 1][1]) {
                resultat = "M";
                Bdd.lootEtInsererMinerai(resultat, perso_id);
            } else if (jet3 > paliers[niveau - 1][2]) {
                resultat = "S";
                Bdd.lootEtInsererMinerai(resultat, perso_id);
            } else if (jet4 > paliers[niveau - 1][3]) {
                resultat = "I";
                Bdd.lootEtInsererMinerai(resultat, perso_id);
            } else {
                resultat = "D";
                Bdd.lootEtInsererMinerai(resultat, perso_id);
            }
            // System.out.println(resultat);
        }
    }
    public static int getMortaliteValue(String mortalite) {
        switch (mortalite.toLowerCase()) {
            case "mortel":
                return 1;
            case "surhumain":
                return 2;
            case "immortel":
                return 3;
            case "demi-dieu":
                return 4;
            case "divin":
                return 5;
            default:
                return 0; // Valeur par défaut si la mortalité n'est pas reconnue
        }
    }
}
