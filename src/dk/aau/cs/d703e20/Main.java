package dk.aau.cs.d703e20;

import dk.aau.cs.d703e20.ast.ASTBuilder;
import dk.aau.cs.d703e20.codegen.ArduinoGenerator;
import dk.aau.cs.d703e20.errorhandling.ReasonableErrorStrategy;
import dk.aau.cs.d703e20.ast.structure.ProgramNode;
import dk.aau.cs.d703e20.parser.OurLexer;
import dk.aau.cs.d703e20.parser.OurParser;
import dk.aau.cs.d703e20.semantics.SemanticChecker;
import dk.aau.cs.d703e20.uppaal.ModelChecker;
import dk.aau.cs.d703e20.uppaal.ModelGen;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static String uppaalDirectory;

    public enum ArgumentType {
        Flag,
        InputFile,
        UPPAALfolder,
        OutputFile
    }

    public static void main(String[] args) {
        String inputFileName = null;
        String outputFileName = null;
        boolean prettyPrint = false;
        boolean checkModel = false;
        boolean printGeneratedCode = false;

        String outputDirPath = System.getProperty("user.dir") + "/Resources/output";
        File file = new File(outputDirPath);

        if (!file.isDirectory()) {
            boolean makeDirSucceeded = file.mkdir();
            if (!makeDirSucceeded)
                throw new RuntimeException("Failed to create output folder");
        }

        if (args.length < 1) {
            throw new RuntimeException("Need at least 1 argument");
        }

        // First argument is filename
        ArgumentType nextArg = ArgumentType.InputFile;

        // ...unless it starts with a dash
        if (args[0].startsWith("-"))
            nextArg = ArgumentType.Flag;

        for (String arg : args) {
            switch (nextArg) {
                case Flag:
                    switch (arg) {
                        case "-uppaal":
                        case "-upp":
                        case "-u":
                            nextArg = ArgumentType.UPPAALfolder;
                            break;

                        case "-input":
                        case "-i":
                            nextArg = ArgumentType.InputFile;
                            break;

                        case "-output":
                        case "-o":
                            nextArg = ArgumentType.OutputFile;
                            break;

                        case "-prettyprint":
                        case "-pp":
                            prettyPrint = true;
                            break;

                        case "-check":
                        case "-verify":
                            checkModel = true;
                            break;

                        case "-print":
                        case "-p":
                            printGeneratedCode = true;
                            break;

                        case "-help":
                        case "-h":
                            printHelp();
                            return;
                    }
                    break;

                case InputFile:
                    inputFileName = arg;
                    nextArg = ArgumentType.Flag;
                    break;

                case UPPAALfolder:
                    uppaalDirectory = arg;
                    nextArg = ArgumentType.Flag;
                    break;

                case OutputFile:
                    outputFileName = arg;
                    nextArg = ArgumentType.Flag;
                    break;
            }
        }

        if (inputFileName == null) {
            throw new RuntimeException("No input file specified");
        }

        if (checkModel && uppaalDirectory == null) {
            throw new RuntimeException("Cannot check model without specified UPPAAL directory");
        }

        try {
            System.out.println("Compiling: " + inputFileName);

            // LEXER
            OurLexer lexer = new OurLexer(CharStreams.fromFileName(inputFileName));
            System.out.println("Lexer ok");

            // PARSER
            OurParser parser = new OurParser(new CommonTokenStream(lexer));
            parser.setErrorHandler(new ReasonableErrorStrategy());
            OurParser.ProgramContext parseTree = parser.program();
            System.out.println("Parser ok");

            // AST
            ASTBuilder astBuilder = new ASTBuilder();
            ProgramNode programNode = (ProgramNode) astBuilder.visitProgram(parseTree);
            System.out.println("AST ok");

            //PRETTY PRINT
            if (prettyPrint) {
                System.out.println("Pretty print:\n");
                System.out.println(programNode.prettyPrint(0));
            }

            // SEMANTICS
            SemanticChecker semanticChecker = new SemanticChecker();
            semanticChecker.visitProgram(programNode);
            System.out.println("Semantics ok");

            // VERIFY TIME IN UPPAAL
            if (checkModel) {
                ModelChecker modelChecker = new ModelChecker();
                modelChecker.checkProgram(programNode);
                System.out.println("Time check finished.");
            }

            // Generate Arduino code
            System.out.println("Generating arduino code...");
            ArduinoGenerator arduinoGenerator = new ArduinoGenerator();
            String generatedCode = arduinoGenerator.GenerateArduino(programNode);

            // Print generated code
            if (printGeneratedCode) {
                System.out.println(generatedCode);
            }

            // Save generated code to file
            try {
                if (outputFileName == null) {
                    outputFileName = "output";
                }

                String path = System.getProperty("user.dir");
                FileWriter writer = new FileWriter(new File(path + "\\Resources\\output\\" + outputFileName + ".ino"));
                writer.write(generatedCode);
                writer.close();
                System.out.println("Saved to output file.");
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            System.exit(0);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void printHelp() {
        System.out.println("Use -help for help :)");
    }
}
