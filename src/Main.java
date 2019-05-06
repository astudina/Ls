import java.io.File;

public class Main {

    public static void main(String[] args) {

        boolean fl_l = false;
        boolean fl_h = false;
        boolean fl_r = false;
        boolean fl_o = false;
        String nameAnotherFile = "";

        //разбираем строку параметров
        for (int i = 0; i < args.length - 1; i++){
            switch (args[i]){
                case "-o" :
                    fl_o = true;
                    nameAnotherFile = args[i + 1];
                    i++;
                    break;
                case "-l":
                    fl_l = true;
                    break;
                case "-h":
                    fl_h = true;
                    break;
                case "-r":
                    fl_r = true;
                    break;
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
