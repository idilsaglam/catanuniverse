/*
	Binôme 35
	22015094 - Idil Saglam
	 - Abderrahim Arous
*/
package org.catanuniverse.client.configuration.settings;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.function.Consumer;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.text.AbstractDocument;

class ServerAddressSelector extends JPanel {

    private final JTextField textField;
    private String defaultValue;

    /**
     * Creates a serer address selector
     *
     * @param labelText The text of the label of the selector panel
     * @param toolTipText The tooltip text
     * @param columns The number of columns on the text field
     * @param callback The callback function that will be called each time the selector is updated
     */
    protected ServerAddressSelector(
            String labelText, String toolTipText, int columns, Consumer<URI> callback) {
        this.textField = new JTextField(columns);
        JLabel label = new JLabel(labelText);
        this.defaultValue = "";
        super.setToolTipText(toolTipText);
        // TODO: Add input validator
        this.textField.addCaretListener(
                (CaretEvent ignore) -> {
                    String current = this.textField.getText();
                    if (this.defaultValue.equals(current)) {
                        return;
                    }
                    this.defaultValue = current;
                    try {
                        callback.accept(new URI(current));
                    } catch (URISyntaxException e) {
                        System.out.println("Not a valid uri");
                    }
                });
        ((AbstractDocument) this.textField.getDocument())
                .setDocumentFilter(new MaximumInputLengthFilter(2048));
        this.textField.setInputVerifier(
                new InputVerifier() {
                    @Override
                    public boolean verify(JComponent input) {
                        String text = ((JTextField) input).getText();
                        try {
                            new URI(text);
                            return true;
                        } catch (URISyntaxException e) {
                            return false;
                        }
                    }
                });
        this.textField.setVerifyInputWhenFocusTarget(true);
        this.textField.setToolTipText(toolTipText);
        this.add(label);
        this.add(this.textField);
    }

    /**
     * Creates a server address selector with a callback function
     *
     * @param callback The callback function that will be called each time the selector is updated
     */
    protected ServerAddressSelector(Consumer<URI> callback) {
        this("Enter the game server URI", "Set the URI of the game server to join", 32, callback);
    }

    /**
     * Verify if the server address is valid
     *
     * @return True if the server address is valid or not
     */
    boolean isServerAddressValid() {
        return this.textField.isValid();
    }
}
