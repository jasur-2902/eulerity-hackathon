package uz.shukurov.eulerityproject.Model;

public class FontFamily {

    // Creating Models
    private String name;
    private Font regular;
    private Font italic;
    private Font bold;
    private Font boldAndItalic;


    //Constructor
    public FontFamily(String familyName){

        this.name = familyName;

    }


    //Getter & Setter
    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Font getRegular() {
        return regular;
    }

    public void setRegular(Font regular) {
        this.regular = regular;
    }

    public Font getItalic() {
        return italic;
    }

    public void setItalic(Font italic) {
        this.italic = italic;
    }

    public Font getBold() {
        return bold;
    }

    public void setBold(Font bold) {
        this.bold = bold;
    }

    public Font getBoldAndItalic() {
        return boldAndItalic;
    }

    public void setBoldAndItalic(Font boldAndItalic) {
        this.boldAndItalic = boldAndItalic;
    }

    public boolean equals(String familyName) {

        return name == familyName;

    }


}
