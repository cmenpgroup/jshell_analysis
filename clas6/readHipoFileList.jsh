import org.jlab.jnp.hipo4.data.*;
import org.jlab.jnp.hipo4.io.*;
import org.jlab.clas.physics.*;
import org.jlab.clas.pdg.*;
import org.jlab.groot.base.GStyle;
import org.jlab.groot.data.*;
import org.jlab.groot.ui.*;
import org.jlab.groot.tree.*; // new import for ntuples

System.out.println("\n *** Running readHipoFileList.jsh *** \n");

File file = new File("run_42011.lis");
Scanner sc = new Scanner(file);

HipoChain reader = new HipoChain();

System.out.println("Reading in the files ... \n");

while(sc.hasNextLine()){
 String fileLine =  sc.nextLine(); 
 reader.addFile(fileLine);
 System.out.println(fileLine);
}
reader.open();

Event      event  = new Event();
Bank       hevt   = new Bank(reader.getSchemaFactory().getSchema("HEADER::info"));
Bank       bank   = new Bank(reader.getSchemaFactory().getSchema("EVENT::particle"));

int eventCount = 0; // event counter
int headBankCount = 0; // HEADER bank counter
int partBankCount = 0; // particle bank counter

int printCounter = 50000;

// Loop over all events
while(reader.hasNext()){
  if(eventCount % printCounter == 0) System.out.println(eventCount);

  reader.nextEvent(event);
  event.read(hevt);
  event.read(bank);

  if(hevt.getRows()>0) headBankCount++;  
  if(bank.getRows()>0) partBankCount++;  

  eventCount++; 
}

System.out.printf("Events = %d, HEAD banks = %d, particle banks = %d \n",eventCount, headBankCount, partBankCount);
