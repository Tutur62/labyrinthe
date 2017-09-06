import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.ArrayList;

public class Client{

    public static void main(String[] args){
	if(args.length!=3){
	    System.out.println("Il faut 3 arguments : l'adresse ip du serveur, le port et le nom d'équipe.");
	    System.exit(0);
	}
	Random rand=new Random();
		
	try{

	    Socket s = new Socket(args[0],Integer.parseInt(args[1]));
	    boolean fin=false;
	    
	    // ecriture
	    OutputStream os  = s.getOutputStream();
	    PrintWriter pw = new PrintWriter(os);
	    //lecture
	    InputStream is = s.getInputStream();
	    BufferedReader bf = new BufferedReader(
						    new InputStreamReader(is));

	    pw.println(args[2]);
	    pw.flush();
	    
	    String numJoueur = bf.readLine();

	    //System.out.println("Numero de joueur : "+numJoueur);
			
	    while(!fin){
		String msg = bf.readLine();
		
		System.out.println("Message recu : "+msg);
		System.out.println();
		fin=msg.equals("FIN");

		if(!fin){

		    /*-----------------------------------------------------------------------*/
		    
		    /*TODO - mettre votre stratégie en place ici*/
		    /*Quelques lignes de code pour vous aider*/

		    //Creation du labyrinthe en fonction des informations recues
		    //Bande de veinards, c'est déjà écrit ! Par contre la doc de cette classe n'est pas complète.
		    //Faut pas trop en demander non plus !
			Labyrinthe laby = new Labyrinthe(msg);
			
			Joueur joueur = new Joueur();
			String Impasse = new String();
			//Création du tableau
			int tLaby[][] = new int[laby.getTailleX()][laby.getTailleY()];

		    //Informations sur le joueur
		    //System.out.println("Je me trouve en : ("+laby.getJoueur(Integer.parseInt(numJoueur)).getPosX()+","+laby.getJoueur(Integer.parseInt(numJoueur)).getPosY()+")");
		    ArrayList<Integer> infosMoule = new ArrayList<Integer>();
		    //Parcours du plateau pour trouver toutes les moules et leur valeur
		    for(int j=0;j<laby.getTailleY();j++)
			for(int i=0;i<laby.getTailleX();i++)
			    if(laby.getXY(i,j).getType()==Case.MOULE){
				infosMoule.add(i);infosMoule.add(j);infosMoule.add(laby.getXY(i,j).getPointRapporte());
			    }

		    //Affichage des informations sur les moules du plateau
		   // for(int i=0;i<infosMoule.size()/3;i++)
			//System.out.println("Moule en ("+infosMoule.get(i*3)+","+infosMoule.get(i*3+1)+") pour "+infosMoule.get(i*3+2)+" points");

		    //Je prépare le message suivant à envoyer au serveur : je vais me déplacer vers l'Est.
		    //Pourquoi ? Aucune idée mais faut bien envoyer quelque chose au serveur alors pourquoi pas ?
		    //A vous de faire mieux ici :-)

			//msg="E";
			
			for(int i = 0; i < laby.getTailleX(); i++){
				for(int j = 0; j < laby.getTailleY(); j++){
					if(laby.getXY(i,j).getType()==Case.DUNE){
						tLaby[i][j] = 1;
						}else{
							tLaby[i][j] = 0;
							tLaby[laby.getJoueur(Integer.parseInt(numJoueur)).getPosX()][laby.getJoueur(Integer.parseInt(numJoueur)).getPosY()] = 2;
						}
					}
				}

/*
			for(int i = 0; i < laby.getTailleY(); i++){
				for(int j = 0; j < laby.getTailleX(); j++){
					System.out.print(tLaby[j][i]);
				}
				System.out.println();
			}
*/
			//Conditions de déplacement mode normale
			//Vers à l'Est
			System.out.println("check1");
			if(
			tLaby[laby.getJoueur(Integer.parseInt(numJoueur)).getPosX()+1] 
			[laby.getJoueur(Integer.parseInt(numJoueur)).getPosY()] != 1 &&
			tLaby[laby.getJoueur(Integer.parseInt(numJoueur)).getPosX()-1] 
			[laby.getJoueur(Integer.parseInt(numJoueur)).getPosY()] != 1
			||
			tLaby[laby.getJoueur(Integer.parseInt(numJoueur)).getPosX()+1] 
			[laby.getJoueur(Integer.parseInt(numJoueur)).getPosY()] != 1  &&
			tLaby[laby.getJoueur(Integer.parseInt(numJoueur)).getPosX()] 
			[laby.getJoueur(Integer.parseInt(numJoueur)).getPosY()-1] != 1
			||
			tLaby[laby.getJoueur(Integer.parseInt(numJoueur)).getPosX()+1] 
			[laby.getJoueur(Integer.parseInt(numJoueur)).getPosY()] != 1 &&
			tLaby[laby.getJoueur(Integer.parseInt(numJoueur)).getPosX()] 
			[laby.getJoueur(Integer.parseInt(numJoueur)).getPosY()+1] != 1
			||
			tLaby[laby.getJoueur(Integer.parseInt(numJoueur)).getPosX()-1] 
			[laby.getJoueur(Integer.parseInt(numJoueur)).getPosY()] != 1  &&
			tLaby[laby.getJoueur(Integer.parseInt(numJoueur)).getPosX()] 
			[laby.getJoueur(Integer.parseInt(numJoueur)).getPosY()-1] != 1
			||
			tLaby[laby.getJoueur(Integer.parseInt(numJoueur)).getPosX()-1] 
			[laby.getJoueur(Integer.parseInt(numJoueur)).getPosY()] != 1  &&
			tLaby[laby.getJoueur(Integer.parseInt(numJoueur)).getPosX()] 
			[laby.getJoueur(Integer.parseInt(numJoueur)).getPosY()+1] != 1
			||
			tLaby[laby.getJoueur(Integer.parseInt(numJoueur)).getPosX()-1] 
			[laby.getJoueur(Integer.parseInt(numJoueur)).getPosY()] == 1  &&
			tLaby[laby.getJoueur(Integer.parseInt(numJoueur)).getPosX()] 
			[laby.getJoueur(Integer.parseInt(numJoueur)).getPosY()+1] == 1){
			System.out.println("check2");
			//Vers l'Est
			if(tLaby[laby.getJoueur(Integer.parseInt(numJoueur)).getPosX()+1] 
			[laby.getJoueur(Integer.parseInt(numJoueur)).getPosY()] == 0){
				msg = "E";
				//System.out.println("droite");
			
				//Vers le Sud
				if(tLaby[laby.getJoueur(Integer.parseInt(numJoueur)).getPosX()]
				[laby.getJoueur(Integer.parseInt(numJoueur)).getPosY()-1] == 0){
					msg = "N";
					//System.out.println("haut");
				}else{
					//Vers le Nord
					if(tLaby[laby.getJoueur(Integer.parseInt(numJoueur)).getPosX()]
					[laby.getJoueur(Integer.parseInt(numJoueur)).getPosY()+1] == 0){
						msg = "S";
						//System.out.println("bas");
					}else{
						//Vers l'Ouest
						if(tLaby[laby.getJoueur(Integer.parseInt(numJoueur)).getPosX()-1]
						[laby.getJoueur(Integer.parseInt(numJoueur)).getPosY()] == 0){
							msg = "O";
							//System.out.println("gauche");
						}
					}
				}
			}
			}
			else{
			if
			(tLaby[laby.getJoueur(Integer.parseInt(numJoueur)).getPosX()+1] 
			[laby.getJoueur(Integer.parseInt(numJoueur)).getPosY()] == 1&&
			tLaby[laby.getJoueur(Integer.parseInt(numJoueur)).getPosX()] 
			[laby.getJoueur(Integer.parseInt(numJoueur)).getPosY()-1] == 1&&
			tLaby[laby.getJoueur(Integer.parseInt(numJoueur)).getPosX()-1] 
			[laby.getJoueur(Integer.parseInt(numJoueur)).getPosY()] == 1)
			{
			Impasse="haut";
			}
			//Impasse vers le bas
					if
			(tLaby[laby.getJoueur(Integer.parseInt(numJoueur)).getPosX()+1] 
			[laby.getJoueur(Integer.parseInt(numJoueur)).getPosY()] == 1&&
			tLaby[laby.getJoueur(Integer.parseInt(numJoueur)).getPosX()] 
			[laby.getJoueur(Integer.parseInt(numJoueur)).getPosY()+1] == 1&&
			tLaby[laby.getJoueur(Integer.parseInt(numJoueur)).getPosX()-1] 
			[laby.getJoueur(Integer.parseInt(numJoueur)).getPosY()] == 1)
			{
			Impasse="bas";
			}
			//Impasse vers la droite
					if
			(tLaby[laby.getJoueur(Integer.parseInt(numJoueur)).getPosX()] 
			[laby.getJoueur(Integer.parseInt(numJoueur)).getPosY()+1] == 1&&
			tLaby[laby.getJoueur(Integer.parseInt(numJoueur)).getPosX()+1] 
			[laby.getJoueur(Integer.parseInt(numJoueur)).getPosY()] == 1&&
			tLaby[laby.getJoueur(Integer.parseInt(numJoueur)).getPosX()] 
			[laby.getJoueur(Integer.parseInt(numJoueur)).getPosY()-1] == 1)
			{
			Impasse="droite";
			}
			//Impasse vers la gauche
					if
			(tLaby[laby.getJoueur(Integer.parseInt(numJoueur)).getPosX()+1] 
			[laby.getJoueur(Integer.parseInt(numJoueur)).getPosY()] == 1&&
			tLaby[laby.getJoueur(Integer.parseInt(numJoueur)).getPosX()] 
			[laby.getJoueur(Integer.parseInt(numJoueur)).getPosY()+1] == 1&&
			tLaby[laby.getJoueur(Integer.parseInt(numJoueur)).getPosX()-1] 
			[laby.getJoueur(Integer.parseInt(numJoueur)).getPosY()] == 1)
			{
			Impasse="gauche";
			}
			//Mode sortie Impasse
			if(Impasse=="haut"){
				msg="S";
			}
			if(Impasse=="bas"){
				msg="N";
			}
			if(Impasse=="droite"){
				msg="O";
			}
			if(Impasse=="gauche"){
				msg="E";
			}
			System.out.println("check3");
			}
			System.out.println(Impasse);
			/*-----------------------------------------------------------------------*/
		    
		    //Envoi du message au serveur.
		    pw.println(msg);
		    pw.flush();
		}
		
	    }
	    s.close();
	    
	}catch(Exception e){
	    System.err.println(e);
	    e.printStackTrace();
	}
    }
	
}
