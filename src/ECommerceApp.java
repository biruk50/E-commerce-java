// //javac -cp "../lib/*" -d bin ECommerceApp.java Product.java BuyPanel.java SellPanel.java DB.java BaseFrame.java| java -cp "../lib/*;bin" ECommerceApp
// import javax.swing.*;
// import java.awt.*;

// public class ECommerceApp extends BaseFrame {

//     public DB db;

//     public ECommerceApp(DB db) {
//         super("E_Commerce App");
//         this.db = db;
//     }

//     @Override
//     protected void addGuiComponents() {
//         super.setResizable(true);
//         JMenuBar menuBar = new JMenuBar();
//         JMenu menu = new JMenu("Menu");
//         JMenuItem sellItem = new JMenuItem("Sell");
//         JMenuItem buyItem = new JMenuItem("Buy");

//         sellItem.addActionListener(e -> {
//             setContentPane(new SellPanel(db));
//             revalidate();  // Ensures that the content pane is properly reloaded
//             repaint();
//         });

//         buyItem.addActionListener(e -> {
//             setContentPane(new BuyPanel(db));
//             revalidate();  // Ensures that the content pane is properly reloaded
//             repaint();
//         });

//         menu.add(sellItem);
//         menu.add(buyItem);
//         menuBar.add(menu);
//         setJMenuBar(menuBar);

//         setContentPane(new JPanel());
//     }

//     public static void main(String[] args) {
//         DB db = new DB();
//         SwingUtilities.invokeLater(() -> new ECommerceApp(db).setVisible(true));
//     }
    
// }
import javax.swing.*;
import java.awt.*;

public class ECommerceApp extends BaseFrame {

    public DB db;

    public ECommerceApp(DB db) {
        super("E-Commerce App");
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
            revalidate();
            repaint();
        });

        buyItem.addActionListener(e -> {
            setContentPane(new BuyPanel(db));
            revalidate();
            repaint();
        });

        menu.add(sellItem);
        menu.add(buyItem);
        menuBar.add(menu);
        setJMenuBar(menuBar);

        // Create a welcome panel with instructions
        JPanel welcomePanel = createWelcomePanel();
        setContentPane(welcomePanel);
    }

    private JPanel createWelcomePanel() {
        JPanel welcomePanel = new JPanel();
        welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        welcomePanel.setBackground(new Color(240, 240, 240));

        JLabel welcomeLabel = new JLabel("Welcome to the E-Commerce App!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextArea instructionsArea = new JTextArea(
            "How to use this app:\n\n" +
            "1. To buy an item: Click on 'Menu' > 'Buy'\n" +
            "2. To sell an item: Click on 'Menu' > 'Sell'\n\n" +
            "You can switch between buying and selling at any time using the menu."
        );
        instructionsArea.setFont(new Font("Arial", Font.PLAIN, 16));
        instructionsArea.setLineWrap(true);
        instructionsArea.setWrapStyleWord(true);
        instructionsArea.setEditable(false);
        instructionsArea.setBackground(new Color(240, 240, 240));
        instructionsArea.setAlignmentX(Component.CENTER_ALIGNMENT);

        welcomePanel.add(welcomeLabel);
        welcomePanel.add(Box.createVerticalStrut(30));
        welcomePanel.add(instructionsArea);

        return welcomePanel;
    }

    public static void main(String[] args) {
        DB db = new DB();
        SwingUtilities.invokeLater(() -> new ECommerceApp(db).setVisible(true));
    }
}