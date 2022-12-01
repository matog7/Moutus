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
     * Method that displays the users of the game registered in the database
    */
    public void displayUsers(){
        try{
            Connection con = this.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM users");
            while (rs.next()){
                System.out.print(rs.getString("username")+ " ");
                System.out.print(rs.getString("password")+ " ");
                System.out.print(rs.getInt("score")+ " ");
                System.out.println(rs.getString("role"));
            }
        } catch (Exception e) {
            System.out.println("Affichage échoué"+ e);
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
            ResultSet rs = st.executeQuery("SELECT COUNT(*) as user FROM users WHERE UPPERCASE(username) = "+username+" AND  UPPERCASE(password) = "+password);
            if (rs.getString("user").equals("1")){
                ret = 1;
            }
        } catch (Exception e) {
            System.out.print("User "+username+" connection try failed");
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
            ResultSet rs = st.executeQuery("SELECT username, role FROM users WHERE username LIKE '"+username+"';");
            while (rs.next()){
                ret = rs.getString("username")+ " : "+ rs.getString("role");
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

    /**
     * Method that displays the home menu
     */
    public void displayMenu(){
        System.out.println("JDBC test");
        Scanner sc = new Scanner(System.in);

        System.out.println("\t\t-- Que voulez vous faire ? -- \n\t\t\t 1 : Créer un compte \n\t\t\t 2 : afficher users \n\t\t\t 3 : Quitter");
        System.out.print("Votre choix : ");
        try {
            int choice = sc.nextInt();
            switch (choice){
                case 1:
                    System.out.println("creation de compte");
                    this.createAccount();
                    break;
                case 2:
                    System.out.println("Users list : ");
                    this.displayUsers();
                    //this.connection();
                    break;
                case 3:
                    System.out.println("See ya !");
                    break;
            }
        } catch (InputMismatchException e){
            System.out.println("Choix non valide, réessayez.\n");
            this.displayMenu();
        }
    }

    public void createAccount(){
        System.out.println("\n - Sign Up - ");
        Scanner sc = new Scanner(System.in);
        System.out.print("Username : ");
        String user = sc.nextLine();
        System.out.print("Password : ");
        String pass = sc.nextLine();
        boolean ok = this.newAccount(user, pass);
        if (ok){
            System.out.println("Acces au menu...");
            this.displayAccount(user);
        } else {
            System.out.println("retour au menu d'accueil");
        }
    }

    public void displayAccount(String username){
        System.out.println(" - Bienvenue "+username+" - ");
        Scanner sc = new Scanner(System.in);

        System.out.println("\t\t-- Que voulez vous faire ? -- \n\t\t\t 1 : Mes Informations \n\t\t\t 2 : Lancer Moutus \n\t\t\t 3 : Déconnexion");
        System.out.print("Votre choix : ");
        try {
            int choice = sc.nextInt();
            switch (choice){
                case 1:
                    System.out.println("Infos");
                    break;
                case 2:
                    System.out.println("Chargement...");
                    this.game.displayJeu();
                    break;
                case 3:
                    System.out.println("Revenez vite !");
                    break;
            }
        } catch (InputMismatchException e){
            System.out.println("Choix non valide, réessayez.\n");
            this.displayMenu();
        }
    }

}
