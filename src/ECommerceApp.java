
//javac -cp "../lib/*" -d bin ECommerceApp.java Product.java BuyPanel.java SellPanel.java DB.java BaseFrame.java| java -cp "../lib/*;bin" ECommerceApp
import javax.swing.*;
import java.awt.*;

public class ECommerceApp extends BaseFrame {

    public DB db;
    private static String username;

    public ECommerceApp(DB db, String username) {
        super("E_Commerce App");
        this.db = db;
        ECommerceApp.username = username;
        addGuiComponents();
    }

    @Override
    protected void addGuiComponents() {
        super.setResizable(true);
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        JMenuItem sellItem = new JMenuItem("Sell");
        JMenuItem buyItem = new JMenuItem("Buy");
        JMenuItem viewYourProducts = new JMenuItem("View Your Products");
        menu.add(viewYourProducts);

        // Welcome Panel in the center
        JPanel welcomePanel = new JPanel();
        welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS)); // Vertical stacking
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        welcomePanel.setBackground(new Color(229, 207, 251));

        JLabel welcomeLabel = new JLabel("Welcome " + ECommerceApp.username + "!");
        welcomeLabel.setBackground(new Color(229, 207, 251));
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center align
        welcomePanel.add(welcomeLabel);
        this.add(welcomePanel, BorderLayout.CENTER);

        welcomePanel.add(Box.createVerticalStrut(20));

        JLabel infoLabel = new JLabel("Choose to buy or sell a product by clicking the Menu tab.");
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center align
        welcomePanel.add(infoLabel);

        sellItem.addActionListener(e -> {
            setContentPane(new SellPanel(db));
            revalidate(); // Ensures that the content pane is properly reloaded
            repaint();
        });

        buyItem.addActionListener(e -> {
            setContentPane(new BuyPanel(db));
            revalidate(); // Ensures that the content pane is properly reloaded
            repaint();
        });

        viewYourProducts.addActionListener(e -> {
            setContentPane(new UserProductsPanel(db, this));
            revalidate(); // Ensures that the content pane is properly reloaded
            repaint();
        });
        menu.add(sellItem);
        menu.add(buyItem);
        menuBar.add(menu);
        setJMenuBar(menuBar);

        setContentPane(welcomePanel);
        welcomePanel.revalidate();
        welcomePanel.repaint();
    }

    public static void main(String[] args) {
        DB db = new DB();
        SwingUtilities.invokeLater(() -> new ECommerceApp(db, ECommerceApp.username).setVisible(true));
    }

}
