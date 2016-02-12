package adapter;

import jdbc.JdbcBuild;
import model.Automobile;
import model.Fleet;
import scale.EditOptions;
import util.FileIO;

public abstract class ProxyAutomobile {
	private Automobile a1;
	private static Fleet autoSet = new Fleet();
	private EditOptions eo;
	private JdbcBuild jd= new JdbcBuild();

	private void createThread(int i, String[] para, Automobile auto) {
		this.eo = new EditOptions(i, para, auto);
		eo.start();

	}

//	private void createThread(int i, String[] para, Automobile auto, float price) {
//		this.eo = new EditOptions(i, para, auto, price);
//		eo.start();
//	}

	/*
	 * JDBC initialization
	 */
	public void buildConnect(){
		jd.buildConnect();//start the connect with the MySQL
	}
	
	public void createDB (){
		jd.createDB();//create a JDBC with the MySQL that we can use it in Ecllipse
	}
	
	public void BuildAuto(String fileName) {// build an Automobile object through a filename
											// and add it to LinkedHashMap and MySQL
		
		FileIO fio = new FileIO();
		a1 = fio.readProperties(fileName);
		autoSet.addModel(a1.getModel(), a1);
		jd.addAuto(a1);
	}
	public void BuildAuto(Automobile auto){//add it to the linkedhashmap if parameter is an Automobile object
		autoSet.addModel(auto.getModel(),auto);
	}
	/*
	 * print all the values in the database
	 */
	public void printJDBC(){
		jd.print();
	}
	/*
	 * update functions
	 */
	public void updateAutoName(String oldName, String newName){
		jd.updateAutoName(oldName, newName);
	}
	public void updateOptionSetName(String modelName, String optionSetName,
			String newName) {
		jd.updateOptionSetName(modelName, optionSetName, newName);
		
	}
	public void updateOptionPrice(String modelName, String optionSetName,
			String optionName, float newPrice) {
		jd.updateOptionPrice(modelName, optionSetName, optionName, newPrice);
	}
	/*
	 * delete functions
	 */
	public void deleteAuto(String modelName){
		jd.deleteAuto(modelName);
	}
	public void deleteOptionSet(String modelName, String optionSetName){
		jd.deleteOptionSet(modelName, optionSetName);
	}
	public void deleteOption(String modelName, String optionSetName, String option){
		jd.deleteOption(modelName, optionSetName, option);
	}

	
	
	public void printAuto(String modelName) {// print the values of this object
		Automobile auto = autoSet.readModel(modelName);
		// auto.printChange();
		auto.print();
	}
	public void printAuto() { //print all the objects in this LinkedHashMap
		Automobile[] auto= autoSet.getAll();
		for(int i=0;i<auto.length;i++){
			auto[i].print();
		}
	}
	/*
	 * return all the model names 
	 */
	public String[] getAllModelName() {
		Automobile[] auto= autoSet.getAll();
		String[] names= new String[auto.length];
		for(int i=0;i<auto.length;i++){
			names[i]= auto[i].getModel();
		}
		return names;
	}
	
	public Automobile getAuto(String modelname){
		Automobile auto= autoSet.readModel(modelname);
		return auto;
	}

	

	/*
	 * update the name of one option
	 */
	public  void updateOptionName(String modelName,String optionSetName, String optionName, String newName) {
		Automobile auto = autoSet.readModel(modelName);
		synchronized (auto) {
			String[] para = { optionSetName, optionName, newName };
			System.out.println("before update : " + newName);
			this.createThread(1, para, auto);// updateOptionSetName, id==1

			System.out.println("after update : " + newName);
			try {
				auto.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}

	/*
	 * update the price of one option
	 */
	

}
