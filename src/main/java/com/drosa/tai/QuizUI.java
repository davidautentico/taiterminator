package com.drosa.tai;

import javax.swing.*;
import java.io.File;

public class QuizUI {
    private final Quiz quiz;
    private JFrame frame;
    private JPanel panel;
    private JTextArea questionArea;
    private JRadioButton[] optionButtons;
    private ButtonGroup group;
    private JButton submitButton;
    private JLabel correctLabel;
    private JLabel incorrectLabel;
    private JLabel invalidLabel;
    private JTextArea referenceArea;
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
        frame.setSize(800, 600); // Increased width for better readability
        frame.setLocationRelativeTo(null);

        panel = new JPanel();
        panel.setLayout(null);
        frame.add(panel);

        fileLabel = new JLabel("No file selected");
        fileLabel.setBounds(10, 10, 760, 25);
        panel.add(fileLabel);

        JButton selectFileButton = new JButton("Select File");
        selectFileButton.setBounds(10, 40, 160, 25);
        panel.add(selectFileButton);
        selectFileButton.addActionListener(e -> chooseFile());

        questionArea = new JTextArea();
        questionArea.setLineWrap(true);
        questionArea.setWrapStyleWord(true);
        questionArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(questionArea);
        scrollPane.setBounds(10, 80, 760, 100);
        panel.add(scrollPane);

        group = new ButtonGroup();
        optionButtons = new JRadioButton[4];
        for (int i = 0; i < optionButtons.length; i++) {
            optionButtons[i] = new JRadioButton();
            optionButtons[i].setBounds(10, 190 + (i * 30), 760, 25);
            group.add(optionButtons[i]);
            panel.add(optionButtons[i]);
        }

        submitButton = new JButton("Submit");
        submitButton.setBounds(10, 330, 160, 25);
        panel.add(submitButton);
        submitButton.addActionListener(e -> submitAnswer());

        correctLabel = new JLabel("Correct: 0");
        correctLabel.setBounds(10, 380, 160, 25);
        panel.add(correctLabel);

        incorrectLabel = new JLabel("Incorrect: 0");
        incorrectLabel.setBounds(180, 380, 160, 25);
        panel.add(incorrectLabel);

        invalidLabel = new JLabel("Invalid: 0");
        invalidLabel.setBounds(350, 380, 160, 25);
        panel.add(invalidLabel);

        referenceArea = new JTextArea();
        referenceArea.setLineWrap(true);
        referenceArea.setWrapStyleWord(true);
        referenceArea.setEditable(false);
        JScrollPane referenceScrollPane = new JScrollPane(referenceArea);
        referenceScrollPane.setBounds(10, 420, 760, 100);
        panel.add(referenceScrollPane);

        invalidButton = new JButton("Mark Invalid");
        invalidButton.setBounds(180, 40, 160, 25);
        panel.add(invalidButton);
        invalidButton.addActionListener(e -> markInvalid());

        frame.setVisible(true);
    }

    private void chooseFile() {
        JFileChooser fileChooser = new JFileChooser();
        String projectPath = System.getProperty("user.dir");
        String resourcesPath = projectPath + File.separator + "src" + File.separator + "main" + File.separator + "resources";
        fileChooser.setCurrentDirectory(new File(resourcesPath));
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
            String question = quiz.getQuestions().get(quiz.getCurrentQuestionIndex());
            questionArea.setText((quiz.getCurrentQuestionIndex() + 1) + ". " + question);

            String[] options = quiz.getOptions().get(quiz.getCurrentQuestionIndex());
            for (int i = 0; i < options.length; i++) {
                optionButtons[i].setText(options[i]);
                optionButtons[i].setSelected(false);
            }
            group.clearSelection();
            //resultLabel.setText("");
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
            //resultLabel.setText("Correct!");
        } else {
            quiz.incrementIncorrectCount();
            incorrectLabel.setText("Incorrect: " + quiz.getIncorrectCount());
            referenceArea.append((quiz.getCurrentQuestionIndex() + 1) + ". FAILED: " + quiz.getReferences().get(quiz.getCurrentQuestionIndex()));
            referenceArea.append("\n");
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
