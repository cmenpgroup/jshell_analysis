// Import Libraries 
import org.jlab.jnp.hipo4.io.*; 
import org.jlab.jnp.hipo4.data.*;
import org.jlab.groot.data.*;
import org.jlab.groot.graphics.*;
import org.jlab.groot.math.*;
import org.jlab.groot.ui.*;
import org.jlab.clas.physics.*;
import org.jlab.clas.physics.LorentzVector;
import org.jlab.clas.physics.PhysicsEvent;
import org.jlab.clas.physics.Particle;
import org.jlab.clas.physics.EventFilter;
import org.jlab.physics.io.DataManager;

//---------------------------------------------------------------------
// Create HipoReader object and load in file. Create event class and bank instance.
HipoReader reader = new HipoReader(); 
reader.open("/home/grassla/SimOuts.hipo"); 
Event     event = new Event(); 
Bank  gen = new Bank(reader.getSchemaFactory().getSchema("MC::Particle"));
Bank  particles = new Bank(reader.getSchemaFactory().getSchema("REC::Particle"));

//----------------------------------------------------------------------
PhysicsEvent physEvent = DataManager.getPhysicsEvent(10.6,particles);

// Loop through events from Reconstructed bank and fill histograms.
EventFilter  all = new EventFilter("X+:X-:Xn");

reader.getEvent(event,0); // Reads the first event and resets to the begining of the file

while(reader.hasNext()==true){
     reader.nextEvent(event);
     event.read(particles);
     if(all.isValid(physEvent)==true){
       particles.show();
       break;
     }
}
