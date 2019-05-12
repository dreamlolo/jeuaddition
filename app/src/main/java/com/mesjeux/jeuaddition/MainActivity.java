package com.mesjeux.jeuaddition;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.AchievementsClient;
import com.google.android.gms.games.AnnotatedData;
import com.google.android.gms.games.EventsClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.LeaderboardsClient;
import com.google.android.gms.games.PlayersClient;
import com.google.android.gms.games.leaderboard.LeaderboardScore;
import com.google.android.gms.games.leaderboard.LeaderboardVariant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.Constraints;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import static android.support.constraint.Constraints.TAG;


import com.google.android.gms.ads.InterstitialAd;


public class MainActivity extends Activity       {




    GameSurface gameView;//extends SurfaceView
    FrameLayout game;// Sort of "holder" for everything we are placing
    LinearLayout GameButtons;//Holder for the buttons
    LinearLayout bestScores;
    private int soundWoosh;
    private SoundPool sounds;
    public String level;
    public String scoreBuffer;
    public Button butPause;
     public Button butBack;
     public Button butAgain;
     public TextView bestGameScore;
    public TextView bestTrickScore;
    public String getScoreLeaderBoard;

    private InterstitialAd mInterstitialAd;

    // Client used to sign in with Google APIs
    private GoogleSignInClient mGoogleSignInClient;
    GoogleApiClient mGoogleClient;
    // Client variables
    private AchievementsClient mAchievementsClient;
    private LeaderboardsClient mLeaderboardsClient;
    private EventsClient mEventsClient;
    private PlayersClient mPlayersClient;

    // request codes we use when invoking an external activity
    private static final int RC_UNUSED = 5001;
    private static final int RC_SIGN_IN = 9001;
    private static final int RC_LEADERBOARD_UI = 9004;
    private Typeface faceFont;
    // achievements and scores we're pending to push to the cloud
    // (waiting for the user to sign in, for instance)
    public boolean soundOnOff;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
     /*   GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
*/


     // Intersticiel en ouverture déplacée vers le lancement de l'activité.

 /*       AdRequest request = new AdRequest.Builder()
                .addTestDevice("3EE17F0CB486869757B626A6F2A24DC8")  // noteplus 5
                .build();

        MobileAds.initialize(this,
                "ca-app-pub-3940256099942544~3347511713");

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.i("ADD" ," no add");
        }
        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded(){
                mInterstitialAd.show();
            }

            @Override
            public void onAdClosed() {
                gameView.gamePause=false;
            }

        });
*/

        this.faceFont= ResourcesCompat.getFont(this,R.font.cherry_cream_soda);

        this.scoreBuffer="";
        Log.i("GameChild","Avant probleme");
        Intent myIntent = getIntent(); // gets the previously created intent
        this.level = myIntent.getStringExtra("level");
        this.soundOnOff=myIntent.getBooleanExtra("sound",true);
    Log.i("GameChild",String.valueOf(this.level));
        Log.i("GameMainActivity",String.valueOf(this.soundOnOff));
        this.getScoreLeaderBoard="";

        sounds = new SoundPool(10, AudioManager.STREAM_MUSIC,0);
        soundWoosh=sounds.load(this,R.raw.woosh,1);

        gameView = new GameSurface(this,this.level,soundOnOff);
           game = new FrameLayout(this);
           GameButtons = new LinearLayout(this);
           bestScores = new LinearLayout(this);


        butPause = new Button(this);
        butBack = new Button(this);
        butAgain = new Button(this);
        bestGameScore=new TextView(this);
        bestTrickScore=new TextView(this);

       // butOne.setText("Button");
        butPause.setBackgroundResource(R.drawable.pause);

        WindowManager wm = (WindowManager) this.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int xx = size.x;
        int yy = size.y;

    //  butOne.setId(10); //good to set ID to some random number because defaults new button id's all to same number

