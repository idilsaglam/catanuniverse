package org.catanuniverse.client.game.board;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.catanuniverse.core.game.Harbor;
import org.catanuniverse.core.game.Resource;

public class Exchange extends JTabbedPane {
  private static final int DEFAULT_COEFFICIENT = 3;

  private final BiConsumer<HashMap<Resource, Integer>, AbstractMap.Entry<Resource, Integer>> callback;
  private HashMap<Resource, Integer> inputResources;
  private final int coeff;
  private Set<Harbor> harbors;
  /**
   * Create an exchange panel with given callback function
   * @param callback The callback methods which will be called when exchange button clicked
   * @param inputResources The input resource to exchange
   * @param harbors The list of harbors of the current user
   * @param coeff The exchange rate
   */
  public Exchange(
          BiConsumer<HashMap<Resource, Integer>, AbstractMap.Entry<Resource, Integer>> callback,
          HashMap<Resource, Integer> inputResources,
          Set<Harbor> harbors, int coeff) {
    this.callback = callback;
    this.update(inputResources, harbors);
    this.coeff = coeff;

    // TODO: Create other panes for harbors
  }

  private void removeAllTabs() {
    for (int i = 0; i<this.getTabCount(); i++) {
      this.removeTabAt(i);
    }
  }

  /**
   * Updates the inputResources and harbors for the exchange
   * @param inputResources The hashmap of input resources of the current user
   * @param harbors The set of harbors of the current user
   */
    public void update(HashMap<Resource, Integer> inputResources, Set<Harbor> harbors) {
    this.removeAllTabs();
    this.inputResources = inputResources;
    this.harbors = harbors;
    this.addTab("Exchange", new ExchangePane(this.inputResources, this.coeff));
    this.harbors.forEach(this::addNewPane);
  }

  private void addNewPane(Harbor harbor) {
    Supplier<HashMap<Resource, Integer>> genereateResources = () -> {
      if (harbor.getResource() == null) {
        return this.inputResources;
      }
      HashMap<Resource, Integer> result = new HashMap<>();
      result.put(harbor.getResource(), this.inputResources.get(harbor.getResource()));
      return result;
    };
    this.addTab(
            String.format(
                    "%d:1 %s",
                    harbor.getCoeff(),
                    harbor.getResource() == null ? "?" : harbor.getResource()),
            new ExchangePane(
               genereateResources.get(),
                harbor.getCoeff()
            )
            );
  }


  public void updateHarbors(Set<Harbor> harbors) {
    this.harbors = harbors;
    // TODO: Update tab panels
  }


  private class ExchangePane extends JPanel {

    private final JPanel resourcesToGive, resourcesToReceive;
    private final JButton confirmExchangeButton;
    private final HashMap<Resource, Integer> inputResources;
    private final int coefficient;
    private final HashMap<Resource, Integer> exchangeOutput;

    public ExchangePane(HashMap<Resource, Integer> inputResources, int coefficient) {
      this.inputResources = inputResources;
      this.resourcesToGive = new JPanel();
      this.exchangeOutput = new HashMap<>();
      this.coefficient = coefficient;

      this.initResourcesToGivePanel();
      this.resourcesToReceive = new JPanel();
      this.initResourceToReceivePanel();
      this.confirmExchangeButton = new JButton("Exchange");
      this.initPanels();

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
      this.confirmExchangeButton.addActionListener(this::exchange);
    }

    /**
     * Handles the exhange operation
     * @param ignore The click event
     */
    private void exchange(ActionEvent ignore) {
      // TODO: Complete
    }

    private void initResourceToReceivePanel() {
      this.resourcesToReceive.setLayout(new GridLayout(3, 0));
      TitledBorder titledBorder = BorderFactory.createTitledBorder("Receive");
      titledBorder.setTitleJustification(TitledBorder.CENTER);
      this.resourcesToReceive.setBorder(titledBorder);
      ButtonGroup buttonGroup = new ButtonGroup();
      JRadioButton radioButton;
      for (Resource resource : Resource.values()) {
        radioButton = new JRadioButton(resource.toString());
        this.resourcesToReceive.add(radioButton);
        buttonGroup.add(radioButton);
      }
    }

    private void initResourcesToGivePanel() {
      this.resourcesToGive.setLayout(new GridLayout(3, 0));
      TitledBorder titledBorder = BorderFactory.createTitledBorder("Give");
      titledBorder.setTitleJustification(TitledBorder.CENTER);
      this.resourcesToGive.setBorder(titledBorder);
      SpinnerRow spinnerRow;
      for (Resource resource : this.inputResources.keySet()) {
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
