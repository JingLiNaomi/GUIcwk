package drawapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;

public class Parser
{
  private BufferedReader reader;
  private ImagePanel image;
  private MainWindow frame;
  private int lineCounter;
  private ArrayList<String> instructions;
  public Parser(BufferedReader reader, ImagePanel image, MainWindow frame)
  {
    this.reader = reader;
    this.image = image;
    this.frame = frame;
    instructions = new ArrayList<String>();
  }

  public void parse() throws IOException
  {
    iniStepThroughBtns();
    try
    {
      reader.reset();
      reader.mark(2000);
      lineCounter=0;
      String line = reader.readLine();
      while (line != null)
      {
        instructions.add(line);
        parseLine(line);
        line = reader.readLine();
        lineCounter++;
      }
    }
    catch (IOException e)
    {
      frame.postMessage("Parse failed.");
      return;
    }
    catch (ParseException e)
    {
      frame.postMessage("Parse Exception: " + e.getMessage());
      frame.postMessage("Caused by instruction: " + instructions.get(lineCounter));
      return;
    }
    frame.postMessage("Drawing completed");
  }
  public void parseStepThrough()
  {
      try
    {
      lineCounter=0;
      String line = reader.readLine();
      if (line != null)
      {  
        parseLine(line);
        lineCounter++;
      }
      else
      {
           frame.postMessage("Drawing completed");
      }
    }
    catch (IOException e)
    {
      frame.postMessage("Parse failed.");
      return;
    }
    catch (ParseException e)
    {
      frame.postMessage("Parse Exception: " + e.getMessage());
      return;
    }
  }
  private void parseLine(String line) throws ParseException
  {
    if (line.length() < 2) return;
    String command = line.substring(0, 2);
    if (command.equals("DL")) { drawLine(line.substring(2,line.length())); return; }
    if (command.equals("DR")) { drawRect(line.substring(2, line.length())); return; }
    if (command.equals("FR")) { fillRect(line.substring(2, line.length())); return; }
    if (command.equals("FO")) { fillOval(line.substring(2, line.length())); return; }
    if (command.equals("SC")) { setColour(line.substring(3, line.length())); return; }
    if (command.equals("DS")) { drawString(line.substring(3, line.length())); return; }
    if (command.equals("DA")) { drawArc(line.substring(2, line.length())); return; }
    if (command.equals("DO")) { drawOval(line.substring(2, line.length())); return; }
    if (command.equals("DI")) { drawImage(line.substring(2, line.length())); return; }
    if (command.equals("SG")) { setGradient(line.substring(2, line.length())); return; }
    if (command.equals("SS")) { setSize(line.substring(2, line.length())); return; }
    if (command.equals("SI")) { saveImage(line.substring(2, line.length())); return; }
    throw new ParseException("Unknown drawing command");
  }

  private void drawLine(String args) throws ParseException
  {
    int x1 = 0;
    int y1 = 0;
    int x2 = 0;
    int y2 = 0;

    StringTokenizer tokenizer = new StringTokenizer(args);
    x1 = getInteger(tokenizer);
    y1 = getInteger(tokenizer);
    x2 = getInteger(tokenizer);
    y2 = getInteger(tokenizer);
    image.drawLine(x1,y1,x2,y2);
  }

  private void drawRect(String args) throws ParseException
  {
    int x1 = 0;
    int y1 = 0;
    int x2 = 0;
    int y2 = 0;

    StringTokenizer tokenizer = new StringTokenizer(args);
    x1 = getInteger(tokenizer);
    y1 = getInteger(tokenizer);
    x2 = getInteger(tokenizer);
    y2 = getInteger(tokenizer);
    image.drawRect(x1, y1, x2, y2);
  }

  private void fillRect(String args) throws ParseException
  {
    int x1 = 0;
    int y1 = 0;
    int x2 = 0;
    int y2 = 0;

    StringTokenizer tokenizer = new StringTokenizer(args);
    x1 = getInteger(tokenizer);
    y1 = getInteger(tokenizer);
    x2 = getInteger(tokenizer);
    y2 = getInteger(tokenizer);
    image.fillRect(x1, y1, x2, y2);
  }

  private void fillOval(String args) throws ParseException
  {
    int x = 0;
    int y = 0;
    int w = 0;
    int h = 0;
    StringTokenizer tokenizer = new StringTokenizer(args);
    x = getInteger(tokenizer);
    y = getInteger(tokenizer);
    w = getInteger(tokenizer);
    h = getInteger(tokenizer);
    image.fillOval(x, y, w, h);
    }

  private void drawArc(String args) throws ParseException
  {
    int x = 0;
    int y = 0;
    int width = 0;
    int height = 0;
    int startAngle = 0;
    int arcAngle = 0;

    StringTokenizer tokenizer = new StringTokenizer(args);
    x = getInteger(tokenizer);
    y = getInteger(tokenizer);
    width = getInteger(tokenizer);
    height = getInteger(tokenizer);
    startAngle = getInteger(tokenizer);
    arcAngle = getInteger(tokenizer);
    image.drawArc(x, y, width, height, startAngle, arcAngle);
  }

