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
// if there is one we can create lorentz vector for the particle and calculate W2 and Q2, and plot it. 
// First we will declare histogram objects and canvas object:
H1F  hW = new H1F("hW" ,100, 0.5, 4.0);
hW.setTitleX("W [GeV]");
hW.setTitleY("Counts");

H1F hQ2 = new H1F("hQ2",100, 0.1, 4.0);
hQ2.setTitleX("Q^2 [GeV/c^2]");
hQ2.setTitleY("Counts");

H2F  hWvsQ2 = new H2F("hWvsQ2" ,100, 0.5, 4.0, 100, 0.1, 4.0);
hWvsQ2.setTitleX("W [GeV]");
hWvsQ2.setTitleY("Q^2 [GeV/c^2]");
   
// Loop over the events and count how many events we have where electron is detected.
int counter = 0;
int    elec = 0;
  
while(reader.hasNext()==true){
     reader.nextEvent(event);
     event.read(particles);
     if(particles.getRows()>0){
         int pid = particles.getInt("pid",0);
         if(pid==11){
            elec++;
         }
     }
     counter++;
}
System.out.println("processed # " + counter + " , electrons : " + elec);
 
// Loop over the events and calculate Q2 and W from the LorentzVectors 
LorentzVector  vBeam   = new LorentzVector(0.0,0.0,10.6,10.6);
LorentzVector  vTarget = new LorentzVector(0.0,0.0,0.0,0.938);
LorentzVector electron = new LorentzVector(); 
LorentzVector       vW = new LorentzVector(); 
LorentzVector      vQ2 = new LorentzVector();
  
reader.getEvent(event,0); // Reads the first event and resets to the begining of the file
  
while(reader.hasNext()==true){
     reader.nextEvent(event);
     event.read(particles);
     if(particles.getRows()>0){
         int pid = particles.getInt("pid",0);
         if(pid==11){
            electron.setPxPyPzM(
                particles.getFloat("px",0), 
                particles.getFloat("py",0),
                particles.getFloat("pz",0),
                0.0005);
             
            vW.copy(vBeam);
            vW.add(vTarget);
            vW.sub(electron);
             
            vQ2.copy(vBeam);
            vQ2.sub(electron);
             
            hW.fill(vW.mass());
            hQ2.fill(-vQ2.mass2());

            hWvsQ2.fill(vW.mass(),-vQ2.mass2());
         }
     }
}
  
TCanvas ec1 = new TCanvas("ec1",800,400);

ec1.divide(2,1);
  
ec1.cd(0).draw(hW);
ec1.cd(1).draw(hQ2);

TCanvas ec2 = new TCanvas("ec2",600,600);
ec2.cd(0).draw(hWvsQ2);

