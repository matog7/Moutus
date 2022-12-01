import java.sql.*;
import java.util.ArrayList;

/*
 * Script that implements and runs the motus game 
 * @author Auger Matéo 
 */

public class jeuMotus {
    public static void main(String[] args){
        ArrayList<String> list = new ArrayList<String>();
        
        // Creation of the game structure
        Motus jeu = new Motus(list); 


        // Creation of the words 
        String mot = "Hello";String mot2 = "Coucou";String mot3 = "Bonjour";String mot4 = "Faisselle";String mot5 = "Chemin";
        String mot6 = "Concubines";String mot7 = "Taille";String mot8 = "Ministre";String mot9 = "Fabrique";String mot10 = "Cordillere";
        String mot11 = "Somme";String mot12 = "Hamster";String mot13 = "Violon";String mot14 = "Batracien";String mot15 = "Gravelot";

        // Addition of the words in the game
        jeu.addMots(mot);jeu.addMots(mot2);jeu.addMots(mot3);jeu.addMots(mot4);
        jeu.addMots(mot5);jeu.addMots(mot6);jeu.addMots(mot7);jeu.addMots(mot8);
        jeu.addMots(mot9);jeu.addMots(mot10);jeu.addMots(mot11);jeu.addMots(mot12);
        jeu.addMots(mot13);jeu.addMots(mot14);jeu.addMots(mot15);

        // Creation of the connection to the database
        Connect connect = new Connect(jeu);

        // Display the game
        // jeu.displayJeu();

        connect.displayMenu();
    }
}
