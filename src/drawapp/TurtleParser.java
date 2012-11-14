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

class TurtleParser {
  private BufferedReader reader;
  private ImagePanel image;
  private MainWindow frame;
  private int lineCounter;
  private ArrayList<String> instructions;
  
  public TurtleParser(BufferedReader reader, ImagePanel image, MainWindow frame)
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
    if (command.equals("SA")) { startAt(line.substring(2,line.length())); return; }
    if (command.equals("FW")) { forward(line.substring(2, line.length())); return; }
    if (command.equals("LT")) { left(line.substring(2, line.length())); return; }
    if (command.equals("RT")) { right(line.substring(2, line.length())); return; }
    if (command.equals("PU")) { penUp(); return; }
    if (command.equals("PD")) { penDown(); return; }
    throw new ParseException("Unknown drawing command");
  }

     private int getInteger(StringTokenizer tokenizer) throws ParseException
  {
    if (tokenizer.hasMoreTokens())
      return Integer.parseInt(tokenizer.nextToken());
    else
      throw new ParseException("Missing Integer value");
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

    private void startAt(String args) throws ParseException
    {
        int x = 0;
        int y = 0;
        int angle=0;
        StringTokenizer tokenizer = new StringTokenizer(args);
        x = getInteger(tokenizer);
        y = getInteger(tokenizer);
        angle = getInteger(tokenizer);
        image.startAt(x,y,angle);
    }

    private void forward(String args) throws ParseException
    {
        int dist = 0;
        StringTokenizer tokenizer = new StringTokenizer(args);
        dist = getInteger(tokenizer);
        image.forward(dist);
    }

    private void left(String args) throws ParseException
    {
        int angle = 0;
        StringTokenizer tokenizer = new StringTokenizer(args);
        angle = getInteger(tokenizer);
        image.left(angle);
    }

    private void right(String args) throws ParseException
    {
        int angle = 0;
        StringTokenizer tokenizer = new StringTokenizer(args);
        angle = getInteger(tokenizer);
        image.right(angle);    
    }

    private void penUp() throws ParseException
    {
        image.penUp();
    }

    private void penDown() throws ParseException
    {
        image.penDown();    
    }
}
