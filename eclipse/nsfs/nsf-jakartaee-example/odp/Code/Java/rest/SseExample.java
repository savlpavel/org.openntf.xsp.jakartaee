package rest;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import bean.SseChatBean;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.concurrent.ManagedExecutorService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.validation.constraints.NotEmpty;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.sse.Sse;
import jakarta.ws.rs.sse.SseBroadcaster;
import jakarta.ws.rs.sse.SseEventSink;

@Path("sseChat")
@ApplicationScoped
public class SseExample {
	@Inject @Named("java:comp/DefaultManagedExecutorService")
	private ManagedExecutorService executor;
	
	@Inject
	private SseChatBean chatBean;
	
	private Sse sse;
	private SseBroadcaster sseBroadcaster;
	
	@PostConstruct
	public void init() {
		executor.submit(() -> {
			BlockingQueue<String> queue = chatBean.listen();
			try {
				String message;
				while((message = queue.take()) != null) {
					String id = UUID.randomUUID().toString();
					JsonObject payload = Json.createObjectBuilder()
						.add("message", message)
						.build();
					
					if(this.sseBroadcaster != null) {
						this.sseBroadcaster.broadcast(
							sse.newEventBuilder()
								.name("message")
								.id(id)
								.mediaType(MediaType.APPLICATION_JSON_TYPE)
								.data(String.class, payload.toString())
								.reconnectDelay(250)
								.build()
						);
					}
				}
			} catch (InterruptedException e) {
				// Good
			} finally {
				chatBean.unregister(queue);
			}
		});
	}
	
	@PreDestroy
	public void term() {
		if(this.sseBroadcaster != null) {
			this.sseBroadcaster.close();
		}
	}
	
	@Context
	public void setSse(Sse sse) {
		this.sse = sse;
		this.sseBroadcaster = sse.newBroadcaster();
	}
	
	@GET
	@Produces(MediaType.SERVER_SENT_EVENTS)
	public void get(@Context SseEventSink sseEventSink) throws InterruptedException, ExecutionException {
		executor.submit(() -> {
			this.sseBroadcaster.register(sseEventSink);
			try {
				while(true) {
					TimeUnit.MILLISECONDS.sleep(250);
				}
			}catch(InterruptedException e) {
				// Closing
			}
			sseEventSink.close();
		});
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void sendMessage(@FormParam("message") @NotEmpty String message) {
		chatBean.publish(message);
	}
}