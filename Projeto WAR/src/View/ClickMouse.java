package View;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class ClickMouse implements ActionListener {
    Component c;

    public ClickMouse(Component x) {
        c=x;
    }
    public void actionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(c,"Inclusão Efetuada");
    }
}