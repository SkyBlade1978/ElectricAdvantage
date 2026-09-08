package zone.moddev.mc.electricadvantage.init;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LegacyOreSpawnMigrationTest {
    @Rule
    public final TemporaryFolder temporary = new TemporaryFolder();

    @Test
    public void legacyMineralsBecomeExactlyTwoRetainedRulesAndKeepAuthority() throws Exception {
        JsonObject legacy;
        try (InputStreamReader reader = new InputStreamReader(
                getClass().getResourceAsStream("/orespawn-legacy/electricadvantage.json"),
                StandardCharsets.UTF_8)) {
            legacy = new JsonParser().parse(reader).getAsJsonObject();
        }
        Class<?> bridge = Class.forName("com.mcmoddev.orespawn.compat.LegacyOs3Bridge");
        Method translate = bridge.getDeclaredMethod("translateOs1ForTests", String.class, JsonObject.class);
        translate.setAccessible(true);
        JsonObject converted = (JsonObject) translate.invoke(null, "electricadvantage", legacy);
        assertEquals(2, converted.getAsJsonObject("spawns").entrySet().size());
        assertEquals(2, OreSpawnWorldGen.createProvider(false, false).toJson()
                .getAsJsonObject("ores").entrySet().size());

        Path config = temporary.newFolder("config").toPath();
        Path legacyDirectory = Files.createDirectories(config.resolve("orespawn"));
        Files.copy(getClass().getResourceAsStream("/orespawn-legacy/electricadvantage.json"),
                legacyDirectory.resolve("electricadvantage.json"));
        assertTrue(OreSpawnWorldGen.describeOverride(config).contains("legacy importer authority"));
    }
}