        //Define the layout parameter for the button to wrap the content for both width and height
        LinearLayout.LayoutParams b0 = new LinearLayout.LayoutParams(xx/10,xx/10);
        LinearLayout.LayoutParams b1 = new LinearLayout.LayoutParams(xx/10,xx/10);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                             LinearLayout.LayoutParams.MATCH_PARENT,
                               LinearLayout.LayoutParams.MATCH_PARENT);

        GameButtons.setLayoutParams(params);
        LinearLayout.LayoutParams b2 = new LinearLayout.LayoutParams(3*xx/10,xx/5);

        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);



      //  params2.gravity=RelativeLayout.ALIGN_PARENT_BOTTOM;

     //   GameButtons.setOrientation(LinearLayout.HORIZONTAL);
       bestScores.setOrientation(LinearLayout.VERTICAL);

        bestScores.setLayoutParams(params);
        // GameButtons.setWeightSum(1);

        GameButtons.addView(butPause);
        GameButtons.addView(butAgain);
        GameButtons.addView(butBack);
      //   b1..addRule(LinearLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
      //b1.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        bestScores.addView(bestGameScore);
        bestScores.addView(bestTrickScore);


       butPause.setLayoutParams(b1);
        butAgain.setLayoutParams(b1);
        butBack.setLayoutParams(b1);
        butAgain.setBackgroundResource(R.drawable.ic_loop);
        butBack.setBackgroundResource(R.drawable.ic_stopgame);

        b2.setMargins(0,xx/10+10,0,0);
        bestGameScore.setLayoutParams(b2);
        bestTrickScore.setLayoutParams(b2);


        bestTrickScore.setTextColor(Color.WHITE);
        bestGameScore.setTextColor(Color.WHITE);
        bestTrickScore.setTextSize(20);
        bestGameScore.setTextSize(20);
        bestGameScore.setTypeface(faceFont);
        bestTrickScore.setTypeface(faceFont);
     //   bestTrickScore.setText("Best Trick 1000");
      //  bestGameScore.setText("reccord 5000");

        game.addView(gameView);
        game.addView(GameButtons);

        try {
     /*       GoogleSignInOptions gso = new GoogleSignInOptions
                    .Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
                    .build();

            // Build a GoogleSignInClient with the options specified by gso.
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
            */


            // Check for existing Google Sign In account, if the user is already signed in
// the GoogleSignInAccount will be non-null.
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
            Log.i("GaleSign", "acount before signin ");
      //      signIn();

            Log.i("GaleSign", "acount after signin ");
            Log.i("Gale","Before getRecord");
            if (this.level.equals("adult")) {
                getRecordTrick(this, "CgkIs4CkmfkfEAIQBA");
                Log.i("Gale", "Before getRecord2");
                getRecord(this, "CgkIs4CkmfkfEAIQAg");
                Log.i("Gale", "after getRecord2");
            }
            else {
                getRecordTrick(this, "CgkIs4CkmfkfEAIQBg");
                Log.i("Gale", "Before getRecord2");
                getRecord(this, "CgkIs4CkmfkfEAIQBQ");
            }
            game.addView(bestScores);
        } catch(Exception e) {

        } finally {

        }



        // ADmob Section

        AdRequest request = new AdRequest.Builder()
                .addTestDevice("3EE17F0CB486869757B626A6F2A24DC8")  // noteplus 5
                .build();

        MobileAds.initialize(this,
                "ca-app-pub-3806743580112044~3214734124");

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3806743580112044/7248034828");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

     // setContentView(game);


         butPause.setOnClickListener(new OnClickListener() {
         public void onClick(View v) {
             playsound();
             pauseGame();

         }
     });
        butAgain.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                playsound();playAgain();
            }
        });
        butBack.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                playsound();back();
            }
        });

        // Set fullscreen
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Set No Title
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        this.setContentView(game);


      //  this.setContentView(new GameSurface(this));

    }

    public void setScoreLeaderBoard(int score){
 /*   Games.getLeaderboardsClient(this, GoogleSignIn.getLastSignedInAccount(this))
            .submitScore(getString(R.string.leaderboard_classementscoreleaderboard), score);  */
    /*    Games.getLeaderboardsClient(this,GoogleSignIn.getLastSignedInAccount(this))
                .submitScore("CgkIs4CkmfkfEAIQAw", 61);*/
    }

    public void pauseGame(){
        Log.i("Main","Pause");
        gameView.gamePause=!gameView.gamePause;
        if (gameView.gamePause) {
            this.butPause.setBackgroundResource(R.drawable.play);
        } else {
            this.butPause.setBackgroundResource(R.drawable.pause);
        }
    }


    public void playAgain(){

       // setScoreLeaderBoard(gameView.scoreGame);
        if (Math.random()<0.5) {
            newAdd();
        } else {
            gameView.newGame();
            if (gameView.gamePause) {
                this.butPause.setBackgroundResource(R.drawable.play);
            } else {
                this.butPause.setBackgroundResource(R.drawable.pause);
            }
        }
    }

    public void back(){
        Log.i("Game", "dans bouton back");
       // this.finishAffinity();

     //   game.removeAllViews();
     // gameView.gameThread.interrupt();
     //   Thread.currentThread().interrupt();

     Intent intent = new Intent(this, Menu.class);
        intent.putExtra("sound",soundOnOff);
      startActivity(intent);
       // gameView.setVisibility(View.INVISIBLE);

   //    gameView.surfaceDestroyed(gameView.getHolder());
        //gameView.quitter();
     //   gameView.gameThread.setRunning(false);


        //   this.finishAffinity();
     //   System.exit(0);




    }

    public void newAdd() {
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
            Log.i("ADDMainActivitity" ," yes add");
        } else {
            Log.i("ADDMainActivitity" ," no add");
            gameView.newGame();
            if (gameView.gamePause) {
                this.butPause.setBackgroundResource(R.drawable.play);
            } else {
                this.butPause.setBackgroundResource(R.drawable.pause);
            }
        }
        mInterstitialAd.setAdListener(new AdListener(){
         /*   @Override
            public void onAdLoaded(){
                mInterstitialAd.show();
            }
*/
            @Override
            public void onAdClosed() {
                gameView.gamePause=false;
                gameView.newGame();
                etatPauseBouton();
            }

        });

    }

    public void etatPauseBouton(){
        if (gameView.gamePause) {
            this.butPause.setBackgroundResource(R.drawable.play);
        } else {
            this.butPause.setBackgroundResource(R.drawable.pause);
        }
    }

        public void onPause(){
        super.onPause();
        Log.i("Main","Pause dans onPause");
            gameView.gamePause=true;

                this.butPause.setBackgroundResource(R.drawable.play);

    }

    public void onStop(){
        super.onStop();
        Log.i("Main","Pause dans onStop");
    }

    public void onDestroy() {
        super.onDestroy();
        Log.i("Main","Pause dans destroy");


    }
