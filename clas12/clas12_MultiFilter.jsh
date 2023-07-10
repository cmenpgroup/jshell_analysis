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
reader.open("/home/wood5/SimOuts.hipo"); 
Event     event = new Event(); 
Bank  particles = new Bank(reader.getSchemaFactory().getSchema("REC::Particle"));

// Loop through events from Reconstructed bank and fill histograms.
EventFilter  exclProton = new EventFilter("11:2212"); // filter for exclusive proton
boolean exclFirstEvt = true; // use to print out the first event from this filter

EventFilter  inclProton_v1 = new EventFilter("11:2212"); // filter for inclusive proton
inclProton_v1.setInclusive('+');
boolean inclFirstEvt_v1 = true; // use to print out the first event from this filter

EventFilter  inclProton_v2 = new EventFilter("11:2212:X+"); // filter for inclusive proton
boolean inclFirstEvt_v2 = true; // use to print out the first evernt from this filter

reader.getEvent(event,0); // Reads the first event and resets to the begining of the file

while(reader.hasNext()==true){
     reader.nextEvent(event);
     event.read(particles);

     PhysicsEvent physEvent = DataManager.getPhysicsEvent(10.6,particles);

     // analyze the exclusive event
     if(exclProton.isValid(physEvent)==true){
       if(exclFirstEvt==true){
         System.out.println("%%% Exclusive event %%%");
         LorentzVector electronV4 = physEvent.getParticle("[11]").vector(); // create a LorentzVector for the electron
         electronV4.print(); // print the LorentzVector
         particles.show();  // print the bank contents
         exclFirstEvt = false;
       }
     }

     // analyze the inclusive event by the first method
     if(inclProton_v1.isValid(physEvent)==true){
       if(inclFirstEvt_v1==true){
         System.out.println("*** Inclusice event ***");
         particles.show();  // print the bank contents
         inclFirstEvt_v1 = false;
       }
     }

     // analyze the inclusive event by the second method
     if(inclProton_v2.isValid(physEvent)==true){
       if(inclFirstEvt_v2==true){
         System.out.println("*** Inclusice event ***");
         particles.show(); // print the bank contents
         inclFirstEvt_v2 = false;
       }
     }
}

// Print out the summary of these filters
System.out.println("Exclusive proton summary " + exclProton.summary());
System.out.println("Inclusive proton (version 1) summary" + inclProton_v1.summary());
System.out.println("Inclusive proton (version 2) summary" + inclProton_v2.summary());
