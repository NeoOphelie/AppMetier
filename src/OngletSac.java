import org.w3c.dom.ls.LSOutput;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.sql.SQLException;

public class OngletSac extends JPanel {
    public static Object[][] data_potion;
    public static Object[][] data_plante;
    public static Object[][] data_minerai;
    public static Object[][] data_forge;

    private JTable tablePotion;
    private JTable tablePlante;
    private JTable tableMinerai;
    private JTable tableForge;
    private TableModel modelPlante;
    private TableModel modelMinerai;
    private TableModel modelPotion;
    private TableModel modelForge;

    public OngletSac() throws SQLException {
        setLayout(new GridBagLayout());

        /* --------------------------------------------TABLEAU PLANTE ------------------------------------------------*/
        JLabel label_plante = new JLabel("Vos Plantes");
        label_plante.setHorizontalAlignment(SwingConstants.CENTER);
        GridBagConstraints labelConstraints_plante = Utils.createConstraints(0, 0, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, 1.0,0.0);
        add(label_plante, labelConstraints_plante);

        // Data pour tableau plante
        data_plante = Bdd.getDataForTableSac("table_sac_plante", Main.perso_id, Main.perso_na);

        // Tableau plante
        modelPlante = new TableModel(new String[]{"Qt", "Nom", "R", "Statistiques"}, data_plante);
        tablePlante = new JTable(modelPlante);
        tablePlante.setName("table_Plante");
        AjustementGraphiqueTable(tablePlante);

        JScrollPane scrollPanePlante = new JScrollPane(tablePlante);
        scrollPanePlante.setSize(new Dimension(800, 200));
        scrollPanePlante.setAlignmentX(Component.CENTER_ALIGNMENT); // Centrer horizontalement
        scrollPanePlante.setAlignmentY(Component.TOP_ALIGNMENT); // Aligner en haut

        add(scrollPanePlante, Utils.createConstraints(0, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 0.0,1.0));

        /* -------------------------------------------TABLEAU MINERAI ------------------------------------------------*/
        JLabel label_minerai = new JLabel("Vos Minerais");
        label_minerai.setHorizontalAlignment(SwingConstants.CENTER);
        GridBagConstraints labelConstraints_minerai = Utils.createConstraints(1, 0, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, 1.0,0.0);
        add(label_minerai, labelConstraints_minerai);

        // Data pour tableau plante
        data_minerai = Bdd.getDataForTableSac("table_sac_minerai", Main.perso_id, Main.perso_na);

        // Tableau plante
        modelMinerai = new TableModel(new String[]{"Qt", "Nom", "R", "Statistiques", "Passif"}, data_minerai);
        tableMinerai = new JTable(modelMinerai);
        tableMinerai.setName("table_Minerai");
        AjustementGraphiqueTable(tableMinerai);

        JScrollPane scrollPaneMinerai = new JScrollPane(tableMinerai);
        scrollPaneMinerai.setSize(new Dimension(800, 200));
        scrollPaneMinerai.setAlignmentX(Component.CENTER_ALIGNMENT); // Centrer horizontalement
        scrollPaneMinerai.setAlignmentY(Component.TOP_ALIGNMENT); // Aligner en haut

        add(scrollPaneMinerai, Utils.createConstraints(1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH,0.0, 1.0));

        /* --------------------------------------------TABLEAU POTION------------------------------------------------*/
        JLabel label_potion = new JLabel("Vos Potions");
        GridBagConstraints labelConstraints_potion = Utils.createConstraints(0, 2, 2, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, 0.0,.0);
        add(label_potion, labelConstraints_potion);

        // Data pour tableau potion
        data_potion = Bdd.getDataForTableSac("table_sac_potion", Main.perso_id, Main.perso_na);

        // Tableau potion
        modelPotion = new TableModel(new String[]{"id", "Qt", "Nom", "Effet", "Statistiques", "Composition"}, data_potion);
        tablePotion = new JTable(modelPotion);
        tablePotion.setName("table_Potion");
        AjustementGraphiqueTable(tablePotion);

        JScrollPane scrollPanePotion = new JScrollPane(tablePotion);
        // scrollPanePotion.setPreferredSize(new Dimension(900, 180));
        scrollPanePotion.setAlignmentX(Component.CENTER_ALIGNMENT); // Centrer horizontalement
        scrollPanePotion.setAlignmentY(Component.TOP_ALIGNMENT); // Aligner en haut

        add(scrollPanePotion, Utils.createConstraints(0, 3, 2, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 0,1.0));

        /* --------------------------------------------TABLEAU POTION------------------------------------------------*/
        JLabel label_forge = new JLabel("Vos Armes et Armures");
        GridBagConstraints labelConstraints_forge = Utils.createConstraints(0, 4, 2, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, 0,.0);
        add(label_forge, labelConstraints_forge);

        // Data pour tableau potion
        data_forge = Bdd.getDataForTableSac("table_sac_potion", Main.perso_id, Main.perso_na);

        // Tableau potion
        modelForge = new TableModel(new String[]{"id", "Qt", "Nom", "Effet", "Statistiques", "Composition"}, data_forge);
        tableForge = new JTable(modelForge);
        tableForge.setName("table_Forge");
        AjustementGraphiqueTable(tableForge);

        JScrollPane scrollPaneForge = new JScrollPane(tableForge);
        // scrollPanePotion.setPreferredSize(new Dimension(900, 180));
        scrollPaneForge.setAlignmentX(Component.CENTER_ALIGNMENT); // Centrer horizontalement
        scrollPaneForge.setAlignmentY(Component.TOP_ALIGNMENT); // Aligner en haut

        add(scrollPaneForge, Utils.createConstraints(0, 5, 2, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 0,1.0));
    }

    public void refreshData() {
        // Mettez à jour les données des tableaux ici
        data_potion = Bdd.getDataForTableSac("table_sac_potion", Main.perso_id, Main.perso_na);
        data_plante = Bdd.getDataForTableSac("table_sac_plante", Main.perso_id, Main.perso_na);
        data_minerai = Bdd.getDataForTableSac("table_sac_minerai", Main.perso_id, Main.perso_na);
        data_forge = Bdd.getDataForTableSac("table_sac_plante", Main.perso_id, Main.perso_na);
        modelPotion.setData(data_potion);
        modelPlante.setData(data_plante);
        modelMinerai.setData(data_minerai);
    }

    private void AjustementGraphiqueTable(JTable table) {
        TableColumnModel columnModel = table.getColumnModel();
        // changement largeur colonne Rang et quantité
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            String columnName = (String) table.getColumnModel().getColumn(i).getIdentifier();
            if ("id".equals(columnName)) {
                columnModel.getColumn(i).setMaxWidth(35);
            } else if ("Qt".equals(columnName)) {
                columnModel.getColumn(i).setMaxWidth(30);
            } else if ("R".equals(columnName)) {
                columnModel.getColumn(i).setMaxWidth(30);
            }// Ajoutez d'autres conditions pour d'autres colonnes si nécessaire
        }
    }
}