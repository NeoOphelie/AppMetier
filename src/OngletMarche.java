import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OngletMarche extends JPanel {
    private JComboBox<String> boxPlanteAchat = new JComboBox<>();
    private JComboBox<String> boxPlanteVente = new JComboBox<>();
    private JComboBox<String> boxPotionVente = new JComboBox<>();
    private  JComboBox<String> boxMineraiAchat = new JComboBox<>();
    private  JComboBox<String> boxMineraiVente = new JComboBox<>();
    private  JComboBox<String> boxForgeVente = new JComboBox<>();
    private String[] listPlanteAchat;
    private String[] listPlanteVente;
    private String[] listPotionVente;
    private String[] listMineraiAchat;
    private String[] listMineraiVente;

    public OngletMarche() {
        setLayout(new GridBagLayout());
        refreshBox();

        // ------------------------------------------------------------------------------------------------PARTIE PLANTE
        // Label texte
        JLabel TitrePlante = new JLabel("Boutique du Botaniste");
        GridBagConstraints TitrePlanteConstraints = Utils.createConstraints(0, 0, 4, 1, GridBagConstraints.NORTH, GridBagConstraints.NONE, 0.0,0.0);
        add(TitrePlante, TitrePlanteConstraints);

        // Liste déroulante PLante achat
        boxPlanteAchat.setEditable(true);
        boxPlanteAchat.setMaximumRowCount(7);
        boxPlanteAchat.setPrototypeDisplayValue("UnNomVraimentTrèsLong - UnEffetDePotion - DesStatsDeFou"); // Limite de caractères visibles
        GridBagConstraints boxPlanteAchatConstraints = Utils.createConstraints(0, 1, 2, 1, GridBagConstraints.NORTH, GridBagConstraints.NONE, 0.0, 0);
        add(boxPlanteAchat, boxPlanteAchatConstraints);

        // Saisie quantité
        JTextField quantitePlanteAchat = Utils.createNumericField();
        quantitePlanteAchat.setColumns(5);// Limite de 5 caractères
        GridBagConstraints quantitePlanteAchatConstraints = Utils.createConstraints(2, 1, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.VERTICAL, 0.0,0.0);
        add(quantitePlanteAchat, quantitePlanteAchatConstraints);

        // Bouton Achat
        JButton PlanteAchatButton = new JButton("Achat");
        GridBagConstraints PlanteAchatButtonConstraints = Utils.createConstraints(3, 1, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.NONE, 0.0,0.0);
        add(PlanteAchatButton, PlanteAchatButtonConstraints);

        // Liste déroulante PLante vente
        boxPlanteVente.setEditable(true);
        boxPlanteVente.setMaximumRowCount(7);
        boxPlanteVente.setPrototypeDisplayValue("UnNomVraimentTrèsLong - UnEffetDePotion - DesStatsDeFou"); // Limite de caractères visibles
        GridBagConstraints boxPlanteVenteConstraints = Utils.createConstraints(0, 2, 2, 1, GridBagConstraints.NORTH, GridBagConstraints.NONE, 0.0, 0);
        add(boxPlanteVente, boxPlanteVenteConstraints);

        // Saisie quantité
        JTextField quantitePlanteVente = Utils.createNumericField();
        quantitePlanteVente.setColumns(5); // Limite de 5 caractères
        GridBagConstraints quantitePlanteVenteConstraints = Utils.createConstraints(2, 2, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.VERTICAL, 0.0,0.0);
        add(quantitePlanteVente, quantitePlanteVenteConstraints);

        // Bouton Achat
        JButton PlanteVenteButton = new JButton("Vente");
        GridBagConstraints PlanteVenteButtonConstraints = Utils.createConstraints(3, 2, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.NONE, 0.0,0.0);
        add(PlanteVenteButton, PlanteVenteButtonConstraints);

        // Création d'un séparateur
        JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
        add(separator, Utils.createConstraints(0, 3, 4, 1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, 0.0,0.0));

        // ------------------------------------------------------------------------------------------------PARTIE POTION
        // Label texte
        JLabel TitrePotion = new JLabel("Boutique de l'Apothicaire");
        GridBagConstraints TitrePotionConstraints = Utils.createConstraints(0, 4, 4, 1, GridBagConstraints.NORTH, GridBagConstraints.NONE, 0.0,0.0);
        add(TitrePotion, TitrePotionConstraints);

        // Liste déroulante Potion vente
        boxPotionVente.setEditable(true);
        boxPotionVente.setMaximumRowCount(7);
        boxPotionVente.setPrototypeDisplayValue("UnNomVraimentTrèsLong - UnEffetDePotion - DesStatsDeFou"); // Limite de caractères visibles
        GridBagConstraints boxPotionVenteConstraints = Utils.createConstraints(0, 5, 2, 1, GridBagConstraints.NORTH, GridBagConstraints.NONE, 0.0, 0);
        add(boxPotionVente, boxPotionVenteConstraints);

        // Saisie quantité
        JTextField quantitePotionVente = Utils.createNumericField();
        quantitePotionVente.setColumns(5); // Limite de 5 caractères
        GridBagConstraints quantitePotionVenteConstraints = Utils.createConstraints(2, 5, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.VERTICAL, 0.0,0.0);
        add(quantitePotionVente, quantitePotionVenteConstraints);

        // Bouton Achat
        JButton PotionVenteButton = new JButton("Vente");
        GridBagConstraints PotionVenteButtonConstraints = Utils.createConstraints(3, 5, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.NONE, 0.0,0.0);
        add(PotionVenteButton, PotionVenteButtonConstraints);

        // Création d'un séparateur
        JSeparator separatorPotion = new JSeparator(JSeparator.HORIZONTAL);
        add(separatorPotion, Utils.createConstraints(0, 6, 4, 1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, 0.0,0.0));

        // -----------------------------------------------------------------------------------------------PARTIE MINERAI
        // Label texte
        JLabel TitreMinerai = new JLabel("Boutique du Forgeron");
        GridBagConstraints TitreMineraiConstraints = Utils.createConstraints(0, 7, 4, 1, GridBagConstraints.NORTH, GridBagConstraints.NONE, 0.0,0.0);
        add(TitreMinerai, TitreMineraiConstraints);

        // Liste déroulante Minerai achat
        boxMineraiAchat.setEditable(true);
        boxMineraiAchat.setMaximumRowCount(7);
        boxMineraiAchat.setPrototypeDisplayValue("UnNomVraimentTrèsLong - UnEffetDePotion - DesStatsDeFou"); // Limite de caractères visibles
        GridBagConstraints boxMineraiAchatConstraints = Utils.createConstraints(0, 8, 2, 1, GridBagConstraints.NORTH, GridBagConstraints.NONE, 0.0, 0);
        add(boxMineraiAchat, boxMineraiAchatConstraints);

        // Saisie quantité
        JTextField quantiteMineraiAchat =  Utils.createNumericField();
        quantiteMineraiAchat.setColumns(5); // Limite de 5 caractères
        GridBagConstraints quantiteMineraiAchatConstraints = Utils.createConstraints(2, 8, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.VERTICAL, 0.0,0.0);
        add(quantiteMineraiAchat, quantiteMineraiAchatConstraints);

        // Bouton Achat
        JButton MineraiAchatButton = new JButton("Achat");
        GridBagConstraints MineraiAchatButtonConstraints = Utils.createConstraints(3, 8, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.NONE, 0.0,0.0);
        add(MineraiAchatButton, MineraiAchatButtonConstraints);

        // Liste déroulante Minerai vente
        boxMineraiVente.setEditable(true);
        boxMineraiVente.setMaximumRowCount(7);
        boxMineraiVente.setPrototypeDisplayValue("UnNomVraimentTrèsLong - UnEffetDePotion - DesStatsDeFou"); // Limite de caractères visibles
        GridBagConstraints boxMineraiVenteConstraints = Utils.createConstraints(0, 9, 2, 1, GridBagConstraints.NORTH, GridBagConstraints.NONE, 0.0, 0);
        add(boxMineraiVente, boxMineraiVenteConstraints);

        // Saisie quantité
        JTextField quantiteMineraiVente = Utils.createNumericField();
        quantiteMineraiVente.setColumns(5); // Limite de 5 caractères
        GridBagConstraints quantiteMineraiVenteConstraints = Utils.createConstraints(2, 9, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.VERTICAL, 0.0,0.0);
        add(quantiteMineraiVente, quantiteMineraiVenteConstraints);

        // Bouton Achat
        JButton MineraiVenteButton = new JButton("Vente");
        GridBagConstraints MineraiVenteButtonConstraints = Utils.createConstraints(3, 9, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.NONE, 0.0,0.0);
        add(MineraiVenteButton, MineraiVenteButtonConstraints);

        // Création d'un séparateur
        JSeparator separator_minerai = new JSeparator(JSeparator.HORIZONTAL);
        add(separator_minerai, Utils.createConstraints(0, 10, 4, 1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, 0.0,1.0));

        // Action bouton Achat Plante : ajouter la quantité au sac. Sans limite d'inventaire
        PlanteAchatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Obtenir les éléments sélectionnés dans la JList
                String selectedItems = (String) boxPlanteAchat.getSelectedItem();
                // Check si la quantité entrée est valide
                if (quantitePlanteAchat.getText().isEmpty() || (Integer.parseInt(quantitePlanteAchat.getText()) <= 0  )) {
                    JOptionPane.showMessageDialog(OngletMarche.this, "Veuillez saisir une quantité positive.");
                } else {
                    // Ajouter la plante X fois dans le sac.
                    int quantite = Integer.parseInt(quantitePlanteAchat.getText());
                    JOptionPane.showMessageDialog(OngletMarche.this, "Achat effectué");
                    int id = Bdd.getIdPlante(selectedItems);
                    try {
                        Bdd.insertIntoTableSacPlante(id, quantite, Main.perso_id);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                refreshBox();
            }
        });

        // Action bouton Achat Minerai : ajouter la quantité au sac. sans limite d'inventaire
        MineraiAchatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Obtenir les éléments sélectionnés dans la JList
                String selectedItems = (String) boxMineraiAchat.getSelectedItem();
                // Check si la quantité entrée est valide
                if (quantiteMineraiAchat.getText().isEmpty() || (Integer.parseInt(quantiteMineraiAchat.getText()) <= 0  )) {
                    JOptionPane.showMessageDialog(OngletMarche.this, "Veuillez saisir une quantité positive.");
                } else {
                    // Ajouter la plante X fois dans le sac.
                    JOptionPane.showMessageDialog(OngletMarche.this, "Achat effectué");
                    int id = Bdd.getIdMinerai(selectedItems);
                    int quantite = Integer.parseInt(quantiteMineraiAchat.getText());
                    try {
                        Bdd.insertIntoTableSacMinerai(id, quantite, Main.perso_id);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                refreshBox();
            }
        });

        // Action bouton Vente Minerai : enelever la quantité au sac.
        MineraiVenteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Obtenir les éléments sélectionnés dans la JList
                String selectedItems = (String) boxMineraiVente.getSelectedItem();
                // Check si la quantité entrée est valide
                if (quantiteMineraiVente.getText().isEmpty() || (Integer.parseInt(quantiteMineraiVente.getText()) <= 0  )) {
                    JOptionPane.showMessageDialog(OngletMarche.this, "Veuillez saisir une quantité positive.");
                } else {
                    // Ajouter la plante X fois dans le sac.
                    JOptionPane.showMessageDialog(OngletMarche.this, "Vente effectuée");
                    int id = Bdd.getIdMinerai(selectedItems);
                    int quantite = Integer.parseInt(quantiteMineraiVente.getText());
                    Bdd.removeItem("table_sac_minerai",Main.perso_id,id,quantite);
                }
                refreshBox();
            }
        });
        // Action bouton Vente Plante : enelever la quantité au sac.
        PlanteVenteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Obtenir les éléments sélectionnés dans la JList
                String selectedItems = (String) boxPlanteVente.getSelectedItem();
                // Check si la quantité entrée est valide
                if (quantitePlanteVente.getText().isEmpty() || (Integer.parseInt(quantitePlanteVente.getText()) <= 0  )) {
                    JOptionPane.showMessageDialog(OngletMarche.this, "Veuillez saisir une quantité positive.");
                } else {
                    // Ajouter la plante X fois dans le sac.
                    JOptionPane.showMessageDialog(OngletMarche.this, "Vente effectuée");
                    int id = Bdd.getIdPlante(selectedItems);
                    int quantite = Integer.parseInt(quantitePlanteVente.getText());
                    Bdd.removeItem("table_sac_plante",Main.perso_id,id,quantite);
                }
                refreshBox();
            }
        });
        // Action bouton Vente Plante : enelever la quantité au sac.
        PotionVenteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Obtenir les éléments sélectionnés dans la JList
                String selectedItems = (String) boxPotionVente.getSelectedItem();
                // Check si la quantité entrée est valide
                if (quantitePotionVente.getText().isEmpty() || (Integer.parseInt(quantitePotionVente.getText()) <= 0  )) {
                    JOptionPane.showMessageDialog(OngletMarche.this, "Veuillez saisir une quantité positive.");
                } else {
                    // Ajouter la plante X fois dans le sac.
                    JOptionPane.showMessageDialog(OngletMarche.this, "Vente effectuée");
                    int id = findIdInString(selectedItems);
                    int quantite = Integer.parseInt(quantitePotionVente.getText());
                    Bdd.removeItem("table_sac_potion",Main.perso_id,id,quantite);
                }
                refreshBox();
            }
        });
    }

    public void refreshBox() {
        listPlanteAchat = Bdd.getAllPlants().toArray(new String[0]);
        listPlanteVente =  Bdd.getAllPlanteFromJoueur(6, Main.perso_id, true).toArray(new String[0]);
        listPotionVente = Bdd.getAllPotionFromJoueur(Main.perso_id, true).toArray(new String[0]);
        listMineraiAchat = Bdd.getAllMinerai().toArray(new String[0]);
        listMineraiVente = Bdd.getAllMineraiFromJoueur("divin", Main.perso_id, true).toArray(new String[0]);

        Utils.mettreAJourListe(boxPlanteVente, List.of(listPlanteVente));
        Utils.mettreAJourListe(boxPlanteAchat, List.of(listPlanteAchat));
        Utils.mettreAJourListe(boxPotionVente, List.of(listPotionVente));
        Utils.mettreAJourListe(boxMineraiAchat, List.of(listMineraiAchat));
        Utils.mettreAJourListe(boxMineraiVente, List.of(listMineraiVente));
    }

    public static int findIdInString(String texte) {
        Pattern pattern = Pattern.compile("\\(([^)]+)\\)");
        Matcher matcher = pattern.matcher(texte);

        if (matcher.find()) {
            String id = matcher.group(1);
            return Integer.parseInt(id);
        }
        return 0;
    }
}