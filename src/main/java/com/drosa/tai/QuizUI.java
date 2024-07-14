package com.drosa.tai;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class QuizUI {
    private Quiz quiz;
    private JFrame frame;
    private JPanel panel;
    private JLabel questionLabel;
    private JRadioButton[] optionButtons;
    private ButtonGroup group;
    private JLabel resultLabel;
    private JButton submitButton;
    private JLabel correctLabel;
    private JLabel incorrectLabel;
    private JLabel invalidLabel;
    private JLabel fileLabel;
    private JButton invalidButton;
    private String currentFileName = "";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(QuizUI::new);
    }

    public QuizUI() {
        quiz = new Quiz();
        initializeUI();
    }

    private void initializeUI() {
        frame = new JFrame("Quiz");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLocationRelativeTo(null);

        panel = new JPanel();
        frame.add(panel);
        panel.setLayout(null);

        // Select file and load questions
        fileLabel = new JLabel("No file selected");
        fileLabel.setBounds(10, 10, 580, 25);
        panel.add(fileLabel);

        JButton selectFileButton = new JButton("Select File");
        selectFileButton.setBounds(10, 40, 160, 25);
        panel.add(selectFileButton);
        selectFileButton.addActionListener(e -> chooseFile());

        // Question display setup
        questionLabel = new JLabel("");
        questionLabel.setBounds(10, 90, 580, 25);
        panel.add(questionLabel);

        // Radio buttons for options
        group = new ButtonGroup();
        optionButtons = new JRadioButton[4];
        for (int i = 0; i < optionButtons.length; i++) {
            optionButtons[i] = new JRadioButton();
            optionButtons[i].setBounds(10, 120 + (i * 30), 580, 25);
            group.add(optionButtons[i]);
            panel.add(optionButtons[i]);
        }

        // Button to submit answer
        submitButton = new JButton("Submit");
        submitButton.setBounds(10, 270, 160, 25);
        panel.add(submitButton);
        submitButton.addActionListener(e -> submitAnswer());

        // Labels for correct, incorrect, and invalid counters
        correctLabel = new JLabel("Correct: 0");
        correctLabel.setBounds(10, 320, 160, 25);
        panel.add(correctLabel);

        incorrectLabel = new JLabel("Incorrect: 0");
        incorrectLabel.setBounds(180, 320, 160, 25);
        panel.add(incorrectLabel);

        invalidLabel = new JLabel("Invalid: 0");
        invalidLabel.setBounds(350, 320, 160, 25);
        panel.add(invalidLabel);

        // Button to mark as invalid
        invalidButton = new JButton("Mark Invalid");
        invalidButton.setBounds(180, 40, 160, 25);
        panel.add(invalidButton);
        invalidButton.addActionListener(e -> markInvalid());

        frame.setVisible(true);
    }

    private void chooseFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            quiz.loadQuestionsFromCSV(filePath);
            currentFileName = fileChooser.getSelectedFile().getName();
            fileLabel.setText("File: " + currentFileName);
            refreshQuestion();
        }
    }

    private void refreshQuestion() {
        if (quiz.getCurrentQuestionIndex() < quiz.getQuestions().size()) {
            String question = (String) quiz.getQuestions().get(quiz.getCurrentQuestionIndex());
            questionLabel.setText((quiz.getCurrentQuestionIndex() + 1) + ". " + question);
            String[] options = quiz.getOptions().get(quiz.getCurrentQuestionIndex());
            for (int i = 0; i < options.length; i++) {
                optionButtons[i].setText(options[i]);
                optionButtons[i].setSelected(false);
            }
            group.clearSelection();
            resultLabel.setText("");
        } else {
            finishQuiz();
        }
    }

    private void submitAnswer() {
        int selectedIndex = -1;
        for (int i = 0; i < optionButtons.length; i++) {
            if (optionButtons[i].isSelected()) {
                selectedIndex = i;
                break;
            }
        }

        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(frame, "Please select an option.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (selectedIndex == quiz.getCorrectAnswers().get(quiz.getCurrentQuestionIndex())) {
            quiz.incrementCorrectCount();
            correctLabel.setText("Correct: " + quiz.getCorrectCount());
            resultLabel.setText("Correct!");
        } else {
            quiz.incrementIncorrectCount();
            incorrectLabel.setText("Incorrect: " + quiz.getIncorrectCount());
            resultLabel.setText("Incorrect. Correct is: " + quiz.getCorrectAnswerText());
        }

        quiz.incrementCurrentQuestionIndex();
        refreshQuestion();
    }

    private void markInvalid() {
        quiz.incrementInvalidCount();
        invalidLabel.setText("Invalid: " + quiz.getInvalidCount());
        quiz.incrementCurrentQuestionIndex();
        refreshQuestion();
    }

    private void finishQuiz() {
        JOptionPane.showMessageDialog(frame, "Quiz completed.\nCorrect: " + quiz.getCorrectCount() +
                "\nIncorrect: " + quiz.getIncorrectCount() +
                "\nInvalid: " + quiz.getInvalidCount(), "Quiz Complete", JOptionPane.INFORMATION_MESSAGE);
        quiz.setCurrentQuestionIndex(0); // Reset for possible quiz restart
        refreshQuestion(); // Refresh UI to start position
    }
}
