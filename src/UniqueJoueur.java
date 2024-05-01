public class UniqueJoueur {
    // Attributs
    private String nom;
    private int id;
    private int niveauAlchimiste;
    private int niveauForgeron;

    // Constructeur
    public UniqueJoueur(String nom, int id, int niveauAlchimiste, int niveauForgeron) {
        this.nom = nom;
        this.id = id;
        this.niveauAlchimiste = niveauAlchimiste;
        this.niveauForgeron = niveauForgeron;
    }

    // Méthode pour afficher les détails du joueur
    public void afficherDetails() {
        System.out.println("Nom: " + nom);
        System.out.println("ID: " + id);
        System.out.println("Niveau d'alchimiste: " + niveauAlchimiste);
        System.out.println("Niveau de forgeron: " + niveauForgeron);
    }

    // Méthode pour augmenter le niveau d'alchimiste
    public void augmenterNiveauAlchimiste() {
        niveauAlchimiste++;
        System.out.println("Le niveau d'alchimiste de " + nom + " a été augmenté à " + niveauAlchimiste + ".");
    }

    // Méthode pour augmenter le niveau de forgeron
    public void augmenterNiveauForgeron() {
        niveauForgeron++;
        System.out.println("Le niveau de forgeron de " + nom + " a été augmenté à " + niveauForgeron + ".");
    }

    // Méthode principale pour tester la classe
    public static void main(String[] args) {
        UniqueJoueur joueur1 = new UniqueJoueur("Joueur1", 1, 3, 5);
        joueur1.afficherDetails();

        joueur1.augmenterNiveauAlchimiste();
        joueur1.augmenterNiveauForgeron();
        joueur1.afficherDetails();
    }
}
