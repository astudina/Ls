import java.io.File;

public class Main {

    public static void main(String[] args) {

        boolean fl_l = false;
        boolean fl_h = false;
        boolean fl_r = false;
        boolean fl_o = false;
        String nameAnotherFile = "";
        //разбираем строку параметров
        // -1 потому что не рассматриваем последний элемент; -2 потому что проверяем, есть ли имя директории после -o
        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].equalsIgnoreCase("-o") && i < args.length - 2) {
                fl_o = true;
                nameAnotherFile = args[i + 1];
                args[i + 1] = "";
            }
            if (args[i].equalsIgnoreCase("-l")) {
                fl_l = true;
            }
            if (args[i].equalsIgnoreCase("-h")) {
                fl_h = true;
            }
            if (args[i].equalsIgnoreCase("-r")) {
                fl_r = true;
            }
        }


        //создаём файлик/директорию
        File file = new File(args[args.length - 1]);
        //создаём и настраиваем объект
        Ls ls = fl_o ? new Ls(fl_l, fl_h, fl_r, nameAnotherFile) : new Ls(fl_l, fl_h, fl_r);
        //просим вывести информацию про тот файлик
        ls.output(file);
    }

}
