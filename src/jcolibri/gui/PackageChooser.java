package jcolibri.gui;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import jcolibri.cbrcore.packagemanager.PackageInfo;
import jcolibri.cbrcore.packagemanager.PackageManager;

/**
 * <p>
 * Título: ClassName
 * </p>
 * <p>
 * Descripción:
 * </p>
 * <p>
 * Copyright:
 * </p>
 * <p>
 * Empresa:
 * </p>
 * 
 * @author Juan Antonio Recio García
 * @version 1.0
 */

public class PackageChooser extends JDialog {
	private static final long serialVersionUID = 1L;

	private ArrayList RBlist = new ArrayList();

	public PackageChooser(Frame parent) {
		super(parent, true);
		try {
			Collection packs = PackageManager.getInstance().getPackages();
			// this.getContentPane().setLayout(new
			// BoxLayout(this.getContentPane(),BoxLayout.Y_AXIS));
			GridLayout gl = new GridLayout(packs.size() + 2, 1);
			gl.setVgap(15);
			gl.preferredLayoutSize(this);
			this.getContentPane().setLayout(gl);
			this
					.getContentPane()
					.add(
							new JLabel(
									"Which jCOLIBRI extensions do you want to use in your application?"));
			int height = 20;
			for (Iterator iter = packs.iterator(); iter.hasNext();) {
				PackageInfo pi = (PackageInfo) iter.next();
				JPanel p = new JPanel();
				p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
				JRadioButton rb = new JRadioButton(pi.getName(), null);
				RBlist.add(rb);
				p.add(rb);
				p.add(new JLabel(pi.getDescription()));
				p
						.setBorder(BorderFactory
								.createEtchedBorder(javax.swing.border.EtchedBorder.LOWERED));
				this.getContentPane().add(p);
				if (pi.getName().equals("Core")) {
					rb.setSelected(true);
					rb.setEnabled(false);
				}
				height += 100;
			}

			JButton bOK = new JButton("Ok");
			bOK.setPreferredSize(new Dimension(100, 30));
			bOK.setMaximumSize(new Dimension(100, 30));
			this.getContentPane().add(bOK);
			bOK.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					updateActivePackages();
					dispose();
				}
			});

			// this.setSize(500,height);
			this.pack();

			java.awt.Dimension screenSize = java.awt.Toolkit
					.getDefaultToolkit().getScreenSize();
			setBounds((screenSize.width - 500) / 2,
					(screenSize.height - height) / 2, 500, height);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateActivePackages() {
		PackageManager pkgMngr;
		Iterator it;

		pkgMngr = PackageManager.getInstance();
		it = RBlist.iterator();
		while (it.hasNext()) {
			JRadioButton rb = (JRadioButton) it.next();
			pkgMngr.getPackageByName(rb.getText()).setActive(rb.isSelected());
		}
	}

	public static void main(String[] args) {
		PackageChooser packageChooser1 = new PackageChooser(null);
		packageChooser1.setVisible(true);
	}

}