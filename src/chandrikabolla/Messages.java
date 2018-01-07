package chandrikabolla;

/**
 * Messages that are passed around the actors are usually immutable classes.
 * Think how you go about creating immutable classes:) Make them all static
 * classes inside the Messages class.
 * 
 * This class should have all the immutable messages that you need to pass
 * around actors. You are free to add more classes(Messages) that you think is
 * necessary
 * 
 * @author chand
 *
 */
public class Messages {
	
	private final int numOfVowels;
	
	public Messages(int numOfVowels)
	{
		this.numOfVowels=numOfVowels;
	}
	
	public int getNumOfVowels(){
		return numOfVowels;
	}
	
   public static final class ReadyMessages{
		public static final String readyReq="Are you ready?";
		public static final String readyResp="Yes!! I'm ready";
	}
   
   public static final class CounterRequestMessages{
	   private final String filename;
	   private final int startAt;
	   private final int endAt;
	   
	   public CounterRequestMessages(String filename,int startAt,int endAt){
		   this.filename=filename;
		   this.startAt=startAt;
		   this.endAt=endAt;
	   }
	   
	   public String getFilename(){
		   return filename;
	   }
	   public int getStartAt(){
		   return startAt;
	   }
	   public int getEndAt(){
		   return endAt;
	   }
	   
	   public String toString(){
		   return this.filename+":"+this.startAt+" to "+this.endAt;
	   }
   }
   
   public static final class CounterResponseMessages{
	   private final int numberOfVowels;
	   private final String filename;
	   public CounterResponseMessages(String filename,int numberOfVowels)
	   {
		   this.numberOfVowels=numberOfVowels;
		   this.filename=filename;
	   }
	   
	   public String getFilename(){
		   return filename;
	   }
	   public int getNumberOfVowels(){
		   return numberOfVowels;
	   }
	   
	   public String toString(){
		   return this.filename+" vowels found: "+numberOfVowels;
	   }
   }
   
   public static final class ResultMessages{
	   private final int fluctuation;
	   private final String filename;
	   public ResultMessages(int fluctuation,String filename){
		   this.fluctuation=fluctuation;
		   this.filename=filename;
	   }
	   public int getFluctuation(){
		   return fluctuation;
	   }
	   public String getFilename(){
		   return filename;
	   }
	   
	   public String toString(){
		   return this.filename+" fluctuation: "+fluctuation;
	   }
   }

}
