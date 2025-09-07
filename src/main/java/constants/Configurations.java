package constants;

import enums.SpotGenerateType;

import java.io.File;
import java.security.SecureRandom;
import java.util.Random;

public interface Configurations {

    Random RANDOM = new SecureRandom();

    SpotGenerateType SPOT_GENERATE_TYPE = SpotGenerateType.RANDOM;

    int SPOT_HISTORY_LIST_SIZE = 100;

    int BALANCE_HISTORY_SIZE = 100;

    int DEFAULT_INITIAL_BALANCE = 10000;

    int DEFAULT_MINIMUM_BET = 100;

    int DEFAULT_MAXIMUM_BET = 50000;

    File SETTING_FILE_DIRECTORY = new File("data/settings");

    String ENABLE_STRATEGY_TXT = "enableStrategy.txt";
}