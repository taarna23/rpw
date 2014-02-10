package net.mightypork.rpw.gui.widgets;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.*;
import javax.swing.Box.Filler;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;

import net.mightypork.rpw.gui.Gui;
import net.mightypork.rpw.gui.Icons;
import net.mightypork.rpw.gui.helpers.ClickListener;
import net.mightypork.rpw.library.Sources;
import net.mightypork.rpw.project.Project;
import net.mightypork.rpw.project.Projects;
import net.mightypork.rpw.tasks.Tasks;
import net.mightypork.rpw.tree.assets.EAsset;
import net.mightypork.rpw.tree.assets.tree.AssetTreeGroup;
import net.mightypork.rpw.tree.assets.tree.AssetTreeLeaf;
import net.mightypork.rpw.tree.assets.tree.AssetTreeNode;
import net.mightypork.rpw.utils.Utils;
import net.mightypork.rpw.utils.files.FileUtils;

import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTitledSeparator;


public class SidePanel {

	public JXPanel panel;

	private Box infoBox;
	private JXLabel projectName;
	private JXLabel projectBase;
	private JButton buttonOpenBase;
	private JButton buttonEditProps;
	private JLabel projectIconLabel;

	private Box previewBox;
	private JPanel previewPanel;
	private CardLayout previewCardLayout;

	private JTextArea previewText;
	private TitledBorder previewTextBorder;

	private JLabel previewImage;
	private TitledBorder previewImageBorder;
	private JPanelWithBackground previewImageBg;

	private JButton btnEditI;
	private JButton btnMetaI;
	private JButton btnReplaceI;

	private JButton btnEditT;
	private JButton btnReplaceT;

	private JButton btnEditA;
	private JButton btnReplaceA;


	private static final String IMAGE = "IMAGE";
	private static final String TEXT = "TEXT";
	private static final String AUDIO = "AUDIO";

	private AssetTreeLeaf displayedLeaf;

	private TitledBorder previewAudioBorder;

	private Border previewBorder;


	public SidePanel() {

		panel = new JXPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(Gui.GAP, Gui.GAPL, Gui.GAP, Gui.GAPL));

		panel.setPreferredSize(new Dimension(320, 700));
		panel.setMinimumSize(new Dimension(320, 300));

		panel.add(infoBox = createProjectInfoBox());
		infoBox.setAlignmentX(0.5f);

		panel.add(Box.createRigidArea(new Dimension(300, 20)));

		panel.add(previewBox = createPreviewBox());
		previewBox.setAlignmentX(0.5f);

		panel.add(Box.createVerticalGlue());

		addActions();

