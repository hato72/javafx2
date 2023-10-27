import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.paint.Color;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javafx.animation.AnimationTimer;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.text.Font;

import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;

public class App2 extends Application {
  private int initialSceneWidth  = 800;
  private int initialSceneHeight = 400;

  private MapAndChars mapAndChars;

  @Override
  public void start(Stage st) throws Exception {
    Group root = new Group();
    
    Scene scene = new Scene(root, initialSceneWidth, initialSceneHeight, Color.rgb(0,0,0));
    st.setTitle("Tile-based Game");
    st.setScene(scene);
    st.show();

    scene.setOnKeyPressed(this::keyPressed); //キーボードイベント処理
    
    mapAndChars = new MapAndChars(root);
    
    mapAndChars.drawScreen(30);
    
    // 再描画（1/60秒）時間ごとの処理を定義   
    AnimationTimer timer = new AnimationTimer() {
      long startTime = 0;
      @Override
      public void handle(long t) { // tは現在時刻（ナノ秒）を表す（時間に応じた処理を定義するなら使う）
        if (startTime == 0) {
          startTime = t;
        }
        mapAndChars.drawScreen(30 - (int) ((t - startTime) / 1000000000));
      }
    };

    timer.start();
  }

  public static void main(String[] a) throws Exception {
    launch(a);
  }

  public void keyPressed(KeyEvent e) { //キーが押されたときに呼び出される関数
    KeyCode key = e.getCode();
    int dir = -1;
    switch ( key ) {
      case LEFT: dir = 2; break;// left
      case RIGHT: dir = 0; break;// right
      case UP: dir = 1; break;// up
      case DOWN: dir = 3; break;// down
    }
    if ( dir >= 0 ) mapAndChars.boyMove(dir);
  }
}

class MapAndChars {
  // 二次元マップの準備
  private char[][] map;
  private int MX = 16, MY = 7;
  private String[] initialMap = {"      g   F     ",
                                 "     B3BBB7BB   ",
                                 "     BBBB6BBB   ",
                                 "    MB2BBBB8BG  ",
                                 "     1BB4BBBB   ",
                                 "     BBBB5BBB   ",
                                 "                "};

  // キャラクタ配置用（彦星）
  private int boyX, boyY;
  private ImageView boyView;
  private int boyMotion;
  private double boySize = 39;
  private Line erase1, erase2;

  // キャラクタ配置用（織姫）　mapやキャラクタを表示するクラス内に宣言
  private int girlX, girlY,girl1X,girl1Y,girl2X,girl2Y;
  private Image girlImage01, girlImage02;
  private ImageView girlView,girlView1,girlView2;
  private int girlMotion;
  private int vx,ex,tx;

    //block
  private int blockX,blockY,block1X,block1Y,block2X,block2Y,block3X,block3Y,block4X,block4Y,block5X,block5Y,block6X,block6Y,block7X,block7Y,block8X,block8Y,block9X,block9Y;
  private Image blockImage;
  private ImageView blockView,blockView1,blockView2,blockView3,blockView4,blockView5,blockView6,blockView7,blockView8,blockView9;
  private double blockSize = 40,blockSize1 = 40,blockSize2 = 40,blockSize3 = 40,blockSize4 = 40,blockSize5 = 40,blockSize6 = 40,blockSize7 = 40,blockSize8 = 40,blockSize9 = 40;

  // time
  private boolean gameover = false;
  private boolean gameclear = false;
  private Text timeText;
  private Text timeText1;
  

  // コンストラクタ
  MapAndChars(Group root) {
    // マップの周囲を見えない壁で囲む
    map = new char[MY+2][MX+2];
    for (int x = 0; x <= MX+1; x++) { //水平方向の見えないブロック
      map[0][x] = 'B'; 
      map[MY+1][x] = 'B';
    }
    for (int y = 0; y <= MY+1; y++) {
      map[y][0] = 'B'; 
      map[y][MX+1] = 'B';
    }
    // マップデータの読み込み
    for (int y = 1; y <= MY; y++) {
      for (int x = 1; x <= MX; x++) {
        map[y][x] = initialMap[y-1].charAt(x-1);
      }
    }

    drawInitialMapAndChars(root);
    
  }

