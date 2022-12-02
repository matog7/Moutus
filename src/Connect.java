import java.io.*;
import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Connect {

    private Connection con = null;
    private Motus game = null;

    /**
     * Method that builds a connection to the database
     */
    public Connect(Motus motus){
        // connection to jdbc database
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.con = DriverManager.getConnection("jdbc:mysql://localhost:3306/motus", "root", "root");
            this.game = motus;
            System.out.println("Connexion réussie");
        } catch (Exception e) {
            System.out.println("Connexion échouée : "+e);
        }
    }

    /**
     * Method to gets the connection
     * @return the connection to the database of users
     */
    public Connection getConnection(){
        return this.con;
    }

    /**
     * Method that allows to add a new user in the database (only for admin member)
     * @param name the name of the user
     * @param score the score of the user
     */
    public void addUser(String username, String password, String role){
        int score = 0;
        try {
            Connection con = this.getConnection();
            Statement st = con.createStatement();
            st.executeUpdate("INSERT INTO users VALUES ('" +username +"', '"+ password+"', "+ score+ ", '"+role+"');");
            System.out.println("Ajout réussi");
        } catch (Exception e) {
            System.out.println("Ajout échoué");
        }
    }

    /**
     * Connection method for the user
     * @param username the name of the user, written in the shell
     * @param password the password of the user, written in the shell
     * @return 1 if the connection is successful, 0 if the connection is not successful
    */
    public int connection(String username, String password){
        int ret = 0;
        try{
            Connection con = this.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT COUNT(*) as user FROM users WHERE username = '"+username+"' AND  password = '"+password+"';");
            if (rs.next() && rs.getInt("user") == 1){
                ret = 1;
            }
        } catch (Exception e) {
            System.out.print("User '"+username+"' connection try failed, "+e);
        }
        return ret;
    }

    /**
     * Method to retrieve the role of the user
     * @param username the name of the user
     * @return the role of the user
    */
    public String getRole(String username){
        String ret = null;
        try {
            Connection con = this.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT role FROM users WHERE username LIKE '"+username+"';");
            while (rs.next()){
                ret = rs.getString("role");
            }
        } catch (Exception e) {
            System.out.print("User "+ username +" does not exist."+ e);
        }
        return ret;
    }

    /**
     * Method that creates a new account from the sign up display
     * @param username the username written by the user
     * @param password the password written by the user
     * @return True if its successfull, false otherwise (in order to access to the right menu for each case)
     */
    public boolean newAccount(String username, String password){
        int score = 0;
        String role = "player";
        boolean check = true;
        try {
            Connection con = this.getConnection();
            Statement st = con.createStatement();
            st.executeUpdate("INSERT INTO users VALUES ('" +username +"', '"+ password+"', "+ score+ ", '"+role+"');");
            System.out.println("Votre compte vient d'etre creer.");
        } catch (Exception e) {
            check = false;
            System.out.println("Création de compte impossible, "+e);
        }
        return check;
    }

    // -------------------------------- ALL THE DISPLAY SCRIPTS -------------------------------- //


    /**
     * Method that displays the home menu
     */
    public void displayHome(){
        System.out.println(" - Jeux.com - ");
        Scanner sc = new Scanner(System.in);

        System.out.println("\t-- Que voulez vous faire ? -- \n\t\t 1 : Créer un compte \n\t\t 2 : Se connecter \n\t\t 3 : Quitter");
        System.out.print("Votre choix : ");
        try {
            int choice = sc.nextInt();
            switch (choice){
                case 1:
                    System.out.println("Acces a la creation de compte..");
                    this.signUpDisplay();
                    break;
                case 2:
                    System.out.println("Acces a la connexion..");
                    this.connectionDisplay();
                    break;
                case 3:
                    System.out.println("See ya !");
                    break;
            }
        } catch (InputMismatchException e){
            System.out.println("Choix non valide, réessayez.\n");
            this.displayHome();
        }
    }

    public void signUpDisplay(){
        System.out.println("\n - Sign Up - ");
        Scanner sc = new Scanner(System.in);
        System.out.print("Username : ");
        String user = sc.nextLine();
        System.out.print("Password : ");
        String pass = sc.nextLine();
        boolean ok = this.newAccount(user, pass);
        if (ok){
            System.out.println("Acces au menu...");
            this.displayMenu(user);
        } else {
            System.out.println("retour au menu d'accueil");
            this.displayHome();
        }
    }

    public void connectionDisplay(){
        System.out.println("\n - Connection - ");
        Scanner sc = new Scanner(System.in);

        Console console = System.console();
        String user = "";
        String pass = "";

        user = console.readLine("Entrez votre nom d'utilisateur: ");
        pass = new String(console.readPassword("Entrez votre mot de passe: "));
        
        int connect = this.connection(user, pass);
        if (connect == 1){
            System.out.println("Connexion en cours..");
            this.displayMenu(user);
        } else {
            System.out.println("Mauvais identifiant ou mot de passe. Reesayez.");
            this.connectionDisplay();
        }
    }

    public void displayMenu(String user){
        System.out.println("\n - Bienvenue "+user+" - ");
        Scanner sc = new Scanner(System.in);

        if (this.getRole(user).equalsIgnoreCase("admin")){
            this.displayAdmin(user);
        } else {
            System.out.println("\t-- Que voulez vous faire ? -- \n\t\t 1 : Lancer Moutus \n\t\t 2 : Leaderboard \n\t\t 3 : Mes informations \n\t\t\t 4 : Deconnexion");
            System.out.print("Votre choix : ");
            try {
                int display = sc.nextInt();
                switch (display){
                    case 1:
                        System.out.println("Chargement de Moutus..");
                        this.game.displayJeu(user);
                        break;
                    case 2:
                        System.out.println("Acces au leaderboard..");
                        this.displayLeaderboard(user);
                        break;
                    case 3:
                        System.out.println("Acces a vos informations en cours..");
                        break;
                    case 4:
                        System.out.println("See ya !");
                        break;
                }
            } catch (InputMismatchException e){
                System.out.println("Choix non valide, réessayez.\n");
                this.displayHome();
            }
        }
    }

    public void displayAdmin(String user){
        Scanner sc = new Scanner(System.in);
        System.out.println("\t-- Que voulez vous faire ? -- \n\t\t 1 : Lancer Moutus \n\t\t 2 : Leaderboard \n\t\t 3 : Mes informations"
                + "\n\t\t 4 : Ajout utilisateur \n\t\t 5 : Modifier role utilisateur \n\t\t 6 : Deconnexion");
        System.out.print("Votre choix : ");
        try {
            int display = sc.nextInt();
            switch (display){
                case 1:
                    System.out.println("Chargement de Moutus..");
                    this.game.displayJeu(user);
                    break;
                case 2:
                    System.out.println("Acces au leaderboard..");
                    this.displayLeaderboard(user);
                    break;
                case 3:
                    System.out.println("Acces a vos informations en cours..");
                    break;
                case 4:
                    System.out.println("Ajout utilisateur en developpement..");
                    break;
                case 5:
                    System.out.println("Maj role utilisateur en developpement..");
                    break;
                case 6:
                    System.out.println("See ya !");
                    break;
            }
        } catch (InputMismatchException e){
            System.out.println("Choix non valide, réessayez.\n");
            this.displayHome();
        }
    }

    /**
     * Method that displays the users of the game registered in the database
    */
    public void displayLeaderboard(String user){
        System.out.println("\n - Leaderboard Moutus - ");
        int i = 1;
        try{
            Connection con = this.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT username, score FROM users ORDER BY score DESC");
            while (rs.next()){
                if (rs.getString("username").equals(user)){
                    System.out.print(i + " - "+rs.getString("username")+ " (vous), ");
                } else {
                    System.out.print(i + " - "+rs.getString("username")+ ", ");
                }
                System.out.print(rs.getInt("score")+ "\n");
                i++;
            }
        } catch (Exception e) {
            System.out.println("Affichage échoué"+ e);
        }
        this.displayMenu(user);
    }


    public void displayInfo(String user){
        System.out.println("\n - Vos informations - ");
        int i = 1;
        try{
            Connection con = this.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM users WHERE username LIKE '"+user+"';");
            while (rs.next()){
                System.out.println("\nVotre nom d'utilisateur : "+rs.getString("username"));
                System.out.println("\nVotre mot de passe : "+rs.getString("password"));
                System.out.println("\nVotre score total : "+rs.getString("score"));
            }
        } catch (Exception e) {
            System.out.println("Affichage échoué"+ e);
        }
        this.displayMenu(user);
    }

}
