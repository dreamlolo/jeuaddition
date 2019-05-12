package com.mesjeux.jeuaddition;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.*;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Button;
import android.content.Intent;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.LeaderboardsClient;
import com.google.android.gms.games.leaderboard.Leaderboard;
import com.google.android.gms.games.leaderboard.LeaderboardVariant;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.logging.Handler;

import static android.support.constraint.Constraints.TAG;



public class GameSurface extends SurfaceView implements SurfaceHolder.Callback {


    public GameThread gameThread;
    private SoundPool sounds;
    private int soundCheer;
    private int soundBarrelExploding;
    private int soundCarExplosion;
    private int soundWoosh;

    private boolean waitUpdate;
    private boolean sound=false;
    private boolean soundOnOff=false;
  //  private ChibiCharacter chibi1;
  //  private ArrayList <ChibiCharacter> ChibiList=new ArrayList<ChibiCharacter> ();
    private ArrayList <ArrayList<Integer>> tableau=new ArrayList<>();
    public ArrayList <ArrayList<ChibiCharacter2>> tableauWhichChibi=new ArrayList<>();
    private int screenXSize;
    private int screenYSize;
    public float scaleX;
    public float scaleY;
    public int ySeparation;

    public int scoreGame=0;
    public int oldScore=0;
    public int bestTrick=0;
    public boolean bestTrickReset=false;
    public int bestTrickReccord=0;
    public int bestScore=0;
    public int affichePlus=0;
    public String scorePlus="";

    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInAccount account;
    private LeaderboardsClient test;

    public boolean noUpdate=false;
    public ArrayList <ArrayList<Integer>> toDel =new ArrayList<>();
    public int actifChibiX=4;
    public int actifChibiY=0;
    public boolean encoreDescendre=true;
    public ArrayList<ArrayList<ArrayList<ChibiCharacter>>> memoire;
    public int nextStep;

    public String childOrAdult;
    public int level=500;
    public int niveau=1;
    public int attendNiveauSuivant=0;
    public int nombreBlocs;
    public int xBordTableau=50;
    public boolean longPress=false;
    public boolean longPressX=false;
    public boolean longPressY=false;

    public boolean publishScore=false;
    public boolean gameOver=false;
    public boolean animOverFini=false;
    public boolean gamePause=false;
    public boolean surfaceDestroy=false;
    public Bitmap explodeBitmap;
    public Bitmap[] listeImageExplode;
    public ExplodeCharacter explodeInstance;
    public int etoileDraw=0;


    public Bitmap chibiBitmap;
    public Bitmap chibiBitmapVide;
    public Bitmap[] listechibiBitmap;
    public Bitmap basImage;
    public Bitmap droiteImage;
    public Bitmap gaucheImage;
    public Bitmap niceImage;
    public Bitmap extraImage;
    public ChibiCharacter chibiInstance;
    public ChibiCharacter2 chibiType2;
    public ChibiConstructListImage chibiConstructListImage;
    public ChibiConstructListImage chibiConstructListImageScore;

    public ArrayList<ArrayList <Integer>>  copieDeToDel=new ArrayList<>();
    public Integer compteurExplode;
    public ArrayList<ArrayList<ExplodeCharacter>> tableauExplodeCharacter;
    public ArrayList<ChibiCharacter2> listeChiffreScore;

    private Button pause;
    private Typeface faceFont;

    public GameSurface(Context context,String typeJeu,Boolean soundon)  {
        super(context);

        // Make Game Surface focusable so it can handle events. .
        this.setFocusable(true);

        this.childOrAdult=typeJeu;
     //   Bitmap chibiBitmapVide = BitmapFactory.decodeResource(this.getResources(), R.drawable.numberimage);
        // Sét callback.
        this.getHolder().addCallback(this);

        this.faceFont= ResourcesCompat.getFont(this.getContext(),R.font.cherry_cream_soda);
        this.sound=false;
        this.soundOnOff=soundon;
        Log.i("GameSurface "," Sounds " +String.valueOf(soundOnOff));
        WindowManager wm = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int xx = size.x;
        int yy = size.y;
     /*   System.out.println("x");
        System.out.println(xx);
        System.out.println("y");
        System.out.println(yy); */
        this.screenXSize=xx;
        this.screenYSize=yy;
        this.ySeparation=(int) Math.round(yy*0.67);
        this.nombreBlocs=6;
        this.scaleX=(float) 0.9*screenXSize/800;
        this.scaleY=(float) 0.9*screenYSize/10;

        this.xBordTableau=Math.round(screenXSize/2-this.nombreBlocs*this.scaleY/2);

        this.actifChibiX=4;
        this.actifChibiY=0;
    //    System.out.println(scaleX);
       System.out.println(scaleY);

        this.noUpdate=false;
        this.memoire=new ArrayList<ArrayList<ArrayList<ChibiCharacter>>>();
        this.nextStep=1;
    Log.i("GameChid", "dans gamesS "+(this.childOrAdult));
        if (this.childOrAdult.equals("child")) {
            this.nombreBlocs=7;
            Log.i("GameChid", "coucou");
        }
        else {
            this.nombreBlocs=6;
        }
        this.level=500;
        this.gameOver=false;
        this.animOverFini=false;
        copieDeToDel=new ArrayList<>();
        this.compteurExplode=0;
        this.tableauExplodeCharacter=new ArrayList<ArrayList<ExplodeCharacter>>();
        this.listeChiffreScore=new ArrayList<ChibiCharacter2>();

  //      Button pause = new Button(this.getContext());
    //    pause.setWidth(300);
       // pause.setBackground(R.drawable.ic_pause_circle_outline_black_24dp);
      //  pause.setText("test");

   //    this.chibiBitmapVide = BitmapFactory.decodeResource(this.getResources(), R.drawable.numberimage);

        Bitmap chibiBitmap1 = BitmapFactory.decodeResource(this.getResources(),R.drawable.numberimage);
        this.chibiConstructListImage=new ChibiConstructListImage(this,chibiBitmap1,1,1);


     //   chibiType2=new ChibiCharacter2(this,1,1,8);
        chibiBitmap1 = BitmapFactory.decodeResource(this.getResources(),R.drawable.numberimagechiffres);
        this.chibiConstructListImageScore=new ChibiConstructListImage(this,chibiBitmap1,1,1);


        //SOUNDS
        sounds = new SoundPool(20, AudioManager.STREAM_MUSIC,0);
        soundCheer = sounds.load(context, R.raw.onepersoncheering, 1);
        soundBarrelExploding=sounds.load(context,R.raw.barrelexploding,1);
        soundCarExplosion=sounds.load(context,R.raw.carexplosion,1);
        soundWoosh=sounds.load(context,R.raw.woosh,1);


    }

