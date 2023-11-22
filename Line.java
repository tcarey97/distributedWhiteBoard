// Full name: Thomas Carey
// Student ID: 831811

import java.awt.*;

public class Line extends ParentShape {
    public Line(int xPress, int yPress, int xRelease, int yRelease, String selectedColour) {
        super(xPress, yPress, xRelease, yRelease);
        setColour(selectedColour);
        calculateForPaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponents(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(this.shapeColour);
        g2.drawLine(xPress,yPress,xRelease,yRelease);
    }

    @Override
    public void moveUp(){
        this.yPress -= 5;
        this.yRelease -= 5;
    }
    @Override
    public void moveDown(){
        this.yPress += 5;
        this.yRelease += 5;
    }

    @Override
    public void moveLeft(){
        this.xPress -= 5;
        this.xRelease -= 5;
    }

    @Override
    public void moveRight(){
        this.xPress += 5;
        this.xRelease += 5;
    }

}