import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PopUpJoueur extends JPanel {
    public PopUpJoueur() {
        setLayout(new GridBagLayout());

        // Label
        JLabel label = new JLabel("Quel personnage part à l'aventure ?");
        GridBagConstraints labelConstraints = createConstraints(0, 0, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.CENTER, 0.0);
        add(label, labelConstraints);

        // Liste des noms de personnages disponible
        String[] listNom = Bdd.getNomsFromTablePerso().toArray(new String[0]);
        JComboBox<String> NomComboBox = new JComboBox<>(listNom);
        NomComboBox.setEditable(true);
        NomComboBox.setMaximumRowCount(4);
        NomComboBox.setPrototypeDisplayValue("UnNomVraimentTrèsLong"); // Limite de 20 caractères visibles
        GridBagConstraints localisationConstraints = createConstraints(0, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, 0.0);
        add(NomComboBox, localisationConstraints);

        // Bouton "nouveau joueur"
        JButton nouveauJoueurButton = new JButton("Nouveau Joueur");
        GridBagConstraints nouveauJoueurButtonConstraints = createConstraints(0, 2, 1, 1, GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL, 0.0);
        add(nouveauJoueurButton, nouveauJoueurButtonConstraints);

        // Bouton "Partir" pour lancer l'aventure
        JButton partirButton = new JButton("Partir");
        GridBagConstraints partirButtonConstraints = createConstraints(0, 3, 1, 1, GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL, 0.0);
        add(partirButton, partirButtonConstraints);

        // Gestionnaire d'événements pour le bouton "Nouveau Joueur"
        nouveauJoueurButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Boîte de dialogue pour saisir le nouveau joueur
                JTextField nomField = new JTextField(10);
                JTextField niveauField = new JTextField(10);
                JTextField rangField = new JTextField(10);

                // Créez la liste déroulante avec les options
                String[] options = {"mortel", "surhumain", "immortel", "demi-dieu", "divin"};
                JComboBox<String> rangList = new JComboBox<>(options);
                // Créez la liste déroulante avec les options pour forge et alchimiste
                String[] options_alchimiste = {"1","2","3","4","5","6"};
                JComboBox<String> list_options_alchimiste = new JComboBox<>(options_alchimiste);
                String[] options_forge = {"1","2","3","4","5"};
                JComboBox<String> list_options_forge = new JComboBox<>(options_forge);

                // Ajoutez la liste déroulante au panel
                JPanel inputPanel = new JPanel(new GridLayout(4, 2));
                inputPanel.add(new JLabel("Nom du joueur:"));
                inputPanel.add(nomField);
                inputPanel.add(new JLabel("Rang de Mortalité:"));
                inputPanel.add(rangList);
                inputPanel.add(new JLabel("Niveau Alchimiste:"));
                inputPanel.add(list_options_alchimiste);
                inputPanel.add(new JLabel("Niveau Forgerons:"));
                inputPanel.add(list_options_forge);

                int result = JOptionPane.showConfirmDialog(PopUpJoueur.this, inputPanel, "Nouveau Joueur",
                        JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION) {
                    // Récupérer les valeurs saisies
                    String nouveauNom = nomField.getText();
                    String nouveauRang = (String) rangList.getSelectedItem();
                    String nouveauNa = (String) list_options_alchimiste.getSelectedItem();
                    String nouveauNf = (String) list_options_forge.getSelectedItem();

                    // Ajouter le nouveau joueur à la base de données
                    if (((nouveauNom != null) && !nouveauNom.isEmpty()) || ((nouveauRang != null) && !nouveauRang.isEmpty())) {
                        // Appel à la méthode pour ajouter le joueur à la base de données
                        Bdd.ajouterNouveauJoueur(nouveauNom, nouveauNa, nouveauNf,nouveauRang);

                        // Mettre à jour la liste des noms dans la JComboBox
                        String[] listNom = Bdd.getNomsFromTablePerso().toArray(new String[0]);
                        NomComboBox.setModel(new DefaultComboBoxModel<>(listNom));
                    }
                }
            }
        });

        // Gestionnaire d'événements pour le bouton "Partir"
        partirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.perso_nom = (String) NomComboBox.getSelectedItem();
                Main.perso_id = Bdd.getIdFromNom(Main.perso_nom);
                Main.perso_rang = Bdd.getRangPerso(Main.perso_id);
                Main.perso_na = Bdd.getNiveauNaPerso(Main.perso_id);
                Main.perso_nf = Bdd.getNiveauNfPerso(Main.perso_id);

            }
        });

    }

    private GridBagConstraints createConstraints(int gridx, int gridy, int gridwidth, int gridheight, int anchor, int fill, double weighty) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = gridx;
        constraints.gridy = gridy;
        constraints.gridwidth = gridwidth;
        constraints.gridheight = gridheight;
        constraints.anchor = anchor;
        constraints.fill = fill;
        constraints.insets = new Insets(5, 5, 5, 5); // Espacement entre les composants
        constraints.weighty = weighty; // Poids de l'espace restant (verticale)
        return constraints;
    }
}