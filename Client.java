import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.ArrayList;

public class Client {
	//Permet de stocker la direction précédente prise par le joueur
	private static String anciendeplacement = new String();
	//Vérifie si l'orientation est égale à l'ancienne direction
	//Permet de déterminé la position du joueur dans l'espace en utilisant equals()
	private static boolean equalsens(String verif){
		if(anciendeplacement.equals(verif)){
			return true;
		}else{
			return false;
		}
	}
	//A partir des coordonnées du joueur et des valeurs x et y passés en paramètre
	//détermine si une case est une dune
	private static boolean verifdune(int valeurx, int valeury, Labyrinthe laby, String numJoueur) {
		if (laby.getXY(laby.getJoueur(Integer.parseInt(numJoueur)).getPosX() + valeurx,
			laby.getJoueur(Integer.parseInt(numJoueur)).getPosY() + valeury).getType() != 1){
			return true;
		} else {
			return false;
		}
	}
	//Utilisation algorithme de la main gauche
	//Précision la notion de gauche,droite,haut,bas est à partir la position du joueur
	private static String deplacer( String msg, Labyrinthe laby, String numJoueur) {
		//On vérifie si le déplacement précédent Est est ou Nord pour connaître l'orientation
		if (equalsens("E")||equalsens("N")) {
			//Si la case de gauche est un chemin et le déplacement précédent est Nord
			//On va à gauche quand on vient du Nord soit l'Ouest
			if (verifdune(-1, 0, laby, numJoueur)&&equalsens("N")) {
				msg = "O";
			}
			//Pour l'Est et le Nord on vérifie si la case du haut est un chemin
			//Cela revient donc à tourner à gauche pour l'Est et continuer tout droit pour
			//le Nord
			else if (verifdune(0, -1, laby, numJoueur)) {
				msg = "N";
			}
			//Pour l'Est et le Nord on vérifie si la case de droite est un chemin
			//Cela revient donc à tourner à droite pour le Nord et continuer tout droit pour
			//l'Est
			else if (verifdune(1, 0, laby, numJoueur)) {} 
			//Pour l'Est et le Nord on vérifie si la case du bas est un chemin
			//Cela revient donc à tourner à faire demi-tour pour le Nord et tourner
			//à droite pour l'Est
			else if (verifdune(0, 1, laby, numJoueur)) {
				msg = "S";
			} 
			//Pour l'Est on vérifie si la case de gauche est un chemin
			//Cela revient donc à tourner à faire demi-tour pour l'Est
			else if (verifdune(-1, 0, laby, numJoueur)&&equalsens("E")) {
				msg = "O";
			}
		//On vérifie si le déplacement précédent Sud est ou Ouest pour connaître l'orientation
		} else if (equalsens("S")||equalsens("O")) 
			//Pour le Sud on vérifie si la case de droite est un chemin
			//Cela revient donc à tourner à gauche pour l'Est et continuer tout droit pour
			//le Nord
			if (verifdune(1, 0, laby, numJoueur)&&equalsens("S")) {
			} 
			//Pour l'Ouest et le Sud on vérifie si la case du bas est un chemin
			//Cela revient donc à tourner à gauche pour l'Ouest et continuer tout droit pour
			//le Sud
			else if (verifdune(0, 1, laby, numJoueur)) {
				msg = "S";
			} 
			//Pour l'Ouest et le Sud on vérifie si la case de gauche est un chemin
			//Cela revient donc à tourner à droite pour le Sud et continuer tout droit pour
			//l'Ouest
			else if (verifdune(-1, 0, laby, numJoueur)) {
				msg = "O";
			//Pour l'Ouest et le Sud on vérifie si la case du haut est un chemin
			//Cela revient donc à tourner à faire demi-tour pour le Sud et 
			//de tourner à droite pour l'Ouest
			} else if (verifdune(0, -1, laby, numJoueur)) {
				msg = "N";
			}
		//Retourne la direction à prendre
		return msg;
	}
	public static void main(String[] args) {
		if (args.length != 3) {
			System.out.println("Il faut 3 arguments : l'adresse ip du serveur, le port et le nom d'équipe.");
			System.exit(0);
		}
		Random rand = new Random();
		anciendeplacement = "N";
		int compteur = 0;
		boolean verif = true;
		try {
			Socket s = new Socket(args[0], Integer.parseInt(args[1]));
			boolean fin = false;

			// ecriture
			OutputStream os = s.getOutputStream();
			PrintWriter pw = new PrintWriter(os);
			// lecture
			InputStream is = s.getInputStream();
			BufferedReader bf = new BufferedReader(new InputStreamReader(is));

			pw.println(args[2]);
			pw.flush();

			String numJoueur = bf.readLine();

			System.out.println("Numero de joueur : " + numJoueur);

			while (!fin) {
				String msg = bf.readLine();

				System.out.println("Message recu : " + msg);
				System.out.println();
				fin = msg.equals("FIN");

				if (!fin) {

					/*-----------------------------------------------------------------------*/

					/* TODO - mettre votre stratégie en place ici */
					/* Quelques lignes de code pour vous aider */

					// Creation du labyrinthe en fonction des informations
					// recues
					// Bande de veinards, c'est déjà écrit ! Par contre la doc
					// de cette classe n'est pas complète.
					// Faut pas trop en demander non plus !
					Labyrinthe laby = new Labyrinthe(msg);

					// Informations sur le joueur
					System.out.println("Je me trouve en : (" + laby.getJoueur(Integer.parseInt(numJoueur)).getPosX()
							+ "," + laby.getJoueur(Integer.parseInt(numJoueur)).getPosY() + ")");
					ArrayList<Integer> infosMoule = new ArrayList<Integer>();
					// Parcours du plateau pour trouver toutes les moules et
					// leur valeur
					for (int j = 0; j < laby.getTailleY(); j++)
						for (int i = 0; i < laby.getTailleX(); i++)
							if (laby.getXY(i, j).getType() == Case.MOULE) {
								infosMoule.add(i);
								infosMoule.add(j);
								infosMoule.add(laby.getXY(i, j).getPointRapporte());
							}

					// Affichage des informations sur les moules du plateau

					// Je prépare le message suivant à envoyer au serveur : je
					// vais me déplacer vers l'Est.
					// Pourquoi ? Aucune idée mais faut bien envoyer quelque
					// chose au serveur alors pourquoi pas ?
					// A vous de faire mieux ici :-)
					msg = "E";
					//Récupère la direction à prendre
					msg = deplacer( msg, laby, numJoueur);
					//Récupère l'ancienne direction
					anciendeplacement = msg;
					/*-----------------------------------------------------------------------*/

					// Envoi du message au serveur.
					pw.println(msg);
					pw.flush();
				}

			}
			s.close();

		} catch (Exception e) {
			System.err.println(e);
			e.printStackTrace();
		}
	}

}
/***************************************************************/
//Travail fourni non utlisable
//Création d'un tableau qui devait servir pour le labyrinthe imparfait
// char tab new char[laby.getTailleX()][laby.getTailleY()]
// for(int i = 0; i < laby.getTailleX(); i++){
// 	for(int j = 0; j < laby.getTailleY(); j++){
// 		if(laby.getXY(i,j).getType()==Case.DUNE){
//Si c'est une dune la valeur est D sinon c'est un chemin possible donc C
// 				tab[i][j] = 'D';
// 		}else{
// 				tab[i][j] = 'C';
// 			 }
// 		}
// 	}
// }
//La case prend la valeur P signifiant qu'elle a était parcouru
//tab[laby.getJoueur(Integer.parseInt(numJoueur)).getPosX()][laby.getJoueur(Integer.parseInt(numJoueur)).getPosY()] = 'P';
//Vérifie si le chemin potentiel n'est pas une dune et si c'est un chemin non parcouru
// 
// private static boolean verifdune(int valeurx, int valeury, Labyrinthe laby, String numJoueur) {
// 	if (laby.getXY(laby.getJoueur(Integer.parseInt(numJoueur)).getPosX() + valeurx,
// 		laby.getJoueur(Integer.parseInt(numJoueur)).getPosY() + valeury).getType() != 1||
// 		[laby.getJoueur(Integer.parseInt(numJoueur)).getPosY()]
// 		[laby.getJoueur(Integer.parseInt(numJoueur)).getPosX()]!='P'){
// 		return true;
// 	} else {
// 		return false;
// 	}
// }
//Problèmes rencontrés ne permettant pas l'implantation du code:
//-Difficultés pour dire si un labyrinthe est parfait
//-Même quand le labyrinthe était parfait, le robot pouvait se bloquer dans des impasses
//et le code ne marchait que dans un certain type de labyrinthe parfait(sans impasses)