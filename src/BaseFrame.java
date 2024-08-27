import javax.swing.*;
import java.awt.*;

public abstract class BaseFrame extends JFrame {
    public BaseFrame(String title) {
        initialize(title);
    }

    private void initialize(String title) {
        setTitle(title);
        setSize(500, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(null);
        setLocationRelativeTo(null);

        addGuiComponents();
    }

    protected abstract void addGuiComponents();

    protected JPanel createFormField(String label, JComponent field) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel jLabel = new JLabel(label);
        jLabel.setPreferredSize(new Dimension(110, 25));
        panel.add(jLabel);
        panel.add(field);
        panel.setOpaque(false);
        return panel;
    }
}