//@override
    public void onShowLeaderboardsRequested() {
        mLeaderboardsClient.getAllLeaderboardsIntent()
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {
                        startActivityForResult(intent, RC_UNUSED);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                       // handleException(e, "R.string.leaderboards_exception"));
                        Log.i("GameS","R.string.leaderboards_exception");
                    }
                });
    }

    /*
    @Override
    public void onEnteredScore(int requestedScore) {
        // Compute final score (in easy mode, it's the requested score; in hard mode, it's half)
        int finalScore = mHardMode ? requestedScore / 2 : requestedScore;

        mWinFragment.setScore(finalScore);
        mWinFragment.setExplanation(mHardMode ? getString(R.string.hard_mode_explanation) :
                getString(R.string.easy_mode_explanation));

        // check for achievements
        checkForAchievements(requestedScore, finalScore);

        // update leaderboards
        updateLeaderboards(finalScore);

        // push those accomplishments to the cloud, if signed in
        pushAccomplishments();

        // switch to the exciting "you won" screen
        switchToFragment(mWinFragment);

        mEventsClient.increment(getString(R.string.event_number_chosen), 1);
    }

    // Checks if n is prime. We don't consider 0 and 1 to be prime.
    // This is not an implementation we are mathematically proud of, but it gets the job done.
    private boolean isPrime(int n) {
        int i;
        if (n == 0 || n == 1) {
            return false;
        }
        for (i = 2; i <= n / 2; i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }
*/
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
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


    private void signInSilently() {
        GoogleSignInClient signInClient = GoogleSignIn.getClient(this,
                GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);
        signInClient.silentSignIn().addOnCompleteListener(this,
                new OnCompleteListener<GoogleSignInAccount>() {
                    @Override
                    public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                        if (task.isSuccessful()) {
                            // The signed in account is stored in the task's result.
                            GoogleSignInAccount signedInAccount = task.getResult();
                        } else {
                            // Player will need to sign-in explicitly using via UI
                            Log.i("GameSign","not sign in");
                        }
                    }
                });
    }

    private boolean isSignedIn() {
        return GoogleSignIn.getLastSignedInAccount(this) != null;
    }

    private void startSignInIntent() {
        GoogleSignInClient signInClient = GoogleSignIn.getClient(this,
                GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);
        Intent intent = signInClient.getSignInIntent();

        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(intent);
        if (result.isSuccess()) {
            // The signed in account is stored in the result.
            GoogleSignInAccount signedInAccount = result.getSignInAccount();
        } else {
            String message = result.getStatus().getStatusMessage();
            if (message == null || message.isEmpty()) {
                message = "R.string.signin_other_error20";
            }
            new AlertDialog.Builder(this).setMessage(message)
                    .setNeutralButton(android.R.string.ok, null).show();

        }
    }