    public void stopDrawThread(){
        if (this.gameThread == null){
            Log.d("Game", "DrawThread is null");
            return;
        }
        this.gameThread.setRunning(false);
        while (true){
            try{
                Log.d("Menu", "Request last frame");
                this.gameThread.join(5000);
                break;
            } catch (Exception e) {
                Log.e("Menu", "Could not join with draw thread");
            }
        }
        this.gameThread = null;
    }



    public ArrayList<ArrayList<Integer>> InitTableau(){
        ArrayList<ArrayList<Integer>> tab= new ArrayList<>();

        for (int i=0;i<10;i++) {
            //int[] tampon= {-1,-1};
            ArrayList<Integer> tampon=new ArrayList<Integer>();
            for (int j=0;j<10;j++){
                tampon.add(-1);
            }
            tab.add(tampon);

        }

        return(tab);
    }

    public ArrayList<ArrayList<ChibiCharacter2>> InitTableauChibiPosition(){
      /*  ArrayList<ArrayList<ChibiCharacter>> tab= new ArrayList<>();
      Bitmap chibiBitmap1 = BitmapFactory.decodeResource(this.getResources(),R.drawable.numberimage);

        for (int i=0;i<nombreBlocs;i++) {
            //int[] tampon= {-1,-1};
            ArrayList<ChibiCharacter> tampon=new ArrayList<ChibiCharacter>();
            for (int j=0;j<10;j++){
                tampon.add(new ChibiCharacter(this,chibiBitmap1,i,j,10));
               // Log.i("GameS" ,String.valueOf(tampon.get(j).bitmapImage));
            }
            tab.add(tampon);

        }

        this.actifChibiX=nombreBlocs/2;
        this.actifChibiY=0;

        tab.get(nombreBlocs/2).set(0,new ChibiCharacter(this,chibiBitmap1,nombreBlocs/2,0));
        tab.get(nombreBlocs/2).get(0).setFixe(false);


        return(tab); */
        ArrayList<ArrayList<ChibiCharacter2>> tab= new ArrayList<>();
       // Bitmap chibiBitmap1 = BitmapFactory.decodeResource(this.getResources(),R.drawable.numberimage);

        for (int i=0;i<nombreBlocs;i++) {
            //int[] tampon= {-1,-1};
            ArrayList<ChibiCharacter2> tampon=new ArrayList<ChibiCharacter2>();
            for (int j=0;j<10;j++){
                tampon.add(new ChibiCharacter2(this,i,j,chibiConstructListImage,10));
                // Log.i("GameS" ,String.valueOf(tampon.get(j).bitmapImage));
            }
            tab.add(tampon);

        }

        this.actifChibiX=nombreBlocs/2;
        this.actifChibiY=0;

        tab.get(nombreBlocs/2).set(0,new ChibiCharacter2(this,nombreBlocs/2,0,chibiConstructListImage));
        tab.get(nombreBlocs/2).get(0).setFixe(false);


        return(tab);
    }


    public void newGame(){

        //    System.out.println(scaleX);
        this.waitUpdate=false;
        this.niveau=1;
        this.attendNiveauSuivant=0;
        this.gamePause=true;
        this.noUpdate=true;
        this.toDel.clear();
        this.tableau=InitTableau();
        this.tableauWhichChibi=InitTableauChibiPosition();
        this.bestTrickReccord=0;
        this.bestTrick=0;
        this.bestTrickReset=false;
        this.etoileDraw=0;
        this.noUpdate=false;
        gamePause=false;
        this.sound=false;
        this.publishScore=false;

        copieDeToDel.clear();

        this.nextStep=1;
        this.level=500;
        this.gameOver=false;

        this.compteurExplode=0;

        Log.i("GameS","newGame?");
       scoreGame=0;
        oldScore=0;
        affichePlus=0;
         scorePlus="";

        if (sounds != null) {
            sounds.release();
            sounds = null;
            sounds = new SoundPool(20, AudioManager.STREAM_MUSIC,0);
            soundCheer = sounds.load(this.getContext(), R.raw.onepersoncheering, 1);
            soundBarrelExploding=sounds.load(this.getContext(),R.raw.barrelexploding,1);
            soundCarExplosion=sounds.load(this.getContext(),R.raw.carexplosion,1);
            soundWoosh=sounds.load(this.getContext(),R.raw.woosh,1);
        }

    }

    public void quitter(){

        ((MainActivity) getContext()).finish();
        System.exit(0);

    }




