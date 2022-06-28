import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class Main
{
    private JPanel JPanel1;
    private JTextField textField1;
    private JButton button1;
    private JTextPane textPane1;

    public Main()
    {
        button1.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                super.mouseClicked(e);
                String path = textField1.getText();
                GenerateExcel generateExcel = new GenerateExcel(path);
                generateExcel.generate();
            }
        });
    }

    public static void main(String[] args)
    {
        JFrame frame = new JFrame("FormMain");
        frame.setContentPane(new Main().JPanel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
