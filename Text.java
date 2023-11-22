// Full name: Thomas Carey
// Student ID: 831811

import java.awt.*;

public class Text extends ParentShape {
    private String userText;

    public Text(String userText, int x, int y, String selectedColour) {
        super(x,y);
        this.userText = userText;
        setColour(selectedColour);

    }

    public void paintComponent(Graphics g) {
        super.paintComponents(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(this.shapeColour);
//        g2.setFont(new Font("Times New Roman", Font.PLAIN, 50));
        g2.drawString(this.userText, this.xPress, this.yPress);
    }


}