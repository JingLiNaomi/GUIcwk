
package drawapp;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import javafx.application.Application;
import javafx.stage.Stage;


public class DrawApp extends Application {
    @Override
    public void start(Stage stage) throws IOException{
        final MainWindow mainWindow = new MainWindow();
        ImagePanel imagePanel;
        imagePanel = mainWindow.getImagePanel();
        Reader reader = new InputStreamReader(System.in);
        BufferedReader bReader = new BufferedReader(reader);
        bReader.mark(2000);
        String line = bReader.readLine();
        if (line.equals("turtle"))
        {
            TurtleParser parser = new TurtleParser(bReader,imagePanel,mainWindow);
            parser.parse();
        }
        else
        {
            Parser parser = new Parser(bReader,imagePanel,mainWindow);
            parser.parse();
        }
        mainWindow.show();
    }
    
     public static void main(String[] args) {
        launch(args);
    }
 

  
}
