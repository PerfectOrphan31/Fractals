package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.geometry.Point2D;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

public class Main extends Application {
    private Random rand = new Random();
    private ArrayList<Point2D> startpoint2D= new ArrayList<>();
    private ArrayList<Point2D> currPoint = new ArrayList<>();
    private URL url= this.getClass().getResource("styling.css");
    private String css = url.toExternalForm();
    private int size=0;
    private int pointsize=0;
    private int xmin=0, xmax=0, ymin=0, ymax=0;

    /*public Main() throws MalformedURLException {
    }*/

    @Override
    public void start(Stage primaryStage) throws Exception{
        try {
            getSize();
            getPointSize();
            primaryStage.setTitle("POINTS!");
            final NumberAxis xAxis = new NumberAxis(0, 50, 5);
            final NumberAxis yAxis = new NumberAxis(0, 50, 5);
            final ScatterChart<Number, Number> sc = new ScatterChart<>(xAxis, yAxis);
            sc.setTitle(size + " Fractal!");
            playSound();
            XYChart.Series<Number, Number> points = first_points(size);
            points.setName("Starting Points");
            XYChart.Series<Number, Number> drawn = draw(size);
            drawn.setName("Points that were drawn");
            sc.getData().add(points);
            sc.getData().add(drawn);
            yAxis.setLowerBound(ymin);
            yAxis.setUpperBound(ymax);
            xAxis.setLowerBound(xmin);
            xAxis.setUpperBound(xmax);
            Scene scene = new Scene(sc, 1500, 1000);
            scene.getStylesheets().add(css);
            primaryStage.setScene(scene);
            primaryStage.show();
            playSound();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private XYChart.Series<Number,Number> first_points(int num){
        XYChart.Series<Number,Number> retdat = new XYChart.Series<>();
        for (int i=0; i<num; i++){
            int x,y;
            x=rand.nextInt(50);
            y=rand.nextInt(50);
            if(i==0){
                xmax=x;
                xmin=x;
                ymax=y;
                ymin=y;
            }else if(i>0) {
                if (x < xmin){
                    xmin = x;
            }
                else if (x > xmax){
                    xmax = x;
            }
                if (y < ymin){
                    ymin = y;
            }
                if(y>ymax) {
                    ymax = y;
                }
            }
            if(i<3) {
                startpoint2D.add(new Point2D(x, y));
            }else{
                while(x<xmax&&x>xmin&&y<ymax&&y>ymin){
                    x = rand.nextInt(50);
                    y = rand.nextInt(50);
                }
                if (x < xmin){
                    xmin = x;
                }
                else if (x > xmax){
                    xmax = x;
                }
                if (y < ymin){
                    ymin = y;
                }
                if(y>ymax) {
                    ymax = y;
                }
                startpoint2D.add(new Point2D(x, y));
            }
        }
        for (Point2D i:startpoint2D) {
            retdat.getData().add(new XYChart.Data<>(i.getX(),i.getY()));
        }
        return retdat;
    }

    private XYChart.Series<Number,Number> draw(int num){
        XYChart.Series<Number,Number> retdat = new XYChart.Series<>();
        double diff = 2.0;
        for(int i =0; i<pointsize;i++)
        if (currPoint.isEmpty()){
            currPoint.add(new Point2D(rand.nextDouble()*100,rand.nextDouble()*100));
        }else{
            int dir = rand.nextInt(num);
            for(int m=0; m<num; m++){
                if(m==dir) {
                    double x = (startpoint2D.get(m).getX() - currPoint.get(i - 1).getX()) / diff + currPoint.get(i - 1).getX();
                    double y = (startpoint2D.get(m).getY() - currPoint.get(i - 1).getY()) / diff + currPoint.get(i - 1).getY();
                    currPoint.add(new Point2D(x,y));
                }
            }
        }
        for (Point2D j:currPoint){
            retdat.getData().add(new XYChart.Data<>(j.getX(),j.getY()));
        }
        return retdat;
    }

    private void getSize(){
        TextInputDialog dialog = new TextInputDialog("Input");
        dialog.setTitle("Fractal Points Input");
        dialog.setHeaderText("Use this dialog to input how many fixed points you would like this fractal to have.\nNote: anything over 5 points starts to lose the clarity of the fractal.");
        dialog.setContentText("Enter the number of points here.");
        Optional<String> result = dialog.showAndWait();
        if(result.isPresent()){
            size=Integer.parseInt(result.get());
        }
    }

    private void getPointSize(){
        TextInputDialog dialog = new TextInputDialog("Input");
        dialog.setTitle("Fractal Points Size Input");
        dialog.setHeaderText("Use this dialog to input how many points you would like this fractal to have.\nNote: anymore points than 50000 can tremendously slow this program down.");
        dialog.setContentText("Enter the number of points here.");
        Optional<String> result = dialog.showAndWait();
        if(result.isPresent()){
            pointsize=Integer.parseInt(result.get());
        }
    }

    private synchronized void playSound(){
        try {
            Clip clip = AudioSystem.getClip();
            AudioInputStream is = AudioSystem.getAudioInputStream(getClass().getResourceAsStream("chimes.wav"));
            clip.open(is);
            clip.start();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void Main(String[] args) {
        launch(args);
    }
}
