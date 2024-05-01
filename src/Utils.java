import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class Utils {

    public static JTextField createNumericField() {
        JTextField textField = new JTextField();
        textField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE))) {
                    e.consume();
                }
            }
        });
        return textField;
    }
    public static GridBagConstraints createConstraints(int gridx, int gridy, int gridwidth, int gridheight, int anchor, int fill, double weightx, double weighty) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = gridx;
        constraints.gridy = gridy;
        constraints.gridwidth = gridwidth;
        constraints.gridheight = gridheight;
        constraints.anchor = anchor;
        constraints.fill = fill;
        constraints.insets = new Insets(5, 5, 5, 5); // Espacement entre les composants
        constraints.weightx = weightx;
        constraints.weighty = weighty; // Poids de l'espace restant (verticale)
        return constraints;
    }
    /**
     * Renvoie la liste des mortalités disponibles
     */
    static String getMortaliteString(String mortalite) {
        switch (mortalite) {
            case "mortel":
                return "('M')";
            case "surhumain":
                return "('M', 'S')";
            case "immortel":
                return "('M 'S', 'I')";
            default:
                return "('M', 'S', 'I', 'D')";
        }
    }
    /**
     * Renvoie la liste des raretés disponibles
     */
    static String getRangString(int na) {
        switch (na) {
            case 1:
                return "('C')";
            case 2:
                return "('C', 'R')";
            case 3:
                return "('C', 'R', 'TR')";
            case 4:
                return "('C', 'R', 'TR', 'M')";
            default:
                return "('C', 'R', 'TR', 'M', 'L')";
        }
    }
    public static void mettreAJourListe(JComboBox<String> comboBox, List<String> nouvellesDonnees) {
        if (comboBox != null){
            comboBox.removeAllItems(); // Supprimer les éléments actuels de la JComboBox
        }
        for (String newData : nouvellesDonnees) {
            comboBox.addItem(newData); // Ajouter les nouvelles données à la JComboBox
        }
    }
}