/*
    private void showLeaderboard() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        Log.i("GaleSign","acount dans showleaderboard signin "+ account.getDisplayName());
        Games.getLeaderboardsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                .getLeaderboardIntent(getString(R.string.app_id))
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {
                        startActivityForResult(intent, RC_LEADERBOARD_UI);
                    }
                });
    }
*/
    private void showLeaderboard() {
    Games.getLeaderboardsClient(this, GoogleSignIn.getLastSignedInAccount(this))
            .getLeaderboardIntent(getString(R.string.leaderboard_best_trick))
            .addOnSuccessListener(new OnSuccessListener<Intent>() {
                @Override
                public void onSuccess(Intent intent) {
                    startActivityForResult(intent, RC_LEADERBOARD_UI);
                }
            });
}


public void getRecord(Context context,String idLeaderBoard){

    Log.i("Gale","Main activity dans getRecord");

 /*   GoogleSignInOptions gso = new GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
            .requestEmail()
            .build();
    // Build a GoogleSignInClient with the options specified by gso.
    mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    */

    // Check for existing Google Sign In account, if the user is already signed in
// the GoogleSignInAccount will be non-null.


  //  GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

    Log.i("Gale","Main activity dans getRecordBis");

    LeaderboardsClient test=Games.getLeaderboardsClient(this, GoogleSignIn.getLastSignedInAccount(this)) ;
    Log.i("Gale","Main activity dans getRecord1");
    //   String bestT=Long.toString( test.loadCurrentPlayerLeaderboardScore("CgkIs4CkmfkfEAIQBA", LeaderboardVariant.TIME_SPAN_ALL_TIME, LeaderboardVariant.COLLECTION_PUBLIC).getResult().get().getRawScore());
    //  Log.i("Gale","ancien score : " +bestT );

    //  mLeaderboardsClient = Games.getLeaderboardsClient(this, test);
    test.loadCurrentPlayerLeaderboardScore(idLeaderBoard, LeaderboardVariant.TIME_SPAN_ALL_TIME, LeaderboardVariant.COLLECTION_PUBLIC)
            .addOnSuccessListener(this, new OnSuccessListener<AnnotatedData<LeaderboardScore>>() {

                @Override
                public void onSuccess(AnnotatedData<LeaderboardScore> leaderboardScoreAnnotatedData) {
                    long score = 0L;
                    long rank=0L;
                    if (leaderboardScoreAnnotatedData != null) {
                        if (leaderboardScoreAnnotatedData.get() != null) {
                            score = leaderboardScoreAnnotatedData.get().getRawScore();
                            rank= leaderboardScoreAnnotatedData.get().getRank();
                            Log.i("Gale","Main activity Rang du joueur "+String.valueOf(rank));
                            sendToViewScore(Long.toString(score));
                            //Toast.makeText(MainActivity.this, Long.toString(score), Toast.LENGTH_SHORT).show();
                            Log.i("Gale", "Main activity LeaderBoard: " + Long.toString(score));
                        } else {
                          //   Toast.makeText(MainActivity.this, "no data at .get()", Toast.LENGTH_SHORT).show();
                            Log.i("Gale", "Main activity LeaderBoard: .get() is null");
                            sendToViewScore("?");
                        }
                    } else {
                       // Toast.makeText(MainActivity.this, "no data...", Toast.LENGTH_SHORT).show();
                        Log.i("Gale", "Main activity LeaderBoard: " + Long.toString(score));
                        sendToViewScore("?");
                    }
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                   // Toast.makeText(MainActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Main activity LeaderBoard: FAILURE");
                    sendToViewScore("?");
                }
            });


    }

    public void getRecordTrick(Context context,String idLeaderBoard){
        Log.i("Gale","Main activity dans getrecordTrick");
        LeaderboardsClient test=Games.getLeaderboardsClient(this, GoogleSignIn.getLastSignedInAccount(this)) ;
        //   String bestT=Long.toString( test.loadCurrentPlayerLeaderboardScore("CgkIs4CkmfkfEAIQBA", LeaderboardVariant.TIME_SPAN_ALL_TIME, LeaderboardVariant.COLLECTION_PUBLIC).getResult().get().getRawScore());
        //  Log.i("Gale","ancien score : " +bestT );

        //  mLeaderboardsClient = Games.getLeaderboardsClient(this, test);
        test.loadCurrentPlayerLeaderboardScore(idLeaderBoard, LeaderboardVariant.TIME_SPAN_ALL_TIME, LeaderboardVariant.COLLECTION_PUBLIC)
                .addOnSuccessListener(this, new OnSuccessListener<AnnotatedData<LeaderboardScore>>() {

                    @Override
                    public void onSuccess(AnnotatedData<LeaderboardScore> leaderboardScoreAnnotatedData) {
                        long score = 0L;
                        if (leaderboardScoreAnnotatedData != null) {
                            if (leaderboardScoreAnnotatedData.get() != null) {
                                score = leaderboardScoreAnnotatedData.get().getRawScore();
                                sendToViewTrick(Long.toString(score));
                                //Toast.makeText(MainActivity.this, Long.toString(score), Toast.LENGTH_SHORT).show();
                                Log.i("Gale", "Main activity LeaderBoard: " + Long.toString(score));
                            } else {
                               // Toast.makeText(MainActivity.this, "no data at .get()", Toast.LENGTH_SHORT).show();
                                Log.i("Gale", "Main activity LeaderBoard: .get() is null");
                                sendToViewTrick("?");
                            }
                        } else {
                            //Toast.makeText(MainActivity.this, "no data...", Toast.LENGTH_SHORT).show();
                            Log.i("Gale", "Main activity LeaderBoard: " + Long.toString(score));
                            sendToViewTrick("?");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Toast.makeText(MainActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                        Log.i("Gale", "LeaderBoard: FAILURE");
                        //sendToViewTrick("?");
                    }
                });


    }

    public void sendToViewScore(String s) {
        if (!s.equals("?")) {
            this.scoreBuffer += "\n" + "Best Score " + s;
            bestGameScore.setText(this.scoreBuffer);

        }
    }

    public void sendToViewTrick(String s) {
        if (!s.equals("?")) {
            this.scoreBuffer += "\n" + "Best Trick " + s;

            bestGameScore.setText(this.scoreBuffer);
        }
    }

    public void playsound() {
        if (soundOnOff) {
            sounds.play(soundWoosh, 1.0f, 1.0f, 0, 0, 1.5f);
        }
    }

    public boolean isSoundOnOff() {
        return(soundOnOff);
    }

}