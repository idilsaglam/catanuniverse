package org.catanuniverse.client.game.board;

import java.awt.*;
import java.util.HashMap;
import java.util.function.BiConsumer;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.catanuniverse.core.game.Resource;

public class Exchange extends JPanel {

  private final BiConsumer<HashMap<Resource, Integer>, Resource> callback;
  private final HashMap<Resource, Integer> inputResources;
  private final HashMap<Resource, Integer> exchangeOutput;
  private final int coeff;
  private final JPanel resourcesToGive, resourcesToReceive;
  private final JButton confirmExchangeButton;
  /**
   * Create an exchange panel with given callback function
   * @param callback The callback methods which will be called when exchange button clicked
   * @param inputResources The input resource to exchange
   * @param coeff The exchange rate
   */
  public Exchange(
      BiConsumer<HashMap<Resource, Integer>, Resource> callback,
      HashMap<Resource, Integer> inputResources,
      int coeff) {
    this.callback = callback;
    this.inputResources = inputResources;
    this.coeff = coeff;
    this.exchangeOutput = new HashMap<>();
    this.resourcesToGive = new JPanel();
    this.initResourcesToGivePanel();
    this.resourcesToReceive = new JPanel();
    this.initResourceToReceivePanel();
    this.confirmExchangeButton = new JButton("Exchange");
    this.initPanels();

  }

  private void initResourcesToGivePanel() {
    this.resourcesToGive.setLayout(new GridLayout(3, 0));
    TitledBorder titledBorder = BorderFactory.createTitledBorder("Give");
    titledBorder.setTitleJustification(TitledBorder.CENTER);
    this.resourcesToGive.setBorder(titledBorder);
    SpinnerRow spinnerRow;
    for(Resource resource: this.inputResources.keySet()) {
      this.exchangeOutput.put(resource, 0);
      spinnerRow = new SpinnerRow(
              resource,
              this.inputResources.get(resource),
              0,
              (ChangeEvent e) -> {
                this.exchangeOutput.put(resource, (Integer) ((JSpinner) e.getSource()).getValue());
              }
      );
      this.resourcesToGive.add(spinnerRow);
    }
  }

  private void initResourceToReceivePanel() {
    this.resourcesToReceive.setLayout(new GridLayout(3, 0));
    TitledBorder titledBorder = BorderFactory.createTitledBorder("Receive");
    titledBorder.setTitleJustification(TitledBorder.CENTER);
    this.resourcesToReceive.setBorder(titledBorder);
    ButtonGroup buttonGroup = new ButtonGroup();
    JRadioButton radioButton;
    for (Resource resource: Resource.values()) {
      radioButton = new JRadioButton(resource.toString());
      this.resourcesToReceive.add(radioButton);
      buttonGroup.add(radioButton);
    }
  }

  private void initPanels() {
    GridBagConstraints gbc = new GridBagConstraints();
    this.setLayout(new GridBagLayout());
    gbc.gridx = 0;
    gbc.gridy = 0;
    this.add(this.resourcesToGive, gbc);
    gbc.gridx++;
    this.add(this.resourcesToReceive, gbc);
    gbc.gridx--;
    gbc.gridy++;
    this.add(this.confirmExchangeButton, gbc);
  }


  private static class SpinnerRow extends JPanel {
    private final SpinnerModel model;
    private final JSpinner spinner;
    private final JLabel label;
    public SpinnerRow(Resource resource, int maxValue, int currentValue, ChangeListener changeListener) {
      this.model = new SpinnerNumberModel(
          currentValue,
          0,
          maxValue,
          1
      );
      this.spinner = new JSpinner(this.model);
      this.label = new JLabel(resource.toString());
      GridBagConstraints gbc = new GridBagConstraints();
      this.setLayout(new GridBagLayout());
      gbc.gridx = 0;
      gbc.gridy = 0;
      this.add(this.label, gbc);
      gbc.gridx++;
      this.spinner.addChangeListener(changeListener);
      this.add(this.spinner, gbc);
    }
  }







}
