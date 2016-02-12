package driver;

import adapter.BuildAuto;

/*
 * Name: Ge Jin
 * andrew id: gjin
 * date : jun 24, 2015
 */



public class Driver {
	public static void main(String[] args){
//		BuildCarModelOptions model= new BuildCarModelOptions();//create an object and start it
//		model.run();
		BuildAuto ba= new BuildAuto();
		ba.buildConnect();
		ba.createDB();
		ba.BuildAuto("readFileProp.properties");
		ba.BuildAuto("readFileProp1.properties");
//		ba.printJDBC();
//		ba.updateAutoName("BMW A6", "Audi ss");
//		ba.updateOptionSetName("BMW A6", "color", "colorcolor");
//		ba.updateOptionPrice("BMW A6", "brakes", "standard", 800);
//		ba.deleteAuto("BMW A6");
//		ba.deleteOptionSet("BMW A6", "color");
		ba.deleteOption("BMW A6", "brakes", "standard");
		ba.printJDBC();
	}
	

}
