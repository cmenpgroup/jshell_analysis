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
import org.jlab.physics.io.DataManager;
 
// Create the reader and load in the file
HipoReader reader = new HipoReader(); // Create a reader obejct 
reader.open("../../simu_0/dst.hipo"); // open a file
 
Event     event = new Event();
Bank  particles = new Bank(reader.getSchemaFactory().getSchema("REC::Particle"));
 
// Define a filter to select event with e-,p,pi+ and ny number of negative particles and any number of positive particles.
EventFilter  eventFilter = new EventFilter("11:2212:211:X+:X-:Xn");
 
// Loop over the events and calculate the missing mass.  Fill histograms of the mass, theta, and phi for the missing particle.
reader.getEvent(event,0); // Reads the first event and resets to the begining of the file
int filterCounter = 0;
int eventCounter  = 0;
  
// Create the 1-D histograms
H1F         hmxppi = new H1F("hmxppi" ,100,0.05, 0.8);
H1F    hmxppitheta = new H1F("hmxppitheta" ,100,0.0, 1.0);
H1F      hmxppiphi = new H1F("hmxppiphi" ,100,-3.16, 3.16);

LorentzVector vL_Mx = new LorentzVector();
  
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
         // particles togethre. The sybols here are either PID or
         // special characters, like :
         // [b] - The beam particle (in this case electron with energy 10.6 GEV)
         // [t] - target particle (by default proton with 0 momentum)
         // In the string describing the particle, one can use either PID's
         // or names of the particles. This line is also valid:
         // Particle pim = physEvent.getParticle("[b]+[t]-[11]-[2212]-[211]");
         //---------
         Particle pim = physEvent.getParticle("[b]+[t]-[e-]-[p]-[pi+]");
          
         hmxppi.fill(pim.mass());
         hmxppitheta.fill(pim.vector().theta());
         hmxppiphi.fill(pim.vector().phi());
         // This is another way that missing mass can be calculated
         // Get particles one by one, and add theor lorentz vectors
         // Although, one has to agree that the method above is more readable
         Particle gamma0 = physEvent.getParticleByPid(22,0);
         Particle gamma1 = physEvent.getParticleByPid(22,1);
         vL_Mx.copy(physEvent.beamParticle().vector());
         vL_Mx.add(physEvent.targetParticle().vector());
         vL_Mx.sub(physEvent.getParticleByPid(  11,0).vector());
         vL_Mx.sub(physEvent.getParticleByPid(2212,0).vector());
         vL_Mx.sub(physEvent.getParticleByPid( 211,0).vector());
         //----------------------------------------------------------------------------
         // If you uncomment this line (next line) you'll see that the masses
         // calculated are exactly the same.
         //System.out.printf(" %.5f = %.5f%n",pim.mass(),vL_Mx.mass()); 
  
     }
     eventCounter++; 
}
  
System.out.println("analyzed " + eventCounter + " events. # passed filter = " + filterCounter); 
 
// Create a canvas with 3 pads and plot the histograms
TCanvas ec = new TCanvas("ec",900,300);
ec.divide(3,1);
   
hmxppi.setTitleX("M^x(e^-p#pi^+) [GeV]");
hmxppi.setTitleY("Counts");
  
hmxppitheta.setTitleX("#theta^x(e^-p#pi^+) [rad]");
hmxppiphi.setTitleX("#phi^x(e^-p#pi^+) [rad]");
  
//-----------------------------------------------
// Add some color to the plots.
  
hmxppi.setFillColor(43);
hmxppiphi.setFillColor(44);
hmxppitheta.setFillColor(47);
  
ec.cd(0).draw(hmxppi);
ec.cd(1).draw(hmxppitheta);
ec.cd(2).draw(hmxppiphi);
