import org.jlab.jnp.hipo4.data.*;
import org.jlab.jnp.hipo4.io.*;
import org.jlab.clas.physics.*;
import org.jlab.clas.pdg.PhysicsConstants;

import org.jlab.groot.base.GStyle
import org.jlab.groot.data.*;
import org.jlab.groot.ui.*;
import org.jlab.groot.tree.*; // new import for ntuples

int maxEvents = -1;
String outFile = "eg2Proton_AnaTree_Hists.hipo"; // name of the outpfile that the histograms are written
String fileName = "ntuple_C_Npos.hipo"; // name of the inout file with the data tree

long st = System.currentTimeMillis(); // start time

TreeHipo tree = new TreeHipo(fileName,"protonTree::tree"); // the writer adds ::tree to the name of the tree
int entries = tree.getEntries();   // get the number of events in the file
System.out.println(" ENTRIES = " + entries);  // print the number of events in the file to the screen

// to analyze the entire file, set max Events less than zero
if(maxEvents < 0){
  maxEvents = entries;
}

// Select the target in the cuts
// iTgt = 0 (deuterium)
// iTgt = 1 (solid - C, Fe, or Pb)
List vec = tree.getDataVectors("q2:W:nu:yb","pFidCut==1&&eFidCut==1&&iTgt==0",maxEvents);

// Create the histograms
H1F hQ2 = new H1F().create("hQ2",100,(DataVector)vec.get(0),0.0,5.0);
hQ2.setTitleX("Q^2 (GeV^2)");
hQ2.setTitleY("Counts");
H1F hW = new H1F().create("hW",100,(DataVector)vec.get(1),0.0,3.0);
hW.setTitleX("W (GeV)");
hW.setTitleY("Counts");
H1F hNu = new H1F().create("hNu",100,(DataVector)vec.get(2),2.0,4.5);
hNu.setTitleX("#nu (GeV)");
hNu.setTitleY("Counts");
H1F hYb = new H1F().create("hYb",100,(DataVector)vec.get(3),0.4,1.0);
hYb.setTitleX("Y_b");
hYb.setTitleY("Counts");

String dirname = "/electron";
TDirectory dir = new TDirectory();
dir.mkdir(dirname);
dir.cd(dirname);

// create the canvas for the display
int c1a_title_size = 24;
TCanvas c1 = new TCanvas("c1",800,800);
c1.divide(2,2);
c1.cd(0);
c1.draw(hQ2);
dir.addDataSet(hQ2);
c1.cd(1);
c1.draw(hW);
dir.addDataSet(hW);
c1.cd(2);
c1.draw(hNu);
dir.addDataSet(hNu);
c1.cd(3);
c1.draw(hYb);
dir.addDataSet(hYb);

dir.writeFile(outFile); // write the histograms to the file

long et = System.currentTimeMillis(); // end time
long time = et-st; // time to run the script
System.out.println(" time = " + (time/1000.0)); // print run time to the screen
