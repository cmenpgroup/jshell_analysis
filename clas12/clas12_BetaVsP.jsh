//---- imports for HIPO4 library
import org.jlab.jnp.hipo4.io.*; 
import org.jlab.jnp.hipo4.data.*;
//---- imports for GROOT library
import org.jlab.groot.data.*;
import org.jlab.groot.graphics.*;
import org.jlab.groot.ui.*;
//---- imports for PHYSICS library
import org.jlab.clas.physics.*;
 
// Create the reader and load in the file
HipoReader reader = new HipoReader(); // Create a reader obejct 
reader.open("../../simu_0/dst.hipo"); // open a file
 
Event     event = new Event(); 
Bank  particles = new Bank(reader.getSchemaFactory().getSchema("REC::Particle"));
 
// Loop over the events in the file and read particle bank and check for electron in the first raw, 
// First we will declare histogram objects and canvas object:
H2F  hBetaVsP = new H2F("hBetaVsP" ,100, 0.0, 10.0, 100, 0.0, 1.05);
hBetaVsP.setTitleX("P [GeV]");
hBetaVsP.setTitleY("#beta");
   
// Create the LorentzVector for the particles 
Vector3 v3Part   = new Vector3(0.0,0.0,0.0);
  
//reader.getEvent(event,0); // Reads the first event and resets to the begining of the file
  
while(reader.hasNext()==true){
     reader.nextEvent(event);
     event.read(particles);
     if(particles.getRows()>0){
         v3Part.setXYZ(
                particles.getFloat("px",0), 
                particles.getFloat("py",0),
                particles.getFloat("pz",0));
         if(particles.getFloat("beta",0)>0.0){
           hBetaVsP.fill(v3Part.mag(),particles.getFloat("beta",0));
         }
    }
}
  
TCanvas can = new TCanvas("can",600,600);
can.cd(0).draw(hBetaVsP);

