package jcolibri.evaluation.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;

import net.sourceforge.chart2d.*;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import jcolibri.evaluation.EvaluationResult;

public class EvaluationResultGUI
{
    private static final long serialVersionUID = 1L;
    
    private static Chart2D chart;
    private static JDialog dialog;
    private static EvaluationResult evalResult; 
    
    public static void show(JFrame parentFrame, EvaluationResult er, String title)
    {
        evalResult = er;
        
       dialog = new JDialog(parentFrame,true);
       dialog.setTitle("jCOLIBRI Evaluation");
       dialog.getContentPane().setLayout(new BorderLayout());
       
       JPanel data = new JPanel();
       data.setLayout(new BoxLayout(data,BoxLayout.X_AXIS));
       data.add(new JLabel("Cycles: "+ er.getNumberOfCycles()));
       data.add(Box.createGlue());
       data.add(new JLabel("Time: "+ er.getTotalTime()+" ms"));
       data.add(Box.createGlue());
       data.add(new JLabel("Time per cycle: "+ er.getTimePerCycle()+" ms"));
       //data.add(Box.createGlue());
       //data.add( new JLabel("Average: "+ String.format("%6f",er.getEvaluationAverage())));
       dialog.getContentPane().add(data,BorderLayout.NORTH);

       chart = getChart(title, er);
       
       JTextArea textArea = new JTextArea();
       JScrollPane sp = new JScrollPane(textArea);
       sp.setViewportView(textArea);
       textArea.setText(er.getTextualInfo());
       textArea.setEditable(false);
       
       JPanel p = new JPanel();
       p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
       p.add(chart);
       p.add(sp);
       
       dialog.getContentPane().add(p, BorderLayout.CENTER);
       
       JPanel buttons = new JPanel();
       //buttons.setLayout(new BoxLayout(buttons,BoxLayout.X_AXIS));
       JButton exportData = new JButton("Export data");
       exportData.addActionListener( new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                try{
                    FileDialog fd = new FileDialog(dialog, "Save as CSV", FileDialog.SAVE);
                    fd.setFile("evaluation.csv");
                    fd.setVisible(true);
                    String name = fd.getDirectory() + fd.getFile();
                    File file = new File(name);
                    saveEvaluationToCSV(evalResult, file);
                }
                catch(Exception ex) { 
                    ex.printStackTrace(); 
                }                
            }
           
       });
       
       
       JButton exportChart = new JButton("Export chart");
       exportChart.addActionListener( new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                try{
                    FileDialog fd = new FileDialog(dialog, "Save as JPG", FileDialog.SAVE);
                    fd.setFile("evaluation.jpg");
                    fd.setVisible(true);
                    String name = fd.getDirectory() + fd.getFile();
                    File file = new File(name);
                    saveComponentToJPG(chart, file);
                }
                catch(Exception ex) { 
                    ex.printStackTrace(); 
                }
            }
       });
       
       buttons.add(exportData);
       buttons.add(exportChart);
       
       dialog.getContentPane().add(buttons, BorderLayout.SOUTH);

       dialog.setPreferredSize(new Dimension(640,300));
       dialog.pack();
       dialog.doLayout();
       dialog.setVisible(true);
    }
    
    
    private static Chart2D getChart(String title, EvaluationResult er) {

        //<-- Begin Chart2D configuration -->

        //Configure object properties
        Object2DProperties object2DProps = new Object2DProperties();
        object2DProps.setObjectTitleText (title);

        //Configure chart properties
        Chart2DProperties chart2DProps = new Chart2DProperties();
        chart2DProps.setChartDataLabelsPrecision (-2);

        //Configure legend properties
        LegendProperties legendProps = new LegendProperties();
        legendProps.setLegendExistence(true);
        String[] labels = er.getLabels();
        legendProps.setLegendLabelsTexts (labels);

        //Configure graph chart properties
        GraphChart2DProperties graphChart2DProps = new GraphChart2DProperties();
        //String[] labelsAxisLabels = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        graphChart2DProps.setLabelsAxisExistence(false); //setLabelsAxisLabelsTexts (labelsAxisLabels);
        //graphChart2DProps.setLabelsAxisTitleText ("Iteration");
        graphChart2DProps.setNumbersAxisTitleText ("Evaluation");
        graphChart2DProps.setLabelsAxisTicksAlignment (GraphChart2DProperties.CENTERED);
        //graphChart2DProps.setChartDatasetCustomGreatestValue(1);
        //graphChart2DProps.setChartDatasetCustomizeGreatestValue(true);

        //Configure graph properties
        GraphProperties graphProps = new GraphProperties();
        graphProps.setGraphBarsExistence (false);
        graphProps.setGraphLinesExistence (true);
        graphProps.setGraphLinesThicknessModel (2);
        graphProps.setGraphLinesWithinCategoryOverlapRatio (1f);
        graphProps.setGraphDotsExistence (false);
        graphProps.setGraphDotsThicknessModel (4);
        graphProps.setGraphDotsWithinCategoryOverlapRatio (1f);
        graphProps.setGraphAllowComponentAlignment (true);
        graphProps.setGraphOutlineComponentsExistence (true);

        int lines = labels.length;
        int lineSize = er.getCycleEvaluation(labels[0]).size();
        
        //Configure dataset
        Dataset dataset = new Dataset (lines, lineSize, 1);
          
        for( int l=0; l<lines; l++)
        {
            Vector<Double> line = er.getCycleEvaluation(labels[l]);
            for (int j = 0; j < dataset.getNumCats(); ++j) {
                  dataset.set (l, j, 0, (float)line.get(j).floatValue());
                }
        }

        //Configure graph component colors
        MultiColorsProperties multiColorsProps = new MultiColorsProperties();

        //Configure chart
        LBChart2D chart2D = new LBChart2D();
        chart2D.setObject2DProperties (object2DProps);
        chart2D.setChart2DProperties (chart2DProps);
        chart2D.setLegendProperties (legendProps);
        chart2D.setGraphChart2DProperties (graphChart2DProps);
        chart2D.addGraphProperties (graphProps);
        chart2D.addDataset (dataset);
        chart2D.addMultiColorsProperties (multiColorsProps);

        //Optional validation:  Prints debug messages if invalid only.
        if (!chart2D.validate (false)) chart2D.validate (true);

        //<-- End Chart2D configuration -->

        return chart2D;
      }
    

     static void saveEvaluationToCSV(EvaluationResult er, File file) throws IOException{
         PrintWriter pw = new PrintWriter(file);
         pw.println("# Cycles: "+ er.getNumberOfCycles());
         pw.println("# Time: "+ er.getTotalTime()+" ms");
         pw.println("# Time per cycle: "+ er.getTimePerCycle()+" ms");
         //pw.println("# Average: "+ String.format("%6f",er.getEvaluationAverage()));
         
         String[] labels = er.getLabels();
         
         for(int l = 0; l<labels.length; l++)
         {
             Vector<Double> res = er.getCycleEvaluation(labels[l]);
             pw.print(labels[l]);
             for(int i=0; i<res.size(); i++)
                 pw.print(";"+res.get(i));
             pw.println();
         }
             
         pw.close();
     }
          
     static void saveComponentToJPG(Component component, File file) throws IOException{
            BufferedImage image = (BufferedImage)component.createImage(component.getWidth(),component.getHeight());
            Graphics graphics = image.getGraphics();
            if(graphics != null) { component.paintAll(graphics); }
            FileOutputStream fileStream = new FileOutputStream(file);
            JPEGEncodeParam encodeParam = JPEGCodec.getDefaultJPEGEncodeParam(image);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(fileStream);
            encoder.encode(image,encodeParam);
            fileStream.close();
     }


}
