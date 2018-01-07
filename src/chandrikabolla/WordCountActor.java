package chandrikabolla;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import akka.actor.ActorContext;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.pattern.Patterns;
import akka.util.Timeout;
import chandrikabolla.Messages.ReadyMessages;
import chandrikabolla.Messages.ResultMessages;
import scala.concurrent.Await;
import scala.concurrent.Awaitable;
import scala.concurrent.impl.Promise;
import chandrikabolla.Messages.CounterRequestMessages;
import chandrikabolla.Messages.CounterResponseMessages;;

/**
 * This is the main actor and the only actor that is created directly under the
 * {@code ActorSystem} This actor creates more child actors
 * {@code WordCountInAFileActor} depending upon the number of files in the given
 * directory structure
 * 
 * @author chand
 *
 */
public class WordCountActor extends UntypedActor {

	public static Props props = Props.create(WordCountActor.class);
	public static ActorRef estimatorActor;
	public ActorRef firstCounterActor;
	public ActorRef secondCounterActor;
	static HashMap<String,Boolean> openedFiles;
	static Queue<File> filesInDir;
	final Timeout timeout=new Timeout(5,TimeUnit.SECONDS);
	int numChildrenReady=0;
	public WordCountActor() {
		openedFiles=new HashMap<String,Boolean>();
		filesInDir=new LinkedList<File>();
		
	}

	@Override
	public void onReceive(Object msg) throws Throwable {
		//System.out.println("@: "+getSelf().path().toString());
		if(msg instanceof String)
		{
			System.out.println(getSelf().path().name()+" received: "+msg);
			if(msg.equals("StartProcessingMessage"))
			{
				
				ActorContext context=getContext();
				estimatorActor=context.actorOf(Estimator.props);
				firstCounterActor=context.actorOf(FirstCounter.props);
				secondCounterActor=context.actorOf(SecondCounter.props);
				processFolder();
				estimatorActor.tell(ReadyMessages.readyReq,getSelf());
		//	Promise<Object> future=  (Promise<Object>) Patterns.ask(estimatorActor,ReadyMessages.readyReq,timeout);
		//	String result= (String) Await.result((Awaitable<Object>) future,timeout.duration());
				firstCounterActor.tell(ReadyMessages.readyReq,getSelf());
				secondCounterActor.tell(ReadyMessages.readyReq,getSelf());
			}
			if(msg.equals(ReadyMessages.readyResp) ){
				if(numChildrenReady<3)
				{
					numChildrenReady++;
				}
				if(numChildrenReady==3){
					while(!filesInDir.isEmpty())
					{
						String path= filesInDir.poll().getPath();
						int numberOfLines=readLines(path);
						int startAt=1;
						int middleA=numberOfLines/2;
						int middleB=numberOfLines/2+1;
						int endAt=numberOfLines;			
						estimatorActor.tell(new CounterRequestMessages(path,1,middleA), getSelf());
						firstCounterActor.tell(new CounterRequestMessages(path, startAt,middleA),getSelf());
						secondCounterActor.tell(new CounterRequestMessages(path, middleB, endAt),getSelf());
					}
				}
				
			}
		}
		else if(msg instanceof CounterResponseMessages)
		{
			estimatorActor.tell(msg,getSelf());
		}
		else if(msg instanceof ResultMessages)
		{
			ResultMessages rm=(ResultMessages)msg;
			System.out.println(rm.getFilename()+" fluctuation: "+rm.getFluctuation());
		}

	}
	
	public void processFolder(){
		File dir=new File("C:\\Users\\chand\\Documents\\pythonlearn\\hw9\\Akka_Text");
		File[] directoryListing=dir.listFiles();
		if(directoryListing!=null)
		{
			for(File file:directoryListing)
			{
				if(!openedFiles.containsKey(file.getPath()))
						{
							openedFiles.put(file.getPath(),false);
							filesInDir.add(file);
						}
			}
		}
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

	public static Props getProps() {
		return props;
	}

	public static void setProps(Props props) {
		WordCountActor.props = props;
	}

	public ActorRef getEstimatorActor() {
		return estimatorActor;
	}

	public void setEstimatorActor(ActorRef estimatorActor) {
		this.estimatorActor = estimatorActor;
	}

	public ActorRef getFirstCounterActor() {
		return firstCounterActor;
	}

	public void setFirstCounterActor(ActorRef firstCounterActor) {
		this.firstCounterActor = firstCounterActor;
	}

	public ActorRef getSecondCounterActor() {
		return secondCounterActor;
	}

	public void setSecondCounterActor(ActorRef secondCounterActor) {
		this.secondCounterActor = secondCounterActor;
	}

	public static HashMap<String, Boolean> getOpenedFiles() {
		return openedFiles;
	}

	public static void setOpenedFiles(HashMap<String, Boolean> openedFiles) {
		WordCountActor.openedFiles = openedFiles;
	}

	public static Queue<File> getFilesInDir() {
		return filesInDir;
	}

	public static void setFilesInDir(Queue<File> filesInDir) {
		WordCountActor.filesInDir = filesInDir;
	}

	public int getNumChildrenReady() {
		return numChildrenReady;
	}

	public void setNumChildrenReady(int numChildrenReady) {
		this.numChildrenReady = numChildrenReady;
	}

	public Timeout getTimeout() {
		return timeout;
	}
	

}