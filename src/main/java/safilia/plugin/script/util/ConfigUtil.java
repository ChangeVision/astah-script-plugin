package safilia.plugin.script.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import com.change_vision.safilia.api.inf.SafiliaAPI;

public class ConfigUtil {
    private static final String USER_HOME = "user.home";
    private static final String DOT_ASTAH = ".astah";
    private static final String SAFILIA = "safilia";
    private static final String DOT_SAFILIA = ".safilia";
    private static final String SAT = "sat";
    private static final String DOT_SAFETY_ASSURANCE = ".safety_assurance";
    private static final String FILE_NAME = "astah_plugin_script.properties";
    private static final String DEFAULT_SCRIPT_DIR = "default_script_directory";

    private static String filePath;
    private static Properties config;

    static {
        config = new Properties();
        StringBuilder builder = new StringBuilder();
        builder.append(getConfigDirectory());
        builder.append(File.separator);
        builder.append(FILE_NAME);
        filePath = builder.toString();
        load();
    }

    private static String getConfigDirectory() {
        StringBuilder builder = new StringBuilder();
        builder.append(System.getProperty(USER_HOME));
        builder.append(File.separator);
        if (isSpecificTool(SAFILIA)) {
            builder.append(DOT_SAFILIA);
        }
        return builder.toString();
    }

    private static boolean isSpecificTool(String toolName) {
        try {
            String installPath = SafiliaAPI.getSafiliaAPI().getProjectAccessor()
                    .getSafiliaInstallPath();
            File[] files = new File(installPath).listFiles();
            for (File file : files) {
                if (file.getName().startsWith(toolName)) {
                    return true;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getDefaultScriptDirectory() {
        String path = config.getProperty(DEFAULT_SCRIPT_DIR);
        return path != null && !path.trim().isEmpty() ? path : null;
    }

    public static void saveDefaultScriptDirectory(String path) {
        config.setProperty(DEFAULT_SCRIPT_DIR, path);
        store();
    }

    private static void load() {
        InputStream stream = null;
        try {
            stream = new FileInputStream(filePath);
            config.load(stream);
        } catch (IOException e) {
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void store() {
        OutputStream stream = null;
        try {
            stream = new FileOutputStream(filePath);
            config.store(stream, null);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
