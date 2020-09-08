package commands;

import proga.Sender;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;

public class Help extends AbstractCommand {
    private String answer;


    @Override
    public void executeCommand(ExecutorService FTP, ExecutorService poolSend, DatagramSocket datagramSocket , InetSocketAddress inetSocketAddress) throws InterruptedException {
        Runnable help = () -> {
            poolSend.submit(new Sender(datagramSocket, inetSocketAddress, "help: Вывести справку по доступным командам " +
            "\ninfo: Вывести информацию о коллекции " +
                    "\nshow: Вывести все элементы коллекции в строковом представлении " +
                    "\nadd: Добавить новый элемент в коллекцию " +
                    "\nupdate id: Обновить значение элемента коллекции, id которого равен заданному " +
                    "\nremove_by_id id: Удалить элемент из коллекции по его id " +
                    "\nclear: Очистить коллекцию " +
                    "\nexecute_script file_name: Считать и исполнить скрипт из указанного файла " +
                    "\nremove_head: вывести первый элемент коллекции и удалить его " +
                    "\nadd_if_max: добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции" +
                    "\nremove_lower: удалить из коллекции все элементы, меньшие, чем заданный " +
                    "\nmin_by_distance: вывести любой объект из коллекции, значение поля distance которого является минимальным " +
                    "\nmax_by_from: вывести любой объект из коллекции, значение поля from которого является максимальным " +
                    "\nprint_field_ascending_distance distance: вывести значения поля distance в порядке возрастания " +
                    "............................................________\n" +
                    "....................................,.-‘”...................``~.,\n" +
                    ".............................,.-”...................................\"-.,\n" +
                    ".........................,/...............................................”:,\n" +
                    ".....................,?......................................................\\,\n" +
                    ".................../...........................................................,}\n" +
                    "................./......................................................,:`^`..}\n" +
                    ".............../...................................................,:”........./\n" +
                    "..............?.....__.........................................:`.........../\n" +
                    "............./__.(.....\"~-,_..............................,:`........../\n" +
                    ".........../(_....”~,_........\"~,_....................,:`........_/\n" +
                    "..........{.._$;_......”=,_.......\"-,_.......,.-~-,},.~”;/....}\n" +
                    "...........((.....*~_.......”=-._......\";,,./`..../”............../\n" +
                    "...,,,___.\\`~,......\"~.,....................`.....}............../\n" +
                    "............(....`=-,,.......`........................(......;_,,-”\n" +
                    "............/.`~,......`-...............................\\....../\\\n" +
                    ".............\\`~.*-,.....................................|,./.....\\,__\n" +
                    ",,_..........}.>-._\\...................................|..............`=~-,\n" +
                    ".....`=~-,_\\_......`\\,.................................\\\n" +
                    "...................`=~-,,.\\,...............................\\\n" +
                    "................................`:,,...........................`\\..............__\n" +
                    ".....................................`=-,...................,%`>--==``\n" +
                    "........................................_\\..........._,-%.......`\\\n" +
                    "...................................,<`.._|_,-&``................`\\\n" +
                    "\n"));
        };
        FTP.execute(help);
    }
}