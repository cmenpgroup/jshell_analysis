//---- imports for GROOT library
import org.jlab.groot.data.*;
import org.jlab.groot.graphics.*;
import org.jlab.groot.ui.*;

TDirectory dir2 = new TDirectory();
dir2.readFile("myfile.hipo");
H1F hReadGauss = dir2.getObject("Gaussian/", "hRandGauss");
  
// Declare the canvas object with name and dimensions
TCanvas can = new TCanvas("can",600,600);

can.getPad().setTitleFontSize(32);
can.getPad().setAxisTitleFontSize(24);
can.getPad().setAxisLabelFontSize(18);
can.getPad().setStatBoxFontSize(18);

hReadGauss.setLineWidth(2);
hReadGauss.setLineColor(21);
hReadGauss.setFillColor(32);
hReadGauss.setOptStat(1110);

// Draw the histogram
can.cd(0).draw(hReadGauss);

hReadGauss.save("histContents.lis");
