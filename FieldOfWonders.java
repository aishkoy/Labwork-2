import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class FieldOfWonders {
    static Random rand = new Random();
    static Scanner sc = new Scanner(System.in);

    static String[] encodedWord;
    public static double points;

    static int toursNum = 3;
    static String[] usedWords;
    static int[] attemptsLeftInEachRound;
    static double[] scoreInEachRound;


    public static void main(String[] args) {
        do{
            gameReset();
            for (int i = 0; i < toursNum; i++) {
                tourLaunch(i);
            }
            printResult();
        } while (isGameRestarted());
        sc.close();
    }

    public static void gameReset(){
        usedWords = new String[toursNum];
        attemptsLeftInEachRound = new int[toursNum];
        scoreInEachRound = new double[toursNum];
    }

    public static void tourLaunch(int roundNumber) {
        println("Тур №" + (roundNumber + 1));

        String[] words = selectCategory();
        String word;

        do {
            word = selectWord(words);
        } while (wasWordUsedBefore(word));

        String usedLetters = "";
        usedWords[roundNumber] = word;
        getEncodedWord(word);

        int numberOfAttempts = 3;
        println("Загадываемое слово: " + Arrays.toString(encodedWord));

        while (checkEncodedWord() && numberOfAttempts > 0) {
            println("Осталось попыток: " + numberOfAttempts);

            String action = chooseAction();
            if (action.equals("word")) {
                String input = chooseWord(word);
                if (input.equalsIgnoreCase(word)) {
                    points = 0;
                    points += word.length() + (word.length() * 0.05);
                    println("Поздравляем! Вы угадали слово.");
                } else {
                    println("Неверное слово. Игра окончена.");
                    numberOfAttempts = 0;
                }
                break;
            } else {
                String replacementLetter = chooseLetter(usedLetters);
                usedLetters += replacementLetter;
                if (replaceLetterInEncodedWord(word, replacementLetter)) {
                    println("Вы нашли букву!");
                } else {
                    println("Такой буквы нет...");
                    numberOfAttempts--;
                }
            }
            println("Состояние слова: " + Arrays.toString(encodedWord));
        }

        attemptsLeftInEachRound[roundNumber] = numberOfAttempts;
        scoreInEachRound[roundNumber] = points;
        if (numberOfAttempts == 0 && checkEncodedWord()) {
            println("\nК сожалению, вы исчерпали все попытки...");
            endGame(word);
        } else {
            println("\nПоздравляем! Вы победили в этом раунде!");
            endGame(word);
        }
        points = 0;
    }


    public static String[] selectCategory(){
        String[] categories = {"Города", "Фрукты", "Животные"};
        int indexOfCategory = rand.nextInt(categories.length);

        println("Категория: " + categories[indexOfCategory]);
        switch (indexOfCategory) {
            case 0 :
                return new String[]{"Бишкек", "Москва", "Новосибирск"};
                case 1 :
                    return new String[]{"Ананас", "Яблоко", "Апельсин"};
            default:
                return new String[]{"Кошка", "Обезьяна", "Собака"};
        }
    }

    public static String selectWord(String[] words){
        int indexOfWord = rand.nextInt(words.length);
        return words[indexOfWord];
    }

    public static void getEncodedWord(String word){
        encodedWord = new String[word.length()];
        Arrays.fill(encodedWord, "*");
    }

    public static String chooseLetter(String usedLetters){
        while(true){
            print("\nВведите букву: ");
            String letter = sc.nextLine().strip().toLowerCase();
            if(!usedLetters.contains(letter) && !letter.isBlank() && letter.length() == 1 && letter.matches("[а-яё]+")){
                return letter;
            }
            if(usedLetters.contains(letter) && !letter.isBlank() && letter.length() == 1){
                println("\nВы уже раннее вводили такую букву! Введите другую.");
                continue;
            }
            if(!letter.matches("[а-яё]+") || letter.length() != 1 || letter.isBlank()){
                println("\nНеверный ввод! Попробуйте еще раз!");
            }
        }
    }

    public static boolean replaceLetterInEncodedWord(String word, String replacementLetter){
        boolean isLetterFound = false;
        for(int i = 0; i < word.length(); i++){
            if(replacementLetter.equalsIgnoreCase(String.valueOf(word.charAt(i)))){
                encodedWord[i] = String.valueOf(word.charAt(i));
                points++;
                isLetterFound =  true;
            }
        }
        return isLetterFound;
    }

    public static boolean checkEncodedWord(){
        for (String s : encodedWord) {
            if (s.equals("*")) {
                return true;
            }
        }
        return false;
    }

    public static void endGame(String word){
        println("Игра окончена!");
        println("Отгадываемое слово: " + word);
        printf("Количество набранных очков: %.1f.%n%n", points);
    }

    public static boolean wasWordUsedBefore(String word){
        for(String s : usedWords){
            if(s != null && s.equals(word)){
                return true;
            }
        }
        return false;
    }

    public static double sumOfScores(){
        double sum = 0;
        for(double score : scoreInEachRound){
            sum += score;
        }
        return sum;
    }

    public static void printResult(){
        String[] rounds = {"- 1 -", "- 2 -", "- 3 -"};
        String lineHorizontal = "-";
        String lineVertical = "|";
        println("\n" + lineHorizontal.repeat(14) + " Итоги игры! " + lineHorizontal.repeat(14));
        printf("%9s%5s%9s%5s%10s%n", "Раунд", lineVertical, "Очки", lineVertical, "Попытки");
        String decorativeLine = lineHorizontal.repeat(13) + lineVertical + lineHorizontal.repeat(13) + lineVertical + lineHorizontal.repeat(13);
        println(decorativeLine);
        for (int i = 0; i < toursNum; i++) {
            printf("%9s%5s%9.1f%5s%7d%n", rounds[i], lineVertical, scoreInEachRound[i], lineVertical, attemptsLeftInEachRound[i]);
        }
        println(decorativeLine);
        printf("%9s%5s%9.1f%5s%7d%n", "Всего", lineVertical, sumOfScores(), lineVertical, attemptsLeftInEachRound[2]);
    }

    public static boolean isGameRestarted(){
        while(true){
            print("\nХотите сыграть еще? (введите да/нет): ");
            String answer = sc.nextLine().strip().toLowerCase();
            if(answer.equals("да")){
                return true;
            } else if (answer.equals("нет")) {
                println("Спасибо за игру! До свидания.");
                return false;
            } else {
                println("Неверный ввод. Пожалуйста, введите 'да' или 'нет'.");
            }
        }
    }

    public static String chooseAction(){
        while (true) {
            println("\nЧто вы хотите ввести?");
            println("1. Ввести букву");
            println("2. Ввести слово");
            print("Введите номер (1 или 2): ");
            String choice = sc.nextLine().strip();

            if (choice.equals("1")) {
                return "letter";
            } else if (choice.equals("2")) {
                return "word";
            } else {
                println("Неверный ввод! Пожалуйста, выберите 1 или 2.");
            }
        }
    }

    public static String chooseWord(String word) {
        while (true) {
            print("\nВведите ваше слово: ");
            String input = sc.nextLine().strip().toLowerCase();

            if (input.length() == word.length() && input.matches("[а-яё]+")) {
                return input;
            } else {
                println("Неверное слово! Попробуйте еще раз.");
            }
        }
    }


    public static void println(String str){
        System.out.println(str);
    }

    public static void print(String str){
        System.out.print(str);
    }

    public static void printf(String str, Object... args){
        System.out.printf(str, args);
    }
}
