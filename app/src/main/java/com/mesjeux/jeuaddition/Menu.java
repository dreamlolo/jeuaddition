package com.mesjeux.jeuaddition;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

import com.google.android.gms.games.AnnotatedData;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.LeaderboardsClient;
import com.google.android.gms.games.leaderboard.LeaderboardScore;
import com.google.android.gms.games.leaderboard.LeaderboardVariant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.Locale;

import static android.support.constraint.Constraints.TAG;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Menu extends AppCompatActivity {

    private GoogleSignInClient mGoogleSignInClient;
    private InterstitialAd mInterstitialAd;
    private Button mVictoryChildButton, mVictoryAdultButton,mrulebutton,mLaunchButton,mLaunchChildButton,mSoundButton;
    // request codes we use when invoking an external activity
    private static final int RC_UNUSED = 5001;
    private static final int RC_SIGN_IN = 9001;
    private static final int RC_LEADERBOARD_UI = 9004;
    private static final String leaderboardChildScore="CgkIs4CkmfkfEAIQBQ";
    private int soundWoosh;
    private SoundPool sounds;
    private Boolean connecte=false;
    public Boolean soundOnOff=true;



    // Client used to sign in with Google APIs


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



Log.i("GameMenu"," on create");
        Intent myIntent = getIntent(); // gets the previously created intent
        // Sample AdMob App ID: ca-app-pub-3940256099942544~3347511713





        //son section
        this.soundOnOff=myIntent.getBooleanExtra("sound",true);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

//Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

//set content view AFTER ABOVE sequence (to avoid crash)
        setContentView(R.layout.activity_menu);


       Button mQuit;

        mrulebutton=findViewById(R.id.buttonRules);
        mLaunchButton = findViewById(R.id.buttonLaunchAdultGame);
        mLaunchChildButton=findViewById(R.id.buttonLaunchChildGame);
        mVictoryChildButton=findViewById(R.id.buttonVictoryChild);
        mVictoryAdultButton=findViewById(R.id.buttonVictoryAdult);
        mSoundButton=findViewById(R.id.buttonSound);
      //  mGoogleJeuxInstall=findViewById(R.id.buttonGoogleJeuInstall);
     //  mGoogleJeuxInstall.setVisibility(View.INVISIBLE);
        mQuit=findViewById(R.id.buttonQuit);


        Log.i("GameJeu",String.valueOf(isSignedIn()));


    // ADmob Section

            AdRequest request = new AdRequest.Builder()
                    .addTestDevice("3EE17F0CB486869757B626A6F2A24DC8")  // noteplus 5
                    .build();

            MobileAds.initialize(this,
                    "ca-app-pub-3806743580112044~3214734124");

            mInterstitialAd = new InterstitialAd(this);
            mInterstitialAd.setAdUnitId("ca-app-pub-3806743580112044/7248034828");
            mInterstitialAd.loadAd(new AdRequest.Builder().build());


        signInSilently();
        Log.i("GameMenu ",String.valueOf(isSignedIn()));


   /*     mGoogleJeuxInstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                   startSignInIntent();
                    }

        });
*/

        if (soundOnOff) {
            mSoundButton.setBackgroundResource(R.drawable.ic_volume_down_black_24dp);

        }
        else {
            mSoundButton.setBackgroundResource(R.drawable.ic_volume_off_black_24dp);

        }

        sounds = new SoundPool(10, AudioManager.STREAM_MUSIC,0);

        soundWoosh=sounds.load(this,R.raw.woosh,1);

        mrulebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
               showImage();
                playsound();
            }


        });

        mSoundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                if (soundOnOff) {
                    mSoundButton.setBackgroundResource(R.drawable.ic_volume_off_black_24dp);

                }
                else {

                    mSoundButton.setBackgroundResource(R.drawable.ic_volume_down_black_24dp);
                }
               soundOnOff=!soundOnOff;

            }
        });


        mLaunchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mLaunchButton.setBackgroundResource(0);
                mLaunchButton.setText("  Wait  ");
                playsound();
                String choixNiveau="adult";

                launchAdmobAdult();

               // mLaunchButton.setBackgroundColor(Color.WHITE);
              /*  Intent newGame = new Intent(Menu.this, MainActivity.class);
                newGame.putExtra("level","adult");
                newGame.putExtra("sound",soundOnOff);
                startActivity(newGame); */

            }


        });

        mLaunchChildButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                playsound();
                mLaunchChildButton.setBackgroundResource(0);
                mLaunchChildButton.setText("  Wait  ");
                // mLaunchButton.setBackgroundColor(Color.WHITE);
              //  Intent newGame = new Intent(Menu.this, MainActivity.class);
                String choixNiveau="child";
              //  newGame.putExtra("level","child");
               // newGame.putExtra("sound",soundOnOff);



                launchAdmobChild();


                //startActivity(newGame);
            }


        });


        mVictoryAdultButton.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

             showLeaderboard();
                playsound();}
        });

        mVictoryChildButton.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                playsound();
                if (connecte) {
                    showLeaderboardChild();
                } else {
                    startSignInIntent();
                }
            }
        });



        mQuit.setOnClickListener (new View.OnClickListener() {
                                      @Override
                                      public void onClick(final View v) {playsound();
                                            finishAffinity();
                                      }
                                  });




    }

    public void launchAdmobAdult() {


       if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Intent newGame = new Intent(Menu.this, MainActivity.class);
            newGame.putExtra("level","adult");
            newGame.putExtra("sound",soundOnOff);
            startActivity(newGame);
        }
        mInterstitialAd.setAdListener(new AdListener(){
         /*   @Override
            public void onAdLoaded(){
                mInterstitialAd.show();
            } */

            @Override
            public void onAdClosed() {
                //gameView.gamePause=false;
                Intent newGame = new Intent(Menu.this, MainActivity.class);
                newGame.putExtra("level","adult");
                newGame.putExtra("sound",soundOnOff);
                startActivity(newGame);
            }

        });
    }

    public void launchAdmobChild() {


        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Intent newGame = new Intent(Menu.this, MainActivity.class);
            newGame.putExtra("level","child");
            newGame.putExtra("sound",soundOnOff);
            startActivity(newGame);
        }
        mInterstitialAd.setAdListener(new AdListener(){
         /*   @Override
            public void onAdLoaded(){
                mInterstitialAd.show();
            } */

            @Override
            public void onAdClosed() {
                //gameView.gamePause=false;
                Intent newGame = new Intent(Menu.this, MainActivity.class);
                newGame.putExtra("level","child");
                newGame.putExtra("sound",soundOnOff);
                startActivity(newGame);
            }

        });
    }



    public void playsound() {
        Log.i("GameMenu ","sound"+String.valueOf(soundOnOff));
        if (soundOnOff) {
            sounds.play(soundWoosh, 1.0f, 1.0f, 0, 0, 1.5f);
        }
    }


    //fenetre popup pour les règles
    public void showImage() {
       // Locale.getDefault().getDisplayLanguage();
        Log.i("Game","Langue= "+String.valueOf(Locale.getDefault().getLanguage()));

        Dialog builder = new Dialog(this);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //nothing;
            }
        });

        TextView textView=new TextView(this);
        textView.setBackgroundResource(R.drawable.rule);
        textView.setTextSize(15);
        textView.setTextColor(Color.WHITE);
        Typeface face = ResourcesCompat.getFont(this,R.font.cherry_cream_soda);
       // Typeface face=Typeface.createFromAsset(getAssets(),   "R.font.cherrycreamsoda.ttf ");
        textView.setTypeface(face);
        textView.setText(R.string.rule);
        textView.setMovementMethod(new ScrollingMovementMethod());
        textView.setPadding(50,50,50,50);

        ImageView imageView = new ImageView(this);
       // imageView.setImageURI(imageUri);
        imageView.setImageResource(R.drawable.rule);
     /*   builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        builder.show(); */

        RelativeLayout relativeLayout=new RelativeLayout(this);
      RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,      ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(0, 0, 0, 0);




       // relativeLayout.addView(textView);
        builder.setCanceledOnTouchOutside(true);
        builder.addContentView(textView, params);
        builder.show();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int displayWidth = displayMetrics.widthPixels;
        // The absolute height of the available display size in pixels.
        int displayHeight = displayMetrics.heightPixels;
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

        // Copy the alert dialog window attributes to new layout parameter instance
        layoutParams.copyFrom(builder.getWindow().getAttributes());

        // Set the alert dialog window width and height
        // Set alert dialog width equal to screen width 90%
        // int dialogWindowWidth = (int) (displayWidth * 0.9f);
        // Set alert dialog height equal to screen height 90%
        // int dialogWindowHeight = (int) (displayHeight * 0.9f);

        // Set alert dialog width equal to screen width 70%
        int dialogWindowWidth = (int) (displayWidth * 0.7f);
        // Set alert dialog height equal to screen height 70%
        int dialogWindowHeight = (int) (displayHeight * 0.7f);

        // Set the width and height for the layout parameters
        // This will bet the width and height of alert dialog
        layoutParams.width = dialogWindowWidth;
        layoutParams.height = dialogWindowHeight;

        // Apply the newly created layout parameters to the alert dialog window
        builder.getWindow().setAttributes(layoutParams);
    }

    public void getRecord( final Button button,String idLeaderBoard) {
        try {
       /*     Log.i("Gale", "dans getRecord");

            GoogleSignInOptions gso = new GoogleSignInOptions
                    .Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
                    //   .requestId()
                    .build();
            // Build a GoogleSignInClient with the options specified by gso.
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
            // Check for existing Google Sign In account, if the user is already signed in
// the GoogleSignInAccount will be non-null.
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

            Log.i("Gale", "dans getRecordBis");
*/
           LeaderboardsClient test = Games.getLeaderboardsClient(this, GoogleSignIn.getLastSignedInAccount(this));
          Log.i("Gale", "dans getRecord1");
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
                                    sendToViewScore(button, Long.toString(score));
                                    //Toast.makeText(MainActivity.this, Long.toString(score), Toast.LENGTH_SHORT).show();
                                    Log.i("GameJeu", "LeaderBoard: " + Long.toString(score));
                                } else {
                                    //   Toast.makeText(MainActivity.this, "no data at .get()", Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "LeaderBoard: .get() is null");
                                    sendToViewScore(button, "0");
                                }
                            } else {
                                // Toast.makeText(MainActivity.this, "no data...", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "LeaderBoard: " + Long.toString(score));
                                sendToViewScore(button, "?");
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                           // Toast.makeText(Menu.this, "Failure", Toast.LENGTH_SHORT).show();
                            Log.i("Game ","LeaderBoard: FAILURE");
                            sendToViewScore(button, "???");
                        }
                    });


        } catch (Exception e) {
            // this.mVictoryAdultButton.setVisibility(View.INVISIBLE);
            this.mVictoryChildButton.setVisibility(View.INVISIBLE);
            afficheConnect();
        }
        finally {}
    }

    public void sendToViewScore(Button button,String s) {
        button.setVisibility(View.VISIBLE);
        button.setText(s);
        Log.i("GameMenu","add scpre "+s);
    }

    private void showLeaderboard() {
        if (isSignedIn()) {
            Games.getLeaderboardsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                    .getLeaderboardIntent(getString(R.string.leaderboard_meilleurs_scores))
                    .addOnSuccessListener(new OnSuccessListener<Intent>() {
                        @Override
                        public void onSuccess(Intent intent) {
                            startActivityForResult(intent, RC_LEADERBOARD_UI);
                        }
                    });
        }
    }

    private void showLeaderboardChild() {
        if (isSignedIn()) {
            Games.getLeaderboardsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                    .getLeaderboardIntent(getString(R.string.leaderboard_meilleurs_scores_niveau_facile))
                    .addOnSuccessListener(new OnSuccessListener<Intent>() {
                        @Override
                        public void onSuccess(Intent intent) {
                            startActivityForResult(intent, RC_LEADERBOARD_UI);
                        }
                    });
        }
    }

 /*   private void startSignInIntent() {
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
                //   .requestId()
                .build();

        GoogleSignInClient signInClient = GoogleSignIn.getClient(this,
                GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);
        Intent intent = signInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
      //  getRecord(mVictoryAdultButton,"CgkIs4CkmfkfEAIQAg");
       // getRecord( mVictoryChildButton, getResources().getString(R.string.leaderboard_meilleurs_scores_niveau_facile));
    } */

    private void startSignInIntent() {
        GoogleSignInClient signInClient = GoogleSignIn.getClient(this,
                GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);
        Intent intent = signInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
        Log.i("GameMenu ","startsigniin intent");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // The signed in account is stored in the result.
                GoogleSignInAccount signedInAccount = result.getSignInAccount();
              //  mGoogleJeuxInstall.setVisibility(View.INVISIBLE);
              //  mVictoryChildButton.setVisibility(View.VISIBLE);
                mVictoryAdultButton.setVisibility(View.VISIBLE);
                getRecord(mVictoryAdultButton,"CgkIs4CkmfkfEAIQAg");
                getRecord( mVictoryChildButton, "CgkIs4CkmfkfEAIQBQ");
                afficheChild();
                connecte=true;
            } else {
                String message = result.getStatus().getStatusMessage();
                if (message == null || message.isEmpty()) {
                    message = "Problème de connexion à google play jeux. Vos scores ne seront pas enregistrés.";
                }
                new AlertDialog.Builder(this).setMessage(message)
                        .setNeutralButton(android.R.string.ok, null).show();

            }
        }
    }

    private boolean isSignedIn() {
        return GoogleSignIn.getLastSignedInAccount(this) != null;
    }



    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

  /*  private void signInSilently() {
        GoogleSignInClient signInClient = GoogleSignIn.getClient(this,
                GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);
        Boolean go=false;
        signInClient.silentSignIn().addOnCompleteListener(this,
                new OnCompleteListener<GoogleSignInAccount>() {
                    @Override
                    public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                        if (task.isSuccessful()) {
                            // The signed in account is stored in the task's result.
                            GoogleSignInAccount signedInAccount = task.getResult();
                            getRecord(mVictoryAdultButton,"CgkIs4CkmfkfEAIQAg");
                            getRecord( mVictoryChildButton, "CgkIs4CkmfkfEAIQBQ");
                            afficheChild();
                            connecte=true;
                        } else {

                             //  mGoogleJeuxInstall.setVisibility(View.VISIBLE);
                              mVictoryAdultButton.setVisibility(View.INVISIBLE);
                             //   mVictoryChildButton.setVisibility(View.INVISIBLE);
                                Log.i("GameJeu","non signe ou pas d'internet");
                                afficheConnect();
                                connecte=false;


                        }
                    }




                });
    }
*/

    private void signInSilently() {
        GoogleSignInOptions signInOptions = GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN;
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (GoogleSignIn.hasPermissions(account, signInOptions.getScopeArray())) {
            // Already signed in.
            // The signed in account is stored in the 'account' variable.
            //GoogleSignInAccount signedInAccount = account;
            getRecord(mVictoryAdultButton,"CgkIs4CkmfkfEAIQAg");
            getRecord( mVictoryChildButton, "CgkIs4CkmfkfEAIQBQ");
            afficheChild();
            connecte=true;
        } else {
            // Haven't been signed-in before. Try the silent sign-in first.
            GoogleSignInClient signInClient = GoogleSignIn.getClient(this, signInOptions);
            signInClient
                    .silentSignIn()
                    .addOnCompleteListener(
                            this,
                            new OnCompleteListener<GoogleSignInAccount>() {
                                @Override
                                public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                                    if (task.isSuccessful()) {
                                        // The signed in account is stored in the task's result.
                                        GoogleSignInAccount signedInAccount = task.getResult();
                                    } else {
                                        //  mGoogleJeuxInstall.setVisibility(View.VISIBLE);
                                        mVictoryAdultButton.setVisibility(View.INVISIBLE);
                                        //   mVictoryChildButton.setVisibility(View.INVISIBLE);
                                        Log.i("GameJeu","non signe ou pas d'internet");
                                        afficheConnect();
                                        connecte=false;
                                        // Player will need to sign-in explicitly using via UI.
                                        // See [sign-in best practices](http://developers.google.com/games/services/checklist) for guidance on how and when to implement Interactive Sign-in,
                                        // and [Performing Interactive Sign-in](http://developers.google.com/games/services/android/signin#performing_interactive_sign-in) for details on how to implement
                                        // Interactive Sign-in.
                                    }
                                }
                            });
        }
    }




    @Override
    protected void onResume() {
        super.onResume();
        mLaunchButton.setBackgroundResource(R.drawable.persojeujdditionadult4);
        //mLaunchButton.setText("V10");
        mLaunchChildButton.setBackgroundResource(R.drawable.persojeujdditionchild4);
        mLaunchChildButton.setText("");
        Log.i("GameMenu","onresume");


        if (sounds != null) {
            sounds.release();
            sounds = null;

            sounds = new SoundPool(10, AudioManager.STREAM_MUSIC,0);

            soundWoosh=sounds.load(this,R.raw.woosh,1);
        }

        Log.i("GameMenu onResume av",String.valueOf(isSignedIn()));
        signInSilently();
        Log.i("GameMenu onResume ap",String.valueOf(isSignedIn()));
    }

    public void afficheChild() {
        mVictoryChildButton.setTextSize(TypedValue.COMPLEX_UNIT_SP,40);
        mVictoryChildButton.setBackgroundResource(R.drawable.victoire);
        mVictoryChildButton.setShadowLayer(1F,6F,6F,Color.BLACK);
       // mVictoryChildButton.setAlpha( 0.75F);

    }

    public void afficheConnect() {
        mVictoryChildButton.setBackgroundResource(0);
      //  mVictoryChildButton.setBackgroundColor(Color.WHITE);
        mVictoryChildButton.setBackgroundResource((R.drawable.buttonwhite));
       // mVictoryChildButton.setAlpha( 1F);
        mVictoryChildButton.setShadowLayer(0F,0F,0F,0);
        mVictoryChildButton.setTextSize(TypedValue.COMPLEX_UNIT_SP,20 );
        mVictoryChildButton.setText("Cliquer ici pour ajouter un compte google play jeu. Vous pourrez ainsi enregistrer vos scores et les classements");

    }
}
