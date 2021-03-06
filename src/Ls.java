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
    private static final String[] list = new String[]{"bytes", "Kb", "Mb", "Gb", "Tb", "Pb", "Eb", "Zb", "Yb"};

    public Ls(boolean fl_l, boolean fl_h, boolean fl_r) {
        this(fl_l, fl_h, fl_r, "");
    }

    public Ls(boolean fl_l, boolean fl_h, boolean fl_r, String nameAnotherFile) {
        this.fl_l = fl_l;
        this.fl_h = fl_h;
        this.fl_r = fl_r;

        if (!nameAnotherFile.equals("")) {
            fl_o = true;
            this.nameAnotherFile = nameAnotherFile;
        } else {
            fl_o = false;
        }
    }

    void output(File file){
        OutputStream outputStream;
        try {
            if (fl_o) {
                File f = new File(nameAnotherFile);
                outputStream = new FileOutputStream(f);
            } else {
                outputStream = System.out;
            }
            output(file, outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    void output(File file, OutputStream outputStream) {

        String content = makeContent(file);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
        try {
            bufferedOutputStream.write(content.getBytes());
            bufferedOutputStream.flush();
        } catch (IOException e) {
            System.out.println("output stream unavailable");
        }
    }

    String makeContent(File file) {
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
        return f.getName() + " " + humanReadableXrw(f) + " " + "last mod.: " + d.toString()
                + " " + "length: " + humanReadableSize(f) + "\n";
    }

    private String humanReadableSize(File file) {
        double length = file.length();
        if (!fl_h) {
            return String.valueOf(length) + "bytes";
        } else {
            int cnt = 0;
            while (length >= 100 & cnt < list.length) {
                length /= 1024;
                cnt++;
            }

            DecimalFormat f = new DecimalFormat("0.0");
            String size = f.format(length);
            size += list[cnt];

            return size;
        }
    }

    private String humanReadableXrw(File file) {
        String opportunities = "";
        if (fl_h) {
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
