package jcolibri.evaluation.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Collection;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import jcolibri.cbrcore.CBRCore;
import jcolibri.util.CBRLogger;
import jcolibri.util.ImagesContainer;

import jcolibri.evaluation.EvaluationResult;
import jcolibri.evaluation.Evaluator;
import jcolibri.evaluation.EvaluatorsFactory;
import jcolibri.evaluation.util.LoadFromXML;
import jcolibri.evaluation.util.SaveToXML;


/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */

public class EvaluationGUI extends JInternalFrame implements EvaluatorsInfoListener
{

    private static final long serialVersionUID = 1L;

    // ------------------------------------------------------------------------------
    // -------------------------------ATRIBUTOS--------------------------------------

    private CBRCore core;
    private JFrame parentFrame;

    // ------------------------------------------------------------------------------
    // ---------------------------ATRIBUTOS
    // GRÁFICOS---------------------------------

    JComboBox jComboBoxEvaluador = new JComboBox();

    JComboBox jComboBoxSolEv = new JComboBox();
    
    JTextField jTextFieldIdent = new JTextField();

    DefaultTableModel tableModel;



    // ------------------------------------------------------------------------------
    // ------------------------------CONSTRUCTORA------------------------------------
    public EvaluationGUI(JFrame parentFrame, CBRCore core)
    {
        this.parentFrame = parentFrame;
        this.core = core;
        try
        {
            initComponents();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    // ------------------------------------------------------------------------------

    private void initComponents() throws Exception
    {
        //////////////////////////////////
        // LOAD / SAVE (NORTH)
        /////////////////////////////////
        this.setFrameIcon(ImagesContainer.CONFIG_EVALUATION);
        
        JButton jButtonLoad = new JButton("Load from XML");
        jButtonLoad.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                loadXML();
            }
        });
        jButtonLoad.setIcon(ImagesContainer.LOAD);

