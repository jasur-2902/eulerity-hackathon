package uz.shukurov.eulerityproject.Model;

import android.util.Log;

import java.util.ArrayList;

public class CreatingFontFamilies {

    private FontFamily mFontFamilies[] = new FontFamily[20];
    private ArrayList<FontFamily> fonts = new ArrayList<FontFamily>();
    private int count = 0;

    public CreatingFontFamilies() {

    }

    //Adding new font family
    public void addFamily(FontFamily newFontFamily){
        this.mFontFamilies[count] = newFontFamily;
        fonts.add(newFontFamily);
        count++;
    }

    public ArrayList<FontFamily>  getFontFamily() {
        return fonts;
    }

    // constructor
    public void setFontFamily(FontFamily[] newFontFamily) {
        this.mFontFamilies = newFontFamily;
    }

    //Adding fonts to the families
    public void addFont (String name, Font font){
        for(int i=0; i<count; i++){
            if(mFontFamilies[i].getName().equals(name)){

                if (font.isBold() && font.isItalic()){
                    fonts.get(i).setBoldAndItalic(font);
                    mFontFamilies[i].setBoldAndItalic(font); }
                else if(!font.isItalic() && !font.isBold()){
                    fonts.get(i).setRegular(font);
                    mFontFamilies[i].setRegular(font); }
                else if (font.isBold()){
                    fonts.get(i).setBold(font);
                    mFontFamilies[i].setBold(font);}
                else if(font.isItalic()){
                    fonts.get(i).setItalic(font);
                    mFontFamilies[i].setItalic(font);}

            }
        }


    }

    public int getCount() {
        return count;
    }


    //Quick Test
    public void test(){
        for(int i=0; i<count; i++) {
            Log.d("TESTERDOINGNEWTEST", String.valueOf(this.mFontFamilies[i].toString()));
        }
    }

}
