enum colour {black,blue,cyan,darkgray,gray,green,lightgray,magenta,orange,pink,red,white,yellow};
typedef enum colour colour;

void drawLine(int,int,int,int);
void drawRect(int,int,int,int);
void drawOval(int,int,int,int);
void drawArc(int,int,int,int,int,int);
void fillRect(int,int,int,int);
void drawString(char*,int,int);

void setColour(colour);
void drawImage(int,int,int,int,char*);
void setGradient(int,int,int,int,int,char*,char*,char*);
void setSize(int,int);
void saveImage(char*);
void fillOval(int,int,int,int);


void turtle();
void startAt(int,int,int);
void forward(int);
void left(int);
void right(int);
void penUp();
void penDown();