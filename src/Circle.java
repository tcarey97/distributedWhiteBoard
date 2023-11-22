//// Full name: Thomas Carey
//// Student ID: 831811

import java.awt.*;

public class Circle extends ParentShape {
    public Circle(int xPress, int yPress, int xRelease, int yRelease, String selectedColour) {
        super(xPress, yPress, xRelease, yRelease);
        setColour(selectedColour);
        calculateForPaint();
    }

    @Override
    public void calculateForPaint() {
        this.width = xRelease - xPress;
        this.height = this.width;

    }

    public void paintComponent(Graphics g) {
        super.paintComponents(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(this.shapeColour);
        g2.drawOval(xPress,yPress,this.width,this.height);


    }

}
