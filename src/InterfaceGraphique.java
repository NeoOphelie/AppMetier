import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.sql.SQLException;

public class InterfaceGraphique extends JFrame {
    private JPanel bandeauPanel;
    private JLabel texteLabel;

    private JTabbedPane onglets;

    private JPanel ongletSac;
    private JPanel ongletAcceuil;
    private JPanel ongletAlchimie;
    private JPanel ongletForge;
    private JPanel ongletMarche;
    private JPanel ongletSavoir;
    private JPanel ongletParametres;

    public InterfaceGraphique() throws SQLException {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Application Aranor d'Hunn");
        setSize(1000, 900);
        setLayout(new BorderLayout());

        // Création bordereau joueur
        bandeauPanel = new JPanel();
        bandeauPanel.setBackground(Color.GRAY);
        texteLabel = new JLabel();
        texteLabel.setForeground(Color.WHITE);
        bandeauPanel.add(texteLabel);

        add(bandeauPanel, BorderLayout.NORTH);

        // Création des onglets
        ongletAcceuil = new OngletAcceuil();
        ongletSac = new OngletSac();
        ongletAlchimie = new OngletAlchimie();
        ongletForge = new OngletForge();
        ongletMarche = new OngletMarche();
        ongletSavoir = new OngletSavoir();
        ongletParametres = new OngletParametres();


        // Ajout des onglets à l'ongletbedPane
        onglets = new JTabbedPane();
        onglets.addTab("Acceuil", ongletAcceuil);
        onglets.addTab("Sac", ongletSac);
        onglets.addTab("Alchimie", ongletAlchimie);
        onglets.addTab("Forge", ongletForge);
        onglets.addTab("Marché", ongletMarche);
        onglets.addTab("Savoir", ongletSavoir);
        onglets.addTab("Paramètres", ongletParametres);

        // Ajoutez un ChangeListener au JTabbedPane
        onglets.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                ongletChanged();
            }
        });


        add(onglets, BorderLayout.CENTER);

        // centrer la fenetre
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void ongletChanged() {
        // Méthode appelée lorsqu'un changement d'onglet est détecté
        int selectedIndex = onglets.getSelectedIndex();
        if (selectedIndex == 1) {  // Si l'onglet sélectionné est l'onglet "Sac"
            OngletSac ongletSac = (OngletSac) onglets.getComponentAt(selectedIndex);
            ongletSac.refreshData();  // Supposons que vous avez une méthode refreshData dans votre classe OngletSac
        }
        if (selectedIndex == 2) {  // Si l'onglet sélectionné est l'onglet "Alchimie"
            OngletAlchimie ongletAlchimie = (OngletAlchimie) onglets.getComponentAt(selectedIndex);
            ongletAlchimie.remplirListModel();  // Supposons que vous avez une méthode refreshData dans votre classe OngletSac
        }
        if (selectedIndex == 3) {
            OngletForge ongletForge = (OngletForge) onglets.getComponentAt(selectedIndex);
            ongletForge.refreshBox();
        }
        if (selectedIndex == 4) {
            OngletMarche ongletMarche = (OngletMarche) onglets.getComponentAt(selectedIndex);
            ongletMarche.refreshBox();
        }

    }

    // fonction de mise à jour du bordereau joueur
    public void updateText(String var1, int var2, String var3) {
        texteLabel.setText(var1 + ",  Aventurier " + var3 + " de niveau d'alchimiste " + var2);
    }
}
