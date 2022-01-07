package org.catanuniverse.client.game.board;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.HashMap;
import java.util.function.BiConsumer;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.catanuniverse.core.game.Resource;

public class Exchange extends JPanel {

  private final BiConsumer<HashMap<Resource, Integer>, Resource> callback;
  private final HashMap<Resource, Integer> inputResources;
  private final HashMap<Resource, Integer> exchangeOutput;
  private final int coeff;
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
      this.add(spinnerRow);
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
