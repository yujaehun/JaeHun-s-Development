
public class Animal {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	
	}

}

abstract class Mammal extends Animal{
	static int numMammal=0;
	public int getNumMammals(){
		return numMammal;
	}
}

abstract class Reptile extends Animal{
	static int numReptile=0;
	public int getNumReptiles(){
		return numReptile;
	}
}

class Cat extends Mammal{
	String name;
	float weight;
	String nameSlave;
	
	public Cat(String name){
		this.name = name;
		Mammal.numMammal ++;
	}
	
	public Cat(String name, String nameSlave){
		this.name = name;
		this.nameSlave = nameSlave;
		Mammal.numMammal ++;
	}
	
	public Cat(String name, float weight){
		this.name = name;
		this.weight = weight;
		Mammal.numMammal ++;
	}
	
	public Cat(String name, float weight, String nameSlave){
		this.name = name;
		this.weight = weight;
		this.nameSlave = nameSlave;
		Mammal.numMammal ++;
	}
	void setName(String name){
		this.name = name;
	}
	String getName(){
		return this.name;
	}
	
	void setWeight(float weight){
		this.weight = weight;
	}
	float getWeight(){
		return this.weight;
	}
	
	void setNameSlave(String nameSlave){
		this.nameSlave = nameSlave;
	}
	String getNameSlave(){
		return this.nameSlave;
	}
	
	public void breed(){
		numMammal += 3;
	}
	
	public void meow(){
		System.out.println(this.name+": meow");
	}
	public void sleep(){
		System.out.println(this.name+": Zzz");
	}
}

class Dog extends Mammal{
	String name;
	float weight;
	String nameSlave;
	
	public Dog(String name){
		this.name = name;
		Mammal.numMammal ++;
	}
	
	public Dog(String name, String nameSlave){
		this.name = name;
		this.nameSlave = nameSlave;
		Mammal.numMammal ++;
	}
	
	public Dog(String name, float weight){
		this.name = name;
		this.weight = weight;
		Mammal.numMammal ++;
	}
	
	public Dog(String name, float weight, String nameSlave){
		this.name = name;
		this.weight = weight;
		this.nameSlave = nameSlave;
		Mammal.numMammal ++;
	}
	void setName(String name){
		this.name = name;
	}
	String getName(){
		return this.name;
	}
	
	void setWeight(float weight){
		this.weight = weight;
	}
	float getWeight(){
		return this.weight;
	}
	
	void setNameSlave(String nameSlave){
		this.nameSlave = nameSlave;
	}
	String getNameSlave(){
		return this.nameSlave;
	}
	
	public void breed(){
		numMammal += 5;
	}
	public void bark(){
		System.out.println(this.name+": bowwow");
	}
}

class Crocodile extends Reptile{
	String name;
	float weight;
	
	public Crocodile(String name){
		this.name = name;
		Reptile.numReptile ++;
	}
	
	public Crocodile(String name, float weight){
		this.name = name;
		this.weight = weight;
		Reptile.numReptile ++;
	}
	void setName(String name){
		this.name = name;
	}
	String getName(){
		return this.name;
	}
	
	void setWeight(float weight){
		this.weight = weight;
	}
	float getWeight(){
		return this.weight;
	}
	void spawn(){
		numReptile += 20;
	}
}