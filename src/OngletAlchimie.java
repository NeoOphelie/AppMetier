import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class OngletAlchimie extends JPanel {
    private List<String> listeSelection;
    private JList<String> multiSelectList;
    private JScrollPane listScrollPane;
    private MultiSelectListDemo boxMultiSelect;

    public OngletAlchimie() {

        setLayout(new GridBagLayout());

        //________________________________________________________________________________________________Partie LOOT

        // Label texte
        JLabel TitreLoot = new JLabel("Cueillir des fleurs");
        GridBagConstraints TitreLootConstraints = Utils.createConstraints(0, 0, 3, 1, GridBagConstraints.NORTH, GridBagConstraints.NONE, 0.0,0.0);
        add(TitreLoot, TitreLootConstraints);

        // Label texte
        JLabel lootLabel = new JLabel("Choisissez la quantité à récupérer et la localisation");
        GridBagConstraints lootLabelConstraints = Utils.createConstraints(0, 1, 3, 1, GridBagConstraints.NORTH, GridBagConstraints.NONE, 0.0,0.0);
        add(lootLabel, lootLabelConstraints);

        // Champ de saisie pour la quantité
        JTextField quantiteField = Utils.createNumericField();
        quantiteField.setColumns(5); // Limite de 5 caractères
        GridBagConstraints quantiteConstraints = Utils.createConstraints(1, 2, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.VERTICAL, 0.0,0.0);
        add(quantiteField, quantiteConstraints);

        // Liste de choix pour la localisation
        String[] localisationOptions = {"Cercle"};
        JComboBox<String> localisationComboBox = new JComboBox<>(localisationOptions);
        localisationComboBox.setEditable(true);
        localisationComboBox.setMaximumRowCount(2);
        localisationComboBox.setPrototypeDisplayValue("xxxxxxxxxxxxxxxxxxxx"); // Limite de 20 caractères visibles
        GridBagConstraints localisationConstraints = Utils.createConstraints(0, 2, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,0.0, 0.0);
        add(localisationComboBox, localisationConstraints);

        // Bouton "Loot"
        JButton lootButton = new JButton("Cueillir");
        GridBagConstraints lootButtonConstraints = Utils.createConstraints(2, 2, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.NONE, 0.0,0.0);
        add(lootButton, lootButtonConstraints);

        // Création d'un séparateur
        JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
        add(separator, Utils.createConstraints(0, 3, 3, 1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, 0.0,0.0));

        //________________________________________________________________________________________________Partie Synthèse

        // Texte "Création de potion"
        JLabel TitreSynthese = new JLabel("Synthétiser des Potions");
        GridBagConstraints TitreSyntheseConstraints = Utils.createConstraints(0, 4, 3, 1, GridBagConstraints.NORTH, GridBagConstraints.NONE, 0.0,0.0);
        add(TitreSynthese, TitreSyntheseConstraints);

        // Texte "Création de potion"
        String labelText; // Déclarez la variable en dehors des blocs if et else
        if (Main.perso_na <= 3) {
            labelText = String.format("Choix de la composition : sélectionnez exactement %d plantes", 3);
        } else {
            labelText = String.format("Choix de la composition : sélectionnez exactement %d plantes", Main.perso_na);
        }
        JLabel creationLabel = new JLabel(labelText); // Utilisez la variable pour créer le JLabel
        GridBagConstraints creationLabelConstraints = Utils.createConstraints(0, 5, 4, 1, GridBagConstraints.NORTH, GridBagConstraints.NONE, 0.0, 0.0);
        add(creationLabel, creationLabelConstraints);

        // Calcul du nombre d'éléments sélectionnables
        int emplacement_composition = Math.max(Main.perso_na, 3);

        // Créer la liste de sélection multiple avec limite d'éléments sélectionnables
        boxMultiSelect = new MultiSelectListDemo(emplacement_composition);
        multiSelectList = boxMultiSelect.getMultiSelectList();
        listScrollPane = new JScrollPane(multiSelectList);

        GridBagConstraints listConstraints = Utils.createConstraints(0, 6, 3, 1, GridBagConstraints.NORTH, GridBagConstraints.BOTH, .0,1.0);
        add(listScrollPane, listConstraints);

        remplirListModel();

        //------------------------------------------------------------------

        // Label texte
        JLabel PlanteTexte1 = new JLabel("");
//        PlanteTexte.setSize(300,60);
        GridBagConstraints PlanteTexteConstraints1 = Utils.createConstraints(0, 7, 3, 1, GridBagConstraints.NORTH, GridBagConstraints.NONE, 0.0,0.0);
        add(PlanteTexte1, PlanteTexteConstraints1);
        JLabel PlanteTexte2 = new JLabel("");
//        PlanteTexte.setSize(300,60);
        GridBagConstraints PlanteTexteConstraints2 = Utils.createConstraints(0, 8, 3, 1, GridBagConstraints.NORTH, GridBagConstraints.NONE, 0.0,0.0);
        add(PlanteTexte2, PlanteTexteConstraints2);

        // Bouton synthétiser
        JButton SyntheseButton = new JButton("Synthèse");
        GridBagConstraints SyntheseButtonConstraints = Utils.createConstraints(1, 9, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.NONE, 0.0,1.0);
        add(SyntheseButton, SyntheseButtonConstraints);

        // Bouton plante
        JButton PlanteButton = new JButton("Plantes");
        GridBagConstraints PlanteButtonConstraints = Utils.createConstraints(2, 9, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.NONE, 0.0,1.0);
        add(PlanteButton, PlanteButtonConstraints);

        // Action du bouton Loot : tirage aléatoire des plantes correspondantes et enregistrement.
        lootButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedValue = (String) localisationComboBox.getSelectedItem();
                String quantiteSaisie = quantiteField.getText();
                int quantite = 0; // Initialisez la quantité à 0 par défaut
                int quantite_possede = 0;
                int max_possession = 60;
                try {
                    quantite_possede = Bdd.getNombrePlanteJoueur(Main.perso_id);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                try {
                    quantite = Integer.parseInt(quantiteSaisie);
                } catch (NumberFormatException ex) {
                    // La quantité saisie n'est pas un nombre valide
                }
                if (selectedValue != null && !selectedValue.equals("empty") && quantite > 0) {
                    if ((quantite_possede + quantite > max_possession) && (quantite_possede < max_possession)){
                        quantite = max_possession - quantite_possede;
                        try {
                            SystemeAlchimie.lootPlante(Main.perso_na, quantite, selectedValue);
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(OngletAlchimie.this, "Erreur lors de l'enregistrement");
                            throw new RuntimeException(ex);
                        }
                        JOptionPane.showMessageDialog(OngletAlchimie.this, "Loot effectué!\n Inventaire plein.");
                    } else if (quantite_possede >= max_possession) {
                        JOptionPane.showMessageDialog(OngletAlchimie.this, "Inventaire déjà plein.");
                    }else {
                        try {
                            SystemeAlchimie.lootPlante(Main.perso_na, quantite, selectedValue);
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(OngletAlchimie.this, "Erreur lors de l'enregistrement");
                            throw new RuntimeException(ex);
                        }
                        JOptionPane.showMessageDialog(OngletAlchimie.this, "Loot effectué!");
                    }
                    remplirListModel();
                } else {
                    JOptionPane.showMessageDialog(OngletAlchimie.this, "Veuillez saisir une quantité positive !");
                }
            }
        });

        // Action bouton stats : affichage des plantes sélectionnées et des stats brutes cumulées
        PlanteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Obtenir les éléments sélectionnés dans la JList
                String[] selectedItems = multiSelectList.getSelectedValuesList().toArray(new String[0]);

                // Limitez le nombre d'éléments sélectionnables
                if (selectedItems.length != emplacement_composition) {
                    JOptionPane.showMessageDialog(OngletAlchimie.this, "Veuillez sélectionner exactement " + emplacement_composition + " éléments.");
                } else {
                    // Afficher les plantes sélectionnées
                    String labelText = "";
                    String labelText1 = "Plantes Sélectionnées :";
                    for (String selectedItem : selectedItems) {
                        labelText += selectedItem + ", "; // Ajoutez un élément suivi d'une virgule
                    }
                    labelText = labelText.substring(0, (labelText.length() - 2));
                    // Mise à jour du texte
                    PlanteTexte1.setText(labelText1);
                    PlanteTexte2.setText(labelText);
                }


            }
        });

        // Action du bouton synthèse : craft de la potion :
        //                                  - retirer les plantes sélectionnées
        //                                  - calcule des stats brutes
        //                                  - calcule des stats complexes
        //                                  - lance le choix d'effet spécial (implication du MJ)
        //                                  - enregistrement de la potion
        // Pas d'annulation possible
        SyntheseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Obtenir les éléments sélectionnés dans la JList
                String[] selectedItems = multiSelectList.getSelectedValuesList().toArray(new String[0]);
                List<String> listPlanteSelection = Arrays.asList(selectedItems);
                Collections.sort(listPlanteSelection);
                List<Integer> statsPotionBrute;
                String statsPotionComplexeString;

                // Limitez le nombre d'éléments sélectionnable
                if (selectedItems.length != emplacement_composition) {
                    JOptionPane.showMessageDialog(OngletAlchimie.this, "Veuillez sélectionner exactement " + emplacement_composition + " éléments.");
                }  else {
                    // récupération calcul des statistiques
                    statsPotionBrute = SystemeAlchimie.calculStatistiqueBrute(listPlanteSelection);
                    statsPotionComplexeString = SystemeAlchimie.calculStatistiqueComplexe(Main.perso_na, statsPotionBrute);
                    String statsPotionBruteString = statsPotionBrute.stream()
                            .map(Object::toString)
                            .collect(Collectors.joining(","));
                    String plantePotion = String.join(",", selectedItems);

                    // boucle pour test les stats
//                    for (int i = 0; i < 10 ; i++) {
//                        System.out.println("_______________craft n°"+i+"________________");
//                        // Fonction de calcul des stats Complexe
//                        statsPotionComplexeString = SystemeAlchimie.calculStatistiqueComplexe(Main.perso_na, statsPotionBrute);
//                        System.out.println(statsPotionComplexeString);
//                    }

                    // Boîte de dialogue pour saisir l'effet et le nom
                    JTextField nomField = new JTextField(10);
                    JTextField effetField = new JTextField(10);

                    JPanel inputPanel = new JPanel(new GridLayout(2, 2));
                    inputPanel.add(new JLabel("Nom de la potion:"));
                    inputPanel.add(nomField);
                    inputPanel.add(new JLabel("Effet de la potion:"));
                    inputPanel.add(effetField);

                    int result = JOptionPane.showConfirmDialog(OngletAlchimie.this, inputPanel, "Nouvelle Potion ?",
                            JOptionPane.OK_CANCEL_OPTION);

                    if (result == JOptionPane.OK_OPTION) {
                        // Récupérer les valeurs saisies et changement si nulle
                        String nouveauNom = nomField.getText();
                        String nouvelEffet = effetField.getText();
                        if(nouveauNom.isEmpty()){nouveauNom = "Potion Sans Nom";}

                        try {
                            int id = Bdd.trouverPotionIdStats(nouvelEffet, statsPotionBruteString, statsPotionComplexeString, plantePotion);
                            if (id >= 0) {
                                // Potion qui existe.
                                // Check si le joueur la possède
                                if (Bdd.potionPossedeId(id, Main.perso_id)){
                                    // Si oui incrementer la potion pour le joueur
                                    // prévenir que la potion existe déjà
                                    JOptionPane.showMessageDialog(OngletAlchimie.this, "Vous avez déjà synthétisé cette potion.");
                                    Bdd.ajouterDansTableSacPotion(true, Main.perso_id, id, 1);
                                }else {
                                    // Sinon ajouter la potion pour le joueur
                                    // prévenir que la nouvelle potion est faite
                                    Bdd.ajouterDansTableSacPotion(false, Main.perso_id, id, 1);
                                    JOptionPane.showMessageDialog(OngletAlchimie.this, "Vous avez trouvé une nouvelle potion.");
                                }
                            } else {
                                // Potion qui n'existe pas : ajout dans la table potion
                                int idNouvellePotion = Bdd.enregistrerNouvellePotion(nouveauNom, nouvelEffet, statsPotionBruteString, statsPotionComplexeString, plantePotion, Main.perso_na );
                                // Ajout dans la table sac potion joueur
                                // prévenir que la nouvelle potion est faite
                                Bdd.ajouterDansTableSacPotion(false, Main.perso_id, idNouvellePotion, 1);
                                JOptionPane.showMessageDialog(OngletAlchimie.this, "Vous avez trouvé une nouvelle potion.");
                            }
                            // Suppression des plantes consommées
                            Bdd.ajusterQuantitesSacPlante(Main.perso_id, listPlanteSelection);
                            remplirListModel();
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
            }
        });
    }
    /**
     * Remplissage de la box de selection de craft
     */
    public void remplirListModel() {
        // Obtenez le modèle de liste associé à votre JList
        DefaultListModel<String> listModel = boxMultiSelect.getListModel();
        // Vider la box de selection
        listModel.clear();

        listeSelection = Bdd.getAllPlanteFromJoueur(Main.perso_na, Main.perso_id, false);

        // Ajoutez les éléments de votre listeSelection au modèle de liste
        if (!listeSelection.isEmpty()) {
            // Ajout des éléments dans la box de selection"
            for (String item : listeSelection) {
                listModel.addElement(item);
            }
        }
    }
}
