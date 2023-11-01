package Menu;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JOptionPane;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import main.Juego1943;

public class Sistema extends JFrame implements ItemListener, ActionListener {
    private JPanel panelJuego;
    private JPanel panelImagen;
    private JPanel panelLista;
    private List juegos;
    private JButton iniciar;
    private JButton ajustes;
    private JPanel panelAccion;
    private CardLayout cardLayout;

    private Juego1943 juego1943; 

    private JTextField name;

    public Sistema() { //debe recibir un juego
        setSize(800, 580);
        setTitle("Menu de Juegos");
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        iniciarComponentes();
    }

    private void iniciarComponentes() {
        colocarPanel();
        colocarLista();
        colocarBotones();
        colocarNombre();
        mostrarImagenPorDefecto();
    }

    private void colocarPanel() {
        panelJuego = new JPanel();
        panelJuego.setLayout(null);
        getContentPane().add(panelJuego, BorderLayout.CENTER);
    }

    private void colocarNombre(){
        JLabel etiquetaUsuario = new JLabel("Nombre de Usuario:");
        name = new JTextField(15);
        JPanel panelNombreUsuario = new JPanel();
        panelNombreUsuario.add(etiquetaUsuario);
        panelNombreUsuario.add(name);
        panelAccion.add(panelNombreUsuario);
    }

    private void colocarBotones() {
        panelAccion = new JPanel();
        iniciar = new JButton("Iniciar");
        ajustes = new JButton("Ajustes");
        ajustes.addActionListener(this);
        iniciar.addActionListener(this);
        panelAccion.add(iniciar);
        panelAccion.add(ajustes);
        this.add(panelAccion, BorderLayout.SOUTH);
    }

    private void colocarLista() {
        panelLista = new JPanel();
        juegos = new List(10, false);
        panelImagen = new JPanel();
        panelJuego.add(panelImagen, BorderLayout.CENTER);
        getContentPane().add(panelImagen);
        cardLayout = new CardLayout();
        panelImagen.setLayout(cardLayout);
        getContentPane().add(panelLista, BorderLayout.WEST);
        panelLista.add(juegos);
        listaVentanaPrincipal();
        juegos.select(0);
    }

    private void listaVentanaPrincipal() {
        juegos.add("Juego1943");
        juegos.add("Pacman");
        juegos.add("Tetris");
        juegos.addItemListener(this);
        this.add(panelLista, BorderLayout.WEST);
        panelLista.add(juegos);
    }

    private void mostrarImagenPorDefecto(){
        panelImagen.removeAll();
        JLabel labelBatallaMidway = new JLabel(new ImageIcon("res/juegos/BatallaMidway.png"));
        panelImagen.add(labelBatallaMidway);
        panelImagen.revalidate();
        panelImagen.repaint();
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        String selectedItem = juegos.getSelectedItem();
        switch (selectedItem) {
            case "Juego1943":
                mostrarImagenPorDefecto();
                break;
            case "Pacman":
                panelImagen.removeAll();
                JLabel labelPacman = new JLabel(new ImageIcon("res/juegos/Pacman.png"));
                panelImagen.add(labelPacman);
                break;
            case "Tetris":
                panelImagen.removeAll();
                JLabel labelTetris = new JLabel(new ImageIcon("res/juegos/Tetris.png"));
                panelImagen.add(labelTetris);
                break;
            default:
                break;
        }
        panelImagen.revalidate();
        panelImagen.repaint();
        cardLayout.show(panelImagen, (String) e.getItem());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String juegoSeleccionado = juegos.getSelectedItem();
        String usuario = name.getText();

        if (e.getActionCommand().equals(iniciar.getActionCommand())) {
            if (juegoSeleccionado.equals("Juego1943") && !usuario.isEmpty()) { 
                juego1943 = new Juego1943(); //le hago un new de cada juego
                juego1943.setJugador(new Jugador(usuario));
                juego1943.start();
                setVisible(false); // Oculta la ventana de Sistema
            } else{
                JOptionPane.showMessageDialog(this, "Debe ingresar su nombre de usuario", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        } else if (e.getActionCommand().equals(ajustes.getActionCommand())) {
            Ajustes ajustes = new Ajustes();
            ajustes.setVisible(true);
        }
    }

    public static void main(String[] args) {
        Sistema v1 = new Sistema();
        v1.setVisible(true);
    }
}