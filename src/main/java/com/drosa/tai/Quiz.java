package com.drosa.tai;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Quiz {
    private final List<String> questions = new ArrayList<>();
    private final List<String[]> options = new ArrayList<>();
    private final List<Integer> correctAnswers = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private int correctCount = 0;
    private int incorrectCount = 0;
    private int invalidCount = 0;

    public void loadQuestionsFromCSV(String filePath) {
        questions.clear();
        options.clear();
        correctAnswers.clear();
        currentQuestionIndex = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;  // Skip the header line.
                    continue;
                }
                String[] parts = line.split(";");
                if (parts.length == 6) {
                    questions.add(parts[0]);
                    options.add(new String[]{parts[1], parts[2], parts[3], parts[4]});
                    correctAnswers.add(Integer.parseInt(parts[5].trim()) - 1); // Adjust for index starting from 0
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getQuestion() {
        if (currentQuestionIndex < questions.size()) {
            return questions.get(currentQuestionIndex);
        }
        return null;
    }

    public String[] getCurrentOptions() {
        if (currentQuestionIndex < options.size()) {
            return options.get(currentQuestionIndex);
        }
        return new String[]{"", "", "", ""};
    }

    public int getCorrectAnswerIndex() {
        return correctAnswers.get(currentQuestionIndex);
    }

    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    public void setCurrentQuestionIndex(int index) {
        currentQuestionIndex = index;
    }

    public void incrementCurrentQuestionIndex() {
        if (currentQuestionIndex < questions.size() - 1) {
            currentQuestionIndex++;
        }
    }

    public int getCorrectCount() {
        return correctCount;
    }

    public void incrementCorrectCount() {
        correctCount++;
    }

    public int getIncorrectCount() {
        return incorrectCount;
    }

    public void incrementIncorrectCount() {
        incorrectCount++;
    }

    public int getInvalidCount() {
        return invalidCount;
    }

    public void incrementInvalidCount() {
        invalidCount++;
    }

    public void setCorrectCount(int count) {
        correctCount = count;
    }

    public void setIncorrectCount(int count) {
        incorrectCount = count;
    }

    public void setInvalidCount(int count) {
        invalidCount = count;
    }

    public String getCorrectAnswerText() {
        if (currentQuestionIndex < options.size()) {
            return options.get(currentQuestionIndex)[correctAnswers.get(currentQuestionIndex)];
        }
        return "";
    }

    public List<String> getQuestions() {
        return questions;
    }

    public List<String[]> getOptions() {
        return options;
    }

    public List<Integer> getCorrectAnswers() {
        return correctAnswers;
    }
}
