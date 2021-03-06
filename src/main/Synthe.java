package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/******************************************************************************
 *  Compilation:  javac Synthe.java
 *  Execution:    java Synthe input.txt tempo
 *  Dependencies: StdAudio.java Accord.java Note.java
 *
 *
 *
 ******************************************************************************/
@SuppressWarnings({"nls","hiding","resource"})
public class Synthe {

	/** Actuel tempo de la partition */
	public static int tempo ;

	/** Booleen repr�sentant l'ajout ou pas d'harmoniques au signal */
	public static boolean harm = false ;

	/** ??? */
	public static boolean guitar = false ;

	/** Methode main() principale du projet Synthe
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		if (args.length < 2) {
			System.out.println("Usage : java Synthe <fichier> <tempo> <harm/guitar>");
			System.exit(-1);
		}

		Synthe.tempo = Integer.parseInt(args[1]);

		if (args.length == 3) {
			if (args[2].equals("harm")) 
				Synthe.harm = true ;
			else if (args[2].equals("guitar")) 
				Synthe.guitar = true ;
		}

		try {
			BufferedReader fichier = new BufferedReader(new FileReader(args[0]));
			String ligne;

			while ((ligne = fichier.readLine()) != null) {
				playLigne(ligne, Synthe.harm, Synthe.guitar);
			}

			fichier.close();
		} catch (IOException ex) {
			ex.printStackTrace(); 
		}
		
		System.exit(0);
	}

	
	/** Methode qui joue usr la sortie audio la ligne du fichier partition passee parametre
	 * 
	 * @param ligne chaine de caractere representant la ligne a jouer
	 * @param harm boolean nous infdiquant si l'on doit ajouter les harmoniques au signal
	 * @param guitar ???
	 */
	public static void playLigne(String ligne, boolean harm, boolean guitar) {
		//On r�cup�re chaque �l�ment de la ligne entre chaque virgule
		String delims = "[,]";
		String [] tokens = ligne.split(delims);

		//On r�cup�re les deux derniers �l�ments de la ligne, la dur�e et l'amplitude
		double amplitude = Double.parseDouble(tokens[tokens.length-1]);
		double duree = Note.faceToDuration(tokens[tokens.length-2], Synthe.tempo);
		
		//On ajoute la premi�re note de la ligne � l'accord
		Accord accord = new Accord(Note.sToNote(tokens[0], amplitude, duree, (harm || guitar)));
		
		//S'il y a d'autres notes sur la ligne, on les ajoute � l'accord
		for (int i=1;i<tokens.length-2;i++) {
			accord.addNote(Note.sToNote(tokens[i], amplitude, duree, (harm || guitar)));
		}
		
		//Enfin, on joue l'accord
		accord.play();
	}


}


