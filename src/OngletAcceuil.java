import javax.swing.*;
import java.awt.*;

public class OngletAcceuil extends JPanel {
    public OngletAcceuil() {
        // Texte d'acceuil
        JLabel textAcceuillabel = new JLabel("<html><center>" +
                "<br><br><p>Bienvenue sur l'application métier de l'Aranor d'Huun<br><br></p>" +
                "<p>Ici, vous allez perfectionner vos compétences d'Alchimiste et de Forgeron.</p><br>" +
                "<p>Vous trouverez dans votre sac l'ensemble de vos plantes, potions, minerais, armes et armures. <br>" +
                "L'onglet d'Alchimie permet de trouver des plantes et de synthétiser des potions.<br>" +
                "La forge permet de fondre vos minerais et forger des armes ou armures.<br><br></p>" +
                "<p>Les boutons <em>Refresh</em> permettent de mettre à jour les données dans les tableaux,<br>" +
                "notamment après l'utilisation de la forge, de l'alambic ou après avoir trouvé des minerais ou végétaux.</p>" +
                "</center></html>");
        GridBagConstraints textAcceuilConstraints = Utils.createConstraints(0, 0, 2, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, 0.0, 0.0);
        add(textAcceuillabel, textAcceuilConstraints);




    }
}