		updateProjectInfo();
		redrawPreview();
	}


	private Box createPreviewBox() {

		previewBorder = new CompoundBorder(BorderFactory.createLineBorder(new Color(0x666666)), BorderFactory.createEmptyBorder(5, 5, 5, 5));

		//@formatter:off
		VBox vbox, vb2;
		
		vbox = new VBox();
		JXTitledSeparator s = new JXTitledSeparator("Selected Item Preview");
		s.setAlignmentX(0);
		vbox.add(s);
		
		Filler f = (Filler) Box.createRigidArea(new Dimension(305, 1));
		f.setAlignmentX(0);
		vbox.add(f);

		HBox hb;

		previewPanel = new JPanel(previewCardLayout = new CardLayout());
		previewPanel.setAlignmentX(0);
		previewCardLayout.setVgap(0);
		
		
		vb2 = new VBox();
			vb2.setAlignmentX(0);
						
			vb2.gap();
			
			hb = new HBox();
				hb.setAlignmentX(0);
				
				btnEditI = new JButton("Edit", Icons.MENU_EDIT);
				btnMetaI = new JButton("Meta", Icons.MENU_EDIT);
				btnReplaceI = new JButton("Replace", Icons.MENU_IMPORT_BOX);
		
				hb.glue();
				hb.add(btnEditI);
				hb.gap();
				hb.add(btnMetaI);
				hb.gap();
				hb.add(btnReplaceI);
				hb.glue();
						
			vb2.add(hb);
			vb2.gap();
			
			hb = new HBox();
				hb.setAlignmentX(0);
				hb.glue();
				
				JPanel p = new JPanel();
				previewImageBg = new JPanelWithBackground(Icons.TRANSPARENT.getImage());				
				
				previewImageBg.add(previewImage = new JLabel());
				previewImageBg.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		
				p.setBorder(
						previewImageBorder = BorderFactory.createTitledBorder(
								previewBorder,
								"%Texture%"
						)
				);
				
				previewImage.setHorizontalAlignment(SwingConstants.CENTER);
				previewImage.setVerticalAlignment(SwingConstants.CENTER);
				
				previewImage.setBorder(BorderFactory.createEmptyBorder());
				previewImageBg.setBorder(BorderFactory.createEmptyBorder());
				
				previewImageBg.setOpaque(true);

				previewImageBg.setPreferredSize(new Dimension(256, 256));
				
				previewImageBg.setBackground(new Color(0x333333));
				p.add(previewImageBg);
				
				Gui.forceSize(p, 290, 300);
		
				hb.add(p);
				
				hb.glue();
			vb2.add(hb);
			vb2.glue();
			
		previewPanel.add(vb2, IMAGE);
		
		
		vb2 = new VBox();
			vb2.setAlignmentX(0);

			vb2.gap();
			
			hb = new HBox();
				hb.setAlignmentX(0);
				
				btnEditT = new JButton("Edit text", Icons.MENU_EDIT);
				btnReplaceT = new JButton("Replace", Icons.MENU_IMPORT_BOX);
		
				hb.glue();
				hb.add(btnEditT);
				hb.gap();
				hb.add(btnReplaceT);
				hb.glue();
					
			vb2.add(hb);
			vb2.gap();
						
			
			hb = new HBox();
				hb.setAlignmentX(0);
				hb.glue();
				
				JScrollPane sp = new JScrollPane();
				
				previewText = new JTextArea();
				previewText.setEditable(false);
				previewText.setFont(new Font(Font.MONOSPACED, 0, 10));
				previewText.setMargin(new Insets(5, 5, 5, 5));
				previewText.setEditable(false);
				previewText.setLineWrap(false);
				
				sp.setViewportView(previewText);
				
				Gui.forceSize(sp, 290, 300);
							
				sp.setBorder(
						previewTextBorder = BorderFactory.createTitledBorder(
								previewBorder,
								"%Text%"
						)
				);

				sp.setAlignmentX(0);
				
				hb.add(sp);
				
				hb.glue();
				
			vb2.add(hb);			

			vb2.glue();
		previewPanel.add(vb2, TEXT);

		
		
		
		vb2 = new VBox();
			vb2.setAlignmentX(0);
						
			vb2.gap();	
			
			hb = new HBox();
				hb.setAlignmentX(0);
				
				btnEditA = new JButton("Edit", Icons.MENU_EDIT);
				btnReplaceA = new JButton("Replace", Icons.MENU_IMPORT_BOX);
		
				hb.glue();
				hb.add(btnEditA);
				hb.gap();
				hb.add(btnReplaceA);
				hb.glue();
					
			vb2.add(hb);
			vb2.gap();
			
			hb = new HBox();
				hb.setAlignmentX(0);
				hb.glue();
				JLabel imageIcon;
				hb.add(imageIcon = new JLabel(Icons.AUDIO));		
				Gui.forceSize(imageIcon, 290, 300);
				imageIcon.setHorizontalAlignment(SwingConstants.CENTER);
				imageIcon.setVerticalAlignment(SwingConstants.CENTER);
				
				imageIcon.setBorder(
						previewAudioBorder = BorderFactory.createTitledBorder(
								previewBorder,
								"%Audio%"
						)
				);
		
				hb.glue();
			vb2.add(hb);
			
			vb2.glue();
		previewPanel.add(vb2, AUDIO);
		
		
		vbox.add(previewPanel);

		//@formatter:on

		return vbox;
	}


	private Box createProjectInfoBox() {

		//@formatter:off
		VBox vb = new VBox();
		Filler f = (Filler) Box.createRigidArea(new Dimension(305, 1));
		f.setAlignmentX(0);
		vb.add(f);
		
		vb.titsep("Project Info").setAlignmentX(0);

		HBox hb;

		hb = new HBox();

			buttonEditProps = new JButton(Icons.MENU_SETUP);
			hb.add(buttonEditProps);
			hb.gap();
	
			projectName = new JXLabel(" ");
			projectName.setToolTipText("Project name");
			projectName.setForeground(new Color(0x333366));
			projectName.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
			projectName.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			projectName.setAlignmentX(0);
			hb.add(projectName);
	
			hb.padding(5, 5, 5, 5);
			hb.glue();
			hb.setAlignmentX(0);
		vb.add(hb);

		hb = new HBox();
			hb.glue();
			hb.add(projectIconLabel = new JXLabel());
	
			//@formatter:off
			projectIconLabel.setBorder(
					BorderFactory.createTitledBorder(
							new CompoundBorder(
									BorderFactory.createLineBorder(new Color(0x666666)),
									BorderFactory.createEmptyBorder(5, 5, 5, 5)
							),
							"Project Icon"
					)
			);	

			projectIconLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

			hb.glue();
			hb.setAlignmentX(0);
		vb.add(hb);

		hb = new HBox();
			buttonOpenBase = new JButton(Icons.MENU_OPEN);
			hb.add(buttonOpenBase);
			hb.gap();
	
			projectBase = new JXLabel("Open in file manager...");
			projectBase.setForeground(new Color(0x333333));
			projectBase.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
			hb.add(projectBase);
	
			hb.padding(5, 5, 5, 5);
			hb.glue();
			hb.setAlignmentX(0);
		vb.add(hb);
		vb.glue();
		//@formatter:on

		return vb;
	}


	public void updateProjectInfo() {

		Project p = Projects.getActive();

		if (p != null) {

			String name = p.getProjectTitle();
			int length_name = (panel.getWidth() - 75) / 11;
			name = Utils.cropStringAtEnd(name, length_name);
			projectName.setText(name);

			File iconFile = new File(Projects.getActive().getProjectDirectory(), "pack.png");

			ImageIcon ic = Icons.getIconFromFile(iconFile, new Dimension(128, 128));
			projectIconLabel.setIcon(ic);

			infoBox.setVisible(true);

		} else {
			infoBox.setVisible(false);
			previewBox.setVisible(false);
			updatePreview(null);
		}
	}


	public void redrawPreview() {

		updatePreview(displayedLeaf);
	}


	public void updatePreview(AssetTreeNode selected) {

		if (selected == null || selected instanceof AssetTreeGroup) {
			previewBox.setVisible(false);
			displayedLeaf = null;

		} else {
			previewBox.setVisible(true);

			AssetTreeLeaf leaf = (AssetTreeLeaf) selected;
			String source = leaf.resolveAssetSource();

			String path = leaf.getAssetEntry().getPath();
			String fname = FileUtils.getFilename(path);

			displayedLeaf = leaf;

			InputStream in;

			EAsset type = leaf.getAssetType();

			if (type.isImage()) {

				// image asset
				String key = leaf.getAssetKey();
				if (key.startsWith("assets.minecraft.textures.font.")) {
					previewImageBg.setbackground(Icons.TRANSPARENT_FONTS.getImage());
				} else {
					previewImageBg.setbackground(Icons.TRANSPARENT.getImage());
				}


				try {
					in = Sources.getAssetStream(source, leaf.getAssetKey());
				} catch (IOException e) {
					return;
				}

				if (in == null) {
					previewImage.setIcon(null);
				} else {
					ImageIcon i = Icons.getIconFromStream(in, new Dimension(256, 256));

					previewImage.setIcon(i);

					String fn = Utils.cropStringAtEnd(fname, 25);

					previewImageBorder.setTitle(fn + " (" + i.getDescription() + ")");
				}

				boolean metaInProj = leaf.isMetaProvidedByProject();
				btnMetaI.setIcon(metaInProj ? Icons.MENU_EDIT : Icons.MENU_NEW);

				previewCardLayout.show(previewPanel, IMAGE);

			} else if (type.isText()) {

				// text asset
				String text;

				try {
					in = Sources.getAssetStream(leaf.resolveAssetSource(), leaf.getAssetKey());
					text = FileUtils.streamToString(in, 100);
				} catch (Exception e) {
					return;
				}


				if (in == null) {
					previewText.setText("");
				} else {
					previewText.setText(text);

					previewTextBorder.setTitle(fname);
				}

				previewText.setCaretPosition(0); // scroll to top

				previewCardLayout.show(previewPanel, TEXT);

			} else if (type.isSound()) {

				// sound asset
				previewAudioBorder.setTitle(fname);

				previewCardLayout.show(previewPanel, AUDIO);

			} else {

				// undisplayable
				previewBox.setVisible(false);
			}


			previewBox.repaint();
		}
	}


	private void addActions() {

		buttonOpenBase.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				Tasks.taskOpenProjectFolder();
			}
		});

		buttonEditProps.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				Tasks.taskDialogProjectProperties();
			}
		});

		projectIconLabel.addMouseListener(new ClickListener() {

			@Override
			public void mouseClicked(MouseEvent e) {

				Tasks.taskDialogProjectProperties();
			}
		});

		ActionListener listenerReplace = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				Tasks.taskImportReplacement(displayedLeaf);
			}
		};

		btnReplaceI.addActionListener(listenerReplace);
		btnReplaceT.addActionListener(listenerReplace);
		btnReplaceA.addActionListener(listenerReplace);

		btnEditI.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				Tasks.taskEditAsset(displayedLeaf);
			}
		});


		btnMetaI.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				Tasks.taskEditMeta(displayedLeaf);
			}
		});

		btnEditT.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				Tasks.taskEditAsset(displayedLeaf);
			}
		});


		btnEditA.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				Tasks.taskEditAsset(displayedLeaf);
			}
		});
	}
}