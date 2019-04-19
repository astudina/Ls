import java.io.*;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Date;

public class Ls {
    private boolean fl_l;
    private boolean fl_h;
    private boolean fl_r;
    private boolean fl_o;
    private String nameAnotherFile;

    public Ls(boolean fl_l, boolean fl_h, boolean fl_r) {
        this(fl_l, fl_h, fl_r, "");
    }

    public Ls(boolean fl_l, boolean fl_h, boolean fl_r, String nameAnotherFile) {
        this.fl_l = fl_l;
        this.fl_h = fl_h;
        this.fl_r = fl_r;
        this.fl_o = true;
        this.nameAnotherFile = nameAnotherFile;
    }

    public void output(File file) {

        String content = makeContent(file);
        try {
            OutputStream outputStream;
            if (fl_o) {
                File f = new File(nameAnotherFile);
                outputStream = new FileOutputStream(f);
            } else {
                outputStream = System.out;
            }
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
            try {
                bufferedOutputStream.write(content.getBytes());
                bufferedOutputStream.flush();
            } catch (IOException e) {
                System.out.println("output stream unavailable");
            }
        } catch (FileNotFoundException e) {
            System.out.println("file not create / io exception");
        }
    }

    private String makeContent(File file) {
        String content = "";
        if (fl_l) {
            //если вывод в длинном формате
            if (file.isDirectory()) {
                //если по этому пути лежит директория
                Arrays.sort(file.listFiles());
                for (int i = 0; i < file.listFiles().length; i++) {
                    //если стоит флаг r , то считаем индекс с конца, иначе в прямом порядке
                    File f = file.listFiles()[fl_r ? file.listFiles().length - 1 - i : i];


                    content += contentMaker(f);
                }
            } else {
                //если по этому пути лежит файл
                content += contentMaker(file);
            }
        } else {
            //если нужно вывести просто имена файлов в директории
            if (file.isDirectory()) {
                Arrays.sort(file.list());
                for (int i = 0; i < file.list().length; i++) {
                    //если стоит флаг r , то считаем индекс с конца, иначе в прямом порядке
                    String s = file.list()[fl_r ? file.list().length - 1 - i : i];
                    content += s + "\n";
                }
            } else {
                //короткий формат, путь к файлу
                content += file.getName() + "\n";
            }
        }

        return content;
    }

    private String contentMaker(File f) {
        Date d = new Date(f.lastModified());
        return f.getName() + " " + humanReadableXrw(f, fl_h) + " " + "last mod.: " + d.toString()
                + " " + "length: " + humanReadableSize(f, fl_h) + "\n";
    }

    private String humanReadableSize(File file, boolean flag) {

        double length = file.length();
        if (!flag) {
            return String.valueOf(length) + "bytes";
        } else {
            int cnt = 0;
            while (length >= 100) {
                length /= 1024;
                cnt++;
            }

            DecimalFormat f = new DecimalFormat("##.00");
            String size = f.format(length);
            switch (cnt) {
                case 0:
                    size += "bytes";
                    break;
                case 1:
                    size += "Kb";
                    break;
                case 2:
                    size += "Mb";
                    break;
                case 4:
                    size += "Gb";
                    break;
            }

            return size;
        }
    }

    public String humanReadableXrw(File file, boolean flag) {
        String opportunities = "";
        if (flag) {
            if (file.canExecute()) {
                opportunities += "x";
            }
            if (file.canRead()) {
                opportunities += "r";
            }
            if (file.canWrite()) {
                opportunities += "w";
            }
        } else {
            int xrw = 0;
            if (file.canExecute()) {
                xrw += 100;
            }
            if (file.canRead()) {
                xrw += 10;
            }
            if (file.canWrite()) {
                xrw += 1;
            }
            opportunities = String.valueOf(xrw);
        }
        return opportunities;
    }

    public void setFl_l(boolean fl_l) {
        this.fl_l = fl_l;
    }

    public void setFl_h(boolean fl_h) {
        this.fl_h = fl_h;
    }

    public void setFl_r(boolean fl_r) {
        this.fl_r = fl_r;
    }

    public void setFl_o(boolean fl_o) {
        this.fl_o = fl_o;
    }

    public void setNameAnotherFile(String nameAnotherFile) {
        this.nameAnotherFile = nameAnotherFile;
    }
}
