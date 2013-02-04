package fi.iki.photon.batmud.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import fi.iki.photon.batmud.AreaContainer;

/**
 * The top half of the BatPathFinderUI user interface.
 * 
 * This class extends JPanel and contains all the user interface widgets.
 * As described on the other class, these widgets also act as a memory, so whenever
 * some setting value is needed (for example "to" field), this class is queried.
 * 
 * @author Teppo Kankaanp‰‰
 *
 */

public class BatPathFinderWindow extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private final JLabel autoFindLabel = new JLabel("Auto-find on:");
	private final JComboBox<String> continentBox = new JComboBox<>(new String[] { "Laenor", "Desolathya", "Rothikgen", "Lucentium", "Furnachia" });
	private final JLabel fromLabel = new JLabel("From:");
	private final JTextField from = new JTextField(10);
	private final JLabel toLabel = new JLabel("To:");
	private final JTextField to = new JTextField(10);
//	private final JLabel navalLabel = new JLabel("Ship:");
	private final JRadioButton walkR = new JRadioButton("Walk");
	private final JRadioButton navalR = new JRadioButton("Ship");
//	private final JLabel interLabel = new JLabel("Intercont.:");
	private final JRadioButton interContinentalR = new JRadioButton("IC Ship");
	private final JRadioButton esirisR = new JRadioButton("Esiris");
//	private final JLabel mountLabel = new JLabel("Mounted:");
	private final JRadioButton mountR = new JRadioButton("Mount");
	private final JComboBox<String> navLevel = new JComboBox<>(new String[] { "1","2","3","4","5","6" });
	private final JTextField liftPower = new JTextField("140",3);
	private final JButton find = new JButton("Find!");
	private final JTextArea resultArea = new JTextArea(3, 30);
	private final JButton go = new JButton("Go!");
	private JScrollPane resultScroller;

	private final BatPathFinderUI bpfui;
	
//	private int currCont = 0;
//	private String contString = "Laenor";

