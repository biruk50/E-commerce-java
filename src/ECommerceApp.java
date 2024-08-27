//javac -cp "../lib/*" -d bin ECommerceApp.java Product.java BuyPanel.java SellPanel.java DB.java BaseFrame.java| java -cp "../lib/*;bin" ECommerceApp
import javax.swing.*;
import java.awt.*;

public class ECommerceApp extends BaseFrame {

    public DB db;

    public ECommerceApp(DB db) {
        super("E_Commerce App");
        this.db = db;
    }

    @Override
    protected void addGuiComponents() {
        super.setResizable(true);
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        JMenuItem sellItem = new JMenuItem("Sell");
        JMenuItem buyItem = new JMenuItem("Buy");

        sellItem.addActionListener(e -> {
            setContentPane(new SellPanel(db));
            revalidate();  // Ensures that the content pane is properly reloaded
            repaint();
        });

        buyItem.addActionListener(e -> {
            setContentPane(new BuyPanel(db));
            revalidate();  // Ensures that the content pane is properly reloaded
            repaint();
        });

        menu.add(sellItem);
        menu.add(buyItem);
        menuBar.add(menu);
        setJMenuBar(menuBar);

        setContentPane(new JPanel());
    }

    public static void main(String[] args) {
        DB db = new DB();
        SwingUtilities.invokeLater(() -> new ECommerceApp(db).setVisible(true));
    }
    
}
