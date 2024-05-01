import java.sql.SQLException;
import java.util.*;

public class SystemeAlchimie {
    public static void lootPlante(int niveau, int quantite, String localisation) throws SQLException {
        // Définition des paliers par niveau
        int[][] paliers = {
                {20, 10, 5, 1},    // Niveau 1
                {40, 20, 10, 2},   // Niveau 2
                {60, 30, 15, 3},   // Niveau 3
                {80, 40, 20, 4},   // Niveau 4
                {100, 50, 25, 5},  // Niveau 5
                {100, 60, 30, 10}  // Niveau 6
        };

        Random random = new Random();

        for (int i = 0; i < quantite; i++) {
            int jet1 = random.nextInt(100) + 1;
            int jet2 = random.nextInt(100) + 1;
            int jet3 = random.nextInt(100) + 1;
            int jet4 = random.nextInt(100) + 1;

            String resultat = "";

            if (jet1 > paliers[niveau - 1][0]) {
                resultat = "C";
                Bdd.lootEtInsererPlante(resultat, Main.perso_id);
            } else if (jet2 > paliers[niveau - 1][1]) {
                resultat = "R";
                Bdd.lootEtInsererPlante(resultat, Main.perso_id);
            } else if (jet3 > paliers[niveau - 1][2]) {
                resultat = "TR";
                Bdd.lootEtInsererPlante(resultat, Main.perso_id);
            } else if (jet4 > paliers[niveau - 1][3]) {
                resultat = "M";
                Bdd.lootEtInsererPlante(resultat, Main.perso_id);
            } else {
                resultat = "L";
                Bdd.lootEtInsererPlante(resultat, Main.perso_id);
            }
        }
    }

