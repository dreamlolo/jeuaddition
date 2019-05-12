package com.mesjeux.jeuaddition;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import java.util.ArrayList;

public class ChibiCharacter2 {






        // Row index of Image are being used.


        public int colUsing;
        private int cumulTime=0;
        public int tableauX,tableauY,valeur;



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

        public int x,y;

        private GameSurface gameSurface;

        public ChibiCharacter2(GameSurface gameSurface,   int x, int y, ChibiConstructListImage chibiConstructList,int  valeur) {

            this.gameSurface= gameSurface;
            this.x=x;
            this.y=y;
            this.movingVectorX=0;
            this.movingVectorY=chibiYSize+marge;
            this.tableauX=new Integer (x);
            this.tableauY=new Integer (y);
            this.valeur=valeur;
            this.fixe=false;
            this.waitTime=1000;
            this.theoriqueMoveDone=false;

            this.bitmapImage=chibiConstructList.getchibiImage(valeur);

            Log.i("Menu","Chibi valeur"+String.valueOf(valeur));




        }

        public ChibiCharacter2(GameSurface gameSurface,  int x, int y,ChibiConstructListImage chibiConstructList) {


            this.gameSurface= gameSurface;
            this.x=x;
            this.y=y;

            this.chibiXSize=Math.round(gameSurface.scaleY)-marge*2;
            this.chibiYSize=Math.round(gameSurface.scaleY)-marge*2;
            this.movingVectorX=0;
            this.movingVectorY=chibiYSize+marge;
            this.tableauX=new Integer (1);
            this.tableauY=new Integer (0);
            this.valeur=0;
            this.fixe=true;
            this.waitTime=1000;

            int nbRandom=(int) (Math.random()*9);
            this.valeur=nbRandom+1;

            this.bitmapImage=chibiConstructList.getchibiImage(this.valeur);



            //  this.bitmapImage=this.leftToRights[5];

        }







        public void update()  {
            //  this.setFixe();
   /*     Log.i("GameS updatchib",String.valueOf(x)
        +" "+String.valueOf(y)
        +" "+String.valueOf(this.fixe)); */
            Log.i("GameUpdateChibi","entree chibi update");
            Log.i("GameUpdateChibi","est fixe ? "+String.valueOf(this.fixe));
            Log.i("GameUpdateChibi","valeur ? "+String.valueOf(this.getChibiValeur()));
            Log.i("GameUpdateChibi","en dessous de sortie "+String.valueOf(this.gameSurface.tableauWhichChibi.get(gameSurface.nombreBlocs/2).get(1).getChibiValeur()));
            Log.i("GameS","gameSurface nopudate ? "+String.valueOf(  this.gameSurface.noUpdate));
            Log.i("GameUpdateChibi","chibi x "+String.valueOf(this.x));
            Log.i("GameUpdateChibi","chibi y "+String.valueOf(this.y));
            Log.i("GameS","compteurExplode "+String.valueOf(this.gameSurface.compteurExplode));
            if (!this.fixe && !this.gameSurface.gameOver) {



                //      Log.i("GameS", "daans chibi update "+String.valueOf(!this.fixe));
                this.colUsing++;
                this.colUsing = 0;

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


                //descente des chibis !
                if (cumulTime > this.gameSurface.level && !this.gameSurface.noUpdate && this.y <9 &&
                        this.gameSurface.tableauWhichChibi.get(x).get(y+1).getChibiValeur()==10 ) {
                    // Calculate the new position of the game character.

                        //Log.i("GameUpdateChibi", "debut  echange");
                        this.gameSurface.echangeChibiTab(this.gameSurface.tableauWhichChibi, this.x, this.y, this.x, this.y + 1);
                        //  Log.i("GameS", "doit descendre");
                        cumulTime = 0;
                        this.gameSurface.setActifChibiY(this.gameSurface.getActifChibiY() + 1);
                    }
                    else {
                    if (this.y == 0 && this.gameSurface.tableauWhichChibi.get(this.x).get(1).getChibiValeur() != 10) {
                        Log.i("GameUpdateChibi", "descente");
                        Log.i("GameUpdateChibi", String.valueOf(this.y));
                        Log.i("GameUpdateChibi", String.valueOf(this.gameSurface.tableauWhichChibi.get(this.x).get(1).getChibiValeur()));

                        Log.i("GameUpdateChibi", "gameOVER");
                        this.gameSurface.gameOver = true;
                        this.gameSurface.publishScore=true;
                        this.gameSurface.animOverFini=false;
                    }
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





           /*     if (this.gameSurface.tableauWhichChibi.get(gameSurface.nombreBlocs/2).get(1).getChibiValeur()!=10
                        && this.gameSurface.tableauWhichChibi.get(gameSurface.nombreBlocs/2).get(0).getChibiValeur()!=10) {
                    this.gameSurface.gameOver=true;

                }
*/

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

        public ChibiCharacter2 getChibi(){
            return(this);
        }

        public Integer getX() {return(this.x);}
    public Integer getY() {return(this.y);}

        public void setCumulTimeToDescent() {
            this.cumulTime=this.gameSurface.level+1;
        }

        public ArrayList<com.mesjeux.jeuaddition.ChibiCharacter> echangeChibi(com.mesjeux.jeuaddition.ChibiCharacter chibi1, com.mesjeux.jeuaddition.ChibiCharacter chibi2) {
            com.mesjeux.jeuaddition.ChibiCharacter tamp=chibi1.getChibi();
            ArrayList<com.mesjeux.jeuaddition.ChibiCharacter> liste=new ArrayList<com.mesjeux.jeuaddition.ChibiCharacter>();
            liste.add(chibi2);
            liste.add(chibi1);
            return(liste);

        }
    }

