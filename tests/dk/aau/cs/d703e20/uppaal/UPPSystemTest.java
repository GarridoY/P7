package dk.aau.cs.d703e20.uppaal;

import com.uppaal.model.core2.PrototypeDocument;
import dk.aau.cs.d703e20.ast.Enums;
import dk.aau.cs.d703e20.ast.statements.VariableDeclarationNode;
import dk.aau.cs.d703e20.uppaal.structures.UPPSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class UPPSystemTest {
    UPPSystem system;

    @BeforeEach
    void setUp() {
        system = new UPPSystem(new PrototypeDocument());
    }

    @Test
    void testCreateTemplate() {
        // Create template with name and initial location
        system.createTemplate("test01", 1);

        // Assert that there is a template, with an initial location
        assertNotNull(system.getTemplateList().get(0).getLocationList().get(0).getProperty("init"));
    }

    @Test
    void testSetDeclaration() {
        // Create templates
        system.createTemplate("test01", 1);
        system.createTemplate("test02", 2);
        // Set system declaration
        system.setSysDeclaration();
        // Assert that property was set
        assertEquals("P1 = test01(1); P2 = test02(2); \nsystem P1, P2;", system.getProperty("system").getValue().toString());
    }

    @Test
    void testAddChan() {
        // Add channels
        system.addChan("startTemp");
        // Set global declarations (Channels)
        system.setGlobalDecl();

        assertEquals("// Global declarations\n" +
                     "chan startTemp;\n" +
                     "int lock = 0;\n" +
                     "int prevLock = 0;\n" +
                     "bool atNotRunning = true;\n",
                system.getProperty("declaration").getValue().toString());
    }

    @Test
    void testAddDigitalPin() {
        system.addDigitalPin("in", 3);
        // Set global declarations (Channels)
        system.setGlobalDecl();

        assertTrue(system.getProperty("declaration").getValue().toString().contains("chan in[3][2];\n"));
    }

    @Test
    void testAddDecl() {
        // Add clock to system
        VariableDeclarationNode variableDeclarationNode = new VariableDeclarationNode(Enums.DataType.CLOCK, "x");
        system.addClockDecl(variableDeclarationNode);

        // Set global declarations (Clocks)
        system.setGlobalDecl();

        assertTrue(system.getProperty("declaration").getValue().toString().contains("clock x;\n"));
    }
}
