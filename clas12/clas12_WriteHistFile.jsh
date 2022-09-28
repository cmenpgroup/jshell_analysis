//---- imports for GROOT library
import org.jlab.groot.data.*;
import org.jlab.groot.graphics.*;
import org.jlab.groot.ui.*;

// Declare histogram object. In this case, the histogram is a guassian filled by random events
H1F hRandGauss = new H1F("hRandGauss",100,-5,5); 
hRandGauss.setTitleX("Randomly Generated Function");
hRandGauss.setTitleY("Counts");

Random randomGenerator = new Random();
for(int i=0; i<30000; i++){
   hRandGauss.fill(randomGenerator.nextGaussian());
}

// Declare the canvas object with name and dimensions
TCanvas can = new TCanvas("can",600,600);

hRandGauss.setLineWidth(2);
hRandGauss.setLineColor(21);
hRandGauss.setFillColor(34);

// Draw the histogram
can.cd(0).draw(hRandGauss);

// Declare the directory where the histogram will be saved in the file
TDirectory dir = new TDirectory();
dir.mkdir("/Gaussian");
dir.cd("Gaussian");

dir.addDataSet(hRandGauss);       // add the histogram to the directory
String histFile = "myfile2.hipo";  // declare the output file name
dir.writeFile(histFile);          // write the file
