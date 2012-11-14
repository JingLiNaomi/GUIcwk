package drawapp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

public class MainWindow extends Stage
{
  public static final int DEFAULT_WIDTH = 500;
  public static final int DEFAULT_HEIGHT = 300;

  private int width;
  private int height;

  private ImagePanel imagePanel;
  private Label  messageView;
  private Button  quitButton,stepButton,nextButton;
  private ScrollPane scrollPane;
  private Scene scene;
  
  public MainWindow()
  {
    this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
   
  }

  public MainWindow(int width, int height)
  {
    super.setTitle("Draw App");
    super.setWidth(width+20);
    super.setHeight(height+200);
    this.width = width;
    this.height = height;
    this.setMinWidth(250);
    buildGUI();
  }

  private void buildGUI()
  {
    BorderPane backPanel = new BorderPane();
    imagePanel = new ImagePanel(width, height);
    backPanel.setCenter(imagePanel);

    messageView = new Label();
    messageView.setWrapText(true);
    scrollPane = new ScrollPane();
    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
    scrollPane.setContent(messageView);
    BorderPane lowerPanel = new BorderPane();
    lowerPanel.setCenter(scrollPane);

    BorderPane buttonPanel = new BorderPane();
    quitButton = new Button("Close Window");
    stepButton = new Button("Step through");
    nextButton = new Button("Next");
    buttonPanel.setRight(quitButton);
    buttonPanel.setLeft(stepButton);
    buttonPanel.setCenter(nextButton);
    nextButton.setVisible(false);
    quitButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
    @Override 
    public void handle(javafx.event.ActionEvent e) {
        closeActionPerformed();
    }});
   
    lowerPanel.setBottom(buttonPanel);

    backPanel.setBottom(lowerPanel);
    Group root = new Group();
    scene=new Scene(root,DEFAULT_WIDTH,DEFAULT_HEIGHT);
    this.setScene(scene);
    root.getChildren().add(backPanel);
  }

  public ImagePanel getImagePanel()
  {
    return imagePanel;
  }

  public void postMessage(final String s)
  {
     messageView.setText(messageView.getText()+'\n'+s);
  }

  public void closeActionPerformed()
  {
      this.close();
      System.exit(0);
  }
  protected Button getStepButton()
  {return stepButton;}
  
  protected Button getNextButton()
  {return nextButton;}
  
  protected void setSize(int width, int height)
  {
      imagePanel.setSize(width,height);
      this.setWidth(width+20);
      this.setHeight(height+180);
  }

    void saveImage(String url) {
        WritableImage wImage=new WritableImage(width,height);
        scene.snapshot(wImage);
              generateImageFile(wImage,url);
    }
            private void generateImageFile(WritableImage image,String url){
                try {
                    BufferedImage bimg = convertToAwtImage(image);
                    File outputfile = new File(url);
                    ImageIO.write(bimg, "png", outputfile);
                } catch (IOException e) {
                   System.out.println("");
                }
        }

        private java.awt.image.BufferedImage convertToAwtImage(javafx.scene.image.Image fxImage) {
                if (Image.impl_isExternalFormatSupported(BufferedImage.class)) {
                        java.awt.image.BufferedImage awtImage = new BufferedImage((int)fxImage.getWidth(), (int)fxImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
                        return (BufferedImage)fxImage.impl_toExternalImage(awtImage);
                } else {
                        return null;
                }
        }

}