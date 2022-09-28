//---- imports for GROOT library
import org.jlab.groot.data.*;
import org.jlab.groot.graphics.*;
import org.jlab.groot.ui.*;

import org.jlab.jnp.utils.options.OptionParser;

OptionParser p = new OptionParser("clas12_AddHistFiles.groovy");
        
p.addRequired("-o", "output file name");
        
p.parse(args);
String outputFile = p.getOption("-o").stringValue();
System.out.println("Output file " + outputFile);

TDirectory.addFiles(outputFile, p.getInputList());
