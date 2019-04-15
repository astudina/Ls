import java.io.*;
import java.util.Arrays;
import java.util.Date;

public class Ls {
    private String content = "";
    String[] args;
    boolean fl_l = false;
    boolean fl_h = false;
    boolean fl_r = false;
    boolean fl_o = false;
    String nameAnotherFile = null;

    public Ls(String[] args) {
        this.args = args;
        String path = args[args.length - 1];

        for (int i = 0; i < args.length; i++) {
            if (args[i].equalsIgnoreCase("-l")) {
                fl_l = true;
            }
            if (args[i].equalsIgnoreCase("-h")) {
                fl_h = true;
            }
            if (args[i].equalsIgnoreCase("-r")) {
                fl_r = true;
            }
            if (args[i].equalsIgnoreCase("-o")) {
                fl_o = true;
                nameAnotherFile = args[i + 1];
            }
        }
        File file = new File(path);

        output(makeContent(file, fl_l, fl_h, fl_r), fl_o, nameAnotherFile);
    }

    private void output(String content, boolean toFile, String path) {
        try {
            OutputStream outputStream;
            if (toFile) {
                File f = new File(path);
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

    private String makeContent(File file, boolean l, boolean h, boolean r) {
        String content = "";
        if (l) {
            //если вывод в длинном формате
            if (file.listFiles() != null) {
                //если по этому пути лежит директория
                Arrays.sort(file.listFiles());
                for (int i = 0; i < file.listFiles().length; i++) {
                    //если стоит флаг r , то считаем индекс с конца, иначе в прямом порядке
                    File f = file.listFiles()[r ? file.listFiles().length - 1 - i : i];

                    Date d = new Date(f.lastModified());
                    content += f.getName() + " " + humanReadableXrw(f, h) + " " + "last mod.: " + d.toString()
                            + " " + "length: " + humanReadableSize(f, h) + "\n";
                }
            } else {
                //если по этому пути лежит файл
                Date d = new Date(file.lastModified());

                content += file.getName() + " " + humanReadableXrw(file, h) + " " + "last mod.: " + d.toString()
                        + " " + "length: " + humanReadableSize(file, h) + "\n";
            }
        } else {
            //если нужно вывести просто имена файлов в директории
            if (file.list() != null) {
                Arrays.sort(file.list());
                for (int i = 0; i < file.list().length; i++) {
                    //если стоит флаг r , то считаем индекс с конца, иначе в прямом порядке
                    String s = file.list()[r ? file.list().length - 1 - i : i];
                    content += s + "\n";
                }
            } else {
                //короткий формат, путь к файлу
                content += file.getName() + "\n";
            }
        }

        return content;
    }

    private String humanReadableSize(File file, boolean flag) {

        long length = file.length();
        if (!flag) {
            return String.valueOf(length) + "bytes";
        } else {
            String size = "";
            if (length / (1024 * 1024 * 1024) > 0) {
                size += length / 1024 * 1024 * 1024 + "Gb ";
            }
            if (length / (1024 * 1024) > 0) {
                size += length / 1024 * 1024 + "Mb ";
            }
            if (length / 1024 > 0) {
                size += length / 1024 + "Kb ";
            }
            if (size.equals("") | length % 1024 > 0) {
                size += length % 1024 + "bytes";
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
}
