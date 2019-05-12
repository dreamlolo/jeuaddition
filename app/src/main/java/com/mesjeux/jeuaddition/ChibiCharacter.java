package com.mesjeux.jeuaddition;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.widget.Toast;
import android.content.Context;
import android.app.Application;
import android.view.WindowManager;
import android.view.Display;
import android.view.PointerIcon;
import android.graphics.Point;
import android.content.Context;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class ChibiCharacter extends GameObject {

    public  int ROW_TOP_TO_BOTTOM = 0;
    public  int ROW_RIGHT_TO_LEFT = 1;
    public  int ROW_LEFT_TO_RIGHT = 0;
    public  int ROW_BOTTOM_TO_TOP = 1;

    // Row index of Image are being used.
    public int rowUsing = ROW_LEFT_TO_RIGHT;

    public int colUsing;
    private int cumulTime=0;
    public int tableauX,tableauY,valeur;

    private Bitmap[] leftToRights;
    private Bitmap[] rightToLefts;
    private Bitmap[] topToBottoms;
    private Bitmap[] bottomToTops;

    // Velocity of game character (pixel/millisecond)
    public static final float VELOCITY = 0.1f;

    private int movingVectorX = 0;
    private int movingVectorY = 5;
    private int marge=3;
    private long lastDrawNanoTime =-1;

    private int chibiXSize;
    private int chibiYSize;
    private boolean fixe;
    public Bitmap bitmapImage;
    public Integer waitTime;
    public boolean theoriqueMoveDone;



    private GameSurface gameSurface;

    public ChibiCharacter(GameSurface gameSurface, Bitmap image, int x, int y,int valeur) {
        super(image, 2, 5, x, y);
        this.gameSurface= gameSurface;

        this.topToBottoms = new Bitmap[colCount]; // 3
        this.rightToLefts = new Bitmap[colCount]; // 3
        this.leftToRights = new Bitmap[colCount]; // 3
        this.bottomToTops = new Bitmap[colCount]; // 3
        this.chibiXSize=Math.round(gameSurface.scaleY)-marge*2;
        this.chibiYSize=Math.round(gameSurface.scaleY)-marge*2;
        this.movingVectorX=0;
        this.movingVectorY=chibiYSize+marge;
        this.tableauX=new Integer (x);
        this.tableauY=new Integer (y);
      //  this.valeur=0;
        this.fixe=false;
        this.waitTime=1000;
        this.theoriqueMoveDone=false;



    Log.i("Menu","Chibi valeur"+String.valueOf(valeur));


        for(int col = 0; col< this.colCount; col++ ) {
            this.topToBottoms[col] = this.createSubImageAt(ROW_TOP_TO_BOTTOM, col);
            this.topToBottoms[col]=Bitmap.createScaledBitmap(this.topToBottoms[col],Math.round(gameSurface.scaleY) ,Math.round(gameSurface.scaleY),true);
            this.rightToLefts[col]  = this.createSubImageAt(ROW_RIGHT_TO_LEFT, col);
            this.rightToLefts[col]=Bitmap.createScaledBitmap(this.rightToLefts[col],Math.round(gameSurface.scaleY),Math.round(gameSurface.scaleY),true);
            this.leftToRights[col] = this.createSubImageAt(ROW_LEFT_TO_RIGHT, col);
            this.leftToRights[col]=Bitmap.createScaledBitmap(this.leftToRights[col],Math.round(gameSurface.scaleY),Math.round(gameSurface.scaleY),true);
            this.bottomToTops[col]  = this.createSubImageAt(ROW_BOTTOM_TO_TOP, col);
            this.bottomToTops[col]=Bitmap.createScaledBitmap(this.bottomToTops[col],Math.round(gameSurface.scaleY),Math.round(gameSurface.scaleY),true);
        }

    //    Log.i("Games","InitTabChib "+String.valueOf(valeur));
        int colonne,ligne;
        if (valeur>=10) {
            this.valeur = valeur;
            colonne = 4;
            this.bitmapImage = this.bottomToTops[colonne];
        } else {
            int nbRandom = valeur - 1;
            this.valeur = nbRandom + 1;

            if (nbRandom < 5) {
                ligne = 0;
                colonne = nbRandom;
            } else {
                ligne = 1;
                colonne = nbRandom - 5;
                //valeur=valeur-2;
            }


      //      Log.i("GameSchibi", "random " + String.valueOf(ligne) + "," + String.valueOf(colonne));
       //     Log.i("GameSchibi", "random valeur" + String.valueOf(this.valeur));
            switch (ligne) {
                case 1:
                    this.bitmapImage = this.bottomToTops[colonne];
                    break;
                case 0:
                    this.bitmapImage = this.leftToRights[colonne];
                    break;
            }
        }


    }

    public ChibiCharacter(GameSurface gameSurface, Bitmap image, int x, int y) {
        super(image, 2, 5, x, y);

        this.gameSurface= gameSurface;

        this.topToBottoms = new Bitmap[colCount]; // 3
        this.rightToLefts = new Bitmap[colCount]; // 3
        this.leftToRights = new Bitmap[colCount]; // 3
        this.bottomToTops = new Bitmap[colCount]; // 3
        this.chibiXSize=Math.round(gameSurface.scaleY)-marge*2;
        this.chibiYSize=Math.round(gameSurface.scaleY)-marge*2;
        this.movingVectorX=0;
        this.movingVectorY=chibiYSize+marge;
        this.tableauX=new Integer (1);
        this.tableauY=new Integer (0);
        this.valeur=0;
        this.fixe=true;
        this.waitTime=1000;




        for(int col = 0; col< this.colCount; col++ ) {
            this.topToBottoms[col] = this.createSubImageAt(ROW_TOP_TO_BOTTOM, col);
            this.topToBottoms[col]=Bitmap.createScaledBitmap(this.topToBottoms[col],Math.round(gameSurface.scaleY) ,Math.round(gameSurface.scaleY),true);
            this.rightToLefts[col]  = this.createSubImageAt(ROW_RIGHT_TO_LEFT, col);
            this.rightToLefts[col]=Bitmap.createScaledBitmap(this.rightToLefts[col],Math.round(gameSurface.scaleY),Math.round(gameSurface.scaleY),true);
            this.leftToRights[col] = this.createSubImageAt(ROW_LEFT_TO_RIGHT, col);
            this.leftToRights[col]=Bitmap.createScaledBitmap(this.leftToRights[col],Math.round(gameSurface.scaleY),Math.round(gameSurface.scaleY),true);
            this.bottomToTops[col]  = this.createSubImageAt(ROW_BOTTOM_TO_TOP, col);
            this.bottomToTops[col]=Bitmap.createScaledBitmap(this.bottomToTops[col],Math.round(gameSurface.scaleY),Math.round(gameSurface.scaleY),true);
        }

        int nbRandom=(int) (Math.random()*9);
        this.valeur=nbRandom+1;
        int ligne;
        int colonne;
        if (nbRandom<5) {
            ligne=0;
            colonne=nbRandom;
        } else  {
            ligne=1;
            colonne=nbRandom-5;
            //valeur=valeur-2;
        }

      // Log.i("GameSchibi","random " +String.valueOf(ligne)+","+String.valueOf(colonne));
      // Log.i("GameSchibi","random valeur" +String.valueOf(this.valeur));
        switch (ligne){
            case 1:
                this.bitmapImage=this.bottomToTops[colonne];
                break;
            case 0:
                this.bitmapImage=this.leftToRights[colonne];
                break;
        }



      //  this.bitmapImage=this.leftToRights[5];

    }

    public Bitmap[] getMoveBitmaps()  {
        int ligne=(int) (Math.random()*2);
        //int colonne=(int) (Math.random()*6);
        switch (ligne)  {
            case 0:
                return  this.bottomToTops;
            case 1:
                return this.leftToRights;
       /*     case ROW_RIGHT_TO_LEFT:
                return this.rightToLefts;
            case ROW_TOP_TO_BOTTOM:
                return this.topToBottoms;
        */    default:
                return null;
        }
    }

    public Bitmap getCurrentMoveBitmap()  {
        Bitmap[] bitmaps = this.getMoveBitmaps();

        return bitmaps[this.colUsing];
    }


    public void update()  {
      //  this.setFixe();
   /*     Log.i("GameS updatchib",String.valueOf(x)
        +" "+String.valueOf(y)
        +" "+String.valueOf(this.fixe)); */
         Log.i("GameS","entree chibi update");
        Log.i("GameS","est fixe ? "+String.valueOf(this.fixe));
        Log.i("GameS","en dessous de sortie "+String.valueOf(this.gameSurface.tableauWhichChibi.get(gameSurface.nombreBlocs/2).get(1).getChibiValeur()));
        Log.i("GameS","gameSurface nopudate ? "+String.valueOf(  this.gameSurface.noUpdate));
        Log.i("GameS","chibi x "+String.valueOf(this.x));
        Log.i("GameS","chibi y "+String.valueOf(this.y));
        Log.i("GameS","compteurExplode "+String.valueOf(this.gameSurface.compteurExplode));
        if (!this.fixe ) {



      //      Log.i("GameS", "daans chibi update "+String.valueOf(!this.fixe));
            this.colUsing++;
            this.colUsing = 0;
            if (colUsing >= this.colCount) {
                this.colUsing = 0;
            }
            // Current time in nanoseconds
            long now = System.nanoTime();

            // Never once did draw.
            if (lastDrawNanoTime == -1) {
                lastDrawNanoTime = now;
            }
            // Change nanoseconds to milliseconds (1 nanosecond = 1000000 milliseconds).
            int deltaTime = (int) ((now - lastDrawNanoTime) / 1000000);
            cumulTime += deltaTime;

       //     Log.i("GameS","CumulTime= "+String.valueOf(cumulTime));
            // Distance moves
            float distance = VELOCITY * deltaTime;

           if (gameSurface.longPressY) {
               if (this.getY()>0) {
                cumulTime=this.gameSurface.level+1;
            }
           }

            double movingVectorLength = Math.sqrt(movingVectorX * movingVectorX + movingVectorY * movingVectorY);
            if (cumulTime > this.gameSurface.level && !this.gameSurface.noUpdate && this.y <9 && this.gameSurface.tableauWhichChibi.get(x).get(y+1).getChibiValeur()==10 ) {
                // Calculate the new position of the game character.
                this.gameSurface.echangeChibiTab(this.gameSurface.tableauWhichChibi,this.x,this.y,this.x,this.y+1);
            //  Log.i("GameS", "doit descendre");
                cumulTime = 0;
                this.gameSurface.setActifChibiY(this.gameSurface.getActifChibiY()+1);




            }

            if (cumulTime > this.gameSurface.level &&   this.gameSurface.noUpdate && this.gameSurface.compteurExplode==0) {
                int point;
                Log.i("GameSa","attendre niveau "+String.valueOf(this.gameSurface.attendNiveauSuivant));
                Log.i("GameS","cumultime : "+String.valueOf(cumulTime));
                this.gameSurface.oldScore=this.gameSurface.scoreGame;
                int step=Math.round(cumulTime/350);

                //pour eviter de rentrer au pas 1 avec un cumulTime deja trop grand !
                if (this.gameSurface.nextStep==1 && step>1){
                    step=1;
                    cumulTime=350;
                }


            //    Log.i("GameS","affichage time step= "+String.valueOf(step));
             //   Log.i("GameS","affichage next step= "+String.valueOf(this.gameSurface.nextStep));
                switch (step){
                    case 1:
                        if (this.gameSurface.nextStep == step) {
                            this.gameSurface.delTabListe(this.gameSurface.tableauWhichChibi, this.gameSurface.getToDel());
                            this.gameSurface.nextStep += 1;
                        }
                        break;
                    case 2: if (this.gameSurface.nextStep == step) {
                        this.gameSurface.faireDescendreChibi(this.gameSurface.tableauWhichChibi);
                        if (this.gameSurface.encoreDescendre) {
                            this.gameSurface.nextStep += 1;
                        } else {
                            this.gameSurface.nextStep=1;
                            cumulTime=0;
                            gameSurface.noUpdate=false;
                            gameSurface.bestTrickReset=true;

                            this.gameSurface.attendNiveauSuivant++;
                        }
                     //   Log.i("Gale","Noupdate apres step 2 "+String.valueOf(this.gameSurface.noUpdate));
                    }
                    case 3: if (this.gameSurface.nextStep == step) {
                        point = this.gameSurface.compteDixChibi(this.gameSurface.tableauWhichChibi);
                        if (point==0) {
                            this.gameSurface.nextStep=1;
                            cumulTime=0;
                            gameSurface.noUpdate=false;
                            gameSurface.bestTrickReset=true;
                            this.gameSurface.attendNiveauSuivant++;}
                        else {
                            this.gameSurface.nextStep+=1;
                            this.gameSurface.addScoreGame(point*2);
                            this.gameSurface.bestTrick+=point*2;
                            this.gameSurface.etoileDraw=10;
                           // Log.i("Gale","Noupdate apres step 3, gain "+String.valueOf(point*2));
                        }

                    }
                }
                if (this.gameSurface.nextStep>3) {
                  //  Log.i("GameS","affichage time step etape 2= "+String.valueOf((step-4)%3));
                   // Log.i("GameS","affichage time step etape 2= "+String.valueOf((this.gameSurface.nextStep-4)%3));
                    switch ((step-4)%3) {
                        case 0:
                            if ((this.gameSurface.nextStep-4)%3 == (step-4)%3) {
                                this.gameSurface.delTabListe(this.gameSurface.tableauWhichChibi, this.gameSurface.getToDel());
                                this.gameSurface.nextStep += 1;
                            }
                        case 1:
                            if ((this.gameSurface.nextStep-4)%3 == (step-4)%3) {
                                this.gameSurface.faireDescendreChibi(this.gameSurface.tableauWhichChibi);
                                this.gameSurface.nextStep += 1;
                            }
                        case 2:
                            if ((this.gameSurface.nextStep-4)%3 == (step-4)%3) {
                                point = this.gameSurface.compteDixChibi(this.gameSurface.tableauWhichChibi);
                                if (point == 0) {
                                    this.gameSurface.nextStep=1;
                                    cumulTime = 0;
                                    gameSurface.noUpdate = false;
                                    gameSurface.bestTrickReset=true;
                                   // this.gameSurface.attendNiveauSuivant++;;
                                } else {
                                    this.gameSurface.addScoreGame(point*3);
                                    this.gameSurface.bestTrick+=point*3;
                                    this.gameSurface.nextStep += 1;
                                    Log.i("Gale","Noupdate apres step bis 3, gain "+String.valueOf(point*3));
                                }
                            }
                    }
                }
                //this.gameSurface.setNoUpdate(false);
                Log.i("GameS","sortie de step");
            }





            if (this.gameSurface.tableauWhichChibi.get(gameSurface.nombreBlocs/2).get(1).getChibiValeur()!=10
                    && this.gameSurface.tableauWhichChibi.get(gameSurface.nombreBlocs/2).get(0).getChibiValeur()!=10) {
                this.gameSurface.gameOver=true;

            }


        }


    }

    public void draw(Canvas canvas)  {
        int xBord=gameSurface.xBordTableau;
        //Bitmap bitmap = this.getCurrentMoveBitmap();
        canvas.drawBitmap(this.bitmapImage,xBord+x*this.gameSurface.scaleY, 50+y*this.gameSurface.scaleY, null);
        // Last draw time.
        this.lastDrawNanoTime= System.nanoTime();
        this.movingVectorX=0;
    }

    public void setMovingVector(int movingVectorX, int movingVectorY)  {
        this.movingVectorX= movingVectorX;
        this.movingVectorY = movingVectorY;
    }

    public void setMovingVectorX(int movingVectorX)  {
        this.movingVectorX= movingVectorX;

    }

    public void setMovingVectorY(int yy) {
        this.movingVectorY=yy;
    }

    public int getMovingVectorX()  {
        return(this.movingVectorX);

    }

    public int getMovingVectorY()  {
        return(this.movingVectorY);

    }

    public int getChibiXSize() {
        return chibiXSize+marge;
    }

    public int getChibiYSize() {
        return chibiYSize+marge;
    }

    public void setChibiTableauY(int yy) {
        this.tableauY=yy;
    }



    public void setChibiTableauX(int yy) {
        this.tableauX=yy;
    }

    public void addChibiTableauX(int xx) {
        this.tableauX=xx+this.tableauX;
    }

    public int getChibiTableauX() {return(this.tableauX);}

    public int getChibiTableauY() {return(this.tableauY);}

    public int getChibiValeur() {return(this.valeur);}

    public void setFixe() {
        this.fixe=true;
    }

    public void setFixe(boolean b) {
        this.fixe=false;
    }

    public boolean getFixe(){
        return(this.fixe);
    }

    public Bitmap getBitmapImage(){
        return(this.bitmapImage);
    }

    public void setBitmapImage(Bitmap bitm) {
        this.bitmapImage=bitm;
    }

    public ChibiCharacter getChibi(){
        return(this);
    }

    public void setCumulTimeToDescent() {
        this.cumulTime=this.gameSurface.level+1;
    }

    public ArrayList<ChibiCharacter> echangeChibi(ChibiCharacter chibi1,ChibiCharacter chibi2) {
        ChibiCharacter tamp=chibi1.getChibi();
        ArrayList<ChibiCharacter> liste=new ArrayList<ChibiCharacter>();
        liste.add(chibi2);
        liste.add(chibi1);
        return(liste);

    }
}
