package drawapp;

import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.ArcType;

public class ImagePanel extends Group
{
  private Canvas image;
  private GraphicsContext graphics;
  private double x,y;               //For turtle mode
  private boolean penDown;          //For turtle mode
  private double angle;             //Turtle mode: when turtle is facing up, angle=90
  public ImagePanel(int width, int height)
  {
    setImageSize(width, height);
  }

  private void setImageSize(int width, int height)
  {
    image = new Canvas((double)width, (double)height);
    graphics = image.getGraphicsContext2D();
    clear(Color.WHITE);
    paintComponent(graphics);
  }

  protected void paintComponent(GraphicsContext g)
  {
    g.setFill(Color.GRAY);
    g.fillRect(0, 0, image.getWidth(), image.getHeight());
    this.getChildren().add(image);
  }

  public void setBackgroundColour(Color colour)
  {
    graphics.setFill(colour);
    graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
    graphics.setFill(Color.BLACK);
  }

  public void clear(Color colour)
  {
    setBackgroundColour(colour);
  }

  public void setColour(Color colour)
  {
    graphics.setFill(colour);
    graphics.setStroke(colour);
  }

  public void drawLine(int x1, int y1, int x2, int y2)
  {
    graphics.strokeLine(x1, y1, x2, y2);
  }

  public void drawRect(int x1, int y1, int x2, int y2)
  {
    graphics.strokeRect(x1, y1, x2, y2);
  }

  public void fillRect(int x1, int y1, int x2, int y2)
  {
    graphics.fillRect(x1, y1, x2, y2);
  }
  
  public void fillOval(int x, int y, int w, int h) 
  {
        graphics.fillOval(x, y, w, h);
  }
  public void drawString(int x, int y, String s)
  {
    graphics.strokeText(s,x,y);
  }

  public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle)
  {
    graphics.strokeArc(x,y,width,height,startAngle,arcAngle,ArcType.OPEN);
  }

  public void drawOval(int x, int y, int width, int height)
  {
    graphics.strokeOval(x,y,width,height);
  }

   public void drawImage(int x, int y, int width, int height, String filename) {
        String url = ImagePanel.class.getResource(filename).toString();
        Image pic = new Image(url,width,height,false,true);
        graphics.drawImage(pic, x, y);
    }
   public void setGradient(int x1,int y1,int x2,int y2,boolean proportional,CycleMethod cycleMethod,Color startColour,Color endColour){
       LinearGradient gradient = new LinearGradient(x1, y1, x2, y2, proportional, cycleMethod, new Stop[] {
            new Stop(0, startColour),
            new Stop(1, endColour)
        });
       graphics.setFill(gradient);
   }

   public void setSize(int width, int height) {
        image.setWidth(width);
        image.setHeight(height);
        graphics.setFill(Color.GRAY);
        graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
    }

   public void startAt(int x, int y,int angle) {
        this.x=x;
        this.y=y;
        graphics.beginPath();
        graphics.moveTo(x, y);
        penDown=false;
        this.angle=angle;
        setColour(Color.YELLOW);
    }

   public void forward(int dist) {
        double dx=x+dist*Math.cos(angle/180*Math.PI);
        double dy=y-dist*Math.sin(angle/180*Math.PI);
        if(penDown)
        {
            graphics.lineTo(dx,dy);
            graphics.stroke();
        }
        else
        {
            graphics.moveTo(dx, dy);
        }
        x=dx;
        y=dy;
    }

   public void left(int angle) {
        this.angle+=angle;
    }

    public void right(int angle) {
        this.angle-=angle;
    }

    public void penUp() {
        penDown=false;
    }
    
    public void penDown() {
        penDown=true;
    }

}
