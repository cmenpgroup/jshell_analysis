//---- imports for GROOT library
import org.jlab.groot.data.*;
import org.jlab.groot.graphics.*;
import org.jlab.groot.ui.*;

import groovy.cli.commons.CliBuilder;
import java.nio.file.Files;
import java.nio.file.Paths;

def cli = new CliBuilder(usage:'clas12_AddHistFiles.groovy [options] fileList')
cli.h(longOpt:'help', 'Print this message.')
cli.o(longOpt:'output', args:1, argName:'Histogram output file', type: String, 'Output file name')

def options = cli.parse(args);
if (!options) return;
if (options.h){ cli.usage(); return; }

def outputFile = "clas12_AddHistFiles.hipo";
if(options.o) outputFile = options.o;

def extraArguments = options.arguments()
if (extraArguments.isEmpty()){
  println "No input path!";
  cli.usage();
  return;
}

String inputFile;
extraArguments.each { input ->
  inputFile = input;
}

// read the list of files from the input file
List<String> inputList = Files.readAllLines(Paths.get(inputFile));

// create a TDirectory object  
TDirectory myDir = new TDirectory();
// add the histograms in the inputList and save to the outputFile  
myDir.addFiles(outputFile,inputList);  
