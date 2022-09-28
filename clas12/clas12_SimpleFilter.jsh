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
//import org.jlab.jnp.reader.*;
import org.jlab.physics.io.DataManager;
 
// Create the reader and load in the file
HipoReader reader = new HipoReader(); // Create a reader obejct
reader.open("../../simu_0/dst.hipo"); // open a file
 
Event     event = new Event();
Bank  particles = new Bank(reader.getSchemaFactory().getSchema("REC::Particle"));
 
// Define a filter that will require an electron, a proton and two pions detected in the final state. Xn - stands for any number of neutral particles (includes 0)
EventFilter  eventFilter = new EventFilter("11:2212:211:-211:Xn");
 
// Loop over the events, check the event topology, and increment the counters
reader.getEvent(event,0); // Reads the first event and resets to the begining of the file
int filterCounter = 0;
int eventCounter  = 0;
  
while(reader.hasNext()==true){
      
     reader.nextEvent(event); // read the event object
     event.read(particles);   // read particles bank from the event
      
     // Data manages creates a physics event with beam energy 10.6 GeV
     // and from particles bank for reconstructed particles info
     PhysicsEvent physEvent = DataManager.getPhysicsEvent(10.6,particles);
      
     // check if event passes the filter (11 - electron, 2212 - proton, 211-pion+,
     // -211- pion-, and Xn - any number of neutral particles, including 0)
     if(eventFilter.isValid(physEvent)==true){
         filterCounter++;
     }
     eventCounter++;
}
System.out.println("analyzed " + eventCounter + " events. # passed filter = " + filterCounter);

