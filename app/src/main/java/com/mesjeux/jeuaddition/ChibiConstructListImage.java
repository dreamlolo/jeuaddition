package com.mesjeux.jeuaddition;

import android.graphics.Bitmap;
import android.util.Log;

public class ChibiConstructListImage extends  GameObject {
    public Bitmap listeChibiImage[];

    public ChibiConstructListImage(GameSurface gameSurface, Bitmap image, int x, int y) {
        super(image, 2, 5, x, y);
        this.listeChibiImage = new Bitmap[12];

        Bitmap bitmapImage;
        int marge=3;
        Bitmap[] topToBottoms = new Bitmap[colCount]; // 3
        Bitmap[] rightToLefts = new Bitmap[colCount]; // 3
        Bitmap[] leftToRights = new Bitmap[colCount]; // 3
        Bitmap[] bottomToTops = new Bitmap[colCount]; // 3
        int chibiXSize=Math.round(gameSurface.scaleY)-marge*2;
        int chibiYSize=Math.round(gameSurface.scaleY)-marge*2;

        int ROW_TOP_TO_BOTTOM = 0;
        int ROW_RIGHT_TO_LEFT = 1;
        int ROW_LEFT_TO_RIGHT = 0;
        int ROW_BOTTOM_TO_TOP = 1;



     //   Log.i("Menu","Chibi valeur"+String.valueOf(valeur));


        for(int col = 0; col< this.colCount; col++ ) {
            topToBottoms[col] = this.createSubImageAt(ROW_TOP_TO_BOTTOM, col);
            topToBottoms[col]=Bitmap.createScaledBitmap(topToBottoms[col],Math.round(gameSurface.scaleY) ,Math.round(gameSurface.scaleY),true);
            rightToLefts[col]  = this.createSubImageAt(ROW_RIGHT_TO_LEFT, col);
            rightToLefts[col]=Bitmap.createScaledBitmap(rightToLefts[col],Math.round(gameSurface.scaleY),Math.round(gameSurface.scaleY),true);
            leftToRights[col] = this.createSubImageAt(ROW_LEFT_TO_RIGHT, col);
            leftToRights[col]=Bitmap.createScaledBitmap(leftToRights[col],Math.round(gameSurface.scaleY),Math.round(gameSurface.scaleY),true);
            bottomToTops[col]  = this.createSubImageAt(ROW_BOTTOM_TO_TOP, col);
            bottomToTops[col]=Bitmap.createScaledBitmap(bottomToTops[col],Math.round(gameSurface.scaleY),Math.round(gameSurface.scaleY),true);
        }

        //    Log.i("Games","InitTabChib "+String.valueOf(valeur));
        bitmapImage = bottomToTops[1];
        for (int valeur=1;valeur<=10;valeur++) {
            int colonne, ligne;
            if (valeur >= 10) {
                //this.valeur = valeur;
                colonne = 4;
                bitmapImage = bottomToTops[colonne];
            } else {
                int nbRandom = valeur - 1;
                valeur = nbRandom + 1;

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
                        bitmapImage = bottomToTops[colonne];
                        break;
                    case 0:
                        bitmapImage = leftToRights[colonne];
                        break;
                }
            }

            listeChibiImage[valeur]=bitmapImage;
        }


    }

    public Bitmap getchibiImage(int index){
        return(this.listeChibiImage[index]);}

}