    public void update()  {
        int comptedix;

        if (gameOver && publishScore){
            publishScore=false;
            publierScore();

        }

        Log.i("GameUpdate","Etat de gameover dans entree updatesurface "+String.valueOf(gameOver));
        if (!gameOver ) {

            //sprite d'explosion mis à jours
            if (copieDeToDel.size() > 0) {
                this.noUpdate = true;
                //Log.i("GameS","dans explosion taille de copietoDel");
                if (this.compteurExplode != 12) {
                    for (int i = 0; i < copieDeToDel.size(); i++) {
                        this.tableauWhichChibi.get(copieDeToDel.get(i).get(0)).get(copieDeToDel.get(i).get(1)).setBitmapImage(explodeInstance.getExplodeImage(this.compteurExplode));
                    }
                    this.compteurExplode += 1;
                } else {
                    this.compteurExplode = 0;
                    for (int i = 0; i < copieDeToDel.size(); i++) {
                        this.tableauWhichChibi.get(copieDeToDel.get(i).get(0)).get(copieDeToDel.get(i).get(1)).setBitmapImage(chibiInstance.getBitmapImage());
                    }

                    copieDeToDel.clear();
                }
            }
        }

        //animation finale
        if (gameOver && !animOverFini) {
            this.noUpdate=true;
            Log.i("GameUpdate","dans explosion finale");
            if (this.compteurExplode != 24) {
                for (int i = 0; i < tableauWhichChibi.size(); i++) {
                    for (int j = 0; j < tableauWhichChibi.get(i).size(); j++) {
                        this.tableauWhichChibi.get(i).get(j).setBitmapImage(explodeInstance.getExplodeImage(compteurExplode/2));

                    }
                }
                this.compteurExplode += 1;
            } else {
                this.compteurExplode = 0;
                for (int i = 0; i < tableauWhichChibi.size(); i++)  {
                    for (int j = 0; j < tableauWhichChibi.get(i).size(); j++) {
                        this.tableauWhichChibi.get(i).get(j).setBitmapImage(chibiInstance.getBitmapImage());
                    }
                }
                animOverFini=true;


            }
        }

        //partie en cours continue
         if (!gameOver && !gamePause && !waitUpdate ) {

            waitUpdate=true;

            this.tableauWhichChibi.get(this.actifChibiX).get(this.actifChibiY).update();

     /*       Log.i("GameS", "dansGameSurface update"
                +String.valueOf(this.actifChibiX)
                    +" "+String.valueOf(this.actifChibiY)
                  + String.valueOf(this.tableauWhichChibi.get(this.actifChibiX).get(this.actifChibiY).getFixe()));
    */
            Log.i("GameUpdate","dans games update actif chiby "+String.valueOf(this.tableauWhichChibi.get(this.actifChibiX).get(this.actifChibiY).y  ));

             Log.i("GameUpdate",String.valueOf(this.actifChibiX));
             Log.i("GameUpdate",String.valueOf(this.actifChibiY));
            // Log.i("GameUpdate",String.valueOf(this.tableauWhichChibi.get(this.actifChibiX).get(this.actifChibiY + 1).getChibiValeur()));

     //si il y a un nombre en dessous ou bas du tableau
            if (this.tableauWhichChibi.get(this.actifChibiX).get(this.actifChibiY).y == 9 ||
                    this.tableauWhichChibi.get(this.actifChibiX).get(this.actifChibiY + 1).getChibiValeur() != 10 ) {
                this.longPressY=false;
                this.tableauWhichChibi.get(this.actifChibiX).get(this.actifChibiY).setFixe();
                if (soundOnOff) {
                    sounds.play(soundWoosh, 1.0f, 1.0f, 0, 0, 1.5f);
                }

                Log.i("GameUpdate", "lancement compteDix");
                comptedix= compteDixChibi(this.tableauWhichChibi);
                if (comptedix>0 && soundOnOff) {
                    sounds.play(soundCarExplosion,1.0f, 1.0f, 0, 0, 1.5f);
                }

                bestTrick=bestTrick+comptedix;
                scoreGame = scoreGame +comptedix;
                if (bestTrick>bestTrickReccord) {
                    bestTrickReccord=bestTrick;
                }
                Log.i("Gale","best trick "+String.valueOf(bestTrick));
                Log.i("Gale","best trick record: "+String.valueOf(bestTrickReccord));
                if (bestTrickReset) {
                    bestTrick=0;
                }

                this.decreaseLevel();

                    ChibiCharacter2 newChib = new ChibiCharacter2(this, nombreBlocs / 2, 0, chibiConstructListImage);
                    newChib.setFixe(false);
                    this.tableauWhichChibi.get(nombreBlocs / 2).set(0, newChib);
                    this.setActifChibiX(nombreBlocs / 2);
                    this.setActifChibiY(0);



             // sort le nouveau pion si possible et compte les points
                // if supprimé c'est update dans gamesurface qui vérifie si sortie possible
     /*           if ((this.tableauWhichChibi.get(nombreBlocs/2).get(1).getChibiValeur()==10 && this.actifChibiY!=0
                        && this.tableauWhichChibi.get(this.actifChibiX).get(this.actifChibiY).getFixe()) || comptedix!=0) {
      */            // !!   ChibiCharacter newChib = new ChibiCharacter2(this, chibiBitmapVide, nombreBlocs / 2, 0);




         /*         } else {
                        gameOver=true;
                        animOverFini=false;
                Log.i("GameUpdate", " mise gameover a true");



*/
            }


        }

    }




    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            //updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
        }
    }


    private boolean isSignedIn() {
        return GoogleSignIn.getLastSignedInAccount(this.getContext()) != null;
    }