  private void drawOval(String args) throws ParseException
  {
    int x1 = 0;
    int y1 = 0;
    int width = 0;
    int height = 0;

    StringTokenizer tokenizer = new StringTokenizer(args);
    x1 = getInteger(tokenizer);
    y1 = getInteger(tokenizer);
    width = getInteger(tokenizer);
    height = getInteger(tokenizer);
    image.drawOval(x1, y1, width, height);
  }

  private void drawString(String args) throws ParseException
  {
    int x = 0;
    int y = 0 ;
    String s = "";
    StringTokenizer tokenizer = new StringTokenizer(args);
    x = getInteger(tokenizer);
    y = getInteger(tokenizer);
    int position = args.indexOf("@");
    if (position == -1) throw new ParseException("DrawString string is missing");
    s = args.substring(position+1,args.length());
    image.drawString(x,y,s);
  }

  private void setColour(String colourName) throws ParseException
  {
      image.setColour(parseColour(colourName));
  }

  private void drawImage(String args) throws ParseException
  {
    int x = 0;
    int y = 0;
    int width = 0;
    int height = 0;
    String filename = null;

    StringTokenizer tokenizer = new StringTokenizer(args);
    x = getInteger(tokenizer);
    y = getInteger(tokenizer);
    width = getInteger(tokenizer);
    height = getInteger(tokenizer);
    filename = getString(tokenizer);
    image.drawImage(x, y, width, height, filename);
  }
    private void setGradient(String args) throws ParseException
  {
    int x1 = 0;
    int y1 = 0;
    int x2 = 0;
    int y2 = 0;
    int proportional=0; 
    String cycleMethod=""; 
    String startColour="";
    String endColour="";
        
    StringTokenizer tokenizer = new StringTokenizer(args);
    x1 = getInteger(tokenizer);
    y1 = getInteger(tokenizer);
    x2 = getInteger(tokenizer);
    y2 = getInteger(tokenizer);
    proportional = getInteger(tokenizer);
    cycleMethod=getString(tokenizer);
    startColour=getString(tokenizer);
    endColour=getString(tokenizer);
    image.setGradient(x1, y1, x2, y2, (proportional==1),parseCycleMethod(cycleMethod),parseColour(startColour),parseColour(endColour));
  }
  
   private void setSize(String args) throws ParseException
  {
      if(lineCounter>1)
          throw new ParseException("setSize() must be set first otherwise will use default size");
    int width = 0;
    int height = 0;
    StringTokenizer tokenizer = new StringTokenizer(args);
    width = getInteger(tokenizer);
    height = getInteger(tokenizer);
    frame.setSize(width, height);
  }
      private void saveImage(String args) throws ParseException
  {
    String url="";
    StringTokenizer tokenizer = new StringTokenizer(args);
    url = getString(tokenizer);
    frame.saveImage(url);
  }
   
  private int getInteger(StringTokenizer tokenizer) throws ParseException
  {
    if (tokenizer.hasMoreTokens())
      return Integer.parseInt(tokenizer.nextToken());
    else
      throw new ParseException("Missing Integer value");
  }
  private String getString(StringTokenizer tokenizer) throws ParseException
  {
    if (tokenizer.hasMoreTokens())
      return tokenizer.nextToken();
    else
      throw new ParseException("Missing Integer value");
  }  
    private Color parseColour(String colourName) throws ParseException
  {
    if (colourName.equals("black")) {return Color.BLACK;}
    if (colourName.equals("blue")) {return Color.BLUE;}
    if (colourName.equals("cyan")) {return Color.CYAN;}
    if (colourName.equals("darkgray")) {return Color.DARKGRAY;}
    if (colourName.equals("gray")) {return Color.GRAY;}
    if (colourName.equals("green")) {return Color.GREEN;}
    if (colourName.equals("lightgray")) {return Color.LIGHTGRAY;}
    if (colourName.equals("magenta")) {return Color.MAGENTA;}
    if (colourName.equals("orange")) {return Color.ORANGE;}
    if (colourName.equals("pink")) {return Color.PINK;}
    if (colourName.equals("red")) {return Color.RED;}
    if (colourName.equals("white")) {return Color.WHITE;}
    if (colourName.equals("yellow")) {return Color.YELLOW;}
    throw new ParseException("Invalid colour name");
  }
    private CycleMethod parseCycleMethod(String cycleMethod) throws ParseException
  {
    if (cycleMethod.equals("NO_CYCLE")) {return CycleMethod.NO_CYCLE;}
    if (cycleMethod.equals("REFLECT")) {return CycleMethod.REFLECT;}
    if (cycleMethod.equals("REPEAT")) {return CycleMethod.REPEAT;}
    throw new ParseException("Invalid cycleMethod name");
  }
  private void iniStepThroughBtns()
  {
      Button stepButton=frame.getStepButton();
      final Button nextButton=frame.getNextButton();
      stepButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
      @Override 
      public void handle(javafx.event.ActionEvent e) {
        //enable next button
      nextButton.setVisible(true);
              try {
                  //start over drawing
                  reader.reset();
              } catch (IOException ex) {
                  Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
              }
      image.clear(Color.GRAY);
      parseStepThrough();
      }});
      nextButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
      @Override 
      public void handle(javafx.event.ActionEvent e) {
       parseStepThrough();
    }});
   }


}
