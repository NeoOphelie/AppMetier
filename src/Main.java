import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class Main {
    // variable global, id & nom perso, niveau alchimie
    public static int perso_id;
    public static int perso_na;
    public static int perso_nf;
    public static String perso_nom;
    public static String perso_rang;
    public static boolean characterSelected;

    public static void main(String[] args) {
        // Appel popUp de selection de joueur
        popUpSelectionJoueur();
    }

    private static void popUpSelectionJoueur() {
        // Fenêtre de selection joueur
        JFrame frame = new JFrame("Sélection de personnage");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Ferme la popup, ne termine pas l'application
        frame.setPreferredSize(new Dimension(400, 300));
        PopUpJoueur popUpjoueur = new PopUpJoueur();
        frame.getContentPane().add(popUpjoueur);
        frame.pack();
        frame.setLocationRelativeTo(null); // Centre la popup sur l'écran
        frame.setVisible(true);
        // boucle pour attendre le choix de l'utilisateur
        characterSelected = false;
        while (!characterSelected) {
            // attente de l'utilisateur
            if (perso_nom != null && perso_id != 0) {
                characterSelected = true; // choix du personnage fait
                // fermeture automatique de la fenêtre
                frame.dispose();
            }
            try {
                Thread.sleep(100); // temps d'attente entre vérification
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // lancement interface quand perso sélectionné
        lancementInterface();
    }

    private static void lancementInterface() {
        SwingUtilities.invokeLater(() -> {
            InterfaceGraphique interfaceGraphique = null;
            try {
                interfaceGraphique = new InterfaceGraphique();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            interfaceGraphique.updateText(perso_nom, perso_na, perso_rang);
        });
    }
}
