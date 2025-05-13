package couplesmatching;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AgencyBuilder {

    private final BufferedReader input;
    private final static String invalidMaleLineMsg =
            "Invalid male line. Expected format: maleName:femaleName1,femaleName2,...,femaleNameN";
    private final static String invalidFemaleLineMsg =
            "Invalid female line. Expected format: femaleName:maleName1,maleName2,...,maleNameN";

    public AgencyBuilder(BufferedReader input) {
        this.input = input;
    }

    /* Expected input data format:
     * Man1: Woman11,Woman12,Woman13,...,Woman1N
     * ...
     * ManN: WomanN1,WomanN2,WomanN3,...,WomanNN
     * (mandatory blank line that separates men and women)
     * Woman1: Man11,Man12,Man13,...,Man1N
     * ...
     * WomanN: WomanN1,WomanN2,WomanN3,...,WomanNN
     *
     * In this context:
     * - N: amount of men (also women)
     * - WomanIJ: name of the woman qualified as J-th best by the I-th man.
     * - ManIJ: name of the man qualified as J-th best by the I-th woman.
     * The returned agency will then contain all men and women, each with their full ranking
     * of all members of the opposite sex.
     */
    public Agency build() throws InvalidInputException, IOException {
        Agency agency = new Agency();

        Map<String, Person> women = parseWomen(agency);
        Map<String, Person> men = parseMenWithTheirPreferences(agency, women);
        parseWomenPreferences(women, men);

        return agency;
    }

    private Map<String, Person> parseWomen(Agency agency) throws InvalidInputException, IOException {
        if (!input.markSupported()) {
            throw new IOException("Input stream does not support marking. Try a FileReader");
        }
        final int maxLineSize = 512;
        input.mark(maxLineSize);
        String firstMaleLine = input.readLine();
        if (firstMaleLine == null) {
            throw new InvalidInputException("Empty input data", "");
        }
        if (firstMaleLine.length() > maxLineSize) {
            final String msg = String.format("First line exceeded max line size %d", maxLineSize);
            throw new InvalidInputException(msg, firstMaleLine);
        }
        input.reset();

        String[] womenNames = getWomenNames(firstMaleLine);
        Map<String, Person> women = new HashMap<>();
        for (String womanName : womenNames) {
            Person woman = new Person(womanName.trim(),false);
            women.put(woman.getName(), woman);
            agency.addWoman(woman);
        }

        return women;
    }

    private static String[] getWomenNames(String firstMaleLine) throws InvalidInputException {
        String[] firstLineSections = firstMaleLine.split(":");
        if (firstLineSections.length != 2) {
            throw new InvalidInputException(invalidMaleLineMsg, firstMaleLine);
        }

        String womenSection = firstLineSections[1];
        return womenSection.split(",");
    }

    private Map<String, Person> parseMenWithTheirPreferences(
            Agency agency, Map<String, Person> women
    ) throws InvalidInputException, IOException {

        Map<String, Person> men = new HashMap<>();
        String line = input.readLine();
        while (!line.isEmpty()){
            String[] sections = line.split(":");
            if (sections.length != 2) {
                throw new InvalidInputException(invalidMaleLineMsg, line);
            }

            String manName = sections[0].trim();
            Person man = new Person(manName,true);
            String[] womenNames = sections[1].split(",");

            int calif = 0;
            for (String womanName : womenNames ) {
                man.setRanking(women.get(womanName), calif);
                calif++;
            }

            men.put(man.getName(), man);
            agency.addMan(man);
            line = input.readLine();
        }

        return men;
    }

    private void parseWomenPreferences(
            Map<String, Person> women, Map<String, Person> men
    ) throws InvalidInputException, IOException {
        String line = input.readLine();
        while (line != null){
            String[] sections = line.split(":");
            if (sections.length != 2) {
                throw new InvalidInputException(invalidFemaleLineMsg, line);
            }

            String womanName = sections[0].trim();
            Person woman = women.get(womanName);
            String[] menNames = sections[1].split(",");

            int calif = 0;
            for (String manName : menNames ) {
                woman.setRanking(men.get(manName), calif);
                calif++;
            }

            line = input.readLine();
        }
    }
}
