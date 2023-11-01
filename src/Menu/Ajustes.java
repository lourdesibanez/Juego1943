package Menu;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class Ajustes extends JFrame implements ActionListener, ChangeListener, KeyListener {
    private JPanel panel;
    private JCheckBox pantallaCompletaCheckBox;
    private JCheckBox sonidoCheckBox;
    private JComboBox<String> avionComboBox;
    private JSlider volumenSlider;
    private JComboBox<String> pistaMusicalComboBox;
    private JComboBox<String> teclaDisparoComboBox; // Agregado
    private JComboBox<String> teclaPausaComboBox;   // Agregado
    private JComboBox<String> teclaAtaqueEspecialComboBox; // Agregado
    private JButton guardar;
    private JButton volver;
    private JButton reset;

    // Variables para almacenar los códigos de teclas configuradas por el usuario
    private int teclaDisparo = KeyEvent.VK_X;
    private int teclaPausa = KeyEvent.VK_SPACE;
    private int teclaAtaqueEspecial = KeyEvent.VK_Z;

    public Ajustes() {
        setSize(400, 450);
        setTitle("Ajustes");
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        iniciarComponentes();
    }

    private void iniciarComponentes() {
        panel = new JPanel();
        panel.setLayout(null);
        getContentPane().add(panel);
        boolean pantallaCompleta = false;
        boolean sonidoActivado = true;
        String avionSeleccionado;
        int volumen = 50;
        String pistaMusicalSeleccionada;
        try {
            Properties propiedades = new Properties();

            // Abre el archivo de entrada para cargar los valores
            InputStream archivoEntrada = new FileInputStream("jgame.properties");

            // Carga los valores desde el archivo
            propiedades.load(archivoEntrada);

            // Cierra el archivo
            archivoEntrada.close();

            // Obtén los valores cargados del archivo .properties
            pantallaCompleta = Boolean.parseBoolean(propiedades.getProperty("pantallaCompleta"));
            sonidoActivado = Boolean.parseBoolean(propiedades.getProperty("sonidoActivado"));
            avionSeleccionado = propiedades.getProperty("avionSeleccionado");
            volumen = Integer.parseInt(propiedades.getProperty("volumen"));
            pistaMusicalSeleccionada = propiedades.getProperty("pistaMusicalSeleccionada");
            teclaDisparo = Integer.parseInt(propiedades.getProperty("teclaDisparo", String.valueOf(KeyEvent.VK_X)));
            teclaPausa = Integer.parseInt(propiedades.getProperty("teclaPausa", String.valueOf(KeyEvent.VK_SPACE)));
            teclaAtaqueEspecial = Integer.parseInt(propiedades.getProperty("teclaAtaqueEspecial", String.valueOf(KeyEvent.VK_Z)));
        } catch (IOException e) {
            e.printStackTrace();
            // Si ocurre algún error al cargar el archivo o leer los valores, puedes establecer valores por defecto o manejar la situación según lo desees.
        }

        JLabel pantallaCompletaLabel = new JLabel("Juego en pantalla completa:");
        pantallaCompletaLabel.setBounds(20, 20, 200, 25);
        panel.add(pantallaCompletaLabel);

        pantallaCompletaCheckBox = new JCheckBox();
        pantallaCompletaCheckBox.setBounds(250, 20, 20, 25);
        panel.add(pantallaCompletaCheckBox);

        pantallaCompletaCheckBox.setSelected(pantallaCompleta);

        JLabel sonidoLabel = new JLabel("Sonido activado:");
        sonidoLabel.setBounds(20, 60, 200, 25);
        panel.add(sonidoLabel);

        sonidoCheckBox = new JCheckBox();
        sonidoCheckBox.setBounds(250, 60, 20, 25);
        panel.add(sonidoCheckBox);

        sonidoCheckBox.setSelected(sonidoActivado);

        JLabel avionLabel = new JLabel("Seleccionar avión:");
        avionLabel.setBounds(20, 100, 200, 25);
        panel.add(avionLabel);

        avionComboBox = new JComboBox<>();
        avionComboBox.setBounds(200, 100, 150, 25);
        avionComboBox.addItem("Avión original");
        avionComboBox.addItem("Avión alternativo 1");
        avionComboBox.addItem("Avión alternativo 2");
        panel.add(avionComboBox);

        JLabel volumenLabel = new JLabel("Volumen:");
        volumenLabel.setBounds(20, 140, 100, 25);
        panel.add(volumenLabel);

        volumenSlider = new JSlider(0, 100);
        volumenSlider.setBounds(120, 140, 200, 25);
        volumenSlider.addChangeListener(this);
        panel.add(volumenSlider);

        JLabel pistaMusicalLabel = new JLabel("Seleccionar pista musical:");
        pistaMusicalLabel.setBounds(20, 180, 200, 25);
        panel.add(pistaMusicalLabel);

        pistaMusicalComboBox = new JComboBox<>();
        pistaMusicalComboBox.setBounds(200, 180, 150, 25);
        pistaMusicalComboBox.addItem("Tema original");
        pistaMusicalComboBox.addItem("Otra pista 1");
        pistaMusicalComboBox.addItem("Otra pista 2");
        panel.add(pistaMusicalComboBox);

        // Agregar etiquetas y combos para configurar las teclas
        JLabel teclaDisparoLabel = new JLabel("Tecla Disparo:");
        teclaDisparoLabel.setBounds(20, 220, 150, 25);
        panel.add(teclaDisparoLabel);

        teclaDisparoComboBox = new JComboBox<>();
        teclaDisparoComboBox.setBounds(200, 220, 100, 25);
        teclaDisparoComboBox.addItem("X");
        teclaDisparoComboBox.addItem("C");
        teclaDisparoComboBox.addItem("Espacio");
        panel.add(teclaDisparoComboBox);

        JLabel teclaPausaLabel = new JLabel("Tecla Pausa:");
        teclaPausaLabel.setBounds(20, 260, 150, 25);
        panel.add(teclaPausaLabel);

        teclaPausaComboBox = new JComboBox<>();
        teclaPausaComboBox.setBounds(200, 260, 100, 25);
        teclaPausaComboBox.addItem("Espacio");
        teclaPausaComboBox.addItem("P");
        teclaPausaComboBox.addItem("Enter");
        panel.add(teclaPausaComboBox);

        JLabel teclaAtaqueEspecialLabel = new JLabel("Tecla Ataque Especial:");
        teclaAtaqueEspecialLabel.setBounds(20, 300, 180, 25);
        panel.add(teclaAtaqueEspecialLabel);

        teclaAtaqueEspecialComboBox = new JComboBox<>();
        teclaAtaqueEspecialComboBox.setBounds(200, 300, 100, 25);
        teclaAtaqueEspecialComboBox.addItem("Z");
        teclaAtaqueEspecialComboBox.addItem("A");
        teclaAtaqueEspecialComboBox.addItem("B");
        panel.add(teclaAtaqueEspecialComboBox);

        guardar = new JButton("Guardar");
        guardar.setBounds(50, 340, 100, 30);
        guardar.addActionListener(this);
        panel.add(guardar);

        volver = new JButton("Volver");
        volver.setBounds(170, 340, 100, 30);
        volver.addActionListener(this);
        panel.add(volver);

        reset = new JButton("RESET");
        reset.setBounds(290, 340, 80, 30);
        reset.addActionListener(this);
        panel.add(reset);
    }

    private void cargarValoresPorDefecto() {
        pantallaCompletaCheckBox.setSelected(false); // Por defecto, en ventana.
        sonidoCheckBox.setSelected(true); // Por defecto, sonido activado.
        avionComboBox.setSelectedIndex(0); // Por defecto, avión original.
        volumenSlider.setValue(50); // Por defecto, volumen a la mitad.
        pistaMusicalComboBox.setSelectedIndex(0); // Por defecto, tema original.

        // Asignar los valores por defecto a las teclas
        teclaDisparoComboBox.setSelectedItem(KeyEvent.getKeyText(KeyEvent.VK_X));
        teclaPausaComboBox.setSelectedItem(KeyEvent.getKeyText(KeyEvent.VK_SPACE));
        teclaAtaqueEspecialComboBox.setSelectedItem(KeyEvent.getKeyText(KeyEvent.VK_Z));
    }

    public void guardarConfiguracion(boolean pantallaCompleta, boolean sonidoActivado, String avionSeleccionado, int volumen, String pistaMusicalSeleccionada, int teclaDisparo, int teclaPausa, int teclaAtaqueEspecial) {
        try {
            Properties propiedades = new Properties();

            // Establece los valores en el objeto Properties
            propiedades.setProperty("pantallaCompleta", String.valueOf(pantallaCompleta));
            propiedades.setProperty("sonidoActivado", String.valueOf(sonidoActivado));
            propiedades.setProperty("avionSeleccionado", avionSeleccionado);
            propiedades.setProperty("volumen", String.valueOf(volumen));
            propiedades.setProperty("pistaMusicalSeleccionada", pistaMusicalSeleccionada);
            propiedades.setProperty("teclaDisparo", String.valueOf(teclaDisparo));
            propiedades.setProperty("teclaPausa", String.valueOf(teclaPausa));
            propiedades.setProperty("teclaAtaqueEspecial", String.valueOf(teclaAtaqueEspecial));

            // Abre el archivo de salida para guardar los valores
            OutputStream archivoSalida = new FileOutputStream("jgame.properties");

            // Guarda los valores en el archivo
            propiedades.store(archivoSalida, "Configuración del juego");

            // Cierra el archivo
            archivoSalida.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == guardar) {
            // Obtener los valores seleccionados en la configuración
            boolean pantallaCompleta = pantallaCompletaCheckBox.isSelected();
            boolean sonidoActivado = sonidoCheckBox.isSelected();
            String avionSeleccionado = (String) avionComboBox.getSelectedItem();
            int volumen = volumenSlider.getValue();
            String pistaMusicalSeleccionada = (String) pistaMusicalComboBox.getSelectedItem();

            // Obtener los códigos de las teclas seleccionadas por el usuario
            teclaDisparo = obtenerCodigoTecla((String) teclaDisparoComboBox.getSelectedItem());
            teclaPausa = obtenerCodigoTecla((String) teclaPausaComboBox.getSelectedItem());
            teclaAtaqueEspecial = obtenerCodigoTecla((String) teclaAtaqueEspecialComboBox.getSelectedItem());

            guardarConfiguracion(pantallaCompleta, sonidoActivado, avionSeleccionado, volumen, pistaMusicalSeleccionada, teclaDisparo, teclaPausa, teclaAtaqueEspecial);

            // Cerrar la ventana de ajustes
            this.dispose();
        } else if (e.getSource() == volver) {
            // Si el usuario decide volver sin guardar los cambios, simplemente cerramos la ventana.
            this.dispose();
        } else if (e.getSource() == reset) {
            // Resetear los valores a sus configuraciones por defecto.
            cargarValoresPorDefecto();
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        // Aquí puedes agregar acciones adicionales cuando el valor del slider cambie (por ejemplo, actualizar una etiqueta con el valor del volumen).
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Aquí puedes capturar las teclas cuando el usuario las presiona
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    // Método para obtener el código de tecla a partir de su nombre
    private int obtenerCodigoTecla(String nombreTecla) {
        int codigoTecla = KeyEvent.VK_UNDEFINED;
        switch (nombreTecla) {
            case "X":
                codigoTecla = KeyEvent.VK_X;
                break;
            case "C":
                codigoTecla = KeyEvent.VK_C;
                break;
            case "Espacio":
                codigoTecla = KeyEvent.VK_SPACE;
                break;
            case "P":
                codigoTecla = KeyEvent.VK_P;
                break;
            case "Enter":
                codigoTecla = KeyEvent.VK_ENTER;
                break;
            case "Z":
                codigoTecla = KeyEvent.VK_Z;
                break;
            case "A":
                codigoTecla = KeyEvent.VK_A;
                break;
            case "B":
                codigoTecla = KeyEvent.VK_B;
                break;
        }
        return codigoTecla;
    }
}
