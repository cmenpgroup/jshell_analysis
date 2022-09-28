//---- imports for GROOT library
import org.jlab.groot.data.*;
import org.jlab.groot.graphics.*;
import org.jlab.groot.ui.*;

List<String> inputList = Files.readAllLines(Paths.get("myfile.lis"));

// create a TDirectory object  
TDirectory myDir = new TDirectory();
// add the histograms in the inputList and save to the outputFile
String outputFile = "myOutput.hipo";  
myDir.addFiles(outputFile,inputList);  
