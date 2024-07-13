package com.drosa.tai;

import javax.swing.*;

public class Quiz {
    private static final String[] questions = {
            "1. ¿Qué comando se usa para listar los archivos y directorios en Linux?",
            "2. ¿Qué comando se usa para cambiar el directorio en Linux?",
            "3. ¿Qué comando muestra el contenido de un archivo en la terminal?",
            "4. ¿Qué comando se usa para copiar archivos en Linux?",
            "5. ¿Qué comando se usa para mover archivos en Linux?",
            "6. ¿Qué comando se usa para eliminar archivos en Linux?"
    };

    private static final String[][] options = {
            {"ls", "cd", "mkdir", "rm"},
            {"cd", "ls", "pwd", "mv"},
            {"cat", "echo", "nano", "vim"},
            {"cp", "mv", "rm", "ls"},
            {"mv", "cp", "rm", "mkdir"},
            {"rm", "mkdir", "ls", "cp"}
    };

    private static final int[] correctAnswers = {0, 0, 0, 0, 0, 0}; // Índices de las respuestas correctas para cada pregunta

    private int currentQuestionIndex = 0;
    private int correctCount = 0;
    private int incorrectCount = 0;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Quiz::new);
    }

    public Quiz() {
        JFrame frame = new JFrame("Quiz");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 300); // Ajustar el tamaño del JFrame
        frame.setLocationRelativeTo(null); // Centrar el JFrame en la pantalla

        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);

        frame.setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel questionLabel = new JLabel(questions[currentQuestionIndex]);
        questionLabel.setBounds(10, 20, 580, 25); // Ajustar el tamaño del JLabel para que se ajuste al nuevo ancho del JFrame
        panel.add(questionLabel);

        JRadioButton[] optionButtons = new JRadioButton[4];
        ButtonGroup group = new ButtonGroup();

        for (int i = 0; i < options[currentQuestionIndex].length; i++) {
            optionButtons[i] = new JRadioButton(options[currentQuestionIndex][i]);
            optionButtons[i].setBounds(10, 50 + (i * 30), 580, 25); // Ajustar el tamaño de los JRadioButtons
            group.add(optionButtons[i]);
            panel.add(optionButtons[i]);
        }

        JButton submitButton = new JButton("Enviar");
        submitButton.setBounds(10, 180, 200, 25);
        panel.add(submitButton);

        JLabel resultLabel = new JLabel("");
        resultLabel.setBounds(10, 210, 580, 25); // Ajustar el tamaño del JLabel para que se ajuste al nuevo ancho del JFrame
        panel.add(resultLabel);

        submitButton.addActionListener(e -> {
            boolean answered = false;
            for (int i = 0; i < optionButtons.length; i++) {
                if (optionButtons[i].isSelected()) {
                    answered = true;
                    if (i == correctAnswers[currentQuestionIndex]) {
                        resultLabel.setText("¡Correcto!");
                        correctCount++;
                    } else {
                        resultLabel.setText("Incorrecto. La respuesta correcta es " + options[currentQuestionIndex][correctAnswers[currentQuestionIndex]] + ".");
                        incorrectCount++;
                    }
                    break;
                }
            }
            if (!answered) {
                resultLabel.setText("Por favor, selecciona una opción.");
            } else {
                currentQuestionIndex++;
                if (currentQuestionIndex < questions.length) {
                    updateQuestion(questionLabel, optionButtons, resultLabel);
                } else {
                    resultLabel.setText("¡Has completado el quiz!");
                    submitButton.setEnabled(false);
                    JOptionPane.showMessageDialog(panel, "Puntuación final:\nCorrectas: " + correctCount + "\nIncorrectas: " + incorrectCount);
                }
            }
        });
    }

    private void updateQuestion(JLabel questionLabel, JRadioButton[] optionButtons, JLabel resultLabel) {
        questionLabel.setText(questions[currentQuestionIndex]);
        ButtonGroup group = new ButtonGroup();
        for (int i = 0; i < options[currentQuestionIndex].length; i++) {
            optionButtons[i].setText(options[currentQuestionIndex][i]);
            optionButtons[i].setSelected(false);
            group.add(optionButtons[i]);
        }
        resultLabel.setText("");
    }
}
