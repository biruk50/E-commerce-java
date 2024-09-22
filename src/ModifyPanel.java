import java.awt.*;
import javax.swing.*;;

public class ModifyPanel extends SellPanel {
    private int productId;
    private DB db;

    public ModifyPanel(DB db, int productId) {
        super(db);
        this.db = db;
        this.productId = productId;
    }

    @Override
    protected void handleSubmit() {
        String name = nameField.getText();
        String description = descriptionArea.getText();
        double price;
        int quantity;

        try {
            price = Double.parseDouble(priceField.getText());
            quantity = Integer.parseInt(quantityField.getText());
        } catch (NumberFormatException e) {
            showErrorMessage("Please enter valid numbers for price and quantity");
            return;
        }

        if (name.isEmpty() || description.isEmpty() || photoPaths.isEmpty()) {
            showErrorMessage("Please fill all the fields and upload at least one photo");
            return;
        }
            db.updateProduct(productId, name, description, price, quantity);
            JOptionPane.showMessageDialog(ModifyPanel.this, "Product updated successfully!", "Success", JOptionPane.PLAIN_MESSAGE);
    }
}
