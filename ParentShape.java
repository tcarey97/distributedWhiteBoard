// Full name: Thomas Carey
// Student ID: 831811

import javax.swing.*;
import java.awt.*;

public abstract class ParentShape extends JPanel {
    protected int xPress;
    protected int yPress;
    protected int xRelease;
    protected int yRelease;
    protected int width;
    protected int height;
    protected Color shapeColour = Color.black;

    public ParentShape(int xPress, int yPress, int xRelease, int yRelease){
        this.xPress = xPress;
        this.yPress = yPress;
        this.xRelease = xRelease;
        this.yRelease = yRelease;

    }

    public ParentShape(int x, int y){
        this.xPress = x;
        this.yPress = y;
    }

    public void calculateForPaint(){
        if (xRelease>=xPress && yRelease>=yPress){
            this.width = xRelease - xPress;
            this.height = yRelease - yPress;
        }

    }

    public void modifyShape(int newWidth, int newHeight){
        this.width = newWidth;
        this.height = newHeight;

    }

    public void moveUp(){
        this.yPress -= 5;
    }
    public void moveDown(){
        this.yPress += 5;
    }

    public void moveLeft(){
        this.xPress -= 5;
    }

    public void moveRight(){
        this.xPress += 5;
    }

    public void setColour(String selectedColor){

        if (selectedColor.equals("Black")){
             this.shapeColour = Color.black;
        } else if (selectedColor.equals("White")){
            this.shapeColour = Color.white;
        } else if (selectedColor.equals("Red")){
            this.shapeColour = Color.red;
        }else if (selectedColor.equals("Lime")){
            Color lime = new Color(0,255,0);
            this.shapeColour = lime;
        } else if (selectedColor.equals("Blue")){
            this.shapeColour = Color.blue;
        }else if (selectedColor.equals("Yellow")){
            this.shapeColour = Color.yellow;
        } else if (selectedColor.equals("Aqua")){
            this.shapeColour = Color.CYAN;
        }else if (selectedColor.equals("Magenta")){
            this.shapeColour = Color.magenta;
        } else if (selectedColor.equals("Silver")){
            this.shapeColour = Color.lightGray;
        }else if (selectedColor.equals("Gray")){
            this.shapeColour = Color.gray;
        } else if (selectedColor.equals("Maroon")){
            Color maroon = new Color(128,0,0);
            this.shapeColour = maroon;
        }else if (selectedColor.equals("Olive")){
            Color olive = new Color(85, 107, 47);
            this.shapeColour = olive;
        } else if (selectedColor.equals("Green")){
            this.shapeColour = Color.green;
        } else if (selectedColor.equals("Purple")){
            Color purple = new Color(119, 0, 200);
            this.shapeColour = purple;
        } else if (selectedColor.equals("Teal")){
            Color teal = new Color(0, 128, 128);
            this.shapeColour = teal;
        } else if (selectedColor.equals("Navy")){
            Color navy = new Color(2, 7, 93);
            this.shapeColour = navy;
        }

    }

    @Override
    public boolean equals(Object obj) {

        ParentShape otherShape = (ParentShape) obj;

        if (this.xPress == otherShape.xPress && this.yPress == otherShape.yPress && this.xRelease == otherShape.xRelease
        && this.yRelease == otherShape.yRelease && this.getName().equals(otherShape.getName())){
            return true;
        } else {
            return false;
        }
    }
}
