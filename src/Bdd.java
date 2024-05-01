import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;
public class Bdd {

    /**
     *  Méthode de test de la connection à la bdd
     */
    public static void testConnection() {
        Connection connection = null;

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:bdd_aranor_hunn.db");

        } catch (ClassNotFoundException e) {
            System.err.println("Could not find JDBC driver!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Error connecting to the database!");
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.err.println("Error closing the connection!");
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * Connection à la BDD
     */
    private static Connection getConnection() throws SQLException {
        // Configuration de la connexion à la base de données
        String url = "jdbc:sqlite:bdd_aranor_hunn.db"; // Exemple pour une base de données SQLite
        String user = "username"; // Nom d'utilisateur
        String password = "password"; // Mot de passe (si applicable)

        Connection connection = DriverManager.getConnection(url, user, password);
        return connection;
    }

    // ----------------------------------------------------------------------------------------------TABLES DIVERSES SAC
    /**
     * Récupération des données des tables et d'un perso passé en paramètre pour alimenter les tables de sac
     */
    public static Object[][] getDataForTableSac(String tableName, int idPerso, int niveau_alchimiste) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = Bdd.getConnection();
            statement = connection.createStatement();

            // Définir la requête SQL en fonction de la table spécifiée
            String query = "";
            switch (tableName) {
                case "table_sac_plante":
                    query = "SELECT tsp.quantite, tpl.nom, tpl.rang, tpl.alchimiste"+ niveau_alchimiste +
                            " FROM table_sac_plante tsp " +
                            "JOIN table_plante tpl ON tsp.plante_id = tpl.id " +
                            "WHERE tsp.quantite > 0 AND tsp.perso_id = " + idPerso + " ORDER BY tpl.rang, tpl.nom";

                    break;
                case "table_sac_potion":
                    query = "SELECT tpo.id, tsp.quantite, tpo.nom, tpo.effet, tpo.stats_complexe, tpo.composition, tpo.stats_brute " +
                            "FROM table_sac_potion tsp " +
                            "JOIN table_potion tpo ON tsp.potion_id = tpo.id " +
                            "WHERE tsp.quantite > 0 AND tsp.perso_id = " + idPerso;
                    break;
                case "table_sac_minerai":
                    query = "SELECT tsm.quantite, tm.nom, tm.rang, tm.stats, tm.passif " +
                            "FROM table_sac_minerai tsm " +
                            "JOIN table_minerai tm ON tm.id = tsm.minerai_id " +
                            "WHERE tsm.quantite > 0 AND tsm.perso_id = " + idPerso + " ORDER BY tm.rang, tm.nom";
                    break;
                // Ajoutez d'autres cas pour d'autres tables si nécessaire
                default:
                    System.err.println("Table not recognized: " + tableName);
                    return null;
            }

            // Exécuter la requête SQL
            resultSet = statement.executeQuery(query);

            // Obtenir le nombre de colonnes
            int columnCount = resultSet.getMetaData().getColumnCount();

            // Utiliser une liste pour stocker les lignes de données
            List<Object[]> rows = new ArrayList<>();

            while (resultSet.next()) {
                // Créer un tableau d'objets pour stocker les valeurs de chaque colonne dans une ligne
                Object[] rowData = new Object[columnCount];

                // Parcourir chaque colonne et récupérer sa valeur
                for (int i = 0; i < columnCount; i++) {
                    rowData[i] = resultSet.getObject(i + 1);
                }

                // Ajouter la ligne à la liste
                rows.add(rowData);
            }

            // Convertir la liste de lignes en tableau bidimensionnel
            Object[][] data = new Object[rows.size()][columnCount];

            for (int i = 0; i < rows.size(); i++) {
                data[i] = rows.get(i);
            }

            return data;
        } catch (SQLException e) {
            System.err.println("Error executing SQL query!");
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    System.err.println("Error closing the result set!");
                    e.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    System.err.println("Error closing the statement!");
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.err.println("Error closing the connection!");
                    e.printStackTrace();
                }
            }
        }

        return null; // En cas d'erreur, renvoyer null
    }
    public static void removeItem(String table, int perso_id, int item_id, int quantite) {
        Connection connection = null;
        PreparedStatement selectStatement = null;
        PreparedStatement updateStatement = null;
        String item = "";
        if (table.equals("table_sac_plante")){item = "plante_id";}
        else if (table.equals("table_sac_minerai")) {item = "minerai_id";}
        else if (table.equals("table_sac_potion")) {item = "potion_id";}

        try {
            connection = getConnection();

            selectStatement = connection.prepareStatement("SELECT quantite FROM " + table + " WHERE " + item + " = ? AND perso_id = ?");
            selectStatement.setInt(1, item_id);
            selectStatement.setInt(2, perso_id);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                int existingQuantite = resultSet.getInt("quantite");
                int newQuantite = Math.max(0, existingQuantite - quantite);

                updateStatement = connection.prepareStatement("UPDATE " + table + " SET quantite = ? WHERE " + item + " = ? AND perso_id = ?");
                updateStatement.setInt(1, newQuantite);
                updateStatement.setInt(2, item_id);
                updateStatement.setInt(3, perso_id);
                updateStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer l'exception ici (affichage, journalisation, etc.)
        } finally {
            // Fermer les ressources dans le bloc finally pour garantir leur libération
            try {
                if (selectStatement != null) {
                    selectStatement.close();
                }
                if (updateStatement != null) {
                    updateStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                // Gérer l'exception ici (affichage, journalisation, etc.)
            }
        }
    }
    /// -----------------------------------------------------------------------------------------------TABLE SAC PLANTES
    /**
     * Insertion dans la table plante
     */
    public static void insertIntoTableSacPlante(int plante_id, int quantite, int perso_id) throws SQLException {
        Connection connection = null;
        PreparedStatement selectStatement = null;
        PreparedStatement updateStatement = null;
        PreparedStatement insertStatement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();

            // Vérifier s'il existe déjà une entrée pour cette plante et ce personnage
            String checkQuery = "SELECT quantite FROM table_sac_plante WHERE plante_id = ? AND perso_id = ?";
            selectStatement = connection.prepareStatement(checkQuery);
            selectStatement.setInt(1, plante_id);
            selectStatement.setInt(2, perso_id);
            resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                // Une entrée existe déjà, donc nous devons augmenter la quantité existante d'un
                int existingQuantite = resultSet.getInt("quantite");
                int newQuantite = existingQuantite + quantite;

                // Mettre à jour la quantité
                String updateQuery = "UPDATE table_sac_plante SET quantite = ? WHERE plante_id = ? AND perso_id = ?";
                updateStatement = connection.prepareStatement(updateQuery);
                updateStatement.setInt(1, newQuantite);
                updateStatement.setInt(2, plante_id);
                updateStatement.setInt(3, perso_id);
                updateStatement.executeUpdate();
            } else {
                // Aucune entrée n'existe encore, donc nous pouvons insérer une nouvelle entrée
                String insertQuery = "INSERT INTO table_sac_plante (quantite, perso_id, plante_id) VALUES (?, ?, ?)";
                insertStatement = connection.prepareStatement(insertQuery);
                insertStatement.setInt(1, quantite);
                insertStatement.setInt(2, perso_id);
                insertStatement.setInt(3, plante_id);
                insertStatement.executeUpdate();
            }
        } finally {
            // Fermer les ressources dans le bloc finally pour garantir leur libération
            if (resultSet != null) {
                resultSet.close();
            }
            if (selectStatement != null) {
                selectStatement.close();
            }
            if (updateStatement != null) {
                updateStatement.close();
            }
            if (insertStatement != null) {
                insertStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }
    /**
     * Récupération de toutes les plantes d'un joueur selon son niveau d'alchimie. possibility de distinct les résultats
     */
    public static List<String> getAllPlanteFromJoueur(int na, int perso_id, boolean distinct) {
        Connection connection = null;
        PreparedStatement selectStatement = null;
        ResultSet resultSet = null;
        List<String> plantNames = new ArrayList<>(); // Créez une liste pour stocker les noms de plantes

        String rang = Utils.getRangString(na);

        try {
            connection = getConnection();
            String checkQuery;

            if (distinct) {
                checkQuery = "SELECT DISTINCT tpl.nom FROM table_plante tpl " +
                        "JOIN table_sac_plante tspl ON tspl.plante_id = tpl.id " +
                        "WHERE tspl.perso_id = ? AND tspl.quantite > 0 " +
                        "AND tpl.rang IN " + rang +
                        "ORDER BY tpl.rang, tpl.nom";
            } else {
                checkQuery = "SELECT tpl.nom, tspl.quantite FROM table_plante tpl " +
                        "JOIN table_sac_plante tspl ON tspl.plante_id = tpl.id " +
                        "WHERE tspl.perso_id = ? AND tspl.quantite > 0 " +
                        "AND tpl.rang IN " + rang +
                        "ORDER BY tpl.rang, tpl.nom";
            }

            selectStatement = connection.prepareStatement(checkQuery);
            selectStatement.setInt(1, perso_id); // Remplacez le paramètre par le signe d'interrogation
            resultSet = selectStatement.executeQuery();

            // Parcourez les résultats et ajoutez les noms à la liste en fonction de la quantité
            while (resultSet.next()) {
                String plantName;
                if (distinct) {
                    plantName = resultSet.getString("nom");
                    plantNames.add(plantName);
                } else {
                    plantName = resultSet.getString("nom");
                    int quantity = resultSet.getInt("quantite");
                    for (int i = 0; i < quantity; i++) {
                        plantNames.add(plantName);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Pour le suivi dans la console, vous pouvez également remplacer par un logger
            plantNames = null; // En cas d'erreur, définir la liste à null
        } finally {
            // Fermer les ressources dans le bloc finally pour garantir leur libération
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (selectStatement != null) {
                    selectStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace(); // Pour le suivi dans la console, vous pouvez également remplacer par un logger
            }
        }
        return plantNames; // Renvoyer la liste des noms de plantes ou null
    }

    /**
     * Récupération du nombre de plantes du joueur
     */
    public static int getNombrePlanteJoueur(int perso_id) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection(); // Utilisation de la méthode getConnection

            String query = "SELECT SUM(quantite) AS total_plante FROM table_sac_plante " +
                    "WHERE perso_id =" + perso_id;

            statement = connection.prepareStatement(query);

            resultSet = statement.executeQuery();

            // Récupération du résultat de la somme
            if (resultSet.next()) {
                return resultSet.getInt("total_plante");
            } else {
                // Ajustez cela selon vos besoins, par exemple, -1 peut indiquer une erreur ou une absence de données
                return -1;
            }

        } finally {
            // Fermeture des ressources dans le bloc finally
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }
    /**
     * Mise à jour de la quantité de plante des plantes dans la liste d'entrée lors de la synthèse
     */
    public static void ajusterQuantitesSacPlante(int joueurId, List<String> nomsPlantes) throws SQLException {
        Connection connection = null;
        PreparedStatement updateStatement = null;

        try {
            connection = getConnection();

            // Préparez la requête de mise à jour pour chaque plante dans la liste
            String updateQuery = "UPDATE table_sac_plante SET quantite = quantite - 1 WHERE perso_id = ? " +
                    "AND plante_id IN (SELECT id FROM table_plante WHERE nom = ?)";
            updateStatement = connection.prepareStatement(updateQuery);

            // Parcourez la liste des noms de plantes
            for (String nomPlante : nomsPlantes) {
                // Ajustez la quantité pour chaque plante
                updateStatement.setInt(1, joueurId);
                updateStatement.setString(2, nomPlante);
                updateStatement.executeUpdate();
            }

        } finally {
            // Fermeture des ressources dans le bloc finally
            if (updateStatement != null) {
                updateStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    /// ---------------------------------------------------------------------------------------------------TABLE PLANTES
    /**
     * Récupération de toutes les plantes disponibles du jeu
     */
    public static List<String> getAllPlants() {
        Connection connection = null;
        PreparedStatement selectStatement = null;
        ResultSet resultSet = null;
        List<String> allPlants = new ArrayList<>(); // Initialisation à une liste vide

        try {
            connection = getConnection();
            String selectQuery = "SELECT nom FROM table_plante ORDER BY nom";
            selectStatement = connection.prepareStatement(selectQuery);
            resultSet = selectStatement.executeQuery();

            while (resultSet.next()) {
                String plantName = resultSet.getString("nom");
                allPlants.add(plantName);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Pour le suivi dans la console, vous pouvez également remplacer par un logger
            allPlants = null;
        } finally {
            try{
                if (resultSet != null) {
                    resultSet.close();
                }
                if (selectStatement != null) {
                    selectStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
            ex.printStackTrace(); // Pour le suivi dans la console, vous pouvez également remplacer par un logger
            }
        }
        return allPlants;
    }
    /**
     * Récupération des stats des plantes pour une plante
     */
    public static List<Integer> getDataForComposition(String nomPlante, int niveau_alchimiste) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection(); // Utilisation de la méthode getConnection

            String query = "SELECT id, nom, alchimiste" + niveau_alchimiste +" AS stats FROM table_plante WHERE nom = ?";

            statement = connection.prepareStatement(query);
            statement.setString(1, nomPlante);

            resultSet = statement.executeQuery();

            // Si la plante est trouvée, renvoyer ses statistiques sous forme d'un tableau d'objets
            if (resultSet.next()) {
//                System.out.println(resultSet.getString("stats"));
                String statsString = resultSet.getString("stats");
                String[] statsArray = statsString.split(",");
                List<Integer> stats = new ArrayList<>();

                for (String stat : statsArray) {
                    stats.add(Integer.parseInt(stat.trim()));
                }

                return stats;
            } else {
                // Plante non trouvée, renvoyer null ou une valeur par défaut
                return null;
            }
        } finally {
            // Fermeture des ressources dans le bloc finally
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    /**
     * Renvoie l'id d'une plante d'après son nom
     */
    public static int getIdPlante(String nomPlante) {
        Connection connection = null;
        PreparedStatement selectStatement = null;
        ResultSet resultSet = null;
        int planteId = -1; // Initialisation de l'ID de la plante à -1 (valeur par défaut)

        try {
            connection = getConnection(); // Obtenez votre connexion à la base de données ici

            String selectQuery = "SELECT id FROM table_plante WHERE nom = ?";
            selectStatement = connection.prepareStatement(selectQuery);
            selectStatement.setString(1, nomPlante);

            resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                planteId = resultSet.getInt("id");
            }
        } catch (SQLException e) {
            // Gérer l'exception ici (affichage, journalisation, etc.)
            e.printStackTrace();
        } finally {
            // Fermer les ressources dans le bloc finally pour garantir leur libération
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (selectStatement != null) {
                    selectStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // Gérer l'exception de fermeture des ressources
                e.printStackTrace();
            }
        }

        return planteId; // Renvoyer l'ID de la plante
    }

    /**
     * Choisir une plante alétoirement
     */
    public static Object[] choisirPlanteAleatoire(String rarete) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection(); // Utilisation de la méthode getConnection

            // Requête pour sélectionner aléatoirement une plante de la table_plante avec la rareté spécifiée
            String query = "SELECT id, nom FROM table_plante WHERE rang = ? ORDER BY RANDOM() LIMIT 1";
            statement = connection.prepareStatement(query);
            statement.setString(1, rarete);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nom = resultSet.getString("nom");
                return new Object[] { id, nom };
            }
        } finally {
            // Fermeture des ressources dans le bloc finally
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }

        return null; // Retourner null si aucune plante n'a été trouvée
    }

    /// ------------------------------------------------------------TABLE SAC ET PLANTE : METHODE LOOT ET ENREGISTREMENT
    /**
     * Loot d'une plante selon sa rareté et enregistrement dans la table du personnage
     */
    public static void lootEtInsererPlante(String rarete, int perso_id) throws SQLException {

        // connection
        Connection connection;
        connection = Bdd.getConnection();
        connection.setAutoCommit(false);
        PreparedStatement lootStatement = null;
        PreparedStatement selectStatement = null;
        PreparedStatement updateStatement = null;
        PreparedStatement insertStatement = null;
        ResultSet lootresultSet = null;
        ResultSet resultSet = null;

        try {
            // Requête pour sélectionner aléatoirement une plante de la table_plante avec la rareté spécifiée
            String query = "SELECT id, nom FROM table_plante WHERE rang = ? ORDER BY RANDOM() LIMIT 1";
            lootStatement = connection.prepareStatement(query);
            lootStatement.setString(1, rarete);

            lootresultSet = lootStatement.executeQuery();

            if (lootresultSet.next()) {
                int plante_id = lootresultSet.getInt("id");
                String plante_nom = lootresultSet.getString("nom");
                // System.out.println("Loot : " + plante_id + ", " + plante_nom);

                // Vérifier s'il existe déjà une entrée pour cette plante et ce personnage
                String checkQuery = "SELECT quantite FROM table_sac_plante WHERE plante_id = ? AND perso_id = ?";
                selectStatement = connection.prepareStatement(checkQuery);
                selectStatement.setInt(1, plante_id);
                selectStatement.setInt(2, perso_id);
                resultSet = selectStatement.executeQuery();


                if (resultSet.next()) {
                    // Une entrée existe déjà, donc nous devons augmenter la quantité existante d'un
                    int existingQuantite = resultSet.getInt("quantite");
                    int newQuantite = existingQuantite + 1;

                    // Mettre à jour la quantité
                    String updateQuery = "UPDATE table_sac_plante SET quantite = ? WHERE plante_id = ? AND perso_id = ?";
                    updateStatement = connection.prepareStatement(updateQuery);
                    updateStatement.setInt(1, newQuantite);
                    updateStatement.setInt(2, plante_id);
                    updateStatement.setInt(3, perso_id);
                    updateStatement.executeUpdate();

                } else {
                    // Aucune entrée n'existe encore, donc nous pouvons insérer une nouvelle entrée
                    String insertQuery = "INSERT INTO table_sac_plante (quantite, perso_id, plante_id) VALUES (?, ?, ?)";
                    insertStatement = connection.prepareStatement(insertQuery);
                    int quantite = 1;
                    insertStatement.setInt(1, quantite);
                    insertStatement.setInt(2, perso_id);
                    insertStatement.setInt(3, plante_id);
                    insertStatement.executeUpdate();
                }
            }
            try{
                connection.commit();
//              connection.setAutoCommit(true);
            }catch(SQLException e) {
                connection.rollback();
                System.out.println("ERROR " + e);
            }
        }finally {
            // Validez les opérations
            // Fermer les ressources dans le bloc finally pour garantir leur libération
            if (lootresultSet != null) {
                lootresultSet.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
            if (selectStatement != null) {
                selectStatement.close();
            }
            if (lootStatement != null) {
                lootStatement.close();
            }
            if (updateStatement != null) {
                updateStatement.close();
            }
            if (insertStatement != null) {
                insertStatement.close();
            }
            connection.close();
        }
    }


    /// ------------------------------------------------------------------------------------------------TABLE SAC POTION
    /**
     * Retrouve toutes les potions crafts par un joueur
     */
    public static List<ResultSet> getAllPotionFromJoueur(int persoId) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet;

        List<ResultSet> resultSets = new ArrayList<>();

        try {
            connection = getConnection(); // Utilisez votre méthode getConnection

            String query = "SELECT * FROM table_potion tp " +
                    "JOIN table_sac_potion ON tp.id = table_sac_potion.potion_id " +
                    "WHERE table_sac_potion.perso_id = ?";

            statement = connection.prepareStatement(query);
            statement.setInt(1, persoId);

            resultSet = statement.executeQuery();
            resultSets.add(resultSet);

        } finally {
            // Ne fermez pas resultSet ici pour éviter les problèmes potentiels
            // Fermeture des ressources dans le bloc finally
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }

        return resultSets;
    }
    /**
     * Renvoie un boolean si le joueur possède déjà la potion via l'id
     */
    public static boolean potionPossedeId(int idPotion, int idPerso) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            // Ouverture de la connexion
            connection = getConnection();
            String query = "SELECT COUNT(*) AS count FROM table_sac_potion WHERE potion_id = ? AND perso_id = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, idPotion);
            statement.setInt(2, idPerso);

            resultSet = statement.executeQuery();

            // Récupération du résultat
            if (resultSet.next()) {
                int count = resultSet.getInt("count");
                return count > 0;  // Si count > 0, la potion est déjà présente
            }
        } finally {
            // Fermeture des ressources dans le bloc finally
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();  // Fermeture de la connexion
            }
        }
        return false;
    }
    /**
     * Récupération de tous les minerais que le joueur dispose
     */
    public static List<String> getAllPotionFromJoueur(int perso_id, boolean distinct) {
        Connection connection = null;
        PreparedStatement selectStatement = null;
        ResultSet resultSet = null;
        List<String> mineraiNames = new ArrayList<>(); // Créez une liste pour stocker les noms de plantes

        try {
            connection = getConnection();
            String checkQuery;

            if (distinct) {
                checkQuery = "SELECT DISTINCT tp.id, tp.nom, tp.effet, tp.stats_complexe FROM table_potion tp " +
                        "JOIN table_sac_potion tsp ON tsp.potion_id = tp.id " +
                        "WHERE tsp.perso_id = ? AND tsp.quantite > 0 " +
                        "ORDER BY tp.nom";
            } else {
                checkQuery = "SELECT tp.nom, tp.effet, tp.stats_complexe, tsp.quantite FROM table_potion tp " +
                        "JOIN table_sac_potion tsp ON tsp.potion_id = tp.id " +
                        "WHERE tsp.perso_id = ? AND tsp.quantite > 0 " +
                        "ORDER BY tp.nom";
            }

            selectStatement = connection.prepareStatement(checkQuery);
            selectStatement.setInt(1, perso_id); // Remplacez le paramètre par le signe d'interrogation
            resultSet = selectStatement.executeQuery();

            // Parcourez les résultats et ajoutez les noms à la liste en fonction de la quantité
            while (resultSet.next()) {
                String potionId, potionName, potionEffet, potionStats, text;
                if (distinct) {
                    potionId = resultSet.getString("id");
                    potionName = resultSet.getString("nom");
                    potionEffet = resultSet.getString("effet");
                    potionStats = resultSet.getString("stats_complexe");
                    if (potionEffet.isEmpty()) {
                        text = "(" + potionId + ") " + potionName + " - " + potionStats;
                    }
                    else{
                        text = "(" + potionId + ") " + potionName + " - " + potionEffet + " - " + potionStats;
                    }
                    mineraiNames.add(text);
                } else {
                    potionName = resultSet.getString("nom");
                    int quantity = resultSet.getInt("quantite");
                    for (int i = 0; i < quantity; i++) {
                        mineraiNames.add(potionName);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Pour le suivi dans la console, vous pouvez également remplacer par un logger
            mineraiNames = null; // En cas d'erreur, définir la liste à null
        } finally {
            // Fermer les ressources dans le bloc finally pour garantir leur libération
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (selectStatement != null) {
                    selectStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace(); // Pour le suivi dans la console, vous pouvez également remplacer par un logger
            }
        }
        return mineraiNames; // Renvoyer la liste des noms de plantes ou null
    }

    /// ----------------------------------------------------------------------------------------------------TABLE POTION
    /**
     * Insertion dans la table potion
     */
    public static int enregistrerNouvellePotion(String nom, String effet, String statsBrute, String statsComplexe, String composition, int na) throws SQLException {
        Connection connection = null;
        PreparedStatement insertPotionStatement = null;
        ResultSet generatedKeys = null;

        try {
            connection = getConnection();

            String insertPotionQuery = "INSERT INTO table_potion (nom, effet, stats_brute, stats_complexe, composition, na) VALUES (?, ?, ?, ?, ?, ?)";
            insertPotionStatement = connection.prepareStatement(insertPotionQuery, Statement.RETURN_GENERATED_KEYS);
            insertPotionStatement.setString(1, nom);
            insertPotionStatement.setString(2, effet);
            insertPotionStatement.setString(3, statsBrute);
            insertPotionStatement.setString(4, statsComplexe);
            insertPotionStatement.setString(5, composition);
            insertPotionStatement.setInt(6, na);

            int affectedRows = insertPotionStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("L'enregistrement de la potion a échoué, aucune ligne affectée.");
            }

            generatedKeys = insertPotionStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                return generatedKeys.getInt(1); // Récupérer l'ID généré
            } else {
                throw new SQLException("Échec de la récupération de l'ID de la nouvelle potion.");
            }
        } finally {
            if (generatedKeys != null) {
                generatedKeys.close();
            }
            if (insertPotionStatement != null) {
                insertPotionStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }
    /**
     * Renvoie l'ID d'une potion trouvée par ses stats ou -1 si pas trouvé
     */
    public static int trouverPotionIdStats(String effet, String statsBrute, String statsComplexe, String composition) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            // Ouverture de la connexion
            connection = getConnection();
            String query = "SELECT id FROM table_potion WHERE stats_brute = ? AND stats_complexe = ? AND "
                    + "composition = ? AND effet = ?";

            statement = connection.prepareStatement(query);
            statement.setString(1, statsBrute);
            statement.setString(2, statsComplexe);
            statement.setString(3, composition);
            statement.setString(4, effet);

            resultSet = statement.executeQuery();

            // Récupération de l'ID résultat
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } finally {
            // Fermeture des ressources dans le bloc finally
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();  // Fermeture de la connexion
            }
        }
        // pas d'ID
        return -1;
    }
    /**
     * Ajouter une potion dans la table sac ou incrementer une entrée déjà existante
     */
    public static void ajouterDansTableSacPotion(boolean potionPossede, int persoId, int potionId, int quantite) throws SQLException {
        Connection connection = null;
        PreparedStatement insertOrUpdateStatement = null;

        try {
            connection = getConnection();

            // Vérifier si la potion est déjà présente dans le sac du personnage
            if (potionPossede) {
                // Si oui, incrémenter la quantité
                String updateQuery = "UPDATE table_sac_potion SET quantite = quantite + ? WHERE perso_id = ? AND potion_id = ?";
                insertOrUpdateStatement = connection.prepareStatement(updateQuery);
                insertOrUpdateStatement.setInt(1, quantite);
                insertOrUpdateStatement.setInt(2, persoId);
                insertOrUpdateStatement.setInt(3, potionId);
                insertOrUpdateStatement.executeUpdate();
            } else {
                // Sinon, ajouter une nouvelle ligne dans table_sac_potion
                String insertQuery = "INSERT INTO table_sac_potion (perso_id, potion_id, quantite) VALUES (?, ?, ?)";
                insertOrUpdateStatement = connection.prepareStatement(insertQuery);
                insertOrUpdateStatement.setInt(1, persoId);
                insertOrUpdateStatement.setInt(2, potionId);
                insertOrUpdateStatement.setInt(3, quantite);
                insertOrUpdateStatement.executeUpdate();
            }
        } finally {
            if (insertOrUpdateStatement != null) {
                insertOrUpdateStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    /// ---------------------------------------------------------------------------------------------------TABLE MINERAI
    /**
     * Récupération de tous les minerais disponible du jeu
     */
    public static List<String> getAllMinerai() {
        Connection connection = null;
        PreparedStatement selectStatement = null;
        ResultSet resultSet = null;
        List<String> allMinerai = new ArrayList<>(); // Initialisation à une liste vide

        try {
            connection = getConnection();
            String selectQuery = "SELECT nom FROM table_minerai ORDER BY nom";
            selectStatement = connection.prepareStatement(selectQuery);
            resultSet = selectStatement.executeQuery();

            while (resultSet.next()) {
                String mineraiName = resultSet.getString("nom");
                allMinerai.add(mineraiName);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Pour le suivi dans la console, vous pouvez également remplacer par un logger
            allMinerai = null;
        } finally {
            try{
                if (resultSet != null) {
                    resultSet.close();
                }
                if (selectStatement != null) {
                    selectStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace(); // Pour le suivi dans la console, vous pouvez également remplacer par un logger
            }
        }
        return allMinerai;
    }
    /**
     * Renvoie l'id d'un minerai d'après son nom
     */
    public static int getIdMinerai(String nomPlante) {
        Connection connection = null;
        PreparedStatement selectStatement = null;
        ResultSet resultSet = null;
        int planteId = -1; // Initialisation de l'ID de la plante à -1 (valeur par défaut)

        try {
            connection = getConnection(); // Obtenez votre connexion à la base de données ici

            String selectQuery = "SELECT id FROM table_minerai WHERE nom = ?";
            selectStatement = connection.prepareStatement(selectQuery);
            selectStatement.setString(1, nomPlante);

            resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                planteId = resultSet.getInt("id");
            }
        } catch (SQLException e) {
            // Gérer l'exception ici (affichage, journalisation, etc.)
            e.printStackTrace();
        } finally {
            // Fermer les ressources dans le bloc finally pour garantir leur libération
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (selectStatement != null) {
                    selectStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // Gérer l'exception de fermeture des ressources
                e.printStackTrace();
            }
        }

        return planteId; // Renvoyer l'ID de la plante
    }
    /**
     * Insertion dans la table plante
     */
    public static void insertIntoTableSacMinerai(int minerai_id, int quantite, int perso_id) throws SQLException {
        Connection connection = null;
        PreparedStatement selectStatement = null;
        PreparedStatement updateStatement = null;
        PreparedStatement insertStatement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();

            // Vérifier s'il existe déjà une entrée pour cette plante et ce personnage
            String checkQuery = "SELECT quantite FROM table_sac_minerai WHERE minerai_id = ? AND perso_id = ?";
            selectStatement = connection.prepareStatement(checkQuery);
            selectStatement.setInt(1, minerai_id);
            selectStatement.setInt(2, perso_id);
            resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                // Une entrée existe déjà, donc nous devons augmenter la quantité existante d'un
                int existingQuantite = resultSet.getInt("quantite");
                int newQuantite = existingQuantite + quantite;

                // Mettre à jour la quantité
                String updateQuery = "UPDATE table_sac_minerai SET quantite = ? WHERE minerai_id = ? AND perso_id = ?";
                updateStatement = connection.prepareStatement(updateQuery);
                updateStatement.setInt(1, newQuantite);
                updateStatement.setInt(2, minerai_id);
                updateStatement.setInt(3, perso_id);
                updateStatement.executeUpdate();
            } else {
                // Aucune entrée n'existe encore, donc nous pouvons insérer une nouvelle entrée
                String insertQuery = "INSERT INTO table_sac_minerai (quantite, perso_id, minerai_id) VALUES (?, ?, ?)";
                insertStatement = connection.prepareStatement(insertQuery);
                insertStatement.setInt(1, quantite);
                insertStatement.setInt(2, perso_id);
                insertStatement.setInt(3, minerai_id);
                insertStatement.executeUpdate();
            }
        } finally {
            // Fermer les ressources dans le bloc finally pour garantir leur libération
            if (resultSet != null) {
                resultSet.close();
            }
            if (selectStatement != null) {
                selectStatement.close();
            }
            if (updateStatement != null) {
                updateStatement.close();
            }
            if (insertStatement != null) {
                insertStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }
    /// -----------------------------------------------------------------------------------------------TABLE SAC MINERAI
    /**
     * Récupération de tous les minerais que le joueur dispose
     */
    public static List<String> getAllMineraiFromJoueur(String mortalite, int perso_id, boolean distinct) {
        Connection connection = null;
        PreparedStatement selectStatement = null;
        ResultSet resultSet = null;
        List<String> mineraiNames = new ArrayList<>(); // Créez une liste pour stocker les noms de plantes

        String rang = Utils.getMortaliteString(mortalite);

        try {
            connection = getConnection();
            String checkQuery;

            if (distinct) {
                checkQuery = "SELECT DISTINCT tm.nom FROM table_minerai tm " +
                        "JOIN table_sac_minerai tsm ON tsm.minerai_id = tm.id " +
                        "WHERE tsm.perso_id = ? AND tsm.quantite > 0 " +
                        "AND tm.rang IN " + rang +
                        "ORDER BY tm.rang, tm.nom";
            } else {
                checkQuery = "SELECT tm.nom, tsm.quantite FROM table_minerai tm " +
                        "JOIN table_sac_minerai tsm ON tsm.minerai_id = tm.id " +
                        "WHERE tsm.perso_id = ? AND tsm.quantite > 0 " +
                        "AND tm.rang IN  " + rang +
                        "ORDER BY tm.rang, tm.nom";
            }

            selectStatement = connection.prepareStatement(checkQuery);
            selectStatement.setInt(1, perso_id); // Remplacez le paramètre par le signe d'interrogation
            resultSet = selectStatement.executeQuery();

            // Parcourez les résultats et ajoutez les noms à la liste en fonction de la quantité
            while (resultSet.next()) {
                String mineraiName;
                if (distinct) {
                    mineraiName = resultSet.getString("nom");
                    mineraiNames.add(mineraiName);
                } else {
                    mineraiName = resultSet.getString("nom");
                    int quantity = resultSet.getInt("quantite");
                    for (int i = 0; i < quantity; i++) {
                        mineraiNames.add(mineraiName);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Pour le suivi dans la console, vous pouvez également remplacer par un logger
            mineraiNames = null; // En cas d'erreur, définir la liste à null
        } finally {
            // Fermer les ressources dans le bloc finally pour garantir leur libération
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (selectStatement != null) {
                    selectStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace(); // Pour le suivi dans la console, vous pouvez également remplacer par un logger
            }
        }
        return mineraiNames; // Renvoyer la liste des noms de plantes ou null
    }
    /**
     * Loot d'une plante selon sa rareté et enregistrement dans la table du personnage
     */
    public static void lootEtInsererMinerai(String rarete, int perso_id) throws SQLException {

        // connection
        Connection connection;
        connection = Bdd.getConnection();
        connection.setAutoCommit(false);
        PreparedStatement lootStatement = null;
        PreparedStatement selectStatement = null;
        PreparedStatement updateStatement = null;
        PreparedStatement insertStatement = null;
        ResultSet lootresultSet = null;
        ResultSet resultSet = null;

        try {
            // Requête pour sélectionner aléatoirement une plante de la table_plante avec la rareté spécifiée
            String query = "SELECT id, nom FROM table_minerai WHERE rang = ? ORDER BY RANDOM() LIMIT 1";
            lootStatement = connection.prepareStatement(query);
            lootStatement.setString(1, rarete);

            lootresultSet = lootStatement.executeQuery();

            if (lootresultSet.next()) {
                int minerai_id = lootresultSet.getInt("id");
                String minerai_nom = lootresultSet.getString("nom");
                // ("Loot : " + minerai_id + ", " + minerai_nom);

                // Vérifier s'il existe déjà une entrée pour cette plante et ce personnage
                String checkQuery = "SELECT quantite FROM table_sac_minerai WHERE minerai_id = ? AND perso_id = ?";
                selectStatement = connection.prepareStatement(checkQuery);
                selectStatement.setInt(1, minerai_id);
                selectStatement.setInt(2, perso_id);
                resultSet = selectStatement.executeQuery();


                if (resultSet.next()) {
                    // Une entrée existe déjà, donc nous devons augmenter la quantité existante d'un
                    int existingQuantite = resultSet.getInt("quantite");
                    int newQuantite = existingQuantite + 5;

                    // Mettre à jour la quantité
                    String updateQuery = "UPDATE table_sac_minerai SET quantite = ? WHERE minerai_id = ? AND perso_id = ?";
                    updateStatement = connection.prepareStatement(updateQuery);
                    updateStatement.setInt(1, newQuantite);
                    updateStatement.setInt(2, minerai_id);
                    updateStatement.setInt(3, perso_id);
                    updateStatement.executeUpdate();

                } else {
                    // Aucune entrée n'existe encore, donc nous pouvons insérer une nouvelle entrée
                    String insertQuery = "INSERT INTO table_sac_minerai (quantite, perso_id, minerai_id) VALUES (?, ?, ?)";
                    insertStatement = connection.prepareStatement(insertQuery);
                    int quantite = 5;
                    insertStatement.setInt(1, quantite);
                    insertStatement.setInt(2, perso_id);
                    insertStatement.setInt(3, minerai_id);
                    insertStatement.executeUpdate();
                }
            }
            try{
                connection.commit();
//              connection.setAutoCommit(true);
            }catch(SQLException e) {
                connection.rollback();
                System.out.println("ERROR " + e);
            }
        }finally {
            // Validez les opérations
            // Fermer les ressources dans le bloc finally pour garantir leur libération
            if (lootresultSet != null) {
                lootresultSet.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
            if (selectStatement != null) {
                selectStatement.close();
            }
            if (lootStatement != null) {
                lootStatement.close();
            }
            if (updateStatement != null) {
                updateStatement.close();
            }
            if (insertStatement != null) {
                insertStatement.close();
            }
            connection.close();
        }
    }
    /**
     * Récupération du nombre de plantes du joueur
     */
    public static int getNombreMineraiJoueur(int perso_id) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection(); // Utilisation de la méthode getConnection
            String query = "SELECT SUM(quantite) AS total_minerai FROM table_sac_minerai " +
                    "WHERE perso_id =" + perso_id;
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            // Récupération du résultat de la somme
            if (resultSet.next()) {
                return resultSet.getInt("total_minerai");
            } else {
                // Ajustez cela selon vos besoins, par exemple, -1 peut indiquer une erreur ou une absence de données
                return -1;
            }
        } finally {
            // Fermeture des ressources dans le bloc finally
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }
    /// ---------------------------------------------------------------------------------------------------TABLE ALLIAGE
    /**
     * Récupération de tous les minerais disponible du jeu
     */
    public static List<String> getAllAlliage() {
        Connection connection = null;
        PreparedStatement selectStatement = null;
        ResultSet resultSet = null;
        List<String> allMinerai = new ArrayList<>(); // Initialisation à une liste vide

        try {
            connection = getConnection();
            String selectQuery = "SELECT nom FROM table_alliage ORDER BY nom";
            selectStatement = connection.prepareStatement(selectQuery);
            resultSet = selectStatement.executeQuery();

            while (resultSet.next()) {
                String mineraiName = resultSet.getString("nom");
                allMinerai.add(mineraiName);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Pour le suivi dans la console, vous pouvez également remplacer par un logger
            allMinerai = null;
        } finally {
            try{
                if (resultSet != null) {
                    resultSet.close();
                }
                if (selectStatement != null) {
                    selectStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace(); // Pour le suivi dans la console, vous pouvez également remplacer par un logger
            }
        }
        return allMinerai;
    }
    /// -----------------------------------------------------------------------------------------------------TABLE FORGE
    /**
     * Récupération de tous les minerais disponible du jeu
     */
    public static List<String> getAllArme(String typeArme) {
        Connection connection = null;
        PreparedStatement selectStatement = null;
        ResultSet resultSet = null;
        List<String> allForgeItem = new ArrayList<>(); // Initialisation à une liste vide

        try {
            connection = getConnection();
            String selectQuery = "SELECT item FROM table_forge WHERE type = '" + typeArme + "' ORDER BY item";
            selectStatement = connection.prepareStatement(selectQuery);
            resultSet = selectStatement.executeQuery();

            while (resultSet.next()) {
                String forgeItem = resultSet.getString("item");
                allForgeItem.add(forgeItem);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Pour le suivi dans la console, vous pouvez également remplacer par un logger
            allForgeItem = null;
        } finally {
            try{
                if (resultSet != null) {
                    resultSet.close();
                }
                if (selectStatement != null) {
                    selectStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace(); // Pour le suivi dans la console, vous pouvez également remplacer par un logger
            }
        }
        return allForgeItem;
    }

    /// ----------------------------------------------------------------------------------------------------TABLE JOUEUR

    /**
     * Récupération des noms des joueurs pour popup de selection
     */
    public static List<String> getNomsFromTablePerso() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        List<String> noms = new ArrayList<>();

        try {
            connection = getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT nom FROM table_perso");

            while (resultSet.next()) {
                String nom = resultSet.getString("nom");
                noms.add(nom);
            }

        } catch (SQLException e) {
            System.err.println("Error executing SQL query!");
            e.printStackTrace();
        } finally {
            // Fermer les ressources dans le bloc finally pour garantir leur libération
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    System.err.println("Error closing the result set!");
                    e.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    System.err.println("Error closing the statement!");
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.err.println("Error closing the connection!");
                    e.printStackTrace();
                }
            }
        }

        return noms;
    }
    /**
     * Récupération de l'ID d'un joueur en utilisant le nom
     */
    public static int getIdFromNom(String nom) {
        int id = -1; // Initialisez-la à une valeur par défaut, par exemple -1

        Connection connection = null;
        PreparedStatement preparedStatement;
        ResultSet resultSet;

        try {
            connection = getConnection();

            String query = "SELECT id FROM table_perso WHERE nom = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, nom);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                id = resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            assert connection != null;
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return id;
    }
    /**
     * Récupération de tous les joueurs
     */
    public static Object[][] getAllJoueur() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = Bdd.getConnection();
            statement = connection.createStatement();

            // Définir la requête SQL en fonction de la table spécifiée
            String query = "SELECT nom, niveau_alchimiste FROM table_perso";

            // Exécuter la requête SQL
            resultSet = statement.executeQuery(query);

            // Obtenir le nombre de colonnes
            int columnCount = resultSet.getMetaData().getColumnCount();

            // Utiliser une liste pour stocker les lignes de données
            List<Object[]> rows = new ArrayList<>();

            while (resultSet.next()) {
                // Créer un tableau d'objets pour stocker les valeurs de chaque colonne dans une ligne
                Object[] rowData = new Object[columnCount];

                // Parcourir chaque colonne et récupérer sa valeur
                for (int i = 0; i < columnCount; i++) {
                    rowData[i] = resultSet.getObject(i + 1);
                }

                // Ajouter la ligne à la liste
                rows.add(rowData);
            }

            // Convertir la liste de lignes en tableau bidimensionnel
            Object[][] data = new Object[rows.size()][columnCount];

            for (int i = 0; i < rows.size(); i++) {
                data[i] = rows.get(i);
            }
            return data;
        } catch (SQLException e) {
            System.err.println("Error executing SQL query!");
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    System.err.println("Error closing the result set!");
                    e.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    System.err.println("Error closing the statement!");
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.err.println("Error closing the connection!");
                    e.printStackTrace();
                }
            }
        }
        return null; // En cas d'erreur, renvoyer null
    }
    /**
     * Récupération du niveau alchimiste du joueur en utilisant l'ID
     */
    public static int getNiveauNaPerso(int id) {
        int niveau = -1; // Initialisez-la à une valeur par défaut, par exemple -1

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();

            String query = "SELECT niveau_alchimiste FROM table_perso WHERE id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                niveau = resultSet.getInt("niveau_alchimiste");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return niveau;
    }
    /**
     * Récupération du niveau forge du joueur en utilisant l'ID
     */
    public static int getNiveauNfPerso(int id) {
        int niveau = -1; // Initialisez-la à une valeur par défaut, par exemple -1

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();

            String query = "SELECT niveau_forgeron FROM table_perso WHERE id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                niveau = resultSet.getInt("niveau_forgeron");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return niveau;
    }
    /**
     * Récupération du niveau de mortalité (rang) du joueur en utilisant l'ID
     */
    public static String getRangPerso(int id) {
        String rang = ""; // Initialisez-la à une valeur par défaut, par exemple -1

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();

            String query = "SELECT rang FROM table_perso WHERE id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                rang = resultSet.getString("rang");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return rang;
    }
    /**
     * Ajout d'un nouveau joueur avec son nom et son niveau
     */
    public static void ajouterNouveauJoueur(String nom, String niveauNa, String niveauNf, String rang) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = getConnection();

            // Requête SQL pour insérer un nouveau joueur dans la table
            String insertQuery = "INSERT INTO table_perso (nom, niveau_alchimiste,niveau_forgeron, rang) VALUES (?, ?, ?, ?)";

            // Création d'une requête préparée
            preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, nom);
            preparedStatement.setString(2, niveauNa);
            preparedStatement.setString(3, niveauNf);
            preparedStatement.setString(4, rang);

            // Exécution de la requête
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Nouveau joueur ajouté avec succès.");
            } else {
                System.err.println("Erreur lors de l'ajout du joueur.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du joueur à la base de données.");
            e.printStackTrace();
        } finally {
            // Fermeture des ressources dans le bloc finally pour garantir leur libération
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    System.err.println("Erreur lors de la fermeture de la déclaration préparée.");
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.err.println("Erreur lors de la fermeture de la connexion.");
                    e.printStackTrace();
                }
            }
        }
    }
    // Fonction pour supprimer un joueur en utilisant son ID
    /**
     * Suppression d'un joueur
     */
    public static void supprimerJoueurParId(int id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = getConnection();
            connection.setAutoCommit(false); // Désactivez la validation automatique pour effectuer des opérations atomiques

            // Supprimez d'abord les lignes dans les autres tables
            String deletePlanteQuery = "DELETE FROM table_sac_plante WHERE perso_id = ?";
            String deletePotionQuery = "DELETE FROM table_sac_potion WHERE perso_id = ?";

            preparedStatement = connection.prepareStatement(deletePlanteQuery);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement(deletePotionQuery);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

            // Ensuite, supprimez la ligne dans la table table_perso
            String deleteJoueurQuery = "DELETE FROM table_perso WHERE id = ?";
            preparedStatement = connection.prepareStatement(deleteJoueurQuery);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

            connection.commit(); // Validez les opérations
        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback(); // En cas d'erreur, annulez les opérations
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            // Réactivez la validation automatique et fermez les ressources (connection, preparedStatement, etc.) ici dans le bloc finally.
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /// -------------------------------------------------------------------------------TABLE AUTRE - FONCTION NON UTILES
    /**
     * Récupération de toutes les plantes disponibles
     */
    public static List<String> getAllPlantNames() throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<String> nomsPlantes = new ArrayList<>();

        try {
            connection = getConnection(); // Utilisation de la méthode getConnection

            // Requête SQL pour récupérer les noms des plantes
            String query = "SELECT nom FROM table_plante"; // Assurez-vous que la table s'appelle "table_plante"
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            // Parcourir les résultats et ajouter les noms à la liste
            while (resultSet.next()) {
                String nomPlante = resultSet.getString("nom");
                nomsPlantes.add(nomPlante);
            }
        }finally {
            // Fermeture des ressources dans le bloc finally
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return nomsPlantes;
    }
    /**
     * Affichage de toutes les tables
     */
    public static void showTables() {
        Connection connection = null;

        try {
            connection = getConnection();

            if (connection != null) {
                //System.out.println("Connected to the database!");

                // Obtenir les métadonnées de la base de données
                DatabaseMetaData metaData = connection.getMetaData();

                // Obtenir les noms des tables
                ResultSet resultSet = metaData.getTables(null, null, null, new String[]{"TABLE"});

                while (resultSet.next()) {
                    String tableName = resultSet.getString("TABLE_NAME");
                    // System.out.println("Table: " + tableName);
                }

                resultSet.close();
            }
        } catch (SQLException e) {
            System.err.println("Error connecting to the database!");
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.err.println("Error closing the connection!");
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * Affichage de toutes colonnes d'une table
     */
    public static void showColumns(String tableName) {
        Connection connection = null;

        try {
            connection = Bdd.getConnection();

            if (connection != null) {
                //System.out.println("Connected to the database!");

                // Obtenir les métadonnées de la base de données
                DatabaseMetaData metaData = connection.getMetaData();

                // Obtenir les colonnes de la table spécifiée
                ResultSet resultSet = metaData.getColumns(null, null, tableName, null);

                while (resultSet.next()) {
                    String columnName = resultSet.getString("COLUMN_NAME");
                    // System.out.println("Column: " + columnName);
                }

                resultSet.close();
            }
        } catch (SQLException e) {
            System.err.println("Error connecting to the database!");
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.err.println("Error closing the connection!");
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * Récupération de toutes les colonnes d'une table
     */
    public static String[] getColumnNamesFromTable(String tableName) throws SQLException {

        try (Connection connection = Bdd.getConnection(); Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName)) {

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            String[] columnNames = new String[columnCount];

            for (int i = 0; i < columnCount; i++) {
                columnNames[i] = metaData.getColumnName(i + 1);
            }

            return columnNames;
        }
        // Fermer les ressources dans le bloc finally pour garantir leur libération
    }

}
