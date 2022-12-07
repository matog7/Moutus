import java.io.*;
import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ConnectAndMenu {

    private Connection con = null;
    private Motus game = null;
    public boolean display = true;

    /**
     * Method that builds a connection to the database and initialize the user's menu (with actions on the database available)
     */
    public ConnectAndMenu(Motus motus){
        // connection to jdbc database
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.con = DriverManager.getConnection("jdbc:mysql://localhost:3306/motus", "root", "root");
            this.game = motus;
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
    public boolean addUser(String username, String password, String role){
        boolean check = true;
        int score = 0;
        try {
            Connection con = this.getConnection();
            Statement st = con.createStatement();
            st.executeUpdate("INSERT INTO users VALUES ('" +username +"', '"+ password+"', "+ score+ ", '"+role+"');");
            System.out.println("Ajout réussi");
        } catch (Exception e) {
            check = false;
            System.out.println("Ajout échoué, "+e);
        }
        return check;
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
            System.out.println("\nVotre compte vient d'etre creer.");
        } catch (Exception e) {
            check = false;
            System.out.println("Création de compte impossible. Essayer avec un autre nom d'utilisateur.");
        }
        return check;
    }

    /**
     * Method that updates the role of the given user 
     * @param username the user's username
     * @param role his new role
     * @return true if ok, false otherwise
     */
    public boolean updateRole(String username, String role){
        boolean check = true;
        try {
            Connection con = this.getConnection();
            Statement st = con.createStatement();
            st.executeUpdate("UPDATE motus.users SET role = '" + role + "' WHERE username LIKE '" + username + "';");
            System.out.println("Role bien mis a jour.");
        } catch (Exception e) {
            check = false;
            System.out.println("Mise a jour impossible. "+e);
        }
        return check;
    }

    // -------------------------------- ALL THE DISPLAY SCRIPTS -------------------------------- //


    /**
     * Method that displays the home menu
     */
    public void displayHome(){
        System.out.println("\n\t- GameShell.java - \n");
        Scanner sc = new Scanner(System.in);
        boolean run = true;
        System.out.println("      Que voulez vous faire ?"
                        +"\n\t1 : Créer un compte"
                        +"\n\t2 : Se connecter" 
                        +"\n\t3 : Quitter \t\t\n");
        
        while(run){
            System.out.print("Votre choix : ");
            try {
                int choice = sc.nextInt();
                switch (choice){
                    case 1:
                        run = false;
                        System.out.println("Acces a la creation de compte..");
                        this.signUpDisplay();
                        break;
                    case 2:
                        run = false;
                        System.out.println("Acces a la connexion..");
                        this.connectionDisplay();
                        break;
                    case 3:
                        run = false;
                        System.out.println("See ya !");
                        break;
                }
                if (choice <= 0 || choice > 3){
                    System.out.println("Choix non valide, réessayez.\n");
                }
            } catch (InputMismatchException e){
                System.out.println("Entrez un chiffre.\n");
                this.displayHome();
            }
        }
        
    }

    /**
     * Method that displays the sign-up menu
     */
    public void signUpDisplay(){
        System.out.println("\n\t - Sign Up - ");

        Console console = System.console();
        String user = "";
        String pass = "";
        String confirmPass = "";

        user = console.readLine("Entrez votre nom d'utilisateur: ");
        pass = console.readLine("Entrer votre mot de passe: ");
        
        confirmPass = new String(console.readPassword("Confirmer votre mot de passe: "));

        if (pass.equals(confirmPass)){
            boolean ok = this.newAccount(user, pass);
            if (ok){
                System.out.println("Maintenant, connectez-vous..");
                this.connectionDisplay();
            } else {
                this.signUpDisplay();
            }
        } else {
            System.out.println("Mauvais mot de passe. Reesayez.");
            this.connectionDisplay();
        }

        
    }

    /**
     * Method that displays the connection menu
     */
    public void connectionDisplay(){
        System.out.println("\n\t - Connection - ");

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


    /**
     * Method that chooses the menu to display, depending on the user's role
     * @param user the username of the connected user
     */
    public void displayMenu(String user){
        Scanner sc = new Scanner(System.in);

        if (this.display){
            System.out.println("\n\t - Bienvenue "+user+" - ");
            this.display = false;
        }

        if (this.getRole(user).equalsIgnoreCase("admin") || this.getRole(user).equalsIgnoreCase("master")){
            this.displayAdmin(user);
        } else {
            this.displayUser(user);
            
        }
    }

    /**
     * Method that displays a menu for the simple users (role : player)
     * @param user the username of the connected user
     */
    public void displayUser(String user){
        Scanner sc = new Scanner(System.in);
        boolean run = true;
        System.out.println("\n      Que voulez vous faire ?"
                        +"\n\t1 : Lancer Moutus "
                        +"\n\t2 : Leaderboard "
                        +"\n\t3 : Mes informations "
                        +"\n\t4 : Deconnexion \n");

        while (run){
            System.out.print("Votre choix : ");
            try {
                int display = sc.nextInt();
                switch (display){
                    case 1:
                        run = false;
                        System.out.println("Chargement de Moutus..");
                        this.game.displayJeu(user);
                        break;
                    case 2:
                        run = false;
                        System.out.println("Acces au leaderboard..");
                        this.displayLeaderboard(user);
                        break;
                    case 3:
                        run = false;
                        System.out.println("Acces a vos informations en cours..");
                        this.displayInfo(user);
                        break;
                    case 4:
                        run = false;
                        System.out.println("A plus tard !");
                        this.displayHome();
                        break;
                    
                }
                if (display <= 0 || display > 4){
                        System.out.println("Choix non valide, réessayez.\n");
                }
            } catch (InputMismatchException e){
                System.out.println("Entrez un chiffre.\n");
                this.displayUser(user);
            }
        }
    }

    /**
     * Method that displays a menu for the admin users (role : admin), with more rights for the master user.
     * @param user the username of the connected user
     */
    public void displayAdmin(String user){
        Scanner sc = new Scanner(System.in);
        boolean run = true;
        System.out.println("\n      Que voulez vous faire ?"
                        +"\n\t1 : Lancer Moutus "
                        +"\n\t2 : Leaderboard "
                        +"\n\t3 : Mes informations "
                        +"\n\t4 : Ajouter un nouvel utilisateur"
                        +"\n\t5 : Modifier le role d'un utilisateur"
                        +"\n\t6 : Deconnexion \n");

        while (run){
            System.out.print("Votre choix : ");
            try {
                int display = sc.nextInt();
                switch (display){
                    case 1:
                        run = false;
                        System.out.println("Chargement de Moutus..");
                        this.game.displayJeu(user);
                        break;
                    case 2:
                        run = false;
                        System.out.println("Acces au leaderboard..");
                        this.displayLeaderboard(user);
                        break;
                    case 3:
                        run = false;
                        System.out.println("Acces a vos informations en cours..");
                        this.displayInfo(user);
                        break;
                    case 4:
                        run = false;
                        System.out.println("Acces a l'ajout d'utilisateur..");
                        this.displayAdd(user);
                        break;
                    case 5:
                        run = false;
                        System.out.println("Acces a la maj du role utilisateur..");
                        this.displayUpdate(user);
                        break;
                    case 6:
                        run = false;
                        System.out.println("A plus tard !");
                        this.displayHome();
                        break;
                }
                if (display <= 0 || display > 6){
                    System.out.println("Choix non valide, réessayez.\n");
                }
            } catch (InputMismatchException e){
                System.out.println("Entrez un chiffre.\n");
                this.displayAdmin(user);
            } 
        }
        
    }

    /**
     * Method that displays the users of the game registered in the database, order by their score
     * @param user the username of the connected user (in order to go back to the main menu and keeping the connection)
     */
    public void displayLeaderboard(String user){
        System.out.println("\n       - Leaderboard Moutus - \n");
        int i = 1;
        try{
            Connection con = this.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT username, score FROM users ORDER BY score DESC");
            while (rs.next()){
                if (rs.getString("username").equals(user)){
                    System.out.print("\t|"+ i + " - "+rs.getString("username")+ " (vous), ");
                } else {
                    System.out.print("\t|"+ i + " - "+rs.getString("username")+ ", ");
                }
                System.out.print(rs.getInt("score")+"\n");
                i++;
            }
        } catch (Exception e) {
            System.out.println("Affichage échoué"+ e);
        }
        this.goBack(user);
    }

    /**
     * Method that displays the infos about your user account
     * @param user the username of the connected user 
     */
    public void displayInfo(String user){
        System.out.println("\n     - Vos informations - ");
        try{
            Connection con = this.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM users WHERE username LIKE '"+user+"';");
            while (rs.next()){
                System.out.println("\nVotre nom d'utilisateur : "+rs.getString("username"));
                System.out.println("Votre mot de passe : "+rs.getString("password"));
                System.out.println("Votre score total : "+rs.getString("score")+"\n");
            }
        } catch (Exception e) {
            System.out.println("Affichage échoué, "+ e);
        }
        this.goBack(user);
    }

    /**
     * Method that displays a 'form' in order to add a new player to the game's database (admin and master only)
     * @param user the username of the connected user (in order to go back to the main menu and keeping the connection)
     */
    public void displayAdd(String user){
        System.out.println("\n       - Ajout - ");
        Scanner sc = new Scanner(System.in);
        System.out.print("Username : ");
        String username = sc.nextLine();
        System.out.print("Password : ");
        String pass = sc.nextLine();
        System.out.print("Role : ");
        String role = sc.nextLine();
        if (role.equalsIgnoreCase("player") || role.equalsIgnoreCase("admin")){

            boolean ok = this.addUser(username, pass, role);
            if (ok){
                System.out.println("L'utilisateur "+username+" a bien ete ajoute.");
                this.goBack(user);
            } else {
                System.out.println("Erreur sur l'ajout, retour au Menu..");
                this.displayAdmin(user);
            }
        } else {
            System.out.println("\nLe role peut etre uniquement admin ou player. Reessayer.");
            this.displayAdd(user);
        }
    }

    /**
     * Method that displays a 'form' in order to update a player's role (admin and master only)
     * @param user the username of the connected user (in order to go back to the main menu and keeping the connection)
     */
    public void displayUpdate(String user){
        Scanner sc = new Scanner(System.in);
        System.out.println("\n     - MAJ role - ");
        System.out.print("Nom de l'utilisateur : ");
        String username = sc.nextLine();

        if (username.equalsIgnoreCase(user)){
            System.out.println("Vous ne pouvez pas modifier votre propre role.");
            this.displayUpdate(user);
        }
        
        String roleUsername = this.getRole(username);
        String userRole = this.getRole(user);
        System.out.println("Role : "+roleUsername);
        if (userRole.equalsIgnoreCase("admin") && roleUsername.equalsIgnoreCase("admin")){
            System.out.println("Cet utilisateur est aussi "+ roleUsername +", vous ne pouvez pas le modifier.\n 1- Chercher autre utilisateur \n 2- Revenir au menu");
            System.out.print("Votre choix : ");
            try {
                int display = sc.nextInt();
                switch (display){
                    case 1:
                        this.displayUpdate(user);
                        break;
                    case 2:
                        this.displayAdmin(user);
                        break;
                }
                if (display <= 0 || display > 2){
                    System.out.println("Choix non valide, réessayez.\n");
                }
            } catch (InputMismatchException e){
                System.out.println("Entrez un chiffre.\n");
            }
        } else {
            System.out.print("Son nouveau role : ");
            String role = sc.nextLine(); 
            if (role.equalsIgnoreCase("player") || role.equalsIgnoreCase("admin")){
                boolean ok = this.updateRole(username, role);
                if (ok){
                    this.goBack(user);
                } else {
                    System.out.println("Erreur de maj, reesayer.\n");
                    this.displayUpdate(user);
                }
            } else{
                System.out.println("\nLe role peut etre uniquement admin ou player. Reessayer.\n");
                this.displayUpdate(user);
            }
        }
    }

    /**
     * Method that displays a quick scanner in order to go back to the main menu
     * @param user the username of the connected user (in order to go back to the main menu and keeping the connection)
     */
    public void goBack(String user){
        Scanner sc = new Scanner(System.in);
        System.out.print("\nEntrez 'r' pour revenir au menu : ");
        try{
           String retour = sc.nextLine();
            if (retour.equals("r")){
                this.displayMenu(user);
            } else {
                System.out.println("Touche non valide, réessayez.");
                this.goBack(user);
            }
        } catch (InputMismatchException e){
            System.out.println("Entrez r, c'est pas compliqué.");
            this.goBack(user);
        }
    }

}
