package chandrikabolla;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import akka.actor.Props;
import akka.actor.UntypedActor;
import chandrikabolla.Messages.CounterRequestMessages;
import chandrikabolla.Messages.ReadyMessages;
import chandrikabolla.Messages.CounterResponseMessages;

/**
 * this actor reads the file, counts the vowels and sends the result to
 * Estimator. 
 *
 * @author chand
 *
 */
public class FirstCounter extends UntypedActor {
	
	public static Props props=Props.create(FirstCounter.class);

	public FirstCounter() {
		
	}

	@Override
	public void onReceive(Object msg) throws Throwable {
		//System.out.println("@: "+getSelf().path().toString());
		if(msg instanceof String)
		{
			System.out.println(getSelf().path().name()+" received: "+msg);
			if(msg.equals(ReadyMessages.readyReq)){
/*				if(getSender().equals(getContext().parent()))
				{*/
					getSender().tell(ReadyMessages.readyResp,getSelf());
//				}
			}
		}
		else if(msg instanceof CounterRequestMessages)
		{
			System.out.println(msg.toString());
			CounterRequestMessages crm=(CounterRequestMessages)msg;
			int count=countVowels(crm.getFilename(),crm.getStartAt(),crm.getEndAt());
		//	System.out.println("Vowels: "+count);
			getSender().tell(new CounterResponseMessages(crm.getFilename(),count),getSelf());
		}

	}
	private int countVowels(String path,int startAt,int endAt) throws IOException{
		FileReader fileReader=new FileReader(path);
		BufferedReader bf=new BufferedReader(fileReader);
		
		String aLine;
		int numberOfVowels=0;
		aLine =bf.readLine();
		int lineNumber=1;
		while(aLine!=null)
		{
			 if(lineNumber>=startAt&&lineNumber<=endAt){
			int countLine=aLine.replaceAll("[^aeiouyAEIOUY]","").length();
			numberOfVowels+=countLine;
			
			
			}
			 aLine=bf.readLine();
			 lineNumber++;
		}
		
		return numberOfVowels;
	}

}
