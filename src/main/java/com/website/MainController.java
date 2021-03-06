package com.website;

import com.website.fmodel.FileManager;
import com.website.fmodel.Initialization;
import com.website.fmodel.Model;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;


import javafx.scene.shape.Rectangle;
import model.Additional;
import model.Dot;

import java.io.File;
import java.util.ArrayList;


public class MainController {

    private final FileManager fileManager= new FileManager();
    private final Initialization init = new Initialization();
    private  GraphicsContext gc;
    private final Model model = new Model();
    private final ArrayList<Dot> dotArr = new ArrayList<>();
    private boolean flag=false;
    private File file;

    @FXML
    private Button drawBtn;

    @FXML
    private TextField cordY;

    @FXML
    private TextField cordX;

    @FXML
    private Canvas canvas;

    @FXML
    private ComboBox<String> figureBox;

    @FXML
    private ComboBox<Integer> brushWidth;



    @FXML
    private Rectangle col1;

    @FXML
    private Rectangle col2;
    @FXML
    private ColorPicker colorBar;




    @FXML
    public void initialize() {

        init.loadBrushTypes(brushWidth);
        init.loadFigures(figureBox);
        this.gc = canvas.getGraphicsContext2D();
        model.start(gc);

    }


    public void color2Clicked(MouseEvent mouseEvent) {
       col2.setFill(colorBar.getValue());
       model.setBrushFillColor(colorBar.getValue());

    }

    public void color1Clicked(MouseEvent mouseEvent) {
        col1.setFill(colorBar.getValue());
        model.setBrushLineColor(colorBar.getValue());
    }

    public void drawBtnClick(MouseEvent mouseEvent) {
        if(dotArr.size()==0){
            Additional.alert("no dots");
        }else{
            model.draw(this.gc,this.dotArr,figureBox.getValue().toString());
        }
    }


    public void addCordBtnClick(MouseEvent mouseEvent) {

        int cordX =  this.init.ValidTextArea(this.cordX.getText());
        int cordY =  this.init.ValidTextArea(this.cordY.getText());

        if(cordX<0||cordY<0){
            Additional.alert("cords must be positive numbers");
        }else{
            Dot dot=new Dot(cordX,cordY);
            this.dotArr.add(dot);

        }
    }

    public void deleteBtnClick(MouseEvent mouseEvent) {
        dotArr.clear();
    }

    public void comboAction(ActionEvent actionEvent) {
        model.setBrushWidth(brushWidth.getValue());
    }

    public void comboAction2(ActionEvent actionEvent) {
        this.model.setFabric(init.getFactoryByName(figureBox.getValue()));
    }


    public void clearCanvas(ActionEvent actionEvent) {
        clearCanvas();
        this.model.clearFigureArr();
    }


    public void mouseMove(MouseEvent mouseEvent) {
        cordX.setText(Double.toString(mouseEvent.getX()));
        cordY.setText(Double.toString(mouseEvent.getY()));
        if(flag) {
            clearCanvas();

            dotArr.add(new Dot((int) mouseEvent.getX(), (int) mouseEvent.getY()));

            model.redraw(gc);
            model.mouseDraw(this.gc,this.dotArr,figureBox.getValue().toString());
            dotArr.remove(dotArr.size()-1);
        }
    }

    public void mouseClick(MouseEvent mouseEvent) {

        if(mouseEvent.getButton().equals(MouseButton.SECONDARY)){
            if(model.is2OrMoreDotsInFigure()&&flag){

                dotArr.add(new Dot((int)mouseEvent.getX(),(int)mouseEvent.getY()));
            }
        }else
        {
            if(!flag){
                dotArr.clear();
                dotArr.add(new Dot((int)mouseEvent.getX(),(int)mouseEvent.getY()));
                this.flag=true;
            }else
            {
                clearCanvas();
                model.redraw(gc);
                dotArr.add(new Dot((int)mouseEvent.getX(),(int)mouseEvent.getY()));
                model.draw(this.gc,this.dotArr,figureBox.getValue().toString());

                this.flag=false;
            }
        }


    }

    public void undoClick(MouseEvent mouseEvent) {
        clearCanvas();
        model.undo(gc);
    }

    private void clearCanvas(){
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    public void redoCLick(MouseEvent mouseEvent) {
        clearCanvas();
        model.redo(gc);
    }

    public void openFile(ActionEvent actionEvent) {

        file=fileManager.openFile();
        model.deserialize(file);
        clearCanvas();
        model.redraw(gc);

    }

    public void saveFile(ActionEvent actionEvent) {

        file=fileManager.saveFile();
        model.serialize(file);
    }
}
