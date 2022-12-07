import java.util.ArrayList;

/*
 * Script that implements and runs the motus game 
 * @author Auger Matéo 
 */

public class GameMotus {
    public static void main(String[] args){
        ArrayList<String> list = new ArrayList<String>();
        
        // Creation of the game structure
       
        Motus game = new Motus(list); 

        // Creation of the words 
        String mot = "Hello";String mot2 = "Coucou";String mot3 = "Bonjour";String mot4 = "Faisselle";String mot5 = "Chemin";
        String mot6 = "Concubines";String mot7 = "Taille";String mot8 = "Ministre";String mot9 = "Fabrique";String mot10 = "Cordillere";
        String mot11 = "Somme";String mot12 = "Hamster";String mot13 = "Violon";String mot14 = "Batracien";String mot15 = "Gravelot";

        // Addition of the words in the game
        game.addMots(mot);game.addMots(mot2);game.addMots(mot3);game.addMots(mot4);
        game.addMots(mot5);game.addMots(mot6);game.addMots(mot7);game.addMots(mot8);
        game.addMots(mot9);game.addMots(mot10);game.addMots(mot11);game.addMots(mot12);
        game.addMots(mot13);game.addMots(mot14);game.addMots(mot15);

        // Creation of : the connection to the database + the menu with actions on the database
        ConnectAndMenu connect = new ConnectAndMenu(game);

        connect.displayHome();
    }
}
