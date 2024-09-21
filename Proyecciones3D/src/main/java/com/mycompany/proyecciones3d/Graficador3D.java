/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.proyecciones3d;

/**
 *
 * @author melis
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Graficador3D extends JFrame implements ActionListener {
    private JButton btnIsometric, btnOblique, btnAxonometric;
    private JButton btnTranslateX, btnTranslateY, btnTranslateZ;
    private JButton btnRotateX, btnRotateY, btnRotateZ;
    private JButton btnScale60, btnScale50, btnScale10;
    private JPanel drawingArea;
    private ArrayList<Point> points2D = new ArrayList<>();
    private ArrayList<double[]> points3D = new ArrayList<>();
    private int marginX = 50; // Margen para evitar que los puntos salgan del recuadro
    private int marginY = 50;
    private int currentProjection = 0;

    public Graficador3D() {
        super("Graficador 3D");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Botones para seleccionar proyecciones
        btnIsometric = new JButton("Proyección Isométrica");
        btnOblique = new JButton("Proyección Oblicua");
        btnAxonometric = new JButton("Proyección Axonométrica");

        // Añadir ActionListener
        btnIsometric.addActionListener(this);
        btnOblique.addActionListener(this);
        btnAxonometric.addActionListener(this);

        // Panel de control con los botones de proyección
        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.add(btnIsometric);
        controlPanel.add(btnOblique);
        controlPanel.add(btnAxonometric);

        // Botones para las transformaciones
        btnTranslateX = new JButton("Trasladar X");
        btnTranslateY = new JButton("Trasladar Y");
        btnTranslateZ = new JButton("Trasladar Z");
        btnRotateX = new JButton("Rotar X");
        btnRotateY = new JButton("Rotar Y");
        btnRotateZ = new JButton("Rotar Z");
        btnScale60 = new JButton("Escalar 60%");
        btnScale50 = new JButton("Escalar 50%");
        btnScale10 = new JButton("Escalar 10%");

        // Añadir ActionListener para transformaciones
        btnTranslateX.addActionListener(this);
        btnTranslateY.addActionListener(this);
        btnTranslateZ.addActionListener(this);
        btnRotateX.addActionListener(this);
        btnRotateY.addActionListener(this);
        btnRotateZ.addActionListener(this);
        btnScale60.addActionListener(this);
        btnScale50.addActionListener(this);
        btnScale10.addActionListener(this);

        // Panel de control con botones de transformación
        JPanel transformPanel = new JPanel(new FlowLayout());
        transformPanel.add(btnTranslateX);
        transformPanel.add(btnTranslateY);
        transformPanel.add(btnTranslateZ);
        transformPanel.add(btnRotateX);
        transformPanel.add(btnRotateY);
        transformPanel.add(btnRotateZ);
        transformPanel.add(btnScale60);
        transformPanel.add(btnScale50);
        transformPanel.add(btnScale10);

        // Área de dibujo
        drawingArea = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (Point point : points2D) {
                    if (point.x >= marginX && point.x <= getWidth() - marginX && 
                        point.y >= marginY && point.y <= getHeight() - marginY) {
                        g.fillOval(point.x, point.y, 5, 5); // Dibuja cada punto en el área
                    }
                }
            }
        };
        drawingArea.setBackground(Color.WHITE);

        // Añadir paneles al frame
        add(controlPanel, BorderLayout.NORTH);
        add(drawingArea, BorderLayout.CENTER);
        add(transformPanel, BorderLayout.SOUTH); // Panel de transformaciones abajo

        // Leer puntos desde el archivo "Datos.txt" al iniciar
        leerPuntosDesdeArchivo("Datos.txt");
    }

    // Método para leer puntos 3D desde el archivo de texto
    private void leerPuntosDesdeArchivo(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.trim().split("\\s+"); // Divide la línea por espacios en blanco
                if (values.length == 3) { // Asegura que hay tres valores (X, Y, Z)
                    try {
                        double x = Double.parseDouble(values[0]);
                        double y = Double.parseDouble(values[1]);
                        double z = Double.parseDouble(values[2]);
                        points3D.add(new double[]{x, y, z}); // Añadir los puntos 3D a la lista
                    } catch (NumberFormatException e) {
                        System.err.println("Error al convertir a número: " + line);
                    }
                } else {
                    System.err.println("Línea inválida: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnIsometric) {
            currentProjection = 0;
        } else if (e.getSource() == btnOblique) {
            currentProjection = 1;
        } else if (e.getSource() == btnAxonometric) {
            currentProjection = 2;
        } else if (e.getSource() == btnTranslateX) {
            translate(10, 0, 0); // Ejemplo: Trasladar 10 en X
        } else if (e.getSource() == btnTranslateY) {
            translate(0, 10, 0); // Ejemplo: Trasladar 10 en Y
        } else if (e.getSource() == btnTranslateZ) {
            translate(0, 0, 10); // Ejemplo: Trasladar 10 en Z
        } else if (e.getSource() == btnRotateX) {
            rotateX(Math.toRadians(10)); // Rotar 10 grados en X
        } else if (e.getSource() == btnRotateY) {
            rotateY(Math.toRadians(10)); // Rotar 10 grados en Y
        } else if (e.getSource() == btnRotateZ) {
            rotateZ(Math.toRadians(10)); // Rotar 10 grados en Z
        } else if (e.getSource() == btnScale60) {
            scale(0.6); // Escalar al 60%
        } else if (e.getSource() == btnScale50) {
            scale(0.5); // Escalar al 50%
        } else if (e.getSource() == btnScale10) {
            scale(0.1); // Escalar al 10%
        }
        // Proyectar puntos según la proyección seleccionada
        switch (currentProjection) {
            case 0:
                points2D = projectIsometric(points3D);
                break;
            case 1:
                points2D = projectOblique(points3D);
                break;
            case 2:
                points2D = projectAxonometric(points3D);
                break;
        }
        // Mantener los puntos dentro del área visible tras las transformaciones
        
        constrainToScreen(); // Asegurar que los puntos no salgan de la pantalla
        repaint(); // Redibuja el área de dibujo con los nuevos puntos proyectados
    }

    // Método para proyección isométrica
    private ArrayList<Point> projectIsometric(ArrayList<double[]> points3D) {
        ArrayList<Point> projectedPoints = new ArrayList<>();
        for (double[] point : points3D) {
            int x = (int) ((point[0] - point[1]) * 50 + 400); // Escalar y centrar en X
            int y = (int) ((point[0] + point[1] - 2 * point[2]) / 2 * 50 + 300); // Escalar y centrar en Y
            projectedPoints.add(new Point(x, y));
        }
        return projectedPoints;
    }

    // Método para proyección oblicua (Caballera)
    private ArrayList<Point> projectOblique(ArrayList<double[]> points3D) {
        ArrayList<Point> projectedPoints = new ArrayList<>();
        for (double[] point : points3D) {
            int x = (int) ((point[0] + 0.5 * point[2]) * 50 + 400); // Proyección en X
            int y = (int) ((point[1] + 0.5 * point[2]) * 50 + 300); // Proyección en Y
            projectedPoints.add(new Point(x, y));
        }
        return projectedPoints;
    }

    // Método para proyección axonométrica
    private ArrayList<Point> projectAxonometric(ArrayList<double[]> points3D) {
        ArrayList<Point> projectedPoints = new ArrayList<>();
        double cosTheta = Math.cos(Math.PI / 4); // Ángulo Theta
        double sinTheta = Math.sin(Math.PI / 4); // Ángulo Theta
        double cosPhi = Math.cos(Math.PI / 6); // Ángulo Phi
        double sinPhi = Math.sin(Math.PI / 6); // Ángulo Phi
        for (double[] point : points3D) {
            int x = (int) ((point[0] * cosTheta + point[1] * sinTheta) * 50 + 400);
            int y = (int) ((point[0] * sinPhi - point[1] * cosPhi - point[2]) * 50 + 300);
            projectedPoints.add(new Point(x, y));
        }
        return projectedPoints;
    }

    // Método para restringir los puntos dentro del área visible
    private void constrainToScreen() {
        for (int i = 0; i < points2D.size(); i++) {
            Point point = points2D.get(i);
            int screenWidth = drawingArea.getWidth();
            int screenHeight = drawingArea.getHeight();
            // Limitar los puntos al margen de la pantalla
            point.x = Math.min(Math.max(marginX, point.x), screenWidth - marginX);
            point.y = Math.min(Math.max(marginY, point.y), screenHeight - marginY);
            points2D.set(i, point);
        }
    }

    // Métodos para transformaciones
    private void translate(double tx, double ty, double tz) {
        for (int i = 0; i < points3D.size(); i++) {
            double[] point = points3D.get(i);
            point[0] += tx;
            point[1] += ty;
            point[2] += tz;
            points3D.set(i, point);
        }
    }

    private void rotateX(double theta) {
        double cosTheta = Math.cos(theta);
        double sinTheta = Math.sin(theta);
        for (int i = 0; i < points3D.size(); i++) {
            double[] point = points3D.get(i);
            double y = point[1];
            double z = point[2];
            point[1] = y * cosTheta - z * sinTheta;
            point[2] = y * sinTheta + z * cosTheta;
            points3D.set(i, point);
        }
    }

    private void rotateY(double theta) {
        double cosTheta = Math.cos(theta);
        double sinTheta = Math.sin(theta);
        for (int i = 0; i < points3D.size(); i++) {
            double[] point = points3D.get(i);
            double x = point[0];
            double z = point[2];
            point[0] = x * cosTheta + z * sinTheta;
            point[2] = -x * sinTheta + z * cosTheta;
            points3D.set(i, point);
        }
    }

    private void rotateZ(double theta) {
        double cosTheta = Math.cos(theta);
        double sinTheta = Math.sin(theta);
        for (int i = 0; i < points3D.size(); i++) {
            double[] point = points3D.get(i);
            double x = point[0];
            double y = point[1];
            point[0] = x * cosTheta - y * sinTheta;
            point[1] = x * sinTheta + y * cosTheta;
            points3D.set(i, point);
        }
    }

    private void scale(double scaleFactor) {
        for (int i = 0; i < points3D.size(); i++) {
            double[] point = points3D.get(i);
            point[0] *= scaleFactor;
            point[1] *= scaleFactor;
            point[2] *= scaleFactor;
            points3D.set(i, point);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Graficador3D().setVisible(true);
        });
    }
}
