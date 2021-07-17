package com.find.app;

import com.find.check.Find;
import org.apache.commons.cli.*;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            processCmdOption(args);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // метод, который обрабатывает командную строку
    private static void processCmdOption(String[] args) throws IOException {
        Find find = new Find();


        // создание ключей -io и -d ---------------------------------------------------------------------
        Options options = new Options();
        options.addOption("i", "input", true,
                "input .json file or directory containing .json files");
        options.addOption("o", "objects", true, "input .txt file with pl/sql objects");
        // парсинг командной строки ---------------------------------------------------------------------
        CommandLineParser cmdParser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = cmdParser.parse(options, args);
        } catch (ParseException e) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("plsqlParser", options);
        }

        // обработка ключей -----------------------------------------------------------------------------
        // если в командной строке есть -i
        if (cmd.hasOption("i") && cmd.hasOption("o")) {
            // если input - json файл
            if (cmd.getOptionValue("i").contains(".json") && cmd.getOptionValue("o").contains(".txt")) {

                // запускаем проверку
                find.FindStatement(cmd.getOptionValue("i"), cmd.getOptionValue("o"));

            }

            // если input - папка (если input не содержит .json)
            if (!cmd.getOptionValue("i").contains(".json") && cmd.getOptionValue("o").contains(".txt")) {

                File inputFolder = new File(cmd.getOptionValue("i"));
                find.processFolder(inputFolder, cmd.getOptionValue("o"));

            }
        }
    }
}