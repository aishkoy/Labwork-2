import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class FirstFile {
    static Random rand = new Random();
    static Scanner sc = new Scanner(System.in);
    static String[] encodedWord;
    public static int points;
    static String usedLetters = "";

    public static void main(String[] args) {
        tourLaunch();
        sc.close();
    }

    public static void tourLaunch(){
        String[] words = selectCategory();
        String word = selectWord(words);
        getEncodedWord(word);

        println("Загадываемое слово: " + Arrays.toString(encodedWord));
        while(checkEncodedWord()){
            String replacementLetter = chooseLetter();
            if(replaceLetterInEncodedWord(word, replacementLetter)){
                println("Вы нашли букву!");
            } else {
                println("Такой буквы нет...");
            }
            println(Arrays.toString(encodedWord));
        }

        println("Поздравляем! Вы нашли все буквы!");
        println("Игра окончена");
        println("Отгадываемое слово: " + word);
        println("Количество набранных очков: " + points);
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

    public static String chooseLetter(){
        while(true){
            print("Введите букву: ");
            String letter = sc.nextLine().strip().toLowerCase();
            if(!usedLetters.contains(letter) && !letter.isBlank() && letter.length() == 1 && letter.matches("[а-яё]+")){
                usedLetters += letter;
                return letter;
            }
            println("Неверный ввод! Попробуйте еще раз!");
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



    public static void println(String str){
        System.out.println(str);
    }

    public static void print(String str){
        System.out.print(str);
    }
}
