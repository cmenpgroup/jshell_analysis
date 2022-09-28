//---- imports for GROOT library
import org.jlab.groot.data.*;
import org.jlab.groot.graphics.*;
import org.jlab.groot.ui.*;

// create a TDirectory object and read in the histogram file
TDirectory dir2 = new TDirectory();
dir2.readFile("myOutput1.hipo");
//dir2.readFile("myfile0.hipo");

// declare a histogram object and set it equal to the histogram in the file
// Note: the name of the histogram in hte file is hRandGauss.  The histogram object is called hReadGauss 
H1F hReadGauss = (H1F)dir2.getObject("Gaussian/", "hRandGauss");
  
// Declare the canvas object with name and dimensions
TCanvas can = new TCanvas("can",600,600);

// set the sizes of the fonts of the labels
can.getPad().setTitleFontSize(32);
can.getPad().setAxisTitleFontSize(24);
can.getPad().setAxisLabelFontSize(18);
can.getPad().setStatBoxFontSize(18);

// set the characteristics of the histogram
hReadGauss.setTitle("Reading a Histogram from a File");
hReadGauss.setLineWidth(2);
hReadGauss.setLineColor(21);
hReadGauss.setFillColor(32);
hReadGauss.setOptStat(1110);

// Draw the histogram
can.cd(0).draw(hReadGauss);

hReadGauss.save("histContents.txt");
