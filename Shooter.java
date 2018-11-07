import java.awt.event.*; 
import java.awt.*;
import javax.swing.*;

class Enemy
{
    int x;
    double y, gravity;
    Enemy(int x)
    {
        this.x = x;
        y = 0;
        gravity = 0.4;
    }
    
    public void update()
    {
        y += gravity;
    }
    
    public void drawEnemy(Graphics2D g)
    {
        g.setColor(Color.yellow);
        g.fillRect(x, (int)y, 5, 10);
    }
    
    public boolean outOfBounds()
    {
        if ((y + 10) > 270)
        return true;
        return false;
    }
}

class Gun
{   
    private double x1, x2, y1, y2;
    private int angle, radius;
    Gun()
    {
        x1 = 250;
        y1 = 250;
        angle = 270;
        radius = 30;
        update();
    }
    
    private void update()
    {
        x2 = radius * Math.cos(Math.toRadians(angle)) + x1;
        y2 = radius * Math.sin(Math.toRadians(angle)) + y1;
    }
    
    public double getX()
    {
        return x2;
    }
    
    public double getY()
    {
        return y2;
    }
    
    public void moveleft()
    {
        if (angle > 190)
        {
            angle -= 10;
            update();
        }
    }
    
    public void moveright()
    {
        if (angle < 350)
        {
            angle += 10;
            update();
        }
    }
    
    public void draw(Graphics2D g)
    {
        g.setColor(Color.white);
        g.fillArc(230, 250, 40, 40, 360, 180);
        g.drawLine((int)x1, (int)y1, (int)x2, (int)y2);
    }
    
}

class Bullet
{
    double x, y;
    double ix, iy;
    Bullet(double fx, double fy)
    {
        this.x = fx;
        this.y = fy;
        this.ix = fx;
        this.iy = fy;
    }
    
    public void update()
    {
        y -= 3;
        x = ((y - iy) * (ix - 250))/(iy - 250) + ix ;
    }
    
    public void drawBullet(Graphics2D g)
    {
        g.setColor(Color.green);
        g.fillOval((int)this.x,(int)this.y,4,4);
    }
    
    public boolean collision(Enemy e)
    {
        Rectangle r1=new Rectangle((int)x,(int)y,4,4);
        Rectangle r2=new Rectangle(e.x, (int)e.y, 5, 10);
        return r1.intersects(r2);
    }
}

class GamePanel extends JPanel implements  ActionListener , KeyListener
{
    Timer t;
    Bullet b;
    Gun gu;
    Label l;
    Enemy e;
    int points;
    public GamePanel()
    {  
        points = 0;
        setSize(500,500);
        setLayout(null);
        setFocusable(true);
        addKeyListener(this);
        setBackground(Color.black);
        gu = new Gun ();
        l = new Label("0" );
        l.setBounds(240, 400, 100, 20);
        l.setForeground(Color.white);
        l.setBackground(Color.black);
        l.setFont(new Font("Arial", Font.BOLD, 16));
        add(l);
        t=new Timer(1,this);
        t.start();
    }

    public void actionPerformed(ActionEvent ae)
    {
        l.setText(points + "");
        if (e == null)
        e = new Enemy ((int)(Math.random() * 500));
        else if (e.outOfBounds())
        e = new Enemy ((int)(Math.random() * 500));
        e.update();
        if (b != null && e != null)
        {
            if (b.collision(e))
            {
                b = null;
                e = null;
                points ++;
            }
            else
            {
                b.update();
            }
        }
        repaint();
    }

    public void paint(Graphics g)
    {
        l.setText(points + "");
        super.paintComponent(g);
        Graphics2D  g2d = (Graphics2D)g;
        g2d.setColor(Color.green);
        g2d.drawLine(0,270, 500, 270);
        gu.draw(g2d);
        if (b != null)
        b.drawBullet(g2d);
        if (e != null)
        e.drawEnemy(g2d);
    }

    public void keyTyped(KeyEvent ke)
    {
    }

    public void keyReleased(KeyEvent ke)
    {
    } 

    public void keyPressed(KeyEvent ke)
    {
        int a=ke.getKeyCode();
        switch(a)
        {
            case KeyEvent.VK_LEFT :
            gu.moveleft();
            break;
            case KeyEvent.VK_RIGHT :
            gu.moveright();
            break;
            case KeyEvent.VK_SPACE :
            b = new Bullet (gu.getX(), gu.getY());
            break;
        }
    }
}
class GameWindow extends JFrame
{
    public GameWindow()
    {
        getContentPane().add(new GamePanel());
        pack();
    }
}
public class Shooter
{
    public static void main()
    {
        GameWindow obj=new GameWindow();
        obj.setSize(500,500);
        obj.setTitle("Platformer Game");
        obj.setVisible(true);
        obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        obj.setResizable(false);
    }
}