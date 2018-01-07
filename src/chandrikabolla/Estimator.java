package chandrikabolla;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

import akka.actor.ActorContext;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.util.Timeout;
import chandrikabolla.Messages.CounterRequestMessages;
import chandrikabolla.Messages.CounterResponseMessages;
import chandrikabolla.Messages.ReadyMessages;
import chandrikabolla.Messages.ResultMessages;

public class Estimator extends UntypedActor{
public static Props props=Props.create(Estimator.class);
private int p1=1;
private double p2=0.8;
private double p3=1.2;
private HashMap<String,Integer> vowelsInFiles;
private HashMap<String,Integer> vowelsExpected;

public ActorRef firstCounterActor;
public ActorRef secondCounterActor;

public ActorRef user;
	public Estimator(){
		vowelsInFiles=new HashMap<String,Integer>();
		vowelsExpected=new HashMap<String,Integer>();

	}
	@Override
	public void onReceive(Object msg) throws Throwable {
	
		if(msg instanceof String)
		{
			System.out.println(getSelf().path().name()+" received: "+msg);
			if(msg.equals("ready?"))
			{
				if(vowelsExpected.size()==10)
				{
					getSender().tell("yes done",getSelf());
				}
			}
			if(msg.equals(ReadyMessages.readyReq)){

									getSender().tell(ReadyMessages.readyResp,getSelf());
//								
			}
	    }
		
		else if(msg instanceof CounterRequestMessages)
		{
			System.out.println(msg.toString());
			CounterRequestMessages crm=(CounterRequestMessages)msg;
			int count=countVowels(crm.getFilename(),crm.getStartAt(),crm.getEndAt());
			count*=p1;
			count*=2;
			vowelsExpected.put(crm.getFilename(),count);
		}
		
		else if(msg instanceof CounterResponseMessages)
		{
			CounterResponseMessages crm=(CounterResponseMessages) msg;
			if(!vowelsInFiles.containsKey(crm.getFilename()))
			{
				vowelsInFiles.put(crm.getFilename(),crm.getNumberOfVowels());
			}
			else{
				int fluctuation;
				int totalVowels=crm.getNumberOfVowels()+vowelsInFiles.get(crm.getFilename());
				if(vowelsExpected.get(crm.getFilename())<totalVowels)
				{
					p1*=p3;
					fluctuation=-(totalVowels-vowelsExpected.get(crm.getFilename()));
				}
				else if(vowelsExpected.get(crm.getFilename())>totalVowels){
					p1*=p2;
					fluctuation=(totalVowels-vowelsExpected.get(crm.getFilename()));
				}
				else{
					fluctuation=0;
				}
				getSender().tell(new ResultMessages(fluctuation,crm.getFilename()),getSelf());
			}
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
	
	public int readLines(String path) throws IOException
	{
		FileReader fileReader=new FileReader(path);
		BufferedReader bf=new BufferedReader(fileReader);
		
		String aLine;
		int numberOfLines=0;
		
		while((aLine =bf.readLine())!=null)
		{
			numberOfLines++;
		}
		
		return numberOfLines;
	}
}
