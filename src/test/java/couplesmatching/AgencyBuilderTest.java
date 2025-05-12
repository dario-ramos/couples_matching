package couplesmatching;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.stream.Stream;

public class AgencyBuilderTest {

    @Test
    void build() {
        String inputData = """
John:Bethany,Anna,Rose
Smith:Bethany,Rose,Anna
Tony:Rose,Bethany,Anna

Anna:Smith,Tony,John
Bethany:Tony,John,Smith
Rose:Smith,Tony,John""";

        StringReader stringReader = new StringReader(inputData);
        BufferedReader inputReader = new BufferedReader(stringReader);
        AgencyBuilder builder = new AgencyBuilder(inputReader);

        assertDoesNotThrow( () -> {
            Agencia agency = builder.build();
            assertIndividual("John", new String[]{"Bethany", "Anna", "Rose"}, agency.getHombre(0));
            assertIndividual("Smith", new String[]{"Bethany", "Rose", "Anna"}, agency.getHombre(1));
            assertIndividual("Tony", new String[]{"Rose", "Bethany", "Anna"}, agency.getHombre(2));
            assertIndividual("Anna", new String[]{"Smith", "Tony", "John"}, agency.getMujer(1));
            assertIndividual("Bethany", new String[]{"Tony", "John", "Smith"}, agency.getMujer(0));
            assertIndividual("Rose", new String[]{"Smith", "Tony", "John"}, agency.getMujer(2));
        } );
    }

    @ParameterizedTest(name = "{1}")
    @MethodSource("getInvalidInputTestData")
    void invalidInput(String inputData, String expectedMessage) {
        StringReader stringReader = new StringReader(inputData);
        BufferedReader inputReader = new BufferedReader(stringReader);
        AgencyBuilder builder = new AgencyBuilder(inputReader);
        InvalidInputException ex = assertThrows(InvalidInputException.class, builder::build);
        assertTrue(ex.getMessage().contains(expectedMessage));
    }

    static Stream<Arguments> getInvalidInputTestData() {
        String invalidFemaleLineData = """
John:Anna,Bethany,Rose
Smith:Bethany,Rose,Anna
Tony:Rose,Bethany,Anna

Anna;Smith,Tony,John
Bethany:Tony,John,Smith
Rose:Smith,Tony,John""";
        return Stream.of(
                Arguments.of("", "Empty input data"),
                Arguments.of("John; Ana,Bethany,Rose", "Invalid male line"),
                Arguments.of(invalidFemaleLineData, "Invalid female line")
        );
    }

    private static void assertIndividual(String expectedName, String[] expectedPreferences, Individuo individual) {
        assertEquals(expectedName, individual.getNombre());
        Individuo favoriteCandidate = individual.mejorPretendido();
        assertNotNull(favoriteCandidate);
        int i = 0;
        while (favoriteCandidate != null) {
            assertEquals(expectedPreferences[i], favoriteCandidate.getNombre());
            favoriteCandidate = individual.mejorPretendido();
            i++;
        }
    }
}
