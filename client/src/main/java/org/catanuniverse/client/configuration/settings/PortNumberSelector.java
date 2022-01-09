/*
	22015094 - Idil Saglam
*/
package org.catanuniverse.client.configuration.settings;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.function.Consumer;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.text.AbstractDocument;

class PortNumberSelector extends JPanel {

    private String defaultPortNumber;
    private final JTextField textField;
    private final JButton randomPortNumberButton;
    private JLabel errorMessage;

    /**
     * Create a new port number selector
     *
     * @param labelText The text of the label of the selector panel
     * @param toolTipText The tooltip text of the selector
     * @param columns The number of the columns in the JTextField
     * @param callback The callback function that will be called each time the text field is updated
     */
    protected PortNumberSelector(
            String labelText, String toolTipText, int columns, Consumer<Integer> callback) {

        super.setToolTipText(toolTipText);
        super.setLayout(new GridBagLayout());

        this.errorMessage = new JLabel("");
        this.errorMessage.setForeground(Color.RED);
        this.errorMessage.setVisible(false);

        GridBagConstraints constraints = new GridBagConstraints();

        this.defaultPortNumber = "";

        this.textField = new JTextField(5);
        ((AbstractDocument) this.textField.getDocument())
                .setDocumentFilter(new MaximumInputLengthFilter(5));
        this.randomPortNumberButton = new JButton("Select automatically");

        this.randomPortNumberButton.addActionListener(
                (ActionEvent ignore) -> {
                    int rp = this.findRandomPort();
                    this.defaultPortNumber = "" + rp;
                    this.textField.setText("" + rp);
                    callback.accept(rp);
                });

        this.textField.setInputVerifier(
                new InputVerifier() {
                    private boolean validatePortNumber(int portNumber) {
                        try {
                            ServerSocket socket = new ServerSocket(0);
                            socket.close();
                            return true;
                        } catch (IOException ignore) {
                            return false;
                        }
                    }

                    @Override
                    public boolean verify(JComponent input) {
                        String text = ((JTextField) input).getText();
                        try {
                            int pn = Integer.parseInt(text);
                            boolean result = pn >= 0 && pn <= 65535;
                            PortNumberSelector.this.errorMessage.setVisible(!result);
                            PortNumberSelector.this.errorMessage.setText(
                                    result
                                            ? ""
                                            : String.format(
                                                    "Port number %d is outside of the range 0 to"
                                                            + " 65535",
                                                    pn));
                            PortNumberSelector.this.errorMessage.repaint();
                            if (!result) return false;
                            result = validatePortNumber(pn);
                            PortNumberSelector.this.errorMessage.setVisible(!result);
                            PortNumberSelector.this.errorMessage.setText(
                                    result ? "" : String.format("Port %d is already in use", pn));
                            return result;
                        } catch (NumberFormatException ignore) {
                            PortNumberSelector.this.errorMessage.setText(
                                    String.format("%s is not a valid port number", text));
                            PortNumberSelector.this.errorMessage.setVisible(true);
                            PortNumberSelector.this.errorMessage.repaint();

                            return false;
                        }
                    }
                });

        this.textField.setVerifyInputWhenFocusTarget(true);

        this.textField.addCaretListener(
                (CaretEvent ignore) -> {
                    String currentValue = this.textField.getText();
                    if (currentValue.equals(this.defaultPortNumber)) {
                        return;
                    }
                    this.defaultPortNumber = currentValue;
                    // TODO: Add automatic port number validation
                    if (currentValue.length() == 0) {
                        return;
                    }
                    callback.accept(Integer.parseInt(currentValue));
                });
        this.textField.setToolTipText(toolTipText);
        this.randomPortNumberButton.setToolTipText("Get a random port from available ports");
        JLabel label = new JLabel(labelText);

        constraints.gridx = 0;
        constraints.gridy = 0;
        this.add(label, constraints);
        constraints.gridx = 1;
        this.add(this.textField, constraints);
        constraints.gridx = 2;
        this.add(this.randomPortNumberButton, constraints);
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 3;
        this.add(this.errorMessage, constraints);
    }

    /**
     * Creates a port number selector with a callback function
     *
     * @param callback The callback function that will be called each time the selector is updated
     */
    PortNumberSelector(Consumer<Integer> callback) {
        this("Select a port number of the game server", "Set the game server port", 10, callback);
    }

    /**
     * Verify if the port number is valid
     *
     * @return True if the port number is valid, false if not
     */
    boolean isPortNumberValid() {
        return this.textField.isValid();
    }

    /**
     * Selects a random port from available ports on the computer
     *
     * @return An available port on the current computer
     */
    private Integer findRandomPort() {
        try {
            ServerSocket socket = new ServerSocket(0);
            int portNumber = socket.getLocalPort();
            socket.close();
            return portNumber;
        } catch (IOException ignore) {
            return findRandomPort();
        }
    }
}
