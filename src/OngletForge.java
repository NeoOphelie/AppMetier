import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;
import java.util.List;

public class OngletForge extends JPanel {

    private String[] armeOptions;
    private String[] minerai1Options;
    private String[] minerai2Options;
    private String[] mineraiOptions;
    private JComboBox<String> mineraiComboBox = new JComboBox<>();
    private JComboBox<String> minerai1ComboBox = new JComboBox<>();
    private JComboBox<String> minerai2ComboBox = new JComboBox<>();


    public OngletForge() {
        setLayout(new GridBagLayout());
        refreshBox();
        //________________________________________________________________________________________________Partie LOOT
        // Label texte
        JLabel titreMinage = new JLabel("Recherche de filon");
        GridBagConstraints titreMinageConstraints = Utils.createConstraints(0, 0, 4, 1, GridBagConstraints.NORTH, GridBagConstraints.NONE, 0.0,0.0);
        add(titreMinage, titreMinageConstraints);

        // Label texte
        JLabel minageLabel = new JLabel("Choisissez la quantité à récupérer et la localisation");
        GridBagConstraints minageLabelConstraints = Utils.createConstraints(0, 1, 3, 1, GridBagConstraints.NORTH, GridBagConstraints.NONE, 0.0,0.0);
        add(minageLabel, minageLabelConstraints);

        // Champ de saisie pour la quantité
        JTextField quantiteMinageField = Utils.createNumericField();
        quantiteMinageField.setColumns(5); // Limite de 5 caractères
        GridBagConstraints quantiteMinageFieldConstraints = Utils.createConstraints(1, 2, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.VERTICAL, 0.0,0.0);
        add(quantiteMinageField, quantiteMinageFieldConstraints);

        // Liste de choix pour la localisation
        String[] localisationOptions = {"Cercle"};
        JComboBox<String> localisationComboBox = new JComboBox<>(localisationOptions);
        localisationComboBox.setEditable(true);
        localisationComboBox.setMaximumRowCount(2);
        localisationComboBox.setPrototypeDisplayValue("xxxxxxxxxxxxxxxxxxxx"); // Limite de 20 caractères visibles
        GridBagConstraints localisationConstraints = Utils.createConstraints(0, 2, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,0.0, 0.0);
        add(localisationComboBox, localisationConstraints);

        // Bouton "Loot"
        JButton lootButton = new JButton("Miner");
        GridBagConstraints lootButtonConstraints = Utils.createConstraints(2, 2, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.NONE, 0.0,0.0);
        add(lootButton, lootButtonConstraints);

        // Création d'un séparateur
        JSeparator separatorMinage = new JSeparator(JSeparator.HORIZONTAL);
        add(separatorMinage, Utils.createConstraints(0, 3, 3, 1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, 0.0,0.0));

        //________________________________________________________________________________________________Partie ALLIAGE
        // Label texte
        JLabel titreAlliage = new JLabel("Fondre vos minerais");
        GridBagConstraints titreAlliageConstraints = Utils.createConstraints(0, 4, 3, 1, GridBagConstraints.NORTH, GridBagConstraints.NONE, 0.0,0.0);
        add(titreAlliage, titreAlliageConstraints);

        // Label texte
        JLabel alliageLabel = new JLabel("Choisissez les 2 minerais et la quantité ");
        GridBagConstraints alliageLabelConstraints = Utils.createConstraints(0, 5, 3, 1, GridBagConstraints.NORTH, GridBagConstraints.NONE, 0.0,0.0);
        add(alliageLabel, alliageLabelConstraints);

        // Liste de choix pour le minerai 1
        minerai1ComboBox.setEditable(true);
        minerai1ComboBox.setMaximumRowCount(5);
        minerai1ComboBox.setPrototypeDisplayValue("xxxxxxxxxxxxxxxxxxxx"); // Limite de 20 caractères visibles
        GridBagConstraints minerai1ComboBoxConstraints = Utils.createConstraints(0, 6, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,0.0, 0.0);
        add(minerai1ComboBox, minerai1ComboBoxConstraints);

        // Liste de choix pour le minerai 2
        minerai2ComboBox.setEditable(true);
        minerai2ComboBox.setMaximumRowCount(5);
        minerai2ComboBox.setPrototypeDisplayValue("xxxxxxxxxxxxxxxxxxxx"); // Limite de 20 caractères visibles
        GridBagConstraints minerai2ComboBoxConstraints = Utils.createConstraints(0, 7, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,0.0, 0.0);
        add(minerai2ComboBox, minerai2ComboBoxConstraints);

        // Champ de saisie pour la quantité
        JTextField quantiteAlliageField = Utils.createNumericField();
        quantiteAlliageField.setColumns(5); // Limite de 5 caractères
        GridBagConstraints quantiteAlliageFieldConstraints = Utils.createConstraints(1, 7, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.VERTICAL, 0.0,0.0);
        add(quantiteAlliageField, quantiteAlliageFieldConstraints);

        // Bouton "Fondre"
        JButton fondreButton = new JButton("Fondre");
        GridBagConstraints fondreButtonConstraints = Utils.createConstraints(2, 7, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.NONE, 0.0,0.0);
        add(fondreButton, fondreButtonConstraints);

        // Création d'un séparateur
        JSeparator separatorAlliage = new JSeparator(JSeparator.HORIZONTAL);
        add(separatorAlliage, Utils.createConstraints(0, 8, 3, 1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, 0.0,0.0));

        //________________________________________________________________________________________________Partie Forge
        // Label texte
        JLabel titreForge = new JLabel("Forger vos armes et armures");
        GridBagConstraints titreForgeConstraints = Utils.createConstraints(0, 9, 3, 1, GridBagConstraints.NORTH, GridBagConstraints.NONE, 0.0,0.0);
        add(titreForge, titreForgeConstraints);

        // Label texte
        JLabel forgeLabel = new JLabel("Choisissez l'objet à forger et son minerai");
        GridBagConstraints forgeLabelConstraints = Utils.createConstraints(0, 10, 3, 1, GridBagConstraints.NORTH, GridBagConstraints.NONE, 0.0,0.0);
        add(forgeLabel, forgeLabelConstraints);

        // Liste de choix pour le type d'arme
        String[] typeArmeOptions = {"Une Main", "Deux Main", "Archerie", "Armure"};
        JComboBox<String> typeArmeComboBox = new JComboBox<>(typeArmeOptions);
        typeArmeComboBox.setEditable(true);
        typeArmeComboBox.setMaximumRowCount(5);
        typeArmeComboBox.setPrototypeDisplayValue("xxxxxxxxxxxxxxxxxxxx"); // Limite de 20 caractères visibles
        GridBagConstraints typeArmeComboBoxConstraints = Utils.createConstraints(0, 11, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,0.0, 0.0);
        add(typeArmeComboBox, typeArmeComboBoxConstraints);

        // Liste de choix pour le choix d'arme
        armeOptions = Bdd.getAllArme((String) typeArmeComboBox.getSelectedItem()).toArray(new String[0]);
        JComboBox<String> armeComboBox = new JComboBox<>(armeOptions);
        armeComboBox.setEditable(true);
        armeComboBox.setMaximumRowCount(5);
        armeComboBox.setPrototypeDisplayValue("xxxxxxxxxxxxxxxxxxxx"); // Limite de 20 caractères visibles
        GridBagConstraints armeComboBoxConstraints = Utils.createConstraints(0, 12, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,0.0, 0.0);
        add(armeComboBox, armeComboBoxConstraints);

        // Liste de choix pour le minerai
        mineraiComboBox.setEditable(true);
        mineraiComboBox.setMaximumRowCount(5);
        mineraiComboBox.setPrototypeDisplayValue("xxxxxxxxxxxxxxxxxxxx"); // Limite de 20 caractères visibles
        GridBagConstraints mineraiComboBoxConstraints = Utils.createConstraints(0, 13, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,0.0, 0.0);
        add(mineraiComboBox, mineraiComboBoxConstraints);

        // Bouton "Forger"
        JButton forgeButton = new JButton("Forger");
        GridBagConstraints forgeButtonConstraints = Utils.createConstraints(2, 13, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.NONE, 0.0,0.0);
        add(forgeButton, forgeButtonConstraints);

        // Création d'un séparateur
        JSeparator separatorForge = new JSeparator(JSeparator.HORIZONTAL);
        add(separatorForge, Utils.createConstraints(0, 14, 3, 1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, 0.0,1.0));

        typeArmeComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    String selectedTypeArme = (String) typeArmeComboBox.getSelectedItem();
                    armeOptions = Bdd.getAllArme(selectedTypeArme).toArray(new String[0]);
                    Utils.mettreAJourListe(armeComboBox, List.of(armeOptions));
                }
            }
        });

        // Action du bouton Loot : tirage aléatoire des plantes correspondantes et enregistrement.
        lootButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedLocalisation = (String) localisationComboBox.getSelectedItem();
                String quantiteSaisie = quantiteMinageField.getText();
                int quantite = 0; // Initialisez la quantité à 0 par défaut
                int quantite_possede = 0;
                int max_possession = 100;
                try {
                    quantite_possede = Bdd.getNombreMineraiJoueur(Main.perso_id);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                try {
                    quantite = Integer.parseInt(quantiteSaisie);
                } catch (NumberFormatException ex) {
                    // La quantité saisie n'est pas un nombre valide
                }
                if (selectedLocalisation != null && !selectedLocalisation.equals("empty") && quantite > 0) {
                    if ((quantite_possede + quantite > max_possession) && (quantite_possede < max_possession)){
                        quantite = max_possession - quantite_possede;
                        try {
                            SystemeForge.lootMinerai(Main.perso_id, Main.perso_rang, quantite, selectedLocalisation);
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(OngletForge.this, "Erreur lors de l'enregistrement");
                            throw new RuntimeException(ex);
                        }
                        JOptionPane.showMessageDialog(OngletForge.this, "Loot effectué!\n Inventaire plein.");
                    } else if (quantite_possede >= max_possession) {
                        JOptionPane.showMessageDialog(OngletForge.this, "Inventaire déjà plein.");
                    }else {
                        try {
                            SystemeForge.lootMinerai(Main.perso_id, Main.perso_rang, quantite, selectedLocalisation);
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(OngletForge.this, "Erreur lors de l'enregistrement");
                            throw new RuntimeException(ex);
                        }
                        JOptionPane.showMessageDialog(OngletForge.this, "Loot effectué!");
                    }
//                remplirListModel();
                } else {
                    JOptionPane.showMessageDialog(OngletForge.this, "Veuillez saisir une quantité positive !");
                }
                refreshBox();
            }
        });
    }

    public void refreshBox() {
        minerai1Options = Bdd.getAllMineraiFromJoueur("divin", Main.perso_id, true).toArray(new String[0]);
        minerai2Options = Bdd.getAllMineraiFromJoueur("divin", Main.perso_id, true).toArray(new String[0]);
        mineraiOptions = Bdd.getAllMineraiFromJoueur("divin", Main.perso_id, true).toArray(new String[0]);

        Utils.mettreAJourListe(minerai1ComboBox, List.of(minerai1Options));
        Utils.mettreAJourListe(minerai2ComboBox, List.of(minerai2Options));
        Utils.mettreAJourListe(mineraiComboBox, List.of(mineraiOptions));
    }
}