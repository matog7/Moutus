import java.util.ArrayList;
import java.util.InputMismatchException;
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
        if (mot.length() < 5 && mot.length() > 10){
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
        ArrayList<Character> badChar = new ArrayList<>();
        ArrayList<Character> goodChar = new ArrayList<>();
        
        //At the first start only the first letter is shown and add it to the goodChar array 
        String ret = "\n" + aTrouver.charAt(0);  
        goodChar.add(aTrouver.charAt(0));

        for (int i = 1; i < aTrouver.length(); i++){
            if (aTrouver.charAt(i) == rep.charAt(i)){
                ret = ret + rep.charAt(i);
                goodChar.add(rep.charAt(i));
            }
            else {
                ret = ret + "_";
                for (int j = 1; j < rep.length(); j++){
                    if (aTrouver.charAt(i) == rep.charAt(j)){
                        if (!badChar.contains(rep.charAt(j))){
                            badChar.add(rep.charAt(j));
                        }
                        
                    }
                }
            }
        }

        if (badChar.size() > 1){
            ret = ret + " et les lettres "+ badChar.toString().replace("[", "").replace("]", "")+ " sont mal placees ou apparaissent plusieurs fois...";
        } else if (goodChar.size() == aTrouver.length()){
            ret = "\n* mou - mou - moutus ! *";
        } else if (badChar.size() == 1){
            ret = ret + " et la lettre "+ badChar.toString().replace("[", "").replace("]", "")+ " est mal placee ou apparait plusieurs fois...";
        } else {
            ret = ret + " essayez encore...";
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
        System.out.println("\n\t -- MOUTUS -- \n");
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
                System.out.println("\tEntrez un mot de la bonne taille.\n");
            } else {
                essai += 1;
                ret = verifRep(motATrouver, rep);
                System.out.println(ret);
                if (rep.equalsIgnoreCase(motATrouver)){
                    System.out.println("\n\tBravo ! Vous avez trouvez "+ motATrouver +" en "+ essai +" essai(s).");
                    end = true;
                }   
            }
        }
        if (essai == 6){
            System.out.println("\n\tEchec ! Vous avez perdu, le mot etait "+ motATrouver +".");
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
        System.out.println("\t\t-- Que voulez vous faire ? -- \n\t\t\t 1 : Score \n\t\t\t 2 : Rejouer \n\t\t\t 3 : Quitter");
        System.out.print("Votre choix : ");
        try {
            int choice = sc.nextInt();
            switch (choice){
                case 1:
                    System.out.println("Votre score est de "+ this.score+" points.\n");
                    this.menu();
                    break;
                case 2:
                    this.displayJeu();
                    break;
                case 3:
                    System.out.println("Revenez demain !");
                    break;
            }
        } catch (InputMismatchException e){
            System.out.println("Choix non valide, réessayez.\n");
            this.menu();
        }
    }
}