//	private JPanel pathPanel;

	/**
	 * A standard constructor.
	 * @param ui
	 */
	
	BatPathFinderWindow(BatPathFinderUI ui) {
		this.bpfui = ui;
		
//		pathPanel = new JPanel();
		setLayout(new GridLayout2(6, 1));
//		this.setLayout(new GridBagLayout());
//		GridBagConstraints c = new GridBagConstraints();
		JPanel texts = new JPanel(new GridLayout2(3,2));
		JPanel results = new JPanel(new BorderLayout());
		JPanel choices = new JPanel(new FlowLayout());
		
		texts.add(autoFindLabel);
		texts.add(continentBox);
		texts.add(fromLabel);
		texts.add(from);
		texts.add(toLabel);
		texts.add(to);
		choices.add(walkR);
		walkR.setSelected(true);
//		choices.add(mountLabel);
		choices.add(mountR);
//		choices.add(navalLabel);
		choices.add(navalR);
//		choices.add(interLabel);
		choices.add(interContinentalR);
		choices.add(esirisR);
		choices.add(new JLabel("N:"));
		choices.add(navLevel);
		navLevel.setSelectedItem("5");
		choices.add(new JLabel("L:"));
		choices.add(liftPower);
//		pathPanel.add(texts, BorderLayout.NORTH);
		add(texts);
		add(choices);
//		pathPanel.add(choices2);
		add(find);

		ButtonGroup choiceGroup = new ButtonGroup();
		choiceGroup.add(walkR);
		choiceGroup.add(navalR);
		choiceGroup.add(interContinentalR);
		choiceGroup.add(mountR);
		choiceGroup.add(esirisR);
		
//		from.setActionCommand("from");
//		from.addActionListener(this);
//		to.setActionCommand("to");
//		to.addActionListener(this);
	
		// No need to listen to continent modifications. We read it
		// when we need it.
//		continentBox.addActionListener(this);
		setContinent("Laenor");
//		continentBox.setActionCommand("cont");
		
		resultArea.setLineWrap(true);
		resultArea.setEditable(false);
		resultScroller = new JScrollPane(resultArea);
		resultScroller.setPreferredSize(new Dimension(200,50));
		
		results.add(new JLabel("Path left:"));
		results.add(resultScroller);
		add(results);

		add(go);
		find.setActionCommand("find");
		find.addActionListener(this);
		go.setActionCommand("go");
		go.addActionListener(this);
	}

	/**
	 * A standard getter for status of go button.
	 * @return go
	 */
	
	boolean isGoEnabled() {
		return go.isEnabled();
	}
	/**
	 * A standard setter for status of go button.
	 */

	void setGoEnabled(boolean b) {
		go.setEnabled(b);
	}

	/**
	 * A standard getter for the from field.
	 * @return from
	 */

	String getFrom() {
		return from.getText();
	}

	/**
	 * A standard getter for the to field.
	 * @return to
	 */
	String getTo() {
		return to.getText();
	}
	
	/**
	 * A standard setter for the from field.
	 */
	void setFrom(String text) {
		from.setText(text);
	}

	/**
	 * A standard setter for the to field.
	 */
	void setTo(String text) {
		to.setText(text);
	}

	/**
	 * Returns the type of travel that is selected: walk=1, mounted=2, naval=3, interContinental=4, esiris=5.
	 */
	
	int getTravel() {
		if (walkR.isSelected()) return 1;
		if (mountR.isSelected()) return 2;
		if (navalR.isSelected()) return 3;
		if (interContinentalR.isSelected()) return 4;
		if (esirisR.isSelected()) return 5;
		return 0;
	}

	/**
	 * Sets the type of travel.
	 * @param t
	 */
	
	void setTravel(int t) {
		switch (t) {
		case 1: walkR.setSelected(true); break;
		case 2: mountR.setSelected(true); break;
		case 3: navalR.setSelected(true); break;
		case 4: interContinentalR.setSelected(true); break;
		case 5: esirisR.setSelected(true); break;
		}
	}

	/**
	 * Sets the report area.
	 * @param text
	 */
	
	void setReport(String text) {
		resultArea.setText(text);
	}
	
	/**
	 * A standard setter for continent.
	 * @param continent
	 */
	
	void setContinent(String continent) {
		continentBox.setSelectedItem(continent);
	}

	/**
	 * Returns the integer value of the continent based on AreaContainer.CONT_LAENOR etc.
	 * @return Integer value of a continent.
	 */
	
	int getContinent() {
		String cont = (String) continentBox.getSelectedItem();
		if ("Laenor".equals(cont)) { return AreaContainer.CONT_LAENOR; }
		if ("Lucentium".equals(cont)) { return AreaContainer.CONT_LUC; }
		if ("Desolathya".equals(cont)) { return AreaContainer.CONT_DESO; }
		if ("Rothikgen".equals(cont)) { return AreaContainer.CONT_ROTH; }
		if ("Furnachia".equals(cont)) { return AreaContainer.CONT_FURN; }
		return -1;
	}

	/**
	 * A standard getter for continent.
	 * @return continent string
	 */

	String getContString() {
		return (String) continentBox.getSelectedItem();
	}

	/**
	 * A standard getter for navigator level.
	 * @return navigator level
	 */
	int getNav() {
		return navLevel.getSelectedIndex();
	}
	/**
	 * A standard getter for lift level.
	 * @return lift level
	 */
	int getLift() {
		return Integer.parseInt(liftPower.getText());
	}
	/**
	 * Listener for find and go buttons.
	 * On click perform dosearch or dogo.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
//		System.out.println(e);
		if ("find".equals(e.getActionCommand())) {
			bpfui.doSearch();
			setGoEnabled(true);
		}
		if ("go".equals(e.getActionCommand())) {
			bpfui.doGo(false);
		}
		/*
		if ("cont".equals(e.getActionCommand())) {
			if (! (e.getSource() instanceof JComboBox<?>)) {
				return;
			}
			@SuppressWarnings("unchecked")
			JComboBox<String> cb = (JComboBox<String>)e.getSource();
			String cont = (String) cb.getSelectedItem();
//			int newHitType = selectedHitType;
//			System.out.println()
			setContinent(cont);
		}
		*/
	}

}
