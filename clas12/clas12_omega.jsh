//Make Sure you leave out public class
import org.jlab.jnp.hipo4.io.*;
import org.jlab.jnp.hipo4.data.*;
import org.jlab.groot.data.*;
import org.jlab.groot.graphics.*;
import org.jlab.clas.physics.*;
import org.jlab.groot.ui.*;;
import org.jlab.groot.math.*;
import java.util.Random;
import javax.swing.JFrame;
import org.jlab.groot.graphics.EmbeddedCanvas;
import org.jlab.jnp.hipo4.data.Event;
import org.jlab.jnp.hipo4.data.Bank;


HipoReader reader = new HipoReader(); // Create a reader obejct
reader.open("../../simu_0/dst.hipo");
Event     event = new Event();
Bank  particles = new Bank(reader.getSchemaFactory().getSchema("REC::Particle"));
        
H1F  hW = new H1F("hW" ,100, 0.5, 4.0);        
hW.setTitleX("W [GeV]");
hW.setTitleY("Counts");

H1F hQ2 = new H1F("hQ2",100, 0.1, 4.0);
hQ2.setTitleX("Q^2 [GeV/c]^2");
hQ2.setTitleY("Counts");

H1F hPi0 = new H1F("hPi0", 100, 0, 0.75);
hPi0.setTitleX("M_#pi [GeV]");
hPi0.setTitleY("Counts");

H1F hOmega = new H1F("hOmega", 100, 0, 2.0);
hOmega.setTitleX("M_#omega [GeV]");
hOmega.setTitleY("Counts");

H1F hOmegaCut = new H1F("hOmegaCut", 100, 0, 2.0);
hOmegaCut.setTitleX("M_#omega [GeV]");
hOmegaCut.setTitleY("Counts");

H1F hTheta_eg1 = new H1F("hTheta_eg1", 100, 0, 60);
hTheta_eg1.setTitleX("#theta(e#gamma_1) [deg.]");
hTheta_eg1.setTitleY("Counts");

H2F hThetaVhPi0_eg1 = new H2F("hThetaVhPi0_eg1", 100, 0, 0.75, 100, 0, 60);
hThetaVhPi0_eg1.setTitleX("M_#pi [GeV]");
hThetaVhPi0_eg1.setTitleY("#theta(e#gamma_1) [deg.]");

H1F hTheta_eg2 = new H1F("hTheta_eg2", 100, 0, 60);
hTheta_eg2.setTitleX("#theta(e#gamma_2) [deg.]");
hTheta_eg2.setTitleY("Counts");

H2F hThetaVhPi0_eg2 = new H2F("hThetaVhPi0_eg2", 100, 0, 0.75, 100, 0, 60);
hThetaVhPi0_eg2.setTitleX("M_#pi [GeV]");
hThetaVhPi0_eg2.setTitleY("#theta(e#gamma_2) [deg.]");

TCanvas ec = new TCanvas("ec",800,800);
LorentzVector  vBeam   = new LorentzVector(0.0,0.0,10.6,10.6);
LorentzVector  vTarget = new LorentzVector(0.0,0.0,0.0,0.938);
LorentzVector electron = new LorentzVector();
LorentzVector posPion = new LorentzVector();
LorentzVector negPion = new LorentzVector();
LorentzVector gamma1 = new LorentzVector();
LorentzVector gamma2 = new LorentzVector();
LorentzVector vPi0 = new LorentzVector();
LorentzVector vOmega = new LorentzVector();
LorentzVector       vW = new LorentzVector();
LorentzVector      vQ2 = new LorentzVector();
        
reader.getEvent(event,0);

while(reader.hasNext()==true){
  reader.nextEvent(event);
  event.read(particles);
  int events=0;
  int electronCount=0;
  int posPionCount=0;
  int negPionCount=0;
  int gammaCount=0;
  if(particles.getRows()>=5){
    events++;
    for(int row=0;row<particles.getRows();row++){
      int pid=particles.getInt("pid",row);
      double px = particles.getFloat("px",row);
      double py = particles.getFloat("py",row);
      double pz = particles.getFloat("pz",row);
      if(pid==11){
        electron.setPxPyPzM(px,py,pz,0.0);
        electronCount++;
        vW.copy(vBeam);
        vW.add(vTarget);
        vW.sub(electron);

        vQ2.copy(vBeam);
        vQ2.sub(electron);

        hW.fill(vW.mass());//vPi0.fill and vPi0
        hQ2.fill(-vQ2.mass2());
      }

      if(pid==22){
        if(gammaCount==0){
          gamma1.setPxPyPzM(px,py,pz,0.0);
        }else{
          gamma2.setPxPyPzM(px,py,pz,0.0);
        }
        gammaCount++;
      }


      if(pid==211){
        posPion.setPxPyPzM(px,py,pz,0.135);
        posPionCount++;
      }

      if(pid==-211){
        negPion.setPxPyPzM(px,py,pz,0.135);
        negPionCount++;
      }

      if(electronCount>=1 && posPionCount>=1 && negPionCount>=1 && gammaCount>=2){
        vPi0.copy(gamma1);
        vPi0.add(gamma2);
        hPi0.fill(vPi0.mass());

        vOmega.copy(posPion);
        vOmega.add(negPion);
        vOmega.add(vPi0);
        hOmega.fill(vOmega.mass());
       
        double theta_eg1 = electron.vect().theta(gamma1.vect());
        hTheta_eg1.fill(theta_eg1);
        hThetaVhPi0_eg1.fill(vPi0.mass(), theta_eg1);

        double theta_eg2 = electron.vect().theta(gamma2.vect());
        hTheta_eg2.fill(theta_eg2);
        hThetaVhPi0_eg2.fill(vPi0.mass(), theta_eg2);

        if((vPi0.mass() > 0.05) && (vPi0.mass() < 0.2)){
          hOmegaCut.fill(vOmega.mass());
        }
      }
    }
  }
}

ec.divide(3,3);
ec.cd(0).draw(hW);
ec.cd(1).draw(hQ2);
ec.cd(2).draw(hPi0);
ec.cd(3).draw(hOmega);
ec.cd(4).draw(hOmegaCut);
ec.cd(5).draw(hTheta_eg1);
ec.cd(6).draw(hThetaVhPi0_eg1);
ec.cd(7).draw(hTheta_eg2);
ec.cd(8).draw(hThetaVhPi0_eg2);
