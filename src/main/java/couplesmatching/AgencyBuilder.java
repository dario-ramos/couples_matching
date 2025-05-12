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

    /* TODO: Translate
     * Lectura del archivo con los datos de entrada.
     * El formato de la entrada es:
     * Hombre1: Mujer11,Mujer12,Mujer13,...,Mujer1N
     * ...
     * HombreN: MujerN1,MujerN2,MujerN3,...,MujerNN
     *
     * Mujer1: Hombre11,Hombre12,Hombre13,...,Hombre1N
     * ...
     * MujerN: HombreN1,HombreN2,HombreN3,...,HombreNN
     *
     * Donde N es la cantidad de hombres (y mujeres) y, MujerIJ es la mujer cali-
     * ficada con J por el hombre I (idem para HombreIJ).
     * Para realizar el parseo, tengo en cuenta el hecho de que con la primera lí-
     * nea del archivo puedo obtener los nombres de todas las mujeres (y, por lo
     * tanto, crearlas).
     * Luego, comienzo la lectura línea a línea agregando a la lista de preferen-
     * cias de cada hombre la mujer que aparezca (y en cada línea que leo debo
     * crear un hombre).
     * Cuando llega el momento de leer las preferencias de cada mujer ya tengo a
     * todos los hombres creados (los creé mientras leía sus preferencias).
     * Para tener acceso rápido al objeto "couplesmatching.Individuo" que representa a cada hom-
     * bre o a cada mujer mientras leo el archivo (esto es, identificándolos por
     * su nombre) utilizo una tabla de hash.
     */
    public Agencia build() throws InvalidInputException, IOException {
        Agencia agency = new Agencia();

        Map<String, Individuo> women = parseWomen(agency);
        Map<String, Individuo> men = parseMenWithTheirPreferences(agency, women);
        parseWomenPreferences(women, men);

        return agency;
    }

    private Map<String, Individuo> parseWomen(Agencia agency) throws InvalidInputException, IOException {
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
        Map<String, Individuo> women = new HashMap<>();
        for (String womanName : womenNames) {
            Individuo woman = new Individuo(womanName.trim(),false);
            women.put(woman.getNombre(), woman);
            agency.addMujer(woman);
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

    private Map<String, Individuo> parseMenWithTheirPreferences(
            Agencia agency, Map<String, Individuo> women
    ) throws InvalidInputException, IOException {

        Map<String, Individuo> men = new HashMap<>();
        String line = input.readLine();
        while (!line.isEmpty()){
            String[] sections = line.split(":");
            if (sections.length != 2) {
                throw new InvalidInputException(invalidMaleLineMsg, line);
            }

            String manName = sections[0].trim();
            Individuo man = new Individuo (manName,true);
            String[] womenNames = sections[1].split(",");

            int calif = 0;
            for (String womanName : womenNames ) {
                man.setPuesto(women.get(womanName), calif);
                calif++;
            }

            men.put(man.getNombre(), man);
            agency.addHombre(man);
            line = input.readLine();
        }

        return men;
    }

    private void parseWomenPreferences(
            Map<String, Individuo> women, Map<String, Individuo> men
    ) throws InvalidInputException, IOException {
        String line = input.readLine();
        while (line != null){
            String[] sections = line.split(":");
            if (sections.length != 2) {
                throw new InvalidInputException(invalidFemaleLineMsg, line);
            }

            String womanName = sections[0].trim();
            Individuo woman = women.get(womanName);
            String[] menNames = sections[1].split(",");

            int calif = 0;
            for (String manName : menNames ) {
                woman.setPuesto(men.get(manName), calif);
                calif++;
            }

            line = input.readLine();
        }
    }
}
