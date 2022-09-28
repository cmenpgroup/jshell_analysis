List<String> fileList = Files.readAllLines(Paths.get("../clas6/run_42011.lis"));
for(String line : fileList){
   System.out.println("-> " + line);
}  