  public void drawInitialMapAndChars(Group root) {
    Image image;
    timeText = new Text(30, 50, "Time: 30");
    timeText.setFont(new Font("TimeRoman", 18));
    timeText.setFill(Color.ORANGE);
    root.getChildren().add(timeText);
    timeText1 = new Text(40, 70, " ");
    timeText1.setFont(new Font("TimeRoman", 20));
    timeText1.setFill(Color.GREEN);
    root.getChildren().add(timeText1);
    erase1 = new Line(0, 0, 0, 0);
    erase2 = new Line(0, 0, 0, 0);
    erase1.setStroke(Color.CYAN);
    erase2.setStroke(Color.CYAN);
    root.getChildren().add(erase1);
    root.getChildren().add(erase2);
    
    for (int y = 1; y <= MY; y++) {
      for (int x = 1; x <= MX; x++) {
	  int xx = 40*x+20, yy = 40*y+20; //
        switch ( map[y][x] ) {
	    case 'B': 
            blockImage = new Image("./block2.png");
	        blockView = new ImageView(blockImage);
		    blockX = x;
		    blockY = y;
		    drawBlock();
		    root.getChildren().add(blockView);
		    break;

	    case '1': 
            blockImage = new Image("./block2.png");
	        blockView1 = new ImageView(blockImage);
		    block1X = x;
		    block1Y = y;
		    drawBlock1();
		    root.getChildren().add(blockView1);
		    break;

	    case '2': 
            blockImage = new Image("./block2.png");
	        blockView2 = new ImageView(blockImage);
		    block2X = x;
		    block2Y = y;
		    drawBlock2();
		    root.getChildren().add(blockView2);
		    break;

        case '3': 
            blockImage = new Image("./block2.png");
	        blockView3 = new ImageView(blockImage);
		    block3X = x;
		    block3Y = y;
		    drawBlock3();
		    root.getChildren().add(blockView3);
		    break;

	    case '4': 
            blockImage = new Image("./block2.png");
	        blockView4 = new ImageView(blockImage);
		    block4X = x;
		    block4Y = y;
		    drawBlock4();
		    root.getChildren().add(blockView4);
		    break;

        case '5': 
            blockImage = new Image("./block2.png");
	        blockView5 = new ImageView(blockImage);
		    block5X = x;
		    block5Y = y;
		    drawBlock5();
		    root.getChildren().add(blockView5);
		    break;

	    case '6': 
            blockImage = new Image("./block2.png");
	        blockView6 = new ImageView(blockImage);
		    block6X = x;
		    block6Y = y;
		    drawBlock6();
		    root.getChildren().add(blockView6);
		    break;

        case '7':
            blockImage = new Image("./block2.png");
	        blockView7 = new ImageView(blockImage);
		    block7X = x;
		    block7Y = y;
		    drawBlock7();
		    root.getChildren().add(blockView7);
		    break;

	    case '8': 
            blockImage = new Image("./block2.png");
	        blockView8 = new ImageView(blockImage);
		    block8X = x;
		    block8Y = y;
		    drawBlock8();
		    root.getChildren().add(blockView8);
		    break;
	  // 織姫の描画
        case 'G': 
            girlImage01 = new Image("./orihime01.gif");
            girlImage02 = new Image("./orihime02.gif");
            girlView = new ImageView( girlImage01 );
            girlX = x;
            girlY = y;
		    drawGirl();
            root.getChildren().add(girlView);
            break;
	  // 彦星の描画 
        case 'M': 
            image = new Image("./hikoboshi.gif");
            boyView = new ImageView( image );
            boyX = x;
            boyY = y;
            drawBoy();
            root.getChildren().add(boyView);
            break;

	    case 'g': 
            girlImage01 = new Image("./orihime01.gif");
            girlImage02 = new Image("./orihime02.gif");
            girlView1 = new ImageView( girlImage01 );
            girl1X = x;
            girl1Y = y;
            drawGirl1();
            root.getChildren().add(girlView1);
            break;

	    case 'F': 
            girlImage01 = new Image("./orihime01.gif");
            girlImage02 = new Image("./orihime02.gif");
            girlView2 = new ImageView( girlImage01 );
            girl2X = x;
            girl2Y = y;
            drawGirl2();
            root.getChildren().add(girlView2);
		    break;
	    }
      }
    }
  }

  public void drawScreen(int t) { 
    if ( boySize < -5 ) return;
    drawBoy();
    girlMove();
    girlMove1();
    girlMove2();

    drawBlock();
    drawBlock1();
    drawBlock2();
    drawBlock3();
    drawBlock4();
    drawBlock5();
    drawBlock6();
    drawBlock7();
    drawBlock8();
    displayTime(t);
  }

