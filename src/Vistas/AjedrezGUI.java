package Vistas;

import Exceptions.MovimientoInvalidoException;
import modelos.ColorPieza;
import modelos.Pieza;
import modelos.Posicion;
import modelos.Tablero;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class AjedrezGUI extends JFrame {
    private Tablero tablero; // Instancia de tu clase Tablero
    private Posicion piezaSeleccionada = null; // Para manejar la selección de piezas
    private JLabel turnoLabel; // Etiqueta para mostrar el turno actual
    private boolean turnoBlancas; // Estado del turno
    private JPanel panelTablero; // Panel para el tablero
    private final int TAMANO_CASILLA = 120; // Tamaño de cada casilla
    private JPanel panelSeleccionado; // Panel de la pieza seleccionada

    // Paneles para las piezas comidas
    private JPanel panelPiezasComidasBlancas;
    private JPanel panelPiezasComidasNegras;
    private ArrayList<Pieza> piezasComidasBlancas;
    private ArrayList<Pieza> piezasComidasNegras;

    public AjedrezGUI() {
        setTitle("Juego de Ajedrez");
        setSize(1100, 1100); // Tamaño de la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout()); // Usar BorderLayout para colocar el JLabel en la parte superior

        // Inicializar el JLabel para mostrar el turno
        turnoLabel = new JLabel("Turno: Blancas", SwingConstants.CENTER);
        turnoLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(turnoLabel, BorderLayout.NORTH); // Añadir el JLabel al norte

        tablero = new Tablero(); // Inicializa el tablero
        turnoBlancas = true; // Comienza con el turno de las blancas

        // Inicializar listas para piezas comidas
        piezasComidasBlancas = new ArrayList<>();
        piezasComidasNegras = new ArrayList<>();

        inicializarPanelesPiezasComidas();
        inicializarPanelTablero();
        actualizarTablero();

        setVisible(true);
    }

    private void inicializarPanelesPiezasComidas() {
        // Crear paneles para las piezas comidas
        panelPiezasComidasBlancas = new JPanel();
        panelPiezasComidasBlancas.setLayout(new BoxLayout(panelPiezasComidasBlancas, BoxLayout.Y_AXIS));
        panelPiezasComidasBlancas.setBorder(BorderFactory.createTitledBorder("Piezas Comidas Blancas"));
        panelPiezasComidasBlancas.setPreferredSize(new Dimension(200, 300)); // Ajusta el tamaño preferido

        panelPiezasComidasNegras = new JPanel();
        panelPiezasComidasNegras.setLayout(new BoxLayout(panelPiezasComidasNegras, BoxLayout.Y_AXIS));
        panelPiezasComidasNegras.setBorder(BorderFactory.createTitledBorder("Piezas Comidas Negras"));
        panelPiezasComidasNegras.setPreferredSize(new Dimension(200, 300)); // Ajusta el tamaño preferido

        // Añadir los paneles al lado derecho del tablero
        JPanel panelDerecho = new JPanel();
        panelDerecho.setLayout(new BorderLayout());
        panelDerecho.add(panelPiezasComidasBlancas, BorderLayout.NORTH);
        panelDerecho.add(panelPiezasComidasNegras, BorderLayout.SOUTH);

        add(panelDerecho, BorderLayout.EAST); // Añadir el panel derecho al marco
    }

    private void inicializarPanelTablero() {
        panelTablero = new JPanel();
        panelTablero.setLayout(new GridLayout(8, 8)); // Usar GridLayout para el tablero

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++ ) {
                JPanel casilla = new JPanel();
                casilla.setPreferredSize(new Dimension(TAMANO_CASILLA, TAMANO_CASILLA));
                // Establecer el color de fondo de la casilla
                if ((i + j) % 2 == 0) {
                    casilla.setBackground(Color.WHITE);
                } else {
                    casilla.setBackground(Color.GRAY);
                }

                // Agregar un MouseListener a cada casilla
                final int fila = i;
                final int columna = j;
                casilla.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        manejarMovimiento(fila, columna);
                    }
                });

                panelTablero.add(casilla);
            }
        }

        add(panelTablero, BorderLayout.CENTER); // Añadir el panel del tablero al centro
    }

    private void manejarMovimiento(int fila, int columna) {
        // Invertir el índice de fila para la selección de piezas
        int filaReal = 7 - fila; // Invertir el índice de fila
        if (piezaSeleccionada == null) {
            // Seleccionar la pieza
            Pieza pieza = tablero.getTablero()[filaReal][columna];
            if (pieza != null && pieza.getColor() == (turnoBlancas ? ColorPieza.BLANCO : ColorPieza.NEGRO)) {
                piezaSeleccionada = new Posicion(filaReal, columna);
                // Resaltar el panel de la pieza seleccionada
                panelSeleccionado = (JPanel) panelTablero.getComponent(fila * 8 + columna);
                panelSeleccionado.setBorder(new LineBorder(Color.BLACK, 5)); // Agregar borde negro
            }
        } else {
            // Intentar mover la pieza
            try {
                Posicion destino = new Posicion(filaReal, columna);
                // Verificar que la posición de destino sea válida
                if (destino.getFila() < 0 || destino.getFila() > 7 || destino.getColumna() < 0 || destino.getColumna() > 7) {
                    throw new MovimientoInvalidoException("Las filas y columnas deben estar entre 0 y 7.");
                }

                // Verificar si hay una pieza en la posición de destino
                Pieza piezaDestino = tablero.getTablero()[destino.getFila()][destino.getColumna()];
                boolean movimientoExitoso = tablero.moverPieza(piezaSeleccionada, destino);
                if (movimientoExitoso) {
                    // Si se movió exitosamente, verificar si se comió una pieza
                    if (piezaDestino != null) {
                        // Si se comió una pieza, agregarla al panel correspondiente
                        if (piezaDestino.getColor() == ColorPieza.BLANCO) {
                            piezasComidasNegras.add(piezaDestino);
                            actualizarPanelPiezasComidas(panelPiezasComidasNegras, piezaDestino);
                        } else {
                            piezasComidasBlancas.add(piezaDestino);
                            actualizarPanelPiezasComidas(panelPiezasComidasBlancas, piezaDestino);
                        }
                    }
                    actualizarTablero();
                    // Cambiar el turno
                    turnoBlancas = !turnoBlancas; // Alternar el turno
                    actualizarTurnoLabel(); // Actualizar el JLabel
                } else {
                    mostrarError("Movimiento no válido."); // Manejar el caso de movimiento no válido
                }
            } catch (MovimientoInvalidoException e) {
                mostrarError(e.getMessage()); // Mostrar el mensaje de error
                // Si el movimiento es inválido, no cambiar el turno
            }

            // Restaurar el borde del panel anterior
            if (panelSeleccionado != null) {
                panelSeleccionado.setBorder(null); // Eliminar el borde de selección
            }
            piezaSeleccionada = null; // Reiniciar la selección
        }
    }

    private void actualizarPanelPiezasComidas(JPanel panel, Pieza pieza) {
        // Crear la ruta de la imagen de la pieza
        String imagePath = "Images/" + (pieza.getColor() == ColorPieza.BLANCO ? "B-" : "N-") + pieza.getClass().getSimpleName().charAt(0) + ".gif";
        ImageIcon icon = new ImageIcon(imagePath); // Cargar la imagen

        // Crear un JLabel con la imagen
        JLabel piezaLabel = new JLabel(icon);
        piezaLabel.setHorizontalAlignment(SwingConstants.CENTER); // Centrar la imagen
        piezaLabel.setVerticalAlignment(SwingConstants.CENTER); // Centrar la imagen verticalmente

        // Añadir el JLabel al panel
        panel.add(piezaLabel);
        panel.revalidate(); // Actualiza el panel
        panel.repaint(); // Redibuja el panel
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void actualizarTurnoLabel() {
        if (turnoBlancas) {
            turnoLabel.setText("Turno: Blancas");
        } else {
            turnoLabel.setText("Turno: Negras");
        }
    }

    private void actualizarTablero() {
        panelTablero.repaint(); // Redibujar el panel del tablero
        // Actualizar las piezas en cada casilla
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                JPanel casilla = (JPanel) panelTablero.getComponent(i * 8 + j);
                casilla.removeAll(); // Limpiar la casilla
                Pieza pieza = tablero.getTablero()[7 - i][j]; // Invertir el índice de fila
                if (pieza != null) {
                    String imagePath = "Images/" + (pieza.getColor() == ColorPieza.BLANCO ? "B-" : "N-") + pieza.getClass().getSimpleName().charAt(0) + ".gif";
                    ImageIcon icon = new ImageIcon(imagePath);
                    JLabel piezaLabel = new JLabel(icon);
                    piezaLabel.setHorizontalAlignment(SwingConstants.CENTER); // Centrar la imagen
                    piezaLabel.setVerticalAlignment(SwingConstants.CENTER); // Centrar la imagen verticalmente
                    casilla.setLayout(new GridBagLayout()); // Usar GridBagLayout para centrar
                    casilla.add(piezaLabel); // Añadir la imagen de la pieza a la casilla
                }
                casilla.revalidate(); // Revalidar la casilla
                casilla.repaint(); // Redibujar la casilla
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AjedrezGUI()); // Ejecuta la GUI en el hilo de eventos
    }
}