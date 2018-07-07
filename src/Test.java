import common.Cache;
import common.Constants;
import common.Utils;
import dao.BaseDao;
import entity.Interaction;
import entity.Option;
import entity.Statement;
import net.sf.json.JSONObject;

import java.util.*;

/**
 * @author Zhou Guanliang
 * @since 2018/5/28
 */
public class Test {
    public static void main(String[] args) {
        init();
        display();
    }

    public static void init() {
        Utils.init("resource/test.json");
    }

    public static void manualInit() {
        Utils.initAll(manualConfig());
    }

    public static JSONObject manualConfig() {
        JSONObject jsonObject = new JSONObject();

        Statement statement = new Statement();
        statement.setText("welcome!");
        statement.setId(Utils.generateId(Statement.class));
//        statement.setPreClazz(Statement.class);
//        statement.setPreId(1);
        Map<String, Statement> statementMap = new HashMap<>();
        statementMap.put(Integer.toString(statement.getId()), statement);
        jsonObject.put(Constants.KEY_STATEMENTS, statementMap);

        Interaction interaction = new Interaction();
        interaction.setId(Utils.generateId(Interaction.class));
//        interaction.setPreId(1);
//        interaction.setPreClazz(Interaction.class);
        interaction.setPreface("aaa");
        Integer[] oIds = {1, 2};
        interaction.setOptionIds(Arrays.asList(oIds));
        Map<String, Interaction> interactionMap = new HashMap<>();
        interactionMap.put(Integer.toString(interaction.getId()), interaction);
        jsonObject.put(Constants.KEY_INTERACTIONS, interactionMap);

        Option option = new Option();
        option.setText("A");
        option.setId(Utils.generateId(Option.class));
        option.setCode(1);
        option.setInteractionId(1);
        option.setNextId(3);
        option.setNextClazz(Option.class);
        Map<String, Option> optionMap = new HashMap<>();
        optionMap.put(Integer.toString(option.getId()), option);
        jsonObject.put(Constants.KEY_OPTIONS, optionMap);

        return jsonObject;
    }

    public static void display() {
        Map<String, Object> map = Cache.getData();
        SortedMap<String, Object> sortedMap = new TreeMap<>();
        map.forEach(sortedMap::put);

        String s = Utils.toPrettyJson(sortedMap);
        System.out.println(s);
    }
}