  public void drawBoy() {
    if ( gameover ) boySize -= 0.25;

    switch ( (int) boySize ) {
    case 39: 
      boyView.setX(40*boyX+30);
      boyView.setY(40*boyY+20);
      boyView.toFront();
      break;
    case 0:
      boyView.setImage(null);
    case -1:
    case -2:
    case -3:
    case -4:
    case -5:
      int xx = 40*boyX+31+10, yy = 40*boyY+20+20;
      erase1.setStartX(xx+boySize);
      erase1.setStartY(yy+boySize);
      erase1.setEndX(xx-boySize);
      erase1.setEndY(yy-boySize);
      erase2.setStartX(xx-boySize);
      erase2.setStartY(yy+boySize);
      erase2.setEndX(xx+boySize);
      erase2.setEndY(yy-boySize);
      break;
    default:
      boyView.setFitHeight(boySize);
      boyView.setFitWidth(boySize/2);
      boyView.setX(40*boyX+31+(10-boySize/4));
      boyView.setY(40*boyY+20+(20-boySize/2));
    }
  }

  public void boyMove(int dir) { //矢印きーが押されたら主人公を移動できるか判断し、位置を移動する処理を行う。そのあとで再描画を行うことで主人公を移動させることができる。
    int dx = 0, dy = 0;
    switch ( dir ) {
      case 0: dx =  1; break; // right
      case 1: dy = -1; break; // up
      case 2: dx = -1; break; // left
      case 3: dy =  1; break; // down
    }
    if ( dx == 0 && dy == 0 ) return;
    if ( map[boyY+dy][boyX+dx] == ' ' || boyX+1 == MX+1 || boyY+1 == MY+1 ||boyX+1 == 0 || boyY+1 == 0 || gameover || gameclear) return; // block
    boyX += dx; boyY += dy;
   
    if ( map[boyY][boyX+1] == 'G' ) gameclear = true;
    
    if( map[boyY][boyX] == '1' || map[boyY][boyX] == '2' || map[boyY][boyX] == '3' || map[boyY][boyX] == '4' || map[boyY][boyX] == '5' || map[boyY][boyX] == '6' || map[boyY][boyX] == '7' || map[boyY][boyX] == '8') gameover = true;

  }
  
  public void drawGirl() {
     
    girlView.setX(40*girlX+30); 
    girlView.setY(40*girlY+20);
    girlView.toFront();
    
  }

  public void drawGirl1() { //g
    girlView1.setX(40*girl1X+30);
    girlView1.setY(40*girl1Y+20);
    girlView1.toFront();
  }
  
  public void drawGirl2() { //F
    girlView2.setX(40*girl2X+30);
    girlView2.setY(40*girl2Y+20);
    girlView2.toFront();
  }

  public void girlMove() {
    if (!gameclear) {
      girlView.setImage(girlImage01);
      return;
    }; 

    girlMotion = (girlMotion+1)%120; // girlMotion（120フレーム分）の中の画像を設定
    switch (girlMotion) {
    case 0:
      girlView.setImage(girlImage01); break;
    case 12:
      girlView.setImage(girlImage02); break;
    case 24:
      girlView.setImage(girlImage01); break;
    case 36:
      girlView.setImage(girlImage02); break;
    case 48:
      girlView.setImage(girlImage01); break;
    case 60:
      girlView.setImage(girlImage02); break;
    case 72:
      girlView.setImage(girlImage01); break;
    case 84:
      girlView.setImage(girlImage02); break;
    case 96:
      girlView.setImage(girlImage01); break;
    case 108:
      girlView.setImage(girlImage02); break;
    case 120:
      girlView.setImage(girlImage01); break;


    }
  }

  public void girlMove1(){
    vx = vx + 1;
    
    if(40*girl1Y +vx >= 400){
	vx = vx-400;
    }
    girlView1.setX(40*girl1X + 30);
    girlView1.setY(40*girl1Y+vx);
    girlView1.toFront();

    if((40*girl1X <= 40*boyX+31 && 40*boyX+31<= 40*girl1X + 40) && (40*girl1Y + vx <= 40*boyY + 20 && 40*boyY + 20 <= 40*girl1Y + vx + 20)) gameover = true;
  }