    public static List<Integer> calculStatistiqueBrute(List<String> selectedItems) {
        // Liste des stats de la potion : cumule des stats des plantes
        List<Integer> statsPotion = new ArrayList<>(Collections.nCopies(5, 0));

        for (String selectedItem : selectedItems) {
            try {
                List<Integer> stats = Bdd.getDataForComposition(selectedItem, Main.perso_na);
//                System.out.println("Statistiques de " + selectedItem + " : " + stats);
                for (int i = 0; i < statsPotion.size(); i++) {
                    int somme = statsPotion.get(i) + stats.get(i);
                    statsPotion.set(i, somme);
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
//        System.out.println("Stats Potion : " + statsPotion);
        return statsPotion;
    }

    public static String calculStatistiqueComplexe(int na, List<Integer> statsBrutes) {
        /* liste des stats complexes :
        1 S = 5hp * niveau alchimiste ___ 1 * na hp pendant 3 tours ___ 8 * na HP temporaire
        RF :
        calcul =  0.25SL * na pendant 1 journée
        01-19 : calcul * 0
        20-39 : calcul * 1
        40-59 : calcul * 2
        60-79 : calcul * 3
        80-99 : calcul * 4
        ------> Arrondissement sup

        1 D = 3 * na Dégats instanté ( explosif ) ___ 2 * na Dégats de poison pendant 3 tours ___ +1 * na dégat supplémentaire adaptatif
        1 Re = 1 * na Armure physique ___ 1 * na armure magique
        1 M = + 0.07 * na mouvement
        ------> Arrondissement sup
        */
        // Liste des stats de la potion : cumule des stats des plantes
        // version list
        //List<String> statsComplexe = new ArrayList<>(Arrays.asList("", "", "", "", ""));
        String statsComplexe = "";
        // randominsation et initialisation des choix de stats complexe
        Random random = new Random();
        int jetS = 0;
        // int jetRf = 0;
        int jetD = 0;
        int jetRe = 0;
        // int jetM = 0;
        // on parcourt la liste de stat brut
        for (int i = 0; i < statsBrutes.size(); i++) {
            int element = statsBrutes.get(i);
            if (element > 0) {
                if (i == 0) {
                    // index 0 : S : 3 choix possible
                    jetS = random.nextInt(3) + 1;
                    // calcul et application stats complexe
                    if (jetS == 1) {
                        // S : 5HP * element * na
                        int S = 5 * na * element;
                        statsComplexe += ("+" + S + " HP,");
                    } else if (jetS == 2) {
                        // S : 1HP * na * element pendant 3 tours
                        int S = element * na;
                        statsComplexe += ("+" + S + " HP/3t,");
                    } else {
                        // S : 8 * na * element HP temporaire
                        int S = 8 * element * na;
                        statsComplexe += ("+" + S + " HPt,");
                    }
                } else if (i == 1) {
                    // index 1 : Rf : 1 choix possible
                    // jetRf = random.nextInt(2) + 1;
                    // calcul et application stats complexe
                    // base : 0.25SL * na pendant 1 journée
                    double base = 0.25 * na;
                    double Rf;
                    if (element < 20) {
                        Rf = base * 0;
                    } else if (element < 40) {
                        Rf = base * 1;
                    } else if (element < 60) {
                        Rf = base * 2;
                    } else if (element < 80) {
                        Rf = base * 4;
                    } else {
                        Rf = base * 5;
                    }
                    Rf = Math.round(Rf);
                    if (Rf>0) {
                        statsComplexe += ("+" + Rf + " SL/J,");
                    }
                } else if (i == 2) {
                    // index 2 : D : 3 choix possible
                    jetD = random.nextInt(3) + 1;
                    // calcul et application stats complexe
                    if (jetD == 1) {
                        // D : 3 * na * element degat instantané
                        int D = 3 * na * element;
//                        System.out.println("D :" + D + "dégat explosif instantané");
//                        statsComplexe.set(i, ("+" + D + "Dei"));
                        statsComplexe += ("+" + D + " Dei,");
                    } else if (jetD == 2){
                        // D : 2 * na * element dégat poison pendant 3 tours
                        int D = 2 * na * element;
//                        System.out.println("D :" + D + "dégat poison/3t");
//                        statsComplexe.set(i, (D + "Dp/3t"));
                        statsComplexe += ("+" + D + " Dp/3t,");
                    } else {
                        // D : +1 * na * element degat supplementaire adaptatif
                        int D = element * na;
//                        System.out.println("D :+" + D + "dégat supp adaptatif");
//                        statsComplexe.set(i, ("+" + D + "Dsa"));
                        statsComplexe += ("+" + D + " Dsa,");
                    }
                } else if (i == 3) {
                    // index 4 : Re : 2 choix possible
                    jetRe = random.nextInt(2) + 1;
                    // calcul et application stats complexe
                    if (jetRe == 1){
                        // Re : 1 * na* element armure physique
                        int Re = na * element;
//                        System.out.println("Re :+" + Re + "armure physique");
//                        statsComplexe.set(i, ("+" + Re + "Ap"));
                        statsComplexe += ("+" + Re + " Ap,");
                    } else{
                        // Re : 1 * na * element armure magique
                        int Re = na * element;
//                        System.out.println("Re :" + Re + "armure magique");
//                        statsComplexe.set(i, ("+" + Re + "Am"));
                        statsComplexe += ("+" + Re + " Am,");

                    }
                } else {
                    // index 5 : M : 1 choix possible
                    // jetM = random.nextInt(1) + 1;
                    // M : 0.3 * element mouvement
                    double M = 0.3 * element;
                    M = Math.round(M);
//                    System.out.println("M :+" + M + "mouvement");
//                    statsComplexe.set(i, ("+" + M + "M"));
                    statsComplexe += ("+" + M + " M,");
                }
            }
        }
        statsComplexe = statsComplexe.substring(0, statsComplexe.length() - 1);
//        System.out.println(statsComplexe);
//        System.out.println(statsComplexe);
        return statsComplexe;
    }
    /// Fonction de calcul des stats totaux d'une potion
    /*

    on laisse le choix pour le calcul des stats secondaire de la potion

    on incremente la valeur mais pas le nombre de tours

    Effet saisisable par le joueur.
    -> pop up pour demander si effet ( réponse MJ ) si oui, saisie de texte.
    Vérification pallier pour effet potion par rapport au stats.
    */

    /// fonction de calcul effet stat + multiplicateur taille
}
