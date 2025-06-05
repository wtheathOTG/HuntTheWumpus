package project;

import project.UI.UIManager;

public class TriviaManager {
    private int correct;

    //0: fail | 1: success | 2: ran out of gold
    public int askTrivia(int questionNum, int threshold) {
        correct = 0;
        var trivia = UIManager.instance.addTrivia(questionNum, threshold);

        var questions = new String[] {
                "Question 1: Yes or No?",
                "Question 2: Yes or No?",
                "Question 3: Yes or No?",
                "Question 4: Yes or No?",
                "Question 5: Yes or No?"
        };

        for (var i = 0; i < questionNum; i++) {
            trivia.setQuestion(questions[i]);
            UIManager.instance.renderScreen();
            getAnswer();

            if (!ScoreManager.score.tryRemoveGold()) {
                UIManager.instance.removeTrivia(trivia);
                UIManager.instance.renderScreen();
                return 2;
            }
        }

        UIManager.instance.removeTrivia(trivia);
        UIManager.instance.renderScreen();

        return (correct >= threshold ? 1 : 0);
    }

    private void getAnswer() {
        UIManager.clearTextBelowScreen();

        System.out.println("Answer: ");
        var validatedInput = validateInput(GameManager.scan.next());

        while (validatedInput == -1) {
            UIManager.clearTextBelowScreen();
            System.out.println("Invalid Answer, try again: ");
            validatedInput = validateInput(GameManager.scan.next());
        }

        if (validatedInput == 1) correct++;
    }

    private int validateInput(String input) {
        if (input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes")) {
            return 1;
        }
        if (input.equalsIgnoreCase("n") || input.equalsIgnoreCase("no")) {
            return 0;
        }
        return -1;
    }
}
