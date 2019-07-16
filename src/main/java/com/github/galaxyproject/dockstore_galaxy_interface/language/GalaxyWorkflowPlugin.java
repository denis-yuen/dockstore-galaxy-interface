package com.github.galaxyproject.dockstore_galaxy_interface.language;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import io.dockstore.common.VersionTypeValidation;
import io.dockstore.language.RecommendedLanguageInterface;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

/**
 * @author jmchilton
 */
public class GalaxyWorkflowPlugin implements RecommendedLanguageInterface {

    @Override
    public String launchInstructions(String trsID) {
        return null;
    }

    @Override
    public VersionTypeValidation validateWorkflowSet(String initialPath, String contents,
        Map<String, Pair<String, GenericFileType>> indexedFiles) {
        VersionTypeValidation validation = new VersionTypeValidation(true, new HashMap<>());
        for(String line : contents.split("\\r?\\n")) {
            if (!line.startsWith("import") && !line.startsWith("author") && !line.startsWith("description")) {
                validation.setValid(false);
                validation.getMessage().put(initialPath, "unknown keyword");
            }
        }
        return validation;
    }

    @Override
    public VersionTypeValidation validateTestParameterSet(Map<String, Pair<String, GenericFileType>> indexedFiles) {
        return new VersionTypeValidation(true, new HashMap<>());
    }

    @Override
    public Pattern initialPathPattern() {
        return Pattern.compile("/.*\\.swl");
    }

    @Override
    public Map<String, Pair<String, GenericFileType>> indexWorkflowFiles(String initialPath, String contents, FileReader reader) {
        Map<String, Pair<String, GenericFileType>> results = new HashMap<>();
        for(String line : contents.split("\\r?\\n")) {
            if (line.startsWith("import")) {
                final String[] s = line.split(":");
                final String importedFile = reader.readFile(s[1].trim());
                results.put(s[1].trim(), new ImmutablePair<>(importedFile, GenericFileType.IMPORTED_DESCRIPTOR));
            }
        }
        return results;
    }

    @Override
    public RecommendedLanguageInterface.WorkflowMetadata parseWorkflowForMetadata(String initialPath, String contents,
        Map<String, Pair<String, GenericFileType>> indexedFiles) {
        RecommendedLanguageInterface.WorkflowMetadata metadata = new RecommendedLanguageInterface.WorkflowMetadata();
        for(String line : contents.split("\\r?\\n")) {
            if (line.startsWith("author")) {
                final String[] s = line.split(":");
                metadata.setAuthor(s[1].trim());
            }
            if (line.startsWith("description")) {
                final String[] s = line.split(":");
                metadata.setDescription(s[1].trim());
            }
        }
        return metadata;
    }
}