public void publierScore() {
    if (isSignedIn()) {
        try {
            account = GoogleSignIn.getLastSignedInAccount(this.getContext());
            // Log.i("Gale ", String.valueOf(account.getEmail()));
            //      Games.getLeaderboardsClient(this.getContext(),account).submitScore("CgkIs4CkmfkfEAIQAg", this.scoreGame);
            if (this.childOrAdult.equals("adult")) {
                Games.getLeaderboardsClient(this.getContext(), account)
                        .submitScore(getResources().getString(R.string.leaderboard_meilleurs_scores), this.scoreGame);
                //          .submitScore("CgkIs4CkmfkfEAIQAg", this.scoreGame);
                Log.i("Gale", "Sortie game over inscription score  adult " + String.valueOf(this.scoreGame));
                //  Games.getLeaderboardsClient(this.getContext(),account).submitScore("CgkIs4CkmfkfEAIQAw", this.bestTrickReccord);
                Games.getLeaderboardsClient(this.getContext(), account)
                        .submitScore(getResources().getString(R.string.leaderboard_best_trick), this.bestTrickReccord);
                Log.i("Gale", "Sortie game over inscription besttrick adult " + String.valueOf(this.bestTrickReccord));
            } else {

                Games.getLeaderboardsClient(this.getContext(), account)
                        .submitScore(getResources().getString(R.string.leaderboard_meilleurs_scores_niveau_facile), this.scoreGame);
                //          .submitScore("CgkIs4CkmfkfEAIQAg", this.scoreGame);
                Log.i("Gale", "Sortie game over inscription score child " + String.valueOf(this.scoreGame));
                //  Games.getLeaderboardsClient(this.getContext(),account).submitScore("CgkIs4CkmfkfEAIQAw", this.bestTrickReccord);
                Games.getLeaderboardsClient(this.getContext(), account)
                        .submitScore(getResources().getString(R.string.leaderboard_meilleure_combinaison_niveau_facile), this.bestTrickReccord);
                Log.i("Gale", "Sortie game over inscription besttrick child " + String.valueOf(this.bestTrickReccord));

            }

        } finally {


        }
    }
}


    public boolean onTouchEvent(MotionEvent event) {
        //int action = event.getActionMasked();

        if (!gameOver && !gamePause) {
        Log.i("GameMvt", "dans entrée onSimpleTouchEvent");
        Log.i("GameMvt",String.valueOf(longPressY));
        Log.i("GameMvt",String.valueOf(screenYSize * 0.55));
        Log.i("GameMvt",String.valueOf(event.getX()+", "+event.getY()));

            //    if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (event.getAction() == event.ACTION_DOWN) {
                if (event.getY() < 8*this.screenYSize/10) {

                    longPressY = false;
                    int moveX;
                    int x = (int) Math.round((-this.xBordTableau + event.getX()) / scaleY);
                    int y = (int) Math.round((-50 + event.getY()) / scaleY);
                  //  int xtab = this.ChibiList.get(ChibiList.size() - 1).getChibiTableauX();
                   // int ytab = this.ChibiList.get(ChibiList.size() - 1).getChibiTableauY();
                 //   int movingVectorX = this.ChibiList.get(ChibiList.size() - 1).getMovingVectorX();

                    int xtab2 = actifChibiX;

                    int ytab2 = actifChibiY;
                    //        Log.i("GameS","actif chibi move x "+String.valueOf(xtab2));
                    //        Log.i("GameS","actif chibi move y "+String.valueOf(ytab2));
                    moveX = 0;
                    ChibiCharacter2 tampon = this.tableauWhichChibi.get(xtab2).get(ytab2);
                    if (x > xtab2 && xtab2 < nombreBlocs - 1 && !tampon.getFixe()) {
                                   Log.i("GameMvt","ontouchversdroite ");
                        if (this.tableauWhichChibi.get(xtab2 + 1).get(ytab2).valeur == 10) {
                            this.tableauWhichChibi = this.echangeChibiTab(this.tableauWhichChibi, xtab2, ytab2, xtab2 + 1, ytab2);
                            this.actifChibiX = this.actifChibiX + 1;
                        }

         /*         Log.i("GameS","ontouchversdroite "
                            +String.valueOf(this.tableauWhichChibi.get(xtab2).get(ytab2).getChibiTableauX()
                            +" "+String.valueOf(this.tableauWhichChibi.get(xtab2).get(ytab2).getChibiTableauY())
                    +" " + String.valueOf(this.tableauWhichChibi.get(xtab2).get(ytab2).getChibiValeur())));

                    Log.i("GameS","ontouchversdroite "
                            +String.valueOf(this.tableauWhichChibi.get(xtab2+1).get(ytab2).getChibiTableauX()
                            +" "+String.valueOf(this.tableauWhichChibi.get(xtab2+1).get(ytab2).getChibiTableauY())
                            +" " + String.valueOf(this.tableauWhichChibi.get(xtab2+1).get(ytab2).getChibiValeur())));
    */
                    }

                    if (x < xtab2 && xtab2 > 0 && !tampon.getFixe()) {
                        if (this.tableauWhichChibi.get(xtab2 - 1).get(ytab2).valeur == 10) {
                                            Log.i("GameMvt", "ontouchversgauche ");
                            this.tableauWhichChibi = this.echangeChibiTab(this.tableauWhichChibi, xtab2, ytab2, xtab2 - 1, ytab2);
                            this.actifChibiX = this.actifChibiX - 1;
                        }
                    }


                    //      Log.i("GameSurface2",String.valueOf(xtab)+" "+String.valueOf(ytab));
                    return true;
                } else {
                    if (this.tableauWhichChibi.get(this.actifChibiX).get(this.actifChibiY).y > 0) {
                        this.longPressY = true;
                    }
                    //   Log.i("GameMvt","bas ecran pressé...faire descendre pience");
                    //  Log.i("GameMvt","valeur de longPress"+String.valueOf(longPressY));

                }
                //   Log.i("GameMvt","Sortie"+String.valueOf(longPressX)+" "+String.valueOf(longPressY));
            }

        }
        return false;
    }


        @Override
        public void draw (Canvas canvas){
            super.draw(canvas);
   /*  for (int i=0;i<ChibiList.size();i++) {
            this.ChibiList.get(i).draw(canvas);;
        }   */
            // Log.i("GameS","draw " +String.valueOf(tableauWhichChibi.size()));


            for (int i = 0; i < this.tableauWhichChibi.size(); i++)
                for (int j = 0; j < this.tableauWhichChibi.get(i).size(); j++) {
              /*  Log.i("GameS","draw "
                        +String.valueOf(i)+", "
                        +String.valueOf(j) + " "
                        + String.valueOf(tableauWhichChibi.get(i).get(j).x)
                        +" "+String.valueOf(tableauWhichChibi.get(i).get(j).y)); */
                    this.tableauWhichChibi.get(i).get(j).draw(canvas);
                }
            Paint paint = new Paint();
            paint.setTypeface(faceFont);
            paint.setColor(Color.WHITE);

         //   canvas.drawLine(0, ySeparation, screenXSize, ySeparation, paint );
         //   canvas.drawLine(0, ySeparation+1, screenXSize, ySeparation+1, paint );
         //   canvas.drawLine(0, ySeparation+2, screenXSize, ySeparation+2, paint );
            paint.setColor(Color.parseColor("#FFB11E"));
            paint.setTextSize(screenXSize/20);

            //   paint.setTypeface(Typeface.create("Comic",Typeface.BOLD));

            canvas.drawText("Score",29 * screenXSize / 40, screenYSize/10,paint);
            // + this.scoreGame, 7 * screenXSize / 10, 120, paint);
            canvas.drawText("Level " + this.niveau, 29 * screenXSize / 40, 4*screenYSize/10, paint);


            if ((this.etoileDraw==1 || this.etoileDraw==10) && soundOnOff ) {
                sounds.play(soundCheer, 1.0f, 1.0f, 0, 0, 1.5f);
            }
            if (this.etoileDraw > 0) {

                etoileDraw++;
                if (etoileDraw > 8 && etoileDraw < 10) {
                    etoileDraw = 0;
                }
                if (etoileDraw > 25) {
                    etoileDraw = 0;
                }
                if (etoileDraw < 10) {
                    canvas.drawBitmap(niceImage, screenXSize*1/3, screenYSize/40, null);

                } else {
                    canvas.drawBitmap(extraImage, screenXSize*1/3, screenYSize/4, null);
                }

            }

          if (scoreGame>=0) {
                drawScore(scoreGame,canvas);
           }

            if (gameOver) {
                this.affichePlus = 0;
                paint.setTextSize(screenXSize/10);
                paint.setTypeface(faceFont);

                canvas.drawText("GAME OVER ", screenXSize*1/7, 6*screenYSize/10, paint);
                Log.i("GameSurface ","soundexplode "+String.valueOf(soundOnOff)) ;
                if (!sound && soundOnOff) {
                    sounds.play(soundBarrelExploding, 1.0f, 1.0f, 0, 0, 1.5f);
                    sound=true;
                }

            }

            waitUpdate=false;
        }




 public void drawScore(int points,Canvas canvas){
        int nombre=points;
        String test="";
        int valeurChiffre;
    int j=0;
     Log.i("Points ",test+"test2");
     if (points==0) {
         listeChiffreScore.get(9).x=7+(4-j);
         listeChiffreScore.get(9).draw(canvas);
     }

        while (points>0) {
            valeurChiffre=points%10;
            test=String.valueOf(valeurChiffre)+test;
            Log.i("Points ",test+"test3");

          //  Log.i("Points ",test+"test4");
         //  listeChiffreScore.get(points%10-1).x=11;
            if (valeurChiffre==0) {
                listeChiffreScore.get(9).x=7+(4-j);
                listeChiffreScore.get(9).draw(canvas);
            } else {
                listeChiffreScore.get(valeurChiffre-1).x=7+(4-j);
                listeChiffreScore.get(valeurChiffre - 1).draw(canvas);
            }
            points=points/10;
            j=j+1;
        }

        Log.i("Points final,  ",test);
 }




        // Implements method of SurfaceHolder.Callback
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i("Gale","surfacecreatd");
        {

        }


    if (!surfaceDestroy) {
        try {
            GoogleSignInOptions gso = new GoogleSignInOptions
                    .Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
                    .requestId()
                    .build();
            // Build a GoogleSignInClient with the options specified by gso.
            mGoogleSignInClient = GoogleSignIn.getClient(this.getContext(), gso);
            account = GoogleSignIn.getLastSignedInAccount(this.getContext());
        //    Log.i("Gale", "Login? "+ String.valueOf(account.getId()));
          //  Log.i("Gale", String.valueOf(account.getEmail()));
            Log.i("Sign", "dans surface created ok");

        } finally {
            Log.i("Sign", "dans surface created non ok");
        }
     //   Games.getLeaderboardsClient(this.getContext(), Games.getLeaderboardsClient(this.getContext(),mGoogleSignInClient) );

   /*     test=Games.getLeaderboardsClient(this.getContext(), GoogleSignIn.getLastSignedInAccount(this.getContext())) ;
        String bestT=Long.toString( test.loadCurrentPlayerLeaderboardScore("CgkIs4CkmfkfEAIQBA", LeaderboardVariant.TIME_SPAN_ALL_TIME, LeaderboardVariant.COLLECTION_PUBLIC).getResult().get().getRawScore());
        Log.i("Gale","ancien score : " +bestT ); */


//code propre pour traiter les  cas d'erreur
 /*       mLeaderboardsClient.loadCurrentPlayerLeaderboardScore(getString(R.string.leaderboard_id), LeaderboardVariant.TIME_SPAN_ALL_TIME, LeaderboardVariant.COLLECTION_PUBLIC)
                .addOnSuccessListener(this, new OnSuccessListener<AnnotatedData<LeaderboardScore>>() {
                    @Override
                    public void onSuccess(AnnotatedData<LeaderboardScore> leaderboardScoreAnnotatedData) {
                        long score = 0L;
                        if (leaderboardScoreAnnotatedData != null) {
                            if (leaderboardScoreAnnotatedData.get() != null) {
                                score = leaderboardScoreAnnotatedData.get().getRawScore();
                                Toast.makeText(MainActivity.this, Long.toString(score), Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "LeaderBoard: " + Long.toString(score));
                            } else {
                                Toast.makeText(MainActivity.this, "no data at .get()", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "LeaderBoard: .get() is null");
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "no data...", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "LeaderBoard: " + Long.toString(score));
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "LeaderBoard: FAILURE");
                    }
                });
*/


  /*      GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this.getContext())

                .addApi(Drive.API)
                .addScope(Drive.SCOPE_FILE)
                .build();
*/

  /*      Games.getLeaderboardsClient(this.getContext(), GoogleSignIn.getLastSignedInAccount(this.getContext()))
                .loadCurrentPlayerLeaderboardScore(   "ee"   ,
                        "CgkIs4CkmfkfEAIQBA", LeaderboardVariant.TIME_SPAN_ALL_TIME,
                        LeaderboardVariant.COLLECTION_PUBLIC);
*/


     this.attendNiveauSuivant = 0;
     this.chibiBitmapVide = BitmapFactory.decodeResource(this.getResources(), R.drawable.numberimage);
     //this.drawString("this is something I want people to <p color=\"#00FF00\">NOTICE</p>", x, y);
     Bitmap chibiBitmap1 = BitmapFactory.decodeResource(this.getResources(), R.drawable.numberimagechiffres);
     // chibiBitmap1 =scaleDown(chibiBitmap1, 20, true);
     //     Bitmap chibiBitmap = BitmapFactory.decodeResource(this.getResources(),R.drawable.numberimage);

     this.chibiInstance = new ChibiCharacter(this, chibiBitmapVide, xBordTableau, 50, 10);


  //   this.ChibiList.add(new ChibiCharacter(this, chibiBitmapVide, xBordTableau, 50));


 //    this.tableau = InitTableau();
     this.tableauWhichChibi = InitTableauChibiPosition();

     Bitmap bitmapExplosion = BitmapFactory.decodeResource(this.getResources(), R.mipmap.explosionverte);
     ;
     explodeInstance = new ExplodeCharacter(this, bitmapExplosion, xBordTableau, 50);

     for (int i = 1; i < 11; i++) {
         ChibiCharacter2 chibi = new ChibiCharacter2(this,  12, 1, chibiConstructListImageScore, i);
         listeChiffreScore.add(chibi);
     }


     this.basImage = BitmapFactory.decodeResource(getResources(), R.drawable.bas5);
     //   this.basImage=Bitmap.createBitmap(basImage,0,0, Math.round(screenXSize-5*scaleX-5*2*3), (int) Math.round(screenYSize*1/3));


     //   basImage=Bitmap.createScaledBitmap(basImage, Math.round((screenXSize-scaleY*5-5*2*3)/2), screenYSize*1/3,true);
     basImage = scaleDown(basImage, screenYSize * 1 / 3, true);

     //   basImage=scaleDown(basImage,screenYSize*1/3,true);
     this.droiteImage = BitmapFactory.decodeResource(getResources(), R.drawable.droite);
     droiteImage = scaleDown(droiteImage, screenYSize * 1 / 3, true);
     this.gaucheImage = BitmapFactory.decodeResource(getResources(), R.drawable.retour);
     gaucheImage = scaleDown(gaucheImage, screenYSize * 1 / 3, true);


     niceImage = BitmapFactory.decodeResource(getResources(), R.drawable.nice);
     extraImage = BitmapFactory.decodeResource(getResources(), R.drawable.extra);
     //     this.tableau.get(0);
     //    this.tableau.get(0).set(1,5);  //met un 5 ligne0 colonne5
    } else {
        surfaceDestroy=false;
    }

        this.gameThread = new GameThread(this,holder);
        this.gameThread.setRunning(true);
        this.gameThread.start();
    }

    // Implements method of SurfaceHolder.Callback
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            Log.i("Game","SurfaceChanged");
        surfaceDestroy=true;
    }

    // Implements method of SurfaceHolder.Callback

    public void surfaceDestroyedOld(SurfaceHolder holder) {

        boolean retry = true;
        Log.i("Game", "dans surfaceDestroyed");
        while (retry) {
            try {
                this.gameThread.setRunning(false);

                // Parent thread must wait until the end of GameThread.
                this.gameThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.i("Game", "dans surfaceDestroyed exception");
            }
            retry = true;
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    //   boolean retry= true;
        Log.i("GameS ","surface destroyed");
        surfaceDestroy=true;
   /*      this.getHolder().removeCallback(this);
        this.getHolder().getSurface().release();
        this.gameThread.surfaceDestroyed();*/
        this.gameThread.setRunning(false);
        Log.i("GameS ","surface destroyed2");
        stopDrawThread();
        Log.i("GameS ","surface destroyed3");
        // and release the surface
        holder.getSurface().release();
        Log.i("GameS ","surface destroyed4");
        holder = null;
      //  surfaceReady = false;
    /*    Log.i("Final","dans surfaceDestroyed");
      //  holder.getSurface().release();
        holder.getSurface().release();
        holder = null; */
        //surfaceReady = false;
        Log.i("Game","Destroyed5");

    }

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }

    public ArrayList<ArrayList<Integer>> getTableau(){
        return(tableau);
    }


    public Integer compteDixChibi(ArrayList<ArrayList<ChibiCharacter2>> tab) {
        int points = 0;
        int pointTotal;
        int bonus=0;
        int score;
        int k;
        this.oldScore=this.scoreGame;
        pointTotal=0;
        for (int i = 0; i < tab.size(); i++) {
            for (int j = 0; j < tab.get(i).size(); j++) {
                bonus=0;
                if (tab.get(i).get(j).getChibiValeur() == 10) {
                    score = 0;
                    bonus=0;
                } else {
                    score = tab.get(i).get(j).getChibiValeur();
                    bonus++; //chaque bloc rapporte 1 point

                }
                k = 0;

                while (score != 0 && (score < 10) && (j + k + 1 < tab.get(i).size()) && (tab.get(i).get(j + k + 1).getChibiValeur() != 10)) {
                    k = k + 1;
                    score += tab.get(i).get(j + k).getChibiValeur();
                    bonus++;
                    //        Log.i("GameS","i+k="+String.valueOf(i+k)+"   j="+String.valueOf(j));
                    //       Log.i("GameS","dans while,"+String.valueOf(score));
                }
                //       Log.i("GameS","after while");

                if (score == 10) {
                    //  Log.i("GameS","dans delHor score 10");
                    for (int z = 0; z < k + 1; z++) {
                        ArrayList<Integer> tampon = new ArrayList<Integer>();
                        tampon.add(i);
                        tampon.add(j+z);
                        toDel.add(tampon);
                        //   Log.i("GameS","dans delHor");
                    }
                    pointTotal+=10;
                    points +=  10 +bonus;
                    bonus=0;
                    //Log.i("GameSurface","horizontal ", i, " ", j);

                }
            }


        }
        for (int j=0;j<tab.get(0).size();j++) {
            for (int i = 0; i < tab.size(); i++) {
                bonus=0;
                if (tab.get(i).get(j).getChibiValeur() == 10) {
                    score = 0;

                } else {
                    score = tab.get(i).get(j).getChibiValeur();
                    bonus++;
                }
                k = 0;
                //      Log.i("GameS","avant while i+k="+String.valueOf(i)+"   j="+String.valueOf(j)+ "and score "+String.valueOf(score));
                while (score!=0 && score < 10 && i + k + 1 < tab.size() && tab.get(i+k+1).get(j).getChibiValeur()!= 10) {
                    k = k + 1;
                    score += tab.get(i+k).get(j).getChibiValeur();
                    bonus++;
                    //       Log.i("GameS","i+k="+String.valueOf(i)+"   j="+String.valueOf(j+k));
                    //     Log.i("GameS","dans while,"+String.valueOf(score));
                }
                if (score == 10) {
                    for (int z = 0; z < k + 1; z++) {
                        ArrayList<Integer> tampon = new ArrayList<Integer>();
                        tampon.add(i+z);
                        tampon.add(j);
                        toDel.add(tampon);
                        //  Log.i("GameS","dans delVert");
                    }

                    pointTotal+=10;
                    points += 10 +bonus;
                    bonus=0;
                    // print("vertical ",i," ",j)

                }
            }

        }


        Log.i("GameScore",String.valueOf(points));

        if (toDel.size()>0) {
            Log.i("GameS","toDelSize plus que zero, pointTotal="+String.valueOf(pointTotal));
            this.noUpdate=true;
           // this.decreaseLevel();
            copieDeToDel= clone(toDel);
            points+=5*((pointTotal-1)/10);
            bestTrickReset=false;

        }
        if (points>19) {
            etoileDraw=1;
        }
        return(points);
    }


    public ArrayList<ArrayList<Integer>> clone(ArrayList<ArrayList<Integer>> original){
        //Log.i("GameS","avant copie");
    //    afficheDel(original);
        //Log.i("GameS","apres affiche original copie");
        ArrayList<ArrayList<Integer>> copie=new ArrayList<ArrayList<Integer>>();
        for (int i=0;i<original.size();i++){
            ArrayList<Integer> tampon=new ArrayList<Integer>();

            tampon.add(original.get(i).get(0));
            tampon.add(original.get(i).get(1));
             copie.add(tampon);
            }


       // Log.i("GameS","après copie");
    //    afficheDel(copie);
        return(copie);

    }

    private void afficheDel (ArrayList<ArrayList<Integer>> liste){
        Log.i("GameS","dans affiche del");
       for (int i=0;i<liste.size();i++){


                Log.i("GameS",String.valueOf(liste.get(i).get(0))+" "+String.valueOf(liste.get(i).get(1)));

            }
            Log.i("GameS","Sortie  affiche del");
    }

    public Integer compteDix(ArrayList<ArrayList<Integer>> tab){
      //  ArrayList <ArrayList<Integer>> toDel=new ArrayList<ArrayList<Integer>>();
        int points=0;
        int score;
        int k;
   /*     Log.i("GameS","dans compteDix");
        Log.i("GameS",String.valueOf(tab.get(0).size()));
        Log.i("GameS",String.valueOf(tab.size()));  */
        for (int i=0;i<tab.get(0).size();i++) {
            for (int j = 0; j < tab.size(); j++) {

                if (tab.get(j).get(i) == -1) {
                    score = 0;
                } else {
                    score = tab.get(j).get(i);
                }
                k = 0;
            //    Log.i("GameS","before while");
            //    Log.i("GameS","avant while i+k="+String.valueOf(i)+"   j="+String.valueOf(j)+ "and score "+String.valueOf(score));
                while (score!=0 && (score < 10) && (i + k + 1 < tab.get(j).size()) && (tab.get(j).get(i + k + 1) != -1)) {
                    k = k + 1;
                    score += tab.get(j).get(i + k);
            //        Log.i("GameS","i+k="+String.valueOf(i+k)+"   j="+String.valueOf(j));
             //       Log.i("GameS","dans while,"+String.valueOf(score));
                }
         //       Log.i("GameS","after while");

                if (score == 10) {
                  //  Log.i("GameS","dans delHor score 10");
                    for (int z = 0; z < k + 1; z++) {
                        ArrayList<Integer> tampon = new ArrayList<Integer>();
                        tampon.add(i+z);
                        tampon.add(j);
                        toDel.add(tampon);
                     //   Log.i("GameS","dans delHor");
                    }
                    points = points + 10;
                        //Log.i("GameSurface","horizontal ", i, " ", j);

                }
            }
        }
       for (int i=0;i<tab.size();i++) {
            for (int j = 0; j < tab.get(0).size(); j++) {
                if (tab.get(j).get(i) == -1) {
                    score = 0;
                } else {
                    score = tab.get(j).get(i);
                }
                k = 0;
          //      Log.i("GameS","avant while i+k="+String.valueOf(i)+"   j="+String.valueOf(j)+ "and score "+String.valueOf(score));
                while (score!=0 && score < 10 && j + k + 1 < tab.size() && tab.get(j+k+1).get(i)!= -1) {
                    k = k + 1;
                    score += tab.get(j+k).get(i);
             //       Log.i("GameS","i+k="+String.valueOf(i)+"   j="+String.valueOf(j+k));
               //     Log.i("GameS","dans while,"+String.valueOf(score));
                }
                if (score == 10) {
                    for (int z = 0; z < k + 1; z++) {
                        ArrayList<Integer> tampon = new ArrayList<Integer>();
                        tampon.add(i);
                        tampon.add(j+z);
                        toDel.add(tampon);
                      //  Log.i("GameS","dans delVert");
                    }


                     points = points + 10;
                        // print("vertical ",i," ",j)

                }
            }
        }


    //#print(toDel)
     //   Log.i("GameSurface","il reste dans le tableau"+String.valueOf(tab.size()));
        if (toDel.size()>0) {
            Log.i("GameS","toDelSize plus que zero 2");
            this.noUpdate=true;

        }

       // Log.i("GameSurface","j'ai efface..."+String.valueOf(toDel.size()));
    //    afficheTableau(tab);
        return(points);

    }

    public void afficheTableau(ArrayList<ArrayList<Integer>> tab){
        for (int i=0;i<tab.size();i++){
            String s=new String(String.valueOf(i)+"    ");
            for (int j=0;j<tab.get(0).size();j++) {
                if (tab.get(i).get(j) > 0) {
                    s = s + "  " + String.valueOf(tab.get(i).get(j));
                } else {
                    s = s + " " + String.valueOf(tab.get(i).get(j));
                }
            }

            Log.i("GameSurface",s);
        }
        Log.i("GameSurface"," ");


    }


    public void delTabListe(ArrayList<ArrayList<ChibiCharacter2>> tab,ArrayList<ArrayList<Integer>> liste) {


        Bitmap chibiBitmap1 = BitmapFactory.decodeResource(this.getResources(),R.drawable.numberimage);
    //    Log.i("GameS","liste Size " + String.valueOf(liste.size()));
        for (int i=0;i<liste.size();i++) {
      //      Log.i("GameS","liste Size " + String.valueOf(liste.get(i).get(0)));
        //    Log.i("GameS","liste Size " + String.valueOf(liste.get(i).get(1)));
            tab.get(liste.get(i).get(0)).set(liste.get(i).get(1), new ChibiCharacter2(this,  liste.get(i).get(0), liste.get(i).get(1), chibiConstructListImage,10));
        }

        this.noUpdate=true;
        toDel.clear();
    }


    public void faireDescendreChibi(ArrayList<ArrayList<ChibiCharacter2>> tab) {
        int k,kk;
       // Log.i("GameS","Avant descendre");
        //afficheTableau(tab);
        this.encoreDescendre=false;
        for (int i = 0; i <tab.size(); i++) {
            for (int j=tab.get(i).size() - 2; j > 0; j--){

                if (tab.get(i).get(j+1).getChibiValeur() == 10 && tab.get(i).get(j).getChibiValeur() != 10) {
                    k = 0;
                   // Log.i("GameS","dans descendre1 "+String.valueOf(i)+" "+ String.valueOf(j));
                    while (k + j < tab.get(i).size()-1 && tab.get(i).get(j+k+1).getChibiValeur() == 10) {
                        k = k + 1;
                    }
                  //  Log.i("GameS","dans descendre1 "+String.valueOf(i+k-1)+" "+ String.valueOf(j))
                        tab=echangeChibiTab(tableauWhichChibi,i,  j,i,  j+k);
                        this.encoreDescendre=true;
                }
                        kk=0;
                 /*       while (kk < this.ChibiList.size()) {

                            Log.i("GameS faire descendre0",String.valueOf(ChibiList.get(kk).y));
                            Log.i("GameS faire descendre0",String.valueOf(ChibiList.get(kk).getChibiTableauX()));
                            Log.i("GameS faire descendre0",String.valueOf(ChibiList.get(kk).getChibiTableauY()));
                            if (ChibiList.get(kk).getChibiTableauX() == i && ChibiList.get(kk).getChibiTableauY() == j+k-1) {


                                ChibiList.get(kk).setChibiTableauY(j+k);
                                //   ChibiList.get(kk).y+=this.ChibiList.get(kk).getMovingVectorY();
                                ChibiList.get(kk).y+=15;
                                Log.i("GameS faire descendre2",String.valueOf(ChibiList.get(kk).y));
                                Log.i("GameS faire descendre3"," ");
                                //  Log.i("GameS","un chibi efface " +String.valueOf(liste.get(i).get(0))+" "+String.valueOf(liste.get(i).get(1)));

                            }
                            kk++;

                        } */



            }
        }
      //  Log.i("GameS","Apres descendre "+String.valueOf(encoreDescendre));
    }



      //  afficheTableau(tab);
       // Log.i("GameS","fin");



 /*   public void delTabListe2(ArrayList<ArrayList<Integer>> tab,ArrayList<ArrayList<Integer>> liste) {
        this.noUpdate=false;
        int k;
        boolean remove;

        for (int i=0;i<ChibiList.size();i++){
            Log.i("GameS",String.valueOf(ChibiList.get(i).getChibiTableauX())+" "
                    +String.valueOf(ChibiList.get(i).getChibiTableauY())+" "
                    +" valeur du chivbi"+ String.valueOf(ChibiList.get(i).getChibiValeur()));
        }

   //     Log.i("GameS","dans del taille de liste "+String.valueOf(liste.size()));
        for (int i = 0; i < liste.size(); i++) {
            tab.get(liste.get(i).get(1)).set(liste.get(i).get(0), -1);
            k=0;
            Log.i("GameS","recherche de a effacer"+String.valueOf(liste.get(i).get(0))+" "+String.valueOf(liste.get(i).get(1)));
            remove=false;
            while (k < this.ChibiList.size() && !remove) {

           //     Log.i("GameS",String.valueOf(ChibiList.get(k).getChibiTableauX())+" "+String.valueOf(ChibiList.get(k).getChibiTableauY()));
                if (ChibiList.get(k).getChibiTableauX() == liste.get(i).get(0) && ChibiList.get(k).getChibiTableauY() == liste.get(i).get(1)) {


                    ChibiList.remove(k);
                    Log.i("GameS","un chibi efface " +String.valueOf(liste.get(i).get(0))+" "+String.valueOf(liste.get(i).get(1)));
                    remove=true;
                }
                k++;

            }


        }
        afficheTableau(tab);
        toDel.clear();
    }

*/

    public  ArrayList<ArrayList<ChibiCharacter2 >>  echangeChibiTab(ArrayList<ArrayList<ChibiCharacter2 >> tab2,int xxtab,int yytab, int xxtab2,int yytab2) {
       // Bitmap chibiBitmap1 = BitmapFactory.decodeResource(this.getResources(),R.drawable.numberimage);
   /*   ChibiCharacter tampon11=tab2.get(xxtab).get(yytab).getChibi();
        ChibiCharacter tampon22=tab2.get(xxtab2).get(yytab2).getChibi();
        tampon11.y=(yytab2);
        tampon11.x=(xxtab2);
        tampon22.y=(yytab);
        tampon22.x=(xxtab);
       Bitmap tamponBitmap=tampon11.getBitmapImage();
        tampon11.setBitmapImage(tampon22.getBitmapImage());
        tampon22.setBitmapImage(tamponBitmap);
        tab2.get(xxtab).set(yytab,tampon22);
        tab2.get(xxtab2).set(yytab2,tampon11); */

        ChibiCharacter2 tampon11=tab2.get(xxtab).get(yytab).getChibi();
        ChibiCharacter2 tampon22=tab2.get(xxtab2).get(yytab2).getChibi();
        tampon11.y=(yytab2);
        tampon11.x=(xxtab2);
        tampon22.y=(yytab);
        tampon22.x=(xxtab);
     /*   Bitmap tamponBitmap=tampon11.getBitmapImage();
        tampon11.setBitmapImage(tampon22.getBitmapImage());
        tampon22.setBitmapImage(tamponBitmap); */
        tab2.get(xxtab).set(yytab,tampon22);
        tab2.get(xxtab2).set(yytab2,tampon11);

     //   Log.i("GameS","fin echange");
      //  Log.i("GameS",String.valueOf(yytab));
        return(tab2);
    }

    public ArrayList <ArrayList<Integer>> getToDel(){
        return (this.toDel);
    };

    public int getActifChibiX(){
        return (this.actifChibiX);
    }

    public int getActifChibiY(){
        return (this.actifChibiY);
    }

    public void setActifChibiX(int b){
        this.actifChibiX=b;
    }

    public void setActifChibiY(int b){
        this.actifChibiY=b;
    }

    public boolean getEncoreDescendre(){
        return(this.encoreDescendre);
    }

    public void setNoUpdate(boolean b){
        this.noUpdate=b;
    }

    public void addScoreGame (int points){
        this.scoreGame+=points;
        decreaseLevel();
    }

    public void decreaseLevel() {

        if (this.childOrAdult.equals("adult")) {
            if (this.attendNiveauSuivant == this.niveau + 1
                    || this.attendNiveauSuivant == 10) {
                if (this.level > 180) {
                    this.level *= 0.8;
                } else {
                    this.level *= 0.96;
                }
                this.niveau++;
                this.attendNiveauSuivant = 0;
            }
        } else {
            if (this.attendNiveauSuivant == 10) {
                if (this.level > 200) {
                    this.level *= 0.96;
                } else {
                    this.level *= 0.93;
                }
                this.niveau++;
                this.attendNiveauSuivant = 0;
            }

        }

    }



}

