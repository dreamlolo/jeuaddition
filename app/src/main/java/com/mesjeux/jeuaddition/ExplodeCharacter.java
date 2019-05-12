package com.mesjeux.jeuaddition;


import android.graphics.Bitmap;

public class ExplodeCharacter extends  GameObject {

    public  Bitmap listeExplodeImage[];

    public ExplodeCharacter(GameSurface gameSurface, Bitmap image, int x, int y) {
        super(image, 3, 4, x, y);
        this.listeExplodeImage = new Bitmap[12];
        for (int col = 0; col < this.colCount; col++) {
            this.listeExplodeImage[col] = this.createSubImageAt(0, col);
            this.listeExplodeImage[col] = Bitmap.createScaledBitmap(this.listeExplodeImage[col], Math.round(gameSurface.scaleY), Math.round(gameSurface.scaleY), true);
        }
        for (int col = 0; col < this.colCount; col++) {
            this.listeExplodeImage[col + 4] = this.createSubImageAt(1, col);
            this.listeExplodeImage[col+4] = Bitmap.createScaledBitmap(this.listeExplodeImage[col + 4], Math.round(gameSurface.scaleY), Math.round(gameSurface.scaleY), true);
        }
        for (int col = 0; col < this.colCount; col++) {
            this.listeExplodeImage[col + 8] = this.createSubImageAt(2, col);
            this.listeExplodeImage[col + 8] = Bitmap.createScaledBitmap(this.listeExplodeImage[col + 8], Math.round(gameSurface.scaleY), Math.round(gameSurface.scaleY), true);
        }
    }

    public Bitmap getExplodeImage(int index){
        return(this.listeExplodeImage[index]);
    }

}




