package org.catanuniverse.client.game.board;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;
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
  private Set<Harbor> harbors;
  /**
   * Create an exchange panel with given callback function
   * @param callback The callback methods which will be called when exchange button clicked
   * @param inputResources The input resource to exchange
   * @param harbors The list of harbors of the current user
   */
  public Exchange(
          BiConsumer<HashMap<Resource, Integer>, AbstractMap.Entry<Resource, Integer>> callback,
          HashMap<Resource, Integer> inputResources,
          Set<Harbor> harbors) {
    this.callback = callback;
    this.update(inputResources, harbors);
  }

  /**
   * Updates the inputResources and harbors for the exchange
   * @param inputResources The hashmap of input resources of the current user
   * @param harbors The set of harbors of the current user
   */
    public void update(HashMap<Resource, Integer> inputResources, Set<Harbor> harbors) {

    this.removeAll();
    System.out.printf("Number of tabs in the current pane %d\n", this.getTabCount());
    this.inputResources = inputResources;
    this.harbors = harbors;
    this.addTab("Exchange", new ExchangePane(this.inputResources, Exchange.DEFAULT_COEFFICIENT));
    this.harbors.forEach(this::addNewPane);
    this.revalidate();
    this.repaint();
  }

  /**
   * Add new harbor pane to the current JTabPane
   * @param harbor The harbor which will be used for creating the tab
   */
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


  /**
   * Update harbors
   * @param harbors The new harbors to update
   */
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
    private final ButtonGroup resourceToReceiveButtonGroup;
    public ExchangePane(HashMap<Resource, Integer> inputResources, int coefficient) {
      System.out.printf("Coefficient %d\n",coefficient);
      this.inputResources = inputResources;
      this.resourcesToGive = new JPanel();
      this.exchangeOutput = new HashMap<>();
      this.coefficient = coefficient;
      this.resourceToReceiveButtonGroup = new ButtonGroup();

      this.initResourcesToGivePanel();
      this.resourcesToReceive = new JPanel();
      this.initResourceToReceivePanel();
      this.confirmExchangeButton = new JButton("Exchange");
      this.initPanels();

    }

    /**
     * Initialise both resources to give and resource to receive as well as the confirmation button
     */
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
     * Handles the exchange operation
     * @param ignore The click event
     */
    private void exchange(ActionEvent ignore) {
      int sum = this.exchangeOutput.values().stream().reduce(0, Integer::sum);
      System.out.printf("Sum of the all exchange output values %d\n", sum);
      System.out.printf("The coefficient value %d\n", this.coefficient);
      sum = (int)Math.ceil(sum / (this.coefficient * 1.));
      System.out.printf("The calculated result %d\n", sum);
      Resource r = Resource.valueOf(this.resourceToReceiveButtonGroup.getSelection().getActionCommand());
      Exchange.this.callback.accept(this.exchangeOutput, new AbstractMap.SimpleEntry<>(r, sum));
    }

    private void initResourceToReceivePanel() {
      this.resourcesToReceive.setLayout(new GridLayout(3, 0));
      TitledBorder titledBorder = BorderFactory.createTitledBorder("Receive");
      titledBorder.setTitleJustification(TitledBorder.CENTER);
      this.resourcesToReceive.setBorder(titledBorder);
      JRadioButton radioButton;
      for (Resource resource : Resource.values()) {
        radioButton = new JRadioButton(resource.toString());
        radioButton.setActionCommand(resource.toString());
        this.resourcesToReceive.add(radioButton);
        this.resourceToReceiveButtonGroup.add(radioButton);
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
