package couplesmatching;

import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SolutionPresenterTest {

    @Test
    void write() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter outputWriter = new PrintWriter( stringWriter );
        SolutionPresenter presenter = new SolutionPresenter(outputWriter);
        ArrayList couples = new ArrayList();
        couples.add( new Couple(new Person("Smith", true), new Person("Rose", false)) );
        couples.add( new Couple(new Person("Tony", true), new Person("Bethany", false)) );
        couples.add( new Couple(new Person("John", true), new Person("Anna", false)) );

        presenter.write(couples, 100);

        String expectedOutput = """
------------------------------
Couple count: 3
Elapsed time: 100 ms
-----------SOLUTION-----------
#1: Smith - Rose
#2: Tony - Bethany
#3: John - Anna
""";
        assertEquals(expectedOutput, stringWriter.toString());
    }
}
