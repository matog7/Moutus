package game;
import java.util.ArrayList;
import java.util.Scanner;


/*
 * Script that creates the motus game 
 * @author Auger Matéo 
 */

public class Motus {
    private ArrayList<String> listeMots;
    private int score = 0;

    /**
     * Method that a initializes a "Moutus" game with a ArrayList of words
     * @param liste the ArrayList of words
     */
    public Motus(ArrayList<String> liste) {
        if (listeMots == null) {
            this.listeMots = liste;
        } else {
            System.out.println("ERREUR Motus() : liste de mots null");
            this.listeMots = new ArrayList<String>();
        }
    }

    /**
     * Method that allows the user to add a new word in the ArrayList
     * @param mot the word to be added
     */ 
    public void addMots(String mot){
        if (mot.length() < 5 && mot.length() > 11){
            System.out.println("ERREUR addMots() : il faut ajouter un mot avec au moins 5 lettres");
        } else {
            this.listeMots.add(mot);
        }
    }

    /**
     * Method that chooses a word randomly inside the ArrayList 
     * @return the word choosen
     */
    public String randomMots(){
        int random = (int)(Math.random() * this.listeMots.size());
        return this.listeMots.get(random);
    }

    /**
     * Method that does all the controls on the word, firstly if the character of the words are at the good places.
     * If not, it checks if the character is included in the word to retrieves and stock it in an array.
     * @param aTrouver the word to be retrieved
     * @param rep the user's answer
     * @return the array with the letters at the wrong place and the word actualize with the characters found, in a string
     */
    public String verifRep(String aTrouver, String rep){                
        ArrayList<Character> lettre = new ArrayList<Character>();
        String ret = "" + aTrouver.charAt(0);  //At the first start only the first letter is shown

            for (int i = 1; i < aTrouver.length(); i++){
                if (aTrouver.charAt(i) == rep.charAt(i)){
                    ret = ret + rep.charAt(i);
                }
                else {
                    ret = ret + "_";
                    for (int j = 1; j < rep.length(); j++){
                        if (aTrouver.charAt(i) == rep.charAt(j)){
                            if (!lettre.contains(rep.charAt(j))){
                                lettre.add(rep.charAt(j));
                            }
                            
                        }
                    }
                    
                }
                
            }
            if (lettre.size() > 1){
                ret = ret + " et les lettres "+ lettre.toString()+ " sont mal placees ou apparaissent plusieurs fois...";
            } else if (lettre.size() == 0){
                ret = ret + " essayez encore !";
            } else {
                ret = ret + " et la lettre "+ lettre.toString()+ " est mal placee ou apparait plusieurs fois...";
            }
            
        return ret;
    }

    /**
     * Method that adds point(s) to the score.
     * @param s point(s) scored
     */
    public void addScore(int s){
        this.score = this.score + s;
    }

    /**
     * Method that displays the game with a scanner in the shell.
     */
    public void displayJeu(){
        System.out.println("\t -- MOUTUS --");
        Scanner sc = new Scanner(System.in);    // Creation of the scanner for the dialogue in the shell with the user
        int essai = 0;
        boolean end = false;

        String motATrouver = randomMots();
        System.out.print(motATrouver.charAt(0));

        for (int i = 1; i < motATrouver.length(); i++){
             System.out.print("_"); 
        }
        System.out.print(" en "+ motATrouver.length()+ " lettres");
        System.out.println();
        
        while (!end && essai < 6){
            System.out.print("Votre reponse: ");
            String rep = sc.nextLine();
            String ret = "";
            if (rep.length() < motATrouver.length() || rep.length() > motATrouver.length()){
                System.out.println("Entrez un mot de la bonne taille");
            } else {
                essai += 1;
                ret = verifRep(motATrouver, rep);
                System.out.println(ret);
                if (rep.equalsIgnoreCase(motATrouver)){
                    System.out.println("Bravo ! vous avez trouvez "+ motATrouver +" en "+ essai +" essai(s)");
                    end = true;
                }   
            }
        }
        if (essai == 6){
            System.out.println("Echec !");
        } else if (essai == 5){
            this.addScore(1);
        } else if (essai >= 3 == essai <= 4){
            this.addScore(3);
        } else if (essai >= 1 == essai <= 2){
            this.addScore(5);
        }
        this.menu();
    }

    /**
     * Method that displays the user's menu at the end of the game, again with a scanner in the shell.
     */
    public void menu(){
        // MENU
        Scanner sc = new Scanner(System.in);
        System.out.println("\t-- Que voulez vous faire ? -- \n\t\t - 1 : Score \n\t\t - 2 : Rejouer \n\t\t - 3 : Quitter");
        System.out.print("Votre choix : ");
        int choice = sc.nextInt();
        if (choice == 1){
            System.out.println("Votre score d'aujourd'hui est de "+ this.score +" point(s)");
            this.menu();
        } else if (choice == 2){
            this.displayJeu();
        } else if (choice == 3){
            System.out.println("Revenez demain !");
        }

    }
}
