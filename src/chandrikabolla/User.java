package chandrikabolla;

import java.util.concurrent.TimeUnit;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.util.Timeout;
import scala.concurrent.Await;
import scala.concurrent.Awaitable;
import scala.concurrent.impl.Promise;


/**
 * Main class for your estimation actor system.
 *
 * @author chand
 *
 */
public class User {
public static ActorRef wordCounterNode;
static final Timeout timeout=new Timeout(1000000,TimeUnit.SECONDS);
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		ActorSystem system = ActorSystem.create("EstimationSystem");

		/*
		 * Create the Estimator Actor and send it the StartProcessingFolder
		 * message. Once you get back the response, use it to print the result.
		 * Remember, there is only one actor directly under the ActorSystem.
		 * Also, do not forget to shutdown the actorsystem
		 */
		
		//Create the Estimator(WordCounter) Actor and send it the StartProcessingFolder message.
         wordCounterNode= system.actorOf(WordCountActor.props, "wordCounter_Node");
        wordCounterNode.tell("StartProcessingMessage", null);
  	//Promise<Object> future=  (Promise<Object>) Patterns.ask(estimatorNode,"ready?",1000000);
   // 		String result= (String) Await.result((Awaitable<Object>) future,timeout.duration());
   //   System.out.println(future.get());
	}
	
	

}
