package krjakbrjak.bazel.impl;

import krjakbrjak.bazel.Kind;
import krjakbrjak.bazel.LocationParser;
import org.apache.commons.lang3.StringUtils;

import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocationParserImpl implements LocationParser {
    private static final ResourceBundle resource = ResourceBundle.getBundle("Bazel");

    @Override
    public String parseLocation(String output, Kind kind) {
        String[] split = StringUtils.splitByWholeSeparator(StringUtils.defaultString(output), kind.toString());
        if (split.length != 2) {
            return StringUtils.EMPTY;
        }

        int pos = StringUtils.lastIndexOf(split[0], '/');
        if (pos == StringUtils.INDEX_NOT_FOUND) {
            return StringUtils.EMPTY;
        }

        String base = StringUtils.substring(split[0], 0, pos);

        Pattern pattern = Pattern.compile(resource.getString("bazel.target.regex"));
        Matcher m = pattern.matcher(StringUtils.strip(split[1]));

        if (!m.matches()) {
            return StringUtils.EMPTY;
        }

        if (StringUtils.indexOf(base, m.group(1)) == StringUtils.INDEX_NOT_FOUND) {
            return StringUtils.EMPTY;
        }

        String source = m.group(2);

        return StringUtils.joinWith("/", base, source);
    }
}
