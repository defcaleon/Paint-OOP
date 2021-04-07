package com.website.figures;

import com.website.interfaces.Brushes;
import com.website.interfaces.Lines;
import com.website.model.Dot;
import com.website.model.Initialization;
import com.website.model.Model;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

public class Ellipse implements Lines {

    public  Ellipse(){}

    @Override
    public void paint(GraphicsContext gc, Brushes brush, ArrayList<Dot> dots) {
        if(dots.size()<2){
            Model.alert("need 2 cords to draw rectangle");
            return;
        }
        Initialization.gcInit(gc,brush);
        int width=Math.abs(dots.get(1).getX()-dots.get(0).getX());
        int height=Math.abs(dots.get(1).getY()-dots.get(0).getY());
        gc.strokeOval(dots.get(0).getX(),dots.get(0).getY(),width,height);
        gc.fillOval(dots.get(0).getX(),dots.get(0).getY(),width,height);

    }
}