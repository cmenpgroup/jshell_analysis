//---- imports for HIPO4 library
import org.jlab.jnp.hipo4.io.*;
import org.jlab.jnp.hipo4.data.*;
//---- import for command line parser
import org.jlab.jnp.utils.options.OptionParser;
//---- imports for GROOT library
import org.jlab.groot.data.*;
import org.jlab.groot.graphics.*;
import org.jlab.groot.ui.*;
//---- imports for PHYSICS library
import org.jlab.clas.physics.*;

OptionParser p = new OptionParser("clas12_CommandLineParser.groovy");
        
p.addRequired("-M", "Max. Events");
p.addOption("-D", "10000", "Increment for Event Counter");

p.parse(args);
int maxEvents = p.getOption("-M").intValue();
int dEvents = p.getOption("-D").intValue();

String inFile = "myFile.hipo";
if(p.getInputList().size()==1){
    inFile = p.getInputList().get(0);
}else{
    System.out.println("*** Wrong number of input files.  Only one input file required. ***")
    p.printUsage();
    System.exit(0);
}

// Create the reader and load in the file
HipoReader reader = new HipoReader(); // Create a reader obejct
reader.open(inFile); // open a file
 
Event     event = new Event();
Bank  particles = new Bank(reader.getSchemaFactory().getSchema("REC::Particle"));

// Declare histogram objects and canvas object:
H2F  hBetaVsP = new H2F("hBetaVsP" ,100, 0.0, 10.0, 100, 0.0, 1.05);
hBetaVsP.setTitleX("P [GeV]");
hBetaVsP.setTitleY("#beta");
   
// Create the LorentzVector for the particles
Vector3 v3Part   = new Vector3(0.0,0.0,0.0);
  
int counter = 0; // initialize the event counter

while(reader.hasNext()==true && counter<maxEvents){
    if(counter%dEvents == 0) System.out.println(counter); // print the event number
    reader.nextEvent(event);
    event.read(particles);
    if(particles.getRows()>0){ // check to see if there are particles in the bank
        for(int row=0; row < particles.getRows(); row++){ // loop over the particles
            v3Part.setXYZ(
                particles.getFloat("px",row),
                particles.getFloat("py",row),
                particles.getFloat("pz",row));
            if(particles.getFloat("beta",row)>0.0){
                hBetaVsP.fill(v3Part.mag(),particles.getFloat("beta",row));
            }
        }
    }
    counter++;
}
  
TCanvas can = new TCanvas("can",600,600);
can.cd(0).draw(hBetaVsP);