        JButton jButtonSave = new JButton("Save to XML");
        jButtonSave.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                saveXML();
            }
        });
        jButtonSave.setIcon(ImagesContainer.SAVE);
        
        JPanel jPanelLoadSave = new JPanel();
        jPanelLoadSave.setLayout(new FlowLayout());
        jPanelLoadSave.add(jButtonLoad);
        jPanelLoadSave.add(jButtonSave);
        
        //////////////////////////////////
        // EVALUATE (SOUTH)
        /////////////////////////////////        
        
        JButton jButtonEvaluate = new JButton("Evaluate");
        jButtonEvaluate.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                evaluate();
            }
        });
        jButtonEvaluate.setIcon(ImagesContainer.APPLY);

        JPanel jPanelEvaluate = new JPanel(); 
        jPanelEvaluate.add(jButtonEvaluate, null);
        
        
        /////////////////////////////////////
        // CONFIG (CENTER)
        /////////////////////////////////////
        
        
        // --------------- ID ----------------------------------------------
        JLabel jLabelId = new JLabel("Id: ");
        jTextFieldIdent.setText("My Evaluation");
        
        JPanel jPanelId = new JPanel();
        jPanelId.setLayout(new BorderLayout());
        jPanelId.add(jLabelId, BorderLayout.WEST);
        jPanelId.add(jTextFieldIdent, BorderLayout.CENTER);

        // -------------- EVALUATOR --------------------------------------
        
        JLabel jLabelTipoEv = new JLabel("Evaluator type:");        

        this.jComboBoxEvaluador.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                evaluatorSelected();
            }
         });

        JButton jButtonAddEv = new JButton("Add  evaluators  from  dir  ...");
        jButtonAddEv.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                addEvaluators();
            }
        });
        jButtonAddEv.setIcon(ImagesContainer.LOAD_ARROW);

        JPanel jPanelEvType = new JPanel();
        jPanelEvType.setLayout(new BorderLayout());
        jPanelEvType.add(jLabelTipoEv, BorderLayout.NORTH);
        jPanelEvType.add(jComboBoxEvaluador, BorderLayout.CENTER);
        
        JPanel panelButtonAddEv = new JPanel();
        panelButtonAddEv.add(jButtonAddEv);
        jPanelEvType.add(panelButtonAddEv, BorderLayout.SOUTH);

        
        // --------------- CYCLE EVALUATORS --------------------------------
        
        JLabel jLabelCycleEval = new JLabel("Cycle Evaluator:");
        JButton jButtonAddCycleEval = new JButton("Add Cycle Evaluators from dir...");
        
        jButtonAddCycleEval.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                addCycleEvaluators();
            }
        });
        jButtonAddCycleEval.setIcon(ImagesContainer.LOAD_ARROW);
        
        JPanel jPanelCycleEvType = new JPanel();
        jPanelCycleEvType.setLayout(new BorderLayout());
        jPanelCycleEvType.add(jLabelCycleEval, BorderLayout.NORTH);
        jPanelCycleEvType.add(jComboBoxSolEv, BorderLayout.CENTER);
        
        JPanel panelButtonAddCycleEval = new JPanel();
        panelButtonAddCycleEval.add(jButtonAddCycleEval);
        jPanelCycleEvType.add(panelButtonAddCycleEval, BorderLayout.SOUTH);
        
        // ---------------PARAMETERS-------------------------------------------------
        JLabel jLabelConfigParams = new JLabel("Configuration parameters:");
        
        tableModel = new DefaultTableModel();
        JTable jTableParams = new JTable(tableModel);
        JScrollPane jScrollPane1 = new JScrollPane();
        jScrollPane1.setAutoscrolls(true);
        jScrollPane1.setBorder(BorderFactory.createLoweredBevelBorder());
        jScrollPane1.setDebugGraphicsOptions(0);
        jScrollPane1.setVerifyInputWhenFocusTarget(true);
        
        jTableParams.setAutoscrolls(true);
        jTableParams.setDoubleBuffered(false);
        jTableParams.setAutoCreateColumnsFromModel(true);
        jTableParams.setCellSelectionEnabled(false);
        jTableParams.setColumnSelectionAllowed(false);
        jTableParams.setRowMargin(1);
        jTableParams.setRowSelectionAllowed(true);

        tableModel.addColumn("Parameter name");
        tableModel.addColumn("Parameter value");
        
        jScrollPane1.getViewport().add(jTableParams);
        jScrollPane1.setPreferredSize(new Dimension(400,100));
        

        JPanel jPanelParams = new JPanel();
        jPanelParams.setLayout(new BorderLayout());
        jPanelParams.add(jLabelConfigParams, BorderLayout.NORTH);
        jPanelParams.add(jScrollPane1, BorderLayout.CENTER);



        // --------------------------------------------------------------------------
        // ---------------RESULT-----------------------------------------------------


        JPanel jPanelConfig = new JPanel();
        jPanelConfig.setLayout(new BoxLayout(jPanelConfig, BoxLayout.Y_AXIS));
        jPanelConfig.add(Box.createVerticalStrut(10));
        jPanelConfig.add(jPanelId);
        jPanelConfig.add(Box.createVerticalStrut(10));
        jPanelConfig.add(jPanelEvType);
        jPanelConfig.add(Box.createVerticalStrut(10));
        jPanelConfig.add(jPanelCycleEvType);
        jPanelConfig.add(Box.createVerticalStrut(10));
        jPanelConfig.add(jPanelParams);
        jPanelConfig.add(Box.createVerticalStrut(10));

        

        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(jPanelLoadSave, BorderLayout.NORTH);
        this.getContentPane().add(jPanelConfig, BorderLayout.CENTER);
        this.getContentPane().add(jPanelEvaluate, BorderLayout.SOUTH);
               
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Configure Evaluation");
        
        this.pack();
        change();
    }


    // ------------------------------------------------------------------------------
    // --------------------METODOS RELACIONADOS CON BOTONES--------------------------
    
    void evaluatorSelected()
    {
        for(int r = tableModel.getRowCount()-1;r>=0; r--)
            tableModel.removeRow(r);
        String className = (String)this.jComboBoxEvaluador.getSelectedItem();
        Collection<String> params = EvaluatorsInfo.getInstance().getEvaluatorParams(className);
        for(String paramName: params)
            tableModel.addRow(new Object[]{paramName,null});
        
    }
    
    
    void evaluate()
    {
        String evaluatorClassName = (String)this.jComboBoxEvaluador.getSelectedItem();
        String cycleEvaluatorClassName = (String)this.jComboBoxSolEv.getSelectedItem();
        HashMap<String, String> parameters = new HashMap<String,String>();
        for(int r=0; r<tableModel.getRowCount(); r++)
            parameters.put((String)tableModel.getValueAt(r,0), (String)tableModel.getValueAt(r,1));    

        Evaluator eval = EvaluatorsFactory.getEvaluator(evaluatorClassName, cycleEvaluatorClassName, parameters, core);
        EvaluationResult er = eval.evaluate();
        if(!er.checkData())
            JOptionPane.showInternalMessageDialog(this,"Error, data cannot be represented because vectors have different size", "No representable data", JOptionPane.ERROR_MESSAGE);
        else
            EvaluationResultGUI.show(parentFrame, er, jTextFieldIdent.getText());
        
    }

    // ------------------------------------------------------------------------------
    void saveXML()
    {
        FileDialog fd = new FileDialog(parentFrame,
                "Select the XML file to save with the evaluation info", FileDialog.SAVE);
        fd.setVisible(true);
        if (fd.getFile() == null)
            return;
        
        String archivo = fd.getDirectory() + fd.getFile();
        
        String id = this.jTextFieldIdent.getText();
        String evaluatorClassName = (String)this.jComboBoxEvaluador.getSelectedItem();
        String cycleEvaluatorClassName = (String)this.jComboBoxSolEv.getSelectedItem();
        HashMap<String, String> parameters = new HashMap<String,String>();
        for(int r=0; r<tableModel.getRowCount(); r++)
            parameters.put((String)tableModel.getValueAt(r,0), (String)tableModel.getValueAt(r,1));    
        
        SaveToXML.save(archivo, id, evaluatorClassName, parameters, cycleEvaluatorClassName);
    }

    // ------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------
    void loadXML()
    {
        try
        {
            FileDialog fd = new FileDialog(parentFrame,
                    "Select the XML file with the evaluation info", FileDialog.LOAD);
            fd.setVisible(true);
            if (fd.getFile() == null)
                return;
            
            String archivo = fd.getDirectory() + fd.getFile();

            // This method call EvaluatorsFactory
            LoadFromXML.load(archivo);

            this.jTextFieldIdent.setText(LoadFromXML.getIdEvaluator());
            
            String evaluatorClassName = LoadFromXML.getEvaluatorClassName();
            Class evaluatorClass = Class.forName(evaluatorClassName);
            EvaluatorsInfo.getInstance().addEvaluatorClass(evaluatorClass);
            jComboBoxEvaluador.setSelectedItem(evaluatorClassName);

            String cycleEvaluatorClassName = LoadFromXML.getCycleEvaluatorClassName();
            jComboBoxSolEv.setSelectedItem(cycleEvaluatorClassName);
            
            for(int r = tableModel.getRowCount()-1;r>=0; r--)
                tableModel.removeRow(r);
            
            // Load parameters name and value into the table
            Evaluator evaluator = (Evaluator) evaluatorClass.newInstance();
            Collection<String> nombresParams = evaluator.getParamsNames();
            HashMap<String, String> parameters = LoadFromXML.getParametros();
            for (String nombre : nombresParams)
            {
                String[] row = { nombre, (String) parameters.get(nombre) };
                tableModel.addRow(row);
            }
        } catch (Exception ex)
        {
            CBRLogger.log(this.getClass(), CBRLogger.ERROR, ex.getMessage());
        }

    }

    void addEvaluators()
    {
        String pckg = JOptionPane.showInputDialog(this,"Insert package containing Evaluators");
        if(pckg!=null)
            EvaluatorsInfo.getInstance().addEvaluatorsFromDir(pckg);
    }

    void addCycleEvaluators()
    {
        String pckg = JOptionPane.showInputDialog(this,"Insert package containing Evaluators");
        if(pckg!=null)
            EvaluatorsInfo.getInstance().addCycleEvaluatorsFromDir(pckg);
    }


    
    

    public void change()
    {
        Object selected = jComboBoxEvaluador.getSelectedItem();
        jComboBoxEvaluador.removeAllItems();
        for(String s:EvaluatorsInfo.getInstance().getEvaluators())
            jComboBoxEvaluador.addItem(s);
        if(selected != null)
            jComboBoxEvaluador.setSelectedItem(selected);
 
        selected = jComboBoxSolEv.getSelectedItem();
        jComboBoxSolEv.removeAllItems();
        for(String s:EvaluatorsInfo.getInstance().getCycleEvaluators())
            jComboBoxSolEv.addItem(s);
        if(selected != null)
            jComboBoxSolEv.setSelectedItem(selected);

    }
}// MainGUI

