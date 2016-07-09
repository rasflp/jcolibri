/**
 * 
 */
package jcolibri.gui.editor;

import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import jcolibri.gui.ParameterEditor;


public class WeightEditor extends JPanel implements ParameterEditor, ChangeListener
{
    private static final long serialVersionUID = 1L;
    
    JSlider slider;
    JLabel label;
    
    public WeightEditor()
    {
        this.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        this.setLayout(new FlowLayout());
        slider = new JSlider();
        slider.setMaximum(20);
        slider.setValue(20);
        slider.setMaximumSize(new Dimension(100,15));
        slider.setPreferredSize(new Dimension(100,15));
        slider.setSize(new Dimension(100,15));
        label  = new JLabel();
        label.setMinimumSize(new Dimension(30,15));
        label.setPreferredSize(new Dimension(30,15));
        label.setSize(new Dimension(30,15));
        this.add(slider);
        this.add(label);
        slider.addChangeListener(this);
        this.stateChanged(null);
    }
    
    public Object getEditorValue()
    {
        return new Double(  (double)slider.getValue() / 20);
    }

    public JComponent getJComponent()
    {
        return (JComponent) this;
    }

    public void setConfig(Object config)
    {
        // EMPTY
        
    }

    public void setDefaultValue(Object defaultValue)
    {
        if (!(defaultValue instanceof Double))
            return;
        double value = ((Double)defaultValue).doubleValue();   
        slider.setValue((int)(value*20));
    }

    public void stateChanged(ChangeEvent e)
    {
        double v = (double)slider.getValue() /20;
        String res =  String.valueOf(v);
        if(res.length()>4)
            res = res.substring(0,4);
        label.setText(res);
    }
    
}

