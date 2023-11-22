// Full name: Thomas Carey
// Student ID: 831811

import java.awt.*;

public class Oval extends ParentShape {
    public Oval(int xPress, int yPress, int xRelease, int yRelease, String selectedColour) {
        super(xPress, yPress, xRelease, yRelease);

        setColour(selectedColour);

        calculateForPaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponents(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(this.shapeColour);
        g2.drawOval(xPress,yPress,this.width,this.height);

    }

}