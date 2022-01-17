package model_entity;

import java.awt.event.KeyEvent;
import javax.swing.JTextField;

/**
 *
 * @author julian076
 */
public class Eventos {

    public void soloTexto(KeyEvent evt) {
// declaramos una variable y le asignamos un evento
        char caracter = evt.getKeyChar();
        if ((caracter < 'a' || caracter > 'z') && (caracter < 'A' || caracter > 'Z')
                && (caracter != (char) KeyEvent.VK_BACK_SPACE) && (caracter != (char) KeyEvent.VK_SPACE)) {
            evt.consume();
        }
    }

    public void soloNumeros(KeyEvent evt) {
// declaramos una variable y le asignamos un evento
        char numeros = evt.getKeyChar();
        if ((numeros < '0' || numeros > '9') && (numeros != (char) KeyEvent.VK_BACK_SPACE)) {
            evt.consume();
        }
    }

    public void soloDecimal(KeyEvent evt, JTextField textField) {
// declaramos una variable y le asignamos un evento
        char decimal = evt.getKeyChar();
        if ((decimal < '0' || decimal > '9') && textField.getText().contains(".") && (decimal != (char) KeyEvent.VK_BACK_SPACE)) {
            evt.consume();
        } else if ((decimal < '0' || decimal> '9') && (decimal != '.') && (decimal != (char) KeyEvent.VK_BACK_SPACE)) {
            evt.consume();
        }
    }

}
