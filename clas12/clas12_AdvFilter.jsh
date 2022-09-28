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
reader.open("/Users/wood5/jlab/simu_0/dst.hipo"); // open a file
 
Event event = new Event(); 
Bank  particles = new Bank(reader.getSchemaFactory().getSchema("REC::Particle"));
 
// Define a filter to select event with e-,proton,pi+ and any number of negative particles, any number of positive particles, and any number of neutral particles.
EventFilter  eventFilter = new EventFilter("11:2212:211:X+:X-:Xn");
 
// Get the first event and initialize the counters
reader.getEvent(event,0); // Reads the first event and resets to the begining of the file
int filterCounter = 0;
int matchCounter  = 0;
int eventCounter  = 0;
 
// Create 1-D histograms
H1F  histMatch = new H1F("histMatch" ,100,0.70, 1.02);
H1F  histMX    = new H1F("histMX"    ,100,0.05, 0.80);
 
// Loop over the events in the reader, check the filter condition, make some cuts, and fill the histograms.
while(reader.hasNext()==true){
      
     reader.nextEvent(event); // read the event object
     event.read(particles);   // read particles bank from the event
      
     // Data manages creates a physics event with beam energy 10.6 GeV
     // and from particles bank for reconstructed particles info
     PhysicsEvent physEvent = DataManager.getPhysicsEvent(10.6,particles);
      
     // check if event passes the filter - at least two photons
     if(eventFilter.isValid(physEvent)==true){
         filterCounter++;
         Particle  mxPim = physEvent.getParticle("[b]+[t]-[11]-[2212]-[211]");
         Particle  match = physEvent.getBestMatch(mxPim,-1);
         if(match.getStatus()>0){
             matchCounter++;
             double cosine = mxPim.cosTheta(match);
             histMatch.fill(cosine);
             if(cosine>0.997) histMX.fill(mxPim.mass());
         }
     }
     eventCounter++;
}
 
// Print out the statistics
System.out.println("analyzed " + eventCounter + " events. # passed filter = " + filterCounter);
System.out.println("matched negative particles = " + matchCounter);
 
// Create a canvas
TCanvas ec = new TCanvas("ec",700,300);
 
//Set the fill color for one histogram
histMatch.setFillColor(33);
  
// Divide the canvas into 2 pads
ec.divide(2,1);
 
// Plot one histogram in pad 0 and the other histogram on pad 1
ec.cd(0).draw(histMatch);
ec.cd(1).draw(histMX);
