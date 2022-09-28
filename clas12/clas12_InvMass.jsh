//---- imports for HIPO4 library
import org.jlab.jnp.hipo4.io.*;
import org.jlab.jnp.hipo4.data.Event;
import org.jlab.jnp.hipo4.data.Bank;
//---- imports for GROOT library
import org.jlab.groot.data.*;
import org.jlab.groot.graphics.*;
import org.jlab.groot.ui.*;
//---- imports for PHYSICS library
import org.jlab.clas.physics.*;
//import org.jnp.reader.*;
import org.jlab.physics.io.DataManager;
 
// Create the reader and load in the file
HipoReader reader = new HipoReader(); // Create a reader obejct
reader.open("../../simu_0/dst.hipo"); // open a file
 
Event     event = new Event();
Bank  particles = new Bank(reader.getSchemaFactory().getSchema("REC::Particle"));
 
// Define a filter that will require two photons and any number of positive, negative and neutral particles along with them (essentially inclusive 2 gamma)
EventFilter  eventFilter = new EventFilter("22:22:X+:X-:Xn");
 
// Loop over the events and apply the event topology.  Create the pi0 invariant mass in 2 ways - 
// (1) directly from the getParticvle() method and 
// (2) by adding the photon LorentzVectors.
 
reader.getEvent(event,0); // Reads the first event and resets to the begining of the file
int filterCounter = 0;
int eventCounter  = 0;
  
H1F    hpi0 = new H1F("hpi0"   ,100,0.05, 0.28);
H1F hpi0vec = new H1F("hpi0vec",100,0.05, 0.28);
  
LorentzVector vL_pi0 = new LorentzVector();

while(reader.hasNext()==true){
      
     reader.nextEvent(event); // read the event object
     event.read(particles);   // read particles bank from the event
      
     // Data manages creates a physics event with beam energy 10.6 GeV
     // and from particles bank for reconstructed particles info
     PhysicsEvent physEvent = DataManager.getPhysicsEvent(10.6,particles);
      
     // check if event passes the filter - at least two photons
     if(eventFilter.isValid(physEvent)==true){
         filterCounter++;
         // The particle Lorentz vector is constructed by adding
         // two photons lorentz vectors 
         // [22,0] - first photons (or 0'th)
         // [22,1] - second photons (or index 1)
         Particle pi0 = physEvent.getParticle("[22,0]+[22,1]");
         hpi0.fill(pi0.mass());
         // This is another way that invariant mass can be calculated
         // Get particles one by one, and add theor lorentz vectors
         Particle gamma0 = physEvent.getParticleByPid(22,0);
         Particle gamma1 = physEvent.getParticleByPid(22,1);
         vL_pi0.copy(gamma0.vector());
         vL_pi0.add(gamma1.vector());
         hpi0vec.fill(vL_pi0.mass());
     }
     eventCounter++;
}
System.out.println("analyzed " + eventCounter + " events. # passed filter = " + filterCounter);
 
// Create the canvas with 2 pads and draw the histograms
TCanvas ec = new TCanvas("ec",800,400); 
  
hpi0.setTitleX("M(#gamma#gamma) [GeV]");
hpi0.setTitleY("Counts");
hpi0vec.setTitleX("M(#gamma#gamma) [GeV]");
hpi0vec.setTitleY("Counts");
 
ec.divide(2,1); 
ec.cd(0).draw(hpi0);
ec.cd(1).draw(hpi0vec);