    public void girlMove2(){
    tx = tx+2;

    if(40*girl2Y +tx >= 400){
	tx = tx-400;
    }
    girlView2.setX(40*girl2X + 30);
    girlView2.setY(40*girl2Y+tx);
    girlView2.toFront();

    if((40*girl2X <= 40*boyX+31 && 40*boyX+31<= 40*girl2X + 40) && (40*girl2Y + tx <= 40*boyY + 20 && 40*boyY + 20 <= 40*girl2Y + tx + 20)) gameover = true;

    /*if((40*girl2X+30 <= 40*boyX+30 && 40*boyX+30<= 40*girl2X + 100) && (40*girl2Y + tx <= 40*boyY + 20 && 40*boyY + 20 <= 40*girl2Y + tx + 20)) gameover = true;*/
  }

  public void displayTime(int t) {
    if ( gameover ) {
        timeText.setFill(Color.RED);
        timeText.setText("Game Over");
    }else if(gameclear){
	    timeText.setFill(Color.GREEN);
	    timeText.setText("Game Clear!!");
	    timeText1.setFill(Color.GREEN);
	    timeText1.setText("Great!!");
    } else {
        timeText.setFill(Color.RED);
        timeText.setText("落ちないタイルを進み上からの偽織姫をよけて織姫の前のマスまでたどり着け! 制限時間: " + t);
      if ( t == 0 ) gameover = true;
    }
  }

  public void drawBlock(){
    
    blockView.setX(40*blockX+21); 
    blockView.setY(40*blockY+20);
    //blockView.toFront();
  }

  public void drawBlock1(){
    
      
    if ( map[boyY][boyX] == '1' ) blockSize -= 20;

    switch ( (int) blockSize ) {
    case 40: 
      blockView1.setX(40*block1X+21);
      blockView1.setY(40*block1Y+20);
      break;
    case 0:
      blockView1.setImage(null);
    }
  }

  public void drawBlock2(){
    
      
    if ( map[boyY][boyX] == '2' ) blockSize1 -= 20;

    switch ( (int) blockSize1 ) {
    case 40: 
      blockView2.setX(40*block2X+21);
      blockView2.setY(40*block2Y+20);
      break;
    case 0:
      blockView2.setImage(null);
    }
  }

  public void drawBlock3(){
    
      
    if ( map[boyY][boyX] == '3' ) blockSize2 -= 20;

    switch ( (int) blockSize2 ) {
    case 40: 
      blockView3.setX(40*block3X+21);
      blockView3.setY(40*block3Y+20);
      break;
    case 0:
      blockView3.setImage(null);
    }
  }

  public void drawBlock4(){
    
      
    if ( map[boyY][boyX] == '4' ) blockSize3 -= 20;

    switch ( (int) blockSize3 ) {
    case 40: 
      blockView4.setX(40*block4X+21);
      blockView4.setY(40*block4Y+20);
      break;
    case 0:
      blockView4.setImage(null);
    }
  }
   
  public void drawBlock5(){
    
      
    if ( map[boyY][boyX] == '5' ) blockSize4 -= 20;

    switch ( (int) blockSize4 ) {
    case 40: 
      blockView5.setX(40*block5X+21);
      blockView5.setY(40*block5Y+20);
      break;
    case 0:
      blockView5.setImage(null);
    }
  }

  public void drawBlock6(){
    
      
    if ( map[boyY][boyX] == '6' ) blockSize5 -= 20;

    switch ( (int) blockSize5 ) {
    case 40: 
      blockView6.setX(40*block6X+21);
      blockView6.setY(40*block6Y+20);
      break;
    case 0:
      blockView6.setImage(null);
    }
  }
  
  public void drawBlock7(){
    
      
    if ( map[boyY][boyX] == '7' ) blockSize6 -= 20;

    switch ( (int) blockSize6 ) {
    case 40: 
      blockView7.setX(40*block7X+21);
      blockView7.setY(40*block7Y+20);
      break;
    case 0:
      blockView7.setImage(null);
    }
  }

  public void drawBlock8(){
    
      
    if ( map[boyY][boyX] == '8' ) blockSize7 -= 20;

    switch ( (int) blockSize7 ) {
    case 40: 
      blockView8.setX(40*block8X+21);
      blockView8.setY(40*block8Y+20);
      break;
    case 0:
      blockView8.setImage(null);
    }
  }
}
