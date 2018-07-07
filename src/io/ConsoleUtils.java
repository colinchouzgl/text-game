package io;

import dao.BaseDao;
import entity.Entity;
import entity.Interaction;
import entity.Option;
import entity.Statement;

import java.util.List;
import java.util.Scanner;

/**
 * @author Zhou Guanliang
 * @since 2018/7/7
 */
public class ConsoleUtils {
    private static final String END_SYMBOL = "done";

    public static final String COMMAND_EXIT = "exit";
    public static final String COMMAND_CHECK = "check";
    public static final String COMMAND_GET = "get";
    public static final String COMMAND_APPEND = "append";
    public static final String COMMAND_DELETE = "del";
    public static final String COMMAND_EDIT = "edit";
    public static final String COMMAND_SEARCH = "find";
    public static final String COMMAND_USE_SOURCE = "use";
    public static final String COMMAND_SHOW_SOURCE = "src";
    public static final String COMMAND_HELP = "help";
    public static final String COMMAND_ABORT = "abort";

    private static Scanner sc = new Scanner(System.in);
    private static String input;

    public static void main(String[] args) {
        run();
    }

    public static void run() {
        if (!BaseDao.hasBeginning()) {
            createBeginning();
        }

        while (true) {
            System.out.println("Continue creating, or edit existing nodes:");
            while (true) {
                List<Entity> unfinished = BaseDao.getUnfinished();

                System.out.print(">>");
                input = sc.nextLine();
                if (input.toLowerCase().equals(COMMAND_EXIT)) {
                    System.out.println("Bye~");
                    break;
                }

                Entity entity;
                switch (input.toLowerCase()) {
                    case COMMAND_CHECK:
                        System.out.println("Unfinished nodes:");
                        unfinished.forEach(node -> System.out.println(node.getDesc()));
                        break;
                    case COMMAND_GET:
                        entity = inputNode();
                        System.out.println(entity.toString());
                        break;
                    case COMMAND_APPEND:
                        entity = inputNode();

                        if (entity instanceof Interaction) {
                            //add option
                            addOption((Interaction) entity);
                        } else {
                            //add link head
                            addLinkHead(entity.getId(), entity.getClass());
                        }
                    default:
                }
            }
        }
    }

    private static Entity inputNode() {
        Entity entity;
        System.out.println("Node:");
        while (true) {
            System.out.print(">>");
            input = sc.nextLine();
            if (input.length() <= 1) {
                System.out.println("Illegal format!(eg. s8/i15/o20)");
            } else {
                Class clazz = parseTypeCode(input.substring(0, 1));
                if (clazz == null) {
                    System.out.println("Illegal format!(eg. s8/i15/o20)");
                } else {
                    try {
                        int id = Integer.parseInt(input.substring(1, input.length()));
                        entity = BaseDao.get(id, clazz);
                        if (entity == null) {
                            System.out.println("No such node found!");
                        }
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("Illegal format!(eg. s8/i15/o20)");
                    }
                }
            }
        }
        return entity;
    }

    private static void createBeginning() {
        System.out.println("Create your first node!");
        addLinkHead(0, null);
    }

    private static void addLinkHead(int preId, Class preClazz) {
        System.out.println("Choose node type: (1-Statement, 2-Interaction)");
        while (true) {
            System.out.print(">>");
            input = sc.nextLine();
            try {
                int choice = Integer.parseInt(input);
                switch (choice) {
                    case 1:
                        Statement statement = new Statement();
                        //Set text
                        System.out.println("Set text:");
                        System.out.print(">>");
                        String text = "";
                        while (true) {
                            input = sc.nextLine();
                            if (input.equals(END_SYMBOL)) {
                                break;
                            }
                            text += input + "\n";
                        }
                        if (text.length() > 0) {
                            text = text.substring(0, text.length() - 1);
                        }
                        statement.setText(text);
//                        statement.setPreId(preId);
//                        statement.setPreClazz(preClazz);
                        BaseDao.insert(statement);
                        return;
                    case 2:
                        Interaction interaction = new Interaction();
                        //Set preface
                        System.out.println("Set preface:");
                        System.out.print(">>");
                        String preface = "";
                        while (true) {
                            input = sc.nextLine();
                            if (input.equals(END_SYMBOL)) {
                                break;
                            }
                            preface += input;
                        }
                        interaction.setPreface(preface);

                        //Set option size
                        System.out.println("Set option size:");
                        while (true) {
                            System.out.print(">>");
                            input = sc.nextLine();
                            try {
                                int optionSize = Integer.parseInt(input);
                                interaction.setOptionSize(optionSize);
                                break;
                            } catch (NumberFormatException e) {
                                System.out.println("Integer supported only!");
                            }
                        }
//                        interaction.setPreId(preId);
//                        interaction.setPreClazz(preClazz);
                        BaseDao.insert(interaction);
                        return;
                    default:
                        System.out.println("Illegal number!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Integer supported only!");
            }
        }
    }

    private static void addOption(Interaction interaction) {
        System.out.println("Add option to " + interaction.getDesc());
        Option option = new Option();

        //Set code
        System.out.println("Set code:");
        while (true) {
            System.out.print(">>");
            input = sc.nextLine();
            try {
                int code = Integer.parseInt(input);

                List<Option> options = BaseDao.getOptionsByInteraction(interaction.getId());
                boolean exist = false;
                for (Option o : options) {
                    if (o.getCode() == code) {
                        System.out.println("Code " + code + " already exists!");
                        exist = true;
                        break;
                    }
                }
                if (!exist) {
                    option.setCode(code);
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Only integer supported!");
            }
        }

        //Set text
        System.out.println("Set text:");
        System.out.print(">>");
        String text = "";
        while (true) {
            input = sc.nextLine();
            if (input.equals(END_SYMBOL)) {
                break;
            }
            text += input;
        }
        option.setText(text);
        option.setInteractionId(interaction.getId());
        option = BaseDao.insert(option);

        //Update interaction
        List<Integer> optionIds = interaction.getOptionIds();
        optionIds.add(option.getId());
        return;
    }

    private static Class<? extends Entity> parseTypeCode(String code) {
        switch (code.toLowerCase()) {
            case "s":
                return Statement.class;
            case "i":
                return Interaction.class;
            case "o":
                return Option.class;
            default:
                return null;
        }
    }
}
